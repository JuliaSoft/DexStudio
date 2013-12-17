package com.juliasoft.dexstudio.table;

import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class DexStringTableModel implements TableModel
{
	private String[][] data;
	private String[] columnNames = { "String Value" };
	
	public DexStringTableModel(Set<String> strings)
	{
		data = new String[strings.size()][columnNames.length];
		int i = 0;
		for(String str : strings)
		{
			data[i][0] = str;
			i++;
		}
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0)
	{}
	
	@Override
	public Class<?> getColumnClass(int arg0)
	{
		return String.class;
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
		return data[row][column];
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	@Override
	public void removeTableModelListener(TableModelListener arg0)
	{}
	
	@Override
	public void setValueAt(Object obj, int row, int column)
	{
		if(obj instanceof String)
			data[row][column] = (String) obj;
		else
			throw new IllegalArgumentException();
	}
}
