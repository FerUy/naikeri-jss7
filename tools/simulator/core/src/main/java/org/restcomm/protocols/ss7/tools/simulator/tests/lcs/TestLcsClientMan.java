package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoSClass;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingOptionMilliseconds;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;

import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;

import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientNameImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LocationTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SupportedGADShapesImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSCodewordImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSQoSImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ResponseTimeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSPrivacyCheckImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AreaIdentificationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AreaDefinitionImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AreaEventInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AreaImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ReportingPLMNImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ReportingPLMNListImpl;

import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;

import org.restcomm.protocols.ss7.tools.simulator.Stoppable;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.common.TesterBase;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapMan;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.management.TesterHostImpl;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class TestLcsClientMan extends TesterBase implements TestLcsClientManMBean, Stoppable, MAPServiceLsmListener {

    private static final Logger logger = LogManager.getLogger(TestLcsClientMan.class);

    public static String SOURCE_NAME = "TestLcsClientMan";
    private final String name;
    private MapMan mapMan;
    private boolean isStarted = false;
    private MAPProvider mapProvider;
    private MAPServiceLsm mapServiceLsm;
    private MAPParameterFactory mapParameterFactory;
    private int countMapLcsReq = 0;
    private int countMapLcsResp = 0;
    private String currentRequestDef = "";

    public TestLcsClientMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        mapServiceLsm = mapProvider.getMAPServiceLsm();
        this.mapParameterFactory = mapProvider.getMAPParameterFactory();
        mapServiceLsm.activate();
        mapServiceLsm.addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);

        isStarted = true;

        return true;
    }

    public void setTesterHost(TesterHostImpl testerHost) {
        this.testerHost = testerHost;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String getState() {
        return "<html>" +
            SOURCE_NAME +
            ": " +
            "<br>Count: countMapLcsReq-" +
            countMapLcsReq +
            ", countMapLcsResp-" +
            countMapLcsResp +
            "</html>";
    }

    @Override
    public void execute() {
    }

    @Override
    public void stop() {
    }

    //***************************//
    //**** SRILCS methods ***//
    //*************************//
    @Override
    public String performSendRoutingInfoForLCSRequest() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSRequest();
    }

    public String sendRoutingInfoForLCSRequest() {

        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                    MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm mapDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                    this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            TestLcsClientConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsClientConfigurationData();
            ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "3797554321");
            SubscriberIdentity targetMS = new SubscriberIdentityImpl(isdnAdd);

            ISDNAddressString mlcAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "222333");

            mapDialogLsm.addSendRoutingInfoForLCSRequest(mlcAddress, targetMS, null);
            logger.debug("Added SRIforLCS msisdn:" + targetMS + ", sriForLCSIsdnAddress:" + mlcAddress);

            mapDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SRIforLCS", createSRILCSReqData(mapDialogLsm.getLocalDialogId(), mlcAddress.getAddress(),
                    targetMS.getMSISDN().getAddress()), Level.INFO);

            currentRequestDef += "Sent SRILCS Request;";

        } catch (MAPException e) {
            return "Exception " + e;
        }

        return "sendRoutingInfoForLCSRequest sent";
    }


    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInforForLCSRequestIndication) {
        logger.debug("onSendRoutingInfoForLCSRequest");

        this.countMapLcsReq++;

        if (!isStarted)
            return;

        MAPParameterFactory mapParameterFactory = this.mapProvider.getMAPParameterFactory();

        ISDNAddressString mlc = sendRoutingInforForLCSRequestIndication.getMLCNumber();
        SubscriberIdentity targetMS = sendRoutingInforForLCSRequestIndication.getTargetMS();

        MAPDialogLsm curDialog = sendRoutingInforForLCSRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SendRoutingInfoForLCSRequest",
                createSRILCSReqData(curDialog.getLocalDialogId(),
                        mlc.getAddress(),
                        targetMS.getMSISDN().getAddress()), Level.INFO);

        ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),
                getNetworkNodeNumber());

        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, networkNodeNumber.getAddress());
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
        LMSI lmsi;
        Random rand = new Random();
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
        LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, lmsi, null, gprsNodeIndicator,
                additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

        try {
            GSNAddress vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
            GSNAddress pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x12 });
            GSNAddress additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });

            curDialog.addSendRoutingInfoForLCSResponse(
                    sendRoutingInforForLCSRequestIndication.getInvokeId(),
                    targetMS,
                    lcsLocationInfo,
                    null,
                    vGmlcAddress,
                    hGmlcAddress,
                    pprAddress,
                    additionalVGmlcAddress);
            logger.debug("set addSendRoutingInfoForLCSResponse");
            curDialog.send();
            logger.debug("addSendRoutingInfoForLCSResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSResponse",
                    createSRILCSResData(curDialog.getLocalDialogId(), networkNodeNumber.getAddress()), Level.INFO);

        } catch (MAPException e) {
            logger.debug("Failed building response " + e);
        }

    }

    private String createSRILCSReqData(long dialogId, String address, String msisdn) {
        return "dialogId=" +
            dialogId +
            ", GMLC address=\"" +
            address +
            "\"" +
            ", MSISDN=\"" +
            msisdn +
            "\"";
    }

    private String createSRILCSResData(long dialogId, String address) {
        return "dialogId=" +
            dialogId +
            ", networkNodeNumber=\"" +
            address +
            "\"";
    }

    private String createSLRReqData(Long dialogId, String networkNodeNumberAddress) {
        return "dialogId=" +
            dialogId +
            ", networkNodeNumber=\"" +
            networkNodeNumberAddress +
            "\"";
    }

    private String createSLRReqData(long dialogId, LCSEvent lcsEvent, String address, LCSClientID lcsClientID, ISDNAddressString msisdn, IMSI imsi, IMEI imei,
                                    ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate, Integer lcsReferenceNumber,
                                    CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress) {
        return "dialogId=" +
            dialogId +
            ", lcsEvent=\"" +
            lcsEvent +
            "\", networkNodeNumber=\"" +
            address + ", " +
            "\", lcsClientID=\"" +
            lcsClientID + ", " +
            "\", MSISDN=\"" +
            msisdn.getAddress() + ", " +
            "\", IMSI=\"" +
            imsi.getData() + ", " +
            "\", IMEI=\"" +
            imei.getIMEI() + ", " +
            "\", locationEstimate=\"" +
            locationEstimate + ", " +
            "\", latitude=\"" +
            locationEstimate.getLatitude() + ", " +
            "\", longitude=\"" +
            locationEstimate.getLongitude() + ", " +
            "\", altitude=\"" +
            locationEstimate.getAltitude() + ", " +
            "\", locationEstimateConfidence=\"" +
            locationEstimate.getConfidence() + ", " +
            "\", locationEstimateInnerRadius=\"" +
            locationEstimate.getInnerRadius() + ", " +
            "\", locationEstimateTypeOfShape=\"" +
            locationEstimate.getTypeOfShape() + ", " +
            "\", ageOfLocationEstimate=\"" +
            ageOfLocationEstimate +
            "\", lcsReferenceNumber=\"" +
            lcsReferenceNumber + "\", " +
            cellIdOrSai + ", " +
            hgmlcAddress;
    }

    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInforForLCSResponseIndication) {
        logger.debug("onSendRoutingInfoForLCSResponse");

    }


    //*********************//
    //**** PSL methods ***//
    //*******************//
    @Override
    public String performProvideSubscriberLocationRequest() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return ProvideSubscriberLocationRequest();
    }

    private String ProvideSubscriberLocationRequest() {
        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                    MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm mapDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                    this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            //TestLcsClientConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsClientConfigurationData();

            // Then, create parameters for concerning MAP operation
            ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "3797554321");
            ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "222333");
            LocationEstimateType locationEstimateType = LocationEstimateType.currentLocation;
            DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl();
            deferredLocationEventType.getEnteringIntoArea();
            LocationType locationType = new LocationTypeImpl(locationEstimateType, deferredLocationEventType);
            ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "340444567");
            MAPExtensionContainer mapExtensionContainer = null;
            LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, mapExtensionContainer);
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.anonymousLocation;
            String clientName = "340012";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "*911#";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            byte[] apn = new BigInteger("8877665544", 16).toByteArray();
            APN lcsAPN = new APNImpl(apn);
            LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, null);
            boolean privacyOverride = false;
            IMSI imsi = new IMSIImpl("124356871012345");
            String lmsiId = "4321";
            byte[] lmsid = lmsiId.getBytes();
            LMSI lmsi = new LMSIImpl(lmsid);
            IMEI imei = new IMEIImpl("01171400466105");
            LCSPriority lcsPriority = LCSPriority.normalPriority;
            boolean ellipsoidPoint = true;
            boolean ellipsoidPointWithUncertaintyCircle = true;
            boolean ellipsoidPointWithUncertaintyEllipse = true;
            boolean polygon = false;
            boolean ellipsoidPointWithAltitude = false;
            boolean ellipsoidPointWithAltitudeAndUncertaintyEllipsoid = false;
            boolean ellipsoidArc = false;
            SupportedGADShapes supportedGADShapes = new SupportedGADShapesImpl(ellipsoidPoint, ellipsoidPointWithUncertaintyCircle,
                    ellipsoidPointWithUncertaintyEllipse, polygon, ellipsoidPointWithAltitude, ellipsoidPointWithAltitudeAndUncertaintyEllipsoid, ellipsoidArc);
            Integer lcsReferenceNumber = 379;
            Integer lcsServiceTypeID = 1;
            // DataCodingScheme codingScheme = new DataCodingSchemeImpl(1);
            USSDString lcsCodewordString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSCodeword lcsCodeword = new LCSCodewordImpl(cbsDataCodingScheme, lcsCodewordString);
            Integer horizontalAccuracy = 100;
            Integer verticalAccuracy = 1000;
            boolean verticalCoordinateRequest = false;
            ResponseTimeCategory responseTimeCategory = ResponseTimeCategory.delaytolerant;
            ResponseTime responseTime = new ResponseTimeImpl(responseTimeCategory);
            MAPExtensionContainer extensionContainer = null;
            boolean velocityRequested = false;
            LCSQoSClass lcsQoSClass = null;
            LCSQoS lcsQoS = new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, extensionContainer, velocityRequested, lcsQoSClass);
            PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithNotification;
            PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;
            LCSPrivacyCheck lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
            ArrayList<Area> areaList = new ArrayList<>();
            AreaType areaType = AreaType.locationAreaId;
            String aId = "102132";
            byte[] areaId = aId.getBytes();
            AreaIdentification areaIdentification = new AreaIdentificationImpl(areaId);
            Area area1 = new AreaImpl(areaType, areaIdentification);
            areaList.add(area1);
            AreaDefinition areaDefinition = new AreaDefinitionImpl(areaList);
            OccurrenceInfo occurrenceInfo = OccurrenceInfo.oneTimeEvent;
            Integer intervalTime = 10;
            AreaEventInfo areaEventInfo = new AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);
            byte[] homeGmlcAddress = new BigInteger("999988887777", 16).toByteArray();
            GSNAddress hGmlcAddress = new GSNAddressImpl(homeGmlcAddress);
            boolean moLrShortCircuitIndicator = false;
            int reportingAmount = 3;
            int reportingInterval = 60;
            ReportingOptionMilliseconds reportingOptionMilliseconds = null;
            PeriodicLDRInfo periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, reportingOptionMilliseconds);
            boolean plmnListPrioritized = false;
            ArrayList<ReportingPLMN> plmnList = new ArrayList<>();
            String plmnIdstr = "321";
            byte[] plmnID = plmnIdstr.getBytes();
            PlmnId plmnId = new PlmnIdImpl(plmnID);
            RANTechnology ranTechnology = RANTechnology.umts;
            boolean ranPeriodicLocationSupport = true;
            ReportingPLMN reportingPLMN = new ReportingPLMNImpl(plmnId, ranTechnology, ranPeriodicLocationSupport);
            plmnList.add(reportingPLMN);
            ReportingPLMNList reportingPLMNList = new ReportingPLMNListImpl(plmnListPrioritized, plmnList);

            mapDialogLsm.addProvideSubscriberLocationRequest(locationType, mlcNumber, lcsClientID, privacyOverride, imsi, msisdn, lmsi, imei, lcsPriority,
                    lcsQoS, extensionContainer, supportedGADShapes, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword,
                    lcsPrivacyCheck, areaEventInfo, hGmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo, reportingPLMNList);

            logger.debug("Added ProvideSubscriberLocationRequest");

            mapDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ProvideSubscriberLocationRequest", createPSLRequestData(mapDialogLsm.getLocalDialogId(),
                    locationType, mlcNumber, lcsClientID, imsi, msisdn, lmsi, lcsPriority, lcsQoS, imei, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck,
                    areaEventInfo, hGmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception " + e;
        }

        return "subscriberLocationReportRequest sent";

    }

    private String createPSLRequestData(long dialogId,
                                        LocationType locationType,
                                        ISDNAddressString mlcNumber,
                                        LCSClientID lcsClientID,
                                        IMSI imsi,
                                        ISDNAddressString msisdn,
                                        LMSI lmsi,
                                        LCSPriority lcsPriority,
                                        LCSQoS lcsQoS,
                                        IMEI imei,
                                        Integer lcsReferenceNumber,
                                        Integer lcsServiceTypeID,
                                        LCSCodeword lcsCodeword,
                                        LCSPrivacyCheck lcsPrivacyCheck,
                                        AreaEventInfo areaEventInfo,
                                        GSNAddress hgmlcAddress,
                                        boolean moLrShortCircuitIndicator,
                                        PeriodicLDRInfo periodicLDRInfo) {
        return "dialogId=" +
            dialogId + "\",\n " +
            "locationType=\"" +
            locationType + "\",\n " +
            "locationEstimateType=\"" +
            locationType.getLocationEstimateType().getType() + "\",\n " +
            "deferredLocationEventType=\"" +
            locationType.getDeferredLocationEventType() + "\",\n " +
            "mlcNumber=\"" +
            mlcNumber.getAddress() + "\",\n " +
            "lcsClientID=\"" +
            lcsClientID + "\",\n " +
            "IMSI=\"" +
            imsi.getData() + "\",\n " +
            "MSISDN=\"" +
            msisdn.getAddress() + "\",\n " +
            "LMSI=\"" +
            lmsi + "\",\n " +
            "lcsPriority=\"" +
            lcsPriority + "\",\n " +
            "lcsQos=\"" +
            lcsQoS + "\",\n " +
            "lcsQosHorizontalAccuracy=\"" +
            lcsQoS.getHorizontalAccuracy() + "\",\n " +
            "lcsQosVerticalAccuracy=\"" +
            lcsQoS.getVerticalAccuracy() + "\",\n " +
            "lcsQosResponseTimeCategory=\"" +
            lcsQoS.getResponseTime().getResponseTimeCategory() + "\",\n " +
            "lcsQosVerticalCoordinateRequest=\"" +
            lcsQoS.getVerticalCoordinateRequest() + "\",\n " +
            "IMEI=\"" +
            imei + "\",\n " +
            "lcsReferenceNumber=\"" +
            lcsReferenceNumber + "\",\n " +
            "lcsServiceTypeID=\"" +
            lcsServiceTypeID + "\",\n " +
            "lcsCodeword=\"" +
            lcsCodeword + "\",\n " +
            "lcsPrivacyCheck=\"" +
            lcsPrivacyCheck + "\",\n " +
            "areaEventInfo=\"" +
            areaEventInfo + "\",\n " +
            "areaId=\"" +
            areaEventInfo.getAreaDefinition().getAreaList().get(0) + "\",\n " +
            "occurrenceInfo=\"" +
            areaEventInfo.getOccurrenceInfo().getInfo() + "\",\n " +
            "intervalTime=\"" +
            areaEventInfo.getIntervalTime() + "\",\n " +
            "hgmlcAddress=\"" +
            hgmlcAddress + "\",\n " +
            "moLrShortCircuitIndicator=\"" +
            moLrShortCircuitIndicator + "\",\n " +
            "periodicLDRInfo=\"" +
            periodicLDRInfo;
    }

    private String createPSLResponse(long dialogId, ExtGeographicalInformation locationEstimate) {
        return "dialogId=" +
            dialogId + "\",\n " +
            "locationEstimate=\"" +
            locationEstimate + "\"";
    }

    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) {

        logger.debug("onProvideSubscriberLocationRequest");
        if (!isStarted)
            return;

        this.countMapLcsReq++;

        MAPDialogLsm curDialog = provideSubscriberLocationRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: ProvideSubscriberLocationRequest",
                createPSLRequest(curDialog.getLocalDialogId(),
                        provideSubscriberLocationRequestIndication.getLocationType(),
                        provideSubscriberLocationRequestIndication.getMlcNumber(),
                        provideSubscriberLocationRequestIndication.getLCSClientID(),
                        provideSubscriberLocationRequestIndication.getIMSI(),
                        provideSubscriberLocationRequestIndication.getMSISDN(),
                        provideSubscriberLocationRequestIndication.getIMEI(),
                        provideSubscriberLocationRequestIndication.getLCSReferenceNumber(),
                        provideSubscriberLocationRequestIndication.getLCSServiceTypeID(),
                        provideSubscriberLocationRequestIndication.getLCSCodeword(),
                        provideSubscriberLocationRequestIndication.getLCSPrivacyCheck(),
                        provideSubscriberLocationRequestIndication.getAreaEventInfo(),
                        provideSubscriberLocationRequestIndication.getHGMLCAddress(),
                        provideSubscriberLocationRequestIndication.getMoLrShortCircuitIndicator(),
                        provideSubscriberLocationRequestIndication.getPeriodicLDRInfo()
                ), Level.INFO);

        PositioningDataInformation geranPositioningData = null;
        UtranPositioningDataInfo utranPositioningData = null;
        Integer ageOfLocationEstimate = 0;
        AddGeographicalInformation additionalLocationEstimate = null;
        MAPExtensionContainer extensionContainer = null;
        boolean deferredMTLRResponseIndicator = false;
        CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = null;
        boolean saiPresent = false;
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;
        VelocityEstimate velocityEstimate = null;
        boolean moLrShortCircuitIndicator = false;
        GeranGANSSpositioningData geranGANSSpositioningData = null;
        UtranGANSSpositioningData utranGANSSpositioningData = null;
        ServingNodeAddress targetServingNodeForHandover = null;
        UtranAdditionalPositioningData utranAdditionalPositioningData = null;
        Integer utranBaroPressureMeas = null;
        UtranCivicAddress utranCivicAddress = null;

        try {
            ExtGeographicalInformation locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(34.790000, -124.910000);

            curDialog.addProvideSubscriberLocationResponse(
                    provideSubscriberLocationRequestIndication.getInvokeId(),
                    locationEstimate,
                    geranPositioningData,
                    utranPositioningData,
                    ageOfLocationEstimate,
                    additionalLocationEstimate,
                    extensionContainer,
                    deferredMTLRResponseIndicator,
                    cellGlobalIdOrServiceAreaIdOrLAI,
                    saiPresent,
                    accuracyFulfilmentIndicator,
                    velocityEstimate,
                    moLrShortCircuitIndicator,
                    geranGANSSpositioningData,
                    utranGANSSpositioningData,
                    targetServingNodeForHandover,
                    utranAdditionalPositioningData,
                    utranBaroPressureMeas,
                    utranCivicAddress);

            logger.debug("set addProvideSubscriberLocationResponse");
            curDialog.send();
            logger.debug("addProvideSubscriberLocationResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingInfoForLCSResponse",
                    createPSLResponse(curDialog.getLocalDialogId(),
                            locationEstimate), Level.INFO);

        } catch (MAPException e) {
            logger.debug("Failed building  SendRoutingInfoForLCS response " + e);
        }
    }

    private String createPSLRequest(
            long dialogId,
            LocationType locationType,
            ISDNAddressString mlcNumber,
            LCSClientID lcsClientID,
            IMSI imsi,
            ISDNAddressString msisdn,
            IMEI imei,
            Integer lcsReferenceNumber,
            Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword,
            LCSPrivacyCheck lcsPrivacyCheck,
            AreaEventInfo areaEventInfo,
            GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator,
            PeriodicLDRInfo periodicLDRInfo) {
        return "dialogId=" +
            dialogId + "\",\n " +
            "locationType=\"" +
            locationType + "\",\n " +
            "mlcNumber=\"" +
            mlcNumber.getAddress() + "\",\n " +
            "lcsClientID=\"" +
            lcsClientID + "\",\n " +
            "imsi=\"" +
            imsi + "\",\n " +
            "msisdn=\"" +
            msisdn.getAddress() + "\",\n " +
            "imei=\"" +
            imei + "\",\n " +
            "lcsReferenceNumber=\"" +
            lcsReferenceNumber + "\",\n " +
            "lcsServiceTypeID=\"" +
            lcsServiceTypeID + "\",\n " +
            "lcsCodeword=\"" +
            lcsCodeword + "\",\n " +
            "lcsPrivacyCheck=\"" +
            lcsPrivacyCheck + "\",\n " +
            "areaEventInfo=\"" +
            areaEventInfo + "\",\n " +
            "hgmlcAddress=\"" +
            hgmlcAddress + "\",\n " +
            "moLrShortCircuitIndicator=\"" +
            moLrShortCircuitIndicator + "\",\n " +
            "periodicLDRInfo=\"" +
            periodicLDRInfo;
    }

    @Override
    public void onProvideSubscriberLocationResponse(
            ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) {
        if (!isStarted) {
            Configurator.initialize(new DefaultConfiguration());
            Configurator.setRootLevel(Level.INFO);
            String msg = "The tester is not started";
            logger.trace(msg);
        }
    }


    //*********************//
    //**** SLR methods ***//
    //*******************//
    public String performSubscriberLocationReportResponse() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportResponse();
    }

    public String subscriberLocationReportResponse() {

        try {
            // MAP LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext, MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm clientDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                    this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            //TestLcsServerConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData();

            // Create Routing Information parameters for concerning MAP operation
            ISDNAddressString naEsrd = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "5982123007");
            ISDNAddressString naEsrk = null; //new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5982123009");

            MAPExtensionContainer mapExtensionContainer = null;
            GSNAddress hGmlcAddress = null;
            boolean molrShortCircuitIndicator = false;
            ReportingPLMNList reportingPLMNList = null;
            Integer lcsReferenceNumber = null;

            clientDialogLsm.addSubscriberLocationReportResponse(clientDialogLsm.getLocalDialogId(), naEsrd, naEsrk, mapExtensionContainer, hGmlcAddress,
                    molrShortCircuitIndicator, reportingPLMNList, lcsReferenceNumber);

            logger.debug("Added SubscriberLocationReportResponse");

            clientDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportResponse", createSLRResData(clientDialogLsm.getLocalDialogId(),
                    naEsrd.toString(), null), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception " + e;
        }

        return "subscriberLocationReportResponse sent";

    }

    public String performSubscriberLocationReportRequest() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportRequest();
    }

    private String subscriberLocationReportRequest() {
        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                    MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm clientDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                    this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            TestLcsServerConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData();

            // SLR Mandatory parameters LCSEvent, LCSClientID & Network Node Number
            MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
            LCSEvent lcsEvent = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent();

            LCSClientExternalID lcsClientExternalID = null;
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.anonymousLocation;
            String clientName = "340012";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "*911#";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            byte[] apn = new BigInteger("8877665544", 16).toByteArray();
            APN lcsAPN = new APNImpl(apn);
            LCSRequestorID lcsRequestorID = null;
            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(configData.getLcsClientType(), lcsClientExternalID, lcsClientInternalID,
                    lcsClientName, lcsClientDialedByMS, lcsAPN, lcsRequestorID);

            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumber());

            // SLR TC-user optional parameters
            IMEI imei = mapParameterFactory.createIMEI(getIMEI());

            String lmsiStr = "4321";
            byte[] Lmsi = lmsiStr.getBytes();
            LMSI lmsi = mapFactory.createLMSI(Lmsi);

            // LSR Conditional parameters
            IMSI imsi = mapParameterFactory.createIMSI(getIMSI());

            ISDNAddressString msisdn = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getMSISDN());

            TypeOfShape typeOfShape = TypeOfShape.EllipsoidArc;
            double latitude = 34.790000;
            double longitude = -124.910000;
            double uncertainty = 100.0;
            double uncertaintySemiMajorAxis = 50.0;
            double uncertaintySemiMinorAxis = 30.0;
            double angleOfMajorAxis = 10.0;
            int confidence = 5;
            int altitude = 1000;
            double uncertaintyAltitude = 200.0;
            int innerRadius = 40;
            double uncertaintyRadius = 5.00;
            double offsetAngle = 10.0;
            double includedAngle = 30.0;
            ExtGeographicalInformation locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis,
                    uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle, includedAngle);

            boolean gprsNodeIndicator = false;

            GSNAddress hgmlcAddress = createGSNAddress(getHGMLCAddress());

            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;

            ISDNAddressString naEsrd = null;
            ISDNAddressString naEsrk = null;
            SLRArgExtensionContainer slrArgExtensionContainer = null;

            CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = mapParameterFactory.createCellGlobalIdOrServiceAreaIdFixedLength(this.getMCC(), this.getMNC(), this.getLAC(), this.getCellId());
            CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = mapParameterFactory.createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);

            MAPExtensionContainer extensionContainer = null;
            AddGeographicalInformation additionalLocationEstimate = null;

            PositioningDataInformation geranPositioningData = null;
            UtranPositioningDataInfo utranPositioningDataInfo = null;
            Integer lcsServiceTypeID = 1;
            boolean saiPresent = false;
            boolean pseudonymIndicator = false;
            String velStr = "00001";
            byte[] velEstimate = velStr.getBytes();
            VelocityEstimate velocityEstimate = new VelocityEstimateImpl(velEstimate);
            Integer sequenceNumber = 0;
            PeriodicLDRInfo periodicLDRInfo = mapParameterFactory.createPeriodicLDRInfo(10, 60, null);
            boolean moLrShortCircuitIndicator = false;
            GeranGANSSpositioningData geranGANSSpositioningData = null;
            UtranGANSSpositioningData utranGANSSpositioningData = null;
            ServingNodeAddress targetNodeForHandover = null;
            AdditionalNumber additionalNumber = null;
            boolean lcsCapabilitySetRelease98_99 = true;
            boolean lcsCapabilitySetRelease4 = true;
            boolean lcsCapabilitySetRelease5 = true;
            boolean lcsCapabilitySetRelease6 = true;
            boolean lcsCapabilitySetRelease7 = true;
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            byte[] mme = new BigInteger("8720c00a30a1743401000a", 16).toByteArray();
            DiameterIdentity mmeName = new DiameterIdentityImpl(mme);
            byte[] aaa = new BigInteger("8720c00a30a1743401101112", 16).toByteArray();
            DiameterIdentity aaaServerName = new DiameterIdentityImpl(aaa);
            DiameterIdentity sgsnName = null;
            DiameterIdentity sgsnRealm = null;

            LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, lmsi, extensionContainer, gprsNodeIndicator,
                    additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            boolean msAvailable = false;
            boolean enteringIntoArea = false;
            boolean leavingFromArea = false;
            boolean beingInsideArea = true;
            boolean periodicLDR = false;
            DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
            TerminationCause terminationCause = TerminationCause.congestion;
            DeferredmtlrData deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
            UtranAdditionalPositioningData utranAdditionalPositioningData = null;
            Integer utranBaroPressureMeas = null;
            UtranCivicAddress utranCivicAddress = null;

            clientDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo,
                    msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, getAgeOfLocationEstimate(), slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData,
                    getLCSReferenceNumber(), geranPositioningData, utranPositioningDataInfo, cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator,
                    accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData,
                    utranGANSSpositioningData, targetNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);
            logger.debug("Added SubscriberLocationReportRequest");

            clientDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportRequest", createSLRReqData(clientDialogLsm.getLocalDialogId(), lcsEvent,
                    this.getNetworkNodeNumber(), lcsClientID, msisdn, imsi, imei, locationEstimate, getAgeOfLocationEstimate(), getLCSReferenceNumber(),
                    deferredmtlrData, cellIdOrSai, hgmlcAddress, accuracyFulfilmentIndicator), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception " + e;
        }

        return "subscriberLocationReportRequest sent";
    }

    private String createSLRReqData(long dialogId, LCSEvent lcsEvent, String networkNodeNumber, LCSClientID lcsClientID, ISDNAddressString msisdn, IMSI imsi,
                                    IMEI imei, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate, Integer lcsReferenceNumber,
                                    DeferredmtlrData deferredmtlrData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress,
                                    AccuracyFulfilmentIndicator accuracyFulfilmentIndicator) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", lcsEvent=\"");
        sb.append(lcsEvent.getEvent());
        sb.append("\", networkNodeNumber=\"");
        sb.append(networkNodeNumber).append(", ");
        sb.append("\", lcsClientID=\"");
        sb.append(lcsClientID).append(", ");
        sb.append("\", MSISDN=\"");
        sb.append(msisdn.getAddress()).append(", ");
        sb.append("\", IMSI=\"");
        sb.append(imsi.getData()).append(", ");
        sb.append("\", IMEI=\"");
        sb.append(imei).append(", ");
        sb.append("\", locationEstimate=\"");
        sb.append(locationEstimate).append(", ");
        sb.append("\", latitude=\"");
        sb.append(locationEstimate.getLatitude()).append(", ");
        sb.append("\", longitude=\"");
        sb.append(locationEstimate.getLongitude()).append(", ");
        sb.append("\", altitude=\"");
        sb.append(locationEstimate.getAltitude()).append(", ");
        sb.append("\", locationEstimateConfidence=\"");
        sb.append(locationEstimate.getConfidence()).append(", ");
        sb.append("\", locationEstimateInnerRadius=\"");
        sb.append(locationEstimate.getInnerRadius()).append(", ");
        sb.append("\", locationEstimateTypeOfShape=\"");
        sb.append(locationEstimate.getTypeOfShape()).append(", ");
        sb.append("\", ageOfLocationEstimate=\"");
        sb.append(ageOfLocationEstimate);
        sb.append("\", lcsReferenceNumber=\"");
        sb.append(lcsReferenceNumber).append("\", ");
        sb.append("\", deferredmtlrData=\"");
        sb.append(deferredmtlrData).append(", ");
        sb.append("\", MCC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC())).append(", ");
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        sb.append("\", MNC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC())).append(", ");
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        sb.append("\", LAC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac())).append(", ");
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        sb.append("\", LAC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode())).append(", ");
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        sb.append("\", H-GMLCAddress=\"");
        sb.append(hgmlcAddress);
        sb.append("\", accuracyFulfilmentIndicator=\"");
        sb.append(accuracyFulfilmentIndicator);
        sb.append("\", reportingPLMNList=\"");
        /*for (int i = 0; i < reportingPLMNList.getPlmnList().size(); i++) {
            sb.append(reportingPLMNList.getPlmnList().get(i)).append(", ");
        }*/

        return sb.toString();
    }

    private String createSLRResData(long dialogId, String naESRD, String naESRK) {
        return "dialogId=" +
            dialogId +
            ", naESRD=\"" +
            naESRD +
            ", naESRK=\"" +
            naESRK +
            "\"";
    }

    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {
        logger.debug("onSubscriberLocationReportRequest");
        this.countMapLcsReq++;
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = subscriberLocationReportRequestIndication.getMAPDialog();
        String networkNodeNumberAddress = subscriberLocationReportRequestIndication.getLCSLocationInfo().getNetworkNodeNumber().getAddress();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SubscriberLocationReportRequest",
                createSLRReqData(curDialog.getLocalDialogId(), subscriberLocationReportRequestIndication.getLCSEvent(),
                        networkNodeNumberAddress,
                        subscriberLocationReportRequestIndication.getLCSClientID(),
                        subscriberLocationReportRequestIndication.getMSISDN(),
                        subscriberLocationReportRequestIndication.getIMSI(),
                        subscriberLocationReportRequestIndication.getIMEI(),
                        subscriberLocationReportRequestIndication.getLocationEstimate(),
                        subscriberLocationReportRequestIndication.getAgeOfLocationEstimate(),
                        subscriberLocationReportRequestIndication.getLCSReferenceNumber(),
                        subscriberLocationReportRequestIndication.getDeferredmtlrData(),
                        subscriberLocationReportRequestIndication.getCellGlobalIdOrServiceAreaIdOrLAI(),
                        subscriberLocationReportRequestIndication.getHGMLCAddress(),
                        subscriberLocationReportRequestIndication.getAccuracyFulfilmentIndicator()), Level.INFO);

        ISDNAddressString naEsrd = null;
        ISDNAddressString naEsrk = null;
        if (subscriberLocationReportRequestIndication.getNaESRD() != null) {
            naEsrd = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, subscriberLocationReportRequestIndication.getNaESRD().getAddress());
        } else if (subscriberLocationReportRequestIndication.getNaESRK() != null) {
            naEsrk = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, subscriberLocationReportRequestIndication.getNaESRK().getAddress());
        }
        GSNAddress hGmlcAddress = subscriberLocationReportRequestIndication.getHGMLCAddress();
        boolean molrShortCircuitIndicator = true;
        ReportingPLMNList reportingPLMNList = null;
        Integer lcsReferenceNumber = subscriberLocationReportRequestIndication.getLCSReferenceNumber();

        try {
            curDialog.addSubscriberLocationReportResponse(subscriberLocationReportRequestIndication.getInvokeId(), naEsrd, naEsrk, null,
                    hGmlcAddress, molrShortCircuitIndicator, reportingPLMNList, lcsReferenceNumber);
            logger.debug("\nset addSubscriberLocationReportResponse");
            curDialog.send();
            logger.debug("\naddSubscriberLocationReportResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportResponse",
                    createSLRResData(curDialog.getLocalDialogId(), getNaESRDAddress(), getNaESRKAddress()), Level.INFO);

        } catch (MAPException e) {
            logger.debug("Failed building response " + e);
        }
    }

    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {
        logger.debug("onSubscriberLocationReportResponse");
        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SubscriberLocationReportResponse", this
                        .createSLRResData(
                                subscriberLocationReportResponseIndication
                                        .getInvokeId(),
                                subscriberLocationReportResponseIndication
                                        .getNaESRD().toString(), subscriberLocationReportResponseIndication
                                        .getNaESRK().toString()), Level.INFO);
    }

    private static AreaDefinition getAreaDefinition(int areaRand) throws MAPException {
        ArrayList<Area> areaList = new ArrayList<>();
        AreaType areaType;
        AreaIdentification areaIdentification;
        Area area1, area2, area3;

        switch (areaRand) {
            case 1:
                areaType = AreaType.countryCode;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 0, 0, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 2:
                areaType = AreaType.plmnId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 0, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 3:
                areaType = AreaType.locationAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 1201, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 4:
                areaType = AreaType.routingAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 102, 1263);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 5:
                areaType = AreaType.cellGlobalId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 104, 32047);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 6:
                areaType = AreaType.utranCellId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 7, 0, 134283263);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                break;
            case 7:
                areaType = AreaType.countryCode;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 0, 0, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.locationAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 1201, 0);
                area2 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                areaList.add(area2);
                break;
            case 8:
                areaType = AreaType.locationAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 1201, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.utranCellId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 7, 0, 134283263);
                area2 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                areaList.add(area2);
                break;
            case 9:
                areaType = AreaType.routingAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 102, 1263);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.cellGlobalId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 104, 32047);
                area2 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.utranCellId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 7, 0, 134283263);
                area3 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                areaList.add(area2);
                areaList.add(area3);
                break;
            case 10:
                areaType = AreaType.plmnId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 0, 0);
                area1 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.locationAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 1201, 0);
                area2 = new AreaImpl(areaType, areaIdentification);
                areaType = AreaType.routingAreaId;
                areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 102, 1263);
                area3 = new AreaImpl(areaType, areaIdentification);
                areaList.add(area1);
                areaList.add(area2);
                areaList.add(area3);
                break;
        }
        return new AreaDefinitionImpl(areaList);
    }

    private static ReportingPLMNList getReportingPLMNList() {
        ArrayList<ReportingPLMN> reportingPLMNs = new ArrayList<>();
        PlmnId plmnId1 = new PlmnIdImpl(748, 1);
        RANTechnology rat1 = RANTechnology.umts;
        boolean ranPeriodicLocationSupport1 = true;
        PlmnId plmnId2 = new PlmnIdImpl(748, 7);
        RANTechnology rat2 = RANTechnology.gsm;
        boolean ranPeriodicLocationSupport2 = false;
        ReportingPLMN rPlmn1 = new ReportingPLMNImpl(plmnId1, rat1, ranPeriodicLocationSupport1);
        ReportingPLMN rPlmn2 = new ReportingPLMNImpl(plmnId2, rat2, ranPeriodicLocationSupport2);
        reportingPLMNs.add(rPlmn1);
        reportingPLMNs.add(rPlmn2);
        boolean plmnListPrioritized = true;
        return new ReportingPLMNListImpl(plmnListPrioritized, reportingPLMNs);
    }

    //**********************************************************//
    //*** Common methods for MAP LSM operations' attributes ***//
    //********************************************************//
    @Override
    public void putAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        this.setAddressNature(x);
    }

    @Override
    public void putNumberingPlanType(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        this.setNumberingPlanType(x);
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public NumberingPlanMapType getNumberingPlanType() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getNumberingPlanType().getIndicator());
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void setNumberingPlanType(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setNumberingPlanType(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getIMSI();
    }

    @Override
    public void setIMSI(String imsi) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setIMSI(imsi);
        this.testerHost.markStore();
    }

    @Override
    public String getIMEI() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getIMEI();
    }

    @Override
    public void setIMEI(String imei) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setIMEI(imei);
        this.testerHost.markStore();
    }

    @Override
    public String getMSISDN() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getMSISDN();
    }

    @Override
    public void setMSISDN(String msisdn) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setMSISDN(msisdn);
        this.testerHost.markStore();
    }

    @Override
    public String getNetworkNodeNumber() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getNetworkNodeNumberAddress();
    }

    @Override
    public void setNetworkNodeNumber(String address) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setNetworkNodeNumberAddress(address);
        this.testerHost.markStore();
    }

    @Override
    public String getHGMLCAddress() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getHGMLCAddress();
    }

    @Override
    public void setHGMLCAddress(String hgmlcAddress) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setHGMLCAddress(hgmlcAddress);
        this.testerHost.markStore();
    }

    @Override
    public Integer getAgeOfLocationEstimate() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getAgeOfLocationEstimate();
    }

    @Override
    public void setAgeOfLocationEstimate(Integer ageLocationEstimate) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setAgeOfLocationEstimate(ageLocationEstimate);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLCSReferenceNumber() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getLCSReferenceNumber();
    }

    @Override
    public void setLCSReferenceNumber(Integer lcsReferenceNumber) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setLCSReferenceNumber(lcsReferenceNumber);
        this.testerHost.markStore();
    }


    @Override
    public void putLCSEventType(String val) {
        LCSEventType x = LCSEventType.createInstance(val);
        this.setLCSEventType(x);
    }

    @Override
    public LCSEventType getLCSEventType() {
        return new LCSEventType(this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getLCSEvent().getEvent());
    }

    @Override
    public void setLCSEventType(LCSEventType val) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setLCSEvent(LCSEvent.getLCSEvent(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public Integer getMNC() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getMNC();
    }

    @Override
    public Integer getMCC() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getMCC();
    }

    @Override
    public void setMCC(Integer mcc) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setMCC(mcc);
        this.testerHost.markStore();
    }

    @Override
    public void setMNC(Integer mnc) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setMNC(mnc);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLAC() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getLAC();
    }

    @Override
    public void setLAC(Integer lac) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setLAC(lac);
        this.testerHost.markStore();
    }

    @Override
    public Integer getCellId() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getCellId();
    }

    @Override
    public void setCellId(Integer cellId) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setCellId(cellId);
        this.testerHost.markStore();
    }

    @Override
    public String getNaESRDAddress() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getNaESRDAddress();
    }

    @Override
    public void setNaESRDAddress(String address) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setNaESRDAddress(address);
        this.testerHost.markStore();
    }

    @Override
    public String getNaESRKAddress() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getNaESRKAddress();
    }

    @Override
    public void setNaESRKAddress(String address) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setNaESRKAddress(address);
        this.testerHost.markStore();
    }

    //TODO move this helper method to constructor type...
    private GSNAddress createGSNAddress(String gsnAddress) throws MAPException {
        try {
            //From InetAddress javadoc "the host name can either be a machine name, such as "java.sun.com", or a textual representation of its IP address.
            //If a literal IP address is supplied, only the validity of the address format is checked".
            InetAddress address = InetAddress.getByName(gsnAddress);
            GSNAddressAddressType addressType = null;
            if (address instanceof Inet4Address) {
                addressType = GSNAddressAddressType.IPv4;
            } else if (address instanceof Inet6Address) {
                addressType = GSNAddressAddressType.IPv6;
            }
            byte[] addressData = address.getAddress();
            return this.mapParameterFactory.createGSNAddress(addressType, addressData);

        } catch (UnknownHostException e) {
            throw new MAPException("Invalid GSNAddress", e);
        }
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }


}

