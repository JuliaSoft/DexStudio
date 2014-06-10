package com.juliasoft.dexstudio.flow;

import java.util.ArrayList;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.amalia.dex.codegen.InstructionList;
import com.juliasoft.amalia.dex.codegen.istr.BranchInstruction;

public class FlowNode
{
	private InstructionHandle ih;
	private ArrayList<FlowNode> branches;
	private boolean branch;
	private boolean visited;
	
	public FlowNode(InstructionHandle ih)
	{
		this(ih, new ArrayList<FlowNode>());
	}
	
	public FlowNode(InstructionHandle ih, ArrayList<FlowNode> branches)
	{
		this.ih = ih;
		this.branches = branches;
		this.visited = false;
		this.branch = false;
	}
	
	public FlowNode(FlowNode node)
	{
		this.ih = node.getInstructionHandle();
		for(FlowNode branch : node.getBranches())
		{
			this.addBranch(new FlowNode(branch));
		}
		this.visited = false;
		this.branch = node.isBranch();
	}

	//Costruttore che crea l'intero grafo a partire da una IstructionList
	public FlowNode(InstructionList il)
	{
		ArrayList<FlowNode> graph = new ArrayList<FlowNode>();
		FlowNode actual = this;
		
		for(InstructionHandle ih : il)
		{
			//Controllo se non esiste già il nodo dell'istruzione
			if(!graph.contains(ih))
			{
				//Aggiungo il nodo dell'istruzione
				graph.add(new FlowNode(ih));
			}
			//Collego la precedente con quella appena letta
			actual.addBranch(graph.get(graph.indexOf(ih)));
			
			//L'attuale diventa quella appena letta
			actual = graph.get(graph.indexOf(ih));
			
			//Se l'istruzione è una branch instruction
			if(ih instanceof BranchInstruction)
			{
				//ESEMPIO DI FUNZIONAMENTO
				//Se il nodo dell'istruzione Target non esiste
				if(!graph.contains(((BranchInstruction) ih).getTarget()))
				{
					//Aggiungo l'istruzione
					graph.add(new FlowNode(ih));
					//Collego l'istruzione attuale a tutti i suoi target (AD ESEMPIO)
					actual.addBranch(graph.get(graph.indexOf(ih)));
				}
			}
		}
	}
	
	public InstructionHandle getInstructionHandle()
	{
		return ih;
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
	
	public boolean isBranch()
	{
		return this.branch;
	}
	
	public void Branch()
	{
		this.branch = true;
	}
	
	public int getBranchesCount()
	{
		return branches.size();
	}
	
	public FlowNode getBranch(int index)
	{
		return branches.get(index);
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

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof InstructionHandle)
		{
			return this.getInstructionHandle().equals((InstructionHandle)o);
		}
		return false;
	}
}
