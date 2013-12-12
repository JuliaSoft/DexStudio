package com.juliasoft.dexstudio.search;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.tree.DexTreeNode;
import com.juliasoft.dexstudio.utils.DexProgress;

/**
 * TextField for the search dialog
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexSearchTextField extends JTextField implements KeyListener
{
	private DexFrame frame;
	private DexSearch dialog;
	private DexSearchList list;
	
	/**
	 * Constructor
	 * @param frame		The DexFrame reference
	 * @param dialog	The DexSearch reference
	 * @param list		The DexSearchList reference
	 */
	public DexSearchTextField(DexFrame frame, DexSearch dialog, DexSearchList list)
	{
		super(50);
		this.frame = frame;
		this.dialog = dialog;
		this.list = list;
		this.getDocument().addDocumentListener(new DexSearchDocumentListener());
		this.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		this.getCaret().setDot(this.getText().length());
		int sel = list.getSelectedRow();
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			if(sel > 0){
				list.setRowSelectionInterval(sel - 1, sel - 1);
			}
			break;
		case KeyEvent.VK_DOWN:
			if(sel < list.getRowCount() - 1){
				list.setRowSelectionInterval(sel + 1, sel + 1);
			}
			break;
		case KeyEvent.VK_ENTER:
			if(sel != -1)
			{
				DexTreeNode node = (DexTreeNode)list.getModel().getValueAt(sel, 1);
				frame.changeSelectedTab(node.getUserObject());
				dialog.setVisible(false);
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	/**
	 * DocumentListener for DeexSearchTextField
	 * @author Zanoncello Matteo
	 *
	 */
	public class DexSearchDocumentListener implements DocumentListener
	{
		private ArrayList<DexTreeNode> nodes;
		private DexSearchSwingWorker worker;
		
		public DexSearchDocumentListener()
		{
			nodes = frame.getTree().getNodeArray();
		}
		
		@Override
		public void insertUpdate(DocumentEvent e)
		{
			search();
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			search();
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {}
		
		private synchronized void search()
		{
			String str = DexSearchTextField.this.getText();
			if(worker != null)
			{
				worker.stop();
			}
			worker = new DexSearchSwingWorker(str);
			worker.execute();
		}
		
		/**
		 * SwingWorker for the search function
		 * @author Zanoncello Matteo
		 *
		 */
		public class DexSearchSwingWorker extends SwingWorker<TreeSet<DexTreeNode>, DexProgress>
		{
			private String str;
			
			public DexSearchSwingWorker(String str)
			{
				this.str = str;
			}

			@Override
			protected TreeSet<DexTreeNode> doInBackground() throws Exception
			{
				if(str.equals(""))
				{
					list.updateRows(new TreeSet<DexTreeNode>(), "");
					return null;
				}
				
				TreeSet<DexTreeNode> results = new TreeSet<DexTreeNode>();
				
				for(DexTreeNode node : nodes)
				{
					if(Thread.currentThread().isInterrupted())
						return null;
					
					if(node.toString().toLowerCase().startsWith(str.toLowerCase()))
					{
						results.add(node);
					}
				}
				return results;
			}
			
			@Override
			public void done()
			{
				try
				{
					TreeSet<DexTreeNode> results = get();
					if(results == null)
					{
						return;
					}
					list.updateRows(results, str);
				}
				catch (InterruptedException | ExecutionException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void process(List<DexProgress> list)
			{
				
			}
			
			public void stop()
			{
				synchronized(this)
				{
					Thread.currentThread().interrupt();
				}
			}
			
		}
	}
}
