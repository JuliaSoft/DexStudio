package com.juliasoft.dexstudio.view.tree;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.DexView;

@SuppressWarnings("serial")
public class DexTree extends DexView
{
	private JTree tree;
	private DexDisplay frame;
	
	public DexTree(DexDisplay frame)
	{
		this.frame = frame;
		initLayout();
	}
	
	private void initLayout()
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
		// Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		Object node = path.getLastPathComponent();
		// If I can really visualize a tab for the selected element
		if(!(node instanceof DexTreeRoot))
		{
			// Open the popup menu
			new DexTreePopup(frame, tree, node).show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void mouseDoubleClicked(MouseEvent e)
	{
		// Get the selected element of the tree
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
		else if(node instanceof DexTreeClass)
		{
			frame.changeSelectedTab(new DexTab(frame, (ClassGen) node.getUserObject()));
		}
		else if(node instanceof DexTreeMethod)
		{
			frame.changeSelectedTab(new DexTab(frame, (MethodGen) node.getUserObject()));
		}
		else if(node instanceof DexTreeAnnotation)
		{
			frame.changeSelectedTab(new DexTab(frame, (Annotation) node.getUserObject()));
		}
		else if(node instanceof DexTreeStrings)
		{
			frame.changeSelectedTab(new DexTab(frame, (StringSet) node.getUserObject()));
		}
	}
	
	public void updateLayout(DexGen dexGen, String rootLabel)
	{
		DexTreeRoot rootNode = new DexTreeRoot(rootLabel);
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		DexTreeVisitor treeModelvisitor = new DexTreeVisitor(rootNode);
		dexGen.accept(treeModelvisitor);
		treeModel.nodeStructureChanged(rootNode);
	}
	
	public void cleanTree()
	{
		DexTreeRoot rootNode = new DexTreeRoot("<No dex file loaded>");
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		treeModel.nodeStructureChanged(rootNode);
	}
	
	/**
	 * Returns an array of all the classes, methods and annotations nodes in the
	 * tree
	 * 
	 * @return
	 */
	public ArrayList<DexTreeNode> getNodeArray()
	{
		ArrayList<DexTreeNode> nodes = new ArrayList<DexTreeNode>();
		DexTreeNode root = (DexTreeNode) tree.getModel().getRoot();
		getSubTreeNodeArray(root, nodes);
		return nodes;
	}
	
	/**
	 * Returns an array of all the class,method and annotation nodes contained
	 * under the giver node
	 * 
	 * @param node
	 * @param nodes
	 */
	private void getSubTreeNodeArray(DexTreeNode node, ArrayList<DexTreeNode> nodes)
	{
		if(node.getChildCount() == 0)
			return;
		for(int i = 0; i < node.getChildCount(); i++)
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
	
	public ClassGen getClassGen(Type type)
	{
		DexTreeRoot root = (DexTreeRoot) tree.getModel().getRoot();
		DexTreeFolder classes = (DexTreeFolder) root.getChildAt(1);
		// For every package
		for(int i = 0; i < classes.getChildCount(); i++)
		{
			// Get the package
			DexTreePackage pack = (DexTreePackage) classes.getChildAt(i);
			// For every class in the package
			for(int j = 0; j < pack.getChildCount(); j++)
			{
				// Get the class
				DexTreeClass clazz = (DexTreeClass) pack.getChildAt(j);
				// Get the ClassGen
				ClassGen classGen = (ClassGen) clazz.getUserObject();
				// If the type is the same
				if(classGen.getType().equals(type))
				{
					return classGen;
				}
			}
		}
		return null;
	}
	
	@Override
	public String getName()
	{
		return "Packages";
	}
	
	@Override
	public JPanel getTabTitle()
	{
		JPanel res = new JPanel(new GridBagLayout());
		res.setOpaque(false);
		JLabel title = new JLabel(this.getName());
		title.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		GridBagConstraints gbc = new GridBagConstraints();
		res.add(title, gbc);
		return res;
	}
}