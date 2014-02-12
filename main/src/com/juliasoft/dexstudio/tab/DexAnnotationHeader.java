package com.juliasoft.dexstudio.tab;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexAnnotationHeader extends JTextPane
{
	private final String color = "rgb(" + getForeground().getRed() + ", " + getForeground().getGreen() + ", " + getForeground().getBlue() + ")";
	private final String fontName = getFont().getName();
	private final String descriptionFontSize = "14px";
	private final String pakFontSize = "14pt";
	private final String descriptionStyle = ".description{font-family:" + fontName + "; font-size:" + descriptionFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String pakStyle = ".pak{font-family:" + fontName + "; font-size:" + pakFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">" + descriptionStyle + pakStyle + "</style></head><body>";
	private final String closeFormat = "</body></html>";
	
	public DexAnnotationHeader(Annotation ann)
	{
		this.setBackground(Color.WHITE);
		String annName = ann.getType().getName();
		String pakage = "<div class='pak'>" + annName.substring(1, annName.lastIndexOf('/')).replace('/', '.') + "</div>";
		String description = "<div class='description'>" + "<b>Annotation Type</b> " + "@" + Library.printType(ann.getType()) + "</div>";
		String visibility = "<div class='description'><b>Visibility:</b> ";
		switch(ann.getVisibility())
		{
			case VISIBILITY_BUILD:
				visibility += "Build";
				break;
			case VISIBILITY_RUNTIME:
				visibility += "Runtime";
				break;
			case VISIBILITY_SYSTEM:
				visibility += "System";
				break;
			case VISIBILITY_UNSPECIFIED:
				visibility += "Unspecified";
				break;
		}
		visibility += "</div>";
		this.setContentType("text/html");
		this.setText(htmlFormat + pakage + description + visibility + closeFormat);
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.TOP_ALIGNMENT);
		this.setEditable(false);
		this.setHighlighter(null);
		this.setMaximumSize(this.getPreferredSize());
	}
}
