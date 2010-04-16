package nugoh.service.scalaTest;

import nugoh.service.scalaTest.ScalaTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Apr 15, 2010
 * Time: 8:18:55 PM
 */
public class SmallTest {
    ScalaTest scalaTest;

    @Before
    public void setup(){
        scalaTest = new ScalaTest();
        scalaTest.init();
    }

    @Test
    public void run(){
        scalaTest.run(new HashMap());
    }
}
