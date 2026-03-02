#!/bin/bash
set -e

clear

./clean.sh
./build.sh

# Run generated JAR
java -jar jars/App.jar $(cat datapath.dat) $(cat username.dat)

# Print Output
echo $(cat output.json)
