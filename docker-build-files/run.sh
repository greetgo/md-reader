#!/bin/sh

MD_READER_JAVA_OPTS="$MD_READER_JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=*:5005,server=y,suspend=n"

# shellcheck disable=SC2086
exec java $MD_READER_JAVA_OPTS -jar /app.jar
