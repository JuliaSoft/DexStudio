package com.juliasoft.dexstudio.tree;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Annotation node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeAnnotation extends DexTreeNode
{
	public DexTreeAnnotation(Annotation ann)
	{
		this.setUserObject(ann);
	}
	
	@Override
	public String toString()
	{
		Annotation ann = (Annotation) this.getUserObject();
		return "@" + Library.printType(ann.getType());
	}
}
