package com.juliasoft.dexstudio.flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.amalia.dex.codegen.InstructionList;
import com.juliasoft.amalia.dex.codegen.istr.APut;
import com.juliasoft.amalia.dex.codegen.istr.APutBoolean;
import com.juliasoft.amalia.dex.codegen.istr.APutByte;
import com.juliasoft.amalia.dex.codegen.istr.APutChar;
import com.juliasoft.amalia.dex.codegen.istr.APutObject;
import com.juliasoft.amalia.dex.codegen.istr.APutShort;
import com.juliasoft.amalia.dex.codegen.istr.APutWide;
import com.juliasoft.amalia.dex.codegen.istr.AbstractArrayOp;
import com.juliasoft.amalia.dex.codegen.istr.AbstractBinOp;
import com.juliasoft.amalia.dex.codegen.istr.AbstractBinOp2AddrInstruction;
import com.juliasoft.amalia.dex.codegen.istr.AbstractBinOpLit16Instruction;
import com.juliasoft.amalia.dex.codegen.istr.AbstractBinOpLit8Instruction;
import com.juliasoft.amalia.dex.codegen.istr.AbstractCmpInstruction;
import com.juliasoft.amalia.dex.codegen.istr.AbstractConversion;
import com.juliasoft.amalia.dex.codegen.istr.ArrayLength;
import com.juliasoft.amalia.dex.codegen.istr.BranchInstruction;
import com.juliasoft.amalia.dex.codegen.istr.CodegenInstruction;
import com.juliasoft.amalia.dex.codegen.istr.ConstInstruction;
import com.juliasoft.amalia.dex.codegen.istr.FillArrayData;
import com.juliasoft.amalia.dex.codegen.istr.IPut;
import com.juliasoft.amalia.dex.codegen.istr.IPutBoolean;
import com.juliasoft.amalia.dex.codegen.istr.IPutByte;
import com.juliasoft.amalia.dex.codegen.istr.IPutChar;
import com.juliasoft.amalia.dex.codegen.istr.IPutObject;
import com.juliasoft.amalia.dex.codegen.istr.IPutShort;
import com.juliasoft.amalia.dex.codegen.istr.IPutWide;
import com.juliasoft.amalia.dex.codegen.istr.InstanceOf;
import com.juliasoft.amalia.dex.codegen.istr.InstanceOp;
import com.juliasoft.amalia.dex.codegen.istr.MoveInstruction;
import com.juliasoft.amalia.dex.codegen.istr.MoveResultWide;
import com.juliasoft.amalia.dex.codegen.istr.NewArray;
import com.juliasoft.amalia.dex.codegen.istr.NewInstance;
import com.juliasoft.amalia.dex.codegen.istr.PseudoSelect;
import com.juliasoft.amalia.dex.codegen.istr.ReturnInstruction;
import com.juliasoft.amalia.dex.codegen.istr.SPut;
import com.juliasoft.amalia.dex.codegen.istr.SPutBoolean;
import com.juliasoft.amalia.dex.codegen.istr.SPutByte;
import com.juliasoft.amalia.dex.codegen.istr.SPutChar;
import com.juliasoft.amalia.dex.codegen.istr.SPutObject;
import com.juliasoft.amalia.dex.codegen.istr.SPutShort;
import com.juliasoft.amalia.dex.codegen.istr.SPutWide;
import com.juliasoft.amalia.dex.codegen.istr.SelectInstruction;
import com.juliasoft.amalia.dex.codegen.istr.StaticOp;
import com.juliasoft.amalia.dex.codegen.istr.late.Goto;

public class FlowNode
{
	/**
	 * Instruction into the node
	 */
	private InstructionHandle instruction;
	
	/**
	 * Array of following nodes/instructions
	 */
	private List<FlowNode> branches;
	
	/**
	 * Indicates if the node has a next instruction (one or more)
	 */
	private boolean hasNext;
	
	/**
	 * Indicates if the node is already visited
	 */
	private boolean visited;
	
	/**
	 * Simple constructor
	 * @param ih the instruction to put into the node
	 */
	private FlowNode(InstructionHandle ih)
	{
		this(ih, new ArrayList<FlowNode>());
	}
	
