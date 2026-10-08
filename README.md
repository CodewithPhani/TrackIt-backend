# SkyLine Transit - Java Spring Boot Backend

Complete Java Spring Boot backend for the Bus Tracking System.

## Features
- **REST APIs**:
  - `GET /api/routes` - Get all bus routes and stop coordinates
  - `GET /api/routes/{id}` - Get specific bus route by ID
  - `GET /api/buses` - Get live tracking status for all buses
  - `GET /api/buses/{id}` - Get status for a specific bus
  - `POST /api/buses/{id}/incident` - Report delay/incident for a bus
  - `GET /api/analytics` - Fleet KPIs (Active fleet, On-time %, Passengers, Alerts)
  - `GET /api/alerts` - Recent incident alerts
  - `POST /api/simulation/speed` - Change simulation speed
  - `POST /api/simulation/toggle` - Pause / Play simulation
- **Real-Time WebSockets**:
  - STOMP WebSocket Broker on `/ws-bus-tracking`
  - Broadcasts live vehicle telemetry every 2 seconds on `/topic/bus-locations`
- **Database & H2 Console**:
  - Persistent H2 In-Memory Database
  - Pre-seeded with 3 major routes, 12 stops, and active bus fleet
  - H2 Web Console accessible at `http://localhost:8080/h2-console`
- **CORS Configured**:
  - Pre-configured to allow frontend access from `http://localhost:5173` or any origin.

## How to Run

### Option 1: Double-click `start-backend.bat`
Run `start-backend.bat` located in the root project folder.

### Option 2: Command Line
```bash
cd backend
java -cp "bin;lib/*" com.skylinetransit.BusTrackingApplication
```
or with Maven:
```bash
cd backend
mvn spring-boot:run
```
