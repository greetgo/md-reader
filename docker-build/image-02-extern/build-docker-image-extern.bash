#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 123

mkdir       -p                    build/extern/
cp ../../build/dist/extern/*.jar build/extern/

bash ../lib/tpl.bash Dockerfile.tpl >"build/Dockerfile.bash"

BASE_IMAGE="$(bash ../lib/calc_image_base.bash)"

export BASE_IMAGE
bash build/Dockerfile.bash >"build/Dockerfile"

IMAGE_NAME=$(bash ../lib/calc_image_lib.bash)

cd build

echo docker build -t "$IMAGE_NAME" .
docker build -t "$IMAGE_NAME" .

pwd
