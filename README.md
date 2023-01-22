# User Service
Microservice that contains all aspects of the core domain "User".

## Local startup
To start application run.
```
./mvnw -pl user-service-application spring-boot:run
```
The service will be accessible on http://localhost:8080

## Testing
### Unit Tests
All unit tests are bound to "test" goal. Just run:
```
./mvnw test
```
# Integration Tests
All integration tests are bound to "integration-test" goal. Just run:
```
./mvnw integration-test
```

### Go to the browser
```
http://localhost:8080
```

### Cancel port-forwarding with

```
CTRL+C
```
