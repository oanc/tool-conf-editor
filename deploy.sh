#!/bin/bash

VERSION=`cat VERSION`
JAR=ToolConfEditor-$VERSION.jar
SCRIPT=target/tce

if [ ! -e target/$JAR ] ; then
	echo "The jar file does not exist. Please build the"
	echo "project before trying to deploy it."
	exit 1
fi

cat << EOF > $SCRIPT
#/bin/bash
DIR=\`dirname \$0\`
java -jar \$DIR/$JAR \$@
EOF
chmod ug+x $SCRIPT

cd target
echo "Generating tgz file."
tar czf ToolConfEditor-latest.tgz tce $JAR

echo "Uploading archives."
scp -P 22022 ToolConfEditor-latest.tgz suderman@anc.org:/home/www/anc/downloads
scp -P 22022 ToolConfEditor-latest.tgz suderman@anc.org:/home/www/anc/downloads/ToolConfEditor-$VERSION.tgz
