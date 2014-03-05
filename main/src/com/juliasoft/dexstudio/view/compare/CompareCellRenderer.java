package com.juliasoft.dexstudio.view.compare;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.view.NodeType;

@SuppressWarnings("serial")
public class CompareCellRenderer extends DefaultTreeCellRenderer
{
	public CompareCellRenderer()
	{}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		if(!(value instanceof CompareNode))
			throw new IllegalArgumentException();
		
		CompareNode node = (CompareNode) value;
		
		if(node.getType().equals(NodeType.ROOT))
		{
			setIcon(new ImageIcon("imgs/tree/root.png"));
		}
		else if(node.getType().equals(NodeType.FOLDER))
		{
			if(expanded)
				setIcon(new ImageIcon("imgs/tree/folder_open.png"));
			else
				setIcon(new ImageIcon("imgs/tree/folder_close.png"));
		}
		else if(node.getType().equals(NodeType.STRINGS))
		{
			setIcon(getIcon(node));
		}
		else if(node.getType().equals(NodeType.PACKAGE) ||
				node.getType().equals(NodeType.CLASS) ||
				node.getType().equals(NodeType.INTERFACE) ||
				node.getType().equals(NodeType.METHOD) ||
				node.getType().equals(NodeType.FIELD) ||
				node.getType().equals(NodeType.ANNOTATION))
		{
			setIcon(getIcon(node));
			setLabel(node.getState());
		}
		
		return this;
	}
	
	private ImageIcon getIcon(CompareNode node)
	{
		String state = new String();
		switch(node.getState())
		{
			case UNKNOWN:
				state = "unknown";
				break;
			case SAME:
				state = "same";
				break;
			case LEFT_ONLY:
				state = "left";
				break;
			case RIGHT_ONLY:
				state = "right";
				break;
			case DIFFERENT:
				state = "different";
				break;
		}
		switch(node.getType())
		{
			case ROOT:
			case FOLDER:
				return null;
			case STRINGS:
				return new ImageIcon("imgs/tree/" + state + "/strings.png");
			case PACKAGE:
				return new ImageIcon("imgs/tree/" + state + "/package.png");
			case CLASS:
				return new ImageIcon("imgs/tree/" + state + "/class.png");
			case INTERFACE:
				return new ImageIcon("imgs/tree/" + state + "/interface.png");
			case FIELD:
				return new ImageIcon("imgs/tree/" + state + "/field.png");
			case METHOD:
				return new ImageIcon("imgs/tree/" + state + "/method.png");
			case ANNOTATION:
				return new ImageIcon("imgs/tree/" + state + "/annotation.png");
		}
		return null;
	}
	
	private void setLabel(DiffState state)
	{
		String diff = new String();
		switch(state)
		{
			case UNKNOWN:
				diff = "(UNKNOWN)";
				break;
			case SAME:
				diff = "(SAME)";
				break;
			case LEFT_ONLY:
				diff = "(LEFT)";
				break;
			case RIGHT_ONLY:
				diff = "(RIGHT)";
				break;
			case DIFFERENT:
				diff = "(DIFFERENT)";
				break;
		}
		this.setText(diff + " " + this.getText());
	}
}