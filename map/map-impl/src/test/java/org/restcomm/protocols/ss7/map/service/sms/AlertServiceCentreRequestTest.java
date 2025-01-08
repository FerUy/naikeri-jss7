package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsGmscAlertEvent;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class AlertServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 18, 4, 7, -111, -110, 17, 19, 50, 19, -15, 4, 7, -111, -108, -120, 115, 0, -110, -14 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, (byte) 0x82,
                0x01, 0x33, 0x04, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09,
                0x77, 0x39, (byte) 0xf7, 0x04, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, (byte) 0x81, 0x01, 0x01, (byte) 0xa2, 0x52,
                (byte) 0x80, 0x2d, 0x67, 0x6d, 0x73, 0x63, 0x30, 0x33,
                0x2e, 0x67, 0x6d, 0x73, 0x63, 0x2e, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x81,
                0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x83, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19,
                0x09, 0x10, 0x13, (byte) 0xa4, 0x51, (byte) 0x80, 0x30, 0x73,
                0x67, 0x73, 0x6e, 0x35, 0x38, 0x2e, 0x72, 0x61,
                0x63, 0x38, 0x2e, 0x6c, 0x61, 0x63, 0x33, 0x32,
                0x30, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x81,
                0x1d, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x85, 0x07,
                (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x19, 0x09, 0x10, 0x23, (byte) 0xa6,
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
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x87, 0x07, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x19, 0x09, 0x10, 0x20
        };
    }

    private byte[] getEncodedDataRel18_1() {
        return new byte[] { 0x30, 0x63,
                0x04, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09,
                0x77, 0x39, (byte) 0xf7, 0x04, 0x06, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x10, 0x32, 0x04, 0x08, 0x09, 0x41, 0x50,
                0x01, 0x65, 0x08, 0x00, (byte) 0xf0, 0x30, 0x3d, (byte) 0x80,
                0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08, 0x00,
                (byte) 0xf0, (byte) 0x81, 0x18, 0x35, 0x39, 0x38, 0x39, 0x39,
                0x30, 0x37, 0x37, 0x39, 0x33, 0x37, 0x40, 0x72,
                0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e,
                0x6f, 0x72, 0x67, (byte) 0x82, 0x17, 0x6d, 0x74, 0x4c,
                0x6f, 0x61, 0x64, 0x54, 0x65, 0x73, 0x74, 0x40,
                0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x80, 0x04, (byte) 0xb9, 0x08,
                (byte) 0x8f, 0x4e, (byte) 0x81, 0x01, 0x00
        };
    }

    @Test
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(MAPOperationCode.alertServiceCentre);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = asc.getMsisdn();
        AddressString serviceCentreAddress = asc.getServiceCentreAddress();
        IMSI imsi = asc.getImsi();
        CorrelationID correlationID = asc.getCorrelationID();
        Time maximumUeAvailabilityTime = asc.getMaximumUeAvailabilityTime();
        SmsGmscAlertEvent smsGmscAlertEvent = asc.getSmsGmscAlertEvent();
        NetworkNodeDiameterAddress smsGmscDiameterAddress = asc.getSmsGmscDiameterAddress();
        ISDNAddressString newSGSNNumber = asc.getNewSGSNNumber();
        NetworkNodeDiameterAddress newSGSNDiameterAddress = asc.getNewSGSNDiameterAddress();
        ISDNAddressString newMMENumber = asc.getNewMMENumber();
        NetworkNodeDiameterAddress newMMEDiameterAddress = asc.getNewMMEDiameterAddress();
        ISDNAddressString newMSCNumber = asc.getNewMSCNumber();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "49883700292");
        assertNull(imsi);
        assertNull(correlationID);
        assertNull(maximumUeAvailabilityTime);
        assertNull(smsGmscAlertEvent);
        assertNull(smsGmscDiameterAddress);
        assertNull(newSGSNNumber);
        assertNull(newSGSNDiameterAddress);
        assertNull(newMMENumber);
        assertNull(newMMEDiameterAddress);
        assertNull(newMSCNumber);

        // test 2 MAP v8.0.0 with smsGmscAlertEvent, smsGmscDiameterAddress, newSGSNNumber, newSGSNDiameterAddress
        // newMMENumber, newMMEDiameterAddress and newMSCNumber
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new AlertServiceCentreRequestImpl(MAPOperationCode.alertServiceCentre);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = asc.getMsisdn();
        serviceCentreAddress = asc.getServiceCentreAddress();
        imsi = asc.getImsi();
        correlationID = asc.getCorrelationID();
        maximumUeAvailabilityTime = asc.getMaximumUeAvailabilityTime();
        smsGmscAlertEvent = asc.getSmsGmscAlertEvent();
        smsGmscDiameterAddress = asc.getSmsGmscDiameterAddress();
        newSGSNNumber = asc.getNewSGSNNumber();
        newSGSNDiameterAddress = asc.getNewSGSNDiameterAddress();
        newMMENumber = asc.getNewMMENumber();
        newMMEDiameterAddress = asc.getNewMMEDiameterAddress();
        newMSCNumber = asc.getNewMSCNumber();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: alertServiceCentre (64)
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
         *         smsGmscAlertEvent: msUnderNewServingNode (1)
         *         smsGmscDiameterAddress
         *             diameter-Name: gmsc03.gmsc.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *         newSGSNNumber: 91959819091013
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900131
         *         newSGSNDiameterAddress
         *             diameter-Name: sgsn58.rac8.lac320.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: mnc002.mcc748.3gppnetwork.org
         *         newMMENumber: 91959819091023
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900132
         *         newMMEDiameterAddress
         *             diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *         newMSCNumber: 91959819091020
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900102
         */
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertNull(imsi);
        assertNull(correlationID);
        assertNull(maximumUeAvailabilityTime);
        assertEquals(smsGmscAlertEvent, SmsGmscAlertEvent.msUnderNewServingNode);
        //Gmsc
        DiameterIdentity gmscName = new DiameterIdentityImpl("gmsc03.gmsc.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(smsGmscDiameterAddress, new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm));
        // new SGSN
        assertEquals(newSGSNNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newSGSNNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(newSGSNNumber.getAddress(), "598991900131");
        DiameterIdentity sgsnName = new DiameterIdentityImpl("sgsn58.rac8.lac320.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity sgsnRealm = new DiameterIdentityImpl("mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(newSGSNDiameterAddress, new NetworkNodeDiameterAddressImpl(sgsnName, sgsnRealm));
        // new MME
        assertEquals(newMMENumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newMMENumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(newMMENumber.getAddress(), "598991900132");
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity mmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        assertEquals(newMMEDiameterAddress, new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm));
        // new MSC
        assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(newMSCNumber.getAddress(), "598991900102");

        // test 3, MAP v18.0.0 with IMSI, correlationID, maximumUeAvailabilityTime and smsGmscAlertEvent
        rawData = getEncodedDataRel18_1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new AlertServiceCentreRequestImpl(MAPOperationCode.alertServiceCentre);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = asc.getMsisdn();
        serviceCentreAddress = asc.getServiceCentreAddress();
        imsi = asc.getImsi();
        correlationID = asc.getCorrelationID();
        maximumUeAvailabilityTime = asc.getMaximumUeAvailabilityTime();
        smsGmscAlertEvent = asc.getSmsGmscAlertEvent();
        smsGmscDiameterAddress = asc.getSmsGmscDiameterAddress();
        newSGSNNumber = asc.getNewSGSNNumber();
        newSGSNDiameterAddress = asc.getNewSGSNDiameterAddress();
        newMMENumber = asc.getNewMMENumber();
        newMMEDiameterAddress = asc.getNewMMEDiameterAddress();
        newMSCNumber = asc.getNewMSCNumber();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: alertServiceCentre (64)
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
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         correlationID
         *             IMSI: 901405105680000
         *             [Association IMSI: 901405105680000]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         *         maximumUeAvailabilityTime: b9088f4e
         *         smsGmscAlertEvent: msAvailableForMtSms (0)
         */
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(serviceCentreAddress.getAddress(), "5989900123");
        assertEquals(imsi.getData(), "901405105680000");
        assertEquals(correlationID.getSipUriA().getData(), new byte[] {0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37,
                0x39, 0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67});
        assertEquals(correlationID.getSipUriA(), new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(correlationID.getSipUriB().getData(), new byte[] {0x6d, 0x74, 0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65,
                0x73, 0x74, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67});
        assertEquals(correlationID.getSipUriB(), new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(maximumUeAvailabilityTime.getData(), new byte[] { (byte) 0xb9, 0x08, (byte) 0x8f, 0x4e });
        assertEquals(maximumUeAvailabilityTime.getYear(), 1998);
        assertEquals(maximumUeAvailabilityTime.getMonth(), 5);
        assertEquals(maximumUeAvailabilityTime.getDay(), 16);
        assertEquals(maximumUeAvailabilityTime.getHour(), 22);
        assertEquals(maximumUeAvailabilityTime.getMinute(), 18);
        assertEquals(maximumUeAvailabilityTime.getSecond(), 54);
        assertEquals(smsGmscAlertEvent, SmsGmscAlertEvent.msAvailableForMtSms);
        assertNull(smsGmscDiameterAddress);
        assertNull(newSGSNNumber);
        assertNull(newSGSNDiameterAddress);
        assertNull(newMMENumber);
        assertNull(newMMEDiameterAddress);
        assertNull(newMSCNumber);
        System.out.println(maximumUeAvailabilityTime);
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        // test 1
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "29113123311");
        AddressString serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "49883700292");
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress, null, null,
                null, null, null, null, null,
                null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2 MAP v8.0.0 with smsGmscAlertEvent, smsGmscDiameterAddress, newSGSNNumber, newSGSNDiameterAddress
        // newMMENumber, newMMEDiameterAddress and newMSCNumber
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: alertServiceCentre (64)
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
         *         smsGmscAlertEvent: msUnderNewServingNode (1)
         *         smsGmscDiameterAddress
         *             diameter-Name: gmsc03.gmsc.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *         newSGSNNumber: 91959819091013
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900131
         *         newSGSNDiameterAddress
         *             diameter-Name: sgsn58.rac8.lac320.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: mnc002.mcc748.3gppnetwork.org
         *         newMMENumber: 91959819091023
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900132
         *         newMMEDiameterAddress
         *             diameter-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             diameter-Realm: epc.mnc002.mcc748.3gppnetwork.org
         *         newMSCNumber: 91959819091020
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 598991900102
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        //Gmsc
        SmsGmscAlertEvent smsGmscAlertEvent = SmsGmscAlertEvent.msUnderNewServingNode;
        DiameterIdentity gmscName = new DiameterIdentityImpl("gmsc03.gmsc.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity gmscRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress smsGmscDiameterAddress = new NetworkNodeDiameterAddressImpl(gmscName, gmscRealm);
        // new SGSN
        ISDNAddressString newSGSNNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900131");
        DiameterIdentity sgsnName = new DiameterIdentityImpl("sgsn58.rac8.lac320.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity sgsnRealm = new DiameterIdentityImpl("mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress newSGSNDiameterAddress = new NetworkNodeDiameterAddressImpl(sgsnName, sgsnRealm);
        // new MME
        ISDNAddressString newMMENumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900132");
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity mmeRealm = new DiameterIdentityImpl("epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        NetworkNodeDiameterAddress newMMEDiameterAddress = new NetworkNodeDiameterAddressImpl(mmeName, mmeRealm);
        // new MSC
        ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "598991900102");
        asc = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress, null, null,
                null, smsGmscAlertEvent, smsGmscDiameterAddress, newSGSNNumber, newSGSNDiameterAddress,
                newMMENumber, newMMEDiameterAddress, newMSCNumber);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3, MAP v18.0.0 with IMSI, correlationID, maximumUeAvailabilityTime and smsGmscAlertEvent
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: alertServiceCentre (64)
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
         *         IMSI: 901405105680000
         *         [Association IMSI: 901405105680000]
         *         correlationID
         *             IMSI: 901405105680000
         *             [Association IMSI: 901405105680000]
         *             sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *             sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         *         maximumUeAvailabilityTime: b9088f4e
         *         smsGmscAlertEvent: msAvailableForMtSms (0)
         */
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "5989900123");
        IMSI imsi = new IMSIImpl("901405105680000");
        IMSI hlrId = new IMSIImpl("901405105680000");
        SipUri sipUriA = new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8));
        SipUri sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
        CorrelationID correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);
        smsGmscAlertEvent = SmsGmscAlertEvent.msAvailableForMtSms;
        Time maximumUeAvailabilityTime = new TimeImpl(new byte[] { (byte) 0xb9, 0x08, (byte) 0x8f, 0x4e });
        asc = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress, imsi, correlationID,
                maximumUeAvailabilityTime, smsGmscAlertEvent, null, null, null,
                null, null, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
