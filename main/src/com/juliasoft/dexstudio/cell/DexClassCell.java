package com.juliasoft.dexstudio.cell;

import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.utils.Library;

/**
 * The table cell displaying the info of a type(class)
 * 
 * 
 * @author Eugenio Ancona
 * 
 */

@SuppressWarnings("serial")
public class DexClassCell extends JTextPane {
	private final String fontSize = getFont().getSize() + "pt";
	private final String color = "rgb(" + getForeground().getRed() + ", "
			+ getForeground().getGreen() + ", " + getForeground().getBlue()
			+ ")";
	private final String fontName = getFont().getName();
	private final String htmlFormat = "<html><head><style type=\"text/css\">body{font-family:"
			+ fontName
			+ "; font-weight:normal; font-size:"
			+ fontSize
			+ "pt;color:" + color + ";}</style></head><body>";
	private final String closeFormat = "</body></html>";
	private ClassGen clazz;

	public DexClassCell(Type type, DexDisplay display) {
		super();
		this.setContentType("text/html");
		clazz = display.getDexGen().getClassGen(type);
		String text = ((clazz != null) ? "<a href='type'>" : "")
				+ Library.printType(type) + ((clazz != null) ? "</a>" : "");
		this.setText(htmlFormat + text + closeFormat);
	}

}
