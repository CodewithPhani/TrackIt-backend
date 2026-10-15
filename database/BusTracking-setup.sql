-- Run this script in MySQL Workbench or the MySQL command line.
CREATE DATABASE IF NOT EXISTS BusTracking
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Spring Boot / Hibernate creates and updates the application tables on startup.
-- For local development, configure DB_USERNAME and DB_PASSWORD environment variables.
