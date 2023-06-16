#!/usr/bin/env bash
set -e

cd "$(dirname "$0")" || exit 123

BUILD_DIR="../../build"
TMP_DIR="$BUILD_DIR/tmp"

TARGET_DIR="$BUILD_DIR/dist/extern"

mkdir -p "$TMP_DIR"

NAMES_FILE="$TMP_DIR/file-list/file-names.txt"
mkdir -p "$(dirname "$NAMES_FILE")"

find "$TARGET_DIR" -maxdepth 1 -name "*.jar" -exec basename {} \; | sort > "$NAMES_FILE"

md5sum "$NAMES_FILE" | cut -b 1-32
