# online-bookstore-app

## Requirements

For building and running the application you need:

- [JDK 11]
- [Maven ](https://maven.apache.org)

## Build the application locally

```shell
mvn clean install
```


## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.book.BooksApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Deploying the application to Docker

Execute below commands to build and run docker file.

```shell
docker build -t <image-name> .
docker run -dit --name <container-name> -p 8081:8081
```


## PromoCode and Discount configurations

```shell
PromoCode : code1
discountRules: type: FICTION discount: 10
discountRules: type: COMICS discount: 5

```


```shell
PromoCode : code2
discountRules: type: FICTION discount: 5

```

All the configurations are present in application.yml file.


## Swagger URL


```shell

Swagger UI:
http://localhost:8081/v1/swagger-ui/index.html?configUrl=/v1/api-docs/swagger-config

Yaml spec.
http://localhost:8081/v1/api-docs.yaml

```

## Note to the reader

 Future enhancements

1. The controller endpoint  /checkout is currently implemented based on the request only.
   Can be extended to fetch the book from DB and validate before proceeding to checkout.