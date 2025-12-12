# User Service

A Spring Boot microservice for managing user information with REST and gRPC APIs. Designed for integration with AWS API Gateway and includes health check endpoints for load balancing and orchestration.

## Project Overview

This service provides comprehensive user information management capabilities with the following features:

- REST API endpoints for creating and retrieving user information
- gRPC service for inter-service communication
- AWS API Gateway integration via HTTP headers
- Health check and actuator endpoints for AWS infrastructure
- PostgreSQL database persistence
- OpenAPI/Swagger documentation
- Comprehensive logging for monitoring

## Technology Stack

- **Language**: Java 25
- **Framework**: Spring Boot 3.5.6
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **gRPC**: Version 1.71.0
- **Protocol Buffers**: Version 3.25.1
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Security**: Spring Security
- **ORM**: Spring Data JPA with Hibernate

## Project Structure

```
src/
├── main/
│   ├── java/FCJ/user/
│   │   ├── UserApplication.java          - Main Spring Boot application entry point
│   │   ├── config/                       - Configuration classes
│   │   │   ├── OpenAPIConfig.java        - OpenAPI/Swagger configuration
│   │   │   └── SecurityConfig.java       - Spring Security configuration
│   │   ├── controller/                   - REST API controllers
│   │   │   ├── HealthController.java     - Health check endpoints
│   │   │   └── UserInfoController.java   - User information endpoints
│   │   ├── dto/                          - Data Transfer Objects
│   │   │   ├── UserInfoDTO.java
│   │   │   ├── UserInfoCreation.java
│   │   │   └── ErrorResponse.java
│   │   ├── service/                      - Business logic layer
│   │   │   ├── UserInfoService.java      - Service interface
│   │   │   └── UserInfoServiceImpl.java   - Service implementation
│   │   ├── grpc/                         - gRPC service implementations
│   │   │   └── UserInfoGrpcService.java
│   │   ├── model/                        - JPA entities
│   │   ├── repository/                   - Data access layer
│   │   └── exception/                    - Custom exceptions
│   ├── proto/
│   │   └── user_info.proto               - Protocol Buffer definitions
│   └── resources/
│       └── application.properties        - Application configuration
└── test/
    └── java/FCJ/user/                    - Unit and integration tests
```

## Getting Started

### Prerequisites

- Java 25 or higher
- Maven 3.9+
- PostgreSQL 12+
- Git

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd user
```

2. Configure the database in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. Build the project:
```bash
mvn clean package
```

4. Run the application:
```bash
mvn spring-boot:run
```

Or using the built JAR:
```bash
java -jar target/user-0.0.1-SNAPSHOT.jar
```

## Server Ports

- **HTTP/REST API**: 8081
- **gRPC Service**: 9090

## Configuration

### Application Properties

Key configuration settings in `application.properties`:

```properties
# Server Configuration
server.port=8081
grpc.server.port=9090

# gRPC Health Check
grpc.server.enable-keep-alive=true
grpc.server.keep-alive-time=30s
grpc.server.keep-alive-timeout=5s

# Actuator Endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus,env
management.endpoint.health.probes.enabled=true
management.health.livenessState.enabled=true
management.health.readinessState.enabled=true

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# OpenAPI/Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.packages-to-scan=FCJ.user.controller

# Logging
logging.level.FCJ.user.grpc.UserInfoGrpcService=INFO
logging.level.io.grpc=DEBUG
```

## API Endpoints

### Health Check Endpoints

#### Ping
```
GET /api/health/ping
Response: 200 OK
{
  "status": "UP",
  "timestamp": "2025-11-28T10:30:00.123456",
  "service": "user-service",
  "version": "1.0.0"
}
```

#### Detailed Health
```
GET /api/health/detailed
Response: 200 OK with detailed service health information
```

#### Actuator Health (AWS API Gateway)
```
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

### User Information Endpoints

#### Create User Info
```
POST /api/user-info
Headers: X-User-Id: <user-uuid>
Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890",
  "address": "123 Main St"
}
Response: 201 Created
```

#### Create Empty User Info
```
POST /api/user-info/empty
Headers: X-User-Id: <user-uuid>
Response: 201 Created with empty user record
```

#### Get User Info by ID
```
GET /api/user-info/{userId}
Response: 200 OK
{
  "userId": "uuid",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890",
  "address": "123 Main St"
}
```

#### Get Current User Info
```
GET /api/user-info/by-user-id
Headers: X-User-Id: <user-uuid>
Response: 200 OK with current user information
```

#### Update User Info
```
PATCH /api/user-info
Headers: X-User-Id: <user-uuid>
Body: Partial user information to update
Response: 200 OK with updated user information
```

#### Update Membership
```
PATCH /api/user-info/membership
Headers: X-User-Id: <user-uuid>
Body: Membership update information
Response: 200 OK
```

#### Check Transaction
```
GET /api/user-info/check-transaction/{momoTransId}
Response: 200 OK with transaction status
```

## gRPC Service

The service exposes gRPC endpoints for inter-service communication on port 9090. Protocol Buffers are defined in `src/main/proto/user_info.proto`.

### gRPC Service Methods

- **BlogUserInfo**: Retrieves user blog information
- **CreateBlogUserInfo**: Creates blog user information
- Additional methods defined in the proto contract

All gRPC calls are logged at INFO level in `UserInfoGrpcService`.

## Database Schema

User information is persisted in PostgreSQL with the following main entities:

