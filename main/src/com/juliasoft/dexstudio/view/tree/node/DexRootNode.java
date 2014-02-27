package com.juliasoft.dexstudio.view.tree.node;

/**
 * Root node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexRootNode extends DexTreeNode<String>
{
	public DexRootNode(String root)
	{
		super(root);
	}
	
	@Override
	protected String makeLabel(String type)
	{
		return type;
	}
}
