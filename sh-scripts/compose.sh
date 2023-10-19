#!/bin/bash
set -e

if sed --version > /dev/null 2>&1; then
    echo "Info! Setting [sed] command style: GNU"
    sed_command="sed -i"
else
    echo "Info! Setting [sed] command style: BSD"
    sed_command="sed -i ''"
fi 

if [ -f /.dockerenv ]; then
  PARENT_DIR=$SERVER_PATH_CONTAINER
else
  PARENT_DIR=$(dirname "$(dirname "$(realpath "$0")")")
fi

if [ ! -f .env ]; then
  echo "Error!  .env file wasn't fount in project [server] directory" \
        "$PARENT_DIR"/.env
  return 0
fi
source "$PARENT_DIR"/.env

get_arg_value() {
    for arg in "$@"; do
        case $arg in 
            "$1="*) 
                echo "${arg#*=}"
                return
                ;;
        esac
    done
}

# stage=$(get_arg_value "stage" "$@")
# if [ -z "$stage" ]; then echo "Warning!  [stage] wasn't found, set default: localhost"; stage="local"; fi

# ...

return false
