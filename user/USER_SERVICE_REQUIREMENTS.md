# USER SERVICE - SUBSCRIPTION FIELDS REQUIREMENTS

**Ngày tạo:** 2025-12-07
**Mục đích:** Định nghĩa rõ ràng fields và endpoints cần thêm vào User Service

---

## 📊 FIELDS CẦN THÊM VÀO UserInfo ENTITY

### 1. Subscription Fields (6 fields - PAID subscription only)

```java
@Document(collection = "user_info")
public class UserInfo {
    // ============================================
    // EXISTING FIELDS - KHÔNG THAY ĐỔI
    // ============================================
    @Id
    private String id;                    // Record ID (UUID)

    private String userId;                // Cognito User ID (UUID)
    private String fullName;              // Họ tên
    private String avatarUrl;             // URL avatar
    private String phoneNumber;           // Số điện thoại
    private String address;               // Địa chỉ

    // ============================================
    // NEW FIELDS - SUBSCRIPTION (PAID)
    // ============================================

    /**
     * Subscription tier
     * Valid values: "BASIC", "PREMIUM", "VIP"
     * NOTE: KHÔNG lưu "TRIAL" - trial được quản lý bởi Program Service
     */
    private String subscriptionTier;

    /**
     * Subscription status
     * Valid values: "ACTIVE", "GRACE", "EXPIRED", "CANCELLED"
     * NOTE: KHÔNG lưu "TRIALING" - trial status được quản lý bởi Program Service
     */
    private String subscriptionStatus;

    /**
     * Subscription expiration date
     * Null nếu chưa có paid subscription
     */
    private Instant subscriptionExpiresAt;

    /**
     * Payment method used for last payment
     * Valid values: "MOMO", "CREDIT_CARD", "BANK_TRANSFER"
     * Null nếu chưa có payment nào
     */
    private String paymentMethod;

    /**
     * Last payment transaction ID
     * VD: "MOMO123456789"
     * Null nếu chưa có payment nào
     */
    private String lastPaymentId;

    /**
     * Last payment date
     * Null nếu chưa có payment nào
     */
    private Instant lastPaymentDate;

    /**
     * Last payment amount (in VND)
     * VD: 500000.0 (500k VND)
     * Null nếu chưa có payment nào
     */
    private Double lastPaymentAmount;

    /**
     * Next billing date
     * Null nếu subscription đã expire hoặc không auto-renew
     */
    private Instant nextBillingDate;

    /**
     * Auto-renewal enabled
     * Default: false
     */
    private Boolean autoRenewal;
}
```

---

## 🔧 VALIDATION RULES

### Field Validation:

```java
public class UserInfo {

    // Validate subscription tier
    public void setSubscriptionTier(String tier) {
        if (tier != null) {
            // KHÔNG cho phép "TRIAL" tier
            if ("TRIAL".equals(tier)) {
                throw new IllegalArgumentException(
                    "Trial tier is managed by Program Service. " +
                    "User Service only manages paid subscriptions (BASIC, PREMIUM, VIP)."
                );
            }

            // Chỉ cho phép: BASIC, PREMIUM, VIP
            if (!Arrays.asList("BASIC", "PREMIUM", "VIP").contains(tier)) {
                throw new IllegalArgumentException(
                    "Invalid subscription tier. Allowed values: BASIC, PREMIUM, VIP"
                );
            }
        }
        this.subscriptionTier = tier;
    }

    // Validate subscription status
    public void setSubscriptionStatus(String status) {
        if (status != null) {
            // KHÔNG cho phép "TRIALING" status
            if ("TRIALING".equals(status)) {
                throw new IllegalArgumentException(
                    "TRIALING status is managed by Program Service. " +
                    "User Service only manages paid subscription statuses."
                );
            }

            // Chỉ cho phép: ACTIVE, GRACE, EXPIRED, CANCELLED
            if (!Arrays.asList("ACTIVE", "GRACE", "EXPIRED", "CANCELLED").contains(status)) {
                throw new IllegalArgumentException(
                    "Invalid subscription status. Allowed values: ACTIVE, GRACE, EXPIRED, CANCELLED"
                );
            }
        }
        this.subscriptionStatus = status;
    }

    // Validate payment method
    public void setPaymentMethod(String method) {
        if (method != null) {
            if (!Arrays.asList("MOMO", "CREDIT_CARD", "BANK_TRANSFER").contains(method)) {
                throw new IllegalArgumentException(
                    "Invalid payment method. Allowed values: MOMO, CREDIT_CARD, BANK_TRANSFER"
                );
            }
        }
        this.paymentMethod = method;
    }
}
```

---

## 🛣️ ENDPOINTS CẦN CẬP NHẬT

### 1. ✅ PATCH /api/user-info (Already exists - NEED UPDATE)

