.PHONY: image
image:
	mvn package assembly:single
	docker build -t kubespark:latest . --build-arg JAR_FILE=target/kubespark-1.0-SNAPSHOT-jar-with-dependencies.jar

.PHONY: run
run:
	docker run --rm -p4567:4567 kubespark:latest

.PHONY: run-bindmount
run-bindmount:
	@echo bind mounting ${CURDIR}/bindmount on /config
	docker run --rm -p4567:4567 -v ${CURDIR}/bindmount:/config -e CONFIGLOC='/config/configuration.json' kubespark:latest

.PHONY: volume-data
volume-data:
	@echo creating volume...
	docker run --rm -v ${CURDIR}/volumedata:/src -v kubespark-volume:/target busybox cp /src/configuration.json /target

.PHONY: inspect-volume
inspect-volume:
	docker run --rm -ti -v ${CURDIR}/volumedata:/src -v kubespark-volume:/target busybox sh


.PHONY: run-volume
run-volume:
	docker run --rm -p4567:4567 -v kubespark-volume:/config -e CONFIGLOC='/config/configuration.json' kubespark