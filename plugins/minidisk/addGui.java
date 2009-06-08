package plugins.minidisk;

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

public class addGui implements ActionListener
{
	private JTextField nameText = new JTextField();
	private JTextField numberText = new JTextField();
	private JTextField contentsText = new JTextField();

	public addGui()
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
		pane.add(new JLabel("Number: "),c);

		c.gridx = 0;
		c.gridy = 2;
		pane.add(new JLabel("Contents: "),c);		
// *******************************************
		c.gridx = 1;
		c.gridy = 0;
		pane.add(vgc.makeText(nameText,"NAME_BOX",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 1;
		c.gridy = 1;
		pane.add(vgc.makeText(numberText,"NUMBER_BOX",this,Component.CENTER_ALIGNMENT),c);

		c.gridx = 1;
		c.gridy = 2;
		pane.add(vgc.makeText(contentsText,"CONTENTS_BOX",this,Component.CENTER_ALIGNMENT),c);
// *******************************************
		c.gridx = 2;
		c.gridy = 2;
		pane.add(vgc.buildButton("Add","gtk-add","ADD_MD_REC",this,Component.CENTER_ALIGNMENT),c);


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
		} else if ("NUMBER_BOX".equals(cmd)) {
		} else if ("CONTENTS_BOX".equals(cmd)) {
		} else if ("ADD_MD_REC".equals(cmd)) {

			minidiskCommands vc = new minidiskCommands(null,null);
			vc.addMD(nameText.getText(), Integer.parseInt(numberText.getText()), contentsText.getText());
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				new minidiskGui().updateTable(new minidiskCommands(null,null).showAll());
            }});
		
			kerpowgui.updateStatusBar("Added minidisk #" + numberText.getText() + " - " + nameText.getText());

		}
    }
}
