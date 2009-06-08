echo "Exporting classpath ..."
export CLASSPATH="/opt/Cloudscape10.0/lib/derby.jar:/opt/Cloudscape10.0/lib/derbytools.jar:${CLASSPATH}"
echo $CLASSPATH
echo "Compiling all .java files ..."
javac *.java
if [ ! "$1" = "" ]; then
echo "Running $1 ..."
java "$1" "$2" "$3" "$4"
else
echo "######################################"
echo "#  Please specify class file to run! #"
echo "######################################"
fi
