package com.juliasoft.dexstudio.view.node;

/**
 * Folder node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeFolder extends DexTreeNode<String>
{
	

	public DexTreeFolder(String type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(String type)
	{
		return type;
	}
}
