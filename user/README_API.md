# User Info Service API Documentation

## Base Information

- **Base URL**: `http://localhost:8081`
- **API Prefix**: `localhost:8081/api/user-info`
- **Authentication**: User ID is passed via AWS API Gateway header `X-User-Id`

---

## API Endpoints

### 1. Create User Info

Creates a new user information record with all fields.

- **URL**: `/api/user-info`
- **Method**: `POST`
- **Headers**:
  - `Content-Type: application/json`
  - `X-User-Id: {userId}` (required) - UUID format

**Request Body**:
```json
{
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

**Success Response**:
- **Code**: `201 CREATED`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

**Error Responses**:
- **Code**: `400 BAD REQUEST`
  ```json
  {
    "timestamp": "2025-11-24T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid input",
    "path": "/api/user-info"
  }
  ```

- **Code**: `500 INTERNAL SERVER ERROR`
  ```json
  {
    "timestamp": "2025-11-24T10:30:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Error message details",
    "path": "/api/user-info"
  }
  ```

---

### 2. Create Empty User Info

Creates an empty user information record with only the user ID.

- **URL**: `/api/user-info/empty`
- **Method**: `POST`
- **Headers**:
  - `X-User-Id: {userId}` (required) - UUID format

**Request Body**: None

**Success Response**:
- **Code**: `201 CREATED`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": null,
  "avatarUrl": null,
  "phoneNumber": null,
  "address": null
}
```

**Error Responses**:
- **Code**: `400 BAD REQUEST` - Invalid user ID format
- **Code**: `500 INTERNAL SERVER ERROR`

---

### 3. Get User Info by ID

Retrieves user information by the user info UUID.

- **URL**: `/api/user-info/{id}`
- **Method**: `GET`
- **URL Parameters**:
  - `id` (required) - UUID of the user info record

**Success Response**:
- **Code**: `200 OK`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

**Error Responses**:
- **Code**: `404 NOT FOUND`
  ```json
  {
    "timestamp": "2025-11-24T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "UserInfo not found with id: 123e4567-e89b-12d3-a456-426614174000",
    "path": "/api/user-info/123e4567-e89b-12d3-a456-426614174000"
  }
  ```

- **Code**: `500 INTERNAL SERVER ERROR`

---

### 4. Get User Info by User ID

Retrieves user information by the User ID from AWS API Gateway header.

- **URL**: `/api/user-info/by-user-id`
- **Method**: `GET`
- **Headers**:
  - `X-User-Id: {userId}` (required) - UUID format

**Success Response**:
- **Code**: `200 OK`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": "John Doe",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1234567890",
  "address": "123 Main St, City, Country"
}
```

**Error Responses**:
- **Code**: `404 NOT FOUND`
  ```json
  {
    "timestamp": "2025-11-24T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "UserInfo not found with userId: 123e4567-e89b-12d3-a456-426614174001",
    "path": "/api/user-info/by-user-id"
  }
  ```

- **Code**: `500 INTERNAL SERVER ERROR`

---

### 5. Update User Info (Full Update)

Updates all fields of an existing user information record.

- **URL**: `/api/user-info/{id}`
- **Method**: `PUT`
- **URL Parameters**:
  - `id` (required) - UUID of the user info record to update
- **Headers**:
  - `Content-Type: application/json`

**Request Body**:
```json
{
  "fullName": "Jane Doe",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "phoneNumber": "+9876543210",
  "address": "456 Oak Ave, New City, Country"
}
```

**Success Response**:
- **Code**: `200 OK`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": "Jane Doe",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "phoneNumber": "+9876543210",
  "address": "456 Oak Ave, New City, Country"
}
```

**Error Responses**:
- **Code**: `404 NOT FOUND` - User info not found
- **Code**: `400 BAD REQUEST` - Invalid input
- **Code**: `500 INTERNAL SERVER ERROR`

---

### 6. Patch User Info (Partial Update)

Partially updates fields of an existing user information record. Only provided fields will be updated.

- **URL**: `/api/user-info/{id}`
- **Method**: `PATCH`
- **URL Parameters**:
  - `id` (required) - UUID of the user info record to patch
- **Headers**:
  - `Content-Type: application/json`

**Request Body** (all fields are optional):
```json
{
  "fullName": "Jane Smith",
  "phoneNumber": "+1111111111"
}
```

