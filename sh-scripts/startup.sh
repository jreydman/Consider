#!/usr/bin/env bash

set -e

if [ -f /.dockerenv ]; then
  PARENT_DIR=$SERVER_PATH_CONTAINER
  SHSCRIPTS_PATH_LOCAL=/opt
else
  PARENT_DIR=$(dirname "$(dirname "$(realpath "$0")")")
  SHSCRIPTS_PATH_LOCAL=$PARENT_DIR/sh-scripts
fi

echo "Info! startup: " "$PARENT_DIR" "$SHSCRIPTS_PATH_LOCAL"

# ...

echo false
