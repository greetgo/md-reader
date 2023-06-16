#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 115

bash     lib/create_dist.bash
bash extern/build-docker-image-extern.bash
bash   app/build-docker-image-app.bash

IMAGE_LIB="$(bash lib/calc_image_lib.bash)"
IMAGE_APP="$(bash lib/calc_image_app.bash)"

echo ""
echo "***"
echo "***"
echo "*** RUN COMMANDS"
echo ""
echo "docker push $IMAGE_LIB"
echo "docker push $IMAGE_APP"
echo ""
