package com.juliasoft.dexstudio.view.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Cell Renderer for the tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class TreeCellRenderer extends DefaultTreeCellRenderer {
	private ImageIcon root_ico;
	private ImageIcon folder_close_ico;
	private ImageIcon folder_open_ico;
	private ImageIcon strings_ico;
	private ImageIcon package_ico;
	private ImageIcon class_ico;
	private ImageIcon interface_ico;
	private ImageIcon field_ico;
	private ImageIcon method_ico;
	private ImageIcon annotation_ico;

	public TreeCellRenderer() {
		// Initializing images
		root_ico = new ImageIcon("imgs/tree/root.png");
		folder_close_ico = new ImageIcon("imgs/tree/folder_close.png");
		folder_open_ico = new ImageIcon("imgs/tree/folder_open.png");
		strings_ico = new ImageIcon("imgs/tree/same/strings.png");
		package_ico = new ImageIcon("imgs/tree/same/package.png");
		class_ico = new ImageIcon("imgs/tree/same/class.png");
		interface_ico = new ImageIcon("imgs/tree/same/interface.png");
		field_ico = new ImageIcon("imgs/tree/same/field.png");
		method_ico = new ImageIcon("imgs/tree/same/method.png");
		annotation_ico = new ImageIcon("imgs/tree/same/annotation.png");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		// Default render
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		if (!(value instanceof TreeNode))
			throw new IllegalArgumentException();
		TreeNode node = (TreeNode) value;
		switch (node.getType()) {
		case ROOT:
			setIcon(root_ico);
			break;
		case FOLDER:
			if (expanded)
				setIcon(folder_open_ico);
			else
				setIcon(folder_close_ico);
			break;
		case STRINGS:
			setIcon(strings_ico);
			break;
		case PACKAGE:
			setIcon(package_ico);
			break;
		case CLASS:
			setIcon(class_ico);
			break;
		case INTERFACE:
			setIcon(interface_ico);
			break;
		case FIELD:
			setIcon(field_ico);
			break;
		case METHOD:
			setIcon(method_ico);
			break;
		case ANNOTATION:
			setIcon(annotation_ico);
			break;
		}
		return this;
	}
}
