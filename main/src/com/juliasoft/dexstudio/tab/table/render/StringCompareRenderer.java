package com.juliasoft.dexstudio.tab.table.render;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.juliasoft.amalia.dex.codegen.diff.DiffState;

/**
 * Editor and renderer for diff cells in TringCompareTab
 * 
 * 
 * @author Eugenio Ancona
 * 
 */
@SuppressWarnings("serial")
public class StringCompareRenderer extends AbstractDexEditorRenderer {

	@Override
	protected boolean supportsRendering(Object value) {
		return true;
	}

	@Override
	protected JTextPane getJTextPane(Object value) {

		if (!(value instanceof DiffState))

			throw new IllegalArgumentException();

		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);

		Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);

		Icon icon;

		switch ((DiffState) value) {

		case LEFT_ONLY:
			icon = new ImageIcon("imgs/cmp/string_left.png");
			break;

		case RIGHT_ONLY:
			icon = new ImageIcon("imgs/cmp/string_right.png");
			break;
		default:
			icon = null;

		}
		JLabel label = new JLabel(icon);
		StyleConstants.setComponent(labelStyle, label);

		try {
			document.insertString(document.getLength(), "Ignored", labelStyle);
		} catch (BadLocationException badLocationException) {
			System.err.println("Oops");
		}

		JTextPane textPane = new JTextPane(document);

		return textPane;

	}

}
