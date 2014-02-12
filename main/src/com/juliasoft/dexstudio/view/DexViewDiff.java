package com.juliasoft.dexstudio.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

import com.juliasoft.amalia.dex.codegen.diff.DexDiff;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.exception.ViewNotFoundException;

@SuppressWarnings("serial")
public class DexViewDiff extends DexView
{
	private String abs;
	private DexFrame frame;
	
	public DexViewDiff(DexFrame frame, DexDiff diff, String abs)
	{
		this.abs = abs;
		this.frame = frame;
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel("ALBERO DI CONFRONTO"));
		this.add(panel);
	}
	
	@Override
	public String getName()
	{
		return "CMP " + this.abs.substring(abs.lastIndexOf('/') + 1, abs.lastIndexOf('.'));
	}
	
	@Override
	public JPanel getTabTitle()
	{
		JPanel res = new JPanel(new GridBagLayout());
		res.setOpaque(false);
		JLabel title = new JLabel(this.getName());
		title.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		JButton close = new JButton(new ImageIcon("imgs/tab/close.png"));
		close.setPreferredSize(new Dimension(17, 17));
		close.setToolTipText("close this tab");
		close.setUI(new BasicButtonUI());
		close.setContentAreaFilled(false);
		close.setFocusable(false);
		close.setBorder(BorderFactory.createEtchedBorder());
		close.setBorderPainted(false);
		close.addMouseListener(closeButtonMouseListener);
		close.setRolloverEnabled(true);
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.getMenu().removeCompare(abs);
				try
				{
					frame.getViewManager().removeView(DexViewDiff.this.getName());
				}
				catch(ViewNotFoundException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		res.add(title, gbc);
		res.add(close, gbc);
		return res;
	}
}
