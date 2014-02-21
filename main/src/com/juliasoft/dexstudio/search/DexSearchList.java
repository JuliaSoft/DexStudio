package com.juliasoft.dexstudio.search;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.dexstudio.view.node.DexTreeNode;

/**
 * JTable with additional functions for managed the results of a search
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexSearchList extends JTable
{
	/** User input string */
	private String search = new String();
	
	/**
	 * Constructor
	 */
	public DexSearchList()
	{
		// Setting Layout
		this.setModel(new DexSearchTableModel());
		this.getColumnModel().getColumn(0).setCellRenderer(new IconColumnCellRenderer());
		this.getColumnModel().getColumn(1).setCellRenderer(new TextColumnCellRenderer());
		this.getColumnModel().getColumn(0).setMaxWidth(40);
		this.setTableHeader(null);
		this.setRowHeight(25);
		this.setIntercellSpacing(new Dimension(0, 0));
		this.setShowGrid(false);
	}
	
	/**
	 * Update the list of results in the table
	 * 
	 * @param nodes
	 *            The array of new results to be compared with the local one
	 * @param search
	 *            User input string for additional informations into the
	 *            visualization
	 */
	public synchronized void updateRows(TreeSet<DexTreeNode<?>> nodes, String search)
	{
		this.search = search;
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.getDataVector().removeAllElements();
		this.update(getGraphics());
		this.repaint();
		// Insert new rows
		for(DexTreeNode<?> node : nodes)
		{
			Object obj = node.getUserObject();
			if(obj instanceof ClassGen)
			{
				if(AccessFlag.ACC_INTERFACE.isSet(((ClassGen) obj).getFlags()))
					model.addRow(new Object[] { "imgs/tree/class.png", node });
				else
					model.addRow(new Object[] { "imgs/tree/interface.png", node });
			}
			else if(obj instanceof Set<?>)
				model.addRow(new Object[] { "imgs/tree/strings.png", node });
		}
		// Update selection and graphics
		if(DexSearchList.this.getRowCount() > 0)
			DexSearchList.this.setRowSelectionInterval(0, 0);
		this.update(getGraphics());
	}
	
	/**
	 * Custom TableModel for DexSearchList
	 * 
	 * @author Zanoncello Matteo
	 */
	public class DexSearchTableModel extends DefaultTableModel
	{
		public DexSearchTableModel()
		{
			super(0, 2);
		}
		
		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	}
	
	/**
	 * Custom TableCellRenderer for the first column of the search results list
	 * 
	 * @author Zanoncello Matteo
	 */
	public class IconColumnCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col)
		{
			JLabel label = new JLabel();
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setIcon(new ImageIcon((String) value));
			label.setBackground(selected ? Color.LIGHT_GRAY : Color.white);
			label.setOpaque(true);
			return label;
		}
	}
	
	/**
	 * Custom TableCellRenderer for the second column of the search results list
	 * 
	 * @author Zanoncello Matteo
	 */
	public class TextColumnCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col)
		{
			DexTreeNode<?> node = (DexTreeNode<?>) value;
			String str = node.toString();
			JLabel label = new JLabel();
			label.setBackground(selected ? Color.LIGHT_GRAY : Color.white);
			label.setOpaque(true);
			label.setSize(label.getPreferredSize());
			label.setToolTipText(str);
			if(node.getUserObject() instanceof ClassGen)
			{
				ClassGen obj = (ClassGen) node.getUserObject();
				String pkg = obj.toHuman().substring(0, obj.toHuman().lastIndexOf('.'));
				label.setText("<html><b>" + str.substring(0, search.length()) + "</b>" + str.substring(search.length()) + " : <span style='color: #999999;'>" + pkg + "</span></html>");
			}
			else if(node.getUserObject() instanceof Set<?>)
			{
				label.setText("<html><b>" + str.substring(0, search.length()) + "</b>" + str.substring(search.length()) + "</html>");
			}
			return label;
		}
	}
}
