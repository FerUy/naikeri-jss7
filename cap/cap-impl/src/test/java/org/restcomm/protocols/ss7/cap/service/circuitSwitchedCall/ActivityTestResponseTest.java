package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

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
public class ActivityTestResponseTest {

    private static final Logger logger = LogManager.getLogger(ActivityTestResponseTest.class.getName());

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        ActivityTestResponseImpl original = new ActivityTestResponseImpl();
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "activityTestResponse", ActivityTestResponseImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.debug(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ActivityTestResponseImpl copy = reader.read("activityTestResponse", ActivityTestResponseImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
    }

}
