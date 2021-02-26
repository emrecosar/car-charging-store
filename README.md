# Assignment

## Tech Stack

* Java 8
* Spring Boot 2.4.3
* JUnit 5

## How to Run

Note: Default server port is set to `8090` to avoid conflicting with possible installed docker application which latest
versions runs under `8080` port.

### Run with maven

Build and Run:

```bash
mvn clean package spring-boot:run
```

Also;

Build:

```bash
mvn clean package
```

Run tests only:

```bash
mvn clean test
```

Run the application:

```bash
mvn spring-boot:run
```

### Run as JAR file

Build:

```bash
mvn clean package
```

Run the application:

```bash
java -jar target/assignment-1.0.0.jar
```

### Swagger API Documentation

```bash
http://localhost:8090/swagger-ui.html
```

### Test Reports

Test reports are generated under `target/surfire-reports`