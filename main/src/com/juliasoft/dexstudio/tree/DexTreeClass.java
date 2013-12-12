package com.juliasoft.dexstudio.tree;
import com.juliasoft.amalia.dex.codegen.ClassGen;
import com.juliasoft.dexstudio.utils.Library;

@SuppressWarnings("serial")
public class DexTreeClass extends DexTreeNode
{
	
    public DexTreeClass(ClassGen clazz)
    {
        this.setUserObject(clazz);
    }
    
    @Override
    public String toString()
    {
        ClassGen clazz = (ClassGen) this.getUserObject();
        String className = clazz.getType().getName().replace("/", ".");
        return Library.shortName(className);
    }
}
