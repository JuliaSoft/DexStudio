package com.juliasoft.dexstudio.view.tree.node;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public abstract class DexTreeNode<T> extends DefaultMutableTreeNode implements Comparable<DexTreeNode<T>>
{
	private String label;
	private T obj;
	
	public DexTreeNode(T obj)
	{
		this.obj = obj;
		this.label = makeLabel(obj);
	}
	
	@Override
	public int compareTo(DexTreeNode<T> node)
	{
		int cmp = this.toString().toLowerCase().compareTo(node.toString().toLowerCase());
		if(cmp == 0)
			return 1;
		return cmp;
	}
	
	protected abstract String makeLabel(T obj);
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public T getObj()
	{
		return obj;
	}
	
	public void setObj(T obj)
	{
		this.obj = obj;
	}
	
	// TODO (Maybe): Update every call of this method in the project with a call
	// of getLeft();
	@Override
	public Object getUserObject()
	{
		return this.getObj();
	}
	
	@Override
	public String toString()
	{
		return this.label;
	}
}
