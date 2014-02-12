package com.juliasoft.dexstudio.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree node
 * 
 * @author Zanoncello Matteo
 */
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
