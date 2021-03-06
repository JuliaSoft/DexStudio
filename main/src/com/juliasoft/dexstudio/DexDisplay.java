package com.juliasoft.dexstudio;

import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.dexstudio.tab.DexTab;
import com.juliasoft.dexstudio.view.tree.DexTree;

public interface DexDisplay {
	/**
	 * Open a new tab in the tab manager. The content of tab depends on the type
	 * of the passed object.
	 * 
	 * @param tab
	 *            The object to open in the new tab. This object must be an
	 *            instance of ClassGen, MethodGen, Annotation or a Set<Strings>
	 */
	public void openNewTab(DexTab tab);

	/**
	 * Change the selected tab in the tab manager with a new one. The content of
	 * the tab depends on the type of the passed object.
	 * 
	 * @param tab
	 *            The object to open in the tab. This object must be an instance
	 *            of ClassGen, MethodGen, Annotation or a Set<Strings>
	 */
	public void changeSelectedTab(DexTab tab);

	public DexTree getTree();

	public DexGen getDexGen();
}
