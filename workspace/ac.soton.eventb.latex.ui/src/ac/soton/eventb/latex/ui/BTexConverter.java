/*******************************************************************************
 * Copyright (c) 2008, 2012 Systerel and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Soton - Initial API and implementation
 *******************************************************************************/
package ac.soton.eventb.latex.ui;

import static ac.soton.eventb.latex.ui.Bsym2Tex.labelConvert;
import static ac.soton.eventb.latex.ui.LatexUI.cal;

import java.util.Calendar;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.IAction;
import org.eventb.core.IAxiom;
import org.eventb.core.ICarrierSet;
import org.eventb.core.ICommentedElement;
import org.eventb.core.IConstant;
import org.eventb.core.IContextRoot;
import org.eventb.core.IConvergenceElement.Convergence;
import org.eventb.core.IEvent;
import org.eventb.core.IExtendsContext;
import org.eventb.core.IGuard;
import org.eventb.core.IInvariant;
import org.eventb.core.IMachineRoot;
import org.eventb.core.IParameter;
import org.eventb.core.IRefinesEvent;
import org.eventb.core.IRefinesMachine;
import org.eventb.core.ISeesContext;
import org.eventb.core.IVariable;
import org.eventb.core.IVariant;
import org.eventb.core.IWitness;
import org.eventb.core.basis.SeesContext;
import org.eventb.internal.ui.UIUtils;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.RodinDBException;

/**
 * @author htson
 * @author Markus Gaisbauer
 * @author K.Damchoom
 */
@SuppressWarnings("restriction")
public class BTexConverter {

	// The content string of the form text
	private StringBuilder formString;
	// This local object, toTex, is used to convert B notations into LaTeX forms
	private String mcn = "";

	public BTexConverter() {
		formString = new StringBuilder("");
		formString.setLength(0);
	}

	// Get the components of the date
	int year = cal.get(Calendar.YEAR); // 2007
	int month = cal.get(Calendar.MONTH); // 0=Jan, 1=Feb, ...
	int day = cal.get(Calendar.DAY_OF_MONTH); // 1...

	int hour12 = cal.get(Calendar.HOUR); // 0..11
	int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0..23
	int min = cal.get(Calendar.MINUTE); // 0..59
	int sec = cal.get(Calendar.SECOND); // 0..59
	int ampm = cal.get(Calendar.AM_PM); // 0=AM, 1=PM

	private String gtime() {
		String st = "";
		if (hour12 < 10)
			st = st + "0" + hour12;
		else
			st = st + hour12;
		if (min < 10)
			st = st + ":0" + min;
		else
			st = st + ":" + min;
		if (sec < 10)
			st = st + ":0" + sec;
		else
			st = st + ":" + sec;
		if (ampm == 0)
			st = st + " AM";
		else
			st = st + " PM";
		return st;
	}

	private String gdate() {
		String st = "" + day;
		if (month == 0)
			st = st + " Jan";
		else if (month == 1)
			st = st + " Feb";
		else if (month == 2)
			st = st + " Mar";
		else if (month == 3)
			st = st + " Apr";
		else if (month == 4)
			st = st + " May";
		else if (month == 5)
			st = st + " Jun";
		else if (month == 6)
			st = st + " Jul";
		else if (month == 7)
			st = st + " Aug";
		else if (month == 8)
			st = st + " Sep";
		else if (month == 9)
			st = st + " Oct";
		else if (month == 10)
			st = st + " Nov";
		else if (month == 11)
			st = st + " Dec";
		st = st + " " + year;
		return st;
	}

	private void appendHeader() {
		beginLevel0();
		append("\\documentclass[10pt,a4paper]{report}");
		endLevel();
		beginLevel0();
		append("\\usepackage[top=3cm, bottom=2.5cm, left=3cm, right=2.5cm] {geometry}");
		endLevel();
		beginLevel0();
		append("\\usepackage {bsymb,b2latex}");
		endLevel();

		beginLevel0();
		append("\\usepackage{fancyhdr,lastpage,color}");
		endLevel();
		beginLevel0();
		append("\\lhead{\\rm An Event-B Specification of " + labelConvert(mcn)
				+ "}");
		endLevel();
		beginLevel0();
		append("\\rhead {\\rm Page \\thepage~of \\pageref{LastPage}}");
		endLevel();
		beginLevel0();
		append("\\lfoot{}\\cfoot{}\\rfoot{}");
		endLevel();
		beginLevel0();
		append("\\pagestyle{fancy}");
		endLevel();
		beginLevel0();
		append("%---------------------------------------------------------");
		endLevel();
		beginLevel0();
		append("\\begin{document}");
		endLevel();
		beginLevel0();
		append("\\thispagestyle{empty}");
		endLevel();
		beginLevel0();
		append("\\begin{description}");
		endLevel();
		beginLevel0();
		append("\\BTitle{" + labelConvert(mcn) + "}{" + gdate() + "}{"
				+ gtime() + "}");
		endLevel();
	}

