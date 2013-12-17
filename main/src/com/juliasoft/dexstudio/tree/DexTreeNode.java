package com.juliasoft.dexstudio.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeNode extends DefaultMutableTreeNode implements Comparable<DexTreeNode>
{
	private DexTreeStatus status = DexTreeStatus.UNMODIFIED;
	
	@Override
	public int compareTo(DexTreeNode node)
	{
		int cmp = this.toString().toLowerCase().compareTo(node.toString().toLowerCase());
		if(cmp == 0)
			return 1;
		return cmp;
	}

	public DexTreeStatus getStatus()
	{
		return status;
	}

	public void setStatus(DexTreeStatus status)
	{
		this.status = status;
	}
}
