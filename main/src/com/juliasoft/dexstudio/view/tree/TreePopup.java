package com.juliasoft.dexstudio.view.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.NodeType;

/**
 * Popup menu of the tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class TreePopup extends JPopupMenu
{
	private TreeNode node;
	private JTree tree;
	private TreePopupItem changeTab, newTab, expand;
	private DexDisplay display;
	
	public TreePopup(DexDisplay display, JTree tree, Object node)
	{
		this.display = display;
		this.tree = tree;
		this.node = (TreeNode) node;
		initLayout();
	}
	
	private void initLayout()
	{
		changeTab = new TreePopupItem("Open");
		changeTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object obj = node.getUserObject();
				if(obj instanceof ClassGen)
				{
					display.changeSelectedTab(new DexTab(display, (ClassGen) node.getUserObject()));
				}
				else if(obj instanceof MethodGen)
				{
					display.changeSelectedTab(new DexTab(display, (MethodGen) node.getUserObject()));
				}
				else if(obj instanceof Annotation)
				{
					display.changeSelectedTab(new DexTab(display, (Annotation) node.getUserObject()));
				}
				else if(obj instanceof StringSet)
				{
					display.changeSelectedTab(new DexTab(display, (StringSet) node.getUserObject()));
				}
			}
		});
		newTab = new TreePopupItem("Open in a new tab");
		newTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object obj = node.getUserObject();
				if(obj instanceof ClassGen)
				{
					display.openNewTab(new DexTab(display, (ClassGen) node.getUserObject()));
				}
				else if(obj instanceof MethodGen)
				{
					display.openNewTab(new DexTab(display, (MethodGen) node.getUserObject()));
				}
				else if(obj instanceof Annotation)
				{
					display.openNewTab(new DexTab(display, (Annotation) node.getUserObject()));
				}
				else if(obj instanceof StringSet)
				{
					display.openNewTab(new DexTab(display, (StringSet) node.getUserObject()));
				}
			}
		});
		if(tree.isExpanded(new TreePath(node.getPath())))
		{
			expand = new TreePopupItem("Collapse");
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
			expand = new TreePopupItem("Expand");
			expand.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					tree.expandPath(new TreePath(node.getPath()));
				}
			});
		}
		if(node.getType().equals(NodeType.CLASS) || node.getType().equals(NodeType.METHOD) || node.getType().equals(NodeType.ANNOTATION) || node.getType().equals(NodeType.STRINGS))
		{
			this.add(changeTab);
			this.add(newTab);
		}
		if(!node.isLeaf())
			this.add(expand);
	}
}
