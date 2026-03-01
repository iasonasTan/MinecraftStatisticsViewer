#!/bin/bash
set -e

clear

./clean.sh
./build.sh

# Run generated JAR
java -jar jars/App.jar $(cat datapath.dat) ADStuff

# Print Output
"$(cat viewer.txt)" response.json
