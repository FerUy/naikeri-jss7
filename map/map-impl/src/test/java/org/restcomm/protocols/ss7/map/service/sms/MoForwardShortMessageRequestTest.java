package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ValidityPeriodImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MoForwardShortMessageRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 38, -124, 7, -111, 34, 51, 67, -103, 32, 50, -126, 8, -111, 50, 17, 50, 33, 67, 51, -12, 4, 17,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 71, -124, 8, -111, 50, 17, 50, 33, 67, 51, -12, -126, 7, -111, 34, 51, 67, -103, 32, 50, 4, 40,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
                4, 4, 4, 4, 4, 4, 8, 0, 1, 33, 50, 51, -108, 9, -14 };
    }

    private byte[] getEncodedDataNoDaOa() {
        return new byte[] { 48, 58, -123, 0, -123, 0, 4, 52, 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
    }

    private byte[] getEncodedDataFullNoCiDo() {
        return new byte[] { 48, 80, -128, 8, 2, 1, 17, 50, 84, 118, -104, -16, -124, 6, -74, 16, 50, 84, 118, -104, 4, 9, 11,
                22, 33, 44, 55, 66, 77, 88, 99, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 8, 66, -128, 24, 33, 50, 67, 84,
                -11 };
    }

    private byte[] getEncodedDataRel18() {
        return new byte[] { 0x30, 0x6f,
                (byte) 0x84, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x10, 0x32,
                (byte) 0x82, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x88, (byte) 0x83, 0x00,
                (byte) 0xf2, 0x04, 0x1a, 0x35, (byte) 0xbb, 0x0b, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x09, 0x77, 0x39, (byte) 0xf7, 0x00, 0x00, 0x03,
                0x0d, (byte) 0xd3, (byte) 0xe6, 0x14, (byte) 0xc4, 0x7e, (byte) 0x87, (byte) 0xc9,
                0x20, 0x7a, 0x79, 0x4e, 0x07, 0x04, 0x08, 0x21,
                0x34, 0x65, 0x78, 0x01, 0x21, 0x43, (byte) 0xf5, (byte) 0xa0,
                0x33, (byte) 0x80, 0x08, 0x47, 0x08, 0x32, 0x29, 0x14,
                0x48, 0x47, (byte) 0xf8, (byte) 0x81, 0x11, 0x73, 0x69, 0x70,
                0x3a, 0x6b, 0x62, 0x7a, 0x61, 0x40, 0x61, 0x63,
                0x6d, 0x65, 0x2e, 0x63, 0x6f, 0x6d, (byte) 0x82, 0x14,
                0x73, 0x69, 0x70, 0x3a, 0x66, 0x65, 0x72, 0x40,
                0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x01, 0x01
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedDataSimple();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SM_RP_DA sm_rp_da = ind.getSM_RP_DA();
        SM_RP_OA sm_rp_oa = ind.getSM_RP_OA();
        SmsSignalInfo sm_rp_ui = ind.getSM_RP_UI();
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        IMSI imsi = ind.getIMSI();
        CorrelationID correlationID = ind.getCorrelationID();
        SMDeliveryOutcome smDeliveryOutcome = ind.getSmDeliveryOutcome();
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddress(), "223334990223");
        assertEquals(sm_rp_oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_oa.getMsisdn().getAddress(), "2311231234334");
        assertTrue(Arrays.equals(sm_rp_ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 }));
        assertNull(extensionContainer);
        assertNull(imsi);
        assertNull(correlationID);
        assertNull(smDeliveryOutcome);

        // test 2
        rawData = getEncodedDataComplex();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_rp_da = ind.getSM_RP_DA();
        sm_rp_oa = ind.getSM_RP_OA();
        sm_rp_ui = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        imsi = ind.getIMSI();
        correlationID = ind.getCorrelationID();
        smDeliveryOutcome = ind.getSmDeliveryOutcome();
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddress(), "2311231234334");
        assertEquals(sm_rp_oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_oa.getMsisdn().getAddress(), "223334990223");
        assertTrue(Arrays.equals(sm_rp_ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }));
        assertNull(extensionContainer);
        assertEquals(imsi.getData(), "001012233349902");
        assertNull(correlationID);
        assertNull(smDeliveryOutcome);

        // test 3
        rawData = getEncodedDataNoDaOa();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_rp_da = ind.getSM_RP_DA();
        sm_rp_oa = ind.getSM_RP_OA();
        sm_rp_ui = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        imsi = ind.getIMSI();
        assertNull(sm_rp_da.getServiceCentreAddressDA());
        assertNull(sm_rp_da.getIMSI());
        assertNull(sm_rp_da.getLMSI());
        assertNull(sm_rp_oa.getMsisdn());
        assertNull(sm_rp_oa.getServiceCentreAddressOA());
        assertTrue(Arrays.equals(sm_rp_ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));
        assertNull(extensionContainer);
        assertNull(imsi);
        assertNull(correlationID);
        assertNull(smDeliveryOutcome);

        // test 4
        rawData = getEncodedDataFullNoCiDo();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_rp_da = ind.getSM_RP_DA();
        sm_rp_oa = ind.getSM_RP_OA();
        sm_rp_ui = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        imsi = ind.getIMSI();
        correlationID = ind.getCorrelationID();
        smDeliveryOutcome = ind.getSmDeliveryOutcome();
        assertEquals(sm_rp_da.getIMSI().getData(), "201011234567890");
        assertEquals(sm_rp_oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.network_specific_number);
        assertEquals(sm_rp_oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(sm_rp_oa.getServiceCentreAddressOA().getAddress(), "0123456789");
        assertTrue(Arrays.equals(sm_rp_ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }));
        assertEquals(imsi.getData(), "240881122334455");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertNull(correlationID);
        assertNull(smDeliveryOutcome);

        // test 5 (MAP rel. 18)
        rawData = getEncodedDataRel18();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        sm_rp_da = ind.getSM_RP_DA();
        sm_rp_oa = ind.getSM_RP_OA();
        sm_rp_ui = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        imsi = ind.getIMSI();
        correlationID = ind.getCorrelationID();
        smDeliveryOutcome = ind.getSmDeliveryOutcome();
        /* Wireshark sample taken from MAP load test
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mo-forwardSM (46)
         *         sm-RP-DA: serviceCentreAddressDA (4)
         *             serviceCentreAddressDA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-OA: msisdn (2)
         *             msisdn: 911326888300f2
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 31628838002
         *         sm-RP-UI: 35bb0b919598097739f70000030dd3e614c47e87c9207a794e07
         *         IMSI: 124356871012345
         *         [Association IMSI: 124356871012345]
         *         correlationID
         *             IMSI: 748023924184748
         *             [Association IMSI: 748023924184748]
         *             sip-uri-A: 7369703a6b627a614061636d652e636f6d
         *             sip-uri-B: 7369703a6665724072657374636f6d6d2e6f7267
         *         sm-DeliveryOutcome: absentSubscriber (1)
         * GSM SMS TPDU (GSM 03.40) SMS-SUBMIT
         *     0... .... = TP-RP: TP Reply Path parameter is not set in this SMS SUBMIT/DELIVER
         *     .0.. .... = TP-UDHI: The TP UD field contains only the short message
         *     ..1. .... = TP-SRR: A status report is requested
         *     ...1 0... = TP-VPF: TP-VP field present - relative format (2)
         *     .... .1.. = TP-RD: Instruct SC to reject duplicates
         *     .... ..01 = TP-MTI: SMS-SUBMIT (1)
         *     TP-MR: 187
         *     TP-Destination-Address - (59899077937)
         *         Length: 11 address digits
         *         1... .... = Extension: No extension
         *         .001 .... = Type of number: International (1)
         *         .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *         TP-DA Digits: 59899077937
         *         E.164 number (MSISDN): 59899077937
         *     TP-PID: 0
         *         00.. .... = Defines formatting for subsequent bits: 0x0
         *         ..0. .... = Telematic interworking: no telematic interworking, but SME-to-SME protocol
         *         ...0 0000 = The SM-AL protocol being used between the SME and the MS: 0
         *     TP-DCS: 0
         *         00.. .... = Coding Group Bits: General Data Coding indication (0)
         *         Special case, GSM 7 bit default alphabet
         *     TP-Validity-Period: 20 minutes
         *     TP-User-Data-Length: (13) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         SMS text: SMS load test
         */
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_da.getServiceCentreAddressDA().getAddress(), "5989900123");
        assertNull(sm_rp_da.getIMSI());
        assertNull(sm_rp_da.getLMSI());
        assertNull(sm_rp_oa.getServiceCentreAddressOA());
        assertEquals(sm_rp_oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(sm_rp_oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sm_rp_oa.getMsisdn().getAddress(), "31628838002");
        assertFalse(sm_rp_oa.getMsisdn().isExtension());
        assertEquals(sm_rp_ui.getData(), new byte[] { 0x35, (byte) 0xbb, 0x0b, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39, (byte) 0xf7,
                0x00, 0x00, 0x03, 0x0d, (byte) 0xd3, (byte) 0xe6, 0x14, (byte) 0xc4, 0x7e, (byte) 0x87, (byte) 0xc9, 0x20, 0x7a, 0x79, 0x4e, 0x07 });
        assertNotNull(sm_rp_ui.decodeTpdu(true));
        assertEquals(sm_rp_ui.decodeTpdu(true).getSmsTpduType(), SmsTpduType.SMS_SUBMIT);
        SmsSubmitTpduImpl smsTpdu = new SmsSubmitTpduImpl(sm_rp_ui.decodeTpdu(true).encodeData(), Charset.defaultCharset());
        assertEquals(smsTpdu.getSmsTpduType(), SmsTpduType.SMS_SUBMIT);
        assertTrue(smsTpdu.getRejectDuplicates());
        assertFalse(smsTpdu.getReplyPathExists());
        assertTrue(smsTpdu.getStatusReportRequest());
        assertEquals(smsTpdu.getMessageReference(), 187);
        assertEquals(smsTpdu.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(smsTpdu.getDestinationAddress().getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertEquals(smsTpdu.getDestinationAddress().getAddressValue(), "59899077937");
        assertEquals(smsTpdu.getProtocolIdentifier().getCode(), 0);
        assertEquals(smsTpdu.getDataCodingScheme().getCode(), 0);
        assertEquals(smsTpdu.getValidityPeriod().getRelativeFormatValue().intValue(), 3);
        assertEquals(smsTpdu.getUserData().getDataCodingScheme().getCode(), 0);
        assertEquals(smsTpdu.getUserData().getEncodedUserDataLength(), 13);
        assertNull(extensionContainer);
        assertEquals(imsi.getData(), "124356871012345");
        assertEquals(correlationID.getHlrId().getData(), "748023924184748");
        assertEquals(correlationID.getSipUriA().getData(), "sip:kbza@acme.com".getBytes(StandardCharsets.UTF_8));
        assertEquals(correlationID.getSipUriB().getData(), "sip:fer@restcomm.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(sca);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2311231234334");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 },
                null);
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2311231234334");
        sm_RP_DA = new SM_RP_DAImpl(sca);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }, null);
        IMSI imsi = new IMSIImpl("001012233349902");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3
        sm_RP_DA = new SM_RP_DAImpl();
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }, null);
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNoDaOa();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4
        IMSI imsi0 = new IMSIImpl("201011234567890");
        sm_RP_DA = new SM_RP_DAImpl(imsi0);
        msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.land_mobile, "0123456789");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        imsi = new IMSIImpl("240881122334455");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFullNoCiDo();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 (MAP rel. 18)
        /* Wireshark sample taken from MAP load test
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: mo-forwardSM (46)
         *         sm-RP-DA: serviceCentreAddressDA (4)
         *             serviceCentreAddressDA: 919598091032
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 5989900123
         *         sm-RP-OA: msisdn (2)
         *             msisdn: 911326888300f2
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 31628838002
         *         sm-RP-UI: 35bb0b919598097739f70000030dd3e614c47e87c9207a794e07
         *         IMSI: 124356871012345
         *         [Association IMSI: 124356871012345]
         *         correlationID
         *             IMSI: 748023924184748
         *             [Association IMSI: 748023924184748]
         *             sip-uri-A: 7369703a6b627a614061636d652e636f6d
         *             sip-uri-B: 7369703a6665724072657374636f6d6d2e6f7267
         *         sm-DeliveryOutcome: absentSubscriber (1)
         * GSM SMS TPDU (GSM 03.40) SMS-SUBMIT
         *     0... .... = TP-RP: TP Reply Path parameter is not set in this SMS SUBMIT/DELIVER
         *     .0.. .... = TP-UDHI: The TP UD field contains only the short message
         *     ..1. .... = TP-SRR: A status report is requested
         *     ...1 0... = TP-VPF: TP-VP field present - relative format (2)
         *     .... .1.. = TP-RD: Instruct SC to reject duplicates
         *     .... ..01 = TP-MTI: SMS-SUBMIT (1)
         *     TP-MR: 187
         *     TP-Destination-Address - (59899077937)
         *         Length: 11 address digits
         *         1... .... = Extension: No extension
         *         .001 .... = Type of number: International (1)
         *         .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *         TP-DA Digits: 59899077937
         *         E.164 number (MSISDN): 59899077937
         *     TP-PID: 0
         *         00.. .... = Defines formatting for subsequent bits: 0x0
         *         ..0. .... = Telematic interworking: no telematic interworking, but SME-to-SME protocol
         *         ...0 0000 = The SM-AL protocol being used between the SME and the MS: 0
         *     TP-DCS: 0
         *         00.. .... = Coding Group Bits: General Data Coding indication (0)
         *         Special case, GSM 7 bit default alphabet
         *     TP-Validity-Period: 20 minutes
         *     TP-User-Data-Length: (13) depends on Data-Coding-Scheme
         *     TP-User-Data
         *         SMS text: SMS load test
         */
        AddressString serviceCentreAddressDA = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        sm_RP_DA = new SM_RP_DAImpl(serviceCentreAddressDA);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        imsi = new IMSIImpl("124356871012345");
        boolean rejectDuplicates = true;
        boolean replyPathExists = false;
        boolean statusReportRequest = true;
        int messageReference = 187;
        AddressField destinationAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "59899077937");
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(0);
        ValidityPeriod validityPeriod = new ValidityPeriodImpl(3);
        DataCodingScheme dataCodingScheme = new DataCodingSchemeImpl(0);
        UserDataHeader userDataHeader = new UserDataHeaderImpl();
        Charset gsm8Charset = Charset.defaultCharset();
        UserData userData = new UserDataImpl("SMS load test", dataCodingScheme, userDataHeader, gsm8Charset);
        SmsTpduImpl smsTpdu = new SmsSubmitTpduImpl(rejectDuplicates, replyPathExists, statusReportRequest, messageReference, destinationAddress,
                protocolIdentifier, validityPeriod, userData);
        sm_RP_UI = new SmsSignalInfoImpl(smsTpdu, gsm8Charset);
        IMSI hlrId = new IMSIImpl("748023924184748");
        SipUri sipUriA = new SipUriImpl("sip:kbza@acme.com".getBytes(StandardCharsets.UTF_8));
        SipUri sipUriB = new SipUriImpl("sip:fer@restcomm.org".getBytes(StandardCharsets.UTF_8));
        CorrelationID correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
        SMDeliveryOutcome smDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi, correlationID, smDeliveryOutcome);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
