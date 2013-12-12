package com.juliasoft.dexstudio.menu;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenuItem;

/**
 * Menu item of the frame
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexMenuItem extends JMenuItem
{
	/**
	 * Stirng constructor
	 * @param name	The name of the menu item
	 */
	public DexMenuItem(String name)
	{
		super(name);
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, 30));
		this.setPreferredSize(new Dimension(150, 30));
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	}
}
