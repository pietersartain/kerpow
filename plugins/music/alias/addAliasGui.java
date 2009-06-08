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

public class addAliasGui implements ActionListener
{

	private int verbosityLevel = 0;
	private String artistBox = "";
	private JTextField newAlias;

	public addAliasGui()
	{
		verbosityLevel = musicMain.verbosityLevel;
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		JPanel pane = new JPanel();
		//pane.setLayout(new GridLayout(0,3,5,5));
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		
		c.ipadx = 5;
		c.ipady = 2;
		
		c.weightx = 0.5;
		c.weighty =  0.5;

// *******************************************		
		c.gridx = 0;
		c.gridy = 0;
		pane.add(new JLabel("Master: "),c);

		c.gridx = 0;
		c.gridy = 1;
		pane.add(new JLabel("Alias: "),c);

// *******************************************
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		pane.add(artistBox(),c);

		c.gridx = 1;
		c.gridy = 1;
		pane.add(aliasText(),c);

// *******************************************

		c.gridx = 2;
		c.gridy = 2;
		pane.add(buildButton("Add","gtk-add","ADD_ALIAS"),c);


		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );
		return pane;
	}

	private JComboBox artistBox()
	{
		JComboBox dropList = new JComboBox();
		
		dropList.addItem(" -Artist- ");
		
		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM artist ORDER BY name DESC","Artist listing failed: ");
			
			if (rs == null)
				System.out.println("I'm null, mummy!");
			
			while (!rs.isLast())
			{
				rs.next();
				//dropNames.add(0,rs.getString(2));
				dropList.addItem(rs.getString(2));
			}
		} catch (Throwable e) { new exhandle("addGui.artistBox() failed with: ", e, verbosityLevel); }

		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		dropList.addActionListener(this);
		dropList.setActionCommand("ARTIST_BOX");
		return dropList;
	}

	private JTextField aliasText()
	{
		newAlias = new JTextField(20);
		newAlias.setMinimumSize(new Dimension(120,20));
		newAlias.setPreferredSize(new Dimension(120,20));
		newAlias.setMaximumSize(new Dimension(120,20));
		newAlias.addActionListener(this);
		newAlias.setActionCommand("ALIAS_NAME");
		return newAlias;
	}

	/**
	 *  Helper function to build side buttons.
	 */
	private JButton buildButton(String name, String image, String action)
	{
		ImageIcon myIcon = null;
		if (image != null) { myIcon = new images().makeImage(image); }
		
		JButton sideButton = new JButton(name, myIcon);
		sideButton.setMinimumSize(new Dimension(100,24));
		sideButton.setPreferredSize(new Dimension(100,24));
		sideButton.setMaximumSize(new Dimension(100,24));
		sideButton.addActionListener(this);
		sideButton.setActionCommand(action);
		return sideButton;
	}

	// ******* This makes stuff happen on button clicks *********
    public void actionPerformed(ActionEvent e) 
	{
		String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        if ("ARTIST_BOX".equals(cmd)) 
		{
			JComboBox cb = (JComboBox)e.getSource();
			artistBox = (String)cb.getSelectedItem();
        } 
		else if ("ADD_ALIAS".equals(cmd)) 
		{
			
			aliasCommands ac = new aliasCommands(null,null);
						
			//if (mc.checkArtist(artist) == -1) { mc.addRecord("artist",artist); }
			//if (mc.checkAlbum(album) == -1) { mc.addRecord("album",album); }
			//if (mc.checkMusic(mc.checkAlbum(album),mc.checkArtist(artist)) == -1) { mc.addMusic(artist,album,formatBox,"0"); }
			
			if (ac.checkAlias(artistBox, newAlias.getText()) == -1) { ac.addAlias(artistBox, newAlias.getText()); }
			
			//mc.addMusic(artist,album,formatBox,"0");
			
			//kerpowObjectManager.runDB.sqlRun("insert into artist(name) values(" + artist + ")","Failed to make music combo: ");
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new aliasMainGui().updateTable(new aliasCommands(null,null).showDB());
            }});
		
			kerpowgui.updateStatusBar("Aliased \"" + newAlias.getText() + "\" to " + artistBox + ".");				
        }
    }
}
