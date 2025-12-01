# gRPC Health Check Testing Guide

## Overview
This document provides instructions for testing the gRPC health check service implementation and verifying that the `IllegalArgumentException: Invalid UUID string: health-check-probe` error has been resolved.

## Prerequisites

### Tools Required
1. **grpcurl** - gRPC CLI tool
   - Download: https://github.com/fullstorydev/grpcurl/releases
   - Add to PATH

2. **Postman** or **BloomRPC** - For gRPC testing UI (optional)
   - Postman: https://www.postman.com/
   - BloomRPC: https://github.com/bloomrpc/bloomrpc

## Testing Steps

### 1. Start the Application

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/user-0.0.1-SNAPSHOT.jar
# or
mvn spring-boot:run
```

Expected output should show:
```
gRPC server started, listening on port 9090
```

### 2. Test Health Check Service

#### Using grpcurl

Test the default health check:
```bash
grpcurl -plaintext localhost:9090 grpc.health.v1.Health/Check
```

Expected response:
```json
{
  "status": "SERVING"
}
```

Test health check for a specific service:
```bash
grpcurl -plaintext -d '{"service":"FCJ.user.grpc.UserInfoService"}' localhost:9090 grpc.health.v1.Health/Check
```

Expected response:
```json
{
  "status": "SERVING"
}
```

Test the watch endpoint (streaming):
```bash
grpcurl -plaintext localhost:9090 grpc.health.v1.Health/Watch
```

This will keep a streaming connection open showing health status updates.

### 3. Test Business Logic (blogUserInfo)

Test with a valid UUID:
```bash
grpcurl -plaintext \
  -d '{"id":"550e8400-e29b-41d4-a716-446655440000"}' \
  localhost:9090 FCJ.user.grpc.UserInfoService/BlogUserInfo
```

Expected response (if user exists):
```json
{
  "name": "John Doe",
  "avatar": "https://example.com/avatar.jpg"
}
```

Test with an invalid UUID (should be properly handled):
```bash
grpcurl -plaintext \
  -d '{"id":"invalid-uuid"}' \
  localhost:9090 FCJ.user.grpc.UserInfoService/BlogUserInfo
```

Expected response (gRPC error with INVALID_ARGUMENT):
```
Code: InvalidArgument
Message: Invalid ID format: Invalid UUID format: invalid-uuid
```

### 4. Verify Error Handling

Check application logs for proper error handling. You should see log entries like:

```
2025-12-01 10:30:45 - FCJ.user.grpc.GrpcHealthCheckService - gRPC Health Check requested for service: 
2025-12-01 10:30:50 - FCJ.user.grpc.UserInfoGrpcService - gRPC: blogUserInfo called with id=invalid-uuid
2025-12-01 10:30:50 - FCJ.user.grpc.UserInfoGrpcService - gRPC: blogUserInfo - Invalid argument error: Invalid UUID format: invalid-uuid
```

### 5. Test with Kubernetes/Docker

If deploying to Kubernetes, the health check probe should now work:

```yaml
livenessProbe:
  grpc:
    port: 9090
    service: ""  # Uses default service
  initialDelaySeconds: 10
  periodSeconds: 10

readinessProbe:
  grpc:
    port: 9090
    service: ""  # Uses default service
  initialDelaySeconds: 5
  periodSeconds: 10
```

### 6. Verify No "health-check-probe" Errors

Monitor the application logs and verify that:
- ✅ No `IllegalArgumentException: Invalid UUID string: health-check-probe` errors appear
- ✅ Health check requests are handled by `GrpcHealthCheckService`
- ✅ Business logic errors are separate from health check errors

## Common Issues and Troubleshooting

### Issue: "Command not found: grpcurl"
**Solution**: Install grpcurl or add to PATH

### Issue: "Failed to connect to localhost:9090"
**Solution**: 
- Verify application is running on port 9090
- Check firewall settings
- Use `-plaintext` flag for non-TLS connections

### Issue: "Service not found" error
**Solution**:
- Ensure proto files were properly compiled
- Rebuild with `mvn clean compile protobuf:compile`

### Issue: Still getting "Invalid UUID string: health-check-probe"
**Solution**:
- Verify `GrpcHealthCheckService.java` was created successfully
- Check that `pom.xml` has the `grpc-services` dependency
- Rebuild with `mvn clean package`
- Check application logs to confirm `@GrpcService` bean is registered

## Monitoring and Metrics

### Check gRPC Metrics

Access Actuator metrics:
```bash
curl http://localhost:8081/actuator/metrics
```

Look for gRPC-related metrics:
```bash
curl http://localhost:8081/actuator/metrics/grpc.server.rpc.started
curl http://localhost:8081/actuator/metrics/grpc.server.rpc.completed
```

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.net.devh.boot.grpc.server=DEBUG
logging.level.io.grpc=DEBUG
```

## Performance Testing

### Concurrent Health Checks

```bash
# Run multiple health checks concurrently
for i in {1..100}; do
  grpcurl -plaintext localhost:9090 grpc.health.v1.Health/Check &
done
wait
```

Expected: All requests should succeed with no errors.

## Cleanup

After testing, stop the application:
```bash
Ctrl+C
```

## Summary

✅ Health check requests are properly routed to `GrpcHealthCheckService`
✅ Business logic requests are handled by appropriate service methods
✅ Invalid UUID errors are properly caught and reported
✅ No `health-check-probe` UUID errors in logs
✅ Application is production-ready for gRPC with health checks

