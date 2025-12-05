# API Changes Summary for Frontend Team

## Updated: December 6, 2025

---

## Overview

The User Info API has been updated to provide better support for AWS API Gateway integration and improved flexibility for the frontend team.

---

## What Changed ✅

### 1. **New Endpoint: Get User Info by User ID**
   - **URL:** `GET /api/user-info/by-user-id`
   - **Authentication:** Requires `X-User-Id` header
   - **Use Case:** Retrieve current user's profile information using the header provided by AWS API Gateway
   - **Response:** `200 OK` with UserInfoDTO
   - **Why:** Eliminates the need for frontend to track user IDs separately; AWS API Gateway provides it automatically

### 2. **New Endpoint: Patch User Info by User ID**
   - **URL:** `PATCH /api/user-info` (without ID in path)
   - **Authentication:** Requires `X-User-Id` header
   - **Use Case:** Update current user's profile information partially using the header from AWS API Gateway
   - **Request Body:** Any subset of fields (all optional)
   - **Response:** `200 OK` with updated UserInfoDTO
   - **Why:** Simpler and more intuitive for updating the current user's profile

### 3. **Added New Field: Membership**
   - **Field Name:** `membership`
   - **Type:** String (nullable)
   - **Available in:** All UserInfoDTO responses
   - **Description:** Represents the membership status of the user
   - **Example:** `"false"`, `"true"`, `"premium"` (format depends on your business logic)

---

## What Stays the Same (No Changes) 🔄

### Existing Endpoints - Still Available

| Endpoint | Method | Authentication | Status |
|----------|--------|-----------------|--------|
| `/api/user-info` | POST | X-User-Id | ✅ Unchanged |
| `/api/user-info/empty` | POST | X-User-Id | ✅ Unchanged |
| `/api/user-info/{id}` | GET | None | ✅ Unchanged |
| `/api/user-info/{id}` | PUT | None | ✅ Unchanged |
| `/api/user-info/{id}` | PATCH | None | ✅ Unchanged |
| `/api/user-info/{id}` | DELETE | None | ✅ Unchanged |
| `/api/health/*` | GET | None | ✅ Unchanged |

### Request/Response Schemas

**UserInfoCreation (Request Body)** - No changes:
- `fullName` (String, optional)
- `avatarUrl` (String, optional)
- `phoneNumber` (String, optional)
- `address` (String, optional)

**UserInfoDTO (Response)** - Same fields, plus new `membership` field:
- `id` (UUID)
- `userId` (UUID)
- `fullName` (String, nullable)
- `avatarUrl` (String, nullable)
- `phoneNumber` (String, nullable)
- `address` (String, nullable)
- **`membership` (String, nullable)** ← NEW

### Authentication & Headers

- `X-User-Id` header is still required for creation and new read/update endpoints
- Same validation rules apply
- User ID must be a valid UUID format

### Error Handling

All error responses remain the same:
- Status codes: 400, 404, 500, etc.
- Error response format unchanged
- Error messages follow the same pattern

---

## Migration Guide for Frontend Team

### If you were using the old way (with UUID in URL):

**Old way (Still works):**
```javascript
// Get user info by UUID
fetch('http://localhost:8081/api/user-info/123e4567-e89b-12d3-a456-426614174000')
  .then(r => r.json())

// Update user info by UUID
fetch('http://localhost:8081/api/user-info/123e4567-e89b-12d3-a456-426614174000', {
  method: 'PATCH',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ phoneNumber: '+1111111111' })
})
```

### New recommended way (Using AWS API Gateway header):

```javascript
// Get current user's info (recommended)
fetch('http://localhost:8081/api/user-info/by-user-id', {
  headers: { 'X-User-Id': 'USER_ID_FROM_AWS_GATEWAY' }
})
  .then(r => r.json())

// Update current user's info (recommended)
fetch('http://localhost:8081/api/user-info', {
  method: 'PATCH',
  headers: {
    'Content-Type': 'application/json',
    'X-User-Id': 'USER_ID_FROM_AWS_GATEWAY'
  },
  body: JSON.stringify({ phoneNumber: '+1111111111' })
})
```

---

## Recommended Migration Timeline

### Phase 1 (Immediate):
- Start using the new endpoints for any new features
- Both old and new endpoints work simultaneously

### Phase 2 (Optional):
- Gradually migrate existing code to use the new endpoints
- Old endpoints will continue to work for backward compatibility

### Phase 3 (Future):
- Old endpoints will remain available; no deprecation planned

---

## Complete API Endpoint Summary

### Health Endpoints
- `GET /api/health/ping` - Simple health check
- `GET /api/health/detailed` - Detailed health with components
- `GET /api/health/live` - Kubernetes liveness probe
- `GET /api/health/ready` - Kubernetes readiness probe
- `GET /api/health` - Combined health status

### User Info Endpoints

**Create:**
- `POST /api/user-info` (requires X-User-Id header)
- `POST /api/user-info/empty` (requires X-User-Id header)

**Read:**
- `GET /api/user-info/{id}` (by info record ID)
- `GET /api/user-info/by-user-id` (by user ID, requires X-User-Id header) **NEW**

**Update:**
- `PUT /api/user-info/{id}` (full update by info record ID)
- `PATCH /api/user-info` (partial update by user ID, requires X-User-Id header) **NEW**
- `PATCH /api/user-info/{id}` (partial update by info record ID)

**Delete:**
- `DELETE /api/user-info/{id}` (by info record ID)

---

## Base URL

**Unchanged:**
- HTTP: `http://localhost:8081`
- gRPC: `localhost:9090`

---

## Questions?

For detailed API documentation with request/response examples, see:
- Full Documentation: `/API_DOCUMENTATION.md`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

---

*Document Last Updated: December 6, 2025*

