package com.example.test25;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Fetches upstream resources on behalf of the browser and handles post-login redirects.
 */
public class ProxyServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ProxyServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String target = request.getParameter("url");
        String next = request.getParameter("next");

        LOG.info("proxy request from " + request.getRemoteAddr() + " for " + target);

        if (next != null) {
            response.sendRedirect(next);
            return;
        }

        if (target == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing url");
            return;
        }

        URL upstream = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) upstream.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        response.setStatus(conn.getResponseCode());
        response.setContentType(conn.getContentType());

        try (InputStream in = conn.getInputStream();
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        } finally {
            conn.disconnect();
        }
    }
}
