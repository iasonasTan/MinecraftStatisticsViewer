#!/bin/bash
set -e
clear
./clean.sh

MAIN_SOURCE="app/msv/main/Main.java"
MAIN_CLASS="app.msv.main.Main"

cd src
jar -xf ../libs/*.jar
javac -d ../out/ -cp "." "$MAIN_SOURCE"
mv com/ ../out/

cd ..
jar -cfe jars/App.jar \
	"$MAIN_CLASS" \
	-C out . \
	-C resources .

java -jar jars/App.jar $(cat datapath.dat) janekv

"$(cat viewer.txt)" response.json
