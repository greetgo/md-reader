#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 115

bash               lib/create_dist.bash
bash    image-01-base/build-docker-image-base.bash
bash image-02-extern/build-docker-image-extern.bash
bash   image-03-app/build-docker-image-app.bash

IMAGE_BASE="$(bash lib/calc_image_base.bash)"
IMAGE_LIB="$(bash lib/calc_image_lib.bash)"
IMAGE_APP="$(bash lib/calc_image_app.bash)"

echo ""
echo "***"
echo "***"
echo "*** RUN COMMANDS"
echo ""
echo "docker push $IMAGE_BASE"
echo "docker push $IMAGE_LIB"
echo "docker push $IMAGE_APP"
echo ""
