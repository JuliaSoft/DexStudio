package com.juliasoft.dexstudio.utils;

import java.awt.Cursor;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;

public class DexCursor
{
	public static void startWaitCursor(JComponent component)
	{
		RootPaneContainer root = (RootPaneContainer) component.getTopLevelAncestor();
		root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		root.getGlassPane().setVisible(true);
	}
	
	public static void stopWaitCursor(JComponent component)
	{
		RootPaneContainer root = (RootPaneContainer) component.getTopLevelAncestor();
		root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		root.getGlassPane().setVisible(false);
	}
}
