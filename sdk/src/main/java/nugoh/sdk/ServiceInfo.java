package nugoh.sdk;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 18, 2009
 * Time: 5:56:52 PM
 */
public interface ServiceInfo {
    List<ServiceDescription> getAllServiceInfo();
    ServiceDescription getServiceInfo(String serviceId);
}
