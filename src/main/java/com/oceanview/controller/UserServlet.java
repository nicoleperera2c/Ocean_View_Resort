package com.oceanview.controller;

import com.oceanview.exception.ServiceException;
import com.oceanview.model.User;
import com.oceanview.service.impl.UserServiceImpl;
import com.oceanview.service.interfaces.IUserService;
import com.oceanview.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/app/users/*")
public class UserServlet extends HttpServlet {

    private IUserService userService;

    @Override
    public void init() throws ServletException {
        // Direct instantiation as per current project pattern instead of full DI
        // container
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            listUsers(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (pathInfo.equals("/add")) {
            createUser(request, response);
        } else if (pathInfo.equals("/edit")) {
            updateUser(request, response);
        } else if (pathInfo.equals("/toggle-status")) {
            toggleUserStatus(request, response);
        } else if (pathInfo.equals("/reset-password")) {
            resetPassword(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/user-list.jsp").forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("errorMsg", "Failed to load users: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "add");
        request.getRequestDispatcher("/WEB-INF/views/user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (ValidationUtil.isNullOrEmpty(idParam)) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=Missing+user+ID");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam);
            User targetUser = userService.getUserById(userId);

            request.setAttribute("targetUser", targetUser);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/WEB-INF/views/user-form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=Invalid+user+ID");
        } catch (ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=" + e.getMessage());
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            User newUser = new User();
            newUser.setUsername(request.getParameter("username"));
            newUser.setFullName(request.getParameter("fullName"));
            newUser.setEmail(request.getParameter("email"));
            newUser.setPhone(request.getParameter("phone"));
            newUser.setRole(User.UserRole.valueOf(request.getParameter("role"))); // ADMIN, MANAGER, STAFF

            String password = request.getParameter("password");

            userService.createUser(newUser, password);
            response.sendRedirect(request.getContextPath() + "/app/users?success=User+created+successfully");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMsg", "Invalid Role selection.");
            showAddForm(request, response);
        } catch (ServiceException e) {
            request.setAttribute("errorMsg", e.getMessage());
            showAddForm(request, response); // Stay on form and show error
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));

            // Re-fetch existing first to preserve username/passwordHash that aren't
            // editable here
            User userToUpdate = userService.getUserById(userId);

            userToUpdate.setFullName(request.getParameter("fullName"));
            userToUpdate.setEmail(request.getParameter("email"));
            userToUpdate.setPhone(request.getParameter("phone"));
            userToUpdate.setRole(User.UserRole.valueOf(request.getParameter("role")));

            userService.updateUser(userToUpdate);
            response.sendRedirect(request.getContextPath() + "/app/users?success=User+updated+successfully");

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=Invalid+form+data");
        } catch (ServiceException e) {
            // Setup generic error redirect (to avoid reshowing form with lost state)
            response.sendRedirect(request.getContextPath() + "/app/users?error=" + e.getMessage());
        }
    }

    private void toggleUserStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int targetUserId = Integer.parseInt(request.getParameter("userId"));

            HttpSession session = request.getSession(false);
            User currentAdmin = (User) session.getAttribute("user");

            userService.toggleUserStatus(targetUserId, currentAdmin.getUserId());

            response.sendRedirect(request.getContextPath() + "/app/users?success=User+status+updated");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=Invalid+user+ID");
        } catch (ServiceException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=" + e.getMessage());
        }
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int targetUserId = Integer.parseInt(request.getParameter("userId"));
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            // Confirm passwords match
            if (newPassword == null || !newPassword.equals(confirmPassword)) {
                response.sendRedirect(request.getContextPath()
                        + "/app/users/edit?id=" + targetUserId + "&error=Passwords+do+not+match");
                return;
            }

            userService.resetPassword(targetUserId, newPassword);
            response.sendRedirect(request.getContextPath() + "/app/users?success=Password+reset+successfully");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/app/users?error=Invalid+user+ID");
        } catch (ServiceException e) {
            // Try to redirect back to the edit form with the error
            String userIdParam = request.getParameter("userId");
            if (userIdParam != null) {
                response.sendRedirect(request.getContextPath()
                        + "/app/users/edit?id=" + userIdParam + "&error=" + e.getMessage());
            } else {
                response.sendRedirect(request.getContextPath() + "/app/users?error=" + e.getMessage());
            }
        }
    }
}
