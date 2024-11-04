package org.restcomm.protocols.ss7.map.load.lsm;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.restcomm.protocols.ss7.map.load.CsvWriter;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.NetworkIdState;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.sccpext.impl.SccpExtModuleImpl;
import org.restcomm.protocols.ss7.sccpext.router.RouterExt;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import org.apache.commons.lang3.RandomUtils;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

public class Client extends TestHarnessLocationServicesManagement {

    private static final Logger logger = Logger.getLogger(Client.class);

    // TCAP
    private static TCAPStack tcapStack;

    // MAP
    private MAPStackImpl mapStack;
    private static MAPProvider mapProvider;

    // SCCP
    SccpExtModuleImpl sccpExtModule;
    private SccpStackImpl sccpStack;
    private Router router;
    private RouterExt routerExt;
    private SccpResource sccpResource;

    // M3UA
    private M3UAManagementImpl clientM3UAMgmt;

    // SCTP
    private NettySctpManagementImpl sctpManagement;

    // a ramp-up period is required for performance testing.
    int endCount;
    transient boolean endReportPrinted;

    // AtomicInteger nbConcurrentDialogs = new AtomicInteger(0);

    volatile long start = 0L;
    volatile long prev = 0L;

    private RateLimiter rateLimiterObj = null;

    private CsvWriter csvWriter;

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {

        this.rateLimiterObj = RateLimiter.create(MAXCONCURRENTDIALOGS); // rate

        this.initSCTP(ipChannelType);

        // Initialize M3UA first
        this.initM3UA();

        // Initialize SCCP
        this.initSCCP();

        // Initialize TCAP
        this.initTCAP();

        // Initialize MAP
        this.initMAP();

        // Finally, start the ASP
        this.clientM3UAMgmt.startAsp("ASP1");

        this.csvWriter = new CsvWriter("map");
        this.csvWriter.addCounter(CREATED_DIALOGS);
        this.csvWriter.addCounter(SUCCESSFUL_DIALOGS);
        this.csvWriter.addCounter(ERROR_DIALOGS);
        this.csvWriter.start(TEST_START_DELAY, PRINT_WRITER_PERIOD);
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.sctpManagement = new NettySctpManagementImpl("Client");
        // this.sctpManagement.setSingleThread(false);
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResources();

        // 1. Create SCTP Association
        sctpManagement.addAssociation(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType, null);
    }

