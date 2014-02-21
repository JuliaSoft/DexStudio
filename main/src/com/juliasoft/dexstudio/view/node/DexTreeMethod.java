package com.juliasoft.dexstudio.view.node;

import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;


/**
 * Method node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeMethod extends DexTreeNode<MethodGen>
{

	public DexTreeMethod(MethodGen type)
	{
		super(type);
	}
	
	public DexTreeMethod(DiffNode<MethodGen> type)
	{
		super(type);
	}

	@Override
	protected String makeLabel(MethodGen type)
	{
		String result = (type.isConstructor() ? Library.printType(type.getOwnerClass()) : type.getName()) + "(";
		TypeList params = type.getPrototype().getParameters();
		if(params != null)
		{
			for(Type typ : params)
			{
				result += Library.printType(typ) + ", ";
			}
			result = result.substring(0, result.length() - 2);
		}
		result += ")";
		if(!type.isConstructor())
		{
			result += " : " + Library.printType(type.getPrototype().getReturnType());
		}
		return result;
	}
}
