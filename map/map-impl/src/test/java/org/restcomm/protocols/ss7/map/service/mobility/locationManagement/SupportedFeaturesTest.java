package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class SupportedFeaturesTest {

    public byte[] getData() {
        return new byte[] { 0x03, 0x05, 0x06, 0x55, 0x55, 0x55, 0x40 };
    }

    public byte[] getData1() {
        // return new byte[] { 3, 5, 6, -86, -86, -86, -128 }; old
        return new byte[] { 0x03, 0x06, 0x00, (byte) 0xaa, (byte) 0xaa, (byte) 0xaa, (byte) 0x80, 0x00 };
    }

    public byte[] getData2() {
        return new byte[] { 0x03, 0x06, 0x00, 0x00, (byte) 0x1f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        // test 1
        byte[] data = this.getData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SupportedFeaturesImpl prim = new SupportedFeaturesImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.getOdbAllApn());
        assertTrue(prim.getOdbHPLMNApn());
        assertFalse(prim.getOdbVPLMNApn());
        assertTrue(prim.getOdbAllOg());
        assertFalse(prim.getOdbAllInternationalOg());
        assertTrue(prim.getOdbAllIntOgNotToHPLMNCountry());
        assertFalse(prim.getOdbAllInterzonalOg());
        assertTrue(prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertFalse(prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(prim.getRegSub());
        assertFalse(prim.getTrace());
        assertTrue(prim.getLcsAllPrivExcep());
        assertFalse(prim.getLcsUniversal());
        assertTrue(prim.getLcsCallSessionRelated());
        assertFalse(prim.getLcsCallSessionUnrelated());
        assertTrue(prim.getLcsPLMNOperator());
        assertFalse(prim.getLcsServiceType());
        assertTrue(prim.getLcsAllMOLRSS());
        assertFalse(prim.getLcsBasicSelfLocation());
        assertTrue(prim.getLcsAutonomousSelfLocation());
        assertFalse(prim.getLcsTransferToThirdParty());
        assertTrue(prim.getSmMoPp());
        assertFalse(prim.getBarringOutgoingCalls());
        assertTrue(prim.getBaoc());
        assertFalse(prim.getBoic());
        assertTrue(prim.getBoicExHC());
        assertFalse(prim.getLocalTimeZoneRetrieval());
        assertFalse(prim.getAdditionalMsisdn());
        assertFalse(prim.getSmsInMME());
        assertFalse(prim.getSmsInSGSN());
        assertFalse(prim.getUeReachabilityNotification());
        assertFalse(prim.getStateLocationInformationRetrieval());
        assertFalse(prim.getPartialPurge());
        assertFalse(prim.getGddInSGSN());
        assertFalse(prim.getSgsnCAMELCapability());
        assertFalse(prim.getPcscfRestoration());
        assertFalse(prim.getDedicatedCoreNetworks());
        assertFalse(prim.getNonIPPDNTypeAPNs());
        assertFalse(prim.getNonIPPDPTypeAPNs());
        assertFalse(prim.getNrAsSecondaryRAT());

        // test 2
        data = this.getData1();

        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SupportedFeaturesImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getOdbAllApn());
        assertFalse(prim.getOdbHPLMNApn());
        assertTrue(prim.getOdbVPLMNApn());
        assertFalse(prim.getOdbAllOg());
        assertTrue(prim.getOdbAllInternationalOg());
        assertFalse(prim.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOg());
        assertFalse(prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertFalse(prim.getRegSub());
        assertTrue(prim.getTrace());
        assertFalse(prim.getLcsAllPrivExcep());
        assertTrue(prim.getLcsUniversal());
        assertFalse(prim.getLcsCallSessionRelated());
        assertTrue(prim.getLcsCallSessionUnrelated());
        assertFalse(prim.getLcsPLMNOperator());
        assertTrue(prim.getLcsServiceType());
        assertFalse(prim.getLcsAllMOLRSS());
        assertTrue(prim.getLcsBasicSelfLocation());
        assertFalse(prim.getLcsAutonomousSelfLocation());
        assertTrue(prim.getLcsTransferToThirdParty());
        assertFalse(prim.getSmMoPp());
        assertTrue(prim.getBarringOutgoingCalls());
        assertFalse(prim.getBaoc());
        assertTrue(prim.getBoic());
        assertFalse(prim.getBoicExHC());
        assertFalse(prim.getLocalTimeZoneRetrieval());
        assertFalse(prim.getAdditionalMsisdn());
        assertFalse(prim.getSmsInMME());
        assertFalse(prim.getSmsInSGSN());
        assertFalse(prim.getUeReachabilityNotification());
        assertFalse(prim.getStateLocationInformationRetrieval());
        assertFalse(prim.getPartialPurge());
        assertFalse(prim.getGddInSGSN());
        assertFalse(prim.getSgsnCAMELCapability());
        assertFalse(prim.getPcscfRestoration());
        assertFalse(prim.getDedicatedCoreNetworks());
        assertFalse(prim.getNonIPPDNTypeAPNs());
        assertFalse(prim.getNonIPPDPTypeAPNs());
        assertFalse(prim.getNrAsSecondaryRAT());

        // test 3
        data = this.getData2();

        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SupportedFeaturesImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(prim.getOdbAllApn());
        assertFalse(prim.getOdbHPLMNApn());
        assertFalse(prim.getOdbVPLMNApn());
        assertFalse(prim.getOdbAllOg());
        assertFalse(prim.getOdbAllInternationalOg());
        assertFalse(prim.getOdbAllIntOgNotToHPLMNCountry());
        assertFalse(prim.getOdbAllInterzonalOg());
        assertFalse(prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertFalse(prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertFalse(prim.getRegSub());
        assertFalse(prim.getTrace());
        assertTrue(prim.getLcsAllPrivExcep());
        assertTrue(prim.getLcsUniversal());
        assertTrue(prim.getLcsCallSessionRelated());
        assertTrue(prim.getLcsCallSessionUnrelated());
        assertTrue(prim.getLcsPLMNOperator());
        assertTrue(prim.getLcsServiceType());
        assertTrue(prim.getLcsAllMOLRSS());
        assertTrue(prim.getLcsBasicSelfLocation());
        assertTrue(prim.getLcsAutonomousSelfLocation());
        assertTrue(prim.getLcsTransferToThirdParty());
        assertTrue(prim.getSmMoPp());
        assertTrue(prim.getBarringOutgoingCalls());
        assertTrue(prim.getBaoc());
        assertTrue(prim.getBoic());
        assertTrue(prim.getBoicExHC());
        assertTrue(prim.getLocalTimeZoneRetrieval());
        assertTrue(prim.getAdditionalMsisdn());
        assertTrue(prim.getSmsInMME());
        assertTrue(prim.getSmsInSGSN());
        assertTrue(prim.getUeReachabilityNotification());
        assertTrue(prim.getStateLocationInformationRetrieval());
        assertTrue(prim.getPartialPurge());
        assertTrue(prim.getGddInSGSN());
        assertTrue(prim.getSgsnCAMELCapability());
        assertTrue(prim.getPcscfRestoration());
        assertTrue(prim.getDedicatedCoreNetworks());
        assertTrue(prim.getNonIPPDNTypeAPNs());
        assertTrue(prim.getNonIPPDPTypeAPNs());
        assertTrue(prim.getNrAsSecondaryRAT());
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
        // Test 1
        SupportedFeaturesImpl prim = new SupportedFeaturesImpl(true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false, false);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // Test 2
        prim = new SupportedFeaturesImpl(false, false, false, false, false, false, false, false, false, false,
                false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

    }

}
