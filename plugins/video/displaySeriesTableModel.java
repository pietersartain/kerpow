package plugins.video;

import com.kaear.common.*;
import com.kaear.cli.*;
import com.kaear.gui.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class displaySeriesTableModel extends AbstractTableModel {
	
	    private boolean DEBUG = false;
		private static Vector data;
        private String[] columnNames;
		
		public displaySeriesTableModel(dataList buildList)
		{
			//Pull in the information for whatever module is required.
			columnNames = buildList.getColumnHeaders();
			data = buildList.makeList();
		}
		
		public void setData(dataList buildList)
		{
			data = buildList.makeList();
			fireTableDataChanged();
		}
		
	    public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

		
		public Object getValueAt(int row, int col) {
			return ((String[])data.elementAt(row))[col];
        }
		

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
			if (col == 0) {
                return false;
            } else {
                return true;
            }
        }
		
		/*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                                   + " to " + value
                                   + " (an instance of "
                                   + value.getClass() + ")");
            }

            //data[row][col] = value;
			
			String[] myStrData = (String[])data.elementAt(row);
			
			/*
			for (int x=0; x < myStrData.length; x++)
			{
				if (x == col)
				{
					myStrData[x] = (String)value;
				}
			}*/
			myStrData[col] = (String)value;
			
			data.setElementAt(myStrData,row);
			
			// Updates the actual view of the JTable
            fireTableCellUpdated(row, col);
			
			// Given a name, fetch the ID
			/*String newVal = getAnID((String)value,col);
			
			if (newVal.equals("-1")) {
				newVal = (String)value;
			}
			*/
			// Updates the database to match
			//kerpowObjectManager.runDB.sqlRun("UPDATE music SET " + getName(col) + " = " + newVal + " WHERE id = " + myStrData[0],"Table edit failed: ");

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
		
		public void deleteRow(int row)
		{
			Vector args = new Vector();
			args.add(0,getValueAt(row,0));
			
			data.remove(row);
			fireTableRowsDeleted(row,row);
	
			//new musicCommands("deleteMusic",args);
		}
		
		/*
		private String getAnID(String value, int s)
		{
			/**
			 * Columns:
			 *
			 * 0 = ID
			 * 1 = Artist
			 * 2 = Album
			 * 3 = Format
			 * 4 = Disc
			 */
		/*	musicCommands mc = new musicCommands(null,null);
			
			if (s == 1) { return String.valueOf(mc.checkArtist(value)); }
			else
			if (s == 2) { return String.valueOf(mc.checkAlbum(value)); }
			else
			if (s == 3) { return String.valueOf(mc.checkFormat(value)); }
			else
			if (s == 4) { return "-1"; }
			else
			{ return ""; }
		}
		
		private String getName(int s)
		{
			/**
			 * Columns:
			 *
			 * 0 = ID
			 * 1 = Artist
			 * 2 = Album
			 * 3 = Format
			 * 4 = Disc
			 */

		/*	if (s == 0) { return "id"; }
			else
			if (s == 1) { return "artist"; }
			else
			if (s == 2) { return "album"; }
			else
			if (s == 3) { return "format"; }
			else
			if (s == 4) { return "disc"; }
			else
			{ return ""; }
		}
		*/

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data.elementAt(i));
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
	
}
