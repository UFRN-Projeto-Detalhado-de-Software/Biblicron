FROM ubuntu:22.04

COPY . /server

WORKDIR /server

RUN apt update -y; \
    apt install -y maven; \
    apt install -y default-jre

CMD ["mvn", "spring-boot:run"]
