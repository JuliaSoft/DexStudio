package com.juliasoft.dexstudio.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;

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
		//Initializing images
		root_ico = new ImageIcon("imgs/tree/root.png");
		folder_close_ico = new ImageIcon("imgs/tree/folder_close.png");
		folder_open_ico = new ImageIcon("imgs/tree/folder_open.png");
		strings_ico = new ImageIcon("imgs/tree/strings.png");
		package_ico = new ImageIcon("imgs/tree/package.png");
		class_ico = new ImageIcon("imgs/tree/class.png");
		interface_ico = new ImageIcon("imgs/tree/interface.png");
		field_ico = new ImageIcon("imgs/tree/field.png");
		method_ico = new ImageIcon("imgs/tree/method.png");
		annotation_ico = new ImageIcon("imgs/tree/annotation.png");
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		//Default render
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		//Customizing
		if(value instanceof DexTreeRoot)
			setIcon(root_ico);
		
		else if(value instanceof DexTreeFolder)
		{
			if(expanded)
				setIcon(folder_open_ico);
			else
				setIcon(folder_close_ico);
		}
		
		else if(value instanceof DexTreePackage)
			setIcon(package_ico);
		
		else if(value instanceof DexTreeStrings)
			setIcon(strings_ico);
		
		else if(value instanceof DexTreeClass)
		{
			ClassGen clazz = (ClassGen)((DexTreeClass) value).getUserObject();
			if(AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags()))	//Se la classe ?? un interfaccia
				setIcon(interface_ico);
			else
				setIcon(class_ico);
		}
		
		else if(value instanceof DexTreeMethod)
			setIcon(method_ico);
		
		else if(value instanceof DexTreeField)
			setIcon(field_ico);
		
		else if(value instanceof DexTreeAnnotation)
			setIcon(annotation_ico);
		
        return this;
	}
}
