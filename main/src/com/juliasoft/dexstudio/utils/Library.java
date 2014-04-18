package com.juliasoft.dexstudio.utils;

import com.juliasoft.amalia.dex.codegen.AccessFlag;
import com.juliasoft.amalia.dex.codegen.MethodGen;
import com.juliasoft.amalia.dex.codegen.Type;
import com.juliasoft.amalia.dex.codegen.TypeList;

public final class Library {
	public static String printType(Type type) {
		String typeName = type.getName();
		int array = 0;
		String result = "";
		while (typeName.contains("[")) {
			array++;
			typeName = typeName.substring(1, typeName.length());
		}
		switch (typeName) {
		case "V":
			result += "void";
			break;
		case "Z":
			result += "boolean";
			break;
		case "B":
			result += "byte";
			break;
		case "S":
			result += "short";
			break;
		case "C":
			result += "char";
			break;
		case "I":
			result += "int";
			break;
		case "J":
			result += "long";
			break;
		case "F":
			result += "float";
			break;
		case "D":
			result += "double";
			break;
		default:
			result += typeName.substring(typeName.lastIndexOf('/') + 1,
					typeName.indexOf(';'));
		}
		while (array-- > 0)
			result += "[]";
		return result;
	}

	public static String printSignature(MethodGen meth) {
		String result = "";
		if (!meth.isConstructor())
			result += Library.printType(meth.getPrototype().getReturnType())
					+ " " + meth.getName() + "(";
		else
			result += Library.printType(meth.getOwnerClass()) + "(";
		TypeList params = meth.getPrototype().getParameters();
		if (params != null) {
			for (Type type : meth.getPrototype().getParameters()) {
				result += Library.printType(type) + ", ";
			}
			result = result.substring(0, result.length() - 2);
		}
		result += ")";
		return result;
	}

	public static String printClassFlags(int classFlags) {
		String result = "";
		boolean separator = false;
		for (AccessFlag flag : AccessFlag.values()) {
			if (!flag.allowedForClass()
					|| flag.equals(AccessFlag.ACC_INTERFACE)
					|| (flag.equals(AccessFlag.ACC_ABSTRACT) && AccessFlag.ACC_INTERFACE
							.isSet(classFlags)))
				continue;
			else if (flag.isSet(classFlags)) {
				if (separator)
					result += " ";
				else
					separator = true;
				result += flag.getFlagName();
			}
		}
		return result;
	}

	public static String shortName(String longName) {
		char separator;
		if (longName.contains("/"))
			separator = '/';
		else if (longName.contains("."))
			separator = '.';
		else
			return longName.substring(1);
		return longName.substring(longName.lastIndexOf(separator) + 1,
				longName.indexOf(';'));
	}
}
