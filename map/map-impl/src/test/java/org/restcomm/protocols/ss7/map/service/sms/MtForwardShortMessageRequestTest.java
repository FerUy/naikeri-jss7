package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverTpdu;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;

import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MtForwardShortMessageRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 73, -128, 8, 16, 33, 34, 34, 17, -126, 21, -12, -124, 7, -111, -127, 33, 105, 0, -112, -10, 4,
                52, 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 70, -128, 8, 1, -128, 56, 67, 84, 101, 118, -9, -124, 6, -111, 17, 17, 33, 34, 34, 4, 7, 11,
                22, 33, 44, 55, 66, 77, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, (byte) 0x81,
                (byte) 0x9c, (byte) 0x80, 0x08, 0x47, 0x08, 0x13, 0x32, 0x54,
                0x76, (byte) 0x98, (byte) 0xf0, (byte) 0x84, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, 0x04, 0x2c, (byte) 0xc0, 0x06, (byte) 0xd0,
                0x34, (byte) 0xda, 0x0d, 0x00, 0x04, 0x42, 0x11, 0x42,
                0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04,
                0x3e, (byte) 0x94, 0x00, 0x00, 0x4c, 0x6f, 0x61, 0x64,
                0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x4d, 0x54,
                0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78,
                0x74, 0x05, 0x00, (byte) 0x83, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, (byte) 0xa4, 0x50, (byte) 0x80, 0x2b, 0x6d,
                0x73, 0x63, 0x30, 0x34, 0x2e, 0x6d, 0x6d, 0x65,
                0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67
        };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] { 0x30, (byte) 0x81,
                (byte) 0xec, (byte) 0x80, 0x08, 0x47, 0x08, 0x13, 0x32, 0x54,
                0x76, (byte) 0x98, (byte) 0xf0, (byte) 0x84, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, 0x04, 0x2c, (byte) 0xe8, 0x06, (byte) 0xd0,
                0x34, (byte) 0xda, 0x0d, 0x00, 0x04, 0x42, 0x11, 0x42,
                0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04,
                0x3e, (byte) 0x94, 0x00, 0x00, 0x4c, 0x6f, 0x61, 0x64,
                0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x4d, 0x54,
                0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78,
                0x74, 0x05, 0x00, 0x02, 0x01, 0x3c, 0x04, 0x04,
                (byte) 0xeb, 0x0e, 0x6e, (byte) 0xd9, (byte) 0x80, 0x00, (byte) 0xa1, 0x3d,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x13, 0x32, 0x54, 0x76,
                (byte) 0x98, (byte) 0xf0, (byte) 0x81, 0x18, 0x35, 0x39, 0x38, 0x39,
                0x39, 0x30, 0x37, 0x37, 0x39, 0x33, 0x37, 0x40,
                0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x82, 0x17, 0x6d, 0x74,
                0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65, 0x73, 0x74,
                0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d,
                0x6d, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x82, 0x04, (byte) 0xeb,
                0x1f, 0x03, (byte) 0xff, (byte) 0x83, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, (byte) 0xa4, 0x50, (byte) 0x80, 0x2b, 0x6d,
                0x73, 0x63, 0x30, 0x34, 0x2e, 0x6d, 0x6d, 0x65,
                0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SM_RP_DA sm_RP_DA = ind.getSM_RP_DA();
        SM_RP_OA sm_RP_OA = ind.getSM_RP_OA();
        SmsSignalInfo sm_RP_UI = ind.getSM_RP_UI();
        boolean moreMessagesToSend = ind.getMoreMessagesToSend();
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        Integer smDeliveryTimer = ind.getSmDeliveryTimer();
        Time smDeliveryStartTime = ind.getSmDeliveryStartTime();
        boolean smsOverIPOnlyIndicator = ind.getSmsOverIPOnlyIndicator();
        CorrelationID correlationID = ind.getCorrelationID();
        Time maximumRetransmissionTime = ind.getMaximumRetransmissionTime();
        ISDNAddressString smsGmscAddress = ind.getSmsGmscAddress();
        NetworkNodeDiameterAddress smsGmscDiameterAddress = ind.getSmsGmscDiameterAddress();
        assertEquals(sm_RP_DA.getIMSI().getData(), "011222221128514");
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "18129600096");
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));
        assertFalse(moreMessagesToSend);
        assertNull(extensionContainer);
        assertNull(smDeliveryTimer);
        assertNull(smDeliveryStartTime);
        assertFalse(smsOverIPOnlyIndicator);
        assertNull(correlationID);
        assertNull(maximumRetransmissionTime);
        assertNull(smsGmscAddress);
        assertNull(smsGmscDiameterAddress);

        // test 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_RP_DA = ind.getSM_RP_DA();
        sm_RP_OA = ind.getSM_RP_OA();
        sm_RP_UI = ind.getSM_RP_UI();
        moreMessagesToSend = ind.getMoreMessagesToSend();
        extensionContainer = ind.getExtensionContainer();
        smDeliveryTimer = ind.getSmDeliveryTimer();
        smDeliveryStartTime = ind.getSmDeliveryStartTime();
        smsOverIPOnlyIndicator = ind.getSmsOverIPOnlyIndicator();
        correlationID = ind.getCorrelationID();
        maximumRetransmissionTime = ind.getMaximumRetransmissionTime();
        smsGmscAddress = ind.getSmsGmscAddress();
        smsGmscDiameterAddress = ind.getSmsGmscDiameterAddress();
        assertEquals(sm_RP_DA.getIMSI().getData(), "100883344556677");
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "1111122222");
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77 }));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertTrue(moreMessagesToSend);
        assertNull(smDeliveryTimer);
        assertNull(smDeliveryStartTime);
        assertFalse(smsOverIPOnlyIndicator);
        assertNull(correlationID);
        assertNull(maximumRetransmissionTime);
        assertNull(smsGmscAddress);
        assertNull(smsGmscDiameterAddress);

        // test 3, MAP v18.0.0 with smsGmscAddress and smsGmscDiameterAddress
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_RP_DA = ind.getSM_RP_DA();
        sm_RP_OA = ind.getSM_RP_OA();
        sm_RP_UI = ind.getSM_RP_UI();
        moreMessagesToSend = ind.getMoreMessagesToSend();
        extensionContainer = ind.getExtensionContainer();
        smDeliveryTimer = ind.getSmDeliveryTimer();
        smDeliveryStartTime = ind.getSmDeliveryStartTime();
        smsOverIPOnlyIndicator = ind.getSmsOverIPOnlyIndicator();
        correlationID = ind.getCorrelationID();
        maximumRetransmissionTime = ind.getMaximumRetransmissionTime();
        smsGmscAddress = ind.getSmsGmscAddress();
        smsGmscDiameterAddress = ind.getSmsGmscDiameterAddress();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mt-forwardSM (44)
         *         sm-RP-DA: imsi (0)
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *         sm-RP-OA: serviceCentreAddressOA (4)
         *             serviceCentreAddressOA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-UI: c006d034da0d0004421142008295291c0605043e9400004c6f61642074657374204d542d534d532074657874
         *         moreMessagesToSend
         *         smsGmscAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         smsGmscDiameterAddress
         *             diameter-Name: msc04.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER
         *     1... .... = TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
         *     .1.. .... = TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
         *     ..0. .... = TP-SRI: A status report shall not be returned to the SME
         *     .... 0... = TP-LP: The message has not been forwarded and is not a spawned message
         *     .... .0.. = TP-MMS: More messages are waiting for the MS in this SC
         *     .... ..00 = TP-MTI: SMS-DELIVER (0)
         *     TP-Originating-Address - (447)
         *     TP-PID: 0
         *     TP-DCS: 4
         *     TP-Service-Centre-Time-Stamp
         *     TP-User-Data-Length: (28) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         User-Data Header
         *             User Data Header Length: 6
         *             IE: Application port addressing scheme, 16 bit address (SMS Control)
         *         SMS body: 4c6f61642074657374204d542d534d532074657874
         */
        assertEquals(sm_RP_DA.getIMSI().getData(), "748031234567890");
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "5989900123");
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { (byte) 0xc0, 0x06, (byte) 0xd0, 0x34, (byte) 0xda, 0x0d, 0x00, 0x04, 0x42, 0x11, 0x42,
                0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04, 0x3e, (byte) 0x94, 0x00, 0x00, 0x4c, 0x6f, 0x61, 0x64, 0x20, 0x74, 0x65,
                0x73, 0x74, 0x20, 0x4d, 0x54, 0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78, 0x74 }));
        SmsDeliverTpdu smsDeliverTpdu = new SmsDeliverTpduImpl(sm_RP_UI.decodeTpdu(false).encodeData(), Charset.defaultCharset());
        assertEquals(smsDeliverTpdu.getSmsTpduType(), SmsTpduType.SMS_DELIVER);
        assertTrue(smsDeliverTpdu.getReplyPathExists()); // TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
        assertTrue(smsDeliverTpdu.getUserDataHeaderIndicator());// TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
        assertFalse(smsDeliverTpdu.getStatusReportIndication()); // TP-SRI: A status report shall not be returned to the SME
        assertFalse(smsDeliverTpdu.getForwardedOrSpawned()); // TP-LP: The message has not been forwarded and is not a spawned message
        assertTrue(smsDeliverTpdu.getMoreMessagesToSend()); // TP-MMS: More messages are waiting for the MS in this SC
        assertEquals(smsDeliverTpdu.getOriginatingAddress().getAddressValue(), "447"); // TP-Originating-Address - (447)
        assertEquals(smsDeliverTpdu.getProtocolIdentifier().getCode(), 0); // TP-PID: 0
        assertEquals(smsDeliverTpdu.getDataCodingScheme().getCode(), 4); // TP-DCS: 4
        assertEquals(smsDeliverTpdu.getUserData().getEncodedUserDataLength(), 28); // TP-User-Data-Length: (28) depends on Data-Coding-Scheme
        assertNull(extensionContainer);
        assertTrue(moreMessagesToSend);
        assertNull(smDeliveryTimer);
        assertNull(smDeliveryStartTime);
        assertFalse(smsOverIPOnlyIndicator);
        assertNull(correlationID);
        assertNull(maximumRetransmissionTime);
        assertEquals(smsGmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(smsGmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsGmscAddress.getAddress(), "5989900123");
        assertEquals(smsGmscDiameterAddress.getDiameterName(), new DiameterIdentityImpl("msc04.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(smsGmscDiameterAddress.getDiameterRealm(), new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));

        // test 4, MAP v18.0.0 with smDeliveryTimer, smDeliveryStartTime, smsOverIP-OnlyIndicator, correlationID
        // smsGmscAddress and smsGmscDiameterAddress
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_RP_DA = ind.getSM_RP_DA();
        sm_RP_OA = ind.getSM_RP_OA();
        sm_RP_UI = ind.getSM_RP_UI();
        moreMessagesToSend = ind.getMoreMessagesToSend();
        extensionContainer = ind.getExtensionContainer();
        smDeliveryTimer = ind.getSmDeliveryTimer();
        smDeliveryStartTime = ind.getSmDeliveryStartTime();
        smsOverIPOnlyIndicator = ind.getSmsOverIPOnlyIndicator();
        correlationID = ind.getCorrelationID();
        maximumRetransmissionTime = ind.getMaximumRetransmissionTime();
        smsGmscAddress = ind.getSmsGmscAddress();
        smsGmscDiameterAddress = ind.getSmsGmscDiameterAddress();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mt-forwardSM (44)
         *         sm-RP-DA: imsi (0)
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *         sm-RP-OA: serviceCentreAddressOA (4)
         *             serviceCentreAddressOA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-UI: e806d034da0d0004421142008295291c0605043e9400004c6f61642074657374204d542d534d532074657874
         *         moreMessagesToSend
         *         smDeliveryTimer: 60
         *         smDeliveryStartTime: eb0e6ed9
         *         smsOverIP-OnlyIndicator
         *         correlationID
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         *         maximumRetransmissionTime: eb1f03ff
         *         smsGmscAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         smsGmscDiameterAddress
         *             diameter-Name: msc04.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER
         *     1... .... = TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
         *     .1.. .... = TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
         *     ..1. .... = TP-SRI: A status report shall be returned to the SME
         *     .... 1... = TP-LP: The message has either been forwarded or is a spawned message
         *     .... .0.. = TP-MMS: More messages are waiting for the MS in this SC
         *     .... ..00 = TP-MTI: SMS-DELIVER (0)
         *     TP-Originating-Address - (447)
         *     TP-PID: 0
         *     TP-DCS: 4
         *     TP-Service-Centre-Time-Stamp
         *     TP-User-Data-Length: (28) depends on Data-Coding-Scheme
         *     TP-User-Data
         *       User-Data Header
         *         User Data Header Length: 6
         *         IE: Application port addressing scheme, 16 bit address (SMS Control)
         *       SMS body: 4c6f61642074657374204d542d534d532074657874
         */
        assertEquals(sm_RP_DA.getIMSI().getData(), "748031234567890");
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "5989900123");
        assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { (byte) 0xe8, 0x06, (byte) 0xd0, 0x34, (byte) 0xda, 0x0d, 0x00, 0x04,
                0x42, 0x11, 0x42, 0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04, 0x3e, (byte) 0x94, 0x00, 0x00, 0x4c,
                0x6f, 0x61, 0x64, 0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x4d, 0x54, 0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78, 0x74}));
        smsDeliverTpdu = new SmsDeliverTpduImpl(sm_RP_UI.decodeTpdu(false).encodeData(), Charset.defaultCharset());
        assertEquals(smsDeliverTpdu.getSmsTpduType(), SmsTpduType.SMS_DELIVER);
        assertTrue(smsDeliverTpdu.getReplyPathExists()); // TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
        assertTrue(smsDeliverTpdu.getUserDataHeaderIndicator());// TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
        assertTrue(smsDeliverTpdu.getStatusReportIndication()); // TP-SRI: A status report shall not be returned to the SME
        assertTrue(smsDeliverTpdu.getForwardedOrSpawned()); // TP-LP: The message has not been forwarded and is not a spawned message
        assertTrue(smsDeliverTpdu.getMoreMessagesToSend()); // TP-MMS: More messages are waiting for the MS in this SC
        assertEquals(smsDeliverTpdu.getOriginatingAddress().getAddressValue(), "447"); // TP-Originating-Address - (447)
        assertEquals(smsDeliverTpdu.getProtocolIdentifier().getCode(), 0); // TP-PID: 0
        assertEquals(smsDeliverTpdu.getDataCodingScheme().getCode(), 4); // TP-DCS: 4
        assertEquals(smsDeliverTpdu.getUserData().getEncodedUserDataLength(), 28); // TP-User-Data-Length: (28) depends on Data-Coding-Scheme
        assertNull(extensionContainer);
        assertTrue(moreMessagesToSend);
        assertEquals(smDeliveryTimer.intValue(), 60);
        assertEquals(smDeliveryStartTime.getData(), new byte[] { (byte) 0xeb, 0x0e, 0x6e, (byte) 0xd9 });
        assertEquals(smDeliveryStartTime, new TimeImpl(2024, 12, 19, 10, 7, 21));
        assertEquals(smDeliveryStartTime.getYear(), 2024);
        assertEquals(smDeliveryStartTime.getMonth(), 12);
        assertEquals(smDeliveryStartTime.getDay(), 19);
        assertEquals(smDeliveryStartTime.getHour(), 10);
        assertEquals(smDeliveryStartTime.getMinute(), 7);
        assertEquals(smDeliveryStartTime.getSecond(), 21);
        assertTrue(smsOverIPOnlyIndicator);
        assertEquals(correlationID.getHlrId().getData(), "748031234567890");
        assertEquals(correlationID.getSipUriA().getData(), new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37,
                0x39, 0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(correlationID.getSipUriA(), new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(correlationID.getSipUriB().getData(), new byte[] { 0x6d, 0x74, 0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65,
                0x73, 0x74, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67});
        assertEquals(correlationID.getSipUriB(), new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(maximumRetransmissionTime.getData(), new byte[] { (byte) 0xeb, 0x1f, 0x03, (byte) 0xff });
        assertEquals(maximumRetransmissionTime, new TimeImpl(2024, 12, 31, 23, 59, 59));
        assertEquals(maximumRetransmissionTime.getYear(), 2024);
        assertEquals(maximumRetransmissionTime.getMonth(), 12);
        assertEquals(maximumRetransmissionTime.getDay(), 31);
        assertEquals(maximumRetransmissionTime.getHour(), 23);
        assertEquals(maximumRetransmissionTime.getMinute(), 59);
        assertEquals(maximumRetransmissionTime.getSecond(), 59);
        assertEquals(smsGmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(smsGmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsGmscAddress.getAddress(), "5989900123");
        assertEquals(smsGmscDiameterAddress.getDiameterName(), new DiameterIdentityImpl("msc04.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(smsGmscDiameterAddress.getDiameterRealm(), new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        IMSI imsi = new IMSIImpl("011222221128514");
        SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(imsi);
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18129600096");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8,
                1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11,
                11, 0 }, null);
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, false, null,
                null, null, false, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        imsi = new IMSIImpl("100883344556677");
        sm_RP_DA = new SM_RP_DAImpl(imsi);
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111122222");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77 }, null);
        ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer(), null, null,
                false, null, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3, MAP v18.0.0 with smsGmscAddress and smsGmscDiameterAddress
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mt-forwardSM (44)
         *         sm-RP-DA: imsi (0)
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *         sm-RP-OA: serviceCentreAddressOA (4)
         *             serviceCentreAddressOA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-UI: c006d034da0d0004421142008295291c0605043e9400004c6f61642074657374204d542d534d532074657874
         *         moreMessagesToSend
         *         smsGmscAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         smsGmscDiameterAddress
         *             diameter-Name: msc04.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER
         *     1... .... = TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
         *     .1.. .... = TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
         *     ..0. .... = TP-SRI: A status report shall not be returned to the SME
         *     .... 0... = TP-LP: The message has not been forwarded and is not a spawned message
         *     .... .0.. = TP-MMS: More messages are waiting for the MS in this SC
         *     .... ..00 = TP-MTI: SMS-DELIVER (0)
         *     TP-Originating-Address - (447)
         *     TP-PID: 0
         *     TP-DCS: 4
         *     TP-Service-Centre-Time-Stamp
         *     TP-User-Data-Length: (28) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         User-Data Header
         *             User Data Header Length: 6
         *             IE: Application port addressing scheme, 16 bit address (SMS Control)
         *         SMS body: 4c6f61642074657374204d542d534d532074657874
         */
        imsi = new IMSIImpl("748031234567890");
        sm_RP_DA = new SM_RP_DAImpl(imsi);
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { (byte) 0xc0, 0x06, (byte) 0xd0, 0x34, (byte) 0xda, 0x0d, 0x00, 0x04, 0x42, 0x11, 0x42,
                0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04, 0x3e, (byte) 0x94, 0x00, 0x00, 0x4c, 0x6f, 0x61, 0x64, 0x20, 0x74, 0x65,
                0x73, 0x74, 0x20, 0x4d, 0x54, 0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78, 0x74 }, null);
        ISDNAddressString smsGmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        DiameterIdentity gmscName = new DiameterIdentityImpl("msc04.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
        ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                null, null, null,
                false, null, null, smsGmscAddress, smsGmscDiameterAddress);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4, MAP v18.0.0 with smDeliveryTimer, smDeliveryStartTime, smsOverIP-OnlyIndicator, correlationID
        // smsGmscAddress and smsGmscDiameterAddress
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mt-forwardSM (44)
         *         sm-RP-DA: imsi (0)
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *         sm-RP-OA: serviceCentreAddressOA (4)
         *             serviceCentreAddressOA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-UI: e806d034da0d0004421142008295291c0605043e9400004c6f61642074657374204d542d534d532074657874
         *         moreMessagesToSend
         *         smDeliveryTimer: 60
         *         smDeliveryStartTime: eb0e6ed9
         *         smsOverIP-OnlyIndicator
         *         correlationID
         *             IMSI: 748031234567890
         *             [Association IMSI: 748031234567890]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         *         maximumRetransmissionTime: eb1f03ff
         *         smsGmscAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         smsGmscDiameterAddress
         *             diameter-Name: msc04.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *
         * GSM SMS TPDU (GSM 03.40) SMS-DELIVER
         *     1... .... = TP-RP: TP Reply Path parameter is set in this SMS SUBMIT/DELIVER
         *     .1.. .... = TP-UDHI: The beginning of the TP UD field contains a Header in addition to the short message
         *     ..1. .... = TP-SRI: A status report shall be returned to the SME
         *     .... 1... = TP-LP: The message has either been forwarded or is a spawned message
         *     .... .0.. = TP-MMS: More messages are waiting for the MS in this SC
         *     .... ..00 = TP-MTI: SMS-DELIVER (0)
         *     TP-Originating-Address - (447)
         *     TP-PID: 0
         *     TP-DCS: 4
         *     TP-Service-Centre-Time-Stamp
         *     TP-User-Data-Length: (28) depends on Data-Coding-Scheme
         *     TP-User-Data
         *       User-Data Header
         *         User Data Header Length: 6
         *         IE: Application port addressing scheme, 16 bit address (SMS Control)
         *       SMS body: 4c6f61642074657374204d542d534d532074657874
         */
        imsi = new IMSIImpl("748031234567890");
        sm_RP_DA = new SM_RP_DAImpl(imsi);
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        sm_RP_UI = new SmsSignalInfoImpl( new byte[] { (byte) 0xe8, 0x06, (byte) 0xd0, 0x34, (byte) 0xda, 0x0d, 0x00, 0x04,
                0x42, 0x11, 0x42, 0x00, (byte) 0x82, (byte) 0x95, 0x29, 0x1c, 0x06, 0x05, 0x04, 0x3e, (byte) 0x94, 0x00, 0x00, 0x4c,
                0x6f, 0x61, 0x64, 0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x4d, 0x54, 0x2d, 0x53, 0x4d, 0x53, 0x20, 0x74, 0x65, 0x78, 0x74},
                null);
        Integer smDeliveryTimer = 60;
        Time smDeliveryStartTime = new TimeImpl(2024, 12, 19, 10, 7, 21);
        SipUri sipUriA = new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8));
        SipUri sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
        CorrelationID correlationID = new CorrelationIDImpl(imsi, sipUriA, sipUriB);
        Time maximumRetransmissionTime = new TimeImpl(2024, 12, 31, 23, 59, 59);
        smsGmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        gmscName = new DiameterIdentityImpl("msc04.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
        ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true, null,
                smDeliveryTimer, smDeliveryStartTime, true, correlationID, maximumRetransmissionTime,
                smsGmscAddress, smsGmscDiameterAddress);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
