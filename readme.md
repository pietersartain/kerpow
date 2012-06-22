# kerpow

My own caffeine-powered archiving database program. Built using a plugable architecture, the core program provides APIs and the database hooks, while the individual plugins provide the archival functionality.

kerpow originally used the Cloudscape embedded database as a backend, but migrated to use Apache Derby after IBM open sourced it.

## Build tools

The dev script symlinked from build-tools into the root directory is the main entry point to controlling builds. The options available from the dev script are as follows:

* ./dev com/kaear/gui/kerpowgui # compile and run the main program
* ./dev com/kaear/gui/kerpowgui --clean # remove *.class files
* ./dev com/kaear/gui/kerpowgui --make-package # zip up a distributable version of kerpow

## Current state

The HEAD version doesn't appear to work, although it builds fine. The issue will be related to the network capability I was attempting to add to the program.