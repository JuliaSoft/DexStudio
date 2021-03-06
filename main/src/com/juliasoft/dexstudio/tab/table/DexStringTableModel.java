package com.juliasoft.dexstudio.tab.table;

import java.util.Set;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Table model for the string table in a string tab
 * 
 * 
 * @author Eugenio Ancona
 * 
 */
@SuppressWarnings("serial")
public class DexStringTableModel extends AbstractTableModel implements
		TableModel {
	private String[] data;
	private String[] columnNames = { "String Value" };

	public DexStringTableModel(Set<String> strings) {
		data = strings.toArray(new String[strings.size()]);
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		if (arg0 == 0) {
			return String.class;
		}
		return super.getColumnClass(arg0);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int idx) {
		return columnNames[idx];
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data[row];
	}
}
