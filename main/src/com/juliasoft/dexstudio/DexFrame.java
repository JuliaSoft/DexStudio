package com.juliasoft.dexstudio;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.dexstudio.log.DexLog;
import com.juliasoft.dexstudio.menu.DexMenu;
import com.juliasoft.dexstudio.menu.DexOpenApk;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.tab.DexTabManager;
import com.juliasoft.dexstudio.tree.DexTree;
import com.juliasoft.dexstudio.tree.DexTreeAnnotation;
import com.juliasoft.dexstudio.tree.DexTreeClass;
import com.juliasoft.dexstudio.tree.DexTreeMethod;
import com.juliasoft.dexstudio.tree.DexTreeNode;
import com.juliasoft.dexstudio.tree.DexTreeStrings;

/**
 * Main frame of the project.
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexFrame extends JFrame implements DexVisualizable
{
	private DexTree tree = new DexTree();
	private DexTabManager tabManager = new DexTabManager();
	private DexLog log = new DexLog();
	private DexMenu menu = new DexMenu();
	
	/**
	 * Constructor
	 */
	public DexFrame()
	{
		// Setting the layout
		this.setLayout(new BorderLayout());
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("DexStudio");
		this.setIconImage(new ImageIcon("imgs/logo.png").getImage());
		this.setJMenuBar(menu);
		JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, tabManager);
		splitPane1.setFocusable(false);
		splitPane1.setPreferredSize(splitPane1.getMaximumSize());
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane1, log);
		splitPane2.setFocusable(false);
		this.getContentPane().add(splitPane2, BorderLayout.CENTER);
		this.setVisible(true);
		this.revalidate();
	}
	
	/**
	 * Update the layout of the frame
	 * 
	 * @param dexGen
	 *            DexGen file to open in the frame
	 * @param rootName
	 *            The name showed on the root of the navigation tree
	 */
	public void updateLayout(DexGen dexGen, String rootName)
	{
		this.tabManager.cleanTabs();
		this.tree.updateLayout(dexGen, rootName);
	}
	
	@Override
	public void openNewTab(DexTreeNode node)
	{
		DexTab tab = createTab(node);
		tabManager.addTab(tab);
	}
	
	@Override
	public void changeSelectedTab(DexTreeNode node)
	{
		DexTab tab = createTab(node);
		if(tabManager.getSelectedIndex() == -1)
			tabManager.addTab(tab);
		else
			tabManager.changeTab(tab);
	}
	
	private DexTab createTab(DexTreeNode node)
	{
		if(node instanceof DexTreeStrings)
			return new DexTab(this, (DexTreeStrings) node);
		else if(node instanceof DexTreeClass)
			return new DexTab(this, (DexTreeStrings) node);
		else if(node instanceof DexTreeMethod)
			return new DexTab(this, (DexTreeMethod) node);
		else if(node instanceof DexTreeAnnotation)
			return new DexTab(this, (DexTreeAnnotation) node);
		return null;
	}
	
	/**
	 * Add a string in the logger console
	 * 
	 * @param str
	 *            The adding string
	 */
	public void log(String str)
	{
		log.log(str);
	}
	
	/**
	 * Open a file browser to choose the apk file to open
	 */
	public void openApk()
	{
		// Browse the apk file
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Android Package (APK)", "apk");
		chooser.setFileFilter(filter);
		int read = chooser.showOpenDialog(this);
		if(read == JFileChooser.CANCEL_OPTION)
			return;
		File f = chooser.getSelectedFile();
		if( ! f.getPath().endsWith(".apk"))
		{
			JOptionPane.showMessageDialog(null, "Invalid file extension");
			return;
		}
		new DexOpenApk(this, f);
		menu.open(true);
		menu.updateItems();
	}
	
	public void closeApk()
	{
		this.tabManager.cleanTabs();
		this.tree.cleanTree();
	}
	
	/**
	 * Returns the navigation tree of the frame
	 * 
	 * @return the navigation tree
	 */
	public DexTree getTree()
	{
		return this.tree;
	}
}
