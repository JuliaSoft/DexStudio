package com.juliasoft.dexstudio.utils;

import java.util.Collection;
import java.util.HashSet;

import com.juliasoft.amalia.dex.codegen.Annotation;

/**
 * Editor and renderer for AnnonationCell objects
 * 
 * 
 * @author Eugenio Ancona
 * 
 */
@SuppressWarnings("serial")
public class AnnotationSet extends HashSet<Annotation> {
	public AnnotationSet(Collection<Annotation> set) {
		super(set);
	}
}