	/**
	 * Full-parameters constructor
	 */
	private FlowNode(InstructionHandle ih, List<FlowNode> branches)
	{
		this.visited = false;
		this.instruction = ih;
		this.branches = branches;
		this.hasNext = (branches != null && branches.size() > 0) ? true : false;
	}
	
	/**
	 * Copy constructor
	 * @param node
	 */
	public FlowNode(FlowNode node)
	{
		this.visited = false;
		this.instruction = node.getInstruction();
		for(FlowNode branch : node.getBranches())
		{
			this.addBranch(new FlowNode(branch));
		}
	}
	
	/**
	 * Main Constructor
	 * @param il
	 */
	public FlowNode(InstructionList il)
	{
		if(il.size() == 0)
			throw new IllegalArgumentException("Lista di istruzioni vuota");
		
		//Creo tutti i nodi del grafo senza collegamenti
		ArrayList<FlowNode> graph = new ArrayList<FlowNode>();
		boolean start = true;
		for(InstructionHandle ih : il)
		{
			if(start)
			{
				this.instruction = ih;
				graph.add(this);
				start = false;
			}
			else
				graph.add(new FlowNode(ih));
		}
		
		//Scorro le istruzioni
		Iterator<InstructionHandle> iterator = il.iterator();
		//Ottengo il nodo dell'istruzione attuale..
		FlowNode actual = graph.get(graph.indexOf(new FlowNode(iterator.next())));
		
		while(actual != null)
		{
			//..e quello della successiva nell'elenco
			FlowNode next = (iterator.hasNext()) ? graph.get(graph.indexOf(new FlowNode(iterator.next()))) : null;
			
			CodegenInstruction ins = actual.getInstruction().getInstruction();
			
			//Unione dell'istruzione attuale con quella successiva
			if(next!= null
				&& !(ins instanceof SelectInstruction)
				&& !(ins instanceof Goto)
				&& !(ins instanceof ReturnInstruction))
			{
				actual.addBranch(next);
			}
			
			//Se l'istruzione attuale � una BranchInstruction
			if(ins instanceof BranchInstruction && !(ins instanceof FillArrayData))
			{
				InstructionHandle ih = ((BranchInstruction)ins).getTarget();
				FlowNode target = graph.get(graph.indexOf(new FlowNode(ih)));
				actual.addBranch(target);
			}
			
			if(ins instanceof PseudoSelect)
			{
				
				InstructionHandle[] ihs = ((PseudoSelect)ins).getTargets();
				for(InstructionHandle ih : ihs)
				{
					FlowNode target = graph.get(graph.indexOf(new FlowNode(ih)));
					actual.addBranch(target);
				}
			}
			
			//Il successivo diventa il nuovo attuale
			actual = next;
		}
	}
	
	/**
	 * Get the closest instructions that can modify a register before the instruction target
	 * @param target
	 * @param register
	 * @param last
	 * @return
	 */
	public List<InstructionHandle> lastInstruction(InstructionHandle target, int register)
	{
		this.unvisit();
		return this.lastInstruction(target, register, null);
	}
	
	private List<InstructionHandle> lastInstruction(InstructionHandle target, int register, InstructionHandle last)
	{
		List<InstructionHandle> result = new ArrayList<InstructionHandle>();
		
		if(this.equals(new FlowNode(target)))
		{
			if(last != null)
				result.add(last);
		}
		else
		{
			this.visit();
			
			if(this.isModifierFor(register))
			{
				last = this.getInstruction();
			}
			
			boolean isBranched = (this.getBranchesCount() > 1) ? true : false;
			
			if(this.hasNext)
			{
				for(FlowNode branch : this.getBranches())
				{
					if(!branch.isVisited())
					{
						result.addAll((isBranched ? branch.cloneGraph() : branch).lastInstruction(target, register, last));
					}
				}
			}
		}
		return result;
	}
	
