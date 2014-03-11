package com.juliasoft.dexstudio.view.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import com.juliasoft.amalia.dex.DexGenVisitor;
import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.DalvikCode;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.codegen.FieldGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.ParamGen;
import com.juliasoft.dexstudio.utils.StringSet;
import com.juliasoft.dexstudio.view.NodeType;

/**
 * Visitor of the DexGen file for the built of the tree
 * 
 * @author Zanoncello Matteo
 */
public class TreeVisitor implements DexGenVisitor
{
	private final Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
	private final Map<String, TreeNode> packages = new HashMap<String, TreeNode>();
	
	public TreeVisitor(TreeNode rootNode)
	{
		nodeStack.push(rootNode);
	}
	
	@Override
	public void visitEnter(DexGen dexGen)
	{}
	
	@Override
	public void visitEnter(ContextGen ctxGen)
	{
		TreeNode ctxNode = new TreeNode(NodeType.FOLDER, "Context");
		nodeStack.peek().add(ctxNode);
		ctxNode.add(new TreeNode(NodeType.STRINGS, new StringSet(ctxGen.getStrings())));
	}
	
	@Override
	public void visitLeave(ContextGen ctxGen)
	{}
	
	@Override
	public void visitEnterClasses()
	{
		TreeNode codegenNode = new TreeNode(NodeType.FOLDER, "Classes");
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
			TreeNode packageNode = new TreeNode(NodeType.PACKAGE, (actual != "") ? actual.replace('/', '.') : "[Default Package]");
			packages.put(actual, packageNode);
			nodeStack.peek().add(packageNode);
		}
		if(AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags()))
		{
			TreeNode classNode = new TreeNode(NodeType.INTERFACE, clazz);
			packages.get(actual).add(classNode);
			nodeStack.push(classNode);
		}
		else
		{
			TreeNode classNode = new TreeNode(NodeType.CLASS, clazz);
			packages.get(actual).add(classNode);
			nodeStack.push(classNode);
		}
	}
	
	@Override
	public void visitEnter(MethodGen meth)
	{
		TreeNode methNode = new TreeNode(NodeType.METHOD, meth);
		nodeStack.peek().add(methNode);
		nodeStack.push(methNode);
	}
	
	@Override
	public void visitEnter(FieldGen field)
	{
		TreeNode fieldNode = new TreeNode(NodeType.FIELD, field);
		nodeStack.peek().add(fieldNode);
		nodeStack.push(fieldNode);
	}
	
	@Override
	public void visitEnter(Annotation ann)
	{
		TreeNode annNode = new TreeNode(NodeType.ANNOTATION, ann);
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