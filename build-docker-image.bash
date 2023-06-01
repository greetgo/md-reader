#!/usr/bin/env bash

set -e

rm -rf build

./gradlew build

cd "$(dirname "$0")" || exit 113

VERSION=$(cat version/version.txt)

docker build -t "greetgo/md-reader:$VERSION" .
