#!/bin/sh

compiledir=`mktemp -d`
cp src $compiledir/src -R
cp lib/lwjgl/jar $compiledir/lib -R
cp lib/twl/TWL.jar $compiledir/lib/
cd lib/json/json-simple-1.1.1.jar $compiledir/lib/
cd $compiledir/src
javac -cp "../lib/*" `find -name *.java`
RESULT=$?
/usr/bin/rm $compiledir -R
[ $RESULT -ne 0 ] && echo "Compile failed - disallowing commit" && exit 1
echo "Compile succeeded - allowing commit" && exit 0
