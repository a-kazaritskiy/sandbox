# Sandbox Transfer Application

RESTful service for making transfer between accounts.

Implemented with in-memory data storage.

## Purpose

1. Describe simple transfer system with REST-API
2. Test main transfer logic

## Run

java -jar sandbox.jar

### Run with external config

java -Dlogging.config=file:/logback-spring.xml -jar sandbox.jar --spring.config.location=file:/application.properties

## Test

### Preconditions

1. Assume application is run on localhost:8080
2. Predefined accounts (me/you) with balance of 100 RUB and system account with balance of 100500 RUB
2.1 accounts can be configured via application.properties
<code>
storage.initializer.accounts={account list, except system}

storage.initializer.account.${account}.currency={Currency ISO 4217 code}

storage.initializer.account.${account}.balance={Balance with 2 fraction digits}
</code>

### Get Account info

<code>
curl -X GET localhost:8080/accounts/me
</code>

<code>
{"result":{"id":"me","currency":"RUB","balance":"100.00"}}
</code>

### Make Deposit

<code>
curl -X POST localhost:8080/accounts/me/deposit --header 'Content-Type: application/json' --data '{"amount": "1.00"}'
</code>

<code>
{"result":{"id":"4","status":"success","currency":"RUB","amount":"1.00","balance":"101.00"}}
</code>

### Transfer

#### Calculate

<code>
curl -X POST localhost:8080/transfer/calculate --header 'Content-Type: application/json' --data '{"fromAccount": "me","toAccount": "you","toAmount": "1.00"}'
</code>

<code>
{"result":{"from":{"account":"me","currency":"RUB","amount":"1.00"},"to":{"account":"you","currency":"RUB","amount":"1.00"},"rate":"1","fee":"0"}}
</code>

#### Make

<code>
curl -X POST localhost:8080/transfer/make --header 'Content-Type: application/json' --data '{"fromAccount": "me","toAccount": "you","toAmount": "1.00"}'
</code>

<code>
{"result":{"id":"5","status":"success","from":{"account":"me","currency":"RUB","amount":"1.00"},"to":{"account":"you","currency":"RUB","amount":"1.00"},"rate":"1","fee":"0"}}
</code>

