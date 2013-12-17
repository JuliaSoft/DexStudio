package com.juliasoft.dexstudio.tree;

import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.dexstudio.utils.Library;

/**
 * Method node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeMethod extends DexTreeNode
{
	public DexTreeMethod(MethodGen meth)
	{
		this.setUserObject(meth);
	}
	
	@Override
	public String toString()
	{
		MethodGen meth = (MethodGen) this.getUserObject();
		String result = (meth.isConstructor() ? Library.printType(meth.getOwnerClass()) : meth.getName()) + "(";
		TypeList params = meth.getPrototype().getParameters();
		if(params != null)
		{
			for(Type type : params)
			{
				result += Library.printType(type) + ", ";
			}
			result = result.substring(0, result.length() - 2);
		}
		result += ")";
		if(!meth.isConstructor())
		{
			result += " : " + Library.printType(meth.getPrototype().getReturnType());
		}
		return result;
	}
}
