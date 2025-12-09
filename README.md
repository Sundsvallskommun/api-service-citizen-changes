# Citizen-changes

_Manages reminders and eligibility checks for daycare and school bus travel. Includes scheduled jobs to verify if
citizens with active school bus applications have moved, and sends seasonal reminders for citizens to update their
applications for the next semester._

## Getting Started

### Prerequisites

- **Java 25 or higher**
- **Maven**
- **Git**
- **[Dependent Microservices](#dependencies)**

### Installation

1. **Clone the repository:**

   ```bash
   git clone git@github.com:Sundsvallskommun/api-service-citizen-changes.git
   cd api-service-citizen-changes
   ```
2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#Configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible.
   See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

   ```bash
   mvn spring-boot:run
   ```

## Dependencies

This microservice depends on the following services:

- **Citizen**
  - **Purpose:** To find address information about a citizen.
  - **Repository:** Not available at this moment.
  - **Additional Notes:** Citizen is a API serving data
    from [Metadatakatalogen](https://utveckling.sundsvall.se/digital-infrastruktur/metakatalogen).
- **Messaging**
  - **Purpose:** To send e-mails and sms to affected citizens and email reports to the administrators.
  - **Repository:** [api-service-messaging](https://github.com/Sundsvallskommun/api-service-messaging)
  - **Setup Instructions:** Refer to its documentation for installation and configuration steps.
- **Open-e Platform**
  - **Purpose:** This service retrieves errand information from the Open-e Platform.
  - **Repository:** [Open-ePlatform](https://github.com/Open-ePlatform/Open-ePlatform)
  - **Setup Instructions:** Refer to its documentation for installation and configuration steps.

Ensure that these services are running and properly configured before starting this microservice.

## API Documentation

Access the API documentation via Swagger UI:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Usage

### API Endpoints

Refer to the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X GET http://localhost:8080/{municipalityId}/daycare
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in
`application.yml`.

### Key Configuration Parameters

- **Server Port:**

  ```yaml
  server:
    port: 8080
  ```
- **Database Settings:**

  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/your_database
      username: your_db_username
      password: your_db_password
  ```
- **External Service URLs:**

  ```yaml
  integration:
    citizen:
      base-url: http://dependency_service_url
    messaging:
      base-url: http://dependency_service_url
    open-e:
      base-url: https://e-tjanster.sundsvall.se
    mapper:
      link-template: http://link_templete_url/%%flowinstanceid%%
      recipients-elevresa: recipient@test.se
      recipients-skolskjuts: recipient@test.se
    service:
      family-id: 123,456

  spring:
    security:
      oauth2:
        client:
          provider:
            citizen:
              token-uri: http://dependecy_service_token_url
            messaging:
              token-uri: http://dependecy_service_token_url
          registration:
            citizen:
              client-id: some-client-id
              client-secret: some-client-secret
            messaging:
              client-id: some-client-id
              client-secret: some-client-secret
  ```

### Database Initialization

The project is set up with [Flyway](https://github.com/flyway/flyway) for database migrations. Flyway is disabled by
default so you will have to enable it to automatically populate the database schema upon application startup.

```yaml
spring:
  flyway:
    enabled: true
```

- **No additional setup is required** for database initialization, as long as the database connection settings are
  correctly configured.

### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please
see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Code status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-citizen-changes&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-citizen-changes)

---

Copyright (c) 2024 Sundsvalls kommun
