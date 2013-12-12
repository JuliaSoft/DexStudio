package com.juliasoft.dexstudio.tree;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class DexTreeNode extends DefaultMutableTreeNode implements Comparable<DexTreeNode>
{
	@Override
	public int compareTo(DexTreeNode node)
	{
		int cmp = this.toString().toLowerCase().compareTo(node.toString().toLowerCase());
		if(cmp == 0)
			return 1;
		return cmp;
	}
}
