package com.juliasoft.dexstudio.cell;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.ParamGen;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexMethodCell extends JTextPane {

	private MethodGen meth;
	private ClassGen returnType;
	private ClassGen[] params;
	
	public DexMethodCell(MethodGen meth, DexFrame frame){
		
		super();
		
		this.meth = meth;
		
		returnType = frame.getTree().getClassGen(meth.getReturnType());
		
		params = new ClassGen[meth.getParams().size()];
		
		int i=0;
		for (ParamGen param : meth.getParams()){
			
			params[i] = frame.getTree().getClassGen(param.getType());
			i++;
			
		}
		

		
		String color = "rgb("+getForeground().getRed() + ", "+getForeground().getGreen() + ", "+getForeground().getBlue() +")";
		String fontName = "Courier";
		int fontSize = getFont().getSize();
		
		String text = "<html><head><style type=\"text/css\">body{font-family:"+ fontName + "; font-weight:normal; font-size:"+ fontSize +"pt;color:" + color + ";}</style></head><body>";
		
		if(meth.isConstructor())
			
			text += ((meth != null)?"<a href='meth'>" : "") + Library.printType(meth.getOwnerClass()) + ((meth != null)?"</a>" : "") + "(";
		
		else {
		
			text += ((returnType != null)?"<a href='returnclass'>" : "") + Library.printType(meth.getReturnType()) + ((returnType != null)?"</a>" : "") + " ";
			text += ((meth != null)?"<a href='meth'>" : "") + meth.getName() + ((meth != null)?"</a>" : "") + "(";
			
		}
		
		int j=0;
		boolean separator = false;
		for (ParamGen param : meth.getParams()){
			
			if (!separator)
				separator = true;
			
			else text += ", ";
			
			String paramName = ((param.getParamName()!= null)? " " + param.getParamName(): " arg" + j );
			
			text += ((params[j] != null)?"<a href='par" + j + "'>" : "") + Library.printType(param.getType()) + ((params[j] != null)?"</a>" : "") + paramName;
			j++;
			
		}
		
		text += ")</body></html>";
			
		this.setContentType("text/html");
		this.setText(text);
	}
	
	public ClassGen getReturnTypeClass(){
		
		return returnType;
		
	}
	
	public MethodGen getMethod(){
		
		return meth;
		
	}
	
	public ClassGen[] getParams(){
		
		return params;
		
	}
	
	
	public Object getLinkedObject(int pos){
		
		
		String text = this.getText().replaceAll("<[^>]*>|\n| {2,}", ""); // clean form html tags
			
		if(!meth.isConstructor() && pos < text.indexOf(' '))
			
			return returnType;
		
		else if(pos < text.indexOf('('))
			
			return meth;
		
		else if (pos < text.indexOf(')') && params.length >0 && text.charAt(pos) != ',' && text.charAt(pos) != ' '){
			
			String beforeClick = text.substring(text.indexOf('('), pos);
			
			int commaCount = 0;
			int spaceCount = 0;
			for(int i=0; i<beforeClick.length(); i++){
				
				if(beforeClick.charAt(i) == ',')
					
					commaCount++;
				
				else if(beforeClick.charAt(i) == ' ')
				
					spaceCount++;
			}
			
			if(spaceCount == commaCount * 2)
			
				return params[commaCount];
			
			
			
		}
		
		return null;
		
	}
	
}
