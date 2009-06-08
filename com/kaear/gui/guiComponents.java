package com.kaear.gui;

import com.kaear.common.*;
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

public class guiComponents
{
	public guiComponents()
	{
	}
	
	/**
	 *  Helper function to build combo boxes. 
	 */
	public JComboBox makeCombo(String preItem, String sqlstmt, int getItem, String command, ActionListener al, float alignment)
	{
		JComboBox dropList = new JComboBox();

		if (preItem != null)
			dropList.addItem(preItem);

		if (alignment == -1)
			alignment = Component.LEFT_ALIGNMENT;

		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt,"makeCombo() failed.");
			while (!rs.isLast())
			{
				rs.next();
				dropList.addItem(rs.getString(getItem));
			}
		} catch (Throwable e) { new exhandle("makeCombo() failed with: ", e); }
		
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		dropList.setAlignmentX(alignment);
		dropList.addActionListener(al);
		dropList.setActionCommand(command);
		return dropList;
	}

	/**
	 *  Helper function to build combo boxes. 
	 */
	public JComboBox makeCombo(String[] dropNames, String command, ActionListener al, float alignment)
	{
		if (alignment == -1)
			alignment = Component.LEFT_ALIGNMENT;

		JComboBox dropList = new JComboBox(dropNames);
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		dropList.setAlignmentX(alignment);
		dropList.addActionListener(al);
		dropList.setActionCommand(command);
		return dropList;
	}

	/**
	 *  Helper function to build text fields. 
	 */
	public JTextField makeText(JTextField thisText, String command, ActionListener al, float alignment)
	{
		if (alignment == -1)
			alignment = Component.LEFT_ALIGNMENT;

		thisText.setColumns(15);
		thisText.setMinimumSize(new Dimension(120,20));
		thisText.setPreferredSize(new Dimension(120,20));
		thisText.setMaximumSize(new Dimension(120,20));
		thisText.setAlignmentX(alignment);
		thisText.addActionListener(al);
		thisText.setActionCommand(command);
		return thisText;
	}
	
	/**
	 *  Helper function to build text fields (with other widths!). 
	 */
	public JTextField makeText(JTextField thisText, String command, ActionListener al, float alignment, int columns)
	{
		JTextField thisText1 = makeText(thisText,command,al,alignment);
		thisText1.setColumns(columns);
		return thisText1;
	}
	/**
	 *  Helper function to build side buttons.
	 */
	public JButton buildButton(String name, String image, String action, ActionListener al, float alignment)
	{
		ImageIcon myIcon = null;
		if (image != null) { myIcon = new images().makeImage(image); }

		if (alignment == -1)
			alignment = Component.LEFT_ALIGNMENT;
		
		JButton sideButton = new JButton(name, myIcon);
		sideButton.setMinimumSize(new Dimension(120,24));
		sideButton.setPreferredSize(new Dimension(120,24));
		sideButton.setMaximumSize(new Dimension(120,24));
		sideButton.setAlignmentX(alignment);
		sideButton.addActionListener(al);
		sideButton.setActionCommand(action);
		return sideButton;
	}

	/**
	 *  Helper function to build side toggle buttons.
	 */
	public JToggleButton toggleButton(String name, String image, String action, ActionListener al, float alignment)
	{
		ImageIcon myIcon = null;
		if (image != null) { myIcon = new images().makeImage(image); }

		if (alignment == -1)
			alignment = Component.LEFT_ALIGNMENT;
		
		JToggleButton toggleButton = new JToggleButton(name, myIcon);
		toggleButton.setMinimumSize(new Dimension(120,24));
		toggleButton.setPreferredSize(new Dimension(120,24));
		toggleButton.setMaximumSize(new Dimension(120,24));
		toggleButton.setAlignmentX(alignment);
		toggleButton.addActionListener(al);
		toggleButton.setActionCommand(action);
		return toggleButton;
	}	
	
	/**
	 *  Helper function to build side toggle buttons.
	 */
	public JSeparator buildSeparator()
	{
		JSeparator horizontalRule = new JSeparator();
		horizontalRule.setMinimumSize(new Dimension(120,18));
		horizontalRule.setPreferredSize(new Dimension(120,18));
		horizontalRule.setMaximumSize(new Dimension(120,18));
		horizontalRule.setAlignmentX(Component.LEFT_ALIGNMENT);
		return horizontalRule;
	}
	
	/**
	 *  Helper function to modify the way table fields are edited (Create a drop-down box)
	 */
	public void setUpTableField(JTable table, TableColumn tablecolumn, String sqlstmt)
	{
    	//Set up the editor for the artist list.
    	JComboBox comboBox = new JComboBox();

		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe(sqlstmt,"Format listing failed: ");
			while (!rs.isLast())
			{
				rs.next();
				comboBox.addItem(rs.getString(1));
			}
		} catch (Throwable e) { new exhandle("setUpTableField() failed with: ", e); }

    	tablecolumn.setCellEditor(new DefaultCellEditor(comboBox));
	}

}
