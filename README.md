
# Finance Management REST API

This project is a RESTful API built with Spring Boot for managing users and their financial transactions. It supports basic CRUD operations, transaction filtering by date, pagination, validation, and structured error handling.

The API was developed as part of a Master's coursework project and follows a layered architecture (Controller → Service → Repository).

---

## Features

* Create, update and delete users
* Add, update, and delete transactions
* Retrieve transactions with:
  * Date range filtering
  * Pagination support
* Monthly budget validation
* Structured error responses
* DTO-based request/response handling
* Global exception handling

---

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* H2
* Maven

---

## API Endpoints

### Users

* `POST /api/users` – Create user
* `PUT /api/users/{userId}` – Update user
* `DELETE /api/users/{userId}` – Delete user

### Transactions

* `POST /api/users/{userId}/transactions` – Add transaction
* `PUT /api/users/{userId}/transactions/{transactionId}` – Update transaction
* `DELETE /api/users/{userId}/transactions/{transactionId}` – Delete transaction
* `GET /api/users/{userId}/transactions` – Get transactions (supports pagination and date filtering)

---

## Pagination

Transactions endpoint supports:

* `page` – Page index (starts at 0)
* `size` – Number of records per page
* `startDate` – Filter start date (YYYY-MM-DD)
* `endDate` – Filter end date (YYYY-MM-DD)

Example:

```
GET /api/users/1/transactions?startDate=2026-01-01&endDate=2026-12-31&page=0&size=5
```

---

## Error Handling

The API implements global exception handling with consistent JSON error responses including:

* HTTP status code
* Error message
* Timestamp

Validation errors return `400 Bad Request`.
Missing resources return `404 Not Found`.

---

## Running the Project

1. Clone the repository
2. Run using SpringToolsForEclipse IDE 
3. Run as Spring Boot App
3. The API will start on:

   ```
   http://localhost:8080
   ```

