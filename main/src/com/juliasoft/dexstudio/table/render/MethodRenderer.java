package com.juliasoft.dexstudio.table.render;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.dexstudio.DexVisualizable;
import com.juliasoft.dexstudio.cell.DexMethodCell;

@SuppressWarnings("serial")
public class MethodRenderer extends AbstractDexEditorRenderer {

    private final DexVisualizable display;

    public MethodRenderer(DexVisualizable display)
    {
        this.display = display;
    }

    @Override
    protected JTextPane getJTextPane(final Object value)
    {
        if (!(value instanceof MethodGen))
        {
            throw new IllegalArgumentException("value");
        }
        DexMethodCell cell = new DexMethodCell((MethodGen) value);
        HyperlinkListener hll = new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e)
            {
                if (e.getEventType().equals(EventType.ACTIVATED))
                {
                    display.changeSelectedTab(value);
                }
            }
        };
        cell.addHyperlinkListener(hll);
        return cell;
    }

    @Override
    protected boolean supportsRendering(Object value)
    {
        return (value instanceof MethodGen);
    }

}
