#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 121

IMAGE_BASE="$(bash ../lib/calc_image_base.bash)"

docker build -t "$IMAGE_BASE" .
