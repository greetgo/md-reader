#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 123

mkdir       -p                    build/extern/
cp ../../build/dist/extern/*.jar build/extern/

IMAGE_NAME=$(bash ../lib/calc_image_lib.bash)

echo docker build -t "$IMAGE_NAME" .
docker build -t "$IMAGE_NAME" .

echo ""
echo "***"
echo "*** RUN COMMAND"
echo "***"
echo ""
echo "docker push $IMAGE_NAME"
echo ""
echo "***"
echo "***"
echo "***"
