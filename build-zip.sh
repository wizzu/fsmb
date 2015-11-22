#!/bin/bash

ZIP=fsmb-package.zip
CONTENTS=(\
  README.md \
  pom.xml \
  src/main/webapp/WEB-INF/web.xml \
  src/main/java/org/noname/fsmb/*.java \
  src/main/java/org/noname/fsmb/*/*.java \
  src/main/java/org/noname/fsmb/*/*/*.java \
  src/test/java/org/noname/fsmb/*.java \
  src/test/java/org/noname/fsmb/*/*.java \
  src/test/java/org/noname/fsmb/*/*/*.java \
  samples/*
  build-zip.sh
)

if [ -f $ZIP ]; then
  echo "Removing previous $ZIP"
  rm $ZIP
fi

echo "Creating $ZIP"
zip $ZIP ${CONTENTS[@]}

echo
echo "$ZIP created"

