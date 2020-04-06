#!/bin/bash

gradle wrapper

chmod +x gradlew

./gradlew clean build

docker-compose up --build