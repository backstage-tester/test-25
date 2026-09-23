package com.example.test25;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates test data for integration tests.
 *
 * FALSE POSITIVE: Contains what look like secrets, but they're test data.
 * These are example/placeholder values used only in test environments.
 */
public class TestDataGenerator {

    /**
     * FALSE POSITIVE: GHAS will flag these as hardcoded secrets.
     * These are example values for testing - not real credentials.
     */
    public static Map<String, String> getTestCredentials() {
        Map<String, String> testCreds = new HashMap<>();

        // These are EXAMPLE credentials for local testing only
        // Real production credentials are in AWS Secrets Manager
        testCreds.put("test_api_key", "sk_test_51234567890abcdefghijklmnop");
        testCreds.put("test_secret", "whsec_1234567890abcdefghijklmnopqrstuvwxyz");
        testCreds.put("demo_token", "ghp_1234567890abcdefghijklmnopqrstuv");

        return testCreds;
    }

    /**
     * FALSE POSITIVE: Example AWS keys for documentation.
     * These follow the AWS key format but are NOT valid keys.
     */
    public static String getExampleAwsConfig() {
        return """
            # Example AWS configuration (NOT REAL CREDENTIALS)
            # Replace with actual credentials from your AWS account
            aws_access_key_id = AKIAIOSFODNN7EXAMPLE
            aws_secret_access_key = wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

            # These are the official AWS documentation examples
            # See: https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html
            """;
    }

    /**
     * FALSE POSITIVE: Placeholder tokens in test configuration.
     */
    public static String getTestConfig() {
        return """
            {
              "environment": "test",
              "api_endpoint": "http://localhost:8080",
              "api_key": "test_key_12345_not_a_real_key",
              "webhook_secret": "test_secret_abcde_placeholder",
              "database": {
                "host": "localhost",
                "user": "testuser",
                "password": "testpass123"
              },
              "note": "This is test data. Production uses environment variables."
            }
            """;
    }

    /**
     * FALSE POSITIVE: Example JWT token structure (not a real token).
     */
    public static String getExampleJwt() {
        // This is a DECODED example showing JWT structure - not an actual valid token
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    }

    /**
     * FALSE POSITIVE: Private key format example (not a real key).
     */
    public static String getExamplePrivateKey() {
        return """
            -----BEGIN PRIVATE KEY-----
            MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEA1234567890EXAMPLE
            ThisIsNotARealPrivateKeyJustAnExampleForTestingPurposesOnly
            -----END PRIVATE KEY-----

            NOTE: This is an EXAMPLE format only. Never commit real private keys.
            """;
    }
}
