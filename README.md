# Javalin(version 6) R.E.S.T Api Development Pipeline (CI/CD - Traefik)

### Description

This is a simple REST API written in Java that allows you to create, read, update and delete users. 
The API is secured with JWT tokens and uses a PostgresSQL database for storing user data.

This project is also used for testing the CI/CD pipeline with Github Actions. (See the .github/workflows folder)
We use Github Actions to build the project, run the tests and push the Docker image to Docker Hub.

### Technologies used:

- JDK 17 (Java 17)
- Hibernate (JPA Provider)
- Javalin (Web Framework)
- PostgresSQL (Database)
- Maven (Dependency Management)
- Docker (Containerization)
- Docker Compose (Container Orchestration)
- JUnit (Unit Testing)
- Mockito (Mocking Framework)
- Log4j (Logging Framework)
- Testcontainers (Integration Testing)
- Rest Assured (API Testing)
- Træfik (Reverse Proxy, Load Balancer, Deployment)
- DigitalOcean (Cloud Provider)
- Github Actions (CI/CD)
- Caddy (Web Server)

### Prerequisites

- JDK 17
- Docker
- Maven
- Git
- Postman (Optional)

### For Mac users with M1 and M2 chips has to make some changes to the HibernateConfig file

Do the following to get the Testcontainers up and running:

1. Run the following commands in your terminal

```bash 
    # New tag (mac m1/m2): 
    docker tag arm64v8/postgres:latest postgresql:15.3-alpine3.18
    
    # Symlink
    sudo ln -s $HOME/.docker/run/docker.sock /var/run/docker.sock
```
