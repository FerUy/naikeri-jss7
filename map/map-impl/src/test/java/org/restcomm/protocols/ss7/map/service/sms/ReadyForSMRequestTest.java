
package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
*/
public class ReadyForSMRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 12, (byte) 128, 7, 17, 17, 33, 34, 34, 51, (byte) 243, 10, 1, 1 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 57, (byte) 128, 7, 17, 17, 33, 34, 34, 51, (byte) 243, 10, 1, 1, 5, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 129, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 0x30, 0x0f,
                (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                0x00, (byte) 0xf0, 0x0a, 0x01, 0x00, 0x05, 0x00
        };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x13,
                (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                0x00, (byte) 0xf0, 0x0a, 0x01, 0x00, 0x04, 0x04, (byte) 0xb9,
                0x08, (byte) 0x8f, 0x4c
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ReadyForSMRequestImpl rsmRequest = new ReadyForSMRequestImpl();
        rsmRequest.decodeAll(asn);

        IMSI imsi = rsmRequest.getImsi();
        AlertReason alertReason = rsmRequest.getAlertReason();
        boolean alertReasonIndicator = rsmRequest.getAlertReasonIndicator();
        MAPExtensionContainer extensionContainer = rsmRequest.getExtensionContainer();
        boolean additionalAlertReasonIndicator = rsmRequest.getAdditionalAlertReasonIndicator();
        Time maximumUeAvailabilityTime = rsmRequest.getMaximumUeAvailabilityTime();
        assertEquals(imsi.getData(), "1111122222333");
        assertEquals(alertReason, AlertReason.memoryAvailable);
        assertFalse(alertReasonIndicator);
        assertNull(extensionContainer);
        assertFalse(additionalAlertReasonIndicator);
        assertNull(maximumUeAvailabilityTime);

        // test 2
        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        rsmRequest = new ReadyForSMRequestImpl();
        rsmRequest.decodeAll(asn);
        imsi = rsmRequest.getImsi();
        alertReason = rsmRequest.getAlertReason();
        alertReasonIndicator = rsmRequest.getAlertReasonIndicator();
        extensionContainer = rsmRequest.getExtensionContainer();
        additionalAlertReasonIndicator = rsmRequest.getAdditionalAlertReasonIndicator();
        maximumUeAvailabilityTime = rsmRequest.getMaximumUeAvailabilityTime();
        assertEquals(imsi.getData(), "1111122222333");
        assertEquals(alertReason, AlertReason.memoryAvailable);
        assertTrue(alertReasonIndicator);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertTrue(additionalAlertReasonIndicator);
        assertNull(maximumUeAvailabilityTime);

        // test 3 from MAP SMS load test
        rawData = getEncodedData3();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        rsmRequest = new ReadyForSMRequestImpl();
        rsmRequest.decodeAll(asn);

        imsi = rsmRequest.getImsi();
        alertReason = rsmRequest.getAlertReason();
        alertReasonIndicator = rsmRequest.getAlertReasonIndicator();
        extensionContainer = rsmRequest.getExtensionContainer();
        additionalAlertReasonIndicator = rsmRequest.getAdditionalAlertReasonIndicator();
        maximumUeAvailabilityTime = rsmRequest.getMaximumUeAvailabilityTime();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: readyForSM (66)
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         alertReason: ms-Present (0)
         *         alertReasonIndicator
         */
        assertEquals(imsi.getData(), "901405105680000");
        assertEquals(alertReason, AlertReason.msPresent);
        assertTrue(alertReasonIndicator);
        assertNull(extensionContainer);
        assertFalse(additionalAlertReasonIndicator);
        assertNull(maximumUeAvailabilityTime);

        // test 4, MAP v18.0.0 with maximumUeAvailabilityTime
        rawData = getEncodedDataRel18_0();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        rsmRequest = new ReadyForSMRequestImpl();
        rsmRequest.decodeAll(asn);

        imsi = rsmRequest.getImsi();
        alertReason = rsmRequest.getAlertReason();
        alertReasonIndicator = rsmRequest.getAlertReasonIndicator();
        extensionContainer = rsmRequest.getExtensionContainer();
        additionalAlertReasonIndicator = rsmRequest.getAdditionalAlertReasonIndicator();
        maximumUeAvailabilityTime = rsmRequest.getMaximumUeAvailabilityTime();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: readyForSM (66)
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         alertReason: ms-Present (0)
         *         maximumUeAvailabilityTime: b9088f4c
         */
        assertEquals(imsi.getData(), "901405105680000");
        assertEquals(alertReason, AlertReason.msPresent);
        assertFalse(alertReasonIndicator);
        assertNull(extensionContainer);
        assertFalse(additionalAlertReasonIndicator);
        assertEquals(maximumUeAvailabilityTime.getData(), new byte[] { (byte) 0xb9, 0x08, (byte) 0x8f, 0x4c });
        assertEquals(maximumUeAvailabilityTime.getYear(), 1998);
        assertEquals(maximumUeAvailabilityTime.getMonth(), 5);
        assertEquals(maximumUeAvailabilityTime.getDay(), 16);
        assertEquals(maximumUeAvailabilityTime.getHour(), 22);
        assertEquals(maximumUeAvailabilityTime.getMinute(), 18);
        assertEquals(maximumUeAvailabilityTime.getSecond(), 52);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        IMSI imsi = new IMSIImpl("1111122222333");
        ReadyForSMRequestImpl rsmRequest = new ReadyForSMRequestImpl(imsi, AlertReason.memoryAvailable, false, null, false, null);

        AsnOutputStream asnOS = new AsnOutputStream();

        rsmRequest.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        rsmRequest = new ReadyForSMRequestImpl(imsi, AlertReason.memoryAvailable, true, MAPExtensionContainerTest.GetTestExtensionContainer(), true, null);

        asnOS = new AsnOutputStream();

        rsmRequest.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3 from MAP SMS load test
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: readyForSM (66)
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         alertReason: ms-Present (0)
         *         alertReasonIndicator
         */
        imsi = new IMSIImpl("901405105680000");
        rsmRequest = new ReadyForSMRequestImpl(imsi, AlertReason.msPresent, true, null, false, null);

        asnOS = new AsnOutputStream();

        rsmRequest.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4, MAP v18.0.0 with maximumUeAvailabilityTime
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: readyForSM (66)
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         alertReason: ms-Present (0)
         *         maximumUeAvailabilityTime: b9088f4c
         */
        // Time [year=1998, month=5, day=16, hour=22, minute=18, second=52]
        Time maximumUeAvailabilityTime = new TimeImpl(1998, 5, 16, 22, 18, 52);
        rsmRequest = new ReadyForSMRequestImpl(imsi, AlertReason.msPresent, false, null, false, maximumUeAvailabilityTime);

        asnOS = new AsnOutputStream();

        rsmRequest.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
