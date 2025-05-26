package fr.enseeiht.rodinextension.sample.basis;

import org.eventb.core.basis.EventBElement;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.IRodinElement;

import fr.enseeiht.rodinextension.sample.IBound;

public class Bound extends EventBElement implements IBound {

	public Bound(String name, IRodinElement parent) {
		super(name, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IInternalElementType<? extends IInternalElement> getElementType() {
		return IBound.ELEMENT_TYPE;
	}

}
