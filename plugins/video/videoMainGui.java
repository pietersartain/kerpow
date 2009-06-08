package plugins.video;

import com.kaear.common.*;
import com.kaear.gui.*;

// The gui ...
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class videoMainGui implements guiPlugin
{

	private int verbosityLevel = 0;

	public videoMainGui()
	{
		//verbosityLevel = musicMain.verbosityLevel;
		verbosityLevel = 2;
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setOpaque(true);
		
		tabbedPane.addTab("Films", null, new videoFilmsGui().makeGui(), null);
		tabbedPane.addTab("Series", null, new videoSeriesGui().makeGui(), null);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		return tabbedPane;
	}
}
