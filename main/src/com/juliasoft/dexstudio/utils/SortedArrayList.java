package com.juliasoft.dexstudio.utils;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class SortedArrayList<E> extends ArrayList<E>
{
	@SuppressWarnings("unchecked")
	@Override
	public boolean add(E value)
	{
		boolean result = super.add(value);
        Comparable<E> cmp = (Comparable<E>) value;
        for (int i = size()-1; i > 0 && cmp.compareTo(get(i-1)) < 0; i--)
            Collections.swap(this, i, i-1);
        return result;
	}
}
