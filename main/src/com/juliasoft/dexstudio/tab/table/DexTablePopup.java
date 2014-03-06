package com.juliasoft.dexstudio.tab.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexTreeTab;
import com.juliasoft.dexstudio.utils.StringSet;

@SuppressWarnings("serial")
public class DexTablePopup extends JPopupMenu
{
	private Object obj;
	private DexDisplay display;
	DexTablePopupItem changeTab, newTab, expand;
	
	public DexTablePopup(DexDisplay display, Object obj)
	{
		this.display = display;
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
				if(obj instanceof ClassGen)
				{
					display.changeSelectedTab(new DexTreeTab(display, (ClassGen) obj));
				}
				else if(obj instanceof MethodGen)
				{
					display.changeSelectedTab(new DexTreeTab(display, (MethodGen) obj));
				}
				else if(obj instanceof Annotation)
				{
					display.changeSelectedTab(new DexTreeTab(display, (Annotation) obj));
				}
				else if(obj instanceof StringSet)
				{
					display.changeSelectedTab(new DexTreeTab(display, (StringSet) obj));
				}
			}
		});
		newTab = new DexTablePopupItem("Open in a new tab");
		newTab.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(obj instanceof ClassGen)
				{
					display.openNewTab(new DexTreeTab(display, (ClassGen) obj));
				}
				else if(obj instanceof MethodGen)
				{
					display.openNewTab(new DexTreeTab(display, (MethodGen) obj));
				}
				else if(obj instanceof Annotation)
				{
					display.openNewTab(new DexTreeTab(display, (Annotation) obj));
				}
				else if(obj instanceof StringSet)
				{
					display.openNewTab(new DexTreeTab(display, (StringSet) obj));
				}
			}
		});
		if(obj instanceof ClassGen || obj instanceof MethodGen)
		{
			this.add(changeTab);
			this.add(newTab);
		}
	}
}