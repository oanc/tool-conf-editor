#!/bin/bash

VERSION=`cat VERSION`
JAR=ToolConfEditor-$VERSION.jar
SCRIPT=$HOME/bin/tce

cp target/$JAR $HOME/bin

cat << EOF > $SCRIPT
#/bin/bash
DIR=\`dirname \$0\`
java -jar \$DIR/$JAR \$@
EOF
chmod ug+x $SCRIPT
