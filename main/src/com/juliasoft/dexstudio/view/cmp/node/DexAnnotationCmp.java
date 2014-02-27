package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexAnnotationCmp extends DexCmpNode<Annotation>
{
	public DexAnnotationCmp(DiffNode<Annotation> diff)
	{
		super(diff);
	}

	@Override
	protected String makeCmpLabel(Annotation obj)
	{
		return "@" + Library.printType(obj.getType());
	}
}
