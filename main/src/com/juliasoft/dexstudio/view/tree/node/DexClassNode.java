package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Class node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexClassNode extends DexTreeNode<ClassGen>
{
	public DexClassNode(ClassGen clazz)
	{
		super(clazz);
	}
	
	@Override
	protected String makeLabel(ClassGen type)
	{
		String className = type.getType().getName().replace("/", ".");
		return Library.shortName(className);
	}
}
