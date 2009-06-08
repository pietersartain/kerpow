/*
 * Created by:  Pieter Sartain
 *
 * Licensed under the GPL:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *
 */

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
