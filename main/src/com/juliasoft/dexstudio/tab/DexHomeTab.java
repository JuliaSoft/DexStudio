package com.juliasoft.dexstudio.tab;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DexHomeTab extends JPanel implements DexTab
{
	
	public DexHomeTab()
	{
		super(new BorderLayout());
		this.add(new JLabel("DexStudio"), BorderLayout.NORTH);
	}

	@Override
	public String getTitle()
	{
		return "Home";
	}

	@Override
	public Component getTab()
	{
		return this;
	}

	@Override
	public ImageIcon getIco()
	{
		return new ImageIcon("imgs/icon.png");
	}}
