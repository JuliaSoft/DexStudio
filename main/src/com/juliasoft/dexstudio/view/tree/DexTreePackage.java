package com.juliasoft.dexstudio.view.tree;

/**
 * Package node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreePackage extends DexTreeNode
{
	public DexTreePackage(String packageName)
	{
		this.setUserObject(packageName);
	}
	
	@Override
	public String toString()
	{
		return ((String) this.getUserObject()).replace("/", ".");
	}
}
