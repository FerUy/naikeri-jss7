package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author <a href="mailto:fernando.mendioroz@gmail.com">Fernando Mendioroz</a>
*/
public class DeleteSubscriberDataRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 8, (byte) 128, 6, 17, 33, 34, 51, 67, 68 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 74, -128, 6, 17, 33, 34, 51, 67, 68, -95, 3, -126, 1, 48, -94, 6, 4, 1, 33, 4, 1, 17, -124, 0, -123, 2, 0, 11, -121, 0, -120,
                0, -119, 0, -90, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 42, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 170, 2, 5, 0, (byte) 139, 0, (byte) 172, 2, 5, 0, (byte) 141, 0, (byte) 142,
                0, (byte) 143, 3, 2, (byte) 144, 0, (byte) 144, 0, (byte) 145, 0, (byte) 178, 5, 48, 3, 2, 1, 15, (byte) 147, 0, (byte) 148, 0 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x62,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x78, 0x01, 0x21,
                0x57, (byte) 0xf0, (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x12, (byte) 0x83,
                0x01, 0x20, (byte) 0xa2, 0x03, 0x04, 0x01, 0x44, (byte) 0x84,
                0x00, (byte) 0x85, 0x02, 0x21, 0x0f, (byte) 0x87, 0x00, (byte) 0x88,
                0x00, (byte) 0x89, 0x00, (byte) 0x8b, 0x00, (byte) 0xac, 0x0c, 0x30,
                0x0a, 0x04, 0x03, 0x0c, 0x0a, 0x01, 0x04, 0x03,
                0x0c, 0x0c, 0x02, (byte) 0x8d, 0x00, (byte) 0x8e, 0x00, (byte) 0x8f,
                0x03, 0x02, (byte) 0x90, 0x00, (byte) 0x90, 0x00, (byte) 0x91, 0x00,
                (byte) 0xb2, 0x02, 0x05, 0x00, (byte) 0x93, 0x00, (byte) 0x94, 0x00,
                (byte) 0x96, 0x00, (byte) 0x97, 0x00, (byte) 0x95, 0x00, (byte) 0x98, 0x00,
                (byte) 0x99, 0x00, (byte) 0x9a, 0x00, (byte) 0x9b, 0x00, (byte) 0x9c, 0x00,
                (byte) 0x9d, 0x00, (byte) 0x9e, 0x00, (byte) 0x9f, 0x1f, 0x00, (byte) 0x9f,
                0x20, 0x00
        };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] { 0x30, 0x5e,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x78, 0x01, 0x21,
                0x57, (byte) 0xf0, (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x2d, (byte) 0x83,
                0x01, 0x60, (byte) 0xa2, 0x03, 0x04, 0x01, 0x72, (byte) 0x84,
                0x00, (byte) 0x85, 0x02, 0x21, 0x0f, (byte) 0x87, 0x00, (byte) 0x88,
                0x00, (byte) 0x89, 0x00, (byte) 0x8b, 0x00, (byte) 0xac, 0x02, 0x05,
                0x00, (byte) 0x8d, 0x00, (byte) 0x8e, 0x00, (byte) 0x8f, 0x03, 0x02,
                (byte) 0x90, 0x00, (byte) 0x90, 0x00, (byte) 0x91, 0x00, (byte) 0xb2, 0x08,
                0x30, 0x06, 0x02, 0x01, 0x01, 0x02, 0x01, 0x02,
                (byte) 0x93, 0x00, (byte) 0x94, 0x00, (byte) 0x96, 0x00, (byte) 0x97, 0x00,
                (byte) 0x95, 0x00, (byte) 0x98, 0x00, (byte) 0x99, 0x00, (byte) 0x9a, 0x00,
                (byte) 0x9b, 0x00, (byte) 0x9c, 0x00, (byte) 0x9d, 0x00, (byte) 0x9e, 0x00,
                (byte) 0x9f, 0x1f, 0x00, (byte) 0x9f, 0x20, 0x00
        };
    }

    private byte[] getEncodedDataRel18_2() {
        return new byte[] { 0x30, 0x5e,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x78, 0x01, 0x21,
                0x06, (byte) 0xf5, (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x11, (byte) 0x83,
                0x01, 0x61, (byte) 0xa2, 0x03, 0x04, 0x01, (byte) 0x82, (byte) 0x84,
                0x00, (byte) 0x85, 0x02, 0x21, 0x0f, (byte) 0x87, 0x00, (byte) 0x88,
                0x00, (byte) 0x89, 0x00, (byte) 0xaa, 0x08, 0x30, 0x06, 0x02,
                0x01, 0x01, 0x02, 0x01, 0x02, (byte) 0x8b, 0x00, (byte) 0x8d,
                0x00, (byte) 0x8e, 0x00, (byte) 0x8f, 0x03, 0x02, (byte) 0x90, 0x00,
                (byte) 0x90, 0x00, (byte) 0x91, 0x00, (byte) 0xb2, 0x02, 0x05, 0x00,
                (byte) 0x93, 0x00, (byte) 0x94, 0x00, (byte) 0x96, 0x00, (byte) 0x97, 0x00,
                (byte) 0x95, 0x00, (byte) 0x98, 0x00, (byte) 0x99, 0x00, (byte) 0x9a, 0x00,
                (byte) 0x9b, 0x00, (byte) 0x9c, 0x00, (byte) 0x9d, 0x00, (byte) 0x9e, 0x00,
                (byte) 0x9f, 0x1f, 0x00, (byte) 0x9f, 0x20, 0x00
        };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        DeleteSubscriberDataRequestImpl asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = asc.getImsi();
        ArrayList<ExtBasicServiceCode> basicServiceList = asc.getBasicServiceList();
        ArrayList<SSCode> ssList = asc.getSsList();
        boolean roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        ZoneCode regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        boolean vbsGroupIndication = asc.getVbsGroupIndication();
        boolean vgcsGroupIndication = asc.getVgcsGroupIndication();
        boolean camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        MAPExtensionContainer extensionContainer = asc.getExtensionContainer();
        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        LSAInformationWithdraw lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        boolean gmlcListWithdraw = asc.getGmlcListWithdraw();
        boolean istInformationWithdraw = asc.getIstInformationWithdraw();
        SpecificCSIWithdraw specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        boolean chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        boolean stnSrWithdraw = asc.getStnSrWithdraw();
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        boolean apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        boolean csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        boolean subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        boolean subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        boolean subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        boolean vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        boolean additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        boolean csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        boolean imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        boolean userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        boolean dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        boolean ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        boolean resetIdsWithdraw = asc.getResetIdsWithdraw();
        boolean iabOperationWithdraw = asc.getIabOperationWithdraw();

        assertEquals(imsi.getData(), "111222333444");
        assertNull(basicServiceList);
        assertNull(ssList);
        assertFalse(roamingRestrictionDueToUnsupportedFeature);
        assertNull(regionalSubscriptionIdentifier);
        assertFalse(vbsGroupIndication);
        assertFalse(vgcsGroupIndication);
        assertFalse(camelSubscriptionInfoWithdraw);
        assertNull(extensionContainer);
        assertNull(gprsSubscriptionDataWithdraw);
        assertFalse(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertNull(lsaInformationWithdraw);
        assertFalse(gmlcListWithdraw);
        assertFalse(istInformationWithdraw);
        assertNull(specificCSIWithdraw);
        assertFalse(chargingCharacteristicsWithdraw);
        assertFalse(stnSrWithdraw);
        assertNull(epsSubscriptionDataWithdraw);
        assertFalse(apnOiReplacementWithdraw);
        assertFalse(csgSubscriptionDeleted);
        assertFalse(subscribedPeriodicTAURAUTimerWithdraw);
        assertFalse(subscribedPeriodicLAUTimerWithdraw);
        assertFalse(subscribedVsrvccWithdraw);
        assertFalse(vplmnCsgSubscriptionDeleted);
        assertFalse(additionalMSISDNWithdraw);
        assertFalse(csToPsSRVCCWithdraw);
        assertFalse(imsiGroupIdListWithdraw);
        assertFalse(userPlaneIntegrityProtectionWithdraw);
        assertFalse(dlBufferingSuggestedPacketCountWithdraw);
        assertFalse(ueUsageTypeWithdraw);
        assertFalse(resetIdsWithdraw);
        assertFalse(iabOperationWithdraw);

        // test 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        basicServiceList = asc.getBasicServiceList();
        ssList = asc.getSsList();
        roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        vbsGroupIndication = asc.getVbsGroupIndication();
        vgcsGroupIndication = asc.getVgcsGroupIndication();
        camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        extensionContainer = asc.getExtensionContainer();
        gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        gmlcListWithdraw = asc.getGmlcListWithdraw();
        istInformationWithdraw = asc.getIstInformationWithdraw();
        specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        stnSrWithdraw = asc.getStnSrWithdraw();
        epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        resetIdsWithdraw = asc.getResetIdsWithdraw();
        iabOperationWithdraw = asc.getIabOperationWithdraw();

        assertEquals(imsi.getData(), "111222333444");
        assertEquals(basicServiceList.size(), 1);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        assertEquals(ssList.size(), 2);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(ssList.get(1).getSupplementaryCodeValue(), SupplementaryCodeValue.clip);
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        assertEquals(regionalSubscriptionIdentifier.getValue(), 11);
        assertTrue(vbsGroupIndication);
        assertTrue(vgcsGroupIndication);
        assertTrue(camelSubscriptionInfoWithdraw);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertNull(gprsSubscriptionDataWithdraw);
        assertFalse(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertNull(lsaInformationWithdraw);
        assertFalse(gmlcListWithdraw);
        assertFalse(istInformationWithdraw);
        assertNull(specificCSIWithdraw);
        assertFalse(chargingCharacteristicsWithdraw);
        assertFalse(stnSrWithdraw);
        assertNull(epsSubscriptionDataWithdraw);
        assertFalse(apnOiReplacementWithdraw);
        assertFalse(csgSubscriptionDeleted);
        assertFalse(subscribedPeriodicTAURAUTimerWithdraw);
        assertFalse(subscribedPeriodicLAUTimerWithdraw);
        assertFalse(subscribedVsrvccWithdraw);
        assertFalse(vplmnCsgSubscriptionDeleted);
        assertFalse(additionalMSISDNWithdraw);
        assertFalse(csToPsSRVCCWithdraw);
        assertFalse(imsiGroupIdListWithdraw);
        assertFalse(userPlaneIntegrityProtectionWithdraw);
        assertFalse(dlBufferingSuggestedPacketCountWithdraw);
        assertFalse(ueUsageTypeWithdraw);
        assertFalse(resetIdsWithdraw);
        assertFalse(iabOperationWithdraw);

        // test 3
        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        basicServiceList = asc.getBasicServiceList();
        ssList = asc.getSsList();
        roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        vbsGroupIndication = asc.getVbsGroupIndication();
        vgcsGroupIndication = asc.getVgcsGroupIndication();
        camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        extensionContainer = asc.getExtensionContainer();
        gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        gmlcListWithdraw = asc.getGmlcListWithdraw();
        istInformationWithdraw = asc.getIstInformationWithdraw();
        specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        stnSrWithdraw = asc.getStnSrWithdraw();
        epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        resetIdsWithdraw = asc.getResetIdsWithdraw();
        iabOperationWithdraw = asc.getIabOperationWithdraw();

        assertEquals(imsi.getData(), "111222333444");
        assertNull(basicServiceList);
        assertNull(ssList);
        assertFalse(roamingRestrictionDueToUnsupportedFeature);
        assertNull(regionalSubscriptionIdentifier);
        assertFalse(vbsGroupIndication);
        assertFalse(vgcsGroupIndication);
        assertFalse(camelSubscriptionInfoWithdraw);
        assertNull(extensionContainer);
        assertTrue(gprsSubscriptionDataWithdraw.getAllGPRSData());
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertTrue(lsaInformationWithdraw.getAllLSAData());
        assertTrue(gmlcListWithdraw);
        assertTrue(istInformationWithdraw);
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertTrue(chargingCharacteristicsWithdraw);
        assertTrue(stnSrWithdraw);
        assertEquals(epsSubscriptionDataWithdraw.getContextIdList().size(), 1);
        assertEquals(epsSubscriptionDataWithdraw.getContextIdList().get(0).intValue(), 15);
        assertTrue(apnOiReplacementWithdraw);
        assertTrue(csgSubscriptionDeleted);
        assertFalse(subscribedPeriodicTAURAUTimerWithdraw);
        assertFalse(subscribedPeriodicLAUTimerWithdraw);
        assertFalse(subscribedVsrvccWithdraw);
        assertFalse(vplmnCsgSubscriptionDeleted);
        assertFalse(additionalMSISDNWithdraw);
        assertFalse(csToPsSRVCCWithdraw);
        assertFalse(imsiGroupIdListWithdraw);
        assertFalse(userPlaneIntegrityProtectionWithdraw);
        assertFalse(dlBufferingSuggestedPacketCountWithdraw);
        assertFalse(ueUsageTypeWithdraw);
        assertFalse(resetIdsWithdraw);
        assertFalse(iabOperationWithdraw);

        // test 4, MAP v18.0.0 with epsSubscriptionDataWithdraw: allEPS-Data (0), lsaIdentityList: 2 items
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        basicServiceList = asc.getBasicServiceList();
        ssList = asc.getSsList();
        roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        vbsGroupIndication = asc.getVbsGroupIndication();
        vgcsGroupIndication = asc.getVgcsGroupIndication();
        camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        extensionContainer = asc.getExtensionContainer();
        gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        gmlcListWithdraw = asc.getGmlcListWithdraw();
        istInformationWithdraw = asc.getIstInformationWithdraw();
        specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        stnSrWithdraw = asc.getStnSrWithdraw();
        epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        resetIdsWithdraw = asc.getResetIdsWithdraw();
        iabOperationWithdraw = asc.getIabOperationWithdraw();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012750
         *         [Association IMSI: 748026871012750]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataCDA-1200bps (18)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: allShortMessageServices (32)
         *         ss-List: 1 item
         *             SS-Code: ccbs-B - completion of call to busy subscribers, destination side (68)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         lsaInformationWithdraw: lsaIdentityList (1)
         *             lsaIdentityList: 2 items
         *                 LSAIdentity: 0c0a01
         *                 LSAIdentity: 0c0c02
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: allEPS-Data (0)
         *             allEPS-Data
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        assertEquals(imsi.getData(), "748026871012750");
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);
        assertEquals(ssList.size(), 1);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.ccbs_B);
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        assertEquals(regionalSubscriptionIdentifier.getData(), new byte[] {0x21, 0x0f});
        assertTrue(vbsGroupIndication);
        assertTrue(vgcsGroupIndication);
        assertTrue(camelSubscriptionInfoWithdraw);
        assertNull(extensionContainer);
        assertNull(gprsSubscriptionDataWithdraw);
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().size(), 2);
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().get(0).getData(), new byte[] {0x0c, 0x0a, 0x01});
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().get(1).getData(), new byte[] {0x0c, 0x0c, 0x02});
        assertTrue(gmlcListWithdraw);
        assertTrue(istInformationWithdraw);
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertFalse(specificCSIWithdraw.getMoSmsCsi());
        assertFalse(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());
        assertTrue(chargingCharacteristicsWithdraw);
        assertTrue(stnSrWithdraw);
        assertNull(epsSubscriptionDataWithdraw.getContextIdList());
        assertTrue(epsSubscriptionDataWithdraw.getAllEpsData());
        assertTrue(apnOiReplacementWithdraw);
        assertTrue(csgSubscriptionDeleted);
        assertTrue(subscribedPeriodicTAURAUTimerWithdraw);
        assertTrue(subscribedPeriodicLAUTimerWithdraw);
        assertTrue(subscribedVsrvccWithdraw);
        assertTrue(vplmnCsgSubscriptionDeleted);
        assertTrue(additionalMSISDNWithdraw);
        assertTrue(csToPsSRVCCWithdraw);
        assertTrue(imsiGroupIdListWithdraw);
        assertTrue(userPlaneIntegrityProtectionWithdraw);
        assertTrue(dlBufferingSuggestedPacketCountWithdraw);
        assertTrue(ueUsageTypeWithdraw);
        assertTrue(resetIdsWithdraw);
        assertTrue(iabOperationWithdraw);

        // test 5, MAP v18.0.0 with epsSubscriptionDataWithdraw: contextIdList 2 items, lsaInformationWithdraw: allLSAData (0)
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        basicServiceList = asc.getBasicServiceList();
        ssList = asc.getSsList();
        roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        vbsGroupIndication = asc.getVbsGroupIndication();
        vgcsGroupIndication = asc.getVgcsGroupIndication();
        camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        extensionContainer = asc.getExtensionContainer();
        gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        gmlcListWithdraw = asc.getGmlcListWithdraw();
        istInformationWithdraw = asc.getIstInformationWithdraw();
        specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        stnSrWithdraw = asc.getStnSrWithdraw();
        epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        resetIdsWithdraw = asc.getResetIdsWithdraw();
        iabOperationWithdraw = asc.getIabOperationWithdraw();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012750
         *         [Association IMSI: 748026871012750]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataPDS-4800bps (45)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: allFacsimileTransmissionServices (96)
         *         ss-List: 1 item
         *             SS-Code: aocc - advice of charge charging (114)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         lsaInformationWithdraw: allLSAData (0)
         *             allLSAData
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: contextIdList (1)
         *             contextIdList: 2 items
         *                 ContextId: 1
         *                 ContextId: 2
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        assertEquals(imsi.getData(), "748026871012750");
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataPDS_4800bps);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allFacsimileTransmissionServices);
        assertEquals(ssList.size(), 1);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.aocc);
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        assertEquals(regionalSubscriptionIdentifier.getData(), new byte[] {0x21, 0x0f});
        assertTrue(vbsGroupIndication);
        assertTrue(vgcsGroupIndication);
        assertTrue(camelSubscriptionInfoWithdraw);
        assertNull(extensionContainer);
        assertNull(gprsSubscriptionDataWithdraw);
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertTrue(lsaInformationWithdraw.getAllLSAData());
        assertTrue(gmlcListWithdraw);
        assertTrue(istInformationWithdraw);
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertFalse(specificCSIWithdraw.getMoSmsCsi());
        assertFalse(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());
        assertTrue(chargingCharacteristicsWithdraw);
        assertTrue(stnSrWithdraw);
        assertEquals(epsSubscriptionDataWithdraw.getContextIdList().size(), 2);
        assertEquals(epsSubscriptionDataWithdraw.getContextIdList().get(0).intValue(), 1);
        assertEquals(epsSubscriptionDataWithdraw.getContextIdList().get(1).intValue(), 2);
        assertTrue(apnOiReplacementWithdraw);
        assertTrue(csgSubscriptionDeleted);
        assertTrue(subscribedPeriodicTAURAUTimerWithdraw);
        assertTrue(subscribedPeriodicLAUTimerWithdraw);
        assertTrue(subscribedVsrvccWithdraw);
        assertTrue(vplmnCsgSubscriptionDeleted);
        assertTrue(additionalMSISDNWithdraw);
        assertTrue(csToPsSRVCCWithdraw);
        assertTrue(imsiGroupIdListWithdraw);
        assertTrue(userPlaneIntegrityProtectionWithdraw);
        assertTrue(dlBufferingSuggestedPacketCountWithdraw);
        assertTrue(ueUsageTypeWithdraw);
        assertTrue(resetIdsWithdraw);
        assertTrue(iabOperationWithdraw);

        // test 6, MAP v18.0.0 with gprsSubscriptionDataWithdraw: contextIdList 2 items
        rawData = getEncodedDataRel18_2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        basicServiceList = asc.getBasicServiceList();
        ssList = asc.getSsList();
        roamingRestrictionDueToUnsupportedFeature = asc.getRoamingRestrictionDueToUnsupportedFeature();
        regionalSubscriptionIdentifier = asc.getRegionalSubscriptionIdentifier();
        vbsGroupIndication = asc.getVbsGroupIndication();
        vgcsGroupIndication = asc.getVgcsGroupIndication();
        camelSubscriptionInfoWithdraw = asc.getCamelSubscriptionInfoWithdraw();
        extensionContainer = asc.getExtensionContainer();
        gprsSubscriptionDataWithdraw = asc.getGPRSSubscriptionDataWithdraw();
        roamingRestrictedInSgsnDueToUnsuppportedFeature = asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        lsaInformationWithdraw = asc.getLSAInformationWithdraw();
        gmlcListWithdraw = asc.getGmlcListWithdraw();
        istInformationWithdraw = asc.getIstInformationWithdraw();
        specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        chargingCharacteristicsWithdraw = asc.getChargingCharacteristicsWithdraw();
        stnSrWithdraw = asc.getStnSrWithdraw();
        epsSubscriptionDataWithdraw = asc.getEPSSubscriptionDataWithdraw();
        apnOiReplacementWithdraw = asc.getApnOiReplacementWithdraw();
        csgSubscriptionDeleted = asc.getCsgSubscriptionDeleted();
        subscribedPeriodicTAURAUTimerWithdraw = asc.getSubscribedPeriodicTAURAUTimerWithdraw();
        subscribedPeriodicLAUTimerWithdraw = asc.getSubscribedPeriodicLAUTimerWithdraw();
        subscribedVsrvccWithdraw = asc.getSubscribedVsrvccWithdraw();
        vplmnCsgSubscriptionDeleted = asc.getVplmnCsgSubscriptionDeleted();
        additionalMSISDNWithdraw = asc.getAdditionalMSISDNWithdraw();
        csToPsSRVCCWithdraw = asc.getCsToPsSRVCCWithdraw();
        imsiGroupIdListWithdraw = asc.getImsiGroupIdListWithdraw();
        userPlaneIntegrityProtectionWithdraw = asc.getUserPlaneIntegrityProtectionWithdraw();
        dlBufferingSuggestedPacketCountWithdraw = asc.getDlBufferingSuggestedPacketCountWithdraw();
        ueUsageTypeWithdraw = asc.getUeUsageTypeWithdraw();
        resetIdsWithdraw = asc.getResetIdsWithdraw();
        iabOperationWithdraw = asc.getIabOperationWithdraw();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012605
         *         [Association IMSI: 748026871012605]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataCDA-300bps (17)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: facsimileGroup3AndAlterSpeech (97)
         *         ss-List: 1 item
         *             SS-Code: uus2 - UUS2 user-to-user signalling (130)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         gprsSubscriptionDataWithdraw: contextIdList (1)
         *             contextIdList: 2 items
         *                 ContextId: 1
         *                 ContextId: 2
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: allEPS-Data (0)
         *             allEPS-Data
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        assertEquals(imsi.getData(), "748026871012605");
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_300bps);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.facsimileGroup3AndAlterSpeech);
        assertEquals(ssList.size(), 1);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.uus2);
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        assertEquals(regionalSubscriptionIdentifier.getData(), new byte[] {0x21, 0x0f});
        assertTrue(vbsGroupIndication);
        assertTrue(vgcsGroupIndication);
        assertTrue(camelSubscriptionInfoWithdraw);
        assertNull(extensionContainer);
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().size(), 2);
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().get(0).intValue(), 1);
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().get(1).intValue(), 2);
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        assertNull(lsaInformationWithdraw);
        assertTrue(gmlcListWithdraw);
        assertTrue(istInformationWithdraw);
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertFalse(specificCSIWithdraw.getMoSmsCsi());
        assertFalse(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());
        assertTrue(chargingCharacteristicsWithdraw);
        assertTrue(stnSrWithdraw);
        assertNull(epsSubscriptionDataWithdraw.getContextIdList());
        assertTrue(epsSubscriptionDataWithdraw.getAllEpsData());
        assertTrue(apnOiReplacementWithdraw);
        assertTrue(csgSubscriptionDeleted);
        assertTrue(subscribedPeriodicTAURAUTimerWithdraw);
        assertTrue(subscribedPeriodicLAUTimerWithdraw);
        assertTrue(subscribedVsrvccWithdraw);
        assertTrue(vplmnCsgSubscriptionDeleted);
        assertTrue(additionalMSISDNWithdraw);
        assertTrue(csToPsSRVCCWithdraw);
        assertTrue(imsiGroupIdListWithdraw);
        assertTrue(userPlaneIntegrityProtectionWithdraw);
        assertTrue(dlBufferingSuggestedPacketCountWithdraw);
        assertTrue(ueUsageTypeWithdraw);
        assertTrue(resetIdsWithdraw);
        assertTrue(iabOperationWithdraw);
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        // test 1
        IMSIImpl imsi = new IMSIImpl("111222333444");
        DeleteSubscriberDataRequestImpl asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, null, false, null,
                false, false, null, false, false, null, false, false, false, false, false, false, false, false, false, false, false, false, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        ExtBasicServiceCode basicService = new ExtBasicServiceCodeImpl(extBearerService);
        basicServiceList.add(basicService);
        ArrayList<SSCode> ssList = new ArrayList<>();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.clip);
        ssList.add(ssCode);
        ssList.add(ssCode2);
        ZoneCode regionalSubscriptionIdentifier = new ZoneCodeImpl(11);
        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, true, regionalSubscriptionIdentifier, true, true, true,
                MAPExtensionContainerTest.GetTestExtensionContainer(), null, false, null, false, false, null, false, false, null, false, false,
                 false, false, false, false, false, false, false, false, false, false, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3
        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl(true);
        LSAInformationWithdraw lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
        SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false, false, false,
                false, false);
        ArrayList<Integer> contextIdList = new ArrayList<>();
        contextIdList.add(15);
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
        asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, gprsSubscriptionDataWithdraw, true,
                lsaInformationWithdraw, true, true, specificCSIWithdraw, true, true, epsSubscriptionDataWithdraw, true, true,
                false, false, false, false, false, false, false, false, false, false, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4, MAP v18.0.0 with epsSubscriptionDataWithdraw: allEPS-Data (0), lsaIdentityList: 2 items
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012750
         *         [Association IMSI: 748026871012750]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataCDA-1200bps (18)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: allShortMessageServices (32)
         *         ss-List: 1 item
         *             SS-Code: ccbs-B - completion of call to busy subscribers, destination side (68)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         lsaInformationWithdraw: lsaIdentityList (1)
         *             lsaIdentityList: 2 items
         *                 LSAIdentity: 0c0a01
         *                 LSAIdentity: 0c0c02
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: allEPS-Data (0)
         *             allEPS-Data
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        imsi = new IMSIImpl("748026871012750");
        basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_1200bps;
        ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
        ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode);
        TeleserviceCodeValue teleserviceCodeValue = TeleserviceCodeValue.allShortMessageServices;
        ExtTeleserviceCode extTeleserviceCode = new ExtTeleserviceCodeImpl(teleserviceCodeValue);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        ssList = new ArrayList<>();
        SupplementaryCodeValue supplementaryCodeValue = SupplementaryCodeValue.ccbs_B;
        ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        regionalSubscriptionIdentifier = new ZoneCodeImpl(new byte[] {0x21, 0x0F});
        boolean vbsGroupIndication = true;
        boolean vgcsGroupIndication = true;
        boolean camelSubscriptionInfoWithdraw = true;
        gprsSubscriptionDataWithdraw = null;
        boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = true;
        ArrayList<LSAIdentity> lsaIdentityList = new ArrayList<>();
        LSAIdentity lsaIdentity1 = new LSAIdentityImpl(new byte[]{12, 10, 1});
        LSAIdentity lsaIdentity2 = new LSAIdentityImpl(new byte[]{12, 12, 2});
        lsaIdentityList.add(lsaIdentity1);
        lsaIdentityList.add(lsaIdentity2);
        lsaInformationWithdraw = new LSAInformationWithdrawImpl(lsaIdentityList);
        boolean gmlcListWithdraw = true;
        boolean istInformationWithdraw = true;
        specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false,
                false, false, false, false);
        boolean chargingCharacteristicsWithdraw = true;
        boolean stnSrWithdraw = true;
        epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(true);
        boolean apnOiReplacementWithdraw = true;
        boolean csgSubscriptionDeleted = true;
        boolean subscribedPeriodicTAURAUTimerWithdraw = true;
        boolean subscribedPeriodicLAUTimerWithdraw = true;
        boolean subscribedVsrvccWithdraw = true;
        boolean vplmnCsgSubscriptionDeleted = true;
        boolean additionalMSISDNWithdraw = true;
        boolean csToPsSRVCCWithdraw = true;
        boolean imsiGroupIdListWithdraw = true;
        boolean userPlaneIntegrityProtectionWithdraw = true;
        boolean dlBufferingSuggestedPacketCountWithdraw = true;
        boolean ueUsageTypeWithdraw = true;
        boolean resetIdsWithdraw = true;
        boolean iabOperationWithdraw = true;
        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier,
                vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw,
                roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw,
                chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted,
                subscribedPeriodicTAURAUTimerWithdraw, subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted,
                additionalMSISDNWithdraw, csToPsSRVCCWithdraw, imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw,
                dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw, iabOperationWithdraw);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5, MAP v18.0.0 with epsSubscriptionDataWithdraw: contextIdList 2 items, lsaInformationWithdraw: allLSAData (0)
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012750
         *         [Association IMSI: 748026871012750]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataPDS-4800bps (45)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: allFacsimileTransmissionServices (96)
         *         ss-List: 1 item
         *             SS-Code: aocc - advice of charge charging (114)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         lsaInformationWithdraw: allLSAData (0)
         *             allLSAData
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: contextIdList (1)
         *             contextIdList: 2 items
         *                 ContextId: 1
         *                 ContextId: 2
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        imsi = new IMSIImpl("748026871012750");
        basicServiceList = new ArrayList<>();
        bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_4800bps;
        extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
        extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode);
        teleserviceCodeValue = TeleserviceCodeValue.allFacsimileTransmissionServices;
        extTeleserviceCode = new ExtTeleserviceCodeImpl(teleserviceCodeValue);
        extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        ssList = new ArrayList<>();
        supplementaryCodeValue = SupplementaryCodeValue.aocc;
        ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
        contextIdList = new ArrayList<>();
        contextIdList.add(1);
        contextIdList.add(2);
        epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier,
                vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw,
                roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw,
                chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted,
                subscribedPeriodicTAURAUTimerWithdraw, subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted,
                additionalMSISDNWithdraw, csToPsSRVCCWithdraw, imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw,
                dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw, iabOperationWithdraw);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6, MAP v18.0.0 with gprsSubscriptionDataWithdraw: contextIdList 2 items
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: deleteSubscriberData (8)
         *         IMSI: 748026871012605
         *         [Association IMSI: 748026871012605]
         *         basicServiceList: 2 items
         *             Ext-BasicServiceCode: ext-BearerService (2)
         *                 ext-BearerService: dataCDA-300bps (17)
         *             Ext-BasicServiceCode: ext-Teleservice (3)
         *                 ext-Teleservice: facsimileGroup3AndAlterSpeech (97)
         *         ss-List: 1 item
         *             SS-Code: uus2 - UUS2 user-to-user signalling (130)
         *         roamingRestrictionDueToUnsupportedFeature
         *         regionalSubscriptionIdentifier: 210f
         *         vbsGroupIndication
         *         vgcsGroupIndication
         *         camelSubscriptionInfoWithdraw
         *         gprsSubscriptionDataWithdraw: contextIdList (1)
         *             contextIdList: 2 items
         *                 ContextId: 1
         *                 ContextId: 2
         *         roamingRestrictedInSgsnDueToUnsuppportedFeature
         *         gmlc-ListWithdraw
         *         istInformationWithdraw
         *         Padding: 2
         *         specificCSI-Withdraw: 9000
         *             1... .... = o-csi: True
         *             .0.. .... = ss-csi: False
         *             ..0. .... = tif-csi: False
         *             ...1 .... = d-csi: True
         *             .... 0... = vt-csi: False
         *             .... .0.. = mo-sms-csi: False
         *             .... ..0. = m-csi: False
         *             .... ...0 = gprs-csi: False
         *             0... .... = t-csi: False
         *             .0.. .... = mt-sms-csi: False
         *             ..0. .... = mg-csi: False
         *             ...0 .... = o-IM-CSI: False
         *             .... 0... = d-IM-CSI: False
         *             .... .0.. = vt-IM-CSI: False
         *         chargingCharacteristicsWithdraw
         *         stn-srWithdraw
         *         epsSubscriptionDataWithdraw: allEPS-Data (0)
         *             allEPS-Data
         *         apn-oi-replacementWithdraw
         *         csg-SubscriptionDeleted
         *         subscribedPeriodicTAU-RAU-TimerWithdraw
         *         subscribedPeriodicLAU-TimerWithdraw
         *         subscribed-vsrvccWithdraw
         *         vplmn-Csg-SubscriptionDeleted
         *         additionalMSISDN-Withdraw
         *         cs-to-ps-SRVCC-Withdraw
         *         imsiGroupIdList-Withdraw
         *         userPlaneIntegrityProtectionWithdraw
         *         dl-Buffering-Suggested-Packet-Count-Withdraw
         *         ue-UsageTypeWithdraw
         *         reset-idsWithdraw
         *         iab-OperationWithdraw
         */
        imsi = new IMSIImpl("748026871012605");
        basicServiceList = new ArrayList<>();
        bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_300bps;
        extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
        extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode);
        teleserviceCodeValue = TeleserviceCodeValue.facsimileGroup3AndAlterSpeech;
        extTeleserviceCode = new ExtTeleserviceCodeImpl(teleserviceCodeValue);
        extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        ssList = new ArrayList<>();
        supplementaryCodeValue = SupplementaryCodeValue.uus2;
        ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        contextIdList = new ArrayList<>();
        contextIdList.add(1);
        contextIdList.add(2);
        gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl(contextIdList);
        lsaInformationWithdraw = null;
        epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(true);

        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier,
                vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw,
                roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw,
                chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted,
                subscribedPeriodicTAURAUTimerWithdraw, subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted,
                additionalMSISDNWithdraw, csToPsSRVCCWithdraw, imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw,
                dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw, iabOperationWithdraw);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
