# Toggle Hub

Toggle Hub is a Feature Flagging Tool built with Spring Boot. It allows for dynamic management of feature flags to control software behavior without redeploying.

## Technology Stack

- **Java**: 17
- **Framework**: Spring Boot 3.2.4
- **Template Engine**: Thymeleaf
- **Database**: H2 (File-based)
- **Security**: Spring Security

## Prerequisites

- Java 17 or higher
- Maven

## Setup & Running

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd Toggle-Hub
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   The application will start on port `8081`.

## Usage

- **Application Access**: Open your browser and navigate to [http://localhost:8081](http://localhost:8081).
- **H2 Database Console**: Access the database console at [http://localhost:8081/h2-console](http://localhost:8081/h2-console).
    - **JDBC URL**: `jdbc:h2:file:./data/togglehubdb`
    - **User**: `sa`
    - **Password**: `password`

## Login

If security is enabled, use the configured credentials (or the default `user` and auto-generated password printed in the console if not customized).
