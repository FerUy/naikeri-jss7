package org.restcomm.protocols.ss7.map.load.mobility_management.gprs;

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
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UEUsageType;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetId;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdjacentAccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EDRXCycleLength;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtAccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IMSIGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentificationPriorityValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAOnlyAccessIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
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
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationQuintupletImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.QuintupletListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSizeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelayImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExternalClientImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LCSPrivacyClassImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAAttributesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSADataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LongGroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MOLRClassImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ServiceTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ZoneCodeImpl;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Server extends org.restcomm.protocols.ss7.map.load.mobility_management.gprs.TestHarnessMobilityManagement {

    private static Logger logger = Logger.getLogger(Server.class);

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
        this.sccpResource.addRemoteSsn(0, CLIENT_SPC, SGSN_SSN, 0, false);

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
            CLIENT_IP = args[1];
        }
        System.out.println("CLIENT_IP="+ CLIENT_IP);

        if (args.length >= 3) {
            CLIENT_PORT = Integer.parseInt(args[2]);
        }
        System.out.println("CLIENT_PORT="+ CLIENT_PORT);

        if (args.length >= 4) {
            SERVER_IP = args[3];
        }
        System.out.println("SERVER_IP="+ SERVER_IP);

        if (args.length >= 5) {
            SERVER_PORT = Integer.parseInt(args[4]);
        }
        System.out.println("SERVER_PORT="+ SERVER_PORT);

        if (args.length >= 6) {
            CLIENT_SPC = Integer.parseInt(args[5]);
        }
        System.out.println("CLIENT_SPC="+ CLIENT_SPC);

        if (args.length >= 7) {
            SERVER_SPC = Integer.parseInt(args[6]);
        }
        System.out.println("SERVER_SPC="+ SERVER_SPC);

        if (args.length >= 8) {
            NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        System.out.println("NETWORK_INDICATOR="+ NETWORK_INDICATOR);

        if (args.length >= 9) {
            SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        System.out.println("SERVICE_INDICATOR="+ SERVICE_INDICATOR);

        if (args.length >= 10) {
            SSN = Integer.parseInt(args[9]);
        }
        System.out.println("SSN="+ SSN);

        if (args.length >= 11) {
            ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        System.out.println("ROUTING_CONTEXT="+ ROUTING_CONTEXT);

        if(args.length >= 12){
            DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        System.out.println("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT="+ TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        final Server server = new Server();
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
            UEUsageType ueUsageType = null;

            mapDialogMobility.addSendAuthenticationInfoResponse(invokeId, authenticationSetList, mapExtensionContainer,
                    epsAuthenticationSetList, ueUsageType);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while answering SendAuthenticationInfoRequest ", e);
        }

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) {
        logger.error(String.format("ERROR: received SendAuthenticationInfoResponse at the server (acting as HLR) over DialogId=%d",
                sendAuthenticationInfoResponseIndication.getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationRequest for DialogId=%d", updateLocationRequestIndication
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse updateLocationResponseIndication) {
        logger.error(String.format("ERROR: received UpdateLocationResponse at the server (acting as HLR) over DialogId=%d",
                updateLocationResponseIndication.getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest insertSubscriberDataRequest) {
        logger.error(String.format("ERROR: received InsertSubscriberDataRequest at the server (acting as HLR) over DialogId=%d",
                insertSubscriberDataRequest.getMAPDialog().getLocalDialogId()));
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
            boolean addCapability = true;
            boolean sgsnMmeSeparationSupported = true;
            boolean mmeRegisteredForSMS = true;
            mapDialogMobility.addUpdateGprsLocationResponse(invokeId, hlrNumber, extensionContainer, addCapability,
                    sgsnMmeSeparationSupported, mmeRegisteredForSMS);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataResponse ", e);
        }
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest cancelLocationRequest) {
        logger.error(String.format("ERROR: received CancelLocationRequest at the server (acting as HLR) over DialogId=%d",
                cancelLocationRequest.getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse cancelLocationResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onCancelLocationResponse for DialogId=%d",
                    cancelLocationResponse.getMAPDialog().getLocalDialogId()));
        }
        // Start a new CL with subscription withdraw and reattach procedure (simulating an OSS request)
        new Thread(new GprsSubscriptionWithdrawReattach(this)).start();
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest updateGprsLocationRequestIndication) {
        try {
            // Create Dialog for MAP CL
            AddressString clDestinationRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
            AddressString clOriginRef = this.mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

            SccpAddress clClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SGSN_SSN, "491710400000");

            MAPDialogMobility cancelLocationDialog = this.mapProvider.getMAPServiceMobility().
                    createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version3),
                            clClientSccpAddress, clOriginRef, clServerSccpAddress, clDestinationRef);

            IMSI imsi = updateGprsLocationRequestIndication.getImsi();
            if (!imsi.getData().equals("901405105682021")) {
                IMSIWithLMSI imsiWithLmsi = null;
                CancellationType cancellationType = CancellationType.updateProcedure;
                MAPExtensionContainer extensionContainer = null;
                TypeOfUpdate typeOfUpdate = null;
                boolean mtrfSupportedAndAuthorized = false;
                boolean mtrfSupportedAndNotAuthorized = false;
                ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                ISDNAddressString newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                LMSI lmsi = null;
                boolean reattachRequired = false;

                cancelLocationDialog.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                        mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, lmsi, reattachRequired);
                cancelLocationDialog.send();
            }

            long isdInvokeId = updateGprsLocationRequestIndication.getInvokeId() + 1;
            MAPDialogMobility insertSubscriberDataDialog = updateGprsLocationRequestIndication.getMAPDialog();

            MAPExtensionContainer extCont = null;
            // msisdn
            ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
            // category
            Category category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
            // subscriberStatus
            SubscriberStatus subscriberStatus = SubscriberStatus.serviceGranted;
            // bearerServiceList
            ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<>();
            ExtBearerServiceCode extBearerServiceCode1 = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allDataCDAServices);
            ExtBearerServiceCode extBearerServiceCode2 = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allDataCDS_Services);
            bearerServiceList.add(extBearerServiceCode1);
            bearerServiceList.add(extBearerServiceCode2);
            // teleserviceList
            ArrayList<ExtTeleserviceCode> teleserviceList = getExtTeleserviceCodes();
            // provisionedSS
            ArrayList<ExtSSInfo> provisionedSS = new ArrayList<>();
            SSCode clip = new SSCodeImpl(SupplementaryCodeValue.clip);
            ExtSSStatus clipExtSSStatus = new ExtSSStatusImpl(true, true, false, true);
            SSSubscriptionOption clipSubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
            ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
            ExtSSData extSSDataClip = new ExtSSDataImpl(clip, clipExtSSStatus, clipSubscriptionOption, basicServiceGroupList, extCont);
            SSCode clir = new SSCodeImpl(SupplementaryCodeValue.clir);
            ExtSSStatus clirExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
            SSSubscriptionOption clirSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
            ExtSSData extSSDataClir = new ExtSSDataImpl(clir, clirExtSSStatus, clirSubscriptionOption, basicServiceGroupList, extCont);
            ExtSSInfo ssInfoClip = new ExtSSInfoImpl(extSSDataClip);
            ExtSSInfo ssInfoClir = new ExtSSInfoImpl(extSSDataClir);
            provisionedSS.add(ssInfoClip);
            provisionedSS.add(ssInfoClir);
            // odbData
            ODBData odbData = null;
            // roamingRestrictionDueToUnsupportedFeature
            boolean roamingRestrictionDueToUnsupportedFeature = true;
            // regionalSubscriptionData
            ArrayList<ZoneCode> regionalSubscriptionData = new ArrayList<>();
            ZoneCode zc1 = new ZoneCodeImpl(1);
            ZoneCode zc2 = new ZoneCodeImpl(2);
            ZoneCode zc3 = new ZoneCodeImpl(3);
            regionalSubscriptionData.add(zc1);
            regionalSubscriptionData.add(zc2);
            regionalSubscriptionData.add(zc3);
            // vbsSubscriptionData
            ArrayList<VoiceBroadcastData> vbsSubscriptionData = new ArrayList<>();
            GroupId gId = new GroupIdImpl("1");
            boolean broadcastInitEntitlement = true;
            LongGroupId lGId = new LongGroupIdImpl("5");
            VoiceBroadcastData voiceBroadcastData = new VoiceBroadcastDataImpl(gId, broadcastInitEntitlement, extCont, lGId);
            vbsSubscriptionData.add(voiceBroadcastData);
            // vgcsSubscriptionData
            ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
            // vlrCamelSubscriptionInfo
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
            // naeaPreferredCI
            NAEAPreferredCI naeaPreferredCI = null;
            // gprsSubscriptionData
            boolean completeDataListIncluded = true;
            ArrayList<PDPContext> gprsDataList = new ArrayList<>();
            int pdpContextId = 1;
            PDPType pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
            PDPAddress pdpAddress = new PDPAddressImpl(new byte[] { 21 });
            QoSSubscribed_ReliabilityClass reliabilityClass = QoSSubscribed_ReliabilityClass.reserved_7;
            QoSSubscribed_DelayClass delayClass = QoSSubscribed_DelayClass.delay_Class_4_bestEffort;
            QoSSubscribed_PrecedenceClass precedenceClass = QoSSubscribed_PrecedenceClass.normalPriority;
            QoSSubscribed_PeakThroughput peakThroughput = QoSSubscribed_PeakThroughput.upTo_4000_octetS;
            QoSSubscribed_MeanThroughput meanThroughput = QoSSubscribed_MeanThroughput._2000_octetH;
            QoSSubscribed qosSubscribed = new QoSSubscribedImpl(reliabilityClass, delayClass, precedenceClass, peakThroughput, meanThroughput);
            boolean vplmnAddressAllowed = true;
            APN apn = new APNImpl("internet");
            int allocationRetentionPriority = 9;
            ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes;
            ExtQoSSubscribed_DeliveryOrder deliveryOrder = ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo;
            ExtQoSSubscribed_TrafficClass trafficClass = ExtQoSSubscribed_TrafficClass.interactiveClass;
            int maximumSduSizeData = 151;
            boolean isSourceData = true;
            ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(maximumSduSizeData, isSourceData);
            int maximumBitRateForUL = 128;
            ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForUL, isSourceData);
            int maximumBitRateForDL = 576;
            ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForDL, isSourceData);
            ExtQoSSubscribed_ResidualBER residualBER = ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved;
            ExtQoSSubscribed_SduErrorRatio sduErrorRatio = ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved;
            ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3;
            int transferDelayValue = 1000;
            ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelayImpl(transferDelayValue, isSourceData);
            int gbrUL = 64;
            ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(gbrUL, isSourceData);
            int gbrDL = 256;
            ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(gbrDL, isSourceData);
            ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus,
                    deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink, maximumBitRateForDownlink, residualBER,
                    sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
            boolean isNormalCharging = true;
            boolean isPrepaidCharging = false;
            boolean isFlatRateCharging = false;
            boolean isChargingByHotBillingCharging = false;
            ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
            Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor = Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
            boolean optimisedForSignallingTraffic = true;
            int maxBRDLExt = 256000;
            ExtQoSSubscribed_BitRateExtended maxBitRateForDLExt = new ExtQoSSubscribed_BitRateExtendedImpl(maxBRDLExt, isSourceData);
            int gbrExtDL = 128000;
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDLExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrExtDL, isSourceData);
            Ext2QoSSubscribed ext2QoSSubscribed = new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic,
                    maxBitRateForDLExt, guaranteedBitRateForDLExtended);
            int mbrULExt = 256000;
            ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(mbrULExt, isSourceData);
            int gbrULExt = 128000;
            ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrULExt, isSourceData);
            Ext3QoSSubscribed ext3QoSSubscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
            Ext4QoSSubscribed ext4QoSSubscribed = new Ext4QoSSubscribedImpl(91);
            APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
            ExtPDPType extpdpType = new ExtPDPTypeImpl(new byte[] { 58, 59 });
            PDPAddress extpdpAddress = new PDPAddressImpl(new byte[] { 60 });
            SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
            LIPAPermission lipaPermission = LIPAPermission.lipaConditional;
            PDPContext pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                    extCont, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed, ext4QoSSubscribed,
                    apnOiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
            gprsDataList.add(pdpContext);
            GPRSSubscriptionData gprsSubscriptionData = new GPRSSubscriptionDataImpl(completeDataListIncluded,
                    gprsDataList, extCont, apnOiReplacement);

            // roamingRestrictedInSgsnDueToUnsupportedFeature
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;
            // networkAccessMode
            NetworkAccessMode networkAccessMode = NetworkAccessMode.onlyPacket;
            // lsaInformation
            LSAOnlyAccessIndicator lsaOnlyAccessIndicator = LSAOnlyAccessIndicator.accessOutsideLSAsAllowed;
            LSAIdentity lsaIdentity = new LSAIdentityImpl(new byte[] { 12, 10, 1 });
            ArrayList<LSAData> lsaDataList = new ArrayList<>();
            LSAIdentificationPriorityValue lsaIdentificationPriorityValue = LSAIdentificationPriorityValue.Priority_2;
            boolean preferentialAccessAvailable = true;
            boolean activeModeSupportAvailable = true;
            LSAAttributes lsaAttributes = new LSAAttributesImpl(lsaIdentificationPriorityValue, preferentialAccessAvailable,
                    activeModeSupportAvailable);
            boolean lsaActiveModeIndicator = true;
            LSAData lsaData = new LSADataImpl(lsaIdentity, lsaAttributes, lsaActiveModeIndicator, extCont);
            lsaDataList.add(lsaData);
            LSAInformation lsaInformation = new LSAInformationImpl(completeDataListIncluded, lsaOnlyAccessIndicator,
                    lsaDataList, extCont);
            // lmuIndicator
            boolean lmuIndicator = true;
            // lcsInformation (gmlcList)
            ArrayList<ISDNAddressString> gmlcList = new ArrayList<>();
            ISDNAddressString gmlcAddress =
                    new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917104600321");
            gmlcList.add(gmlcAddress);
            // lcsInformation (lcsPrivacyExceptionList)
            SSCode ssCode1 = new SSCodeImpl(SupplementaryCodeValue.allMOLR_SS);
            SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.autonomousSelfLocation);
            SSCode ssCode3 = new SSCodeImpl(SupplementaryCodeValue.allLCSPrivacyException);
            SSCode ssCode4 = new SSCodeImpl(SupplementaryCodeValue.allPLMN_specificSS);
            ExtSSStatus extSSStatus1 = new ExtSSStatusImpl(true, false, false, false);
            ExtSSStatus extSSStatus2 = new ExtSSStatusImpl(false, true, false, false);
            ExtSSStatus extSSStatus3 = new ExtSSStatusImpl(false, false, true, false);
            ExtSSStatus extSSStatus4 = new ExtSSStatusImpl(false, false, false, true);
            LCSClientInternalID lcsClientInternalID1 = LCSClientInternalID.broadcastService;
            LCSClientInternalID lcsClientInternalID2 = LCSClientInternalID.oandMHPLMN;
            LCSClientInternalID lcsClientInternalID3 = LCSClientInternalID.targetMSsubscribedService;
            LCSClientInternalID lcsClientInternalID4 = LCSClientInternalID.anonymousLocation;
            ArrayList<LCSClientInternalID> plmnClientList1 = new ArrayList<>();
            plmnClientList1.add(lcsClientInternalID1);
            plmnClientList1.add(lcsClientInternalID2);
            ArrayList<LCSClientInternalID> plmnClientList2 = new ArrayList<>();
            plmnClientList2.add(lcsClientInternalID2);
            ArrayList<LCSClientInternalID> plmnClientList3 = new ArrayList<>();
            plmnClientList3.add(lcsClientInternalID3);
            ArrayList<LCSClientInternalID> plmnClientList4 = new ArrayList<>();
            plmnClientList4.add(lcsClientInternalID4);
            ArrayList<ExternalClient> externalClientList1 = new ArrayList<>();
            ISDNAddressString externalAddress1 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "874927492");
            LCSClientExternalID clientIdentity1 = new LCSClientExternalIDImpl(externalAddress1, extCont);
            GMLCRestriction gmlcRestriction1 = GMLCRestriction.homeCountry;
            NotificationToMSUser notificationToMSUser1 = NotificationToMSUser.notifyLocationAllowed;
            ExternalClient externalClient1 = new ExternalClientImpl(clientIdentity1, gmlcRestriction1, notificationToMSUser1, extCont);
            externalClientList1.add(externalClient1);
            ArrayList<ExternalClient> externalClientList2 = new ArrayList<>();
            ISDNAddressString externalAddress2 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "398279222");
            LCSClientExternalID clientIdentity2 = new LCSClientExternalIDImpl(externalAddress2, extCont);
            GMLCRestriction gmlcRestriction2 = GMLCRestriction.gmlcList;
            NotificationToMSUser notificationToMSUser2 = NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse;
            ExternalClient externalClient2 = new ExternalClientImpl(clientIdentity2, gmlcRestriction2, notificationToMSUser2, extCont);
            externalClientList2.add(externalClient2);
            ArrayList<ExternalClient> externalClientList3 = new ArrayList<>();
            ISDNAddressString externalAddress3 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "23543252234");
            LCSClientExternalID clientIdentity3 = new LCSClientExternalIDImpl(externalAddress3, extCont);
            GMLCRestriction gmlcRestriction3 = GMLCRestriction.homeCountry;
            NotificationToMSUser notificationToMSUser3 = NotificationToMSUser.locationNotAllowed;
            ExternalClient externalClient3 = new ExternalClientImpl(clientIdentity3, gmlcRestriction3, notificationToMSUser3, extCont);
            externalClientList3.add(externalClient3);
            ArrayList<ExternalClient> externalClientList4 = new ArrayList<>();
            ISDNAddressString externalAddress4 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "598990298245");
            LCSClientExternalID clientIdentity4 = new LCSClientExternalIDImpl(externalAddress4, extCont);
            GMLCRestriction gmlcRestriction4 = GMLCRestriction.gmlcList;
            NotificationToMSUser notificationToMSUser4 = NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse;
            ExternalClient externalClient4 = new ExternalClientImpl(clientIdentity4, gmlcRestriction4, notificationToMSUser4, extCont);
            externalClientList4.add(externalClient4);
            ArrayList<ServiceType> serviceTypeList1 = new ArrayList<>();
            int serviceTypeIdentity1 = 1;
            ServiceType serviceType1 = new ServiceTypeImpl(serviceTypeIdentity1, gmlcRestriction1, notificationToMSUser1, extCont);
            serviceTypeList1.add(serviceType1);
            ArrayList<ServiceType> serviceTypeList2 = new ArrayList<>();
            int serviceTypeIdentity2 = 2;
            ServiceType serviceType2 = new ServiceTypeImpl(serviceTypeIdentity2, gmlcRestriction2, notificationToMSUser2, extCont);
            serviceTypeList2.add(serviceType2);
            ArrayList<ServiceType> serviceTypeList3 = new ArrayList<>();
            int serviceTypeIdentity3 = 3;
            ServiceType serviceType3 = new ServiceTypeImpl(serviceTypeIdentity3, gmlcRestriction3, notificationToMSUser3, extCont);
            serviceTypeList3.add(serviceType3);
            ArrayList<ServiceType> serviceTypeList4 = new ArrayList<>();
            int serviceTypeIdentity4 = 4;
            ServiceType serviceType4 = new ServiceTypeImpl(serviceTypeIdentity4, gmlcRestriction4, notificationToMSUser4, extCont);
            serviceTypeList4.add(serviceType4);
            LCSPrivacyClass lcsPrivacyClass1 = new LCSPrivacyClassImpl(ssCode1, extSSStatus1, notificationToMSUser1, externalClientList1,
                    plmnClientList1, extCont, externalClientList1, serviceTypeList1);
            LCSPrivacyClass lcsPrivacyClass2 = new LCSPrivacyClassImpl(ssCode2, extSSStatus2, notificationToMSUser2, externalClientList2,
                    plmnClientList2, extCont, externalClientList2, serviceTypeList2);
            LCSPrivacyClass lcsPrivacyClass3 = new LCSPrivacyClassImpl(ssCode3, extSSStatus3, notificationToMSUser3, externalClientList3,
                    plmnClientList3, extCont, externalClientList3, serviceTypeList3);
            LCSPrivacyClass lcsPrivacyClass4 = new LCSPrivacyClassImpl(ssCode4, extSSStatus4, notificationToMSUser4, externalClientList4,
                    plmnClientList4, extCont, externalClientList4, serviceTypeList4);
            ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList = new ArrayList<>();
            lcsPrivacyExceptionList.add(lcsPrivacyClass1);
            lcsPrivacyExceptionList.add(lcsPrivacyClass2);
            lcsPrivacyExceptionList.add(lcsPrivacyClass3);
            lcsPrivacyExceptionList.add(lcsPrivacyClass4);
            // lcsInformation (molrList)
            ArrayList<MOLRClass> molrList = new ArrayList<>();
            MOLRClass molrClass1 = new MOLRClassImpl(ssCode1, extSSStatus1, extCont);
            MOLRClass molrClass2 = new MOLRClassImpl(ssCode2, extSSStatus2, extCont);
            MOLRClass molrClass3 = new MOLRClassImpl(ssCode3, extSSStatus3, extCont);
            molrList.add(molrClass1);
            molrList.add(molrClass2);
            molrList.add(molrClass3);
            // lcsInformation (addLcsPrivacyExceptionList)
            // add-lcs-PrivacyExceptionList may be sent only if lcs-PrivacyExceptionList is
            // present and contains four instances of LCS-PrivacyClass. If the mentioned condition
            // is not satisfied the receiving node shall discard add-lcs-PrivacyExceptionList.
            // If an LCS-PrivacyClass is received both in lcs-PrivacyExceptionList and in
            // add-lcs-PrivacyExceptionList with the same SS-Code, then the error unexpected
            // data value shall be returned.
            SSCode addSsCode = new SSCodeImpl(SupplementaryCodeValue.plmn_specificSS_1);
            ExtSSStatus addExtSSStatus = new ExtSSStatusImpl(true, true, false, true);
            ArrayList<LCSClientInternalID> addPlmnClientList = new ArrayList<>();
            addPlmnClientList.add(lcsClientInternalID1);
            addPlmnClientList.add(lcsClientInternalID2);
            addPlmnClientList.add(lcsClientInternalID3);
            addPlmnClientList.add(lcsClientInternalID4);
            ArrayList<ExternalClient> addExternalClientList = new ArrayList<>();
            ISDNAddressString addExternalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "874927492");
            LCSClientExternalID addClientIdentity = new LCSClientExternalIDImpl(addExternalAddress, extCont);
            GMLCRestriction addGmlcRestriction = GMLCRestriction.homeCountry;
            NotificationToMSUser addMNtificationToMSUser = NotificationToMSUser.notifyLocationAllowed;
            ExternalClient addExternalClient = new ExternalClientImpl(addClientIdentity, addGmlcRestriction, addMNtificationToMSUser, extCont);
            addExternalClientList.add(addExternalClient);
            ArrayList<ServiceType> addServiceTypeList = new ArrayList<>();
            int addServiceTypeIdentity = 5;
            ServiceType addServiceType4 = new ServiceTypeImpl(addServiceTypeIdentity, addGmlcRestriction, addMNtificationToMSUser, extCont);
            addServiceTypeList.add(addServiceType4);
            LCSPrivacyClass addLcsPrivacyClass = new LCSPrivacyClassImpl(addSsCode, addExtSSStatus, addMNtificationToMSUser, addExternalClientList,
                    addPlmnClientList, extCont, addExternalClientList, addServiceTypeList);
            ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList = new ArrayList<>();
            addLcsPrivacyExceptionList.add(addLcsPrivacyClass);
            LCSInformation lcsInformation = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
            // istAlertTimer
            Integer istAlertTimer = 15;
            // superChargerSupportedInHLR
            AgeIndicator superChargerSupportedInHLR = new AgeIndicatorImpl(new byte[] { 4, 1, 48 });
            // mcSsInfo
            MCSSInfo mcSsInfo = null;
            // csAllocationRetentionPriority
            CSAllocationRetentionPriority csAllocationRetentionPriority = null;
            // sgsnCamelSubscriptionInfo
            GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attach;
            long sk = 12;
            ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
            DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.continueTransaction;
            GPRSCamelTDPData gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, sk, gsmSCFAddress, defaultSessionHandling, extCont);
            ArrayList<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<>();
            gprsCamelTDPDataList.add(gprsCamelTDPData);
            Integer camelCapabilityHandling = 3;
            boolean notificationToCSE = true;
            boolean csiActive = true;
            GPRSCSI gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            int serviceKey = 7;
            ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<>();
            SMSTriggerDetectionPoint smsTDP = SMSTriggerDetectionPoint.smsDeliveryRequest;
            DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;
            SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, extCont);
            smsCamelTdpDataList.add(smscameltdpData);
            SMSCSI moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<>();
            ArrayList<MTSMSTPDUType> mtsmstpduTypeArrayList = new ArrayList<>();
            MTSMSTPDUType mtsmstpduType1 = MTSMSTPDUType.smsDELIVER;
            MTSMSTPDUType mtsmstpduType2 = MTSMSTPDUType.smsSUBMITREPORT;
            MTSMSTPDUType mtsmstpduType3 = MTSMSTPDUType.smsSTATUSREPORT;
            mtsmstpduTypeArrayList.add(mtsmstpduType1);
            mtsmstpduTypeArrayList.add(mtsmstpduType2);
            mtsmstpduTypeArrayList.add(mtsmstpduType3);
            MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
            mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
            ArrayList<MMCode> mobilityTriggers = new ArrayList<>();
            MMCode mmCode = new MMCodeImpl(MMCodeValue.GPRSAttach);
            mobilityTriggers.add(mmCode);
            MGCSI mgCsi = new MGCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extCont, notificationToCSE, csiActive);
            SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi,
                    extCont, mtSmsCSI, mtSmsCamelTdpCriteriaList, mgCsi);
            // chargingCharacteristics already defined
            // accessRestrictionData
            boolean utranNotAllowed = false;
            boolean geranNotAllowed = false;
            boolean ganNotAllowed = true;
            boolean eUtranNotAllowed = false;
            boolean iHspaEvolutionNotAllowed = false;
            boolean hoToNon3GppAccessNotAllowed = true;
            AccessRestrictionData accessRestrictionData =
                    new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed, eUtranNotAllowed, hoToNon3GppAccessNotAllowed);
            // icsIndicator
            Boolean icsIndicator = Boolean.TRUE;
            // epsSubscriptionData
            EPSSubscriptionData epsSubscriptionData = null;
            // csgSubscriptionDataList
            ArrayList<CSGSubscriptionData> csgSubscriptionDataList = null;
            // ueReachabilityRequestIndicator
            boolean ueReachabilityRequestIndicator = true;
            // sgsnNumber
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    "491710490000");
            // mmeName
            byte[] mmeNameArray = "mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8);
            DiameterIdentity mmeName = new DiameterIdentityImpl(mmeNameArray);
            // subscribedPeriodicRAUTAUtimer
            Long subscribedPeriodicRAUTAUtimer = 360L;
            // vplmnLIPAAllowed
            boolean vplmnLIPAAllowed = true;
            // mdtUserConsent
            boolean mdtUserConsent = true;
            // subscribedPeriodicLAUtimer
            Long subscribedPeriodicLAUtimer = 300L;
            // vplmnCSGSubscriptionDataList
            ArrayList<CSGSubscriptionData> vplmnCSGSubscriptionDataList = null;
            // additionalMSISDN
            ISDNAddressString additionalMSISDN = null;
            // psAndSMSOnlyServiceProvision
            boolean psAndSMSOnlyServiceProvision = true;
            // smsInSGSNAllowed
            boolean smsInSGSNAllowed = true;
            // csToPsSRVCCAllowedIndicator
            boolean csToPsSRVCCAllowedIndicator = true;
            // pcscfRestorationRequest
            boolean pcscfRestorationRequest = true;
            // adjacentAccessRestrictionDataList
            ArrayList<AdjacentAccessRestrictionData> adjacentAccessRestrictionDataList = null;
            // imsiGroupIdList
            ArrayList<IMSIGroupId> imsiGroupIdList = null;
            // ueUsageType
            UEUsageType ueUsageType = null;
            // userPlaneIntegrityProtectionIndicator
            boolean userPlaneIntegrityProtectionIndicator = true;
            // dlBufferingSuggestedPacketCount
            Long dlBufferingSuggestedPacketCount = 3L;
            // resetIdList
            ArrayList<ResetId> resetIdList = null;
            // eDRXCycleLengthList
            ArrayList<EDRXCycleLength> eDRXCycleLengthList = null;
            // extAccessRestrictionData
            ExtAccessRestrictionData extAccessRestrictionData = null;
            // iabOperationAllowedIndicator
            boolean iabOperationAllowedIndicator = false;

            insertSubscriberDataDialog.addInsertSubscriberDataRequest(isdInvokeId,null, msisdn, category, subscriberStatus,
                    bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                    regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extCont,
                    naeaPreferredCI, gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode,
                    lsaInformation, lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority,
                    sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                    ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                    subscribedPeriodicLAUtimer, vplmnCSGSubscriptionDataList, additionalMSISDN, psAndSMSOnlyServiceProvision,
                    csToPsSRVCCAllowedIndicator, smsInSGSNAllowed, pcscfRestorationRequest, adjacentAccessRestrictionDataList, imsiGroupIdList,
                    ueUsageType, userPlaneIntegrityProtectionIndicator, dlBufferingSuggestedPacketCount, resetIdList,
                    eDRXCycleLengthList, extAccessRestrictionData, iabOperationAllowedIndicator);

            insertSubscriberDataDialog.send();

        } catch (Exception e) {
            logger.error("ERROR while processing onUpdateLocationRequest ", e);
        }
    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse updateGprsLocationResponse) {
        logger.error(String.format("ERROR: received UpdateGprsLocationResponse at the server (acting as HLR) over DialogId=%d", updateGprsLocationResponse
                .getMAPDialog().getLocalDialogId()));
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
        logger.error(String.format("onPurgeMSResponse received at the server (acting as HLR) over DialogId=%d", purgeMSResponse
                .getMAPDialog().getLocalDialogId()));
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

    private static class GprsSubscriptionWithdrawReattach implements Runnable {

        private final Server server4GprsSubscriptionWithdrawReattach;

        public GprsSubscriptionWithdrawReattach(Server server) {
            server4GprsSubscriptionWithdrawReattach = server;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                logger.debug("On Subscription Withdraw and Reattach command, about to send CL");
                server4GprsSubscriptionWithdrawReattach.sendCLOnSubWithdrawAndReattach("901405105682021");
            } catch (InterruptedException e) {
                logger.error("Error: " + e.getMessage());
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
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, SGSN_SSN, "491710400000");

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

    private static ArrayList<ExtTeleserviceCode> getExtTeleserviceCodes() {
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
        ExtTeleserviceCode dataTeleservices = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allDataTeleservices);
        ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
        teleserviceList.add(dataTeleservices);
        teleserviceList.add(shortMessageMT_PP);
        teleserviceList.add(shortMessageMO_PP);
        return teleserviceList;
    }
}
