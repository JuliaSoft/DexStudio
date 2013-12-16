package com.juliasoft.dexstudio.tree;

/**
 * Folder node
 * @author Zanoncello Matteo
 *
 */
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
