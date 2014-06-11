package com.juliasoft.dexstudio.tab.table.render;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;

public class AnalysisHyperlinkListener implements HyperlinkListener {

	private InstructionHandle ih;
	private InstructionRenderer rend;
	
	public AnalysisHyperlinkListener(InstructionRenderer rend, InstructionHandle ih){
		
		this.rend = rend;
		this.ih = ih;
		
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType().equals(EventType.ACTIVATED)) {
			String code = e.getDescription();
			
			if (code.matches("v[0-9]+")) {
				int register = Integer.parseInt(code.substring(1));
				
				rend.setSelectedRegister(register);
				rend.setSelectedInstruction(ih);
				
				System.out.println("Selected v" + register + " on instruction " + ih.getInstruction().toHuman());
				
				
				/*FlowNode graph = new FlowNode(((ExtendedInstructionHandle)value).getMethodGen().getCode().getInstructionList());
				
				ArrayList<InstructionHandle> result = graph.lastInstruction(((ExtendedInstructionHandle)value).getInstructionHandle(), register, null); // cacolo le instruzioni 
				display.openNewTab(new DexTreeTab(display, (ExtendedInstructionHandle)value, register, result));
				*/
			} else
				throw new IllegalArgumentException("Wrong Event");
		}
	

	}
}
