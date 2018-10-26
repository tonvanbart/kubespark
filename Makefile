.PHONY: image
image:
	mvn package assembly:single
	docker build -t kubespark:latest . --build-arg JAR_FILE=target/kubespark-1.0-SNAPSHOT-jar-with-dependencies.jar

.PHONY: run
run:
	docker run --rm -p4567:4567 kubespark:latest

.PHONY: run-bindmount
run-bindmount:
	echo bind mount ${CURDIR}/bindmount on /config
	docker run --rm -p4567:4567 -v ${CURDIR}/bindmount:/config -e CONFIGLOC='/config/configuration.json' kubespark:latest