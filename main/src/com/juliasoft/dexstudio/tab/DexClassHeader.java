package com.juliasoft.dexstudio.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexClassHeader extends JTextPane
{
	private final String color = "rgb("+getForeground().getRed() + ", "+getForeground().getGreen() + ", "+getForeground().getBlue() +")";
	private final String fontName = getFont().getName();
	private final String pakFontSize = 14 + "pt";
	private final String descriptionFontSize = 14 + "px";
	private final String pakStyle = ".pak{font-family:" + fontName + "; font-size:" + pakFontSize + "; color:"+ color +"; font-weight:normal;}";
	private final String descriptionStyle = ".description{font-family:" + fontName + "; font-size:" + descriptionFontSize + "; color:"+ color +"; font-weight:normal;}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">" + pakStyle + descriptionStyle + "</style></head><body>";
	private final String closeFormat = "</body></html>";
	
	private ClassGen superClass;
	private ClassGen[] interfaces;
	private Annotation[] annotations;
	
	public DexClassHeader(DexFrame frame, ClassGen clazz)
	{
		this.superClass = frame.getTree().getClassGen(clazz.getSuperclass());
		TypeList interfList = clazz.getInterfaces();
		Collection<Annotation> annList = clazz.getAnnotations();
		
		int i = 0;
		if(interfList != null){
			
			this.interfaces = new ClassGen[interfList.size()];
			
			for(Type interf : interfList)
				
				interfaces[i++] = frame.getTree().getClassGen(interf);
			
		} 
		
		i=0;
		if(!annList.isEmpty()){
			
			annotations = new Annotation[annList.size()];
			
			for(Annotation ann : annList)
				
				annotations[i++] = ann;
			
			
		}
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);
		this.setAlignmentX(LEFT_ALIGNMENT);
		
		String className = clazz.getType().getName();
		String pakName = className.contains("/")? className.substring(1, className.lastIndexOf('/')) : "[default pakage]";
		pakName = pakName.replace('/', '.');
		String pakage = "<div class='pak'>" + pakName + "</div>";
		
		String flags = "<b>" + Library.printClassFlags(clazz.getFlags()).replaceAll("[<,>]", "") + "</b>";
		String name = (AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags())? " <b>interface</b> " : " <b>class</b> ") + Library.shortName(className) + "<br />";
		String extendz = "<b>extends:</b> " + ((superClass != null)?"<a href='superclass'>": "") + Library.shortName(clazz.getSuperclass().getName().replace('/', '.')) + ((superClass != null)? "</a>" : "") + "<br />";
		String implementz = "<b>implements:</b> ";
		String annots = "<b>annotations:</b> ";
		
		i=0;
		boolean separator = false;
		if (interfList == null)
			
			implementz = "";
			
		else {
			
			
			for(Type interf : interfList){
				
				if(separator)
					implementz += ", ";
				else separator = true;
				
				implementz += ((interfaces[i] != null)?"<a href='int"+ i +"'>": "") + Library.shortName(interf.getName()) + ((interfaces[i] != null)?"</a>": "");
				i++;
			}
			
			implementz += "<br />";
			
		}
		
		separator = false;
		
		if (annList.isEmpty())
			
			annots = "";
			
		else {
			
			for(i=0; i< annotations.length; i++){
				
				if(separator)
					annots += ", ";
				else separator = true;
				
				annots += ("<a href='ann"+ i +"'>"  + "@" + Library.printType(annotations[i].getType())+ "</a>");
			}
			
		}
		
		String description = "<div class='description'>" + flags + name + extendz + implementz + annots + "</div>";
		
		
		this.setContentType("text/html");
		this.setText(htmlFormat + pakage + description + closeFormat);
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.TOP_ALIGNMENT);
		this.setEditable(false);
		this.setHighlighter(null);
		this.setMaximumSize(this.getPreferredSize());
		
		
		this.addHyperlinkListener(new HyperlinkListener(){

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				
				int mask = e.getInputEvent().getModifiers();
				
				if(((mask & InputEvent.BUTTON1_MASK) == mask) && HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())){
					
					String code = e.getDescription();
					
					if(code.equals("superclass"))
					
						((DexFrame) SwingUtilities.getWindowAncestor(DexClassHeader.this)).changeSelectedTab(superClass);

					else if(code.matches("int[0-9]+")){
						
						int index = Integer.parseInt(code.substring(3));
					
						((DexFrame) SwingUtilities.getWindowAncestor(DexClassHeader.this)).changeSelectedTab(interfaces[index]);
					
					}
					
					else if(code.matches("ann[0-9]+")){
						
						int index = Integer.parseInt(code.substring(3));
						
						((DexFrame) SwingUtilities.getWindowAncestor(DexClassHeader.this)).changeSelectedTab(annotations[index]);
						
					}
				}
				
			}
			
		});
		
	}
}
