package com.juliasoft.dexstudio.view.cmp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.diff.AnnotationDiff;
import com.juliasoft.amalia.dex.codegen.diff.DexDiff;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.amalia.dex.codegen.diff.FieldDiff;
import com.juliasoft.amalia.dex.codegen.diff.MethodDiff;
import com.juliasoft.amalia.dex.codegen.diff.SimpleClassDiff;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.exception.ViewNotFoundException;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.DexView;
import com.juliasoft.dexstudio.view.cmp.node.DexAnnotationCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexClassCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexFieldCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexMethodCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexPackageCmp;
import com.juliasoft.dexstudio.view.tree.DexTreePopup;
import com.juliasoft.dexstudio.view.tree.node.DexAnnotationNode;
import com.juliasoft.dexstudio.view.tree.node.DexClassNode;
import com.juliasoft.dexstudio.view.tree.node.DexFolderNode;
import com.juliasoft.dexstudio.view.tree.node.DexMethodNode;
import com.juliasoft.dexstudio.view.tree.node.DexPackageNode;
import com.juliasoft.dexstudio.view.tree.node.DexRootNode;
import com.juliasoft.dexstudio.view.tree.node.DexStringsNode;
import com.juliasoft.dexstudio.view.tree.node.DexTreeNode;

@SuppressWarnings("serial")
public class DexCmp extends DexView
{
	private String name;
	private DexFrame frame;
	private JTree tree = new JTree();
	private Map<String, DexPackageCmp> pkgs = new TreeMap<String, DexPackageCmp>();
	
	public DexCmp(DexFrame frame, String name)
	{
		this.name = name;
		this.frame = frame;
		this.setPreferredSize(new Dimension(300, 600));
		tree.setCellRenderer(new DexCmpCellRenderer());
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
	
	public void update(DexDiff diff)
	{
		diff.update(true);
		DexRootNode rootNode = new DexRootNode(this.getName());
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		tree.setModel(treeModel);
		DexFolderNode context = new DexFolderNode("Context");
		DexFolderNode classes = new DexFolderNode("Classes");
		rootNode.add(context);
		//context.add(new DexStringsCmp(null)); TODO: strings comparison
		rootNode.add(classes);
		for(DiffNode<?> child : diff.getClassDiffs())
		{
			updateNode(child, classes);
		}
		treeModel.nodeStructureChanged(rootNode);
	}
	
	private void updateNode(DiffNode<?> diff, DexTreeNode<?> node)
	{
		DexTreeNode<?> new_node = null;
		if(diff.getDiffClass().equals(ClassGen.class))
		{
			ClassGen clazz = (diff.getState().equals(DiffState.RIGHT_ONLY)) ? (ClassGen) diff.getRight() : (ClassGen) diff.getLeft();
			String pkg = clazz.toHuman();
			if(pkg.contains("."))
			{
				pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			}
			else
			{
				pkg = "[Default Package]";
			}
			// If not exists, add the new package node
			if(!pkgs.containsKey(pkg))
			{
				DexPackageCmp pkg_node = new DexPackageCmp(pkg);
				pkgs.put(pkg, pkg_node);
				node.add(pkg_node);
			}
			DexPackageCmp pkg_node = pkgs.get(pkg);
			pkg_node.setState(updateState(pkg_node.getState(), diff.getState()));
			new_node = new DexClassCmp(new SimpleClassDiff((ClassGen)diff.getLeft(), (ClassGen)diff.getRight()));
			pkg_node.add(new_node);
		}
		else if(diff.getDiffClass().equals(FieldGen.class))
		{
			new_node = new DexFieldCmp(new FieldDiff((FieldGen)diff.getLeft(), (FieldGen)diff.getRight()));
			node.add(new_node);
		}
		else if(diff.getDiffClass().equals(MethodGen.class))
		{
			new_node = new DexMethodCmp(new MethodDiff((MethodGen)diff.getLeft(), (MethodGen)diff.getRight()));
			node.add(new_node);
		}
		else if(diff.getDiffClass().equals(Annotation.class))
		{
			new_node = new DexAnnotationCmp(new AnnotationDiff((Annotation)diff.getLeft(), (Annotation)diff.getRight()));
			node.add(new_node);
		}
		else
		{
			new_node = new DexFolderNode("<missing>");
			node.add(new_node);
		}
		for(DiffNode<?> child : diff.getChildren())
		{
			updateNode(child, new_node);
		}
	}
	
	private DiffState updateState(DiffState actual, DiffState child)
	{
		if(child.equals(DiffState.UNKNOWN))
		{
			return DiffState.UNKNOWN;
		}
		switch(actual)
		{
			case SAME:
				if(!child.equals(DiffState.SAME))
					return child;
				return DiffState.SAME;
			case UNKNOWN:
				return DiffState.UNKNOWN;
			case DIFFERENT:
				return DiffState.DIFFERENT;
			case LEFT_ONLY:
				if(child.equals(DiffState.RIGHT_ONLY) || child.equals(DiffState.SAME) || child.equals(DiffState.DIFFERENT))
					return DiffState.DIFFERENT;
				return DiffState.LEFT_ONLY;
			case RIGHT_ONLY:
				if(child.equals(DiffState.LEFT_ONLY) || child.equals(DiffState.SAME) || child.equals(DiffState.DIFFERENT))
					return DiffState.DIFFERENT;
				return DiffState.LEFT_ONLY;
		}
		return DiffState.UNKNOWN;
	}
	
	@Override
	public String getName()
	{
		return this.name.substring(name.lastIndexOf('/') + 1);
	}
	
	private void mouseRightPressed(MouseEvent e)
	{
		// Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		Object node = path.getLastPathComponent();
		// If I can really visualize a tab for the selected element
		if(!(node instanceof DexRootNode))
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
		if(node instanceof DexFolderNode | node instanceof DexPackageNode)
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
		else if(node instanceof DexClassNode)
		{
			frame.changeSelectedTab(new DexTab(frame, (ClassGen) node.getUserObject()));
		}
		else if(node instanceof DexMethodNode)
		{
			frame.changeSelectedTab(new DexTab(frame, (MethodGen) node.getUserObject()));
		}
		else if(node instanceof DexAnnotationNode)
		{
			frame.changeSelectedTab(new DexTab(frame, (Annotation) node.getUserObject()));
		}
		else if(node instanceof DexStringsNode)
		{
			frame.changeSelectedTab(new DexTab(frame, (StringSet) node.getUserObject()));
		}
	}
	
	@Override
	public JPanel getTabTitle()
	{
		JPanel res = new JPanel(new GridBagLayout());
		res.setOpaque(false);
		JLabel icon = new JLabel(new ImageIcon("imgs/tab/cmp.png"));
		JLabel title = new JLabel(this.getName());
		title.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		JButton close = new JButton(new ImageIcon("imgs/tab/close.png"));
		close.setPreferredSize(new Dimension(17, 17));
		close.setToolTipText("close this tab");
		close.setUI(new BasicButtonUI());
		close.setContentAreaFilled(false);
		close.setFocusable(false);
		close.setBorder(BorderFactory.createEtchedBorder());
		close.setBorderPainted(false);
		close.addMouseListener(closeButtonMouseListener);
		close.setRolloverEnabled(true);
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					frame.getViewManager().removeView(DexCmp.this.getName());
				}
				catch(ViewNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		res.add(icon, gbc);
		res.add(title, gbc);
		res.add(close, gbc);
		return res;
	}
}
