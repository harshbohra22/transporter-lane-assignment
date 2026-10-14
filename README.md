# Transporter Assignment on Lanes Optimization Service

The service solves the **Transporter Assignment on Lanes** procurement problem by optimizing the allocation of transporters across Indian freight lanes under cost minimization and carrier participation constraints.

---

## 📌 Objectives & Problem Formulation

1. **Cost Minimization**: Finds the optimal carrier allocation that minimizes total freight procurement cost across all lanes.
2. **Maximize Transporter Usage**: Utilizes up to the user-specified `maxTransporters` limit to distribute risk and maintain carrier engagement.
3. **Full Lane Coverage**: Enforces that every single lane is serviced by at least one quoting transporter.
4. **Graceful Handling of Edge Cases**: Validates impossible assignments, unserviced lanes, and constraint violations with structured HTTP responses.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java 17 (LTS)
- **Framework**: Spring Boot 3.3.5
- **Persistence**: Spring Data JPA, Hibernate ORM
- **Database**: H2 In-Memory (Zero-config local development & testing) / PostgreSQL compatible
- **API Documentation**: SpringDoc OpenAPI 3.0 (Interactive Swagger UI)
- **Design Patterns**:
  - **Strategy Design Pattern**: Pluggable optimization engines (`ExactOptimizationStrategy` using combinatorial branch exploration and `GreedyOptimizationStrategy` for large-scale heuristic allocations).
  - **Repository Pattern & DTO Pattern**: Clean separation of domain entities and REST contracts.
- **Testing**: JUnit 5, Spring Boot Test, MockMvc (100% test pass rate)
- **Build Tool**: Maven Wrapper (`mvnw` / `mvnw.cmd`)
- **Containerization**: Multi-stage Docker build

---

## 🚀 Getting Started

### Prerequisites
- Java 17 JDK installed
- Git

### Run Locally

1. Clone or navigate to the project directory:
   ```bash
   cd freightfox-transporter-assignment
   ```

2. Run tests to verify the setup:
   ```bash
   # On Windows:
   .\mvnw.cmd test

   # On Linux/macOS:
   ./mvnw test
   ```

3. Start the application:
   ```bash
   # On Windows:
   .\mvnw.cmd spring-boot:run

   # On Linux/macOS:
   ./mvnw spring-boot:run
   ```

The application will start on port `8080`.

---

## 📖 API Documentation & Swagger UI

