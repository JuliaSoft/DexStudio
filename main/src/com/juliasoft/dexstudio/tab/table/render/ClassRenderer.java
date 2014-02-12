package com.juliasoft.dexstudio.tab.table.render;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.dexstudio.DexDisplay;
import com.juliasoft.dexstudio.cell.DexClassCell;

@SuppressWarnings("serial")
public class ClassRenderer extends AbstractDexEditorRenderer
{
	private final DexDisplay display;
	
	public ClassRenderer(DexDisplay display)
	{
		this.display = display;
	}
	
	@Override
	protected JTextPane getJTextPane(final Object value)
	{
		if(!(value instanceof Type))
		{
			throw new IllegalArgumentException("value");
		}
		DexClassCell cell = new DexClassCell((Type) value);
		HyperlinkListener hll = new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				display.changeSelectedTab(null); // TODO: aggiungere metodo per
													// ottenere ClassGen da Type
			}
		};
		cell.addHyperlinkListener(hll);
		return cell;
	}
	
	@Override
	protected boolean supportsRendering(Object value)
	{
		return (value instanceof Type);
	}
}
