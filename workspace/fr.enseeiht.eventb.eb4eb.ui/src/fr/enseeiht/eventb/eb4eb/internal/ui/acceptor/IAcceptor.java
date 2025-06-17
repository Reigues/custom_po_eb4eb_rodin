package fr.enseeiht.eventb.eb4eb.internal.ui.acceptor;

import org.eclipse.core.runtime.CoreException;

import fr.enseeiht.eventb.eb4eb.internal.ui.builder.IBuilder;
import fr.enseeiht.eventb.eb4eb.internal.ui.visitor.IVisitor;

public interface IAcceptor<Builder extends IBuilder, Visitor extends IVisitor<Builder>> {
	public void accept(Visitor visitor, Builder builder) throws CoreException;
}
