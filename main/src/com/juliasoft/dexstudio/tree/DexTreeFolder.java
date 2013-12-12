package com.juliasoft.dexstudio.tree;


@SuppressWarnings("serial")
public class DexTreeFolder extends DexTreeNode
{
	
    public DexTreeFolder(String folderName)
    {
        this.setUserObject(folderName);
    }
    
    @Override
    public String toString()
    {
        return (String) this.getUserObject();
    }
}
