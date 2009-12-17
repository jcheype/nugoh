package nugoh.sdk;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 3:51:04 PM
 */
public interface ActionFactory {
    Action createAction(ActionNode actionNode, long timeout, boolean autoRegister) throws Exception;
    void registerAction(Action action, String id) throws Exception;
    void unregisterAction(String id) throws Exception;
    Action retrieveAction(String actionId);
}
