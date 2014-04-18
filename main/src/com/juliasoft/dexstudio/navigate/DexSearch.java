package com.juliasoft.dexstudio.navigate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.amalia.dex.codegen.diff.SimpleClassDiff;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.exception.ViewNotFoundException;
import com.juliasoft.dexstudio.tab.DexCompareTab;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.view.DexViewManager;
import com.juliasoft.dexstudio.view.compare.CompareNode;
import com.juliasoft.dexstudio.view.tree.TreeNode;

/**
 * Main class for the search function
 * 
 * 
 * @author Matteo Zanoncello
 * 
 */
@SuppressWarnings("serial")
public class DexSearch extends JDialog {
	private SearchBar search;
	private SearchList list;
	private JComboBox<String> viewSelect;

	public DexSearch(final DexFrame frame) {
		this.setIconImage(new ImageIcon("imgs/logo.png").getImage());
		int width = frame.getSize().width;
		int height = frame.getSize().height;
		this.setBounds(width / 3, height / 3, width / 2, height / 2);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setTitle("Search");
		viewSelect = new JComboBox<String>();
		DexViewManager viewManager = frame.getViewManager();
		try {
			for (int i = 0; i < viewManager.getTabCount(); i++) {
				viewSelect.addItem(viewManager.getView(i).getName());
			}
		} catch (ViewNotFoundException e) {
			e.printStackTrace();
		}
		viewSelect.setSelectedIndex(0);
		viewSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.updateView(viewSelect.getSelectedIndex());
				search.clean();
			}
		});
		list = new SearchList(frame);
		list.updateView(0);
		search = new SearchBar(frame, this, list);
		search.requestFocus();
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the selected file
				Object item = list.getSelectedItem();
				if (item instanceof TreeNode) {
					ClassGen clazz = (ClassGen) ((TreeNode) item)
							.getUserObject();
					frame.openNewTab(new DexTreeTab(frame, clazz));
				} else if (item instanceof CompareNode) {
					SimpleClassDiff diff = (SimpleClassDiff) ((CompareNode) item)
							.getDiff();
					if (diff.getState().equals(DiffState.SAME))
						frame.openNewTab(new DexTreeTab(frame, diff.getLeft()));
					else
						frame.openNewTab(new DexCompareTab(frame, diff
								.getLeft(), diff.getRight()));
				}
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DexSearch.this.dispose();
			}
		});
		JPanel top = new JPanel(new BorderLayout());
		top.add(viewSelect, BorderLayout.NORTH);
		top.add(search, BorderLayout.CENTER);
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(ok);
		bottom.add(cancel);
		this.add(top, BorderLayout.NORTH);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
	}
}
