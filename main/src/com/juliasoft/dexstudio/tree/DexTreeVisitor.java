package com.juliasoft.dexstudio.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import com.juliasoft.amalia.dex.DexGenVisitor;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.DalvikCode;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.ParamGen;
import com.juliasoft.dexstudio.utils.StringSet;

/**
 * Visitor of the DexGen file for the built of the tree
 * 
 * @author Zanoncello Matteo
 */
public class DexTreeVisitor implements DexGenVisitor
{
	private final Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
	private final Map<String, DexTreePackage> packages = new HashMap<String, DexTreePackage>();
	
	public DexTreeVisitor(DexTreeRoot rootNode)
	{
		nodeStack.push(rootNode);
	}
	
	@Override
	public void visitEnter(DexGen dexGen)
	{}
	
	@Override
	public void visitEnter(ContextGen ctxGen)
	{
		DexTreeFolder ctxNode = new DexTreeFolder("Context");
		nodeStack.peek().add(ctxNode);
		ctxNode.add(new DexTreeStrings(new StringSet(ctxGen.getStrings())));
		// ctxNode.add(new DexTreeTypes(ctxGen.getTypes())); //Manca il metodo
		// getTypes()
	}
	
	@Override
	public void visitLeave(ContextGen ctxGen)
	{}
	
	@Override
	public void visitEnterClasses()
	{
		DexTreeFolder codegenNode = new DexTreeFolder("Classes");
		nodeStack.peek().add(codegenNode);
		nodeStack.push(codegenNode);
	}
	
	@Override
	public void visitEnter(ClassGen clazz)
	{
		// Check if I'm in a new Package
		String actual = getPackage(clazz.getType().getName());
		if(!packages.containsKey(actual))
		{
			DexTreePackage packageNode = new DexTreePackage((actual != "") ? actual : "[Default Package]");
			packages.put(actual, packageNode);
			nodeStack.peek().add(packageNode);
		}
		DexTreeClass classNode = new DexTreeClass(clazz);
		packages.get(actual).add(classNode);
		nodeStack.push(classNode);
	}
	
	@Override
	public void visitEnter(MethodGen meth)
	{
		DexTreeMethod methNode = new DexTreeMethod(meth);
		nodeStack.peek().add(methNode);
		nodeStack.push(methNode);
	}
	
	@Override
	public void visitEnter(FieldGen field)
	{
		DexTreeField fieldNode = new DexTreeField(field);
		nodeStack.peek().add(fieldNode);
		nodeStack.push(fieldNode);
	}
	
	@Override
	public void visitEnter(Annotation ann)
	{
		DexTreeAnnotation annNode = new DexTreeAnnotation(ann);
		nodeStack.peek().add(annNode);
		nodeStack.push(annNode);
	}
	
	@Override
	public void visitEnter(ParamGen param)
	{}
	
	@Override
	public void visitEnter(DalvikCode code)
	{}
	
	@Override
	public void visitLeave(ClassGen clazz)
	{
		nodeStack.pop();
	}
	
	@Override
	public void visitLeave(MethodGen meth)
	{
		nodeStack.pop();
	}
	
	@Override
	public void visitLeave(FieldGen field)
	{
		nodeStack.pop();
	}
	
	@Override
	public void visitLeave(Annotation field)
	{
		nodeStack.pop();
	}
	
	@Override
	public void visitLeave(ParamGen param)
	{}
	
	@Override
	public void visitLeave(DalvikCode code)
	{}
	
	@Override
	public void visitLeaveClasses()
	{
		nodeStack.pop();
	}
	
	@Override
	public void visitLeave(DexGen dexGen)
	{}
	
	private String getPackage(String className)
	{
		if(className.lastIndexOf('/') != -1)
			return className.substring(1, className.lastIndexOf('/'));
		else
			return "";
	}
}