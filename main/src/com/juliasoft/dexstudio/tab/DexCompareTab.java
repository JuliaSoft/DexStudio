package com.juliasoft.dexstudio.tab;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;

@SuppressWarnings("serial")
public class DexCompareTab extends JSplitPane implements DexTab {
	private String title;
	private DexTreeTab leftTab, rightTab;

	public DexCompareTab(DexDisplay frame, ClassGen left, ClassGen right) {
		if (left != null)
			leftTab = new DexTreeTab(frame, left);
		if (right != null)
			rightTab = new DexTreeTab(frame, right);
		initLayout();
	}

	public DexCompareTab(DexDisplay frame, MethodGen left, MethodGen right) {
		if (left != null)
			leftTab = new DexTreeTab(frame, left);
		if (right != null)
			rightTab = new DexTreeTab(frame, right);
		initLayout();
	}

	public DexCompareTab(DexDisplay frame, Annotation left, Annotation right) {
		if (left != null)
			leftTab = new DexTreeTab(frame, left);
		if (right != null)
			rightTab = new DexTreeTab(frame, right);
		initLayout();
	}

	private void initLayout() {
		title = ((leftTab != null) ? leftTab : rightTab).getTitle();
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.setDividerLocation(0.5);
		this.setResizeWeight(0.5);
	}

	@Override
	public String getTitle() {
		return "Compare: " + this.title;
	}

	@Override
	public Component getTab() {
		if (leftTab == null)
			return rightTab;
		else if (rightTab == null)
			return leftTab;
		else {
			this.setLeftComponent(leftTab);
			this.setRightComponent(rightTab);
			return this;
		}
	}

	@Override
	public ImageIcon getIco() {
		return ((leftTab != null) ? leftTab : rightTab).getIco();
	}
}
