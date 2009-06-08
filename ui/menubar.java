import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class menubar {

	public JComponent menubar() {
/*	}


protected JComponent makeMenu()
{	*/	
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
}
