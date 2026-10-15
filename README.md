# Anurag University Bus Tracking Backend

## Stack and features
- Java 17+, Spring Boot 3, Spring Data JPA, MySQL, WebSockets
- `GET /api/routes`, `GET /api/buses`, and `GET /api/alerts`
- `POST /api/auth/login` and `POST /api/auth/signup` (student registration only)
- `POST /api/buses/{id}/location` accepts GPS only from an authenticated driver assigned to that bus
- Admin-only simulation controls; BCrypt password hashing
- Three seeded demo routes, 18 stops, and buses B001–B007

## MySQL setup
1. In MySQL, run `CREATE DATABASE BusTracking;`.
2. Configure `DB_USERNAME` and `DB_PASSWORD` environment variables, or edit `application.properties` for your local MySQL credentials.
3. Start the backend from this folder using `mvn spring-boot:run` (Java 17+).
4. JPA creates/updates tables in `BusTracking` on startup. Do not use `ddl-auto=update` as a production migration strategy.

Default local settings expect `root` with an empty password. If your MySQL user has a password, set `DB_PASSWORD` before starting. You can also override the URL with `DB_URL`.

## Demo accounts
- Admin: `admin@anurag.edu.in` / `Admin@123`
- Driver B001: `driver@anurag.edu.in` / `Driver@123`
- Driver B006: `driver006@anurag.edu.in` / `Driver006@123`
- Driver B007: `driver007@anurag.edu.in` / `Driver007@123`
- Students register from the login screen. They can optionally enter a bus ID such as B001, B006, or B007, or the vehicle registration.

Change seeded passwords and implement admin-managed account provisioning before production. Session tokens are held in memory and expire when the backend restarts; use a production identity/session solution before public deployment.

## GPS notes
Browser GPS needs HTTPS on a phone (localhost works during local development). Enable location sharing on the assigned driver's device and grant permission. The browser reports the best available device fix; no software can guarantee perfect GPS accuracy. The map reports GPS accuracy and last update time. Route coordinates are approximate demo pins and must be verified by Anurag University transport staff before actual use.
