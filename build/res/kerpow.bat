
for  %%f  in  (plugins\*.jar)   do   set  classpath={%classpath% + ":" + %%f}

java -cp %classpath% com.kaear.gui.kerpowgui
