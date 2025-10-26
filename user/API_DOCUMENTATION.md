# User Info API Documentation

## Overview
This API provides endpoints for managing user information records. All creation endpoints require authentication via AWS API Gateway and extract the user ID from HTTP headers.

**Base URL:** `/api/user-info`

**Authentication:** User ID is passed via the `X-User-Id` header from AWS API Gateway for creation endpoints.

---

## Endpoints

### 1. Create User Info
Creates a new user information record with all fields.

- **URL:** `/api/user-info`
- **Method:** `POST`
- **Headers:**
  - `X-User-Id` (required): UUID of the user from AWS API Gateway
  - `Content-Type`: `application/json`

#### Request Body
```json
{
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

#### Request Body Schema
| Field | Type | Required | Description | Example |
|-------|------|----------|-------------|---------|
| fullName | String | No | Full name of the user | "John Doe" |
| avatarUrl | String | No | URL to user's avatar image | "https://example.com/avatar.jpg" |
| phoneNumber | String | No | User's phone number | "+1234567890" |
| address | String | No | User's physical address | "123 Main St, City, Country" |

#### Response
**Status Code:** `201 Created`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 400 | Invalid input data |
| 500 | Internal server error |

---

### 2. Create Empty User Info
Creates an empty user information record with only the user ID.

- **URL:** `/api/user-info/empty`
- **Method:** `POST`
- **Headers:**
  - `X-User-Id` (required): UUID of the user from AWS API Gateway

#### Request Body
None required

#### Response
**Status Code:** `201 Created`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": null,
  "avatarUrl": null,
  "phoneNumber": null,
  "address": null
}
```

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 400 | Invalid input data |
| 500 | Internal server error |

---

### 3. Get User Info by ID
Retrieves user information by its unique identifier.

- **URL:** `/api/user-info/{id}`
- **Method:** `GET`
- **Path Parameters:**
  - `id` (required): UUID of the user info record

#### Response
**Status Code:** `200 OK`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 404 | User info not found |
| 500 | Internal server error |

---

### 4. Update User Info (Full Update)
Updates all fields of an existing user information record. This is a full update - all fields should be provided.

- **URL:** `/api/user-info/{id}`
- **Method:** `PUT`
- **Path Parameters:**
  - `id` (required): UUID of the user info record to update
- **Headers:**
  - `Content-Type`: `application/json`

#### Request Body
```json
{
  "fullName": "Jane Doe",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "phoneNumber": "+9876543210",
  "address": "456 Oak Ave, New City, Country"
}
```

#### Request Body Schema
| Field | Type | Required | Description | Example |
|-------|------|----------|-------------|---------|
| fullName | String | No | Full name of the user | "Jane Doe" |
| avatarUrl | String | No | URL to user's avatar image | "https://example.com/new-avatar.jpg" |
| phoneNumber | String | No | User's phone number | "+9876543210" |
| address | String | No | User's physical address | "456 Oak Ave, New City, Country" |

#### Response
**Status Code:** `200 OK`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": "Jane Doe",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "phoneNumber": "+9876543210",
  "address": "456 Oak Ave, New City, Country"
}
```

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 400 | Invalid input data |
| 404 | User info not found |
| 500 | Internal server error |

---

### 5. Patch User Info (Partial Update)
Partially updates fields of an existing user information record. Only provided fields will be updated.

- **URL:** `/api/user-info/{id}`
- **Method:** `PATCH`
- **Path Parameters:**
  - `id` (required): UUID of the user info record to patch
- **Headers:**
  - `Content-Type`: `application/json`

#### Request Body
```json
{
  "phoneNumber": "+1111111111"
}
```

#### Request Body Schema
All fields are optional. Only include fields you want to update.

| Field | Type | Required | Description | Example |
|-------|------|----------|-------------|---------|
| fullName | String | No | Full name of the user | "Jane Doe" |
| avatarUrl | String | No | URL to user's avatar image | "https://example.com/new-avatar.jpg" |
| phoneNumber | String | No | User's phone number | "+1111111111" |
| address | String | No | User's physical address | "456 Oak Ave, New City, Country" |

#### Response
**Status Code:** `200 OK`

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174000",
  "fullName": "Jane Doe",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "phoneNumber": "+1111111111",
  "address": "456 Oak Ave, New City, Country"
}
```

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 400 | Invalid input data |
| 404 | User info not found |
| 500 | Internal server error |

