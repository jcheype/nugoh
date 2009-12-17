package nugoh.restful;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 12:38:47 AM
 */
public class RestfulTest {

    @Test
    public void pathPattern(){
        NamedPattern namedPattern = new NamedPattern("/toto/{foo}/{bar}");
        Map<String, Object> result = namedPattern.getMap("/toto/ju/ch");
        assertEquals(result.get("foo"), "ju");
        assertEquals(result.get("bar"), "ch");
    }
}
