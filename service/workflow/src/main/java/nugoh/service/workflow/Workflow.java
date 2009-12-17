package nugoh.service.workflow;

import nugoh.sdk.Action;
import nugoh.sdk.ActionFactory;
import nugoh.sdk.ActionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Workflow implements Cloneable {
    private final Logger logger = LoggerFactory.getLogger(Workflow.class);

    private List<Action> actionList = new ArrayList<Action>();

    private ActionNode actionNode;
    private ActionFactory actionFactory;

    public void setActionNode(ActionNode actionNode) {
        this.actionNode = actionNode;
    }

    public void setActionFactory(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    public void init() throws Exception {
        for(ActionNode subActionNode : actionNode.getActionNodes()){
            actionList.add(actionFactory.createAction(subActionNode, 1000, false));
        }
    }

    public void run(Map<String, Object> context) throws Exception {
        logger.debug("Wokflow actions: " + actionList);
        for(Action action : actionList){
            action.run(context);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Workflow wf = new Workflow();
        wf.setActionFactory(actionFactory);
        return wf;
    }
}