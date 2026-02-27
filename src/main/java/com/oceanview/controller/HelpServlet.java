package com.oceanview.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HelpServlet - displays the help/guidelines page
 * Fulfils the "Help Section" requirement from the assignment
 */
@WebServlet(name = "HelpServlet", urlPatterns = { "/app/help" })
public class HelpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/help.jsp").forward(request, response);
    }
}
