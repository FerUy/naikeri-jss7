package org.restcomm.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.SipUri;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
*
* @author kostiantyn nosach
* @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
*/

public class CorrelationIDTest {

    private byte[] getEncodedDataFull() {
        return new byte[] {32, 61, -128, 5, 17, 17, 33, 34, 34, -127, 27, 115, 105, 112, 58, 107, 111, 110, 
                115, 116, 97, 110, 116, 105, 110, 64, 116, 101, 108, 101, 115, 116, 97, 120, 46, 99, 111, 109, 
                -126, 23, 115, 105, 112, 58, 110, 111, 115, 97, 99, 104, 64, 116, 101, 108, 101, 115, 116, 97,
                120, 46, 99, 111, 109
        };
    }

    private byte[] getEncodedDataFromRSMDS_ASC() {
        return new byte[] {32, 61, -128, 8, 9, 65, 80, 1, 101, 8, 0, -16, -127, 24, 53, 57, 56, 57, 57, 48, 55,
                55, 57, 51, 55, 64, 114, 101, 115, 116, 99, 111, 109, 109, 46, 111, 114, 103, -126, 23, 109, 116,
                76, 111, 97, 100, 84, 101, 115, 116, 64, 114, 101, 115, 116, 99, 111, 109, 109, 46, 111, 114, 103
        };
    }

    private byte[] getEncodedDataFromSRISM() {
        return new byte[]{32, 25, -126, 23, 109, 116, 76, 111, 97, 100, 84, 101, 115, 116, 64, 114, 101, 115, 116,
                99, 111, 109, 109, 46, 111, 114, 103
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedDataFull();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        CorrelationIDImpl correlationId = new CorrelationIDImpl();
        correlationId.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI hlrId = correlationId.getHlrId();
        SipUri sipUriA = correlationId.getSipUriA();
        SipUri sipUriB = correlationId.getSipUriB();
        assertEquals(hlrId.getData(), "1111122222");
        assertEquals(new String (sipUriA.getData()), "sip:konstantin@telestax.com");
        assertEquals(new String (sipUriB.getData()), "sip:nosach@telestax.com");

        // test 2 from a MAP RSMDS/ASC example with correlationID containing all parameters (hlrId, sipUriA and sipUriB)
        rawData = getEncodedDataFromRSMDS_ASC();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        correlationId = new CorrelationIDImpl();
        correlationId.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        hlrId = correlationId.getHlrId();
        sipUriA = correlationId.getSipUriA();
        sipUriB = correlationId.getSipUriB();
        // Wireshark example
        /*
         * correlationID
         *     IMSI: 901405105680000
         *     [Association IMSI: 901405105680000]
         *         Mobile Country Code (MCC): International Mobile, shared code (901)
         *         Mobile Network Code (MNC): Deutsche Telekom AG (40)
         *     sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *     sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        assertEquals(hlrId.getData(), "901405105680000");
        assertEquals(sipUriA.getData(), new byte[] { 0x35, 0x39, 0x38, 0x39, 0x39, 0x30, 0x37, 0x37, 0x39,
                0x33, 0x37, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(sipUriB.getData(), new byte[] { 0x6d, 0x74, 0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65, 0x73,
                0x74, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(new String (sipUriA.getData()), "59899077937@restcomm.org");
        assertEquals(new String (sipUriB.getData()), "mtLoadTest@restcomm.org");

        // test 3 from a MAP SRISM example with correlationID containing only its M parameter (sipUriB)
        rawData = getEncodedDataFromSRISM();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        correlationId = new CorrelationIDImpl();
        correlationId.decodeAll(asn);

        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        hlrId = correlationId.getHlrId();
        sipUriA = correlationId.getSipUriA();
        sipUriB = correlationId.getSipUriB();
        // Wireshark example
        /*
         * correlationID
         *     sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        assertNull(hlrId);
        assertNull(sipUriA);
        assertEquals(new String (sipUriB.getData()), "mtLoadTest@restcomm.org");
        assertEquals(sipUriB.getData(), new byte[] { 0x6d, 0x74, 0x4c, 0x6f, 0x61, 0x64, 0x54, 0x65, 0x73,
                0x74, 0x40, 0x72, 0x65, 0x73, 0x74, 0x63, 0x6f, 0x6d, 0x6d, 0x2e, 0x6f, 0x72, 0x67 });
        assertEquals(new String (sipUriB.getData()), "mtLoadTest@restcomm.org");

    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        IMSIImpl hlrId = new IMSIImpl("1111122222");
        byte [] dataSipA = "sip:konstantin@telestax.com".getBytes();
        byte [] dataSipB = "sip:nosach@telestax.com".getBytes();
        SipUriImpl sipUriA = new SipUriImpl(dataSipA);
        SipUriImpl sipUriB = new SipUriImpl(dataSipB);
        
        CorrelationIDImpl correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);

        AsnOutputStream asnOS = new AsnOutputStream();
        asnOS.reset();
        correlationID.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, 0);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataFull();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2 from a MAP RSMDS/ASC example with correlationID containing all parameters (hlrId, sipUriA and sipUriB)
        // Wireshark example
        /*
         * correlationID
         *     IMSI: 901405105680000
         *     [Association IMSI: 901405105680000]
         *         Mobile Country Code (MCC): International Mobile, shared code (901)
         *         Mobile Network Code (MNC): Deutsche Telekom AG (40)
         *     sip-uri-A: 35393839393037373933374072657374636f6d6d2e6f7267
         *     sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        hlrId = new IMSIImpl("901405105680000");
        sipUriA = new SipUriImpl("59899077937@restcomm.org".getBytes(StandardCharsets.UTF_8));
        sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
        correlationID = new CorrelationIDImpl(hlrId, sipUriA, sipUriB);

        asnOS = new AsnOutputStream();
        asnOS.reset();
        correlationID.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFromRSMDS_ASC();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3 from a MAP SRISM example with correlationID containing only its M parameter (sipUriB)
        // Wireshark example
        /*
         * correlationID
         *     sip-uri-B: 6d744c6f6164546573744072657374636f6d6d2e6f7267
         */
        sipUriB = new SipUriImpl("mtLoadTest@restcomm.org".getBytes(StandardCharsets.UTF_8));
        correlationID = new CorrelationIDImpl(null, null, sipUriB);

        asnOS = new AsnOutputStream();
        asnOS.reset();
        correlationID.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFromSRISM();

        assertTrue(Arrays.equals(rawData, encodedData));
    }
}

