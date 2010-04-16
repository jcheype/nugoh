package nugoh.core;

import nugoh.sdk.ActionNode;
import nugoh.sdk.PojoFactory;
import nugoh.sdk.ServiceDescription;
import nugoh.sdk.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 18, 2009
 * Time: 6:04:26 PM
 */
public class ServiceInfoOSGIImpl implements ServiceInfo {
    private final Logger logger = LoggerFactory.getLogger(CoreActivator.class);
    private final BundleContext bundleContext;

    public ServiceInfoOSGIImpl(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    private void serviceDescriptionFromPojo(Object pojo, ServiceDescription serviceDescription) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class clazz = pojo.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            Method m = pd.getWriteMethod();

            if (m != null && String.class.isAssignableFrom(pd.getPropertyType())) {
                serviceDescription.getParameters().add(pd.getName());
            } else if (m != null && ActionNode.class.isAssignableFrom(pd.getPropertyType())) {
                serviceDescription.setSubnodes(true);
            }

            m = pd.getReadMethod();
            if (m != null ) {
                Object defaultValue = m.invoke(pojo);
                if(defaultValue != null){
                    serviceDescription.getDefaultValueMap().put(pd.getName(), defaultValue.toString());
                }
            }

        }
    }

    private ServiceDescription serviceDescriptionFromServiceReference(ServiceReference ref) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        ServiceDescription serviceDescription = new ServiceDescription();
        Object pojo = bundleContext.getService(ref);

        String serviceName = (String) ref.getProperty("service");

        serviceDescription.setServiceName(serviceName);

        serviceDescriptionFromPojo(pojo, serviceDescription);
        return serviceDescription;
    }

    private ServiceDescription serviceDescriptionFromServiceFactoryReference(ServiceReference ref) throws Exception {
        PojoFactory pojoFactory = (PojoFactory) bundleContext.getService(ref);
        Object pojo = pojoFactory.newInstance();

        ServiceDescription serviceDescription = new ServiceDescription();
        String serviceName = (String) ref.getProperty("service");
        serviceDescription.setServiceName(serviceName);

        serviceDescriptionFromPojo(pojo, serviceDescription);
        return serviceDescription;
    }

    @Override
    public List<ServiceDescription> getAllServiceInfo() {
        List<ServiceDescription> serviceDescriptionList = new ArrayList();
           serviceDescriptionList.addAll(getAllPojoServiceInfo());
           serviceDescriptionList.addAll(getAllFactoryServiceInfo());

        return serviceDescriptionList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ServiceDescription> getAllPojoServiceInfo() {
        List<ServiceDescription> serviceDescriptionList = new ArrayList();

        try {
            ServiceReference[] references = bundleContext.getAllServiceReferences(null, "(&(type=pojo)(service=*))");
            for (ServiceReference ref : references) {
                serviceDescriptionList.add(serviceDescriptionFromServiceReference(ref));
            }
        } catch (Exception e) {
            logger.error("Cannot retrieve ServiceInfo pojo list" ,e);
        }
        return serviceDescriptionList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ServiceDescription> getAllFactoryServiceInfo() {
        List<ServiceDescription> serviceDescriptionList = new ArrayList();

        try {
            ServiceReference[] references = bundleContext.getAllServiceReferences(null, "(&(type=factory)(service=*))");
            for (ServiceReference ref : references) {
                serviceDescriptionList.add(serviceDescriptionFromServiceFactoryReference(ref));
            }
        } catch (Exception e) {
            logger.error("Cannot retrieve ServiceInfo factory list", e);
        }
        return serviceDescriptionList;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public ServiceDescription getServiceInfo(String serviceName) {
        ServiceDescription desc = null;
        try {
            ServiceReference[] references = bundleContext.getAllServiceReferences(null, "(&(type=pojo)(service=" + serviceName + "))");
            if (references != null && references.length > 0) {
                desc = serviceDescriptionFromServiceReference(references[0]);
            }

        } catch (Exception e) {
            logger.error("Cannot retrieve ServiceInfo: " + serviceName, e);
        }

        return desc;
    }
}
