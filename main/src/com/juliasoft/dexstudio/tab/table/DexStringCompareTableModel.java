package com.juliasoft.dexstudio.tab.table;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

@SuppressWarnings("serial")
public class DexStringCompareTableModel extends AbstractTableModel implements TableModel
{
	private DiffNode<?>[] data;
	protected String[] columnNames = { "String Value" };
	
	public DexStringCompareTableModel(DiffNode<?> ctx)
	{
		data = ctx.getChildren().toArray(new DiffNode<?>[ctx.getChildren().size()]);
		
	}

	@Override
	public Class<?> getColumnClass(int arg0)
	{
		if(arg0 == 0)
		{
			return DiffNode.class;
		}
		return super.getColumnClass(arg0);
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int idx)
	{
		return columnNames[idx];
	}
	
	@Override
	public int getRowCount()
	{
		return data.length;
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		return data[row];
	}
	
}
