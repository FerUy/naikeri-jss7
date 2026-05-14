package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedNodes;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
*/
public class ProvideSubscriberInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 12, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 162, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 62, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 129, 4, 11, 22, 33, 44, (byte) 162, 2, (byte) 128, 0, (byte) 163, 39,
                (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161,
                3, 31, 32, 33, (byte) 132, 1, 4 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 0x30, 0x28,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x78, 0x01, 0x21,
                0x14, (byte) 0xf6, (byte) 0x81, 0x04, 0x72, 0x02, (byte) 0xe9, (byte) 0x8c,
                (byte) 0xa2, 0x13, (byte) 0x80, 0x00, (byte) 0x81, 0x00, (byte) 0x83, 0x00,
                (byte) 0x84, 0x01, 0x00, (byte) 0x86, 0x00, (byte) 0x85, 0x00, (byte) 0x87,
                0x00, (byte) 0x8b, 0x00, (byte) 0x8c, 0x00, (byte) 0x84, 0x01, 0x05
        };
    }

    private byte[] getEncodedData4() {
        return new byte[] { 0x30, 0x26,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x78, 0x01, 0x21,
                0x35, (byte) 0xf9, (byte) 0x81, 0x04, 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce,
                (byte) 0xa2, 0x11, (byte) 0x80, 0x00, (byte) 0x81, 0x00, (byte) 0x84, 0x01,
                0x01, (byte) 0x8b, 0x00, (byte) 0x88, 0x00, (byte) 0x89, 0x02, 0x00,
                (byte) 0x80, (byte) 0x8a, 0x00, (byte) 0x84, 0x01, 0x01

        };
    }

    private byte[] getLmsiData2() {
        return new byte[] { 11, 22, 33, 44 };
    }

    private byte[] getLmsiData3() {
        return new byte[] { 0x72, 0x02, (byte) 0xe9, (byte) 0x8c };
    }

    private byte[] getLmsiData4() {
        return new byte[] { 0x71, (byte) 0xff, (byte) 0xac, (byte) 0xce };

    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberInformation" })
    public void testDecode() throws Exception {

        // encoded data 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ProvideSubscriberInfoRequestImpl asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = asc.getImsi();
        assertEquals(imsi.getData(), "111222333444");
        LMSI lmsi = asc.getLmsi();
        assertNull(lmsi);
        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getCallPriority());

        // encoded data 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertEquals(imsi.getData(), "111222333444");
        lmsi = asc.getLmsi();
        assertEquals(lmsi.getData(), getLmsiData2());
        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertFalse(asc.getRequestedInfo().getSubscriberState());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
        assertEquals(asc.getCallPriority(), EMLPPPriority.priorityLevel4);

        // encoded data 3 (got from MAP load test on CS domain)
        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertEquals(imsi.getData(), "748026871012416");
        lmsi = asc.getLmsi();
        assertEquals(lmsi.getData(), getLmsiData3());
        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertTrue(asc.getRequestedInfo().getSubscriberState());
        assertNull(asc.getRequestedInfo().getExtensionContainer());
        assertTrue(asc.getRequestedInfo().getCurrentLocation());
        assertEquals(asc.getRequestedInfo().getRequestedDomain(), DomainType.csDomain);
        assertTrue(asc.getRequestedInfo().getImei());
        assertTrue(asc.getRequestedInfo().getMsClassmark());
        assertTrue(asc.getRequestedInfo().getMnpRequestedInfo());
        assertTrue(asc.getRequestedInfo().getLocationInformationEPSSupported());
        assertFalse(asc.getRequestedInfo().getTadsData());
        assertNull(asc.getRequestedInfo().getRequestedNodes());
        assertFalse(asc.getRequestedInfo().getServingNodeIndication());
        assertTrue(asc.getRequestedInfo().getLocalTimeZoneRequest());
        assertNull(asc.getExtensionContainer());
        assertEquals(asc.getCallPriority(), EMLPPPriority.priorityLevelB);

        // encoded data 4 (got from MAP load test on PS domain)
        rawData = getEncodedData4();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertEquals(imsi.getData(), "748026871012539");
        lmsi = asc.getLmsi();
        assertEquals(lmsi.getData(), getLmsiData4());
        assertTrue(asc.getRequestedInfo().getLocationInformation());
        assertTrue(asc.getRequestedInfo().getSubscriberState());
        assertNull(asc.getRequestedInfo().getExtensionContainer());
        assertFalse(asc.getRequestedInfo().getCurrentLocation());
        assertEquals(asc.getRequestedInfo().getRequestedDomain(), DomainType.psDomain);
        assertFalse(asc.getRequestedInfo().getImei());
        assertFalse(asc.getRequestedInfo().getMsClassmark());
        assertFalse(asc.getRequestedInfo().getMnpRequestedInfo());
        assertTrue(asc.getRequestedInfo().getLocationInformationEPSSupported());
        assertTrue(asc.getRequestedInfo().getTadsData());
        assertTrue(asc.getRequestedInfo().getRequestedNodes().getMme());
        assertFalse(asc.getRequestedInfo().getRequestedNodes().getSgsn());
        assertTrue(asc.getRequestedInfo().getServingNodeIndication());
        assertFalse(asc.getRequestedInfo().getLocalTimeZoneRequest());
        assertNull(asc.getExtensionContainer());
        assertEquals(asc.getCallPriority(), EMLPPPriority.priorityLevel1);
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberInformation" })
    public void testEncode() throws Exception {

        // encoded data 1
        IMSIImpl imsi = new IMSIImpl("111222333444");
        LMSI lmsi = null;
        boolean locationInformation = true;
        boolean subscriberState = false;
        boolean currentLocation = false;
        DomainType requestedDomain = null;
        boolean imei = false;
        boolean msClassmark = false;
        boolean mnpRequestedInfo = false;
        boolean locationInformationEPSSupported = false;
        boolean tadsData = false;
        RequestedNodes requestedNodes = null;
        boolean servingNodeIndication = false;
        boolean localTimeZoneRequest = false;
        MAPExtensionContainer extensionContainer = null;
        EMLPPPriority callPriority = null;
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer, currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes, servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
        ProvideSubscriberInfoRequestImpl asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, extensionContainer, callPriority);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // encoded data 2
        lmsi = new LMSIImpl(getLmsiData2());
        asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, MAPExtensionContainerTest.GetTestExtensionContainer(), EMLPPPriority.priorityLevel4);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // encoded data 3 (got from MAP load test on CS domain)
        imsi = new IMSIImpl("748026871012416");
        lmsi = new LMSIImpl(getLmsiData3());
        subscriberState = true;
        currentLocation = true;
        requestedDomain = DomainType.csDomain;
        imei = true;
        msClassmark = true;
        mnpRequestedInfo = true;
        locationInformationEPSSupported = true;
        localTimeZoneRequest = true;

        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer, currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes, servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
        callPriority = EMLPPPriority.priorityLevelB;
        asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, extensionContainer, callPriority);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // encoded data 4 (got from MAP load test on PS domain)
        imsi = new IMSIImpl("748026871012539");
        lmsi = new LMSIImpl(getLmsiData4());
        currentLocation = false;
        requestedDomain = DomainType.psDomain;
        imei = false;
        msClassmark = false;
        mnpRequestedInfo = false;
        tadsData = true;
        requestedNodes = new RequestedNodesImpl(true, false);
        servingNodeIndication = true;
        localTimeZoneRequest = false;
        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer, currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes, servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
        callPriority = EMLPPPriority.priorityLevel1;
        asc = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, extensionContainer, callPriority);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
