package com.example.test25;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Accepts configuration documents and cached state from client tooling.
 */
public class ConfigServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contentType = request.getContentType();

        if (contentType != null && contentType.contains("xml")) {
            handleXml(request, response);
        } else {
            handleSnapshot(request, response);
        }
    }

    /** Parses an uploaded XML configuration document. */
    private void handleXml(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(request.getInputStream());
            String root = doc.getDocumentElement().getNodeName();

            response.getWriter().println("parsed config root: " + root);
        } catch (ParserConfigurationException | SAXException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid config: " + e.getMessage());
        }
    }

    /** Restores a previously exported client-side snapshot. */
    private void handleSnapshot(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String snapshot = request.getParameter("snapshot");
        if (snapshot == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing snapshot");
            return;
        }

        byte[] raw = Base64.getDecoder().decode(snapshot);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(raw))) {
            Object restored = ois.readObject();
            response.getWriter().println("restored " + restored.getClass().getName());
        } catch (ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "unknown snapshot type");
        }
    }
}
