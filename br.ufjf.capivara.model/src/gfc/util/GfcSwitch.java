/**
 */
package gfc.util;

import gfc.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gfc.GfcPackage
 * @generated
 */
public class GfcSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static GfcPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GfcSwitch() {
		if (modelPackage == null) {
			modelPackage = GfcPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case GfcPackage.FLOWCHART: {
				Flowchart flowchart = (Flowchart)theEObject;
				T result = caseFlowchart(flowchart);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.NODE: {
				Node node = (Node)theEObject;
				T result = caseNode(node);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.ENTRY_NODE: {
				EntryNode entryNode = (EntryNode)theEObject;
				T result = caseEntryNode(entryNode);
				if (result == null) result = caseNode(entryNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.PROCESSING_NODE: {
				ProcessingNode processingNode = (ProcessingNode)theEObject;
				T result = caseProcessingNode(processingNode);
				if (result == null) result = caseNode(processingNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.DECISION_NODE: {
				DecisionNode decisionNode = (DecisionNode)theEObject;
				T result = caseDecisionNode(decisionNode);
				if (result == null) result = caseNode(decisionNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.LOOP_DECISION_NODE: {
				LoopDecisionNode loopDecisionNode = (LoopDecisionNode)theEObject;
				T result = caseLoopDecisionNode(loopDecisionNode);
				if (result == null) result = caseNode(loopDecisionNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.EXIT_NODE: {
				ExitNode exitNode = (ExitNode)theEObject;
				T result = caseExitNode(exitNode);
				if (result == null) result = caseNode(exitNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case GfcPackage.EDGE: {
				Edge edge = (Edge)theEObject;
				T result = caseEdge(edge);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Flowchart</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Flowchart</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFlowchart(Flowchart object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNode(Node object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entry Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entry Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntryNode(EntryNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Processing Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Processing Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessingNode(ProcessingNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Decision Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Decision Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDecisionNode(DecisionNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Loop Decision Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Loop Decision Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLoopDecisionNode(LoopDecisionNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Exit Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Exit Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseExitNode(ExitNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdge(Edge object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //GfcSwitch
