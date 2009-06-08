import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class kerpowgui implements ActionListener {

    final static String LOOKANDFEEL = "GTK+";
	public static kerpowObjectManager kerpowObjectManager;
	public MyTableModel tableModel;
	public JTable table;
	public JLabel statusBar = new JLabel();
	
	public kerpowgui() {
	kerpowObjectManager = new kerpowObjectManager();
	}


	public Component createComponents() {
	
		JPanel masterPane = new JPanel();
		//JFrame masterPane = new JFrame();
		masterPane.setLayout(new BoxLayout(masterPane,BoxLayout.PAGE_AXIS));
		
		// Add the menu
		masterPane.add(makeMenu());

		// Add the toolbar
		//masterPane.add(makeToolbar());

		// Add the tabbed pane
		masterPane.add(makeTabbedpane());
		
		masterPane.add(makeStatusbar());
		
		// Set preferred size
		masterPane.setPreferredSize(new Dimension(600,400));

		return masterPane;
// ************** Done! *****************		
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

// ************** JToolbar *****************
protected JComponent makeToolbar()
{
		JToolBar toolBar = new JToolBar();
		
		JButton refreshButton = new JButton(new ImageIcon("res/gtk-refresh.png"));
		refreshButton.setMinimumSize(new Dimension(24,24));
		refreshButton.setPreferredSize(new Dimension(30,30));
		refreshButton.setMaximumSize(new Dimension(30,30));
		refreshButton.addActionListener(this);
		refreshButton.setActionCommand("REFRESH_MUSIC");
		
		JButton updateButton = new JButton(new ImageIcon("res/gtk-refresh.png"));
		updateButton.setMinimumSize(new Dimension(24,24));
		updateButton.setPreferredSize(new Dimension(30,30));
		updateButton.setMaximumSize(new Dimension(30,30));
		updateButton.addActionListener(this);
		updateButton.setActionCommand("UPDATE_MUSIC");

		toolBar.add(refreshButton);
		toolBar.add(updateButton);
		toolBar.setFloatable(false);		

		toolBar.setMinimumSize(new Dimension(100000,30));
		toolBar.setPreferredSize(new Dimension(100000,30));
		toolBar.setMaximumSize(new Dimension(Short.MAX_VALUE,30));
		
		return toolBar;
}


// ******* This makes stuff happen on button clicks *********

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String description = null;

        // Handle each button.
        if ("REFRESH_MUSIC".equals(cmd)) { //first button clicked
			kerpowObjectManager.music.updateDB(kerpowObjectManager.preferences.getMusicPath(),kerpowObjectManager.preferences.getExcludePath());
        } else if ("UPDATE_MUSIC".equals(cmd)) { // second button clicked
            updateTable();
        } 
		/*
		else if (NEXT.equals(cmd)) { // third button clicked
            description = "taken you to the next <something>.";
        }

        displayResult("If this were a real app, it would have "
                        + description);
					*/
					
		kerpowObjectManager.runDB.dbCommit();
		updateTable();
    }

// ************** This refreshes the table contents *****************

	public void updateTable() {
//		System.out.println("Updating table ... ");
//		tableModel.fireTableStructureChanged();
		
//		DefaultTableModel model = (DefaultTableModel)tableModel.getModel();
		//tableModel.fireTableDataChanged();

		//tableModel = new MyTableModel(new musicList());
		//JTable table = new JTable(tableModel);
		//table = new JTable(tableModel);
		table.setModel(new MyTableModel(new musicList()));

	}

// ************** This is a filler *****************

		//masterPane.add(Box.createRigidArea(new Dimension(0,50)));

// ************** JSplitPane *****************
protected JComponent makeSplitpane()
{
		// Left hand side
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane,BoxLayout.PAGE_AXIS));

//		JLabel label = new JLabel("\'Sup ma bitches!");
//		pane.add(label);

		JButton refreshButton = new JButton("Update ...",new ImageIcon("res/gtk-refresh.png"));
		refreshButton.setMinimumSize(new Dimension(120,30));
		refreshButton.setPreferredSize(new Dimension(120,30));
		refreshButton.setMaximumSize(new Dimension(120,30));
		refreshButton.addActionListener(this);
		refreshButton.setActionCommand("REFRESH_MUSIC");

		JButton refreshButton1 = new JButton("Update ...",new ImageIcon("res/gtk-refresh.png"));
		refreshButton1.setMinimumSize(new Dimension(120,30));
		refreshButton1.setPreferredSize(new Dimension(120,30));
		refreshButton1.setMaximumSize(new Dimension(120,30));
		refreshButton1.addActionListener(this);
		refreshButton1.setActionCommand("UPDATE_MUSIC");

		pane.add(refreshButton);
		pane.add(refreshButton1);

		
		pane.setBorder(BorderFactory.createEmptyBorder(
                                        5, //top
                                        5, //left
                                        5, //bottom
                                        5) //right
                                        );


		// Right hand side
		JPanel pane1 = new JPanel(new GridLayout(0,1));
//        tableModel = new MyTableModel(new musicList());
		tableModel = new MyTableModel(new musicList());
		//tableModel.addTableModelListener(
		//System.out.println(tableModel.getTableModelListeners());
		table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        pane1.add(scrollPane);
 

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pane, pane1);
		return splitPane;
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
		statusBar.setOpaque(true);
		statusBar.setBackground(Color.WHITE);
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setText("Stuff going on ...");
		
		return statusBar;
}

// ************** JTabbedPane *****************	
protected JComponent makeTabbedpane()
{
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setOpaque(true);

        tabbedPane.addTab("Tab 1", null, makeSplitpane(),
                          "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", null, panel2,
                          "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Tab 3", null, panel3,
                          "Still does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
	
        //Uncomment the following line to use scrolling tabs.
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
        
		initLookAndFeel();
        
		//Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("kerpow!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        kerpowgui app = new kerpowgui();
        
		// Build the gui:
		Component contents = app.createComponents();
		
		//frame.setLayout(new BoxLayout(frame,BoxLayout.PAGE_AXIS));
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
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
		//initmain();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
