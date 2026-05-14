package org.restcomm.protocols.ss7.map.load.lsm;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;

import java.net.URL;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public abstract class TestHarnessLocationServicesManagement implements MAPDialogListener, MAPServiceLsmListener {

    private static final Logger logger = LogManager.getLogger("map.test");
    protected static final String CREATED_DIALOGS = "CreatedScenario";
    protected static final String SUCCESSFUL_DIALOGS = "CompletedScenario";
    protected static final String ERROR_DIALOGS = "FailedScenario";

    protected static final String LOG_FILE_NAME = "log.file.name";
    protected static String logFileName = "maplog.txt";

    protected static int NDIALOGS = 1440000;

    protected static int MAXCONCURRENTDIALOGS = 100;

    // MTP Details
    protected static int CLIENT_SPC = 1001; // Client Signaling Point Code
    protected static int SERVER_SPC = 2002; // Server Signaling Point Code
    protected static int NETWORK_INDICATOR = 2; // National Network
    protected static int SERVICE_INDICATOR = 3; // Upper layer is SCCP
    protected static int SSN = 8;
    protected static int CLIENT_SSN = 145;
    protected static int GMLC_SSN = 145;
    protected static int SERVER_SSN = 12;
    protected static int HLR_SSN = 6;
    protected static int MSC_SSN = 8;
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
    protected static String SCCP_CLIENT_ADDRESS = "491710470201";
    protected static String SCCP_SERVER_ADDRESS = "491710460000";

    protected static RoutingIndicator ROUTING_INDICATOR = RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN;
    protected static RoutingIndicator ROUTING_INDICATOR_GT = RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE;

    protected final ParameterFactoryImpl factory = new ParameterFactoryImpl();

    protected static int TEST_START_DELAY = 10000;
    protected static int TEST_END_DELAY = 3000;
    protected static int PRINT_WRITER_PERIOD = 2000;

    protected TestHarnessLocationServicesManagement() {
        init();
    }

    public void init() {
        try {
            URL resourcePropertiesUrl = TestHarnessLocationServicesManagement.class.getResource("/log4j2.properties");
            if (resourcePropertiesUrl != null) {
                ConfigurationFactory.getInstance().getConfiguration(null, null, resourcePropertiesUrl.toURI());
            } else {
                Configurator.initialize(new DefaultConfiguration());
                Configurator.setRootLevel(Level.INFO);
            }

            String lf = System.getProperties().getProperty(LOG_FILE_NAME);
            if (lf != null) {
                logFileName = lf;
            }

            try {
                LoggerContext lc = (LoggerContext) LogManager.getContext(false);
                FileAppender fa = FileAppender.newBuilder().withFileName(logFileName)
                    .setConfiguration(lc.getConfiguration()).build();
                fa.start();
                lc.getConfiguration().addAppender(fa);
                lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(fa.getName()));
                lc.updateLoggers();

            } catch (Exception exception) {
                logger.error(exception.getMessage());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
