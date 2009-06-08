package com.kaear.common;

import javax.swing.JComponent;
import java.awt.event.ActionEvent;

public interface guiPlugin
{
	public JComponent makeGui();
	public void updateTable(String sqlstmt);
}
