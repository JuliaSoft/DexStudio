package com.juliasoft.dexstudio.cell;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.InstructionHandle;
import com.juliasoft.dexstudio.utils.InstructionState;

@SuppressWarnings("serial")
public class DexInstructionCell extends JTextPane {

	private final String fontSize = getFont().getSize() + "pt";
	private final String color = "rgb(" + getForeground().getRed() + ", "
			+ getForeground().getGreen() + ", " + getForeground().getBlue()
			+ ")";
	private final String fontName = getFont().getName();
	private final String htmlFormat = "<html><head><style type=\"text/css\">body{font-family:"
			+ fontName
			+ "; font-weight:normal; font-size:"
			+ fontSize
			+ "pt;color:" + color + ";}"
			+ ".select{color:red;}"
			+ ".result{background-color: green;}"
			+ "</style></head><body>";
	private final String closeFormat = "</body></html>";
	
	public DexInstructionCell(InstructionHandle ih, InstructionState state, int register){
		
		super();
		
		String text = ih.getInstruction().toHuman();
		
		text = text.replaceAll("(v[0-9]+)", "<a href='$1'>$1</a>");
		
		if(state.equals(InstructionState.SELECTED)){
		
			String registerToSelect = "<a href='v" + register + "'>v" + register + "</a>";
			String selectedRegister = "<a class='select' href='v" + register + "'>v" + register + "</a>";
			
			text= text.replaceFirst(registerToSelect, selectedRegister);
		}
		
		else if(state.equals(InstructionState.RESULT)){
			
			text = "<div class='result'>" +text +"</div>";
			
		}
		
		this.setContentType("text/html");
		this.setText(htmlFormat + text + closeFormat);
		
	}
	
}
