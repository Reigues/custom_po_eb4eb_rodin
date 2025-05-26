package fr.enseeiht.rodinextension.sample.ui.manipulation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eventb.core.IEvent;
import org.eventb.ui.manipulation.IAttributeManipulation;
import org.rodinp.core.IAttributeType;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.RodinCore;
import org.rodinp.core.RodinDBException;

import fr.enseeiht.rodinextension.sample.QualProbPlugin;

public class ProbabilisticAttributeManipulation implements IAttributeManipulation {

	 public static IAttributeType.Boolean PROB_ATTRIBUTE = RodinCore
				.getBooleanAttrType(QualProbPlugin.PLUGIN_ID + ".probabilistic");
	
	public ProbabilisticAttributeManipulation() {
		// TODO Auto-generated constructor stub
	}
	
 	private IEvent asEvent(IRodinElement element) {
		assert element instanceof IEvent;
		return (IEvent) element;
	}

	@Override
	public void setDefaultValue(IRodinElement element, IProgressMonitor monitor) throws RodinDBException {
		System.out.println("===============DefaultValue=================");
		asEvent(element).setAttributeValue(PROB_ATTRIBUTE, false, monitor);
	}

	@Override
	public boolean hasValue(IRodinElement element, IProgressMonitor monitor) throws RodinDBException {
		return asEvent(element).hasAttribute(PROB_ATTRIBUTE);
	}

	@Override
	public String getValue(IRodinElement element, IProgressMonitor monitor) throws CoreException {
		return asEvent(element).getAttributeValue(PROB_ATTRIBUTE) ? "probabilistic" : "standard";
	}

	@Override
	public void setValue(IRodinElement element, String value, IProgressMonitor monitor) throws RodinDBException {
		asEvent(element).setAttributeValue(
				PROB_ATTRIBUTE, 
				switch (value) {
					case "standard": {
						yield false;
					}
					case "probabilistic": {
						yield true;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + value);
					}, 
				monitor);
	}

	@Override
	public void removeAttribute(IRodinElement element, IProgressMonitor monitor) throws RodinDBException {
		asEvent(element).removeAttribute(PROB_ATTRIBUTE, monitor);

	}

	@Override
	public String[] getPossibleValues(IRodinElement element, IProgressMonitor monitor) {
		return new String[] {"standard", "probabilistic"};
	}

}
