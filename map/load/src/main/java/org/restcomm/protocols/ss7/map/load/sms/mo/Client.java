package org.restcomm.protocols.ss7.map.load.sms.mo;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.map.load.CsvWriter;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.restcomm.protocols.ss7.map.service.sms.SM_RP_OAImpl;
import org.restcomm.protocols.ss7.map.service.sms.SipUriImpl;
import org.restcomm.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ValidityPeriodImpl;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Client extends TestHarnessSmsMo {

    private static final Logger logger = LogManager.getLogger(Client.class);

    // TCAP
    private TCAPStack tcapStack;

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

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
        sctpManagement.addAssociation(CLIENT_IP, CLIENT_PORT, SERVER_IP, SERVER_PORT, CLIENT_ASSOCIATION_NAME, ipChannelType,
                null);
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
        logger.info("ASP={}", asp);

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
        this.sccpResource.addRemoteSsn(0, SERVER_SPC, SSN, 0, false);

        this.router.addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0, null);
        this.router.addMtp3Destination(1, 1, SERVER_SPC, SERVER_SPC, 0, 255, 255);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
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
        this.tcapStack = new TCAPStackImpl("Test", this.sccpStack.getSccpProvider(), SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {

        // this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), SSN);
        this.mapStack = new MAPStackImpl("TestClient", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSms().activate();

        this.mapStack.start();
    }

    private void initiateSMS() throws MAPException {
        NetworkIdState networkIdState = this.mapStack.getMAPProvider().getNetworkIdState(0);
        int executorCongestionLevel = this.mapStack.getMAPProvider().getExecutorCongestionLevel();
        if (!(networkIdState == null
                || networkIdState.isAvailable() && networkIdState.getCongLevel() <= 0 && executorCongestionLevel <= 0)) {
            // congestion or unavailable
            logger.warn("**** Outgoing congestion control: MAP load test client: networkIdState={}, executorCongestionLevel={}", networkIdState, executorCongestionLevel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            }
        }

        this.rateLimiterObj.acquire();

        // First create Dialog
        AddressString origRef = this.mapProvider.getMAPParameterFactory()
                .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        AddressString destRef = this.mapProvider.getMAPParameterFactory()
                .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990012345");

        SccpAddress clientSccpAddress = createSccpAddressForMoSmsClient(ROUTING_INDICATOR, CLIENT_SPC, SSN, SCCP_CLIENT_ADDRESS);
        SccpAddress serverSccpAddress = createSccpAddressForMoSmsClient(ROUTING_INDICATOR, SERVER_SPC, SSN, SCCP_SERVER_ADDRESS);
        MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(MAPApplicationContext
                .getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version3),
                clientSccpAddress, origRef, serverSccpAddress, destRef);

        AddressString serviceCentreAddressDA = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        SM_RP_DA sm_rp_da = new SM_RP_DAImpl(serviceCentreAddressDA);
        ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory()
            .createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");
        SM_RP_OAImpl sm_rp_oa = new SM_RP_OAImpl();
        sm_rp_oa.setMsisdn(msisdn);
        boolean rejectDuplicates = true;
        boolean replyPathExists = false;
        boolean statusReportRequest = true;
        int messageReference = 187;
        AddressField destinationAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
            NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "59899077937");
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(0);
        ValidityPeriod validityPeriod = new ValidityPeriodImpl(3);
        DataCodingScheme dataCodingScheme = new DataCodingSchemeImpl(0);
        UserDataHeader userDataHeader = new UserDataHeaderImpl();
        Charset gsm8Charset = Charset.defaultCharset();
        UserData userData = new UserDataImpl("SMS load test", dataCodingScheme, userDataHeader, gsm8Charset);
        SmsTpduImpl smsTpdu = new SmsSubmitTpduImpl(rejectDuplicates, replyPathExists, statusReportRequest, messageReference, destinationAddress,
            protocolIdentifier, validityPeriod, userData);
        SmsSignalInfo sm_rp_ui = new SmsSignalInfoImpl(smsTpdu, gsm8Charset);
        IMSI imsi = null;
        IMSI hlrId;
        SipUri sipUriA;
        SipUri sipUriB;
        CorrelationID correlationID = null;
        SMDeliveryOutcome smDeliveryOutcome = null;
        Random rand = new Random();
        switch (rand.nextInt(6) + 1) {
            case 1:
                // A Dummy MSISDN value is used for MSISDN-less SMS in IMS.
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                sm_rp_oa.setMsisdn(msisdn);
                // In the  MSISDN-less SMS in IMS case the originating user is identified by SIP-URI-A of the correlationID parameter.
                hlrId = new IMSIImpl(String.valueOf(901405105680001L));
                sipUriA = new SipUriImpl("sip:kbza@acme.com".getBytes(StandardCharsets.UTF_8));
                sipUriB = new SipUriImpl("sip:fer@restcomm.org".getBytes(StandardCharsets.UTF_8));
                correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
                smDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                break;
            case 2:
                // A Dummy MSISDN value is used for MSISDN-less SMS in IMS.
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                sm_rp_oa.setMsisdn(msisdn);
                // In the  MSISDN-less SMS in IMS case the originating user is identified by SIP-URI-A of the correlationID parameter.
                hlrId = new IMSIImpl(String.valueOf(901405105680002L));
                sipUriA = new SipUriImpl("sip:kbza@acme.com".getBytes(StandardCharsets.UTF_8));
                sipUriB = new SipUriImpl("sip:fer@restcomm.org".getBytes(StandardCharsets.UTF_8));
                correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
                smDeliveryOutcome = SMDeliveryOutcome.memoryCapacityExceeded;
                break;
            case 3:
                // A Dummy MSISDN value is used for MSISDN-less SMS in IMS.
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                sm_rp_oa.setMsisdn(msisdn);
                // In the  MSISDN-less SMS in IMS case the originating user is identified by SIP-URI-A of the correlationID parameter.
                hlrId = new IMSIImpl(String.valueOf(901405105680000L));
                sipUriA = new SipUriImpl("sip:kbza@acme.com".getBytes(StandardCharsets.UTF_8));
                sipUriB = new SipUriImpl("sip:fer@restcomm.org".getBytes(StandardCharsets.UTF_8));
                correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
                smDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                break;
            case 4:
                imsi = new IMSIImpl(String.valueOf(901405105680000L));
                break;
            case 5:
                imsi = new IMSIImpl(String.valueOf(901405105680102L));
                break;
            default:
                break;
        }

        mapDialogSms.addMoForwardShortMessageRequest(sm_rp_da, sm_rp_oa, sm_rp_ui, null, imsi, correlationID, smDeliveryOutcome);

        // nbConcurrentDialogs.incrementAndGet();

        // This will initiate the TC-BEGIN with INVOKE component
        mapDialogSms.send();

        this.csvWriter.incrementCounter(CREATED_DIALOGS);
    }

    private SccpAddress createSccpAddressForMoSmsClient(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
        BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = SSN;
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

        logger.info("IpChannelType={}", ipChannelType);

        if (args.length >= 4) {
            TestHarnessSmsMo.CLIENT_IP = args[3];
        }

        logger.info("CLIENT_IP={}", TestHarnessSmsMo.CLIENT_IP);

        if (args.length >= 5) {
            TestHarnessSmsMo.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        logger.info("CLIENT_PORT={}", TestHarnessSmsMo.CLIENT_PORT);

        if (args.length >= 6) {
            TestHarnessSmsMo.SERVER_IP = args[5];
        }

        logger.info("SERVER_IP={}", TestHarnessSmsMo.SERVER_IP);

        if (args.length >= 7) {
            TestHarnessSmsMo.SERVER_PORT = Integer.parseInt(args[6]);
        }

        logger.info("SERVER_PORT={}", TestHarnessSmsMo.SERVER_PORT);

        if (args.length >= 8) {
            TestHarnessSmsMo.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        logger.info("CLIENT_SPC={}", TestHarnessSmsMo.CLIENT_SPC);

        if (args.length >= 9) {
            TestHarnessSmsMo.SERVER_SPC = Integer.parseInt(args[8]);
        }

        logger.info("SERVER_SPC={}", TestHarnessSmsMo.SERVER_SPC);

        if (args.length >= 10) {
            TestHarnessSmsMo.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        logger.info("NETWORK_INDICATOR={}", TestHarnessSmsMo.NETWORK_INDICATOR);

        if (args.length >= 11) {
            TestHarnessSmsMo.SERVICE_INDICATOR = Integer.parseInt(args[10]);
        }

        logger.info("SERVICE_INDICATOR={}", TestHarnessSmsMo.SERVICE_INDICATOR);

        if (args.length >= 12) {
            TestHarnessSmsMo.SSN = Integer.parseInt(args[11]);
        }

        logger.info("SSN={}", TestHarnessSmsMo.SSN);

        if (args.length >= 13) {
            TestHarnessSmsMo.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        logger.info("ROUTING_CONTEXT={}", TestHarnessSmsMo.ROUTING_CONTEXT);

        if (args.length >= 14) {
            TestHarnessSmsMo.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[13]);
        }

        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT={}", TestHarnessSmsMo.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        if (args.length >= 15) {
            TestHarnessSmsMo.RAMP_UP_PERIOD = Integer.parseInt(args[14]);
        }

        logger.info("RAMP_UP_PERIOD={}", TestHarnessSmsMo.RAMP_UP_PERIOD);

        if (args.length >= 16) {
            TestHarnessSmsMo.SCCP_CLIENT_ADDRESS = args[15];
        }

        logger.info("SCCP_CLIENT_ADDRESS={}", TestHarnessSmsMo.SCCP_CLIENT_ADDRESS);

        if (args.length >= 17) {
            TestHarnessSmsMo.SCCP_SERVER_ADDRESS = args[16];
        }

        logger.info("SCCP_SERVER_ADDRESS={}", TestHarnessSmsMo.SCCP_SERVER_ADDRESS);

        if (args.length >= 18) {
            TestHarnessSmsMo.ROUTING_INDICATOR = RoutingIndicator.valueOf(Integer.parseInt(args[17]));
        }

        logger.info("ROUTING_INDICATOR={}", TestHarnessSmsMo.ROUTING_INDICATOR);

            if (args.length >= 19) {
            TestHarnessSmsMo.SENDING_MESSAGE_THREAD_COUNT = Integer.parseInt(args[18]);
        }

        logger.info("SENDING_MESSAGE_THREAD_COUNT={}", TestHarnessSmsMo.SENDING_MESSAGE_THREAD_COUNT);

        // logger.info("Number of calls to be completed = " + noOfCalls +
        // " Number of concurrent calls to be maintained = " +
        // noOfConcurrentCalls);

        NDIALOGS = noOfCalls;

        logger.info("NDIALOGS={}", NDIALOGS);

        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        logger.info("MAXCONCURRENTDIALOGS={}", MAXCONCURRENTDIALOGS);

        final Client client = new Client();
        client.endCount = TestHarnessSmsMo.RAMP_UP_PERIOD;

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(TestHarnessSmsMo.TEST_START_DELAY);

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
                // }// end of while (client.nbConcurrentDialogs.intValue() >= MAXCONCURRENTDIALOGS)

                //if (client.endCount < 0) {
                //    client.start = System.currentTimeMillis();
                //    client.prev = client.start;
                    // logger.warn("StartTime = " + client.start);
                //}

               // client.initiateUSSD();
            }

            client.terminate();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onErrorComponent
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long,
     * org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage)
     */
    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        logger.error("onErrorComponent for Dialog={} and invokeId={} MAPErrorMessage={}", mapDialog.getLocalDialogId(), invokeId, mapErrorMessage);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long, org.restcomm.protocols.ss7.tcap.asn.comp.Problem)
     */
    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        logger.error("onRejectComponent for Dialog={} and invokeId={} Problem={} isLocalOriginated={}", mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long)
     */
    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        logger.error("onInvokeTimeout for Dialog={} and invokeId={}", mapDialog.getLocalDialogId(), invokeId);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogDelimiter
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogDelimiter for DialogId={}", mapDialog.getLocalDialogId());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogRequest
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.AddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogRequest for DialogId={} DestinationReference={} OriginReference={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogRequestEricsson
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.AddressString, org.restcomm.protocols.ss7.map.api.primitives.IMSI,
     * org.restcomm.protocols.ss7.map.api.primitives.AddressString)
     */
    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, AddressString arg3,
                                        AddressString arg4) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogRequest for DialogId={} DestinationReference={} OriginReference={} ", mapDialog.getLocalDialogId(), destReference, origReference);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogAccept( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogAccept for DialogId={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), extensionContainer);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogReject( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason, org.restcomm.protocols.ss7.map.api.dialog.MAPProviderError,
     * org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext,
                               MAPExtensionContainer extensionContainer) {
        logger.error("onDialogReject for DialogId={} MAPRefuseReason={} ApplicationContextName={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer);
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogUserAbort
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        logger.error("onDialogUserAbort for DialogId={} MAPUserAbortChoice={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), userReason, extensionContainer);
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogProviderAbort
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
            MAPExtensionContainer extensionContainer) {
        logger.error("onDialogProviderAbort for DialogId={} MAPAbortProviderReason={} MAPAbortSource={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer);
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogClose(org .mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug("DialogClose for Dialog={}", mapDialog.getLocalDialogId());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogNotice( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic)
     */
    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        logger.error("onDialogNotice for DialogId={} MAPNoticeProblemDiagnostic={} ", mapDialog.getLocalDialogId(), noticeProblemDiagnostic);
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogResease
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogRelease for DialogId={}", mapDialog.getLocalDialogId());
        }
        this.csvWriter.incrementCounter(SUCCESSFUL_DIALOGS);
        this.endCount++;

        if (this.endCount < NDIALOGS) {
            if ((this.endCount % 10000) == 0) {
                long current = System.currentTimeMillis();
                float sec = (float) (current - prev) / 1000f;
                prev = current;
                logger.warn("Completed 10000 Dialogs, dialogs per second: {}", 10000 / sec);
            }
        } else {
            if (!endReportPrinted) {
                endReportPrinted = true;
                long current = System.currentTimeMillis();
                logger.warn("Start Time = {}", start);
                logger.warn("Current Time = {}", current);
                float sec = (float) (current - start) / 1000f;

                logger.warn("Total time in sec = {}", sec);
                logger.warn("Throughput = {}", NDIALOGS / sec);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogTimeout
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        logger.error("onDialogTimeout for DialogId={}", mapDialog.getLocalDialogId());
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public MAPProvider getMAPProvider() {
        return null;
    }

    @Override
    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        return null;
    }

    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public MAPDialogSms createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference, Long localTransactionId) throws MAPException {
        return null;
    }

    @Override
    public MAPDialogSms createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference) throws MAPException {
        return null;
    }

    @Override
    public void addMAPServiceListener(MAPServiceSmsListener mapServiceSmsListener) {

    }

    @Override
    public void removeMAPServiceListener(MAPServiceSmsListener mapServiceSmsListener) {

    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwardShortMessageResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onMoForwardShortMessageResponse for DialogId={}", moForwardShortMessageResponseIndication
                    .getMAPDialog().getLocalDialogId());
            try {
                SmsSignalInfo sm_rp_ui = moForwardShortMessageResponseIndication.getSM_RP_UI();
                SmsTpduType smsTpduType = null;
                if (sm_rp_ui != null) {
                    SmsTpdu smsTpdu = sm_rp_ui.decodeTpdu(true);
                    smsTpduType = smsTpdu.getSmsTpduType();
                }
                MAPMessageType mt = moForwardShortMessageResponseIndication.getMessageType();
                if (logger.isDebugEnabled()) {
                    logger.debug("onMoForwardShortMessageResponse for invokeId=%d{}, sm_rp_ui= {}, smsTpduType={}, message type={}", moForwardShortMessageResponseIndication
                            .getInvokeId(), sm_rp_ui, smsTpduType, mt);
                }

            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onReportSMDeliveryStatusRequest for DialogId={}", reportSMDeliveryStatusRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = reportSMDeliveryStatusRequestIndication.getInvokeId();
            MAPDialogSms mapDialogSms = reportSMDeliveryStatusRequestIndication.getMAPDialog();
            ISDNAddressString storedMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
            mapDialogSms.addReportSMDeliveryStatusResponse(invokeId, storedMsisdn, null);
            mapDialogSms.close(false);
        } catch (MAPException e) {
            logger.error("Error onReportSMDeliveryStatusRequest", e);
        }
    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwardShortMessageRequestIndication) {

    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwardShortMessageResponseIndication) {

    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwardShortMessageRequestIndication) {

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwardShortMessageRequestIndication) {

    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwardShortMessageResponseIndication) {

    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMRequestIndication) {

    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMResponseIndication) {

    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusResponseIndication) {

    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreRequestIndication) {

    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreRequestIndication) {

    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreResponseIndication) {

    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest readyForSMRequest) {

    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse readyForSMResponse) {

    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest noteSubscriberPresentRequest) {

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

                    initiateSMS();
                }
            } catch (MAPException ex) {
                logger.error("Exception when sending a new MAP dialog", ex);
            }
        }

    }
}
