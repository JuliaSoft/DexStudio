package com.juliasoft.dexstudio.tab;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel for the top of every tab, with a title and a close button
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexTabTitle extends JPanel
{
	/**
	 * Constructor
	 * @param tab	The DexTab instance
	 */
	public DexTabTitle(DexTab tab)
	{
		this.setSize(100, 50);
		this.setLayout(new FlowLayout());
		this.setOpaque(false);
		JLabel title = new JLabel(tab.getTitle());
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		DexTabCloseButton close = new DexTabCloseButton(tab);
		this.add(title);
		this.add(close);
	}
	
}
