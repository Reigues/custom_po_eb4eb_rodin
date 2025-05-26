/*******************************************************************************
 * Copyright (c) 2009, 2012 Systerel and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Soton - Initial API and implementation
 *******************************************************************************/
package ac.soton.eventb.latex.ui;

import static ac.soton.eventb.latex.ui.B2LatexUIPlugin.DEBUG;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eventb.core.EventBPlugin;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinFile;

public class LatexUI implements IObjectActionDelegate {

	protected static final Calendar cal = new GregorianCalendar();

	// Get the components of the date
	int year = cal.get(Calendar.YEAR); // 2002
	int month = cal.get(Calendar.MONTH); // 0=Jan, 1=Feb, ...
	int day = cal.get(Calendar.DAY_OF_MONTH); // 1...

	private String gendate() {
		String st = "_" + day + "_";
		if (month == 0)
			st = st + " Jan";
		else if (month == 1)
			st = st + "Feb";
		else if (month == 2)
			st = st + "Mar";
		else if (month == 3)
			st = st + "Apr";
		else if (month == 4)
			st = st + "May";
		else if (month == 5)
			st = st + "Jun";
		else if (month == 6)
			st = st + "Jul";
		else if (month == 7)
			st = st + "Aug";
		else if (month == 8)
			st = st + "Sep";
		else if (month == 9)
			st = st + "Oct";
		else if (month == 10)
			st = st + "Nov";
		else if (month == 11)
			st = st + "Dec";
		st = st + "_" + year;
		return st;
	}

	/*
	 * construct action
	 */
	public LatexUI() {
		super();
	}

