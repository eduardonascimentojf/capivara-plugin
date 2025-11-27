package gfc.diagram.edit.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// NOVOS IMPORTS
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;
// FIM NOVOS IMPORTS

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;

import gfc.diagram.edit.commands.GfcCreateShortcutDecorationsCommand;
import gfc.diagram.edit.policies.FlowchartCanonicalEditPolicy;
import gfc.diagram.edit.policies.FlowchartItemSemanticEditPolicy;
import gfc.diagram.part.GfcVisualIDRegistry;

/**
 * @generated
 */
public class FlowchartEditPart extends DiagramEditPart {

	/**
	 * @generated
	 */
	public final static String MODEL_ID = "Gfc"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 1000;

	// NOVO CAMPO PARA O LISTENER
	private LayoutListener layoutListener;

	/**
	 * @generated
	 */
	public FlowchartEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new FlowchartItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new FlowchartCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicyWithCustomReparent(GfcVisualIDRegistry.TYPED_INSTANCE));
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DiagramDragDropEditPolicy() {
			public Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
				ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>();
				for (Iterator<?> it = dropRequest.getObjects().iterator(); it.hasNext();) {
					Object nextObject = it.next();
					if (false == nextObject instanceof EObject) {
						continue;
					}
					viewDescriptors.add(new CreateViewRequest.ViewDescriptor(new EObjectAdapter((EObject) nextObject),
							Node.class, null, getDiagramPreferencesHint()));
				}
				return createShortcutsCommand(dropRequest, viewDescriptors);
			}

			private Command createShortcutsCommand(DropObjectsRequest dropRequest,
					List<CreateViewRequest.ViewDescriptor> viewDescriptors) {
				Command command = createViewsAndArrangeCommand(dropRequest, viewDescriptors);
				if (command != null) {
					return command.chain(new ICommandProxy(new GfcCreateShortcutDecorationsCommand(getEditingDomain(),
							(View) getModel(), viewDescriptors)));
				}
				return null;
			}
		});
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.POPUPBAR_ROLE);
	}

	/**
	 * @generated NOT
	 * Sobrescreve a criação da figura do diagrama para:
	 * - tornar o plano de fundo opaco;
	 * - pintar de branco;
	 * - adicionar margem interna (padding) para que nós não fiquem colados na borda.
	 *
	 * Ajuste o valor de MarginBorder (atualmente 20) conforme quiser.
	 */
	@Override
	public IFigure createFigure() {
		IFigure fig = super.createFigure();
		fig.setOpaque(true);
		fig.setBackgroundColor(ColorConstants.white);
		fig.setBorder(new MarginBorder(20));
		return fig;
	}

	// NOVO MÉTODO: Chamado quando o EditPart se torna ativo
	@Override
	public void activate() {
		super.activate();
		// Cria e adiciona o listener para auto-ajustar o tamanho
		layoutListener = new LayoutListener.Stub() {
			@Override
			public void postLayout(IFigure container) {
				// A ação de fato acontece aqui
				updateFigureBounds();
			}
		};
		getFigure().addLayoutListener(layoutListener);

		// Dispara uma atualização inicial
		Display.getCurrent().asyncExec(this::updateFigureBounds);
	}

	// NOVO MÉTODO: Chamado quando o EditPart se torna inativo
	@Override
	public void deactivate() {
		// Remove o listener para evitar memory leaks
		if (layoutListener != null) {
			getFigure().removeLayoutListener(layoutListener);
			layoutListener = null;
		}
		super.deactivate();
	}

	// NOVO MÉTODO: Lógica para calcular e aplicar o novo tamanho
	protected void updateFigureBounds() {
		IFigure contentPane = getContentPane();
		if (contentPane.getChildren().isEmpty()) {
			return; // Não faz nada se o diagrama estiver vazio
		}

		// Calcula o retângulo que engloba todos os elementos filhos
		Rectangle boundingBox = null;
		for (Object child : contentPane.getChildren()) {
			if (child instanceof IFigure) {
				IFigure childFigure = (IFigure) child;
				if (boundingBox == null) {
					boundingBox = childFigure.getBounds().getCopy();
				} else {
					boundingBox.union(childFigure.getBounds());
				}
			}
		}

		if (boundingBox != null) {
			// Adiciona uma margem extra para não ficar colado
			// O valor aqui (20) deve ser o mesmo da sua MarginBorder para consistência
			int margin = 20;
			boundingBox.expand(margin, margin);

			// Define o tamanho preferido da figura principal
			// O layout manager usará isso para ajustar o tamanho do canvas
			getFigure().setPreferredSize(boundingBox.getSize());
		}
	}
}