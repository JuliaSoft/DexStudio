package com.juliasoft.dexstudio.tab.table;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.amalia.dex.codegen.InstructionList;
import com.juliasoft.amalia.dex.codegen.MethodGen;

public class DexCodeTableModel implements TableModel
{
	private String[][] data;
	private String[] columnNames = { "Opcode", "Instruction", "DebugInfo" };
	
	public DexCodeTableModel(MethodGen meth)
	{
		if(!(meth.isAbstract()))
		{
			InstructionList insts = meth.getCode().getInstructionList();
			data = new String[insts.size()][columnNames.length];
			int i = 0;
			for(InstructionHandle handle : insts)
			{
				data[i][0] = handle.toString();
				data[i][1] = handle.getInstruction().toHuman();
				data[i][2] = handle.getDebugInfoString();
				i++;
			}
		}
		else
			data = new String[0][0];
	}
	
	@Override
	public int getColumnCount()
	{
		return columnNames.length;
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
	public void addTableModelListener(TableModelListener l)
	{}
	
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return String.class;
	}
	
	@Override
	public String getColumnName(int columnIndex)
	{
		return columnNames[columnIndex];
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{}
}
