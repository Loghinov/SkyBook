# Code Map

## Root (62 files)

### agents/ (2 files)
Rosetta workflow state and scripts.
- init-workspace-flow-state.md
- TEMP/

#### agents/TEMP/ (1 file)
Temporary codemap generation artifacts.
- codemap.sh

### src/ (54 files)
Application source code and resources.

#### src/main/java/com/flightbooking/ (47 files)
Core application package.

##### src/main/java/com/flightbooking/ — Root Controllers & Main
Application entry point and top-level controllers.
- FlightBookingApplication.java
- AdminController.java
- AuthController.java
- BookingController.java
- FlightController.java
- PaymentController.java
- UserController.java

##### src/main/java/com/flightbooking/config/ (1 file)
Configuration and initialization.
- DataInitializer.java

##### src/main/java/com/flightbooking/controller/ (6 files)
REST API endpoints (inherited from root).
- AdminController.java
- AuthController.java
- BookingController.java
- FlightController.java
- PaymentController.java
- UserController.java

##### src/main/java/com/flightbooking/dto/ (11 files)
Data transfer objects for API contracts.

###### src/main/java/com/flightbooking/dto/request/ (6 files)
Request DTOs.
- CreateBookingRequest.java
- CreateFlightRequest.java
- CreateUserRequest.java
- LoginRequest.java
- ProcessPaymentRequest.java
- RegisterRequest.java
- SearchFlightRequest.java
- UpdateProfileRequest.java

###### src/main/java/com/flightbooking/dto/response/ (5 files)
Response DTOs.
- AuthResponse.java
- BookingResponse.java
- FlightResponse.java
- PaymentResponse.java
- UserResponse.java

##### src/main/java/com/flightbooking/entity/ (6 files)
JPA domain entities.
- Booking.java
- BookingStatus.java
- Flight.java
- Payment.java
- PaymentStatus.java
- Role.java
- User.java

##### src/main/java/com/flightbooking/exception/ (4 files)
Exception handling and global error mapping.
- BookingException.java
- GlobalExceptionHandler.java
- ResourceNotFoundException.java
- UnauthorizedAccessException.java

##### src/main/java/com/flightbooking/mapper/ (4 files)
Entity-to-DTO/DTO-to-Entity mappers.
- BookingMapper.java
- FlightMapper.java
- PaymentMapper.java
- UserMapper.java

##### src/main/java/com/flightbooking/repository/ (4 files)
Spring Data JPA repositories (data access).
- BookingRepository.java
- FlightRepository.java
- PaymentRepository.java
- UserRepository.java

##### src/main/java/com/flightbooking/security/ (5 files)
JWT, authentication, and security configuration.
- JwtAuthenticationFilter.java
- JwtUtil.java
- SecurityConfig.java
- SecurityUtils.java
- UserDetailsServiceImpl.java

##### src/main/java/com/flightbooking/service/ (8 files)
Business logic interfaces and implementations.

###### src/main/java/com/flightbooking/service/ (4 files)
Service interfaces.
- BookingService.java
- FlightService.java
- PaymentService.java
- UserService.java

###### src/main/java/com/flightbooking/service/impl/ (4 files)
Service implementations.
- BookingServiceImpl.java
- FlightServiceImpl.java
- PaymentServiceImpl.java
- UserServiceImpl.java

#### src/main/resources/ (7 files)
Configuration and static assets.

##### src/main/resources/ (1 file)
- application.properties

##### src/main/resources/static/ (3 files)
Static HTML frontend.
- admin.html
- dashboard.html
- index.html

### Root Level Files (6 files)
- pom.xml — Maven build configuration
- gain.json — Rosetta SDLC metadata
- run.sh — Application startup script
- .gitignore — Git exclusions
- .prettierignore — Prettier exclusions (auto-generated)