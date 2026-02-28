package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Authentication Filter - protects all /app/* URLs
 * Checks for valid user session before allowing access
 * Redirects unauthenticated users to login page
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = { "/app/*" })
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (!isLoggedIn) {
            // User not authenticated - redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- Role-Based Access Control (RBAC) ---
        com.oceanview.model.User user = (com.oceanview.model.User) session.getAttribute("user");
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length()); // e.g., /app/rooms/add

        boolean isAuthorized = checkAuthorization(user, path);

        if (isAuthorized) {
            chain.doFilter(request, response);
        } else {
            // User authenticated but unauthorized for this specific resource
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(request, response);
        }
    }

    /**
     * Scans the requested path against the user's role permissions.
     */
    private boolean checkAuthorization(com.oceanview.model.User user, String path) {
        // Admins have access to everything
        if (user.getRole() == com.oceanview.model.User.UserRole.ADMIN) {
            return true;
        }

        // Manager Permissions
        if (user.getRole() == com.oceanview.model.User.UserRole.MANAGER) {
            // Managers cannot access the User Management module
            if (path.startsWith("/app/users")) {
                return false;
            }
            return true; // Can access everything else (dashboard, reservations, guests, rooms, reports,
                         // etc.)
        }

        // Staff Permissions
        if (user.getRole() == com.oceanview.model.User.UserRole.STAFF) {
            // Staff cannot access Rooms, Reports, or User Management modules
            if (path.startsWith("/app/rooms") ||
                    path.startsWith("/app/reports") ||
                    path.startsWith("/app/users")) {
                return false;
            }
            return true; // Can access dashboard, reservations, guests, payments
        }

        return false; // Safely deny any unknown roles
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
