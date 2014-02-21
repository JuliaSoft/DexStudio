package com.juliasoft.dexstudio.tab.header;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.Collection;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.Const;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.ParamGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexMethodHeader extends JTextPane
{
	private final String color = "rgb(" + getForeground().getRed() + ", " + getForeground().getGreen() + ", " + getForeground().getBlue() + ")";
	private final String fontName = getFont().getName();
	private final String clazzFontSize = 14 + "px";
	private final String descriptionFontSize = 14 + "pt";
	private final String classStyle = ".class{font-family:" + fontName + "; font-size:" + clazzFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String descriptionStyle = ".description{font-family:" + fontName + "; font-size:" + descriptionFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">" + classStyle + descriptionStyle + "</style></head><body>";
	private final String closeFormat = "</body></html>";
	private ClassGen ownerClass;
	private ClassGen[] params;
	private MethodGen meth;
	private ClassGen returnType;
	private Annotation[] annotations;
	private DexDisplay display;
	
	public DexMethodHeader(DexDisplay display, MethodGen meth)
	{
		this.display = display;
		this.meth = meth;
		this.setBackground(Color.WHITE);
		this.returnType = display.getDexGen().getClassGen(meth.getReturnType());
		TypeList parList = meth.getPrototype().getParameters();
		if(parList != null)
		{
			params = new ClassGen[parList.size()];
			int i = 0;
			for(Type param : parList)
				params[i++] = display.getDexGen().getClassGen(param);
		}
		String longName = meth.getOwnerClass().getName().replace('/', '.');
		String pakName = longName.contains(".") ? longName.substring(1, longName.lastIndexOf('.')) : "[default pakage]";
		String shortName = Library.shortName(longName);
		this.ownerClass = display.getDexGen().getClassGen(meth.getOwnerClass());
		Collection<Annotation> anns = meth.getAnnotations();
		String annots;
		if(anns.isEmpty())
			annots = "";
		else
		{
			annotations = new Annotation[anns.size()];
			int i = 0;
			for(Annotation ann : anns)
				annotations[i++] = ann;
			annots = "<b>annotations:</b> ";
			boolean separator = false;
			for(i = 0; i < annotations.length; i++)
			{
				if(separator)
					annots += ", ";
				else
					separator = true;
				annots += "<a href='ann" + i + "'>" + "@" + Library.printType(annotations[i].getType()) + "</a>";
			}
		}
		String clazz = "<div class='description'>" + pakName + "." + ((ownerClass != null) ? "<a href=\"ownerclass\">" : "") + shortName + ((ownerClass != null) ? "</a>" : "") + "</div>";
		String description = "<div class='class'><b>" + AccessFlag.decodeToHuman(meth.getFlags(), false, Const.FLAG_USE_METHOD) + "</b> " + printLinkSignature() + "<br />" + annots + "</div>";
		this.setContentType("text/html");
		this.setText(htmlFormat + clazz + description + closeFormat);
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.TOP_ALIGNMENT);
		this.setEditable(false);
		this.setHighlighter(null);
		this.setMaximumSize(this.getPreferredSize());
		this.addHyperlinkListener(new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				int mask = e.getInputEvent().getModifiers();
				if(((mask & InputEvent.BUTTON1_MASK) == mask) && HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType()))
				{
					String code = e.getDescription();
					if(code.equals("ownerclass"))
						DexMethodHeader.this.display.changeSelectedTab(new DexTab(DexMethodHeader.this.display, ownerClass));
					else if(code.equals("returntype"))
						DexMethodHeader.this.display.changeSelectedTab(new DexTab(DexMethodHeader.this.display, returnType));
					else if(code.matches("par[0-9]+"))
					{
						int index = Integer.parseInt(code.substring(3));
						DexMethodHeader.this.display.changeSelectedTab(new DexTab(DexMethodHeader.this.display, params[index]));
					}
					else if(code.matches("ann[0-9]+"))
					{
						int index = Integer.parseInt(code.substring(3));
						DexMethodHeader.this.display.changeSelectedTab(new DexTab(DexMethodHeader.this.display, annotations[index]));
					}
				}
			}
		});
	}
	
	private String printLinkSignature()
	{
		String result = "";
		if(!meth.isConstructor())
			result += ((returnType != null) ? "<a href='returntype'>" : "") + Library.printType(meth.getPrototype().getReturnType()) + ((returnType != null) ? "</a>" : "") + " " + meth.getName() + "(";
		else
			result += Library.printType(meth.getOwnerClass()) + "(";
		TypeList parameters = meth.getPrototype().getParameters();
		if(parameters != null)
		{
			int i = 0;
			boolean separator = false;
			for(ParamGen param : meth.getParams())
			{
				if(separator)
					result += ", ";
				else
					separator = true;
				String paramName = ((param.getParamName() != null) ? " " + param.getParamName() : " arg" + i);
				result += ((params[i] != null) ? "<a href='par" + i + "'>" : "") + Library.printType(param.getType()) + ((params[i] != null) ? "</a>" : "") + paramName;
				i++;
			}
		}
		result += ")";
		return result;
	}
}
