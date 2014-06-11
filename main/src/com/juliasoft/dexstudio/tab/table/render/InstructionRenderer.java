package com.juliasoft.dexstudio.tab.table.render;

import java.util.ArrayList;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.cell.DexInstructionCell;
import com.juliasoft.dexstudio.utils.InstructionState;

@SuppressWarnings("serial")
public class InstructionRenderer extends AbstractDexEditorRenderer {

	private final DexDisplay display;
	InstructionHandle selectedInstruction;
	int register;
	ArrayList<InstructionHandle> lastInstructions;
	
	public InstructionRenderer(DexDisplay display) {
		
		this.display = display;
		
	}
	
	public void setSelectedInstruction(InstructionHandle ih){
		
		this.selectedInstruction = ih;
	}
	
	public void setSelectedRegister(int register){
		
		this.register = register;
	}
	
	public void setLastInstructions(ArrayList<InstructionHandle> lastInstructions){
		
		this.lastInstructions = lastInstructions;
	}
	
	@Override
	protected JTextPane getJTextPane(final Object value) {
		 
		if(!(value instanceof InstructionHandle))
			throw new IllegalArgumentException();
		
		InstructionHandle ih = (InstructionHandle)value;
		DexInstructionCell cell;
		
		if(selectedInstruction != null && selectedInstruction.equals(ih))
		
		 cell = new DexInstructionCell(ih, InstructionState.SELECTED, register); 
		
		else cell = new DexInstructionCell(ih, InstructionState.OFF, 0);
		
		cell.addHyperlinkListener(new AnalysisHyperlinkListener(this, ih));
		return cell;
		
	}

	@Override
	protected boolean supportsRendering(Object value) {
		return (value instanceof InstructionHandle);
	}
	
}
