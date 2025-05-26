package fr.enseeiht.eb4eb.generator.internal.ui.visitor;

import org.eventb.core.ISCEvent;
import org.eventb.core.ISCInvariant;
import org.eventb.core.ISCMachineRoot;
import org.eventb.core.ISCVariable;
import org.eventb.core.ISCVariant;

import fr.enseeiht.eb4eb.generator.internal.ui.builder.EB4EBContextBuilder;

public class EB4EBStructurGeneratorVisitor implements IMachineRootVisitor<EB4EBContextBuilder> {

	@Override
	public void visitMachineRoot(EB4EBContextBuilder builder, ISCMachineRoot machineRoot) {
		System.out.println("tetetetetetet !");
	}

	@Override
	public void visitVariable(EB4EBContextBuilder builder, ISCVariable machineRoot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInvariant(EB4EBContextBuilder builder, ISCInvariant machineRoot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitVariant(EB4EBContextBuilder builder, ISCVariant machineRoot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitEvent(EB4EBContextBuilder builder, ISCEvent machineRoot) {
		// TODO Auto-generated method stub
		
	}

}
