#!/usr/bin/env bash
set -e

cd "$(dirname "$0")" || exit 123

cd ../../
./gradlew dist
