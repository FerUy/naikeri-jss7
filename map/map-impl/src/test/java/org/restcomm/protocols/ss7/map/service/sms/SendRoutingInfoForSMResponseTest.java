package org.restcomm.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class SendRoutingInfoForSMResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 27, 4, 8, 2, -112, 9, 2, 16, 17, 34, -9, -96, 15, -127, 7, -111, 33, 48, 18, 0, -110, -11, 4,
                4, 0, 3, 98, 49 };
    }

    private byte[] getEncodedData0() {
        return new byte[] { 48, 115, 4, 7, 17, 1, 35, 34, 51, 19, 17, (byte) 160, 63, (byte) 129, 5, (byte) 198, 0, 0, 17, 17, 4, 4, 0, 2, 1, 0, 48, 39,
                (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161,
                3, 31, 32, 33, (byte) 166, 7, (byte) 129, 5, (byte) 166, (byte) 153, (byte) 153, (byte) 153, (byte) 153, (byte) 164, 39, (byte) 160, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 22, 4, 7, 82, 0, 17, 17, 17, 17, 17, -96, 8, -127, 6, -111, -105, -103, 25, 17, 17, -126, 1, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 30, 4, 7, 82, 0, 17, 17, 17, 17, 17, -96, 8, -127, 6, -111, -105, -103, 25, 17, 17, -126, 1, 0, -91, 6, 2, 1, 30, 2, 1, 40 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x31,
                0x04, 0x08, 0x47, 0x08, 0x13, 0x32,
                0x54, 0x76, (byte) 0x98, (byte) 0xf1, (byte) 0xa0, 0x25, (byte) 0x81, 0x07,
                (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00, 0x23, 0x04,
                0x04, 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce, (byte) 0xa6, 0x09, (byte) 0x80,
                0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00, 0x23,
                (byte) 0xa9, 0x09, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19,
                0x09, 0x10, 0x13
        };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] {0x30, (byte) 0x82,
                0x01, 0x36, 0x04, 0x08,
                0x47, 0x08, 0x13, 0x32, 0x54, 0x76, (byte) 0x98, (byte) 0xf1,
                (byte) 0xa0, (byte) 0x82, 0x01, 0x28, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x19, 0x09, 0x00, 0x23, 0x04, 0x04, 0x72,
                0x02, (byte) 0xe9, (byte) 0x8c, (byte) 0xa7, 0x5b, (byte) 0x80, 0x36, 0x6d,
                0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d,
                0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e,
                0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65,
                0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67,
                (byte) 0xa8, 0x5c, (byte) 0x80, 0x37, 0x6d, 0x6d, 0x65, 0x63,
                0x30, 0x33, 0x31, 0x2e, 0x6d, 0x6d, 0x65, 0x67,
                0x69, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d,
                0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e,
                0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63,
                0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70,
                0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e,
                0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0xaa, 0x5c,
                (byte) 0x80, 0x37, 0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33,
                0x32, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33,
                0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67
        };
    }

    private byte[] getEncodedDataRel18_2() {
        return new byte[] { 0x30, 0x7d,
                0x04, 0x08, 0x47, 0x08, 0x13,
                0x32, 0x54, 0x76, (byte) 0x98, (byte) 0xf1, (byte) 0xa0, 0x71, (byte) 0x81,
                0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00, 0x23,
                (byte) 0x8c, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x50,
                0x50, (byte) 0xad, 0x5b, (byte) 0x80, 0x36, 0x6d, 0x6d, 0x65,
                0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d, 0x65, 0x67,
                0x69, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d,
                0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e,
                0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63,
                0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70,
                0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e,
                0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x90, 0x00
        };
    }

    private byte[] getEncodedDataRel18_3() {
        return new byte[] { 0x30, 0x57,
                0x04, 0x08, 0x47, 0x08, 0x13, 0x32,
                0x54, 0x76, (byte) 0x98, (byte) 0xf1, (byte) 0xa0, 0x4b, (byte) 0x81, 0x07,
                (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00, 0x23, 0x04,
                0x04, 0x72, 0x02, (byte) 0xe7, (byte) 0xd5, (byte) 0x8e, 0x07, (byte) 0x91,
                (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x60, 0x00, (byte) 0xaf, 0x2f,
                (byte) 0x80, 0x14, 0x73, 0x6d, 0x73, 0x66, 0x30, 0x33,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, (byte) 0x81, 0x17,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x74, 0x65,
                0x6c, 0x63, 0x6f, 0x2e, 0x63, 0x6f, 0x6d, (byte) 0x91,
                0x00
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "200990200111227");
        ISDNAddressString networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "12032100295");
        LMSI lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertEquals(lmsi.getData(), new byte[] { 0, 3, 98, 49 });
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        boolean gprsNodeIndicator = ind.getLocationInfoWithLMSI().getGprsNodeIndicator();
        assertFalse(gprsNodeIndicator);
        AdditionalNumber additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        NetworkNodeDiameterAddress networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        AdditionalNumber thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        boolean imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        ISDNAddressString smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        NetworkNodeDiameterAddress smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        ISDNAddressString smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        NetworkNodeDiameterAddress smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        boolean smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        boolean smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        IpSmGwGuidance ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        Boolean mwdSet = ind.getMwdSet();
        assertNull(mwdSet);

        // test 2
        rawData = getEncodedData0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "11103222333111");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.subscriber_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(networkNodeNumber.getAddress(), "00001111");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertTrue(Arrays.equals(new byte[] { 0, 2, 1, 0 }, lmsi.getData()));
        extensionContainer = ind.getLocationInfoWithLMSI().getExtensionContainer();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertEquals(additionalNumber.getSGSNNumber().getAddressNature(), AddressNature.national_significant_number);
        assertEquals(additionalNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(additionalNumber.getSGSNNumber().getAddress(), "99999999");
        assertFalse(additionalNumber.getSGSNNumber().isExtension());
        assertNull(additionalNumber.getMSCNumber());
        extensionContainer = ind.getExtensionContainer();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        gprsNodeIndicator = ind.getLocationInfoWithLMSI().getGprsNodeIndicator();
        assertFalse(gprsNodeIndicator);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        mwdSet = ind.getMwdSet();
        assertNull(mwdSet);

        // test 3
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "25001111111111");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "7999911111");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertNull(lmsi);
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        assertFalse(ind.getMwdSet());

        // test 4
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "25001111111111");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "7999911111");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertNull(lmsi);
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertEquals(ipSmGwGuidance.getMinimumDeliveryTimeValue(), 30);
        assertEquals(ipSmGwGuidance.getRecommendedDeliveryTimeValue(), 40);
        assertNull(ipSmGwGuidance.getExtensionContainer());
        assertFalse(ind.getMwdSet());

        // test 5 MAP v18.0.0 locationInfoWithLMSI with additional-Number and thirdNumber
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 71ffacce
         *                 additional-Number: msc-Number (0)
         *                     msc-Number: 91959819090023
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 598991900032
         *                 thirdNumber: msc-Number (0)
         *                     msc-Number: 91959819091013
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 598991900131
         */
        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "748031234567891");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertEquals(lmsi.getData(), new byte[] { 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce });
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber.getSGSNNumber());
        assertEquals(additionalNumber.getMSCNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(additionalNumber.getMSCNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(additionalNumber.getMSCNumber().getAddress(), "598991900032");
        assertFalse(additionalNumber.getMSCNumber().isExtension());
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber.getSGSNNumber());
        assertEquals(thirdNumber.getMSCNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(thirdNumber.getMSCNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(thirdNumber.getMSCNumber().getAddress(), "598991900131");
        assertFalse(thirdNumber.getMSCNumber().isExtension());
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        mwdSet = ind.getMwdSet();
        assertNull(mwdSet);

        // test 6 MAP v18.0.0 locationInfoWithLMSI with networkNodeDiameterAddress, additionalNetworkNodeDiameterAddress and thirdNetworkNodeDiameterAddress
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 7202e98c
         *                 networkNodeDiameterAddress
         *                     diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 additionalNetworkNodeDiameterAddress
         *                     diameter-Name: mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 thirdNetworkNodeDiameterAddress
         *                     diameter-Name: mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         */
        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "748031234567891");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertEquals(lmsi.getData(), new byte[] { 0x72, 0x02, (byte) 0xe9, (byte) 0x8c });
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertEquals(networkNodeDiameterAddress.getDiameterName().getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(networkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertEquals(additionalNetworkNodeDiameterAddress.getDiameterName().getData(), "mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(additionalNetworkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertEquals(thirdNetworkNodeDiameterAddress.getDiameterName().getData(), "mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(thirdNetworkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        mwdSet = ind.getMwdSet();
        assertNull(mwdSet);

        // test 7 MAP v18.0.0 locationInfoWithLMSI with smsf-3gpp-Number, smsf-3gpp-DiameterAddress and smsf-3gpp-address-indicator
        rawData = getEncodedDataRel18_2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 smsf-3gpp-Number: 91959819095050
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900505
         *                 smsf-3gpp-DiameterAddress
         *                     diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 smsf-3gpp-address-indicator
         */
        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "748031234567891");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertNull(lmsi);
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertEquals(smsf3gppNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(smsf3gppNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsf3gppNumber.getAddress(), "598991900505");
        assertFalse(smsf3gppNumber.isExtension());
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertEquals(smsf3gppDiameterAddress.getDiameterName().getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(smsf3gppDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertTrue(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        mwdSet = ind.getMwdSet();
        assertNull(mwdSet);

        // test 8 MAP v18.0.0 locationInfoWithLMSI with smsf-non-3gpp-Number, smsf-non-3gpp-DiameterAddress and smsf-non-3gpp-address-indicator
        rawData = getEncodedDataRel18_3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 7202e7d5
         *                 smsf-non-3gpp-Number: 91959819096000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900600
         *                 smsf-non-3gpp-DiameterAddress
         *                     diameter-Name: smsf03.mnc002.mcc748
         *                     diameter-Realm: mnc002.mcc748.telco.com
         *                 smsf-non-3gpp-address-indicator
         */
        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "748031234567891");
        networkNodeNumber = ind.getLocationInfoWithLMSI().getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = ind.getLocationInfoWithLMSI().getLMSI();
        assertEquals(lmsi.getData(), new byte[] { 0x72, 0x02, (byte) 0xe7, (byte) 0xd5 });
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = ind.getLocationInfoWithLMSI().getAdditionalNumber();
        assertNull(additionalNumber);
        thirdNumber = ind.getLocationInfoWithLMSI().getThirdNumber();
        assertNull(thirdNumber);
        networkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNetworkNodeDiameterAddress = ind.getLocationInfoWithLMSI().getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = ind.getLocationInfoWithLMSI().getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = ind.getLocationInfoWithLMSI().getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = ind.getLocationInfoWithLMSI().getSmsfNon3gppNumber();
        assertEquals(smsfNon3gppNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(smsfNon3gppNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsfNon3gppNumber.getAddress(), "598991900600");
        assertFalse(smsfNon3gppNumber.isExtension());
        smsfNon3gppDiameterAddress = ind.getLocationInfoWithLMSI().getSmsfNon3gppDiameterAddress();
        assertEquals(smsfNon3gppDiameterAddress.getDiameterName().getData(), "smsf03.mnc002.mcc748".getBytes(StandardCharsets.UTF_8));
        assertEquals(smsfNon3gppDiameterAddress.getDiameterRealm().getData(), "mnc002.mcc748.telco.com".getBytes(StandardCharsets.UTF_8));
        smsfNon3gppAddressIndicator = ind.getLocationInfoWithLMSI().getSmsfNon3gppAddressIndicator();
        assertTrue(smsfNon3gppAddressIndicator);
        ipSmGwGuidance = ind.getIpSmGwGuidance();
        assertNull(ipSmGwGuidance);
        mwdSet = ind.getMwdSet();
        assertNull(mwdSet);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        IMSI imsi = new IMSIImpl("200990200111227");
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12032100295");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });

        LocationInfoWithLMSI locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, null, null,
                null, null, null, false, null, null,
                null, null, false, false);
        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        imsi = new IMSIImpl("11103222333111");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.subscriber_number, NumberingPlan.land_mobile, "00001111");
        lmsi = new LMSIImpl(new byte[] { 0, 2, 1, 0 });
        ISDNAddressString sgsnAdditionalNumber = new ISDNAddressStringImpl(AddressNature.national_significant_number,
                NumberingPlan.land_mobile, "99999999");
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnAdditionalNumber);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), false, additionalNumber, null,
                null, null, null, false, null, null,
                null, null, false, false);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, MAPExtensionContainerTest.GetTestExtensionContainer(), null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3
        imsi = new IMSIImpl("25001111111111");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7999911111");
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, null, null, false, null, null,
                null, null, null, false, null, null,
                null, null, false, false);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4
        imsi = new IMSIImpl("25001111111111");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7999911111");
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, null, null, false, null, null,
                null, null, null, false, null, null,
                null, null, false, false);
        IpSmGwGuidanceImpl ipSmGwGuidance = new IpSmGwGuidanceImpl(30, 40, null);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, false, ipSmGwGuidance);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 MAP v18.0.0 locationInfoWithLMSI with additional-Number and thirdNumber
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 71ffacce
         *                 additional-Number: msc-Number (0)
         *                     msc-Number: 91959819090023
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 598991900032
         *                 thirdNumber: msc-Number (0)
         *                     msc-Number: 91959819091013
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 598991900131
         */
        imsi = new IMSIImpl("748031234567891");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce });
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        additionalNumber = new AdditionalNumberImpl(mscNumber, null);
        ISDNAddressString trdNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900131");
        AdditionalNumber thirdNumber = new AdditionalNumberImpl(trdNumber, null);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, additionalNumber, null,
                null, thirdNumber, null, false, null, null,
                null, null, false, false);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6 MAP v18.0.0 locationInfoWithLMSI with networkNodeDiameterAddress, additionalNetworkNodeDiameterAddress and thirdNetworkNodeDiameterAddress
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 7202e98c
         *                 networkNodeDiameterAddress
         *                     diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 additionalNetworkNodeDiameterAddress
         *                     diameter-Name: mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 thirdNetworkNodeDiameterAddress
         *                     diameter-Name: mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         */
        imsi = new IMSIImpl("748031234567891");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x72, 0x02, (byte) 0xe9, (byte) 0x8c });
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        DiameterIdentity mmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        DiameterIdentity addMmeName = new DiameterIdentityImpl("mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        DiameterIdentity addMmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        DiameterIdentity thirdMmeName = new DiameterIdentityImpl("mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        DiameterIdentity thirdMmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        NetworkNodeDiameterAddress networkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm);
        NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(addMmeName, addMmeRealm);
        NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(thirdMmeName, thirdMmeRealm);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, null, networkNodeDiameterAddress,
                additionalNetworkNodeDiameterAddress, null, thirdNetworkNodeDiameterAddress, false, null, null,
                null, null, false, false);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 7 MAP v18.0.0 locationInfoWithLMSI with smsf-3gpp-Number, smsf-3gpp-DiameterAddress and smsf-3gpp-address-indicator
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 smsf-3gpp-Number: 91959819095050
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900505
         *                 smsf-3gpp-DiameterAddress
         *                     diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                     diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *                 smsf-3gpp-address-indicator
         */
        imsi = new IMSIImpl("748031234567891");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        ISDNAddressString smsf3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900505");
        DiameterIdentity smsfName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity smsfRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsf3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfName, smsfRealm);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, null, null, false, null, null,
                null, null, null, false, smsf3gppNumber, smsf3gppDiameterAddress,
                null, null, true, false);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_2();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 8 MAP v18.0.0 locationInfoWithLMSI with smsf-non-3gpp-Number, smsf-non-3gpp-DiameterAddress and smsf-non-3gpp-address-indicator
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: sendRoutingInfoForSM (45)
         *             IMSI: 748031234567891
         *             [Association IMSI: 748031234567891]
         *             locationInfoWithLMSI
         *                 networkNode-Number: 91959819090023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900032
         *                 lmsi: 7202e7d5
         *                 smsf-non-3gpp-Number: 91959819096000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 598991900600
         *                 smsf-non-3gpp-DiameterAddress
         *                     diameter-Name: smsf03.mnc002.mcc748
         *                     diameter-Realm: mnc002.mcc748.telco.com
         *                 smsf-non-3gpp-address-indicator
         */
        imsi = new IMSIImpl("748031234567891");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x72, 0x02, (byte) 0xe7, (byte) 0xd5 });
        ISDNAddressString smsfNon3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900600");
        DiameterIdentity smsfNon3gppName = new DiameterIdentityImpl("smsf03.mnc002.mcc748".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity smsfNon3gppRealm = new DiameterIdentityImpl("mnc002.mcc748.telco.com".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsfNon3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfNon3gppName, smsfNon3gppRealm);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, null, null,
                null, null, null, false, null, null,
                smsfNon3gppNumber, smsfNon3gppDiameterAddress, false, true);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_3();

        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
