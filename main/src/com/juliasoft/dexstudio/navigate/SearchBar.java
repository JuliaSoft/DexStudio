package com.juliasoft.dexstudio.navigate;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.amalia.dex.codegen.diff.SimpleClassDiff;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.tab.DexCompareTab;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.utils.DexProgress;
import com.juliasoft.dexstudio.view.compare.CompareNode;
import com.juliasoft.dexstudio.view.tree.TreeNode;

/**
 * The input bar on the search window
 * 
 * 
 * @author Matteo Zanoncello
 * 
 */

@SuppressWarnings("serial")
public class SearchBar extends JTextField implements KeyListener {
	private DexFrame frame;
	private DexSearch search;
	private SearchList list;

	public SearchBar(DexFrame frame, DexSearch search, SearchList list) {
		super(50);

		this.frame = frame;
		this.search = search;
		this.list = list;
		this.getDocument().addDocumentListener(new SearchDocumentListener());
		this.addKeyListener(this);
	}

	public void clean() {
		this.setText("");
		list.updateList(new ArrayList<Object>(), "");
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.getCaret().setDot(this.getText().length());
		int sel = list.getSelectedRow();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (sel > 0) {
				list.setRowSelectionInterval(sel - 1, sel - 1);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (sel < list.getRowCount() - 1) {
				list.setRowSelectionInterval(sel + 1, sel + 1);
			}
			break;
		case KeyEvent.VK_ENTER:
			if (sel != -1) {
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
				search.setVisible(false);
				search.dispose();
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public class SearchDocumentListener implements DocumentListener {
		private SearchSwingWorker worker;

		public SearchDocumentListener() {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			search();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			search();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		private synchronized void search() {
			String str = SearchBar.this.getText();
			if (worker != null) {
				worker.stop();
			}
			worker = new SearchSwingWorker(str);
			worker.execute();
		}

		/**
		 * SwingWorker for the search function
		 * 
		 * @author Zanoncello Matteo
		 */
		public class SearchSwingWorker extends
				SwingWorker<ArrayList<Object>, DexProgress> {
			private String str;

			public SearchSwingWorker(String str) {
				this.str = str;
			}

			@Override
			protected ArrayList<Object> doInBackground() throws Exception {
				ArrayList<Object> results = new ArrayList<Object>();
				if (!str.equals("")) {
					for (Object obj : list.getNodes()) {
						if (Thread.currentThread().isInterrupted())
							return null;
						if (obj instanceof TreeNode) {
							if (((TreeNode) obj).getLabel().toLowerCase()
									.startsWith(str.toLowerCase())) {
								results.add(obj);
							}
						} else if (obj instanceof CompareNode) {
							if (((CompareNode) obj).getLabel().toLowerCase()
									.startsWith(str.toLowerCase())) {
								results.add(obj);
							}
						}
					}
				}
				return results;
			}

			@Override
			public void done() {
				try {
					ArrayList<Object> results = get();
					if (results == null) {
						return;
					}
					list.updateList(results, str);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void process(List<DexProgress> list) {
			}

			public void stop() {
				synchronized (this) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