- **UserInfo**: Core user information (first name, last name, email, phone, address)
- **UserMetadata**: Additional user metadata and tracking information

Database auto-migration is enabled via Hibernate DDL (`hibernate.ddl-auto=update`).

## Authentication & Authorization

User identification is handled via AWS API Gateway through the `X-User-Id` HTTP header. This header contains the unique user UUID and is required for:

- Creating user information (POST endpoints)
- Updating user information (PATCH endpoints)
- Accessing current user data

The Spring Security configuration manages endpoint protection and authorization policies.

## API Documentation

### Swagger UI

When the application is running, access the interactive API documentation at:

```
http://localhost:8081/swagger-ui.html
```

### OpenAPI/JSON

The OpenAPI specification is available at:

```
http://localhost:8081/api-docs
```

Enable the OpenAPI configuration by uncommenting `OpenAPIConfig.java` in the config folder if not already active.

## Logging

The service includes comprehensive logging for debugging and monitoring:

- **Root Level**: INFO
- **gRPC Service**: INFO (with detailed handshake logs at DEBUG)
- **Log Format**: `%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n`

gRPC method calls are logged with request/response details for audit and troubleshooting.

## Building with Docker

A Dockerfile is included for containerization:

```bash
docker build -t user-service:latest .
docker run -p 8081:8081 -p 9090:9090 user-service:latest
```

Ensure the PostgreSQL database is accessible from the container (update connection string as needed).

## Testing

Run all tests with Maven:

```bash
mvn test
```

Tests are located in `src/test/java/FCJ/user/`.

## Development

### Adding New Endpoints

1. Create a controller method in `src/main/java/FCJ/user/controller/`
2. Define DTOs in `src/main/java/FCJ/user/dto/`
3. Implement business logic in `src/main/java/FCJ/user/service/`
4. Add OpenAPI annotations for documentation

### Extending gRPC Service

1. Update `src/main/proto/user_info.proto` with new service definitions
2. Regenerate proto files (Maven will do this automatically)
3. Implement service methods in `UserInfoGrpcService.java`

## Maven Commands

- **Clean and Build**: `mvn clean package`
- **Run Application**: `mvn spring-boot:run`
- **Run Tests**: `mvn test`
- **Generate Proto Files**: `mvn protobuf:compile protobuf:compile-custom`
- **View Dependency Tree**: `mvn dependency:tree`

## Troubleshooting

### Database Connection Issues

If you encounter `Invalid jdbc url` or connection errors:
1. Verify PostgreSQL is running
2. Check connection string in `application.properties`
3. Ensure database exists and credentials are correct

### gRPC Compilation Errors

If gRPC compilation fails:
1. Run `mvn clean` to clear previous builds
2. Ensure protobuf compiler is installed
3. Check proto file syntax in `user_info.proto`

### Swagger Documentation Not Loading

If Swagger UI shows "Failed to fetch":
1. Verify springdoc dependency is in `pom.xml`
2. Check OpenAPI configuration is enabled
3. Ensure the application is running on port 8081
4. Check CORS configuration in SecurityConfig

### gRPC Port Conflicts

If port 9090 is already in use:
1. Change `grpc.server.port` in `application.properties`
2. Update gRPC client configurations accordingly

## Dependencies

Key dependencies managed via Maven:

- Spring Boot Web, Security, Data JPA, Actuator
- gRPC libraries (protobuf, stub, netty-shaded)
- PostgreSQL JDBC driver
- Lombok for code generation
- SpringDoc OpenAPI for Swagger
- JUnit, Spring Security Test for testing

See `pom.xml` for complete dependency list and versions.

## Environment Variables

Optional environment variables for Docker/Kubernetes deployment:

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SERVER_PORT`: HTTP server port (default: 8081)
- `GRPC_SERVER_PORT`: gRPC server port (default: 9090)

## Deployment

### Local Deployment

```bash
mvn clean package
java -jar target/user-0.0.1-SNAPSHOT.jar
```

### Docker Deployment

```bash
docker build -t user-service:latest .
docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/user_db \
           -e SPRING_DATASOURCE_PASSWORD=password \
           -p 8081:8081 \
           -p 9090:9090 \
           user-service:latest
```

### Kubernetes Deployment

Use the Dockerfile to build an image, then deploy with:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: user-service:latest
        ports:
        - containerPort: 8081
        - containerPort: 9090
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgres-service:5432/user_db
```

## Contributing

When contributing to this project:

1. Follow existing code structure and naming conventions
2. Add OpenAPI annotations to new endpoints
3. Write unit tests for new functionality
4. Update this README if adding new features
5. Run `mvn clean package` before committing
6. Ensure all tests pass with `mvn test`

## Notes

- User IDs are expected as valid UUIDs in most endpoints
- Membership and transaction information fields were removed in recent updates
- Keep gRPC and REST API in sync for consistent functionality
- All timestamps are stored in UTC
- Consider using AWS Secrets Manager for database credentials in production

## Support

For issues and questions:

1. Check the API documentation at `/swagger-ui.html`
2. Review application logs with appropriate logging levels
3. Verify PostgreSQL connectivity and schema
4. Consult the proto file for gRPC contract details

## License

Refer to the project's LICENSE file for licensing information.

---

**Last Updated**: December 2025
**Current Version**: 0.0.1-SNAPSHOT
**Java Version**: 25
**Spring Boot Version**: 3.5.6

