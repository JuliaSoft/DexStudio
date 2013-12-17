package com.juliasoft.dexstudio.tab;

import java.awt.Color;
import java.awt.Component;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class DexStringHeader extends JTextPane
{
	private final String color = "rgb(" + getForeground().getRed() + ", " + getForeground().getGreen() + ", " + getForeground().getBlue() + ")";
	private final String fontName = getFont().getName();
	private final String titleFontSize = "14px";
	private final String numFontSize = "14pt";
	private final String titleStyle = ".title{font-family:" + fontName + "; font-size:" + titleFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String numStyle = ".num{font-family:" + fontName + "; font-size:" + numFontSize + "; color:" + color + "; font-weight:normal;}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">" + titleStyle + numStyle + "</style></head><body>";
	private final String closeFormat = "</body></html>";
	
	public DexStringHeader(Set<String> strings)
	{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);
		int strCount = strings.size();
		String text = "<div class='title'><b>Strings</b></div><div class='num'>Strings#: " + strCount + "</div>";
		this.setContentType("text/html");
		this.setText(htmlFormat + text + closeFormat);
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.TOP_ALIGNMENT);
		this.setEditable(false);
		this.setHighlighter(null);
		this.setMaximumSize(this.getPreferredSize());
	}
}
