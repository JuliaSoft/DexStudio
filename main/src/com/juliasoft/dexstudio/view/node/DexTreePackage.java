package com.juliasoft.dexstudio.view.node;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

/**
 * Package node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreePackage extends DexTreeNode<String>
{

	public DexTreePackage(String type)
	{
		super(type);
	}
	
	public DexTreePackage(DiffNode<String> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(String type)
	{
		return type.replace("/", ".");
	}

}