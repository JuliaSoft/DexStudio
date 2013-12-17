package com.juliasoft.dexstudio;

public interface DexVisualizable
{
	/**
	 * Open a new tab in the tab manager. The content of tab depends on the type
	 * of the passed object.
	 * 
	 * @param obj
	 *            The object to open in the new tab. This object must be an
	 *            instance of ClassGen, MethodGen, Annotation or a Set<Strings>
	 */
	public void openNewTab(Object obj);
	
	/**
	 * Change the selected tab in the tab manager with a new one. The content of
	 * the tab depends on the type of the passed object.
	 * 
	 * @param obj
	 *            The object to open in the tab. This object must be an instance
	 *            of ClassGen, MethodGen, Annotation or a Set<Strings>
	 */
	public void changeSelectedTab(Object obj);
}
