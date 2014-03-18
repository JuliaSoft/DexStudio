package com.juliasoft.dexstudio.tab.table.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;

@SuppressWarnings("serial")
public class StringCompareRenderer extends AbstractDexEditorRenderer {


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JTextPane result = (JTextPane)super.getTableCellEditorComponent(table, value, isSelected, row, column);
		
		if(!(value instanceof DiffNode<?>))
			
			throw new IllegalArgumentException();
		
		switch(((DiffNode<?>)value).getState()){
			
		case LEFT_ONLY: result.setBackground(new Color(0xF84545));
		result.setBorder(BorderFactory.createMatteBorder(2, 3, 2, 2, new Color(0xF84545)));
		break;
		
		case RIGHT_ONLY: result.setBackground(new Color(0x45F88D)); 
		result.setBorder(BorderFactory.createMatteBorder(2, 3, 2, 2, new Color(0x45F88D)));
		break;
		default: ;				
			
		}
		return result;
	}
	
	
	@Override
	protected boolean supportsRendering(Object value) {
		return true;
	}

	@Override
	protected JTextPane getJTextPane(Object value) {

		JTextPane pane = new JTextPane();
		
		DiffNode<?> node = (DiffNode<?>)value;
		
		String text = (node.getState().equals(DiffState.LEFT_ONLY) || node.getState().equals(DiffState.DIFFERENT))? node.getLeft().toString() : node.getRight().toString();
		
		pane.setText(text);
		return pane;
	}

}
