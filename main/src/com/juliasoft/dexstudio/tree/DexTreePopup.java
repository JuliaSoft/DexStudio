package com.juliasoft.dexstudio.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.juliasoft.dexstudio.DexFrame;


/**
 * Popup menu of the tree
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexTreePopup extends JPopupMenu
{
	private DefaultMutableTreeNode node;
	private JTree tree;
	
	DexTreePopupItem changeTab, newTab, expand;
	
	public DexTreePopup(JTree tree, Object node)
	{
		this.tree = tree;
		this.node = (DefaultMutableTreeNode) node;
		initLayout();
	}
	
	private void initLayout()
	{
		changeTab = new DexTreePopupItem("Open");
		changeTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DexFrame) SwingUtilities.getWindowAncestor(tree)).changeSelectedTab(node.getUserObject());
			}
		});
		
		newTab = new DexTreePopupItem("Open in a new tab");
		newTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DexFrame) SwingUtilities.getWindowAncestor(tree)).openNewTab(node.getUserObject());
			}
		});
		
		if(tree.isExpanded(new TreePath(node.getPath())))
		{
			expand = new DexTreePopupItem("Collapse");
			expand.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					tree.collapsePath(new TreePath(node.getPath()));
				}
			});
		}
		else
		{
			expand = new DexTreePopupItem("Expand");
			expand.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					tree.expandPath(new TreePath(node.getPath()));
				}
			});
		}
		
		if(node instanceof DexTreeClass || node instanceof DexTreeMethod || node instanceof DexTreeAnnotation || node instanceof DexTreeStrings)
		{
			this.add(changeTab);
			this.add(newTab);
		}
		if(!node.isLeaf())
			this.add(expand);
	}
	
}
