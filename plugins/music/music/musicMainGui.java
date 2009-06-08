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

package plugins.music.music;

import plugins.music.*;

import com.kaear.interfaces.*;
import com.kaear.common.*;
import com.kaear.gui.*;
import com.kaear.res.images;

// The gui ...
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

// The rest...
import java.util.Properties;
import java.util.Vector;
import java.io.File;
import java.lang.Process;
import java.sql.ResultSet;

public class musicMainGui implements ActionListener
{
	private String searchBox;
	private JTextField textField = new JTextField();
	public static displayMusicTableModel tableModel;
	public static JTable table;

	public musicMainGui()
	{
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		// Left hand side
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.PAGE_AXIS));
		
		guiComponents gc = new guiComponents();

		pane.add(gc.buildSeparator());

		pane.add(gc.buildButton("Show all","xfce-sound","SHOW_ALL",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Unarchived","xfce-sound","UNARCHIVED",this,Component.LEFT_ALIGNMENT));

		pane.add(gc.buildSeparator());

		pane.add(gc.makeText(textField,"SEARCH_BOX",this,Component.LEFT_ALIGNMENT));

		String[] dropNames = { "Artist", "Album", "Format", "Disc" };
		JComboBox cb = gc.makeCombo(dropNames,"CHANGE_SEARCH",this,Component.LEFT_ALIGNMENT);
		pane.add(cb);
		searchBox = (String)cb.getSelectedItem();

		pane.add(gc.buildButton("Find","file-manager","FIND_MUSIC",this,Component.LEFT_ALIGNMENT));
		
		pane.add(gc.buildSeparator());

		pane.add(gc.buildButton("Update","gtk-refresh","UPDATE_MUSIC",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.toggleButton("Add","gtk-add","ADD_MUSIC",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Clean","xfce-trash_full","CLEAN_MUSIC",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Delete","gtk-delete","DELETE_MUSIC",this,Component.LEFT_ALIGNMENT));
		
		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displayMusicTableModel(new musicList(new musicCommands(null,null).showDB()));
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(700, 70));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

		// Mess with the artist edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(1),"SELECT name FROM artist ORDER BY name ASC");

		// Mess with the album edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(2),"SELECT name FROM album ORDER BY name ASC");

		// Mess with the format edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(3),"SELECT name FROM format ORDER BY name ASC");

        //Add the scroll pane to this panel.
        pane1.add(scrollPane);
 
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pane, pane1);
		return splitPane;
	}

// ******* This makes stuff happen on button clicks *********
    public void actionPerformed(ActionEvent e) 
	{
        String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        if ("UPDATE_MUSIC".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Updating ... ");
			Vector args = new Vector();
			args.add(0,musicMain.mp.getMusicPath());
			args.add(1,musicMain.mp.getExcludePath());
			new musicCommands("updateDB",args);
        } 
		else if ("CHANGE_SEARCH".equals(cmd)) 
		{
			JComboBox cb = (JComboBox)e.getSource();
			searchBox = (String)cb.getSelectedItem();
        }
		else if ("FIND_MUSIC".equals(cmd) || "SEARCH_BOX".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Searching ...");
			//updateTable(new musicCommands(null,null).searchDB(searchBox,textField.getText()));
			Vector args = new Vector();
			args.add(0,searchBox);
			args.add(1,textField.getText());
			new musicCommands("searchDB",args);
        } 
		else if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new musicCommands(null,null).showDB());
        } 
		else if ("UNARCHIVED".equals(cmd)) 
		{
			updateTable(new musicCommands(null,null).showUnarchived());
		}
		else if ("ADD_MUSIC".equals(cmd)) 
		{
			JToggleButton jtb = (JToggleButton)e.getSource();
			if (jtb.isSelected())
			{
				kerpowgui.addInfoBar(new addGui().makeGui(),100, (JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
		}
		else if ("DELETE_MUSIC".equals(cmd)) 
		{
			tableModel.deleteRow(table.getSelectedRow());
		}
		else if ("CLEAN_MUSIC".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Cleaning ...");
			new musicCommands("cleanMusic",null);
		}
}

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) {
		tableModel.setData(new musicList(sqlstmt));

		guiComponents gc = new guiComponents();

		// Mess with the artist edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(1),"SELECT name FROM artist ORDER BY name ASC");

		// Mess with the album edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(2),"SELECT name FROM album ORDER BY name ASC");

		// Mess with the format edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(3),"SELECT name FROM format ORDER BY name ASC");
	}
}
