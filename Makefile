.PHONY: image
image:
	mvn package assembly:single
	docker build -t kubespark:latest . --build-arg JAR_FILE=target/kubespark-1.0-SNAPSHOT-jar-with-dependencies.jar