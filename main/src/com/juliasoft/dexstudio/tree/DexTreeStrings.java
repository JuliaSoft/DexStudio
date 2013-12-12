package com.juliasoft.dexstudio.tree;

import java.util.Set;

@SuppressWarnings("serial")
public class DexTreeStrings extends DexTreeNode
{
	
    public DexTreeStrings(Set<String> strings)
    {
    	this.setUserObject(strings);
    }
    
    @Override
    public String toString()
    {
        return "Strings";
    }
}
