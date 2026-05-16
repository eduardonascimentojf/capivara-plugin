/**
 */
package gfc.impl;

import gfc.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GfcFactoryImpl extends EFactoryImpl implements GfcFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GfcFactory init() {
		try {
			GfcFactory theGfcFactory = (GfcFactory)EPackage.Registry.INSTANCE.getEFactory(GfcPackage.eNS_URI);
			if (theGfcFactory != null) {
				return theGfcFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GfcFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GfcFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case GfcPackage.FLOWCHART: return createFlowchart();
			case GfcPackage.ENTRY_NODE: return createEntryNode();
			case GfcPackage.PROCESSING_NODE: return createProcessingNode();
			case GfcPackage.DECISION_NODE: return createDecisionNode();
			case GfcPackage.LOOP_DECISION_NODE: return createLoopDecisionNode();
			case GfcPackage.EXIT_NODE: return createExitNode();
			case GfcPackage.SWITCH_NODE: return createSwitchNode();
			case GfcPackage.CASE_NODE: return createCaseNode();
			case GfcPackage.EDGE: return createEdge();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Flowchart createFlowchart() {
		FlowchartImpl flowchart = new FlowchartImpl();
		return flowchart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntryNode createEntryNode() {
		EntryNodeImpl entryNode = new EntryNodeImpl();
		return entryNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProcessingNode createProcessingNode() {
		ProcessingNodeImpl processingNode = new ProcessingNodeImpl();
		return processingNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DecisionNode createDecisionNode() {
		DecisionNodeImpl decisionNode = new DecisionNodeImpl();
		return decisionNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LoopDecisionNode createLoopDecisionNode() {
		LoopDecisionNodeImpl loopDecisionNode = new LoopDecisionNodeImpl();
		return loopDecisionNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SwitchNode createSwitchNode() {
		SwitchNodeImpl switchNode = new SwitchNodeImpl();
		return switchNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CaseNode createCaseNode() {
		CaseNodeImpl caseNode = new CaseNodeImpl();
		return caseNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExitNode createExitNode() {
		ExitNodeImpl exitNode = new ExitNodeImpl();
		return exitNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Edge createEdge() {
		EdgeImpl edge = new EdgeImpl();
		return edge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GfcPackage getGfcPackage() {
		return (GfcPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GfcPackage getPackage() {
		return GfcPackage.eINSTANCE;
	}

} //GfcFactoryImpl
