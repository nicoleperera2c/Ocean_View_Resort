package com.oceanview.service.impl;

import com.oceanview.dao.DAOFactory;
import com.oceanview.dao.interfaces.IReservationDAO;
import com.oceanview.dao.interfaces.IRoomDAO;
import com.oceanview.exception.DAOException;
import com.oceanview.exception.ServiceException;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.interfaces.IReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Report Service Implementation
 * Generates dashboard statistics, occupancy, revenue, and reservation reports
 */
public class ReportServiceImpl implements IReportService {

    private IReservationDAO reservationDAO;
    private IRoomDAO roomDAO;

    public ReportServiceImpl() {
        DAOFactory factory = DAOFactory.getInstance();
        this.reservationDAO = factory.getReservationDAO();
        this.roomDAO = factory.getRoomDAO();
    }

    @Override
    public Map<String, Object> getDashboardStatistics() throws ServiceException {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Total rooms and occupancy
            List<Room> allRooms = roomDAO.findAll();
            int totalRooms = allRooms.size();
            int occupiedRooms = 0;
            for (Room room : allRooms) {
                if (room.getStatus() == Room.RoomStatus.OCCUPIED) {
                    occupiedRooms++;
                }
            }

            // Occupancy rate
            double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100.0 : 0.0;

            // Active reservations
            List<Reservation> activeReservations = reservationDAO.findActiveReservations();

            // Today's check-ins and check-outs
            List<Reservation> todayCheckIns = reservationDAO.findCheckInsForDate(LocalDate.now());
            List<Reservation> todayCheckOuts = reservationDAO.findCheckOutsForDate(LocalDate.now());

            // Total revenue from completed payments
            BigDecimal totalRevenue = calculateTotalRevenue();

            stats.put("totalRooms", totalRooms);
            stats.put("occupiedRooms", occupiedRooms);
            stats.put("availableRooms", totalRooms - occupiedRooms);
            stats.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0);
            stats.put("activeReservations", activeReservations.size());
            stats.put("todayCheckIns", todayCheckIns.size());
            stats.put("todayCheckOuts", todayCheckOuts.size());
            stats.put("totalRevenue", totalRevenue);

            return stats;

        } catch (DAOException e) {
            throw new ServiceException("Error generating dashboard statistics: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getOccupancyReport(LocalDate date) throws ServiceException {
        if (date == null) {
            date = LocalDate.now();
        }

        Map<String, Object> report = new HashMap<>();

        try {
            List<Room> allRooms = roomDAO.findAll();
            int totalRooms = allRooms.size();
            int occupiedRooms = roomDAO.countRoomsByStatus(Room.RoomStatus.OCCUPIED);
            int maintenanceRooms = roomDAO.countRoomsByStatus(Room.RoomStatus.MAINTENANCE);
            int availableRooms = totalRooms - occupiedRooms - maintenanceRooms;

            double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100.0 : 0.0;

            report.put("date", date);
            report.put("totalRooms", totalRooms);
            report.put("occupiedRooms", occupiedRooms);
            report.put("availableRooms", availableRooms);
            report.put("maintenanceRooms", maintenanceRooms);
            report.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0);

            return report;

        } catch (DAOException e) {
            throw new ServiceException("Error generating occupancy report: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getReservationReport(LocalDate startDate, LocalDate endDate) throws ServiceException {
        if (startDate == null || endDate == null) {
            throw new ServiceException("Start and end dates are required for reservation report");
        }

        Map<String, Object> report = new HashMap<>();

        try {
            List<Reservation> reservations = reservationDAO.findByDateRange(startDate, endDate);

            int totalReservations = reservations.size();
            int confirmedCount = 0;
            int checkedInCount = 0;
            int checkedOutCount = 0;
            int cancelledCount = 0;

            for (Reservation reservation : reservations) {
                switch (reservation.getStatus()) {
                    case CONFIRMED:
                        confirmedCount++;
                        break;
                    case CHECKED_IN:
                        checkedInCount++;
                        break;
                    case CHECKED_OUT:
                        checkedOutCount++;
                        break;
                    case CANCELLED:
                        cancelledCount++;
                        break;
                    default:
                        break;
                }
            }

            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("totalReservations", totalReservations);
            report.put("confirmed", confirmedCount);
            report.put("checkedIn", checkedInCount);
            report.put("checkedOut", checkedOutCount);
            report.put("cancelled", cancelledCount);
            report.put("reservations", reservations);

            return report;

        } catch (DAOException e) {
            throw new ServiceException("Error generating reservation report: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getRevenueReport(LocalDate startDate, LocalDate endDate) throws ServiceException {
        if (startDate == null || endDate == null) {
            throw new ServiceException("Start and end dates are required for revenue report");
        }

        Map<String, Object> report = new HashMap<>();

        try {
            List<Reservation> reservations = reservationDAO.findByDateRange(startDate, endDate);

            BigDecimal totalRevenue = BigDecimal.ZERO;
            int completedCount = 0;

            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == Reservation.ReservationStatus.CHECKED_OUT) {
                    if (reservation.getTotalAmount() != null) {
                        totalRevenue = totalRevenue.add(reservation.getTotalAmount());
                    }
                    completedCount++;
                }
            }

            BigDecimal averageRevenue = completedCount > 0
                    ? totalRevenue.divide(BigDecimal.valueOf(completedCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("totalRevenue", totalRevenue);
            report.put("completedReservations", completedCount);
            report.put("averageRevenue", averageRevenue);

            return report;

        } catch (DAOException e) {
            throw new ServiceException("Error generating revenue report: " + e.getMessage(), e);
        }
    }

    /**
     * Calculate total revenue from all completed reservations
     */
    private BigDecimal calculateTotalRevenue() throws DAOException {
        List<Reservation> allReservations = reservationDAO.findByStatus(
                Reservation.ReservationStatus.CHECKED_OUT);

        BigDecimal total = BigDecimal.ZERO;
        for (Reservation reservation : allReservations) {
            if (reservation.getTotalAmount() != null) {
                total = total.add(reservation.getTotalAmount());
            }
        }
        return total;
    }
}