	private boolean isModifierFor(int registry)
	{
		CodegenInstruction ins = this.getInstruction().getInstruction();
		if(ins instanceof MoveInstruction && ((MoveInstruction) ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof MoveResultWide && ((MoveResultWide) ins).getRegister() == registry)
		{
			return true;
		}
		else if(ins instanceof ConstInstruction && ((ConstInstruction) ins).getRegister() == registry)
		{
			return true;
		}
		else if(ins instanceof StaticOp)
		{
			StaticOp staticOp = (StaticOp) ins;
			if((staticOp instanceof SPut
			 || staticOp instanceof SPutBoolean
			 || staticOp instanceof SPutByte
			 || staticOp instanceof SPutChar
			 || staticOp instanceof SPutObject
			 || staticOp instanceof SPutShort
			 || staticOp instanceof SPutWide) && staticOp.getRegister() == registry)
				return true;
		}
		else if(ins instanceof InstanceOp)
		{
			InstanceOp instanceOp = (InstanceOp) ins;
			if((instanceOp instanceof IPut
			 || instanceOp instanceof IPutBoolean
			 || instanceOp instanceof IPutByte
			 || instanceOp instanceof IPutChar
			 || instanceOp instanceof IPutObject
			 || instanceOp instanceof IPutShort
			 || instanceOp instanceof IPutWide) && instanceOp.getValueReg() == registry)
				return true;
		}
		else if(ins instanceof AbstractArrayOp)
		{
			AbstractArrayOp arrayOp = (AbstractArrayOp) ins;
			if((arrayOp instanceof APut
			 || arrayOp instanceof APutBoolean
			 || arrayOp instanceof APutByte
			 || arrayOp instanceof APutChar
			 || arrayOp instanceof APutObject
			 || arrayOp instanceof APutShort
			 || arrayOp instanceof APutWide) && arrayOp.getRegisters()[0] == registry)
				return true;
		}
		else if(ins instanceof InstanceOf && ((InstanceOf) ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof ArrayLength && ((ArrayLength) ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof NewInstance && ((NewInstance)ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof NewArray && ((NewArray)ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractCmpInstruction && ((AbstractCmpInstruction) ins).getDstreg() == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractConversion && ((AbstractConversion) ins).getRegisters()[0] == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractBinOp && ((AbstractBinOp) ins).getRegisters()[0] == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractBinOp2AddrInstruction && ((AbstractBinOp2AddrInstruction) ins).getRegisters()[0] == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractBinOpLit16Instruction && ((AbstractBinOpLit16Instruction) ins).getRegisters()[0] == registry)
		{
			return true;
		}
		else if(ins instanceof AbstractBinOpLit8Instruction && ((AbstractBinOpLit8Instruction) ins).getRegisters()[0] == registry)
		{
			return true;
		}
		
		return false;
	}
	
	
	//Getters
	
	public InstructionHandle getInstruction()
	{
		return instruction;
	}
	
	public List<FlowNode> getBranches()
	{
		return branches;
	}
	
	public FlowNode getBranch(int index)
	{
		return branches.get(index);
	}
	
	public int getBranchesCount()
	{
		return branches.size();
	}
	
	public boolean isVisited()
	{
		return visited;
	}
	
	public boolean hasNext()
	{
		return this.hasNext;
	}
	
	
	//Setters
	
	public void visit()
	{
		this.visited = true;
	}
	
	public void addBranch(FlowNode branch)
	{
		if(branch == null)
			return;
		this.hasNext = true;
		if(this.branches == null)
			branches = new ArrayList<FlowNode>();
		branches.add(branch);
	}
	
	
	
	public FlowNode cloneGraph()
	{
		return this.cloneGraphSupport(new ArrayList<FlowNode>());
	}
	
	private FlowNode cloneGraphSupport(ArrayList<FlowNode> graph)
	{
		FlowNode clone = new FlowNode(this.getInstruction());
		if(graph.contains(clone))
			return graph.get(graph.indexOf(clone));
		graph.add(clone);
		clone.visited = false;
		for(FlowNode branch : this.getBranches())
		{
			clone.addBranch(branch.cloneGraphSupport(graph));
		}
		return clone;
	}
	
	public void unvisit()
	{
		this.visited = false;
		for(FlowNode branch : this.branches)
			branch.unvisit();
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof FlowNode)
		{
			return this.getInstruction().equals(((FlowNode)o).getInstruction());
		}
		return false;
	}
}
