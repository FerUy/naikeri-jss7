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
public class ActivityTestRequestTest {

    private static final Logger logger = LogManager.getLogger(ActivityTestRequestTest.class.getName());

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        ActivityTestRequestImpl original = new ActivityTestRequestImpl();
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "activityTestRequest", ActivityTestRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.info(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ActivityTestRequestImpl copy = reader.read("activityTestRequest", ActivityTestRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
    }

}
