
![](https://www.diamondbybold.com/wp-content/uploads/2016/09/rest-api-1-300x225.png)

# Boleto - API  
## [![Build Status](https://travis-ci.org/tperrut/boleto-api.svg?branch=master)](https://travis-ci.org/tperrut/boleto-api)

This project aims to create a * API Rest * for bankroll generation, using technology [Java 8]((http://java.com). [SpringBoot](http://projects.spring.io/spring-boot/) was used to build the API, [Travis.ci](https://travis-ci.org) for build automation, [Swagger](http://swagger.com) for documentation, and finally Automatic Deploy on [Heroku].

>The system has the following endpoint
	
 |VERB | END_POINT       |
|:---  |:---              |
   |POST | create_boleto  |   
  |GET  | list_boletos    |   
   | GET | datail_boltetos |   
   | GET | find_by_cliente  |
   | PUT | payment_boleto  |
   |DELETE | cancel_boleto |
--------


 It was developed using [Spring Boot](http://projects.spring.io/spring-boot/).   

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)


## Getting started

To start this web application just follow these steps:

   Build the project via Maven:

    $ mvn clean install

   Start the application:
        In your IDE invoke the class method Application#main to start the server , or
        From command line execute:

    $ java -jar target/boleto-api-1-0.1.jar

   Browse to the application root for API documentation:

    http://localhost:8080/


## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `/com/boleto/api/BoletoApgiiApplication.java` class from your IDE.



Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Tests 

Use maven goal 'test'

```shell
mvn test
```

 
## Documentation

Foi utilizado a biblioteca [Swagger](http://swagger.com).
	Url para acessar a documentação dos EndPoints da API :
	`http://server:port/swagger-ui.html`

## Information

- Spring boot automatically provides an embedded Tomcat server and a persistence layer based on Hibernate (as JPA provider).
- Data are automatically stored in an in-memory database. Changes are lost after restarting the application.
- All REST endpoints *can be tested locally with the Swagger* UI frontend:


## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
