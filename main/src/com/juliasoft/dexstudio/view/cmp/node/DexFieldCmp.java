package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

@SuppressWarnings("serial")
public class DexFieldCmp extends DexCmpNode<FieldGen>
{
	public DexFieldCmp(DiffNode<FieldGen> diff)
	{
		super(diff);
	}
	
	@Override
	protected String makeCmpLabel(FieldGen obj)
	{
		return obj.getName();
	}
}
