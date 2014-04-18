package com.juliasoft.dexstudio.tab;

/**
 * Every tab in the main window implements this interface, allowing to access title, icon and the actual Component
 * 
 * 
 * @author Matteo Zanoncello
 *
 */
import java.awt.Component;

import javax.swing.ImageIcon;

public interface DexTab {
	public String getTitle();

	public Component getTab();

	public ImageIcon getIco();
}
