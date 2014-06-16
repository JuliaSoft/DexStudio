package com.juliasoft.dexstudio.tab;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.ContextGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.flow.FlowNode;
import com.juliasoft.dexstudio.tab.header.DexAnnotationHeader;
import com.juliasoft.dexstudio.tab.header.DexClassHeader;
import com.juliasoft.dexstudio.tab.header.DexMethodHeader;
import com.juliasoft.dexstudio.tab.header.DexStringCompareHeader;
import com.juliasoft.dexstudio.tab.header.DexStringHeader;
import com.juliasoft.dexstudio.tab.table.DexCodeTableModel;
import com.juliasoft.dexstudio.tab.table.DexConstructorTableModel;
import com.juliasoft.dexstudio.tab.table.DexFieldTableModel;
import com.juliasoft.dexstudio.tab.table.DexMethodTableModel;
import com.juliasoft.dexstudio.tab.table.DexStringCompareTableModel;
import com.juliasoft.dexstudio.tab.table.DexStringTableModel;
import com.juliasoft.dexstudio.tab.table.DexTable;
import com.juliasoft.dexstudio.tab.table.DexValueTableModel;
import com.juliasoft.dexstudio.utils.Library;
import com.juliasoft.dexstudio.utils.StringSet;

/**
 * Tab where DexGen elements' informations are visualized
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTreeTab extends JScrollPane implements DexTab {
	private JPanel content;
	private String title;
	private ImageIcon ico;
	private FlowNode analysisGraph= null;

	/**
	 * Constructor of ClassGen tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param clazz
	 *            The ClassGen instance
	 */
	public DexTreeTab(DexDisplay frame, ClassGen clazz) {
		initLayout();
		title = Library.shortName(clazz.getType().getName());
		content.add(new DexClassHeader(frame, clazz));
		content.add(new DexTable(frame, new DexFieldTableModel(clazz)));
		content.add(new DexTable(frame, new DexConstructorTableModel(clazz)));
		content.add(new DexTable(frame, new DexMethodTableModel(clazz)));
		ico = new ImageIcon(
				(AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags())) ? "imgs/tab/interface.png"
						: "imgs/tab/class.png");
	}

	/**
	 * Constructor of MethodGen tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param meth
	 *            The MethodGen instance
	 */
	public DexTreeTab(DexDisplay frame, MethodGen meth) {
		initLayout();
		title = (meth.isConstructor()) ? Library
				.printType(meth.getOwnerClass()) + "()" : meth.getName() + "()";
		
		if(!meth.isAbstract())
			analysisGraph = new FlowNode(meth.getCode().getInstructionList());
		
		content.add(new DexMethodHeader(frame, meth));
		content.add(new DexTable(frame, new DexCodeTableModel(meth), analysisGraph));
		ico = new ImageIcon("imgs/tab/method.png");
		
		
	}

	/**
	 * Constructor of Annotation tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param ann
	 *            The Annotation instance
	 */
	public DexTreeTab(DexDisplay frame, Annotation ann) {
		initLayout();
		title = Library.printType(ann.getType());
		content.add(new DexAnnotationHeader(ann));
		content.add(new DexTable(frame, new DexValueTableModel(ann)));
		ico = new ImageIcon("imgs/tab/annotation.png");
	}

	/**
	 * Constructor of Strings tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param strs
	 *            The Set of the strings
	 */
	public DexTreeTab(DexDisplay frame, StringSet strs) {
		initLayout();
		title = "Strings";
		content.add(new DexStringHeader(strs));
		content.add(new DexTable(frame, new DexStringTableModel(strs)));
		ico = new ImageIcon("imgs/tab/strings.png");
	}

	public DexTreeTab(DexDisplay frame, DiffNode<?> ctx) {
		if (!ctx.getDiffClass().equals(ContextGen.class))
			throw new IllegalArgumentException("DiffNode<ContextGen> expected");
		initLayout();
		title = "String comparison";
		content.add(new DexStringCompareHeader(ctx));
		content.add(new DexTable(frame, new DexStringCompareTableModel(ctx)));
		ico = new ImageIcon("imgs/tab/strings.png");
	}
	
	private void initLayout() {
		this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getVerticalScrollBar().setUnitIncrement(15);
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
				Color.white));
		this.setViewportView(content);
	}

	/**
	 * Get the title of the tab
	 * 
	 * 
	 */
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public ImageIcon getIco() {
		return this.ico;
	}

	@Override
	public Component getTab() {
		return this;
	}
}
