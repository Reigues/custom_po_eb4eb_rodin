package fr.enseeiht.eb4eb.generator.internal.ui.builder;

import org.eventb.core.IContextRoot;
import org.eventb.core.ast.Predicate;

public abstract class ContextBuilder implements IContextBuilder {
	
	private IContextRoot root;
	
	public ContextBuilder(IContextRoot root) {
		this.root = root;
	}

	protected void addCarrierSet(String name) {
		
	}
	
	protected void addConstant(String name) {

	}

	protected void addAxiom(Predicate predicate) {

	}
	
}
