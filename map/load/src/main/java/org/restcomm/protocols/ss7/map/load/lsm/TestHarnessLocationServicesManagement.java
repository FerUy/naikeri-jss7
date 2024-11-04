package org.restcomm.protocols.ss7.map.load.lsm;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @modified <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public abstract class TestHarnessLocationServicesManagement implements MAPDialogListener, MAPServiceLsmListener {

    private static final Logger logger = Logger.getLogger("map.test");
    protected static final String CREATED_DIALOGS = "CreatedScenario";
    protected static final String SUCCESSFUL_DIALOGS = "CompletedScenario";
    protected static final String ERROR_DIALOGS = "FailedScenario";

    protected static final String LOG_FILE_NAME = "log.file.name";
    protected static String logFileName = "maplog.txt";

    protected static int NDIALOGS = 1440000;

    protected static int MAXCONCURRENTDIALOGS = 100;

    // MTP Details
    protected static int CLIENT_SPC = 1001; // Client Signaling Point Code
    protected static int SERVER_SPC = 2000; // Server Signaling Point Code
    protected static int NETWORK_INDICATOR = 2; // National Network
    protected static int SERVICE_INDICATOR = 3; // Upper layer is SCCP
    protected static int HLR_SSN = 6;
    protected static int MSC_SSN = 8;
    protected static int GMLC_SSN = 145;
    protected static int SGSN_SSN = 149;

    // M3UA details
    protected static String CLIENT_IP = "127.0.0.1";
    protected static int CLIENT_PORT = 2345;

    protected static String SERVER_IP = "127.0.0.1";
    protected static int SERVER_PORT = 3434;

    protected static int ROUTING_CONTEXT = 100;

    protected static int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    protected static int SENDING_MESSAGE_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    protected static int RAMP_UP_PERIOD = -100;

    protected final String SERVER_ASSOCIATION_NAME = "serverAssociation";
    protected final String CLIENT_ASSOCIATION_NAME = "clientAssociation";

    protected final String SERVER_NAME = "testserver";

    // TCAP Details
    protected static final int MAX_DIALOGS = 500000;
    protected static String SCCP_GMLC_ADDRESS = "491710470201";
    protected static String SCCP_HLR_ADDRESS = "491710460000";
    protected static String SCCP_MSC_ADDRESS = "491710460015";
    protected static String SCCP_SGSN_ADDRESS = "491710460025";



    protected static RoutingIndicator ROUTING_INDICATOR = RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN;

    protected final ParameterFactoryImpl factory = new ParameterFactoryImpl();

    protected static int TEST_START_DELAY = 20000;
    protected static int TEST_END_DELAY = 3000;
    protected static int PRINT_WRITER_PERIOD = 2000;

    protected TestHarnessLocationServicesManagement() {
        init();
    }

    public void init() {
        try {
            //Properties tckProperties = new Properties();

            InputStream inStreamLog4j = TestHarnessMobilityManagement.class.getResourceAsStream("/log4j.properties");

            System.out.println("Input Stream = " + inStreamLog4j);

            Properties propertiesLog4j = new Properties();
            try {
                propertiesLog4j.load(inStreamLog4j);
                PropertyConfigurator.configure(propertiesLog4j);
            } catch (Exception e) {
                logger.error(e.getMessage());
                BasicConfigurator.configure();
            }

            logger.debug("log4j configured");

            String lf = System.getProperties().getProperty(LOG_FILE_NAME);
            if (lf != null) {
                logFileName = lf;
            }

            // If already created a print writer then just use it.
            try {
                logger.addAppender(new FileAppender(new SimpleLayout(), logFileName));
            } catch (FileNotFoundException fileNotFoundException) {
                logger.error(fileNotFoundException.getMessage());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }
}
