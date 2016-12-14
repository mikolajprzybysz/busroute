#!/usr/bin/env bash
set -e

function helpmessage {
    bold=$(tput bold)
    normal=$(tput sgr0)

    echo "
${bold}SYNOPSIS${normal}
    build.sh start [FILEPATH]
    build.sh stop
"
}

if [ "$1" == "help" ] || [ "$1" == "" ] ; then
    helpmessage
    exit
fi

if [ "$1" == "start" ] ; then
    RUN="java -jar target/busrouteservice.jar $1 $2"
fi

if [ "$1" == "stop" ] ; then
    RUN="java -jar target/busrouteservice.jar $1"
fi

RUN="java -jar target/busrouteservice.jar $1"
NAME=mikolajprzybysz-busroute-service