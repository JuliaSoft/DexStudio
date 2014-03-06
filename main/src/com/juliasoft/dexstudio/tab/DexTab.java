package com.juliasoft.dexstudio.tab;

import java.awt.Component;

import javax.swing.ImageIcon;

public interface DexTab
{
	public String getTitle();
	
	public Component getTab();
	
	public ImageIcon getIco();
}
