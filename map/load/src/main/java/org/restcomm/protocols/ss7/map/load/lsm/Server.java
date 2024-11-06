package org.restcomm.protocols.ss7.map.load.lsm;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
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
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.Polygon;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingOptionMilliseconds;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent.emergencyCallOrigination;
import static org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType.currentLocation;
import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

public class Server extends TestHarnessLocationServicesManagement {

    private static Logger logger = Logger.getLogger(Server.class);

    // MAP
    private MAPStackImpl mapStack;
    private static MAPProvider mapProvider;

    // TCAP
    private TCAPStack tcapStack;

    // SCCP
    SccpExtModuleImpl sccpExtModule;
    private SccpStackImpl sccpStack;
    private SccpResource sccpResource;
    private Router router;
    private RouterExt routerExt;

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

        // Step 2 : Create ASP
        AspFactory aspFactor = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");

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

        this.router = this.sccpStack.getRouter();
        this.routerExt = sccpExtModule.getRouterExt();
        this.sccpResource = this.sccpStack.getSccpResource();

        this.sccpResource.addRemoteSpc(0, CLIENT_SPC, 0, 0);
        this.sccpResource.addRemoteSsn(0, CLIENT_SPC, CLIENT_SSN, 0, false);

        this.router.addMtp3ServiceAccessPoint(1, 1, SERVER_SPC, NETWORK_INDICATOR, 0, null);
        this.router.addMtp3Destination(1, 1, CLIENT_SPC, CLIENT_SPC, 0, 255, 255);
        this.router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, SERVER_SPC, 0);
        this.routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, CLIENT_SPC, 0);
        this.routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        this.routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0, null);
        this.routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern,
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
        this.mapStack = new MAPStackImpl("TestServer", this.tcapStack.getProvider());
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
            ssn = SERVER_SSN;
        }
        return fact.createSccpAddress(ri, gt, dpc, ssn);
    }

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].equalsIgnoreCase("tcp")) {
            ipChannelType = IpChannelType.TCP;
        }
        System.out.println("IpChannelType="+ipChannelType);

        if (args.length >= 2) {
            TestHarnessLocationServicesManagement.CLIENT_IP = args[1];
        }
        System.out.println("CLIENT_IP="+ TestHarnessLocationServicesManagement.CLIENT_IP);

        if (args.length >= 3) {
            TestHarnessLocationServicesManagement.CLIENT_PORT = Integer.parseInt(args[2]);
        }
        System.out.println("CLIENT_PORT="+ TestHarnessLocationServicesManagement.CLIENT_PORT);

        if (args.length >= 4) {
            TestHarnessLocationServicesManagement.SERVER_IP = args[3];
        }
        System.out.println("SERVER_IP="+ TestHarnessLocationServicesManagement.SERVER_IP);

        if (args.length >= 5) {
            TestHarnessLocationServicesManagement.SERVER_PORT = Integer.parseInt(args[4]);
        }
        System.out.println("SERVER_PORT="+ TestHarnessLocationServicesManagement.SERVER_PORT);

        if (args.length >= 6) {
            TestHarnessLocationServicesManagement.CLIENT_SPC = Integer.parseInt(args[5]);
        }
        System.out.println("CLIENT_SPC="+ TestHarnessLocationServicesManagement.CLIENT_SPC);

        if (args.length >= 7) {
            TestHarnessLocationServicesManagement.SERVER_SPC = Integer.parseInt(args[6]);
        }
        System.out.println("SERVER_SPC="+ TestHarnessLocationServicesManagement.SERVER_SPC);

        if (args.length >= 8) {
            TestHarnessLocationServicesManagement.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        System.out.println("NETWORK_INDICATOR="+ TestHarnessLocationServicesManagement.NETWORK_INDICATOR);

        if (args.length >= 9) {
            TestHarnessLocationServicesManagement.SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        System.out.println("SERVICE_INDICATOR="+ TestHarnessLocationServicesManagement.SERVICE_INDICATOR);

        if (args.length >= 10) {
            TestHarnessLocationServicesManagement.SSN = Integer.parseInt(args[9]);
        }
        System.out.println("SSN="+ TestHarnessLocationServicesManagement.SSN);

        if (args.length >= 11) {
            TestHarnessLocationServicesManagement.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        System.out.println("ROUTING_CONTEXT="+ TestHarnessLocationServicesManagement.ROUTING_CONTEXT);

        if(args.length >= 12) {
            TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        System.out.println("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT=" + TestHarnessLocationServicesManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

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
            logger.debug(String.format("sendRoutingInfoForLCSRequestIndication for DialogId=%d", sendRoutingInfoForLCSRequestIndication
                    .getMAPDialog().getLocalDialogId()));
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
            int lmsiRandom = rand.nextInt(10) + 1;
            switch (lmsiRandom) {
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
            DiameterIdentity sgsnName = new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnRealm = new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            LCSLocationInfo lcsLocationInfo = mapFactory.createLCSLocationInfo(mscNumber, lmsi, null, gprsNodeIndicator,
                    additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            GSNAddress vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
            GSNAddress pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x12 });
            GSNAddress additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });

            sriLcsDialog.addSendRoutingInfoForLCSResponse(invokeId, subscriberIdentity, lcsLocationInfo, null, vGmlcAddress, hGmlcAddress,
                    pprAddress, additionalVGmlcAddress);
            // This will initiate the TC-BEGIN with INVOKE component
            sriLcsDialog.close(false);

            // Create Dialog for sending SLR to the GMLC
            AddressString origRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_MSC_ADDRESS);
            AddressString destRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_GMLC_ADDRESS);
            SccpAddress origSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, SERVER_SSN, SCCP_MSC_ADDRESS);
            SccpAddress destSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, CLIENT_SSN, SCCP_GMLC_ADDRESS);
            MAPDialogLsm slrDialog = mapProvider.getMAPServiceLsm()
                    .createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                            MAPApplicationContextVersion.version3), origSccpAddress, origRef, destSccpAddress, destRef);

            // SLR is not deferred MT LT
            sendMapSLR(slrDialog, false);


        } catch (MAPException mapException) {
            logger.error("MAP Exception while processing onSendRoutingInfoForLCSRequest ", mapException);
        } catch (Exception e) {
            logger.error("Exception while processing onSendRoutingInfoForLCSRequest ", e);
        }
    }

    private void sendMapSLR(MAPDialogLsm mapDialogLsm, boolean isDeferred) {
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
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    SCCP_MSC_ADDRESS);
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    SCCP_SGSN_ADDRESS);

            ISDNAddressString naEsrd = null;
            ISDNAddressString naEsrk = null;
            int naEsr = rand.nextInt(3) + 1;
            if (naEsr == 1)
                naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1210101075");
            else
                naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9289277009");

            // LCS-Event ::= ENUMERATED { emergencyCallOrigination (0), emergencyCallRelease (1), mo-lr (2), ..., deferredmt-lrResponse (3) }
            LCSEvent lcsEvent = emergencyCallOrigination;
            if (isDeferred)
                lcsEvent = LCSEvent.deferredmtlrResponse;

            LocationEstimateType locationEstimateType = currentLocation;
            // public enum LocationEstimateType {currentLocation(0), currentOrLastKnownLocation(1), initialLocation(2), activateDeferredLocation(3), cancelDeferredLocation(4)..

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
            int lmsiRandom = rand.nextInt(10) + 1;
            switch (lmsiRandom) {
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
            DiameterIdentity sgsnName = new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            DiameterIdentity sgsnRealm = new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
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
            int typeOfShapeRandomOption = rand.nextInt(6) + 1;
            switch (typeOfShapeRandomOption) {
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

                /*  char packet_bytes[] = { 0x53, 0x29, 0xea, 0x8a, 0x37, 0x43, 0x11, 0x29, 0xea, 0x88, 0x37, 0x43, 0x03, 0x29, 0xea, 0x00, 0x37, 0x43, 0x18};   */
                byte[] polygonData1 = { 83,
                        41, (byte) 234, (byte) 138, 55, 67, 17,
                        41, (byte) 234, (byte) 136, 55, 67, 3,
                        41, (byte) 234, 0, 55, 67, 24};

                /*  char packet_bytes[] = { 0x53, 0x29, 0xea, 0x8a, 0x37, 0x43, 0x11, 0x29, 0xea, 0x88, 0x37, 0x43, 0x03, 0x29, 0xea, 0x00, 0x37, 0x43, 0x18};  */
                byte[] polygonData2 = { 83,
                        44, 29, (byte) 188, 53, (byte) 227, (byte) 135,
                        44, 29, (byte) 193, 53, (byte) 227, (byte) 130,
                        44, 29, (byte) 190, 53, (byte) 227, 123};

                /* char packet_bytes[] =  { 0x53, 0x24, 0xa7, 0x3c, 0x34, 0x25, 0x00, 0x24, 0xa7, 0x31, 0x34, 0x24, 0xff, 0x24, 0xa7, 0x32,0x34, 0x25, 0x00}; */
                byte[] polygonData3 = { 83,
                        36, (byte) 167, 60, 52, 37, 0,
                        36, (byte) 167, 49, 52, 36, (byte) 255,
                        36, (byte) 167, 50, 52, 37, 0};

                /* char packet_bytes[] =  { 0x53, 0x24, 0x7c, 0xa3, 0x3b, 0x31, 0x70, 0x24, 0x7e, 0x07, 0x3b, 0x31, 0x8a, 0x24, 0x7f, 0xe0, 0x3b, 0x31, 0x48}; */
                byte[] polygonData4 = { 83,
                        36, 124, (byte) 163, 59, 49, 112,
                        36, 126, 7, 59, 49, (byte) 138,
                        36, 127, (byte) 224, 59, 49, 72};

            /* char packet_bytes[] =  { 0x53, 0x25, 0xe5, 0xb3, 0x34, 0x42, 0xd3, 0x25, 0xe6, 0x40, 0x34, 0x43, 0x7c, 0x25, 0xe6, 0x83, 0x34, 0x43, 0x79
                                        0x25, 0xe6, 0x84, 0x34, 0x43, 0x7d};  */
                byte[] polygonData5 = { 84,
                        37, (byte) 229, (byte) 179, 52, 66, (byte) 211,
                        37, (byte) 230, 64, 52, 67, 124,
                        37, (byte) 230, (byte) 131, 52, 67, 121,
                        37, (byte) 230, (byte) 132, 52, 67, 125};

                Polygon polygon1;
                Polygon polygon2;
                Polygon polygon3;
                Polygon polygon4;
                Polygon polygon5;
                PolygonImpl polygon6 = new PolygonImpl();

                try {
                    switch (additionalLocationEstimateRandomOption) {
                        case 1:
                            polygon1 = new PolygonImpl(polygonData1);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon1.getData());
                            break;
                        case 2:
                            polygon2 = new PolygonImpl(polygonData2);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon2.getData());
                            break;
                        case 3:
                            polygon3 = new PolygonImpl(polygonData3);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon3.getData());
                            break;
                        case 4:
                            polygon4 = new PolygonImpl(polygonData4);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon4.getData());
                            break;
                        case 5:
                            polygon5 = new PolygonImpl(polygonData5);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon5.getData());
                            break;
                        case 6:
                            polygon6.setData(ellipsoidPoints);
                            additionalLocationEstimate = new AddGeographicalInformationImpl(polygon6.getData());
                            break;
                    }
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            }

            SLRArgPCSExtensions slrArgPcsExtensions = null;
            /*long[] oid = {0, 0, 17, 773, 1, 1, 1};
            byte[] privateExtData = hexStringToByteArray("1144");
            MAPPrivateExtension mapPrivateExtension = new MAPPrivateExtensionImpl(oid, privateExtData);
            ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<>();
            privateExtensionList.add(mapPrivateExtension);
            SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(true);
            SLRArgExtensionContainer slrArgExtensionContainer = new SLRArgExtensionContainerImpl(privateExtensionList, slrArgPcsExtensions);*/

            DeferredLocationEventType deferredLocationEventType;
            TerminationCause terminationCause;
            DeferredmtlrData deferredmtlrData = null;
            // the deferredmt-lrData parameter shall be included if and only if the lcs-Event indicates a deferredmt-lrResponse.
            if (lcsEvent == LCSEvent.deferredmtlrResponse) {
                boolean msAvailable = false;
                boolean enteringIntoArea = true;
                boolean leavingFromArea = false;
                boolean beingInsideArea = false;
                boolean periodicLDR = false;
                deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
                terminationCause = TerminationCause.congestion;
                deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
            }

            // Method=Mobile Based E-OTD, Usage=1: Attempted successfully: results not used to generate location
            // Method=Mobile Assisted E-OTD, Usage=3: Attempted successfully: results used to generate location
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=0: Attempted unsuccessfully due to failure or interruption
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            // Method=Timing Advance, Usage=3: Attempted successfully: results used to generate location
            // Method=Conventional GPS, Usage=2: Attempted successfully: results used to verify but not generate location
            byte[] geranPosData = new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60};
            PositioningDataInformationImpl geranPositioningDataInfo = new PositioningDataInformationImpl(geranPosData);

            // Method=OTDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Reserved (GERAN use only), Usage=0: Attempted unsuccessfully due to failure or interruption - not used
            // Method=U-TDOA, Usage=3: Attempted successfully: results used to generate location
            // Method=Cell ID, Usage=2: Attempted successfully: results used to verify but not generate location - not used
            // Method=Mobile Assisted GPS, Usage=3: Attempted successfully: results used to generate location
            byte[] utranPosData = new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b};
            UtranPositioningDataInfoImpl utranPositioningDataInfo = new UtranPositioningDataInfoImpl(utranPosData);

            Integer lcsServiceTypeID = 1;
            boolean pseudonymIndicator = false;
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled;

            VelocityType velocityType = VelocityType.HorizontalWithVerticalVelocityAndUncertainty;
            int horizontalSpeed = 101;
            int bearing = 3;
            int verticalSpeed = 2;
            int uncertaintyHorizontalSpeed = 5;
            int uncertaintyVerticalSpeed = 1;
            VelocityEstimate velocityEstimate = null;
            try {
                velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }

            Integer sequenceNumber = rand.nextInt(8639999) - 1; // SequenceNumber ::= INTEGER (1..8639999)

            int reportingAmount = 3;
            int reportingInterval = 600;
            int reportingAmountMilliseconds = 8639999; // ReportingAmountMilliseconds ::= INTEGER (1..8639999000)
            int reportingIntervalMilliseconds = 999; // ReportingIntervalMilliseconds ::= INTEGER (1..999)
            ReportingOptionMilliseconds reportingOptionMilliseconds = new ReportingOptionMillisecondsImpl(reportingAmountMilliseconds, reportingIntervalMilliseconds);
            PeriodicLDRInfo periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, reportingOptionMilliseconds);

            boolean moLrShortCircuitIndicator = true;

            int mcc, mnc, lac, ci;
            mcc = 748;
            mnc = 1;
            lac = 101;
            ci = 10263;
            boolean saiPresent = false;
            int cgiRand = rand.nextInt(10) + 1;
            switch(cgiRand) {
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

            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            byte[] geranGANSSData = new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03};
            GeranGANSSpositioningDataImpl geranGanssPositioningData = new GeranGANSSpositioningDataImpl(geranGANSSData);
            // Method=MS-Based, GANSSId=Galileo
            // Method=MS-Assisted, GANSSId=GLONASS
            // Method=Conventional, GANSSId=SBAS
            byte[] utranGanssData = new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03};
            UtranGANSSpositioningDataImpl utranGanssPositioningData = new UtranGANSSpositioningDataImpl(utranGanssData);

            boolean isMsc = true;
            ServingNodeAddress servingNodeAddress = new ServingNodeAddressImpl(networkNodeNumber, isMsc);

            Integer lcsReferenceNumber = null;
            if (isDeferred) // If a lcs event indicates deferred mt-lr response, the lcs-Reference number shall be included.
             lcsReferenceNumber = rand.nextInt(Integer.MAX_VALUE) - 1;

            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });

            // Method=Standalone, AddPosId=WLAN
            // Method=MS-Assisted, AddPosId=Bluetooth
            byte[] data = new byte[] {0x57, (byte) 0x8F};
            UtranAdditionalPositioningData utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(data);
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

            mapDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk,
                    locationEstimate, ageOfLocationEstimate, null, additionalLocationEstimate, deferredmtlrData,
                    lcsReferenceNumber, geranPositioningDataInfo, utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI,
                    hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate,
                    sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    servingNodeAddress, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

            // This will initiate the TC-BEGIN with INVOKE component
            mapDialogLsm.send();
        } catch (MAPException e) {
            logger.error(String.format("Error while sending MAP SLR:" + e));
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

    protected static byte[] hexStringToByteArray(String s) {
        int len;
        byte[] data = null;
        if (s != null) {
            len = s.length();
            data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
        }
        return data;
    }
}
