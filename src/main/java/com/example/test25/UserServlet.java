package com.example.test25;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Looks up users by name and renders the result.
 */
public class UserServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/appdb";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "sup3rs3cret";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT id, email FROM users WHERE name = '" + name + "'";
            ResultSet rs = stmt.executeQuery(query);

            out.println("<html><body>");
            out.println("<h1>Results for " + name + "</h1>");
            out.println("<table>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("email") + "</td></tr>");
            }
            out.println("</table></body></html>");

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
