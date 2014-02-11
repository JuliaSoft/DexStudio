package com.juliasoft.dexstudio.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DexViewSample extends DexView
{
	public DexViewSample()
	{
		JPanel content = new JPanel();
		content.add(new JLabel("VIEW DI PROVA"));
		this.add(content);
	}
	
	@Override
	public String getName()
	{
		return "PROVA";
	}
}
