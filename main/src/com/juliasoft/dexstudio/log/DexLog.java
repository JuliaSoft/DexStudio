package com.juliasoft.dexstudio.log;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Logger console
 * 
 * @author Zanoncello Matteo
 */
@SuppressWarnings("serial")
public class DexLog extends JPanel
{
	private JTextPane log = new JTextPane();
	private JComboBox<String> selection = new JComboBox<String>();
	private final String fontSize = 14 + "pt";
	private final String fontName = getFont().getName();
	private final String bodyStyle = "body{font-family:" + fontName + "font-size:" + fontSize + ";font-weight:normal;}";
	private final String warnStyle = ".warn{background-color:#EEEE66;}";
	private final String errorStyle = ".error{color:red;}";
	private final String debugStyle = ".debug{background-color:#DDDDFF}";
	private final String divStyle = "div{border-bottom: 1px solid black; padding: 3px 2px};";

	
	private final String htmlStart = "<html><head><style type='text/css'>"+ bodyStyle + warnStyle + errorStyle + debugStyle + divStyle +"</style></head><body>";
	private final String htmlClose = "</body></html>";
	//private HTMLDocument doc;
	
	private final class SwingAppender extends AbstractAppender implements Appender
	{
		public SwingAppender()
		{
			this("SwingAppender", null, PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%t] %-5level %c - %msg%n", null, null, "UTF8", null));
		}
		
		protected SwingAppender(String name, Filter filter, Layout<? extends Serializable> layout)
		{
			super(name, filter, layout);
		}
		
		@Override
		public void append(LogEvent event)
		{
			
			HTMLDocument doc = (HTMLDocument)log.getDocument();
			Element body = doc.getElement(doc.getDefaultRootElement(), StyleConstants.NameAttribute, HTML.Tag.BODY);
			
			String str = getLayout().toSerializable(event).toString();
			
			if(str.contains("WARN"))
				
				str = "<div class='warn'>" + str + "</div>";
			
			else if(str.contains("ERROR"))
				
				str = "<div class='warn'>" + str + "</div>";
			
			else if(str.contains("DEBUG"))
				
				str = "<div class='debug'>" + str + "</div>";
			
			try {
				doc.insertBeforeEnd(body, str);
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.setCaretPosition(doc.getLength());
		}
	}
	
	/**
	 * Constructor
	 */
	public DexLog()
	{
		super(new BorderLayout());
		log.setEditable(false);
		log.setContentType("text/html");
		log.setText(htmlStart + htmlClose);
		log.setMargin(new Insets(0,0,0,0));
		/*DefaultCaret c = (DefaultCaret)log.getCaret();
		c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);*/
		
		JPanel header = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(log);
		selection.addItem("Show All");
		selection.addItem("Option 2");
		selection.addItem("Option 3");
		header.add(new JLabel("Log"), BorderLayout.WEST);
		header.add(selection, BorderLayout.EAST);
		this.add(header, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		addVisualLogAppender();
	}
	
	private void addVisualLogAppender()
	{
		Logger l = (Logger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		if(l.getAppenders().containsKey("SwingAppender"))
		{
			// XXX: log??
			return;
		}
		l.addAppender(new SwingAppender());
	}
	
}
