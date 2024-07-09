include .env

docker_compose_params=--file docker/docker-compose.yaml --env-file .env -p ${PROJECT_NAME}

up:
	docker-compose ${docker_compose_params} up -d ${services}
	sleep 4
	$(MAKE) app-run

down:
	docker-compose ${docker_compose_params} down -v
	docker images -a | grep "${PROJECT_NAME}" | awk '{print $$3}' | xargs docker rmi -f

restart: clear
	$(MAKE) down
	$(MAKE) up

clear:
	clear

app-fx-run:
	cd app && mvn -X javafx:run

app-build:
	cd app && mvn clean compile assembly:single

app-jar-run:
	cd app && java -Xmx4G -Xms2G -jar target/Consider-1.0-SNAPSHOT-jar-with-dependencies.jar