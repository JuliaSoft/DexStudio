package com.juliasoft.dexstudio.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexFrame;

@SuppressWarnings("serial")
public class DexTablePopup extends JPopupMenu
{
	private Object obj;
	private DexFrame frame;
	
	DexTablePopupItem changeTab, newTab, expand;
	
	public DexTablePopup(DexFrame frame, Object obj)
	{
		this.frame = frame;
		this.obj = obj;
		initLayout();
	}
	
	private void initLayout()
	{
		changeTab = new DexTablePopupItem("Open");
		changeTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.changeSelectedTab(obj);
			}
		});
		
		newTab = new DexTablePopupItem("Open in a new tab");
		newTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frame.openNewTab(obj);
			}
		});
		
		if(obj instanceof ClassGen || obj instanceof MethodGen)
		{
			this.add(changeTab);
			this.add(newTab);
		}
	}
	
}