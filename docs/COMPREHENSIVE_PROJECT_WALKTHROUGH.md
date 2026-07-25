# Enterprise Claims Center — Comprehensive System & Architecture Walkthrough

## Executive Summary

The **Enterprise Claims Center** is a distributed, event-driven insurance First Notice of Loss (FNOL) intake, rules adjudication, and saga financial settlement platform designed as a modern, high-performance alternative to Guidewire ClaimCenter.

Built with **Java 21**, **Spring Boot 3.3.2**, **Spring Cloud Gateway**, **Drools Rule Engine (v8.44.0)**, **Apache Kafka**, **Redis Cluster**, and **PostgreSQL**, this architecture processes claims through automated business rules, matches workload across adjuster specialist teams, and manages financial reserve ledgers in real-time.

---

## 1. System Architecture Diagram

```
                                  [ Google Chrome Browser ]
                                              │
                                              ▼ (Port 8090)
                          ┌───────────────────────────────────────┐
                          │          Spring Cloud Gateway         │
                          │   (LDAP Single Sign-On su/gw & UI)    │
                          └───────────────────┬───────────────────┘
                                              │
             ┌────────────────────────────────┼────────────────────────────────┐
             │                                │                                │
             ▼ (Port 8081)                    ▼ (Port 8082)                    ▼ (Port 8083 / 8084)
┌─────────────────────────┐      ┌─────────────────────────┐      ┌─────────────────────────┐
│ Policy Verification Svc │      │  FNOL Intake & Rules    │      │ Adjuster & Reserve Svc  │
│  (PostgreSQL & Redis)   │      │ (Drools Adjudication)   │      │   (Financial Ledger)    │
└─────────────────────────┘      └────────────┬────────────┘      └─────────────────────────┘
                                              │
                                              ▼
                                 ┌─────────────────────────┐
                                 │     Kafka Event Bus     │
                                 │ (claims.fnol.submitted) │
                                 └─────────────────────────┘
```

---

## 2. Infrastructure & Service Component Inventory

| Service / Container Component | Service Name | Protocol & Port | Primary Responsibilities |
| :--- | :--- | :--- | :--- |
| **API Gateway** | `api-gateway` | `HTTP :8090` | Web UI, LDAP Auth (`su`/`gw`), Rate Limiter, Static Assets, Health (`/api/v1/health`) |
| **Policy Verification Service** | `policy-service` | `HTTP :8081` | Policy validation, coverage check, active date verification, Redis cache |
| **FNOL Intake & Rules Engine** | `fnol-intake-service` | `HTTP :8082` | FNOL intake, Drools rules evaluation (Severity/Priority), Kafka event producer |
| **Adjuster Assignment Service** | `adjuster-assignment-service` | `HTTP :8083` | Kafka consumer, capacity radar, dynamic workload matching algorithm |
| **Reserve & Settlement Service** | `reserve-settlement-service` | `HTTP :8084` | Saga financial ledger, reserve creation, payout execution & tracking |
| **PostgreSQL Database** | `claims-postgres` | `TCP :5432` | Relational persistence for policy tables, adjuster workloads, reserve ledgers |
| **Redis Cache** | `claims-redis` | `TCP :6379` | High-speed policy lookup caching & API Gateway distributed rate-limiting |
| **Kafka Event Broker** | `claims-kafka-broker` | `TCP :9092` | Asynchronous event streaming across microservices (`claims.fnol.submitted`) |
| **Kafka Management Console** | `claims-kafka-ui` | `HTTP :8085` | Browser interface for topic inspection and event telemetry |

---

## 3. End-to-End Functional Capabilities

### 3.1. Active Directory / LDAP Single Sign-On (SSO)
- **Interface**: Glassmorphism Login Overlay (`#loginOverlay`).
- **Credentials**: Username `su`, Password `gw` (Guidewire Administrator Standard).
- **Directory Domain**: `CORP.GUIDEWIRE.LOCAL`.
- **Session Persistence**: Browser `sessionStorage` token management with active session pill (`su (Super User - Guidewire Admin)`) and Logout capability.

### 3.2. Policy Verification & Redis Caching
- As loss reports are created, policy lookup (`#policyNumber`) queries the Policy Service via Gateway.
- Validates active coverage periods, coverage types (Collision, Comprehensive, Property Damage), and deductible limits.
- Caches lookup results in Redis for sub-millisecond retrieval.

### 3.3. Drools Rule Engine Adjudication
- Executes business rules in `fnol-routing-rules.drl`:
  - **Catastrophic (CAT) Severity**: Triggered when loss amount exceeds threshold or bodily injuries are reported.
  - **High / Medium / Low Severity**: Evaluated against line of business (Auto, Property, General Liability) and police incident report status.
  - Calculates recommended financial reserve multipliers.

### 3.4. Kafka Event Streaming & Saga Workflow
- Upon successful adjudication, publishes `ClaimSubmittedEvent` to Kafka topic `claims.fnol.submitted`.
- The Adjuster Assignment Service consumes the event and auto-assigns the claim to the specialist with the lowest workload capacity percentage.
- The Reserve Settlement Service consumes the event and initializes the Saga financial reserve ledger.

### 3.5. Financial Payout Ledger Portal
- Displays active claims, initial reserve balance, remaining reserve, and total paid payouts.
- Interactive **Payout Execution Modal** enables claim payouts (Direct Deposit, Wire Transfer, Check) with real-time reserve deduction and audit logging.

### 3.6. Interactive System Telemetry & Health Monitoring
- Clicking the **Kafka Bus & Redis Cluster Active** header badge triggers an active health check via `/api/v1/health`.
- Displays real-time operational status for Redis, Kafka, and all 4 microservices.

---

## 4. Verification & Testing Summary

All modules have been compiled and verified with Maven:

```bash
mvn clean test
```

### Test Execution Highlights
- `Policy Verification Service`: 2/2 tests passed (`PolicyServiceTest`).
- `FNOL Intake & Rules Service`: 2/2 tests passed (`DroolsRulesTest`).
- Root Reactor Build: **100% BUILD SUCCESS** across all 7 POM modules.

---

## 5. How to Run & Access the Application

### 5.1. Start Infrastructure Dependencies
```bash
# Start Postgres, Redis, Kafka Broker, and Kafka UI
docker compose up -d
```

### 5.2. Launch Microservices
```bash
# Run Policy Service (Port 8081)
mvn spring-boot:run -pl policy-service

# Run FNOL Intake Service (Port 8082)
mvn spring-boot:run -pl fnol-intake-service

# Run Adjuster Service (Port 8083)
mvn spring-boot:run -pl adjuster-assignment-service

# Run Reserve Service (Port 8084)
mvn spring-boot:run -pl reserve-settlement-service

# Run API Gateway & Web Console (Port 8090)
mvn spring-boot:run -pl api-gateway
```

### 5.3. Web Browser Access
- **Enterprise Claims Console**: [http://localhost:8090](http://localhost:8090) (Login: `su` / `gw`)
- **Kafka Monitoring UI**: [http://localhost:8085](http://localhost:8085)

---

## 6. GitHub Repository Details

- **Remote URL**: [https://github.com/azeez1846/Spring_Claims](https://github.com/azeez1846/Spring_Claims)
- **Branch**: `main`
- **Latest Commit**: Included LDAP Single Sign-On, Gateway Rate Limiting Safety, System Health Telemetry, and Full Documentation.
