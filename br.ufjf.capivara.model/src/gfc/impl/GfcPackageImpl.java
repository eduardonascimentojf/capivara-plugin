/**
 */
package gfc.impl;

import gfc.DecisionNode;
import gfc.Edge;
import gfc.EntryNode;
import gfc.ExitNode;
import gfc.Flowchart;
import gfc.GfcFactory;
import gfc.GfcPackage;
import gfc.LoopDecisionNode;
import gfc.Node;
import gfc.ProcessingNode;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GfcPackageImpl extends EPackageImpl implements GfcPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass flowchartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entryNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass processingNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass decisionNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass loopDecisionNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass exitNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass edgeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see gfc.GfcPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GfcPackageImpl() {
		super(eNS_URI, GfcFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link GfcPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GfcPackage init() {
		if (isInited) return (GfcPackage)EPackage.Registry.INSTANCE.getEPackage(GfcPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredGfcPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		GfcPackageImpl theGfcPackage = registeredGfcPackage instanceof GfcPackageImpl ? (GfcPackageImpl)registeredGfcPackage : new GfcPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theGfcPackage.createPackageContents();

		// Initialize created meta-data
		theGfcPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theGfcPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GfcPackage.eNS_URI, theGfcPackage);
		return theGfcPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFlowchart() {
		return flowchartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFlowchart_Nodes() {
		return (EReference)flowchartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFlowchart_Edges() {
		return (EReference)flowchartEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNode() {
		return nodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNode_Id() {
		return (EAttribute)nodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNode_Label() {
		return (EAttribute)nodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntryNode() {
		return entryNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProcessingNode() {
		return processingNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDecisionNode() {
		return decisionNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLoopDecisionNode() {
		return loopDecisionNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getExitNode() {
		return exitNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEdge() {
		return edgeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEdge_Source() {
		return (EReference)edgeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEdge_Target() {
		return (EReference)edgeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEdge_Label() {
		return (EAttribute)edgeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GfcFactory getGfcFactory() {
		return (GfcFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		flowchartEClass = createEClass(FLOWCHART);
		createEReference(flowchartEClass, FLOWCHART__NODES);
		createEReference(flowchartEClass, FLOWCHART__EDGES);

		nodeEClass = createEClass(NODE);
		createEAttribute(nodeEClass, NODE__ID);
		createEAttribute(nodeEClass, NODE__LABEL);

		entryNodeEClass = createEClass(ENTRY_NODE);

		processingNodeEClass = createEClass(PROCESSING_NODE);

		decisionNodeEClass = createEClass(DECISION_NODE);

		loopDecisionNodeEClass = createEClass(LOOP_DECISION_NODE);

		exitNodeEClass = createEClass(EXIT_NODE);

		edgeEClass = createEClass(EDGE);
		createEReference(edgeEClass, EDGE__SOURCE);
		createEReference(edgeEClass, EDGE__TARGET);
		createEAttribute(edgeEClass, EDGE__LABEL);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		entryNodeEClass.getESuperTypes().add(this.getNode());
		processingNodeEClass.getESuperTypes().add(this.getNode());
		decisionNodeEClass.getESuperTypes().add(this.getNode());
		loopDecisionNodeEClass.getESuperTypes().add(this.getNode());
		exitNodeEClass.getESuperTypes().add(this.getNode());

		// Initialize classes and features; add operations and parameters
		initEClass(flowchartEClass, Flowchart.class, "Flowchart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFlowchart_Nodes(), this.getNode(), null, "nodes", null, 0, -1, Flowchart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFlowchart_Edges(), this.getEdge(), null, "edges", null, 0, -1, Flowchart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nodeEClass, Node.class, "Node", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNode_Id(), ecorePackage.getEInt(), "id", "0", 0, 1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getNode_Label(), ecorePackage.getEString(), "label", null, 0, 1, Node.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entryNodeEClass, EntryNode.class, "EntryNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(processingNodeEClass, ProcessingNode.class, "ProcessingNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(decisionNodeEClass, DecisionNode.class, "DecisionNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(loopDecisionNodeEClass, LoopDecisionNode.class, "LoopDecisionNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(exitNodeEClass, ExitNode.class, "ExitNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(edgeEClass, Edge.class, "Edge", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEdge_Source(), this.getNode(), null, "source", null, 1, 1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEdge_Target(), this.getNode(), null, "target", null, 1, 1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEdge_Label(), ecorePackage.getEString(), "label", null, 0, 1, Edge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// gmf.diagram
		createGmfAnnotations();
		// gmf.node
		createGmf_1Annotations();
		// gmf.link
		createGmf_2Annotations();
	}

	/**
	 * Initializes the annotations for <b>gmf.diagram</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createGmfAnnotations() {
		String source = "gmf.diagram";
		addAnnotation
		  (flowchartEClass,
		   source,
		   new String[] {
		   });
	}

	/**
	 * Initializes the annotations for <b>gmf.node</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createGmf_1Annotations() {
		String source = "gmf.node";
		addAnnotation
		  (entryNodeEClass,
		   source,
		   new String[] {
			   "figure", "ellipse",
			   "label.icon", "false",
			   "label.placement", "internal",
			   "label", "id",
			   "size", "40,40",
			   "border.color", "40,167,69"
		   });
		addAnnotation
		  (processingNodeEClass,
		   source,
		   new String[] {
			   "figure", "ellipse",
			   "label.icon", "false",
			   "label.placement", "internal",
			   "label", "id",
			   "size", "40,40",
			   "border.color", "0,0,0"
		   });
		addAnnotation
		  (decisionNodeEClass,
		   source,
		   new String[] {
			   "figure", "ellipse",
			   "label.icon", "false",
			   "label.placement", "internal",
			   "label", "id",
			   "size", "40,40",
			   "border.color", "0,123,255"
		   });
		addAnnotation
		  (loopDecisionNodeEClass,
		   source,
		   new String[] {
			   "figure", "ellipse",
			   "label.icon", "false",
			   "label.placement", "internal",
			   "label", "id",
			   "size", "40,40",
			   "border.color", "255,165,0"
		   });
		addAnnotation
		  (exitNodeEClass,
		   source,
		   new String[] {
			   "figure", "gfc.diagram.figures.DoubleEllipseFigure",
			   "label.icon", "false",
			   "label.placement", "internal",
			   "label", "id",
			   "size", "40,40"
		   });
	}

	/**
	 * Initializes the annotations for <b>gmf.link</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createGmf_2Annotations() {
		String source = "gmf.link";
		addAnnotation
		  (edgeEClass,
		   source,
		   new String[] {
			   "source", "source",
			   "target", "target",
			   "target.decoration", "arrow",
			   "color", "0,0,0",
			   "width", "2"
		   });
	}

} //GfcPackageImpl
