package com.juliasoft.dexstudio.view.cmp.node;

import java.util.Set;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

@SuppressWarnings("serial")
public class DexStringsCmp extends DexCmpNode<Set<String>>
{
	public DexStringsCmp(DiffNode<Set<String>> diff)
	{
		super(diff);
	}
	
	@Override
	protected String makeCmpLabel(Set<String> obj)
	{
		return "String comparison";
	}
}
