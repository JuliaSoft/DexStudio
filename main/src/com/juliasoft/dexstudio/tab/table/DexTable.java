package com.juliasoft.dexstudio.tab.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.cst.Constant;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.table.render.AnnotationRenderer;
import com.juliasoft.dexstudio.tab.table.render.ClassRenderer;
import com.juliasoft.dexstudio.tab.table.render.DefaultDexRenderer;
import com.juliasoft.dexstudio.tab.table.render.MethodRenderer;
import com.juliasoft.dexstudio.tab.table.render.StringCompareRenderer;
import com.juliasoft.dexstudio.utils.AnnotationSet;

/**
 * Container component for all the visualized table in the project
 * 
 * @author Ancona Eugenio, Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTable extends JPanel {
	private JTable table;

	/**
	 * Constructor
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param model
	 *            The model of the table to visualize
	 */
	public DexTable(DexDisplay display, TableModel model) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		table = new JTable(model);
		if (table.getRowCount() == 0) {
			this.setVisible(false);
			return;
		}
		// Setting Layout
		this.setBackground(new Color(0.8f, 0.8f, 0.8f));
		this.setBorder(BorderFactory
				.createMatteBorder(20, 0, 0, 0, Color.white));
		this.setAlignmentY(TOP_ALIGNMENT);
		this.setAlignmentX(LEFT_ALIGNMENT);
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				new Color(0.8f, 0.8f, 0.8f)));
		content.setAlignmentX(LEFT_ALIGNMENT);
		table.setBackground(Color.WHITE);
		table.getTableHeader().setBackground(new Color(222, 227, 233));
		// table.setAutoCreateRowSorter(true);
		table.setRowSorter(new TableRowSorter<TableModel>(model));
		// table.getTableHeader().set .setReorderingAllowed(false);
		// DexTableCellRenderer renderer = new
		// DexTableCellRenderer(model.getRowCount(), model.getColumnCount(),
		// frame);
		DefaultDexRenderer editRend = new DefaultDexRenderer();
		MethodRenderer mRend = new MethodRenderer(display);
		AnnotationRenderer aRend = new AnnotationRenderer(display);
		ClassRenderer cRend = new ClassRenderer(display);
		StringCompareRenderer sRend = new StringCompareRenderer();
		table.setDefaultRenderer(Type.class, cRend);
		table.setDefaultRenderer(String.class, editRend);
		table.setDefaultRenderer(MethodGen.class, mRend);
		table.setDefaultRenderer(FieldGen.class, editRend);
		table.setDefaultRenderer(AnnotationSet.class, aRend);
		table.setDefaultRenderer(Constant.class, editRend);
		table.setDefaultRenderer(DiffState.class, sRend);
		table.setDefaultEditor(String.class, editRend);
		table.setDefaultEditor(Type.class, cRend);
		table.setDefaultEditor(MethodGen.class, mRend);
		table.setDefaultEditor(FieldGen.class, editRend);
		table.setDefaultEditor(AnnotationSet.class, aRend);
		table.setDefaultEditor(DiffState.class, sRend);
		table.setRowHeight(20);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setCellSelectionEnabled(false);
		MouseAdapter mouse = new ActiveTableListener();
		table.addMouseMotionListener(mouse);
		table.addMouseListener(mouse);
		// Setting the content
		JLabel title = new JLabel();
		title.setHorizontalAlignment(JLabel.LEFT);
		title.setAlignmentX(LEFT_ALIGNMENT);
		title.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		if (model instanceof DexFieldTableModel) {
			title.setText("Field Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		} else if (model instanceof DexConstructorTableModel) {
			title.setText("Constructor Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		} else if (model instanceof DexMethodTableModel) {
			title.setText("Method Summary");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		} else if (model instanceof DexValueTableModel) {
			title.setText("Value Summary");
		} else if (model instanceof DexCodeTableModel) {
			title.setText("Code");
			table.getColumnModel().getColumn(0).setPreferredWidth(120);
			table.getColumnModel().getColumn(0).setMaxWidth(150);
		} else if (model instanceof DexStringTableModel) {
			title.setText("String Summary");
		} else if (model instanceof DexStringCompareTableModel) {

			title.setText("String Comparison");
			table.getColumnModel().getColumn(0).setPreferredWidth(15);
			table.getColumnModel().getColumn(0).setMaxWidth(35);
		}

		content.add(table.getTableHeader());
		content.add(table);
		this.add(title);
		this.add(content);
	}

	/**
	 * @author Ancona Eugenio
	 */
	private class ActiveTableListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			activeCell(e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// activeCell(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			activeCell(e);
		}

		private void activeCell(MouseEvent e) {
			Point p = e.getPoint();
			if (p != null) {
				int row = table.rowAtPoint(p);
				int column = table.columnAtPoint(p);
				if (row != table.getEditingRow()
						|| column != table.getEditingColumn()) {
					if (table.isEditing()) {
						if (!table.getCellEditor().stopCellEditing())
							table.getCellEditor().cancelCellEditing();
					} else {
						if (row != -1 && column != -1
								&& table.isCellEditable(row, column))
							table.editCellAt(row, column);
					}
				}
			}
		}
	}
}
