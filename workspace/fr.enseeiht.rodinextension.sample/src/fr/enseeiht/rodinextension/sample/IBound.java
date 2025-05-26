package fr.enseeiht.rodinextension.sample;

import org.eventb.core.ICommentedElement;
import org.eventb.core.IExpressionElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.RodinCore;

public interface IBound extends ICommentedElement, IExpressionElement {
	IInternalElementType<IBound> ELEMENT_TYPE = RodinCore
			.getInternalElementType(QualProbPlugin.PLUGIN_ID + ".bound");
}