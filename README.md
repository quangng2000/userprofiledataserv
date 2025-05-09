# User Profile Data Service

This is a multi-module Maven project implementing a clean architecture approach for a user profile data service.

## Project Structure

The project consists of two main modules:

1. **userprofiledataservAPI** - The API specification module
   * Contains OpenAPI specifications
   * Generates client and server code from API definitions
   * Provides interfaces that enforce the contract between clients and services

2. **userprofiledataservService** - The service implementation module
   * Implements the business logic
   * Connects to databases (MongoDB, H2)
   * Includes Spring Security, JWT authentication
   * Has messaging capabilities with Spring Cloud Stream and Kafka

## Technology Stack

* Java 21
* Spring Boot 3.2.3
* Spring Cloud 2023.0.0
* OpenAPI Generator 7.0.1
* MongoDB
* JPA/Hibernate
* JWT Authentication
* Kafka Messaging

## Build Instructions

To build the entire project:

```bash
mvn clean install
```

To build individual modules:

```bash
mvn clean install -pl userprofiledataservAPI
mvn clean install -pl userprofiledataservService
```

## Development

This project follows clean architecture principles with a clear separation between:

* API definitions and contracts
* Core business logic 
* Infrastructure concerns (database, messaging, etc.)

When making changes, ensure you maintain this separation to preserve the architectural integrity of the system.
