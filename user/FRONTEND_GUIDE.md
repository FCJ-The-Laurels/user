# Frontend Integration Guide - Quick Reference

## TL;DR - What Changed

### ✅ New Features
1. **Get your profile:** `GET /api/user-info/by-user-id` + `X-User-Id` header
2. **Update your profile:** `PATCH /api/user-info` + `X-User-Id` header (recommended)
3. **New field:** `membership` field added to all user responses

### 🔄 Still Works
- All existing endpoints work exactly the same
- No breaking changes

---

## Base URL
```
http://localhost:8081
```

---

## Common Use Cases

### 1. Get Current User's Profile
```javascript
// Recommended way (NEW)
const response = await fetch('/api/user-info/by-user-id', {
  headers: {
    'X-User-Id': 'USER_ID_FROM_AWS'  // AWS API Gateway provides this
  }
});
const user = await response.json();
console.log(user);
// {
//   id: "...",
//   userId: "...",
//   fullName: "John Doe",
//   avatarUrl: "...",
//   phoneNumber: "...",
//   address: "...",
//   membership: "false"
// }
```

### 2. Update Current User's Profile
```javascript
// Recommended way (NEW) - Update only what changed
const response = await fetch('/api/user-info', {
  method: 'PATCH',
  headers: {
    'Content-Type': 'application/json',
    'X-User-Id': 'USER_ID_FROM_AWS'
  },
  body: JSON.stringify({
    phoneNumber: '+1234567890'  // Only update this field
  })
});
const updated = await response.json();
```

### 3. Create User Profile
```javascript
// When user signs up (requires AWS API Gateway header)
const response = await fetch('/api/user-info', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-User-Id': 'USER_ID_FROM_AWS'
  },
  body: JSON.stringify({
    fullName: 'John Doe',
    avatarUrl: 'https://...',
    phoneNumber: '+1234567890',
    address: '123 Main St, City, Country'
  })
});
const user = await response.json();
```

### 4. Create Empty User Profile
```javascript
// Quick profile creation without details
const response = await fetch('/api/user-info/empty', {
  method: 'POST',
  headers: {
    'X-User-Id': 'USER_ID_FROM_AWS'
  }
});
const user = await response.json();
// { id: "...", userId: "...", fullName: null, ... }
```

---

## Request/Response Reference

### Request Body (Create/Update)
```json
{
  "fullName": "John Doe",              // Optional
  "avatarUrl": "https://example.com/avatar.jpg",  // Optional
  "phoneNumber": "+1234567890",        // Optional
  "address": "123 Main St, City, Country"  // Optional
}
```

### Response Body
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country",
  "membership": "false"  // NEW field
}
```

---

## All Endpoints

### Health Checks (No Authentication)
```
GET  /api/health/ping          - Simple check
GET  /api/health/detailed      - Detailed status
GET  /api/health/live          - Kubernetes liveness
GET  /api/health/ready         - Kubernetes readiness
GET  /api/health               - Combined status
```

### User Management

**Create:**
```
POST /api/user-info            - Full profile (requires X-User-Id)
POST /api/user-info/empty      - Empty profile (requires X-User-Id)
```

**Read:**
```
GET  /api/user-info/{id}       - By record ID
GET  /api/user-info/by-user-id - By user ID (requires X-User-Id) [NEW]
```

**Update:**
```
PUT  /api/user-info/{id}       - Full update by record ID
PATCH /api/user-info           - Partial update by user ID (requires X-User-Id) [NEW]
PATCH /api/user-info/{id}      - Partial update by record ID
```

**Delete:**
```
DELETE /api/user-info/{id}     - Delete by record ID
```

---

## Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success - Data returned |
| 201 | Created - Resource created |
| 204 | No Content - Deleted successfully |
| 400 | Bad Request - Invalid data |
| 404 | Not Found - Resource doesn't exist |
| 500 | Server Error |

---

## Required Headers

### For AWS API Gateway Integration
```javascript
headers: {
  'X-User-Id': 'UUID_FROM_API_GATEWAY'  // Provided by AWS API Gateway
}
```

### For JSON Requests
```javascript
headers: {
  'Content-Type': 'application/json'
}
```

---

## Error Handling

All errors follow this format:
```javascript
// Error response example
{
  "timestamp": "2025-12-06T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "UserInfo not found with id: ...",
  "path": "/api/user-info/..."
}
```

---

## Tips for Frontend Team

1. **Always include `X-User-Id`** when AWS API Gateway provides it
2. **Use the new endpoints** (by-user-id, PATCH /api/user-info) for better UX
3. **Handle null values** for optional fields (fullName, avatarUrl, etc.)
4. **Check membership field** for user tier/subscription features
5. **Implement proper error handling** for 404 and 500 responses

---

## Need More Details?

See `API_DOCUMENTATION.md` for:
- Complete endpoint specifications
- Detailed request/response examples
- cURL command examples
- Swagger/OpenAPI documentation

Access Swagger UI:
```
http://localhost:8081/swagger-ui.html
```

---

*Last Updated: December 6, 2025*

