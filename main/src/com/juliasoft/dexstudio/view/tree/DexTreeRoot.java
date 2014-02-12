package com.juliasoft.dexstudio.view.tree;

/**
 * Root node
 * 
 * @author Zanoncello Matteo
 */
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
