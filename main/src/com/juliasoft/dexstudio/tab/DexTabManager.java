package com.juliasoft.dexstudio.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Tab manager of the project
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexTabManager extends JTabbedPane {
	/**
	 * Constructor
	 */
	public DexTabManager() {
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Remove all the visualized tabs
	 */
	public void cleanTabs() {
		this.removeAll();
	}

	/**
	 * Add a new DexTab
	 * 
	 * @param tab
	 *            The DexTab instance
	 */
	public void addTab(DexTab tab) {
		super.addTab(tab.getTitle(), tab.getTab());
		this.setSelectedIndex(this.getTabCount() - 1);
		this.setTabComponentAt(this.getSelectedIndex(), makeTabTitle(tab));
	}

	/**
	 * Change the content of the current tab
	 * 
	 * @param tab
	 *            The DexTab instance
	 */
	public void changeTab(DexTab tab) {
		int pos = this.getSelectedIndex();
		this.removeTabAt(pos);
		this.insertTab(tab.getTitle(), null, tab.getTab(), null, pos);
		this.setTabComponentAt(pos, makeTabTitle(tab));
		this.setSelectedIndex(pos);
	}

	private JPanel makeTabTitle(final DexTab tab) {
		JPanel result = new JPanel(new GridBagLayout());
		result.setOpaque(false);
		JLabel icon = new JLabel(tab.getIco());
		JLabel title = new JLabel(tab.getTitle());
		title.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		JButton close = new JButton(new ImageIcon("imgs/tab/close.png"));
		close.setPreferredSize(new Dimension(17, 17));
		close.setToolTipText("close this tab");
		close.setUI(new BasicButtonUI());
		close.setContentAreaFilled(false);
		close.setFocusable(false);
		close.setBorder(BorderFactory.createEtchedBorder());
		close.setBorderPainted(false);
		close.addMouseListener(closeButtonMouseListener);
		close.setRolloverEnabled(true);
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DexTabManager.this.removeTabAt(DexTabManager.this
						.indexOfTab(tab.getTitle()));
			}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		result.add(icon, gbc);
		result.add(title, gbc);
		result.add(close, gbc);
		return result;
	}

	private final static MouseListener closeButtonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};
}
