package com.juliasoft.dexstudio.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.Const;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.cst.Constant;

public class DexFieldTableModel implements TableModel
{
	private Object[][] data;
	private String[] columnNames = { "Flags", "Type", "Name", "Initalization", "Annotations" };
	
	public DexFieldTableModel(ClassGen clazz)
	{
		List<FieldGen> fields = new ArrayList<FieldGen>();
		fields.addAll(clazz.getStatic_fields());
		fields.addAll(clazz.getInstance_fields());
		
		Object[] staticValues =  clazz.getStaticValues().toArray();
		
		data = new Object[fields.size()][columnNames.length];
		int i = 0;
		for(FieldGen field : fields)
		{
			data[i][0] = AccessFlag.decodeToHuman(field.getFlags(), true, Const.FLAG_USE_FIELD);
			data[i][1] = field.getType();
			data[i][2] = field.getName();
			data[i][3] = (i < staticValues.length)? ((Constant)staticValues[i]) : "<not initialized>";
			data[i][4] = new HashSet<Annotation>(field.getAnnotations());
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
			case 2:
				return String.class;
			case 1:
				return Type.class;
			case 3: 
				return Constant.class;
			case 4:
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
		switch(column)
		{
			case 1:
			case 4:
				return true;
			default:
				return false;
		}
	}
	
	@Override
	public void removeTableModelListener(TableModelListener arg0)
	{}
	
	@Override
	public void setValueAt(Object value, int row, int column) throws IllegalArgumentException
	{}
}
