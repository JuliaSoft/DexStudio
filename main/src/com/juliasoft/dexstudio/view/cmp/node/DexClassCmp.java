package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexClassCmp extends DexCmpNode<ClassGen>
{
	public DexClassCmp(DiffNode<ClassGen> diff)
	{
		super(diff);
	}
	
	@Override
	protected String makeCmpLabel(ClassGen cmp)
	{
		String className = cmp.getType().getName().replace("/", ".");
		return Library.shortName(className);
	}
}
