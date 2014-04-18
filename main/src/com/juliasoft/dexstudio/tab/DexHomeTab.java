package com.juliasoft.dexstudio.tab;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class DexHomeTab extends JPanel implements DexTab {

	private final String color = "rgb(" + getForeground().getRed() + ", "
			+ getForeground().getGreen() + ", " + getForeground().getBlue()
			+ ")";
	private final String fontName = getFont().getName();
	private final String descriptionFontSize = "20px";
	private final String descriptionStyle = ".description{font-family:"
			+ fontName + "; font-size:" + descriptionFontSize + "; color:"
			+ color + "; font-weight:normal; text-align:center;}";
	private final String htmlFormat = "<html><head><style type=\"text/css\">"
			+ descriptionStyle + "</style></head><body>";
	private final String closeFormat = "</body></html>";

	public DexHomeTab() {
		super(new BorderLayout());
		JLabel textLabel = new JLabel(htmlFormat
				+ "<div class='description'>Welcome to DexStudio</div>"
				+ closeFormat);
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(textLabel, BorderLayout.NORTH);
		JLabel imageLabel = new JLabel();
		imageLabel.setIcon(new ImageIcon("logo/logo_dexstudio.png"));
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(imageLabel, BorderLayout.CENTER);
	}

	@Override
	public String getTitle() {
		return "Home";
	}

	@Override
	public Component getTab() {
		return this;
	}

	@Override
	public ImageIcon getIco() {
		return new ImageIcon("imgs/icon.png");
	}
}
