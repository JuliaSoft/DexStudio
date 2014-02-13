package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

/**
 * Field node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeField extends DexTreeNode<FieldGen>
{
	
	public DexTreeField(FieldGen type)
	{
		super(type);
	}
	
	public DexTreeField(DiffNode<FieldGen> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(FieldGen type)
	{
		return type.getName();
	}

}
