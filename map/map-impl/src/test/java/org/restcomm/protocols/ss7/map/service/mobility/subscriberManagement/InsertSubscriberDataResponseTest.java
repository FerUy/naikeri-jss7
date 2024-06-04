
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ExtSupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ExtSupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InsertSubscriberDataResponseTest {

    public byte[] getData() {
        return new byte[] { 48, 81, -95, 3, 4, 1, 16, -94, 3, 4, 1, 38, -93, 3, 4, 1, 0, -124, 5, 3, 74, -43, 85, 80, -123, 1,
                1, -122, 2, 4, -16, -89, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -120, 2, 1, -2, -119, 5, 6, 85, 85, 85, 64 };
    }

    public byte[] getData1() {
        return new byte[] { 48, 25, -95, 3, 4, 1, 16, -94, 3, 4, 1, 38, -93, 3, 4, 1, 0, -124, 5, 3, 74, -43, 85, 80, -123, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // ISD Response V3 Test
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InsertSubscriberDataResponseImpl prim = new InsertSubscriberDataResponseImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // ssList
        ArrayList<SSCode> ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        SSCode ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        // odbGeneralData
        ODBGeneralData odbGeneralData = prim.getODBGeneralData();
        assertFalse(odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertFalse(odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertFalse(odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertFalse(odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertFalse(odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertFalse(odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertFalse(odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertFalse(odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInternationalCFBarred());

        // regionalSubscriptionResponse
        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

        // supportedCamelPhases
        SupportedCamelPhases supportedCamelPhases = prim.getSupportedCamelPhases();
        assertTrue(supportedCamelPhases.getPhase1Supported());
        assertTrue(supportedCamelPhases.getPhase2Supported());
        assertTrue(supportedCamelPhases.getPhase3Supported());
        assertTrue(supportedCamelPhases.getPhase4Supported());

        // extensionContainer
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // offeredCamel4CSIs
        OfferedCamel4CSIs offeredCamel4CSIs = prim.getOfferedCamel4CSIs();
        assertTrue(offeredCamel4CSIs.getDCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getOCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());
        assertTrue(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getVtCsi());

        // supportedFeatures
        SupportedFeatures supportedFeatures = prim.getSupportedFeatures();
        assertFalse(supportedFeatures.getOdbAllApn());
        assertTrue(supportedFeatures.getOdbHPLMNApn());
        assertFalse(supportedFeatures.getOdbVPLMNApn());
        assertTrue(supportedFeatures.getOdbAllOg());
        assertFalse(supportedFeatures.getOdbAllInternationalOg());
        assertTrue(supportedFeatures.getOdbAllIntOgNotToHPLMNCountry());
        assertFalse(supportedFeatures.getOdbAllInterzonalOg());
        assertTrue(supportedFeatures.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertFalse(supportedFeatures.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(supportedFeatures.getRegSub());
        assertFalse(supportedFeatures.getTrace());
        assertTrue(supportedFeatures.getLcsAllPrivExcep());
        assertFalse(supportedFeatures.getLcsUniversal());
        assertTrue(supportedFeatures.getLcsCallSessionRelated());
        assertFalse(supportedFeatures.getLcsCallSessionUnrelated());
        assertTrue(supportedFeatures.getLcsPLMNOperator());
        assertFalse(supportedFeatures.getLcsServiceType());
        assertTrue(supportedFeatures.getLcsAllMOLRSS());
        assertFalse(supportedFeatures.getLcsBasicSelfLocation());
        assertTrue(supportedFeatures.getLcsAutonomousSelfLocation());
        assertFalse(supportedFeatures.getLcsTransferToThirdParty());
        assertTrue(supportedFeatures.getSmMoPp());
        assertFalse(supportedFeatures.getBarringOutgoingCalls());
        assertTrue(supportedFeatures.getBaoc());
        assertFalse(supportedFeatures.getBoic());
        assertTrue(supportedFeatures.getBoicExHC());

        // extSupportedFeatures
        ExtSupportedFeatures extSupportedFeatures = prim.getExtSupportedFeatures();
        if (extSupportedFeatures != null)
            assertFalse(extSupportedFeatures.isUnlicensedSpectrumAsSecondaryRAT());

        // IST Response V2 Test
        data = this.getData();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new InsertSubscriberDataResponseImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

        // ssList
        ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allSS);

        //odbGeneralData
        odbGeneralData = prim.getODBGeneralData();
        assertFalse(odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertFalse(odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertFalse(odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertFalse(odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertFalse(odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertFalse(odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertFalse(odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertFalse(odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertFalse(odbGeneralData.getRegistrationInternationalCFBarred());

        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        // Start ISD Response Version 3 Test

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);

        // ssList
        ArrayList<SSCode> ssList = new ArrayList<>();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allSS);
        ssList.add(ssCode);

        // odbGeneralData
        ODBGeneralData odbGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);

        // regionalSubscriptionResponse
        RegionalSubscriptionResponse regionalSubscriptionResponse = RegionalSubscriptionResponse.tooManyZoneCodes;

        // supportedCamelPhases
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);

        // extensionContainer
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        // offeredCamel4CSIs
        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);

        // supportedFeatures
        SupportedFeatures supportedFeatures = new SupportedFeaturesImpl(false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false, false, false, false, false, false, false, false, false, false, false, false, false, false);

        // extSupportedFeatures
        ExtSupportedFeatures extSupportedFeatures = new ExtSupportedFeaturesImpl(false);

        InsertSubscriberDataResponseImpl prim = new InsertSubscriberDataResponseImpl(3, teleserviceList, bearerServiceList,
                ssList, odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                offeredCamel4CSIs, supportedFeatures, extSupportedFeatures);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Start ISD Response Version 2 Test
        prim = new InsertSubscriberDataResponseImpl(2, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

    }
}
