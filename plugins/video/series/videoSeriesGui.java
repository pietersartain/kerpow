package plugins.video.series;

import plugins.video.*;
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
	private String searchCombo;
	private JTextField searchField = new JTextField();
	public static displaySeriesTableModel tableModel;
	public static JTable table;

	public videoSeriesGui()
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

		pane.add(gc.makeText(searchField,"SEARCH_BOX",this,Component.LEFT_ALIGNMENT));
		String[] searchList = {"Name","Classification","Quality","Format","Location","Disks"};
		JComboBox cb = gc.makeCombo(searchList,"CHANGE_SEARCH",this,Component.LEFT_ALIGNMENT);
		pane.add(cb);
		searchCombo = (String)cb.getSelectedItem();
		pane.add(gc.buildButton("Find","file-manager","FIND_SERIES",this,Component.LEFT_ALIGNMENT));
		
		pane.add(gc.buildSeparator());

		pane.add(gc.toggleButton("Add","gtk-add","ADD_SERIES",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Clean","gtk-refresh","CLEAN_SERIES",this,Component.LEFT_ALIGNMENT));
		pane.add(gc.buildButton("Delete","gtk-delete","DELETE_SERIES",this,Component.LEFT_ALIGNMENT));
		
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

		// Format
		gc.setUpTableField(table, table.getColumnModel().getColumn(5),"SELECT name FROM videoformat ORDER BY id ASC");
		// Quality
		gc.setUpTableField(table, table.getColumnModel().getColumn(6),"SELECT name FROM quality ORDER BY id ASC");
		// Classification
		gc.setUpTableField(table, table.getColumnModel().getColumn(8),"SELECT name FROM classification ORDER BY name ASC");

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
		else if ("FIND_SERIES".equals(cmd) || "SEARCH_BOX".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Searching ...");
			Vector args = new Vector();
			args.add(0,searchField.getText());
			args.add(1,searchCombo);
			new videoCommands("searchDB",args);
        } 
		else if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new videoCommands(null,null).showSeries());
        } 
		else if ("ADD_SERIES".equals(cmd)) 
		{
			JToggleButton jtb = (JToggleButton)e.getSource();
			if (jtb.isSelected())
			{
				kerpowgui.addInfoBar(new addSeriesGui().makeGui(),100, (JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
		}
		else if ("CLEAN_SERIES".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Cleaning series ...");
			Vector args = new Vector();
			args.add(0,"series");
			new videoCommands("cleanRecords",args);
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
		guiComponents gc = new guiComponents();

		// Format
		gc.setUpTableField(table, table.getColumnModel().getColumn(5),"SELECT name FROM videoformat ORDER BY id ASC");
		// Quality
		gc.setUpTableField(table, table.getColumnModel().getColumn(6),"SELECT name FROM quality ORDER BY id ASC");
		// Classification
		gc.setUpTableField(table, table.getColumnModel().getColumn(8),"SELECT name FROM classification ORDER BY name ASC");
	}
}
