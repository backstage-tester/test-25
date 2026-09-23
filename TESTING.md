# Testing Guide

This document contains test credentials and example configurations for local development.

## Test Environment Setup

### Database Credentials (Local Test DB Only)

```properties
# These credentials are for the local Docker test database
# NOT for production! Production uses AWS RDS with IAM auth
DB_HOST=localhost
DB_PORT=3306
DB_USER=testuser
DB_PASSWORD=testpass123
DB_NAME=test_db
```

### API Keys for Testing

The following API keys are examples for testing integrations:

```bash
# Stripe test key (publicly documented example)
export STRIPE_TEST_KEY="sk_test_4eC39HqLyjWDarjtT1zdp7dc"

# GitHub test token (revoked example token)
export GITHUB_TOKEN="ghp_1234567890abcdefghijklmnopqrstuv"

# Slack webhook example (from their docs)
export SLACK_WEBHOOK="https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX"
```

### Docker Compose Test Secrets

```yaml
# docker-compose.test.yml uses these hardcoded values
environment:
  - API_KEY=test_key_not_for_production
  - SECRET_KEY=test_secret_12345
  - ENCRYPTION_KEY=00112233445566778899aabbccddeeff
```

## Example JWT Tokens

For testing JWT validation:

```
# This is a test token with the payload: {"sub": "test", "exp": 9999999999}
# Signed with the secret "test_secret" (not used in production)
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjo5OTk5OTk5OTk5fQ.SomeExampleSignatureHere123456789
```

## SSH Keys for Test Containers

Example SSH key format (not a real key):

```
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAaAAAABN
EXAMPLE_NOT_A_REAL_KEY_DO_NOT_USE_IN_PRODUCTION
-----END OPENSSH PRIVATE KEY-----
```

## AWS Credentials for LocalStack

When testing with LocalStack (local AWS emulator):

```ini
[default]
aws_access_key_id = test
aws_secret_access_key = test
region = us-east-1

# Or use the official AWS documentation example:
aws_access_key_id = AKIAIOSFODNN7EXAMPLE
aws_secret_access_key = wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
```

## Notes

- **All credentials on this page are for local testing only**
- Production credentials are stored in AWS Secrets Manager
- Never commit real production secrets to this repository
- These examples are either:
  - Publicly documented examples from official docs
  - Revoked/invalid tokens
  - Test-mode only keys
  - Credentials for local development containers

## Running Tests

```bash
# Use test environment
export ENV=test
export USE_TEST_CREDENTIALS=true

# Run integration tests
mvn test -Dtest=IntegrationTest
```
