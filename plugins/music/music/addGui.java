package plugins.music.music;

import plugins.music.*;

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
	private String albumBox;
	private String artistBox;
	private String formatBox;
	private JTextField newArtist = new JTextField();
	private JTextField newAlbum = new JTextField();

	public addGui()
	{
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
		// Left hand side
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		guiComponents gc = new guiComponents();
		
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
		JComboBox cb = gc.makeCombo("New artist ...","SELECT name FROM artist ORDER BY name DESC",1,"ARTIST_BOX",this,Component.CENTER_ALIGNMENT);
		pane.add(cb,c);
		artistBox = (String)cb.getSelectedItem();

		c.gridx = 1;
		c.gridy = 1;
		cb = gc.makeCombo("New album ...","SELECT name FROM album ORDER BY name DESC",1,"ALBUM_BOX",this,Component.CENTER_ALIGNMENT);
		pane.add(cb,c);
		albumBox = (String)cb.getSelectedItem();

		c.gridx = 1;
		c.gridy = 2;
		cb = gc.makeCombo(null,"SELECT name FROM format ORDER BY name DESC",1,"FORMAT_BOX",this,Component.CENTER_ALIGNMENT);
		pane.add(cb,c);
		formatBox = (String)cb.getSelectedItem();
// *******************************************
		c.gridx = 2;
		c.gridy = 0;
		pane.add(gc.makeText(newArtist,"NEW_ARTIST",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 2;
		c.gridy = 1;
		pane.add(gc.makeText(newAlbum,"NEW_ALBUM",this,Component.CENTER_ALIGNMENT),c);

// *******************************************

		c.gridx = 3;
		c.gridy = 2;
		pane.add(gc.buildButton("Add","gtk-add","ADD_MUSIC_REC",this,Component.CENTER_ALIGNMENT),c);

		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );
		return pane;
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
			
			if (mc.checkArtist(artist) == -1) { mc.addRecord("artist",artist); }
			if (mc.checkAlbum(album) == -1) { mc.addRecord("album",album); }
			if (mc.checkMusic(mc.checkAlbum(album),mc.checkArtist(artist)) == -1) { mc.addMusic(artist,album,formatBox,"0"); }
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new musicMainGui().updateTable(new musicCommands(null,null).showDB());
            }});
		
			kerpowgui.updateStatusBar("Added \"" + album + "\" by " + artist + ".");				
        }
    }
}
