package com.juliasoft.dexstudio.tab.table;

import java.util.Map;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.cst.Constant;

/**
 * Table model for the name-value table in an annotation tab
 * 
 * 
 * @author Eugenio Ancona
 * 
 */
public class DexValueTableModel implements TableModel {
	private String[][] data;
	private String[] columnNames = { "Key", "Value" };

	public DexValueTableModel(Annotation ann) {
		Set<Map.Entry<String, Constant>> entries = ann.getElements();
		data = new String[entries.size()][columnNames.length];
		int i = 0;
		for (Map.Entry<String, Constant> entry : entries) {
			data[i][0] = entry.getKey();
			data[i][1] = entry.getValue().toHuman();
			i++;
		}
	}

	@Override
	public void addTableModelListener(TableModelListener arg0) {
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return data[row][column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}

	@Override
	public void setValueAt(Object value, int row, int column)
			throws IllegalArgumentException {
		if (value instanceof String)
			data[row][column] = (String) value;
		else
			throw new IllegalArgumentException();
	}
}
