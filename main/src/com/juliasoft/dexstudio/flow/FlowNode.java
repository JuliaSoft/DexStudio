package com.juliasoft.dexstudio.flow;

import java.util.ArrayList;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.amalia.dex.codegen.InstructionList;
import com.juliasoft.amalia.dex.codegen.istr.BranchInstruction;

public class FlowNode
{
	private ArrayList<InstructionHandle> ihs;
	private ArrayList<FlowNode> branches;
	private boolean visited;
	
	public FlowNode()
	{
		this(new ArrayList<InstructionHandle>(), new ArrayList<FlowNode>());
	}
	
	public FlowNode(FlowNode node)
	{
		this.ihs = new ArrayList<InstructionHandle>(node.getInstructionHandles());
		for(FlowNode branch : node.getBranches())
		{
			this.addBranch(new FlowNode(branch));
		}
		this.visited = false;
	}
	
	public FlowNode(ArrayList<InstructionHandle> ihs, ArrayList<FlowNode> branches)
	{
		this.ihs = ihs;
		this.branches = branches;
		this.visited = false;
	}
	
	public FlowNode(InstructionList il)
	{
		for(InstructionHandle ih : il)
		{
			//Se l'istruzione è una branch instruction
			if(ih instanceof BranchInstruction)
			{
				//Controllo se esiste
			}
		}
	}
	
	public ArrayList<InstructionHandle> getInstructionHandles()
	{
		return ihs;
	}
	
	public ArrayList<FlowNode> getBranches()
	{
		return branches;
	}
	
	public boolean isVisited()
	{
		return visited;
	}
	
	public void visit()
	{
		this.visited = true;
	}
	
	public int getBranchesCount()
	{
		return branches.size();
	}
	
	public FlowNode getBranch(int index)
	{
		return branches.get(index);
	}
	
	public InstructionHandle getHead()
	{
		return ihs.get(0);
	}
	
	public void addBranch(FlowNode branch)
	{
		branches.add(branch);
	}
	
	public void removeBranch(int index)
	{
		branches.remove(index);
	}
	
	public FlowNode getSubGraph()
	{
		return new FlowNode(this);
	}
}
