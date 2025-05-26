/*******************************************************************************
 * Copyright (c) 2008, 2012 Systerel and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Soton - Initial API and implementation
 *     Systerel - fixed bug 3473014
 *******************************************************************************/
package ac.soton.eventb.latex.ui;

import java.text.DecimalFormat;

/**
 * @author K. Damchoom
 */
public class Bsym2Tex {

	private static final char TAB = '\t';
	private static final char NEWLINE = '\n';

	private static DecimalFormat df = new DecimalFormat("##.#");

	private static final String[] allBSym = { "\u2115", "\u2119", "\u2124",
			"\u22c2", "\u22c3", "\u2228", "\u00ac", "\u22a4", "\u22a5",
			"\u2218", "\u2a65", "\u25b7", "\u222a", "\u2229", "\u21a6",
			"\u2192", "\u2284", "\u2288", "\u2209", "\u21d4", "\u21d2",
			"\u2227", "\u2200", "\u2203", "\u2260", "\u2264", "\u2265",
			"\u2282", "\u2286", "\ue102", "\ue100", "\ue101", "\u2194",
			"\u2916", "\u21f8", "\u2914", "\u21a3", "\u2900", "\u21a0",
			"\u2205", "\u2216", "\u00d7", "\ue103", "\u2297", "\u2225",
			"\u223c", "\u2a64", "\u25c1", "\u03bb", "\u2025", "\u00b7",
			"\u2212", "\u2217", "\u00f7", "\u2254", ":\u2208", ":\u2223",
			"\u2208", "\u2223", "{", "}", "_", "%", "#", ">", "<" };
	/* This mapping table is based on bsymb.sty version 1.7 */
	private static final String[] allTexSym = { "\\nat", "\\pow", "\\intg",
			"\\Inter", "\\Union", "\\lor", "\\lnot", "\\btrue", "\\bfalse",
			"\\circ", "\\ransub", "\\ranres", "\\bunion", "\\binter",
			"\\mapsto", "\\tfun", "\\nsubset", "\\nsubseteq", "\\notin",
			"\\leqv", "\\limp", "\\land", "\\forall", "\\exists", "\\neq",
			"\\leq", "\\geq", "\\subset", "\\subseteq", "\\strel", "\\trel",
			"\\srel", "\\rel", "\\tbij", "\\pfun", "\\pinj", "\\tinj",
			"\\psur", "\\tsur", "\\emptyset", "\\setminus", "\\cprod", "\\ovl",
			"\\dprod", "\\pprod", "^{-1}", "\\domsub", "\\domres", "\\lambda",
			"\\upto", "\\qdot", "-", "*", "/", ":=", ":\\in", ":|", "\\in",
			"|", "\\{", "\\}", "\\_", "\\%", "\\#", ">", "<" };


	/*
	 * Convert B specs to latex forms i.e. axioms, theorems, invariants,
	 * variants, guards, actions
	 */
	public static String texConvert(String str, int level, int offset) {
		String s = "";
		double sp = (offset + 2) * 0.2;
		int csp = 0;
		int newline = 0;

		int j;
		for (int i = 0; i < str.length(); i++) {

			if (str.charAt(i) == '$') {
				continue;
			}

			if (str.charAt(i) == NEWLINE && newline == 0) {
				newline = 1;
				csp = 0;
				sp = (offset + 2) * 0.2;
			}
			if (newline == 1 && str.charAt(i) != ' ' && str.charAt(i) != '\t'
					&& str.charAt(i) != NEWLINE) {
				sp = sp + csp * 0.2;
				s = s + "\t\t\\\\\\hspace*{" + df.format(sp) + " cm}  ";
				newline = 0;
			}
			if (newline == 1 && str.charAt(i) == ' ') {
				csp = csp + 1;
				continue;
			}
			if (newline == 1 && str.charAt(i) == TAB) {
				csp = csp + 3;
				continue;
			}

			j = isSym("" + str.charAt(i));
			if (j != -1 && str.charAt(i) != ' ') {
				s = s + allTexSym[j];
				if (!(str.charAt(i) == '\u2119' || str.charAt(i) == '\u2115' || str
						.charAt(i) == '_'))
					s = s + " ";
				continue;
			}
			if (i > 0
					&& str.charAt(i) == '\u0031'
					&& (str.charAt(i - 1) == '\u2119' || str.charAt(i - 1) == '\u2115'))
				s = s + "_1";
			else
				s = s + str.charAt(i);
		}// end for
		return s;
	}

	/*
	 * Converting label to latex form
	 */
	public static String labelConvert(String str) {
		final StringBuilder s = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '$') {
				continue;
			} else if (str.charAt(i) == ' ') {
				s.append("~");
			} else if (str.charAt(i) == '%') {
				s.append("\\%");
			} else if (str.charAt(i) == '_') {
				s.append("\\_");
			} else if (str.charAt(i) == '#') {
				s.append("\\#");
			} else
				s.append(str.charAt(i));
		}
		return s.toString();
	}

	/*
	 * Converting comment into latex format
	 */
	public static String cmtConvert(String str, int level, boolean isback) {
		final StringBuilder s = new StringBuilder();
		s.append("\n");
		appendTabs(s, level);
		if (isback) {
			s.append("\\bcmt{ ");
		} else {
			s.append("\\cmt{ ");
		}
		int j = 0;
		for (int i = 0; i < str.length(); i++) {
			j = isSym("" + str.charAt(i));
			if (j != -1) { // if it is a symbol
				s.append('$');
				s.append(allTexSym[j]);
				s.append('$');
				if (!(str.charAt(i) == '\u2119' || str.charAt(i) == '\u2115' || str
						.charAt(i) == '_')) {
					s.append(" ");
				}
				continue;
			}
			if (i > 0 && str.charAt(i) == '\u0031'
					&& (str.charAt(i - 1) == '\u2119' //
					|| str.charAt(i - 1) == '\u2115')) {
				s.append("_1");
			} else {
				s.append(str.charAt(i));
			}
			if (str.charAt(i) == NEWLINE) {
				appendTabs(s, level);
				s.append("\\newline ");
				continue;
			}
		}// end for
		s.append(" }");
		return s.toString();
	}

	private static void appendTabs(StringBuilder s, int level) {
		for (int i = 0; i < level; i++) {
			s.append("\t");
		}
	}

	/*
	 * to check whether the parameter sm is a symbol or not
	 */
	private static int isSym(String sm) {
		for (int i = 0; i < allBSym.length; i++)
			if (allBSym[i].equals(sm)) {
				return i;
			}
		return -1; // is not a symbol
	}
}