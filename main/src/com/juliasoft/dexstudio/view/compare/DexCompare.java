package com.juliasoft.dexstudio.view.compare;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.diff.DexDiff;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.exception.ViewNotFoundException;
import com.juliasoft.dexstudio.tab.DexCompareTab;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.DexView;
import com.juliasoft.dexstudio.view.NodeType;

/**
 * Tree reperesentation of the structure of an apk in comparison view
 * 
 * 
 * @author Matteo Zanoncello
 * 
 */
@SuppressWarnings("serial")
public class DexCompare extends DexView {
	private String name;
	private DexFrame frame;
	private JTree tree;
	private Map<String, CompareNode> pkgs = new TreeMap<String, CompareNode>();

	public DexCompare(DexFrame frame, String name) {
		this.name = name;
		this.frame = frame;
		this.setPreferredSize(new Dimension(300, 600));
		CompareNode defaultRoot = new CompareNode(NodeType.ROOT,
				"<No dex file loaded>");
		DefaultTreeModel treeModel = new DefaultTreeModel(defaultRoot);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new CompareCellRenderer());
		tree.setToggleClickCount(0);
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					mouseRightPressed(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					mouseRightPressed(e);
				} else if (e.getClickCount() == 2) {
					mouseDoubleClicked(e);
				}
			}
		});
		this.setViewportView(tree);
	}

	public void update(DexDiff diff) {
		diff.update(true);
		CompareNode root = new CompareNode(NodeType.ROOT, this.getName());
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		CompareNode context = new CompareNode(NodeType.FOLDER, "Context");
		CompareNode classes = new CompareNode(NodeType.FOLDER, "Classes");
		root.add(context);
		context.add(new CompareNode(NodeType.STRINGS, diff.getContextDiff()));
		root.add(classes);
		for (DiffNode<?> child : diff.getClassDiffs()) {
			updateNode(child, classes);
		}
		treeModel.nodeStructureChanged(root);
	}

	private void updateNode(DiffNode<?> diff, CompareNode node)
			throws IllegalArgumentException {
		CompareNode new_node = null;
		if (diff.getDiffClass().equals(ClassGen.class)) {
			ClassGen clazz = (diff.getState().equals(DiffState.RIGHT_ONLY)) ? (ClassGen) diff
					.getRight() : (ClassGen) diff.getLeft();
			String pkg = clazz.toHuman();
			if (pkg.contains(".")) {
				pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			} else {
				pkg = "[Default Package]";
			}
			// If not exists, add the new package node
			if (!pkgs.containsKey(pkg)) {
				CompareNode pkg_node = new CompareNode(NodeType.PACKAGE, pkg);
				pkgs.put(pkg, pkg_node);
				node.add(pkg_node);
			}
			CompareNode pkg_node = pkgs.get(pkg);
			pkg_node.setState(pkg_node.updateState(diff.getState()));
			// Se la classe e' un interfaccia
			if (AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags())) {
				new_node = new CompareNode(NodeType.INTERFACE, diff);
				pkg_node.add(new_node);
			} else {
				new_node = new CompareNode(NodeType.CLASS, diff);
				pkg_node.add(new_node);
			}
		} else if (diff.getDiffClass().equals(FieldGen.class)) {
			new_node = new CompareNode(NodeType.FIELD, diff);
			node.add(new_node);
		} else if (diff.getDiffClass().equals(MethodGen.class)) {
			new_node = new CompareNode(NodeType.METHOD, diff);
			node.add(new_node);
		} else if (diff.getDiffClass().equals(Annotation.class)) {
			new_node = new CompareNode(NodeType.ANNOTATION, diff);
			node.add(new_node);
		} else {
			throw new IllegalArgumentException();
		}
		for (DiffNode<?> child : diff.getChildren()) {
			updateNode(child, new_node);
		}
	}

	@Override
	public String getName() {
		return this.name.substring(name.lastIndexOf('/') + 1);
	}

	private void mouseRightPressed(MouseEvent e) {
		// Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		Object node = path.getLastPathComponent();
		// If I can really visualize a tab for the selected element
		if (node instanceof CompareNode
				&& !((CompareNode) node).getType().equals(NodeType.ROOT)) {
			// Open the popup menu
			new ComparePopup(frame, tree, node).show(e.getComponent(),
					e.getX(), e.getY());
		}
	}

	private void mouseDoubleClicked(MouseEvent e) {
		// Get the selected element of the tree
		TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
		CompareNode node = (CompareNode) path.getLastPathComponent();
		if (node.getType().equals(NodeType.FOLDER)
				|| node.getType().equals(NodeType.PACKAGE)) {
			if (tree.isExpanded(path)) {
				tree.collapsePath(path);
			} else {
				tree.expandPath(path);
			}
		} else {
			if (node.getDiff().getState().equals(DiffState.SAME)) {
				if (node.getType().equals(NodeType.CLASS)
						|| node.getType().equals(NodeType.INTERFACE)) {
					frame.changeSelectedTab(new DexTreeTab(frame,
							(ClassGen) node.getDiff().getLeft()));
				} else if (node.getType().equals(NodeType.METHOD)) {
					frame.changeSelectedTab(new DexTreeTab(frame,
							(MethodGen) node.getDiff().getLeft()));
				} else if (node.getType().equals(NodeType.ANNOTATION)) {
					frame.changeSelectedTab(new DexTreeTab(frame,
							(Annotation) node.getDiff().getLeft()));
				} else if (node.getType().equals(NodeType.STRINGS)) {
					frame.changeSelectedTab(new DexTreeTab(frame,
							new StringSet(((ContextGen) node.getDiff()
									.getLeft()).getStrings())));
				}
			} else {
				if (node.getType().equals(NodeType.CLASS)
						|| node.getType().equals(NodeType.INTERFACE)) {
					frame.changeSelectedTab(new DexCompareTab(frame,
							(ClassGen) node.getDiff().getLeft(),
							(ClassGen) node.getDiff().getRight()));
				} else if (node.getType().equals(NodeType.METHOD)) {
					frame.changeSelectedTab(new DexCompareTab(frame,
							(MethodGen) node.getDiff().getLeft(),
							(MethodGen) node.getDiff().getRight()));
				} else if (node.getType().equals(NodeType.ANNOTATION)) {
					frame.changeSelectedTab(new DexCompareTab(frame,
							(Annotation) node.getDiff().getLeft(),
							(Annotation) node.getDiff().getRight()));
				} else if (node.getType().equals(NodeType.STRINGS)) {
					frame.changeSelectedTab(new DexTreeTab(frame, node
							.getDiff()));
				}
			}
		}
	}

	@Override
	public JPanel getTabTitle() {
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
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					frame.getViewManager()
							.removeView(DexCompare.this.getName());
				} catch (ViewNotFoundException e1) {
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

	public ArrayList<Object> getClassNodes() {
		ArrayList<Object> nodes = new ArrayList<Object>();
		CompareNode root = (CompareNode) tree.getModel().getRoot();
		root = (CompareNode) root.getChildAt(1);
		for (int i = 0; i < root.getChildCount(); i++) {
			CompareNode pkg = (CompareNode) root.getChildAt(i);
			for (int j = 0; j < pkg.getChildCount(); j++) {
				nodes.add((CompareNode) pkg.getChildAt(j));
			}
		}
		return nodes;
	}
}
