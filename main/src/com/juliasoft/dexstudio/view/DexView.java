package com.juliasoft.dexstudio.view;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public abstract class DexView extends JScrollPane
{
	public abstract String getName();
	
	public abstract JPanel getTabTitle();
	
	protected final static MouseListener closeButtonMouseListener = new MouseAdapter()
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
