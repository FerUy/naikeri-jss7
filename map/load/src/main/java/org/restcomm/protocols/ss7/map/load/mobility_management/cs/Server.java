package org.restcomm.protocols.ss7.map.load.mobility_management.cs;

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
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationQuintupletImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.QuintupletListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
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

import java.util.ArrayList;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Server extends TestHarnessMobilityManagement {

    private static Logger logger = Logger.getLogger(org.restcomm.protocols.ss7.map.load.mobility_management.cs.Server.class);

    // MAP
    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

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
        this.sccpResource.addRemoteSsn(0, CLIENT_SPC, VLR_SSN, 0, false);

        this.router.addMtp3ServiceAccessPoint(1, 1, SERVER_SPC, NETWORK_INDICATOR, 0, null);
        this.router.addMtp3Destination(1, 1, CLIENT_SPC, CLIENT_SPC, 0, 255, 255);

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
        this.tcapStack = new TCAPStackImpl("TestServer", this.sccpStack.getSccpProvider(), HLR_SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        this.mapStack = new MAPStackImpl("TestServer", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();

        this.mapProvider.addMAPDialogListener(this);
        this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceMobility().activate();

        this.mapStack.start();
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
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
        }
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                        AddressString imsi, AddressString vlr) {
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
            logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(),
                    extensionContainer));
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

        this.endCount++;

        if ((this.endCount % 10000) == 0) {
            long currentTime = System.currentTimeMillis();
            long processingTime = currentTime - start;
            start = currentTime;
            logger.warn("Completed 10000 Dialogs in " + processingTime + " milliseconds");
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

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].toLowerCase().equals("tcp")) {
            ipChannelType = IpChannelType.TCP;
        } else {
            ipChannelType = IpChannelType.SCTP;
        }
        System.out.println("IpChannelType="+ipChannelType);

        if (args.length >= 2) {
            TestHarnessMobilityManagement.CLIENT_IP = args[1];
        }
        System.out.println("CLIENT_IP="+ TestHarnessMobilityManagement.CLIENT_IP);

        if (args.length >= 3) {
            TestHarnessMobilityManagement.CLIENT_PORT = Integer.parseInt(args[2]);
        }
        System.out.println("CLIENT_PORT="+ TestHarnessMobilityManagement.CLIENT_PORT);

        if (args.length >= 4) {
            TestHarnessMobilityManagement.SERVER_IP = args[3];
        }
        System.out.println("SERVER_IP="+ TestHarnessMobilityManagement.SERVER_IP);

        if (args.length >= 5) {
            TestHarnessMobilityManagement.SERVER_PORT = Integer.parseInt(args[4]);
        }
        System.out.println("SERVER_PORT="+ TestHarnessMobilityManagement.SERVER_PORT);

        if (args.length >= 6) {
            TestHarnessMobilityManagement.CLIENT_SPC = Integer.parseInt(args[5]);
        }
        System.out.println("CLIENT_SPC="+ TestHarnessMobilityManagement.CLIENT_SPC);

        if (args.length >= 7) {
            TestHarnessMobilityManagement.SERVER_SPC = Integer.parseInt(args[6]);
        }
        System.out.println("SERVER_SPC="+ TestHarnessMobilityManagement.SERVER_SPC);

        if (args.length >= 8) {
            TestHarnessMobilityManagement.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        System.out.println("NETWORK_INDICATOR="+ TestHarnessMobilityManagement.NETWORK_INDICATOR);

        if (args.length >= 9) {
            TestHarnessMobilityManagement.SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        System.out.println("SERVICE_INDICATOR="+ TestHarnessMobilityManagement.SERVICE_INDICATOR);

        if (args.length >= 10) {
            TestHarnessMobilityManagement.SSN = Integer.parseInt(args[9]);
        }
        System.out.println("SSN="+ TestHarnessMobilityManagement.SSN);

        if (args.length >= 11) {
            TestHarnessMobilityManagement.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        System.out.println("ROUTING_CONTEXT="+ TestHarnessMobilityManagement.ROUTING_CONTEXT);

        if(args.length >= 12){
            TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        System.out.println("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT="+ TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        final org.restcomm.protocols.ss7.map.load.mobility_management.cs.Server server = new org.restcomm.protocols.ss7.map.load.mobility_management.cs.Server();
        try {
            server.initializeStack(ipChannelType);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onSendAuthenticationInfoRequest for DialogId=%d", sendAuthenticationInfoRequestIndication
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = sendAuthenticationInfoRequestIndication.getInvokeId();
            MAPDialogMobility mapDialogMobility = sendAuthenticationInfoRequestIndication.getMAPDialog();

            IMSI imsi = sendAuthenticationInfoRequestIndication.getImsi();
            byte[] rand;
            if (imsi.getData().equals("901405105682021"))
                rand = new byte[] {(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                        (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x75};
            else
                rand = new byte[] {(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                    (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x74};
            byte[] xres = new byte[] {(byte) 0xe4, (byte) 0xb9, (byte) 0xca, 0x0c, 0x2b, 0x12, 0x37, (byte) 0xb6};
            byte[] ck = new byte[] {(byte) 0x81, (byte) 0xe8, 0x64, (byte) 0xf0, (byte) 0xc5, 0x0a, 0x53, 0x64, (byte) 0xda,
                    (byte) 0xed, 0x49, 0x76, 0x03, (byte) 0xc9, (byte) 0xbf, 0x5d};
            byte[] ik = new byte[] {(byte) 0x92, 0x59, 0x62, 0x13, (byte) 0xf2, 0x43, 0x75, 0x69, (byte) 0x96, (byte) 0x9d, 0x26,
                    0x0d, (byte) 0xac, 0x60, (byte) 0xbf, 0x6b};
            byte[] autn = new byte[] {0x5a, (byte) 0xcd, 0x63, 0x56, (byte) 0x83, (byte) 0xfe, (byte) 0x80, 0x00, (byte) 0xba,
                    (byte) 0x95, (byte) 0xba, (byte) 0xae, 0x08, 0x0a, 0x30, 0x73};
            AuthenticationQuintuplet authenticationQuintuplet = new AuthenticationQuintupletImpl(rand, xres, ck, ik, autn);
            ArrayList<AuthenticationQuintuplet> authenticationQuintupletList = new ArrayList<>();
            authenticationQuintupletList.add(authenticationQuintuplet);
            QuintupletList quintupletList = new QuintupletListImpl(authenticationQuintupletList);
            AuthenticationSetList authenticationSetList = new AuthenticationSetListImpl(quintupletList);
            MAPExtensionContainer mapExtensionContainer = null;
            EpsAuthenticationSetList epsAuthenticationSetList = null;

            mapDialogMobility.addSendAuthenticationInfoResponse(invokeId, authenticationSetList, mapExtensionContainer,
                    epsAuthenticationSetList);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while answering SendAuthenticationInfoRequest ", e);
        }

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) {
        logger.error(String.format("ERROR: received SendAuthenticationInfoResponse over DialogId=%d", sendAuthenticationInfoResponseIndication
                .getMAPDialog().getLocalDialogId(), " over the server (acting as HLR)"));
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationRequest for DialogId=%d", updateLocationRequestIndication
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            // Create Dialog for MAP CL
            AddressString clDestinationRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
            AddressString clOriginRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

            SccpAddress clClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");

            MAPDialogMobility cancelLocationDialog = this.mapProvider.getMAPServiceMobility().
                    createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version3),
                            clClientSccpAddress, clOriginRef, clServerSccpAddress, clDestinationRef);

            IMSI imsi = updateLocationRequestIndication.getImsi();
            if (!imsi.getData().equals("901405105682021")) {
                IMSIWithLMSI imsiWithLmsi = null;
                CancellationType cancellationType = CancellationType.updateProcedure;
                MAPExtensionContainer extensionContainer = null;
                TypeOfUpdate typeOfUpdate = null;
                boolean mtrfSupportedAndAuthorized = false;
                boolean mtrfSupportedAndNotAuthorized = false;
                ISDNAddressString newMSCNumber = null; // new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                ISDNAddressString newVLRNumber = null; // new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                LMSI lmsi = null;
                boolean reattachRequired = false;

                cancelLocationDialog.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                        mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, lmsi, reattachRequired);
                cancelLocationDialog.send();
            }

            long isdInvokeId = updateLocationRequestIndication.getInvokeId() + 1;
            MAPDialogMobility insertSubscriberDataDialog = updateLocationRequestIndication.getMAPDialog();

            ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
            Category category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
            SubscriberStatus subscriberStatus = SubscriberStatus.serviceGranted;
            ArrayList<ExtBearerServiceCode> bearerServiceList = null;
            ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
            ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
            ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
            teleserviceList.add(shortMessageMT_PP);
            teleserviceList.add(shortMessageMO_PP);
            ArrayList<ExtSSInfo> provisionedSS = new ArrayList<>();
            SSCode clip = new SSCodeImpl(SupplementaryCodeValue.clip);
            ExtSSStatus clipExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
            SSSubscriptionOption clipSubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
            ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
            MAPExtensionContainer mapExtensionContainer = null;
            ExtSSData extSSDataClip = new ExtSSDataImpl(clip, clipExtSSStatus, clipSubscriptionOption, basicServiceGroupList, mapExtensionContainer);
            SSCode clir = new SSCodeImpl(SupplementaryCodeValue.clir);
            ExtSSStatus clirExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
            SSSubscriptionOption clirSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
            ExtSSData extSSDataClir = new ExtSSDataImpl(clir, clirExtSSStatus, clirSubscriptionOption, basicServiceGroupList, mapExtensionContainer);
            ExtSSInfo ssInfoClip = new ExtSSInfoImpl(extSSDataClip);
            ExtSSInfo ssInfoClir = new ExtSSInfoImpl(extSSDataClir);
            provisionedSS.add(ssInfoClip);
            provisionedSS.add(ssInfoClir);
            ODBData odbData = null;
            boolean roamingRestrictionDueToUnsupportedFeature = false;
            ArrayList<ZoneCode> regionalSubscriptionData = null;
            ArrayList<VoiceBroadcastData> vbsSubscriptionData = null;
            ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
            NAEAPreferredCI naeaPreferredCI = null;
            GPRSSubscriptionData gprsSubscriptionData = null;
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature = false;
            NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;
            LSAInformation lsaInformation = null;
            boolean lmuIndicator = false;
            LCSInformation lcsInformation = null;
            Integer istAlertTimer = null;
            AgeIndicator superChargerSupportedInHLR = null;
            MCSSInfo mcSsInfo = null;
            CSAllocationRetentionPriority csAllocationRetentionPriority = null;
            SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
            ChargingCharacteristics chargingCharacteristics = null;
            AccessRestrictionData accessRestrictionData = null;
            Boolean icsIndicator = null;
            EPSSubscriptionData epsSubscriptionData = null;
            ArrayList<CSGSubscriptionData> csgSubscriptionDataList = null;
            boolean ueReachabilityRequestIndicator = false;
            ISDNAddressString sgsnNumber = null;
            DiameterIdentity mmeName = null;
            Long subscribedPeriodicRAUTAUtimer = null;
            boolean vplmnLIPAAllowed = false;
            boolean mdtUserConsent = false;
            Long subscribedPeriodicLAUtimer = null;

            insertSubscriberDataDialog.addInsertSubscriberDataRequest(isdInvokeId,null, msisdn, category, subscriberStatus,
                    bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                    regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, mapExtensionContainer,
                    naeaPreferredCI, gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode,
                    lsaInformation, lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority,
                    sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                    ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                    subscribedPeriodicLAUtimer);

            insertSubscriberDataDialog.send();

        } catch (Exception e) {
            logger.error("ERROR while processing onUpdateLocationRequest ", e);
        }
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse updateLocationResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationResponse for DialogId=%d", updateLocationResponseIndication
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest insertSubscriberDataRequest) {
        logger.error(String.format("ERROR: received InsertSubscriberDataRequest over DialogId=%d", insertSubscriberDataRequest
                .getMAPDialog().getLocalDialogId(), " over the server (acting as HLR)"));
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse insertSubscriberDataResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onInsertSubscriberDataResponse for DialogId=%d", insertSubscriberDataResponse
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = insertSubscriberDataResponse.getInvokeId();
            MAPDialogMobility mapDialogMobility = insertSubscriberDataResponse.getMAPDialog();
            ISDNAddressString hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285000008002");
            MAPExtensionContainer extensionContainer = null;
            boolean addCapability = false;
            boolean pagingAreaCapability = false;
            mapDialogMobility.addUpdateLocationResponse(invokeId, hlrNumber, extensionContainer, addCapability, pagingAreaCapability);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataResponse ", e);
        }
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest cancelLocationRequest) {
        logger.error(String.format("ERROR: received CancelLocationRequest over DialogId=%d", cancelLocationRequest
                .getMAPDialog().getLocalDialogId(), " over the server (acting as HLR)"));
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse cancelLocationResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onCancelLocationResponse for DialogId=%d", cancelLocationResponse
                    .getMAPDialog().getLocalDialogId()));
        }
        // Start a new CL with subscription withdraw and reattach procedure (simulating an OSS request)
        new Thread(new SubscriptionWithdrawReattach(this)).start();
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest updateGprsLocationRequest) {

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse updateGprsLocationResponse) {

    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest purgeMSRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onPurgeMSRequest for DialogId=%d", purgeMSRequest
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = purgeMSRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = purgeMSRequest.getMAPDialog();
            int ssn = purgeMSRequest.getMAPDialog().getRemoteAddress().getSubsystemNumber();
            boolean freezeTMSI = false;
            boolean freezePTMSI = false;
            if (ssn == 7)
                freezeTMSI = true;
            else if (ssn == 149)
                freezePTMSI = true;
            MAPExtensionContainer extensionContainer = null;
            boolean freezeMTMSI = false;
            mapDialogMobility.addPurgeMSResponse(invokeId, freezeTMSI, freezePTMSI, extensionContainer, freezeMTMSI);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataResponse ", e);
        }
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse purgeMSResponse) {
        logger.error(String.format("onPurgeMSResponse over DialogId=%d", purgeMSResponse
                .getMAPDialog().getLocalDialogId(), " over the server (acting as HLR)"));
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

    private SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = HLR_SSN;
        }
        return fact.createSccpAddress(ri, gt, dpc, ssn);
    }

    private class SubscriptionWithdrawReattach implements Runnable {

        private Server server4SubscriptionWithdrawReattach;

        public SubscriptionWithdrawReattach(Server server) {
            this.server4SubscriptionWithdrawReattach = server;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                logger.debug("On Subscription Withdraw and Reattach command, about to send CL");
                server4SubscriptionWithdrawReattach.sendCLOnSubWithdrawAndReattach("901405105682021");
            } catch (InterruptedException e) {
                logger.error("Error: ", e);
                e.printStackTrace();
            }
        }
    }

    private void sendCLOnSubWithdrawAndReattach(String imsiDigits) {
        try {
            AddressString clDestinationRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
            AddressString clOriginRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

            SccpAddress clClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");

            MAPDialogMobility cancelLocationDialog;
            cancelLocationDialog = this.mapProvider.getMAPServiceMobility().createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version3),
                    clClientSccpAddress, clOriginRef, clServerSccpAddress, clDestinationRef);

            IMSI imsi = new IMSIImpl(imsiDigits);
            IMSIWithLMSI imsiWithLmsi = null;
            CancellationType cancellationType = CancellationType.subscriptionWithdraw;
            MAPExtensionContainer extensionContainer = null;
            TypeOfUpdate typeOfUpdate = null;
            boolean mtrfSupportedAndAuthorized = false;
            boolean mtrfSupportedAndNotAuthorized = false;
            ISDNAddressString newMSCNumber = null;
            ISDNAddressString newVLRNumber = null;
            LMSI lmsi = null;
            boolean reattachRequired = true;

            cancelLocationDialog.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                    mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, lmsi, reattachRequired);
            cancelLocationDialog.send();

        } catch (MAPException e) {
            throw new RuntimeException(e);
        }
    }
}
