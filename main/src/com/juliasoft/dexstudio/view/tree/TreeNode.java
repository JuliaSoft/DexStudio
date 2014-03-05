package com.juliasoft.dexstudio.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.dexstudio.utils.Library;
import com.juliasoft.dexstudio.view.NodeType;

/**
 * Tree node
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class TreeNode extends DefaultMutableTreeNode implements Comparable<TreeNode>
{
	private String label;
	private NodeType type;
	
	public TreeNode(NodeType type, Object obj) throws IllegalArgumentException
	{
		if((type.equals(NodeType.ROOT) || type.equals(NodeType.FOLDER) || type.equals(NodeType.PACKAGE)) && !(obj instanceof String) ||
				(type.equals(NodeType.CLASS) || type.equals(NodeType.INTERFACE)) && !(obj instanceof ClassGen) ||
				type.equals(NodeType.FIELD) && !(obj instanceof FieldGen) ||
				type.equals(NodeType.METHOD) && !(obj instanceof MethodGen) ||
				type.equals(NodeType.ANNOTATION) && !(obj instanceof Annotation))
			throw new IllegalArgumentException();
		this.type = type;
		this.setUserObject(obj);
		
		//Create the label
		if(obj instanceof String)
		{
			this.label = (String) obj;
		}
		if(obj instanceof ClassGen)
		{
			String className = ((ClassGen)obj).getType().getName().replace("/", ".");
			this.label = Library.shortName(className);
		}
		if(obj instanceof FieldGen)
		{
			this.label = ((FieldGen)obj).getName();
		}
		if(obj instanceof MethodGen)
		{
			MethodGen meth = (MethodGen) obj;
			String result = (meth.isConstructor() ? Library.printType(meth.getOwnerClass()) : meth.getName()) + "(";
			TypeList params = meth.getPrototype().getParameters();
			if(params != null)
			{
				for(Type typ : params)
				{
					result += Library.printType(typ) + ", ";
				}
				result = result.substring(0, result.length() - 2);
			}
			result += ")";
			if(!meth.isConstructor())
			{
				result += " : " + Library.printType(meth.getPrototype().getReturnType());
			}
			this.label = result;
		}
		if(obj instanceof Annotation)
		{
			this.label = "@" + Library.printType(((Annotation)obj).getType());
		}
	}
	
	@Override
	public int compareTo(TreeNode node)
	{
		int cmp = this.toString().toLowerCase().compareTo(node.toString().toLowerCase());
		if(cmp == 0)
			return 1;
		return cmp;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	@Override
	public String toString()
	{
		return this.label;
	}

	public NodeType getType()
	{
		return type;
	}

	public void setType(NodeType type)
	{
		this.type = type;
	}
}
