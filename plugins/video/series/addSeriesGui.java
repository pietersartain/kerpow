package plugins.video.series;

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

public class addSeriesGui implements ActionListener
{
	private String qualityBox;
	private String formatBox;
	private String classificationBox;
	private JTextField nameText = new JTextField();
	private JTextField diskText = new JTextField();
	private JTextField locationText = new JTextField();
	private JTextField seasonText = new JTextField();
	private JTextField episodeText = new JTextField();

	public addSeriesGui()
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
		pane.add(vgc.makeText(nameText,"NAME_BOX",this,Component.CENTER_ALIGNMENT,10),c);

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
		pane.add(vgc.makeText(diskText,"DISK_BOX",this,Component.CENTER_ALIGNMENT,10),c);

		c.gridx = 3;
		c.gridy = 1;
		pane.add(vgc.makeText(locationText,"LOCATION_BOX",this,Component.CENTER_ALIGNMENT,10),c);

		c.gridx = 3;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		cb = vgc.makeCombo(null, "SELECT name FROM classification ORDER BY id ASC",1,"CHANGE_CLASSIFICATION",this,Component.LEFT_ALIGNMENT);
		pane.add(cb,c);
		classificationBox = (String)cb.getSelectedItem();
// *******************************************
		
		c.gridx = 4;
		c.gridy = 0;
		pane.add(new JLabel("Season: "),c);
		
		c.gridx = 4;
		c.gridy = 1;
		pane.add(new JLabel("Episodes: "),c);
		
// *******************************************
		c.gridx = 5;
		c.gridy = 0;
		pane.add(vgc.makeText(seasonText,"SEASON_BOX",this,Component.CENTER_ALIGNMENT,10),c);
		
		c.gridx = 5;
		c.gridy = 1;
		pane.add(vgc.makeText(episodeText,"EPISODE_BOX",this,Component.CENTER_ALIGNMENT,10),c);
		
		c.gridx = 5;
		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		pane.add(vgc.buildButton("Add","gtk-add","ADD_SERIES_REC",this,Component.CENTER_ALIGNMENT),c);


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
		} else if ("ADD_SERIES_REC".equals(cmd)) {
			videoCommands vc = new videoCommands(null,null);
			vc.addSeries(nameText.getText(), Integer.parseInt(seasonText.getText()), Integer.parseInt(episodeText.getText()), Integer.parseInt(diskText.getText()), formatBox, qualityBox, locationText.getText(), classificationBox);
			//System.out.println(nameText.getText() + " : " + Integer.parseInt(diskText.getText()) + " : " + formatBox + " : " + qualityBox + " : " + locationText.getText() + " : " + classificationBox);			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new videoSeriesGui().updateTable(new videoCommands(null,null).showSeries());
            }});
		
			kerpowgui.updateStatusBar("Added \"" + nameText.getText() + "\" (season " + seasonText.getText() + ") to \"" + locationText.getText() + "\"");
		}
    }
}
