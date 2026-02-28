package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.service.impl.RoomServiceImpl;
import com.oceanview.service.interfaces.IRoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * RoomServlet - Controller for Room Management (MVC pattern).
 *
 * URL mappings:
 * GET /app/rooms → list all rooms
 * GET /app/rooms/add → show add form
 * GET /app/rooms/edit?id=X → show edit form
 * POST /app/rooms action=add → create room
 * POST /app/rooms action=update → update room
 * POST /app/rooms action=delete → delete room
 */
@WebServlet(name = "RoomServlet", urlPatterns = { "/app/rooms", "/app/rooms/*" })
public class RoomServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IRoomService roomService;

    @Override
    public void init() throws ServletException {
        roomService = new RoomServiceImpl();
    }

    // -------------------------------------------------------------------------
    // GET — list rooms, show add form, show edit form
    // -------------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            listRooms(request, response);
        } else if ("/add".equals(pathInfo)) {
            showAddForm(request, response);
        } else if ("/edit".equals(pathInfo)) {
            showEditForm(request, response);
        } else {
            listRooms(request, response);
        }
    }

    // -------------------------------------------------------------------------
    // POST — add, update, delete
    // -------------------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            handleAdd(request, response);
        } else if ("update".equals(action)) {
            handleUpdate(request, response);
        } else if ("delete".equals(action)) {
            handleDelete(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/app/rooms");
        }
    }

    // -------------------------------------------------------------------------
    // List all rooms
    // -------------------------------------------------------------------------

    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Room> rooms = roomService.getAllRooms();
            request.setAttribute("rooms", rooms);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/room-list.jsp").forward(request, response);
    }

    // -------------------------------------------------------------------------
    // Show add form
    // -------------------------------------------------------------------------

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RoomType> roomTypes = roomService.getAllRoomTypes();
            request.setAttribute("roomTypes", roomTypes);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
    }

    // -------------------------------------------------------------------------
    // Show edit form (pre-populated)
    // -------------------------------------------------------------------------

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                throw new ServiceException("Room ID is required.");
            }
            int roomId = Integer.parseInt(idStr.trim());

            Room room = roomService.getRoomById(roomId);
            request.setAttribute("room", room);

            List<RoomType> roomTypes = roomService.getAllRoomTypes();
            request.setAttribute("roomTypes", roomTypes);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid room ID format.");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/views/room-form.jsp").forward(request, response);
    }

    // -------------------------------------------------------------------------
    // Handle Add
    // -------------------------------------------------------------------------

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Room room = parseRoomFromRequest(request);
            roomService.addRoom(room);

            response.sendRedirect(request.getContextPath() + "/app/rooms?success=Room+added+successfully");
            return;

        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            showAddForm(request, response);
        }
    }

    // -------------------------------------------------------------------------
    // Handle Update
    // -------------------------------------------------------------------------

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Room room = parseRoomFromRequest(request);

            String idStr = request.getParameter("roomId");
            if (idStr == null || idStr.trim().isEmpty()) {
                throw new ServiceException("Room ID is required for update.");
            }
            room.setRoomId(Integer.parseInt(idStr.trim()));

            roomService.updateRoom(room);

            response.sendRedirect(request.getContextPath() + "/app/rooms?success=Room+updated+successfully");
            return;

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid room ID format.");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        showEditForm(request, response);
    }

    // -------------------------------------------------------------------------
    // Handle Delete
    // -------------------------------------------------------------------------

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String idStr = request.getParameter("roomId");
            if (idStr == null || idStr.trim().isEmpty()) {
                throw new ServiceException("Room ID is required for deletion.");
            }
            int roomId = Integer.parseInt(idStr.trim());

            roomService.deleteRoom(roomId);

            response.sendRedirect(request.getContextPath() + "/app/rooms?success=Room+deleted+successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/app/rooms?error=Invalid+room+ID");
        } catch (ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/app/rooms?error=" +
                    java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    // -------------------------------------------------------------------------
    // Shared Helper: parse Room from HTTP request (DRY — used by add & update)
    // -------------------------------------------------------------------------

    private Room parseRoomFromRequest(HttpServletRequest request) throws ServiceException {
        String roomNumber = request.getParameter("roomNumber");
        String roomTypeIdStr = request.getParameter("roomTypeId");
        String floorStr = request.getParameter("floorNumber");
        String statusStr = request.getParameter("status");

        Room room = new Room();

        room.setRoomNumber(roomNumber != null ? roomNumber.trim() : "");

        try {
            room.setRoomTypeId(Integer.parseInt(roomTypeIdStr));
        } catch (NumberFormatException e) {
            throw new ServiceException("Please select a valid room type.");
        }

        try {
            room.setFloorNumber(Integer.parseInt(floorStr));
        } catch (NumberFormatException e) {
            throw new ServiceException("Please enter a valid floor number.");
        }

        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                room.setStatus(Room.RoomStatus.valueOf(statusStr.trim()));
            } catch (IllegalArgumentException e) {
                throw new ServiceException("Invalid room status selected.");
            }
        } else {
            room.setStatus(Room.RoomStatus.AVAILABLE);
        }

        return room;
    }
}
