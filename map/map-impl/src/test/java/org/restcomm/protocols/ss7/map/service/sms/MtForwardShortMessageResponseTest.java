package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverReportTpduImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MtForwardShortMessageResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 48, 4, 5, 11, 22, 33, 44, 55, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataFromLoadTest() {
        return new byte[] { 0x30, 0x16,
                0x04, 0x14, 0x00, (byte) 0xc8, 0x07, 0x00,
                0x00, 0x0f, 0x4d, 0x6a, (byte) 0xcb, 0x38, 0x6d, (byte) 0x82,
                (byte) 0xe4, (byte) 0xe5, 0x39, (byte) 0xfc, (byte) 0xed, (byte) 0x9e, (byte) 0x97, 0x01
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MtForwardShortMessageResponseImpl ind = new MtForwardShortMessageResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SmsSignalInfo sm_RP_UI = ind.getSM_RP_UI();
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 11, 22, 33, 44, 55 }));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        // test 2
        rawData = getEncodedDataFromLoadTest();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MtForwardShortMessageResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_RP_UI = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: mt-forwardSM (44)
         *             sm-RP-UI: 00c80700000f4d6acb386d82e4e539fced9e9701
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER REPORT
         *     .0.. .... = TP-UDHI: The TP UD field contains only the short message
         *     .... ..00 = TP-MTI: SMS-DELIVER REPORT (0)
         *     TP-Failure-Cause (TP-FCS): Reserved (0xc8)
         *     TP-Parameter-Indicator: 0x07, TP-UDL, TP-DCS, TP-PID
         *     TP-PID: 0
         *     TP-DCS: 0
         *     TP-User-Data-Length: (15) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         SMS text: MT-FSM response
         */
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 0x00, (byte) 0xc8, 0x07, 0x00, 0x00, 0x0f, 0x4d, 0x6a, (byte) 0xcb,
                0x38, 0x6d, (byte) 0x82, (byte) 0xe4, (byte) 0xe5, 0x39, (byte) 0xfc, (byte) 0xed, (byte) 0x9e, (byte) 0x97, 0x01 }));
        SmsDeliverReportTpdu smsDeliverReportTpdu = new SmsDeliverReportTpduImpl(sm_RP_UI.getData(), Charset.defaultCharset());
        assertEquals(smsDeliverReportTpdu.getSmsTpduType(), SmsTpduType.SMS_DELIVER_REPORT);
        assertFalse(smsDeliverReportTpdu.getUserDataHeaderIndicator()); // TP-UDHI: The TP UD field contains only the short message
        assertEquals(smsDeliverReportTpdu.getFailureCause().getCode(), 200);
        assertEquals(smsDeliverReportTpdu.getParameterIndicator().getCode(), 7); // TP-Parameter-Indicator: 0x07, TP-UDL, TP-DCS, TP-PID
        assertTrue(smsDeliverReportTpdu.getParameterIndicator().getTP_UDLPresence());
        assertTrue(smsDeliverReportTpdu.getParameterIndicator().getTP_DCSPresence());
        assertTrue(smsDeliverReportTpdu.getParameterIndicator().getTP_PIDPresence());
        assertEquals(smsDeliverReportTpdu.getProtocolIdentifier().getCode(), 0);
        assertEquals(smsDeliverReportTpdu.getDataCodingScheme().getCode(), 0);
        assertEquals(smsDeliverReportTpdu.getUserDataLength(), 15);
        assertNull(extensionContainer);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        MtForwardShortMessageResponseImpl ind = new MtForwardShortMessageResponseImpl(sm_RP_UI, extensionContainer);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: mt-forwardSM (44)
         *             sm-RP-UI: 00c80700000f4d6acb386d82e4e539fced9e9701
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER REPORT
         *     .0.. .... = TP-UDHI: The TP UD field contains only the short message
         *     .... ..00 = TP-MTI: SMS-DELIVER REPORT (0)
         *     TP-Failure-Cause (TP-FCS): Reserved (0xc8)
         *     TP-Parameter-Indicator: 0x07, TP-UDL, TP-DCS, TP-PID
         *     TP-PID: 0
         *     TP-DCS: 0
         *     TP-User-Data-Length: (15) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         SMS text: MT-FSM response
         */
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 0x00, (byte) 0xc8, 0x07, 0x00, 0x00, 0x0f, 0x4d, 0x6a, (byte) 0xcb,
                0x38, 0x6d, (byte) 0x82, (byte) 0xe4, (byte) 0xe5, 0x39, (byte) 0xfc, (byte) 0xed, (byte) 0x9e, (byte) 0x97, 0x01 },
                null);
        ind = new MtForwardShortMessageResponseImpl(sm_RP_UI, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFromLoadTest();
        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
