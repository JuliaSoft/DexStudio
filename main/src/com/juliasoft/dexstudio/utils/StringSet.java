package com.juliasoft.dexstudio.utils;

import java.util.HashSet;
import java.util.SortedSet;

@SuppressWarnings("serial")
public class StringSet extends HashSet<String>
{
	public StringSet(SortedSet<String> strings)
	{
		super(strings);
	}
}
