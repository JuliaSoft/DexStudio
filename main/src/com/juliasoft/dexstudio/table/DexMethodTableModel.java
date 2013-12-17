package com.juliasoft.dexstudio.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;

public class DexMethodTableModel implements TableModel
{
	private String[] columnNames = { "Flags", "Signature", "Annotations" };
	private Object[][] data;
	
	public DexMethodTableModel(ClassGen clazz)
	{
		List<MethodGen> classMethods = new ArrayList<MethodGen>();
		classMethods.addAll(clazz.getVirtual_methods());
		for(MethodGen meth : clazz.getDirect_methods())
		{
			if(!meth.isConstructor())
			{
				classMethods.add(meth);
			}
		}
		data = new Object[classMethods.size()][columnNames.length];
		int i = 0;
		for(MethodGen meth : classMethods)
		{
			data[i][0] = AccessFlag.decodeToHuman(meth.getFlags(), false);
			data[i][1] = meth;
			data[i][2] = new HashSet<Annotation>(meth.getAnnotations());
			i++;
		}
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0)
	{}
	
	@Override
	public Class<?> getColumnClass(int column)
	{
		switch(column)
		{
			case 0:
				return String.class;
			case 1:
				return MethodGen.class;
			case 2:
				return HashSet.class;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
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
		return (column == 0) ? false : true;
	}
	
	@Override
	public void removeTableModelListener(TableModelListener arg0)
	{}
	
	@Override
	public void setValueAt(Object value, int row, int column) throws IllegalArgumentException
	{
		/*
		 * if ( column == 0 && value instanceof String ) data[row][column] =
		 * (String)value; else if(column == 1 && value instanceof MethodGen)
		 * data[row][column] = (MethodGen)value; else throw new
		 * IllegalArgumentException();
		 */
	}
}
