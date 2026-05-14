package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 21, -128, 6, 17, 33, 34, 51, 67, 68, 2, 1, 4, -125, 1, 0, -124, 3, -71, -2, -59, -122, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 57, -128, 6, 51, 51, 67, 68, 68, -12, 2, 1, 5, 5, 0, -127, 0, 48, 34, 4, 16, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 14, 2, 2, 2, 2, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, -125, 1, 1, -123, 1, 6 };
    }

    private byte[] getEncodedData_V2() {
        return new byte[] { 4, 8, 82, 0, 7, 34, 2, 35, 103, -9 };
    }

    private byte[] getRequestingPlmnId() {
        return new byte[] { (byte) 185, (byte) 254, (byte) 197 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendAuthenticationInfoRequestImpl asc = new SendAuthenticationInfoRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        IMSI imsi = asc.getImsi();
        assertEquals(imsi.getData(), "111222333444");
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
        assertEquals(asc.getNumberOfRequestedVectors(), 4);

        assertNotNull(asc.getRequestingPlmnId());
        assertTrue(Arrays.equals(asc.getRequestingPlmnId().getData(), getRequestingPlmnId()));

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertTrue(asc.getAdditionalVectorsAreForEPS());
        assertFalse(asc.getUeUsageTypeRequestIndication());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoRequestImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        imsi = asc.getImsi();
        assertEquals(imsi.getData(), "33333444444");
        assertEquals(asc.getRequestingNodeType(), RequestingNodeType.sgsn);
        assertEquals(asc.getNumberOfRequestedVectors(), 5);

        assertNull(asc.getRequestingPlmnId());

        ReSynchronisationInfo rsi = asc.getReSynchronisationInfo();
        assertTrue(Arrays.equals(rsi.getRand(), ReSynchronisationInfoTest.getRandData()));
        assertTrue(Arrays.equals(rsi.getAuts(), ReSynchronisationInfoTest.getAutsData()));

        assertNull(asc.getExtensionContainer());
        assertEquals((int) asc.getNumberOfRequestedAdditionalVectors(), 6);

        assertTrue(asc.getSegmentationProhibited());
        assertTrue(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());
        assertFalse(asc.getUeUsageTypeRequestIndication());

        rawData = getEncodedData_V2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoRequestImpl(2);
        asc.decodeAll(asn);
        assertEquals(asc.getMapProtocolVersion(), 2);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertEquals(imsi.getData(), "250070222032767");
        assertNull(asc.getRequestingNodeType());
        assertEquals(asc.getNumberOfRequestedVectors(), 0);

        assertNull(asc.getRequestingPlmnId());

        assertNull(asc.getReSynchronisationInfo());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getNumberOfRequestedAdditionalVectors());

        assertFalse(asc.getSegmentationProhibited());
        assertFalse(asc.getImmediateResponsePreferred());
        assertFalse(asc.getAdditionalVectorsAreForEPS());
        assertFalse(asc.getUeUsageTypeRequestIndication());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        long mapProtocolVersion = 3;
        IMSIImpl imsi = new IMSIImpl("111222333444");
        int numberOfRequestedVectors = 4;
        boolean segmentationProhibited = false;
        boolean immediateResponsePreferred = false;
        ReSynchronisationInfo reSynchronisationInfo = null;
        MAPExtensionContainer extensionContainer = null;
        RequestingNodeType requestingNodeType = RequestingNodeType.vlr;
        PlmnIdImpl requestingPlmnId = new PlmnIdImpl(getRequestingPlmnId());
        Integer numberOfRequestedAdditionalVectors = null;
        boolean additionalVectorsAreForEPS = true;
        boolean ueUsageTypeRequestIndicator = false;
        SendAuthenticationInfoRequestImpl asc = new SendAuthenticationInfoRequestImpl(mapProtocolVersion, imsi, numberOfRequestedVectors,
                segmentationProhibited, immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType,
                requestingPlmnId, numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS, ueUsageTypeRequestIndicator);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("33333444444");
        numberOfRequestedVectors = 5;
        segmentationProhibited = true;
        immediateResponsePreferred = true;
        reSynchronisationInfo = new ReSynchronisationInfoImpl(ReSynchronisationInfoTest.getRandData(),
                ReSynchronisationInfoTest.getAutsData());
        requestingNodeType = RequestingNodeType.sgsn;
        requestingPlmnId = null;
        numberOfRequestedAdditionalVectors = 6;
        additionalVectorsAreForEPS = false;
        asc = new SendAuthenticationInfoRequestImpl(mapProtocolVersion, imsi, numberOfRequestedVectors, segmentationProhibited,
                immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
                numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS, ueUsageTypeRequestIndicator);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        mapProtocolVersion = 2;
        imsi = new IMSIImpl("250070222032767");
        numberOfRequestedVectors = 0;
        segmentationProhibited = false;
        immediateResponsePreferred = false;
        reSynchronisationInfo = null;
        requestingNodeType = null;
        numberOfRequestedAdditionalVectors = null;
        asc = new SendAuthenticationInfoRequestImpl(mapProtocolVersion, imsi, numberOfRequestedVectors, segmentationProhibited,
                immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
                numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS, ueUsageTypeRequestIndicator);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
