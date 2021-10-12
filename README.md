# RDID

![build](https://github.com/averak/RDID/workflows/build/badge.svg)
![test](https://github.com/averak/RDID/workflows/test/badge.svg)
![Version 1.0](https://img.shields.io/badge/version-1.0-yellow.svg)
[![MIT License](http://img.shields.io/badge/license-MIT-blue.svg?style=flat)](LICENSE)

RDID(RCC Developer ID) is IdP for Ritsumeikan Computer Club.

## Develop

### Requirements

- Java OpenJDK 17
- Spring Boot 2.5
- MySQL 8.0

### Usage

If you want to run on Windows, you can use `gradlew.bat` instead of of `gradlew`.

#### How to bulid

```sh
$ ./gradlew build -x test
```

When build successful, you can find .jar file in `app/build/libs`

#### How to run

First, you need to launch mysql with `docker-compose`.

```sh
$ docker-compose up -d
```

Then you can launch application.
Default port is `8080`. If you want to change port, run with `-Dserver.port=XXXX`.

```sh
# 1. run .jar file
$ java -jar rippy-<version>.jar  # -Dspring.profiles.active=<environment>

# 2. run on dev environment
$ ./gradlew bootRun
```

#### How to test

```sh
# 1. all tests
$ ./gradlew test

# 2. only unit tests
$ ./gradlew unitTest

# 3. only integration tests
$ ./gradlew integrationTest
```

### API docs

This project support Swagger UI.

1. Run application
2. Access to [Swagger UI](http://localhost:8080/swagger-ui/)
