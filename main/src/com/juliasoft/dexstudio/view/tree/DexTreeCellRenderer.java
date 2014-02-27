package com.juliasoft.dexstudio.view.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.dexstudio.view.tree.node.DexAnnotationNode;
import com.juliasoft.dexstudio.view.tree.node.DexClassNode;
import com.juliasoft.dexstudio.view.tree.node.DexFieldNode;
import com.juliasoft.dexstudio.view.tree.node.DexFolderNode;
import com.juliasoft.dexstudio.view.tree.node.DexMethodNode;
import com.juliasoft.dexstudio.view.tree.node.DexPackageNode;
import com.juliasoft.dexstudio.view.tree.node.DexRootNode;
import com.juliasoft.dexstudio.view.tree.node.DexStringsNode;

/**
 * Cell Renderer for the tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeCellRenderer extends DefaultTreeCellRenderer
{
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
	
	public DexTreeCellRenderer()
	{
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
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		// Default render
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		// Customizing
		if(value instanceof DexRootNode)
			setIcon(root_ico);
		else if(value instanceof DexFolderNode)
		{
			if(expanded)
				setIcon(folder_open_ico);
			else
				setIcon(folder_close_ico);
		}
		else if(value instanceof DexPackageNode)
			setIcon(package_ico);
		else if(value instanceof DexStringsNode)
			setIcon(strings_ico);
		else if(value instanceof DexClassNode)
		{
			ClassGen clazz = (ClassGen) ((DexClassNode) value).getUserObject();
			if(AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags())) // Se la classe
																	// ?? un
																	// interfaccia
				setIcon(interface_ico);
			else
				setIcon(class_ico);
		}
		else if(value instanceof DexMethodNode)
			setIcon(method_ico);
		else if(value instanceof DexFieldNode)
			setIcon(field_ico);
		else if(value instanceof DexAnnotationNode)
			setIcon(annotation_ico);
		return this;
	}
}
