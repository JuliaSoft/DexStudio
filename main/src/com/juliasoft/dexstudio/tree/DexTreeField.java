package com.juliasoft.dexstudio.tree;

import com.juliasoft.amalia.dex.codegen.FieldGen;

@SuppressWarnings("serial")
public class DexTreeField extends DexTreeNode
{
	
    public DexTreeField(FieldGen field)
    {
        this.setUserObject(field);
    }
    
    @Override
    public String toString()
    {
        FieldGen field = (FieldGen) this.getUserObject();
        return field.getName();
    }
}
