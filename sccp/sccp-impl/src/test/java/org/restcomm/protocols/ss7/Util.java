package org.restcomm.protocols.ss7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Util {

    private static final Logger logger = LogManager.getLogger(Util.class.getName());

    public static String getTmpTestDir() {
        try {
            final String[] paths = System.getProperty("surefire.test.class.path").split(File.pathSeparator);
            if (paths.length > 0) {
                // should be xxxxx/target/test-classes
                return paths[0];
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new File(".").getAbsolutePath();
    }

}
