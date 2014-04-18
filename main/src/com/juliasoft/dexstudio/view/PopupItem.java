package com.juliasoft.dexstudio.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenuItem;

/**
 * Item for the popup menu of navigation tree
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class PopupItem extends JMenuItem {
	/**
	 * String constructor
	 * 
	 * @param name
	 *            The name of the menu item
	 */
	public PopupItem(String name) {
		super(name);
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, 30));
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 30));
		this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 12));
	}
}
