package com.juliasoft.dexstudio.view.tree.node;

/**
 * Folder node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexFolderNode extends DexTreeNode<String>
{
	public DexFolderNode(String folder)
	{
		super(folder);
	}
	
	@Override
	protected String makeLabel(String type)
	{
		return type;
	}
}
