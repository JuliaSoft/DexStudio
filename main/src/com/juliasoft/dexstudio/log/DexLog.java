package com.juliasoft.dexstudio.log;

import java.awt.BorderLayout;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

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
public class DexLog extends JPanel {
    private JTextPane log = new JTextPane();

    private final class SwingAppender extends AbstractAppender implements Appender {

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
            Document doc = DexLog.this.log.getDocument();
            try
            {
                doc.insertString(doc.getLength(), getLayout().toSerializable(event).toString(), null);
            } catch (BadLocationException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructor
     */
    public DexLog()
    {
        super(new BorderLayout());
        log.setEditable(false);
        JScrollPane scroll = new JScrollPane(log);
        this.add(new JLabel("Log"), BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        addVisualLogAppender();
    }

    private void addVisualLogAppender()
    {
        Logger l = (Logger) LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
        if (l.getAppenders().containsKey("SwingAppender"))
        {
            // XXX: log??
            return;
        }
        l.addAppender(new SwingAppender());
    }

    /**
     * Append log string to the console
     * 
     * @param str
     */
    public void log(String str)
    {
        if (log.getText() == "")
            log.setText(str);
        else
            log.setText(log.getText() + "\n" + str);
    }
}
