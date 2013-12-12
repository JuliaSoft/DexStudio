package com.juliasoft.dexstudio.tree;


@SuppressWarnings("serial")
public class DexTreeRoot extends DexTreeNode
{
	public DexTreeRoot(String appName)
	{
		this.setUserObject(appName);
	}
	
	public void setName(String name)
	{
		this.setUserObject(name);
	}
	
	@Override
	public String toString()
	{
		return (String) this.getUserObject();
	}
}
