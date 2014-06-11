package com.juliasoft.dexstudio.flow;

import java.util.ArrayList;
import java.util.Iterator;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.amalia.dex.codegen.InstructionList;
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
import com.juliasoft.amalia.dex.codegen.istr.InstanceOf;
import com.juliasoft.amalia.dex.codegen.istr.MoveInstruction;
import com.juliasoft.amalia.dex.codegen.istr.MoveResultWide;
import com.juliasoft.amalia.dex.codegen.istr.NewArray;
import com.juliasoft.amalia.dex.codegen.istr.NewInstance;
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
	 * The following node/instruction
	 * (only if this is not a BranchInstruction)
	 */
	private FlowNode next;
	
	/**
	 * Array of following nodes/instructions
	 * (only if this is a BranchInstruction)
	 */
	private ArrayList<FlowNode> branches;
	
	/**
	 * Indicates if the node is a BranchInstruction
	 */
	private boolean branchInstruction;
	
	/**
	 * Indicates if the node is already visited
	 */
	private boolean visited;
	
	/**
	 * Simple constructor
	 * @param ih the instruction to put into the node
	 */
	public FlowNode(InstructionHandle ih)
	{
		this(ih, null, null, false);
	}
	
	/**
	 * 
	 * @param ih the instruction to put into the node
	 * @param next the next node/instruction
	 */
	public FlowNode(InstructionHandle ih, FlowNode next)
	{
		this(ih, next, null, false);
	}
	
	/**
	 * 
	 * @param ih the instruction to put into the node
	 * @param branches Array of branches (this make the node a BranchInstruction node)
	 */
	public FlowNode(InstructionHandle ih, ArrayList<FlowNode> branches)
	{
		this(ih, null, branches, true);
	}
	
	/**
	 * Full-parameters constructor
	 */
	private FlowNode(InstructionHandle ih, FlowNode next, ArrayList<FlowNode> branches, boolean branchInstruction)
	{
		this.visited = false;
		this.instruction = ih;
		this.next = next;
		this.branches = branches;
		this.branchInstruction = branchInstruction;
	}
	
	/**
	 * Copy constructor
	 * @param node
	 */
	public FlowNode(FlowNode node)
	{
		this.visited = false;
		this.instruction = node.getInstruction();
		if(node.isBranchInstruction())
		{
			for(FlowNode branch : node.getBranches())
			{
				this.addBranch(new FlowNode(branch));
			}
		}
		else
		{
			this.setNext(new FlowNode(node.getNext()));
		}
	}
	
	/**
	 * Main Constructor
	 * @param il
	 */
	public FlowNode(InstructionList il)
	{
		ArrayList<FlowNode> graph = new ArrayList<FlowNode>();
		
		for(InstructionHandle ih : il)
			graph.add(new FlowNode(ih));
		
		if(il.size() > 0)
		{
			Iterator<InstructionHandle> iterator = il.iterator();
			FlowNode actual = graph.get(graph.indexOf(iterator.next()));
			
			while(iterator.hasNext())
			{
				FlowNode prev = actual;
				actual = graph.get(graph.indexOf(iterator.next()));
				
				CodegenInstruction ins = prev.getInstruction().getInstruction();
				
				if(!(ins instanceof SelectInstruction) && !(ins instanceof Goto))
				{
					//Unisco l'istruzione attuale con la precedente
					prev.setNext(actual);
				}
				
				//Capisco se l'istruzione precedente è una branch
				if(ins instanceof BranchInstruction && !(ins instanceof FillArrayData))
				{
					InstructionHandle ih = ((BranchInstruction)ins).getTarget();
					FlowNode target = graph.get(graph.indexOf(ih));
					graph.get(graph.indexOf(ih)).addBranch(target);
				}
				
				//TODO: Gestire i PseudoSelect
				
			}
		}
	}
	
	/**
	 * Get the closest instructions that can modify a register before the instruction target
	 * @param target
	 * @param register
	 * @param last
	 * @return
	 */
	public ArrayList<InstructionHandle> lastInstruction(InstructionHandle target, int register, InstructionHandle last)
	{
		ArrayList<InstructionHandle> result = new ArrayList<InstructionHandle>();
		
		if(this.equals(target))
		{
			result.add(last);
		}
		else
		{
			this.visit();
			
			if(this.isModifierFor(register))
			{
				last = this.getInstruction();
			}
			
			if(this.isBranchInstruction())
			{
				for(FlowNode branch : this.getBranches())
				{
					if(!branch.isVisited())
						result.addAll(branch.cloneGraph().lastInstruction(target, register, last));
				}
			}
			else
			{
				FlowNode next = this.getNext();
				if(next != null)
					result.addAll(next.lastInstruction(target, register, last));
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
		else if(ins instanceof ConstInstruction && ((ConstInstruction) ins).getRegister() == registry)
		{
			return true;
		}
		else if(ins instanceof MoveResultWide && ((MoveResultWide) ins).getRegister() == registry)
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
		else if(ins instanceof InstanceOf && ((InstanceOf) ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof ArrayLength && ((ArrayLength) ins).getDstReg() == registry)
		{
			return true;
		}
		else if(ins instanceof NewInstance && true)	//TODO: getter di NewInstance
		{
			return true;
		}
		else if(ins instanceof NewArray && true)	//TODO: getter di NewArray
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
	
	public FlowNode getNext()
	{
		return this.next;
	}
	
	public InstructionHandle getInstruction()
	{
		return instruction;
	}
	
	public ArrayList<FlowNode> getBranches()
	{
		return branches;
	}
	
	public FlowNode getBranch(int index)
	{
		return branches.get(index);
	}
	
	public boolean isVisited()
	{
		return visited;
	}
	
	public boolean isBranchInstruction()
	{
		return this.branchInstruction;
	}
	
	//Setters
	
	public void visit()
	{
		this.visited = true;
	}
	
	public void branchInstruction()
	{
		this.branchInstruction = true;
	}
	
	public void setNext(FlowNode next)
	{
		if(!this.isBranchInstruction() && this.getBranches() == null)
			this.next = next;
	}
	
	public void addBranch(FlowNode branch)
	{
		if(this.getNext() != null)
		{
			FlowNode nextNode = this.getNext();
			this.next = null;
			branches.add(nextNode);
		}
		branches.add(branch);
		if(!this.isBranchInstruction())
			this.branchInstruction();
	}
	
	public FlowNode cloneGraph()
	{
		return new FlowNode(this);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof InstructionHandle)
		{
			return this.getInstruction().equals((InstructionHandle)o);
		}
		return false;
	}
}
