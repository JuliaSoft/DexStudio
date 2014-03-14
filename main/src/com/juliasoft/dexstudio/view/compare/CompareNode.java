package com.juliasoft.dexstudio.view.compare;

import javax.swing.tree.DefaultMutableTreeNode;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.utils.Library;
import com.juliasoft.dexstudio.view.NodeType;

@SuppressWarnings("serial")
public class CompareNode extends DefaultMutableTreeNode implements Comparable<CompareNode>
{
	private String label;
	private NodeType type;
	private DiffNode<?> diff;
	private DiffState state;
	
	public CompareNode(NodeType type, String name) throws IllegalArgumentException
	{
		if(!type.equals(NodeType.ROOT) && !type.equals(NodeType.FOLDER) && !type.equals(NodeType.PACKAGE))
			throw new IllegalArgumentException();
		this.type = type;
		this.label = name;
		if(type.equals(NodeType.PACKAGE))
			this.state = DiffState.SAME;
	}
	
	public CompareNode(NodeType type, DiffNode<?> diff) throws IllegalArgumentException
	{
		if((type.equals(NodeType.CLASS) || type.equals(NodeType.INTERFACE)) && !diff.getDiffClass().equals(ClassGen.class) ||
				type.equals(NodeType.FIELD) && !diff.getDiffClass().equals(FieldGen.class) ||
				type.equals(NodeType.METHOD) && !diff.getDiffClass().equals(MethodGen.class) ||
				type.equals(NodeType.ANNOTATION) && !diff.getDiffClass().equals(Annotation.class) ||
				type.equals(NodeType.STRINGS) && !diff.getDiffClass().equals(ContextGen.class) ||
				type.equals(NodeType.ROOT) ||
				type.equals(NodeType.FOLDER) ||
				type.equals(NodeType.PACKAGE))
			throw new IllegalArgumentException();
		this.type = type;
		this.diff = diff;
		this.state = diff.getState();
		
		//Create the label
		Object obj = (diff.getState().equals(DiffState.RIGHT_ONLY)) ? diff.getRight() : diff.getLeft();
		if(obj instanceof ClassGen)
		{
			String className = ((ClassGen)obj).getType().getName().replace("/", ".");
			this.label = Library.shortName(className);
		}
		else if(obj instanceof FieldGen)
		{
			this.label = ((FieldGen)obj).getName();
		}
		else if(obj instanceof MethodGen)
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
		else if(obj instanceof Annotation)
		{
			this.label = "@" + Library.printType(((Annotation)obj).getType());
		}
		else if(obj instanceof ContextGen)
		{
			this.label = "Strings";
		}
	}
	
	//Used only for package nodes
	public DiffState updateState(DiffState child)
	{
		if(child.equals(DiffState.UNKNOWN))
		{
			return DiffState.UNKNOWN;
		}
		switch(this.state)
		{
			case SAME:
				if(!child.equals(DiffState.SAME))
					return child;
				return DiffState.SAME;
			case DIFFERENT:
				return DiffState.DIFFERENT;
			case LEFT_ONLY:
				if(child.equals(DiffState.RIGHT_ONLY) || child.equals(DiffState.SAME) || child.equals(DiffState.DIFFERENT))
					return DiffState.DIFFERENT;
				return DiffState.LEFT_ONLY;
			case RIGHT_ONLY:
				if(child.equals(DiffState.LEFT_ONLY) || child.equals(DiffState.SAME) || child.equals(DiffState.DIFFERENT))
					return DiffState.DIFFERENT;
				return DiffState.RIGHT_ONLY;
			default:
				return DiffState.UNKNOWN;	
		}
	}
	
	@Override
	public int compareTo(CompareNode node)
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
	
	public DiffNode<?> getDiff()
	{
		return this.diff;
	}
	
	public void setDiff(DiffNode<?> diff)
	{
		this.diff = diff;
	}
	
	public NodeType getType()
	{
		return this.type;
	}
	
	public void setState(DiffState state)
	{
		this.state = state;
	}
	
	public DiffState getState()
	{
		return this.state;
	}
	
	@Override
	public Object getUserObject()
	{
		return this.getDiff();
	}
	
	@Override
	public String toString()
	{
		return this.label;
	}
}
