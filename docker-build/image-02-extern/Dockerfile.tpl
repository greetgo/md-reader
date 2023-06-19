FROM <% $BASE_IMAGE %>

COPY extern/*.jar /app/extern/

WORKDIR /app
