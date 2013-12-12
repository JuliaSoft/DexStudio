package com.juliasoft.dexstudio.table;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class DexTablePopupItem extends JMenuItem
{
	/**
	 * String constructor
	 * @param name	The name of the menu item
	 */
	public DexTablePopupItem(String name)
	{
		super(name);
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, 30));
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 30));
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	}
}
