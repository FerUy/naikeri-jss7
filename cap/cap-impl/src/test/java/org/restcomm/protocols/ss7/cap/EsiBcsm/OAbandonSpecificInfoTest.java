package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OAbandonSpecificInfoTest {

    private static final Logger logger = LogManager.getLogger(OAbandonSpecificInfoTest.class.getName());

    public byte[] getData1() {
        return new byte[] { (byte) 48, 3, (byte) 159, 50, 0 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        OAbandonSpecificInfoImpl elem = new OAbandonSpecificInfoImpl();
        ais.readTag();
        elem.decodeAll(ais);
        assertTrue(elem.getRouteNotPermitted());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {

        OAbandonSpecificInfoImpl elem = new OAbandonSpecificInfoImpl(true);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        OAbandonSpecificInfoImpl original = new OAbandonSpecificInfoImpl(true);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "oAbandonSpecificInfo", OAbandonSpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.debug(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OAbandonSpecificInfoImpl copy = reader.read("oAbandonSpecificInfo", OAbandonSpecificInfoImpl.class);

        assertEquals(copy.getRouteNotPermitted(), original.getRouteNotPermitted());
    }

}
