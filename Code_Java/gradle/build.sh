#!/bin/bash

if [ $# -eq 0 ]
  then
    echo "No arguments supplied"
    exit 1
fi

projectName=$1

cd ../$projectName/

gradle clean
gradle build
gradle buildSingleProject