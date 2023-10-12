# User Registration and Login with Email and Password, Facebook and Google Demo

This sample application made with Spring Boot is intended to show simple user authentication and authorization using Spring Security.

On the login page user can sign up or log in with username (email) and password. After authentication user redirect on profile page where user can change credentials and log out the account.
Also user can log in to the application by using existing account at an OAuth 2.0 Provider such as Facebook and OpenID Connect 1.0 Provider such as Google.

The application has unit and integration testing methods.

The frontend of the project (login form) is made with help of [tutorial](https://www.codingnepalweb.com/login-registration-form-html-css/).

![image](https://github.com/Kofa-Yoh/spring-registration-login-social-demo/assets/117309392/32fe33ae-f066-4b03-9429-296ff83b6369)

## Stack

* Java 17
* Spring boot 3.2
* Spring MVC
* Spring Security
* Spring Data JPA
* Hibernate
* Maven
* Thymeleaf
* HTML
* CSS
* JavaScript

## Quick Start

### 1. Create database
I use PostgreSQL in this project, but you can choose another DBMS.
Change the application settings connecting with using database.
```properties
# src/main/resources/config/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 2. Change port
```properties
# src/main/resources/config/application.properties
server.port=8081
```

### 3. Register application to use social networks for authentication
* Create a project in Facebook: https://developers.facebook.com/apps/creation
* Create a project in Google Cloud: https://console.cloud.google.com/projectcreate

Uncomment and fill the application settings connecting with social networks.
```properties
# src/main/resources/config/application.properties
spring.security.oauth2.client.registration.facebook.client-id=xxx
spring.security.oauth2.client.registration.facebook.client-secret=xxx
spring.security.oauth2.client.registration.google.client-id=xxx
spring.security.oauth2.client.registration.google.client-secret=xxx
```

## Tests
For unit and integration tests it is made the particular database 'mydb-test'. So you should create a database if you want to run the tests. The settings for application testing locate in the file: **src/test/resources/application-test.properties**

## Useful Links
* Create a project in Facebook: https://developers.facebook.com/apps/creation
* Create a project in Google Cloud: https://console.cloud.google.com/projectcreate
* Tutorial Login & Registration Form in HTML CSS & JavaScript: https://www.codingnepalweb.com/login-registration-form-html-css/
