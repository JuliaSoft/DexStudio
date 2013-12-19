package com.juliasoft.dexstudio.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.search.DexSearch;

/**
 * The menu bar of the frame
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexMenu extends JMenuBar
{
	private boolean open = false;
	private boolean compare = false;
	
	private DexMenuItem menuFileOpen, menuFileSave, menuFileClose, menuCompareOpen, menuCompareClose, menuNavigateSearch, menuInfoHelp, menuInfoAbout;
	
	/**
	 * Constructor
	 */
	public DexMenu()
	{
		// Menu Items
		menuFileOpen = new DexMenuItem("Open");
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		menuFileOpen.setIcon(new ImageIcon("imgs/menu/open.png"));
		menuFileOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DexFrame) SwingUtilities.getWindowAncestor(DexMenu.this)).openApk();
				if(!isOpen())
					open();
			}
		});
		menuFileSave = new DexMenuItem("Save");
		menuFileSave.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
		menuFileSave.setIcon(new ImageIcon("imgs/menu/save.png"));
		menuFileSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
			}
		});
		menuFileClose = new DexMenuItem("Close");
		menuFileClose.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_DOWN_MASK));
		menuFileClose.setIcon(new ImageIcon("imgs/menu/close.png"));
		menuFileClose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DexFrame) SwingUtilities.getWindowAncestor(DexMenu.this)).dispose();
			}
		});
		menuCompareOpen = new DexMenuItem("Compare Apk");
		menuCompareOpen.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_DOWN_MASK));
		//menuCompareOpen.setIcon(new ImageIcon("imgs/menu/search.png"));
		menuCompareOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
				compare();
			}
		});
		menuCompareClose = new DexMenuItem("Close comparation");
		//menuCompareClose.setIcon(new ImageIcon("imgs/menu/search.png"));
		menuCompareClose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
				compare();
			}
		});
		menuNavigateSearch = new DexMenuItem("Search");
		menuNavigateSearch.setAccelerator(KeyStroke.getKeyStroke('F', KeyEvent.CTRL_DOWN_MASK));
		menuNavigateSearch.setIcon(new ImageIcon("imgs/menu/search.png"));
		menuNavigateSearch.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new DexSearch(((DexFrame) SwingUtilities.getWindowAncestor(DexMenu.this)));
			}
		});
		menuInfoHelp = new DexMenuItem("Help");
		menuNavigateSearch.setAccelerator(KeyStroke.getKeyStroke('H', KeyEvent.CTRL_DOWN_MASK));
		menuInfoHelp.setIcon(new ImageIcon("imgs/menu/help.png"));
		menuInfoHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
			}
		});
		menuInfoAbout = new DexMenuItem("About DexStudio");
		menuNavigateSearch.setAccelerator(KeyStroke.getKeyStroke('I', KeyEvent.CTRL_DOWN_MASK));
		menuInfoAbout.setIcon(new ImageIcon("imgs/menu/info.png"));
		menuInfoAbout.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "DexStudio v.1.0.0\n\nDeveloped by:\nAncona Eugenio\nZanoncello Matteo\n", "About DEXplorer", JOptionPane.PLAIN_MESSAGE, new ImageIcon("imgs/logo.png"));
			}
		});
		// Menu
		JMenu menuFile = new JMenu("File");
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileSave);
		menuFile.addSeparator();
		menuFile.add(menuFileClose);
		JMenu menuCompare = new JMenu("Compare");
		menuCompare.add(menuCompareOpen);
		menuCompare.add(menuCompareClose);
		JMenu menuNavigate = new JMenu("Navigate");
		menuNavigate.add(menuNavigateSearch);
		JMenu menuInfo = new JMenu("?");
		menuInfo.add(menuInfoHelp);
		menuInfo.add(menuInfoAbout);
		this.add(menuFile);
		this.add(menuCompare);
		this.add(menuNavigate);
		this.add(menuInfo);
		updateItems();
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
	public boolean isCompare()
	{
		return compare;
	}
	
	public void open()
	{
		open = !open;
		updateItems();
	}
	
	public void compare()
	{
		compare = !compare;
		updateItems();
	}
	
	private void updateItems()
	{
		menuFileSave.setEnabled(open);
		menuCompareOpen.setEnabled(open && !compare);
		menuCompareClose.setEnabled(open && compare);
	}
}
