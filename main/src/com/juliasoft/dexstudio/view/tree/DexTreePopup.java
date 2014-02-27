package com.juliasoft.dexstudio.view.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.tree.node.DexAnnotationNode;
import com.juliasoft.dexstudio.view.tree.node.DexClassNode;
import com.juliasoft.dexstudio.view.tree.node.DexMethodNode;
import com.juliasoft.dexstudio.view.tree.node.DexStringsNode;

/**
 * Popup menu of the tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreePopup extends JPopupMenu
{
	private DefaultMutableTreeNode node;
	private JTree tree;
	private DexTreePopupItem changeTab, newTab, expand;
	private DexDisplay display;
	
	public DexTreePopup(DexDisplay display, JTree tree, Object node)
	{
		this.display = display;
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
		newTab = new DexTreePopupItem("Open in a new tab");
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
		if(node instanceof DexClassNode || node instanceof DexMethodNode || node instanceof DexAnnotationNode || node instanceof DexStringsNode)
		{
			this.add(changeTab);
			this.add(newTab);
		}
		if(!node.isLeaf())
			this.add(expand);
	}
}
