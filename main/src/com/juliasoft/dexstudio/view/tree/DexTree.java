package com.juliasoft.dexstudio.view.tree;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.DexView;
import com.juliasoft.dexstudio.view.NodeType;

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
		TreeNode rootNode = new TreeNode(NodeType.ROOT, "<No dex file loaded>");
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new TreeCellRenderer());
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
		if(node instanceof TreeNode && !((TreeNode)node).getType().equals(NodeType.ROOT))
		{
			// Open the popup menu
			new TreePopup(frame, tree, node).show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void mouseDoubleClicked(MouseEvent e)
	{
		// Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		TreeNode node = (TreeNode) path.getLastPathComponent();
		if(node.getType().equals(NodeType.FOLDER) || node.getType().equals(NodeType.PACKAGE))
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
		else if(node.getType().equals(NodeType.CLASS) || node.getType().equals(NodeType.INTERFACE))
		{
			frame.changeSelectedTab(new DexTreeTab(frame, (ClassGen) node.getUserObject()));
		}
		else if(node.getType().equals(NodeType.METHOD))
		{
			frame.changeSelectedTab(new DexTreeTab(frame, (MethodGen) node.getUserObject()));
		}
		else if(node.getType().equals(NodeType.ANNOTATION))
		{
			frame.changeSelectedTab(new DexTreeTab(frame, (Annotation) node.getUserObject()));
		}
		else if(node.getType().equals(NodeType.STRINGS))
		{
			frame.changeSelectedTab(new DexTreeTab(frame, (StringSet) node.getUserObject()));
		}
	}
	
	public void updateLayout(DexGen dexGen, String rootLabel)
	{
		TreeNode rootNode = new TreeNode(NodeType.ROOT, rootLabel);
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		TreeVisitor treeModelvisitor = new TreeVisitor(rootNode);
		dexGen.accept(treeModelvisitor);
		treeModel.nodeStructureChanged(rootNode);
		((DexFrame) frame).getViewManager().setTabComponentAt(0, this.getTabTitle());
	}
	
	public void cleanTree()
	{
		TreeNode rootNode = new TreeNode(NodeType.ROOT, "<No dex file loaded>");
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		treeModel.nodeStructureChanged(rootNode);
	}
	
	/**
	 * Returns an array of all the class nodes in the tree
	 * 
	 * @return
	 */
	public ArrayList<Object> getClassNodes()
	{
		ArrayList<Object> nodes = new ArrayList<Object>();
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		root = (TreeNode) root.getChildAt(1);
		for(int i=0; i<root.getChildCount(); i++)
		{
			TreeNode pkg = (TreeNode) root.getChildAt(i);
			for(int j=0; j<pkg.getChildCount(); j++)
			{
				nodes.add(pkg.getChildAt(j));
			}
		}
		return nodes;
	}
	
	@Override
	public String getName()
	{
		return ((TreeNode)tree.getModel().getRoot()).getLabel();
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