**Endpoint hiện tại:**
```
PATCH /api/user-info
Headers:
  X-User-Id: {cognito-user-id}
  Content-Type: application/json

Body (hiện tại chỉ có):
{
  "fullName": "string",
  "avatarUrl": "string",
  "phoneNumber": "string",
  "address": "string"
}
```

**CẦN CẬP NHẬT để accept thêm subscription fields:**
```
PATCH /api/user-info
Headers:
  X-User-Id: {cognito-user-id}
  Content-Type: application/json

Body (NEW - add subscription fields):
{
  // Existing fields (optional)
  "fullName": "string",
  "avatarUrl": "string",
  "phoneNumber": "string",
  "address": "string",

  // NEW - Subscription fields (optional)
  "subscriptionTier": "BASIC|PREMIUM|VIP",
  "subscriptionStatus": "ACTIVE|GRACE|EXPIRED|CANCELLED",
  "subscriptionExpiresAt": "2026-01-07T00:00:00Z",

  "paymentMethod": "MOMO|CREDIT_CARD|BANK_TRANSFER",
  "lastPaymentId": "string",
  "lastPaymentDate": "2025-12-07T10:30:00Z",
  "lastPaymentAmount": 500000.0,

  "nextBillingDate": "2026-01-07T00:00:00Z",
  "autoRenewal": false
}
```

**Response (200 OK):**
```json
{
  "id": "user-info-record-id",
  "userId": "cognito-user-id",

  // Existing fields
  "fullName": "Nguyễn Văn A",
  "avatarUrl": "https://...",
  "phoneNumber": "+84901234567",
  "address": "123 ABC Street",

  // NEW - Subscription fields
  "subscriptionTier": "PREMIUM",
  "subscriptionStatus": "ACTIVE",
  "subscriptionExpiresAt": "2026-01-07T00:00:00Z",

  "paymentMethod": "MOMO",
  "lastPaymentId": "MOMO123456789",
  "lastPaymentDate": "2025-12-07T10:30:00Z",
  "lastPaymentAmount": 500000.0,

  "nextBillingDate": "2026-01-07T00:00:00Z",
  "autoRenewal": false
}
```

**Implementation trong UserInfoController:**
```java
@PatchMapping("/api/user-info")
public ResponseEntity<UserInfo> updateUserInfo(
    @RequestHeader("X-User-Id") String userId,
    @RequestBody UserInfoUpdateDTO updateDTO
) {
    // Get existing user info
    UserInfo userInfo = userInfoService.findByUserId(userId);

    // Update basic fields (existing logic)
    if (updateDTO.getFullName() != null) {
        userInfo.setFullName(updateDTO.getFullName());
    }
    if (updateDTO.getAvatarUrl() != null) {
        userInfo.setAvatarUrl(updateDTO.getAvatarUrl());
    }
    // ... other fields

    // NEW - Update subscription fields
    if (updateDTO.getSubscriptionTier() != null) {
        userInfo.setSubscriptionTier(updateDTO.getSubscriptionTier());
    }
    if (updateDTO.getSubscriptionStatus() != null) {
        userInfo.setSubscriptionStatus(updateDTO.getSubscriptionStatus());
    }
    if (updateDTO.getSubscriptionExpiresAt() != null) {
        userInfo.setSubscriptionExpiresAt(updateDTO.getSubscriptionExpiresAt());
    }
    if (updateDTO.getPaymentMethod() != null) {
        userInfo.setPaymentMethod(updateDTO.getPaymentMethod());
    }
    if (updateDTO.getLastPaymentId() != null) {
        userInfo.setLastPaymentId(updateDTO.getLastPaymentId());
    }
    if (updateDTO.getLastPaymentDate() != null) {
        userInfo.setLastPaymentDate(updateDTO.getLastPaymentDate());
    }
    if (updateDTO.getLastPaymentAmount() != null) {
        userInfo.setLastPaymentAmount(updateDTO.getLastPaymentAmount());
    }
    if (updateDTO.getNextBillingDate() != null) {
        userInfo.setNextBillingDate(updateDTO.getNextBillingDate());
    }
    if (updateDTO.getAutoRenewal() != null) {
        userInfo.setAutoRenewal(updateDTO.getAutoRenewal());
    }

    // Save and return
    UserInfo updated = userInfoService.save(userInfo);
    return ResponseEntity.ok(updated);
}
```

---

### 2. ✅ GET /api/user-info/by-user-id (Already exists - NEED UPDATE)

**CẦN CẬP NHẬT response để include subscription fields:**

**Request:**
```
GET /api/user-info/by-user-id
Headers:
  X-User-Id: {cognito-user-id}
```

