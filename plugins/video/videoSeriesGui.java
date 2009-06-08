package plugins.video;

import com.kaear.cli.*;
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

public class videoSeriesGui implements ActionListener
{

	private int verbosityLevel = 0;
	private String searchBox = "Name";
	private JTextField textField;
	public static displaySeriesTableModel tableModel;
	public static JTable table;

	public videoSeriesGui()
	{
		verbosityLevel = 2;
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		// Left hand side
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.PAGE_AXIS));

		pane.add(buildSeparator());

		pane.add(buildSideButton("Show all","xfce-sound","SHOW_ALL"));
		pane.add(buildSideButton("Unarchived","xfce-sound","UNARCHIVED"));

		pane.add(buildSeparator());

		pane.add(buildSearchBox());
		pane.add(buildDropList());
		pane.add(buildSideButton("Find","file-manager","FIND_FILM"));
		
		pane.add(buildSeparator());

		pane.add(toggleButton("Add","gtk-add","ADD_FILM"));
		pane.add(buildSideButton("Delete","gtk-delete","DELETE_FILM"));
		
		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displaySeriesTableModel(new videoSeriesList(new videoCommands(null,null).showSeries()));
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(700, 70));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        pane1.add(scrollPane);
 
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pane, pane1);
		return splitPane;
	}

	private JSeparator buildSeparator()
	{
		JSeparator horizontalRule = new JSeparator();
		horizontalRule.setMinimumSize(new Dimension(120,18));
		horizontalRule.setPreferredSize(new Dimension(120,18));
		horizontalRule.setMaximumSize(new Dimension(120,18));
		horizontalRule.setAlignmentX(Component.LEFT_ALIGNMENT);
		return horizontalRule;
	}
	
	private JTextField buildSearchBox()
	{
		textField = new JTextField(20);
		textField.setMinimumSize(new Dimension(120,20));
		textField.setPreferredSize(new Dimension(120,20));
		textField.setMaximumSize(new Dimension(120,20));
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		textField.addActionListener(this);
		textField.setActionCommand("SEARCH_BOX");
		return textField;
	}

	private JComboBox buildDropList()
	{
		String[] dropNames = { "Artist", "Album", "Format", "Disc" };
		JComboBox dropList = new JComboBox(dropNames);
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		dropList.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropList.addActionListener(this);
		dropList.setActionCommand("CHANGE_SEARCH");
		return dropList;
	}

	/**
	 *  Helper function to build side buttons.
	 */
	private JButton buildSideButton(String name, String image, String action)
	{
		ImageIcon myIcon = new images().makeImage(image);
		
		JButton sideButton = new JButton(name, myIcon);
		sideButton.setMinimumSize(new Dimension(120,24));
		sideButton.setPreferredSize(new Dimension(120,24));
		sideButton.setMaximumSize(new Dimension(120,24));
		sideButton.addActionListener(this);
		sideButton.setActionCommand(action);
		return sideButton;
	}

	/**
	 *  Helper function to build side buttons.
	 */
	private JToggleButton toggleButton(String name, String image, String action)
	{
		ImageIcon myIcon = null;
		if (image != null) { myIcon = new images().makeImage(image); }
		
		JToggleButton toggleButton = new JToggleButton(name, myIcon);
		toggleButton.setMinimumSize(new Dimension(120,24));
		toggleButton.setPreferredSize(new Dimension(120,24));
		toggleButton.setMaximumSize(new Dimension(120,24));
		toggleButton.addActionListener(this);
		toggleButton.setActionCommand(action);
		return toggleButton;
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
			searchBox = (String)cb.getSelectedItem();
        } 
		else if ("FIND_SERIES".equals(cmd) || "SEARCH_BOX".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Searching ...");
			Vector args = new Vector();
			args.add(0,searchBox);
			args.add(1,textField.getText());
			//new musicCommands("searchDB",args);
        } 
		else if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new videoCommands(null,null).showSeries());
        } 
		else if ("ADD_SERIES".equals(cmd)) 
		{
			/*
			JToggleButton jtb = (JToggleButton)e.getSource();
			if (jtb.isSelected())
			{
				kerpowgui.addInfoBar(new addGui().makeGui(),100, (JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
			*/
		}
		else if ("DELETE_SERIES".equals(cmd)) 
		{
			tableModel.deleteRow(table.getSelectedRow());
		}
}

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) {
	
	//System.out.println(sqlstmt + " : " + tableModel + " : " + table);
	
		tableModel.setData(new videoSeriesList(sqlstmt));

		// Mess with the artist edit column
		//setUpArtistList(table, table.getColumnModel().getColumn(1));

		// Mess with the album edit column
		//setUpAlbumList(table, table.getColumnModel().getColumn(2));

		// Mess with the format edit column
		//setUpFormatList(table, table.getColumnModel().getColumn(3));
	}
}