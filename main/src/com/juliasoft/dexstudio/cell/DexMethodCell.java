package com.juliasoft.dexstudio.cell;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.ParamGen;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexMethodCell extends JTextPane
{
	private ClassGen returnType;
	private ClassGen[] params;
	
	public DexMethodCell(MethodGen meth)
	{
		super();
		returnType = meth.getDexGen().getClassGen(meth.getReturnType());
		params = new ClassGen[meth.getParams().size()];
		int i = 0;
		for(ParamGen param : meth.getParams())
		{
			params[i] = meth.getDexGen().getClassGen(param.getType());
			i++;
		}
		String color = "rgb(" + getForeground().getRed() + ", " + getForeground().getGreen() + ", " + getForeground().getBlue() + ")";
		String fontName = "Courier";
		int fontSize = getFont().getSize();
		String text = "<html><head><style type=\"text/css\">body{font-family:" + fontName + "; font-weight:normal; font-size:" + fontSize + "pt;color:" + color + ";}</style></head><body>";
		if(meth.isConstructor())
			text += ((meth != null) ? "<a href='meth'>" : "") + Library.printType(meth.getOwnerClass()) + ((meth != null) ? "</a>" : "") + "(";
		else
		{
			text += ((returnType != null) ? "<a href='returnclass'>" : "") + Library.printType(meth.getReturnType()) + ((returnType != null) ? "</a>" : "") + " ";
			text += ((meth != null) ? "<a href='meth'>" : "") + meth.getName() + ((meth != null) ? "</a>" : "") + "(";
		}
		int j = 0;
		boolean separator = false;
		for(ParamGen param : meth.getParams())
		{
			if(!separator)
				separator = true;
			else
				text += ", ";
			String paramName = ((param.getParamName() != null) ? " " + param.getParamName() : " arg" + j);
			text += ((params[j] != null) ? "<a href='par" + j + "'>" : "") + Library.printType(param.getType()) + ((params[j] != null) ? "</a>" : "") + paramName;
			j++;
		}
		text += ")</body></html>";
		this.setContentType("text/html");
		this.setText(text);
	}
	
	
}
