package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.FieldGen;

/**
 * Field node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexFieldNode extends DexTreeNode<FieldGen>
{
	public DexFieldNode(FieldGen field)
	{
		super(field);
	}
	
	@Override
	protected String makeLabel(FieldGen type)
	{
		return type.getName();
	}
}
