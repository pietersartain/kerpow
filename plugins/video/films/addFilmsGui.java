/*
 * Created by:  Pieter Sartain
 *
 * Licensed under the GPL:
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 *
 */

package plugins.video.films;

import plugins.video.*;
import com.kaear.interfaces.*;
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

public class addFilmsGui implements ActionListener
{
	private String qualityBox = new String();
	private String formatBox = new String();
	private String classificationBox = new String();
	private JTextField nameText = new JTextField();
	private JTextField diskText = new JTextField();
	private JTextField locationText = new JTextField();

	public addFilmsGui()
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
		guiComponents vgc = new guiComponents();
		
		c.ipadx = 5;
		c.ipady = 2;
		
		c.weightx = 0.5;
		c.weighty =  0.5;

		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0,10,0,0);
// *******************************************		
		c.gridx = 0;
		c.gridy = 0;
		pane.add(new JLabel("Name: "),c);

		c.gridx = 0;
		c.gridy = 1;
		pane.add(new JLabel("Format: "),c);

		c.gridx = 0;
		c.gridy = 2;
		pane.add(new JLabel("Quality: "),c);		
// *******************************************
		c.gridx = 1;
		c.gridy = 0;
		pane.add(vgc.makeText(nameText,"NAME_BOX",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JComboBox cb = vgc.makeCombo(null, "SELECT name FROM videoformat ORDER BY id ASC",1,"CHANGE_FORMAT",this,Component.LEFT_ALIGNMENT);
		pane.add(cb,c);
		formatBox = (String)cb.getSelectedItem();

		c.gridx = 1;
		c.gridy = 2;
		cb = vgc.makeCombo(null, "SELECT name FROM quality ORDER BY id ASC",1,"CHANGE_QUALITY",this,Component.LEFT_ALIGNMENT);
		pane.add(cb,c);
		qualityBox = (String)cb.getSelectedItem();
// *******************************************
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(new JLabel("Disks: "),c);

		c.gridx = 2;
		c.gridy = 1;
		pane.add(new JLabel("Location: "),c);

		c.gridx = 2;
		c.gridy = 2;
		pane.add(new JLabel("Classification: "),c);
// *******************************************

		c.gridx = 3;
		c.gridy = 0;
		pane.add(vgc.makeText(diskText,"DISK_BOX",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 3;
		c.gridy = 1;
		pane.add(vgc.makeText(locationText,"LOCATION_BOX",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 3;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		cb = vgc.makeCombo(null, "SELECT name FROM classification ORDER BY id ASC",1,"CHANGE_CLASSIFICATION",this,Component.LEFT_ALIGNMENT);
		pane.add(cb,c);
		classificationBox = (String)cb.getSelectedItem();
// *******************************************
		c.gridx = 4;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		pane.add(vgc.buildButton("Add","gtk-add","ADD_FILM_REC",this,Component.CENTER_ALIGNMENT),c);


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
		
		if ("NAME_BOX".equals(cmd)) {
		} else if ("DISK_BOX".equals(cmd)) {
		} else if ("LOCATION_BOX".equals(cmd)) {
		} else if ("CHANGE_QUALITY".equals(cmd)) {
			JComboBox cb = (JComboBox)e.getSource();
			qualityBox = (String)cb.getSelectedItem();
		} else if ("CHANGE_FORMAT".equals(cmd)) {
			JComboBox cb = (JComboBox)e.getSource();
			formatBox = (String)cb.getSelectedItem();
		} else if ("CHANGE_CLASSIFICATION".equals(cmd)) {
			JComboBox cb = (JComboBox)e.getSource();
			classificationBox = (String)cb.getSelectedItem();
		} else if ("ADD_FILM_REC".equals(cmd)) {
			videoCommands vc = new videoCommands(null,null);
			vc.addFilm(nameText.getText(), Integer.parseInt(diskText.getText()), formatBox, qualityBox, locationText.getText(), classificationBox);
			//System.out.println(nameText.getText() + " : " + Integer.parseInt(diskText.getText()) + " : " + formatBox + " : " + qualityBox + " : " + locationText.getText() + " : " + classificationBox);			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new videoFilmsGui().updateTable(new videoCommands(null,null).showFilms());
            }});
		
			kerpowgui.updateStatusBar("Added \"" + nameText.getText() + "\" to \"" + locationText.getText() + "\"");
		}
    }
}
