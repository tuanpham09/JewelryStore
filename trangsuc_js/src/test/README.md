# API Testing Guide for JewelryStore

This directory contains tests for the JewelryStore API controllers. The tests are written using Spring Boot Test and JUnit 5.

## Test Structure

The tests are organized by controller:

- `AuthControllerTest.java`: Tests for authentication endpoints (register, login, get current user)
- `CartControllerTest.java`: Tests for cart management endpoints (get cart, add item, remove item)
- `OrderControllerTest.java`: Tests for order management endpoints (checkout, get my orders)

## Running Tests

### Using Maven

To run all tests:

```bash
cd trangsuc_js
./mvnw test
```

To run a specific test class:

```bash
./mvnw test -Dtest=AuthControllerTest
```

### Using IDE

You can also run tests directly from your IDE (IntelliJ IDEA, Eclipse, VS Code) by right-clicking on the test class or method and selecting "Run".

## Test Configuration

The tests use:

- `@SpringBootTest`: Loads the full application context
- `@AutoConfigureMockMvc`: Configures MockMvc for testing controllers
- `@MockBean`: Mocks the service layer dependencies
- `@WithMockUser`: Simulates an authenticated user for secured endpoints

## Test Data

Each test class has a `setUp()` method that initializes test data before each test runs. This ensures tests are isolated and don't affect each other.

## Testing Strategy

1. **Unit Tests**: Mock the service layer to isolate controller behavior
2. **Response Validation**: Check HTTP status codes and response body structure
3. **Authentication**: Test both authenticated and unauthenticated access

## Adding New Tests

When adding new tests:

1. Create test data in the `setUp()` method
2. Mock service responses using Mockito
3. Use MockMvc to simulate HTTP requests
4. Validate responses using assertions

## Troubleshooting

If tests are failing, check:

1. Database configuration (tests use H2 in-memory database)
2. Authentication setup (JWT configuration)
3. JSON serialization/deserialization issues
4. Mock service behavior
