# s8-project

This repository contains code for the S8 Hiring Interview Challenge.

The application connects to Kafka Server hosting at localhost:9092 and MongoDB Instance hosting at localhost:27107.

# Running the application
To run the application, use command `mvn spring-boot:run`

Kafka Server and MongoDB Instance need to be available for the application to be working properly.
To change the Kafka and MongoDB instance, update `spring.kafka.consumer.bootstrap-servers` and `spring.data.mongodb.uri`
in application properties

# API
The detailed documentation of the API can be view in
[Swagger](http://localhost:8080/swagger-ui.html)

createUser
- Create user based on ID

getAllCountries
- Get all countries which are available for account creation

createCurrencyAccount
- Create an account in specific country/currency

getCurrencyAccounts
- Retrieve all accounts owned by the user

sendTransaction
- Send transaction to Kafka Topic

findTransactionByUser
- Find all transactions performed by user

findTransactionByAccount
- Find all transactions performed by specific account

ALL API CALL REQUIRED `jwtToken` HEADER with value `admin`

# Repository layout
`Config` contains configuration files including Factory for Kafka Producer and Consumer.
It also contains all the configurations file for Swagger, Authority and other Bean Configurations.

`Constants` contains all the constants used throughout the application

`Controllers` is the package which has all the API Controllers

`Model` contains all the POJO classes used by the application

`Repositories` has all the MongoRepository operation for Data access

`Service` is the package which contains all services related to user,account and transaction

`Utilities` is the package for all utilities used by other services including validation and calculation


