package com.juliasoft.dexstudio.log;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * The logger console using log4j logging systems
 * 
 * @author Eugenio Ancona
 */
@SuppressWarnings("serial")
public class DexLog extends JPanel {
	private JTextPane log = new JTextPane();
	private JComboBox<String> selection = new JComboBox<String>();
	private final String fontSize = 12 + "pt";
	private final String fontName = getFont().getName();
	private final String bodyStyle = "body{font-family:" + fontName
			+ ";font-size:" + fontSize + ";font-weight:normal;}";
	private final String warnStyle = ".warn{background-color:#EEEE66;}";
	private final String errorStyle = ".error{backcolor:red;}";
	private final String debugStyle = ".debug{background-color:#DDDDFF}";
	private final String divStyle = "div{border-bottom: 1px solid black; padding: 3px 2px};";
	private final String htmlStart = "<html><head><style type='text/css'>"
			+ bodyStyle + warnStyle + errorStyle + debugStyle + divStyle
			+ "</style></head><body>";
	private final String htmlClose = "</body></html>";
	private HTMLDocument fullDoc;
	private HTMLDocument warnDoc;
	private HTMLDocument errorDoc;
	private HTMLDocument debugDoc;

	private final class SwingAppender extends AbstractAppender implements
			Appender {
		public SwingAppender() {
			this("SwingAppender", null, PatternLayout.createLayout(
					"%d{HH:mm:ss.SSS} [%t] %-5level %c - %msg%n", null, null,
					"UTF8", null));
		}

		protected SwingAppender(String name, Filter filter,
				Layout<? extends Serializable> layout) {
			super(name, filter, layout);
		}

		@Override
		public void append(LogEvent event) {
			String str = getLayout().toSerializable(event).toString();
			if (str.contains("WARN")) {
				str = "<div class='warn'>" + str + "</div>";
				Element warnBody = warnDoc.getElement(
						warnDoc.getDefaultRootElement(),
						StyleConstants.NameAttribute, HTML.Tag.BODY);
				try {
					warnDoc.insertBeforeEnd(warnBody, str);
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (str.contains("ERROR")) {
				str = "<div class='error'>" + str + "</div>";
				Element errorBody = errorDoc.getElement(
						errorDoc.getDefaultRootElement(),
						StyleConstants.NameAttribute, HTML.Tag.BODY);
				try {
					errorDoc.insertBeforeEnd(errorBody, str);
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			else if (str.contains("DEBUG")) {
				str = "<div class='debug'>" + str + "</div>";
				Element debugBody = debugDoc.getElement(
						debugDoc.getDefaultRootElement(),
						StyleConstants.NameAttribute, HTML.Tag.BODY);
				try {
					debugDoc.insertBeforeEnd(debugBody, str);
				} catch (BadLocationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			else
				str = "<div>" + str + "</div>";

			try {
				Element fullBody = fullDoc.getElement(
						fullDoc.getDefaultRootElement(),
						StyleConstants.NameAttribute, HTML.Tag.BODY);
				fullDoc.insertBeforeEnd(fullBody, str);
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}

	/**
	 * Constructor
	 */
	public DexLog() {
		super(new BorderLayout());
		log.setEditable(false);
		log.setContentType("text/html");
		log.setText(htmlStart + htmlClose);
		log.setMargin(new Insets(0, 0, 0, 0));
		fullDoc = (HTMLDocument) log.getDocument();
		HTMLEditorKit kit = new HTMLEditorKit();
		warnDoc = (HTMLDocument) kit.createDefaultDocument();
		errorDoc = (HTMLDocument) kit.createDefaultDocument();
		debugDoc = (HTMLDocument) kit.createDefaultDocument();
		try {
			warnDoc.setInnerHTML(warnDoc.getDefaultRootElement(), htmlStart
					+ htmlClose);
			errorDoc.setInnerHTML(errorDoc.getDefaultRootElement(), htmlStart
					+ htmlClose);
			debugDoc.setInnerHTML(debugDoc.getDefaultRootElement(), htmlStart
					+ htmlClose);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel header = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(log);
		selection.addItem("All");
		selection.addItem("Errors");
		selection.addItem("Warnings");
		selection.addItem("Debug");
		selection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String selected = (String) cb.getSelectedItem();
				switch (selected) {
				case "All":
					log.setDocument(fullDoc);
					break;
				case "Errors":
					log.setDocument(errorDoc);
					break;
				case "Warnings":
					log.setDocument(warnDoc);
					break;
				case "Debug":
					log.setDocument(debugDoc);
					break;
				}
				log.setCaretPosition(log.getDocument().getLength());
			}
		});
		JPanel buttons = new JPanel(new GridBagLayout());
		JButton clean = new JButton(new ImageIcon("imgs/tab/clean.png"));
		clean.setPreferredSize(new Dimension(17, 17));
		clean.setToolTipText("clean log");
		clean.setUI(new BasicButtonUI());
		clean.setContentAreaFilled(false);
		clean.setFocusable(false);
		clean.setBorder(BorderFactory.createEtchedBorder());
		clean.setBorderPainted(false);
		clean.addMouseListener(cleanButtonMouseListener);
		clean.setRolloverEnabled(true);
		clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DexLog.this.clean();
			}
		});
		buttons.add(clean, gbc);
		buttons.add(Box.createRigidArea(new Dimension(10, 10)));
		buttons.add(selection, gbc);
		header.add(new JLabel("Log"), BorderLayout.WEST);
		header.add(buttons, BorderLayout.EAST);
		this.add(header, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		addVisualLogAppender();
	}

	private void addVisualLogAppender() {
		Logger l = (Logger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		if (l.getAppenders().containsKey("SwingAppender")) {
			// XXX: log??
			return;
		}
		l.addAppender(new SwingAppender());
	}

	public void clean() {
		try {
			Element body = fullDoc.getElement(fullDoc.getDefaultRootElement(),
					StyleConstants.NameAttribute, HTML.Tag.BODY);
			fullDoc.setOuterHTML(body, "<body></body>");
			body = warnDoc.getElement(warnDoc.getDefaultRootElement(),
					StyleConstants.NameAttribute, HTML.Tag.BODY);
			warnDoc.setOuterHTML(body, "<body></body>");
			body = errorDoc.getElement(errorDoc.getDefaultRootElement(),
					StyleConstants.NameAttribute, HTML.Tag.BODY);
			errorDoc.setOuterHTML(body, "<body></body>");
			body = debugDoc.getElement(debugDoc.getDefaultRootElement(),
					StyleConstants.NameAttribute, HTML.Tag.BODY);
			debugDoc.setOuterHTML(body, "<body></body>");
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private final static MouseListener cleanButtonMouseListener = new MouseAdapter() {
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
