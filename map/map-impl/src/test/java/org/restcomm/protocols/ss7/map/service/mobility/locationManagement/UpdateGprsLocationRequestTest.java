package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ExtSupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SMSRegisterRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class UpdateGprsLocationRequestTest {

    private byte[] getData() {
        return new byte[] { 48, -127, -105, 4, 3, 17, 33, 34, 4, 4, -111, 34, 34, -8, 4, 6, 23, 5, 38, 48, 81, 5, 48, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -96, 43, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 0, -126, 0, -125, 6,
                23, 5, 38, 48, 81, 5, -92, 6, -128, 4, 33, 67, 33, 67, -91, 4, -127, 2, 5, -32, -122, 0, -121, 0, -120, 1, 2,
                -119, 0, -118, 0, -117, 0, -116, 0, -115, 0, -114, 1, 1 };
    }

    private byte[] getData1() {
        return new byte[] { 48, (byte) 0x81,
                (byte) 0xf3, 0x04, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65,
                0x28, (byte) 0x85, (byte) 0xf3, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
                0x01, (byte) 0x94, 0x00, 0x00, 0x04, 0x06, 0x17, 0x05,
                0x26, 0x30, 0x51, 0x05, (byte) 0xa0, 0x31, (byte) 0xa2, 0x02,
                (byte) 0x80, 0x00, (byte) 0x83, 0x00, (byte) 0x84, 0x02, 0x04, (byte) 0xe0,
                (byte) 0x85, 0x02, 0x03, (byte) 0xf0, (byte) 0x86, 0x02, 0x01, 0x0e,
                (byte) 0x87, 0x00, (byte) 0x88, 0x02, 0x02, (byte) 0xdc, (byte) 0x89, 0x06,
                0x00, 0x00, 0x1f, (byte) 0xfe, 0x3f, (byte) 0xff, (byte) 0x8a, 0x00,
                (byte) 0x8b, 0x01, (byte) 0xff, (byte) 0x8c, 0x00, (byte) 0x8e, 0x00, (byte) 0x8f,
                0x00, (byte) 0x90, 0x00, (byte) 0x91, 0x02, 0x07, (byte) 0x80, (byte) 0x81,
                0x00, (byte) 0x83, 0x06, 0x17, 0x05, 0x26, 0x30, 0x51,
                0x05, (byte) 0xa4, 0x0a, (byte) 0x80, 0x08, 0x53, 0x06, 0x42,
                (byte) 0x80, 0x61, 0x35, 0x02, (byte) 0xf0, (byte) 0xa5, 0x04, (byte) 0x81,
                0x02, 0x05, (byte) 0xe0, (byte) 0x86, 0x00, (byte) 0x88, 0x01, 0x00,
                (byte) 0x89, 0x00, (byte) 0x8a, 0x00, (byte) 0x8b, 0x00, (byte) 0x8c, 0x00,
                (byte) 0x8d, 0x00, (byte) 0x8e, 0x01, 0x01, (byte) 0xaf, 0x0a, 0x04,
                0x03, 0x62, (byte) 0xf2, 0x10, 0x04, 0x03, 0x62, (byte) 0x92,
                (byte) 0x99, (byte) 0x90, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94,
                0x00, 0x10, (byte) 0x91, 0x01, 0x02, (byte) 0x92, 0x00, (byte) 0x93,
                0x2c, 0x6d, 0x6d, 0x65, 0x2e, 0x32, 0x30, 0x2e,
                0x6d, 0x61, 0x67, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x94, 0x21, 0x65,
                0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67,
                (byte) 0xb7, 0x0a, 0x04, 0x03, 0x62, (byte) 0xf2, 0x20, 0x04,
                0x03, 0x62, (byte) 0xf2, 0x30
        };
    }

    private byte[] getGSNAddressData() {
        return new byte[] { 23, 5, 38, 48, 81, 5 };
    }

    private byte[] getSgsnAddressData() {
        return new byte[] { 0x17, 0x05, 0x26, 0x30, 0x51, 0x05 };
    }

    private byte[] getVGmlcAddressData() {
        return new byte[] { 0x17, 0x05, 0x26, 0x30, 0x51, 0x05 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        UpdateGprsLocationRequestImpl ugl = new UpdateGprsLocationRequestImpl();
        ugl.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ugl.getImsi().getData(), "111222");
        assertEquals(ugl.getSgsnNumber().getAddress(), "22228");
        assertEquals(ugl.getSgsnNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(ugl.getSgsnNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(Arrays.equals(ugl.getSgsnAddress().getData(), getGSNAddressData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ugl.getExtensionContainer()));
        assertTrue(ugl.getSGSNCapability().getSolsaSupportIndicator());
        assertTrue(ugl.getInformPreviousNetworkEntity());
        assertTrue(ugl.getPsLCSNotSupportedByUE());
        assertTrue(Arrays.equals(ugl.getVGmlcAddress().getData(), getGSNAddressData()));
        assertEquals(ugl.getADDInfo().getImeisv().getIMEI(), "12341234");
        assertTrue(ugl.getEPSInfo().getIsrInformation().getCancelSGSN());
        assertTrue(ugl.getServingNodeTypeIndicator());
        assertTrue(ugl.getSkipSubscriberDataUpdate());
        assertEquals(ugl.getUsedRATType(), UsedRATType.gan);
        assertTrue(ugl.getGprsSubscriptionDataNotNeeded());
        assertTrue(ugl.getNodeTypeIndicator());
        assertTrue(ugl.getAreaRestricted());
        assertTrue(ugl.getUeReachableIndicator());
        assertTrue(ugl.getEpsSubscriptionDataNotNeeded());
        assertEquals(ugl.getUESRVCCCapability(), UESRVCCCapability.ueSrvccSupported);

        // test 2
        byte[] data1 = this.getData1();
        AsnInputStream asn1 = new AsnInputStream(data1);
        int tag1 = asn1.readTag();

        UpdateGprsLocationRequestImpl ugl1 = new UpdateGprsLocationRequestImpl();
        ugl1.decodeAll(asn1);

        assertEquals(tag1, Tag.SEQUENCE);
        assertEquals(asn1.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ugl1.getImsi().getData(), "901405105682583");
        assertEquals(ugl1.getSgsnNumber().getAddress(), "491710490000");
        assertEquals(ugl1.getSgsnNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(ugl1.getSgsnNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(Arrays.equals(ugl1.getSgsnAddress().getData(), getSgsnAddressData()));
        assertNull(ugl1.getExtensionContainer());
        assertFalse(ugl1.getSGSNCapability().getSolsaSupportIndicator());
        assertTrue(ugl1.getSGSNCapability().getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertTrue(ugl1.getSGSNCapability().getGprsEnhancementsSupportIndicator());
        assertTrue(ugl1.getSGSNCapability().getSupportedCamelPhases().getPhase1Supported());
        assertTrue(ugl1.getSGSNCapability().getSupportedCamelPhases().getPhase2Supported());
        assertTrue(ugl1.getSGSNCapability().getSupportedCamelPhases().getPhase3Supported());
        assertFalse(ugl1.getSGSNCapability().getSupportedCamelPhases().getPhase4Supported());
        assertTrue(ugl1.getSGSNCapability().getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(ugl1.getSGSNCapability().getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(ugl1.getSGSNCapability().getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(ugl1.getSGSNCapability().getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
        assertFalse(ugl1.getSGSNCapability().getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
        assertFalse(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getOCsi());
        assertFalse(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getDCsi());
        assertFalse(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getVtCsi());
        assertFalse(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getTCsi());
        assertTrue(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getMtSmsCsi());
        assertTrue(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getMgCsi());
        assertTrue(ugl1.getSGSNCapability().getOfferedCamel4CSIs().getPsiEnhancements());
        assertTrue(ugl1.getSGSNCapability().getGprsEnhancementsSupportIndicator());
        assertTrue(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getUtran());
        assertTrue(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getGeran());
        assertFalse(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getGan());
        assertTrue(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getIHspaEvolution());
        assertTrue(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getEUtran());
        assertTrue(ugl1.getSGSNCapability().getSupportedRATTypesIndicator().getNbIot());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllApn());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbHPLMNApn());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbVPLMNApn());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllOg());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllInternationalOg());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllIntOgNotToHPLMNCountry());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllInterzonalOg());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllInterzonalOgNotToHPLMNCountry());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getRegSub());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getTrace());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsAllPrivExcep());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsUniversal());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsCallSessionRelated());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsCallSessionUnrelated());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsPLMNOperator());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsServiceType());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsAllMOLRSS());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsBasicSelfLocation());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsAutonomousSelfLocation());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLcsTransferToThirdParty());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getSmMoPp());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getBarringOutgoingCalls());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getBaoc());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getBoic());
        assertFalse(ugl1.getSGSNCapability().getSupportedFeatures().getBoicExHC());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getLocalTimeZoneRetrieval());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getAdditionalMsisdn());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getSmsInMME());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getSmsInSGSN());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getUeReachabilityNotification());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getStateLocationInformationRetrieval());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getPartialPurge());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getGddInSGSN());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getSgsnCAMELCapability());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getPcscfRestoration());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getDedicatedCoreNetworks());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getNonIPPDNTypeAPNs());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getNonIPPDPTypeAPNs());
        assertTrue(ugl1.getSGSNCapability().getSupportedFeatures().getNrAsSecondaryRAT());
        assertTrue(ugl1.getSGSNCapability().getTAdsDataRetrieval());
        assertTrue(ugl1.getSGSNCapability().getHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertTrue(ugl1.getSGSNCapability().getCancellationTypeInitialAttach());
        assertTrue(ugl1.getSGSNCapability().getMsisdnlessOperationSupported());
        assertTrue(ugl1.getSGSNCapability().getUpdateOfHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertTrue(ugl1.getSGSNCapability().getResetIdsSupported());
        assertTrue(ugl1.getSGSNCapability().getExtSupportedFeatures().isUnlicensedSpectrumAsSecondaryRAT());
        assertTrue(ugl1.getInformPreviousNetworkEntity());
        assertEquals(ugl1.getVGmlcAddress().getData(), getVGmlcAddressData());
        assertEquals(ugl1.getADDInfo().getImeisv().getIMEI(), "356024081653200");
        assertFalse(ugl1.getADDInfo().getSkipSubscriberDataUpdate());
        assertTrue(ugl1.getEPSInfo().getIsrInformation().getUpdateMME());
        assertTrue(ugl1.getEPSInfo().getIsrInformation().getCancelSGSN());
        assertTrue(ugl1.getEPSInfo().getIsrInformation().getInitialAttachIndicator());
        assertTrue(ugl1.getServingNodeTypeIndicator());
        assertEquals(ugl1.getUsedRATType().getCode(), 0);
        assertTrue(ugl1.getGprsSubscriptionDataNotNeeded());
        assertTrue(ugl1.getNodeTypeIndicator());
        assertTrue(ugl1.getAreaRestricted());
        assertTrue(ugl1.getUeReachableIndicator());
        assertTrue(ugl1.getEpsSubscriptionDataNotNeeded());
        assertEquals(ugl1.getUESRVCCCapability().getCode(), 1);
        assertEquals(ugl1.getEPLMNList().get(0).getMcc(), 262);
        assertEquals(ugl1.getEPLMNList().get(0).getMnc(), 1);
        assertEquals(ugl1.getEPLMNList().get(1).getMcc(), 262);
        assertEquals(ugl1.getEPLMNList().get(1).getMnc(), 999);
        assertEquals(ugl1.getMmeNumberForMTSMS().getAddressNature(), AddressNature.international_number);
        assertEquals(ugl1.getMmeNumberForMTSMS().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(ugl1.getMmeNumberForMTSMS().getAddress(), "491710490001");
        assertEquals(ugl1.getSMSRegisterRequest().getCode(), 2);
        assertTrue(ugl1.getSmsOnly());
        assertEquals(ugl1.getSgsnName().getData(), "mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(ugl1.getSgsnRealm().getData(), "epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(ugl1.getAdjacentPLMNList().get(0).getMcc(), 262);
        assertEquals(ugl1.getAdjacentPLMNList().get(0).getMnc(), 2);
        assertEquals(ugl1.getAdjacentPLMNList().get(1).getMcc(), 262);
        assertEquals(ugl1.getAdjacentPLMNList().get(1).getMnc(), 3);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddress sgsnAddress = new GSNAddressImpl(getGSNAddressData());
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapability sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null, false, false, false, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddress vGmlcAddress = new GSNAddressImpl(getGSNAddressData());
        ADDInfo addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfo epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
        boolean servingNodeTypeIndicator = true;
        boolean skipSubscriberDataUpdate = true;
        UsedRATType usedRATType = UsedRATType.gan;
        boolean gprsSubscriptionDataNotNeeded = true;
        boolean nodeTypeIndicator = true;
        boolean areaRestricted = true;
        boolean ueReachableIndicator = true;
        boolean epsSubscriptionDataNotNeeded = true;
        UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;
        ArrayList<PlmnId> ePLMNList = null;
        ISDNAddressString mmeNumberForMTSMS = null;
        SMSRegisterRequest smsRegisterRequest = null;
        boolean smsOnly = false;
        DiameterIdentity sgsnName = null;
        DiameterIdentity sgsnRealm = null;
        boolean lgdSupportIndicator = false;
        boolean removalOfMMERegistrationForSMS = false;
        ArrayList<PlmnId> adjacentPLMNList = null;
        long mapProtocolVersion = 3;

        UpdateGprsLocationRequestImpl ugl = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability,
                ePLMNList, mmeNumberForMTSMS, smsRegisterRequest, smsOnly, sgsnName, sgsnRealm,
                lgdSupportIndicator, removalOfMMERegistrationForSMS, adjacentPLMNList, mapProtocolVersion);

        AsnOutputStream asn = new AsnOutputStream();
        ugl.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        imsi = new IMSIImpl("901405105682583");
        sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710490000");
        sgsnAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        extensionContainer = null;
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
        sgsnCapability = new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer,
                superChargerSupportedInServingNetworkEntity, gprsEnhancementsSupportIndicator, supportedCamelPhases,
                supportedLCSCapabilitySets, offeredCamel4CSIs, smsCallBarringSupportIndicator, supportedRATTypesIndicator,
                supportedFeatures, tAdsDataRetrieval, homogeneousSupportOfIMSVoiceOverPSSessions, cancellationTypeInitialAttach,
                misdnlessOperationSupported, updateOfHomogeneousSupportOfIMSVoiceOverPSSessions, resetIdsSupported,
                extSupportedFeatures);
        psLCSNotSupportedByUE = false;
        vGmlcAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        skipSubscriberDataUpdate = false;
        addInfo = new ADDInfoImpl(new IMEIImpl("356024081653200"), skipSubscriberDataUpdate);
        boolean updateMME = true;
        boolean cancelSGSN = true;
        boolean initialAttachIndicator = true;
        epsInfo = new EPSInfoImpl(new ISRInformationImpl(updateMME, cancelSGSN, initialAttachIndicator));
        usedRATType = UsedRATType.utran;
        ePLMNList = new ArrayList<>();
        PlmnId plmnId1 = new PlmnIdImpl(262,1);
        PlmnId plmnId2 = new PlmnIdImpl(262,999);
        ePLMNList.add(plmnId1);
        ePLMNList.add(plmnId2);
        mmeNumberForMTSMS = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710490001");
        smsRegisterRequest = SMSRegisterRequest.isNoPreference;
        smsOnly = true;
        byte[] sgsnNameArray = "mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8);
        sgsnName = new DiameterIdentityImpl(sgsnNameArray);
        byte[] sgsnRealmArray = "epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8);
        sgsnRealm = new DiameterIdentityImpl(sgsnRealmArray);
        adjacentPLMNList = new ArrayList<>();
        PlmnId adjPlmnId1 = new PlmnIdImpl(262,2);
        PlmnId adjPlmnId2 = new PlmnIdImpl(262,3);
        adjacentPLMNList.add(adjPlmnId1);
        adjacentPLMNList.add(adjPlmnId2);
        UpdateGprsLocationRequestImpl ugl1 = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability,
                ePLMNList, mmeNumberForMTSMS, smsRegisterRequest, smsOnly, sgsnName, sgsnRealm, lgdSupportIndicator,
                removalOfMMERegistrationForSMS, adjacentPLMNList, mapProtocolVersion);

        AsnOutputStream asn1 = new AsnOutputStream();
        ugl1.encodeAll(asn1);

        assertTrue(Arrays.equals(asn1.toByteArray(), this.getData1()));
    }

}
