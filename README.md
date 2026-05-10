# Udemy Clone Backend

A RESTful backend API for an online learning platform built with Spring Boot. Supports course management, video uploads via MinIO object storage, user authentication with JWT, and enrollment tracking.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [Database Schema](#database-schema)
- [Authentication](#authentication)
- [File Storage](#file-storage)
- [Project Structure](#project-structure)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.6 |
| Language | Java 17+ |
| Database | PostgreSQL 16 |
| Object Storage | MinIO (S3-compatible) |
| Authentication | JWT (JJWT 0.11.5) |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct 1.6.3 |
| Build Tool | Gradle |
| Containerization | Docker Compose |

---

## Architecture Overview

```
Client
  │
  ▼
JwtAuthenticationFilter  ─── validates Bearer token, sets SecurityContext
  │
  ▼
Controllers (auth / course / module / video / profile)
  │
  ▼
Services  ───  Repositories (Spring Data JPA)  ───  PostgreSQL
  │
  ▼ (video upload)
MinIO Object Storage
```

All API responses follow a standard envelope:

```json
{
  "isError": false,
  "data": { ... },
  "message": "Success"
}
```

---

## Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle (or use the included `./gradlew` wrapper)

---

## Getting Started

### 1. Clone the repository

```bash
git clone <repo-url>
cd udemy-backend
```

### 2. Start infrastructure services

Starts PostgreSQL and MinIO in the background:

```bash
docker-compose up -d
```

| Service | Host | Port |
|---|---|---|
| PostgreSQL | localhost | 5432 |
| MinIO API | localhost | 9005 |
| MinIO Console | localhost | 9001 |

### 3. Create the MinIO bucket

Open the MinIO web console at `http://localhost:9001` and log in with:

- **Username:** `admin`
- **Password:** `udemyminioAdmin`

Create a bucket named `course-videos`.

### 4. Run the application

```bash
./gradlew bootRun
```

The server starts on **port 9000**. Hibernate will auto-create the schema on first run (`spring.jpa.hibernate.ddl-auto=update`).

---

## Configuration

All settings live in `src/main/resources/application.properties`.

```properties
# Server
server.port=9000

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/udemy_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# MinIO
minio.endpoint=http://localhost:9005
minio.accessKey=admin
minio.secretKey=udemyminioAdmin
minio.bucketName=course-videos

# File upload limits
spring.servlet.multipart.max-file-size=1024MB
spring.servlet.multipart.max-request-size=1024MB
```

> **Security note:** The JWT secret is currently hardcoded in `AuthConstants.java`. Move it to an environment variable or secrets manager before deploying to production.

---

## API Reference

Base URL: `http://localhost:9000/api/v1`

### Authentication

#### Register

```
POST /auth/register
```

**Request body:**

```json
{
  "email": "user@example.com",
  "password": "secret",
  "role": "USER"
}
```

**Response:**

```json
{
  "isError": false,
  "data": {
    "token": "<jwt>"
  },
  "message": "Registered successfully"
}
```

---

#### Login

```
POST /auth/login
```

**Request body:**

```json
{
  "email": "user@example.com",
  "password": "secret"
}
```

**Response:** Same shape as register — returns a JWT token.

---

### Courses

All course endpoints require the `Authorization: Bearer <token>` header unless stated otherwise.

#### Create course

```
POST /courses/create
```

**Request body:**

```json
{
  "title": "Spring Boot Mastery",
  "description": "Learn Spring Boot from scratch",
  "price": 19.99,
  "author": "John Doe",
  "tags": ["java", "backend", "spring"]
}
```

#### Get course

```
GET /courses/{courseId}
```

#### Update course

```
PUT /courses/update/{courseId}
```

Request body has the same shape as create.

---

### Modules

#### Create module

```
POST /module/create
```

**Request body:**

```json
{
  "title": "Introduction",
  "orderIndex": 1,
  "courseId": 42
}
```

---

### Videos

#### Upload video

```
POST /video/upload
Content-Type: multipart/form-data
```

**Form fields:**

| Field | Type | Description |
|---|---|---|
| `file` | File | Video file (max 1024 MB) |
| `videoRequestDto` | JSON part | `{ "title": "Lesson 1", "orderIndex": 1, "moduleId": 5 }` |

**Response includes a MinIO presigned URL for playback.**

#### Get video

```
GET /video/{videoId}
```

#### Delete video

```
DELETE /video/{videoId}
```

---

### Profile

#### Create profile

```
POST /profile/create
```

Requires `USER` role.

**Request body:**

```json
{
  "name": "Jane Doe",
  "age": 28,
  "profession": "Software Engineer",
  "bio": "Passionate about learning"
}
```

#### Get profile

```
GET /profile/me/{profileId}
```

Requires `USER` or `ADMIN` role.

---

## Database Schema

```
users
├── id          BIGINT PK
├── email       VARCHAR UNIQUE NOT NULL
├── password    VARCHAR NOT NULL
└── role        ENUM(ADMIN, USER)

profiles
├── id          BIGINT PK FK → users.id
├── name        VARCHAR
├── age         INTEGER
├── profession  VARCHAR
└── bio         VARCHAR

courses
├── id          BIGINT PK
├── title       VARCHAR
├── description VARCHAR
├── price       DOUBLE
├── author      VARCHAR
└── tags        (element collection)

modules
├── id          BIGINT PK
├── title       VARCHAR
├── orderIndex  INTEGER
└── course_id   BIGINT FK → courses.id

videos
├── id          BIGINT PK
├── title       VARCHAR
├── minioUrl    TEXT
├── orderIndex  INTEGER
└── module_id   BIGINT FK → modules.id

enrollments
├── id             BIGINT PK
├── user_id        BIGINT FK → users.id
├── course_id      BIGINT FK → courses.id
├── enrolledAt     TIMESTAMP
├── startedAt      TIMESTAMP
├── completedAt    TIMESTAMP
└── progressStatus ENUM(NOT_STARTED, IN_PROGRESS, COMPLETED)
```

---

## Authentication

The API uses stateless JWT authentication.

1. Call `/auth/register` or `/auth/login` to receive a token.
2. Include the token in every subsequent request:
   ```
   Authorization: Bearer <token>
   ```
3. Tokens are valid for **10 hours** and signed with HS256.

Role-based access is enforced via `@PreAuthorize` annotations on controller methods. Two roles exist: `USER` and `ADMIN`.

---

## File Storage

Videos are stored in MinIO, an S3-compatible object store. On upload, the backend:

1. Streams the file to the `course-videos` bucket in MinIO.
2. Generates a presigned URL and persists it in the `videos.minioUrl` column.
3. Returns the presigned URL in the response so the client can stream the video directly.

---

## Project Structure

```
src/main/java/com/avijeet/udemybackend/
├── config/
│   ├── auth/           # JWT authentication filter
│   ├── minio/          # MinIO client bean
│   └── security/       # Spring Security configuration
├── controllers/        # REST controllers (auth, course, module, video, profile)
├── dto/                # Request / response DTOs
├── entities/           # JPA entities
├── enums/              # Role, ProgressStatus
├── exceptions/         # Global exception handler + domain exceptions
├── mapper/             # MapStruct mappers (entity ↔ DTO)
├── repository/         # Spring Data JPA repositories
├── service/            # Business logic
└── utils/
    ├── api/            # ApiResponse, ApiRoutes, BaseController
    └── constants/      # ApiConstants, AuthConstants
```
