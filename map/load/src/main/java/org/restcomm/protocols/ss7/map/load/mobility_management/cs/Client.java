package org.restcomm.protocols.ss7.map.load.mobility_management.cs;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.asn.BitSetStrictLength;
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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ExtSupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DaylightSavingTime;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.IMSVoiceOverPsSessionsIndication;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation5GS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TimeZone;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCFInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForIPSMGWData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedServingNode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSG;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCWInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIPInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIRInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCHInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForECTInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.load.CsvWriter;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ExtSupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SuperChargerInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedRATTypesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformation5GSImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.NRCellGlobalIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.NRTAIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PSSubscriberStateImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RouteingNumberImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TimeZoneImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.UserCSGInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCBInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCFInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCHInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCLIPInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCLIRInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCSGImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCWInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForECTInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForIPSMGWDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RequestedServingNodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.FQDNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;
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
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
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
import java.util.Arrays;
import java.util.Random;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Client extends TestHarnessMobilityManagementCs {

    private static final Logger logger = LogManager.getLogger(Client.class);

    // TCAP
    private static TCAPStack tcapStack;

    // MAP
    private MAPStackImpl mapStack;
    private static MAPProvider mapProvider;

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

    static Long imsiForPurge = 901405105680000L;
    static Long imsiForCheckImei_Huawei = 901405105680000L;
    static Long imsiForATM = 901405105680000L;

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
        logger.debug(asp);

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
        sccpResource.addRemoteSsn(0, SERVER_SPC, HLR_SSN, 0, false);

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
        tcapStack = new TCAPStackImpl("Test", this.sccpStack.getSccpProvider(), VLR_SSN);
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
        mapProvider.getMAPServiceMobility().addMAPServiceListener(this);
        mapProvider.getMAPServiceMobility().activate();
        this.mapStack.start();
    }

    private void initiateMobility() throws MAPException {
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
                logger.error("InterruptedException: {}", e.getMessage());
            }
        }

        this.rateLimiterObj.acquire();

        // Send Authentication Info
        sendAuthenticationInfoRequest("901405105682583");
    }

    private static SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = VLR_SSN;
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
            TestHarnessMobilityManagementCs.CLIENT_IP = args[3];
        }

        logger.info("CLIENT_IP={}", TestHarnessMobilityManagementCs.CLIENT_IP);

        if (args.length >= 5) {
            TestHarnessMobilityManagementCs.CLIENT_PORT = Integer.parseInt(args[4]);
        }

        logger.info("CLIENT_PORT={}", TestHarnessMobilityManagementCs.CLIENT_PORT);

        if (args.length >= 6) {
            TestHarnessMobilityManagementCs.SERVER_IP = args[5];
        }

        logger.info("SERVER_IP={}", TestHarnessMobilityManagementCs.SERVER_IP);

        if (args.length >= 7) {
            TestHarnessMobilityManagementCs.SERVER_PORT = Integer.parseInt(args[6]);
        }

        logger.info("SERVER_PORT={}", TestHarnessMobilityManagementCs.SERVER_PORT);

        if (args.length >= 8) {
            TestHarnessMobilityManagementCs.CLIENT_SPC = Integer.parseInt(args[7]);
        }

        logger.info("CLIENT_SPC={}", TestHarnessMobilityManagementCs.CLIENT_SPC);

        if (args.length >= 9) {
            TestHarnessMobilityManagementCs.SERVER_SPC = Integer.parseInt(args[8]);
        }

        logger.info("SERVER_SPC={}", TestHarnessMobilityManagementCs.SERVER_SPC);

        if (args.length >= 10) {
            TestHarnessMobilityManagementCs.NETWORK_INDICATOR = Integer.parseInt(args[9]);
        }

        logger.info("NETWORK_INDICATOR={}", TestHarnessMobilityManagementCs.NETWORK_INDICATOR);

        if (args.length >= 11) {
            TestHarnessMobilityManagementCs.SERVICE_INDICATOR = Integer.parseInt(args[10]);
        }

        logger.info("SERVICE_INDICATOR={}", TestHarnessMobilityManagementCs.SERVICE_INDICATOR);

        if (args.length >= 12) {
            TestHarnessMobilityManagementCs.SSN = Integer.parseInt(args[11]);
        }

        logger.info("SSN={}", TestHarnessMobilityManagementCs.SSN);

        if (args.length >= 13) {
            TestHarnessMobilityManagementCs.ROUTING_CONTEXT = Integer.parseInt(args[12]);
        }

        logger.info("ROUTING_CONTEXT={}", TestHarnessMobilityManagementCs.ROUTING_CONTEXT);

        if (args.length >= 14) {
            TestHarnessMobilityManagementCs.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[13]);
        }

        logger.info("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT={}", TestHarnessMobilityManagementCs.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        if (args.length >= 15) {
            TestHarnessMobilityManagementCs.RAMP_UP_PERIOD = Integer.parseInt(args[14]);
        }

        logger.info("RAMP_UP_PERIOD={}", TestHarnessMobilityManagementCs.RAMP_UP_PERIOD);

        if (args.length >= 16) {
            TestHarnessMobilityManagementCs.SCCP_CLIENT_ADDRESS = args[15];
        }

        logger.info("SCCP_CLIENT_ADDRESS={}", TestHarnessMobilityManagementCs.SCCP_CLIENT_ADDRESS);

        if (args.length >= 17) {
            TestHarnessMobilityManagementCs.SCCP_SERVER_ADDRESS = args[16];
        }

        logger.info("SCCP_SERVER_ADDRESS={}", TestHarnessMobilityManagementCs.SCCP_SERVER_ADDRESS);

        if (args.length >= 18) {
            TestHarnessMobilityManagementCs.ROUTING_INDICATOR = RoutingIndicator.valueOf(Integer.parseInt(args[17]));
        }

        logger.info("ROUTING_INDICATOR={}", TestHarnessMobilityManagementCs.ROUTING_INDICATOR);

        if (args.length >= 19) {
            TestHarnessMobilityManagementCs.SENDING_MESSAGE_THREAD_COUNT = Integer.parseInt(args[18]);
        }

        logger.info("SENDING_MESSAGE_THREAD_COUNT={}", TestHarnessMobilityManagementCs.SENDING_MESSAGE_THREAD_COUNT);

        // logger.info("Number of calls to be completed = " + noOfCalls + " Number of concurrent calls to be maintained = " + noOfConcurrentCalls);

        NDIALOGS = noOfCalls;

        logger.info("NDIALOGS={}", NDIALOGS);

        MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

        logger.info("MAXCONCURRENTDIALOGS={}", MAXCONCURRENTDIALOGS);

        final Client client = new Client();
        client.endCount = TestHarnessMobilityManagementCs.RAMP_UP_PERIOD;

        try {
            client.initializeStack(ipChannelType);

            Thread.sleep(TestHarnessMobilityManagementCs.TEST_START_DELAY);

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
            logger.error("Exception: {}", e.getMessage());
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
        logger.error("Received SendAuthenticationInfoRequest over DialogId={}", sendAuthenticationInfoRequestIndication
                .getMAPDialog().getLocalDialogId());
    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onSendAuthenticationInfoResponse for DialogId={}", sendAuthenticationInfoResponseIndication
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            // Create Dialog
            AddressString originAddressString = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
            AddressString destAddressString = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

            MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
            MAPApplicationContextName mapAcn = MAPApplicationContextName.networkLocUpContext;
            MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
            MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                    originAddressString, serverSccpAddress, destAddressString);

            IMSI imsi;
            byte[] rand = sendAuthenticationInfoResponseIndication.getAuthenticationSetList().getQuintupletList().getAuthenticationQuintuplets().get(0).getRand();
            if (Arrays.equals(rand, new byte[]{(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                    (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x75})) {
                imsi = new IMSIImpl("901405105682021");
            } else {
                imsi = new IMSIImpl("901405105682583");
            }

            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
            ISDNAddressString roamingNumber = null;
            ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
            LMSI lmsi = null;
            MAPExtensionContainer mapExtensionContainer = null;
            VLRCapability vlrCapability = getVlrCapability();
            boolean informPreviousNetworkEntity = false;
            boolean csLCSNotSupportedByUE = false;
            GSNAddress vGmlcAddress = null;
            ADDInfo addInfo = null;
            PagingArea pagingArea = null;
            boolean skipSubscriberDataUpdate = false;
            boolean restorationIndicator = false;
            ArrayList<PlmnId> ePLMNList = new ArrayList<>();
            PlmnId plmnId1 = new PlmnIdImpl(262,1);
            PlmnId plmnId2 = new PlmnIdImpl(262,999);
            ePLMNList.add(plmnId1);
            ePLMNList.add(plmnId2);
            NetworkNodeDiameterAddress mmeDiameterAddress = getNetworkNodeDiameterAddress();

            mapDialogMobility.addUpdateLocationRequest(imsi, mscNumber, roamingNumber, vlrNumber, lmsi, mapExtensionContainer,
                    vlrCapability, informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                    skipSubscriberDataUpdate, restorationIndicator, ePLMNList, mmeDiameterAddress);

            mapDialogMobility.send();

            this.csvWriter.incrementCounter(CREATED_DIALOGS);

        }  catch (MAPException e) {
            logger.error("Error while processing SAI response and sending MAP UL ", e);
        }
    }

    private static NetworkNodeDiameterAddress getNetworkNodeDiameterAddress() {
        byte[] mmeNameBytes = {0x6d, 0x6d, 0x65, 0x2e, 0x32, 0x30, 0x2e, 0x6d, 0x61, 0x67, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67};
        byte[] mmeRealmBytes = {0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67};
        DiameterIdentity mmeName = new DiameterIdentityImpl(mmeNameBytes);
        DiameterIdentity mmeRealm = new DiameterIdentityImpl(mmeRealmBytes);
        return new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm);
    }

    private static VLRCapability getVlrCapability() {
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, false, false);
        boolean solsaSupportIndicator = true;
        ISTSupportIndicator istSupportIndicator = ISTSupportIndicator.istCommandSupported;
        SuperChargerInfo superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl(true);
        boolean longFtnSupported = true;
        SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true, true, false);
        boolean oCsi = false;
        boolean dCsi = false;
        boolean vtCsi = false;
        boolean tCsi = false;
        boolean mtSMSCsi = true;
        boolean mgCsi = true;
        boolean psiEnhancements = true;
        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(oCsi,dCsi,vtCsi,tCsi, mtSMSCsi, mgCsi, psiEnhancements);
        boolean utran = true;
        boolean geran = true;
        boolean gan = false;
        boolean i_hspa_evolution = true;
        boolean e_utran = true;
        boolean nb_iot = true;
        SupportedRATTypes supportedRATTypesIndicator = new SupportedRATTypesImpl(utran, geran, gan, i_hspa_evolution, e_utran, nb_iot);
        boolean longGroupIDSupported = true;
        boolean mtRoamingForwardingSupported = true;
        return new VLRCapabilityImpl(supportedCamelPhases, null, solsaSupportIndicator,
                istSupportIndicator, superChargerSupportedInServingNetworkEntity, longFtnSupported, supportedLCSCapabilitySets,
                offeredCamel4CSIs, supportedRATTypesIndicator, longGroupIDSupported, mtRoamingForwardingSupported);
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest insertSubscriberDataRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onInsertSubscriberDataRequest for DialogId={}", insertSubscriberDataRequest
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = insertSubscriberDataRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = insertSubscriberDataRequest.getMAPDialog();
            ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<>();
            BearerServiceCodeValue bearerServiceCodeValue = getBearerServiceCodeValue();
            ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
            bearerServiceList.add(extBearerServiceCode);
            ArrayList<SSCode> ssList = new ArrayList<>();
            SupplementaryCodeValue supplementaryCodeValue = getSupplementaryCodeValue();
            SSCode ssCode = new SSCodeImpl(supplementaryCodeValue);
            ssList.add(ssCode);
            ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
            ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
            ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
            teleserviceList.add(shortMessageMT_PP);
            teleserviceList.add(shortMessageMO_PP);
            ODBGeneralData odbGeneralData = getOdbGeneralData();
            RegionalSubscriptionResponse regionalSubscriptionResponse = RegionalSubscriptionResponse.networkNodeAreaRestricted;
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
            SupportedFeatures supportedFeatures = getSupportedFeatures();
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

    private static ODBGeneralData getOdbGeneralData() {
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
        return new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                internationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred,
                ssAccessBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, allECTBarred, chargeableECTBarred,
                internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred,
                roamingOutsidePLMNCountryBarred, registrationAllCFBarred, registrationCFNotToHPLMNBarred,
                registrationInterzonalCFBarred, registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
    }

    private static SupportedFeatures getSupportedFeatures() {
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
        return new SupportedFeaturesImpl(odbAllApn, odbHPLMNApn, odbVPLMNApn, odbAllOg, odbAllInternationalOg,
                odbAllIntOgNotToHPLMNCountry, odbAllInterzonalOg, odbAllInterzonalOgNotToHPLMNCountry,
                odbAllInterzonalOgandInternatOgNotToHPLMNCountry, regSub, trace, lcsAllPrivExcep, lcsUniversal,
                lcsCallSessionRelated, lcsCallSessionUnrelated, lcsPLMNOperator, lcsServiceType, lcsAllMOLRSS,
                lcsBasicSelfLocation, lcsAutonomousSelfLocation, lcsTransferToThirdParty, smMoPp, barringOutgoingCalls, baoc,
                boic, boicExHC, localTimeZoneRetrieval, additionalMsisdn, smsInMME, smsInSGSN, ueReachabilityNotification,
                stateLocationInformationRetrieval, partialPurge, gddInSGSN, sgsnCAMELCapability,
                pcscfRestoration, dedicatedCoreNetworks, nonIPPDNTypeAPNs, nonIPPDPTypeAPNs,
                nrAsSecondaryRAT);
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse insertSubscriberDataResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug("onInsertSubscriberDataResponse over DialogId={}", insertSubscriberDataResponse
                    .getMAPDialog().getLocalDialogId());
        }
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequestIndication) {
        logger.error("ERROR: received UpdateLocationRequest at the client (acting as VLR) over DialogId={}", updateLocationRequestIndication
                .getMAPDialog().getLocalDialogId());
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse updateLocationResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug("onUpdateLocationResponse over DialogId={}", updateLocationResponseIndication
                    .getMAPDialog().getLocalDialogId());
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
            logger.debug("onCancelLocationRequest for DialogId={}", cancelLocationRequest
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = cancelLocationRequest.getInvokeId();
            MAPDialogMobility cancelLocationRequestDialog = cancelLocationRequest.getMAPDialog();
            cancelLocationRequestDialog.addCancelLocationResponse(invokeId, null);
            cancelLocationRequestDialog.close(false);

            if (cancelLocationRequest.getCancellationType() == CancellationType.subscriptionWithdraw) {
                if (cancelLocationRequest.isReattachRequired()) {
                    sendAuthenticationInfoRequest("901405105682021");
                }
            }

            new Thread(new PurgeMSSender(this)).start();
            new Thread(new CHISender(this)).start();
            new Thread(new ATMSender(this)).start();

        } catch (MAPException e) {
            logger.error("Error while processing CancelLocationRequest ", e);
        }
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse cancelLocationResponse) {
        logger.error("onCancelLocationResponse over DialogId={}", cancelLocationResponse
                .getMAPDialog().getLocalDialogId());
    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest purgeMSRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onPurgeMSRequest over DialogId={}", purgeMSRequest
                    .getMAPDialog().getLocalDialogId());
        }
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse purgeMSResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug("onPurgeMSResponse over DialogId={}", purgeMSResponse
                    .getMAPDialog().getLocalDialogId());
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
        if (logger.isDebugEnabled()) {
            logger.debug("onResetRequest over DialogId={}", resetRequestIndication.getMAPDialog().getLocalDialogId());
            try {
                if (resetRequestIndication.getHlrNumber() != null)
                    logger.debug("onResetRequest, hlrNumber={}", resetRequestIndication.getHlrNumber());
                if (resetRequestIndication.getSendingNodenumber() != null)
                    logger.debug("onResetRequest, sendingNodeNumber={}", resetRequestIndication.getSendingNodenumber());
                if (resetRequestIndication.getHlrList() != null)
                    logger.debug("onResetRequest, hlrList={}", resetRequestIndication.getHlrList());
                if (resetRequestIndication.getResetIdList() != null)
                    logger.debug("onResetRequest, resetIdList={}", resetRequestIndication.getResetIdList());
                if (resetRequestIndication.getSubscriptionData() != null)
                    logger.debug("onResetRequest, subscriptionData={}", resetRequestIndication.getSubscriptionData());
                if (resetRequestIndication.getSubscriptionDataDeletion() != null)
                    logger.debug("onResetRequest, subscriptionDataDeletion={}", resetRequestIndication.getSubscriptionDataDeletion());

            } catch (Exception e) {
                logger.error("Error while processing onResetRequest ", e);
            }
        }

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
    public void onAnyTimeModificationRequest(AnyTimeModificationRequest anyTimeModificationRequest) {

    }

    @Override
    public void onAnyTimeModificationResponse(AnyTimeModificationResponse anyTimeModificationResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug("onAnyTimeModificationResponse over DialogId={}", anyTimeModificationResponse
                    .getMAPDialog().getLocalDialogId());
        }
    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest provideSubscriberInfoRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onProvideSubscriberInfoRequest over DialogId={}", provideSubscriberInfoRequest
                    .getMAPDialog().getLocalDialogId());
        }

        try {
            long invokeId = provideSubscriberInfoRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = provideSubscriberInfoRequest.getMAPDialog();
            RequestedInfo requestedInfo = provideSubscriberInfoRequest.getRequestedInfo();

            SubscriberInfo subscriberInfo;
            LocationInformation locationInformation = null;
            SubscriberState subscriberState = null;
            IMEI imei = null;
            MSClassmark2 msClassmark2 = null;
            MNPInfoRes mnpInfoRes = null;
            int ageOfLocationInformation = 0;
            Boolean currentLocationRetrieved = null;
            boolean saiPresent = false;
            int mcc, mnc, lac, cellId;
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
            CellGlobalIdOrServiceAreaIdFixedLength cgiOrSai = null;
            LocationNumber locationNumber;
            LocationNumberMap locationNumberMap;
            String mscAddress = getVLRSCCPAddress(SCCP_CLIENT_ADDRESS).getGlobalTitle().getDigits();
            String vlrAddress = getMSCSCCPAddress(SCCP_CLIENT_ADDRESS).getGlobalTitle().getDigits();
            ISDNAddressString mscNumber, vlrNumber;
            GeographicalInformation geographicalInformation;
            GeodeticInformation geodeticInformation;
            byte[] lteCgi;
            EUtranCgi eUtranCgi;
            byte[] trackingAreaId;
            TAId taId;
            RouteingNumber routeingNumber;
            SubscriberStateChoice subscriberStateChoice = null;
            PSSubscriberStateChoice psSubscriberStateChoice = null;
            NotReachableReason notReachableReason = null;
            ArrayList<PDPContextInfo> pdpContextInfoList = null;
            NumberPortabilityStatus numberPortabilityStatus;
            MAPExtensionContainer extensionContainer = null;
            UserCSGInformation userCSGInformation;
            IMSVoiceOverPsSessionsIndication imsVoiceOverPsSessionsIndication;
            Time lastUEActivityTime;
            UsedRATType lastRATType;
            PSSubscriberState epsSubscriberState = null;
            LocationInformationEPS locationInformationEPS = null;
            TimeZone timeZone = null;
            DaylightSavingTime daylightSavingTime = null;
            NRCellGlobalIdImpl nrCellGlobalIdentity = new NRCellGlobalIdImpl();
            FQDN amfAddress;
            PlmnId vplmnId;
            TimeZone localTimeZone;
            UsedRATType ratType;
            NRTAIdImpl nrTrackingAreaIdentity;
            LocationInformation5GS locationInformation5GS = null;
            Random rand = new Random();

            if (requestedInfo.getLocationInformation()) {
                switch(rand.nextInt(2) + 1) {
                    case 1:
                        saiPresent = true; // set saiPresent to false
                        break;
                    case 2:
                        // keep saiPresent to false
                        break;
                }
                switch (rand.nextInt(10) + 1) {
                    case 1:
                    case 2:
                    case 3:
                        subscriberStateChoice = SubscriberStateChoice.assumedIdle;
                        psSubscriberStateChoice = PSSubscriberStateChoice.psAttachedReachableForPaging;
                        break;
                    case 4:
                    case 5:
                    case 6:
                        subscriberStateChoice = SubscriberStateChoice.camelBusy;
                        psSubscriberStateChoice = PSSubscriberStateChoice.psAttachedReachableForPaging;
                        break;
                    case 7:
                        subscriberStateChoice = SubscriberStateChoice.netDetNotReachable;
                        notReachableReason = NotReachableReason.imsiDetached;
                        psSubscriberStateChoice = PSSubscriberStateChoice.netDetNotReachable;
                        break;
                    case 8:
                        subscriberStateChoice = SubscriberStateChoice.notProvidedFromVLR;
                        psSubscriberStateChoice = PSSubscriberStateChoice.notProvidedFromSGSNorMME;
                        break;
                    case 9:
                        subscriberStateChoice = SubscriberStateChoice.netDetNotReachable;
                        notReachableReason = NotReachableReason.restrictedArea;
                        psSubscriberStateChoice = PSSubscriberStateChoice.netDetNotReachable;
                        break;
                    case 10:
                        subscriberStateChoice = SubscriberStateChoice.netDetNotReachable;
                        notReachableReason = NotReachableReason.msPurged;
                        psSubscriberStateChoice = PSSubscriberStateChoice.psAttachedNotReachableForPaging;
                        break;
                    default:
                        subscriberStateChoice = SubscriberStateChoice.assumedIdle;
                        psSubscriberStateChoice = PSSubscriberStateChoice.notProvidedFromSGSNorMME;
                        break;
                }
                if (requestedInfo.getSubscriberState()) {
                    if (requestedInfo.getRequestedDomain() == null || requestedInfo.getRequestedDomain() == DomainType.csDomain)
                        subscriberState = mapProvider.getMAPParameterFactory().createSubscriberState(subscriberStateChoice, notReachableReason);
                }
                TypeOfShape typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                double geographicalLatitude;
                double geographicalLongitude;
                double geographicalUncertainty;
                double geodeticLatitude;
                double geodeticLongitude;
                double geodeticUncertainty;
                int geodeticConfidence = 1;
                int screeningAndPresentationIndicators = 3;
                switch(rand.nextInt(10) + 1) {
                    case 1:
                        mcc = 748;
                        mnc = 1;
                        lac = 101;
                        cellId = 10263;
                        geographicalLatitude = -34.909744;
                        geographicalLongitude = -56.146317;
                        geographicalUncertainty = 1.0;
                        geographicalInformation = new GeographicalInformationImpl(typeOfShape, geographicalLatitude, geographicalLongitude, geographicalUncertainty);
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f8100007ea02"); // ECGI = 748-1-518658; TBCD encoded: 47f8100007ea02
                        trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                        break;
                    case 2:
                        mcc = 748;
                        mnc = 1;
                        lac = 119;
                        cellId = 15336;
                        geographicalInformation = null;
                        geodeticLatitude = -34.910349;
                        geodeticLongitude = -56.149832;
                        geodeticUncertainty = 2.0;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        lteCgi = hexStringToByteArray("47f81000095f02"); // ECGI = 748-1-614146; TBCD encoded: 47f81000095f02
                        trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                        break;
                    case 3:
                        mcc = 748;
                        mnc = 1;
                        lac = 118;
                        cellId = 292;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f870004b2c04"); // ECGI = 748-7-4926468; TBCD encoded: 47f870004b2c04
                        trackingAreaId = hexStringToByteArray("47f8701b58"); // TAI = 748-7-7000; TBCD encoded: 47f8701b58
                        break;
                    case 4:
                        mcc = 748;
                        mnc = 1;
                        lac = 109;
                        cellId = 10175;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f8100007f001"); // // ECGI = 748-1-520193; TBCD encoded: 47f8100007f001
                        trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                        break;
                    case 5:
                        mcc = 748;
                        mnc = 1;
                        lac = 11;
                        cellId = 4812;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f870004c2e08"); // ECGI = 748-7-4992520; TBCD encoded: 47f870004c2e08
                        trackingAreaId = hexStringToByteArray("47f8701b58"); // TAI = 748-7-7000; TBCD encoded: 47f8701b58
                        break;
                    case 6:
                        mcc = 748;
                        mnc = 7;
                        lac = 8820;
                        cellId = 9748;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f87000477304"); // ECGI = 748-7-4682500; TBCD encoded: 47f87000477304
                        trackingAreaId = hexStringToByteArray("47f8701b6c"); // TAI = 748-7-7020; TBCD encoded: 47f8701b6c
                        break;
                    case 7:
                        mcc = 748;
                        mnc = 7;
                        lac = 8552;
                        cellId = 8239;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f870004b3605"); // ECGI = 748-7-4929029; TBCD encoded: 47f870004b3605
                        trackingAreaId = hexStringToByteArray("47f8701b58"); // TAI = 748-7-7000; TBCD encoded: 47f8701b58
                        break;
                    case 8:
                        mcc = 748;
                        mnc = 10;
                        lac = 9501;
                        cellId = 35100;
                        geographicalInformation = null;
                        geodeticLatitude = -34.905624;
                        geodeticLongitude = -55.042191;
                        geodeticUncertainty = 4.0;
                        geodeticConfidence = 10;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        lteCgi = hexStringToByteArray("47f81000004802"); // ECGI = 748-1-18434; TBCD encoded: 47f81000004802
                        trackingAreaId = hexStringToByteArray("47f8100002"); // TAI = 748-1-2; TBCD encoded: 47f8100002
                        break;
                    case 9:
                        mcc = 748;
                        mnc = 7;
                        lac = 8313;
                        cellId = 9281;
                        geographicalInformation = null;
                        geodeticLatitude = -34.891032;
                        geodeticLongitude = -56.0008102;
                        geodeticUncertainty = 4.0;
                        geodeticConfidence = 2;
                        screeningAndPresentationIndicators = 1;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        lteCgi = hexStringToByteArray("47f81000089700"); // ECGI = 748-1-562944; TBCD encoded: 47f81000089700
                        trackingAreaId = hexStringToByteArray("47f8100067"); // TAI = 748-1-103; TBCD encoded: 47f8100067
                        break;
                    case 10:
                        mcc = 748;
                        mnc = 7;
                        lac = 8820;
                        cellId = 8051;
                        geographicalInformation = null;
                        geodeticInformation = null;
                        lteCgi = hexStringToByteArray("47f8010000d502"); // ECGI = 748-10-54530; TBCD encoded: 47f8010000d502
                        trackingAreaId = hexStringToByteArray("47f8017238"); // TAI = 748-10-29240; TBCD encoded: 47f8017238
                        break;
                    default:
                        mcc = 748;
                        mnc = 10;
                        lac = 9501;
                        cellId = 35100;
                        geographicalInformation = null;
                        geodeticLatitude = -34.905624;
                        geodeticLongitude = -55.042190;
                        geodeticUncertainty = 4.0;
                        geodeticConfidence = 10;
                        screeningAndPresentationIndicators = 3;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                        lteCgi = hexStringToByteArray("47f81000004802"); // ECGI = 748-1-18434; TBCD encoded: 47f81000004802
                        trackingAreaId = hexStringToByteArray("47f8100002"); // TAI = 748-1-2; TBCD encoded: 47f8100002
                        break;
                }
                if (requestedInfo.getRequestedDomain() == null || requestedInfo.getRequestedDomain() == DomainType.csDomain) {
                    mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,NumberingPlan.ISDN, mscAddress);
                    vlrNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number,
                            NumberingPlan.ISDN, vlrAddress);
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
                    eUtranCgi = new EUtranCgiImpl(lteCgi);
                    taId = new TAIdImpl(trackingAreaId);
                    String mmeNameStr = "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org";
                    byte[] mme = mmeNameStr.getBytes();
                    DiameterIdentity mmeName = new DiameterIdentityImpl(mme);

                    try {
                        cgiOrSai = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdFixedLength(mcc, mnc, lac, cellId);
                    } catch (MAPException ex) {
                        logger.error(ex.getMessage());
                    }
                    cellGlobalIdOrServiceAreaIdOrLAI = mapProvider.getMAPParameterFactory().createCellGlobalIdOrServiceAreaIdOrLAI(cgiOrSai);
                    if (subscriberStateChoice == SubscriberStateChoice.assumedIdle) {
                        currentLocationRetrieved = true;
                    } else if (subscriberStateChoice == SubscriberStateChoice.camelBusy) {
                        currentLocationRetrieved = true;
                    } else if (subscriberStateChoice == SubscriberStateChoice.notProvidedFromVLR) {
                        ageOfLocationInformation = 3;
                        currentLocationRetrieved = false;
                    } else if (subscriberStateChoice == SubscriberStateChoice.netDetNotReachable) {
                        if (notReachableReason == NotReachableReason.imsiDetached) {
                            ageOfLocationInformation = 1575;
                            currentLocationRetrieved = false;
                            geographicalInformation = null;
                            geodeticInformation = null;
                            mscNumber = null;
                            vlrNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number,
                                    NumberingPlan.ISDN, vlrAddress);
                        } else if (notReachableReason == NotReachableReason.restrictedArea) {
                            ageOfLocationInformation = 300;
                            currentLocationRetrieved = false;
                            geographicalInformation = null;
                            geodeticInformation = null;
                            mscNumber = null;
                            vlrNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number,
                                    NumberingPlan.ISDN, vlrAddress);
                        } else if (notReachableReason == NotReachableReason.msPurged) {
                            ageOfLocationInformation = 221;
                            currentLocationRetrieved = false;
                            geographicalInformation = null;
                            geodeticInformation = null;
                            mscNumber = null;
                            vlrNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number,
                                    NumberingPlan.ISDN, vlrAddress);
                        } else {
                            ageOfLocationInformation = 1879;
                            currentLocationRetrieved = false;
                            geographicalInformation = null;
                            geodeticInformation = null;
                            mscNumber = null;
                            vlrNumber = mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number,
                                    NumberingPlan.ISDN, vlrAddress);
                        }
                    }
                    if (!requestedInfo.getLocationInformationEPSSupported()) {
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber, geodeticInformation,
                                currentLocationRetrieved, saiPresent, locationInformationEPS, null);
                    } else {
                        switch(rand.nextInt(10) + 1) {
                            case 1:
                                BitSetStrictLength csgIdBitSet = new BitSetStrictLength(27);
                                csgIdBitSet.set(0);
                                csgIdBitSet.set(1);
                                csgIdBitSet.set(25);
                                csgIdBitSet.set(26);
                                CSGId csgId = new CSGIdImpl(csgIdBitSet);
                                Integer accessMode = 1;
                                Integer cmi = 2;
                                userCSGInformation = new UserCSGInformationImpl(csgId, null, accessMode, cmi);
                                currentLocationRetrieved = ageOfLocationInformation == 0;
                                // location information not containing EPS location as the target subscriber is not under E-UTRAN
                                locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                        vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber, geodeticInformation,
                                        currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
                                break;
                            case 2:
                            case 3:
                                currentLocationRetrieved = ageOfLocationInformation == 0;
                                // target subscriber is under 5G NR SA
                                nrCellGlobalIdentity.setData(748, 1, 42949672954L);
                                amfAddress = new FQDNImpl("amf1.cluster1.net2.amf.5gc.mnc01.mcc748.3gppnetwork.org".getBytes());
                                vplmnId = new PlmnIdImpl(748, 1);
                                localTimeZone = new TimeZoneImpl(new byte[] {0,9});
                                ratType = UsedRATType.eUtran;
                                nrTrackingAreaIdentity = new NRTAIdImpl();
                                nrTrackingAreaIdentity.setData(748, 1, 595578);
                                geographicalLatitude = -34.909744;
                                geographicalLongitude = -56.146317;
                                geographicalUncertainty = 1.0;
                                geographicalInformation = new GeographicalInformationImpl(typeOfShape, geographicalLatitude, geographicalLongitude, geographicalUncertainty);
                                geodeticInformation = null;
                                locationInformation5GS = new LocationInformation5GSImpl(nrCellGlobalIdentity, eUtranCgi, geographicalInformation,
                                        geodeticInformation, amfAddress, taId, currentLocationRetrieved, ageOfLocationInformation, vplmnId,
                                        localTimeZone, ratType, null, nrTrackingAreaIdentity);
                                break;
                            case 4:
                            case 5:
                                // target subscriber is under 5G NSA (E-UTRAN and NR)
                                nrCellGlobalIdentity.setData(748, 2, 34359738376L);
                                amfAddress = new FQDNImpl("amf3.cluster2.net2.amf.5gc.mnc02.mcc748.3gppnetwork.org".getBytes());
                                vplmnId = new PlmnIdImpl(748, 2);
                                localTimeZone = new TimeZoneImpl(new byte[] {0, 8});
                                ratType = UsedRATType.eUtran;
                                nrTrackingAreaIdentity = new NRTAIdImpl();
                                nrTrackingAreaIdentity.setData(748, 2, 495570);
                                currentLocationRetrieved = ageOfLocationInformation == 0;
                                geodeticLatitude = -34.910349;
                                geodeticLongitude = -56.149832;
                                geodeticUncertainty = 2.0;
                                geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, geodeticLatitude, geodeticLongitude, geodeticUncertainty, geodeticConfidence);
                                locationInformation5GS = new LocationInformation5GSImpl(nrCellGlobalIdentity, eUtranCgi, geographicalInformation,
                                        geodeticInformation, amfAddress, taId, currentLocationRetrieved, ageOfLocationInformation, vplmnId,
                                        localTimeZone, ratType, null, nrTrackingAreaIdentity);
                                lteCgi = hexStringToByteArray("47f81000095f02"); // ECGI = 748-1-614146; TBCD encoded: 47f81000095f02
                                trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                                eUtranCgi = new EUtranCgiImpl(lteCgi);
                                taId = new TAIdImpl(trackingAreaId);
                                mmeNameStr = "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org";
                                mme = mmeNameStr.getBytes();
                                mmeName = new DiameterIdentityImpl(mme);
                                locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                                        geodeticInformation, currentLocationRetrieved, ageOfLocationInformation, mmeName);
                                locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(null, null,
                                        null, null, null, null, null, null, null,
                                        false, false, locationInformationEPS, null);
                                locationInformationEPS = null; // If the HLR receives locationInformationEPS (outside the locationInformation IE) from a VLR, it shall discard it.
                                break;
                            default:
                                currentLocationRetrieved = ageOfLocationInformation == 0;
                                // target subscriber has EPS location information within CS location information
                                locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                                        geodeticInformation, currentLocationRetrieved, ageOfLocationInformation, mmeName);
                                locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(null, null,
                                        null, null, null, null, null, null, null,
                                        false, false, locationInformationEPS, null);
                                break;
                        }
                    }
                }
            }

            if (requestedInfo.getMnpRequestedInfo()) {
                if (subscriberStateChoice != SubscriberStateChoice.netDetNotReachable &&
                        psSubscriberStateChoice != PSSubscriberStateChoice.psAttachedNotReachableForPaging &&
                        psSubscriberStateChoice != PSSubscriberStateChoice.netDetNotReachable) {
                    routeingNumber = new RouteingNumberImpl("491710");
                    IMSI mnpImsi = new IMSIImpl(String.valueOf(imsiForPurge));
                    ISDNAddressString mnpMsisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, "59899077937");
                    numberPortabilityStatus = NumberPortabilityStatus.ownNumberNotPortedOut;
                    mnpInfoRes = mapProvider.getMAPParameterFactory().createMNPInfoRes(routeingNumber, mnpImsi, mnpMsisdn, numberPortabilityStatus, extensionContainer);
                }
            }

            if (requestedInfo.getImei()) {
                if (subscriberStateChoice != SubscriberStateChoice.netDetNotReachable &&
                        psSubscriberStateChoice != PSSubscriberStateChoice.psAttachedNotReachableForPaging &&
                        psSubscriberStateChoice != PSSubscriberStateChoice.netDetNotReachable) {
                    if (requestedInfo.getRequestedDomain() == null || requestedInfo.getRequestedDomain() == DomainType.csDomain) {
                        imei = mapProvider.getMAPParameterFactory().createIMEI("011714004661050");
                    } else {
                        imei = mapProvider.getMAPParameterFactory().createIMEI("011714004661051");
                    }
                }
            }

            if (requestedInfo.getMsClassmark()) {
                if (requestedInfo.getRequestedDomain() == null || requestedInfo.getRequestedDomain() == DomainType.csDomain) {
                    if (subscriberStateChoice != SubscriberStateChoice.netDetNotReachable) {
                        byte[] classmark = {57, 58, 82};
                        msClassmark2 = mapProvider.getMAPParameterFactory().createMSClassmark2(classmark);
                    }
                }
            }

            imsVoiceOverPsSessionsIndication = IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported;
            lastUEActivityTime = new TimeImpl(2024, 8, 5, 10, 27, 49);
            lastRATType = UsedRATType.eUtran;
            if (requestedInfo.getSubscriberState() && requestedInfo.getLocationInformationEPSSupported()) {
                if (locationInformationEPS != null)
                    epsSubscriberState = new PSSubscriberStateImpl(psSubscriberStateChoice, notReachableReason, pdpContextInfoList);
            }
            if (requestedInfo.getLocalTimeZoneRequest()) {
                timeZone = new TimeZoneImpl(new byte[]{0, 3});
                daylightSavingTime = DaylightSavingTime.noAdjustment;
            }

            // If the HLR receives locationInformationGPRS, ps-SubscriberState, gprs-MS-Class or
            // locationInformationEPS (outside the locationInformation IE) from a VLR, it shall discard them.
            subscriberInfo = new SubscriberInfoImpl(locationInformation, subscriberState, null,
                    null, null, imei, msClassmark2, null, mnpInfoRes,
                    imsVoiceOverPsSessionsIndication, lastUEActivityTime, lastRATType, epsSubscriberState,
                    null, timeZone, daylightSavingTime, locationInformation5GS);
            mapDialogMobility.addProvideSubscriberInfoResponse(invokeId, subscriberInfo, null);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing ProvideSubscriberInfoRequest ", e);
        }
    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse provideSubscriberInfoResponse) {

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest deleteSubscriberDataRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onDeleteSubscriberDataRequest over DialogId={}", deleteSubscriberDataRequest
                    .getMAPDialog().getLocalDialogId());
        }
        try {
            long invokeId = deleteSubscriberDataRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = deleteSubscriberDataRequest.getMAPDialog();

            Random rand = new Random();
            RegionalSubscriptionResponse regionalSubscriptionResponse = null;
            switch (rand.nextInt(10 + 1)) {
                case 1:
                    regionalSubscriptionResponse = RegionalSubscriptionResponse.networkNodeAreaRestricted;
                    break;
                case 2:
                    regionalSubscriptionResponse =  RegionalSubscriptionResponse.tooManyZoneCodes;
                    break;
                case 3:
                    regionalSubscriptionResponse =  RegionalSubscriptionResponse.zoneCodesConflict;
                    break;
                case 4:
                    regionalSubscriptionResponse =  RegionalSubscriptionResponse.regionalSubscNotSupported;
                    break;
                default:
                    break;
            }

            mapDialogMobility.addDeleteSubscriberDataResponse(invokeId, regionalSubscriptionResponse, null);
            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing MAP DSD request and sending MAP DSD response", e);
        }
    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse deleteSubscriberDataResponse) {

    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest checkImeiRequest) {

    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse checkImeiResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug("onCheckImeiResponse over DialogId={}", checkImeiResponse
                    .getMAPDialog().getLocalDialogId());
        }
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
            AddressString origRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
            AddressString destRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);

            SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, SCCP_CLIENT_ADDRESS);
            SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

            MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().
                    createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext, MAPApplicationContextVersion.version3),
                            clientSccpAddress, origRef, serverSccpAddress, destRef);

            IMSI imsi = new IMSIImpl(imsiDigits);
            int numberOfRequestedVectors = 5;
            boolean segmentationProhibited = false;
            boolean immediateResponsePreferred = false;
            ReSynchronisationInfo reSynchronisationInfo = getReSynchronisationInfo();
            RequestingNodeType requestingNodeType = RequestingNodeType.vlr;
            byte[] mccMnc = new byte[] {0x47, (byte) 0xf8, 0x10};
            PlmnId requestingPlmnId = new PlmnIdImpl(mccMnc);
            Integer numberOfRequestedAdditionalVectors = 1;
            boolean additionalVectorsAreForEPS = true;
            boolean ueUsageTypeRequestIndication = true;

            mapDialogMobility.addSendAuthenticationInfoRequest(imsi, numberOfRequestedVectors, segmentationProhibited,
                    immediateResponsePreferred, reSynchronisationInfo, null, requestingNodeType, requestingPlmnId,
                    numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS, ueUsageTypeRequestIndication);

            mapDialogMobility.send();

            this.csvWriter.incrementCounter(CREATED_DIALOGS);

        } catch (MAPException e) {
            logger.error("Error while sending CancelLocationRequest ", e);
        }
    }

    private static ReSynchronisationInfo getReSynchronisationInfo() {
        byte[] rand = new byte[] {(byte) 0xf6, (byte) 0xe2, (byte) 0xc3, (byte) 0xdc, (byte) 0xa4, (byte) 0xca,
                (byte) 0xae, (byte) 0x9e, 0x4c, (byte) 0xba, 0x0f, (byte) 0xd3, 0x42, 0x72, (byte) 0xee, 0x46};
        byte[] auts = new byte[] {(byte) 0xe9, 0x15, (byte) 0x97, (byte) 0x88, (byte) 0xbc, (byte) 0xeb, (byte) 0x80,
                0x00, (byte) 0x81, 0x3f, (byte) 0xc0, 0x40, (byte) 0xff, 0x53};
        return new ReSynchronisationInfoImpl(rand, auts);
    }

    private static class PurgeMSSender implements Runnable {

        private final Client client4PurgeMsSender;

        public PurgeMSSender(Client client) {
            client4PurgeMsSender = client;
        }

        public Client getClient() {
            return client4PurgeMsSender;
        }

        @Override
        public void run() {
            imsiForPurge++;

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception on {}, {}", getClient(), ie.getMessage());
            }
            try {
                // Create Dialog
                AddressString originAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
                AddressString destAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

                SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, SCCP_CLIENT_ADDRESS);
                SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

                MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
                MAPApplicationContextName mapAcn = MAPApplicationContextName.msPurgingContext;
                MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                        originAddressString, serverSccpAddress, destAddressString);

                IMSI imsi = new IMSIImpl(String.valueOf(imsiForPurge));
                Random rand = new Random();
                int ageOfLocationInformation;
                LocationInformation locationInformation = null;
                LocationInformationEPS locationInformationEPS = null;
                boolean saiPresent = false;
                int mcc, mnc, lac, cellId;
                CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
                CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = null;
                LocationNumber locationNumber;
                LocationNumberMap locationNumberMap;
                ISDNAddressString mscNumber =  new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "491710490000");
                ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "491710490000");
                GeographicalInformation geographicalInformation;
                GeodeticInformation geodeticInformation;
                byte[] lteCgi;
                EUtranCgi eUtranCgi;
                byte[] trackingAreaId;
                TAId taId;
                TypeOfShape typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
                double latitude;
                double longitude;
                double uncertainty;
                int confidence;
                int screeningAndPresentationIndicators;
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
                DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());

                switch(rand.nextInt(7) + 1) {
                    case 1:
                        ageOfLocationInformation = 0;
                        mcc = 748;
                        mnc = 1;
                        lac = 101;
                        cellId = 10263;
                        latitude = -34.909744;
                        longitude = -56.146317;
                        uncertainty = 1.0;
                        geographicalInformation = new GeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty);
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        saiPresent = true;
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber,
                                null, true, saiPresent, null, null);
                        break;
                    case 2:
                        ageOfLocationInformation = 0;
                        screeningAndPresentationIndicators = 1;
                        latitude = -34.910349;
                        longitude = -56.149832;
                        uncertainty = 2.0;
                        confidence = 2;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
                        lteCgi = hexStringToByteArray("47f81000095f02"); // ECGI = 748-1-614146; TBCD encoded: 47f81000095f02
                        trackingAreaId = hexStringToByteArray("47f810006d"); // TAI = 748-1-109; TBCD encoded: 47f810006d
                        eUtranCgi = new EUtranCgiImpl(lteCgi);
                        taId = new TAIdImpl(trackingAreaId);
                        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, null, null,
                                geodeticInformation, true, ageOfLocationInformation, mmeName);
                        break;
                    case 3:
                        ageOfLocationInformation = 1;
                        mcc = 748;
                        mnc = 1;
                        lac = 109;
                        cellId = 10175;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, null,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber,
                                null, false, saiPresent, null, null);
                        break;
                    case 4:
                        ageOfLocationInformation = 0;
                        mcc = 748;
                        mnc = 1;
                        lac = 11;
                        cellId = 4812;
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        latitude = -34.909744;
                        longitude = -56.146317;
                        uncertainty = 1.0;
                        geographicalInformation = new GeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty);
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, geographicalInformation,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber,
                                null, true, saiPresent, null, null);
                        break;
                    case 5:
                        ageOfLocationInformation = 5;
                        lteCgi = hexStringToByteArray("47f87000477304"); // ECGI = 748-7-4682500; TBCD encoded: 47f87000477304
                        trackingAreaId = hexStringToByteArray("47f8701b6c"); // TAI = 748-7-7020; TBCD encoded: 47f8701b6c
                        eUtranCgi = new EUtranCgiImpl(lteCgi);
                        taId = new TAIdImpl(trackingAreaId);
                        screeningAndPresentationIndicators = 1;
                        latitude = -34.910349;
                        longitude = -56.149832;
                        uncertainty = 2.0;
                        confidence = 2;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
                        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, null, null,
                                geodeticInformation, false, ageOfLocationInformation, mmeName);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(null, null,
                                vlrNumber, null, null, null, null, mscNumber,
                                null, false, false, locationInformationEPS, null);
                        locationInformationEPS = null; // locationInformationEPS will only exist within locationInformation in this case
                        break;
                    case 6:
                        ageOfLocationInformation = 0;
                        lteCgi = hexStringToByteArray("47f87000477304"); // ECGI = 748-7-4682500; TBCD encoded: 47f87000477304
                        trackingAreaId = hexStringToByteArray("47f8701b6c"); // TAI = 748-7-7020; TBCD encoded: 47f8701b6c
                        eUtranCgi = new EUtranCgiImpl(lteCgi);
                        taId = new TAIdImpl(trackingAreaId);
                        latitude = -34.909744;
                        longitude = -56.146317;
                        uncertainty = 1.0;
                        geographicalInformation = new GeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty);
                        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, null, geographicalInformation,
                                null, true, ageOfLocationInformation, mmeName);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(null, null,
                                vlrNumber, null, null, null, null, mscNumber,
                                null, false, false, locationInformationEPS, null);
                        locationInformationEPS = null; // locationInformationEPS will only exist within locationInformation in this case
                        break;
                    case 7:
                        ageOfLocationInformation = 0;
                        mcc = 748;
                        mnc = 7;
                        lac = 8552;
                        cellId = 8239;
                        screeningAndPresentationIndicators = 2;
                        latitude = -34.910349;
                        longitude = -56.149832;
                        uncertainty = 2.0;
                        confidence = 3;
                        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
                        try {
                            cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
                        } catch (MAPException ex) {
                            logger.error(ex.getMessage());
                        }
                        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
                        locationInformation = mapProvider.getMAPParameterFactory().createLocationInformation(ageOfLocationInformation, null,
                                vlrNumber, locationNumberMap, cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber,
                                geodeticInformation, true, saiPresent, null, null);
                        break;
                    default:
                        break;
                }

                mapDialogMobility.addPurgeMSRequest(imsi, vlrNumber, null, null, locationInformation,
                        null, locationInformationEPS);
                mapDialogMobility.send();

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static class ATMSender implements Runnable {

        private final Client client4AtmSender;

        public ATMSender(Client client) {
            client4AtmSender = client;
        }

        public Client getClient() {
            return client4AtmSender;
        }

        @Override
        public void run() {
            imsiForATM++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception for "+getClient()+"." +ie.getMessage());
            }

            try {
                // Create Dialog
                AddressString originAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
                AddressString destAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

                SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, SCCP_CLIENT_ADDRESS);
                SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

                MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
                MAPApplicationContextName mapAcn = MAPApplicationContextName.anyTimeInfoHandlingContext;
                MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                        originAddressString, serverSccpAddress, destAddressString);

                IMSI imsi = new IMSIImpl(String.valueOf(imsiForATM));
                SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(imsi);
                ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                        "491710490023");
                SupplementaryCodeValue supplementaryCodeValue = getSupplementaryCodeValue();
                SSCode ssCode = new SSCodeImpl(supplementaryCodeValue);
                ExtBasicServiceCode basicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(getBearerServiceCodeValue()));
                ExtBasicServiceCode basicServiceCode2 = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(getTeleserviceCodeValue()));
                ExtSSStatus ssStatus = new ExtSSStatusImpl(false, true, false, true);
                AddressString forwardedToNumber = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
                ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(new byte[] { 2, 5 });
                Random rand = new Random();
                Integer noReplyConditionTime = rand.nextInt(26) + 5;
                ModificationInstruction modifyNotificationToCSE = ModificationInstruction.activate;
                ModificationRequestForCFInfo modificationRequestForCFInfo = null;
                ModificationRequestForCBInfo modificationRequestForCBInfo = null;
                ModificationRequestForCSI modificationRequestForCSI = null;
                boolean longFTNSupported = true;
                ModificationRequestForODBData modificationRequestForODBData = null;
                ModificationRequestForIPSMGWData modificationRequestForIPSMGWData = null;
                RequestedServingNode activationRequestForUEReachability = new RequestedServingNodeImpl(true);
                ModificationRequestForCSG modificationRequestForCSG = null;
                ModificationRequestForCWInfo modificationRequestForCWData = null;
                ModificationRequestForCLIPInfo modificationRequestForCLIPData = null;
                ModificationRequestForCLIRInfo modificationRequestForCLIRData = null;
                ModificationRequestForCHInfo modificationRequestForHOLDData= null;
                ModificationRequestForECTInfo modificationRequestForECTData = null;

                switch (rand.nextInt(11) + 1) {
                    case 1:
                        modificationRequestForCFInfo = new ModificationRequestForCFInfoImpl(ssCode, basicServiceCode, ssStatus, forwardedToNumber,
                                forwardedToSubaddress, noReplyConditionTime, modifyNotificationToCSE, null);
                        break;
                    case 2:
                        Password password = new PasswordImpl("1230");
                        Integer wrongPasswordAttemptsCounter = 4;
                        modificationRequestForCBInfo = new ModificationRequestForCBInfoImpl(ssCode, basicServiceCode2, ssStatus, password,
                                wrongPasswordAttemptsCounter, modifyNotificationToCSE, null);
                        break;
                    case 3:
                        RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo = RequestedCAMELSubscriptionInfo.getInstance(rand.nextInt(9));
                        ModificationInstruction modifyCSIState = ModificationInstruction.getInstance(1);
                        AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo = AdditionalRequestedCAMELSubscriptionInfo.getInstance(rand.nextInt(5));
                        modificationRequestForCSI = new ModificationRequestForCSIImpl(requestedCAMELSubscriptionInfo, modifyNotificationToCSE,
                                modifyCSIState, null, additionalRequestedCAMELSubscriptionInfo);
                        break;
                    case 4:
                        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, false, true, false, true, false, true, true, false,
                            true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                                true, false, true, false);
                        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(true, false, false, false);
                        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
                        modificationRequestForODBData = new ModificationRequestForODBDataImpl(odbData, modifyNotificationToCSE, null);
                        break;
                    case 5:
                        ModificationInstruction modifyRegistrationStatus = ModificationInstruction.activate;
                        NetworkNodeDiameterAddress networkNodeDiameterAddress = getNetworkNodeDiameterAddress();
                        modificationRequestForIPSMGWData = new ModificationRequestForIPSMGWDataImpl(modifyRegistrationStatus, null, networkNodeDiameterAddress);
                        break;
                    case 6:
                        modificationRequestForCSG = new ModificationRequestForCSGImpl(modifyNotificationToCSE, null);
                        break;
                    case 7:
                        modificationRequestForCWData = new ModificationRequestForCWInfoImpl(basicServiceCode, ssStatus, modifyNotificationToCSE, null);
                        break;
                    case 8:
                        OverrideCategory overrideCategory = OverrideCategory.getInstance(rand.nextInt(1));
                        modificationRequestForCLIPData = new ModificationRequestForCLIPInfoImpl(ssStatus, overrideCategory, modifyNotificationToCSE, null);
                        break;
                    case 9:
                        CliRestrictionOption cliRestrictionOption = CliRestrictionOption.getInstance(rand.nextInt(2));
                        modificationRequestForCLIRData = new ModificationRequestForCLIRInfoImpl(ssStatus, cliRestrictionOption, modifyNotificationToCSE, null);
                        break;
                    case 10:
                        modificationRequestForHOLDData = new ModificationRequestForCHInfoImpl(ssStatus, modifyNotificationToCSE, null);
                        break;
                    case 11:
                        modificationRequestForECTData = new ModificationRequestForECTInfoImpl(ssStatus, modifyNotificationToCSE, null);
                        break;
                }
                mapDialogMobility.addAnyTimeModificationRequest(30, subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo, modificationRequestForCBInfo,
                        modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData, modificationRequestForIPSMGWData,
                        activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData, modificationRequestForCLIPData, modificationRequestForCLIRData,
                        modificationRequestForHOLDData, modificationRequestForECTData);

                mapDialogMobility.send();

            } catch (MAPException e) {
                logger.error("MAPException while adding MAP ATM to MAP dialog", e);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static class CHISender implements Runnable {

        private final Client client4ChiSender;

        public CHISender(Client client) {
            client4ChiSender = client;
        }

        public Client getClient() {
            return client4ChiSender;
        }

        @Override
        public void run() {
            imsiForCheckImei_Huawei++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception for "+getClient()+"." +ie.getMessage());
            }
            try {
                // Create Dialog
                AddressString originAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
                AddressString destAddressString = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");

                SccpAddress clientSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, SCCP_CLIENT_ADDRESS);
                SccpAddress serverSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);

                MAPApplicationContextVersion mapAcnVersion = MAPApplicationContextVersion.version3;
                MAPApplicationContextName mapAcn = MAPApplicationContextName.equipmentMngtContext;
                MAPApplicationContext mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                        originAddressString, serverSccpAddress, destAddressString);

                IMEI imei = mapProvider.getMAPParameterFactory().createIMEI("011714004661050");
                boolean equipmentStatus = true;
                boolean bmuef = false;
                RequestedEquipmentInfo reqEquipmentInfo;
                IMSI imsi;

                try {
                    Random rand = new Random();
                    switch (rand.nextInt(4) + 1) {
                        case 1:
                            mapAcnVersion = MAPApplicationContextVersion.version2;
                            mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                            mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                                    originAddressString, serverSccpAddress, destAddressString);
                            mapDialogMobility.addCheckImeiRequest_Huawei(imei, null, null, null);
                            break;
                        case 2:
                            mapAcnVersion = MAPApplicationContextVersion.version2;
                            mapAppContext = MAPApplicationContext.getInstance(mapAcn, mapAcnVersion);
                            mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapAppContext, clientSccpAddress,
                                    originAddressString, serverSccpAddress, destAddressString);
                            imsi = new IMSIImpl(String.valueOf(imsiForCheckImei_Huawei));
                            mapDialogMobility.addCheckImeiRequest_Huawei(imei, null, null, imsi);
                            break;
                        case 3:
                            reqEquipmentInfo = new RequestedEquipmentInfoImpl(equipmentStatus, bmuef);
                            mapDialogMobility.addCheckImeiRequest(imei, reqEquipmentInfo, null);
                            break;
                        case 4:
                            bmuef = true;
                            reqEquipmentInfo = new RequestedEquipmentInfoImpl(equipmentStatus, bmuef);
                            mapDialogMobility.addCheckImeiRequest(imei, reqEquipmentInfo, null);
                            break;
                        default:
                            break;
                    }

                    mapDialogMobility.send();

                } catch (MAPException e) {
                    logger.error("MAPException while adding MAP CHI to MAP dialog", e);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    /*
     * VLR SCCP Address creation
     */
    private SccpAddress getVLRSCCPAddress(String vlrAddress) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(vlrAddress, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int vlrSsn = 7;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, vlrSsn);
    }

    private static BearerServiceCodeValue getBearerServiceCodeValue() {
        BearerServiceCodeValue bearerServiceCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(51 + 1)) {
            case 2:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCDAServices;
                break;
            case 3:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_300bps;
                break;
            case 4:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_1200bps;
                break;
            case 5:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_1200_75bps;
                break;
            case 6:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_2400bps;
                break;
            case 7:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_4800bps;
                break;
            case 8:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_9600bps;
                break;
            case 9:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataCDA;
                break;
            case 10:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCDS_Services;
                break;
            case 11:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_1200bps;
                break;
            case 12:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_2400bps;
                break;
            case 13:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_4800bps;
                break;
            case 14:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_9600bps;
                break;
            case 15:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataCDS;
                break;
            case 16:
                bearerServiceCodeValue = BearerServiceCodeValue.allPadAccessCA_Services;
                break;
            case 17:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_300bps;
                break;
            case 18:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_1200bps;
                break;
            case 19:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_1200_75bps;
                break;
            case 20:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_2400bps;
                break;
            case 21:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_4800bps;
                break;
            case 22:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_9600bps;
                break;
            case 23:
                bearerServiceCodeValue = BearerServiceCodeValue.general_padAccessCA;
                break;
            case 24:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataPDS_Services;
                break;
            case 25:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_2400bps;
                break;
            case 26:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_4800bps;
                break;
            case 27:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_9600bps;
                break;
            case 28:
                bearerServiceCodeValue = BearerServiceCodeValue.allAlternateSpeech_DataCDA;
                break;
            case 29:
                bearerServiceCodeValue = BearerServiceCodeValue.allAlternateSpeech_DataCDS;
                break;
            case 30:
                bearerServiceCodeValue = BearerServiceCodeValue.allSpeechFollowedByDataCDA;
                break;
            case 31:
                bearerServiceCodeValue = BearerServiceCodeValue.allSpeechFollowedByDataCDS;
                break;
            case 32:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataPDS;
                break;
            case 33:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCircuitAsynchronous;
                break;
            case 34:
                bearerServiceCodeValue = BearerServiceCodeValue.allAsynchronousServices;
                break;
            case 35:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCircuitSynchronous;
                break;
            case 36:
                bearerServiceCodeValue = BearerServiceCodeValue.allSynchronousServices;
                break;
            /*case 37:
                bearerServiceCodeValue = BearerServiceCodeValue.allPLMN_specificBS;
                break;
            case 38:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_1;
                break;
            case 39:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_2;
                break;
            case 40:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_3;
                break;
            case 41:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_4;
                break;
            case 42:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_5;
                break;
            case 43:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_6;
                break;
            case 44:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_7;
                break;
            case 45:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_8;
                break;
            case 46:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_9;
                break;
            case 47:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_A;
                break;
            case 48:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_B;
                break;
            case 49:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_C;
                break;
            case 50:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_E;
                break;
            case 51:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_F;
                break;*/
            default:
                bearerServiceCodeValue = BearerServiceCodeValue.allBearerServices;
                break;
        }
        return bearerServiceCodeValue;
    }

    private static TeleserviceCodeValue getTeleserviceCodeValue() {
        TeleserviceCodeValue teleserviceCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(33 + 1)) {
            case 2:
                teleserviceCodeValue = TeleserviceCodeValue.allSpeechTransmissionServices;
                break;
            case 3:
                teleserviceCodeValue = TeleserviceCodeValue.telephony;
                break;
            case 4:
                teleserviceCodeValue = TeleserviceCodeValue.emergencyCalls;
                break;
            case 5:
                teleserviceCodeValue = TeleserviceCodeValue.allShortMessageServices;
                break;
            case 6:
                teleserviceCodeValue = TeleserviceCodeValue.shortMessageMT_PP;
                break;
            case 7:
                teleserviceCodeValue = TeleserviceCodeValue.shortMessageMO_PP;
                break;
            case 8:
                teleserviceCodeValue = TeleserviceCodeValue.cellBroadcast;
                break;
            case 9:
                teleserviceCodeValue = TeleserviceCodeValue.allFacsimileTransmissionServices;
                break;
            case 10:
                teleserviceCodeValue = TeleserviceCodeValue.facsimileGroup3AndAlterSpeech;
                break;
            case 11:
                teleserviceCodeValue = TeleserviceCodeValue.automaticFacsimileGroup3;
                break;
            case 12:
                teleserviceCodeValue = TeleserviceCodeValue.facsimileGroup4;
                break;
            case 13:
                teleserviceCodeValue = TeleserviceCodeValue.allDataTeleservices;
                break;
            case 14:
                teleserviceCodeValue = TeleserviceCodeValue.allTeleservices_ExeptSMS;
                break;
            case 15:
                teleserviceCodeValue = TeleserviceCodeValue.allVoiceGroupCallServices;
                break;
            case 16:
                teleserviceCodeValue = TeleserviceCodeValue.voiceGroupCall;
                break;
            case 17:
                teleserviceCodeValue = TeleserviceCodeValue.voiceBroadcastCall;
                break;
            /*case 18:
                teleserviceCodeValue = TeleserviceCodeValue.allPLMN_specificTS;
                break;
            case 19:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_1;
                break;
            case 20:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_2;
                break;
            case 21:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_3;
                break;
            case 22:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_4;
                break;
            case 23:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_5;
                break;
            case 24:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_6;
                break;
            case 25:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_7;
                break;
            case 26:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_8;
                break;
            case 27:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_9;
                break;
            case 28:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_A;
                break;
            case 29:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_B;
                break;
            case 30:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_C;
                break;
            case 31:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_D;
                break;
            case 32:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_E;
                break;
            case 33:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_F;
                break;
            default:
                teleserviceCodeValue = TeleserviceCodeValue.allTeleservices;
                break;*/
            default:
                teleserviceCodeValue = TeleserviceCodeValue.allTeleservices;
        }
        return teleserviceCodeValue;
    }

    private static SupplementaryCodeValue getSupplementaryCodeValue() {
        SupplementaryCodeValue supplementaryCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(72 + 1)) {
            case 1:
                supplementaryCodeValue = SupplementaryCodeValue.allLineIdentificationSS;
                break;
            case 2:
                supplementaryCodeValue = SupplementaryCodeValue.clip;
                break;
            case 3:
                supplementaryCodeValue = SupplementaryCodeValue.clir;
                break;
            case 4:
                supplementaryCodeValue = SupplementaryCodeValue.colp;
                break;
            case 5:
                supplementaryCodeValue = SupplementaryCodeValue.colr;
                break;
            case 6:
                supplementaryCodeValue = SupplementaryCodeValue.mci;
                break;
            case 7:
                supplementaryCodeValue = SupplementaryCodeValue.allNameIdentificationSS;
                break;
            case 8:
                supplementaryCodeValue = SupplementaryCodeValue.cnap;
                break;
            case 9:
                supplementaryCodeValue = SupplementaryCodeValue.allForwardingSS;
                break;
            case 10:
                supplementaryCodeValue = SupplementaryCodeValue.cfu;
                break;
            case 11:
                supplementaryCodeValue = SupplementaryCodeValue.allCondForwardingSS;
                break;
            case 12:
                supplementaryCodeValue = SupplementaryCodeValue.cfb;
                break;
            case 13:
                supplementaryCodeValue = SupplementaryCodeValue.cfnry;
                break;
            case 14:
                supplementaryCodeValue = SupplementaryCodeValue.cfnrc;
                break;
            case 15:
                supplementaryCodeValue = SupplementaryCodeValue.cd;
                break;
            case 16:
                supplementaryCodeValue = SupplementaryCodeValue.allCallOfferingSS;
                break;
            case 17:
                supplementaryCodeValue = SupplementaryCodeValue.ect;
                break;
            case 18:
                supplementaryCodeValue = SupplementaryCodeValue.mah;
                break;
            case 19:
                supplementaryCodeValue = SupplementaryCodeValue.allCallCompletionSS;
                break;
            case 20:
                supplementaryCodeValue = SupplementaryCodeValue.cw;
                break;
            case 21:
                supplementaryCodeValue = SupplementaryCodeValue.hold;
                break;
            case 22:
                supplementaryCodeValue = SupplementaryCodeValue.ccbs_A;
                break;
            case 23:
                supplementaryCodeValue = SupplementaryCodeValue.ccbs_B;
                break;
            case 24:
                supplementaryCodeValue = SupplementaryCodeValue.mc;
                break;
            case 25:
                supplementaryCodeValue = SupplementaryCodeValue.allMultiPartySS;
                break;
            case 26:
                supplementaryCodeValue = SupplementaryCodeValue.multiPTY;
                break;
            case 27:
                supplementaryCodeValue = SupplementaryCodeValue.allCommunityOfInterestSS;
                break;
            case 28:
                supplementaryCodeValue = SupplementaryCodeValue.cug;
                break;
            case 29:
                supplementaryCodeValue = SupplementaryCodeValue.allChargingSS;
                break;
            case 30:
                supplementaryCodeValue = SupplementaryCodeValue.aoci;
                break;
            case 31:
                supplementaryCodeValue = SupplementaryCodeValue.aocc;
                break;
            case 32:
                supplementaryCodeValue = SupplementaryCodeValue.allAdditionalInfoTransferSS;
                break;
            case 33:
                supplementaryCodeValue = SupplementaryCodeValue.uus1;
                break;
            case 34:
                supplementaryCodeValue = SupplementaryCodeValue.uus2;
                break;
            case 35:
                supplementaryCodeValue = SupplementaryCodeValue.uus3;
                break;
            case 36:
                supplementaryCodeValue = SupplementaryCodeValue.allCallRestrictionSS;
                break;
            case 37:
                supplementaryCodeValue = SupplementaryCodeValue.barringOfOutgoingCalls;
                break;
            case 38:
                supplementaryCodeValue = SupplementaryCodeValue.baoc;
                break;
            case 39:
                supplementaryCodeValue = SupplementaryCodeValue.boic;
                break;
            case 40:
                supplementaryCodeValue = SupplementaryCodeValue.boicExHC;
                break;
            case 41:
                supplementaryCodeValue = SupplementaryCodeValue.barringOfIncomingCalls;
                break;
            case 42:
                supplementaryCodeValue = SupplementaryCodeValue.baic;
                break;
            case 43:
                supplementaryCodeValue = SupplementaryCodeValue.bicRoam;
                break;
            case 44:
                supplementaryCodeValue = SupplementaryCodeValue.allPLMN_specificSS;
                break;
            case 45:
                supplementaryCodeValue = SupplementaryCodeValue.allCallPrioritySS;
                break;
            case 46:
                supplementaryCodeValue = SupplementaryCodeValue.emlpp;
                break;
            case 47:
                supplementaryCodeValue = SupplementaryCodeValue.allLCSPrivacyException;
                break;
            case 48:
                supplementaryCodeValue = SupplementaryCodeValue.universal;
                break;
            case 49:
                supplementaryCodeValue = SupplementaryCodeValue.callrelated;
                break;
            case 50:
                supplementaryCodeValue = SupplementaryCodeValue.callunrelated;
                break;
            case 51:
                supplementaryCodeValue = SupplementaryCodeValue.plmnoperator;
                break;
            case 52:
                supplementaryCodeValue = SupplementaryCodeValue.serviceType;
                break;
            case 53:
                supplementaryCodeValue = SupplementaryCodeValue.allMOLR_SS;
                break;
            case 54:
                supplementaryCodeValue = SupplementaryCodeValue.basicSelfLocation;
                break;
            case 55:
                supplementaryCodeValue = SupplementaryCodeValue.autonomousSelfLocation;
                break;
            case 56:
                supplementaryCodeValue = SupplementaryCodeValue.transferToThirdParty;
                break;
            /*case 57:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_1;
                break;
            case 58:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_2;
                break;
            case 59:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_3;
                break;
            case 60:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_4;
                break;
            case 61:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_5;
                break;
            case 62:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_6;
                break;
            case 63:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_7;
                break;
            case 64:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_8;
                break;
            case 65:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_9;
                break;
            case 66:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_a;
                break;
            case 67:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_b;
                break;
            case 68:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_c;
                break;
            case 69:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_d;
                break;
            case 70:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_e;
                break;
            case 71:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_f;
                break;*/
            default:
                supplementaryCodeValue = SupplementaryCodeValue.allSS;
                break;
        }
        return supplementaryCodeValue;
    }

    /*
     * MSC SCCP Address creation
     */
    private SccpAddress getMSCSCCPAddress(String mscAddress) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(mscAddress, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int mscSsn = 8;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, mscSsn);
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
