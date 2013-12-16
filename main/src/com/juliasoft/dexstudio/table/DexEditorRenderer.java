package com.juliasoft.dexstudio.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.HashSet;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.cell.DexAnnotationCell;
import com.juliasoft.dexstudio.cell.DexClassCell;
import com.juliasoft.dexstudio.cell.DexMethodCell;

@SuppressWarnings("serial")
public class DexEditorRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private JTextPane[][] components;
	private DexFrame frame;
	
	public DexEditorRenderer(DexFrame frame, int rows, int columns){
		
		this.frame = frame;
		this.components = new JTextPane[rows][columns];
		
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean arg2, boolean arg3, int row, int column) {
		
		return getComponent(value, row, column);
	
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
		
		return getComponent(value, row, column);
		
		
	}
	
	private Component getComponent(Object value, int row, int column){
		
		JTextPane cell;
		
		if (components[row][column] == null){
			
			if(value instanceof String){
				
				cell = new JTextPane();
				cell.setText((String)value);
				
			}
				
			else if(value instanceof Type){
				
				cell = new DexClassCell((Type)value, frame);
				HyperlinkCellListener listener = new HyperlinkCellListener(cell);
				cell.addHyperlinkListener(listener);
			}
			
			else if(value instanceof MethodGen){
				
				cell = new DexMethodCell((MethodGen)value, frame);
				HyperlinkCellListener listener = new HyperlinkCellListener(cell);
				cell.addHyperlinkListener(listener);
			}
			
			else if (value instanceof HashSet<?>){
				
				cell = new DexAnnotationCell((HashSet<Annotation>)value, frame);
				HyperlinkCellListener listener = new HyperlinkCellListener(cell);
				cell.addHyperlinkListener(listener);
				
			}
				
			else throw new IllegalArgumentException();
				
			cell.setBackground((row % 2 == 0)? Color.WHITE : new Color(238, 238, 239));
			cell.setBorder(BorderFactory.createMatteBorder(2,3,2,2,(row % 2 == 0)? Color.WHITE : new Color(238, 238, 239) ));
			cell.setEditable(false);
			cell.setFocusable(false);
			cell.addHyperlinkListener(new HyperlinkCellListener(cell));
			
			components[row][column] = cell;
			
			return cell;
			
		}
		else {
			
			return components[row][column];
			
		}
	}
	
	@Override
	public Object getCellEditorValue() {
		
		return null;
	}	
		
	private class HyperlinkCellListener implements HyperlinkListener{
		
		private JTextPane cell;
		private HyperlinkCellListener(JTextPane cell){
			
		this.cell = cell;	
			
		}
		
		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			
			int mask = e.getInputEvent().getModifiers();
			
			//LEFT CLICK
			
			if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) && ((mask & InputEvent.BUTTON1_MASK)== mask)){
				
				String code = e.getDescription();
				
				if(code.equals("meth"))
					
					frame.changeSelectedTab(((DexMethodCell)cell).getMethod());
				
				else if (code.equals("returnclass")){
					
					frame.changeSelectedTab(((DexMethodCell)cell).getReturnTypeClass());
				
					
				}
				else if (code.matches("par[0-9]+")){
					
					int index = Integer.parseInt(code.substring(3));
					
					frame.changeSelectedTab(((DexMethodCell)cell).getParams()[index]);
					
				}
				
				else if (code.matches("ann[0-9]+")){
					
					int index = Integer.parseInt(code.substring(3));
				
					frame.changeSelectedTab(((DexAnnotationCell)cell).getAnnotations()[index]);

				}
					
				else if (code.equals("type"))
					
					frame.changeSelectedTab(((DexClassCell)cell).getClazz());
				
			
			}
				
		}

	}
	
}
	

