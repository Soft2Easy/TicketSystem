# Ticket System

## Overview
The Ticket System is a microservices-based application built with Spring Boot, designed to manage events and user registrations. It demonstrates modern software architecture principles, including service decomposition, containerization, and polyglot persistence.

## Features
- **Event Management**: Create and manage events using the Events service.
- **User Registration**: Handle user registrations for events via the Registration service.
- **Microservices Architecture**: Decoupled services for scalability and maintainability.
- **Containerization**: Docker support for easy deployment and environment consistency.
- **Database Integration**: PostgreSQL for events data and MongoDB for registration data.
- **RESTful APIs**: Expose endpoints for event and registration operations.
- **Health Monitoring**: Spring Boot Actuator for application health checks.

## Technologies Used
- **Java 25**: Programming language with the latest features.
- **Spring Boot 4.0.0**: Framework for building microservices.
- **Spring Data JPA**: For PostgreSQL interactions in the Events service.
- **Spring Data MongoDB**: For MongoDB interactions in the Registration service.
- **Spring Web/WebFlux**: For RESTful APIs and reactive programming.
- **Gradle**: Build tool for dependency management and project structure.
- **Docker & Docker Compose**: Containerization and orchestration.
- **PostgreSQL**: Relational database for events.
- **MongoDB**: NoSQL database for registrations.
- **JUnit 5**: Testing framework.
- **Lombok**: For reducing boilerplate code.

## Architecture
The application follows a microservices architecture with two main services:

1. **Events Service** (Port 8081):
   - Manages event data stored in PostgreSQL.
   - Provides CRUD operations for events.

2. **Registration Service** (Port 8082):
   - Handles user registrations stored in MongoDB.
   - Communicates with the Events service for event details.
   - Includes health monitoring via Spring Boot Actuator.

Services are containerized using Docker and orchestrated with Docker Compose, including database containers.

## API Documentation

### Events Service (Port 8081)

#### Organizers
- **GET /organizers**  
  Retrieve all organizers.  
  *Response:* List of Organizer objects.

- **POST /organizers/create**  
  Create a new organizer.  
  *Request Body:* Organizer JSON.  
  *Response:* Created Organizer object.

#### Venues
- **POST /venues/create**  
  Create a new venue.  
  *Request Body:* Venue JSON.  
  *Response:* Created Venue object.

#### Events
- **POST /event/create**  
  Create a new event.  
  *Request Body:* Event JSON (including organizer and venue IDs).  
  *Response:* Created Event object.

- **GET /events?organizerId={id}**  
  Retrieve events by organizer ID.  
  *Response:* List of Event objects.

- **GET /events/{id}**  
  Retrieve a specific event by ID.  
  *Response:* Event object.

#### Products
- **POST /products/create**  
  Create a new product (ticket type) for an event.  
  *Request Body:* Product JSON.  
  *Response:* Created Product object.

- **GET /products/{id}**  
  Retrieve a specific product by ID.  
  *Response:* Product object.

- **GET /products?eventId={id}**  
  Retrieve products by event ID.  
  *Response:* List of Product objects.

### Registration Service (Port 8082)

#### Registrations
- **POST /registrations**  
  Create a new registration for an event product.  
  *Request Body:* Registration JSON (productId, attendeeName).  
  *Response:* Created Registration object with ticket code.

- **GET /registrations/{ticketCode}**  
  Retrieve a registration by ticket code.  
  *Response:* Registration object.

- **PUT /registrations**  
  Update an existing registration (e.g., attendee name).  
  *Request Body:* Registration JSON with ticketCode.  
  *Response:* Updated Registration object.

- **DELETE /registrations/{ticketCode}**  
  Delete a registration by ticket code.  
  *Response:* No content.

## Prerequisites
- Java 25 JDK
- Docker and Docker Compose
- Gradle (optional, as Gradle Wrapper is included)

## Installation and Running

### Using Docker Compose (Recommended)
1. Clone the repository.
2. Navigate to the project root directory.
3. Run `docker-compose up --build` to build and start all services, including databases.

The services will be available at:
- Events Service: http://localhost:8081
- Registration Service: http://localhost:8082

### Using Gradle
1. Ensure PostgreSQL and MongoDB are running locally or via Docker.
2. For Events Service:
   - Navigate to `events/` directory.
   - Run `./gradlew bootRun` (or `gradlew.bat bootRun` on Windows).
3. For Registration Service:
   - Navigate to `registration/` directory.
   - Run `./gradlew bootRun`.

## Configuration
- Default profiles use Docker container names for databases.
- For local development, adjust `application.properties` files in each service's resources.

## Testing
Run tests using Gradle:
- `./gradlew test` from the root or specific service directory.

## Contributing
1. Fork the repository.
2. Create a feature branch.
3. Make changes and add tests.
4. Submit a pull request.

## License
This project is licensed under the MIT License.