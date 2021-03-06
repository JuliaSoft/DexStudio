package com.juliasoft.dexstudio.tab.table.render;

import java.util.ArrayList;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.dexstudio.flow.FlowNode;

public class AnalysisHyperlinkListener implements HyperlinkListener {

	private InstructionHandle ih;
	private InstructionRenderer rend;
	private FlowNode AnalysisGraph;
	
	public AnalysisHyperlinkListener(InstructionRenderer rend, InstructionHandle ih, FlowNode graph){
		
		this.rend = rend;
		this.ih = ih;
		this.AnalysisGraph = graph;
		
	}
	
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType().equals(EventType.ACTIVATED)) {
			String code = e.getDescription();
			
			if (code.matches("v[0-9]+")) {
				int register = Integer.parseInt(code.substring(1));
				
				rend.setSelectedInstruction(ih);
				rend.setSelectedRegister(register);
				
				ArrayList<InstructionHandle> result = new ArrayList<InstructionHandle>(AnalysisGraph.lastInstruction(ih, register));
				
				rend.setLastInstructions(result);
				rend.showResults();
				
			} else
				throw new IllegalArgumentException("Wrong Event");
		}
	

	}
}
