package org.restcomm.protocols.ss7.map.load.mobility_management.gprs;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
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
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ExtSupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SMSRegisterRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.load.CsvWriter;
import org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ExtSupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ISRInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SuperChargerInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedRATTypesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.restcomm.protocols.ss7.map.load.mobility_management.gprs.TestHarnessMobilityManagement.SGSN_SSN;
import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Client extends org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement {

    private static Logger logger = Logger.getLogger(Client.class);

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

    Long imsiForPurge = 901405105680000L;

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

        this.router.addMtp3ServiceAccessPoint(1, 1, CLIENT_SPC, NETWORK_INDICATOR, 0, null);
        this.router.addMtp3Destination(1, 1, SERVER_SPC, SERVER_SPC, 0, 255, 255);
        this.router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

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
        this.tcapStack = new TCAPStackImpl("Test", this.sccpStack.getSccpProvider(), SGSN_SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        // this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), SGSN_SSN);
        this.mapStack = new MAPStackImpl("TestClient", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();
        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceMobility().activate();
        this.mapStack.start();
    }

    private void initiateMobility() throws MAPException {
        NetworkIdState networkIdState = this.mapStack.getMAPProvider().getNetworkIdState(0);
        int executorCongestionLevel = this.mapStack.getMAPProvider().getExecutorCongestionLevel();
        if (!(networkIdState == null
                || networkIdState.isAvailable() && networkIdState.getCongLevel() <= 0 && executorCongestionLevel <= 0)) {
            // congestion or unavailable
            logger.warn("**** Outgoing congestion control: MAP load test client: networkIdState=" + networkIdState
                    + ", executorCongestionLevel=" + executorCongestionLevel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.rateLimiterObj.acquire();

        // Send Authentication Info
        sendAuthenticationInfoRequest("901405105682583");
    }

    private SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = SGSN_SSN;
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
        if (args.length >= 3 && args[2].toLowerCase().equals("tcp")) {
            ipChannelType = IpChannelType.TCP;
        } else {
            ipChannelType = IpChannelType.SCTP;
        }

        System.out.println("IpChannelType=" + ipChannelType);

        if (args.length >= 4) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_IP = args[3];
        }

        System.out.println("CLIENT_IP=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_IP);

        if (args.length >= 5) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        System.out.println("CLIENT_PORT=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_PORT);

        if (args.length >= 6) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_IP = args[5];
        }

        System.out.println("SERVER_IP=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_IP);

        if (args.length >= 7) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_PORT = Integer.parseInt(args[6]);
        }

        System.out.println("SERVER_PORT=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_PORT);

        if (args.length >= 8) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        System.out.println("CLIENT_SPC=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.CLIENT_SPC);

        if (args.length >= 9) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_SPC = Integer.parseInt(args[8]);
        }

        System.out.println("SERVER_SPC=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVER_SPC);

        if (args.length >= 10) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        System.out.println("NETWORK_INDICATOR=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.NETWORK_INDICATOR);

        if (args.length >= 11) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVICE_INDICATOR = Integer.parseInt(args[10]);
        }

        System.out.println("SERVICE_INDICATOR=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SERVICE_INDICATOR);

        if (args.length >= 12) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SSN = Integer.parseInt(args[11]);
        }

        System.out.println("SSN=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SSN);

        if (args.length >= 13) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        System.out.println("ROUTING_CONTEXT=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.ROUTING_CONTEXT);

        if (args.length >= 14) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[13]);
        }

        System.out.println("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        if (args.length >= 15) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.RAMP_UP_PERIOD = Integer.parseInt(args[14]);
        }

        System.out.println("RAMP_UP_PERIOD=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.RAMP_UP_PERIOD);

        if (args.length >= 16) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SCCP_CLIENT_ADDRESS = args[15];
        }

        System.out.println("SCCP_CLIENT_ADDRESS=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SCCP_CLIENT_ADDRESS);

        if (args.length >= 17) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SCCP_SERVER_ADDRESS = args[16];
        }

        System.out.println("SCCP_SERVER_ADDRESS=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SCCP_SERVER_ADDRESS);

        if (args.length >= 18) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.ROUTING_INDICATOR = RoutingIndicator.valueOf(Integer.parseInt(args[17]));
        }

        System.out.println("ROUTING_INDICATOR=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.ROUTING_INDICATOR);

        if (args.length >= 19) {
            org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SENDING_MESSAGE_THREAD_COUNT = Integer.parseInt(args[18]);
        }

        System.out.println("SENDING_MESSAGE_THREAD_COUNT=" + org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.SENDING_MESSAGE_THREAD_COUNT);

        // logger.info("Number of calls to be completed = " + noOfCalls +
        // " Number of concurrent calls to be maintained = " +
        // noOfConcurrentCalls);

        NDIALOGS = noOfCalls;

        System.out.println("NDIALOGS=" + NDIALOGS);

        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        System.out.println("MAXCONCURRENTDIALOGS=" + MAXCONCURRENTDIALOGS);

        final Client client = new Client();
        client.endCount = org.restcomm.protocols.ss7.map.load.mobility_management.cs.TestHarnessMobilityManagement.RAMP_UP_PERIOD;

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(TestHarnessMobilityManagement.TEST_START_DELAY);

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

                    initiateMobility();
                }
            } catch (MAPException ex) {
                logger.error("Exception when sending a new MAP dialog", ex);
            }
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
        logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
                mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long, org.restcomm.protocols.ss7.tcap.asn.comp.Problem)
     */
    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s",
                mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long)
     */
    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));
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
            logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
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
            logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
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
            logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
                    mapDialog.getLocalDialogId(), destReference, origReference));
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
            logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), extensionContainer));
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
        logger.error(String.format(
                "onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
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
        logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
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
        logger.error(String.format(
                "onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
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
            logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
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
        logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
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
            logger.debug(String.format("onDialogRelease for DialogId=%d", mapDialog.getLocalDialogId()));
        }
        this.csvWriter.incrementCounter(SUCCESSFUL_DIALOGS);
        this.endCount++;

        if (this.endCount < NDIALOGS) {
            if ((this.endCount % 10000) == 0) {
                long current = System.currentTimeMillis();
                float sec = (float) (current - prev) / 1000f;
                prev = current;
                logger.warn("Completed 10000 Dialogs, dialogs per second: " + (float) (10000 / sec));
            }
        } else {
            if (!endReportPrinted) {
                endReportPrinted = true;
                long current = System.currentTimeMillis();
                logger.warn("Start Time = " + start);
                logger.warn("Current Time = " + current);
                float sec = (float) (current - start) / 1000f;

                logger.warn("Total time in sec = " + sec);
                logger.warn("Throughput = " + (float) (NDIALOGS / sec));
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
        logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getLocalDialogId()));
        this.csvWriter.incrementCounter(ERROR_DIALOGS);
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
    public void onMAPMessage(MAPMessage mapMessage) {

    }

    @Override
    public MAPDialogMobility createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference, Long localTrId) throws MAPException {
        return null;
    }

    @Override
    public MAPDialogMobility createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference) throws MAPException {
        return null;
    }

    @Override
    public void addMAPServiceListener(MAPServiceMobilityListener mapServiceMobilityListener) {

    }

    @Override
    public void removeMAPServiceListener(MAPServiceMobilityListener mapServiceMobilityListener) {

    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest sendAuthenticationInfoRequestIndication) {
        logger.error(String.format("Received SendAuthenticationInfoRequest over DialogId=%d", sendAuthenticationInfoRequestIndication
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onSendAuthenticationInfoResponse for DialogId=%d", sendAuthenticationInfoResponseIndication
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            ArrayList<AuthenticationQuintuplet> authenticationQuintuplets = sendAuthenticationInfoResponseIndication.
                    getAuthenticationSetList().getQuintupletList().getAuthenticationQuintuplets();
            SccpAddress hlrAddress = sendAuthenticationInfoResponseIndication.getMAPDialog().getRemoteAddress();

            // Create Dialog
            AddressString originAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
            AddressString destAddressString = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SGSN_SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

            MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
            MAPApplicationContextName mapAcn = MAPApplicationContextName.gprsLocationUpdateContext;
            MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
            MAPDialogMobility mapDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                    originAddressString, serverSccpAddress, destAddressString);

            IMSI imsi;
            byte[] rand = sendAuthenticationInfoResponseIndication.getAuthenticationSetList().getQuintupletList().getAuthenticationQuintuplets().get(0).getRand();
            if (Arrays.equals(rand, new byte[]{(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                    (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x75})) {
                imsi = new IMSIImpl("901405105682021");
            } else {
                imsi = new IMSIImpl("901405105682583");
            }

            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    "491710490000");
            GSNAddress sgsnAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
            boolean solsaSupportIndicator = false;
            Boolean sendSubscriberData = true;
            SuperChargerInfo superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl(sendSubscriberData);
            boolean gprsEnhancementsSupportIndicator = true;
            SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true, true, false);
            boolean oCsi = false;
            boolean dCsi = false;
            boolean vtCsi = false;
            boolean tCsi = false;
            boolean mtSMSCsi = true;
            boolean mgCsi = true;
            boolean psiEnhancements = true;
            OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(oCsi,dCsi,vtCsi,tCsi, mtSMSCsi, mgCsi, psiEnhancements);
            boolean smsCallBarringSupportIndicator = true;
            boolean utran = true;
            boolean geran = true;
            boolean gan = false;
            boolean i_hspa_evolution = true;
            boolean e_utran = true;
            boolean nb_iot = true;
            SupportedRATTypes supportedRATTypesIndicator = new SupportedRATTypesImpl(utran, geran, gan, i_hspa_evolution, e_utran, nb_iot);
            boolean odbAllApn = false;
            boolean odbHPLMNApn = false;
            boolean odbVPLMNApn = false;
            boolean odbAllOg = false;
            boolean odbAllInternationalOg = false;
            boolean odbAllIntOgNotToHPLMNCountry = false;
            boolean odbAllInterzonalOg = false;
            boolean odbAllInterzonalOgNotToHPLMNCountry = false;
            boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry = false;
            boolean regSub = false;
            boolean trace = false;
            boolean lcsAllPrivExcep = true;
            boolean lcsUniversal = true;
            boolean lcsCallSessionRelated = true;
            boolean lcsCallSessionUnrelated = true;
            boolean lcsPLMNOperator = true;
            boolean lcsServiceType = true;
            boolean lcsAllMOLRSS = true;
            boolean lcsBasicSelfLocation = true;
            boolean lcsAutonomousSelfLocation = true;
            boolean lcsTransferToThirdParty = true;
            boolean smMoPp = true;
            boolean barringOutgoingCalls = true;
            boolean baoc = false;
            boolean boic = false;
            boolean boicExHC = false;
            boolean localTimeZoneRetrieval = true;
            boolean additionalMsisdn = true;
            boolean smsInMME = true;
            boolean smsInSGSN = true;
            boolean ueReachabilityNotification = true;
            boolean stateLocationInformationRetrieval = true;
            boolean partialPurge = true;
            boolean gddInSGSN = true;
            boolean sgsnCAMELCapability = true;
            boolean pcscfRestoration = true;
            boolean dedicatedCoreNetworks = true;
            boolean nonIPPDNTypeAPNs = true;
            boolean nonIPPDPTypeAPNs = true;
            boolean nrAsSecondaryRAT = true;
            SupportedFeatures supportedFeatures = new SupportedFeaturesImpl(odbAllApn, odbHPLMNApn, odbVPLMNApn, odbAllOg, odbAllInternationalOg,
                    odbAllIntOgNotToHPLMNCountry, odbAllInterzonalOg, odbAllInterzonalOgNotToHPLMNCountry,
                    odbAllInterzonalOgandInternatOgNotToHPLMNCountry, regSub, trace, lcsAllPrivExcep, lcsUniversal,
                    lcsCallSessionRelated, lcsCallSessionUnrelated, lcsPLMNOperator, lcsServiceType, lcsAllMOLRSS,
                    lcsBasicSelfLocation, lcsAutonomousSelfLocation, lcsTransferToThirdParty, smMoPp, barringOutgoingCalls, baoc,
                    boic, boicExHC, localTimeZoneRetrieval, additionalMsisdn, smsInMME, smsInSGSN, ueReachabilityNotification,
                    stateLocationInformationRetrieval, partialPurge, gddInSGSN, sgsnCAMELCapability,
                    pcscfRestoration, dedicatedCoreNetworks, nonIPPDNTypeAPNs, nonIPPDPTypeAPNs,
                    nrAsSecondaryRAT);
            boolean tAdsDataRetrieval = true;
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions = true;
            boolean cancellationTypeInitialAttach = true;
            boolean misdnlessOperationSupported = true;
            boolean updateOfHomogeneousSupportOfIMSVoiceOverPSSessions = true;
            boolean resetIdsSupported = true;
            boolean unlicensedSpectrumAsSecondaryRAT = true;
            ExtSupportedFeatures extSupportedFeatures = new ExtSupportedFeaturesImpl(unlicensedSpectrumAsSecondaryRAT);
            MAPExtensionContainer extensionContainer = null;
            SGSNCapability sgsnCapability = new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer,
                    superChargerSupportedInServingNetworkEntity, gprsEnhancementsSupportIndicator, supportedCamelPhases,
                    supportedLCSCapabilitySets, offeredCamel4CSIs, smsCallBarringSupportIndicator, supportedRATTypesIndicator,
                    supportedFeatures, tAdsDataRetrieval, homogeneousSupportOfIMSVoiceOverPSSessions, cancellationTypeInitialAttach,
                    misdnlessOperationSupported, updateOfHomogeneousSupportOfIMSVoiceOverPSSessions, resetIdsSupported,
                    extSupportedFeatures);
            boolean informPreviousNetworkEntity = true;
            boolean psLCSNotSupportedByUE = false;
            GSNAddress vGmlcAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
            boolean skipSubscriberDataUpdate = false;
            ADDInfo addInfo = new ADDInfoImpl(new IMEIImpl("356024081653200"), skipSubscriberDataUpdate);
            boolean updateMME = true;
            boolean cancelSGSN = true;
            boolean initialAttachIndicator = true;
            EPSInfo epsInfo = new EPSInfoImpl(new ISRInformationImpl(updateMME, cancelSGSN, initialAttachIndicator));
            boolean servingNodeTypeIndicator = true;
            UsedRATType usedRATType = UsedRATType.utran;
            boolean gprsSubscriptionDataNotNeeded = true;
            boolean nodeTypeIndicator = true;
            boolean areaRestricted = true;
            boolean ueReachableIndicator = true;
            boolean epsSubscriptionDataNotNeeded = true;
            UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;
            ArrayList<PlmnId> ePLMNList = new ArrayList<>();
            PlmnId plmnId1 = new PlmnIdImpl(262,1);
            PlmnId plmnId2 = new PlmnIdImpl(262,999);
            ePLMNList.add(plmnId1);
            ePLMNList.add(plmnId2);
            ISDNAddressString mmeNumberForMTSMS = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    "491710490001");
            SMSRegisterRequest smsRegisterRequest = SMSRegisterRequest.isNoPreference;
            boolean smsOnly = true;
            byte[] sgsnNameArray = "mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8);
            DiameterIdentity sgsnName = new DiameterIdentityImpl(sgsnNameArray);
            byte[] sgsnRealmArray = "epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8);
            DiameterIdentity sgsnRealm = new DiameterIdentityImpl(sgsnRealmArray);
            boolean lgdSupportIndicator = false;
            boolean removalOfMMERegistrationForSMS = false;
            ArrayList<PlmnId> adjacentPLMNList = new ArrayList<>();
            PlmnId adjPlmnId1 = new PlmnIdImpl(262,2);
            PlmnId adjPlmnId2 = new PlmnIdImpl(262,3);
            adjacentPLMNList.add(adjPlmnId1);
            adjacentPLMNList.add(adjPlmnId2);

            mapDialogMobility.addUpdateGprsLocationRequest(imsi, sgsnNumber, sgsnAddress, extensionContainer, sgsnCapability,
                    informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo, epsInfo, servingNodeTypeIndicator,
                    skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded, nodeTypeIndicator, areaRestricted,
                    ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability, ePLMNList, mmeNumberForMTSMS, smsRegisterRequest,
                    smsOnly, sgsnName, sgsnRealm, lgdSupportIndicator, removalOfMMERegistrationForSMS, adjacentPLMNList);

            mapDialogMobility.send();

            this.csvWriter.incrementCounter(CREATED_DIALOGS);

        }  catch (MAPException e) {
            logger.error("Error while processing SAI response and sending MAP UGL request", e);
        }
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest insertSubscriberDataRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onInsertSubscriberDataRequest for DialogId=%d", insertSubscriberDataRequest
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = insertSubscriberDataRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = insertSubscriberDataRequest.getMAPDialog();
            ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
            ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
            ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
            ExtTeleserviceCode dataTeleservices = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allDataTeleservices);
            teleserviceList.add(shortMessageMT_PP);
            teleserviceList.add(shortMessageMO_PP);
            teleserviceList.add(dataTeleservices);
            ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<>();
            ExtBearerServiceCode extBearerServiceCode1 = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices);
            ExtBearerServiceCode extBearerServiceCode2 = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allDataCDAServices);
            bearerServiceList.add(extBearerServiceCode1);
            bearerServiceList.add(extBearerServiceCode2);
            ArrayList<SSCode> ssList = new ArrayList<>();
            SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
            ssList.add(ssCode);
            boolean allOGCallsBarred= true;
            boolean internationalOGCallsBarred = true;
            boolean internationalOGCallsNotToHPLMNCountryBarred= true;
            boolean premiumRateInformationOGCallsBarred = false;
            boolean premiumRateEntertainmentOGCallsBarred= true;
            boolean ssAccessBarred= true;
            boolean interzonalOGCallsBarred = true;
            boolean interzonalOGCallsNotToHPLMNCountryBarred= true;
            boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = true;
            boolean allECTBarred= true;
            boolean chargeableECTBarred= true;
            boolean internationalECTBarred = true;
            boolean interzonalECTBarred= true;
            boolean doublyChargeableECTBarred= true;
            boolean multipleECTBarred = true;
            boolean allPacketOrientedServicesBarred= true;
            boolean roamerAccessToHPLMNAPBarred= false;
            boolean roamerAccessToVPLMNAPBarred = false;
            boolean roamingOutsidePLMNOGCallsBarred= false;
            boolean allICCallsBarred= true;
            boolean roamingOutsidePLMNICCallsBarred = true;
            boolean roamingOutsidePLMNICountryICCallsBarred= true;
            boolean roamingOutsidePLMNBarred = false;
            boolean roamingOutsidePLMNCountryBarred= false;
            boolean registrationAllCFBarred= true;
            boolean registrationCFNotToHPLMNBarred = true;
            boolean registrationInterzonalCFBarred= true;
            boolean registrationInterzonalCFNotToHPLMNBarred = false;
            boolean registrationInternationalCFBarred = true;
            ODBGeneralData odbGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                    internationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred,
                    ssAccessBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                    interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, allECTBarred, chargeableECTBarred,
                    internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                    allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                    roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                    roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred,
                    roamingOutsidePLMNCountryBarred, registrationAllCFBarred, registrationCFNotToHPLMNBarred,
                    registrationInterzonalCFBarred, registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
            RegionalSubscriptionResponse regionalSubscriptionResponse = RegionalSubscriptionResponse.tooManyZoneCodes;
            SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);
            MAPExtensionContainer extensionContainer = null;
            boolean oCsi = false;
            boolean dCsi = false;
            boolean vtCsi = false;
            boolean tCsi = false;
            boolean mtSMSCsi = true;
            boolean mgCsi = true;
            boolean psiEnhancements = true;
            OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(oCsi,dCsi,vtCsi,tCsi, mtSMSCsi, mgCsi, psiEnhancements);
            boolean odbAllApn = false;
            boolean odbHPLMNApn = false;
            boolean odbVPLMNApn = false;
            boolean odbAllOg = false;
            boolean odbAllInternationalOg = false;
            boolean odbAllIntOgNotToHPLMNCountry = false;
            boolean odbAllInterzonalOg = false;
            boolean odbAllInterzonalOgNotToHPLMNCountry = false;
            boolean odbAllInterzonalOgandInternatOgNotToHPLMNCountry = false;
            boolean regSub = false;
            boolean trace = false;
            boolean lcsAllPrivExcep = true;
            boolean lcsUniversal = true;
            boolean lcsCallSessionRelated = true;
            boolean lcsCallSessionUnrelated = true;
            boolean lcsPLMNOperator = true;
            boolean lcsServiceType = true;
            boolean lcsAllMOLRSS = true;
            boolean lcsBasicSelfLocation = true;
            boolean lcsAutonomousSelfLocation = true;
            boolean lcsTransferToThirdParty = true;
            boolean smMoPp = true;
            boolean barringOutgoingCalls = true;
            boolean baoc = true;
            boolean boic = true;
            boolean boicExHC = true;
            boolean localTimeZoneRetrieval = true;
            boolean additionalMsisdn = true;
            boolean smsInMME = true;
            boolean smsInSGSN = true;
            boolean ueReachabilityNotification = true;
            boolean stateLocationInformationRetrieval = true;
            boolean partialPurge = true;
            boolean gddInSGSN = true;
            boolean sgsnCAMELCapability = true;
            boolean pcscfRestoration = true;
            boolean dedicatedCoreNetworks = true;
            boolean nonIPPDNTypeAPNs = true;
            boolean nonIPPDPTypeAPNs = true;
            boolean nrAsSecondaryRAT = true;
            SupportedFeatures supportedFeatures = new SupportedFeaturesImpl(odbAllApn, odbHPLMNApn, odbVPLMNApn, odbAllOg, odbAllInternationalOg,
                    odbAllIntOgNotToHPLMNCountry, odbAllInterzonalOg, odbAllInterzonalOgNotToHPLMNCountry,
                    odbAllInterzonalOgandInternatOgNotToHPLMNCountry, regSub, trace, lcsAllPrivExcep, lcsUniversal,
                    lcsCallSessionRelated, lcsCallSessionUnrelated, lcsPLMNOperator, lcsServiceType, lcsAllMOLRSS,
                    lcsBasicSelfLocation, lcsAutonomousSelfLocation, lcsTransferToThirdParty, smMoPp, barringOutgoingCalls, baoc,
                    boic, boicExHC, localTimeZoneRetrieval, additionalMsisdn, smsInMME, smsInSGSN, ueReachabilityNotification,
                    stateLocationInformationRetrieval, partialPurge, gddInSGSN, sgsnCAMELCapability,
                    pcscfRestoration, dedicatedCoreNetworks, nonIPPDNTypeAPNs, nonIPPDPTypeAPNs,
                    nrAsSecondaryRAT);
            boolean unlicensedSpectrumAsSecondaryRAT = true;
            ExtSupportedFeatures extSupportedFeatures = new ExtSupportedFeaturesImpl(unlicensedSpectrumAsSecondaryRAT);

            mapDialogMobility.addInsertSubscriberDataResponse(invokeId, teleserviceList, bearerServiceList, ssList,
                    odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer, offeredCamel4CSIs,
                    supportedFeatures, extSupportedFeatures);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataRequest ", e);
        }
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse insertSubscriberDataResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onInsertSubscriberDataResponse over DialogId=%d", insertSubscriberDataResponse
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequestIndication) {
        logger.error(String.format("ERROR: received UpdateLocationRequest over DialogId=%d", updateLocationRequestIndication
                .getMAPDialog().getLocalDialogId(), " over the client (acting as SGSN)"));
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse updateLocationResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationResponse over DialogId=%d", updateLocationResponseIndication
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest updateGprsLocationRequest) {

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse updateGprsLocationResponse) {

    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest cancelLocationRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onCancelLocationRequest for DialogId=%d", cancelLocationRequest
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = cancelLocationRequest.getInvokeId();
            MAPDialogMobility cancelLocationRequestDialog = cancelLocationRequest.getMAPDialog();
            ReturnResultLast returnResultLast = new ReturnResultLastImpl();
            returnResultLast.setInvokeId(invokeId);
            cancelLocationRequestDialog.sendReturnResultLastComponent(returnResultLast);
            cancelLocationRequestDialog.close(false);
            if (cancelLocationRequest.getCancellationType() == CancellationType.subscriptionWithdraw) {
                if (cancelLocationRequest.isReattachRequired()) {
                    sendAuthenticationInfoRequest("901405105682021");
                }
            }

            new Thread(new PurgeMSSender(this)).start();


        } catch (MAPException e) {
            logger.error("Error while processing CancelLocationRequest ", e);
        }
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse cancelLocationResponse) {
        logger.error(String.format("onCancelLocationResponse over DialogId=%d", cancelLocationResponse
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest purgeMSRequest) {
        logger.error(String.format("ERROR: received PurgeMSRequest over DialogId=%d", purgeMSRequest
                .getMAPDialog().getLocalDialogId(), " over the client (acting as SGSN)"));
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse purgeMSResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onPurgeMSResponse over DialogId=%d", purgeMSResponse
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest sendIdentificationRequest) {

    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse sendIdentificationResponse) {

    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest authenticationFailureReportRequestIndication) {

    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse authenticationFailureReportResponseIndication) {

    }

    @Override
    public void onResetRequest(ResetRequest resetRequestIndication) {

    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest forwardCheckSSIndicationRequestIndication) {

    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest restoreDataRequestIndication) {

    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse restoreDataResponseIndication) {

    }

    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest anyTimeInterrogationRequest) {

    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse anyTimeInterrogationResponse) {

    }

    @Override
    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest anyTimeSubscriptionInterrogationRequest) {

    }

    @Override
    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse anyTimeSubscriptionInterrogationResponse) {

    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest provideSubscriberInfoRequest) {

    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse provideSubscriberInfoResponse) {

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest deleteSubscriberDataRequest) {

    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse deleteSubscriberDataResponse) {

    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest checkImeiRequest) {

    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse checkImeiResponse) {

    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility activateTraceModeRequestMobilityIndication) {

    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility activateTraceModeResponseMobilityIndication) {

    }

    private void sendAuthenticationInfoRequest(String imsiDigits) {
        try {
            // Send Authentication Info
            // First create Dialog
            AddressString origRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
            AddressString destRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SGSN_SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

            MAPDialogMobility mapDialogMobility = this.mapProvider.getMAPServiceMobility().
                    createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext, MAPApplicationContextVersion.version3),
                            clientSccpAddress, origRef, serverSccpAddress, destRef);

            IMSI imsi = new IMSIImpl(imsiDigits);
            int numberOfRequestedVectors = 5;
            boolean segmentationProhibited = false;
            boolean immediateResponsePreferred = false;
            ReSynchronisationInfo reSynchronisationInfo = null;
            MAPExtensionContainer mapExtensionContainer = null;
            RequestingNodeType requestingNodeType = RequestingNodeType.vlr;
            byte[] mccMnc = new byte[] {0x47, (byte) 0xf8, 0x10};
            PlmnId requestingPlmnId = new PlmnIdImpl(mccMnc);
            Integer numberOfRequestedAdditionalVectors = 0;
            boolean additionalVectorsAreForEPS = false;
            boolean ueUsageTypeRequestIndication = false;

            mapDialogMobility.addSendAuthenticationInfoRequest(imsi, numberOfRequestedVectors, segmentationProhibited,
                    immediateResponsePreferred, reSynchronisationInfo, mapExtensionContainer, requestingNodeType, requestingPlmnId,
                    numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS, ueUsageTypeRequestIndication);

            mapDialogMobility.send();

            this.csvWriter.incrementCounter(CREATED_DIALOGS);

        } catch (MAPException e) {
            logger.error("Error while sending CancelLocationRequest ", e);
        }
    }

    private class PurgeMSSender implements Runnable {

        private Client client;

        public PurgeMSSender(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            ++imsiForPurge;

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                logger.error(ie.getMessage());
            }
            try {
                // Create Dialog
                AddressString originAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
                AddressString destAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

                SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SGSN_SSN, SCCP_CLIENT_ADDRESS);
                SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

                MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
                MAPApplicationContextName mapAcn = MAPApplicationContextName.msPurgingContext;
                MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                        originAddressString, serverSccpAddress, destAddressString);

                IMSI imsi = new IMSIImpl(String.valueOf(imsiForPurge));
                ISDNAddressString vlrNumber = null;
                ISDNAddressString sgsnNumber = null;
                MAPExtensionContainer extensionContainer = null;
                Random rand = new Random();
                int randLoc = rand.nextInt(10) + 1;
                int ageOfLocationInformation;
                boolean currentLocationRetrieved;
                LocationInformation locationInformation;
                LocationInformationEPS locationInformationEPS = null;
                LocationInformationGPRS locationInformationGPRS;
                boolean saiPresent = false;
                int mcc, mnc, lac, cellId;
                CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
                CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = null;
                LocationNumber locationNumber;
                LocationNumberMap locationNumberMap;
                ISDNAddressString mscNumber =  new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "491710490000");
                GeographicalInformation geographicalInformation;
                GeodeticInformation geodeticInformation;
                byte[] lteCgi;
                EUtranCgi eUtranCgi;
                byte[] trackingAreaId;
                TAId taId;
                RAIdentity routeingAreaIdentity;
                UserCSGInformation userCSGInformation = null;
                TypeOfShape geographicalTypeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                double geographicalLatitude;
                double geographicalLongitude;
                double geographicalUncertainty;
                TypeOfShape geodeticTypeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                double geodeticLatitude;
                double geodeticLongitude;
                double geodeticUncertainty;
                int geodeticConfidence = 1;
                int screeningAndPresentationIndicators = 3;
                int natureOfAddressIndicator = 4;
                String locationNumberAddressDigits= "819203961904";
                int numberingPlanIndicator = 1;
                int internalNetworkNumberIndicator = 1;
                int addressRepresentationRestrictedIndicator = 1;
                int screeningIndicator = 3;
                locationNumber = new LocationNumberImpl(natureOfAddressIndicator, locationNumberAddressDigits, numberingPlanIndicator,
                        internalNetworkNumberIndicator, addressRepresentationRestrictedIndicator, screeningIndicator);
                locationNumberMap = null;
                try {
                    locationNumberMap = new LocationNumberMapImpl(locationNumber);
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
                String mmneNameStr;
                byte[] mme;
                DiameterIdentity mmeName;
                byte[] raId;
                byte[] lsaId = {49, 51, 50};
                LSAIdentity selectedLSAId = new LSAIdentityImpl(lsaId);

                switch(randLoc) {
                    case 1:
                        ageOfLocationInformation = 0;
                        currentLocationRetrieved = true;
                        mcc = 748;
                        mnc = 1;
                        lac = 101;
                        cellId = 10263;
                        geographicalLatitude = -34.909744;
                        geographicalLongitude = -56.146317;
                        geographicalUncertainty = 1.0;
                        geographicalInformation = new GeographicalInformationImpl(geographicalTypeOfShape, geographicalLatitude, geographicalLongitude, geographicalUncertainty);
                        geodeticInformation = null;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber,
                                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                        break;
                    case 2:
                        ageOfLocationInformation = 0;
                        currentLocationRetrieved = true;
                        geographicalInformation = null;
                        geodeticLatitude = -34.910349;
                        geodeticLongitude = -56.149832;
                        geodeticUncertainty = 2.0;
                        geodeticConfidence = 2;
                        screeningAndPresentationIndicators = 1;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, geodeticTypeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        lteCgi = hexStringToByteArray("47f81000095f02"); // ECGI = 748-1-614146; TBCD encoded: 47f81000095f02
                        trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                        eUtranCgi = new EUtranCgiImpl(lteCgi);
                        taId = new TAIdImpl(trackingAreaId);
                        mmneNameStr = "mmec03.mmeer3000.mme.epc.mnc002.mcc748.3gppnetwork.org";
                        mme = mmneNameStr.getBytes();
                        mmeName = new DiameterIdentityImpl(mme);
                        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                                geodeticInformation, currentLocationRetrieved, ageOfLocationInformation, mmeName);
                        break;
                    case 3:
                        ageOfLocationInformation = 125;
                        currentLocationRetrieved = false;
                        mcc = 748;
                        mnc = 1;
                        lac = 118;
                        cellId = 292;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        raId = hexStringToByteArray("47f810006517");
                        routeingAreaIdentity = new RAIdentityImpl(raId);
                        locationInformationGPRS = mapProvider.getMAPParameterFactory().createLocationInformationGPRS(cellGlobalIdOrServiceAreaIdOrLAI,
                                routeingAreaIdentity, geographicalInformation, sgsnNumber, selectedLSAId, extensionContainer, saiPresent, geodeticInformation,
                                currentLocationRetrieved, ageOfLocationInformation);
                        break;
                    case 4:
                        ageOfLocationInformation = 1;
                        currentLocationRetrieved = false;
                        mcc = 748;
                        mnc = 1;
                        lac = 109;
                        cellId = 10175;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber,
                                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                        break;
                    case 5:
                        ageOfLocationInformation = 0;
                        currentLocationRetrieved = true;
                        mcc = 748;
                        mnc = 1;
                        lac = 11;
                        cellId = 4812;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber,
                                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                        break;
                    case 6:
                        ageOfLocationInformation = 5;
                        currentLocationRetrieved = false;
                        mcc = 748;
                        mnc = 7;
                        lac = 8820;
                        cellId = 9748;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f87000477304"); // ECGI = 748-7-4682500; TBCD encoded: 47f87000477304
                        trackingAreaId = hexStringToByteArray("47f8701b6c"); // TAI = 748-7-7020; TBCD encoded: 47f8701b6c
                        eUtranCgi = new EUtranCgiImpl(lteCgi);
                        taId = new TAIdImpl(trackingAreaId);
                        mmneNameStr = "mmec03.mmeer3000.mme.epc.mnc002.mcc748.3gppnetwork.org";
                        mme = mmneNameStr.getBytes();
                        mmeName = new DiameterIdentityImpl(mme);
                        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                                geodeticInformation, currentLocationRetrieved, ageOfLocationInformation, mmeName);
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber,
                                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                        locationInformationEPS = null;
                        break;
                    case 7:
                        ageOfLocationInformation = 0;
                        currentLocationRetrieved = true;
                        mcc = 748;
                        mnc = 7;
                        lac = 8552;
                        cellId = 8239;
                        geographicalInformation = null;
                        geodeticLatitude = -34.910349;
                        geodeticLongitude = -56.149832;
                        geodeticUncertainty = 2.0;
                        geodeticConfidence = 3;
                        screeningAndPresentationIndicators = 2;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, geodeticTypeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber,
                                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                        break;
                    case 8:
                        ageOfLocationInformation = 0;
                        currentLocationRetrieved = true;
                        mcc = 748;
                        mnc = 10;
                        lac = 9501;
                        cellId = 35100;
                        geographicalInformation = null;
                        geodeticLatitude = -34.905624;
                        geodeticLongitude = -55.042191;
                        geodeticUncertainty = 4.0;
                        geodeticConfidence = 10;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, geodeticTypeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        raId = hexStringToByteArray("47f810006516");
                        routeingAreaIdentity = new RAIdentityImpl(raId);
                        locationInformationGPRS = mapProvider.getMAPParameterFactory().createLocationInformationGPRS(cellGlobalIdOrServiceAreaIdOrLAI,
                                routeingAreaIdentity, geographicalInformation, sgsnNumber, selectedLSAId, extensionContainer, saiPresent, geodeticInformation,
                                currentLocationRetrieved, ageOfLocationInformation);
                        break;
                    case 9:
                        sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                                "491710490000");
                        break;
                    case 10:
                        vlrNumber = mscNumber;
                        break;
                    default:
                        locationInformation = null;
                        locationInformationEPS = null;
                        locationInformationGPRS = null;
                        break;
                }
                // temporal as Wireshark is not recognizing the following in PMS
                locationInformation = null;
                locationInformationGPRS = null;
                locationInformationEPS = null;
                sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");

                //if (locationInformation != null)
                //    vlrNumber = mscNumber;
                //else
                //    sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                //            "491710490000");


                int customInvokeTimeout = 30;

                mapDialogMobility.addPurgeMSRequest(customInvokeTimeout, imsi, vlrNumber, sgsnNumber, null, locationInformation,
                        locationInformationGPRS, locationInformationEPS);
                mapDialogMobility.send();

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
