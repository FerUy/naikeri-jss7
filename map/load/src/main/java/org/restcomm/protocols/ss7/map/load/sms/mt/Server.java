package org.restcomm.protocols.ss7.map.load.sms.mt;

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
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageAbsentSubscriberSM;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsGmscAlertEvent;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.restcomm.protocols.ss7.map.api.smstpdu.FailureCause;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageAbsentSubscriberSMImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.restcomm.protocols.ss7.map.service.sms.CorrelationIDImpl;
import org.restcomm.protocols.ss7.map.service.sms.IpSmGwGuidanceImpl;
import org.restcomm.protocols.ss7.map.service.sms.LocationInfoWithLMSIImpl;
import org.restcomm.protocols.ss7.map.service.sms.MWStatusImpl;
import org.restcomm.protocols.ss7.map.service.sms.SipUriImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.FailureCauseImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
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
import org.restcomm.protocols.ss7.tcap.asn.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Server extends TestHarnessSmsMt {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private MAPProvider mapProvider;

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
        As as = this.serverM3UAMgmt.createAs("RAS1", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);

        // Step 2 : Create ASP
        AspFactory aspFactory = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);

        // Step3 : Assign ASP to AS
        Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");

        // Step 4: Add Route. Remote point code is 2
        this.serverM3UAMgmt.addRoute(CLIENT_SPC, -1, -1, "RAS1");

        logger.debug("AS={}, ASP factory={}, ASP={}", as, aspFactory, asp);
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
        sccpResource.addRemoteSsn(0, CLIENT_SPC, SSN, 0, false);

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
        this.tcapStack = new TCAPStackImpl("TestServer", this.sccpStack.getSccpProvider(), SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        // MAP
        MAPStackImpl mapStack = new MAPStackImpl("TestServer", this.tcapStack.getProvider());
        this.mapProvider = mapStack.getMAPProvider();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSms().activate();

        mapStack.start();
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
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDialogRequest for DialogId={} DestinationReference={} OriginReference={} MAPExtensionContainer={}", mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer);
        }
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                        AddressString imsi, AddressString vlr) {
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

        this.endCount++;

        if ((this.endCount % 10000) == 0) {
            long currentTime = System.currentTimeMillis();
            long processingTime = currentTime - start;
            start = currentTime;
            logger.warn("Completed 10000 Dialogs in {} milliseconds", processingTime);
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

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].equalsIgnoreCase("tcp")) {
            ipChannelType = IpChannelType.TCP;
        }
        logger.info("IpChannelType={}", ipChannelType);

        if (args.length >= 2) {
            TestHarnessSmsMt.CLIENT_IP = args[1];
        }
        logger.info("CLIENT_IP={}", TestHarnessSmsMt.CLIENT_IP);

        if (args.length >= 3) {
            TestHarnessSmsMt.CLIENT_PORT = Integer.parseInt(args[2]);
        }
        logger.info("CLIENT_PORT={}", TestHarnessSmsMt.CLIENT_PORT);

        if (args.length >= 4) {
            TestHarnessSmsMt.SERVER_IP = args[3];
        }
        logger.info("SERVER_IP={}", TestHarnessSmsMt.SERVER_IP);

        if (args.length >= 5) {
            TestHarnessSmsMt.SERVER_PORT = Integer.parseInt(args[4]);
        }
        logger.info("SERVER_PORT={}", TestHarnessSmsMt.SERVER_PORT);

        if (args.length >= 6) {
            TestHarnessSmsMt.CLIENT_SPC = Integer.parseInt(args[5]);
        }
        logger.info("CLIENT_SPC={}", TestHarnessSmsMt.CLIENT_SPC);

        if (args.length >= 7) {
            TestHarnessSmsMt.SERVER_SPC = Integer.parseInt(args[6]);
        }
        logger.info("SERVER_SPC={}", TestHarnessSmsMt.SERVER_SPC);

        if (args.length >= 8) {
            TestHarnessSmsMt.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        logger.info("NETWORK_INDICATOR={}", TestHarnessSmsMt.NETWORK_INDICATOR);

        if (args.length >= 9) {
            TestHarnessSmsMt.SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        logger.info("SERVICE_INDICATOR={}", TestHarnessSmsMt.SERVICE_INDICATOR);

        if (args.length >= 10) {
            TestHarnessSmsMt.SSN = Integer.parseInt(args[9]);
        }
        logger.info("SSN={}", TestHarnessSmsMt.SSN);

        if (args.length >= 11) {
            TestHarnessSmsMt.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        logger.info("ROUTING_CONTEXT={}", TestHarnessSmsMt.ROUTING_CONTEXT);

        if (args.length >= 12){
            TestHarnessSmsMt.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT={}", TestHarnessSmsMt.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        final Server server = new Server();
        try {
            server.initializeStack(ipChannelType);
        } catch (Exception e) {
            logger.error("Exception when starting stack of load class for MT Server.", e);
        }
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
        if (logger.isDebugEnabled()) {
            logger.debug("onMoForwardShortMessageRequest for DialogId={}", moForwardShortMessageRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = moForwardShortMessageRequestIndication.getInvokeId();
            MAPDialogSms mapDialogSms = moForwardShortMessageRequestIndication.getMAPDialog();
            mapDialogSms.setUserObject(invokeId);
            mapDialogSms.close(false);

        } catch (MAPException e) {
            logger.error("Error while sending MoForwardShortMessageRequest ", e);
        }
    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwardShortMessageResponseIndication) {

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwardShortMessageRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onMtForwardShortMessageRequest for DialogId={}", mtForwardShortMessageRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = mtForwardShortMessageRequestIndication.getInvokeId();
            MAPDialogSms mapDialogSms = mtForwardShortMessageRequestIndication.getMAPDialog();

            Random rand = new Random();
            switch (rand.nextInt(4) + 1) {
                case 1:
                    UserData userData = new UserDataImpl("MT-FSM response", new DataCodingSchemeImpl(0), null, null);
                    ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(0);
                    FailureCause failureCause = new FailureCauseImpl(200);
                    SmsDeliverReportTpdu smsDeliverReportTpdu = new SmsDeliverReportTpduImpl(failureCause, protocolIdentifier, userData);
                    SmsSignalInfo sm_RP_UI = mapProvider.getMAPParameterFactory().createSmsSignalInfo(smsDeliverReportTpdu, null);
                    mapDialogSms.addMtForwardShortMessageResponse(invokeId, sm_RP_UI, null);
                    mapDialogSms.close(false);
                    break;
                case 2:
                    mapDialogSms.addMtForwardShortMessageResponse(invokeId, null, null);
                    mapDialogSms.close(false);
                    break;
                case 3:
                    ReturnResultLast returnResultLast = new ReturnResultLastImpl();
                    returnResultLast.setInvokeId(invokeId);
                    mapDialogSms.sendReturnResultLastComponent(returnResultLast);
                    mapDialogSms.close(false);
                    break;
                case 4:
                    MAPErrorMessageAbsentSubscriberSM errorMessageAbsentSubscriberSM = new MAPErrorMessageAbsentSubscriberSMImpl();
                    mapDialogSms.sendErrorComponent(invokeId, errorMessageAbsentSubscriberSM);
                    mapDialogSms.close(false);
                    break;
            }
        } catch (MAPException e) {
            logger.error("Error while sending MtForwardShortMessageRequest result ", e);
        }
    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwardShortMessageResponseIndication) {

    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onSendRoutingInfoForSMRequest for DialogId={}", sendRoutingInfoForSMRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = sendRoutingInfoForSMRequestIndication.getInvokeId();
            MAPDialogSms mapDialogSms = sendRoutingInfoForSMRequestIndication.getMAPDialog();
            mapDialogSms.setUserObject(invokeId);

            IMSI imsi;
            LocationInfoWithLMSI locationInfoWithLMSI;
            IpSmGwGuidance ipSmGwGuidance = null;

            Random rand = new Random();
            switch (rand.nextInt(3) + 1) {
                case 1:
                    imsi = new IMSIImpl("748031234567890");
                    locationInfoWithLMSI = setLocationInfoWithLMSI(sendRoutingInfoForSMRequestIndication);
                    mapDialogSms.addSendRoutingInfoForSMResponse(invokeId, imsi, locationInfoWithLMSI,
                            null, null, null);
                    mapDialogSms.close(false);
                    break;
                case 2:
                    imsi = new IMSIImpl("748031234567891");
                    locationInfoWithLMSI = setLocationInfoWithLMSI(sendRoutingInfoForSMRequestIndication);
                    if (sendRoutingInfoForSMRequestIndication.getIpSmGwGuidanceIndicator()) {
                        int minimumDeliveryTimeValue = 30;
                        int recommendedDeliveryTimeValue = 60;
                        ipSmGwGuidance = new IpSmGwGuidanceImpl(minimumDeliveryTimeValue, recommendedDeliveryTimeValue, null);
                    }
                    mapDialogSms.addSendRoutingInfoForSMResponse(invokeId, imsi, locationInfoWithLMSI,
                            null, null, ipSmGwGuidance);
                    mapDialogSms.close(false);
                    break;
                case 3:
                    ISDNAddressString storedMSISDN = sendRoutingInfoForSMRequestIndication.getMsisdn();
                    boolean scAddressNotIncluded = false;
                    boolean mnrfSet = true;
                    boolean mcefSet = false;
                    boolean mnrgSet = true;
                    boolean mnr5gSet = false;
                    boolean mnr5gn3gSet = false;
                    MWStatus mwStatus = new MWStatusImpl(scAddressNotIncluded, mnrfSet, mcefSet, mnrgSet, mnr5gSet, mnr5gn3gSet);
                    Integer absentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.NoPagingResponseViaTheMSC.getCode();
                    Integer additionalAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.DeregisteredInTheHLRForGPRS.getCode();
                    Integer smsf3gppAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.NoResponseViaTheIP_SM_GW.getCode();
                    Integer smsfNon3gppAbsentSubscriberDiagnosticSM = AbsentSubscriberDiagnosticSM.RoamingRestriction.getCode();
                    mapDialogSms.addInformServiceCentreRequest(storedMSISDN, mwStatus, null, absentSubscriberDiagnosticSM,
                            additionalAbsentSubscriberDiagnosticSM, smsf3gppAbsentSubscriberDiagnosticSM, smsfNon3gppAbsentSubscriberDiagnosticSM);
                    mapDialogSms.send();
                    MAPErrorMessageAbsentSubscriberSM errorMessageAbsentSubscriberSM = new MAPErrorMessageAbsentSubscriberSMImpl();
                    mapDialogSms.sendErrorComponent(invokeId, errorMessageAbsentSubscriberSM);
                    mapDialogSms.close(false);
                    break;
            }
        } catch (MAPException e) {
            logger.error("Error while sending SendRoutingInfoForSMResponse ", e);
        }
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMResponseIndication) {

    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onReportSMDeliveryStatusRequest for DialogId={}", reportSMDeliveryStatusRequestIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            MAPDialogSms mapDialogSms = reportSMDeliveryStatusRequestIndication.getMAPDialog();
            mapDialogSms.setUserObject(reportSMDeliveryStatusRequestIndication.getInvokeId());
            ReturnResultLast returnResultLast = new ReturnResultLastImpl();
            returnResultLast.setInvokeId(reportSMDeliveryStatusRequestIndication.getInvokeId());
            mapDialogSms.sendReturnResultLastComponent(returnResultLast);
            mapDialogSms.close(false);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception when closing dialog at onReportSMDeliveryStatusRequest", e);
            }

            // Start a new dialog and send MAP ASC
            MAPDialogSms mapDialogSmsAlertServiceCentre = setAlertServiceCentre(reportSMDeliveryStatusRequestIndication);
            mapDialogSmsAlertServiceCentre.send();

        } catch (MAPException e) {
            logger.error("Error at onReportSMDeliveryStatusRequest when sending MAP ASC", e);
        }
    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest readyForSMRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onReadyForSMRequest for DialogId={}", readyForSMRequest
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            MAPDialogSms mapDialogSms = readyForSMRequest.getMAPDialog();
            mapDialogSms.setUserObject(readyForSMRequest.getInvokeId());
            mapDialogSms.addReadyForSMResponse(readyForSMRequest.getInvokeId(), null);
            mapDialogSms.close(false);

        } catch (MAPException e) {
            logger.error("Error while sending SendRoutingInfoForSMRequest ", e);
        }

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
    public void onReadyForSMResponse(ReadyForSMResponse readyForSMResponse) {

    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest noteSubscriberPresentRequest) {

    }

    private static LocationInfoWithLMSI setLocationInfoWithLMSI(SendRoutingInfoForSMRequest sriSMReqInd) {
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900130");
        ISDNAddressString trdNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900131");
        ISDNAddressString trd2Number = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900132");
        LMSI lmsi = null;
        boolean gprsNodeIndicator = false;
        AdditionalNumber additionalNumber = null;
        NetworkNodeDiameterAddress networkNodeDiameterAddress = null;
        NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress = null;
        AdditionalNumber thirdNumber = null;
        NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress = null;
        boolean imsNodeIndicator = false;
        ISDNAddressString smsf3gppNumber = null;
        NetworkNodeDiameterAddress smsf3gppDiameterAddress = null;
        ISDNAddressString smsfNon3gppNumber = null;
        NetworkNodeDiameterAddress smsfNon3gppDiameterAddress = null;
        boolean smsf3gppAddressIndicator = false;
        boolean smsfNon3gppAddressIndicator = false;

        Random rand = new Random();
        switch (rand.nextInt(5) + 1) {
            case 1:
                lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 233, (byte) 140});
                DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                DiameterIdentity mmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                DiameterIdentity addMmeName = new DiameterIdentityImpl("mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                DiameterIdentity addMmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                DiameterIdentity thirdMmeName = new DiameterIdentityImpl("mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                DiameterIdentity thirdMmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
                networkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm);
                additionalNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(addMmeName, addMmeRealm);
                thirdNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(thirdMmeName, thirdMmeRealm);
                if (sriSMReqInd.getIpSmGwGuidanceIndicator())
                    imsNodeIndicator = true;
                break;
            case 2:
                lmsi = new LMSIImpl(new byte[] {113, (byte) 255, (byte) 172, (byte) 206});
                if (sriSMReqInd.getGprsSupportIndicator()) {
                    gprsNodeIndicator = true;
                    additionalNumber = new AdditionalNumberImpl(sgsnNumber, null);
                    thirdNumber = new AdditionalNumberImpl(null, trd2Number);
                } else {
                    additionalNumber = new AdditionalNumberImpl(mscNumber, null);
                    thirdNumber = new AdditionalNumberImpl(trdNumber, null);
                }
                break;
            case 3:
                lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 235, 55});
                gprsNodeIndicator = true;
                break;
            case 4:
                lmsi = new LMSIImpl(new byte[] {114, 2, (byte) 231, (byte) 213});
                if (sriSMReqInd.getSmsfSupportIndicator()) {
                    smsfNon3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900600");
                    DiameterIdentity smsfNon3gppName = new DiameterIdentityImpl("smsf03.mnc002.mcc748".getBytes(StandardCharsets.UTF_8));
                    DiameterIdentity smsfNon3gppRealm = new DiameterIdentityImpl("mnc002.mcc748.telco.com".getBytes(StandardCharsets.UTF_8));
                    smsfNon3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfNon3gppName, smsfNon3gppRealm);
                    smsfNon3gppAddressIndicator = true;
                }
                break;
            case 5:
                if (sriSMReqInd.getSmsfSupportIndicator()) {
                    smsf3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900505");
                    DiameterIdentity smsfName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    DiameterIdentity smsfRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    smsf3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfName, smsfRealm);
                    smsf3gppAddressIndicator = true;
                }
                break;
        }

        return new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                networkNodeDiameterAddress, additionalNetworkNodeDiameterAddress, thirdNumber, thirdNetworkNodeDiameterAddress,
                imsNodeIndicator, smsf3gppNumber, smsf3gppDiameterAddress, smsfNon3gppNumber, smsfNon3gppDiameterAddress,
                smsf3gppAddressIndicator, smsfNon3gppAddressIndicator);
    }

    private MAPDialogSms setAlertServiceCentre(ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequestIndication) {
        MAPDialogSms mapDialogSmsAlertServiceCentre = null;
        try {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.error("Interrupted Exception when closing dialog at onReportSMDeliveryStatusRequest", e);
            }

            AddressString destinationAddressString = reportSMDeliveryStatusRequestIndication.getMAPDialog().getReceivedOrigReference();
            AddressString originAddressString = reportSMDeliveryStatusRequestIndication.getMAPDialog().getReceivedDestReference();
            SccpAddress clientSccpAddress = reportSMDeliveryStatusRequestIndication.getMAPDialog().getRemoteAddress();
            SccpAddress serverSccpAddress = reportSMDeliveryStatusRequestIndication.getMAPDialog().getLocalAddress();
            mapDialogSmsAlertServiceCentre = this.mapProvider.getMAPServiceSms().createNewDialog(MAPApplicationContext
                            .getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version2),
                    serverSccpAddress, originAddressString, clientSccpAddress, destinationAddressString);

            ISDNAddressString msisdn = reportSMDeliveryStatusRequestIndication.getMsisdn();
            AddressString serviceCentreAddress = reportSMDeliveryStatusRequestIndication.getServiceCentreAddress();
            IMSI imsi = null;
            CorrelationID correlationID = null;
            Time maximumUeAvailabilityTime = null;
            SmsGmscAlertEvent smsGmscAlertEvent = null;
            NetworkNodeDiameterAddress smsGmscDiameterAddress = null;
            ISDNAddressString newSGSNNumber = null;
            NetworkNodeDiameterAddress newSGSNDiameterAddress = null;
            ISDNAddressString newMMENumber = null;
            NetworkNodeDiameterAddress newMMEDiameterAddress = null;
            ISDNAddressString newMSCNumber = null;

            Random rand = new Random();
            switch (rand.nextInt(3) + 1) {
                case 1:
                    if (reportSMDeliveryStatusRequestIndication.getImsi() != null)
                        imsi = reportSMDeliveryStatusRequestIndication.getImsi();
                    else
                        imsi = new IMSIImpl("901405105680000");
                    String uriA = msisdn.getAddress() + "@restcomm.org";
                    SipUri sipUriA;
                    SipUri sipUriB;
                    sipUriA = new SipUriImpl(uriA.getBytes(StandardCharsets.UTF_8));
                    sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
                    correlationID = new CorrelationIDImpl(imsi, sipUriA, sipUriB);
                    AbsoluteTimeStamp ts = getAbsoluteTimeStamp();
                    maximumUeAvailabilityTime = new TimeImpl(ts.getYear(), ts.getMonth(), ts.getDay(), ts.getHour(), ts.getMinute(), ts.getSecond());
                    smsGmscAlertEvent = SmsGmscAlertEvent.msAvailableForMtSms;
                    break;
                case 2:
                    //Gmsc
                    smsGmscAlertEvent = SmsGmscAlertEvent.msUnderNewServingNode;
                    DiameterIdentity gmscName = new DiameterIdentityImpl("gmsc03.gmsc.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    DiameterIdentity gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
                    // new SGSN
                    newSGSNNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900131");
                    DiameterIdentity sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    DiameterIdentity sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    newSGSNDiameterAddress = new NetworkNodeDiameterAddressImpl(sgsnName, sgsnRealm);
                    // new MME
                    newMMENumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900132");
                    DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    DiameterIdentity mmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    newMMEDiameterAddress = new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm);
                    // new MSC
                    newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900102");
                    break;
                default:
                    break;
            }

            mapDialogSmsAlertServiceCentre.addAlertServiceCentreRequest(msisdn, serviceCentreAddress, imsi, correlationID,
                    maximumUeAvailabilityTime, smsGmscAlertEvent, smsGmscDiameterAddress, newSGSNNumber, newSGSNDiameterAddress,
                    newMMENumber, newMMEDiameterAddress, newMSCNumber);

        } catch (MAPException e) {
            logger.error("Error while sending SendRoutingInfoForSMRequest ", e);
        }
        return mapDialogSmsAlertServiceCentre;
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
}
