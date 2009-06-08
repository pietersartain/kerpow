#!/bin/bash

# build the classpath
for FILE in plugins/*.jar; do
   CLASSPATH="${CLASSPATH:+${CLASSPATH}:}$FILE"
done

for FILE in *.jar; do
   CLASSPATH="${CLASSPATH:+${CLASSPATH}:}$FILE"
done

echo $CLASSPATH

#java -cp "$CLASSPATH" -jar kerpow.jar
java -cp "$CLASSPATH" com.kaear.gui.kerpowgui

echo "kerpow! finished."
