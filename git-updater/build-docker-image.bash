#!/usr/bin/env bash

set -e

cd "$(dirname "$0")" || exit 113

docker build -t "greetgo/git-updater:0.0.1" .
