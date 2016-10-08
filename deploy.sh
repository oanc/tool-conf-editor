#!/bin/bash

VERSION=`cat VERSION`
JAR=ToolConfEditor-$VERSION.jar
SCRIPT=target/tce

cat << EOF > $SCRIPT
#/bin/bash
DIR=\`dirname \$0\`
java -jar \$DIR/$JAR \$@
EOF
chmod ug+x $SCRIPT

cd target
tar czf ToolConfEditor-latest.tgz tce $JAR
scp ToolConfEditor-latest.tgz suderman@anc.org:/home/www/anc/downloads
scp ToolConfEditory-latest.tgz suderman@anc.org:/home/www/anc/downloads/ToolConfEditor-$VERSION.tgz
