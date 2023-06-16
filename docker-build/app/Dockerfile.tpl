FROM <% $LIB_IMAGE %>

COPY src/mime.types         /etc/mime.types
COPY src/run.sh            /app/run.sh
COPY jars/app.jar         /app/
COPY jars/project/*.jar  /app/project/

ENTRYPOINT ["sh", "/app/run.sh"]
