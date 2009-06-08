package plugins.music.music;

import java.io.*;

public class dirFilter implements FilenameFilter 
{
	public boolean accept(File directory, String filename)
	{
		if (new File(directory, filename).isDirectory()) return true;
   		return false;
	}

}

