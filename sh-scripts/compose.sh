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

stage=$(get_arg_value "stage" "$@")
if [ -z "$stage" ]; then echo "Warning!  [stage] wasn't found, set default: paracels"; fi

quoteName="db-paracels$stage-21072023"
echo "Info! quote name: "$quoteName

# ...

if [ -z "$(docker images -q $quoteName)" ]; then
  echo "Info! Creating image by file" "$PARENT_DIR/$DOCKER_PATH_LOCAL"/$quoteName.Dockerfile
	docker build . -f "$PARENT_DIR/$DOCKER_PATH_LOCAL"/$quoteName.Dockerfile -t $quoteName
  echo "Info! Finish image create"
else
	echo "Info! IMAGE IMAGE IS ALREADY EXISTS. ITS SEEMS YOU MAY REBASE IT"
fi

echo "Step! Running $quoteName staging..."
echo "Info! Running docker"
(docker run -d -p $DB_PORT:$DB_PORT -e ISC_PASSWORD=$DB_PASSWORD --name $quoteName $quoteName) &
process=$!
wait $process

return 0
