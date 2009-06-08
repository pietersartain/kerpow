package plugins.music;

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

public class aliasGui implements guiPlugin, ActionListener
{

	private int verbosityLevel = 0;
	private int addmusic = 0;
	private int addalias = 0;
	private String searchBox = "Artist";
	private JTextField textField;
	public displayTableModel tableModel;
	public JTable table;

	public aliasGui()
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

		pane.add(buildSearchBox());
		pane.add(buildDropList());
		pane.add(buildSideButton("Find","file-manager","FIND_MUSIC"));
		
		pane.add(buildSeparator());

		//pane.add(buildSideButton("Add","gtk-add","ADD_MUSIC"));
		pane.add(toggleButton("Add","gtk-add","ADD_MUSIC"));
		pane.add(buildSideButton("Update","gtk-refresh","UPDATE_MUSIC"));

		pane.add(buildSeparator());
		
		pane.add(buildSideButton("Show aliases","xfce4-menueditor","SHOW_ALIAS"));
		pane.add(buildSideButton("Add alias","xfce4-menueditor","ADD_ALIAS"));

		pane.add(buildSeparator());
		

		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displayTableModel(new musicList(new musicCommands(null,null).showDB()));
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
		else if ("ADD_MUSIC".equals(cmd)) 
		{
			if (addmusic == 0 && addalias == 0)
			{
				//updateTable(new musicCommands(null,null).showDB());
				addmusic = 1;
				kerpowgui.addInfoBar(new addGui().makeGui(),100);
			} else {
				if (addalias == 1) {
					kerpowgui.clearInfoBar();
					addmusic = 1;
					kerpowgui.addInfoBar(new addGui().makeGui(),100);
				} else {						
				addmusic = 0;
				kerpowgui.clearInfoBar();
				}
			}
		}
		else if ("ADD_ALIAS".equals(cmd)) 
		{
			if (addalias == 0)
			{
				//updateTable(new musicCommands(null,null).showDB());
				addalias = 1;
				kerpowgui.addInfoBar(new addGui().makeGui(),100);
			} else {
				addalias = 0;
				kerpowgui.clearInfoBar();
			}
		}
		else if ("SHOW_ALIAS".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Executing ...");
			Vector args = new Vector();
			args.add(0,"SELECT * FROM alias ORDER BY alias DESC");
			new musicCommands("runSQL",args);
		}
		
    }

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) {
		table.setModel(new displayTableModel(new musicList(sqlstmt)));
	}


}
