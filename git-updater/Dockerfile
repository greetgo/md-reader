FROM openjdk:17.0.2-jdk

COPY build/libs/md-reader-0.0.1.jar app.jar
COPY docker-build-files/run.sh run.sh
COPY docker-build-files/mime.types /etc/mime.types

ENTRYPOINT ["sh", "/run.sh"]
