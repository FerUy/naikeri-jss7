
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ExtSupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class SGSNCapabilityTest {

    public byte[] getData() {
        return new byte[] { 48, 81, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 2, -128, 0, -125, 0, -124, 2, 4, -16,
                -123, 2, 3, -8, -122, 2, 1, -2, -121, 0, -120, 2, 3, -8, -119, 5, 6, -1, -1, -1, -64, -121, 0, -118, 0, -117,
                1, -1 };
    }

    public byte[] getData1() {
        return new byte[] { 48, 51,
                (byte) 0xa2, 0x02, (byte) 0x80, 0x00, (byte) 0x83, 0x00, (byte) 0x84, 0x02,
                0x04, (byte) 0xe0, (byte) 0x85, 0x02, 0x03, (byte) 0xf0, (byte) 0x86, 0x02,
                0x01, 0x0e, (byte) 0x87, 0x00, (byte) 0x88, 0x02, 0x02, (byte) 0xdc,
                (byte) 0x89, 0x06, 0x00, 0x00, 0x1f, (byte) 0xfe, 0x3f, (byte) 0xff,
                (byte) 0x87, 0x00, (byte) 0x8a, (byte) 0x00, (byte) 0x8b, 0x01, (byte) 0xff,
                (byte) 0x8c, 0x00, (byte) 0x8e, 0x00, (byte) 0x8f, 0x00, (byte) 0x90, 0x00,
                (byte) 0x91, 0x02, 0x07, (byte) 0x80 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // Test 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SGSNCapabilityImpl prim = new SGSNCapabilityImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getSolsaSupportIndicator());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertTrue(prim.getGprsEnhancementsSupportIndicator());
        assertTrue(prim.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(prim.getOfferedCamel4CSIs().getDCsi());
        assertTrue(prim.getSmsCallBarringSupportIndicator());
        assertTrue(prim.getSupportedRATTypesIndicator().getEUtran());
        assertTrue(prim.getSupportedFeatures().getBaoc());
        assertTrue(prim.getTAdsDataRetrieval());
        assertTrue(prim.getHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertFalse(prim.getCancellationTypeInitialAttach());
        assertFalse(prim.getMsisdnlessOperationSupported());
        assertFalse(prim.getUpdateOfHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertFalse(prim.getResetIdsSupported());
        assertNull(prim.getExtSupportedFeatures());

        // Test 2
        data = this.getData1();
        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SGSNCapabilityImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.getSolsaSupportIndicator());
        assertNull(prim.getExtensionContainer());
        assertTrue(prim.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertTrue(prim.getGprsEnhancementsSupportIndicator());
        assertTrue(prim.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(prim.getSupportedCamelPhases().getPhase2Supported());
        assertTrue(prim.getSupportedCamelPhases().getPhase3Supported());
        assertFalse(prim.getSupportedCamelPhases().getPhase4Supported());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
        assertFalse(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
        assertFalse(prim.getOfferedCamel4CSIs().getOCsi());
        assertFalse(prim.getOfferedCamel4CSIs().getDCsi());
        assertFalse(prim.getOfferedCamel4CSIs().getVtCsi());
        assertFalse(prim.getOfferedCamel4CSIs().getTCsi());
        assertTrue(prim.getOfferedCamel4CSIs().getMtSmsCsi());
        assertTrue(prim.getOfferedCamel4CSIs().getMgCsi());
        assertTrue(prim.getOfferedCamel4CSIs().getPsiEnhancements());
        assertTrue(prim.getSmsCallBarringSupportIndicator());
        assertTrue(prim.getSupportedRATTypesIndicator().getEUtran());
        assertTrue(prim.getSupportedRATTypesIndicator().getGeran());
        assertFalse(prim.getSupportedRATTypesIndicator().getGan());
        assertTrue(prim.getSupportedRATTypesIndicator().getEUtran());
        assertTrue(prim.getSupportedRATTypesIndicator().getNbIot());
        assertFalse(prim.getSupportedFeatures().getOdbAllApn());
        assertFalse(prim.getSupportedFeatures().getOdbHPLMNApn());
        assertFalse(prim.getSupportedFeatures().getOdbVPLMNApn());
        assertFalse(prim.getSupportedFeatures().getOdbAllOg());
        assertFalse(prim.getSupportedFeatures().getOdbAllInternationalOg());
        assertFalse(prim.getSupportedFeatures().getOdbAllIntOgNotToHPLMNCountry());
        assertFalse(prim.getSupportedFeatures().getOdbAllInterzonalOg());
        assertFalse(prim.getSupportedFeatures().getOdbAllInterzonalOgNotToHPLMNCountry());
        assertFalse(prim.getSupportedFeatures().getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertFalse(prim.getSupportedFeatures().getRegSub());
        assertFalse(prim.getSupportedFeatures().getTrace());
        assertTrue(prim.getSupportedFeatures().getLcsAllPrivExcep());
        assertTrue(prim.getSupportedFeatures().getLcsUniversal());
        assertTrue(prim.getSupportedFeatures().getLcsCallSessionRelated());
        assertTrue(prim.getSupportedFeatures().getLcsCallSessionUnrelated());
        assertTrue(prim.getSupportedFeatures().getLcsPLMNOperator());
        assertTrue(prim.getSupportedFeatures().getLcsServiceType());
        assertTrue(prim.getSupportedFeatures().getLcsAllMOLRSS());
        assertTrue(prim.getSupportedFeatures().getLcsBasicSelfLocation());
        assertTrue(prim.getSupportedFeatures().getLcsAutonomousSelfLocation());
        assertTrue(prim.getSupportedFeatures().getLcsTransferToThirdParty());
        assertTrue(prim.getSupportedFeatures().getSmMoPp());
        assertTrue(prim.getSupportedFeatures().getBarringOutgoingCalls());
        assertFalse(prim.getSupportedFeatures().getBaoc());
        assertFalse(prim.getSupportedFeatures().getBoic());
        assertFalse(prim.getSupportedFeatures().getBoicExHC());
        assertTrue(prim.getSupportedFeatures().getLocalTimeZoneRetrieval());
        assertTrue(prim.getSupportedFeatures().getAdditionalMsisdn());
        assertTrue(prim.getSupportedFeatures().getSmsInMME());
        assertTrue(prim.getSupportedFeatures().getSmsInSGSN());
        assertTrue(prim.getSupportedFeatures().getUeReachabilityNotification());
        assertTrue(prim.getSupportedFeatures().getStateLocationInformationRetrieval());
        assertTrue(prim.getSupportedFeatures().getPartialPurge());
        assertTrue(prim.getSupportedFeatures().getGddInSGSN());
        assertTrue(prim.getSupportedFeatures().getSgsnCAMELCapability());
        assertTrue(prim.getSupportedFeatures().getPcscfRestoration());
        assertTrue(prim.getSupportedFeatures().getDedicatedCoreNetworks());
        assertTrue(prim.getSupportedFeatures().getNonIPPDNTypeAPNs());
        assertTrue(prim.getSupportedFeatures().getNonIPPDPTypeAPNs());
        assertTrue(prim.getSupportedFeatures().getNrAsSecondaryRAT());
        assertTrue(prim.getTAdsDataRetrieval());
        assertTrue(prim.getHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertTrue(prim.getCancellationTypeInitialAttach());
        assertTrue(prim.getMsisdnlessOperationSupported());
        assertTrue(prim.getUpdateOfHomogeneousSupportOfIMSVoiceOverPSSessions());
        assertTrue(prim.getResetIdsSupported());
        assertTrue(prim.getExtSupportedFeatures().isUnlicensedSpectrumAsSecondaryRAT());

    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {

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
        boolean homogeneousSupportOfIMSVoiceOverPSSessions = true;
        boolean cancellationTypeInitialAttach = true;
        boolean misdnlessOperationSupported = true;
        boolean updateOfHomogeneousSupportOfIMSVoiceOverPSSessions = true;
        boolean resetIdsSupported = true;
        boolean unlicensedSpectrumAsSecondaryRAT = true;
        ExtSupportedFeatures extSupportedFeatures = new ExtSupportedFeaturesImpl(unlicensedSpectrumAsSecondaryRAT);
        MAPExtensionContainer extensionContainer = null;
        SGSNCapabilityImpl sgsnCapability = new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer,
                superChargerSupportedInServingNetworkEntity, gprsEnhancementsSupportIndicator, supportedCamelPhases,
                supportedLCSCapabilitySets, offeredCamel4CSIs, smsCallBarringSupportIndicator, supportedRATTypesIndicator,
                supportedFeatures, tAdsDataRetrieval, homogeneousSupportOfIMSVoiceOverPSSessions, cancellationTypeInitialAttach,
                misdnlessOperationSupported, updateOfHomogeneousSupportOfIMSVoiceOverPSSessions, resetIdsSupported,
                extSupportedFeatures);

        AsnOutputStream asn = new AsnOutputStream();
        sgsnCapability.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
    }

}
