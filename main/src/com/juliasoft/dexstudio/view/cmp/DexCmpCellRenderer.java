package com.juliasoft.dexstudio.view.cmp;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.amalia.dex.codegen.diff.DiffNode;
import com.juliasoft.amalia.dex.codegen.diff.DiffState;
import com.juliasoft.dexstudio.view.cmp.node.DexAnnotationCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexClassCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexFieldCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexMethodCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexPackageCmp;
import com.juliasoft.dexstudio.view.cmp.node.DexStringsCmp;
import com.juliasoft.dexstudio.view.tree.node.DexFolderNode;
import com.juliasoft.dexstudio.view.tree.node.DexRootNode;

@SuppressWarnings("serial")
public class DexCmpCellRenderer extends DefaultTreeCellRenderer
{
	public DexCmpCellRenderer()
	{}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		
		if(value instanceof DexRootNode)
		{
			setIcon(new ImageIcon("imgs/tree/root.png"));
		}
		else if(value instanceof DexFolderNode)
		{
			if(expanded)
				setIcon(new ImageIcon("imgs/tree/folder_open.png"));
			else
				setIcon(new ImageIcon("imgs/tree/folder_close.png"));
		}
		else if(value instanceof DexPackageCmp)
		{
			setIcon(getIcon("package", ((DexPackageCmp) value).getState()));
			setLabel(((DexPackageCmp) value).getState());
		}
		else if(value instanceof DexStringsCmp)
		{
			setIcon(getIcon("strings", ((DexStringsCmp) value).getObj().getState()));
		}
		else if(value instanceof DexClassCmp)
		{
			DiffNode<ClassGen> diff = ((DexClassCmp) value).getObj();
			ClassGen clazz = (ClassGen) ((diff.getState().equals(DiffState.RIGHT_ONLY)) ? diff.getRight() : diff.getLeft());
			// Se la classe non è un interfaccia
			if(AccessFlag.ACC_INTERFACE.isSet(clazz.getFlags()))
			{
				setIcon(getIcon("class", diff.getState()));
			}
			else
			{
				setIcon(getIcon("interface", diff.getState()));
			}
			setLabel(diff.getState());
		}
		else if(value instanceof DexMethodCmp)
		{
			DexMethodCmp node = (DexMethodCmp) value;
			setIcon(getIcon("method", node.getObj().getState()));
			setLabel(node.getObj().getState());
		}
		else if(value instanceof DexFieldCmp)
		{
			DexFieldCmp node = (DexFieldCmp) value;
			setIcon(getIcon("field", node.getObj().getState()));
			setLabel(node.getObj().getState());
		}
		else if(value instanceof DexAnnotationCmp)
		{
			DexAnnotationCmp node = (DexAnnotationCmp) value;
			setIcon(getIcon("annotation", node.getObj().getState()));
			setLabel(node.getObj().getState());
		}
		
		return this;
	}
	
	private ImageIcon getIcon(String type, DiffState diffState)
	{
		String state = new String();
		switch(diffState)
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
		return new ImageIcon("imgs/tree/" + state + "/" + type + ".png");
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
		this.setText(this.getText() + " " + diff);
	}
}