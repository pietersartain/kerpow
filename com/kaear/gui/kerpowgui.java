package com.kaear.gui;

import com.kaear.interfaces.*;
import com.kaear.common.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.Array;

public class kerpowgui implements ActionListener 
{

    private static String LOOKANDFEEL = "System";
	public static kerpowObjectManager kerpowObjectManager;
	public JTable table;
	public static JLabel statusBar = new JLabel();
	public static JPanel infoBar = new JPanel();
	public static int infoBarStatus = 0;
	public static JFrame frame = new JFrame("kerpow!");
	private static JToggleButton toggleList;
	
	public kerpowgui() {
		kerpowObjectManager = new kerpowObjectManager();
	}


	public Component createComponents() {
		JPanel masterPane = new JPanel();
		//JFrame masterPane = new JFrame();
		masterPane.setLayout(new BoxLayout(masterPane,BoxLayout.PAGE_AXIS));
		
		// Add the menu
		//masterPane.add(makeMenu());

		// Add the tabbed pane
		masterPane.add(makeTabbedpane());

		// Add the infobar
		masterPane.add(makeInfoBar());

		// Add the status bar
		masterPane.add(makeStatusbar());
		
		// Set preferred size
		masterPane.setPreferredSize(new Dimension(800,600));

		return masterPane;
	}

// ************** JMenu *****************
	protected JComponent makeMenu()
	{		
        	JMenu fileMenu = new JMenu ("File");
        		JMenuItem printItem = new JMenuItem ("Print");
        		fileMenu.add (printItem);
        		JMenuItem exitItem = new JMenuItem ("Exit");
        		fileMenu.add (exitItem);

        	JMenu helpMenu = new JMenu ("Help");
        		JMenuItem contentsItem = new JMenuItem ("Contents");
        		helpMenu.add (contentsItem);
        		JMenuItem aboutItem = new JMenuItem ("About");
        		helpMenu.add (aboutItem);

			JMenuBar menuBar = new JMenuBar();
			menuBar.add(fileMenu);
			menuBar.add(helpMenu);

			menuBar.setMinimumSize(new Dimension(100000,25));
			menuBar.setPreferredSize(new Dimension(100000,25));
			menuBar.setMaximumSize(new Dimension(Short.MAX_VALUE,25));

			return menuBar;
	}


// ******* This makes stuff happen on button clicks *********
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        if ("REFRESH_MUSIC".equals(cmd)) { //first button clicked
		} else if ("UPDATE_MUSIC".equals(cmd)) { // second button clicked
        } 
    }

// *************** InfoBar *******************
	protected JComponent makeInfoBar()
	{
			infoBar.setPreferredSize(new Dimension(4500,0));
			infoBar.setMinimumSize(new Dimension(4500,0));
			infoBar.setMaximumSize(new Dimension(Short.MAX_VALUE,100));
			infoBar.setSize(new Dimension(4500,0));
			infoBar.setOpaque(true);
			infoBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
			return infoBar;
	}

// ************** StatusBar *****************
	protected JComponent makeStatusbar()
	{
			statusBar.setPreferredSize(new Dimension(4500,25));
			statusBar.setMinimumSize(new Dimension(4500,25));
			statusBar.setMaximumSize(new Dimension(Short.MAX_VALUE,25));
			statusBar.setSize(new Dimension(4500,25));
			statusBar.setAlignmentX(Component.CENTER_ALIGNMENT);
			statusBar.setHorizontalAlignment(SwingConstants.LEFT);
			statusBar.setOpaque(false);
			statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
			updateStatusBar("kerpow initialised.");
			return statusBar;
	}

// ************** JTabbedPane *****************
	protected JComponent makeTabbedpane()
	{
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.setOpaque(true);

			for (int x=0; x < kerpowObjectManager.plugins.getPlugins().size(); x++)
			{
				plugin myPlugin = (plugin)kerpowObjectManager.plugins.getPlugins().get(x);
				String myString = "";

				try { myString = myPlugin.getPluginName(); }
				catch (Throwable e) { new exhandle("Oh bugger:  ",e); }

				tabbedPane.addTab(myPlugin.getPluginName(), null, myPlugin.getGui().makeGui(), "Does nothing");
			}

        	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			return tabbedPane;
	}

	
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
 
 		UIManager.LookAndFeelInfo[] lnfList = UIManager.getInstalledLookAndFeels();
		//LOOKANDFEEL = UIManager.getSystemLookAndFeelClassName();
		
		for (int x=0; Array.getLength(lnfList) > x; x++)
		{
			LOOKANDFEEL = lnfList[x].getName();
		}
		
		if (LOOKANDFEEL.equals("Windows Classic")) {
			LOOKANDFEEL = "System";
		}
        
		initLookAndFeel();
        
		//Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        kerpowgui app = new kerpowgui();
        
		// Build the gui:
		Component contents = app.createComponents();
		
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void initLookAndFeel() {
        String lookAndFeel = null;

        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            } else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            } else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (LOOKANDFEEL.equals("GTK+")) { //new in 1.4.2
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                   + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            try {
                UIManager.setLookAndFeel(lookAndFeel);
            } catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                                   + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            } catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                                   + lookAndFeel
                                   + ") on this platform.");
                System.err.println("Using the default look and feel.");
            } catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                                   + lookAndFeel
                                   + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
   		}
	}
	
	public static void updateStatusBar(String s)
	{
		statusBar.setText(" " + new Date().toString() + ": " + s);
		//statusBar.repaint();
	}
	
	private static void updateInfoBar(int s)
	{
		infoBar.setPreferredSize(new Dimension(4500,s));
		infoBar.setMinimumSize(new Dimension(4500,s));
		infoBar.setMaximumSize(new Dimension(Short.MAX_VALUE,s));
		infoBar.setSize(new Dimension(4500,s));
	}
	
	public static void addInfoBar(JComponent s, int a, JToggleButton e)
	{
		if (toggleList != e) {
			if (toggleList != null) {
				toggleList.setSelected(false);
				clearInfoBar();
			}
			toggleList = e;
		}

		updateInfoBar(a);
		infoBar.add(s);
		frame.pack();
	}
	
	public static void clearInfoBar()
	{
		updateInfoBar(0);
		infoBar.removeAll();
		frame.pack();
	}
		
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
