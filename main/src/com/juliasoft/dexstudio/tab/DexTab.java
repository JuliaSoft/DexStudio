package com.juliasoft.dexstudio.tab;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.table.DexCodeTableModel;
import com.juliasoft.dexstudio.table.DexConstructorTableModel;
import com.juliasoft.dexstudio.table.DexFieldTableModel;
import com.juliasoft.dexstudio.table.DexMethodTableModel;
import com.juliasoft.dexstudio.table.DexStringTableModel;
import com.juliasoft.dexstudio.table.DexTable;
import com.juliasoft.dexstudio.table.DexValueTableModel;
import com.juliasoft.dexstudio.utils.Library;
import com.juliasoft.dexstudio.utils.StringSet;

/**
 * Tab where DexGen elements' informations are visualized
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTab extends JScrollPane
{
	private JPanel content;
	private String title;
	
	/**
	 * Constructor of ClassGen tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param clazz
	 *            The ClassGen instance
	 */
	public DexTab(DexDisplay frame, ClassGen clazz)
	{
		initLayout();
		title = Library.shortName(clazz.getType().getName());
		content.add(new DexClassHeader(frame, clazz));
		content.add(new DexTable(frame, new DexFieldTableModel(clazz)));
		content.add(new DexTable(frame, new DexConstructorTableModel(clazz)));
		content.add(new DexTable(frame, new DexMethodTableModel(clazz)));
	}
	
	/**
	 * Constructor of MethodGen tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param clazz
	 *            The MethodGen instance
	 */
	public DexTab(DexDisplay frame, MethodGen meth)
	{
		initLayout();
		title = (meth.isConstructor()) ? Library.printType(meth.getOwnerClass()) + "()" : meth.getName() + "()";
		content.add(new DexMethodHeader(frame, meth));
		content.add(new DexTable(frame, new DexCodeTableModel(meth)));
	}
	
	/**
	 * Constructor of Annotation tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param clazz
	 *            The Annotation instance
	 */
	public DexTab(DexDisplay frame, Annotation ann)
	{
		initLayout();
		title = Library.printType(ann.getType());
		content.add(new DexAnnotationHeader(ann));
		content.add(new DexTable(frame, new DexValueTableModel(ann)));
	}
	
	/**
	 * Constructor of Strings tab
	 * 
	 * @param frame
	 *            The DexFrame reference
	 * @param clazz
	 *            The Set of the strings
	 */
	public DexTab(DexDisplay frame, StringSet strs)
	{
		initLayout();
		title = "Strings";
		content.add(new DexStringHeader(strs));
		content.add(new DexTable(frame, new DexStringTableModel(strs)));
	}
	
	private void initLayout()
	{
		this.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getVerticalScrollBar().setUnitIncrement(15);
		content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.white));
		this.setViewportView(content);
	}
	
	/**
	 * Get the title of the tab
	 * 
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}
}
