package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ReportSMDeliveryStatusRequestTest {

    private byte[] getEncodedData_V1() {
        return new byte[] { 48, 16, 4, 6, -111, 39, 34, 51, 19, 17, 4, 6, -111, 1, -112, 115, 84, -13 };
    }

    private byte[] getEncodedData0() {
        return new byte[] { 48, 19, 4, 6, -111, 39, 34, 51, 19, 17, 4, 6, -111, 1, -112, 115, 84, -13, 10, 1, 1 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 73, 4, 6, -72, 17, 33, 34, 51, -13, 4, 6, -111, 51, 35, 34, 17, -15, 10, 1, 2, -128, 2, 1, -68,
                -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -124, 1, 0, -123, 2, 2, 43 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x62,
                0x04, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39,
                (byte) 0xf7, 0x04, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x10,
                0x32, 0x0a, 0x01, 0x01, (byte) 0x80, 0x01, 0x00, (byte) 0x89,
                0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08, 0x00,
                (byte) 0xf0, (byte) 0x8a, 0x00, (byte) 0xab, 0x3d, (byte) 0x80, 0x08, 0x09,
                0x41, 0x50, 0x01, 0x65, 0x08, 0x00, (byte) 0xf0, (byte) 0x81,
                0x18, 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37,
                0x37, 0x39, 0x33, 0x37, 0x40, 0x72, 0x65, 0x73,
                0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x82, 0x17, 0x6d, 0x74, 0x4c, 0x6f, 0x61,
                0x64, 0x54, 0x65, 0x73, 0x74, 0x40, 0x72, 0x65,
                0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f,
                0x72, 0x67
        };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] { 0x30, 0x33,
                0x04, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09,
                0x77, 0x39, (byte) 0xf7, 0x04, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, 0x0a, 0x01, 0x01, (byte) 0x80, 0x01,
                0x00, (byte) 0x82, 0x00, (byte) 0x83, 0x00, (byte) 0x84, 0x01, 0x01,
                (byte) 0x85, 0x01, 0x05, (byte) 0x86, 0x00, (byte) 0x87, 0x01, 0x01,
                (byte) 0x88, 0x01, 0x0c, (byte) 0x89, 0x08, 0x09, 0x41, 0x50,
                0x01, 0x65, 0x08, 0x00, (byte) 0xf0
        };
    }

    private byte[] getEncodedDataRel18_2() {
        return new byte[] { 0x30, 0x27,
                0x04, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39,
                (byte) 0xf7, 0x04, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x10,
                0x32, 0x0a, 0x01, 0x01, (byte) 0x80, 0x01, 0x00, (byte) 0x8c,
                0x00, (byte) 0x8d, 0x01, 0x01, (byte) 0x8e, 0x01, 0x03, (byte) 0x8f,
                0x00, (byte) 0x90, 0x01, 0x01, (byte) 0x91, 0x01, 0x04
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData0();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ReportSMDeliveryStatusRequestImpl ind = new ReportSMDeliveryStatusRequestImpl(2);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = ind.getMsisdn();
        AddressString serviceCentreAddress = ind.getServiceCentreAddress();
        SMDeliveryOutcome smDeliveryOutcome = ind.getSMDeliveryOutcome();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "7222333111");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "100937453");
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);

        // test 2
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusRequestImpl(3);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        serviceCentreAddress = ind.getServiceCentreAddress();
        smDeliveryOutcome = ind.getSMDeliveryOutcome();
        Integer absentSubscriberDiagnosticSM = ind.getAbsentSubscriberDiagnosticSM();
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        boolean gprsSupportIndicator = ind.getGprsSupportIndicator();
        boolean deliveryOutcomeIndicator = ind.getDeliveryOutcomeIndicator();
        SMDeliveryOutcome additionalSMDeliveryOutcome = ind.getAdditionalSMDeliveryOutcome();
        Integer additionalAbsentSubscriberDiagnosticSM = ind.getAdditionalAbsentSubscriberDiagnosticSM();
        assertEquals(msisdn.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.national);
        assertEquals(msisdn.getAddress(), "111222333");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "333222111");
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.successfulTransfer);
        assertEquals(absentSubscriberDiagnosticSM.intValue(), 444);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertTrue(gprsSupportIndicator);
        assertFalse(deliveryOutcomeIndicator);
        assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.memoryCapacityExceeded);
        assertEquals(additionalAbsentSubscriberDiagnosticSM.intValue(), 555);

        // test 3
        rawData = getEncodedData_V1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusRequestImpl(1);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        serviceCentreAddress = ind.getServiceCentreAddress();
        smDeliveryOutcome = ind.getSMDeliveryOutcome();
        absentSubscriberDiagnosticSM = ind.getAbsentSubscriberDiagnosticSM();
        extensionContainer = ind.getExtensionContainer();
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        deliveryOutcomeIndicator = ind.getDeliveryOutcomeIndicator();
        additionalSMDeliveryOutcome = ind.getAdditionalSMDeliveryOutcome();
        additionalAbsentSubscriberDiagnosticSM = ind.getAdditionalAbsentSubscriberDiagnosticSM();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "7222333111");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "100937453");
        assertNull(smDeliveryOutcome);
        assertNull(absentSubscriberDiagnosticSM);
        assertNull(extensionContainer);
        assertFalse(gprsSupportIndicator);
        assertFalse(deliveryOutcomeIndicator);
        assertNull(additionalSMDeliveryOutcome);
        assertNull(additionalAbsentSubscriberDiagnosticSM);

        // test 4, MAP v18.0.0 with imsi, singleAttemptDelivery and correlationID
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusRequestImpl(3);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         singleAttemptDelivery
         *         correlationID
         *             IMSI: 901405105680000
         *             [Association IMSI: 901405105680000]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        msisdn = ind.getMsisdn();
        serviceCentreAddress = ind.getServiceCentreAddress();
        smDeliveryOutcome = ind.getSMDeliveryOutcome();
        absentSubscriberDiagnosticSM = ind.getAbsentSubscriberDiagnosticSM();
        extensionContainer = ind.getExtensionContainer();
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        deliveryOutcomeIndicator = ind.getDeliveryOutcomeIndicator();
        additionalSMDeliveryOutcome = ind.getAdditionalSMDeliveryOutcome();
        additionalAbsentSubscriberDiagnosticSM = ind.getAdditionalAbsentSubscriberDiagnosticSM();
        boolean ipSmGwIndicator = ind.getIpSmGwIndicator();
        SMDeliveryOutcome ipSmGwSMDeliveryOutcome = ind.getIpSmGwSMDeliveryOutcome();
        Integer ipSmGwAbsentSubscriberDiagnosticSM = ind.getIpSmGwAbsentSubscriberDiagnosticSM();
        IMSI imsi = ind.getImsi();
        boolean singleAttemptDelivery = ind.getSingleAttemptDelivery();
        CorrelationID correlationID = ind.getCorrelationID();
        boolean smsf3gppDeliveryOutcomeIndicator = ind.getSmsf3gppDeliveryOutcomeIndicator();
        SMDeliveryOutcome smsf3gppDeliveryOutcome = ind.getSmsf3gppDeliveryOutcome();
        Integer smsf3gppAbsentSubscriberDiagnosticSM = ind.getSmsf3gppAbsentSubscriberDiagnosticSM();
        boolean smsfNon3gppDeliveryOutcomeIndicator = ind.getSmsfNon3gppDeliveryOutcomeIndicator();
        SMDeliveryOutcome smsfNon3gppDeliveryOutcome = ind.getSmsfNon3gppDeliveryOutcome();
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = ind.getSmsfNon3gppAbsentSubscriberDiagnosticSM();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(absentSubscriberDiagnosticSM.intValue(), 0);
        assertNull(extensionContainer);
        assertFalse(gprsSupportIndicator);
        assertFalse(deliveryOutcomeIndicator);
        assertNull(additionalSMDeliveryOutcome);
        assertNull(additionalAbsentSubscriberDiagnosticSM);
        assertFalse(ipSmGwIndicator);
        assertNull(ipSmGwSMDeliveryOutcome);
        assertNull(ipSmGwAbsentSubscriberDiagnosticSM);
        assertEquals(imsi.getData(), "901405105680000");
        assertTrue(singleAttemptDelivery);
        assertEquals(correlationID.getHlrId().getData(), "901405105680000");
        assertEquals(correlationID.getSipUriA().getData(), new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37,
                0x39, 0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(correlationID.getSipUriA(), new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(correlationID.getSipUriB().getData(), new byte[] { 0x6d, 0x74, 0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65,
                0x73, 0x74, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(correlationID.getSipUriB(), new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertFalse(smsf3gppDeliveryOutcomeIndicator);
        assertNull(smsf3gppDeliveryOutcome);
        assertNull(smsf3gppAbsentSubscriberDiagnosticSM);
        assertFalse(smsfNon3gppDeliveryOutcomeIndicator);
        assertNull(smsfNon3gppDeliveryOutcome);
        assertNull(smsfNon3gppAbsentSubscriberDiagnosticSM);

        // test 5, MAP v18.0.0 with ip-sm-gw-Indicator, ip-sm-gw-sm-deliveryOutcome and ip-sm-gw-absentSubscriberDiagnosticSM
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusRequestImpl(3);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         gprsSupportIndicator
         *         deliveryOutcomeIndicator
         *         additionalSM-DeliveryOutcome: absentSubscriber (1)
         *         additionalAbsentSubscriberDiagnosticSM: 5
         *         ip-sm-gw-Indicator
         *         ip-sm-gw-sm-deliveryOutcome: absentSubscriber (1)
         *         ip-sm-gw-absentSubscriberDiagnosticSM: 12
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         */
        msisdn = ind.getMsisdn();
        serviceCentreAddress = ind.getServiceCentreAddress();
        smDeliveryOutcome = ind.getSMDeliveryOutcome();
        absentSubscriberDiagnosticSM = ind.getAbsentSubscriberDiagnosticSM();
        extensionContainer = ind.getExtensionContainer();
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        deliveryOutcomeIndicator = ind.getDeliveryOutcomeIndicator();
        additionalSMDeliveryOutcome = ind.getAdditionalSMDeliveryOutcome();
        additionalAbsentSubscriberDiagnosticSM = ind.getAdditionalAbsentSubscriberDiagnosticSM();
        ipSmGwIndicator = ind.getIpSmGwIndicator();
        ipSmGwSMDeliveryOutcome = ind.getIpSmGwSMDeliveryOutcome();
        ipSmGwAbsentSubscriberDiagnosticSM = ind.getIpSmGwAbsentSubscriberDiagnosticSM();
        imsi = ind.getImsi();
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        correlationID = ind.getCorrelationID();
        smsf3gppDeliveryOutcomeIndicator = ind.getSmsf3gppDeliveryOutcomeIndicator();
        smsf3gppDeliveryOutcome = ind.getSmsf3gppDeliveryOutcome();
        smsf3gppAbsentSubscriberDiagnosticSM = ind.getSmsf3gppAbsentSubscriberDiagnosticSM();
        smsfNon3gppDeliveryOutcomeIndicator = ind.getSmsfNon3gppDeliveryOutcomeIndicator();
        smsfNon3gppDeliveryOutcome = ind.getSmsfNon3gppDeliveryOutcome();
        smsfNon3gppAbsentSubscriberDiagnosticSM = ind.getSmsfNon3gppAbsentSubscriberDiagnosticSM();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(absentSubscriberDiagnosticSM.intValue(), 0);
        assertNull(extensionContainer);
        assertTrue(gprsSupportIndicator);
        assertTrue(deliveryOutcomeIndicator);
        assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(additionalAbsentSubscriberDiagnosticSM.intValue(), 5);
        assertTrue(ipSmGwIndicator);
        assertEquals(ipSmGwSMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(ipSmGwAbsentSubscriberDiagnosticSM.intValue(), 12);
        assertEquals(imsi.getData(), "901405105680000");
        assertFalse(singleAttemptDelivery);
        assertNull(correlationID);
        assertFalse(smsf3gppDeliveryOutcomeIndicator);
        assertNull(smsf3gppDeliveryOutcome);
        assertNull(smsf3gppAbsentSubscriberDiagnosticSM);
        assertFalse(smsfNon3gppDeliveryOutcomeIndicator);
        assertNull(smsfNon3gppDeliveryOutcome);
        assertNull(smsfNon3gppAbsentSubscriberDiagnosticSM);

        // test 6, MAP v18.0.0 with smsf-3gpp-xxx and smsf-non-3gpp-xxx
        rawData = getEncodedDataRel18_2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusRequestImpl(3);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         smsf-3gpp-deliveryOutcomeIndicator
         *         smsf-3gpp-deliveryOutcome: absentSubscriber (1)
         *         smsf-3gpp-absentSubscriberDiagSM: 3
         *         smsf-non-3gpp-deliveryOutcomeIndicator
         *         smsf-non-3gpp-deliveryOutcome: absentSubscriber (1)
         *         smsf-non-3gpp-absentSubscriberDiagSM: 4
         */
        msisdn = ind.getMsisdn();
        serviceCentreAddress = ind.getServiceCentreAddress();
        smDeliveryOutcome = ind.getSMDeliveryOutcome();
        absentSubscriberDiagnosticSM = ind.getAbsentSubscriberDiagnosticSM();
        extensionContainer = ind.getExtensionContainer();
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        deliveryOutcomeIndicator = ind.getDeliveryOutcomeIndicator();
        additionalSMDeliveryOutcome = ind.getAdditionalSMDeliveryOutcome();
        additionalAbsentSubscriberDiagnosticSM = ind.getAdditionalAbsentSubscriberDiagnosticSM();
        ipSmGwIndicator = ind.getIpSmGwIndicator();
        ipSmGwSMDeliveryOutcome = ind.getIpSmGwSMDeliveryOutcome();
        ipSmGwAbsentSubscriberDiagnosticSM = ind.getIpSmGwAbsentSubscriberDiagnosticSM();
        imsi = ind.getImsi();
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        correlationID = ind.getCorrelationID();
        smsf3gppDeliveryOutcomeIndicator = ind.getSmsf3gppDeliveryOutcomeIndicator();
        smsf3gppDeliveryOutcome = ind.getSmsf3gppDeliveryOutcome();
        smsf3gppAbsentSubscriberDiagnosticSM = ind.getSmsf3gppAbsentSubscriberDiagnosticSM();
        smsfNon3gppDeliveryOutcomeIndicator = ind.getSmsfNon3gppDeliveryOutcomeIndicator();
        smsfNon3gppDeliveryOutcome = ind.getSmsfNon3gppDeliveryOutcome();
        smsfNon3gppAbsentSubscriberDiagnosticSM = ind.getSmsfNon3gppAbsentSubscriberDiagnosticSM();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertEquals(smDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(absentSubscriberDiagnosticSM.intValue(), 0);
        assertNull(extensionContainer);
        assertFalse(gprsSupportIndicator);
        assertFalse(deliveryOutcomeIndicator);
        assertNull(additionalSMDeliveryOutcome);
        assertNull(additionalAbsentSubscriberDiagnosticSM);
        assertFalse(ipSmGwIndicator);
        assertNull(ipSmGwSMDeliveryOutcome);
        assertNull(ipSmGwAbsentSubscriberDiagnosticSM);
        assertNull(imsi);
        assertFalse(singleAttemptDelivery);
        assertNull(correlationID);
        assertTrue(smsf3gppDeliveryOutcomeIndicator);
        assertEquals(smsf3gppDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(smsf3gppAbsentSubscriberDiagnosticSM.intValue(), 3);
        assertTrue(smsfNon3gppDeliveryOutcomeIndicator);
        assertEquals(smsfNon3gppDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
        assertEquals(smsfNon3gppAbsentSubscriberDiagnosticSM.intValue(), 4);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "7222333111");
        AddressString serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "100937453");
        SMDeliveryOutcome smDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        ReportSMDeliveryStatusRequestImpl ind = new ReportSMDeliveryStatusRequestImpl(2, msisdn, serviceCentreAddress,
                smDeliveryOutcome, null, null, false, false, null,
                null, false, null, null, null, false, null,
                false, null, null, false, null,
                null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        ind = new ReportSMDeliveryStatusRequestImpl(1, msisdn, serviceCentreAddress, null, null, null, false,
                false, null, null, false, null, null,
                null, false, null, false, null, null, false,
                null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3
        msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national, "111222333");
        serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333222111");
        smDeliveryOutcome = SMDeliveryOutcome.successfulTransfer;
        int absentSubscriberDiagnosticSM = 444;
        SMDeliveryOutcome additionalSMDeliveryOutcome = SMDeliveryOutcome.memoryCapacityExceeded;
        int additionalAbsentSubscriberDiagnosticSM = 555;
        ind = new ReportSMDeliveryStatusRequestImpl(3, msisdn, serviceCentreAddress, smDeliveryOutcome, absentSubscriberDiagnosticSM,
                MAPExtensionContainerTest.GetTestExtensionContainer(), true, false, additionalSMDeliveryOutcome,
                additionalAbsentSubscriberDiagnosticSM, false, null, null, null, false, null,
                false, null, null, false, null,
                null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4, MAP v18.0.0 with imsi, singleAttemptDelivery and correlationID
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         singleAttemptDelivery
         *         correlationID
         *             IMSI: 901405105680000
         *             [Association IMSI: 901405105680000]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        smDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        absentSubscriberDiagnosticSM = 0;
        IMSI imsi = new IMSIImpl("901405105680000");
        boolean singleAttemptDelivery = true;
        IMSI hlrId = new IMSIImpl("901405105680000");
        SipUri sipUriA = new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8));
        SipUri sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
        CorrelationID correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);

        ind = new ReportSMDeliveryStatusRequestImpl(3, msisdn, serviceCentreAddress, smDeliveryOutcome, absentSubscriberDiagnosticSM,
                null, false, false, null,
                null, false, null, null,
                imsi, singleAttemptDelivery, correlationID, false, null, null,
                false, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5, MAP v18.0.0 with ip-sm-gw-Indicator, ip-sm-gw-sm-deliveryOutcome and ip-sm-gw-absentSubscriberDiagnosticSM
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         gprsSupportIndicator
         *         deliveryOutcomeIndicator
         *         additionalSM-DeliveryOutcome: absentSubscriber (1)
         *         additionalAbsentSubscriberDiagnosticSM: 5
         *         ip-sm-gw-Indicator
         *         ip-sm-gw-sm-deliveryOutcome: absentSubscriber (1)
         *         ip-sm-gw-absentSubscriberDiagnosticSM: 12
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         */
        boolean gprsSupportIndicator = true;
        boolean deliveryOutcomeIndicator = true;
        additionalSMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        additionalAbsentSubscriberDiagnosticSM = 5;
        boolean ipSmGwIndicator = true;
        SMDeliveryOutcome ipSmGwSMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        Integer ipSmGwAbsentSubscriberDiagnosticSM = 12;
        ind = new ReportSMDeliveryStatusRequestImpl(3, msisdn, serviceCentreAddress, smDeliveryOutcome, absentSubscriberDiagnosticSM,
                null, gprsSupportIndicator, deliveryOutcomeIndicator, additionalSMDeliveryOutcome,
                additionalAbsentSubscriberDiagnosticSM, ipSmGwIndicator, ipSmGwSMDeliveryOutcome, ipSmGwAbsentSubscriberDiagnosticSM,
                imsi, false, null, false, null, null,
                false, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6, MAP v18.0.0 with smsf-3gpp-xxx and smsf-non-3gpp-xxx
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reportSM-DeliveryStatus (47)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-DeliveryOutcome: absentSubscriber (1)
         *         absentSubscriberDiagnosticSM: 0
         *         smsf-3gpp-deliveryOutcomeIndicator
         *         smsf-3gpp-deliveryOutcome: absentSubscriber (1)
         *         smsf-3gpp-absentSubscriberDiagSM: 3
         *         smsf-non-3gpp-deliveryOutcomeIndicator
         *         smsf-non-3gpp-deliveryOutcome: absentSubscriber (1)
         *         smsf-non-3gpp-absentSubscriberDiagSM: 4
         */
        boolean smsf3gppDeliveryOutcomeIndicator = true;
        SMDeliveryOutcome smsf3gppDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        Integer smsf3gppAbsentSubscriberDiagnosticSM = 3;
        boolean smsfNon3gppDeliveryOutcomeIndicator = true;
        SMDeliveryOutcome smsfNon3gppDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = 4;

        ind = new ReportSMDeliveryStatusRequestImpl(3, msisdn, serviceCentreAddress, smDeliveryOutcome, absentSubscriberDiagnosticSM,
                null, false, false, null,
                null, false, null, null,
                null, false, null, smsf3gppDeliveryOutcomeIndicator, smsf3gppDeliveryOutcome, smsf3gppAbsentSubscriberDiagnosticSM,
                smsfNon3gppDeliveryOutcomeIndicator, smsfNon3gppDeliveryOutcome, smsfNon3gppAbsentSubscriberDiagnosticSM);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
