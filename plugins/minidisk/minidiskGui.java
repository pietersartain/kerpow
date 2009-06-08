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

package plugins.minidisk;

import com.kaear.common.*;
import com.kaear.interfaces.*;
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

public class minidiskGui implements ActionListener, guiPlugin
{
	private String searchCombo;
	private JTextField searchField = new JTextField();
	public static displayTableModel tableModel;
	public static JTable table;

	public minidiskGui()
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

		pane.add(gc.buildSeparator());

		pane.add(gc.makeText(searchField,"SEARCH_BOX",this,Component.LEFT_ALIGNMENT));
		
		String[] searchList = {"Name","Number","Contents"};
		JComboBox cb = gc.makeCombo(searchList,"CHANGE_SEARCH",this,Component.LEFT_ALIGNMENT);
		pane.add(cb);
		searchCombo = (String)cb.getSelectedItem();
		
		pane.add(gc.buildButton("Find","file-manager","FIND_MD",this,Component.LEFT_ALIGNMENT));
		
		pane.add(gc.buildSeparator());

		pane.add(gc.toggleButton("Add","gtk-add","ADD_MD",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Delete","gtk-delete","DELETE_MD",this,Component.LEFT_ALIGNMENT));
		
		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displayTableModel(new minidiskList(new minidiskCommands(null,null).showAll()));
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(700, 70));
		
       //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

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
        if ("CHANGE_SEARCH".equals(cmd)) 
		{
			JComboBox cb = (JComboBox)e.getSource();
			searchCombo = (String)cb.getSelectedItem();
        } 
		else if ("FIND_MD".equals(cmd) || "SEARCH_BOX".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Searching ...");
			Vector args = new Vector();
			args.add(0,searchField.getText());
			args.add(1,searchCombo);
			new minidiskCommands("findMD",args);
        } 
		else if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new minidiskCommands(null,null).showAll());
        } 
		else if ("ADD_MD".equals(cmd)) 
		{
			JToggleButton jtb = (JToggleButton)e.getSource();
			if (jtb.isSelected())
			{
				kerpowgui.addInfoBar(new addGui().makeGui(),100, (JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
		}
		else if ("DELETE_MD".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Deleting ...");
			tableModel.deleteRow(table.getSelectedRow());
		}
}

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) 
	{
		tableModel.setData(new minidiskList(sqlstmt));
	}
}
