# 🛒 My Store API

A robust, secure, and extensible RESTful API for managing a modern store, built with Java 17, Spring Boot 3, and
MySQL.  
Supports inventory, orders, carts, user management, authentication, and Stripe payments.

---

## Table of Contents

1. [Features & Capabilities](#features--capabilities)
2. [Architecture Overview](#architecture-overview)
3. [API Documentation](#api-documentation)
4. [Installation & Setup](#installation--setup)
5. [Configuration](#configuration)
6. [Usage Examples](#usage-examples)
7. [Deployment](#deployment)

---

## Features & Capabilities

### Business Features

- **User Management**: Registration, profile, password change, role-based access (USER, ADMIN)
- **Product Catalog**: CRUD for products and categories, category filtering
- **Cart Management**: Create cart, add/update/remove items, view cart
- **Order Processing**: Place orders from cart, view order history, order details
- **Wishlist**: Add/remove products to user wishlists
- **Address & Profile**: Manage addresses and user profiles
- **Admin Panel**: Admin-only endpoints for management

### Technical Capabilities

- **JWT Authentication**: Secure, stateless authentication with access/refresh tokens
- **Role-Based Authorization**: Fine-grained access control for endpoints
- **Input Validation**: Strong validation on all DTOs and requests
- **Error Handling**: Consistent, structured error responses
- **Logging**: Request/response logging filter
- **Database Migrations**: Managed with Flyway
- **Stripe Integration**: Secure payment processing and webhook handling
- **Environment Profiles**: Separate dev/prod configs
- **Extensible Security**: Modular security rules per feature

---

## Architecture Overview

### System Design

- **Spring Boot 3** application, modularized by domain (users, products, carts, orders, payments, etc.)
- **RESTful API** with stateless JWT authentication
- **MySQL** as the primary database, managed via JPA and Flyway
- **Stripe** for payment processing

### Technology Stack

| Layer      | Technology                     |
|------------|--------------------------------|
| Language   | Java 17                        |
| Framework  | Spring Boot 3, Spring Security |
| Database   | MySQL, JPA (Hibernate)         |
| Migrations | Flyway                         |
| Payments   | Stripe Java SDK                |
| Build      | Maven                          |
| Validation | Jakarta Validation             |
| Mapping    | MapStruct, Lombok              |

### Database Schema Overview
![store_api.png](https://github.com/AymanHazem/My-Store-API/blob/main/Schema.png?raw=true)

> See `src/main/resources/db/migration/` for full DDL and sample data.

### Key Design Patterns

- **Controller-Service-Repository** separation
- **DTOs and Mappers** for API payloads
- **Global Exception Handling** via `@ControllerAdvice`
- **Security Rules** as modular Spring components
- **Strategy Pattern** for payment gateway abstraction

---

## API Documentation


### Authentication

- **JWT Bearer Token** in `Authorization` header for most endpoints
- **Refresh Token** via secure HTTP-only cookie
- **Role-based access**: Some endpoints require `ADMIN` role

### Endpoints Overview

#### Auth

| Method | Path            | Description              | Auth Required | Body/Params         |
|--------|-----------------|--------------------------|---------------|---------------------|
| POST   | `/auth/login`   | Login, returns JWT       | No            | email, password     |
| POST   | `/auth/refresh` | Refresh access token     | Cookie        | refreshToken cookie |
| GET    | `/auth/me`      | Get current user profile | Yes           | -                   |

#### Users

| Method | Path                          | Description                | Auth Required | Role       |
|--------|-------------------------------|----------------------------|---------------|------------|
| POST   | `/users`                      | Register new user          | No            | -          |
| GET    | `/users`                      | List users (optional sort) | Yes           | ADMIN      |
| GET    | `/users/{id}`                 | Get user by ID             | Yes           | USER/ADMIN |
| PUT    | `/users/{id}`                 | Update user                | Yes           | USER/ADMIN |
| DELETE | `/users/{id}`                 | Delete user                | Yes           | USER/ADMIN |
| POST   | `/users/{id}/change-password` | Change password            | Yes           | USER/ADMIN |

#### Products

| Method | Path             | Description                | Auth Required | Role  |
|--------|------------------|----------------------------|---------------|-------|
| GET    | `/products`      | List products (filterable) | No            | -     |
| GET    | `/products/{id}` | Get product by ID          | No            | -     |
| POST   | `/products`      | Add product                | Yes           | ADMIN |
| PUT    | `/products/{id}` | Update product             | Yes           | ADMIN |
| DELETE | `/products/{id}` | Delete product             | Yes           | ADMIN |

#### Carts

| Method | Path                                | Description          | Auth Required | Role |
|--------|-------------------------------------|----------------------|---------------|------|
| POST   | `/carts`                            | Create new cart      | No            | -    |
| GET    | `/carts/{cartId}`                   | Get cart by ID       | No            | -    |
| POST   | `/carts/{cartId}/items`             | Add item to cart     | No            | -    |
| PUT    | `/carts/{cartId}/items/{productId}` | Update item quantity | No            | -    |

#### Orders

| Method | Path                | Description                | Auth Required | Role |
|--------|---------------------|----------------------------|---------------|------|
| GET    | `/orders`           | List current user's orders | Yes           | USER |
| GET    | `/orders/{orderId}` | Get order details          | Yes           | USER |

#### Checkout / Payments

| Method | Path                | Description             | Auth Required | Role |
|--------|---------------------|-------------------------|---------------|------|
| POST   | `/checkout`         | Create Stripe checkout  | Yes           | USER |
| POST   | `/checkout/webhook` | Stripe webhook endpoint | No            | -    |

#### Admin

| Method | Path        | Description         | Auth Required | Role  |
|--------|-------------|---------------------|---------------|-------|
| GET    | `/admin/**` | Admin test endpoint | Yes           | ADMIN |


### Error Handling

- **400 Bad Request**: Validation errors, missing/invalid fields
- **401 Unauthorized**: Missing/invalid JWT
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **422 Unprocessable Entity**: Business rule violations
- **500 Internal Server Error**: Payment or server errors

## Example Requests & Responses

### Auth

#### POST `/auth/login`
**Request**
```json
{
  "email": "jane@example.com",
  "password": "securePassword123"
}
```
**Response**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST `/auth/refresh`
**Request**
- No body. Requires `refreshToken` cookie.

**Response**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### GET `/auth/me`
**Response**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "user@example.com"
}
```

---

### Users

#### POST `/users`
**Request**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "securePassword123"
}
```
**Response**
```json
{
  "id": 2,
  "name": "Jane Doe",
  "email": "jane@example.com"
}
```

#### GET `/users`
**Response**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com"
  },
  {
    "id": 2,
    "name": "Jane Doe",
    "email": "jane@example.com"
  }
]
```

#### GET `/users/{id}`
**Response**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "user@example.com"
}
```

#### PUT `/users/{id}`
**Request**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```
**Response**
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com"
}
```

#### DELETE `/users/{id}`
**Response**
- HTTP 204 No Content

#### POST `/users/{id}/change-password`
**Request**
```json
{
  "oldPassword": "oldPass123",
  "newPassword": "newPass456"
}
```
**Response**
- HTTP 200 OK (no body)

---

### Products

#### GET `/products`
**Response**
```json
[
  {
    "id": 1,
    "name": "Bananas",
    "description": "Fresh organic bananas, sold per pound.",
    "price": 1.99,
    "categoryId": 1
  }
]
```

#### GET `/products/{id}`
**Response**
```json
{
  "id": 1,
  "name": "Bananas",
  "description": "Fresh organic bananas, sold per pound.",
  "price": 1.99,
  "categoryId": 1
}
```

#### POST `/products`
**Request**
```json
{
  "name": "Apple",
  "description": "Fresh apple",
  "price": 1.99,
  "categoryId": 1
}
```
**Response**
```json
{
  "id": 2,
  "name": "Apple",
  "description": "Fresh apple",
  "price": 1.99,
  "categoryId": 1
}
```

#### PUT `/products/{id}`
**Request**
```json
{
  "name": "Apple Updated",
  "description": "Fresh green apple",
  "price": 2.49,
  "categoryId": 1
}
```
**Response**
```json
{
  "id": 2,
  "name": "Apple Updated",
  "description": "Fresh green apple",
  "price": 2.49,
  "categoryId": 1
}
```

#### DELETE `/products/{id}`
**Response**
- HTTP 204 No Content

---

### Carts

#### POST `/carts`
**Response**
```json
{
  "id": "b3b7c7e2-8c2e-4e2a-9c2e-8c2e4e2a9c2e",
  "dateCreated": "2024-07-01",
  "items": []
}
```

#### POST `/carts/{cartId}/items`
**Request**
```json
{
  "productId": 1
}
```
**Response**
```json
{
  "id": 1,
  "productId": 1,
  "quantity": 1
}
```

#### GET `/carts/{cartId}`
**Response**
```json
{
  "id": "b3b7c7e2-8c2e-4e2a-9c2e-8c2e4e2a9c2e",
  "dateCreated": "2024-07-01",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 1
    }
  ]
}
```

#### PUT `/carts/{cartId}/items/{productId}`
**Request**
```json
{
  "quantity": 3
}
```
**Response**
```json
{
  "id": 1,
  "productId": 1,
  "quantity": 3
}
```

---

### Orders

#### GET `/orders`
**Response**
```json
[
  {
    "id": 1,
    "status": "PAID",
    "createdAt": "2024-07-01T12:00:00",
    "items": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "unitPrice": 1.99,
        "quantity": 2,
        "totalPrice": 3.98
      }
    ],
    "totalPrice": 3.98
  }
]
```

#### GET `/orders/{orderId}`
**Response**
```json
{
  "id": 1,
  "status": "PAID",
  "createdAt": "2024-07-01T12:00:00",
  "items": [
    {
      "id": 1,
      "orderId": 1,
      "productId": 1,
      "unitPrice": 1.99,
      "quantity": 2,
      "totalPrice": 3.98
    }
  ],
  "totalPrice": 3.98
}
```

---

### Checkout / Payments

#### POST `/checkout`
**Request**
```json
{
  "cartId": "b3b7c7e2-8c2e-4e2a-9c2e-8c2e4e2a9c2e"
}
```
**Response**
```json
{
  "orderId": 1,
  "checkoutUrl": "https://checkout.stripe.com/pay/cs_test_..."
}
```

#### POST `/checkout/webhook`
**Request**
- Stripe webhook payload (raw JSON, varies by event type)

**Response**
- HTTP 200 OK 


---

### Error Example

**Validation Error**

All errors return a structured JSON body, e.g.:

```json
{
  "error": "Invalid Request Body."
}
```

---

## Installation & Setup

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+
- Stripe account (for payments)

### Local Development Setup

1. **Clone the repository**
   ```sh
   git clone https://github.com/your-repo/my-store-api.git
   cd my-store-api
   ```

2. **Configure Environment Variables**

   Copy `.env.example` to `.env` and fill in secrets:
   ```
   JWT_SECRET=your_jwt_secret
   STRIPE_SECRET_KEY=your_stripe_secret
   STRIPE_WEBHOOK_SECRET_KEY=your_stripe_webhook_secret
   ```

3. **Configure Database**

    - Ensure MySQL is running.
    - The default dev config uses:
      ```
      spring.datasource.url=jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true
      spring.datasource.username=root
      spring.datasource.password=root
      ```
    - Update `src/main/resources/application-dev.yaml` as needed.

4. **Run Database Migrations**

   Flyway will auto-run on app startup, or run manually:
   ```sh
   ./mvnw flyway:migrate
   ```

5. **Build and Run the Application**

   ```sh
   ./mvnw spring-boot:run
   ```


---

## Configuration

### application.yaml

- **spring.application.name**: App name
- **spring.jwt.secret**: JWT signing secret (from `.env`)
- **spring.jwt.accessTokenExpiration**: Access token TTL (seconds)
- **spring.jwt.refreshTokenExpiration**: Refresh token TTL (seconds)
- **spring.profiles.active**: Active profile (`dev` or `prod`)
- **stripe.secretkey**: Stripe API key (from `.env`)
- **stripe.webhookSecretkey**: Stripe webhook secret (from `.env`)

### application-dev.yaml

- **spring.datasource.url**: MySQL connection string
- **spring.datasource.username/password**: DB credentials
- **spring.jpa.show-sql**: SQL logging
- **webSiteUrl**: Base URL for Stripe redirects

### application-prod.yaml

- **spring.datasource.url**: Use environment variable for production DB
- **webSiteUrl**: Production site URL

### Security

- **JWT**: All protected endpoints require `Authorization: Bearer <token>`
- **Role-based access**: See SecurityRules classes for endpoint restrictions
- **CSRF**: Disabled (stateless API)
- **Password encoding**: BCrypt

### External Services

- **Stripe**: Configured via `StripeConfig`, keys from environment

---

## Usage Examples

### Authentication

**Login**

```sh
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"yourpassword"}'
```

**Refresh Token**

```sh
curl -X POST http://localhost:8080/auth/refresh \
  --cookie "refreshToken=<refresh_token>"
```

### Products

**List Products**

```sh
curl http://localhost:8080/products
```

**Add Product (Admin)**

```sh
curl -X POST http://localhost:8080/products \
  -H "Authorization: Bearer <admin_jwt>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Apple","description":"Fresh apple","price":1.99,"categoryId":1}'
```

### Cart

**Create Cart**

```sh
curl -X POST http://localhost:8080/carts
```

**Add Item to Cart**

```sh
curl -X POST http://localhost:8080/carts/<cartId>/items \
  -H "Content-Type: application/json" \
  -d '{"productId": 1}'
```

### Checkout

**Create Checkout Session**

```sh
curl -X POST http://localhost:8080/checkout \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{"cartId":"<cart-uuid>"}'
```

---

## Deployment
   - This API Allready Deployed On Railway

### Production Deployment

1. **Build the JAR**
   ```sh
   ./mvnw clean package
   ```

2. **Set Environment Variables**
    - Use `.env` or environment variables for secrets and DB config

3. **Run the JAR**
   ```sh
   java -jar target/my-store-api-1.0.0.jar
   ```