	public void appendEnding() {
		beginLevel0();
		append("\\END");
		endLevel();
		beginLevel0();
		append("\\end{description}");
		endLevel();
		beginLevel0();
		append("\\end{document}");
		endLevel();
	}

	public String getText(IInternalElement root) {
		// IRodinFile rodinFile=root.getRodinFile();
		mcn = root.getRodinFile().getBareName();
		appendHeader();
		addComponentName(root);
		addComponentDependencies(root);
		if (root instanceof IMachineRoot) {
			final IMachineRoot mch = (IMachineRoot) root;
			addVariables(mch);
			addInvariants(mch);
			addEvents(mch);
			addVariant(mch);
		} else if (root instanceof IContextRoot) {
			final IContextRoot ctx = (IContextRoot) root;
			addCarrierSets(ctx);
			addConstants(ctx);
			addAxioms(ctx);
		}
		appendEnding();
		return formString.toString();
	}

	private void addComponentName(IInternalElement root) {
		// Print the Machine/Context name
		beginLevel0();
		if (root instanceof IMachineRoot) {
			append("\\MACHINE{");
		} else if (root instanceof IContextRoot) {
			append("\\CONTEXT{");
		}
		final String bareName = root.getRodinFile().getBareName();
		append(labelConvert(bareName) + "}");
		if (root instanceof ICommentedElement) {
			addComment((ICommentedElement) root, 0, false);
		}
		endLevel();
		return;
	}