Once the application is running, access the interactive Swagger UI:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:freightfoxdb`, Username: `sa`, Password: empty)

---

## 📡 API Specification & cURL Examples

### 1. Submit Input Data
Stores and upserts freight lanes and transporter quote bids.

- **Endpoint**: `POST /api/v1/transporters/input`
- **Headers**: `Content-Type: application/json`

**Sample Request**:
```bash
curl -X POST "http://localhost:8080/api/v1/transporters/input" \
  -H "Content-Type: application/json" \
  -d '{
    "lanes": [
      {"id": 1, "origin": "Mumbai", "destination": "Delhi"},
      {"id": 2, "origin": "Delhi", "destination": "Bangalore"},
      {"id": 3, "origin": "Chennai", "destination": "Kolkata"},
      {"id": 4, "origin": "Pune", "destination": "Hyderabad"},
      {"id": 5, "origin": "Ahmedabad", "destination": "Jaipur"}
    ],
    "transporters": [
      {
        "id": 1,
        "name": "Transporter T1",
        "laneQuotes": [
          {"laneId": 1, "quote": 20835},
          {"laneId": 2, "quote": 10512},
          {"laneId": 3, "quote": 22105},
          {"laneId": 4, "quote": 42481},
          {"laneId": 5, "quote": 19862}
        ]
      },
      {
        "id": 2,
        "name": "Transporter T2",
        "laneQuotes": [
          {"laneId": 1, "quote": 48844},
          {"laneId": 2, "quote": 31326},
          {"laneId": 3, "quote": 18640},
          {"laneId": 4, "quote": 45828},
          {"laneId": 5, "quote": 18297}
        ]
      },
      {
        "id": 3,
        "name": "Transporter T3",
        "laneQuotes": [
          {"laneId": 1, "quote": 39020},
          {"laneId": 2, "quote": 20648},
          {"laneId": 3, "quote": 31438},
          {"laneId": 4, "quote": 36447},
          {"laneId": 5, "quote": 12789}
        ]
      },
      {
        "id": 4,
        "name": "Transporter T4",
        "laneQuotes": [
          {"laneId": 1, "quote": 14400},
          {"laneId": 2, "quote": 44514},
          {"laneId": 3, "quote": 14316},
          {"laneId": 4, "quote": 10678},
          {"laneId": 5, "quote": 13032}
        ]
      },
      {
        "id": 5,
        "name": "Transporter T5",
        "laneQuotes": [
          {"laneId": 1, "quote": 11601},
          {"laneId": 2, "quote": 19760},
          {"laneId": 3, "quote": 40870},
          {"laneId": 4, "quote": 20635},
          {"laneId": 5, "quote": 26421}
        ]
      },
      {
        "id": 6,
        "name": "Transporter T6",
        "laneQuotes": [
          {"laneId": 1, "quote": 35095},
          {"laneId": 2, "quote": 12494},
          {"laneId": 3, "quote": 17808},
          {"laneId": 4, "quote": 36210},
          {"laneId": 5, "quote": 39444}
        ]
      },
      {
        "id": 7,
        "name": "Transporter T7",
        "laneQuotes": [
          {"laneId": 1, "quote": 26070},
          {"laneId": 2, "quote": 41098},
          {"laneId": 3, "quote": 20932},
          {"laneId": 4, "quote": 16897},
          {"laneId": 5, "quote": 27938}
        ]
      }
    ]
  }'
```

**Sample Response**:
```json
{
  "status": "success",
  "message": "Input data saved successfully."
}
```

---

### 2. Get Optimized Results
Calculates the minimum cost allocation of transporters to lanes adhering to `maxTransporters`.

- **Endpoint**: `POST /api/v1/transporters/assignment`
- **Headers**: `Content-Type: application/json`

**Sample Request**:
```bash
curl -X POST "http://localhost:8080/api/v1/transporters/assignment" \
  -H "Content-Type: application/json" \
  -d '{
    "maxTransporters": 3
  }'
```

**Sample Response**:
```json
{
  "status": "success",
  "totalCost": 60139.0,
  "assignments": [
    {"laneId": 1, "transporterId": 5},
    {"laneId": 2, "transporterId": 1},
    {"laneId": 3, "transporterId": 4},
    {"laneId": 4, "transporterId": 4},
    {"laneId": 5, "transporterId": 4}
  ],
  "selectedTransporters": [1, 4, 5]
}
```

---

## 🧪 Test Cases & Verification

The suite includes tests for:
1. **Test Case 1**: 5 lanes and 7 transporters with diverse quotes.
2. **Test Case 2**: Restricted quotes scenario verifying essential transporter discovery.
3. **Infeasible Scenarios**: Insufficient transporter limit (`maxTransporters` lower than mandatory carrier count).
4. **Validation Failures**: Invalid inputs, negative quotes, missing payloads.

Execute tests via:
```bash
.\mvnw.cmd test
```

---

## 🐳 Docker & Cloud Deployment

### Build and Run with Docker
```bash
docker build -t freightfox-transporter-assignment .
docker run -p 8080:8080 freightfox-transporter-assignment
```

### Free Deployment on Render / Railway
1. Push this repository to GitHub.
2. Connect the repo on [Render](https://render.com) as a **Web Service**.
3. Select **Docker** environment or **Java 17 Maven**.
4. The service will be live with Swagger docs accessible publicly.
