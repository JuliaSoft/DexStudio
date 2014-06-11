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
	 * Indicates if the node has a next instruction
	 */
	private boolean hasNext;
	
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
		if(next != null || (branches != null && branches.size() > 0))
			this.hasNext = true;
		else
			this.hasNext = false;
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
		else if(node.hasNext)
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
		
		if(il.size() > 0)
		{
			Iterator<InstructionHandle> iterator = il.iterator();
			FlowNode actual = graph.get(graph.indexOf(new FlowNode(iterator.next())));
			
			while(actual != null)
			{
				FlowNode next = (iterator.hasNext()) ? graph.get(graph.indexOf(new FlowNode(iterator.next()))) : null;
				
				CodegenInstruction ins = actual.getInstruction().getInstruction();
				
				//Unisco l'istruzione successiva con quella attuale
				if(next!= null && !(ins instanceof SelectInstruction) && !(ins instanceof Goto))
				{
					actual.setNext(next);
				}
				
				//Capisco se l'istruzione attuale è una branch
				if(ins instanceof BranchInstruction && !(ins instanceof FillArrayData))
				{
					InstructionHandle ih = ((BranchInstruction)ins).getTarget();
					FlowNode target = graph.get(graph.indexOf(new FlowNode(ih)));
					actual.addBranch(target);
				}
				
				//TODO: Gestire i PseudoSelect
				
				actual = next;
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
			
			if(this.hasNext)
			{
				if(this.isBranchInstruction())
				{
					for(FlowNode branch : this.getBranches())
					{
						if(!branch.isVisited())
						{
							addResult(result, branch.cloneGraph().lastInstruction(target, register, last));
						}
					}
				}
				else
				{
					FlowNode next = this.getNext();
					if(next != null)
						addResult(result, next.lastInstruction(target, register, last));
				}
			}
		}
		return result;
	}
	
	private void addResult(ArrayList<InstructionHandle> base, ArrayList<InstructionHandle> add)
	{
		for(InstructionHandle ih : add)
		{
			if(!base.contains(ih))
				base.add(ih);
		}
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
	
	public boolean hasNext()
	{
		return this.hasNext;
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
		{
			this.next = next;
			this.hasNext = true;
		}
	}
	
	public void addBranch(FlowNode branch)
	{
		if(branch == null)
			return;
		this.hasNext = true;
		if(this.branches == null)
			branches = new ArrayList<FlowNode>();
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
		return this.cloneGraphSupport(new ArrayList<FlowNode>());
	}
	
	public FlowNode cloneGraphSupport(ArrayList<FlowNode> graph)
	{
		FlowNode clone = new FlowNode(this.getInstruction());
		if(graph.contains(clone))
			return graph.get(graph.indexOf(clone));
		graph.add(clone);
		clone.visited = false;
		if(this.isBranchInstruction())
		{
			for(FlowNode branch : this.getBranches())
			{
				clone.addBranch(branch.cloneGraphSupport(graph));
			}
		}
		else if(this.hasNext)
		{
			clone.setNext(this.next.cloneGraphSupport(graph));
		}
		return clone;
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
