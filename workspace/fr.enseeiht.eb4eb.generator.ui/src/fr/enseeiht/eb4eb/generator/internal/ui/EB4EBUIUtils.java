package fr.enseeiht.eb4eb.generator.internal.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.rodinp.core.IOpenable;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;

import fr.enseeiht.eb4eb.generator.ui.EB4EBGeneratorUIPlugin;

public class EB4EBUIUtils {

	/**
	 * Link the current object to an Event-B editor.
	 * <p>
	 * 
	 * @param obj
	 *            the object (e.g. an internal element or a Rodin file)
	 */
	public static void linkToEventBEditor(Object obj) {
		final IRodinFile component = asRodinFile(obj);
		
		if (component == null)
			return;
		
		try {
			IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
					.getDefaultEditor(component.getCorrespondingResource().getName());
			IEditorPart editor = EB4EBGeneratorUIPlugin.getActivePage()
					.openEditor(new FileEditorInput(component.getResource()), desc.getId());
			
			if (editor == null) {
				// External editor
				return;
			}
			
			final ISelectionProvider sp = editor.getSite().getSelectionProvider();
			if (sp == null || component.getRoot().equals(obj)) {
				return;
			}
			sp.setSelection(new StructuredSelection(obj));
		} catch (PartInitException e) {
			String errorMsg = "Error opening Editor";
			MessageDialog.openError(null, null, errorMsg);
			EB4EBGeneratorUIPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, EB4EBGeneratorUIPlugin.PLUGIN_ID, errorMsg, e));
		}
	}
	
	private static IRodinFile asRodinFile(Object obj) {
		if (obj instanceof IRodinProject)
			return null;
		return (IRodinFile) getOpenable(obj);
	}
	
	/**
	 * Method to return the openable for an object (IRodinElement).
	 * <p>
	 * 
	 * @param element
	 *            A Rodin Element
	 * @return The IRodinFile corresponding to the input object
	 */
	public static IOpenable getOpenable(Object element) {
		if (element instanceof IRodinElement) {
			return ((IRodinElement) element).getOpenable();
		} else {
			return null;
		}
	}

}
