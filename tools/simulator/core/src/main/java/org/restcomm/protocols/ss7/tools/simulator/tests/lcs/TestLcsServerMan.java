package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;


import javax.xml.bind.DatatypeConverter;

import com.google.common.collect.Multimap;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
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

import org.restcomm.protocols.ss7.map.api.service.lsm.EllipsoidPoint;
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
import org.restcomm.protocols.ss7.map.api.service.lsm.Polygon;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingOptionMilliseconds;
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
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;

import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;

import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageFacilityNotSupImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageUnauthorizedLCSClientImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;

import org.restcomm.protocols.ss7.map.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientNameImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSRequestorIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PeriodicLDRInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PolygonImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ReportingOptionMillisecondsImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SLRArgExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SLRArgPCSExtensionsImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SupportedGADShapesImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranAdditionalPositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranCivicAddressImpl;
import org.restcomm.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSCodewordImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSPrivacyCheckImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranGANSSpositioningDataImpl;

import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tools.simulator.Stoppable;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.common.TesterBase;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapMan;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.management.TesterHostImpl;

import java.nio.charset.Charset;

import static org.apache.commons.lang3.RandomUtils.nextLong;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class TestLcsServerMan extends TesterBase implements TestLcsServerManMBean, Stoppable, MAPServiceLsmListener {

    private static final Logger logger = LogManager.getLogger(TestLcsServerMan.class);

    public static String SOURCE_NAME = "TestLcsServerMan";
    private MapMan mapMan;
    private boolean isStarted;
    private int countMapLcsReq = 0;
    private int countMapLcsResp = 0;
    private String currentRequestDef = "";
    private MAPProvider mapProvider;
    private MAPServiceLsm mapServiceLsm;
    private MAPParameterFactory mapParameterFactory;

    public TestLcsServerMan(String name) {
        super(SOURCE_NAME);
        this.isStarted = false;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        this.mapServiceLsm = mapProvider.getMAPServiceLsm();
        this.mapParameterFactory = mapProvider.getMAPParameterFactory();

        mapServiceLsm.activate();
        mapServiceLsm.addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);

        this.testerHost.sendNotif(SOURCE_NAME, "LCS Server has been started", "", Level.INFO);
        isStarted = true;
        this.countMapLcsReq = 0;
        this.countMapLcsResp = 0;
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
        isStarted = false;
        mapProvider.getMAPServiceLsm().deactivate();
        mapProvider.getMAPServiceLsm().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "LCS Client has been stopped", "", Level.INFO);
    }

    //***************************//
    //***** SRILCS methods *****//
    //*************************//
    @Override
    public String performSendRoutingInfoForLCSResponse() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSResponse();
    }

    public String sendRoutingInfoForLCSResponse() {

        return "sendRoutingInfoForLCSResponse called automatically";
    }

    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInfoForLCSRequest) {

        logger.debug("\nonSendRoutingInfoForLCSRequest");
        if (!isStarted)
            return;

        this.countMapLcsReq++;

        MAPDialogLsm curDialog = sendRoutingInfoForLCSRequest.getMAPDialog();
        long invokeId = sendRoutingInfoForLCSRequest.getInvokeId();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SendRoutingInfoForLCSRequest",
            createSRILCSReqData(curDialog.getLocalDialogId(), sendRoutingInfoForLCSRequest.getMLCNumber(),
                sendRoutingInfoForLCSRequest.getTargetMS()), Level.INFO);

        Random rand = new Random();

        // Set Calling SCCP Address (HLR for SRILCS response)
        curDialog.setLocalAddress(getHLRSCCPAddress("59899170001"));

        String subId = null;
        // Generate MAP errors for specific MSISDN
        if (sendRoutingInfoForLCSRequest.getTargetMS().getMSISDN() != null)
            subId = sendRoutingInfoForLCSRequest.getTargetMS().getMSISDN().getAddress();
        else if (sendRoutingInfoForLCSRequest.getTargetMS().getIMSI() != null)
            subId = sendRoutingInfoForLCSRequest.getTargetMS().getIMSI().getData();
        if (subId != null) {
            if (subId.equalsIgnoreCase("99998888")) {
                InvokeProblemType invokeProblemType = InvokeProblemType.UnrecognizedOperation;
                Problem problem = new ProblemImpl();
                problem.setInvokeProblemType(invokeProblemType);
                try {
                    curDialog.sendRejectComponent(invokeId, problem);
                    curDialog.close(false);
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
                logger.debug("\nRejectComponent sent");
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: RejectComponent", createSRILCSResData(curDialog.getLocalDialogId(),
                        null, null, null), Level.INFO);
                return;
            }
            if (subId.equalsIgnoreCase("99990000")) {
                MAPErrorMessage mapErrorMessageUnauthorizedLCSClient = new MAPErrorMessageUnauthorizedLCSClientImpl();
                try {
                    curDialog.sendErrorComponent(invokeId, mapErrorMessageUnauthorizedLCSClient);
                    curDialog.close(false);
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
                logger.debug("\nErrorComponent sent");
                this.testerHost.sendNotif(SOURCE_NAME, "Sent: ErrorComponent", createSRILCSResData(curDialog.getLocalDialogId(),
                        null, null, null), Level.INFO);
                return;
            }
        }

        try {
            MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
            ISDNAddressString msisdnAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "59899077937");
            SubscriberIdentity msisdn = new SubscriberIdentityImpl(msisdnAddress);
            IMSI imsiImpl;
            SubscriberIdentity imsi, targetMS = null;
            if (sendRoutingInfoForLCSRequest.getTargetMS().getIMSI() != null)
                targetMS = msisdn;
            if (sendRoutingInfoForLCSRequest.getTargetMS().getMSISDN() != null) {
                msisdnAddress = sendRoutingInfoForLCSRequest.getTargetMS().getMSISDN();
                if (msisdnAddress.getAddress().equals("60196229802"))
                    imsiImpl = new IMSIImpl("502153207655206");
                 else if (msisdnAddress.getAddress().equals("60196229803"))
                    imsiImpl = new IMSIImpl("502153100826899");
                else if (msisdnAddress.getAddress().equals("60196229804"))
                    imsiImpl = new IMSIImpl("502153147968442");
                else
                    imsiImpl = new IMSIImpl("748026871012345");
                imsi = new SubscriberIdentityImpl(imsiImpl);
                targetMS = imsi;
            }
            ISDNAddressString mlcNumber = sendRoutingInfoForLCSRequest.getMLCNumber();
            String mscAddress = "598991800024";
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, mscAddress);
            String additionalMcsAddress = "598991800179";
            String sgsnAddress = "598992000077";
            ISDNAddressString additionalMcsNumber = null;
            ISDNAddressString sgsnNumber = null;
            AdditionalNumber additionalNumber = null;
            boolean gprsNodeIndicator = false;
            int addNumRandom = rand.nextInt(5) + 1;
            switch (addNumRandom) {
                case 1:
                    break;
                case 2:
                    gprsNodeIndicator = true;
                    break;
                case 3:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 4:
                    gprsNodeIndicator = true;
                    sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, sgsnAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 5:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                        NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    gprsNodeIndicator = true;
                    break;
                default:
                    additionalNumber = null; // not needed, just for being explicit about the default case
                    gprsNodeIndicator = false; // not needed, just for being explicit about the default case
                    break;
            }

            logger.warn("Additional Number onSendRoutingInfoForLCSRequest : " + additionalNumber);

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
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = null, additionalLCSCapabilitySets = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            false, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 2:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 3:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                case 4:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                default:
                    break;

            }
            DiameterIdentity mmeName = null;
            DiameterIdentity aaaServerName = null;
            DiameterIdentity sgsnName = null;
            DiameterIdentity sgsnRealm = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 2:
                    sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    break;
                case 3:
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 4:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                default:
                    break;
            }

            LCSLocationInfo lcsLocationInfo = mapFactory.createLCSLocationInfo(mscNumber, lmsi, null, gprsNodeIndicator,
                additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            GSNAddress vGmlcAddress = null;
            GSNAddress hGmlcAddress = null;
            GSNAddress pprAddress = null;
            GSNAddress additionalVGmlcAddress = null;
            switch (rand.nextInt(10 + 1)) {
                case 1:
                    vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
                    break;
                case 2:
                    hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());
                    break;
                case 3:
                    vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
                    additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });
                    break;
                case 4:
                    vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
                    pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.18").getAddress());
                    break;
                case 5:
                    vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
                    hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());
                    additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });
                    break;
                case 6:
                    vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
                    hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());
                    pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.18").getAddress());
                    additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });
                    break;
                default:
                    break;
            }

            int sriLcsResponseDelay = rand.nextInt(150);
            try {
                Thread.sleep(sriLcsResponseDelay);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            curDialog.addSendRoutingInfoForLCSResponse(sendRoutingInfoForLCSRequest.getInvokeId(),
                targetMS, lcsLocationInfo, null, vGmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);

            logger.debug("\nset addSendRoutingForLCSResponse");
            curDialog.close(false);
            logger.debug("\naddSendRoutingForLCSResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingForLCSResponse",
                createSRILCSResData(curDialog.getLocalDialogId(), mscNumber, targetMS, additionalNumber), Level.INFO);

        } catch (MAPException me) {
            logger.debug("Failed building SendRoutingInfoForLCS response " + me);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    private String createSRILCSReqData(long dialogId, ISDNAddressString mlcNumber, SubscriberIdentity targetMS) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        if (mlcNumber != null) {
            sb.append(", mlcNumber=\"");
            sb.append(mlcNumber.getAddress());
        }
        if (targetMS != null) {
            if (targetMS.getMSISDN() != null) {
                sb.append(", MSISDN=\"");
                sb.append(targetMS.getMSISDN().getAddress());
            }
            if (targetMS.getIMSI() != null) {
                sb.append(", IMSI=\"");
                sb.append(new String(targetMS.getIMSI().getData().getBytes()));
            }
        }
        sb.append("\"");
        return sb.toString();
    }


    private String createSRILCSResData(long dialogId, ISDNAddressString networkNodeNumber, SubscriberIdentity targetMS,
                                          AdditionalNumber additionalNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        if (networkNodeNumber != null) {
            sb.append(", networkNodeNumber=\"");
            sb.append(networkNodeNumber.getAddress());
        }
        if (additionalNumber != null) {
            if (additionalNumber.getMSCNumber() != null) {
                sb.append(", Additional MSC Number=\"").append(additionalNumber.getMSCNumber().getAddress());
            } else if (additionalNumber.getSGSNNumber() != null) {
                sb.append(", Additional SGSN Number=\"").append(additionalNumber.getSGSNNumber().getAddress());
            }
        }
        if (targetMS != null) {
            if (targetMS.getMSISDN() != null) {
                sb.append(", MSISDN=\"").append(targetMS.getMSISDN());
            }
            if (targetMS.getIMSI() != null) {
                sb.append(", IMSI=\"").append(targetMS.getIMSI());
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponse) {
        logger.debug("\nonSendRoutingInfoForLCSResponse");
        this.countMapLcsResp++;
        MAPDialogLsm curDialog = sendRoutingInfoForLCSResponse.getMAPDialog();
        this.testerHost.sendNotif(SOURCE_NAME,
            "Rcvd: SendRoutingInfoForLCSResponse", this
                .createSRILCSResData(curDialog.getLocalDialogId(),
                        sendRoutingInfoForLCSResponse.getLCSLocationInfo().getNetworkNodeNumber(),
                        sendRoutingInfoForLCSResponse.getTargetMS(),
                        sendRoutingInfoForLCSResponse.getLCSLocationInfo().getAdditionalNumber()),
            Level.INFO);

    }

    //*********************//
    //**** PSL methods ***//
    //*******************//
    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequest) {

        logger.debug("\nonProvideSubscriberLocationRequest");
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = provideSubscriberLocationRequest.getMAPDialog();
        long invokeId = provideSubscriberLocationRequest.getInvokeId();

        // Set Calling SCCP Address (MSC for PSL response)
        curDialog.setLocalAddress(getMSCSCCPAddress("59899180071"));

        int cbsDataCodingSchemeCode = 15;
        CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
        String ussdLcsString = "3";
        Charset gsm8Charset = Charset.defaultCharset();
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
            NumberingPlan.ISDN, "444567");
        ISDNAddressString msisdn;
        MAPExtensionContainer mapExtensionContainer = null;
        LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, mapExtensionContainer);
        LCSClientInternalID lcsClientInternalID = LCSClientInternalID.oandMHPLMN;
        USSDString ussdString = null;
        boolean saiPresent;
        try {
            ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
        PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithNotification;
        PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

        LCSPrivacyCheck lcsPrivacyCheck;
        LCSClientID lcsClientID;
        boolean privacyOverride = false;
        LCSCodeword lcsCodeword;
        IMSI imsi;
        IMEI imei;
        SupportedGADShapes supportedGADShapes;
        Integer lcsReferenceNumber = null;

        if (provideSubscriberLocationRequest.getIMSI().getData().equals("502153207655206")) {
            try {
                Thread.sleep(15000);
                return;
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        } else if (provideSubscriberLocationRequest.getIMSI().getData().equals("502153100826899")) {
            InvokeProblemType invokeProblemType = InvokeProblemType.ResourceLimitation;
            Problem problem = new ProblemImpl();
            problem.setInvokeProblemType(invokeProblemType);
            try {
                curDialog.sendRejectComponent(invokeId, problem);
                curDialog.close(false);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
            logger.debug("\nRejectComponent sent");
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: RejectComponent",
                createPSLResponse(curDialog.getLocalDialogId(), null, null, null, null, null,
                        false, null, false, null, null, false, null,
                        null, null, null, null, null, null), Level.INFO);
            return;
        } else if (provideSubscriberLocationRequest.getIMSI().getData().equals("502153147968442")) {
            MAPErrorMessage mapErrorMessage1 = new MAPErrorMessageFacilityNotSupImpl();
            try {
                curDialog.sendErrorComponent(invokeId, mapErrorMessage1);
                curDialog.close(false);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
            logger.debug("\nErrorComponent sent");
            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ErrorComponent",
                createPSLResponse(curDialog.getLocalDialogId(), null, null, null, null, null,
                        false, null, false, null, null, false, null,
                        null, null, null, null, null, null), Level.INFO);
            return;
        }

        if (provideSubscriberLocationRequest.getLCSClientID() == null) {
            String clientName = "545248";
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            APN lcsAPN = null;
            try {
                lcsAPN = new APNImpl("restcomm.org");
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
            lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, null);
        } else {
            lcsClientID = provideSubscriberLocationRequest.getLCSClientID();
        }

        if (provideSubscriberLocationRequest.getPrivacyOverride())
            privacyOverride = true;

        if (provideSubscriberLocationRequest.getLCSCodeword() == null) {
            lcsCodeword = new LCSCodewordImpl(cbsDataCodingScheme, ussdString);
        } else {
            lcsCodeword = provideSubscriberLocationRequest.getLCSCodeword();
        }

        if (provideSubscriberLocationRequest.getIMSI() == null) {
            imsi = new IMSIImpl("748026871012345");
        } else {
            imsi = provideSubscriberLocationRequest.getIMSI();
        }

        if (provideSubscriberLocationRequest.getMSISDN() == null) {
            msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "59899077937");
        } else {
            msisdn = provideSubscriberLocationRequest.getMSISDN();
        }

        if (provideSubscriberLocationRequest.getIMEI() == null) {
            imei = new IMEIImpl("01171400466105");
        } else {
            imei = provideSubscriberLocationRequest.getIMEI();
        }

        if (provideSubscriberLocationRequest.getSupportedGADShapes() == null) {
            boolean ellipsoidPoint = true;
            boolean ellipsoidPointWithUncertaintyCircle = true;
            boolean ellipsoidPointWithUncertaintyEllipse = true;
            boolean polygon = true;
            boolean ellipsoidPointWithAltitude = false;
            boolean ellipsoidPointWithAltitudeAndUncertaintyEllipsoid = true;
            boolean ellipsoidArc = true;
            supportedGADShapes = new SupportedGADShapesImpl(ellipsoidPoint, ellipsoidPointWithUncertaintyCircle,
                    ellipsoidPointWithUncertaintyEllipse, polygon, ellipsoidPointWithAltitude,
                    ellipsoidPointWithAltitudeAndUncertaintyEllipsoid, ellipsoidArc);
        } else {
            supportedGADShapes = provideSubscriberLocationRequest.getSupportedGADShapes();
        }

        if (provideSubscriberLocationRequest.getLCSPrivacyCheck() == null) {
            lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
        } else {
            lcsPrivacyCheck = provideSubscriberLocationRequest.getLCSPrivacyCheck();
        }

        if (provideSubscriberLocationRequest.getLCSReferenceNumber() != null) {
            setLCSReferenceNumber(provideSubscriberLocationRequest.getLCSReferenceNumber());
            lcsReferenceNumber = provideSubscriberLocationRequest.getLCSReferenceNumber();
        }

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: ProvideSubscriberLocationRequest",
            createPSLRequestData(curDialog.getLocalDialogId(),
                provideSubscriberLocationRequest.getLocationType(),
                provideSubscriberLocationRequest.getMlcNumber(),
                lcsClientID,
                privacyOverride,
                imsi,
                msisdn,
                provideSubscriberLocationRequest.getLMSI(),
                imei,
                provideSubscriberLocationRequest.getLCSPriority(),
                provideSubscriberLocationRequest.getLCSQoS(),
                supportedGADShapes,
                lcsReferenceNumber,
                provideSubscriberLocationRequest.getLCSServiceTypeID(),
                lcsCodeword,
                lcsPrivacyCheck,
                provideSubscriberLocationRequest.getAreaEventInfo(),
                provideSubscriberLocationRequest.getHGMLCAddress(),
                provideSubscriberLocationRequest.getMoLrShortCircuitIndicator(),
                provideSubscriberLocationRequest.getPeriodicLDRInfo(),
                provideSubscriberLocationRequest.getReportingPLMNList()
            ), Level.INFO);

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
        // // 0x00=0000 0000 -> positioning data discriminator (bits 4-1): 0000 indicate usage of each positioning method that was attempted either successfully or unsuccessfully
        // 0x03=0000 0011 -> 00000=>Method=Timing Advance, 011=>Usage=3 (Attempted successfully: results used to generate location)
        // 0x1b=0001 1011 -> 00011=>Method=Mobile Assisted E-OTD, 011=>Usage=3 (Attempted successfully: results used to generate location)
        // 0x21=0010 0001 -> 00100=>Method=Mobile Based E-OTD, 001=>Usage=1 (Attempted successfully: results not used to generate location
        // 0x2b=0010 1011 -> 00101=>Method=Mobile Assisted GPS, 011=>Usage=3: Attempted successfully: results used to generate location
        // 0x3a=0011 1010 -> 00111=>Method=Conventional GPS, 010=>Usage=2: Attempted successfully: results used to verify but not generate location
        // 0x43=0100 0011 -> 01000=>Method=U-TDOA, 011=>Usage=3: Attempted successfully: results used to generate location
        // 0x60=0110 0000 -> 01100=>Method=Cell ID, 000=>Usage=0: Attempted unsuccessfully due to failure or interruption
        // byte[] geranPosData = new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60};

        // 0x00=0000 0000 -> positioning data discriminator (BIT STRING (SIZE(4))): 0000 indicates the presence of the Positioning Data Set IE (that reports the usage of each non-GANSS method that was successfully used to obtain the location estimate) and the optional presence of the GANSS Positioning Data Set IE. It also indicates the optional presence of the Additional Positioning Data Set IE;
        // 0x00=0000 0000 -> C-ifDiscriminator=0
        // 0x28=0010 1000 -> 00101=>Method=Mobile Assisted GPS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
        // 0x31=0011 0001 -> 00110=>Method=Mobile Based GPS, usage=1 (Attempted successfully: results not used to generate location - not used)
        // 0x40=0100 0000 -> 01000=>Method=U-TDOA, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
        // 0x51=0101 0001 -> 01010=>Method=IPDL, usage=1 (Attempted successfully: results not used to generate location - not used)
        // 0x5c=0101 1100 -> 01011=>Method=RTT, usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
        // 0x4b=0100 1011 -> 01000=>Method=OTDOA, usage 3 (Attempted successfully: results used to generate location)
        // 0x3a=0011 1010 -> 00111=>Method=Conventional GPS, usage=2 (results used to verify but not generate location - not used)
        // byte[] utranPosData = new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a};

        // 0x06 Length Indicator?
        // 0x8c=1000 1100 -> 10=>Method=Conventional, 001=>GANSSId=SBAS, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
        // 0x02=0000 0010 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 10=>usage=2 (Attempted successfully: results used to verify but not generate location)
        // 0x11=0001 0001 -> 00=>Method=MS-Based, 010=>GANSSId=Modernized GPS, 01=>usage=1 (Attempted successfully: results not used to generate location)
        // 0x58=0101 1000 -> 01=>Method=MS-Assisted, 011=>GANSSId=QZSS, 00=usage=0 (Attempted unsuccessfully due to failure or interruption)
        // 0xe8=1110 1000 -> 11=>Method=Reserved, 101=>GANSSId=BDS, 00=usage0 (Attempted unsuccessfully due to failure or interruption)
        // 0x63=0110 0011 -> 01=>MS-Assisted, 100=>GANSSId=GLONASS, 11=usage3 (Attempted successfully: results used to generate location)
        // byte[] geranGANSSData = new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63};

        // 0x01=0000 0001 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 01=>usage=1 (Attempted successfully: results used to generate location)
        // 0x4a=0100 0110 -> 01=>Method=MS-Assisted, 100=>GANSSId=SBAS, 010=>usage=2 (Attempted successfully: results used to verify but not generate location - not used)
        // 0x90=1001 0000 -> 10=>Method=Conventional, 010=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
        // 0x18=0001 1000 -> 00=>Method=MS-Based, 000=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
        // 0xdc=1110 1100 -> 11=>Method=Reserved, 101=>GANSSId=QZSS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
        // 0x63=0110 0011 -> 01=>Method=MS-Assisted, 100=>GANSSId=GLONASS, usage=3 (Attempted successfully: results used to generate location)
        // byte[] utranGanssData = new byte[] {0x01, 0x46, (byte) 0x90, 0x18, (byte) 0xec, 0x63};

        // 0x94=1001 0100 10=>Method=Standalone, 010=>GANSSId=Bluetooth, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
        // 0x4b=0100 1011 00=>Method=MS-Assisted, AddPosId=GANSSId=WLAN, 011=>usage=3 (Attempted successfully: results used to generate location)
        // byte[] utranAddPosData = new byte[] {(byte) 0x94, 0x4b};

        switch (rand.nextInt(7) + 1) {
            case 1:
                geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                break;
            case 2:
                geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                break;
            case 3:
                utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                break;
            case 4:
                utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                break;
            case 5:
                utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {(byte) 0x94, 0x4b});
                break;
            case 6:
                geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                break;
            case 7:
                utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                break;
        }

        boolean deferredMTLRResponseIndicator = false;
        LocationType locationType = provideSubscriberLocationRequest.getLocationType();
        if (locationType.getDeferredLocationEventType() != null) {
            deferredMTLRResponseIndicator = true;
        }

        int mcc, mnc, lac, ci;
        mcc = 748;
        mnc = 1;
        lac = 101;
        ci = 10263;
        saiPresent = false;
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
        if (provideSubscriberLocationRequest.getLCSQoS() != null) {
            if (provideSubscriberLocationRequest.getLCSQoS().getVelocityRequest()) {
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

        boolean moLrShortCircuitIndicator = provideSubscriberLocationRequest.getMoLrShortCircuitIndicator();

        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "59899180071");
        ServingNodeAddress targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, true);

        Integer utranBaroPressureMeas = null;
        UtranCivicAddress utranCivicAddress = null;

        if (geranPositioningDataInfo == null || geranGanssPositioningData == null) {
            utranBaroPressureMeas = rand.nextInt(85000) + 30000; // UtranBaroPressureMeas ::= INTEGER (30000..115000)
            String civicAddressString = null;
            switch (rand.nextInt(7) + 1) {
                case 1:
                    civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<civicAddress xml:lang=\"en-AU\"\n" +
                            "              xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                            "              xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                            "    <country>AU</country>\n" +
                            "    <A1>NSW</A1>\n" +
                            "    <A3>Wollongong</A3>\n" +
                            "    <A4>North Wollongong</A4>\n" +
                            "    <RD>Flinders</RD>\n" +
                            "    <STS>Street</STS>\n" +
                            "    <RDBR>Campbell Street</RDBR>\n" +
                            "    <LMK>Gilligan's Island</LMK>\n" +
                            "    <LOC>Corner</LOC>\n" +
                            "    <NAM>Video Rental Store</NAM>\n" +
                            "    <PC>2500</PC>\n" +
                            "    <ROOM>Westerns and Classics</ROOM>\n" +
                            "    <PLC>store</PLC>\n" +
                            "    <POBOX>Private Box 15</POBOX>\n" +
                            "    <cae:MP>248</cae:MP>\n" +
                            "    <cae:PN>22-109-689</cae:PN>\n" +
                            "</civicAddress>";
                    break;
                case 2:
                    civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<civicAddress>\n" +
                            "    <country>US</country>\n" +
                            "    <A1>New York</A1>\n" +
                            "    <A3>New York</A3>\n" +
                            "    <A4>Broadway</A4>\n" +
                            "    <HNO>123</HNO>\n" +
                            "    <LOC>Suite 75</LOC>\n" +
                            "    <PC>10027-0401</PC>\n" +
                            "</civicAddress>";
                    break;
                case 3:
                    civicAddressString = "<civicAddress xml:lang=\"en-AU\"\n" +
                            "     xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\">\n" +
                            "     <country>AU</country>\n" +
                            "     <A1>NSW</A1>\n" +
                            "     <A3>Wollongong</A3>\n" +
                            "     <A4>North Wollongong</A4>\n" +
                            "     <RD>Flinders</RD>\n" +
                            "     <STS>Street</STS>\n" +
                            "     <RDBR>Campbell Street</RDBR>\n" +
                            "     <LMK>Gilligan's Island</LMK>\n" +
                            "     <LOC>Corner</LOC>\n" +
                            "     <NAM>Video Rental Store</NAM>\n" +
                            "     <PC>2500</PC>\n" +
                            "     <ROOM>Westerns and Classics</ROOM>\n" +
                            "     <PLC>store</PLC>\n" +
                            "     <POBOX>Private Box 15</POBOX>\n" +
                            "   </civicAddress>";
                    break;
                case 4:
                    civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                            "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                            "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                            "     <country>US</country>\n" +
                            "     <A1>CA</A1>\n" +
                            "     <A2>Sacramento</A2>\n" +
                            "     <RD>I5</RD>\n" +
                            "     <cae:MP>248</cae:MP>\n" +
                            "     <cae:PN>22-109-689</cae:PN>\n" +
                            "   </civicAddress>";
                    break;
                case 5:
                    civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                            "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                            "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                            "     <country>US</country>\n" +
                            "     <A1>CA</A1>\n" +
                            "     <A2>Sacramento</A2>\n" +
                            "     <RD>Colorado</RD>\n" +
                            "     <HNO>223</HNO>\n" +
                            "     <cae:STP>Boulevard</cae:STP>\n" +
                            "     <cae:HNP>A</cae:HNP>\n" +
                            "   </civicAddress>";
                    break;
                default:
                    break;
            }
            if (civicAddressString != null) {
                byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
                utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);
            }
        }


        try {
            int pslResponse = rand.nextInt(10) + 1;
            switch (pslResponse) {
                case 1:
                    delayResponse(rand.nextInt(500));
                    break;
                case 2:
                    delayResponse(rand.nextInt(1000));
                    break;
                case 3:
                    delayResponse(rand.nextInt(2000));
                    break;
                case 4:
                    delayResponse(rand.nextInt(3000));
                    break;
                case 5:
                    delayResponse(rand.nextInt(4000));
                    break;
                case 6:
                    delayResponse(rand.nextInt(4500));
                    break;
                case 7:
                    delayResponse(rand.nextInt(5000));
                    break;
                case 8:
                    delayResponse(rand.nextInt(6500));
                    break;
                case 9:
                    delayResponse(rand.nextInt(10000));
                    break;
                case 10:
                    delayResponse(35000);
                    break;
                default:
                    delayResponse(500);
                    break;
            }

            curDialog.addProvideSubscriberLocationResponse(
                provideSubscriberLocationRequest.getInvokeId(),
                locationEstimate,
                geranPositioningDataInfo,
                utranPositioningDataInfo,
                ageOfLocationEstimate,
                additionalLocationEstimate,
                null,
                deferredMTLRResponseIndicator,
                cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent,
                accuracyFulfilmentIndicator,
                velocityEstimate,
                moLrShortCircuitIndicator,
                geranGanssPositioningData,
                utranGanssPositioningData,
                targetServingNodeForHandover,
                utranAdditionalPositioningData,
                utranBaroPressureMeas,
                utranCivicAddress);

            logger.debug("\nset addProvideSubscriberLocationResponse");
            curDialog.close(false);
            logger.debug("\naddProvideSubscriberLocationResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ProvideSubscriberLocationResponse", createPSLResponse(curDialog.getLocalDialogId(),
                    locationEstimate, geranPositioningDataInfo, utranPositioningDataInfo, ageOfLocationEstimate, additionalLocationEstimate,
                    deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI, saiPresent, accuracyFulfilmentIndicator,
                    velocityEstimate, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData, targetServingNodeForHandover,
                    utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress, lcsReferenceNumber), Level.INFO);

        } catch (MAPException me) {
            logger.debug("Exception on addProvideSubscriberLocationResponse " + me);
        }
    }

    private void delayResponse(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private String createPSLResponse(long dialogId, ExtGeographicalInformation locationEstimate,
            PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            boolean deferredMTLRResponseIndicator, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            boolean saiPresent, AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover,
            UtranAdditionalPositioningData utranAdditionalPositioningData, Integer utranBaroPressureMeas,
            UtranCivicAddress utranCivicAddress, Integer lcsReferenceNumber) {

        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId).append("\",\n ");

        if (locationEstimate != null) {
            sb.append("\", addLocationEstimate=\"");
            sb.append("\" Type of Shape=\"").append(locationEstimate.getTypeOfShape());
            if (locationEstimate.getLatitude() > -90 && locationEstimate.getLatitude() < 90) {
                sb.append("\", latitude=\"");
                sb.append(locationEstimate.getLatitude()).append(", ");
            }
            if (locationEstimate.getLongitude() > -180 && locationEstimate.getLongitude() < 180) {
                sb.append("\", longitude=\"");
                sb.append(locationEstimate.getLongitude()).append(", ");
            }
            if (locationEstimate.getTypeOfShape() != null) {
                sb.append("\", typeOfShape=\"");
                sb.append(locationEstimate.getTypeOfShape()).append(", ");
            }
            if (locationEstimate.getUncertainty() >= 0 && locationEstimate.getUncertainty() < 128) {
                sb.append("\", uncertainty=\"");
                sb.append(locationEstimate.getUncertainty()).append(", ");
            }
            if (locationEstimate.getAltitude() > Integer.MIN_VALUE && locationEstimate.getAltitude() < Integer.MAX_VALUE) {
                sb.append("\", altitude=\"");
                sb.append(locationEstimate.getAltitude()).append(", ");
            }
            if (locationEstimate.getUncertaintyAltitude() > Double.MIN_VALUE && locationEstimate.getUncertaintyAltitude() < Double.MAX_VALUE) {
                sb.append("\", uncertaintyAltitude=\"");
                sb.append(locationEstimate.getUncertaintyAltitude()).append(", ");
            }
            if (locationEstimate.getConfidence() > Integer.MIN_VALUE && locationEstimate.getConfidence() < Integer.MAX_VALUE) {
                sb.append("\", confidence=\"");
                sb.append(locationEstimate.getConfidence()).append(", ");
            }
            if (locationEstimate.getInnerRadius() > Integer.MIN_VALUE && locationEstimate.getInnerRadius() < Integer.MAX_VALUE) {
                sb.append("\", innerRadius=\"");
                sb.append(locationEstimate.getInnerRadius()).append(", ");
            }
            if (locationEstimate.getUncertaintyRadius() > Double.MIN_VALUE && locationEstimate.getUncertaintyRadius() < Double.MAX_VALUE) {
                sb.append("\", uncertaintyRadius=\"");
                sb.append(locationEstimate.getUncertaintyRadius()).append(", ");
            }
            if (locationEstimate.getUncertaintySemiMajorAxis() > Double.MIN_VALUE && locationEstimate.getUncertaintySemiMajorAxis() < Double.MAX_VALUE) {
                sb.append("\", uncertaintySemiMajorAxis=\"");
                sb.append(locationEstimate.getUncertaintySemiMajorAxis()).append(", ");
            }
            if (locationEstimate.getUncertaintySemiMinorAxis() > Double.MIN_VALUE && locationEstimate.getUncertaintySemiMinorAxis() < Double.MAX_VALUE) {
                sb.append("\", uncertaintySemiMinorAxis=\"");
                sb.append(locationEstimate.getUncertaintySemiMinorAxis()).append(", ");
            }
            if (locationEstimate.getAngleOfMajorAxis() > Double.MIN_VALUE && locationEstimate.getAngleOfMajorAxis() < Double.MAX_VALUE) {
                sb.append("\", angleOfMajorAxis=\"");
                sb.append(locationEstimate.getAngleOfMajorAxis()).append(", ");
            }
            if (locationEstimate.getOffsetAngle() > Double.MIN_VALUE && locationEstimate.getOffsetAngle() < Double.MAX_VALUE) {
                sb.append("\", offsetAngle=\"");
                sb.append(locationEstimate.getOffsetAngle()).append(", ");
            }
            if (locationEstimate.getIncludedAngle() > Double.MIN_VALUE && locationEstimate.getIncludedAngle() < Double.MAX_VALUE) {
                sb.append("\", includedAngle=\"");
                sb.append(locationEstimate.getIncludedAngle()).append(", ");
            }
        }
        sb.append("\", ageOfLocationEstimate=\"").append(ageOfLocationEstimate);

        if (additionalLocationEstimate != null) {
            sb.append("\", addLocationEstimate=\"");
            sb.append("\" Type of Shape=\"").append(additionalLocationEstimate.getTypeOfShape());
            byte[] addLocationEstimateByteArray = additionalLocationEstimate.getData();
            if (additionalLocationEstimate.getTypeOfShape() == TypeOfShape.Polygon) {
                PolygonImpl polygon = new PolygonImpl(addLocationEstimateByteArray);
                sb.append(polygon);
            }
        }

        if (geranPositioningData != null) {
            try {
                ArrayList<String> methods = geranPositioningData.getLocationGeneratedPositioningMethods();
                StringBuilder geranPositioningDataInfo = new StringBuilder();
                int metCounter = 0;
                for (String met : methods) {
                    metCounter++;
                    geranPositioningDataInfo.append(met);
                    if (methods.size() != metCounter)
                        geranPositioningDataInfo.append(", ");
                }
                sb.append("\", geranPositioningData=\"").append(geranPositioningDataInfo);
            } catch (MAPException e) {
                throw new RuntimeException(e);
            }
        }

        if (utranPositioningData != null) {
            try {
                ArrayList<String> methods = utranPositioningData.getUtranLocationGeneratedPositioningMethods();
                StringBuilder utranPosDataInfo = new StringBuilder();
                int metCounter = 0;
                for (String met : methods) {
                    metCounter++;
                    utranPosDataInfo.append(met);
                    if (methods.size() != metCounter)
                        utranPosDataInfo.append(", ");
                }
                sb.append("\", utranPositioningData=\"").append(utranPosDataInfo);
            } catch (MAPException e) {
                throw new RuntimeException(e);
            }
        }

        if (deferredMTLRResponseIndicator)
            sb.append("\", deferredMTLRResponseIndicator=\"").append(deferredMTLRResponseIndicator);

        if (cellIdOrSai != null) {
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
            if (saiPresent) {
                try {
                    sb.append("\", SAC=\"").append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode()));
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            } else {
                try {
                    sb.append("\", CI=\"").append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode())).append(", ");
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        if (accuracyFulfilmentIndicator != null)
            sb.append("\", accuracyFulfilmentIndicator=\"").append(accuracyFulfilmentIndicator);

        if (velocityEstimate != null) {
            sb.append("\", Velocity Estimate: velocity type=\"").append(velocityEstimate.getVelocityType());
            sb.append("\", horizontal speed=\"").append(velocityEstimate.getHorizontalSpeed());
            sb.append("\", horizontal speed uncertainty=\"").append(velocityEstimate.getUncertaintyHorizontalSpeed());
            sb.append("\", vertical speed=\"").append(velocityEstimate.getVerticalSpeed());
            sb.append("\", vertical speed uncertainty=\"").append(velocityEstimate.getUncertaintyVerticalSpeed());
            sb.append("\", bearing=\"").append(velocityEstimate.getVerticalSpeed());velocityEstimate.getBearing();
        }

        if (moLrShortCircuitIndicator)
            sb.append("\", moLrShortCircuitIndicator=\"").append(moLrShortCircuitIndicator);

        if (geranGANSSpositioningData != null) {
            try {
                Multimap<String, String> methodsAndGanssIds = geranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSIds();
                StringBuilder geranGANSSPosDataInfo = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndGanssIds.entries()) {
                    if (key != null || value != null)
                        geranGANSSPosDataInfo.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    geranGANSSPosDataInfo.append("Method=").append(key).append(", GANSSId=").append(value);
                }
                sb.append("\", GERAN GANSS positioning data=\"").append(geranGANSSPosDataInfo);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (utranGANSSpositioningData != null) {
            try {
                Multimap<String, String> methodsAndGanssIds = utranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSIds();
                StringBuilder utranGANSSPosDataInfo = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndGanssIds.entries()) {
                    if (key != null || value != null)
                        utranGANSSPosDataInfo.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    utranGANSSPosDataInfo.append("Method=").append(key).append(", GANSSId=").append(value);
                }
                sb.append("\", UTRAN GANSS positioning data=\"").append(utranGANSSPosDataInfo);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (targetServingNodeForHandover != null) {
            if (targetServingNodeForHandover.getMscNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(targetServingNodeForHandover.getMscNumber().getAddress());
            if (targetServingNodeForHandover.getSgsnNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(targetServingNodeForHandover.getSgsnNumber().getAddress());
            if (targetServingNodeForHandover.getMmeNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(Arrays.toString(targetServingNodeForHandover.getMmeNumber().getData()));
        }
        if (utranAdditionalPositioningData != null) {
            try {
                Multimap<String, String> methodsAndAddPosIds = utranAdditionalPositioningData.getUtranAdditionalPositioningMethodsAndIds();
                StringBuilder slrUtranAddPositioningData = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndAddPosIds.entries()) {
                    if (key != null || value != null)
                        slrUtranAddPositioningData.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    slrUtranAddPositioningData.append("Method=").append(key).append(", AddPosId=").append(value);
                }
                sb.append("\", UTRAN additional positioning data=\"").append(slrUtranAddPositioningData);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (utranBaroPressureMeas != null)
            sb.append(", UTRAN Barometric Pressure Measurement=\"").append(utranBaroPressureMeas);

        if (utranCivicAddress != null)
            sb.append(", UTRAN civic address=\"").append(Arrays.toString(utranCivicAddress.getData()));

        if (lcsReferenceNumber != null) {
            sb.append("\", lcsReferenceNumber=\"").append(lcsReferenceNumber);
        }

        return sb.toString();
    }

    private String createPSLRequestData(long dialogId, LocationType locationType, ISDNAddressString mlcNumber,
            LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
             LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
             boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList) {

        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=").append(dialogId).append("\",\n ");

        if (locationType != null) {
            sb.append("locationType=\"").append("\",\n ");
            if (locationType.getLocationEstimateType() != null) {
                sb.append("locationEstimateType=\"").append(locationType.getLocationEstimateType().getType()).append("\",\n ");
            }
            if (locationType.getDeferredLocationEventType() != null) {
                sb.append("deferredLocationEventType=\"").append(locationType.getDeferredLocationEventType()).append("\",\n ");
            }
        }

        if (mlcNumber != null) {
            if (mlcNumber.getAddress() != null) {
                sb.append("mlcNumber=\"").append(mlcNumber.getAddress()).append("\",\n ");
            }
        }

        sb.append("lcsClientID=\"").append(lcsClientID).append("\",\n ");

        if (privacyOverride)
            sb.append("privacyOverride=\"").append(privacyOverride).append("\",\n ");

        if (imsi != null) {
            sb.append("IMSI=\"").append(imsi.getData()).append("\",\n ");
        }

        if (msisdn != null) {
            sb.append("MSISDN=\"").append(msisdn.getAddress()).append("\",\n ");
        }

        if (imei != null) {
            sb.append("IMEI=\"").append(imei.getIMEI()).append("\",\n ");
        }

        if (lmsi != null)
            sb.append("LMSI=\"").append(lmsi).append("\",\n ");

        if (lcsPriority != null)
            sb.append("lcsPriority=\"").append(lcsPriority).append("\",\n ");

        if (lcsQoS != null) {
            sb.append("lcsQos=\"").append(lcsQoS).append("\",\n ");
            sb.append("lcsQosHorizontalAccuracy=\"");
            if (lcsQoS.getHorizontalAccuracy() != null)
                sb.append(lcsQoS.getHorizontalAccuracy().intValue()).append("\",\n ");
            sb.append("lcsQosVerticalAccuracy=\"");
            if (lcsQoS.getVerticalAccuracy() != null)
                sb.append(lcsQoS.getVerticalAccuracy().intValue()).append("\",\n ");
            sb.append("lcsQosResponseTimeCategory=\"");
            if (lcsQoS.getResponseTime() != null)
                sb.append(lcsQoS.getResponseTime().getResponseTimeCategory()).append("\",\n ");
            sb.append("lcsQosVerticalCoordinateRequest=\"");
            if (lcsQoS.getVerticalCoordinateRequest())
                sb.append(lcsQoS.getVerticalCoordinateRequest()).append("\",\n ");
        }

        if (supportedGADShapes != null) {
            sb.append("Supported GAD Shapes: EllipsoidArc=").append(supportedGADShapes.getEllipsoidArc())
                    .append(", Polygon").append(supportedGADShapes.getPolygon())
                    .append(", EllipsoidPointWithAltitudeAndUncertaintyEllipsoid").append(supportedGADShapes.getEllipsoidPointWithAltitudeAndUncertaintyEllipsoid())
                    .append(", EllipsoidPointWithAltitude").append(supportedGADShapes.getEllipsoidPointWithAltitude())
                    .append(", EllipsoidPointWithUncertaintyCircle").append(supportedGADShapes.getEllipsoidPointWithUncertaintyCircle())
                    .append(", EllipsoidPointWithUncertaintyEllipse").append(supportedGADShapes.getEllipsoidPointWithUncertaintyEllipse())
                    .append(", EllipsoidPoint").append(supportedGADShapes.getEllipsoidPoint()).append("\",\n ");
        }

        if (lcsReferenceNumber != null)
            sb.append("lcsReferenceNumber=\"").append(lcsReferenceNumber).append("\",\n ");

        if (lcsServiceTypeID != null)
            sb.append("lcsServiceTypeID=\"").append(lcsServiceTypeID).append("\",\n ");

        if (lcsCodeword != null)
            sb.append("lcsCodeword=\"").append(lcsCodeword).append("\",\n ");

        if (lcsPrivacyCheck != null)
            sb.append("lcsPrivacyCheck=\"").append(lcsPrivacyCheck).append("\",\n ");

        if (areaEventInfo != null) {
            sb.append("areaEventInfo=\"").append(areaEventInfo).append("\",\n ");
        }

        if (hgmlcAddress != null) {
            String hGmlcAddress = bytesToHexString(hgmlcAddress.getGSNAddressData());
            try {
                InetAddress address = InetAddress.getByAddress(DatatypeConverter.parseHexBinary(hGmlcAddress));
                hGmlcAddress = address.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            sb.append("\", H-GMLCAddress=\"").append(hGmlcAddress);
        }

        if (moLrShortCircuitIndicator)
            sb.append("moLrShortCircuitIndicator=\"").append(moLrShortCircuitIndicator).append("\",\n ");

        if (periodicLDRInfo != null) {
            sb.append("\"Periodic LDR Info, reporting amount=\"").append(periodicLDRInfo.getReportingAmount());
            sb.append("\"Periodic LDR Info, reporting interval=\"").append(periodicLDRInfo.getReportingInterval());
            if (periodicLDRInfo.getReportingOptionMilliseconds() != null) {
                sb.append("\"Periodic LDR Info, reporting amount ms=\"").append(
                        periodicLDRInfo.getReportingOptionMilliseconds().getReportingAmountMilliseconds());
                sb.append("\"Periodic LDR Info, reporting interval ms=\"").append(
                        periodicLDRInfo.getReportingOptionMilliseconds().getReportingIntervalMilliseconds());
            }
        }

        return sb.toString();
    }

    public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse pslResponse) {

        logger.debug("onProvideSubscriberLocationResponse");

        MAPDialogLsm curDialog = pslResponse.getMAPDialog();

        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
            "Rcvd: ProvideSubscriberLocationResponse", this.createPSLResponse(curDialog.getLocalDialogId(),
                        pslResponse.getLocationEstimate(), pslResponse.getGeranPositioningData(), pslResponse.getUtranPositioningData(),
                        pslResponse.getAgeOfLocationEstimate(), pslResponse.getAdditionalLocationEstimate(), pslResponse.getDeferredMTLRResponseIndicator(),
                        pslResponse.getCellIdOrSai(), pslResponse.getSaiPresent(), pslResponse.getAccuracyFulfilmentIndicator(),
                        pslResponse.getVelocityEstimate(), pslResponse.getMoLrShortCircuitIndicator(), pslResponse.getGeranGANSSpositioningData(),
                        pslResponse.getUtranGANSSpositioningData(), pslResponse.getTargetServingNodeForHandover(),
                        pslResponse.getUtranAdditionalPositioningData(), pslResponse.getUtranBaroPressureMeas(),
                        pslResponse.getUtranCivicAddress(), null), Level.INFO);
    }

    //*********************//
    //**** SLR methods ***//
    //*******************//
    @Override
    public String performSubscriberLocationReportRequest(Boolean refNum) {
        if (!isStarted) {
            return "The tester is not started";
        }

        if (refNum)
            return subscriberLocationReportRequest();
        else
            return subscriberLocationReportRequestNullRefNum();
    }

    private String subscriberLocationReportRequest() {
        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            Random rand = new Random();

            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm mapDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            TestLcsServerConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData();

            // SLR Mandatory parameters LCSEvent, LCSClientID & Network Node Number
            LCSEvent lcsEvent = LCSEvent.deferredmtlrResponse;

            LCSClientExternalID lcsClientExternalID = null;
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.anonymousLocation;
            String clientName = "545248";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "*123#";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            APN lcsAPN = null;
            try {
                lcsAPN = new APNImpl("internet.mnc002.mcc345.gprs");
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
            LCSRequestorID lcsRequestorID = new LCSRequestorIDImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(configData.getLcsClientType(), lcsClientExternalID, lcsClientInternalID,
                lcsClientName, lcsClientDialedByMS, lcsAPN, lcsRequestorID);

            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumber());
            LMSI lmsi = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 233, (byte) 140});
                    break;
                case 2:
                    lmsi = new LMSIImpl(new byte[]{113, (byte) 255, (byte) 172, (byte) 206});
                    break;
                case 3:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 235, 55});
                    break;
                case 4:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 231, (byte) 213});
                    break;
                default:
                    break;
            }
            String mscAddress = "598991800024";
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, mscAddress);
            String additionalMcsAddress = "598991800179";
            String sgsnAddress = "598992000077";
            ISDNAddressString additionalMcsNumber = null;
            ISDNAddressString sgsnNumber = null;
            AdditionalNumber additionalNumber = null;
            boolean gprsNodeIndicator = false;
            int addNumRandom = rand.nextInt(5) + 1;
            switch (addNumRandom) {
                case 1:
                    break;
                case 2:
                    gprsNodeIndicator = true;
                    break;
                case 3:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 4:
                    gprsNodeIndicator = true;
                    sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, sgsnAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 5:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    gprsNodeIndicator = true;
                    break;
                default:
                    additionalNumber = null; // not needed, just for being explicit about the default case
                    gprsNodeIndicator = false; // not needed, just for being explicit about the default case
                    break;
            }

            SupportedLCSCapabilitySets supportedLCSCapabilitySets = null, additionalLCSCapabilitySets = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            false, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 2:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 3:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                case 4:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                default:
                    break;

            }
            DiameterIdentity mmeName = null;
            DiameterIdentity aaaServerName = null;
            DiameterIdentity sgsnName = null;
            DiameterIdentity sgsnRealm = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 2:
                    sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    break;
                case 3:
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 4:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                default:
                    break;
            }
            LCSLocationInfo lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                    supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            // SLR optional parameters
            // -- one of msisdn or imsi is mandatory
            IMSI imsi = null;
            ISDNAddressString msisdn = null;
            if (rand.nextInt(2) + 1 == 1) {
                msisdn = mapParameterFactory.createISDNAddressString(
                        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                        getMSISDN());
            } else {
                imsi = mapParameterFactory.createIMSI(getIMSI());
            }

            IMEI imei = mapParameterFactory.createIMEI(getIMEI());

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
            SLRArgExtensionContainer slrArgExtensionContainer = null;
            if (naEsrkRequest) {
                SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(naEsrkRequest);
                slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
            }

            DeferredLocationEventType deferredLocationEventType;
            TerminationCause terminationCause;
            DeferredmtlrData deferredmtlrData;
            // the deferredmt-lrData parameter shall be included if and only if the lcs-Event indicates a deferredmt-lrResponse.
            PeriodicLDRInfo periodicLDRInfo = null; // This parameter refers to the periodic reporting interval and reporting amount of the deferred periodic location.
            Integer sequenceNumber = null; // SequenceNumber ::= INTEGER (1..8639999)
            // sequenceNumber parameter refers to the number of the periodic location reports completed.
            // The sequence number would be set to 1 in the first location report and increment by 1 for each new report.
            // When the number reaches the reporting amount value,
            // the H-GMLC (for a periodic MT-LR or a periodic MO-LR transfer to third party) will know the procedure is complete
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
                    int randReporting = rand.nextInt(5) + 1;
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

            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());

            MAPExtensionContainer extensionContainer = null;

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

            // 0x00=0000 0000 -> positioning data discriminator (BIT STRING (SIZE(4))): 0000 indicates the presence of the Positioning Data Set IE (that reports the usage of each non-GANSS method that was successfully used to obtain the location estimate) and the optional presence of the GANSS Positioning Data Set IE. It also indicates the optional presence of the Additional Positioning Data Set IE;
            // 0x00=0000 0000 -> C-ifDiscriminator=0
            // 0x28=0010 1000 -> 00101=>Method=Mobile Assisted GPS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x31=0011 0001 -> 00110=>Method=Mobile Based GPS, usage=1 (Attempted successfully: results not used to generate location - not used)
            // 0x40=0100 0000 -> 01000=>Method=U-TDOA, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x51=0101 0001 -> 01010=>Method=IPDL, usage=1 (Attempted successfully: results not used to generate location - not used)
            // 0x5c=0101 1100 -> 01011=>Method=RTT, usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x4b=0100 1011 -> 01000=>Method=OTDOA, usage 3 (Attempted successfully: results used to generate location)
            // 0x3a=0011 1010 -> 00111=>Method=Conventional GPS, usage=2 (results used to verify but not generate location - not used)
            // byte[] utranPosData = new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a};

            // 0x06 Length Indicator?
            // 0x8c=1000 1100 -> 10=>Method=Conventional, 001=>GANSSId=SBAS, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x02=0000 0010 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 10=>usage=2 (Attempted successfully: results used to verify but not generate location)
            // 0x11=0001 0001 -> 00=>Method=MS-Based, 010=>GANSSId=Modernized GPS, 01=>usage=1 (Attempted successfully: results not used to generate location)
            // 0x58=0101 1000 -> 01=>Method=MS-Assisted, 011=>GANSSId=QZSS, 00=usage=0 (Attempted unsuccessfully due to failure or interruption)
            // 0xe8=1110 1000 -> 11=>Method=Reserved, 101=>GANSSId=BDS, 00=usage0 (Attempted unsuccessfully due to failure or interruption)
            // 0x63=0110 0011 -> 01=>MS-Assisted, 100=>GANSSId=GLONASS, 11=usage3 (Attempted successfully: results used to generate location)
            // byte[] geranGANSSData = new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63};

            // 0x01=0000 0001 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 01=>usage=1 (Attempted successfully: results used to generate location)
            // 0x4a=0100 1010 -> 01=>Method=MS-Assisted, 100=>GANSSId=SBAS, 010=>usage=2 (Attempted successfully: results used to verify but not generate location - not used)
            // 0x90=1001 0000 -> 10=>Method=Conventional, 010=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x18=0001 1000 -> 00=>Method=MS-Based, 000=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0xdc=1110 1100 -> 11=>Method=Reserved, 101=>GANSSId=QZSS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x63=0110 0011 -> 01=>Method=MS-Assisted, 100=>GANSSId=GLONASS, usage=3 (Attempted successfully: results used to generate location)
            // byte[] utranGanssData = new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63};

            // 0x94=1001 0100 10=>Method=Standalone, 010=>GANSSId=Bluetooth, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x4b=0100 1011 00=>Method=MS-Assisted, AddPosId=GANSSId=WLAN, 011=>usage=3 (Attempted successfully: results used to generate location)
            // byte[] utranAddPosData = new byte[] {(byte) 0x94, 0x4b};

            switch (rand.nextInt(7) + 1) {
                case 1:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    break;
                case 2:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                    break;
                case 3:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    break;
                case 4:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    break;
                case 5:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {(byte) 0x94, 0x4b});
                    break;
                case 6:
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                    break;
                case 7:
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    break;
            }

            boolean isMsc = true;
            ServingNodeAddress targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, isMsc);

            Integer utranBaroPressureMeas = null;
            UtranCivicAddress utranCivicAddress = null;

            if (geranPositioningDataInfo == null || geranGanssPositioningData == null) {
                utranBaroPressureMeas = rand.nextInt(85000) + 30000; // UtranBaroPressureMeas ::= INTEGER (30000..115000)
                String civicAddressString = null;
                switch (rand.nextInt(7) + 1) {
                    case 1:
                        civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<civicAddress xml:lang=\"en-AU\"\n" +
                                "              xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "              xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "    <country>AU</country>\n" +
                                "    <A1>NSW</A1>\n" +
                                "    <A3>Wollongong</A3>\n" +
                                "    <A4>North Wollongong</A4>\n" +
                                "    <RD>Flinders</RD>\n" +
                                "    <STS>Street</STS>\n" +
                                "    <RDBR>Campbell Street</RDBR>\n" +
                                "    <LMK>Gilligan's Island</LMK>\n" +
                                "    <LOC>Corner</LOC>\n" +
                                "    <NAM>Video Rental Store</NAM>\n" +
                                "    <PC>2500</PC>\n" +
                                "    <ROOM>Westerns and Classics</ROOM>\n" +
                                "    <PLC>store</PLC>\n" +
                                "    <POBOX>Private Box 15</POBOX>\n" +
                                "    <cae:MP>248</cae:MP>\n" +
                                "    <cae:PN>22-109-689</cae:PN>\n" +
                                "</civicAddress>";
                        break;
                    case 2:
                        civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<civicAddress>\n" +
                                "    <country>US</country>\n" +
                                "    <A1>New York</A1>\n" +
                                "    <A3>New York</A3>\n" +
                                "    <A4>Broadway</A4>\n" +
                                "    <HNO>123</HNO>\n" +
                                "    <LOC>Suite 75</LOC>\n" +
                                "    <PC>10027-0401</PC>\n" +
                                "</civicAddress>";
                        break;
                    case 3:
                        civicAddressString = "<civicAddress xml:lang=\"en-AU\"\n" +
                                "     xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\">\n" +
                                "     <country>AU</country>\n" +
                                "     <A1>NSW</A1>\n" +
                                "     <A3>Wollongong</A3>\n" +
                                "     <A4>North Wollongong</A4>\n" +
                                "     <RD>Flinders</RD>\n" +
                                "     <STS>Street</STS>\n" +
                                "     <RDBR>Campbell Street</RDBR>\n" +
                                "     <LMK>Gilligan's Island</LMK>\n" +
                                "     <LOC>Corner</LOC>\n" +
                                "     <NAM>Video Rental Store</NAM>\n" +
                                "     <PC>2500</PC>\n" +
                                "     <ROOM>Westerns and Classics</ROOM>\n" +
                                "     <PLC>store</PLC>\n" +
                                "     <POBOX>Private Box 15</POBOX>\n" +
                                "   </civicAddress>";
                        break;
                    case 4:
                        civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                                "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "     <country>US</country>\n" +
                                "     <A1>CA</A1>\n" +
                                "     <A2>Sacramento</A2>\n" +
                                "     <RD>I5</RD>\n" +
                                "     <cae:MP>248</cae:MP>\n" +
                                "     <cae:PN>22-109-689</cae:PN>\n" +
                                "   </civicAddress>";
                        break;
                    case 5:
                        civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                                "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "     <country>US</country>\n" +
                                "     <A1>CA</A1>\n" +
                                "     <A2>Sacramento</A2>\n" +
                                "     <RD>Colorado</RD>\n" +
                                "     <HNO>223</HNO>\n" +
                                "     <cae:STP>Boulevard</cae:STP>\n" +
                                "     <cae:HNP>A</cae:HNP>\n" +
                                "   </civicAddress>";
                        break;
                    default:
                        break;
                }
                if (civicAddressString != null) {
                    byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
                    utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);
                }
            }

            mapDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk,
                    locationEstimate, getAgeOfLocationEstimate(), slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData,
                    getLCSReferenceNumber(), geranPositioningDataInfo, utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI, hGmlcAddress,
                    lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber,
                    periodicLDRInfo, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);
            logger.debug("Added SubscriberLocationReportRequest");

            mapDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportRequest", createSLRReqData(mapDialogLsm.getLocalDialogId(),
                    lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                    slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData, getLCSReferenceNumber(), geranPositioningDataInfo,
                    utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator,
                    accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator,
                    geranGanssPositioningData, utranGanssPositioningData, targetServingNodeForHandover,
                    utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception on addSubscriberLocationReportRequest: " + e;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return "subscriberLocationReportRequest sent";
    }

    private String subscriberLocationReportRequestNullRefNum() {
        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            Random rand = new Random();

            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm mapDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            //TestLcsServerConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData();

            // SLR Mandatory parameters LCSEvent, LCSClientID & Network Node Number
            LCSEvent lcsEvent = null;
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

            ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "444567");
            LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, null);
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.broadcastService;
            String clientName = "219023";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "*911#";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            APN lcsAPN = new APNImpl("e911");
            LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, null);

            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumber());
            LMSI lmsi = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 233, (byte) 140});
                    break;
                case 2:
                    lmsi = new LMSIImpl(new byte[]{113, (byte) 255, (byte) 172, (byte) 206});
                    break;
                case 3:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 235, 55});
                    break;
                case 4:
                    lmsi = new LMSIImpl(new byte[]{114, 2, (byte) 231, (byte) 213});
                    break;
                default:
                    break;
            }
            String mscAddress = "598991800024";
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, mscAddress);
            String additionalMcsAddress = "598991800179";
            String sgsnAddress = "598992000077";
            ISDNAddressString additionalMcsNumber = null;
            ISDNAddressString sgsnNumber = null;
            AdditionalNumber additionalNumber = null;
            boolean gprsNodeIndicator = false;
            int addNumRandom = rand.nextInt(5) + 1;
            switch (addNumRandom) {
                case 1:
                    break;
                case 2:
                    gprsNodeIndicator = true;
                    break;
                case 3:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 4:
                    gprsNodeIndicator = true;
                    sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, sgsnAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    break;
                case 5:
                    additionalMcsNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                            NumberingPlan.ISDN, additionalMcsAddress);
                    additionalNumber = new AdditionalNumberImpl(additionalMcsNumber, sgsnNumber);
                    gprsNodeIndicator = true;
                    break;
                default:
                    additionalNumber = null; // not needed, just for being explicit about the default case
                    gprsNodeIndicator = false; // not needed, just for being explicit about the default case
                    break;
            }

            SupportedLCSCapabilitySets supportedLCSCapabilitySets = null, additionalLCSCapabilitySets = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            false, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 2:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, false, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    break;
                case 3:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, false);
                    additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                case 4:
                    supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true,
                            true, true, true);
                    break;
                default:
                    break;

            }
            DiameterIdentity mmeName = null;
            DiameterIdentity aaaServerName = null;
            DiameterIdentity sgsnName = null;
            DiameterIdentity sgsnRealm = null;
            switch (rand.nextInt(10) + 1) {
                case 1:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 2:
                    sgsnName = new DiameterIdentityImpl("sgsn1B34.mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    sgsnRealm = new DiameterIdentityImpl("mnc001.mcc748.gprs".getBytes(StandardCharsets.UTF_8));
                    break;
                case 3:
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                case 4:
                    mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
                    break;
                default:
                    break;
            }
            LCSLocationInfo lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                    supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

            // SLR optional parameters
            ISDNAddressString msisdn = null;
            IMSI imsi = null;
            int msisdnOrImsi = rand.nextInt(10) + 1;
            // -- one of msisdn or imsi is mandatory
            if (msisdnOrImsi == 1) {
                long msisdnDigits = nextLong(59898000000L, 59899000000L);
                msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, String.valueOf(msisdnDigits));
            } else {
                long imsiDigits = nextLong(748020000000000L, 748030000000000L);
                imsi = new IMSIImpl(String.valueOf(imsiDigits));
            }

            long imeiDigits = nextLong(100710000000000L, 100720000000000L);
            IMEI imei = new IMEIImpl(String.valueOf(imeiDigits));

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

            SLRArgExtensionContainer slrArgExtensionContainer = null;
            if (naEsrkRequest) {
                SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(naEsrkRequest);
                slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
            }

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

            boolean moLrShortCircuitIndicator = false;

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

            GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, InetAddress.getByName("10.0.0.14").getAddress());

            Integer lcsServiceTypeID = null;
            boolean pseudonymIndicator = false;
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;

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

            // 0x00=0000 0000 -> positioning data discriminator (BIT STRING (SIZE(4))): 0000 indicates the presence of the Positioning Data Set IE (that reports the usage of each non-GANSS method that was successfully used to obtain the location estimate) and the optional presence of the GANSS Positioning Data Set IE. It also indicates the optional presence of the Additional Positioning Data Set IE;
            // 0x00=0000 0000 -> C-ifDiscriminator=0
            // 0x28=0010 1000 -> 00101=>Method=Mobile Assisted GPS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x31=0011 0001 -> 00110=>Method=Mobile Based GPS, usage=1 (Attempted successfully: results not used to generate location - not used)
            // 0x40=0100 0000 -> 01000=>Method=U-TDOA, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x51=0101 0001 -> 01010=>Method=IPDL, usage=1 (Attempted successfully: results not used to generate location - not used)
            // 0x5c=0101 1100 -> 01011=>Method=RTT, usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x4b=0100 1011 -> 01000=>Method=OTDOA, usage 3 (Attempted successfully: results used to generate location)
            // 0x3a=0011 1010 -> 00111=>Method=Conventional GPS, usage=2 (results used to verify but not generate location - not used)
            // byte[] utranPosData = new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a};

            // 0x06 Length Indicator?
            // 0x8c=1000 1100 -> 10=>Method=Conventional, 001=>GANSSId=SBAS, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x02=0000 0010 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 10=>usage=2 (Attempted successfully: results used to verify but not generate location)
            // 0x11=0001 0001 -> 00=>Method=MS-Based, 010=>GANSSId=Modernized GPS, 01=>usage=1 (Attempted successfully: results not used to generate location)
            // 0x58=0101 1000 -> 01=>Method=MS-Assisted, 011=>GANSSId=QZSS, 00=usage=0 (Attempted unsuccessfully due to failure or interruption)
            // 0xe8=1110 1000 -> 11=>Method=Reserved, 101=>GANSSId=BDS, 00=usage0 (Attempted unsuccessfully due to failure or interruption)
            // 0x63=0110 0011 -> 01=>MS-Assisted, 100=>GANSSId=GLONASS, 11=usage3 (Attempted successfully: results used to generate location)
            // byte[] geranGANSSData = new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63};

            // 0x01=0000 0001 -> 00=>Method=MS-Based, 000=>GANSSId=Galileo, 01=>usage=1 (Attempted successfully: results used to generate location)
            // 0x4a=0100 1010 -> 01=>Method=MS-Assisted, 100=>GANSSId=SBAS, 010=>usage=2 (Attempted successfully: results used to verify but not generate location - not used)
            // 0x90=1001 0000 -> 10=>Method=Conventional, 010=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x18=0001 1000 -> 00=>Method=MS-Based, 000=>GANSSId=Modernized GPS, 000=>usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0xdc=1110 1100 -> 11=>Method=Reserved, 101=>GANSSId=QZSS, usage=0 (Attempted unsuccessfully due to failure or interruption - not used)
            // 0x63=0110 0011 -> 01=>Method=MS-Assisted, 100=>GANSSId=GLONASS, usage=3 (Attempted successfully: results used to generate location)
            // byte[] utranGanssData = new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63};

            // 0x94=1001 0100 10=>Method=Standalone, 010=>GANSSId=Bluetooth, 100=>usage=4 (Attempted successfully: case where MS supports multiple mobile based positioning methods and the actual method or methods used by the MS cannot be determined)
            // 0x4b=0100 1011 00=>Method=MS-Assisted, AddPosId=GANSSId=WLAN, 011=>usage=3 (Attempted successfully: results used to generate location)
            // byte[] utranAddPosData = new byte[] {(byte) 0x94, 0x4b};

            switch (rand.nextInt(7) + 1) {
                case 1:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    break;
                case 2:
                    geranPositioningDataInfo = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                    break;
                case 3:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    break;
                case 4:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    break;
                case 5:
                    utranPositioningDataInfo = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x28, 0x31, 0x40, 0x51, 0x5c, 0x4b, 0x3a});
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {(byte) 0x94, 0x4b});
                    break;
                case 6:
                    geranGanssPositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x06, (byte) 0x8c, 0x02, 0x11, 0x58, (byte) 0xe8, 0x63});
                    break;
                case 7:
                    utranGanssPositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x4a, (byte) 0x90, 0x18, (byte) 0xec, 0x63});
                    break;
            }

            boolean isMsc = true;
            ServingNodeAddress targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, isMsc);

            Integer lcsReferenceNumber = null; // needs to be null for this case

            Integer sequenceNumber = null;

            PeriodicLDRInfo periodicLDRInfo = null;
            DeferredmtlrData deferredmtlrData = null;
            Integer utranBaroPressureMeas = null;
            UtranCivicAddress utranCivicAddress = null;

            if (geranPositioningDataInfo == null || geranGanssPositioningData == null) {
                utranBaroPressureMeas = rand.nextInt(85000) + 30000; // UtranBaroPressureMeas ::= INTEGER (30000..115000)
                String civicAddressString = null;
                switch (rand.nextInt(7) + 1) {
                    case 1:
                        civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<civicAddress xml:lang=\"en-AU\"\n" +
                                "              xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "              xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "    <country>AU</country>\n" +
                                "    <A1>NSW</A1>\n" +
                                "    <A3>Wollongong</A3>\n" +
                                "    <A4>North Wollongong</A4>\n" +
                                "    <RD>Flinders</RD>\n" +
                                "    <STS>Street</STS>\n" +
                                "    <RDBR>Campbell Street</RDBR>\n" +
                                "    <LMK>Gilligan's Island</LMK>\n" +
                                "    <LOC>Corner</LOC>\n" +
                                "    <NAM>Video Rental Store</NAM>\n" +
                                "    <PC>2500</PC>\n" +
                                "    <ROOM>Westerns and Classics</ROOM>\n" +
                                "    <PLC>store</PLC>\n" +
                                "    <POBOX>Private Box 15</POBOX>\n" +
                                "    <cae:MP>248</cae:MP>\n" +
                                "    <cae:PN>22-109-689</cae:PN>\n" +
                                "</civicAddress>";
                        break;
                    case 2:
                        civicAddressString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                "<civicAddress>\n" +
                                "    <country>US</country>\n" +
                                "    <A1>New York</A1>\n" +
                                "    <A3>New York</A3>\n" +
                                "    <A4>Broadway</A4>\n" +
                                "    <HNO>123</HNO>\n" +
                                "    <LOC>Suite 75</LOC>\n" +
                                "    <PC>10027-0401</PC>\n" +
                                "</civicAddress>";
                        break;
                    case 3:
                        civicAddressString = "<civicAddress xml:lang=\"en-AU\"\n" +
                                "     xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\">\n" +
                                "     <country>AU</country>\n" +
                                "     <A1>NSW</A1>\n" +
                                "     <A3>Wollongong</A3>\n" +
                                "     <A4>North Wollongong</A4>\n" +
                                "     <RD>Flinders</RD>\n" +
                                "     <STS>Street</STS>\n" +
                                "     <RDBR>Campbell Street</RDBR>\n" +
                                "     <LMK>Gilligan's Island</LMK>\n" +
                                "     <LOC>Corner</LOC>\n" +
                                "     <NAM>Video Rental Store</NAM>\n" +
                                "     <PC>2500</PC>\n" +
                                "     <ROOM>Westerns and Classics</ROOM>\n" +
                                "     <PLC>store</PLC>\n" +
                                "     <POBOX>Private Box 15</POBOX>\n" +
                                "   </civicAddress>";
                        break;
                    case 4:
                        civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                                "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "     <country>US</country>\n" +
                                "     <A1>CA</A1>\n" +
                                "     <A2>Sacramento</A2>\n" +
                                "     <RD>I5</RD>\n" +
                                "     <cae:MP>248</cae:MP>\n" +
                                "     <cae:PN>22-109-689</cae:PN>\n" +
                                "   </civicAddress>";
                        break;
                    case 5:
                        civicAddressString = "<civicAddress xml:lang=\"en-US\"\n" +
                                "        xmlns=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr\"\n" +
                                "        xmlns:cae=\"urn:ietf:params:xml:ns:pidf:geopriv10:civicAddr:ext\">\n" +
                                "     <country>US</country>\n" +
                                "     <A1>CA</A1>\n" +
                                "     <A2>Sacramento</A2>\n" +
                                "     <RD>Colorado</RD>\n" +
                                "     <HNO>223</HNO>\n" +
                                "     <cae:STP>Boulevard</cae:STP>\n" +
                                "     <cae:HNP>A</cae:HNP>\n" +
                                "   </civicAddress>";
                        break;
                    default:
                        break;
                }
                if (civicAddressString != null) {
                    byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
                    utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);
                }
            }

            mapDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate,
                    getAgeOfLocationEstimate(), slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningDataInfo,
                    utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                    velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);
            logger.debug("Added SubscriberLocationReportRequest");

            mapDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportRequest", createSLRReqData(mapDialogLsm.getLocalDialogId(),
                    lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                    slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningDataInfo,
                    utranPositioningDataInfo, cellGlobalIdOrServiceAreaIdOrLAI, hGmlcAddress,
                    lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber,
                    periodicLDRInfo, moLrShortCircuitIndicator, geranGanssPositioningData, utranGanssPositioningData,
                    targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception on addSubscriberLocationReportRequest: " + e;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        return "subscriberLocationReportRequest sent";
    }

    private String createSLRReqData(long dialogId, LCSEvent lcsEvent, LCSClientID lcsClientID,
            LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd,
            ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover, UtranAdditionalPositioningData utranAdditionalPositioningData,
            Integer utranBaroPressureMeas, UtranCivicAddress utranCivicAddress) {

        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", lcsEvent=\"").append(lcsEvent);
        sb.append("\", lcsClientID=\"").append(lcsClientID);
        sb.append("\", lcsLocationInfo=\"").append(lcsLocationInfo);
        sb.append("\", MSISDN=\"").append(msisdn);
        sb.append("\", IMSI=\"");
        if (imsi != null)
            sb.append(imsi.getData()).append(", ");
        sb.append("\", IMEI=\"").append(imei);
        sb.append("\", naESRD=\"").append(naEsrd);
        sb.append("\", naESRK=\"").append(naEsrk);
        if (locationEstimate != null) {
            sb.append("\", addLocationEstimate=\"");
            sb.append("\" Type of Shape=\"").append(locationEstimate.getTypeOfShape());
            if (locationEstimate.getLatitude() > -90 && locationEstimate.getLatitude() < 90) {
                sb.append("\", latitude=\"");
                sb.append(locationEstimate.getLatitude()).append(", ");
            }
            if (locationEstimate.getLongitude() > -180 && locationEstimate.getLongitude() < 180) {
                sb.append("\", longitude=\"");
                sb.append(locationEstimate.getLongitude()).append(", ");
            }
            if (locationEstimate.getTypeOfShape() != null) {
                sb.append("\", typeOfShape=\"");
                sb.append(locationEstimate.getTypeOfShape()).append(", ");
            }
            if (locationEstimate.getUncertainty() >= 0 && locationEstimate.getUncertainty() < 128) {
                sb.append("\", uncertainty=\"");
                sb.append(locationEstimate.getUncertainty()).append(", ");
            }
            if (locationEstimate.getAltitude() > Integer.MIN_VALUE && locationEstimate.getAltitude() < Integer.MAX_VALUE) {
                sb.append("\", altitude=\"");
                sb.append(locationEstimate.getAltitude()).append(", ");
            }
            if (locationEstimate.getUncertaintyAltitude() > Double.MIN_VALUE && locationEstimate.getUncertaintyAltitude() < Double.MAX_VALUE) {
                sb.append("\", uncertaintyAltitude=\"");
                sb.append(locationEstimate.getUncertaintyAltitude()).append(", ");
            }
            if (locationEstimate.getConfidence() > Integer.MIN_VALUE && locationEstimate.getConfidence() < Integer.MAX_VALUE) {
                sb.append("\", confidence=\"");
                sb.append(locationEstimate.getConfidence()).append(", ");
            }
            if (locationEstimate.getInnerRadius() > Integer.MIN_VALUE && locationEstimate.getInnerRadius() < Integer.MAX_VALUE) {
                sb.append("\", innerRadius=\"");
                sb.append(locationEstimate.getInnerRadius()).append(", ");
            }
            if (locationEstimate.getUncertaintyRadius() > Double.MIN_VALUE && locationEstimate.getUncertaintyRadius() < Double.MAX_VALUE) {
                sb.append("\", uncertaintyRadius=\"");
                sb.append(locationEstimate.getUncertaintyRadius()).append(", ");
            }
            if (locationEstimate.getUncertaintySemiMajorAxis() > Double.MIN_VALUE && locationEstimate.getUncertaintySemiMajorAxis() < Double.MAX_VALUE) {
                sb.append("\", uncertaintySemiMajorAxis=\"");
                sb.append(locationEstimate.getUncertaintySemiMajorAxis()).append(", ");
            }
            if (locationEstimate.getUncertaintySemiMinorAxis() > Double.MIN_VALUE && locationEstimate.getUncertaintySemiMinorAxis() < Double.MAX_VALUE) {
                sb.append("\", uncertaintySemiMinorAxis=\"");
                sb.append(locationEstimate.getUncertaintySemiMinorAxis()).append(", ");
            }
            if (locationEstimate.getAngleOfMajorAxis() > Double.MIN_VALUE && locationEstimate.getAngleOfMajorAxis() < Double.MAX_VALUE) {
                sb.append("\", angleOfMajorAxis=\"");
                sb.append(locationEstimate.getAngleOfMajorAxis()).append(", ");
            }
            if (locationEstimate.getOffsetAngle() > Double.MIN_VALUE && locationEstimate.getOffsetAngle() < Double.MAX_VALUE) {
                sb.append("\", offsetAngle=\"");
                sb.append(locationEstimate.getOffsetAngle()).append(", ");
            }
            if (locationEstimate.getIncludedAngle() > Double.MIN_VALUE && locationEstimate.getIncludedAngle() < Double.MAX_VALUE) {
                sb.append("\", includedAngle=\"");
                sb.append(locationEstimate.getIncludedAngle()).append(", ");
            }
        }
        sb.append("\", ageOfLocationEstimate=\"").append(ageOfLocationEstimate);
        if (slrArgExtensionContainer != null)
            if (slrArgExtensionContainer.getSlrArgPcsExtensions() != null)
                sb.append("\", slrArgExtensionContainer=\"").append(slrArgExtensionContainer.getSlrArgPcsExtensions().getNaEsrkRequest());

        if (addLocationEstimate != null) {
            sb.append("\", addLocationEstimate=\"");
            sb.append("\" Type of Shape=\"").append(addLocationEstimate.getTypeOfShape());
            byte[] addLocationEstimateByteArray = addLocationEstimate.getData();
            if (addLocationEstimate.getTypeOfShape() == TypeOfShape.Polygon) {
                PolygonImpl polygon = new PolygonImpl(addLocationEstimateByteArray);
                sb.append(polygon);
            }
        }

        if (lcsReferenceNumber != null)
            sb.append("\", lcsReferenceNumber=\"").append(lcsReferenceNumber);

        if (geranPositioningData != null) {
            try {
                ArrayList<String> methods = geranPositioningData.getLocationGeneratedPositioningMethods();
                StringBuilder geranPositioningDataInfo = new StringBuilder();
                int metCounter = 0;
                for (String met : methods) {
                    metCounter++;
                    geranPositioningDataInfo.append(met);
                    if (methods.size() != metCounter)
                        geranPositioningDataInfo.append(", ");
                }
                sb.append("\", geranPositioningData=\"").append(geranPositioningDataInfo);
            } catch (MAPException e) {
                throw new RuntimeException(e);
            }
        }

        if (utranPositioningData != null) {
            try {
                ArrayList<String> methods = utranPositioningData.getUtranLocationGeneratedPositioningMethods();
                StringBuilder utranPosDataInfo = new StringBuilder();
                int metCounter = 0;
                for (String met : methods) {
                    metCounter++;
                    utranPosDataInfo.append(met);
                    if (methods.size() != metCounter)
                        utranPosDataInfo.append(", ");
                }
                sb.append("\", utranPositioningData=\"").append(utranPosDataInfo);
            } catch (MAPException e) {
                throw new RuntimeException(e);
            }
        }

        if (deferredmtlrData != null) {
            sb.append("\", deferredmtlrData=\"");
            if (deferredmtlrData.getDeferredLocationEventType() != null) {
                sb.append("\", ms available=\"").append(deferredmtlrData.getDeferredLocationEventType().getMsAvailable());
                sb.append("\" being inside area=\"").append(deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea());
                sb.append("\", entering into area=\"").append(deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea());
                sb.append("\", leaving into area=\"").append(deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea());
                sb.append("\", periodic LDR=\"").append(deferredmtlrData.getDeferredLocationEventType().getPeriodicLDR());
            }
            if (deferredmtlrData.getLCSLocationInfo() != null) {
                sb.append("\", LCS location info=\"");
                if (deferredmtlrData.getLCSLocationInfo().getNetworkNodeNumber() != null) {
                    sb.append("\", Network node number=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getNetworkNodeNumber().getAddress());
                }
                if (deferredmtlrData.getLCSLocationInfo().getGprsNodeIndicator()) {
                    sb.append("\", GPRS node indicator=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getGprsNodeIndicator());
                }
                if (deferredmtlrData.getLCSLocationInfo().getAdditionalNumber() != null) {
                    sb.append("\", additional number=\"");
                    if (deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getMSCNumber() != null) {
                        sb.append("\", MSC number=\"");
                        sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getMSCNumber().getAddress());
                    }
                    if (deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getSGSNNumber() != null) {
                        sb.append("\", SGSN number=\"");
                        sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalNumber().getSGSNNumber().getAddress());
                    }
                }
                if (deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets() != null) {
                    sb.append("\", Supported LCS capability sets=\"");
                    sb.append("\" Release 98_99=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
                    sb.append("\" Release 4=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
                    sb.append("\" Release 5=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
                    sb.append("\" Release 6=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
                    sb.append("\" Release 7=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
                }
                if (deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets() != null) {
                    sb.append("\", Additional supported LCS capability sets=\"");
                    sb.append("\" Release 98_99=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99());
                    sb.append("\" Release 4=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease4());
                    sb.append("\" Release 5=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease5());
                    sb.append("\" Release 6=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease6());
                    sb.append("\" Release 7=\"");
                    sb.append(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease7());
                }
                if (deferredmtlrData.getLCSLocationInfo().getMmeName() != null) {
                    sb.append("\", MME name=\"").append(deferredmtlrData.getLCSLocationInfo().getMmeName());
                }
                if (deferredmtlrData.getLCSLocationInfo().getSgsnName() != null) {
                    sb.append("\", SGSN name=\"").append(deferredmtlrData.getLCSLocationInfo().getSgsnName());
                }
                if (deferredmtlrData.getLCSLocationInfo().getSgsnRealm() != null) {
                    sb.append("\", SGSN realm=\"").append(deferredmtlrData.getLCSLocationInfo().getSgsnRealm());
                }
                if (deferredmtlrData.getLCSLocationInfo().getAaaServerName() != null) {
                    sb.append("\", AAA server name=\"").append(deferredmtlrData.getLCSLocationInfo().getAaaServerName());
                }
            }
        }

        if (cellIdOrSai != null) {
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
            if (saiPresent) {
                try {
                    sb.append("\", SAC=\"").append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode()));
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            } else {
                try {
                    sb.append("\", CI=\"").append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode())).append(", ");
                } catch (MAPException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        if (hgmlcAddress != null) {
            String hGmlcAddress = bytesToHexString(hgmlcAddress.getGSNAddressData());
            try {
                InetAddress address = InetAddress.getByAddress(DatatypeConverter.parseHexBinary(hGmlcAddress));
                hGmlcAddress = address.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            sb.append("\", H-GMLCAddress=\"").append(hGmlcAddress);
        }

        if (lcsServiceTypeID != null)
            sb.append("\", lcsServiceTypeID=\"").append(lcsServiceTypeID);

        if (pseudonymIndicator)
            sb.append("\", pseudonymIndicator=\"").append(pseudonymIndicator);

        if (accuracyFulfilmentIndicator != null)
            sb.append("\", accuracyFulfilmentIndicator=\"").append(accuracyFulfilmentIndicator);

        if (velocityEstimate != null) {
            sb.append("\", Velocity Estimate: velocity type=\"").append(velocityEstimate.getVelocityType());
            sb.append("\", horizontal speed=\"").append(velocityEstimate.getHorizontalSpeed());
            sb.append("\", horizontal speed uncertainty=\"").append(velocityEstimate.getUncertaintyHorizontalSpeed());
            sb.append("\", vertical speed=\"").append(velocityEstimate.getVerticalSpeed());
            sb.append("\", vertical speed uncertainty=\"").append(velocityEstimate.getUncertaintyVerticalSpeed());
            sb.append("\", bearing=\"").append(velocityEstimate.getVerticalSpeed());velocityEstimate.getBearing();
        }

        if (sequenceNumber != null)
            sb.append("\", sequenceNumber=\"").append(sequenceNumber);

        if (periodicLDRInfo != null) {
            sb.append("\"Periodic LDR Info, reporting amount=\"").append(periodicLDRInfo.getReportingAmount());
            sb.append("\"Periodic LDR Info, reporting interval=\"").append(periodicLDRInfo.getReportingInterval());
            if (periodicLDRInfo.getReportingOptionMilliseconds() != null) {
                sb.append("\"Periodic LDR Info, reporting amount ms=\"").append(
                        periodicLDRInfo.getReportingOptionMilliseconds().getReportingAmountMilliseconds());
                sb.append("\"Periodic LDR Info, reporting interval ms=\"").append(
                        periodicLDRInfo.getReportingOptionMilliseconds().getReportingIntervalMilliseconds());
            }
        }

        if (moLrShortCircuitIndicator)
            sb.append("\", moLrShortCircuitIndicator=\"").append(moLrShortCircuitIndicator);

        if (geranGANSSpositioningData != null) {
            try {
                Multimap<String, String> methodsAndGanssIds = geranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSIds();
                StringBuilder geranGANSSPosDataInfo = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndGanssIds.entries()) {
                    if (key != null || value != null)
                        geranGANSSPosDataInfo.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    geranGANSSPosDataInfo.append("Method=").append(key).append(", GANSSId=").append(value);
                }
                sb.append("\", GERAN GANSS positioning data=\"").append(geranGANSSPosDataInfo);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (utranGANSSpositioningData != null) {
            try {
                Multimap<String, String> methodsAndGanssIds = utranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSIds();
                StringBuilder utranGANSSPosDataInfo = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndGanssIds.entries()) {
                    if (key != null || value != null)
                        utranGANSSPosDataInfo.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    utranGANSSPosDataInfo.append("Method=").append(key).append(", GANSSId=").append(value);
                }
                sb.append("\", UTRAN GANSS positioning data=\"").append(utranGANSSPosDataInfo);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (targetServingNodeForHandover != null) {
            if (targetServingNodeForHandover.getMscNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(targetServingNodeForHandover.getMscNumber().getAddress());
            if (targetServingNodeForHandover.getSgsnNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(targetServingNodeForHandover.getSgsnNumber().getAddress());
            if (targetServingNodeForHandover.getMmeNumber() != null)
                sb.append("\", targetServingNodeForHandover=\"").append(Arrays.toString(targetServingNodeForHandover.getMmeNumber().getData()));
        }
        if (utranAdditionalPositioningData != null) {
            try {
                Multimap<String, String> methodsAndAddPosIds = utranAdditionalPositioningData.getUtranAdditionalPositioningMethodsAndIds();
                StringBuilder slrUtranAddPositioningData = new StringBuilder();
                String key = null, value = null;
                for (Map.Entry<String, String> entry : methodsAndAddPosIds.entries()) {
                    if (key != null || value != null)
                        slrUtranAddPositioningData.append("; ");
                    key = entry.getKey();
                    value = entry.getValue();
                    slrUtranAddPositioningData.append("Method=").append(key).append(", AddPosId=").append(value);
                }
                sb.append("\", UTRAN additional positioning data=\"").append(slrUtranAddPositioningData);
            } catch (MAPException e) {
                logger.error(e.getMessage());
            }
        }

        if (utranBaroPressureMeas != null)
            sb.append(", UTRAN Barometric Pressure Measurement=\"").append(utranBaroPressureMeas);

        if (utranCivicAddress != null)
            sb.append(", UTRAN civic address=\"").append(Arrays.toString(utranCivicAddress.getData()));

        return sb.toString();
    }

    private String createSLRResData(long dialogId, String address) {
        return "dialogId=" +
            dialogId +
            ", naESRD=\"" +
            address +
            "\"";
    }

    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {
        logger.debug("onSubscriberLocationReportRequest");
        this.countMapLcsReq++;
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = subscriberLocationReportRequestIndication.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SubscriberLocationReportRequest",
            createSLRReqData(curDialog.getLocalDialogId(), subscriberLocationReportRequestIndication.getLCSEvent(),
                    subscriberLocationReportRequestIndication.getLCSClientID(),
                    subscriberLocationReportRequestIndication.getLCSLocationInfo(),
                    subscriberLocationReportRequestIndication.getMSISDN(),
                    subscriberLocationReportRequestIndication.getIMSI(),
                    subscriberLocationReportRequestIndication.getIMEI(),
                    subscriberLocationReportRequestIndication.getNaESRD(),
                    subscriberLocationReportRequestIndication.getNaESRK(),
                    subscriberLocationReportRequestIndication.getLocationEstimate(),
                    subscriberLocationReportRequestIndication.getAgeOfLocationEstimate(),
                    subscriberLocationReportRequestIndication.getSLRArgExtensionContainer(),
                    subscriberLocationReportRequestIndication.getAdditionalLocationEstimate(),
                    subscriberLocationReportRequestIndication.getDeferredmtlrData(),
                    subscriberLocationReportRequestIndication.getLCSReferenceNumber(),
                    subscriberLocationReportRequestIndication.getGeranPositioningData(),
                    subscriberLocationReportRequestIndication.getUtranPositioningData(),
                    subscriberLocationReportRequestIndication.getCellGlobalIdOrServiceAreaIdOrLAI(),
                    subscriberLocationReportRequestIndication.getHGMLCAddress(),
                    subscriberLocationReportRequestIndication.getLCSServiceTypeID(),
                    subscriberLocationReportRequestIndication.getSaiPresent(),
                    subscriberLocationReportRequestIndication.getPseudonymIndicator(),
                    subscriberLocationReportRequestIndication.getAccuracyFulfilmentIndicator(),
                    subscriberLocationReportRequestIndication.getVelocityEstimate(),
                    subscriberLocationReportRequestIndication.getSequenceNumber(),
                    subscriberLocationReportRequestIndication.getPeriodicLDRInfo(),
                    subscriberLocationReportRequestIndication.getMoLrShortCircuitIndicator(),
                    subscriberLocationReportRequestIndication.getGeranGANSSpositioningData(),
                    subscriberLocationReportRequestIndication.getUtranGANSSpositioningData(),
                    subscriberLocationReportRequestIndication.getTargetServingNodeForHandover(),
                    subscriberLocationReportRequestIndication.getUtranAdditionalPositioningData(),
                    subscriberLocationReportRequestIndication.getUtranBaroPressureMeas(),
                    subscriberLocationReportRequestIndication.getUtranCivicAddress()),
                Level.INFO);

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
                createSLRResData(curDialog.getLocalDialogId(), getNaESRDAddress()), Level.INFO);

        } catch (MAPException e) {
            logger.debug("Exception on addSubscriberLocationReportResponse: " + e);
        }
    }

    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {
        logger.debug("onSubscriberLocationReportResponse");
        this.countMapLcsResp++;
        String naESRD = null;
        if (subscriberLocationReportResponseIndication.getNaESRD() != null)
            naESRD = subscriberLocationReportResponseIndication.getNaESRD().getAddress();
        this.testerHost.sendNotif(SOURCE_NAME,
            "Rcvd: SubscriberLocationReportResponse", this.createSLRResData(subscriberLocationReportResponseIndication.getInvokeId(), naESRD), Level.INFO);
    }

    /*
     * HLR SCCP Address creation
     */
    private SccpAddress getHLRSCCPAddress(String address) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(address, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int hlrSsn = 6;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, hlrSsn);
    }

    /*
     * VLR SCCP Address creation
     */
    private SccpAddress getVLRSCCPAddress(String address) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(address, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int vlrSsn = 7;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, vlrSsn);
    }

    /*
     * MSC SCCP Address creation
     */
    private SccpAddress getMSCSCCPAddress(String address) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(address, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int mscSsn = 8;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, mscSsn);
    }

    /*
     * SGSN SCCP Address creation
     */
    private SccpAddress getSGSNSCCPAddress(String address) {
        ParameterFactory sccpParam = new ParameterFactoryImpl();
        int translationType = 0; // Translation Type = 0 : Unknown
        EncodingScheme encodingScheme = null;
        GlobalTitle gt = sccpParam.createGlobalTitle(address, translationType, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, encodingScheme, NatureOfAddress.INTERNATIONAL);
        int sgsnSsn = 149;
        return sccpParam.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, translationType, sgsnSsn);
    }

    private static String bytesToHexString(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //**********************************************************//
    //*** Common methods for MAP LSM operations' attributes ***//
    //********************************************************//
    @Override
    public String getMlcNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMlcNumber();
    }

    @Override
    public void setMlcNumber(String mlcNumber) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMlcNumber(mlcNumber);
    }

    @Override
    public String getNetworkNodeNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNetworkNodeNumber();
    }

    @Override
    public void setNetworkNodeNumber(String data) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNetworkNodeNumber(data);
        this.testerHost.markStore();
    }

    // PSL Request
    @Override
    public Double getLocationEstimateLatitude() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimate().getLatitude();
    }

    @Override
    public void setLocationEstimateLatitude(Double locationEstimateLatitude) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLatitude(locationEstimateLatitude);
        this.testerHost.markStore();
    }

    @Override
    public Double getLocationEstimateLongitude() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimate().getLongitude();
    }

    @Override
    public void setLocationEstimateLongitude(Double locationEstimateLongitude) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLongitude(locationEstimateLongitude);
        this.testerHost.markStore();
    }

    @Override
    public LocationEstimateTypeEnumerated getLocEstimateType() {
        return new LocationEstimateTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimateType().getType());
    }

    @Override
    public void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate) {
        this.testerHost.getConfigurationData().
            getTestLcsServerConfigurationData().setLocationEstimateType(LocationEstimateType.getLocationEstimateType(locEstimate.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public TypeOfShapeEnumerated getTypeOfShape() {
        return new TypeOfShapeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getTypeOfShape().getCode());
    }

    @Override
    public void setTypeOfShapeEnumerated(TypeOfShapeEnumerated typeOfShape) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setTypeOfShape(TypeOfShape.valueOf(typeOfShape.toString()));
        this.testerHost.markStore();
    }

    @Override
    public Integer getLcsServiceTypeID() {
        return this.testerHost.getConfigurationData().
            getTestLcsServerConfigurationData().getLcsServiceTypeID();
    }

    @Override
    public void setLcsServiceTypeID(Integer lcsServiceTypeID) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLcsServiceTypeID(lcsServiceTypeID);
        this.testerHost.markStore();
    }

    @Override
    public LCSClientTypeEnumerated getLcsClientTypeEnumerated() {
        return new LCSClientTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLcsClientType().getType());
    }

    @Override
    public void setLcsClientTypeEnumerated(LCSClientTypeEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLcsClientType(LCSClientType.getLCSClientType(val.intValue()));
        this.testerHost.markStore();
    }


    @Override
    public void setCodeWordUSSDString(String codeWordUSSDString) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCodeWordUSSDString(codeWordUSSDString);
        this.testerHost.markStore();
    }

    @Override
    public String getCodeWordUSSDString() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCodeWordUSSDString();
    }

    @Override
    public void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCallSessionUnrelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated() {
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCallSessionUnrelated().getAction());
    }

    @Override
    public void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCallSessionRelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionRelated() {
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCallSessionRelated().getAction());
    }

    @Override
    public boolean getMoLrShortCircuitIndicator() {
        return this.testerHost.getConfigurationData().
            getTestLcsServerConfigurationData().getMoLrShortCircuitIndicator();
    }

    @Override
    public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
        this.testerHost.markStore();
    }

    @Override
    public LCSEventType getLCSEventType() {
        return new LCSEventType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent().getEvent());
    }

    @Override
    public void setLCSEventType(LCSEventType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSEvent(LCSEvent.getLCSEvent(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public LCSEvent getLCSEvent() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent();
    }

    @Override
    public void setLCSEvent(LCSEvent lcsEvent) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSEvent(lcsEvent);
        this.testerHost.markStore();
    }

    @Override
    public Integer getCellId() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCellId();
    }

    @Override
    public void setCellId(Integer cellId) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCellId(cellId);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLAC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLAC();
    }

    @Override
    public void setLAC(Integer lac) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLAC(lac);
        this.testerHost.markStore();
    }

    @Override
    public Integer getMNC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMNC();
    }

    @Override
    public void setMNC(Integer mnc) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMNC(mnc);
        this.testerHost.markStore();
    }

    @Override
    public Integer getMCC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMCC();
    }

    @Override
    public void setMCC(Integer mcc) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMCC(mcc);
        this.testerHost.markStore();
    }

    @Override
    public Integer getAgeOfLocationEstimate() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAgeOfLocationEstimate();
    }

    @Override
    public void setAgeOfLocationEstimate(Integer ageLocationEstimate) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAgeOfLocationEstimate(ageLocationEstimate);
        this.testerHost.markStore();
    }

    @Override
    public String getHGMLCAddress() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getHGMLCAddress();
    }

    @Override
    public void setHGMLCAddress(String hgmlcAddress) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setHGMLCAddress(hgmlcAddress);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLCSReferenceNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSReferenceNumber();
    }

    @Override
    public void setLCSReferenceNumber(Integer lcsReferenceNumber) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSReferenceNumber(lcsReferenceNumber);
        this.testerHost.markStore();
    }

    @Override
    public String getMSISDN() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMSISDN();
    }

    @Override
    public void setMSISDN(String msisdn) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMSISDN(msisdn);
        this.testerHost.markStore();
    }

    @Override
    public String getLMSI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLMSI();
    }

    @Override
    public void setLMSI(String lmsi) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLMSI(lmsi);
        this.testerHost.markStore();
    }

    @Override
    public String getIMEI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIMEI();
    }

    @Override
    public void setIMEI(String imei) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIMEI(imei);
        this.testerHost.markStore();
    }

    @Override
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIMSI();
    }

    @Override
    public void setIMSI(String imsi) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIMSI(imsi);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getNumberingPlan() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlan();
    }

    @Override
    public void setNumberingPlan(String numPlan) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNumberingPlan(numPlan);
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getNumberingPlanType() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType().getIndicator());
    }

    @Override
    public void setNumberingPlanType(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNumberingPlanType(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void setAreaType(AreaTypeEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAreaType(AreaType.getAreaType(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public AreaTypeEnumerated getAreaType() {
        return new AreaTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAreaType().getType());
    }

    @Override
    public OccurrenceInfoEnumerated getOccurrenceInfo() {
        return new OccurrenceInfoEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getOccurrenceInfo().getInfo());
    }

    @Override
    public void setOccurrenceInfo(OccurrenceInfoEnumerated occurrenceInfo) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setOccurrenceInfo(OccurrenceInfo.getOccurrenceInfo(occurrenceInfo.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public Integer getIntervalTime() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIntervalTime();
    }

    @Override
    public void setIntervalTime(Integer intervalTime) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIntervalTime(intervalTime);
        this.testerHost.markStore();
    }

    @Override
    public void setReportingAmount(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setReportingAmount(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getReportingAmount() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getReportingAmount();
    }

    @Override
    public void setReportingInterval(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setReportingInterval(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getReportingInterval() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getReportingInterval();
    }

    @Override
    public void setDataCodingScheme(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setDataCodingScheme(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getDataCodingScheme() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getDataCodingScheme();
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }

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
    public void putLCSEventType(String val) {
        LCSEventType x = LCSEventType.createInstance(val);
        this.setLCSEventType(x);
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

    //*********************************************************//
    //*** Tester Host MAP LSM operations' reaction methods ***//
    //*******************************************************//

    @Override
    public SRIforLCSReaction getSRIforLCSReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSriForLCSReaction();
    }

    @Override
    public String getSRIforLCSReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSriForLCSReaction().toString();
    }

    @Override
    public void setSRIforLCSReaction(SRIforLCSReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setSriForLCSReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putSRIforLCSReaction(String val) {
        SRIforLCSReaction x = SRIforLCSReaction.createInstance(val);
        this.setSRIforLCSReaction(x);
    }

    @Override
    public PSLReaction getPSLReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getPslReaction();
    }

    @Override
    public String getPSLReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getPslReaction().toString();
    }

    @Override
    public void setPSLReaction(PSLReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setPslReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putPSLReaction(String val) {
        PSLReaction x = PSLReaction.createInstance(val);
        this.setPSLReaction(x);
    }

    @Override
    public SLRReaction getSLRReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSlrReaction();
    }

    @Override
    public String getSLRReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSlrReaction().toString();
    }

    @Override
    public void setSLRReaction(SLRReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setSlrReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putSLRReaction(String val) {
        SLRReaction x = SLRReaction.createInstance(val);
        this.setSLRReaction(x);
    }


}
