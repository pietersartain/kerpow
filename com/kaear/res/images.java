package com.kaear.res;

import com.kaear.common.*;
import javax.swing.ImageIcon;

public class images
{

	public images()
	{
	}
	
	public ImageIcon makeImage(String myImage)
	{
		ImageIcon myIcon = null;
		try { myIcon = new ImageIcon(ClassLoader.getSystemResource("com/kaear/res/" + myImage + ".png"));}
		catch (Throwable e) { new exhandle("Icon creation failed: ", e); }
		return myIcon;
	}
}
