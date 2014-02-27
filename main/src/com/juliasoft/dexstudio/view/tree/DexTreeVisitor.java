package com.juliasoft.dexstudio.view.tree;

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
import com.juliasoft.dexstudio.view.tree.node.DexAnnotationNode;
import com.juliasoft.dexstudio.view.tree.node.DexClassNode;
import com.juliasoft.dexstudio.view.tree.node.DexFieldNode;
import com.juliasoft.dexstudio.view.tree.node.DexFolderNode;
import com.juliasoft.dexstudio.view.tree.node.DexMethodNode;
import com.juliasoft.dexstudio.view.tree.node.DexPackageNode;
import com.juliasoft.dexstudio.view.tree.node.DexRootNode;
import com.juliasoft.dexstudio.view.tree.node.DexStringsNode;

/**
 * Visitor of the DexGen file for the built of the tree
 * 
 * @author Zanoncello Matteo
 */
public class DexTreeVisitor implements DexGenVisitor
{
	private final Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
	private final Map<String, DexPackageNode> packages = new HashMap<String, DexPackageNode>();
	
	public DexTreeVisitor(DexRootNode rootNode)
	{
		nodeStack.push(rootNode);
	}
	
	@Override
	public void visitEnter(DexGen dexGen)
	{}
	
	@Override
	public void visitEnter(ContextGen ctxGen)
	{
		DexFolderNode ctxNode = new DexFolderNode("Context");
		nodeStack.peek().add(ctxNode);
		ctxNode.add(new DexStringsNode(new StringSet(ctxGen.getStrings())));
	}
	
	@Override
	public void visitLeave(ContextGen ctxGen)
	{}
	
	@Override
	public void visitEnterClasses()
	{
		DexFolderNode codegenNode = new DexFolderNode("Classes");
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
			DexPackageNode packageNode = new DexPackageNode((actual != "") ? actual : "[Default Package]");
			packages.put(actual, packageNode);
			nodeStack.peek().add(packageNode);
		}
		DexClassNode classNode = new DexClassNode(clazz);
		packages.get(actual).add(classNode);
		nodeStack.push(classNode);
	}
	
	@Override
	public void visitEnter(MethodGen meth)
	{
		DexMethodNode methNode = new DexMethodNode(meth);
		nodeStack.peek().add(methNode);
		nodeStack.push(methNode);
	}
	
	@Override
	public void visitEnter(FieldGen field)
	{
		DexFieldNode fieldNode = new DexFieldNode(field);
		nodeStack.peek().add(fieldNode);
		nodeStack.push(fieldNode);
	}
	
	@Override
	public void visitEnter(Annotation ann)
	{
		DexAnnotationNode annNode = new DexAnnotationNode(ann);
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