package com.juliasoft.dexstudio.cell;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.dexstudio.utils.AnnotationSet;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexAnnotationCell extends JTextPane
{
	private final String fontSize = getFont().getSize() + "pt";
	private final String color = "rgb(" + getForeground().getRed() + ", " + getForeground().getGreen() + ", " + getForeground().getBlue() + ")";
	private final String fontName = getFont().getName();
	private final String htmlFormat = "<html><head><style type=\"text/css\">body{font-family:" + fontName + "; font-weight:normal; font-size:" + fontSize + "pt;color:" + color + ";}</style></head><body>";
	private final String closeFormat = "</body></html>";
	private Annotation[] annots;
	
	public DexAnnotationCell(AnnotationSet annotations)
	{
		super();
		this.setContentType("text/html");
		if(annotations != null)
		{
			annots = new Annotation[annotations.size()];
			int i = 0;
			for(Annotation ann : annotations)
				annots[i++] = ann;
			String text = new String();
			i = 0;
			boolean separator = false;
			for(Annotation ann : annotations)
			{
				if(separator)
					text += ", ";
				else
					separator = true;
				text += ("<a href='ann" + i + "'>" + "@" + Library.printType(ann.getType()) + "</a>");
				i++;
			}
			this.setText(htmlFormat + text + closeFormat);
		}
	}
	
	public Annotation[] getAnnotations()
	{
		return annots;
	}
}
