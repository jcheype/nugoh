package nugoh.sdk;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 13, 2009
 * Time: 2:24:32 PM
 */
public interface Action {
    public void run(Map<String, Object> context) throws Exception;
}
