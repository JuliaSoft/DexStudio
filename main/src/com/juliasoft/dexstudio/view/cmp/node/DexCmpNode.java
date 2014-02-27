package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.view.tree.node.DexTreeNode;

@SuppressWarnings("serial")
public abstract class DexCmpNode<T> extends DexTreeNode<DiffNode<T>>
{
	public DexCmpNode(DiffNode<T> obj)
	{
		super(obj);
	}

	private String label;
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	@Override
	protected String makeLabel(DiffNode<T> obj)
	{
		return makeCmpLabel((obj.getState().equals(DiffState.RIGHT_ONLY)) ? obj.getRight() : obj.getLeft());
	}
	
	protected abstract String makeCmpLabel(T obj);
}
