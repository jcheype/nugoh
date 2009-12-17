package nugoh.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.MavenUtils.asInProject;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 14, 2009
 * Time: 1:42:39 AM
 */

@RunWith(JUnit4TestRunner.class)
public class OsgiTest {
    @Inject
    private BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() {
        return options(
                frameworks(
                        felix()
                ),
                //bundle( "http://repository.ops4j.org/maven2/org/ops4j/pax/logging/pax-logging-api/1.3.0/pax-logging-api-1.3.0.jar" ),
                //bundle( "http://repository.ops4j.org/maven2/org/ops4j/pax/logging/pax-logging-service/1.3.0/pax-logging-service-1.3.0.jar" ),
                mavenBundle().groupId("org.slf4j").artifactId("slf4j-api"),
                mavenBundle().groupId("org.slf4j").artifactId("slf4j-simple"),
                mavenBundle().groupId("nugoh").artifactId("core")

        );
    }


    @Before
    public void setup() throws Exception {
        assertNotNull(bundleContext);
    }

    @Test
    public void osgiFactoryPreCreate() {
        //ActionFactory actionFactory = new ActionFactoryOSGIImpl(bundleContext);
        //ActionNode an = xmlParser.parse(elements.get(2));

        //Action action = actionFactory.createAction(an, 1000);
        //assertNotNull(action);

    }
}
