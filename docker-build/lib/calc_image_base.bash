#!/usr/bin/env bash
set -e
cd "$(dirname "$0")" || exit 123

VERSION=$(bash calc_version_base.bash)

echo "greetgo/md-reader-base:$VERSION"
