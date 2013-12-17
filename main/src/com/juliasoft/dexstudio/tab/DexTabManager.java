package com.juliasoft.dexstudio.tab;

import java.awt.Color;

import javax.swing.JTabbedPane;

/**
 * Tab manager of the project
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTabManager extends JTabbedPane
{
	/**
	 * Constructor
	 */
	public DexTabManager()
	{
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.setBackground(Color.WHITE);
	}
	
	/**
	 * Remove all the visualized tabs
	 */
	public void cleanTabs()
	{
		this.removeAll();
	}
	
	/**
	 * Add a new DexTab
	 * 
	 * @param tab
	 *            The DexTab instance
	 */
	public void addTab(DexTab tab)
	{
		super.addTab(tab.getTitle(), tab);
		this.setSelectedIndex(this.getTabCount() - 1);
		this.setTabComponentAt(this.getSelectedIndex(), new DexTabTitle(tab));
	}
	
	/**
	 * Change the content of the current tab
	 * 
	 * @param tab
	 *            The DexTab instance
	 */
	public void changeTab(DexTab tab)
	{
		this.setComponentAt(this.getSelectedIndex(), tab);
		this.setTitleAt(this.getSelectedIndex(), tab.getTitle());
		this.setTabComponentAt(this.getSelectedIndex(), new DexTabTitle(tab));
		this.update(getGraphics());
		this.repaint();
	}
}
