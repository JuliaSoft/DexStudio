package com.juliasoft.dexstudio.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;

import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.cell.DexMethodCell;
import com.juliasoft.dexstudio.cell.DexTypeCell;

public class DexTableCellRenderer implements TableCellRenderer{

	private JTextPane[][] components;
	private DexFrame frame;
	
	public DexTableCellRenderer(int rows, int columns, DexFrame frame){
		
		this.frame = frame;
		components = new JTextPane[rows][columns];
		
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JTextPane cell;
		
		if(components[row][column] == null){
		
			if(value instanceof String){
				
				cell = new JTextPane();
				cell.setText((String)value);
				
			}
				
			else if(value instanceof Type)
				
				cell = new DexTypeCell((Type)value, frame);
			
			else if(value instanceof MethodGen)
				
				cell = new DexMethodCell((MethodGen)value, frame);
				
			else throw new IllegalArgumentException();
				
			cell.setBackground((row % 2 == 0)? Color.WHITE : new Color(238, 238, 239));
			cell.setBorder(BorderFactory.createMatteBorder(2,3,2,2,(row % 2 == 0)? Color.WHITE : new Color(238, 238, 239) ));

			
			components[row][column] = cell;
			return cell;
		}
		else
			
			return components[row][column];
		
	}

	public JTextPane getComponent(int row, int column){
		
		return components[row][column];
		
	}

}
