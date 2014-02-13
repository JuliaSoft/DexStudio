package com.juliasoft.dexstudio.view.tree.node;

import javax.swing.tree.DefaultMutableTreeNode;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;

/**
 * Tree node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public abstract class DexTreeNode<T> extends DefaultMutableTreeNode implements Comparable<DexTreeNode<T>>
{
	private String label;
	
	public DexTreeNode(T type)
	{
		label = makeLabel(type);
		this.setUserObject(type);
	}
	
	public DexTreeNode(DiffNode<T> type)
	{
		if(type.getState().equals(DiffState.SAME) || type.getState().equals(DiffState.DIFFERENT) || type.getState().equals(DiffState.LEFT_ONLY))
		{
			label = makeLabel(type.getLeft());
		}
		else if(type.getState().equals(DiffState.RIGHT_ONLY))
		{
			label = makeLabel(type.getRight());
		}
		this.setUserObject(type);
	}
	
	@Override
	public int compareTo(DexTreeNode<T> node)
	{
		int cmp = this.toString().toLowerCase().compareTo(node.toString().toLowerCase());
		if(cmp == 0)
			return 1;
		return cmp;
	}
	
	protected abstract String makeLabel(T type);
	
	@Override
	public String toString()
	{
		return this.label;
	}
}
