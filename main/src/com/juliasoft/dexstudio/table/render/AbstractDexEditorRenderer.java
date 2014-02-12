package com.juliasoft.dexstudio.table.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public abstract class AbstractDexEditorRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private Object value;

    public AbstractDexEditorRenderer()
    {
        super();
    }

    @Override
    public Object getCellEditorValue()
    {
        return value;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        return getComponent(value, row, column);
    }

    protected abstract JTextPane getJTextPane(Object value);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        return getComponent(value, row, column);
    }

    private Component getComponent(Object value, int row, int column)
    {
        this.value = value;
        JTextPane cell;

        if (value != null && supportsRendering(value))
        {
            cell = getJTextPane(value);
        }
        else
        {
            cell = new JTextPane();
            cell.setText(String.valueOf(value));
        }
        cell.setBackground((row % 2 == 0) ? Color.WHITE : new Color(238, 238, 239));
        cell.setBorder(BorderFactory.createMatteBorder(2, 3, 2, 2, (row % 2 == 0) ? Color.WHITE : new Color(238, 238, 239)));
        cell.setEditable(false);
        cell.setFocusable(false);
        // cell.addHyperlinkListener(new HyperlinkCellListener(cell));
        return cell;
    }

    /**
     * Tells if this renderer is able to render the specified value.
     * 
     * @param value
     * @return
     */
    protected abstract boolean supportsRendering(Object value);

}
