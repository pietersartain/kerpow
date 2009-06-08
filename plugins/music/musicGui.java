package plugins.music;

import plugins.music.music.*;
import plugins.music.alias.*;

import com.kaear.interfaces.*;

// The gui ...
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class musicGui implements guiPlugin
{

	public musicGui()
	{
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setOpaque(true);
		
		tabbedPane.addTab("Music", null, new musicMainGui().makeGui(), null);
		tabbedPane.addTab("Alias", null, new aliasMainGui().makeGui(), null);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		return tabbedPane;
	}
}
