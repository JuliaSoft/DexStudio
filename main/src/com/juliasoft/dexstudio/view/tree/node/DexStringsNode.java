package com.juliasoft.dexstudio.view.tree.node;

import java.util.Set;

/**
 * Strings node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexStringsNode extends DexTreeNode<Set<String>>
{
	public DexStringsNode(Set<String> strings)
	{
		super(strings);
	}
	
	@Override
	protected String makeLabel(Set<String> type)
	{
		return "Strings";
	}
}
