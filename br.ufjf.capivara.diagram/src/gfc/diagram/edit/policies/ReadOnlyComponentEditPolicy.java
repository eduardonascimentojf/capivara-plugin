package gfc.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class ReadOnlyComponentEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        // Retorna um comando não executável, bloqueando a deleção
        return UnexecutableCommand.INSTANCE;
    }
}