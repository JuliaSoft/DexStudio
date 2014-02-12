package com.juliasoft.dexstudio.tab.table.render;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.Annotation;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.cell.DexAnnotationCell;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.utils.AnnotationSet;

@SuppressWarnings("serial")
public class AnnotationRenderer extends AbstractDexEditorRenderer
{
	private final DexDisplay display;
	
	public AnnotationRenderer(DexDisplay display)
	{
		this.display = display;
	}
	
	@Override
	protected JTextPane getJTextPane(final Object value)
	{
		if(!(value instanceof AnnotationSet))
		{
			throw new IllegalArgumentException("value");
		}
		DexAnnotationCell cell = new DexAnnotationCell((AnnotationSet) value);// TODO:
																				// creare
																				// metodo??
		HyperlinkListener hll = new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if(e.getEventType().equals(EventType.ACTIVATED))
				{
					String code = e.getDescription();
					if(code.matches("ann[0-9]+"))
					{
						int index = Integer.parseInt(code.substring(3));
						display.changeSelectedTab(new DexTab(display, (Annotation) ((AnnotationSet) value).toArray()[index]));
					}
					else
						throw new IllegalArgumentException("Wrong Event");
				}
			}
		};
		cell.addHyperlinkListener(hll);
		return cell;
	}
	
	@Override
	protected boolean supportsRendering(Object value)
	{
		return (value instanceof AnnotationSet);
	}
}
