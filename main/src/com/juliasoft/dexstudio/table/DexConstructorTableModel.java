package com.juliasoft.dexstudio.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.Const;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.utils.AnnotationSet;

public class DexConstructorTableModel implements TableModel
{
	private Object[][] data;
	private String[] columnNames = { "Flags", "Signature", "Annotations" };
	
	public DexConstructorTableModel(ClassGen clazz)
	{
		List<MethodGen> dirMeth = clazz.getDirect_methods();
		List<MethodGen> constructors = new ArrayList<MethodGen>();
		for(MethodGen meth : dirMeth)
		{
			if(meth.isConstructor())
			{
				constructors.add(meth);
			}
		}
		int i = 0;
		data = new Object[constructors.size()][columnNames.length];
		for(MethodGen meth : constructors)
		{
			data[i][0] = AccessFlag.decodeToHuman(meth.getFlags(), false, Const.FLAG_USE_METHOD);
			data[i][1] = meth;
			data[i][2] = new AnnotationSet(meth.getAnnotations());
			i++;
		}
	}
	
	@Override
	public int getRowCount()
	{
		return data.length;
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return columnNames[columnIndex];
	}
	
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
				return AnnotationSet.class;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return (columnIndex == 0) ? false : true;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return data[rowIndex][columnIndex];
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		/*
		 * if(columnIndex == 0 && aValue instanceof String)
		 * data[rowIndex][columnIndex] = (String) aValue; else if(columnIndex ==
		 * 1 && aValue instanceof MethodGen) data[rowIndex][columnIndex] =
		 * (MethodGen) aValue; else throw new IllegalArgumentException();
		 */
	}
	
	@Override
	public void addTableModelListener(TableModelListener l)
	{}
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{}
}
