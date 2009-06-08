package plugins.video;

import com.kaear.interfaces.*;
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

	public videoMainGui()
	{
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setOpaque(true);
		
		tabbedPane.addTab("Films", null, new plugins.video.films.videoFilmsGui().makeGui(), null);
		tabbedPane.addTab("Series", null, new plugins.video.series.videoSeriesGui().makeGui(), null);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		return tabbedPane;
	}
}
