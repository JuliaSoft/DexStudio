package com.juliasoft.dexstudio.view.compare;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexCompareTab;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.view.NodeType;
import com.juliasoft.dexstudio.view.PopupItem;

/**
 * Popup menu of the tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class ComparePopup extends JPopupMenu {
	private CompareNode node;
	private JTree tree;
	private PopupItem changeTab, newTab, expand;
	private DexDisplay display;

	public ComparePopup(DexDisplay display, JTree tree, Object node) {
		this.display = display;
		this.tree = tree;
		this.node = (CompareNode) node;
		initLayout();
	}

	private void initLayout() {
		changeTab = new PopupItem("Open");
		changeTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DiffNode<?> diff = node.getDiff();
				if (diff.getDiffClass().equals(ClassGen.class)) {
					display.changeSelectedTab(new DexCompareTab(display,
							(ClassGen) diff.getLeft(), (ClassGen) diff
									.getRight()));
				}
				if (diff.getDiffClass().equals(MethodGen.class)) {
					display.changeSelectedTab(new DexCompareTab(display,
							(MethodGen) diff.getLeft(), (MethodGen) diff
									.getRight()));
				}
				if (diff.getDiffClass().equals(Annotation.class)) {
					display.changeSelectedTab(new DexCompareTab(display,
							(Annotation) diff.getLeft(), (Annotation) diff
									.getRight()));
				} else if (diff.getDiffClass().equals(ContextGen.class)) {
					display.changeSelectedTab(new DexTreeTab(display, diff));
				}
			}
		});
		newTab = new PopupItem("Open in a new tab");
		newTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DiffNode<?> diff = node.getDiff();
				if (diff.getDiffClass().equals(ClassGen.class)) {
					display.openNewTab(new DexCompareTab(display,
							(ClassGen) diff.getLeft(), (ClassGen) diff
									.getRight()));
				}
				if (diff.getDiffClass().equals(MethodGen.class)) {
					display.openNewTab(new DexCompareTab(display,
							(MethodGen) diff.getLeft(), (MethodGen) diff
									.getRight()));
				}
				if (diff.getDiffClass().equals(Annotation.class)) {
					display.openNewTab(new DexCompareTab(display,
							(Annotation) diff.getLeft(), (Annotation) diff
									.getRight()));
				} else if (diff.getDiffClass().equals(ContextGen.class)) {
					display.openNewTab(new DexTreeTab(display, diff));
				}
			}
		});
		if (tree.isExpanded(new TreePath(node.getPath()))) {
			expand = new PopupItem("Collapse");
			expand.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tree.collapsePath(new TreePath(node.getPath()));
				}
			});
		} else {
			expand = new PopupItem("Expand");
			expand.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tree.expandPath(new TreePath(node.getPath()));
				}
			});
		}
		if (node.getType().equals(NodeType.CLASS)
				|| node.getType().equals(NodeType.METHOD)
				|| node.getType().equals(NodeType.ANNOTATION)
				|| node.getType().equals(NodeType.STRINGS)) {
			this.add(changeTab);
			this.add(newTab);
		}
		if (!node.isLeaf())
			this.add(expand);
	}
}