    private void initM3UA() throws Exception {
        this.clientM3UAMgmt = new M3UAManagementImpl("Client", null, new Ss7ExtInterfaceImpl());
        this.clientM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.clientM3UAMgmt.setDeliveryMessageThreadCount(DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.clientM3UAMgmt.start();
        this.clientM3UAMgmt.removeAllResources();

        // m3ua as create rc <rc> <ras-name>
        RoutingContext rc = factory.createRoutingContext(new long[] { 101L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        NetworkAppearance na = factory.createNetworkAppearance(102L);
        this.clientM3UAMgmt.createAs("AS1", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);

        // Step 2 : Create ASP
        this.clientM3UAMgmt.createAspFactory("ASP1", CLIENT_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = this.clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

        // Step 4: Add Route. Remote point code is 2
        clientM3UAMgmt.addRoute(SERVER_SPC, -1, -1, "AS1");

    }

    private void initSCCP() throws Exception {
        Ss7ExtInterface ss7ExtInterface = new Ss7ExtInterfaceImpl();
        sccpExtModule = new SccpExtModuleImpl();
        ss7ExtInterface.setSs7ExtSccpInterface(sccpExtModule);
        this.sccpStack = new SccpStackImpl("MapLoadClientSccpStack", ss7ExtInterface);
        this.sccpStack.setMtp3UserPart(1, this.clientM3UAMgmt);

        // this.sccpStack.setCongControl_Algo(SccpCongestionControlAlgo.levelDepended);

        this.sccpStack.start();
        this.sccpStack.removeAllResources();

        this.router = this.sccpStack.getRouter();
        this.routerExt = sccpExtModule.getRouterExt();
        this.sccpResource = this.sccpStack.getSccpResource();

        this.sccpResource.addRemoteSpc(0, SERVER_SPC, 0, 0);
        this.sccpResource.addRemoteSsn(0, SERVER_SPC, HLR_SSN, 0, false);
        this.sccpResource.addRemoteSsn(1, SERVER_SPC, MSC_SSN, 0, false);
        this.sccpResource.addRemoteSsn(2, SERVER_SPC, SGSN_SSN, 0, false);


        this.router.addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0, null);
        this.router.addMtp3Destination(1, 1, SERVER_SPC, SERVER_SPC, 0, 255, 255);
        this.router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, CLIENT_SPC, 0);
        this.routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, SERVER_SPC, 0);
        this.routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        this.routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0, null);
        this.routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K",
                2, -1, null, 0, null);
    }

    private void initTCAP() throws Exception {
        tcapStack = new TCAPStackImpl("Test", this.sccpStack.getSccpProvider(), GMLC_SSN);
        tcapStack.start();
        tcapStack.setDialogIdleTimeout(60000);
        tcapStack.setInvokeTimeout(30000);
        tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        // this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), VLR_SSN);
        this.mapStack = new MAPStackImpl("TestClient", tcapStack.getProvider());
        mapProvider = this.mapStack.getMAPProvider();
        mapProvider.addMAPDialogListener(this);
        mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
        mapProvider.getMAPServiceLsm().activate();
        this.mapStack.start();
    }

    private static SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = GMLC_SSN;
        }
        return fact.createSccpAddress(ri, gt, dpc, ssn);
    }

    public void terminate() {
        try {
            this.csvWriter.stop(TEST_END_DELAY);
        } catch (InterruptedException e) {
            logger.error("an error occurred while stopping csvWriter", e);
        }
    }

    public static void main(String[] args) {

        int noOfCalls = Integer.parseInt(args[0]);
        int noOfConcurrentCalls = Integer.parseInt(args[1]);

        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 3 && args[2].equalsIgnoreCase("tcp")) {
            ipChannelType = IpChannelType.TCP;
        }

        logger.info("IpChannelType=" + ipChannelType);

        if (args.length >= 4) {
            TestHarnessLocationServicesManagement.CLIENT_IP = args[3];
        }

        logger.info("CLIENT_IP=" + TestHarnessLocationServicesManagement.CLIENT_IP);

        if (args.length >= 5) {
            TestHarnessLocationServicesManagement.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        logger.info("CLIENT_PORT=" + TestHarnessLocationServicesManagement.CLIENT_PORT);

        if (args.length >= 6) {
            TestHarnessLocationServicesManagement.SERVER_IP = args[5];
        }

        logger.info("SERVER_IP=" + TestHarnessLocationServicesManagement.SERVER_IP);

        if (args.length >= 7) {
            TestHarnessLocationServicesManagement.SERVER_PORT = Integer.parseInt(args[6]);
        }

        logger.info("SERVER_PORT=" + TestHarnessLocationServicesManagement.SERVER_PORT);

        if (args.length >= 8) {
            TestHarnessLocationServicesManagement.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        logger.info("CLIENT_SPC=" + TestHarnessLocationServicesManagement.CLIENT_SPC);

        if (args.length >= 9) {
            TestHarnessLocationServicesManagement.SERVER_SPC = Integer.parseInt(args[8]);
        }

        logger.info("SERVER_SPC=" + TestHarnessLocationServicesManagement.SERVER_SPC);

        if (args.length >= 10) {
            TestHarnessLocationServicesManagement.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        logger.info("NETWORK_INDICATOR=" + TestHarnessLocationServicesManagement.NETWORK_INDICATOR);

        if (args.length >= 11) {
            TestHarnessLocationServicesManagement.SERVICE_INDICATOR = Integer.parseInt(args[10]);
        }

        logger.info("SERVICE_INDICATOR=" + TestHarnessLocationServicesManagement.SERVICE_INDICATOR);

        if (args.length >= 12) {
            TestHarnessLocationServicesManagement.GMLC_SSN = Integer.parseInt(args[11]);
        }

        logger.info("SSN=" + TestHarnessLocationServicesManagement.GMLC_SSN);

        if (args.length >= 13) {
            TestHarnessLocationServicesManagement.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        logger.info("ROUTING_CONTEXT=" + TestHarnessLocationServicesManagement.ROUTING_CONTEXT);

        if (args.length >= 14) {
            TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[13]);
        }

        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT=" + TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        /*
         * logger.info("Number of calls to be completed = " + noOfCalls + " Number of concurrent calls to be maintained = " +
         * noOfConcurrentCalls);
         */

        if (args.length >= 15) {
            TestHarnessLocationServicesManagement.RAMP_UP_PERIOD = Integer.parseInt(args[14]);
        }

        System.out.println("RAMP_UP_PERIOD=" + TestHarnessLocationServicesManagement.RAMP_UP_PERIOD);

        if (args.length >= 16) {
            TestHarnessLocationServicesManagement.SCCP_GMLC_ADDRESS = args[15];
        }

        System.out.println("SCCP_GMLC_ADDRESS=" + TestHarnessLocationServicesManagement.SCCP_GMLC_ADDRESS);

        if (args.length >= 17) {
            TestHarnessLocationServicesManagement.SCCP_HLR_ADDRESS = args[16];
        }

        System.out.println("SCCP_HLR_ADDRESS=" + TestHarnessLocationServicesManagement.SCCP_HLR_ADDRESS);

        if (args.length >= 18) {
            TestHarnessLocationServicesManagement.ROUTING_INDICATOR = RoutingIndicator.valueOf(Integer.parseInt(args[17]));
        }

        System.out.println("ROUTING_INDICATOR=" + TestHarnessLocationServicesManagement.ROUTING_INDICATOR);

        if (args.length >= 19) {
            TestHarnessLocationServicesManagement.SENDING_MESSAGE_THREAD_COUNT = Integer.parseInt(args[18]);
        }

        System.out.println("SENDING_MESSAGE_THREAD_COUNT=" + TestHarnessLocationServicesManagement.SENDING_MESSAGE_THREAD_COUNT);

        NDIALOGS = noOfCalls;

        logger.info("NDIALOGS=" + NDIALOGS);

        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        logger.info("MAXCONCURRENTDIALOGS=" + MAXCONCURRENTDIALOGS);

        final Client client = new Client();
        client.endCount = TestHarnessLocationServicesManagement.RAMP_UP_PERIOD;

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(TestHarnessLocationServicesManagement.TEST_START_DELAY);

            Thread.sleep(TestHarnessLocationServicesManagement.TEST_START_DELAY);

            // threads creating
            Thread[] threads = new Thread[SENDING_MESSAGE_THREAD_COUNT];
            for (int i = 0; i < SENDING_MESSAGE_THREAD_COUNT; i++) {
                threads[i] = new Thread(client.new DialogInitiator());
            }
            for (int i = 0; i < SENDING_MESSAGE_THREAD_COUNT; i++) {
                threads[i].start();
            }

            while (client.endCount < NDIALOGS) {
                Thread.sleep(100);
            }

            client.terminate();

        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }
    }

    public class DialogInitiator implements Runnable {

        @Override
        public void run() {
            try {
                while (endCount < NDIALOGS) {
                    // while (client.nbConcurrentDialogs.intValue() >= MAXCONCURRENTDIALOGS) {

                    // logger.warn("Number of concurrent MAP dialog's = " +
                    // client.nbConcurrentDialogs.intValue()
                    // + " Waiting for max dialog count to go down!");

                    // synchronized (client) {
                    // try {
                    // client.wait();
                    // } catch (Exception ex) {
                    // }
                    // }
                    // }// end of while (client.nbConcurrentDialogs.intValue() >=
                    // MAXCONCURRENTDIALOGS)

                    if (endCount < 0) {
                        start = System.currentTimeMillis();
                        prev = start;
                        // logger.warn("StartTime = " + client.start);
                    }

                    initiateLsmClient();
                }
            } catch (MAPException ex) {
                logger.error("Exception when sending a new MAP dialog", ex);
            }
        }

    }

    private void initiateLsmClient() throws MAPException {
        NetworkIdState networkIdState = this.mapStack.getMAPProvider().getNetworkIdState(0);
        int executorCongestionLevel = this.mapStack.getMAPProvider().getExecutorCongestionLevel();
        if (!(networkIdState == null || networkIdState.isAvailable() && networkIdState.getCongLevel() <= 0 && executorCongestionLevel <= 0)) {
            // congestion or unavailable
            logger.warn("**** Outgoing congestion control: MAP load test client: networkIdState=" + networkIdState
                    + ", executorCongestionLevel=" + executorCongestionLevel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException: " + e.getMessage());
            }
        }

        this.rateLimiterObj.acquire();

        // Send Authentication Info
        sendRoutingInfoForLCSRequest();
    }

    private void sendRoutingInfoForLCSRequest() throws MAPException {

        try {
            // First create Dialog
            AddressString origRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_GMLC_ADDRESS);
            AddressString destRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_HLR_ADDRESS);
            SccpAddress origSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, GMLC_SSN, SCCP_GMLC_ADDRESS);
            SccpAddress destSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_HLR_ADDRESS);
            MAPDialogLsm mapDialogLsm = mapProvider.getMAPServiceLsm()
                    .createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                                    MAPApplicationContextVersion.version3), origSccpAddress, origRef, destSccpAddress, destRef);

            // Then, create parameters for concerning MAP operation
            long msisdnDigits = RandomUtils.nextLong(59898000000L, 59899000000L);
            SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, String.valueOf(msisdnDigits)));
            ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, SCCP_GMLC_ADDRESS);
            MAPExtensionContainer extensionContainer = null;

            mapDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, subscriberIdentity, extensionContainer);

            // This will initiate the TC-BEGIN with INVOKE component
            mapDialogLsm.send();

        } catch (MAPException e) {
            logger.error(String.format("Error while sending MAP SRILCS:" + e));
        }
    }


    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {

    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {

    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, AddressString ericssonMsisdn, AddressString ericssonVlrNo) {

    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {

    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {

    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {

    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {

    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic mapNoticeProblemDiagnostic) {

    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {

    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {

    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {

    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {

    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {

    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {

    }

    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {

    }

    @Override
    public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) {

    }

    @Override
    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {

    }

    @Override
    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {

    }

    @Override
    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInfoForLCSRequestIndication) {

    }

    @Override
    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponseIndication) {

    }
}
