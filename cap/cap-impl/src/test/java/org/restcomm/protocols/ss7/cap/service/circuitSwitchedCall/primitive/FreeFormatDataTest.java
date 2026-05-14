package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class FreeFormatDataTest {

    private static final Logger logger = LogManager.getLogger(FreeFormatDataTest.class.getName());

    public byte[] getData() {
        return new byte[] { 1, 2, 3, 4, 5 };
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        FreeFormatDataImpl original = new FreeFormatDataImpl(getData());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "freeFormatData", FreeFormatDataImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.info(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FreeFormatDataImpl copy = reader.read("freeFormatData", FreeFormatDataImpl.class);

        assertEquals(copy.getData(), getData());
    }

}
