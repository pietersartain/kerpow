package plugins.music.music;

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

public class addGui implements ActionListener
{

	private int verbosityLevel = 0;
	private String albumBox = "New album ...";
	private String artistBox = "New artist ...";
	private String formatBox = "CD Original";
	private JTextField newArtist;
	private JTextField newAlbum;

	public addGui()
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
		pane.add(new JLabel("Artist: "),c);

		c.gridx = 0;
		c.gridy = 1;
		pane.add(new JLabel("Album: "),c);

		c.gridx = 0;
		c.gridy = 2;
		pane.add(new JLabel("Format: "),c);		
// *******************************************
		c.gridx = 1;
		c.gridy = 0;
		pane.add(artistBox(),c);

		c.gridx = 1;
		c.gridy = 1;
		pane.add(albumBox(),c);

		c.gridx = 1;
		c.gridy = 2;
		pane.add(formatCombo(),c);
// *******************************************
		c.gridx = 2;
		c.gridy = 0;
		pane.add(artistText(),c);

		c.gridx = 2;
		c.gridy = 1;
		pane.add(albumText(),c);

// *******************************************

		c.gridx = 3;
		c.gridy = 2;
		pane.add(buildButton("Add","gtk-add","ADD_MUSIC_REC"),c);


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
		//String[] dropNames = { "CD Original", "CD", "MP3"};
		//Vector dropNames = new Vector();
		
		JComboBox dropList = new JComboBox();
		dropList.addItem("New artist ...");
	
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

		
		//dropNames.add(0,"New artist ...");
		
		//String[] nameList = new String[dropNames.size()];
		//dropNames.copyInto(nameList);
		
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		//dropList.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropList.addActionListener(this);
		dropList.setActionCommand("ARTIST_BOX");
		return dropList;
	}

	private JTextField artistText()
	{
		newArtist = new JTextField(20);
		newArtist.setMinimumSize(new Dimension(120,20));
		newArtist.setPreferredSize(new Dimension(120,20));
		newArtist.setMaximumSize(new Dimension(120,20));
		//newArtist.setAlignmentX(Component.LEFT_ALIGNMENT);
		newArtist.addActionListener(this);
		newArtist.setActionCommand("NEW_ARTIST");
		return newArtist;
	}

	private JComboBox albumBox()
	{
		//String[] dropNames = { "CD Original", "CD", "MP3"};
//		Vector dropNames = new Vector();
		JComboBox dropList = new JComboBox();
		dropList.addItem("New album ...");

		try {
			ResultSet rs = kerpowObjectManager.runDB.sqlExe("SELECT * FROM album ORDER BY name DESC","Album listing failed: ");
			while (!rs.isLast())
			{
				rs.next();
				//dropNames.add(0,rs.getString(2));
				dropList.addItem(rs.getString(2));
			}
		} catch (Throwable e) { new exhandle("addGui.albumBox() failed with: ", e, verbosityLevel); }


//		dropNames.add(0,"New album ...");
		
//		String[] nameList = new String[dropNames.size()];
//		dropNames.copyInto(nameList);
		
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		//dropList.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropList.addActionListener(this);
		dropList.setActionCommand("ALBUM_BOX");
		return dropList;
	}
	
	private JTextField albumText()
	{
		newAlbum = new JTextField(20);
		newAlbum.setMinimumSize(new Dimension(120,20));
		newAlbum.setPreferredSize(new Dimension(120,20));
		newAlbum.setMaximumSize(new Dimension(120,20));
		//newAlbum.setAlignmentX(Component.LEFT_ALIGNMENT);
		newAlbum.addActionListener(this);
		newAlbum.setActionCommand("NEW_ALBUM");
		return newAlbum;
	}

	private JComboBox formatCombo()
	{
		String[] dropNames = { "CD Original", "CD", "MP3"};
		JComboBox dropList = new JComboBox(dropNames);
		dropList.setSelectedIndex(0);
		dropList.setMinimumSize(new Dimension(120,20));
		dropList.setPreferredSize(new Dimension(120,20));
		dropList.setMaximumSize(new Dimension(120,20));
		dropList.setAlignmentX(Component.LEFT_ALIGNMENT);
		dropList.addActionListener(this);
		dropList.setActionCommand("FORMAT_BOX");
		return dropList;
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
			//System.out.println("Artist changing: " + artistBox);
			if (artistBox.equals("New artist ..."))
			{
				newArtist.setEnabled(true);
				newArtist.setBackground(Color.WHITE);
			} else {
				newArtist.setEnabled(false);
				newArtist.setBackground(Color.LIGHT_GRAY);
			}
        } 
		else if ("ALBUM_BOX".equals(cmd)) 
		{
			JComboBox cb = (JComboBox)e.getSource();
			albumBox = (String)cb.getSelectedItem();
			//System.out.println("Album changing: " + albumBox);
			if (albumBox.equals("New album ..."))
			{
				newAlbum.setEnabled(true);
				newAlbum.setBackground(Color.WHITE);
			} else {
				newAlbum.setEnabled(false);
				newAlbum.setBackground(Color.LIGHT_GRAY);
			}
        }
		else if ("FORMAT_BOX".equals(cmd)) 
		{
			JComboBox cb = (JComboBox)e.getSource();
			formatBox = (String)cb.getSelectedItem();
        } 
		else if ("ADD_MUSIC_REC".equals(cmd)) 
		{
			String album = "";
			String artist = "";

			if (artistBox.equals("New artist ..."))
			{
				artist = newArtist.getText();
			} else {
				artist = artistBox;
			}
						
			if (albumBox.equals("New album ..."))
			{
				album = newAlbum.getText();
			} else {
				album = albumBox;
			}
			
			musicCommands mc = new musicCommands(null,null);
			//System.out.println(mc.checkArtist(artist) + " : " + mc.checkAlbum(album) + " : " + mc.checkMusic(mc.checkAlbum(album),mc.checkArtist(artist)));
			
			if (mc.checkArtist(artist) == -1) { mc.addRecord("artist",artist); }
			if (mc.checkAlbum(album) == -1) { mc.addRecord("album",album); }
			if (mc.checkMusic(mc.checkAlbum(album),mc.checkArtist(artist)) == -1) { mc.addMusic(artist,album,formatBox,"0"); }
			
			//mc.addMusic(artist,album,formatBox,"0");
			
			//kerpowObjectManager.runDB.sqlRun("insert into artist(name) values(" + artist + ")","Failed to make music combo: ");
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				//new musicMain().getGui().updateTable(new musicCommands(null,null).showDB());
				new musicMainGui().updateTable(new musicCommands(null,null).showDB());
            }});
		
			kerpowgui.updateStatusBar("Added \"" + album + "\" by " + artist + ".");				
        }
    }
}
