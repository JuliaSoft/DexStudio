package com.juliasoft.dexstudio.navigate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.amalia.dex.codegen.diff.SimpleClassDiff;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.exception.ViewNotFoundException;
import com.juliasoft.dexstudio.view.DexView;
import com.juliasoft.dexstudio.view.compare.CompareNode;
import com.juliasoft.dexstudio.view.compare.DexCompare;
import com.juliasoft.dexstudio.view.tree.DexTree;
import com.juliasoft.dexstudio.view.tree.TreeNode;

@SuppressWarnings("serial")
public class SearchList extends JTable {
	private DexFrame frame;

	private ArrayList<Object> nodes = new ArrayList<Object>();
	private ArrayList<Object> results = new ArrayList<Object>();
	private String src = new String();

	public SearchList(DexFrame frame) {
		this.frame = frame;

		this.setModel(new DefaultTableModel(0, 2) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		this.getColumnModel().getColumn(0)
				.setCellRenderer(new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean selected,
							boolean focus, int row, int col) {
						String str = (String) value;
						String color = str.substring(0, str.indexOf(" "));
						String ico = str.substring(str.indexOf(" ") + 1);
						JLabel label = new JLabel();
						label.setHorizontalAlignment(JLabel.CENTER);
						label.setIcon(new ImageIcon(ico));
						switch (color) {
						case "YELLOW":
							label.setBackground(Color.YELLOW);
							break;
						case "GREEN":
							label.setBackground(Color.GREEN);
							break;
						case "RED":
							label.setBackground(Color.RED);
							break;
						case "BLUE":
							label.setBackground(Color.BLUE);
							break;
						case "WHITE":
							label.setBackground(Color.WHITE);
							break;
						}
						label.setOpaque(true);
						return label;
					}
				});
		this.getColumnModel().getColumn(1)
				.setCellRenderer(new DefaultTableCellRenderer() {
					@Override
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean selected,
							boolean focus, int row, int col) {
						String name = new String();
						ClassGen clazz = null;

						if (value instanceof TreeNode) {
							TreeNode node = (TreeNode) value;
							clazz = (ClassGen) node.getUserObject();
							name = node.getLabel();
						} else if (value instanceof CompareNode) {
							CompareNode node = (CompareNode) value;
							SimpleClassDiff diff = (SimpleClassDiff) node
									.getDiff();
							clazz = (diff.getState().equals(
									DiffState.RIGHT_ONLY) ? diff.getRight()
									: diff.getLeft());
							name = node.getLabel();
						}

						JLabel label = new JLabel();
						label.setBackground(selected ? Color.LIGHT_GRAY
								: Color.white);
						label.setOpaque(true);
						label.setSize(label.getPreferredSize());

						if (clazz != null) {
							String pkg = clazz.toHuman().substring(0,
									clazz.toHuman().lastIndexOf('.'));
							label.setText("<html><b>"
									+ name.substring(0, src.length()) + "</b>"
									+ name.substring(src.length())
									+ " : <span style='color: #999999;'>" + pkg
									+ "</span></html>");
						}
						return label;
					}
				});
		this.getColumnModel().getColumn(0).setMaxWidth(40);
		this.setTableHeader(null);
		this.setRowHeight(25);
		this.setIntercellSpacing(new Dimension(0, 0));
		this.setShowGrid(false);
	}

	public void updateView(int pos) {
		try {
			DexView view = frame.getViewManager().getView(pos);
			if (view instanceof DexTree) {
				nodes = ((DexTree) view).getClassNodes();
			} else if (view instanceof DexCompare) {
				nodes = ((DexCompare) view).getClassNodes();
			} else
				return;
			results.clear();
			this.removeAll();
		} catch (ViewNotFoundException e) {
			e.printStackTrace();
		}

	}

	public synchronized void updateList(ArrayList<Object> results, String src) {
		this.src = src;
		this.results = results;
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		model.getDataVector().removeAllElements();
		this.update(getGraphics());
		this.repaint();
		// Insert new rows
		for (Object obj : results) {
			ClassGen clazz = null;
			String color = "WHITE";
			if (obj instanceof TreeNode) {
				clazz = (ClassGen) ((TreeNode) obj).getUserObject();
			} else if (obj instanceof CompareNode) {
				SimpleClassDiff diff = (SimpleClassDiff) ((CompareNode) obj)
						.getDiff();
				switch (diff.getState()) {
				case DIFFERENT:
					color = "YELLOW";
					break;
				case RIGHT_ONLY:
					color = "GREEN";
					break;
				case LEFT_ONLY:
					color = "RED";
					break;
				case UNKNOWN:
					color = "BLUE";
					break;
				case SAME:
					break;
				}
				clazz = (diff.getState().equals(DiffState.RIGHT_ONLY) ? diff
						.getRight() : diff.getLeft());
			}
			if (clazz == null)
				continue;
			// Controllo se la classe e' un interfaccia
			if (AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags()))
				model.addRow(new Object[] {
						color + " imgs/tree/same/interface.png", obj });
			else
				model.addRow(new Object[] {
						color + " imgs/tree/same/class.png", obj });
		}
		// Update selection and graphics
		if (SearchList.this.getRowCount() > 0)
			SearchList.this.setRowSelectionInterval(0, 0);
		this.update(getGraphics());
	}

	public Object getSelectedItem() {
		return results.get((this.getSelectedRow() == -1) ? 1 : this
				.getSelectedRow());
	}

	public ArrayList<Object> getNodes() {
		return this.nodes;
	}
}