	private void addComponentDependencies(IInternalElement root) {
		if (root instanceof IMachineRoot) {
			IRodinElement[] refines;
			try {
				refines = root.getChildrenOfType(IRefinesMachine.ELEMENT_TYPE);
				if (refines.length != 0) {
					IRefinesMachine refine = (IRefinesMachine) refines[0];
					String name = refine.getAbstractMachineName();
					emptyLine();
					beginLevel0();
					append("\\REFINES{");
					append(labelConvert(name) + "}");
					endLevel();
				}
			} catch (RodinDBException e) {
				return;
			}

		} else if (root instanceof IContextRoot) {
			// EXTENDS clause
			IRodinElement[] extendz;
			try {
				extendz = root.getChildrenOfType(IExtendsContext.ELEMENT_TYPE);
				if (extendz.length != 0) {
					IExtendsContext extend = (IExtendsContext) extendz[0];
					String name = extend.getAbstractContextName();
					emptyLine();
					beginLevel0();
					append("\\EXTENDS{");
					append(labelConvert(name) + "}");
					endLevel();
				}
			} catch (RodinDBException e) {
				return;
			}
		}

		// SEES clause for both context and machine
		IRodinElement[] seeContexts;
		try {
			seeContexts = root.getChildrenOfType(ISeesContext.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}

		int length = seeContexts.length;
		if (length != 0) {
			emptyLine();
			beginLevel0();
			append("\\SEES{");
			for (int i = 0; i < length; i++) {
				try {
					if (i != 0)
						append(", ");
					append(labelConvert(((SeesContext) seeContexts[i])
							.getSeenContextName()));
				} catch (RodinDBException e) {
					continue;
				}
			}
			append("}");
			endLevel();
		}
	}

	private void addVariables(IMachineRoot root) {
		IVariable[] vars;
		try {
			vars = root.getChildrenOfType(IVariable.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (vars.length != 0) {
			beginLevel0();
			append("\\VARIABLES");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (IVariable var : vars) {
				beginLevel2();
				try {
					append("\\Item{ " + labelConvert(var.getIdentifierString())
							+ " }");

					addComment(var, 2, true);

				} catch (RodinDBException e) {
					continue;
				}
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addInvariants(IMachineRoot root) {
		IInvariant[] invs;
		try {
			invs = root.getChildrenOfType(IInvariant.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (invs.length != 0) {
			beginLevel0();
			append("\\INVARIANTS");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (IInvariant inv : invs) {
				beginLevel2();
				try {
					append("\\nItemX{ " + labelConvert(inv.getLabel()) + " }{ ");
					append(Bsym2Tex.texConvert(inv.getPredicateString(), 1, inv
							.getLabel().length())
							+ " }");

					addComment(inv, 2, false);

				} catch (RodinDBException e) {
					continue;
				}
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addCarrierSets(IContextRoot root) {
		ICarrierSet[] sets;
		try {
			sets = root.getChildrenOfType(ICarrierSet.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (sets.length != 0) {
			beginLevel0();
			append("\\SETS");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (ICarrierSet set : sets) {
				beginLevel2();
				try {
					append("\\Item{ " + labelConvert(set.getIdentifierString())
							+ " }");

					addComment(set, 2, true);

				} catch (RodinDBException e) {
					continue;
				}
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addConstants(IContextRoot root) {
		IConstant[] csts;
		try {
			csts = root.getChildrenOfType(IConstant.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (csts.length != 0) {
			beginLevel0();
			append("\\CONSTANTS");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (IConstant cst : csts) {
				beginLevel2();
				try {
					append("\\Item{ " + labelConvert(cst.getIdentifierString())
							+ " }");

					addComment(cst, 2, true);

				} catch (RodinDBException e) {
					continue;
				}
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addAxioms(IContextRoot root) {
		IAxiom[] axms;
		try {
			axms = root.getChildrenOfType(IAxiom.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (axms.length != 0) {
			beginLevel0();
			append("\\AXIOMS");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (IAxiom axm : axms) {
				beginLevel2();
				try {
					append("\\nItemX{ " + labelConvert(axm.getLabel()));
					append(" }{ "
							+ Bsym2Tex.texConvert(axm.getPredicateString(), 1,
									axm.getLabel().length()) + " }");

					addComment(axm, 2, false);

				} catch (RodinDBException e) {
					continue;
				}
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addEvents(IMachineRoot root) {
		IEvent[] evts;
		try {
			evts = root.getChildrenOfType(IEvent.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}

		if (evts.length != 0) {
			beginLevel0();
			append("\\EVENTS");
			endLevel();
			for (IEvent evt : evts) {
				try {
					beginLevel1();
					if (!evt.getLabel().equals("INITIALISATION"))
						append("\\EVT {" + labelConvert(evt.getLabel()) + "}");
					else {
						append("\\INITIALISATION");
						try {
							if (evt.isExtended()) {
								endLevel();
								beginLevel2();
								append("\\\\\\textit{extended}");
								// endLevel();
							}
						} catch (RodinDBException e) {
							// EventBUIExceptionHandler.handleGetAttributeException(e);
							continue;
						}
					}

				} catch (RodinDBException e) {
					continue;
				}

				addComment(evt, 1, false);

				endLevel();
				List<IParameter> params;
				List<IGuard> guards;
				List<IAction> actions;
				IRefinesEvent[] refinesEvents;
				IWitness[] witnesses;

				try {
					refinesEvents = evt
							.getChildrenOfType(IRefinesEvent.ELEMENT_TYPE);
					params = UIUtils.getVisibleChildrenOfType(evt,
							IParameter.ELEMENT_TYPE);
					guards = UIUtils.getVisibleChildrenOfType(evt,
							IGuard.ELEMENT_TYPE);
					witnesses = evt.getChildrenOfType(IWitness.ELEMENT_TYPE);
					actions = UIUtils.getVisibleChildrenOfType(evt,
							IAction.ELEMENT_TYPE);
				} catch (RodinDBException e) {
					continue;
				}

				try {
					Convergence convergence = evt.getConvergence();
					if (convergence == Convergence.ANTICIPATED) {
						beginLevel2();
						append("\\Status{anticipated}");
						endLevel();
					} else if (convergence == Convergence.CONVERGENT) {
						beginLevel2();
						append("\\Status{convergent}");
						endLevel();
					}
				} catch (CoreException e) {
					// Do nothing
				}

				if (refinesEvents.length != 0) {
					beginLevel1();
					try {
						if (evt.isExtended()) {
							append("\\EXTD {");
						} else
							append("\\REF {");

					} catch (RodinDBException e) {
						// EventBUIExceptionHandler.handleGetAttributeException(e);
						continue;
					}

					int num_abs_evt = 0;
					for (IRefinesEvent refinesEvent : refinesEvents) {
						if (num_abs_evt > 0)
							append(", ");
						try {
							append(labelConvert(refinesEvent
									.getAbstractEventLabel()));
							num_abs_evt++;
						} catch (RodinDBException e) {
							// continue;
						}
					}
					append("}");
					endLevel();
				}

				beginLevel2();
				append("\\begin{description}");
				endLevel();

				if (params.size() != 0) {
					beginLevel2();
					append("\\AnyPrm");
					endLevel();
					beginLevel3();
					append("\\begin{description}");
					endLevel();
					for (IParameter param : params) {
						beginLevel3();
						try {
							if (param.getParent().equals(evt))
								append("\\ItemX{ ");
							else
								append("\\Item{ ");
							append(labelConvert(param.getIdentifierString())
									+ " }");
							addComment(param, 3, true);

						} catch (RodinDBException e) {
							continue;
						}
						endLevel();
					}
					beginLevel3();
					append("\\end{description}");
					endLevel();
					beginLevel2();
					append("\\WhereGrd");
					endLevel();
					beginLevel3();
					append("\\begin{description}");
					endLevel();

				} else {
					beginLevel2();
					if (guards.size() != 0) {
						append("\\WhenGrd");
						endLevel();
						beginLevel3();
						append("\\begin{description}");
						endLevel();

					} else {// if (actions.size() != 0) {
						append("\\BeginAct");
						endLevel();
						beginLevel3();
						append("\\begin{description}");
						endLevel();
					}
				}

				if (guards.size() != 0) {
					for (IGuard guard : guards) {
						beginLevel3();
						try {
							if (guard.getParent().equals(evt))
								append("\\nItemX{ ");
							else
								append("\\nItem{ ");
							append(labelConvert(guard.getLabel())
									+ " }{ "
									+ Bsym2Tex.texConvert(
											guard.getPredicateString(), 2,
											guard.getLabel().length()) + " }");

							addComment(guard, 3, false);

						} catch (RodinDBException e) {
							continue;
						}
						endLevel();
					}
					beginLevel3();
					append("\\end{description}");
					endLevel();
				}

				if (witnesses.length != 0) {
					beginLevel2();
					append("\\Witnesses");
					endLevel();
					beginLevel3();
					append("\\begin{description}");
					endLevel();
					for (IWitness witness : witnesses) {
						beginLevel3();
						try {
							append("\\nItem{ "
									+ labelConvert(witness.getLabel())
									+ " }{ "
									+ Bsym2Tex.texConvert(
											witness.getPredicateString(), 2,
											witness.getLabel().length()) + " }");

							addComment(witness, 3, false);

						} catch (RodinDBException e) {
							continue;
						}
						endLevel();
					}
					beginLevel3();
					append("\\end{description}");
					endLevel();
				}

				if (guards.size() != 0) {
					beginLevel2();
					append("\\ThenAct");
					endLevel();
					beginLevel3();
					append("\\begin{description}");
					endLevel();
				}

				if (actions.size() == 0) {
					beginLevel3();
					append("\\Item{ skip }");
					endLevel();
					beginLevel3();
					append("\\end{description}");
					endLevel();
				} else {
					if (actions.size() != 0) {
						for (IAction action : actions) {
							beginLevel3();
							try {
								if (action.getParent().equals(evt))
									append("\\nItemX{ ");
								else
									append("\\nItem{ ");
								append(labelConvert(action.getLabel())
										+ " }{ "
										+ Bsym2Tex.texConvert(
												action.getAssignmentString(),
												2, action.getLabel().length())
										+ " }");

								addComment(action, 3, false);

							} catch (RodinDBException e) {
								continue;
							}
							endLevel();
						}
						beginLevel3();
						append("\\end{description}");
						endLevel();
					}
				}
				beginLevel2();
				append("\\EndAct");
				endLevel();
				beginLevel2();
				append("\\end{description}");
				endLevel();
			}
		}
	}

	private void addVariant(IMachineRoot root) {
		IVariant[] variants;
		try {
			variants = root.getChildrenOfType(IVariant.ELEMENT_TYPE);
		} catch (RodinDBException e) {
			return;
		}
		if (variants.length != 0) {
			beginLevel0();
			append("\\VARIANT");
			endLevel();
			beginLevel1();
			append("\\begin{description}");
			endLevel();
			for (IVariant variant : variants) {
				beginLevel1();
				try {
					append("\\Item{ "
							+ Bsym2Tex.texConvert(
									variant.getExpressionString(), 1, 0) + " }");
				} catch (RodinDBException e) {
					continue;
				}
				addComment(variant, 2, false);
				endLevel();
			}
			beginLevel1();
			append("\\end{description}");
			endLevel();
		}
	}

	private void addComment(ICommentedElement element, int level, boolean isback) {
		try {
			if (element.hasComment()) {
				String comment = element.getComment();
				if (comment.length() != 0) {
					append(Bsym2Tex.cmtConvert(comment, level, isback));
				}
			}
		} catch (RodinDBException e) {
			// ignore
		}
	}

	private void beginLevel0() {
		formString.append("");
	}

	private void beginLevel1() {
		formString.append("\t");
	}

	private void beginLevel2() {
		formString.append("\t\t");
	}

	private void beginLevel3() {
		formString.append("\t\t\t");
	}

	private void endLevel() {
		formString.append("\n");
	}

	private void emptyLine() {
		formString.append("");
	}

	private void append(String s) {
		formString.append(s);
	}

}