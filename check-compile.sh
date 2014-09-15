#!/bin/sh

compiledir=`mktemp -d`
cp src $compiledir/src -R
cp lwjgl-2.9.1/jar $compiledir/lib -R
cp twl/TWL.jar $compiledir/lib/
cd $compiledir/src
javac -cp "../lib/*" `find -name *.java`
RESULT=$?
#/usr/bin/rm $compiledir -R
[ $RESULT -ne 0 ] && echo "Compile failed - disallowing commit" && exit 1
echo "Compile succeeded - allowing commit" && exit 0
