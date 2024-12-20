
package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class LocationInfoWithLMSITest {

    private byte[] getEncodedData0() {
        return new byte[] { -96, 15, -127, 7, -111, -105, 48, 115, 0, 34, -14, 4, 4, 0, 3, 98, 49 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { (byte) 160, 67, (byte) 129, 6, (byte) 168, 33, 67, 101, (byte) 135, 9, 4, 4, 4, 3, 2, 1, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 133, 0,
                (byte) 166, 8, (byte) 129, 6, (byte) 185, (byte) 137, 103, 69, 35, (byte) 241 };
    }

    private byte[] getEncodedDataNnnLmsiAddNumThirdNum() {
        return new byte[] { (byte) 0xa0, 0x27,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00,
                0x23, 0x04, 0x04, 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce, (byte) 0x85,
                0x00, (byte) 0xa6, 0x09, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x19, 0x09, 0x10, 0x03, (byte) 0xa9, 0x09, (byte) 0x81, 0x07,
                (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x10, 0x23
        };
    }

    private byte[] getEncodedDataNnnLmsiNnDiamAddNodeDiamThDiam() {
        return new byte[] { (byte) 0xa0, (byte) 0x82, 0x01, 0x28,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00,
                0x23, 0x04, 0x04, 0x72, 0x02, (byte) 0xe9, (byte) 0x8c, (byte) 0xa7,
                0x5b, (byte) 0x80, 0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30,
                0x33, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33,
                0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0xa8, 0x5c, (byte) 0x80, 0x37,
                0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33, 0x31, 0x2e,
                0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33, 0x30, 0x30,
                0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x81,
                0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0xaa, 0x5c, (byte) 0x80, 0x37, 0x6d, 0x6d,
                0x65, 0x63, 0x30, 0x33, 0x32, 0x2e, 0x6d, 0x6d,
                0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e,
                0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65,
                0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67
        };
    }

    private byte[] getEncodedDataNnnSmsf() {
        return new byte[] { (byte) 0xa0, 0x71,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00,
                0x23, (byte) 0x8c, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09,
                0x50, 0x50, (byte) 0xad, 0x5b, (byte) 0x80, 0x36, 0x6d, 0x6d,
                0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d, 0x65,
                0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x6d,
                0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x90,
                0x00
        };
    }

    private byte[] getEncodedDataNnnSmsfNon3gpp() {
        return new byte[] { (byte) 0xa0, 0x4b,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x00,
                0x23, 0x04, 0x04, 0x72, 0x02, (byte) 0xe7, (byte) 0xd5, (byte) 0x8e,
                0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x60, 0x00,
                (byte) 0xaf, 0x2f, (byte) 0x80, 0x14, 0x73, 0x6d, 0x73, 0x66,
                0x30, 0x33, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                (byte) 0x81, 0x17, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x74, 0x65, 0x6c, 0x63, 0x6f, 0x2e, 0x63, 0x6f,
                0x6d, (byte) 0x91, 0x00
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData0();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        LocationInfoWithLMSIImpl locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "79033700222");
        LMSI lmsi = locationInfoWithLMSI.getLMSI();
        assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 0, 3, 98, 49 }));
        MAPExtensionContainer extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertNull(extensionContainer);
        boolean gprsNodeIndicator = locationInfoWithLMSI.getGprsNodeIndicator();
        assertFalse(gprsNodeIndicator);
        AdditionalNumber additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber);
        NetworkNodeDiameterAddress networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        AdditionalNumber thirdNumber = locationInfoWithLMSI.getThirdNumber();
        assertNull(thirdNumber);
        NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        boolean imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        ISDNAddressString smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        NetworkNodeDiameterAddress smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        ISDNAddressString smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        NetworkNodeDiameterAddress smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        boolean smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        boolean smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);

        // test 2
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.national_significant_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.national);
        assertEquals(networkNodeNumber.getAddress(), "1234567890");
        lmsi = locationInfoWithLMSI.getLMSI();
        assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 4, 3, 2, 1 }));
        extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertEquals(extensionContainer, MAPExtensionContainerTest.GetTestExtensionContainer());
        gprsNodeIndicator = locationInfoWithLMSI.getGprsNodeIndicator();
        assertTrue(gprsNodeIndicator);
        additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber.getMSCNumber());
        assertEquals(additionalNumber.getSGSNNumber().getAddressNature(), AddressNature.network_specific_number);
        assertEquals(additionalNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.private_plan);
        assertEquals(additionalNumber.getSGSNNumber().getAddress(), "987654321");
        networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNumber = locationInfoWithLMSI.getThirdNumber();
        assertNull(thirdNumber);
        thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);

        // test 3 locationInfoWithLMSI with networkNodeNumber, lmsi, additionalNumber and thirdNumber
        rawData = getEncodedDataNnnLmsiAddNumThirdNum();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 71ffacce
         *     gprsNodeIndicator
         *     additional-Number: msc-Number (0)
         *         msc-Number: 91959819091003
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900130
         *     thirdNumber: sgsn-Number (1)
         *         sgsn-Number: 91959819091023
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900132
         */
        networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = locationInfoWithLMSI.getLMSI();
        assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce }));
        extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber.getSGSNNumber());
        assertEquals(additionalNumber.getMSCNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(additionalNumber.getMSCNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(additionalNumber.getMSCNumber().getAddress(), "598991900130");
        assertFalse(additionalNumber.getMSCNumber().isExtension());
        networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNumber = locationInfoWithLMSI.getThirdNumber();
        assertNull(thirdNumber.getMSCNumber());
        assertEquals(thirdNumber.getSGSNNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(thirdNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(thirdNumber.getSGSNNumber().getAddress(), "598991900132");
        assertFalse(thirdNumber.getSGSNNumber().isExtension());
        thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);

        // test 4 locationInfoWithLMSI with networkNodeDiameterAddress, additionalNetworkNodeDiameterAddress and thirdNetworkNodeDiameterAddress
        rawData = getEncodedDataNnnLmsiNnDiamAddNodeDiamThDiam();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 7202e98c
         *     networkNodeDiameterAddress
         *         diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     additionalNetworkNodeDiameterAddress
         *         diameter-Name: mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     thirdNetworkNodeDiameterAddress
         *         diameter-Name: mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         */
        networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = locationInfoWithLMSI.getLMSI();
        assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 0x72, (byte) 0x02, (byte) 0xe9, (byte) 0x8c }));
        extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber);
        networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertEquals(networkNodeDiameterAddress.getDiameterName().getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(networkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertEquals(additionalNetworkNodeDiameterAddress.getDiameterName().getData(), "mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(additionalNetworkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertEquals(thirdNetworkNodeDiameterAddress.getDiameterName().getData(), "mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(thirdNetworkNodeDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);

        // test 5 locationInfoWithLMSI with smsf-3gpp-Number, smsf-3gpp-DiameterAddress and smsf-3gpp-address-indicator
        rawData = getEncodedDataNnnSmsf();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     smsf-3gpp-Number: 91959819095050
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900505
         *     smsf-3gpp-DiameterAddress
         *         diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     smsf-3gpp-address-indicator
         */
        networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = locationInfoWithLMSI.getLMSI();
        assertNull(lmsi);
        extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber);
        networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertEquals(smsf3gppNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(smsf3gppNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsf3gppNumber.getAddress(), "598991900505");
        assertFalse(smsf3gppNumber.isExtension());
        smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertEquals(smsf3gppDiameterAddress.getDiameterName().getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(smsf3gppDiameterAddress.getDiameterRealm().getData(), "epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertTrue(smsf3gppAddressIndicator);
        smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertNull(smsfNon3gppNumber);
        smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertNull(smsfNon3gppDiameterAddress);
        smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertFalse(smsfNon3gppAddressIndicator);

        // test 6 locationInfoWithLMSI with smsf-non-3gpp-Number, smsf-non-3gpp-DiameterAddress and smsf-non-3gpp-address-indicator
        rawData = getEncodedDataNnnSmsfNon3gpp();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl();
        locationInfoWithLMSI.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 7202e7d5
         *     smsf-non-3gpp-Number: 91959819096000
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900600
         *     smsf-non-3gpp-DiameterAddress
         *         diameter-Name: smsf03.mnc002.mcc748
         *         diameter-Realm: mnc002.mcc748.telco.com
         *     smsf-non-3gpp-address-indicator
         */
        networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "598991900032");
        lmsi = locationInfoWithLMSI.getLMSI();
        assertEquals(lmsi.getData(), new byte[] { 0x72, 0x02, (byte) 0xe7, (byte) 0xd5 });
        extensionContainer = locationInfoWithLMSI.getExtensionContainer();
        assertNull(extensionContainer);
        additionalNumber = locationInfoWithLMSI.getAdditionalNumber();
        assertNull(additionalNumber);
        networkNodeDiameterAddress = locationInfoWithLMSI.getNetworkNodeDiameterAddress();
        assertNull(networkNodeDiameterAddress);
        additionalNetworkNodeDiameterAddress = locationInfoWithLMSI.getAdditionalNetworkNodeDiameterAddress();
        assertNull(additionalNetworkNodeDiameterAddress);
        thirdNetworkNodeDiameterAddress = locationInfoWithLMSI.getThirdNetworkNodeDiameterAddress();
        assertNull(thirdNetworkNodeDiameterAddress);
        imsNodeIndicator = locationInfoWithLMSI.getImsNodeIndicator();
        assertFalse(imsNodeIndicator);
        smsf3gppNumber = locationInfoWithLMSI.getSmsf3gppNumber();
        assertNull(smsf3gppNumber);
        smsf3gppDiameterAddress = locationInfoWithLMSI.getSmsf3gppDiameterAddress();
        assertNull(smsf3gppDiameterAddress);
        smsf3gppAddressIndicator = locationInfoWithLMSI.getSmsf3gppAddressIndicator();
        assertFalse(smsf3gppAddressIndicator);
        smsfNon3gppNumber = locationInfoWithLMSI.getSmsfNon3gppNumber();
        assertEquals(smsfNon3gppNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(smsfNon3gppNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsfNon3gppNumber.getAddress(), "598991900600");
        assertFalse(smsfNon3gppNumber.isExtension());
        smsfNon3gppDiameterAddress = locationInfoWithLMSI.getSmsfNon3gppDiameterAddress();
        assertEquals(smsfNon3gppDiameterAddress.getDiameterName().getData(), "smsf03.mnc002.mcc748".getBytes(StandardCharsets.UTF_8));
        assertEquals(smsfNon3gppDiameterAddress.getDiameterRealm().getData(), "mnc002.mcc748.telco.com".getBytes(StandardCharsets.UTF_8));
        smsfNon3gppAddressIndicator = locationInfoWithLMSI.getSmsfNon3gppAddressIndicator();
        assertTrue(smsfNon3gppAddressIndicator);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
        LocationInfoWithLMSIImpl locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, null, null,
                null, null, null, false, null, null, null, null, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData0();
        assertTrue(Arrays.equals(rawData, encodedData));

        //  test 2
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.national_significant_number, NumberingPlan.national, "1234567890");
        lmsi = new LMSIImpl(new byte[] { 4, 3, 2, 1 });
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.private_plan, "987654321");
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), true, additionalNumber, null,
                null, null, null, false, null, null, null,
                null, false, false);

        asnOS.reset();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3 containing networkNodeNumber, lmsi, additionalNumber and thirdNumber
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 71ffacce
         *     gprsNodeIndicator
         *     additional-Number: msc-Number (0)
         *         msc-Number: 91959819091003
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900130
         *     thirdNumber: sgsn-Number (1)
         *         sgsn-Number: 91959819091023
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900132
         */
        networkNodeNumber = new ISDNAddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce });
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "598991900130");
        additionalNumber = new AdditionalNumberImpl(mscNumber, null);
        sgsnNumber = new ISDNAddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "598991900132");
        AdditionalNumber thirdNumber = new AdditionalNumberImpl(null, sgsnNumber);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, true, additionalNumber, null,
                null, thirdNumber, null, false, null, null, null,
                null, false, false);

        asnOS.reset();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNnnLmsiAddNumThirdNum();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4 locationInfoWithLMSI with networkNodeDiameterAddress, additionalNetworkNodeDiameterAddress and thirdNetworkNodeDiameterAddress
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 7202e98c
         *     networkNodeDiameterAddress
         *         diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     additionalNetworkNodeDiameterAddress
         *         diameter-Name: mmec031.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     thirdNetworkNodeDiameterAddress
         *         diameter-Name: mmec032.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         */
        networkNodeNumber = new ISDNAddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x72, (byte) 0x02, (byte) 0xe9, (byte) 0x8c });
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
                additionalNetworkNodeDiameterAddress, null, thirdNetworkNodeDiameterAddress, false, null, null, null,
                null, false, false);

        asnOS.reset();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNnnLmsiNnDiamAddNodeDiamThDiam();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 MAP v18.0.0 locationInfoWithLMSI with smsf-3gpp-Number, smsf-3gpp-DiameterAddress and smsf-3gpp-address-indicator
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     smsf-3gpp-Number: 91959819095050
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900505
         *     smsf-3gpp-DiameterAddress
         *         diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *     smsf-3gpp-address-indicator
         */
        networkNodeNumber = new ISDNAddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        ISDNAddressString smsf3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900505");
        DiameterIdentity smsfName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity smsfRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsf3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfName, smsfRealm);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, null, null, false, null, null,
                null, null, null, false, smsf3gppNumber, smsf3gppDiameterAddress,
                null, null, true, false);

        asnOS.reset();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNnnSmsf();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6 locationInfoWithLMSI with smsf-non-3gpp-Number, smsf-non-3gpp-DiameterAddress and smsf-non-3gpp-address-indicator
        /*
         * locationInfoWithLMSI
         *     networkNode-Number: 91959819090023
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900032
         *     lmsi: 7202e7d5
         *     smsf-non-3gpp-Number: 91959819096000
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 598991900600
         *     smsf-non-3gpp-DiameterAddress
         *         diameter-Name: smsf03.mnc002.mcc748
         *         diameter-Realm: mnc002.mcc748.telco.com
         *     smsf-non-3gpp-address-indicator
         */
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900032");
        lmsi = new LMSIImpl(new byte[] { 0x72, 0x02, (byte) 0xe7, (byte) 0xd5 });
        ISDNAddressString smsfNon3gppNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900600");
        DiameterIdentity smsfNon3gppName = new DiameterIdentityImpl("smsf03.mnc002.mcc748".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity smsfNon3gppRealm = new DiameterIdentityImpl("mnc002.mcc748.telco.com".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsfNon3gppDiameterAddress = new NetworkNodeDiameterAddressImpl(smsfNon3gppName, smsfNon3gppRealm);
        locationInfoWithLMSI = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, false, null, null,
                null, null, null, false, null, null,
                smsfNon3gppNumber, smsfNon3gppDiameterAddress, false, true);

        asnOS.reset();
        locationInfoWithLMSI.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNnnSmsfNon3gpp();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.sms" })
    public void testSerialization() throws Exception {
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
        LocationInfoWithLMSIImpl original = new LocationInfoWithLMSIImpl(networkNodeNumber, lmsi, null, true, null, null,
                null, null, null, false, null, null, null, null, false, false);

        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        LocationInfoWithLMSIImpl copy = (LocationInfoWithLMSIImpl) o;

        // test result
        assertEquals(copy.getNetworkNodeNumber(), original.getNetworkNodeNumber());
        assertEquals(copy.getLMSI(), original.getLMSI());
    }
}
