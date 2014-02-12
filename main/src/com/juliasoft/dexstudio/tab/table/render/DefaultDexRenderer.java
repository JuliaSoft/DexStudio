package com.juliasoft.dexstudio.tab.table.render;

import javax.swing.JTextPane;

import com.juliasoft.amalia.common.HumanReadable;

@SuppressWarnings("serial")
public class DefaultDexRenderer extends AbstractDexEditorRenderer
{
	private final JTextPane pane = new JTextPane();
	
	public DefaultDexRenderer()
	{}
	
	@Override
	protected JTextPane getJTextPane(Object value)
	{
		if(value instanceof HumanReadable)
		{
			pane.setText(((HumanReadable) value).toHuman());
		}
		else
		{
			pane.setText(String.valueOf(value));
		}
		return pane;
	}
	
	/**
	 * Returns true.
	 */
	@Override
	protected boolean supportsRendering(Object value)
	{
		return true;
	}
}
