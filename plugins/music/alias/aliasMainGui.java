package plugins.music.alias;

import plugins.music.*;

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

public class aliasMainGui implements ActionListener
{

	private int verbosityLevel = 0;
	public static displayAliasTableModel tableModel;
	public static JTable table;

	public aliasMainGui()
	{
		verbosityLevel = musicMain.verbosityLevel;
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

		pane.add(buildSeparator());

		pane.add(toggleButton("Add","gtk-add","ADD_ALIAS"));
//		pane.add(buildSideButton("Clean","gtk-refresh","CLEAN_ALIAS"));
		pane.add(buildSideButton("Delete","gtk-delete","DELETE_ALIAS"));

/*
		pane.add(buildSeparator());
		
		pane.add(buildSideButton("Master","file-manager","SET_AS_MASTER"));
		
		pane.add(buildSeparator());
*/		

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
		setUpArtistList(table, table.getColumnModel().getColumn(1));

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
		JTextField textField = new JTextField(20);
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
		String[] dropNames = { "Artist", "Album", "Format", "Misc" };
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

	public void setUpArtistList(JTable table, TableColumn artistColumn) 
	{
    	//Set up the editor for the artist list.
    	JComboBox comboBox = new JComboBox();

		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM artist ORDER BY name ASC","Artist listing failed: ");
			while (!rs.isLast())
			{
				rs.next();
				comboBox.addItem(rs.getString(2));
			}
		} catch (Throwable e) { new exhandle("aliasMainGui.setUpArtistList() failed with: ", e, verbosityLevel); }

    	artistColumn.setCellEditor(new DefaultCellEditor(comboBox));
	}
	
// ******* This makes stuff happen on button clicks *********
    public void actionPerformed(ActionEvent e) 
	{
        String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        /*if ("SET_AS_MASTER".equals(cmd)) 
		{
			Vector args = new Vector();
			args.add(0,tableModel.getValueAt(table.getSelectedRow(),1));
			args.add(0,tableModel.getValueAt(table.getSelectedRow(),2));
			
			new aliasCommands("setMaster",args);
        } 
		else*/
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
		
		// Mess with the artist edit column
		setUpArtistList(table, table.getColumnModel().getColumn(1));
			
	}


}
