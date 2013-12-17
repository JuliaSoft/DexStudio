package com.juliasoft.dexstudio.tab;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Close Button for DexTabTitle
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTabCloseButton extends JButton implements ActionListener
{
	private DexTab tab;
	
	/**
	 * Constructor
	 * 
	 * @param tab
	 *            The DexTab instance
	 */
	public DexTabCloseButton(DexTab tab)
	{
		this.tab = tab;
		// NOTE: not my code at all!
		int size = 17;
		setPreferredSize(new Dimension(size, size));
		setToolTipText("close this tab");
		setUI(new BasicButtonUI());
		setContentAreaFilled(false);
		setFocusable(false);
		setBorder(BorderFactory.createEtchedBorder());
		setBorderPainted(false);
		addMouseListener(buttonMouseListener);
		setRolloverEnabled(true);
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		DexTabManager manager = (DexTabManager) tab.getParent();
		int i = manager.indexOfTab(tab.getTitle());
		if(i != -1)
		{
			manager.removeTabAt(i);
		}
	}
	
	@Override
	public void updateUI()
	{}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		// NOTE: Not my code at all!
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		if(getModel().isPressed())
			g2.translate(1, 1);
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.BLACK);
		if(getModel().isRollover())
			g2.setColor(Color.MAGENTA);
		int delta = 6;
		g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
		g2.dispose();
	}
	
	/**
	 * MouseListener for overing effect on DexTabCloseButton
	 */
	private final static MouseListener buttonMouseListener = new MouseAdapter()
	{
		public void mouseEntered(MouseEvent e)
		{
			Component component = e.getComponent();
			if(component instanceof AbstractButton)
			{
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}
		
		public void mouseExited(MouseEvent e)
		{
			Component component = e.getComponent();
			if(component instanceof AbstractButton)
			{
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
