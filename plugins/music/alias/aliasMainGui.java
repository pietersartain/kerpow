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

package plugins.music.alias;

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

public class aliasMainGui implements ActionListener
{

	public static displayAliasTableModel tableModel;
	public static JTable table;

	public aliasMainGui()
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

		pane.add(gc.toggleButton("Add","gtk-add","ADD_ALIAS",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Delete","gtk-delete","DELETE_ALIAS",this,Component.LEFT_ALIGNMENT));

		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displayAliasTableModel(new aliasList(new aliasCommands(null,null).showDB()));
		table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(700, 70));

		// Mess with the artist edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(1),"SELECT name FROM artist ORDER BY name ASC");

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
		if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new aliasCommands(null,null).showDB());
        } 
		else if ("ADD_ALIAS".equals(cmd)) 
		{
			if (((JToggleButton)e.getSource()).isSelected())
			{
				kerpowgui.addInfoBar(new addAliasGui().makeGui(),100,(JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
		}
		else if ("DELETE_ALIAS".equals(cmd)) 
		{
			tableModel.deleteRow(table.getSelectedRow());
		}
    }

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) {
		//table.setModel(new displayAliasTableModel(new aliasList(sqlstmt)));
		tableModel.setData(new aliasList(sqlstmt));
		guiComponents gc = new guiComponents();
		
		// Mess with the artist edit column
		gc.setUpTableField(table, table.getColumnModel().getColumn(1),"SELECT name FROM artist ORDER BY name ASC");
			
	}


}
