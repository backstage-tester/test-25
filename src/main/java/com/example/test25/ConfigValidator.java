package com.example.test25;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Validates configuration by running hardcoded queries.
 * These queries use NO user input - all values are compile-time constants.
 */
public class ConfigValidator {

    /**
     * FALSE POSITIVE: This looks like SQL injection but uses only hardcoded values.
     * GHAS will flag this, but it's safe because tableList is a constant array.
     */
    public boolean validateDatabaseSchema(Connection conn) throws SQLException {
        String[] tableList = {"users", "sessions", "audit_log", "config"};

        try (Statement stmt = conn.createStatement()) {
            for (String tableName : tableList) {
                // This string concatenation is safe - tableName comes from our hardcoded array
                String query = "SELECT COUNT(*) FROM " + tableName;
                ResultSet rs = stmt.executeQuery(query);
                if (!rs.next() || rs.getInt(1) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * FALSE POSITIVE: Command execution with hardcoded values only.
     * No user input is involved, but GHAS will flag Runtime.exec().
     */
    public String getSystemInfo() throws IOException, InterruptedException {
        // All commands are hardcoded - no user input
        String[] commands = {
            "uptime",
            "df -h /var",
            "free -m"
        };

        StringBuilder result = new StringBuilder();
        for (String cmd : commands) {
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            result.append(cmd).append(": exit ").append(proc.exitValue()).append("\n");
        }
        return result.toString();
    }

    /**
     * FALSE POSITIVE: File path from constants only.
     * GHAS may flag this as path traversal, but paths are hardcoded.
     */
    public boolean checkConfigFiles() {
        String[] configPaths = {
            "/etc/app/database.conf",
            "/etc/app/logging.conf",
            "/etc/app/security.conf"
        };

        for (String path : configPaths) {
            // Safe: path comes from our constant array
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        }
        return true;
    }

    /**
     * FALSE POSITIVE: SQL query built from environment variable.
     * This is safe in controlled deployment where DB_SCHEMA is set by infrastructure,
     * but GHAS can't distinguish this from user input.
     */
    public int getRecordCount(Connection conn, String recordType) throws SQLException {
        // Get schema from environment (NOT from user)
        String schema = System.getenv("DB_SCHEMA");
        if (schema == null) {
            schema = "production";
        }

        // recordType is validated against whitelist
        if (!isValidRecordType(recordType)) {
            throw new IllegalArgumentException("Invalid record type");
        }

        // GHAS flags this, but it's safe due to whitelist validation
        String query = "SELECT COUNT(*) FROM " + schema + "." + recordType;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private boolean isValidRecordType(String type) {
        return type.matches("^[a-z_]+$") &&
               (type.equals("users") || type.equals("sessions") || type.equals("logs"));
    }
}
