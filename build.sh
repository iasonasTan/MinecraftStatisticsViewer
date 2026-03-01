#!/bin/bash
set -e

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
