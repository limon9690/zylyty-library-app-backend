# Library Management System

## Overview
The Library Management System is a web-based application built using Spring Boot. It allows users to borrow and return books, as well as view their borrowing history. Administrators can manage the inventory of books and monitor user activities.

## Features
- **User Management**: Register, authenticate, and manage users.
- **Book Borrowing**: Users can borrow books, ensuring no conflicts when the book is already borrowed.
- **Borrowing Limits**: Enforces borrowing rules (e.g., max two books per user).
- **Borrowing History**: View borrowing records and their statuses.
- **Return Management**: Mark books as returned.
- **Admin Operations**: Manage books and monitor activities.

---

## Technology Stack
- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **ORM**: Hibernate (JPA)
- **API Format**: RESTful APIs
- **Build Tool**: Maven
- **Authentication**: Session-based authentication
- **Environment Configuration**: Spring properties for dynamic environment setup
- **Containerization**: Docker

---


## Endpoints

### User Endpoints
| **Method** | **Endpoint**     | **Description**                 | **Authorization** |
|------------|------------------|---------------------------------|-------------------|
| `POST`     | `/register`      | Register a new user             | Public            |
| `POST`     | `/login`         | Authenticate a user             | Public            |


### Book Endpoints
| **Method** | **Endpoint**         | **Description**                          | **Authorization** |
|------------|----------------------|------------------------------------------|-------------------|
| `POST`     | `/books`             | Add a new book                           | Admin             |
| `GET`      | `/books`             | List all available books (sorted order)  | Authenticated     |
| `PUT`      | `/books/{isbn}`      | Update details of a specific book        | Admin             |
| `DELETE`   | `/books/{isbn}`      | Delete a specific book                   | Admin             |

### Borrowing Endpoints
| **Method** | **Endpoint**         | **Description**                       | **Authorization** |
|------------|----------------------|---------------------------------------|-------------------|
| `POST`     | `/borrow`            | Borrow a book by ISBN                 | Authenticated     |
| `GET`      | `/borrowed`          | View borrowing history of the user    | Authenticated     |
| `DELETE`   | `/return`            | Return a borrowed book by ISBN        | Authenticated     |

---

## Environment Setup

### Prerequisites
- Java 17+
- PostgreSQL database
- Maven
- Docker (optional, for containerized deployment)

### Configuration
Set the following environment variables in a `.env` file or configure them in your deployment environment:

```properties
# Application
spring.application.name=library-app
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
admin.api.key={admin-api-key}

# File Upload
spring.servlet.multipart.max-request-size=1MB
spring.servlet.multipart.max-file-size=1MB
