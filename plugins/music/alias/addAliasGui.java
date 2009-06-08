package plugins.music.alias;

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

public class addAliasGui implements ActionListener
{
	private String artistBox;
	private JTextField newAlias = new JTextField();

	public addAliasGui()
	{
	}

	/**
	 *  Build the main plugin GUI.
	 */
	public JComponent makeGui()
	{
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
		pane.add(new JLabel("Master: "),c);

		c.gridx = 0;
		c.gridy = 1;
		pane.add(new JLabel("Alias: "),c);

// *******************************************
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		JComboBox cb = gc.makeCombo(" -Artist- ","SELECT name FROM artist ORDER BY name DESC",1,"ARTIST_BOX",this,Component.CENTER_ALIGNMENT);
		pane.add(cb,c);
		artistBox = (String)cb.getSelectedItem();

		c.gridx = 1;
		c.gridy = 1;
		pane.add(gc.makeText(newAlias,"ALIAS_NAME",this,Component.CENTER_ALIGNMENT),c);
// *******************************************

		c.gridx = 2;
		c.gridy = 2;
		pane.add(gc.buildButton("Add","gtk-add","ADD_ALIAS",this,Component.CENTER_ALIGNMENT),c);

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
