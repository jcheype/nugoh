package nugoh.core;

import nugoh.sdk.ActionInit;
import nugoh.sdk.ActionNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 12, 2009
 * Time: 4:17:26 PM
 */
public class ActionProxy implements ActionInit {
    private final Logger logger = LoggerFactory.getLogger(ActionProxy.class);

    static private final String INIT_ATTR_NAME = "init-method";
    static private final String RUN_ATTR_NAME = "run-method";
    static private final String INIT_DEFAULT_NAME = "init";
    static private final String RUN_DEFAULT_NAME = "run";

    private Object pojoAction;
    private Method initMethod;
    private Method runMethod;
    private boolean initialized = false;


    private void propertySet(Object pojoAction, ActionNode actionNode) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(pojoAction.getClass(), Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for(PropertyDescriptor pd : propertyDescriptors){
            Method m = pd.getWriteMethod();
            String attribute = actionNode.getAttributes().get(pd.getName());
            if(m != null && attribute != null){
                m.invoke(pojoAction, attribute);
            }
            else if(m != null && ActionNode.class.isAssignableFrom(pd.getPropertyType()) ){
                m.invoke(pojoAction, actionNode);
            }
        }
    }

    public ActionProxy(Object pojoAction, ActionNode actionNode) {
        this.pojoAction = pojoAction;


        try {
            propertySet(pojoAction, actionNode);
        } catch (Exception e) {
            String msg = "Cannot set properties";
            logger.error(msg, e);
            throw new IllegalStateException(msg);
        }

        String initMethodName = actionNode.getAttributes().get(INIT_ATTR_NAME);
        if( initMethodName == null){
            initMethodName = INIT_DEFAULT_NAME;
        }
        try {
            initMethod = pojoAction.getClass().getMethod(initMethodName);
        } catch (NoSuchMethodException e) {
            String msg = "no init method: " + initMethodName;
            logger.info(msg);
            initMethod = null;
        }

        String runMethodName = actionNode.getAttributes().get(RUN_ATTR_NAME);
        if( runMethodName == null){
            runMethodName = RUN_DEFAULT_NAME;
        }
        try {
            runMethod = pojoAction.getClass().getMethod(runMethodName , Map.class);
        } catch (NoSuchMethodException e) {
            String msg = "Cannot find run method: " + runMethodName;
            logger.error(msg, e);
            throw new IllegalStateException(msg);
        }

    }

    synchronized public void init() throws InvocationTargetException, IllegalAccessException {
        if(initMethod != null && !initialized){
            initMethod.invoke(pojoAction);
        }
        initialized = true;
    }

    public void run(Map<String, Object> context) throws InvocationTargetException, IllegalAccessException {
        if(!isInitialized()){
            throw new IllegalStateException("Trying to run a non initialized service");
        }
        runMethod.invoke(pojoAction, context);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
