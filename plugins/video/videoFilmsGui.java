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

public class videoFilmsGui implements ActionListener
{

	private int verbosityLevel = 0;
	private String searchCombo;
	private JTextField searchField = new JTextField();
	public static displayFilmsTableModel tableModel;
	public static JTable table;

	public videoFilmsGui()
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
		
		videoGuiComponents vgc = new videoGuiComponents();
		
		pane.add(vgc.buildSeparator());

		pane.add(vgc.buildButton("Show all","xfce-sound","SHOW_ALL",this,Component.LEFT_ALIGNMENT));
		pane.add(vgc.buildButton("Unarchived","xfce-sound","UNARCHIVED",this,Component.LEFT_ALIGNMENT));

		pane.add(vgc.buildSeparator());

		pane.add(vgc.makeText(searchField,"SEARCH_BOX",this,Component.LEFT_ALIGNMENT));
		
		String[] searchList = {"Name","Classification","Quality","Format","Location","Disks"};
		JComboBox cb = vgc.makeCombo(searchList,"CHANGE_SEARCH",this,Component.LEFT_ALIGNMENT);
		pane.add(cb);
		searchCombo = (String)cb.getSelectedItem();
		
		pane.add(vgc.buildButton("Find","file-manager","FIND_FILM",this,Component.LEFT_ALIGNMENT));
		
		pane.add(vgc.buildSeparator());

		pane.add(vgc.toggleButton("Add","gtk-add","ADD_FILM",this,Component.LEFT_ALIGNMENT));
		pane.add(vgc.buildButton("Delete","gtk-delete","DELETE_FILM",this,Component.LEFT_ALIGNMENT));
		
		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
		tableModel = new displayFilmsTableModel(new videoFilmsList(new videoCommands(null,null).showFilms()));
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
		else if ("FIND_FILM".equals(cmd) || "SEARCH_BOX".equals(cmd)) 
		{
	  		kerpowgui.updateStatusBar("Searching ...");
			//updateTable(new musicCommands(null,null).searchDB(searchBox,textField.getText()));
			Vector args = new Vector();
			args.add(0,searchField.getText());
			args.add(1,searchCombo);
			new videoCommands("findFilm",args);
        } 
		else if ("SHOW_ALL".equals(cmd)) 
		{
			updateTable(new videoCommands(null,null).showFilms());
        } 
		else if ("ADD_FILM".equals(cmd)) 
		{
			JToggleButton jtb = (JToggleButton)e.getSource();
			if (jtb.isSelected())
			{
				kerpowgui.addInfoBar(new addFilmsGui().makeGui(),100, (JToggleButton)e.getSource());
			} else {
				kerpowgui.clearInfoBar();
			}
		}
		else if ("DELETE_FILM".equals(cmd)) 
		{
			tableModel.deleteRow(table.getSelectedRow());
		}
}

// ************** This refreshes the table contents *****************
	public void updateTable(String sqlstmt) {
	
	//System.out.println(sqlstmt + " : " + tableModel + " : " + table);
	
		tableModel.setData(new videoFilmsList(sqlstmt));

		// Mess with the artist edit column
		//setUpArtistList(table, table.getColumnModel().getColumn(1));

		// Mess with the album edit column
		//setUpAlbumList(table, table.getColumnModel().getColumn(2));

		// Mess with the format edit column
		//setUpFormatList(table, table.getColumnModel().getColumn(3));
	}
}
