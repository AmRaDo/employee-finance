#!/bin/bash
mvn clean package
java -jar target/employee-finance-0.0.1-SNAPSHOT.jar
