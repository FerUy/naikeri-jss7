package org.restcomm.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.testng.annotations.Test;

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
public class SendRoutingInfoForSMRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 20, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 30, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51,
                -121, 0, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedData0() {
        return new byte[] { 48, 70, -128, 6, -111, 17, 33, 34, 51, -13, -127, 1, -1, -126, 3, -72, 68, 68, -90, 39, -96, 32,
                48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -121, 0, -120, 1, 1, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 20, -128, 5, -111, 17, 17, 17, 17, -127, 1, 0, -126, 5, -111, 34, 34, 34, 34, -123, 1, 33 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48,25,-128,5,-111,17,17,17,17,-127,1,-1,-126,5,-111,34,34,34,34,-121,0,-117,0,-114,0,-115,0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48,20,-128,5,-111,17,17,17,17,-127,1,0,-126,5,-111,34,34,34,34,-118,1,0 };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] { 0x30, 0x29,
                (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39,
                (byte) 0xf7, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82, 0x06, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x09, 0x10, 0x32, (byte) 0x87, 0x00, (byte) 0x88, 0x01,
                0x00, (byte) 0x89, 0x08, 0x0c, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x64, 0x00, 0x02, (byte) 0x8e, 0x00, (byte) 0x8d, 0x00, (byte) 0x90,
                0x00
        };
    }

    private byte[] getEncodedDataRel18_2() {
        return new byte[] { 0x30, 0x39,
                (byte) 0x80, 0x09, (byte) 0x91, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, (byte) 0xf0, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82, 0x04,
                (byte) 0xbf, 0x77, 0x77, (byte) 0xf7, (byte) 0x87, 0x00, (byte) 0x88, 0x01,
                0x00, (byte) 0x8b, 0x00, (byte) 0xaf, 0x1a, (byte) 0x82, 0x18, 0x35,
                0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63,
                0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x90,
                0x00
        };
    }

    private byte[] getEncodedDataRel18_3() {
        return new byte[] { 0x30, 0x17,
                (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39,
                (byte) 0xf7, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82, 0x06, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x09, 0x10, 0x32, (byte) 0x8a, 0x01, 0x01
        };
    }

    private byte[] getEncodedDataRel18_4() {
        return new byte[] { 0x30, 0x35,
                (byte) 0x80, 0x09, (byte) 0x91, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, (byte) 0xf0, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82, 0x06,
                (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x10, 0x32, (byte) 0x87, 0x00,
                (byte) 0x88, 0x01, 0x00, (byte) 0x89, 0x08, 0x0c, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, 0x02, (byte) 0x8c, 0x08, 0x09,
                0x41, 0x50, 0x01, 0x65, 0x08, 0x00, (byte) 0xf0, (byte) 0x8e,
                0x00, (byte) 0x8d, 0x00, (byte) 0x90, 0x00
        };
    }

    private byte[] getEncodedDataRel18_5() {
        return new byte[] { 0x30, 0x33,
                (byte) 0x80, 0x09, (byte) 0x91, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, (byte) 0xf0, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82, 0x02,
                0x79, (byte) 0xf5, (byte) 0x87, 0x00, (byte) 0x88, 0x01, 0x00, (byte) 0xaf,
                0x1a, (byte) 0x82, 0x18, 0x35, 0x39, 0x38, 0x39, 0x39,
                0x30, 0x37, 0x37, 0x39, 0x33, 0x37, 0x40, 0x72,
                0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e,
                0x6f, 0x72, 0x67
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        // test 1 (getEncodedDataSimple)
        byte[] rawData = getEncodedDataSimple();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertFalse(ind.getSm_RP_PRI());
        AddressString sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertNull(ind.getTeleservice());

        // test 2 (getEncodedDataComplex)
        rawData = getEncodedDataComplex();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertFalse(ind.getSm_RP_PRI());
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertTrue(ind.getGprsSupportIndicator());
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, ind.getSM_RP_SMEA().getData()));
        assertNull(ind.getTeleservice());

        // test 3 (getEncodedData0)
        rawData = getEncodedData0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "111222333");
        assertTrue(ind.getSm_RP_PRI());
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
        assertEquals(sca.getAddress(), "4444");
        assertTrue(ind.getGprsSupportIndicator());
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, ind.getSM_RP_SMEA().getData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertEquals(ind.getSM_RP_MTI(), SM_RP_MTI.SMS_Status_Report);
        assertNull(ind.getTeleservice());

        // test 4 (getEncodedData1)
        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "11111111");
        assertFalse(ind.getSm_RP_PRI());
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "22222222");
        assertFalse(ind.getGprsSupportIndicator());
        assertNull(ind.getSM_RP_SMEA());
        assertNull(ind.getExtensionContainer());
        assertNull(ind.getSM_RP_MTI());
        assertEquals(ind.getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);

        // test 5 (getEncodedData2)
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(ind.getSm_RP_PRI());
        assertTrue(ind.getGprsSupportIndicator());

        assertTrue(ind.getIpSmGwGuidanceIndicator());
        assertTrue(ind.getT4TriggerIndicator());
        assertTrue(ind.getSingleAttemptDelivery());

        // test 6 (getEncodedData3)
        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertFalse(ind.getSm_RP_PRI());
        assertFalse(ind.getGprsSupportIndicator());
        assertFalse(ind.getIpSmGwGuidanceIndicator());
        assertFalse(ind.getT4TriggerIndicator());
        assertFalse(ind.getSingleAttemptDelivery());
        assertEquals(SMDeliveryNotIntended.onlyIMSIRequested, ind.getSmDeliveryNotIntended());

        // test 7 (getEncodedDataRel18_1)
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         SM-RP-SMEA - (491710460020)
         *             Length: 12 address digits
         *             1... .... = Extension: No extension
         *             .001 .... = Type of number: International (1)
         *             .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *             Digits: 491710460020
         *         t4-Trigger-Indicator
         *         singleAttemptDelivery
         *         smsf-supportIndicator
         */
        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        boolean sm_RP_PRI = ind.getSm_RP_PRI();
        assertTrue(sm_RP_PRI);
        AddressString serviceCentreAddress = ind.getServiceCentreAddress();
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertFalse(serviceCentreAddress.isExtension());
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        boolean gprsSupportIndicator = ind.getGprsSupportIndicator();
        assertTrue(gprsSupportIndicator);
        SM_RP_MTI sm_RP_MTI = ind.getSM_RP_MTI();
        assertEquals(sm_RP_MTI.getCode(), 0);
        SM_RP_SMEA sm_RP_SMEA = ind.getSM_RP_SMEA();
        assertEquals(sm_RP_SMEA.getAddressField().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(sm_RP_SMEA.getAddressField().getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertEquals(sm_RP_SMEA.getAddressField().getAddressValue(), "491710460020");
        SMDeliveryNotIntended smDeliveryNotIntended = ind.getSmDeliveryNotIntended();
        assertNull(smDeliveryNotIntended);
        boolean ipSmGwGuidanceIndicator = ind.getIpSmGwGuidanceIndicator();
        assertFalse(ipSmGwGuidanceIndicator);
        IMSI imsi = ind.getImsi();
        assertNull(imsi);
        boolean t4TriggerIndicator = ind.getT4TriggerIndicator();
        assertTrue(t4TriggerIndicator);
        boolean singleAttemptDelivery = ind.getSingleAttemptDelivery();
        assertTrue(singleAttemptDelivery);
        boolean smsfSupportIndicator = ind.getSmsfSupportIndicator();
        assertTrue(smsfSupportIndicator);

        // test 8 (SRISM with correlationID => dummy MSISDN, ip-sm-gwGuidanceIndicator, smsf-supportIndicator)
        rawData = getEncodedDataRel18_2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: bf7777f7
         *             1... .... = Extension: No Extension
         *             .011 .... = Nature of number: Network Specific Number (0x3)
         *             .... 1111 = Number plan: Reserved for extension (0xf)
         *             Address digits: 77777
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         ip-sm-gwGuidanceIndicator
         *         correlationID
         *             sip-uri-B: 35393839393037373933374072657374636f6d6d2e6f7267
         *         smsf-supportIndicator
         */
        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "000000000000000");
        sm_RP_PRI = ind.getSm_RP_PRI();
        assertTrue(sm_RP_PRI);
        serviceCentreAddress = ind.getServiceCentreAddress();
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(serviceCentreAddress.getAddress(), "77777");
        assertFalse(serviceCentreAddress.isExtension());
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        assertTrue(gprsSupportIndicator);
        sm_RP_MTI = ind.getSM_RP_MTI();
        assertEquals(sm_RP_MTI.getCode(), 0);
        sm_RP_SMEA = ind.getSM_RP_SMEA();
        assertNull(sm_RP_SMEA);
        smDeliveryNotIntended = ind.getSmDeliveryNotIntended();
        assertNull(smDeliveryNotIntended);
        ipSmGwGuidanceIndicator = ind.getIpSmGwGuidanceIndicator();
        assertTrue(ipSmGwGuidanceIndicator);
        imsi = ind.getImsi();
        assertNull(imsi);
        t4TriggerIndicator = ind.getT4TriggerIndicator();
        assertFalse(t4TriggerIndicator);
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        assertFalse(singleAttemptDelivery);
        CorrelationID correlationID = ind.getCorrelationID();
        assertNull(correlationID.getHlrId());
        assertNull(correlationID.getSipUriA());
        assertEquals(correlationID.getSipUriB().getData(), new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        smsfSupportIndicator = ind.getSmsfSupportIndicator();
        assertTrue(smsfSupportIndicator);

        // test 9 (with sm-deliveryNotIntended)
        rawData = getEncodedDataRel18_3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-deliveryNotIntended: onlyMCC-MNC-requested (1)
         */
        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        sm_RP_PRI = ind.getSm_RP_PRI();
        assertTrue(sm_RP_PRI);
        serviceCentreAddress = ind.getServiceCentreAddress();
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertFalse(serviceCentreAddress.isExtension());
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        assertFalse(gprsSupportIndicator);
        sm_RP_MTI = ind.getSM_RP_MTI();
        assertNull(sm_RP_MTI);
        sm_RP_SMEA = ind.getSM_RP_SMEA();
        assertNull(sm_RP_SMEA);
        smDeliveryNotIntended = ind.getSmDeliveryNotIntended();
        assertEquals(smDeliveryNotIntended, SMDeliveryNotIntended.onlyMCCMNCRequested);
        ipSmGwGuidanceIndicator = ind.getIpSmGwGuidanceIndicator();
        assertFalse(ipSmGwGuidanceIndicator);
        imsi = ind.getImsi();
        assertNull(imsi);
        t4TriggerIndicator = ind.getT4TriggerIndicator();
        assertFalse(t4TriggerIndicator);
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        assertFalse(singleAttemptDelivery);
        correlationID = ind.getCorrelationID();
        assertNull(correlationID);
        smsfSupportIndicator = ind.getSmsfSupportIndicator();
        assertFalse(smsfSupportIndicator);

        // test 10 (with t4-Trigger-Indicator => dummy MSISDN, SM-RP-SMEA)
        rawData = getEncodedDataRel18_4();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         SM-RP-SMEA - (491710460020)
         *             Length: 12 address digits
         *             1... .... = Extension: No extension
         *             .001 .... = Type of number: International (1)
         *             .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *             Digits: 491710460020
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *             Mobile Country Code (MCC): International Mobile, shared code (901)
         *             Mobile Network Code (MNC): Deutsche Telekom AG (40)
         *         t4-Trigger-Indicator
         *         singleAttemptDelivery
         *         smsf-supportIndicator
         */
        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "000000000000000");
        sm_RP_PRI = ind.getSm_RP_PRI();
        assertTrue(sm_RP_PRI);
        serviceCentreAddress = ind.getServiceCentreAddress();
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertFalse(serviceCentreAddress.isExtension());
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        assertTrue(gprsSupportIndicator);
        sm_RP_MTI = ind.getSM_RP_MTI();
        assertEquals(sm_RP_MTI.getCode(), 0);
        sm_RP_SMEA = ind.getSM_RP_SMEA();
        assertEquals(sm_RP_SMEA.getAddressField().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(sm_RP_SMEA.getAddressField().getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertEquals(sm_RP_SMEA.getAddressField().getAddressValue(), "491710460020");
        smDeliveryNotIntended = ind.getSmDeliveryNotIntended();
        assertNull(smDeliveryNotIntended);
        ipSmGwGuidanceIndicator = ind.getIpSmGwGuidanceIndicator();
        assertFalse(ipSmGwGuidanceIndicator);
        imsi = ind.getImsi();
        assertEquals(imsi.getData(), "901405105680000");
        t4TriggerIndicator = ind.getT4TriggerIndicator();
        assertTrue(t4TriggerIndicator);
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        assertTrue(singleAttemptDelivery);
        correlationID = ind.getCorrelationID();
        assertNull(correlationID);
        smsfSupportIndicator = ind.getSmsfSupportIndicator();
        assertTrue(smsfSupportIndicator);

        // test 11 (SRISM with correlationID => dummy MSISDN, SC address is extension)
        rawData = getEncodedDataRel18_5();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 79f5
         *             0... .... = Extension: Extension
         *             .111 .... = Nature of number: Reserved for extension (0x7)
         *             .... 1001 = Number plan: Private Numbering (0x9)
         *             Address digits: 5
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         correlationID
         *             sip-uri-B: 35393839393037373933374072657374636f6d6d2e6f7267
         */
        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "000000000000000");
        sm_RP_PRI = ind.getSm_RP_PRI();
        assertTrue(sm_RP_PRI);
        serviceCentreAddress = ind.getServiceCentreAddress();
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.reserved_for_extension);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.private_plan);
        assertEquals(serviceCentreAddress.getAddress(), "5");
        assertTrue(serviceCentreAddress.isExtension());
        extensionContainer = ind.getExtensionContainer();
        assertNull(extensionContainer);
        gprsSupportIndicator = ind.getGprsSupportIndicator();
        assertTrue(gprsSupportIndicator);
        sm_RP_MTI = ind.getSM_RP_MTI();
        assertEquals(sm_RP_MTI.getCode(), 0);
        sm_RP_SMEA = ind.getSM_RP_SMEA();
        assertNull(sm_RP_SMEA);
        smDeliveryNotIntended = ind.getSmDeliveryNotIntended();
        assertNull(smDeliveryNotIntended);
        ipSmGwGuidanceIndicator = ind.getIpSmGwGuidanceIndicator();
        assertFalse(ipSmGwGuidanceIndicator);
        imsi = ind.getImsi();
        assertNull(imsi);
        t4TriggerIndicator = ind.getT4TriggerIndicator();
        assertFalse(t4TriggerIndicator);
        singleAttemptDelivery = ind.getSingleAttemptDelivery();
        assertFalse(singleAttemptDelivery);
        correlationID = ind.getCorrelationID();
        assertNull(correlationID.getHlrId());
        assertNull(correlationID.getSipUriA());
        assertEquals(correlationID.getSipUriB().getData(), new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        smsfSupportIndicator = ind.getSmsfSupportIndicator();
        assertFalse(smsfSupportIndicator);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        // test 1 (getEncodedDataSimple)
        //msisdn + sca
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "13457745551");
        AddressString sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null,
                null, false, null, false, false, null, null, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2 (getEncodedDataComplex)
        // msisdn + sca + sm_RP_SMEA
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "13457745551");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SM_RP_SMEA sm_RP_SMEA = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, true, null, sm_RP_SMEA, null, false, null, false, false, null, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3 (getEncodedData0)
        //msisdn + sca + sm_RP_SMEA + extContainer + SM_RP_MTI
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
        sca = new AddressStringImpl(false, AddressNature.network_specific_number, NumberingPlan.national, "4444");
        sm_RP_SMEA = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, MAPExtensionContainerTest.GetTestExtensionContainer(),
                true, SM_RP_MTI.SMS_Status_Report, sm_RP_SMEA, null, false, null, false, false, null, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4 (getEncodedData1)
        //msisdn + sca + tc
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        TeleserviceCodeImpl tc = new TeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, null, false, null, false, false, tc, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 (getEncodedData2)
        //msisdn + sca + sm_RP_PRI + gprsSupportIndicator + ipSmGwGuidanceIndicator + t4TriggerIndicator + this.singleAttemptDelivery
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, null, true, null, null, null, true, null, true, true, null, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6 (getEncodedData3)
        //msisdn + sca + smDeliveryNotIntended
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        SMDeliveryNotIntended smDeliveryNotIntended = SMDeliveryNotIntended.onlyIMSIRequested;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, smDeliveryNotIntended, false, null, false, false, null, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 7 (getEncodedDataRel18_1)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         SM-RP-SMEA - (491710460020)
         *             Length: 12 address digits
         *             1... .... = Extension: No extension
         *             .001 .... = Type of number: International (1)
         *             .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *             Digits: 491710460020
         *         t4-Trigger-Indicator
         *         singleAttemptDelivery
         *         smsf-supportIndicator
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        boolean sm_RP_PRI = true;
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        boolean gprsSupportIndicator = true;
        SM_RP_MTI sM_RP_MTI = SM_RP_MTI.getInstance(0);
        AddressField addressField = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "491710460020");
        sm_RP_SMEA = new SM_RP_SMEAImpl(addressField);
        boolean ipSmGwGuidanceIndicator = false;
        boolean t4TriggerIndicator = true;
        boolean singleAttemptDelivery = true;
        boolean smsfSupportIndicator = true;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, sca, null,
                gprsSupportIndicator, sM_RP_MTI, sm_RP_SMEA, null, ipSmGwGuidanceIndicator,
                null, t4TriggerIndicator, singleAttemptDelivery, null, null, smsfSupportIndicator);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 8 (SRISM with correlationID => dummy MSISDN, ip-sm-gwGuidanceIndicator, smsf-supportIndicator)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: bf7777f7
         *             1... .... = Extension: No Extension
         *             .011 .... = Nature of number: Network Specific Number (0x3)
         *             .... 1111 = Number plan: Reserved for extension (0xf)
         *             Address digits: 77777
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         ip-sm-gwGuidanceIndicator
         *         correlationID
         *             sip-uri-B: 35393839393037373933374072657374636f6d6d2e6f7267
         *         smsf-supportIndicator
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
        sca = new AddressStringImpl(false, AddressNature.network_specific_number, NumberingPlan.reserved, "77777");
        sM_RP_MTI = SM_RP_MTI.getInstance(0);
        sm_RP_SMEA = null;
        ipSmGwGuidanceIndicator = true;
        t4TriggerIndicator = false;
        singleAttemptDelivery = false;
        SipUri sipUriB = new SipUriImpl(new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        CorrelationID correlationID = new CorrelationIDImpl(null, null, sipUriB);
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, sca, null,
                gprsSupportIndicator, sM_RP_MTI, sm_RP_SMEA, null, ipSmGwGuidanceIndicator,
                null, t4TriggerIndicator, singleAttemptDelivery, null, correlationID, smsfSupportIndicator);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_2();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 9 (with sm-deliveryNotIntended)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 919598097739f7
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 59899077937
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         sm-deliveryNotIntended: onlyMCC-MNC-requested (1)
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        gprsSupportIndicator = false;
        sM_RP_MTI = null;
        smDeliveryNotIntended = SMDeliveryNotIntended.onlyMCCMNCRequested;
        ipSmGwGuidanceIndicator = false;
        correlationID = null;
        smsfSupportIndicator = false;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, sca, null,
                gprsSupportIndicator, sM_RP_MTI, sm_RP_SMEA, smDeliveryNotIntended, ipSmGwGuidanceIndicator,
                null, t4TriggerIndicator, singleAttemptDelivery, null, correlationID, smsfSupportIndicator);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 10 (with t4-Trigger-Indicator => dummy MSISDN, SM-RP-SMEA)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 919598091032
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 5989900123
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         SM-RP-SMEA - (491710460020)
         *             Length: 12 address digits
         *             1... .... = Extension: No extension
         *             .001 .... = Type of number: International (1)
         *             .... 0001 = Numbering plan: ISDN/telephone (E.164/E.163) (1)
         *             Digits: 491710460020
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *             Mobile Country Code (MCC): International Mobile, shared code (901)
         *             Mobile Network Code (MNC): Deutsche Telekom AG (40)
         *         t4-Trigger-Indicator
         *         singleAttemptDelivery
         *         smsf-supportIndicator
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
        sca = new AddressStringImpl(false, AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        gprsSupportIndicator = true;
        sM_RP_MTI = SM_RP_MTI.getInstance(0);
        addressField = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "491710460020");
        sm_RP_SMEA = new SM_RP_SMEAImpl(addressField);
        smDeliveryNotIntended = null;
        IMSI imsi = new IMSIImpl("901405105680000");
        t4TriggerIndicator = true;
        singleAttemptDelivery = true;
        smsfSupportIndicator = true;

        ind = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, sca, null,
                gprsSupportIndicator, sM_RP_MTI, sm_RP_SMEA, smDeliveryNotIntended, ipSmGwGuidanceIndicator,
                imsi, t4TriggerIndicator, singleAttemptDelivery, null, correlationID, smsfSupportIndicator);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_4();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 11 (SRISM with correlationID => dummy MSISDN, SC address is extension)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: sendRoutingInfoForSM (45)
         *         msisdn: 9100000000000000f0
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 000000000000000
         *         sm-RP-PRI: True
         *         serviceCentreAddress: 79f5
         *             0... .... = Extension: Extension
         *             .111 .... = Nature of number: Reserved for extension (0x7)
         *             .... 1001 = Number plan: Private Numbering (0x9)
         *             Address digits: 5
         *         gprsSupportIndicator
         *         sm-RP-MTI: 0
         *         correlationID
         *             sip-uri-B: 35393839393037373933374072657374636f6d6d2e6f7267
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000000000000000");
        sca = new AddressStringImpl(true, AddressNature.reserved_for_extension, NumberingPlan.private_plan, "5");
        sM_RP_MTI = SM_RP_MTI.getInstance(0);
        sm_RP_SMEA = null;
        t4TriggerIndicator = false;
        singleAttemptDelivery = false;
        sipUriB = new SipUriImpl(new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        correlationID = new CorrelationIDImpl(null, null, sipUriB);
        smsfSupportIndicator = false;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, sca, null,
                gprsSupportIndicator, sM_RP_MTI, sm_RP_SMEA, null, ipSmGwGuidanceIndicator,
                null, t4TriggerIndicator, singleAttemptDelivery, null, correlationID, smsfSupportIndicator);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_5();

        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
