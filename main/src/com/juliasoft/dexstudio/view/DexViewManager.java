package com.juliasoft.dexstudio.view;

import java.awt.Color;

import javax.swing.JTabbedPane;

import com.juliasoft.dexstudio.exception.ViewNotFoundException;

@SuppressWarnings("serial")
public class DexViewManager extends JTabbedPane
{
	public DexViewManager()
	{
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.setBackground(Color.WHITE);
	}
	
	public void addView(DexView view)
	{
		this.addView(this.getTabCount(), view);
	}
	
	public void addView(int pos, final DexView view)
	{
		this.insertTab(view.getName(), null, view, null, pos);
		this.setTabComponentAt(pos, view.getTabTitle());
	}
	
	public void removeView(int pos) throws ViewNotFoundException
	{
		if(pos >= this.getTabCount())
			throw new ViewNotFoundException();
		this.removeTabAt(pos);
	}
	
	public void removeView(String viewName) throws ViewNotFoundException
	{
		int pos = this.indexOf(viewName);
		if(pos == -1)
			throw new ViewNotFoundException();
		this.removeView(pos);
	}
	
	public void changeView(int pos, DexView view) throws ViewNotFoundException
	{
		this.removeView(pos);
		this.addView(pos, view);
	}
	
	public void changeView(String viewName, DexView view) throws ViewNotFoundException
	{
		int pos = indexOf(viewName);
		if(pos == -1)
			throw new ViewNotFoundException();
		this.removeView(pos);
		this.addView(pos, view);
	}
	
	public DexView getView(int pos) throws ViewNotFoundException
	{
		if(pos >= this.getTabCount())
			throw new ViewNotFoundException();
		return (DexView) this.getTabComponentAt(pos);
	}
	
	public int indexOf(String viewName)
	{
		int i = 0;
		while(i < this.getTabCount())
		{
			if(this.getTitleAt(i).equals(viewName))
				return i;
			i++;
		}
		return -1;
	}
	
	public void clean()
	{
		for(int i=1; i<this.getTabCount(); i++)
		{
			this.remove(i);
		}
	}
}
