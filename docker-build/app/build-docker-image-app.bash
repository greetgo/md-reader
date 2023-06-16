#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 123

rm -rf build

TPL="build/tpl"

mkdir -p "$TPL"
bash ../lib/tpl.bash Dockerfile.tpl >"$TPL/Dockerfile.bash"

LIB_IMAGE="$(bash ../lib/calc_image_lib.bash)"

DOCK="build/dock"
mkdir -p "$DOCK"

export LIB_IMAGE
bash build/tpl/Dockerfile.bash >"$DOCK/Dockerfile"

JARS="$DOCK/jars"
mkdir -p "$JARS/project"

cp -r src "$DOCK/"
cp -r ../../build/dist/project/*.jar "$JARS/project/"
cp    ../../build/dist/md-reader-*.jar "$JARS/app.jar"

IMAGE_APP="$(bash ../lib/calc_image_app.bash)"

cd "$DOCK" || exit 118

docker build -t "$IMAGE_APP" .
