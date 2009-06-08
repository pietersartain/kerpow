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
import com.kaear.common.*;
import com.kaear.interfaces.*;
import com.kaear.gui.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class displayFilmsTableModel extends AbstractTableModel {
	
	    private boolean DEBUG = false;
		private static Vector data;
        private String[] columnNames;
		
		public displayFilmsTableModel(dataList buildList)
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

			String[] myStrData = (String[])data.elementAt(row);
			myStrData[col] = (String)value;
			
			data.setElementAt(myStrData,row);
			
			// Updates the actual view of the JTable
            fireTableCellUpdated(row, col);
			
			// Given a name, fetch the ID
			String newVal = getAnID((String)value,col);
			
			String[] columnNames = new videoFilmsList("").getColumnHeaders();
			String colName = columnNames[col];
			
			if (col == 0 || col > 1) {
				// This is part of the video table, not films
				if (newVal.equals("-1"))
				{
					try { int x = Integer.parseInt((String)value,10);
							newVal = String.valueOf(x); }
					catch (Throwable e) { newVal = "'" + (String)value + "'"; }
				}
				kerpowObjectManager.runDB.sqlRun("UPDATE video SET " + colName + " = " + newVal + " WHERE id = " + myStrData[0],"Table edit failed: ");
			} else {
				if (newVal.equals("-1"))
					newVal = (String)value;
				videoCommands mc = new videoCommands(null,null);
				kerpowObjectManager.runDB.sqlRun("UPDATE films SET name = '" + newVal + "' WHERE id = " + mc.getID(newVal,"films","name"),"Table edit failed: ");
				kerpowObjectManager.runDB.sqlRun("UPDATE video SET type_id = " + mc.getID(newVal,"films","name") + " WHERE id = " + myStrData[0],"Table edit failed: ");
			}

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }
		
		public void deleteRow(int row)
		{
			Vector args = new Vector();
			args.add(0,getValueAt(row,0));
			args.add(1,"films");
			
			data.remove(row);
			fireTableRowsDeleted(row,row);
	
			new videoCommands("deleteRecord",args);
		}
	
		private String getAnID(String value, int s)
		{
			/**
			 * Columns:
			 *
			 * 0 = ID
			 * 1 = Name
			 * 2 = Disks
			 * 3 = Format
			 * 4 = Quality
			 * 5 = Location
			 * 6 = Classification
			 */
			videoCommands mc = new videoCommands(null,null);
			
			if (s == 3) { return String.valueOf(mc.getID(value,"videoformat","name")); }
			else
			if (s == 4) { return String.valueOf(mc.getID(value,"quality","name")); }
			else
			if (s == 6) { return String.valueOf(mc.getID(value,"classification","name")); }
			else
			{ return "-1"; }
		}
		
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
