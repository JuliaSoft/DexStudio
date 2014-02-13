package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Annotation node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeAnnotation extends DexTreeNode<Annotation>
{

	public DexTreeAnnotation(Annotation type)
	{
		super(type);
	}
	
	public DexTreeAnnotation(DiffNode<Annotation> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(Annotation type)
	{
		return "@" + Library.printType(type.getType());
	}
}
