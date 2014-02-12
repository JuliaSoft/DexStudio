package com.juliasoft.dexstudio;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.dexstudio.log.DexLog;
import com.juliasoft.dexstudio.menu.DexMenu;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.tab.DexTabManager;
import com.juliasoft.dexstudio.tree.DexTree;
import com.juliasoft.dexstudio.view.DexViewManager;

/**
 * Main frame of the project.
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexFrame extends JFrame implements DexDisplay
{
	private DexViewManager viewManager = new DexViewManager();
	private DexTree tree = new DexTree(this);
	private DexTabManager tabManager = new DexTabManager();
	private DexLog log = new DexLog();
	private DexMenu menu = new DexMenu(this);
	
	/**
	 * Constructor
	 */
	public DexFrame()
	{
		// Setting the layout
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("DexStudio");
		this.setIconImage(new ImageIcon("imgs/logo.png").getImage());
		this.setJMenuBar(menu);
		JSplitPane contentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabManager, log);
		contentSplitPane.setFocusable(false);
		contentSplitPane.setPreferredSize(contentSplitPane.getMaximumSize());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewManager, contentSplitPane);
		viewManager.addView(tree);
		splitPane.setFocusable(false);
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
		this.setVisible(true);
		this.revalidate();
		contentSplitPane.setDividerLocation(contentSplitPane.getSize().height - 150);
	}
	
	public void cleanLayout()
	{
		tree.cleanTree();
		tabManager.cleanTabs();
	}
	
	/**
	 * Update the layout of the frame
	 * 
	 * @param dexGen
	 *            DexGen file to open in the frame
	 * @param rootName
	 *            The name showed on the root of the navigation tree
	 */
	public void updateLayout(DexGen dexGen, String rootName)
	{
		this.cleanLayout();
		this.tree.updateLayout(dexGen, rootName);
	}
	
	@Override
	public void openNewTab(DexTab tab)
	{
		tabManager.addTab(tab);
	}
	
	@Override
	public void changeSelectedTab(DexTab tab)
	{
		if(tabManager.getSelectedIndex() == -1)
			tabManager.addTab(tab);
		else
			tabManager.changeTab(tab);
	}
	
	/**
	 * Add a string in the logger console
	 * 
	 * @param str
	 *            The adding string
	 */
	public void log(String str)
	{
		log.log(str);
	}
	
	/**
	 * Returns the navigation tree of the frame
	 * 
	 * @return the navigation tree
	 */
	@Override
	public DexTree getTree()
	{
		return this.tree;
	}
	
	public DexMenu getMenu()
	{
		return this.menu;
	}
}
