package org.restcomm.protocols.ss7.map.load.sms.mt;

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
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.errors.AbsentSubscriberDiagnosticSM;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
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
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.restcomm.protocols.ss7.map.load.CsvWriter;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.restcomm.protocols.ss7.map.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.restcomm.protocols.ss7.map.service.sms.SipUriImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ApplicationPortAddressing16BitAddressImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
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
import org.restcomm.protocols.ss7.tcap.asn.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Client extends TestHarnessSmsMt {

    private static final Logger logger = LogManager.getLogger(Client.class);

    // TCAP
    private TCAPStack tcapStack;

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

    // SCCP
    SccpExtModuleImpl sccpExtModule;
    private SccpStackImpl sccpStack;

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

    static Long imsiForParams = 901405105680000L;

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

        Router router = this.sccpStack.getRouter();
        RouterExt routerExt = sccpExtModule.getRouterExt();
        SccpResource sccpResource = this.sccpStack.getSccpResource();

        sccpResource.addRemoteSpc(0, SERVER_SPC, 0, 0);
        sccpResource.addRemoteSsn(0, SERVER_SPC, SSN, 0, false);

        router.addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0, null);
        router.addMtp3Destination(1, 1, SERVER_SPC, SERVER_SPC, 0, 255, 255);
        router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, CLIENT_SPC, 0);
        routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, SERVER_SPC, 0);
        routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0, null);
        routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K",
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

    private void initiateMTSM() throws MAPException {
        NetworkIdState networkIdState = this.mapStack.getMAPProvider().getNetworkIdState(0);
        int executorCongestionLevel = this.mapStack.getMAPProvider().getExecutorCongestionLevel();
        if (!(networkIdState == null || networkIdState.isAvailable() && networkIdState.getCongLevel() <= 0
                && executorCongestionLevel <= 0)) {
            // congestion or unavailable
            logger.warn("**** Outgoing congestion control: MAP load test client: networkIdState=" + networkIdState
                    + ", executorCongestionLevel=" + executorCongestionLevel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                logger.error("InterruptedException when sending initiating MT SM");
            }
        }

        this.rateLimiterObj.acquire();

        // First create Dialog
        AddressString originAddressString = this.mapProvider.getMAPParameterFactory()
            .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990012345");
        AddressString destinationAddressString = this.mapProvider.getMAPParameterFactory()
            .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990067890");

        SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SSN, SCCP_CLIENT_ADDRESS);
        SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SSN, SCCP_SERVER_ADDRESS);
        MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(MAPApplicationContext
                .getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3),
            clientSccpAddress, originAddressString, serverSccpAddress, destinationAddressString);

        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        boolean sm_RP_PRI = true;
        AddressString serviceCentreAddress = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        boolean gprsSupportIndicator = false;
        SM_RP_MTI sM_RP_MTI = null;
        TypeOfNumber typeOfNumber = TypeOfNumber.InternationalNumber;
        NumberingPlanIdentification numberingPlanIdentification = NumberingPlanIdentification.ISDNTelephoneNumberingPlan;
        String addressValue = "491710460020";
        AddressField addressField = new AddressFieldImpl(typeOfNumber, numberingPlanIdentification, addressValue);
        SM_RP_SMEA sM_RP_SMEA = null;
        SMDeliveryNotIntended smDeliveryNotIntended = null;
        boolean ipSmGwGuidanceIndicator = false;
        IMSI imsi = null;
        boolean t4TriggerIndicator = false;
        boolean singleAttemptDelivery = false;
        // TeleserviceCode teleserviceCode = null; // teleservice must be absent in MAP version greater than 1
        String uriB = msisdn.getAddress() + "@restcomm.org";
        SipUri sipUriB;
        CorrelationID correlationID = null;
        boolean smsfSupportIndicator = false;

        Random rand = new Random();
        switch (rand.nextInt(10) + 1) {
            case 1:
                smDeliveryNotIntended = SMDeliveryNotIntended.getInstance(0);
                break;
            case 2:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                ipSmGwGuidanceIndicator = true;
                // correlationID contains the SIP-URI-B identifying the (MSISDN-less) destination user.
                // SIP-URI-A and HLR-ID shall be absent from this parameter.
                sipUriB = new SipUriImpl(uriB.getBytes(StandardCharsets.UTF_8));
                correlationID = new CorrelationIDImpl(null, null, sipUriB);
                // When UE shall be identified by a Correlation ID (SIP-URI-B)
                // the MSISDN shall take the dummy MSISDN value (see clause 3 of 3GPP TS 23.003)
                // ... the dummy MSISDN value composed of 15 digits set to 0 (encoded as an international E.164 number)
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                serviceCentreAddress = new AddressStringImpl(false, AddressNature.network_specific_number, NumberingPlan.reserved, "77777");
                smsfSupportIndicator = true;
                break;
            case 3:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                sM_RP_SMEA = new SM_RP_SMEAImpl(addressField);
                t4TriggerIndicator = true;
                // When SRISM is sent by the SMS-GMSC to the HLR following an T4 Submit Trigger (see 3GPP TS 23.682),
                // MSISDN may not be available. In this case the UE shall be identified by the IMSI
                imsi = new IMSIImpl(String.valueOf(imsiForParams));
                // and the MSISDN shall take the dummy MSISDN value (see clause 3 of 3GPP TS 23.003).
                // ... the dummy MSISDN value composed of 15 digits set to 0 (encoded as an international E.164 number)
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                singleAttemptDelivery = true;
                smsfSupportIndicator = true;
                break;
            case 4:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                sipUriB = new SipUriImpl(uriB.getBytes(StandardCharsets.UTF_8));
                // correlationID contains the SIP-URI-B identifying the (MSISDN-less) destination user.
                // SIP-URI-A and HLR-ID shall be absent from this parameter.
                correlationID = new CorrelationIDImpl(null, null, sipUriB);
                // When UE shall be identified by a Correlation ID (SIP-URI-B)
                // the MSISDN shall take the dummy MSISDN value (see clause 3 of 3GPP TS 23.003)
                // ... the dummy MSISDN value composed of 15 digits set to 0 (encoded as an international E.164 number)
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
                serviceCentreAddress = new AddressStringImpl(true, AddressNature.reserved_for_extension, NumberingPlan.private_plan, "5");
                break;
            case 5:
                smDeliveryNotIntended = SMDeliveryNotIntended.getInstance(1);
                break;
            case 6:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                smsfSupportIndicator = true;
                break;
            case 7:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(1);
                break;
            case 8:
                gprsSupportIndicator = true;
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                break;
            default:
                sM_RP_MTI = SM_RP_MTI.getInstance(0);
                break;
        }

        mapDialogSms.addSendRoutingInfoForSMRequest(msisdn, sm_RP_PRI, serviceCentreAddress, null,
            gprsSupportIndicator, sM_RP_MTI, sM_RP_SMEA, smDeliveryNotIntended, ipSmGwGuidanceIndicator,
            imsi, t4TriggerIndicator, singleAttemptDelivery, null, correlationID, smsfSupportIndicator);

        // nbConcurrentDialogs.incrementAndGet();

        // This will initiate the TC-BEGIN with INVOKE component
        mapDialogSms.send();

        this.csvWriter.incrementCounter(CREATED_DIALOGS);
    }

    private SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
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
            TestHarnessSmsMt.CLIENT_IP = args[3];
        }

        logger.info("CLIENT_IP={}", TestHarnessSmsMt.CLIENT_IP);

        if (args.length >= 5) {
            TestHarnessSmsMt.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        logger.info("CLIENT_PORT={}", TestHarnessSmsMt.CLIENT_PORT);

        if (args.length >= 6) {
            TestHarnessSmsMt.SERVER_IP = args[5];
        }

        logger.info("SERVER_IP={}", TestHarnessSmsMt.SERVER_IP);

        if (args.length >= 7) {
            TestHarnessSmsMt.SERVER_PORT = Integer.parseInt(args[6]);
        }

        logger.info("SERVER_PORT={}", TestHarnessSmsMt.SERVER_PORT);

        if (args.length >= 8) {
            TestHarnessSmsMt.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        logger.info("CLIENT_SPC={}", TestHarnessSmsMt.CLIENT_SPC);

        if (args.length >= 9) {
            TestHarnessSmsMt.SERVER_SPC = Integer.parseInt(args[8]);
        }

        logger.info("SERVER_SPC={}", TestHarnessSmsMt.SERVER_SPC);

        if (args.length >= 10) {
            TestHarnessSmsMt.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        logger.info("NETWORK_INDICATOR={}", TestHarnessSmsMt.NETWORK_INDICATOR);

        if (args.length >= 11) {
            TestHarnessSmsMt.SERVICE_INDICATOR = Integer.parseInt(args[10]);
        }

        logger.info("SERVICE_INDICATOR={}", TestHarnessSmsMt.SERVICE_INDICATOR);

        if (args.length >= 12) {
            TestHarnessSmsMt.SSN = Integer.parseInt(args[11]);
        }

        logger.info("SSN={}", TestHarnessSmsMt.SSN);

        if (args.length >= 13) {
            TestHarnessSmsMt.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        logger.info("ROUTING_CONTEXT={}", TestHarnessSmsMt.ROUTING_CONTEXT);

        if (args.length >= 14) {
            TestHarnessSmsMt.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[13]);
        }

        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT={}", TestHarnessSmsMt.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        if (args.length >= 15) {
            TestHarnessSmsMt.RAMP_UP_PERIOD = Integer.parseInt(args[14]);
        }

        logger.info("RAMP_UP_PERIOD={}", TestHarnessSmsMt.RAMP_UP_PERIOD);

        if (args.length >= 16) {
            TestHarnessSmsMt.SCCP_CLIENT_ADDRESS = args[15];
        }

        logger.info("SCCP_CLIENT_ADDRESS={}", TestHarnessSmsMt.SCCP_CLIENT_ADDRESS);

        if (args.length >= 17) {
            TestHarnessSmsMt.SCCP_SERVER_ADDRESS = args[16];
        }

        logger.info("SCCP_SERVER_ADDRESS={}", TestHarnessSmsMt.SCCP_SERVER_ADDRESS);

        if (args.length >= 18) {
            TestHarnessSmsMt.ROUTING_INDICATOR = RoutingIndicator.valueOf(Integer.parseInt(args[17]));
        }

        logger.info("ROUTING_INDICATOR={}", TestHarnessSmsMt.ROUTING_INDICATOR);

            if (args.length >= 19) {
            TestHarnessSmsMt.SENDING_MESSAGE_THREAD_COUNT = Integer.parseInt(args[18]);
        }

        logger.info("SENDING_MESSAGE_THREAD_COUNT={}", TestHarnessSmsMt.SENDING_MESSAGE_THREAD_COUNT);

        // logger.info("Number of calls to be completed = " + noOfCalls +
        // " Number of concurrent calls to be maintained = " +
        // noOfConcurrentCalls);

        NDIALOGS = noOfCalls;

        logger.info("NDIALOGS={}", NDIALOGS);

        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        logger.info("MAXCONCURRENTDIALOGS={}", MAXCONCURRENTDIALOGS);

        final Client client = new Client();
        client.endCount = TestHarnessSmsMt.RAMP_UP_PERIOD;

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(TestHarnessSmsMt.TEST_START_DELAY);

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

            }

            client.terminate();

        } catch (Exception e) {
            logger.error("Exception when starting stack of load class for MT Client", e);
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
        if (logger.isDebugEnabled()) {
            logger.warn("onErrorComponent for Dialog={} and invokeId={} MAPErrorMessage={}", mapDialog.getLocalDialogId(), invokeId, mapErrorMessage);
        }
        try {
            MAPApplicationContextName mapApplicationContextName = mapDialog.getApplicationContext().getApplicationContextName();
            if (mapApplicationContextName == MAPApplicationContextName.shortMsgMTRelayContext) {
                MAPDialogSms mapDialogSms = setReportSMDeliveryStatus();
                mapDialogSms.send();
            }

            Thread.sleep(500);
            // Create Dialog for MAP RSM
            AddressString originAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990012345");
            AddressString destAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SSN, "598991900032");

            MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
            MAPApplicationContextName mapAcn = MAPApplicationContextName.mwdMngtContext;
            MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);

            MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(mapAppContext, clientSccpAddress,
                    originAddressString, serverSccpAddress, destAddressString);
            IMSI imsi = new IMSIImpl(String.valueOf(imsiForParams));
            AlertReason alertReason = AlertReason.msPresent;
            boolean alertReasonIndicator = false;
            boolean additionalAlertReasonIndicator = false;
            Time maximumUeAvailabilityTime = null;

            Random rand = new Random();
            switch (rand.nextInt(3) + 1) {
                case 1:
                    alertReasonIndicator = true;
                    break;
                case 2:
                    additionalAlertReasonIndicator = true;
                    break;
                case 3:
                    AbsoluteTimeStamp ts = getAbsoluteTimeStamp();
                    maximumUeAvailabilityTime = new TimeImpl(ts.getYear(), ts.getMonth(), ts.getDay(), ts.getHour(), ts.getMinute(), ts.getSecond());
                    break;
            }

            mapDialogSms.addReadyForSMRequest(imsi, alertReason, alertReasonIndicator, null,
                    additionalAlertReasonIndicator, maximumUeAvailabilityTime);
            mapDialogSms.send();

        } catch (InterruptedException e) {
            logger.error("InterruptedException when sending sending MAP RSM");
        } catch (MAPException e) {
            logger.error("Error while processing onErrorResponse and/or sending ReportSMDeliveryStatusRequest ", e);
        }
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
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwardShortMessageRequestIndication) {

    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwardShortMessageResponseIndication) {

    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwardShortMessageRequestIndication) {

    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwardShortMessageResponseIndication) {

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwardShortMessageRequestIndication) {

    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwardShortMessageResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onMtForwardShortMessageResponse for DialogId={}", mtForwardShortMessageResponseIndication
                    .getMAPDialog().getLocalDialogId());
        }
    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMRequestIndication) {

    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onSendRoutingInfoForSMResponse for DialogId={}", sendRoutingInfoForSMResponseIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            // Get IMSI and Network Node Number from sendRoutingInfoForSMResponseIndication
            IMSI imsi = sendRoutingInfoForSMResponseIndication.getIMSI();
            LocationInfoWithLMSI locationInfoWithLMSI = sendRoutingInfoForSMResponseIndication.getLocationInfoWithLMSI();
            AddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
            // Create Dialog
            AddressString originAddressString = this.mapProvider.getMAPParameterFactory()
                .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990012345");

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SSN, networkNodeNumber.getAddress());

            MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
            MAPApplicationContextName mapAcn = MAPApplicationContextName.shortMsgMTRelayContext;
            MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);

            MAPDialogSms mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(mapAppContext, clientSccpAddress,
                originAddressString, serverSccpAddress, networkNodeNumber);

            SM_RP_DA sm_RP_DA = mapProvider.getMAPParameterFactory().createSM_RP_DA(imsi);
            AddressString serviceCentreAddressOA = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
            SM_RP_OA sm_RP_OA = mapProvider.getMAPParameterFactory().createSM_RP_OA_ServiceCentreAddressOA(serviceCentreAddressOA);
            AddressField originatingAddress = new AddressFieldImpl(TypeOfNumber.Alphanumeric, NumberingPlanIdentification.Unknown, "447");
            AbsoluteTimeStamp serviceCentreTimeStamp = getAbsoluteTimeStamp();
            int dcsVal = 4; // 0 = GSM7, 4 = GSM8, 8 = UCS2
            DataCodingScheme dcs = new DataCodingSchemeImpl(dcsVal);
            UserDataHeader udh = null;
            if (dcs.getCharacterSet() == CharacterSet.GSM8) {
                ApplicationPortAddressing16BitAddressImpl apa16 = new ApplicationPortAddressing16BitAddressImpl(16020, 0);
                udh = new UserDataHeaderImpl();
                udh.addInformationElement(apa16);
            }
            boolean moreMessagesToSend = false;
            boolean forwardedOrSpawned = false;
            boolean replyPathExists = false;
            boolean statusReportIndication = false;
            Charset gsm8Charset = Charset.defaultCharset();
            UserData userData = new UserDataImpl("Load test MT-SMS text", dcs, udh, gsm8Charset);
            ProtocolIdentifier pi = new ProtocolIdentifierImpl(0);
            SmsDeliverTpdu tpdu = new SmsDeliverTpduImpl(moreMessagesToSend, forwardedOrSpawned, replyPathExists, statusReportIndication, originatingAddress, pi, serviceCentreTimeStamp, userData);
            SmsSignalInfo sm_RP_UI = mapProvider.getMAPParameterFactory().createSmsSignalInfo(tpdu, gsm8Charset);
            Integer smDeliveryTimer = null;
            Time smDeliveryStartTime = null;
            boolean smsOverIPOnlyIndicator = false;
            CorrelationID correlationID = null;
            Time maximumRetransmissionTime = null;
            ISDNAddressString smsGmscAddress = null;
            DiameterIdentity gmscName = new DiameterIdentityImpl("msc04.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            NetworkNodeDiameterAddress smsGmscDiameterAddress = null;
            Random rand = new Random();
            switch (rand.nextInt(3) + 1) {
                case 1:
                    statusReportIndication = true;
                    tpdu = new SmsDeliverTpduImpl(moreMessagesToSend, forwardedOrSpawned, replyPathExists, statusReportIndication, originatingAddress, pi, serviceCentreTimeStamp, userData);
                    sm_RP_UI = mapProvider.getMAPParameterFactory().createSmsSignalInfo(tpdu, gsm8Charset);
                    smDeliveryTimer = 60;
                    smDeliveryStartTime = new TimeImpl(2024, 10, 7, 12, 29, 1);
                    maximumRetransmissionTime = new TimeImpl(2024, 10, 31, 23, 59, 59);
                    break;
                case 2:
                    moreMessagesToSend = true;
                    replyPathExists = true;
                    tpdu = new SmsDeliverTpduImpl(moreMessagesToSend, forwardedOrSpawned, replyPathExists, statusReportIndication, originatingAddress, pi, serviceCentreTimeStamp, userData);
                    sm_RP_UI = mapProvider.getMAPParameterFactory().createSmsSignalInfo(tpdu, gsm8Charset);
                    smsGmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
                    smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
                    break;
                case 3:
                    moreMessagesToSend = true;
                    forwardedOrSpawned = true;
                    replyPathExists = true;
                    statusReportIndication = true;
                    tpdu = new SmsDeliverTpduImpl(moreMessagesToSend, forwardedOrSpawned, replyPathExists, statusReportIndication, originatingAddress, pi, serviceCentreTimeStamp, userData);
                    sm_RP_UI = mapProvider.getMAPParameterFactory().createSmsSignalInfo(tpdu, gsm8Charset);
                    smDeliveryTimer = 60;
                    smDeliveryStartTime = new TimeImpl(2024, 12, 19, 10, 7, 21);
                    smsOverIPOnlyIndicator = true;
                    ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, "59899077937");
                    String uriA = msisdn.getAddress() + "@restcomm.org";
                    SipUri sipUriA;
                    SipUri sipUriB;
                    sipUriA = new SipUriImpl(uriA.getBytes(StandardCharsets.UTF_8));
                    sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
                    correlationID = new CorrelationIDImpl(imsi, sipUriA, sipUriB);
                    maximumRetransmissionTime = new TimeImpl(2024, 12, 31, 23, 59, 59);
                    smsGmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
                    smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
                    break;
            }

            mapDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, moreMessagesToSend, null, smDeliveryTimer,
                    smDeliveryStartTime, smsOverIPOnlyIndicator, correlationID, maximumRetransmissionTime, smsGmscAddress,
                    smsGmscDiameterAddress);

            mapDialogSms.send();

            this.csvWriter.incrementCounter(CREATED_DIALOGS);

        } catch (MAPException e) {
            logger.error("Error while sending SendRoutingInfoForSMRequest ", e);
        }
    }

    private static AbsoluteTimeStamp getAbsoluteTimeStamp() {
        Calendar cld = new GregorianCalendar();
        int year = cld.get(Calendar.YEAR);
        int mon = cld.get(Calendar.MONTH);
        int day = cld.get(Calendar.DAY_OF_MONTH);
        int h = cld.get(Calendar.HOUR);
        int m = cld.get(Calendar.MINUTE);
        int s = cld.get(Calendar.SECOND);
        int tz = cld.get(Calendar.ZONE_OFFSET);
        return new AbsoluteTimeStampImpl(year - 2000, mon, day, h, m, s, tz / 1000 / 60 / 15);
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequestIndication) {

    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusResponseIndication) {

    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onInformServiceCentreRequest for DialogId={}", informServiceCentreRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            MAPDialogSms mapDialogSms = setReportSMDeliveryStatus();
            mapDialogSms.send();

        } catch (MAPException e) {
            logger.error("MAP Exception while processing onInformServiceCentreRequest and/or sending ReportSMDeliveryStatusRequest ", e);
        } catch (Exception e) {
            logger.error("Exception while processing onInformServiceCentreRequest and/or sending ReportSMDeliveryStatusRequest", e);
        }


    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onAlertServiceCentreRequest for DialogId={}", alertServiceCentreRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            MAPDialogSms mapDialogSms = alertServiceCentreRequestIndication.getMAPDialog();
            ReturnResultLast returnResultLast = new ReturnResultLastImpl();
            returnResultLast.setInvokeId(alertServiceCentreRequestIndication.getInvokeId());
            mapDialogSms.sendReturnResultLastComponent(returnResultLast);
            mapDialogSms.close(false);
            // start the MT-SM process again now that's reported available
            initiateMTSM();
        } catch (MAPException e) {
            logger.error("MAP Exception while processing onAlertServiceCentreRequest ", e);
        } catch (Exception e) {
            logger.error("Exception while processing onAlertServiceCentreRequest.", e);
        }
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

                    initiateMTSM();
                }
            } catch (MAPException ex) {
                logger.error("Exception when sending a new MAP dialog", ex);
            }
        }

    }

    private MAPDialogSms setReportSMDeliveryStatus() {
        MAPDialogSms mapDialogSms = null;
        try {
            AddressString originAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990012345");
            AddressString destinationAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "598990067890");

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SSN, SCCP_SERVER_ADDRESS);
            mapDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(MAPApplicationContext
                            .getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3),
                    clientSccpAddress, originAddressString, serverSccpAddress, destinationAddressString);

            ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "59899077937");
            AddressString serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
            SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
            Integer absentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC.getCode();
            MAPExtensionContainer extensionContainer = null;
            boolean gprsSupportIndicator = false;
            boolean deliveryOutcomeIndicator = false;
            SMDeliveryOutcome additionalSMDeliveryOutcome = null;
            Integer additionalAbsentSubscriberDiagnosticSM = null;
            boolean ipSmGwIndicator = false;
            SMDeliveryOutcome ipSmGwSMDeliveryOutcome = null;
            Integer ipSmGwAbsentSubscriberDiagnosticSM = null;
            IMSI imsi = null;
            boolean singleAttemptDelivery = false;
            SipUri sipUriA;
            SipUri sipUriB;
            CorrelationID correlationID = null;
            boolean smsf3gppDeliveryOutcomeIndicator = false;
            SMDeliveryOutcome smsf3gppDeliveryOutcome = null;
            Integer smsf3gppAbsentSubscriberDiagnosticSM = null;
            boolean smsfNon3gppDeliveryOutcomeIndicator = false;
            SMDeliveryOutcome smsfNon3gppDeliveryOutcome = null;
            Integer smsfNon3gppAbsentSubscriberDiagnosticSM = null;

            Random rand = new Random();
            int param = rand.nextInt(4) + 1;
            switch (param) {
                case 1:
                    gprsSupportIndicator = true;
                    deliveryOutcomeIndicator = true;
                    additionalSMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                    additionalAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheSGSN.getCode();
                    ipSmGwIndicator = true;
                    ipSmGwSMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                    ipSmGwAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.NoResponseViaTheIP_SM_GW.getCode();
                    imsi = new IMSIImpl(String.valueOf(imsiForParams));
                    break;
                case 2:
                    singleAttemptDelivery = true;
                    imsi = new IMSIImpl(String.valueOf(imsiForParams));
                    String uriA = msisdn.getAddress() + "@restcomm.org";
                    sipUriA = new SipUriImpl(uriA.getBytes(StandardCharsets.UTF_8));
                    sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
                    correlationID = new CorrelationIDImpl(imsi, sipUriA, sipUriB);
                    break;
                case 3:
                    smsf3gppDeliveryOutcomeIndicator = true;
                    smsf3gppDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                    smsf3gppAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.DeregisteredInTheHLRForNonGPRS.getCode();
                    smsfNon3gppDeliveryOutcomeIndicator = true;
                    smsfNon3gppDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
                    smsfNon3gppAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.MSPurgedForNonGPRS.getCode();
                    break;
                case 4:
                    break;
            }

            mapDialogSms.addReportSMDeliveryStatusRequest(msisdn, serviceCentreAddress, sMDeliveryOutcome, absentSubscriberDiagnosticSM,
                    extensionContainer, gprsSupportIndicator, deliveryOutcomeIndicator, additionalSMDeliveryOutcome,
                    additionalAbsentSubscriberDiagnosticSM, ipSmGwIndicator, ipSmGwSMDeliveryOutcome, ipSmGwAbsentSubscriberDiagnosticSM, imsi,
                    singleAttemptDelivery, correlationID, smsf3gppDeliveryOutcomeIndicator, smsf3gppDeliveryOutcome, smsf3gppAbsentSubscriberDiagnosticSM,
                    smsfNon3gppDeliveryOutcomeIndicator, smsfNon3gppDeliveryOutcome, smsfNon3gppAbsentSubscriberDiagnosticSM);

        } catch (MAPException e) {
            logger.error("MAP Exception while creating MAP dialog for ReportSMDeliveryStatusRequest.", e);
        } catch (Exception e) {
            logger.error("Exception while creating MAP dialog for ReportSMDeliveryStatusRequest.", e);
        }
        return mapDialogSms;
    }
}
