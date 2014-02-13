package com.juliasoft.dexstudio.view.tree.node;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Class node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeClass extends DexTreeNode<ClassGen>
{

	public DexTreeClass(ClassGen type)
	{
		super(type);
	}
	
	public DexTreeClass(DiffNode<ClassGen> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(ClassGen type)
	{
		String className = type.getType().getName().replace("/", ".");
		return Library.shortName(className);
	}
}