**Success Response**:
- **Code**: `200 OK`
- **Content**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "123e4567-e89b-12d3-a456-426614174001",
  "fullName": "Jane Smith",
  "avatarUrl": "https://example.com/avatar.jpg",
  "phoneNumber": "+1111111111",
  "address": "123 Main St, City, Country"
}
```

**Error Responses**:
- **Code**: `404 NOT FOUND` - User info not found
- **Code**: `400 BAD REQUEST` - Invalid input
- **Code**: `500 INTERNAL SERVER ERROR`

---

### 7. Delete User Info

Deletes a user information record by UUID.

- **URL**: `/api/user-info/{id}`
- **Method**: `DELETE`
- **URL Parameters**:
  - `id` (required) - UUID of the user info record to delete

**Success Response**:
- **Code**: `204 NO CONTENT`
- **Content**: No content

**Error Responses**:
- **Code**: `404 NOT FOUND`
  ```json
  {
    "timestamp": "2025-11-24T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "UserInfo not found with id: 123e4567-e89b-12d3-a456-426614174000",
    "path": "/api/user-info/123e4567-e89b-12d3-a456-426614174000"
  }
  ```

- **Code**: `500 INTERNAL SERVER ERROR`

---

## Data Models

### UserInfoDTO (Response Model)

```json
{
  "id": "UUID - Unique identifier of the user info record",
  "userId": "UUID - User ID from AWS Cognito/API Gateway",
  "fullName": "string - Full name of the user",
  "avatarUrl": "string - URL to user's avatar image",
  "phoneNumber": "string - Phone number",
  "address": "string - User's address"
}
```

### UserInfoCreation (Request Model)

```json
{
  "fullName": "string - Full name of the user (optional)",
  "avatarUrl": "string - URL to user's avatar image (optional)",
  "phoneNumber": "string - Phone number (optional)",
  "address": "string - User's address (optional)"
}
```

### ErrorResponse (Error Model)

```json
{
  "timestamp": "ISO 8601 datetime - When the error occurred",
  "status": "integer - HTTP status code",
  "error": "string - Error type",
  "message": "string - Detailed error message",
  "path": "string - Request path that caused the error"
}
```

---

## Common HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Resource deleted successfully |
| 400 | Bad Request - Invalid input or malformed request |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error occurred |

---

## Integration Notes

### Authentication Header
All endpoints that create or retrieve user-specific data require the `X-User-Id` header:
```
X-User-Id: 123e4567-e89b-12d3-a456-426614174001
```

This header is automatically provided by AWS API Gateway after user authentication.

### UUID Format
All IDs (both `id` and `userId`) use UUID format (version 4):
```
123e4567-e89b-12d3-a456-426614174000
```

### Content Type
For POST, PUT, and PATCH requests, always include:
```
Content-Type: application/json
```

---

## Example Usage

### JavaScript/Fetch Example

```javascript
// Create user info
const createUserInfo = async (userId, userInfo) => {
  const response = await fetch('http://localhost:8081/api/user-info', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': userId
    },
    body: JSON.stringify(userInfo)
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }
  
  return response.json();
};

// Get user info by user ID
const getUserInfo = async (userId) => {
  const response = await fetch('http://localhost:8081/api/user-info/by-user-id', {
    method: 'GET',
    headers: {
      'X-User-Id': userId
    }
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }
  
  return response.json();
};

// Update user info
const updateUserInfo = async (id, userInfo) => {
  const response = await fetch(`http://localhost:8081/api/user-info/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(userInfo)
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }
  
  return response.json();
};

// Partial update
const patchUserInfo = async (id, updates) => {
  const response = await fetch(`http://localhost:8081/api/user-info/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(updates)
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }
  
  return response.json();
};

// Delete user info
const deleteUserInfo = async (id) => {
  const response = await fetch(`http://localhost:8081/api/user-info/${id}`, {
    method: 'DELETE'
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }
};
```

### Axios Example

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/user-info';

// Create user info
const createUserInfo = async (userId, userInfo) => {
  try {
    const response = await axios.post(API_BASE_URL, userInfo, {
      headers: {
        'X-User-Id': userId
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error creating user info:', error.response.data);
    throw error;
  }
};

// Get user info by user ID
const getUserInfo = async (userId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/by-user-id`, {
      headers: {
        'X-User-Id': userId
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching user info:', error.response.data);
    throw error;
  }
};
```

---

## Swagger UI

Interactive API documentation is available at:
```
http://localhost:8081/swagger-ui/index.html
```

---

## Additional Information

- **Service runs on port**: 8081 (HTTP), 9090 (gRPC)
- **Database**: Uses JPA/Hibernate for data persistence
- **Framework**: Spring Boot
- **API Version**: 1.0

For questions or issues, please contact the backend team.

