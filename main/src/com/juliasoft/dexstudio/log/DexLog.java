package com.juliasoft.dexstudio.log;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Logger console
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexLog extends JPanel
{
	private JTextPane log = new JTextPane();
	
	/**
	 * Constructor
	 */
	public DexLog()
	{
		super(new BorderLayout());
		log.setEditable(false);
		JScrollPane scroll = new JScrollPane(log);
		this.add(new JLabel("LOGGER"), BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.setPreferredSize(this.getMinimumSize());
	}
	
	/**
	 * Append log string to the console
	 * @param str
	 */
	public void log(String str)
	{
		if(log.getText() == "")
			log.setText(str);
		else
			log.setText(log.getText() + "\n" + str);
	}
}
