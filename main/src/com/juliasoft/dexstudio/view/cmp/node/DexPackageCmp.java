package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.view.tree.node.DexTreeNode;

@SuppressWarnings("serial")
public class DexPackageCmp extends DexTreeNode<String>
{
	private DiffState state = DiffState.SAME;
	
	public DexPackageCmp(String obj)
	{
		super(obj);
	}

	public DiffState getState()
	{
		return state;
	}

	public void setState(DiffState state)
	{
		this.state = state;
	}

	@Override
	protected String makeLabel(String obj)
	{
		return obj;
	}
	
}
