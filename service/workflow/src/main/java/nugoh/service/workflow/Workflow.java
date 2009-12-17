package nugoh.service.workflow;

import nugoh.sdk.Action;
import nugoh.sdk.ActionFactory;
import nugoh.sdk.ActionNode;
import org.mvel.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workflow implements Cloneable {
    private final Logger logger = LoggerFactory.getLogger(Workflow.class);

    private final List<Action> actionList = new ArrayList<Action>();
    private final Map<Action, Serializable> conditionMap = new HashMap();

    private ActionNode actionNode;
    private ActionFactory actionFactory;

    public void setActionNode(ActionNode actionNode) {
        this.actionNode = actionNode;
    }

    public void setActionFactory(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    public void init() throws Exception {
        MVEL.setThreadSafe(true);
        for(ActionNode subActionNode : actionNode.getActionNodes()){
            Action action = actionFactory.createAction(subActionNode, 1000, false);
            actionList.add(action);
            String conditionString = subActionNode.getAttributes().get("condition");
            if(conditionString != null){
                conditionMap.put(action, MVEL.compileExpression(conditionString));
            }
        }
    }

    public void run(Map<String, Object> context) throws Exception {
        logger.debug("Wokflow actions: " + actionList);
        for(Action action : actionList){
            Serializable conditionExpression = conditionMap.get(action);
            if(conditionExpression != null){
                Boolean condition;
                //synchronized(conditionExpression){
                    condition = (Boolean) MVEL.executeExpression(conditionExpression, context);
                //}
                if(condition){
                    action.run(context);
                }
            }
            else{
                action.run(context);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Workflow wf = new Workflow();
        wf.setActionFactory(actionFactory);
        return wf;
    }
}