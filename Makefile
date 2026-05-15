.PHONY: run test build check docker-up docker-down clean wrapper

run:
	./gradlew run

test:
	./gradlew test

build:
	./gradlew build

check:
	./gradlew clean test build

docker-up:
	docker compose up --build

docker-down:
	docker compose down

clean:
	./gradlew clean

wrapper:
	./gradlew wrapper --gradle-version 8.5 --distribution-type bin
