package com.juliasoft.dexstudio.search;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.tree.DexTreeNode;

/**
 * Search dialog of DexStudio
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexSearch extends JDialog implements WindowListener
{
	private DexFrame frame;
	private DexSearchList list;
	private DexSearchTextField searchBar;
	
	public DexSearch(final DexFrame frame)
	{
		this.frame = frame;
		// Setting Layout
		this.setIconImage(new ImageIcon("imgs/logo.png").getImage());
		frame.setEnabled(false);
		int width = frame.getSize().width;
		int height = frame.getSize().height;
		this.setBounds(width / 3, height / 3, width / 2, height / 2);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setTitle("Search");
		this.addWindowListener(this);
		list = new DexSearchList();
		searchBar = new DexSearchTextField(frame, this, list);
		searchBar.requestFocus();
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Open the selected file
				DexTreeNode content = (DexTreeNode) list.getValueAt(list.getSelectedRow(), 1);
				if(content != null)
				{
					frame.changeSelectedTab(content);
					DexSearch.this.setVisible(false);
					DexSearch.this.dispatchEvent(new WindowEvent(DexSearch.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DexSearch.this.setVisible(false);
				DexSearch.this.dispatchEvent(new WindowEvent(DexSearch.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(ok);
		bottom.add(cancel);
		this.add(searchBar, BorderLayout.NORTH);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		DexSearch.this.frame.setEnabled(true);
		DexSearch.this.frame.requestFocus();
	}
	
	@Override
	public void windowOpened(WindowEvent e)
	{}
	
	@Override
	public void windowClosed(WindowEvent e)
	{}
	
	@Override
	public void windowIconified(WindowEvent e)
	{}
	
	@Override
	public void windowDeiconified(WindowEvent e)
	{}
	
	@Override
	public void windowActivated(WindowEvent e)
	{}
	
	@Override
	public void windowDeactivated(WindowEvent e)
	{}
}
