package com.juliasoft.dexstudio.view.tree;

import com.juliasoft.dexstudio.utils.StringSet;

/**
 * Strings node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeStrings extends DexTreeNode
{
	public DexTreeStrings(StringSet strings)
	{
		this.setUserObject(strings);
	}
	
	@Override
	public String toString()
	{
		return "Strings";
	}
}
