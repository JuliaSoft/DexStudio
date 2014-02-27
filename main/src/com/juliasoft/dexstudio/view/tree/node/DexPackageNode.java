package com.juliasoft.dexstudio.view.tree.node;

/**
 * Package node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexPackageNode extends DexTreeNode<String>
{
	public DexPackageNode(String pkg)
	{
		super(pkg);
	}
	
	@Override
	protected String makeLabel(String type)
	{
		return type.replace("/", ".");
	}
}