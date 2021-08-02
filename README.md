# Reactive DB with Webflux
This project demonstrates the following technologies:
* Spring Webflux
* PostgreSQL R2DBC use, that is, Reactive DB drivers
* Integrating Liquibase with R2DBC - Liquibase requires JDBC drivers
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

In addition, database structure and state will be handled by Liquibase

## Prerequisites
A PostgreSQL instance and database need to be running in the following address:
* localhost:5432/persontest

## Inspiration
The following links provided valuable information for this demo project:
* [Error Handling in Spring Webflux](https://dzone.com/articles/error-handling-in-spring-webflux)
* [Spring WebFlux Validation](https://www.vinsguru.com/spring-webflux-validation/)

