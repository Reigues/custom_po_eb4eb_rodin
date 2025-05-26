package fr.enseeiht.eb4eb.generator.internal.ui.visitor;

import org.eventb.core.ISCEvent;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCMachineRoot;
import org.eventb.core.ISCVariable;
import org.eventb.core.ISCVariant;

import fr.enseeiht.eb4eb.generator.internal.ui.builder.IContextBuilder;

public interface IMachineRootVisitor<Builder extends IContextBuilder> {
	
	public void visitMachineRoot(Builder builder, ISCMachineRoot machineRoot);

	public void visitVariable(Builder builder, ISCVariable machineRoot);

	public void visitInvariant(Builder builder, ISCInvariant machineRoot);

	public void visitVariant(Builder builder, ISCVariant machineRoot);

	public void visitEvent(Builder builder, ISCEvent machineRoot);

}
