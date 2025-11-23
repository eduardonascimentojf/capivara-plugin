/**
 */
package gfc;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gfc.GfcFactory
 * @model kind="package"
 * @generated
 */
public interface GfcPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "gfc";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.ufjf.br/capivara/gfc";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gfc";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GfcPackage eINSTANCE = gfc.impl.GfcPackageImpl.init();

	/**
	 * The meta object id for the '{@link gfc.impl.FlowchartImpl <em>Flowchart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.FlowchartImpl
	 * @see gfc.impl.GfcPackageImpl#getFlowchart()
	 * @generated
	 */
	int FLOWCHART = 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__NODES = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART__EDGES = 1;

	/**
	 * The number of structural features of the '<em>Flowchart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWCHART_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gfc.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.NodeImpl
	 * @see gfc.impl.GfcPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__ID = 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__LABEL = 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gfc.impl.EntryNodeImpl <em>Entry Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.EntryNodeImpl
	 * @see gfc.impl.GfcPackageImpl#getEntryNode()
	 * @generated
	 */
	int ENTRY_NODE = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_NODE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_NODE__LABEL = NODE__LABEL;

	/**
	 * The number of structural features of the '<em>Entry Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTRY_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gfc.impl.ProcessingNodeImpl <em>Processing Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.ProcessingNodeImpl
	 * @see gfc.impl.GfcPackageImpl#getProcessingNode()
	 * @generated
	 */
	int PROCESSING_NODE = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESSING_NODE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESSING_NODE__LABEL = NODE__LABEL;

	/**
	 * The number of structural features of the '<em>Processing Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESSING_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gfc.impl.DecisionNodeImpl <em>Decision Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.DecisionNodeImpl
	 * @see gfc.impl.GfcPackageImpl#getDecisionNode()
	 * @generated
	 */
	int DECISION_NODE = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECISION_NODE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECISION_NODE__LABEL = NODE__LABEL;

	/**
	 * The number of structural features of the '<em>Decision Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECISION_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gfc.impl.LoopDecisionNodeImpl <em>Loop Decision Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.LoopDecisionNodeImpl
	 * @see gfc.impl.GfcPackageImpl#getLoopDecisionNode()
	 * @generated
	 */
	int LOOP_DECISION_NODE = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_DECISION_NODE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_DECISION_NODE__LABEL = NODE__LABEL;

	/**
	 * The number of structural features of the '<em>Loop Decision Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_DECISION_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gfc.impl.ExitNodeImpl <em>Exit Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.ExitNodeImpl
	 * @see gfc.impl.GfcPackageImpl#getExitNode()
	 * @generated
	 */
	int EXIT_NODE = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXIT_NODE__ID = NODE__ID;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXIT_NODE__LABEL = NODE__LABEL;

	/**
	 * The number of structural features of the '<em>Exit Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXIT_NODE_FEATURE_COUNT = NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gfc.impl.EdgeImpl <em>Edge</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gfc.impl.EdgeImpl
	 * @see gfc.impl.GfcPackageImpl#getEdge()
	 * @generated
	 */
	int EDGE = 7;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__LABEL = 2;

	/**
	 * The number of structural features of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link gfc.Flowchart <em>Flowchart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flowchart</em>'.
	 * @see gfc.Flowchart
	 * @generated
	 */
	EClass getFlowchart();

	/**
	 * Returns the meta object for the containment reference list '{@link gfc.Flowchart#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see gfc.Flowchart#getNodes()
	 * @see #getFlowchart()
	 * @generated
	 */
	EReference getFlowchart_Nodes();

	/**
	 * Returns the meta object for the containment reference list '{@link gfc.Flowchart#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Edges</em>'.
	 * @see gfc.Flowchart#getEdges()
	 * @see #getFlowchart()
	 * @generated
	 */
	EReference getFlowchart_Edges();

	/**
	 * Returns the meta object for class '{@link gfc.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see gfc.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link gfc.Node#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gfc.Node#getId()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Id();

	/**
	 * Returns the meta object for the attribute '{@link gfc.Node#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see gfc.Node#getLabel()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Label();

	/**
	 * Returns the meta object for class '{@link gfc.EntryNode <em>Entry Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entry Node</em>'.
	 * @see gfc.EntryNode
	 * @generated
	 */
	EClass getEntryNode();

	/**
	 * Returns the meta object for class '{@link gfc.ProcessingNode <em>Processing Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Processing Node</em>'.
	 * @see gfc.ProcessingNode
	 * @generated
	 */
	EClass getProcessingNode();

	/**
	 * Returns the meta object for class '{@link gfc.DecisionNode <em>Decision Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Decision Node</em>'.
	 * @see gfc.DecisionNode
	 * @generated
	 */
	EClass getDecisionNode();

	/**
	 * Returns the meta object for class '{@link gfc.LoopDecisionNode <em>Loop Decision Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Loop Decision Node</em>'.
	 * @see gfc.LoopDecisionNode
	 * @generated
	 */
	EClass getLoopDecisionNode();

	/**
	 * Returns the meta object for class '{@link gfc.ExitNode <em>Exit Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Exit Node</em>'.
	 * @see gfc.ExitNode
	 * @generated
	 */
	EClass getExitNode();

	/**
	 * Returns the meta object for class '{@link gfc.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge</em>'.
	 * @see gfc.Edge
	 * @generated
	 */
	EClass getEdge();

	/**
	 * Returns the meta object for the reference '{@link gfc.Edge#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see gfc.Edge#getSource()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_Source();

	/**
	 * Returns the meta object for the reference '{@link gfc.Edge#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see gfc.Edge#getTarget()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_Target();

	/**
	 * Returns the meta object for the attribute '{@link gfc.Edge#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see gfc.Edge#getLabel()
	 * @see #getEdge()
	 * @generated
	 */
	EAttribute getEdge_Label();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GfcFactory getGfcFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gfc.impl.FlowchartImpl <em>Flowchart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.FlowchartImpl
		 * @see gfc.impl.GfcPackageImpl#getFlowchart()
		 * @generated
		 */
		EClass FLOWCHART = eINSTANCE.getFlowchart();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLOWCHART__NODES = eINSTANCE.getFlowchart_Nodes();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLOWCHART__EDGES = eINSTANCE.getFlowchart_Edges();

		/**
		 * The meta object literal for the '{@link gfc.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.NodeImpl
		 * @see gfc.impl.GfcPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__ID = eINSTANCE.getNode_Id();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__LABEL = eINSTANCE.getNode_Label();

		/**
		 * The meta object literal for the '{@link gfc.impl.EntryNodeImpl <em>Entry Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.EntryNodeImpl
		 * @see gfc.impl.GfcPackageImpl#getEntryNode()
		 * @generated
		 */
		EClass ENTRY_NODE = eINSTANCE.getEntryNode();

		/**
		 * The meta object literal for the '{@link gfc.impl.ProcessingNodeImpl <em>Processing Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.ProcessingNodeImpl
		 * @see gfc.impl.GfcPackageImpl#getProcessingNode()
		 * @generated
		 */
		EClass PROCESSING_NODE = eINSTANCE.getProcessingNode();

		/**
		 * The meta object literal for the '{@link gfc.impl.DecisionNodeImpl <em>Decision Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.DecisionNodeImpl
		 * @see gfc.impl.GfcPackageImpl#getDecisionNode()
		 * @generated
		 */
		EClass DECISION_NODE = eINSTANCE.getDecisionNode();

		/**
		 * The meta object literal for the '{@link gfc.impl.LoopDecisionNodeImpl <em>Loop Decision Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.LoopDecisionNodeImpl
		 * @see gfc.impl.GfcPackageImpl#getLoopDecisionNode()
		 * @generated
		 */
		EClass LOOP_DECISION_NODE = eINSTANCE.getLoopDecisionNode();

		/**
		 * The meta object literal for the '{@link gfc.impl.ExitNodeImpl <em>Exit Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.ExitNodeImpl
		 * @see gfc.impl.GfcPackageImpl#getExitNode()
		 * @generated
		 */
		EClass EXIT_NODE = eINSTANCE.getExitNode();

		/**
		 * The meta object literal for the '{@link gfc.impl.EdgeImpl <em>Edge</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gfc.impl.EdgeImpl
		 * @see gfc.impl.GfcPackageImpl#getEdge()
		 * @generated
		 */
		EClass EDGE = eINSTANCE.getEdge();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__SOURCE = eINSTANCE.getEdge_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__TARGET = eINSTANCE.getEdge_Target();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDGE__LABEL = eINSTANCE.getEdge_Label();

	}

} //GfcPackage
