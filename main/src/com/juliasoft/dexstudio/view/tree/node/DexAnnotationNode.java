package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Annotation node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexAnnotationNode extends DexTreeNode<Annotation>
{
	public DexAnnotationNode(Annotation ann)
	{
		super(ann);
	}
	
	@Override
	protected String makeLabel(Annotation type)
	{
		return "@" + Library.printType(type.getType());
	}
}
