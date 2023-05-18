#!/usr/bin/env bash

set -e

cd "$(dirname "$0")" || exit 113

VERSION=$(cat version/version.txt)

docker build -t "greetgo/md-reader:$VERSION" .
