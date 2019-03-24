
![](https://www.diamondbybold.com/wp-content/uploads/2016/09/rest-api-1-300x225.png)

# Boleto - API  
## [![Build Status](https://travis-ci.com/tperrut/boleto-api.svg?branch=master)](https://travis-ci.com/tperrut/boleto-api)
API REST para geração de boletos.
	O sistema terá os seguintes endpoints :
	
 |VERB | END_POINT       |
|:---  |:---              |
   |POST | create_boleto   |   
  |GET  | list_boletos    |   
   | GET | datail_boltetos |   
   | GET | find_by_cliente  |
   | PUT | payment_boleto  |
   |DELETE | cancel_boleto |
--------


 Foi desenvolvida utilizando [Spring Boot](http://projects.spring.io/spring-boot/)   

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

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

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
