# JAVALIN R.E.S.T API

### Description

This is a simple REST API written in Java that allows you to create, read, update and delete users. 
The API is secured with JWT tokens and uses a PostgresSQL database for storing user data.

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
