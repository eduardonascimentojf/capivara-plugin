/**
 */
package gfc;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Flowchart</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link gfc.Flowchart#getNodes <em>Nodes</em>}</li>
 *   <li>{@link gfc.Flowchart#getEdges <em>Edges</em>}</li>
 * </ul>
 *
 * @see gfc.GfcPackage#getFlowchart()
 * @model annotation="gmf.diagram"
 * @generated
 */
public interface Flowchart extends EObject {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link gfc.Node}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' containment reference list.
	 * @see gfc.GfcPackage#getFlowchart_Nodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getNodes();

	/**
	 * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
	 * The list contents are of type {@link gfc.Edge}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' containment reference list.
	 * @see gfc.GfcPackage#getFlowchart_Edges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getEdges();

} // Flowchart
