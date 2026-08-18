package com.example.test25;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Streams generated reports back to the caller and can regenerate them on demand.
 */
public class ReportServlet extends HttpServlet {

    private static final String REPORT_DIR = "/var/app/reports/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String report = request.getParameter("report");
        File target = new File(REPORT_DIR + report);

        if (!target.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/octet-stream");
        try (InputStream in = Files.newInputStream(target.toPath());
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dataset = request.getParameter("dataset");
        String format = request.getParameter("format");

        String command = "/usr/local/bin/generate-report --dataset " + dataset + " --format " + format;
        Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });

        try {
            int exitCode = process.waitFor();
            response.getWriter().println("generator exited with " + exitCode);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("report generation interrupted", e);
        }
    }
}
