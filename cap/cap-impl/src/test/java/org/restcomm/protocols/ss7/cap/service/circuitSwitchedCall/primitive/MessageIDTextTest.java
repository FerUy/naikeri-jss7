package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

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
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageIDTextTest {

    private static final Logger logger = LogManager.getLogger(MessageIDTextTest.class.getName());

    public byte[] getData1() {
        return new byte[] { 48, 17, (byte) 128, 9, 72, 101, 108, 108, 111, 32, 33, 33, 33, (byte) 129, 4, 1, 2, 3, 4 };
    }

    public byte[] getDataInt() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MessageIDTextImpl elem = new MessageIDTextImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals(elem.getMessageContent(), "Hello !!!");
        assertTrue(Arrays.equals(elem.getAttributes(), this.getDataInt()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MessageIDTextImpl elem = new MessageIDTextImpl("Hello !!!", getDataInt());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // String messageContent, byte[] attributes
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        String messageContent = "123 ASzs!";
        byte[] attributes = new byte[] { 0x01, (byte) 0xEE };
        MessageIDTextImpl original = new MessageIDTextImpl(messageContent, attributes);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "messageIDText", MessageIDTextImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.info(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MessageIDTextImpl copy = reader.read("messageIDText", MessageIDTextImpl.class);

        assertEquals(copy.getMessageContent(), messageContent);
        assertEquals(copy.getAttributes(), attributes);
    }
}
