package com.juliasoft.dexstudio.view.cmp.node;

import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexMethodCmp extends DexCmpNode<MethodGen>
{	
	public DexMethodCmp(DiffNode<MethodGen> diff)
	{
		super(diff);
	}

	@Override
	protected String makeCmpLabel(MethodGen cmp)
	{
		String result = (cmp.isConstructor() ? Library.printType(cmp.getOwnerClass()) : cmp.getName()) + "(";
		TypeList params = cmp.getPrototype().getParameters();
		if(params != null)
		{
			for(Type typ : params)
			{
				result += Library.printType(typ) + ", ";
			}
			result = result.substring(0, result.length() - 2);
		}
		result += ")";
		if(!cmp.isConstructor())
		{
			result += " : " + Library.printType(cmp.getPrototype().getReturnType());
		}
		return result;
	}
}
