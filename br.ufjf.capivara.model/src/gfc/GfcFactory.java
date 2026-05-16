/**
 */
package gfc;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gfc.GfcPackage
 * @generated
 */
public interface GfcFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GfcFactory eINSTANCE = gfc.impl.GfcFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Flowchart</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Flowchart</em>'.
	 * @generated
	 */
	Flowchart createFlowchart();

	/**
	 * Returns a new object of class '<em>Entry Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entry Node</em>'.
	 * @generated
	 */
	EntryNode createEntryNode();

	/**
	 * Returns a new object of class '<em>Processing Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Processing Node</em>'.
	 * @generated
	 */
	ProcessingNode createProcessingNode();

	/**
	 * Returns a new object of class '<em>Decision Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Decision Node</em>'.
	 * @generated
	 */
	DecisionNode createDecisionNode();

	/**
	 * Returns a new object of class '<em>Loop Decision Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Loop Decision Node</em>'.
	 * @generated
	 */
	LoopDecisionNode createLoopDecisionNode();

	/**
	 * Returns a new object of class '<em>Switch Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Switch Node</em>'.
	 * @generated
	 */
	SwitchNode createSwitchNode();

	/**
	 * Returns a new object of class '<em>Case Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Case Node</em>'.
	 * @generated
	 */
	CaseNode createCaseNode();

	/**
	 * Returns a new object of class '<em>Exit Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Exit Node</em>'.
	 * @generated
	 */
	ExitNode createExitNode();

	/**
	 * Returns a new object of class '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Edge</em>'.
	 * @generated
	 */
	Edge createEdge();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	GfcPackage getGfcPackage();

} //GfcFactory
