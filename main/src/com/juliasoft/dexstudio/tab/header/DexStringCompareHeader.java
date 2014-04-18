package com.juliasoft.dexstudio.tab.header;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;

import com.juliasoft.amalia.dex.codegen.diff.DiffNode;

/**
 * The top component of the String comparison tab
 * 
 * 
 * @author Eugenio Ancona
 * 
 */

@SuppressWarnings("serial")
public class DexStringCompareHeader extends JTextPane {
	private final String color = "rgb(" + getForeground().getRed() + ", "
			+ getForeground().getGreen() + ", " + getForeground().getBlue()
			+ ")";
	private final String fontName = getFont().getName();
	private final String titleFontSize = "14px";
	private final String numFontSize = "14pt";
	private final String titleStyle = ".title{font-family:" + fontName
			+ "; font-size:" + titleFontSize + "; color:" + color
			+ "; font-weight:bold;}";
	private final String numStyle = ".num{font-family:" + fontName
			+ "; font-size:" + numFontSize + "; color:" + color
			+ "; font-weight:normal;}";
	private final String numredStyle = "#red{color:#F84545}";
	private final String numgreenStyle = "#green{color:#45F88D}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">"
			+ titleStyle + numStyle + numredStyle + numgreenStyle
			+ "</style></head><body>";
	private final String closeFormat = "</body></html>";

	public DexStringCompareHeader(DiffNode<?> ctx) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);

		int leftCount = 0;
		int rightCount = 0;
		int sameCount = 0;
		int totCount = ctx.getChildren().size();

		for (DiffNode<?> node : ctx.getChildren()) {

			switch (node.getState()) {

			case SAME:
				sameCount++;
				break;
			case RIGHT_ONLY:
				rightCount++;
				break;
			case LEFT_ONLY:
				leftCount++;
				break;
			default:
				;

			}

		}

		String sameDiv = "<div class='num'>SameStrings#: " + sameCount
				+ "</div>";
		String rightDiv = "<div class='num' id='red'>RightStrings#: "
				+ rightCount + "</div>";
		String leftDiv = "<div class='num' id='green'>LeftStrings#: "
				+ leftCount + "</div>";
		String totDiv = "<div class='num'>TotalStrings#: " + totCount
				+ "</div>";

		String text = "<div class='title'>Strings</div>" + sameDiv + rightDiv
				+ leftDiv + totDiv;
		this.setContentType("text/html");
		this.setText(htmlFormat + text + closeFormat);
		this.setAlignmentY(Component.LEFT_ALIGNMENT);
		this.setAlignmentX(Component.TOP_ALIGNMENT);
		this.setEditable(false);
		this.setHighlighter(null);
		this.setMaximumSize(this.getPreferredSize());

	}
}
