package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

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
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

/**
 * @author kostianyn nosach
 *
 */
public class SplitLegRequestTest {

    private static final Logger logger = LogManager.getLogger(SplitLegRequestTest.class.getName());

    public byte[] getData() {
        return new byte[] { 48, 28, -96, 3, -128, 1, 1, -127, 1, 1, -94, 18, 
                48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        SplitLegRequestImpl elem = new SplitLegRequestImpl();
        ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getLegToBeSplit().getSendingSideID(), LegType.leg1);
        assertEquals(elem.getNewCallSegment(), Integer.valueOf(1));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        LegID legIDToMove = new LegIDImpl(true, LegType.leg1);

        SplitLegRequestImpl elem = new SplitLegRequestImpl(legIDToMove, 1, CAPExtensionsTest.createTestCAPExtensions());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
        
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        LegID legIDToMove = new LegIDImpl(true, LegType.leg1);
        SplitLegRequestImpl original = new SplitLegRequestImpl(legIDToMove, 1, CAPExtensionsTest.createTestCAPExtensions());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "splitLegRequest", SplitLegRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        logger.info(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        SplitLegRequestImpl copy = reader.read("splitLegRequest", SplitLegRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getExtensions().getExtensionFields().get(0).getLocalCode(), original.getExtensions().getExtensionFields().get(0).getLocalCode());
        assertEquals(copy.getExtensions().getExtensionFields().get(1).getCriticalityType(), original.getExtensions().getExtensionFields().get(1).getCriticalityType());
        assertEquals(copy.getLegToBeSplit().getSendingSideID(), original.getLegToBeSplit().getSendingSideID());
        assertEquals(copy.getNewCallSegment(), original.getNewCallSegment());
    }
}
