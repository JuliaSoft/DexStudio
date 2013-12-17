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
	/**
	 * Constructor
	 */
	public DexMenu()
	{
		// Menu Items
		DexMenuItem menuFileOpen = new DexMenuItem("Open");
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		menuFileOpen.setIcon(new ImageIcon("imgs/menu/open.png"));
		menuFileOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				((DexFrame) SwingUtilities.getWindowAncestor(DexMenu.this)).openApk();
			}
		});
		DexMenuItem menuFileSave = new DexMenuItem("Save");
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
		DexMenuItem menuFileClose = new DexMenuItem("Close");
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
		DexMenuItem menuNavigateSearch = new DexMenuItem("Search");
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
		DexMenuItem menuInfoHelp = new DexMenuItem("Help");
		menuInfoHelp.setIcon(new ImageIcon("imgs/menu/help.png"));
		menuInfoHelp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
			}
		});
		DexMenuItem menuInfoAbout = new DexMenuItem("About DexStudio");
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
		JMenu menuNavigate = new JMenu("Navigate");
		menuNavigate.add(menuNavigateSearch);
		JMenu menuInfo = new JMenu("?");
		menuInfo.add(menuInfoHelp);
		menuInfo.add(menuInfoAbout);
		this.add(menuFile);
		this.add(menuNavigate);
		this.add(menuInfo);
	}
}