	public void run(IAction action) {
		final IRodinFile rodinFile = getSelectedComponent();
		if (DEBUG) {
			System.out.println("rodinFile =" + rodinFile);
		}
		if (rodinFile == null) {
			return;
		}
		final IInternalElement root = rodinFile.getRoot();
		final String texFileName = rodinFile.getBareName().toString()
				+ gendate() + ".tex";
		final String latexDirLocation = rodinFile.getResource()
				.getRawLocation().toString();
		if (DEBUG) {
			System.out.println("root =" + root);
		}
		String temp;
		if (File.separator.equals("\\")) {
			temp = "";
		} else if (File.separator.equals("/")) {
			temp = "/";
		} else if (File.separator.equals(":")) {
			temp = "::";
		} else
			temp = "";
		final StringTokenizer st = new StringTokenizer(latexDirLocation, "/");
		int tokens = st.countTokens();
		int tokcounter = 0;
		while (tokcounter < tokens - 1) {
			temp = temp + st.nextToken() + File.separatorChar;
			tokcounter = tokcounter + 1;
		}
		temp = temp + "latex";
		final File f1 = new File(temp);
		f1.mkdir();
		final String writeFile = temp + File.separatorChar + texFileName;
		final BTexConverter btexConverter = new BTexConverter();
		final String text = btexConverter.getText(root);

		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(
					writeFile));
			out.write(text);
			out.close();
		} catch (IOException e) {
			return; // do nothing
		}

		/*
		 * Writing b2latex sty file to the latex folder
		 */
		final String styFile = temp + File.separatorChar + "b2latex.sty";
		final File outF = new File(styFile);
		final String old = temp + File.separatorChar + "b2latexOLD.sty";
		final File oldF = new File(old);
		// rename if the old version exists
		if (!oldF.exists() && outF.exists()) {
			outF.renameTo(oldF);
		}
		// writing new b2latex.sty
		if (!outF.exists()) {
			final String contents = getStyContents();
			try {
				final BufferedWriter output = new BufferedWriter(
						new FileWriter(styFile));
				output.write(contents);
				output.close();
			} catch (IOException e) {
				System.out.println("Error in Writing b2latex.sty" + e);
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	/*
	 * Get the selected component.
	 */
	private ISelection selection;

	private IRodinFile getSelectedComponent() {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() == 1) {
				return EventBPlugin.asEventBFile(ssel.getFirstElement());
			}
		}
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	private String getStyContents() {
		String sty = ""
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% This is b2latex.sty, generated by B2Latex plugin on Rodin"
				+ "\n"
				+ "% Author: K. DAMCHOOM, kd06r@ecs.soton.ac.uk"
				+ "\n"
				+ "% Copyright(c)2008 ECS @ University of Southampton"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% This style file is required for all latex files generated by the"
				+ "\n"
				+ "% B2Latex plugin 0.5.x."
				+ "\n"
				+ "% Each command mentioned below, can be modified for your own style"
				+ "\n"
				+ "% but command names are not allowed."
				+ "\n"
				+ "%"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% Defined colors."
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\\usepackage{color}"
				+ "\n"
				+ "\\definecolor{keycolor}{rgb}{0,0,0.7}"
				+ "  %color of key words, blue is the default"
				+ "\n"
				+ "\\definecolor{labelcolor}{rgb}{0,0.4,0.8}"
				+ "  %color of labels, cyan"
				+ "\n"
				+ "\\definecolor{codecolor}{rgb}{0,0.2,0}"
				+ "  %color of formulas, black"
				+ "\n"
				+ "\\definecolor{xcodecolor}{rgb}{0,0,0}"
				+ "  %color of extended formulas, black"
				+ "\n"
				+ "\\definecolor{cmtcolor}{rgb}{0,0,0}"
				+ "  %color of comments, black"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\\newcommand{\\paraSpc} {\\hspace*{\\fill} \\setlength{\\parskip}{-2pt}} % reduce space between paras"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\\newcommand{\\itemSpc} {\\setlength{\\itemsep}{-0pt}} % reduce space between para items"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% Special commands for MACHINE "
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\\newcommand{\\MACHINE}[1] { \\item[\\textcolor{keycolor}{MACHINE }] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\REFINES}[1]{ \\item[\\textcolor{keycolor}{REFINES }] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\SEES}[1]{ \\item[\\textcolor{keycolor}{SEES }] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\VARIABLES}{\\item[\\textcolor{keycolor}{VARIABLES}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\INVARIANTS}{  \\item[\\textcolor{keycolor}{INVARIANTS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\VARIANT}{ \\item[\\textcolor{keycolor}{VARIANT}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\EVENTS}{  \\item[\\textcolor{keycolor}{EVENTS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\INITIALISATION}{  \\item[\\textcolor{keycolor}{Initialisation}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\EVT}[1]{  \\item[\\textcolor{keycolor}{Event }] \\textit{#1} $\\defi$ \\paraSpc }"
				+ " % opt1\n"
				+ "% Option: deactivate above command and reactivate "
				+ "\n"
				+ "% the command below to display only event name"
				+ "\n"
				+ "%\\newcommand{\\EVT}[1]{  \\item[\\textit{#1}] $\\defi$ \\paraSpc }"
				+ " % opt2\n"
				+ "\n"
				+ "\\newcommand{\\REF}[1]{ \\item[\\textcolor{keycolor}{refines}] \\textit{#1} \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\EXTD}[1]{ \\item[\\textcolor{keycolor}{extends}] \\textit{#1} \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\Status}[1]{ \\item[\\textcolor{keycolor}{Status}] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\AnyPrm}{ \\item[\\textcolor{keycolor}{any}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\WhereGrd}{ \\item[\\textcolor{keycolor}{where}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\WhenGrd}{ \\item[\\textcolor{keycolor}{when}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\Witnesses}{ \\item[\\textcolor{keycolor}{with}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\ThenAct}{ \\item[\\textcolor{keycolor}{then}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\BeginAct}{ \\item[\\textcolor{keycolor}{begin}] \\paraSpc \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\EndAct}{ \\item[\\textcolor{keycolor}{end}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% Special commands for CONTEXT "
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\CONTEXT}[1]{  \\item[\\textcolor{keycolor}{CONTEXT }] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\EXTENDS}[1]{ \\item[\\textcolor{keycolor}{EXTENDS }] #1 \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\SETS}{ \\item[\\textcolor{keycolor}{SETS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\CONSTANTS}{ \\item[\\textcolor{keycolor}{CONSTANTS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\THEOREMS}{ \\item[\\textcolor{keycolor}{THEOREMS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "\\newcommand{\\AXIOMS}{ \\item[\\textcolor{keycolor}{AXIOMS}] \\paraSpc }"
				+ "\n"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "% General commands used in both machines and contexts"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n"
				+ "\n"
				+ "% Specification Title"
				+ "\n"
				+ "\\newcommand{\\BTitle}[3]{"
				+ "\n"
				+ "\\item[\\fbox{\\parbox{6in}{\\centering{~\\\\An Event-B Specification of #1"
				+ "\n"
				+ "\\\\Creation Date: #2 @ #3\\\\}}}]"
				+ "\n"
				+ "}\n"
				+ "% End of MACHINE or CONTEXT"
				+ "\n"
				+ "\\newcommand{\\END}{ \\item[\\textcolor{keycolor}{END}] \\hspace*{\\fill} }"
				+ "\n"
				+ "\n"
				+ "% Printing non-labelled elements"
				+ "\n"
				+ "% (i.e. variables, sets and constants)"
				+ "\n"
				+ "\\newcommand{\\Item}[1]{ \\item[\\textcolor{codecolor}{$\\tt#1$}] \\itemSpc }"
				+ "\n"
				+ "\\newcommand{\\ItemX}[1]{ \\item[\\textcolor{xcodecolor}{$\\it#1$}] \\itemSpc }"
				+ "\n"
				+ "\n"
				+ "% Printing an element with label name"
				+ "\n"
				+ "% (i.e. invariants, theorems, axioms, witnesses and actions)"
				+ "\n"
				+ "% parameter #1 is a label name and #2 is its content"
				+ "\n"
				+ "\\newcommand{\\nItem}[2]{ \\item[\\textcolor{labelcolor}{$\\tt #1:$}] \\textcolor{codecolor}{$\\tt #2$} \\itemSpc }"
				+ " % opt1\n"
				+ "\\newcommand{\\nItemX}[2]{ \\item[\\textcolor{labelcolor}{$\\tt #1:$}] \\textcolor{xcodecolor}{$\\it #2$} \\itemSpc }"
				+ " % opt1\n"
				+ "% Below is an alternative command for printing with no label"
				+ "\n"
				+ "%\\newcommand{\\nItem}[2]{ \\item[] $#2$}"
				+ " % opt2\n"
				+ "\n"
				+ "% Commentations, reselect the second option for displaying asterisks"
				+ "\n"
				+ "% - Newline comment "
				+ "\n"
				+ "\\newcommand{\\cmt}[1]{ \\hspace*{\\fill}\\\\\\textcolor{cmtcolor}{ #1}}"
				+ " %opt1 \n"
				+ "%\\newcommand{\\cmt}[1]{ \\hspace*{\\fill}\\\\ $/\\ast$ #1 $\\ast/$}"
				+ " % opt2\n"
				+ "\n"
				+ "% - Back Comment, for variables, sets and constants"
				+ "\n"
				+ "\\newcommand{\\bcmt}[1]{~~~ \\textcolor{cmtcolor}{#1}}"
				+ " %opt1 \n"
				+ "%\\newcommand{\\bcmt}[1]{~~~ $/\\ast$ #1 $\\ast/$}"
				+ " %opt2\n"
				+ "% Select opt3 or opt4 (including asterisks) if you want to display this kind of comments"
				+ "\n"
				+ "% on the newline like the newline comment"
				+ "\n"
				+ "%\\newcommand{\\bcmt}[1]{ \\hspace*{\\fill}\\\\ #1}"
				+ " %opt3 \n"
				+ "%\\newcommand{\\bcmt}[1]{ \\hspace*{\\fill}\\\\ $/\\ast$ #1 $\\ast/$}"
				+ " % opt4\n"
				+ "\n"
				+ "%-------------------------------------------------------------------"
				+ "\n";
		return sty;
	}
}
