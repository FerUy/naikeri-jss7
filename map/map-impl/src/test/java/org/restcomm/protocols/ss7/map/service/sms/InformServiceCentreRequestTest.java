package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class InformServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[]{48, 4, 3, 2, 2, 64};
    }

    private byte[] getEncodedData1() {
        return new byte[]{48, 61, 4, 6, -111, 17, 33, 34, 51, -13, 3, 2, 2, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 2,
                2, 2, 43, -128, 2, 1, -68};
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x1b,
                0x04, 0x09, (byte) 0x91, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, (byte) 0xf0, 0x03, 0x02, 0x02, 0x50, 0x02,
                0x01, 0x00, (byte) 0x80, 0x01, 0x07, (byte) 0x81, 0x01, 0x0c,
                (byte) 0x82, 0x01, 0x02
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MWStatus mwStatus = isc.getMwStatus();
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertFalse(mwStatus.getMnrgSet());
        assertFalse(mwStatus.getMnr5gSet());
        assertFalse(mwStatus.getMnr5gn3gSet());

        // test 2
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MAPExtensionContainer extensionContainer = isc.getExtensionContainer();
        ISDNAddressString storedMSISDN = isc.getStoredMSISDN();
        mwStatus = isc.getMwStatus();
        int absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
        int additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();
        Integer smsf3gppAbsentSubscriberDiagnosticSM = isc.getSmsf3gppAbsentSubscriberDiagnosticSM();
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = isc.getSmsfNon3gppAbsentSubscriberDiagnosticSM();

        assertNotNull(storedMSISDN);
        assertEquals(storedMSISDN.getAddressNature(), AddressNature.international_number);
        assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(storedMSISDN.getAddress(), "111222333");
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertTrue(mwStatus.getMnrgSet());
        assertFalse(mwStatus.getMnr5gSet());
        assertFalse(mwStatus.getMnr5gn3gSet());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertEquals(absentSubscriberDiagnosticSM, 555);
        assertEquals(additionalAbsentSubscriberDiagnosticSM, 444);
        assertNull(smsf3gppAbsentSubscriberDiagnosticSM);
        assertNull(smsfNon3gppAbsentSubscriberDiagnosticSM);

        // test 3, MAP v18.0.0 with absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM,
        // smsf3gppAbsentSubscriberDiagnosticSM and smsfNon3gppAbsentSubscriberDiagnosticSM
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        extensionContainer = isc.getExtensionContainer();
        storedMSISDN = isc.getStoredMSISDN();
        mwStatus = isc.getMwStatus();
        absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
        additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();
        smsf3gppAbsentSubscriberDiagnosticSM = isc.getSmsf3gppAbsentSubscriberDiagnosticSM();
        smsfNon3gppAbsentSubscriberDiagnosticSM = isc.getSmsfNon3gppAbsentSubscriberDiagnosticSM();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: informServiceCentre (63)
         *         storedMSISDN: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         Padding: 2
         *         mw-Status: 50
         *             0... .... = sc-AddressNotIncluded: False
         *             .1.. .... = mnrf-Set: True
         *             ..0. .... = mcef-Set: False
         *             ...1 .... = mnrg-Set: True
         *             .... 0... = mnr5g-Set: False
         *             .... .0.. = mnr5gn3g-Set: False
         *         absentSubscriberDiagnosticSM: 0
         *         additionalAbsentSubscriberDiagnosticSM: 7
         *         smsf3gppAbsentSubscriberDiagnosticSM: 12
         *         smsfNon3gppAbsentSubscriberDiagnosticSM: 2
         */
        assertNotNull(storedMSISDN);
        assertEquals(AddressNature.international_number, storedMSISDN.getAddressNature());
        assertEquals(NumberingPlan.ISDN, storedMSISDN.getNumberingPlan());
        assertEquals("000000000000000", storedMSISDN.getAddress());
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertTrue(mwStatus.getMnrgSet());
        assertFalse(mwStatus.getMnr5gSet());
        assertFalse(mwStatus.getMnr5gn3gSet());
        assertNull(extensionContainer);
        assertEquals(absentSubscriberDiagnosticSM, 0);
        assertEquals(additionalAbsentSubscriberDiagnosticSM, 7);
        assertEquals(smsf3gppAbsentSubscriberDiagnosticSM.intValue(), 12);
        assertEquals(smsfNon3gppAbsentSubscriberDiagnosticSM.intValue(), 2);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        MWStatus mwStatus = new MWStatusImpl(false, true, false, false, false, false);
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl(null, mwStatus, null, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        isc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "111222333");
        mwStatus = new MWStatusImpl(false, true, false, true, false, false);
        int absentSubscriberDiagnosticSM = 555;
        int additionalAbsentSubscriberDiagnosticSM = 444;
        isc = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM, null,
                null);

        asnOS.reset();
        isc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3, MAP v18.0.0 with absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM,
        // smsf3gppAbsentSubscriberDiagnosticSM and smsfNon3gppAbsentSubscriberDiagnosticSM
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: informServiceCentre (63)
         *         storedMSISDN: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         Padding: 2
         *         mw-Status: 50
         *             0... .... = sc-AddressNotIncluded: False
         *             .1.. .... = mnrf-Set: True
         *             ..0. .... = mcef-Set: False
         *             ...1 .... = mnrg-Set: True
         *             .... 0... = mnr5g-Set: False
         *             .... .0.. = mnr5gn3g-Set: False
         *         absentSubscriberDiagnosticSM: 0
         *         additionalAbsentSubscriberDiagnosticSM: 7
         *         smsf3gppAbsentSubscriberDiagnosticSM: 12
         *         smsfNon3gppAbsentSubscriberDiagnosticSM: 2
         */
        storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "000000000000000");
        mwStatus = new MWStatusImpl(false, true, false, true, false, false);
        absentSubscriberDiagnosticSM = 0;
        additionalAbsentSubscriberDiagnosticSM = 7;
        Integer smsf3gppAbsentSubscriberDiagnosticSM = 12;
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = 2;
        isc = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, null,
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM, smsf3gppAbsentSubscriberDiagnosticSM,
                smsfNon3gppAbsentSubscriberDiagnosticSM);

        asnOS.reset();
        isc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
