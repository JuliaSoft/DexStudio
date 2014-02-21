package com.juliasoft.dexstudio.view.node;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

/**
 * Root node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeRoot extends DexTreeNode<String>
{
	public DexTreeRoot(String type)
	{
		super(type);
	}
	
	public DexTreeRoot(DiffNode<String> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(String type)
	{
		return type;
	}
}
