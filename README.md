# Reactive DB with Webflux
This project demonstrates the following technologies:
* Spring Webflux
* PostgreSQL/H2 R2DBC use, that is, Reactive DB drivers
* Integrating Liquibase with R2DBC - Liquibase requires JDBC drivers!
* Hibernate Validation in Webflux application

The requirements are as follows:
* The whole application needs to be reactive
* Both declarative (Hibernate Validation  based) and business exceptions need to be handled properly
* Database structure needs to be handled by Liquibase

## Architecture
The idea in this demo is to separate the following layers:
* repository - data access
* services - business logic
* handlers - translating results into proper HTTP responses
* routing - routing http requests to appropriate handlers

In addition, database structure and state will be handled by Liquibase

## Prerequisites
If using PostgreSQL, database needs to be running in the following address:
* localhost:5432/persontest

By default file-based H2 database is used.

## Usage
Upon startup, Liquibase creates a simple "person" database table and inserts some data to it, with random UUIDs. You can then manipulate the data with the following API endpoints:
* GET http://localhost:8080/persons (fetch all persons)
* GET http://localhost:8080/persons/<UUID> (fetch person with id)
* GET http://localhost:8080/persons/byLastName/<lastName> (fetch persons according to last name)
* POST http://localhost:8080/persons (data in body, for example src/test/resource/person_new.json)
* PUT http://localhost:8080/persons (data in body, for example src/test/resource/person_update.json)
* DELETE http://localhost:8080/persons/<UUID> (delete person with id)

Appropriate HTTP return codes with error messages should be returned when not found, or faulty input. You can try to update or create a person with over 20 character firstName or faulty Finnish personNumber if you wish to see (for example, src/test/resources/person_invalid_new.json).

## Inspiration
The following links provided valuable information for this demo project:
* [Error Handling in Spring Webflux](https://dzone.com/articles/error-handling-in-spring-webflux)
* [Spring WebFlux Validation](https://www.vinsguru.com/spring-webflux-validation/)

