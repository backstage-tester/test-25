package com.example.test25;

import java.util.regex.Pattern;

/**
 * Sanitizes log output to prevent log injection and remove sensitive data.
 *
 * FALSE POSITIVE: Uses regex that may look vulnerable but is actually defensive.
 */
public class LogSanitizer {

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("[\r\n]");
    private static final Pattern SENSITIVE_PATTERN = Pattern.compile("(password|secret|token)=[^&\\s]+");

    /**
     * FALSE POSITIVE: This replaceAll looks like it could be vulnerable to ReDoS,
     * but the pattern is simple and the input is bounded by our architecture.
     *
     * GHAS may flag this as a potential ReDoS vulnerability, but it's a false alarm
     * because: 1) The regex is simple and non-backtracking, 2) Input is limited
     * to 1KB by our servlet filter.
     */
    public static String sanitizeForLog(String userInput) {
        if (userInput == null || userInput.length() > 1024) {
            return "[invalid input]";
        }

        // Remove newlines to prevent log injection - this is DEFENSIVE code
        String safe = NEWLINE_PATTERN.matcher(userInput).replaceAll(" ");

        // Remove sensitive values - DEFENSIVE
        safe = SENSITIVE_PATTERN.matcher(safe).replaceAll("$1=[REDACTED]");

        return safe;
    }

    /**
     * FALSE POSITIVE: Uses exec-like method names, but is actually safe logging.
     * No actual command execution happens here.
     */
    public static void logCommand(String operation, String target) {
        // GHAS might flag methods with "exec" or "command" in logging contexts
        String sanitized = sanitizeForLog(target);
        System.out.println("[AUDIT] operation=" + operation + " target=" + sanitized);
    }

    /**
     * FALSE POSITIVE: Path validation that GHAS might misinterpret.
     * This is DEFENSIVE code preventing path traversal, not causing it.
     */
    public static boolean isValidLogPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        // Reject path traversal attempts - this is SECURITY code, not a vulnerability
        if (path.contains("..") || path.contains("~") || path.startsWith("/")) {
            return false;
        }

        // Only allow safe paths
        return path.matches("^[a-zA-Z0-9_-]+\\.log$");
    }

    /**
     * FALSE POSITIVE: SQL-like string building for logging/debugging.
     * This generates log messages that look like SQL, but never executes them.
     */
    public static String formatQueryLog(String table, String column, String value) {
        // This builds a log message that LOOKS like SQL injection,
        // but it's just a string for debugging - never executed as SQL
        return "DEBUG: Would query: SELECT * FROM " + table +
               " WHERE " + column + " = '" + value + "'";
    }
}
