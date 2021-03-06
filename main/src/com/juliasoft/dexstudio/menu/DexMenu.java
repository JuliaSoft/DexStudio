package com.juliasoft.dexstudio.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.navigate.DexSearch;

/**
 * The menu bar of the frame
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexMenu extends JMenuBar {
	private boolean open = false;
	private DexMenuItem menuFileOpen, menuFileClose, menuFileExit,
			menuCompareOpen, menuNavigateSearch, menuInfoAbout;
	private DexFrame frame;

	/**
	 * Constructor
	 */
	public DexMenu(DexFrame frame) {
		this.frame = frame;
		// Menu Items
		menuFileOpen = new DexMenuItem("Open apk");
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke('O',
				KeyEvent.CTRL_DOWN_MASK));

		menuFileOpen.setIcon(new ImageIcon("imgs/menu/open.png"));
		menuFileOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openApk();
			}
		});
		/*
		 * menuFileSave = new DexMenuItem("Save apk");
		 * menuFileSave.setAccelerator(KeyStroke.getKeyStroke('S',
		 * KeyEvent.CTRL_DOWN_MASK)); menuFileSave.setIcon(new
		 * ImageIcon("imgs/menu/save.png")); menuFileSave.addActionListener(new
		 * ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * JOptionPane.showMessageDialog(null, "Under Construction, sorry...");
		 * } });
		 */
		menuFileClose = new DexMenuItem("Close apk");
		menuFileClose.setIcon(new ImageIcon("imgs/menu/close.png"));
		menuFileClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeApk();
				open(false);
			}
		});
		menuFileExit = new DexMenuItem("Exit");
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke('Q',
				KeyEvent.CTRL_DOWN_MASK));
		menuFileExit.setIcon(new ImageIcon("imgs/menu/exit.png"));
		menuFileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DexMenu.this.frame.dispose();
			}
		});
		menuCompareOpen = new DexMenuItem("Compare Apk");
		menuCompareOpen.setAccelerator(KeyStroke.getKeyStroke('C',
				KeyEvent.CTRL_DOWN_MASK));
		menuCompareOpen.setIcon(new ImageIcon("imgs/menu/open.png"));
		menuCompareOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compareApk();
			}
		});
		menuNavigateSearch = new DexMenuItem("Search");
		menuNavigateSearch.setAccelerator(KeyStroke.getKeyStroke('F',
				KeyEvent.CTRL_DOWN_MASK));
		menuNavigateSearch.setIcon(new ImageIcon("imgs/menu/search.png"));
		menuNavigateSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DexSearch(DexMenu.this.frame);
			}
		});

		menuInfoAbout = new DexMenuItem("About DexStudio");
		menuInfoAbout.setAccelerator(KeyStroke.getKeyStroke('I',
				KeyEvent.CTRL_DOWN_MASK));
		menuInfoAbout.setIcon(new ImageIcon("imgs/menu/info.png"));
		menuInfoAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								null,
								"DexStudio v.1.0.0\n\nDeveloped by:\nAncona Eugenio\nZanoncello Matteo\nIn collaboration with Juliasoft s.r.l\nhttp://www.juliasoft.com/",
								"About DEXplorer", JOptionPane.PLAIN_MESSAGE,
								new ImageIcon("imgs/logo.png"));
			}
		});
		// Menu
		JMenu menuFile = new JMenu("File");
		menuFile.add(menuFileOpen);
		// menuFile.add(menuFileSave);
		menuFile.add(menuFileClose);
		menuFile.addSeparator();
		menuFile.add(menuFileExit);
		JMenu menuCompare = new JMenu("Compare");
		menuCompare.add(menuCompareOpen);
		JMenu menuNavigate = new JMenu("Navigate");
		menuNavigate.add(menuNavigateSearch);
		JMenu menuInfo = new JMenu("?");
		// menuInfo.add(menuInfoHelp);
		menuInfo.add(menuInfoAbout);
		this.add(menuFile);
		this.add(menuCompare);
		this.add(menuNavigate);
		this.add(menuInfo);
		updateItems();
	}

	public void open(boolean value) {
		open = value;
	}

	public void updateItems() {
		// menuFileSave.setEnabled(open);
		menuFileClose.setEnabled(open);
		menuCompareOpen.setEnabled(open);
		menuNavigateSearch.setEnabled(open);
	}

	/**
	 * Open a file browser to choose the apk file to open
	 */
	public void openApk() {
		// Browse the apk file
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Android Package (APK)", "apk");
		chooser.setFileFilter(filter);
		int read = chooser.showOpenDialog(this);
		if (read == JFileChooser.CANCEL_OPTION)
			return;
		File f = chooser.getSelectedFile();
		if (!f.getPath().endsWith(".apk")) {
			JOptionPane.showMessageDialog(null, "Invalid file extension");
			return;
		}
		closeApk();
		new DexOpenApk(frame, f);
		open(true);
		updateItems();
	}

	public void compareApk() {
		// Browse the apk file
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Android Package (APK)", "apk");
		chooser.setFileFilter(filter);
		int read = chooser.showOpenDialog(this);
		if (read == JFileChooser.CANCEL_OPTION)
			return;
		File f = chooser.getSelectedFile();
		if (!f.getPath().endsWith(".apk")) {
			JOptionPane.showMessageDialog(null, "Invalid file extension");
			return;
		}
		new DexOpenApk(frame, frame.getDexGen(), f);
		updateItems();
	}

	public void closeApk() {
		frame.cleanLayout();
		updateItems();
	}
}