---

### 6. Delete User Info
Deletes a user information record by its unique identifier.

- **URL:** `/api/user-info/{id}`
- **Method:** `DELETE`
- **Path Parameters:**
  - `id` (required): UUID of the user info record to delete

#### Response
**Status Code:** `204 No Content`

No response body.

#### Error Responses
| Status Code | Description |
|------------|-------------|
| 404 | User info not found |
| 500 | Internal server error |

---

## Data Models

### UserInfoDTO (Response Model)
```json
{
  "id": "UUID",
  "userId": "UUID",
  "fullName": "string or null",
  "avatarUrl": "string or null",
  "phoneNumber": "string or null",
  "address": "string or null"
}
```

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Unique identifier for the user info record |
| userId | UUID | User identifier from AWS API Gateway |
| fullName | String | Full name of the user |
| avatarUrl | String | URL to user's avatar image |
| phoneNumber | String | User's phone number |
| address | String | User's physical address |

### UserInfoCreation (Request Model)
```json
{
  "fullName": "string",
  "avatarUrl": "string",
  "phoneNumber": "string",
  "address": "string"
}
```

| Field | Type | Description |
|-------|------|-------------|
| fullName | String | Full name of the user |
| avatarUrl | String | URL to user's avatar image |
| phoneNumber | String | User's phone number |
| address | String | User's physical address |

---

## Error Response Format
All error responses follow this format:

```json
{
  "timestamp": "2025-10-26T10:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "UserInfo not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/user-info/123e4567-e89b-12d3-a456-426614174000"
}
```

---

## Common Status Codes

| Status Code | Meaning |
|------------|---------|
| 200 | OK - Request succeeded |
| 201 | Created - Resource successfully created |
| 204 | No Content - Request succeeded, no content to return |
| 400 | Bad Request - Invalid input data |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server encountered an error |

---

## Notes

1. **User ID Source**: The `userId` field is automatically extracted from the `X-User-Id` header for creation endpoints. It should not be included in the request body.

2. **UUID Format**: All ID fields use UUID format (e.g., `123e4567-e89b-12d3-a456-426614174000`).

3. **Nullable Fields**: All fields except `id` and `userId` can be null in the response.

4. **Full vs Partial Update**: 
   - Use `PUT` for full updates where all fields should be replaced
   - Use `PATCH` for partial updates where only specific fields are modified

5. **User ID Immutability**: The `userId` field cannot be updated after creation. It is set only during the creation process via the HTTP header.

---

## Example Usage

### Creating a User Info with cURL
```bash
curl -X POST "http://localhost:8080/api/user-info" \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 987e6543-e21b-12d3-a456-426614174000" \
  -d '{
    "fullName": "John Doe",
    "avatarUrl": "https://example.com/avatar.jpg",
    "phoneNumber": "+1234567890",
    "address": "123 Main St, City, Country"
  }'
```

### Getting User Info by ID with cURL
```bash
curl -X GET "http://localhost:8080/api/user-info/123e4567-e89b-12d3-a456-426614174000"
```

### Partially Updating User Info with cURL
```bash
curl -X PATCH "http://localhost:8080/api/user-info/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "+9999999999"
  }'
```

### Deleting User Info with cURL
```bash
curl -X DELETE "http://localhost:8080/api/user-info/123e4567-e89b-12d3-a456-426614174000"
```

---

## Swagger/OpenAPI Documentation

For interactive API documentation, visit:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

---

*Last Updated: October 26, 2025*

