package com.juliasoft.dexstudio.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.Type;

public class DexFieldTableModel implements TableModel {

	private Object[][] data;
	private String[] columnNames = {"Flags", "Type", "Name"};
	
	public DexFieldTableModel(ClassGen clazz){
		
		List<FieldGen> fields  = new ArrayList<FieldGen>();
		fields.addAll(clazz.getStatic_fields());
		fields.addAll(clazz.getInstance_fields());
		
		data = new Object[fields.size()][columnNames.length];
		int i=0;
		for(FieldGen field : fields){
			
			data[i][0] = AccessFlag.decodeToHuman(field.getFlags(), true);
			data[i][1] = field.getType();
			data[i][2] = field.getName();
			
			i++;
			
		}
		
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {

	}

	@Override
	public Class<?> getColumnClass(int column) {
		
		switch(column){
		case 0: 
		case 2: return String.class;
		case 1: return Type.class;
		default: throw new IllegalArgumentException();
		
		}
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
		return (column == 1)? true : false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {

	}

	@Override
	public void setValueAt(Object value, int row, int column) throws IllegalArgumentException{
		
		/*if ( column == 0 && value instanceof String )
			
			data[row][column] = (String)value;
		
		else if ( column == 1 && value instanceof Type )
					
			data[row][column] = (Type)value;
		
		else if ( column == 2 && value instanceof FieldGen )
			
			data[row][column] = (FieldGen)value;
		
		else throw new IllegalArgumentException();*/
	}
	
}
