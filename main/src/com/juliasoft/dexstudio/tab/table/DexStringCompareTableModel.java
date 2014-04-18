package com.juliasoft.dexstudio.tab.table;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;

/**
 * Table model for the strign comparison table in the string comparison tab
 * 
 * 
 * @author Eugenio Ancona
 * 
 */
@SuppressWarnings("serial")
public class DexStringCompareTableModel extends AbstractTableModel implements
		TableModel {
	private Object[][] data;
	private String[] columnNames = { "Diff", "String Value" };

	public DexStringCompareTableModel(DiffNode<?> ctx) {
		data = new Object[ctx.getChildren().size()][columnNames.length];

		int i = 0;
		for (DiffNode<?> node : ctx.getChildren()) {

			data[i][0] = node.getState();
			data[i][1] = (node.getState().equals(DiffState.LEFT_ONLY) ? node
					.getLeft() : node.getRight());
			i++;
		}

	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		switch (arg0) {
		case 0:
			return DiffState.class;
		case 1:
			return String.class;
		default:
			throw new IllegalArgumentException();

		}
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
		return data[row][column];
	}

}
