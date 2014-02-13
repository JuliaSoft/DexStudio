package com.juliasoft.dexstudio.view.tree.node;

import java.util.Set;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

/**
 * Strings node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeStrings extends DexTreeNode<Set<String>>
{
	public DexTreeStrings(Set<String> type)
	{
		super(type);
	}
	
	public DexTreeStrings(DiffNode<Set<String>> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(Set<String> type)
	{
		return "Strings";
	}
	
}
