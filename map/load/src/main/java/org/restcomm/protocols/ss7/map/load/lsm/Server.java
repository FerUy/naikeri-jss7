package org.restcomm.protocols.ss7.map.load.lsm;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.EllipsoidPoint;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.Polygon;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingOptionMilliseconds;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientNameImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PolygonImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ReportingOptionMillisecondsImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SLRArgPCSExtensionsImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranAdditionalPositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranCivicAddressImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
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

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Server extends TestHarnessLocationServicesManagement {

    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    private static MAPProvider mapProvider;

    // TCAP
    private TCAPStack tcapStack;

    // SCCP
    SccpExtModuleImpl sccpExtModule;
    private SccpStackImpl sccpStack;

    // M3UA
    private M3UAManagementImpl serverM3UAMgmt;

    // SCTP
    private NettySctpManagementImpl sctpManagement;

    int endCount = 0;
    volatile long start = System.currentTimeMillis();

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {

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
        serverM3UAMgmt.startAsp("RASP1");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.sctpManagement = new NettySctpManagementImpl("Server");
//        this.sctpManagement.setSingleThread(false);
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResources();

        // 1. Create SCTP Server
        sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT, ipChannelType, null);

        // 2. Create SCTP Server Association
        sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);

        // 3. Start Server
        sctpManagement.startServer(SERVER_NAME);
    }

    private void initM3UA() throws Exception {
        this.serverM3UAMgmt = new M3UAManagementImpl("Server", null, new Ss7ExtInterfaceImpl());
        this.serverM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.serverM3UAMgmt.setDeliveryMessageThreadCount(DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.serverM3UAMgmt.start();
        this.serverM3UAMgmt.removeAllResources();

        // Step 1 : Create App Server

        RoutingContext rc = factory.createRoutingContext(new long[] { 101L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        NetworkAppearance na = factory.createNetworkAppearance(102L);
        As as = this.serverM3UAMgmt.createAs("RAS1", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType,
                1, na);
        logger.info("AS={}", as);

        // Step 2 : Create ASP
        AspFactory aspFactory = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);
        logger.info("ASP Factory={}", aspFactory);

        // Step3 : Assign ASP to AS
        Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");
        logger.info("ASP={}", asp);

        // Step 4: Add Route. Remote point code is 2
        this.serverM3UAMgmt.addRoute(CLIENT_SPC, -1, -1, "RAS1");
    }

    private void initSCCP() throws Exception {
        Ss7ExtInterface ss7ExtInterface = new Ss7ExtInterfaceImpl();
        sccpExtModule = new SccpExtModuleImpl();
        ss7ExtInterface.setSs7ExtSccpInterface(sccpExtModule);
        this.sccpStack = new SccpStackImpl("MapLoadServerSccpStack", ss7ExtInterface);
        this.sccpStack.setMtp3UserPart(1, this.serverM3UAMgmt);

        this.sccpStack.start();
        this.sccpStack.removeAllResources();

        Router router = this.sccpStack.getRouter();
        RouterExt routerExt = sccpExtModule.getRouterExt();
        SccpResource sccpResource = this.sccpStack.getSccpResource();

        sccpResource.addRemoteSpc(0, CLIENT_SPC, 0, 0);
        sccpResource.addRemoteSsn(0, CLIENT_SPC, CLIENT_SSN, 0, false);
        sccpResource.addRemoteSsn(1, CLIENT_SPC, HLR_SSN, 0, false);
        sccpResource.addRemoteSsn(2, CLIENT_SPC, MSC_SSN, 0, false);
        sccpResource.addRemoteSsn(3, CLIENT_SPC, SGSN_SSN, 0, false);

        router.addMtp3ServiceAccessPoint(1, 1, SERVER_SPC, NETWORK_INDICATOR, 0, null);
        router.addMtp3Destination(1, 1, CLIENT_SPC, CLIENT_SPC, 0, 255, 255);
        router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, SERVER_SPC, 0);
        routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, CLIENT_SPC, 0);
        routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0, null);
        routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern,
                "K", 2, -1, null, 0, null);
    }

    private void initTCAP() throws Exception {
        this.tcapStack = new TCAPStackImpl("TestServer", this.sccpStack.getSccpProvider(), SERVER_SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        // MAP
        MAPStackImpl mapStack = new MAPStackImpl("TestServer", this.tcapStack.getProvider());
        mapProvider = mapStack.getMAPProvider();

        mapProvider.addMAPDialogListener(this);
        mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
        mapProvider.getMAPServiceLsm().activate();

        mapStack.start();
    }

    private static SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = SERVER_SSN;
        }
        return fact.createSccpAddress(ri, gt, dpc, ssn);
    }

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].equalsIgnoreCase("tcp")) {
            ipChannelType = IpChannelType.TCP;
        }
        logger.info("IpChannelType={}", ipChannelType);

        if (args.length >= 2) {
            TestHarnessLocationServicesManagement.CLIENT_IP = args[1];
        }
        logger.info("CLIENT_IP={}", TestHarnessLocationServicesManagement.CLIENT_IP);

        if (args.length >= 3) {
            TestHarnessLocationServicesManagement.CLIENT_PORT = Integer.parseInt(args[2]);
        }
        logger.info("CLIENT_PORT={}", TestHarnessLocationServicesManagement.CLIENT_PORT);

        if (args.length >= 4) {
            TestHarnessLocationServicesManagement.SERVER_IP = args[3];
        }
        logger.info("SERVER_IP={}", TestHarnessLocationServicesManagement.SERVER_IP);

        if (args.length >= 5) {
            TestHarnessLocationServicesManagement.SERVER_PORT = Integer.parseInt(args[4]);
        }
        logger.info("SERVER_PORT={}", TestHarnessLocationServicesManagement.SERVER_PORT);

        if (args.length >= 6) {
            TestHarnessLocationServicesManagement.CLIENT_SPC = Integer.parseInt(args[5]);
        }
        logger.info("CLIENT_SPC={}", TestHarnessLocationServicesManagement.CLIENT_SPC);

        if (args.length >= 7) {
            TestHarnessLocationServicesManagement.SERVER_SPC = Integer.parseInt(args[6]);
        }
        logger.info("SERVER_SPC={}", TestHarnessLocationServicesManagement.SERVER_SPC);

        if (args.length >= 8) {
            TestHarnessLocationServicesManagement.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        logger.info("NETWORK_INDICATOR={}", TestHarnessLocationServicesManagement.NETWORK_INDICATOR);

        if (args.length >= 9) {
            TestHarnessLocationServicesManagement.SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        logger.info("SERVICE_INDICATOR={}", TestHarnessLocationServicesManagement.SERVICE_INDICATOR);

        if (args.length >= 10) {
            TestHarnessLocationServicesManagement.SSN = Integer.parseInt(args[9]);
        }
        logger.info("SSN={}", TestHarnessLocationServicesManagement.SSN);

        if (args.length >= 11) {
            TestHarnessLocationServicesManagement.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        logger.info("ROUTING_CONTEXT={}", TestHarnessLocationServicesManagement.ROUTING_CONTEXT);

        if(args.length >= 12) {
            TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT={}", TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        final Server server = new Server();
        try {
            server.initializeStack(ipChannelType);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInfoForLCSRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("sendRoutingInfoForLCSRequestIndication for DialogId={}", sendRoutingInfoForLCSRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = sendRoutingInfoForLCSRequestIndication.getInvokeId();
            MAPDialogLsm sriLcsDialog = sendRoutingInfoForLCSRequestIndication.getMAPDialog();

            // Create Routing Information parameters for concerning MAP operation
            MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
            Random rand = new Random();
            SubscriberIdentity subscriberIdentity;
            if (sendRoutingInfoForLCSRequestIndication.getTargetMS().getIMSI() != null) {
                long msisdnDigits = RandomUtils.nextLong(59898000000L, 59899000000L);
                ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        String.valueOf(msisdnDigits));
                subscriberIdentity = new SubscriberIdentityImpl(msisdn);
            } else {
                long imsiDigits = RandomUtils.nextLong(748020000000000L, 748030000000000L);
                subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl(String.valueOf(imsiDigits)));
            }
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, SCCP_SGSN_ADDRESS);
            AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
            LMSI lmsi;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 233, (byte) 140});
                    break;
                case 2:
                    lmsi = new LMSIImpl(new byte[] {113, (byte) 255, (byte) 172, (byte) 206});
                    break;
                case 3:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 235, 55});
                    break;
                case 4:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 231, (byte) 213});
                    break;
                default:
                    lmsi = null;
                    break;
            }
            boolean gprsNodeIndicator = false;
            boolean lcsCapabilitySetRelease98_99 = true;
            boolean lcsCapabilitySetRelease4 = true;
            boolean lcsCapabilitySetRelease5 = true;
            boolean lcsCapabilitySetRelease6 = true;
            boolean lcsCapabilitySetRelease7 = false;
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            lcsCapabilitySetRelease7 = true;
            SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
            LCSLocationInfo lcsLocationInfo = mapFactory.createLCSLocationInfo(mscNumber, lmsi, null, gprsNodeIndicator,
                    additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            GSNAddress vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());
            GSNAddress pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.18").getAddress());
            GSNAddress additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });

            sriLcsDialog.addSendRoutingInfoForLCSResponse(invokeId, subscriberIdentity, lcsLocationInfo, null, vGmlcAddress, hGmlcAddress,
                    pprAddress, additionalVGmlcAddress);
            // This will initiate the TC-BEGIN with INVOKE component
            sriLcsDialog.close(false);

            /*
             * Create Dialog for sending not deferred MAP SLR to the GMLC
             */
            AddressString origRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            AddressString destRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_GMLC_ADDRESS);
            SccpAddress origSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SERVER_SSN, SCCP_MSC_ADDRESS);
            SccpAddress destSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, CLIENT_SSN, SCCP_GMLC_ADDRESS);
            MAPDialogLsm slrDialog = mapProvider.getMAPServiceLsm()
                    .createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                            MAPApplicationContextVersion.version3), origSccpAddress, origRef, destSccpAddress, destRef);

            sendMapSLR(slrDialog, false, null);

        } catch (MAPException mapException) {
            logger.error("MAP Exception while processing onSendRoutingInfoForLCSRequest ", mapException);
        } catch (Exception e) {
            logger.error("Exception while processing onSendRoutingInfoForLCSRequest ", e);
        }
    }

    private void sendMapSLR(MAPDialogLsm mapDialogSLR, boolean isDeferred, Integer pslReferenceNumber) {
    /*
     * subscriberLocationReport OPERATION ::= { --Timer m ARGUMENT
     *   SubscriberLocationReport-Arg RESULT SubscriberLocationReport-Res
     *   ERRORS { systemFailure | dataMissing | resourceLimitation | unexpectedDataValue | unknownSubscriber |
     *   unauthorizedRequestingNetwork | unknownOrUnreachableLCSClient} CODE local:86 }
     *
     *  SubscriberLocationReport-Arg ::= SEQUENCE {
     *  lcs-Event                              LCS-Event,
     *  lcs-ClientID                           LCS-ClientID,
     *  lcsLocationInfo                        LCSLocationInfo,
     *  msisdn                                 [0] ISDN-AddressString OPTIONAL,
     *  imsi                                   [1] IMSI  OPTIONAL,
     *  imei                                   [2] IMEI  OPTIONAL,
     *  na-ESRD                                [3] ISDN-AddressString OPTIONAL,
     *  na-ESRK                                [4] ISDN-AddressString OPTIONAL,
     *  locationEstimate                       [5] Ext-GeographicalInformation OPTIONAL,
     *  ageOfLocationEstimate                  [6] AgeOfLocationInformation OPTIONAL,
     *  slr-ArgExtensionContainer              [7] SLR-ArgExtensionContainer OPTIONAL,
     *  ...,
     *  add-LocationEstimate                   [8] Add-GeographicalInformation OPTIONAL,
     *  deferredmt-lrData                      [9] Deferredmt-lrData OPTIONAL,
     *  lcs-ReferenceNumber                    [10] LCS-ReferenceNumber OPTIONAL,
     *  geranPositioningData                   [11] PositioningDataInformation OPTIONAL,
     *  utranPositioningData                   [12] UtranPositioningDataInfo OPTIONAL,
     *  cellIdOrSai                            [13] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
     *  h-gmlc-Address                         [14] GSN-Address OPTIONAL,
     *  lcsServiceTypeID                       [15] LCSServiceTypeID OPTIONAL,
     *  sai-Present                            [17] NULL OPTIONAL,
     *  pseudonymIndicator                     [18] NULL  OPTIONAL,
     *  accuracyFulfilmentIndicator            [19] AccuracyFulfilmentIndicator OPTIONAL,
     *  velocityEstimate                       [20] VelocityEstimate OPTIONAL,
     *  sequenceNumber                         [21] SequenceNumber OPTIONAL,
     *  periodicLDRInfo                        [22] PeriodicLDRInfo OPTIONAL,
     *  mo-lrShortCircuitIndicator             [23] NULL  OPTIONAL,
     *  geranGANSSpositioningData              [24] GeranGANSSpositioningData OPTIONAL,
     *  utranGANSSpositioningData              [25] UtranGANSSpositioningData OPTIONAL,
     *  targetServingNodeForHandover           [26] ServingNodeAddress OPTIONAL,
     *  utranAdditionalPositioningData         [27] UtranAdditionalPositioningData OPTIONAL,
     *  utranBaroPressureMeas                  [28] UtranBaroPressureMeas OPTIONAL,
     *  utranCivicAddress                      [29] UtranCivicAddress OPTIONAL }
     *
     *  -- one of msisdn or imsi is mandatory
     *
     *  -- a location estimate that is valid for the locationEstimate parameter should
     *  -- be transferred in this parameter in preference to the add-LocationEstimate.
     *
     *  -- the deferredmt-lrData parameter shall be included if and only if the lcs-Event
     *  -- indicates a deferredmt-lrResponse.
     *
     *  -- if the lcs-Event indicates a deferredmt-lrResponse then the locationEstimate
     *  -- and the add-locationEstimate parameters shall not be sent if the
     *  -- supportedGADShapes parameter had been received in ProvideSubscriberLocation-Arg
     *  -- and the shape encoded in locationEstimate or add-LocationEstimate was not marked
     *  -- as supported in supportedGADShapes. In such a case terminationCause
     *  -- in deferredmt-lrData shall be present with value
     *  -- shapeOfLocationEstimateNotSupported.
     *
     *  -- If a lcs event indicates deferred mt-lr response, the lcs-Reference number shall be
     *  -- included.
     *
     *  -- sai-Present indicates that the cellIdOrSai parameter contains a Service Area Identity
     *
     *  SequenceNumber ::= INTEGER (1..8639999)
     */

        // Then, create parameters for concerning MAP operation
        try {
            MAPParameterFactoryImpl mapParameterFactory = new MAPParameterFactoryImpl();
            Random rand = new Random();
            ISDNAddressString msisdn = null;
            IMSI imsi = null;
            int msisdnOrImsi = rand.nextInt(10) + 1;
            // -- one of msisdn or imsi is mandatory
            if (msisdnOrImsi == 1) {
                long msisdnDigits = RandomUtils.nextLong(59898000000L, 59899000000L);
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, String.valueOf(msisdnDigits));
            } else {
                long imsiDigits = RandomUtils.nextLong(748020000000000L, 748030000000000L);
                imsi = new IMSIImpl(String.valueOf(imsiDigits));
            }

            long imeiDigits = RandomUtils.nextLong(100710000000000L, 100720000000000L);
            IMEI imei = new IMEIImpl(String.valueOf(imeiDigits));
            //ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SGSN_ADDRESS);

            ISDNAddressString naEsrd = null;
            ISDNAddressString naEsrk = null;
            boolean naEsrkRequest = false;
            switch (rand.nextInt(3) + 1) {
                case 1:
                    naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1210101075");
                    break;
                case 2:
                    naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9289277009");
                    break;
                default:
                    naEsrkRequest = true;
                    break;
            }

            // LCS-Event ::= ENUMERATED { emergencyCallOrigination (0), emergencyCallRelease (1), mo-lr (2), ..., deferredmt-lrResponse (3),
            // deferredmo-lrTTTPInitiation (4), emergencyCallHandover (5) }
            LCSEvent lcsEvent = null;
            if (isDeferred) {
                lcsEvent = LCSEvent.deferredmtlrResponse;
            } else {
                switch (rand.nextInt(5) + 1) {
                    case 1:
                        lcsEvent = LCSEvent.emergencyCallOrigination;
                        break;
                    case 2:
                        lcsEvent = LCSEvent.emergencyCallRelease;
                        break;
                    case 3:
                        lcsEvent = LCSEvent.molr;
                        break;
                    case 4:
                        lcsEvent = LCSEvent.deferredmolrTTTPInitiation;
                        break;
                    case 5:
                        lcsEvent = LCSEvent.emergencyCallHandover;
                        break;
                }
            }

            ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "444567");
            LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, null);
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.broadcastService;
            String clientName = "219023";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "911";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            APN lcsAPN = new APNImpl("e911");
            LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, null);

            ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, SCCP_MSC_ADDRESS);

            LMSI lmsi;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 233, (byte) 140});
                    break;
                case 2:
                    lmsi = new LMSIImpl(new byte[] {113, (byte) 255, (byte) 172, (byte) 206});
                    break;
                case 3:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 235, 55});
                    break;
                case 4:
                    lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 231, (byte) 213});
                    break;
                default:
                    lmsi = null;
                    break;
            }
            boolean gprsNodeIndicator = true;
            AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
            boolean lcsCapabilitySetRelease98_99 = true;
            boolean lcsCapabilitySetRelease4 = true;
            boolean lcsCapabilitySetRelease5 = true;
            boolean lcsCapabilitySetRelease6 = true;
            boolean lcsCapabilitySetRelease7 = false;
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            lcsCapabilitySetRelease7 = true;
            SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
            LCSLocationInfo lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                    supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            Integer ageOfLocationEstimate = 0;
            ExtGeographicalInformation locationEstimate = null;
            TypeOfShape typeOfShape = null;
            double latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, uncertaintyAltitude, uncertaintyRadius,
                    offsetAngle, includedAngle;
            int confidence, altitude, innerRadius;
            EllipsoidPoint ellipsoidPoint1, ellipsoidPoint2, ellipsoidPoint3, ellipsoidPoint4, ellipsoidPoint5, ellipsoidPoint6;
            // ellipsoidPoint7, ellipsoidPoint8, ellipsoidPoint9, ellipsoidPoint10, ellipsoidPoint11, ellipsoidPoint12, ellipsoidPoint13,
            // ellipsoidPoint14, ellipsoidPoint15;
            // 3 <= numberOfPoints <= 15
            switch (rand.nextInt(6) + 1) {
                case 1:
                    typeOfShape = TypeOfShape.EllipsoidPoint;
                    latitude = 34.909744;
                    longitude = -56.146317;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(latitude, longitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
                case 2:
                    typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                    latitude = -34.910349;
                    longitude = -56.149832;
                    uncertainty = 5.1;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(latitude, longitude, uncertainty);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
                case 3:
                    typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyEllipse;
                    latitude = -34.905624;
                    longitude = -55.042191;
                    uncertaintySemiMajorAxis = 21.2;
                    uncertaintySemiMinorAxis = 10.4;
                    angleOfMajorAxis = 30.0; // orientation of major axis
                    confidence = 1;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(latitude, longitude,
                                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
                case 4:
                    typeOfShape = TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid;
                    latitude = -34.956436;
                    longitude = -54.937820;
                    altitude = 570;
                    uncertaintySemiMajorAxis = 25.4;
                    uncertaintySemiMinorAxis = 12.1;
                    angleOfMajorAxis = 30.2; // orientation of major axis
                    uncertaintyAltitude = 80.1;
                    confidence = 5;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(latitude,
                                longitude, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
                case 5:
                    typeOfShape = TypeOfShape.EllipsoidArc;
                    latitude = -34.939956;
                    longitude = -54.914474;
                    innerRadius = 5;
                    uncertaintyRadius = 1.50;
                    offsetAngle = 20.0;
                    includedAngle = 20.0;
                    confidence = 2;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidArc(latitude, longitude, innerRadius,
                                uncertaintyRadius, offsetAngle, includedAngle, confidence);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
                case 6:
                    typeOfShape = TypeOfShape.Polygon;
                    latitude = 0.0;
                    longitude = 0.0;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(latitude, longitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    break;
            }
            AddGeographicalInformation additionalLocationEstimate = null;
            int additionalLocationEstimateRandomOption = rand.nextInt(6) + 1;
            if (typeOfShape == TypeOfShape.Polygon) {
                ellipsoidPoint1 = new EllipsoidPoint(-2.907010, 70.778014);
                ellipsoidPoint2 = new EllipsoidPoint(-3.017238, 70.708922);
                ellipsoidPoint3 = new EllipsoidPoint(-2.941387, 70.432091);
                ellipsoidPoint4 = new EllipsoidPoint(-3.040019, 70.681903);
                ellipsoidPoint5 = new EllipsoidPoint(-3.045001, 70.700109);
                ellipsoidPoint6 = new EllipsoidPoint(-2.989001, 71.000004);
                EllipsoidPoint[] ellipsoidPoints = {ellipsoidPoint1, ellipsoidPoint2, ellipsoidPoint3, ellipsoidPoint4, ellipsoidPoint5, ellipsoidPoint6};

                try {
                    switch (additionalLocationEstimateRandomOption) {
                        case 1:
                            byte[] polygonData1 = { 83,
                                    41, (byte) 234, (byte) 138, 55, 67, 17,
                                    41, (byte) 234, (byte) 136, 55, 67, 3,
                                    41, (byte) 234, 0, 55, 67, 24};
                            Polygon polygon1 = new PolygonImpl(polygonData1);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon1.getData());
                            break;
                        case 2:
                            byte[] polygonData2 = { 83,
                                    44, 29, (byte) 188, 53, (byte) 227, (byte) 135,
                                    44, 29, (byte) 193, 53, (byte) 227, (byte) 130,
                                    44, 29, (byte) 190, 53, (byte) 227, 123};
                            Polygon polygon2 = new PolygonImpl(polygonData2);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon2.getData());
                            break;
                        case 3:
                            byte[] polygonData3 = { 83,
                                    36, (byte) 167, 60, 52, 37, 0,
                                    36, (byte) 167, 49, 52, 36, (byte) 255,
                                    36, (byte) 167, 50, 52, 37, 0};
                            Polygon polygon3 = new PolygonImpl(polygonData3);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon3.getData());
                            break;
                        case 4:
                            byte[] polygonData4 = { 83,
                                    36, 124, (byte) 163, 59, 49, 112,
                                    36, 126, 7, 59, 49, (byte) 138,
                                    36, 127, (byte) 224, 59, 49, 72};
                            Polygon polygon4 = new PolygonImpl(polygonData4);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon4.getData());
                            break;
                        case 5:
                            byte[] polygonData5 = { 84,
                                    37, (byte) 229, (byte) 179, 52, 66, (byte) 211,
                                    37, (byte) 230, 64, 52, 67, 124,
                                    37, (byte) 230, (byte) 131, 52, 67, 121,
                                    37, (byte) 230, (byte) 132, 52, 67, 125};
                            Polygon polygon5 = new PolygonImpl(polygonData5);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon5.getData());
                            break;
                        case 6:
                            PolygonImpl polygon6 = new PolygonImpl();
                            polygon6.setData(ellipsoidPoints);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon6.getData());
                            break;
                    }
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            }

            /*long[] oid = {0, 0, 17, 773, 1, 1, 1};
            byte[] privateExtData = hexStringToByteArray("1144");
            MAPPrivateExtension mapPrivateExtension = new MAPPrivateExtensionImpl(oid, privateExtData);
            ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<>();
            privateExtensionList.add(mapPrivateExtension);
            SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(true);
            SLRArgExtensionContainer slrArgExtensionContainer = new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);*/
            SLRArgExtensionContainer slrArgExtensionContainer = null;
            if (naEsrkRequest) {
                SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(naEsrkRequest);
                slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
            }

            DeferredLocationEventType deferredLocationEventType;
            TerminationCause terminationCause;
            DeferredmtlrData deferredmtlrData = null;
            // the deferredmt-lrData parameter shall be included if and only if the lcs-Event indicates a deferredmt-lrResponse.
            PeriodicLDRInfo periodicLDRInfo = null; // This parameter refers to the periodic reporting interval and reporting amount of the deferred periodic location.
            Integer sequenceNumber = null; // SequenceNumber ::= INTEGER (1..8639999)
            // sequenceNumber parameter refers to the number of the periodic location reports completed.
            // The sequence number would be set to 1 in the first location report and increment by 1 for each new report.
            // When the number reaches the reporting amount value,
            // the H-GMLC (for a periodic MT-LR or a periodic MO-LR transfer to third party) will know the procedure is complete
            if (lcsEvent == LCSEvent.deferredmtlrResponse) {
                boolean msAvailable = false;
                boolean enteringIntoArea = false;
                boolean leavingFromArea = false;
                boolean beingInsideArea = false;
                boolean periodicLDR = false;
                switch (rand.nextInt(5) + 1) {
                    case 1:
                        msAvailable = true;
                        break;
                    case 2:
                        enteringIntoArea = true;
                        break;
                    case 3:
                        leavingFromArea = true;
                        break;
                    case 4:
                        beingInsideArea = true;
                        break;
                    case 5:
                        periodicLDR = true;
                        int reportingAmount = 3;
                        int reportingInterval = 600;
                        int randReporting = rand.nextInt(2) + 1;
                        if (randReporting == 1) {
                            int reportingAmountMilliseconds = 863999; // ReportingAmountMilliseconds ::= INTEGER (1..8639999000)
                            int reportingIntervalMilliseconds = 100; // ReportingIntervalMilliseconds ::= INTEGER (1..999)
                            ReportingOptionMilliseconds reportingOptionMilliseconds = new ReportingOptionMillisecondsImpl(reportingAmountMilliseconds, reportingIntervalMilliseconds);
                            periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, reportingOptionMilliseconds);
                        } else {
                            periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, null);
                        }
                        sequenceNumber = 1;
                        break;
                }
                deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
                switch (rand.nextInt(20) + 1) {
                    case 1:
                        terminationCause = TerminationCause.normal;
                        break;
                    case 2:
                        terminationCause = TerminationCause.errorUndefined;
                        break;
                    case 3:
                        terminationCause = TerminationCause.internalTimeout;
                        break;
                    case 4:
                        terminationCause = TerminationCause.congestion;
                        break;
                    case 5:
                        terminationCause = TerminationCause.privacyViolation;
                        break;
                    case 6:
                        terminationCause = TerminationCause.shapeOfLocationEstimateNotSupported;
                        break;
                    case 7:
                        terminationCause = TerminationCause.subscriberTermination;
                        break;
                    case 8:
                        terminationCause = TerminationCause.uETermination;
                        break;
                    case 9:
                        terminationCause = TerminationCause.networkTermination;
                        break;
                    default:
                        terminationCause = TerminationCause.mtlrRestart;
                        break;
                }
                if (terminationCause == TerminationCause.mtlrRestart)
                    deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
                else
                    deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, null);
            }

            Integer lcsServiceTypeID = 1;
            boolean pseudonymIndicator = false;
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled;

            VelocityEstimate velocityEstimate = null;
            VelocityType velocityType = VelocityType.HorizontalWithVerticalVelocityAndUncertainty;
            int horizontalSpeed = rand.nextInt(100) + 10;
            int bearing = rand.nextInt(5) + 1;
            int verticalSpeed = rand.nextInt(10) + 1;
            int uncertaintyHorizontalSpeed = rand.nextInt(5) + 1;
            int uncertaintyVerticalSpeed = rand.nextInt(2) + 1;
            try {
                velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed,
                        uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }

            boolean moLrShortCircuitIndicator = true;

            int mcc, mnc, lac, ci;
            mcc = 748;
            mnc = 1;
            lac = 101;
            ci = 10263;
            boolean saiPresent = false;
            switch(rand.nextInt(10) + 1) {
                case 1:
                    saiPresent = true;
                    break;
                case 2:
                    lac = 119;
                    ci = 15336;
                    break;
                case 3:
                    lac = 118;
                    ci = 292;
                    break;
                case 4:
                    lac = 109;
                    ci = 10175;
                    saiPresent = true;
                    break;
                case 5:
                    lac = 11;
                    ci = 4812;
                    saiPresent = true;
                    break;
                case 6:
                    mnc = 7;
                    lac = 8820;
                    ci = 9748;
                    break;
                case 7:
                    mnc = 7;
                    lac = 8552;
                    ci = 8239;
                    saiPresent = true;
                    break;
                case 8:
                    mnc = 10;
                    lac = 9501;
                    ci = 35100;
                    break;
                case 9:
                    mnc = 7;
                    lac = 8313;
                    ci = 9281;
                    saiPresent = true;
                    break;
                case 10:
                    mnc = 7;
                    lac = 8820;
                    ci = 8051;
                    break;
            }
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
            CellGlobalIdOrServiceAreaIdFixedLength cgiOrSai = null;
            try {
                cgiOrSai = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdFixedLength(mcc, mnc, lac, ci);
            } catch (MAPException ex) {
                logger.error(ex.getMessage());
            }
            cellGlobalIdOrServiceAreaIdOrLAI = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdOrLAI(cgiOrSai);

            PositioningDataInformationImpl geranPositioningDataInfo =  null;
            UtranPositioningDataInfoImpl utranPositioningDataInfo = null;
            GeranGANSSpositioningDataImpl geranGanssPositioningData = null;
            UtranGANSSpositioningDataImpl utranGanssPositioningData = null;
            UtranAdditionalPositioningData utranAdditionalPositioningData = null;
            // Method=Mobile Based E-OTD, Usage=1: Attempted successfully: results not used to generate location
            // Method=Mobile Assisted E-OTD, Usage=3: Attempted successfully: results used to generate location
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=0: Attempted unsuccessfully due to failure or interruption
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            // Method=Timing Advance, Usage=3: Attempted successfully: results used to generate location
            // Method=Conventional GPS, Usage=2: Attempted successfully: results used to verify but not generate location
            // byte[] geranPosData = new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60};

            // Method=OTDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Reserved (GERAN use only), Usage=0: Attempted unsuccessfully due to failure or interruption - not used
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=2: Attempted successfully: results used to verify but not generate location - not used
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            // byte[] utranPosData = new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b};

            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            // byte[] geranGANSSData = new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03};

            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            // byte[] utranGanssData = new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03};

            // Method=Standalone, AddPosId=WLAN
            // Method=MS-Assisted, AddPosId=Bluetooth
            // byte[] utranAddPosData = new byte[] {0x57, (byte) 0x8F};

            switch (rand.nextInt(4) + 1) {
                case 1:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    break;
                case 2:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03});
                    break;
                case 3:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
                    break;
                case 4:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03});
                    utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {0x57, (byte) 0x8F});
                    break;
            }

            boolean isMsc = true;
            ServingNodeAddress targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, isMsc);

            Integer lcsReferenceNumber = null;
            if (isDeferred) // If a lcs event indicates deferred mt-lr response, the lcs-Reference number shall be included.
             lcsReferenceNumber = pslReferenceNumber;

            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());

            Integer utranBaroPressureMeas = null;
            UtranCivicAddress utranCivicAddress = null;
            if (geranPositioningDataInfo == null) {
                utranBaroPressureMeas = rand.nextInt(85000) + 30000; // UtranBaroPressureMeas ::= INTEGER (30000..115000)
                //File civicAddressFile = new File("map/load/src/main/java/org/restcomm/protocols/ss7/map/load/lsm/civicAddress.xml");
                //byte[] civicAddressByteArray = new byte[(int) civicAddressFile.length()];
                String civicAddressString = "<cl:civicAddress>\n" +
                        "                        <cl:country>US</cl:country>\n" +
                        "                        <cl:A1>New York</cl:A1>\n" +
                        "                        <cl:A3>New York</cl:A3>\n" +
                        "                        <cl:A6>Broadway</cl:A6>\n" +
                        "                        <cl:HNO>123</cl:HNO>\n" +
                        "                        <cl:LOC>Suite 75</cl:LOC>\n" +
                        "                        <cl:PC>10027-0401</cl:PC>\n" +
                        "                    </cl:civicAddress>";
                byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
                utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);
            }

            mapDialogSLR.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk,
                    locationEstimate, ageOfLocationEstimate, slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData,
                    lcsReferenceNumber, geranPositioningDataInfo, utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI,
                    hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate,
                    sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

            // This will initiate the TC-BEGIN with INVOKE component
            mapDialogSLR.send();

        } catch (MAPException e) {
            logger.error("Error while sending MAP SLR:", e);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onProvideSubscriberLocationRequest for DialogId={}", provideSubscriberLocationRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = provideSubscriberLocationRequestIndication.getInvokeId();
            MAPDialogLsm mapDialogPslResponse = provideSubscriberLocationRequestIndication.getMAPDialog();

            // Create Routing Information parameters for concerning MAP operation
            MAPParameterFactoryImpl mapParameterFactory = new MAPParameterFactoryImpl();
            Random rand = new Random();

            ExtGeographicalInformation locationEstimate = null;
            TypeOfShape typeOfShape = null;
            double latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, uncertaintyAltitude, uncertaintyRadius,
                    offsetAngle, includedAngle;
            int confidence, altitude, innerRadius;
            EllipsoidPoint ellipsoidPoint1, ellipsoidPoint2, ellipsoidPoint3, ellipsoidPoint4, ellipsoidPoint5, ellipsoidPoint6;
            // ellipsoidPoint7, ellipsoidPoint8, ellipsoidPoint9, ellipsoidPoint10, ellipsoidPoint11, ellipsoidPoint12, ellipsoidPoint13,
            // ellipsoidPoint14, ellipsoidPoint15;
            // 3 <= numberOfPoints <= 15
            Integer ageOfLocationEstimate = null;
            AddGeographicalInformation additionalLocationEstimate = null;
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;
            switch (rand.nextInt(6) + 1) {
                case 1:
                    typeOfShape = TypeOfShape.EllipsoidPoint;
                    latitude = 34.909744;
                    longitude = -56.146317;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(latitude, longitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 0;
                    break;
                case 2:
                    typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                    latitude = -34.910349;
                    longitude = -56.149832;
                    uncertainty = 5.1;
                    accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithUncertaintyCircle(latitude, longitude, uncertainty);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 1;
                    break;
                case 3:
                    typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyEllipse;
                    latitude = -34.905624;
                    longitude = -55.042191;
                    uncertaintySemiMajorAxis = 21.2;
                    uncertaintySemiMinorAxis = 10.4;
                    angleOfMajorAxis = 30.0; // orientation of major axis
                    confidence = 1;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithUncertaintyEllipse(latitude, longitude,
                                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 0;
                    break;
                case 4:
                    typeOfShape = TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid;
                    latitude = -34.956436;
                    longitude = -54.937820;
                    altitude = 570;
                    uncertaintySemiMajorAxis = 25.4;
                    uncertaintySemiMinorAxis = 12.1;
                    angleOfMajorAxis = 30.2; // orientation of major axis
                    uncertaintyAltitude = 80.1;
                    confidence = 5;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid(latitude,
                                longitude, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 5;
                    accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled;
                    break;
                case 5:
                    typeOfShape = TypeOfShape.EllipsoidArc;
                    latitude = -34.939956;
                    longitude = -54.914474;
                    innerRadius = 5;
                    uncertaintyRadius = 1.50;
                    offsetAngle = 20.0;
                    includedAngle = 20.0;
                    confidence = 2;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidArc(latitude, longitude, innerRadius,
                                uncertaintyRadius, offsetAngle, includedAngle, confidence);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 10;
                    break;
                case 6:
                    typeOfShape = TypeOfShape.Polygon;
                    latitude = 0.0;
                    longitude = 0.0;
                    try {
                        locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(latitude, longitude);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                    ageOfLocationEstimate = 0;
                    break;
            }

            if (typeOfShape == TypeOfShape.Polygon) {
                try {
                    switch (rand.nextInt(6) + 1) {
                        case 1:
                            byte[] polygonData1 = { 83,
                                    41, (byte) 234, (byte) 138, 55, 67, 17,
                                    41, (byte) 234, (byte) 136, 55, 67, 3,
                                    41, (byte) 234, 0, 55, 67, 24};
                            Polygon polygon1 = new PolygonImpl(polygonData1);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon1.getData());
                            break;
                        case 2:
                            byte[] polygonData2 = { 83,
                                    44, 29, (byte) 188, 53, (byte) 227, (byte) 135,
                                    44, 29, (byte) 193, 53, (byte) 227, (byte) 130,
                                    44, 29, (byte) 190, 53, (byte) 227, 123};
                            Polygon polygon2 = new PolygonImpl(polygonData2);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon2.getData());
                            break;
                        case 3:
                            byte[] polygonData3 = { 83,
                                    36, (byte) 167, 60, 52, 37, 0,
                                    36, (byte) 167, 49, 52, 36, (byte) 255,
                                    36, (byte) 167, 50, 52, 37, 0};
                            Polygon polygon3 = new PolygonImpl(polygonData3);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon3.getData());
                            break;
                        case 4:
                            byte[] polygonData4 = { 83,
                                    36, 124, (byte) 163, 59, 49, 112,
                                    36, 126, 7, 59, 49, (byte) 138,
                                    36, 127, (byte) 224, 59, 49, 72};
                            Polygon polygon4 = new PolygonImpl(polygonData4);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon4.getData());
                            break;
                        case 5:
                            byte[] polygonData5 = { 84,
                                    37, (byte) 229, (byte) 179, 52, 66, (byte) 211,
                                    37, (byte) 230, 64, 52, 67, 124,
                                    37, (byte) 230, (byte) 131, 52, 67, 121,
                                    37, (byte) 230, (byte) 132, 52, 67, 125};
                            Polygon polygon5 = new PolygonImpl(polygonData5);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon5.getData());
                            break;
                        case 6:
                            ellipsoidPoint1 = new EllipsoidPoint(-2.907010, 70.778014);
                            ellipsoidPoint2 = new EllipsoidPoint(-3.017238, 70.708922);
                            ellipsoidPoint3 = new EllipsoidPoint(-2.941387, 70.432091);
                            ellipsoidPoint4 = new EllipsoidPoint(-3.040019, 70.681903);
                            ellipsoidPoint5 = new EllipsoidPoint(-3.045001, 70.700109);
                            ellipsoidPoint6 = new EllipsoidPoint(-2.989001, 71.000004);
                            EllipsoidPoint[] ellipsoidPoints = {ellipsoidPoint1, ellipsoidPoint2, ellipsoidPoint3, ellipsoidPoint4, ellipsoidPoint5, ellipsoidPoint6};
                            PolygonImpl polygon6 = new PolygonImpl();
                            polygon6.setData(ellipsoidPoints);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon6.getData());
                            break;
                    }
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            }

            PositioningDataInformationImpl geranPositioningDataInfo =  null;
            UtranPositioningDataInfoImpl utranPositioningDataInfo = null;
            GeranGANSSpositioningDataImpl geranGanssPositioningData = null;
            UtranGANSSpositioningDataImpl utranGanssPositioningData = null;
            UtranAdditionalPositioningData utranAdditionalPositioningData = null;
            // Method=Mobile Based E-OTD, Usage=1: Attempted successfully: results not used to generate location
            // Method=Mobile Assisted E-OTD, Usage=3: Attempted successfully: results used to generate location
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=0: Attempted unsuccessfully due to failure or interruption
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            // Method=Timing Advance, Usage=3: Attempted successfully: results used to generate location
            // Method=Conventional GPS, Usage=2: Attempted successfully: results used to verify but not generate location
            // byte[] geranPosData = new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60};

            // Method=OTDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Reserved (GERAN use only), Usage=0: Attempted unsuccessfully due to failure or interruption - not used
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=2: Attempted successfully: results used to verify but not generate location - not used
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            // byte[] utranPosData = new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b};

            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            // byte[] geranGANSSData = new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03};

            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            // byte[] utranGanssData = new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03};

            // Method=Standalone, AddPosId=WLAN
            // Method=MS-Assisted, AddPosId=Bluetooth
            // byte[] utranAddPosData = new byte[] {0x57, (byte) 0x8F};

            switch (rand.nextInt(4) + 1) {
                case 1:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    break;
                case 2:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03});
                    break;
                case 3:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
                    break;
                case 4:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03});
                    utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {0x57, (byte) 0x8F});
                    break;
            }

            boolean deferredMTLRResponseIndicator = false;
            LocationType locationType = provideSubscriberLocationRequestIndication.getLocationType();
            if (locationType.getDeferredLocationEventType() != null) {
                deferredMTLRResponseIndicator = true;
            }

            int mcc, mnc, lac, ci;
            mcc = 748;
            mnc = 1;
            lac = 101;
            ci = 10263;
            boolean saiPresent = false;
            switch(rand.nextInt(10) + 1) {
                case 1:
                    saiPresent = true;
                    break;
                case 2:
                    lac = 119;
                    ci = 15336;
                    break;
                case 3:
                    lac = 118;
                    ci = 292;
                    break;
                case 4:
                    lac = 109;
                    ci = 10175;
                    saiPresent = true;
                    break;
                case 5:
                    lac = 11;
                    ci = 4812;
                    saiPresent = true;
                    break;
                case 6:
                    mnc = 7;
                    lac = 8820;
                    ci = 9748;
                    break;
                case 7:
                    mnc = 7;
                    lac = 8552;
                    ci = 8239;
                    saiPresent = true;
                    break;
                case 8:
                    mnc = 10;
                    lac = 9501;
                    ci = 35100;
                    break;
                case 9:
                    mnc = 7;
                    lac = 8313;
                    ci = 9281;
                    saiPresent = true;
                    break;
                case 10:
                    mnc = 7;
                    lac = 8820;
                    ci = 8051;
                    break;
            }
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
            CellGlobalIdOrServiceAreaIdFixedLength cgiOrSai = null;
            try {
                cgiOrSai = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdFixedLength(mcc, mnc, lac, ci);
            } catch (MAPException ex) {
                logger.error(ex.getMessage());
            }
            cellGlobalIdOrServiceAreaIdOrLAI = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdOrLAI(cgiOrSai);

            VelocityEstimate velocityEstimate = null;
            if (provideSubscriberLocationRequestIndication.getLCSQoS() != null) {
                if (provideSubscriberLocationRequestIndication.getLCSQoS().getVelocityRequest()) {
                    VelocityType velocityType = VelocityType.HorizontalWithVerticalVelocityAndUncertainty;
                    int horizontalSpeed = rand.nextInt(100) + 10;
                    int bearing = rand.nextInt(5) + 1;
                    int verticalSpeed = rand.nextInt(10) + 1;
                    int uncertaintyHorizontalSpeed = rand.nextInt(5) + 1;
                    int uncertaintyVerticalSpeed = rand.nextInt(2) + 1;
                    try {
                        velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed,
                                uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
                    } catch (MAPException e) {
                        logger.error(e.getMessage());
                    }
                }
            }

            boolean moLrShortCircuitIndicator = provideSubscriberLocationRequestIndication.getMoLrShortCircuitIndicator();

            ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            ServingNodeAddress targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, true);

            Integer utranBaroPressureMeas = 110000; // UtranBaroPressureMeas ::= INTEGER (30000..115000)

            //File civicAddressFile = new File("map/load/src/main/java/org/restcomm/protocols/ss7/map/load/lsm/civicAddress.xml");
            //byte[] civicAddressByteArray = new byte[(int) civicAddressFile.length()];
            String civicAddressString = "<cl:civicAddress>\n" +
                    "                        <cl:country>US</cl:country>\n" +
                    "                        <cl:A1>New York</cl:A1>\n" +
                    "                        <cl:A3>New York</cl:A3>\n" +
                    "                        <cl:A6>Broadway</cl:A6>\n" +
                    "                        <cl:HNO>123</cl:HNO>\n" +
                    "                        <cl:LOC>Suite 75</cl:LOC>\n" +
                    "                        <cl:PC>10027-0401</cl:PC>\n" +
                    "                    </cl:civicAddress>";
            byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
            UtranCivicAddress utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);

            mapDialogPslResponse.addProvideSubscriberLocationResponse(invokeId, locationEstimate, geranPositioningDataInfo, utranPositioningDataInfo,
                    ageOfLocationEstimate, additionalLocationEstimate, null, deferredMTLRResponseIndicator,
                    cellGlobalIdOrServiceAreaIdOrLAI, saiPresent, accuracyFulfilmentIndicator, velocityEstimate,
                    moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

            mapDialogPslResponse.close(false);

            Thread.sleep(2000);
            /*
             * Create Dialog for sending not deferred MAP SLR to the GMLC
             */
            AddressString origRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            AddressString destRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_GMLC_ADDRESS);
            SccpAddress origSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SERVER_SSN, SCCP_MSC_ADDRESS);
            SccpAddress destSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, CLIENT_SSN, SCCP_GMLC_ADDRESS);
            MAPDialogLsm slrDialog = mapProvider.getMAPServiceLsm()
                    .createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                            MAPApplicationContextVersion.version3), origSccpAddress, origRef, destSccpAddress, destRef);

            Integer lcsReferenceNumber = rand.nextInt(Integer.MAX_VALUE) - 1;
            sendMapSLR(slrDialog, true, lcsReferenceNumber);

        } catch (MAPException e) {
            logger.error("Error while sending MAP PSL response:", e);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponseIndication) {

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
    public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) {

    }

    @Override
    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {

    }

    @Override
    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {

    }
}
