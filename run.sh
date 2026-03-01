#!/bin/bash
set -e

cd src
javac -d ../out/ -cp "." main/Main.java

cd ..

jar -cfe jars/App.jar \
	main.Main \
	-C out . \
	-C resources . \
	-C src .

java -jar jars/App.jar $(cat datapath.dat) janekv

bat response.json
