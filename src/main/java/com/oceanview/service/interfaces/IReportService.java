package com.oceanview.service.interfaces;

import com.oceanview.exception.ServiceException;
import java.time.LocalDate;
import java.util.Map;

public interface IReportService {
    
    Map<String, Object> getOccupancyReport(LocalDate date) throws ServiceException;
    
    Map<String, Object> getReservationReport(LocalDate startDate, LocalDate endDate) throws ServiceException;
    
    Map<String, Object> getRevenueReport(LocalDate startDate, LocalDate endDate) throws ServiceException;
    
    Map<String, Object> getDashboardStatistics() throws ServiceException;
}
