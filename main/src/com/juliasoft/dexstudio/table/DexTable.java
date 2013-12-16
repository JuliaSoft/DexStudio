package com.juliasoft.dexstudio.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexFrame;

/**
 * Table model for all the visualized table in the project
 * @author Ancona Eugenio, Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexTable extends JPanel
{
	private JTable table;
	
	/**
	 * Constructor
	 * @param frame	The DexFrame reference
	 * @param model	The model of the table to visualize
	 */
	public DexTable(DexFrame frame, TableModel model)
	{
		super(new BorderLayout());
		table = new JTable(model);
		if(table.getRowCount() == 0)
		{
			this.setVisible(false);
			return;
		}
		
		//Setting Layout
		this.setBackground(new Color(0.8f, 0.8f, 0.8f));
		this.setBorder(BorderFactory.createMatteBorder(20, 0, 0, 0, Color.white));
		this.setAlignmentX(LEFT_ALIGNMENT);
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0.8f, 0.8f, 0.8f)));
		table.setBackground(Color.WHITE);
		table.getTableHeader().setBackground(new Color(222, 227, 233));
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(false);
		//DexTableCellRenderer renderer = new DexTableCellRenderer(model.getRowCount(), model.getColumnCount(), frame);
		DexEditorRenderer editRend = new DexEditorRenderer(frame, model.getRowCount(), model.getColumnCount());
		table.setDefaultRenderer(Type.class, editRend);
		table.setDefaultRenderer(String.class, editRend);
		table.setDefaultRenderer(MethodGen.class, editRend);
		table.setDefaultRenderer(FieldGen.class, editRend);
		table.setDefaultRenderer(HashSet.class, editRend);
		table.setDefaultEditor(String.class, editRend);
		table.setDefaultEditor(Type.class, editRend);
		table.setDefaultEditor(MethodGen.class, editRend);
		table.setDefaultEditor(FieldGen.class, editRend);
		table.setDefaultEditor(HashSet.class, editRend);
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0,0));
		table.setCellSelectionEnabled(false);
		MouseAdapter mouse = new ActiveTableListener();
		table.addMouseMotionListener(mouse);
		table.addMouseListener(mouse);
		
		//Setting the content
		JLabel title = new JLabel();
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		if(model instanceof DexFieldTableModel)
		{
			title.setText("Field Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		}
		else 
		if(model instanceof DexConstructorTableModel)
		{
			title.setText("Constructor Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		}
		else
		if(model instanceof DexMethodTableModel)
		{
			title.setText("Method Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		}
		else
		if(model instanceof DexValueTableModel)
		{
			title.setText("Value Summary");
		}
		else
		if(model instanceof DexCodeTableModel)
		{
			title.setText("Code");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		}
		else
		if(model instanceof DexStringTableModel)
		{
			title.setText("String Summary");
		}
		content.add(table.getTableHeader());
		content.add(table);
		
		this.add(title, BorderLayout.NORTH);
		this.add(content, BorderLayout.CENTER);
		this.setMaximumSize(new Dimension(999999, this.getPreferredSize().height));
	}
	
	/**
	 * 
	 * @author Ancona Eugenio
	 *
	 */
	private class ActiveTableListener extends MouseAdapter
	{
		@Override
		public void mouseEntered(MouseEvent e)
		{
			activeCell(e);
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			activeCell(e);
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			activeCell(e);
		}
		
		private void activeCell(MouseEvent e)
		{
			Point p = e.getPoint();
			if (p != null)
			{
				int row = table.rowAtPoint(p);
				int column = table.columnAtPoint(p);
				
				if(row != table.getEditingRow() || column != table.getEditingColumn())
				{
					if(table.isEditing())
					{
						if(!table.getCellEditor().stopCellEditing())
							table.getCellEditor().cancelCellEditing();
					}	
					else
					{
						if(row != -1 && column != -1 && table.isCellEditable(row, column))
							table.editCellAt(row, column);
					}
				}
			}
		}
	}
}
	
	

