package com.juliasoft.dexstudio.tree;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexFrame;

/**
 * Tree menu for the navigation
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexTree extends JScrollPane
{
	private JTree tree;
	
	/**
	 * Constructor
	 */
	public DexTree()
	{
		this.setPreferredSize(new Dimension(300, 600));
		DexTreeRoot rootNode = new DexTreeRoot("<No dex file loaded>");
	    DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setCellRenderer(new DexTreeCellRenderer());
        tree.setToggleClickCount(0);
        
        tree.addMouseListener(new MouseAdapter()
        {	
        	public void mousePressed(MouseEvent e)
        	{
        		if(e.isPopupTrigger())
            	{
            		mouseRightPressed(e);
            	}
        	}
        	
        	public void mouseReleased(MouseEvent e)
        	{
        		if(e.isPopupTrigger())
            	{
            		mouseRightPressed(e);
            	}
        		else if(e.getClickCount() == 2)
        		{
        			mouseDoubleClicked(e);
        		}
        	}
        });
        
        this.setViewportView(tree);
	}
	
	private void mouseRightPressed(MouseEvent e)
	{
		//Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		Object node = path.getLastPathComponent();
		
		//If I can really visualize a tab for the selected element
		if(!(node instanceof DexTreeRoot))
		{
			//Open the popup menu
			new DexTreePopup(tree, node).show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void mouseDoubleClicked(MouseEvent e)
	{
		//Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		if(node instanceof DexTreeFolder | node instanceof DexTreePackage)
		{
			if(tree.isExpanded(path))
			{
				tree.collapsePath(path);
			}
			else
			{
				tree.expandPath(path);
			}
		}
		else if(!(node instanceof DexTreeRoot) && !(node instanceof DexTreeField))
		{
			((DexFrame) SwingUtilities.getWindowAncestor(this)).changeSelectedTab(node.getUserObject());
		}
	}
	
	/**
	 * Update the content of the tree
	 * @param dexGen		The Amalia DexGen file reference
	 * @param rootLabel		Name for the root node
	 */
	public void updateLayout(DexGen dexGen, String rootLabel)
	{
		DexTreeRoot rootNode = new DexTreeRoot(rootLabel);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        tree.setModel(treeModel);
        DexTreeVisitor treeModelvisitor = new DexTreeVisitor(rootNode);
        dexGen.accept(treeModelvisitor);
        treeModel.nodeStructureChanged(rootNode);
	}
	
	/**
	 * Returns an array of all the class nodes in the tree
	 * @return
	 */
	public ArrayList<DexTreeNode> getNodeArray()
	{
		ArrayList<DexTreeNode> nodes = new ArrayList<DexTreeNode>();
		DexTreeNode root = (DexTreeNode) tree.getModel().getRoot();
		getSubTreeNodeArray(root, nodes);
		return nodes;
	}
	
	private void getSubTreeNodeArray(DexTreeNode node, ArrayList<DexTreeNode> nodes)
	{
		if(node.getChildCount() == 0)
			return;
		
		for(int i=0; i<node.getChildCount(); i++)
		{
			DexTreeNode child = (DexTreeNode) node.getChildAt(i);
			Object obj = child.getUserObject();
			if(obj instanceof ClassGen || obj instanceof Set<?>)
			{
				nodes.add(child);
			}
			getSubTreeNodeArray(child, nodes);
		}
		
	}
	
	/**
	 * Return the ClassGen file of the specified Type
	 * @param type		The specified Type
	 * @return
	 */
	public ClassGen getClassGen(Type type)
	{
		DexTreeRoot root = (DexTreeRoot) tree.getModel().getRoot();
		DexTreeFolder classes = (DexTreeFolder) root.getChildAt(1);
		
		//For every package
		for(int i=0; i<classes.getChildCount(); i++)
		{
			//Get the package
			DexTreePackage pack = (DexTreePackage) classes.getChildAt(i);
			//For every class in the package
			for(int j=0; j<pack.getChildCount(); j++)
			{
				//Get the class
				DexTreeClass clazz = (DexTreeClass) pack.getChildAt(j);
				//Get the ClassGen
				ClassGen classGen = (ClassGen) clazz.getUserObject();
				//If the type is the same
				if(classGen.getType().equals(type))
				{
					return classGen;
				}
			}
		}
		return null;
	}
}