**Response (200 OK) - UPDATED:**
```json
{
  "id": "user-info-record-id",
  "userId": "cognito-user-id",

  // Existing fields
  "fullName": "Nguyễn Văn A",
  "avatarUrl": "https://...",
  "phoneNumber": "+84901234567",
  "address": "123 ABC Street",

  // NEW - Subscription fields (null if not set)
  "subscriptionTier": "PREMIUM",
  "subscriptionStatus": "ACTIVE",
  "subscriptionExpiresAt": "2026-01-07T00:00:00Z",

  "paymentMethod": "MOMO",
  "lastPaymentId": "MOMO123456789",
  "lastPaymentDate": "2025-12-07T10:30:00Z",
  "lastPaymentAmount": 500000.0,

  "nextBillingDate": "2026-01-07T00:00:00Z",
  "autoRenewal": false
}
```

**Implementation:**
```java
@GetMapping("/api/user-info/by-user-id")
public ResponseEntity<UserInfo> getUserInfoByUserId(
    @RequestHeader("X-User-Id") String userId
) {
    UserInfo userInfo = userInfoService.findByUserId(userId);

    // Response tự động include tất cả fields (kể cả subscription fields)
    return ResponseEntity.ok(userInfo);
}
```

---

## 📝 DTO CLASSES CẦN TẠO/CẬP NHẬT

### UserInfoUpdateDTO.java

```java
package com.smokefree.user.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class UserInfoUpdateDTO {
    // Existing fields
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private String address;

    // NEW - Subscription fields
    private String subscriptionTier;      // "BASIC"|"PREMIUM"|"VIP"
    private String subscriptionStatus;    // "ACTIVE"|"GRACE"|"EXPIRED"|"CANCELLED"
    private Instant subscriptionExpiresAt;

    private String paymentMethod;         // "MOMO"|"CREDIT_CARD"|"BANK_TRANSFER"
    private String lastPaymentId;
    private Instant lastPaymentDate;
    private Double lastPaymentAmount;

    private Instant nextBillingDate;
    private Boolean autoRenewal;
}
```

---

## 🗄️ DATABASE MIGRATION

### Migration Script (MongoDB)

```javascript
// Migration: Add subscription fields to user_info collection
db.user_info.updateMany(
  {},
  {
    $set: {
      subscriptionTier: null,
      subscriptionStatus: null,
      subscriptionExpiresAt: null,
      paymentMethod: null,
      lastPaymentId: null,
      lastPaymentDate: null,
      lastPaymentAmount: null,
      nextBillingDate: null,
      autoRenewal: false
    }
  }
);

// Add indexes for querying
db.user_info.createIndex({ subscriptionTier: 1 });
db.user_info.createIndex({ subscriptionStatus: 1 });
db.user_info.createIndex({ subscriptionExpiresAt: 1 });
```

---

## 🔄 INTEGRATION WITH PROGRAM SERVICE

### Program Service → User Service HTTP Call

Sau khi payment thành công, Program Service cần call User Service:

```java
// In Program Service - PaymentCallbackHandler.java

@Service
public class PaymentCallbackHandler {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.base.url}")
    private String userServiceBaseUrl;

    public void handleMoMoPaymentSuccess(MoMoPaymentCallback callback) {
        // 1. Update program (remove trial)
        Program program = programRepository.findById(callback.getProgramId());
        program.setIsTrial(false);
        program.setTrialStartedAt(null);
        program.setTrialEndExpected(null);
        programRepository.save(program);

        // 2. Update user subscription in User Service
        updateUserSubscription(
            program.getUserId(),
            callback.getSubscriptionType(),
            callback.getTransactionId(),
            callback.getAmount()
        );
    }

    private void updateUserSubscription(
        String userId,
        String tier,
        String transactionId,
        Double amount
    ) {
        String url = userServiceBaseUrl + "/api/user-info";

        // Calculate dates
        Instant now = Instant.now();
        Instant expiresAt = now.plus(30, ChronoUnit.DAYS);
        Instant nextBillingDate = expiresAt;

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("subscriptionTier", tier);
        requestBody.put("subscriptionStatus", "ACTIVE");
        requestBody.put("subscriptionExpiresAt", expiresAt);

        requestBody.put("paymentMethod", "MOMO");
        requestBody.put("lastPaymentId", transactionId);
        requestBody.put("lastPaymentDate", now);
        requestBody.put("lastPaymentAmount", amount);

        requestBody.put("nextBillingDate", nextBillingDate);
        requestBody.put("autoRenewal", false);

        // Create headers with X-User-Id
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", userId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Call User Service
        try {
            restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                request,
                UserInfo.class
            );
            log.info("Updated subscription for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to update user subscription: {}", e.getMessage());
            throw e;
        }
    }
}
```

---

## 📋 IMPLEMENTATION CHECKLIST

### Backend - User Service:

**Entity & Repository:**
- [ ] Add 9 subscription fields to UserInfo.java entity
- [ ] Add validation methods (setSubscriptionTier, setSubscriptionStatus, setPaymentMethod)
- [ ] No changes needed to UserInfoRepository (auto supports new fields)

**DTO:**
- [ ] Add subscription fields to UserInfoUpdateDTO.java
- [ ] No changes needed to response DTO (use entity directly)

**Controller:**
- [ ] Update PATCH /api/user-info to accept subscription fields
- [ ] Update GET /api/user-info/by-user-id response (auto includes new fields)
- [ ] Add validation for tier ("TRIAL" not allowed)
- [ ] Add validation for status ("TRIALING" not allowed)

**Database:**
- [ ] Create migration script to add fields with null defaults
- [ ] Create indexes for subscriptionTier, subscriptionStatus, subscriptionExpiresAt
- [ ] Run migration on dev environment
- [ ] Test migration rollback

**Testing:**
- [ ] Unit tests for validation (TRIAL tier rejected)
- [ ] Unit tests for validation (TRIALING status rejected)
- [ ] Integration test: PATCH /api/user-info with subscription fields
- [ ] Integration test: GET /api/user-info returns subscription fields
- [ ] Integration test: Program Service → User Service HTTP call

---

## 🎯 EXAMPLE USE CASES

### Use Case 1: User Upgrades from Trial to BASIC

**Step 1:** User completes payment on MoMo

**Step 2:** MoMo IPN callback → Program Service Lambda

**Step 3:** Program Service calls User Service:
```bash
curl -X PATCH "https://user-service/api/user-info" \
  -H "X-User-Id: 12345678-1234-1234-1234-123456789abc" \
  -H "Content-Type: application/json" \
  -d '{
    "subscriptionTier": "BASIC",
    "subscriptionStatus": "ACTIVE",
    "subscriptionExpiresAt": "2026-01-07T00:00:00Z",
    "paymentMethod": "MOMO",
    "lastPaymentId": "MOMO123456789",
    "lastPaymentDate": "2025-12-07T10:30:00Z",
    "lastPaymentAmount": 500000.0,
    "nextBillingDate": "2026-01-07T00:00:00Z",
    "autoRenewal": false
  }'
```

**Response:**
```json
{
  "id": "user-info-uuid",
  "userId": "12345678-1234-1234-1234-123456789abc",
  "fullName": "Nguyễn Văn A",
  "subscriptionTier": "BASIC",
  "subscriptionStatus": "ACTIVE",
  "subscriptionExpiresAt": "2026-01-07T00:00:00Z",
  "paymentMethod": "MOMO",
  "lastPaymentId": "MOMO123456789",
  "lastPaymentAmount": 500000.0
}
```

---

### Use Case 2: User Upgrades from BASIC to PREMIUM

**Program Service calls User Service:**
```bash
curl -X PATCH "https://user-service/api/user-info" \
  -H "X-User-Id: 12345678-1234-1234-1234-123456789abc" \
  -H "Content-Type: application/json" \
  -d '{
    "subscriptionTier": "PREMIUM",
    "lastPaymentId": "MOMO987654321",
    "lastPaymentDate": "2025-12-15T10:30:00Z",
    "lastPaymentAmount": 50000.0
  }'
```

**Response:** Updated with new tier and payment info

---

## ❌ ANTI-PATTERNS - KHÔNG LÀM

### ❌ WRONG: Lưu trial tier vào User Service
```java
// WRONG - DO NOT DO THIS
userInfo.setSubscriptionTier("TRIAL");  // ❌ Error: TRIAL not allowed
```

### ❌ WRONG: Lưu TRIALING status vào User Service
```java
// WRONG - DO NOT DO THIS
userInfo.setSubscriptionStatus("TRIALING");  // ❌ Error: TRIALING not allowed
```

### ✅ CORRECT: Chỉ lưu paid subscription
```java
// CORRECT
userInfo.setSubscriptionTier("BASIC");  // ✅ OK
userInfo.setSubscriptionStatus("ACTIVE");  // ✅ OK
```

---

## 📚 RELATED DOCUMENTATION

- [SUBSCRIPTION_ARCHITECTURE.md](./SUBSCRIPTION_ARCHITECTURE.md) - Overall architecture
- [SUBSCRIPTION_INTEGRATION_IMPLEMENTATION.md](./SUBSCRIPTION_INTEGRATION_IMPLEMENTATION.md) - Frontend implementation
- [SUBSCRIPTION_FIELD_MAPPING.md](./SUBSCRIPTION_FIELD_MAPPING.md) - Original field mapping

---

**Document Version:** 1.0.0
**Last Updated:** 2025-12-07
**Author:** Claude Code
**Status:** Ready for Backend Implementation
