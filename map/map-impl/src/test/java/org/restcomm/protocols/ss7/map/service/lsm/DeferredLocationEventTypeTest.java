package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DeferredLocationEventTypeTest {

    private byte[] getEncodedDataMsAvailable() {
        return new byte[] { 3, 2, 3, (byte) 0x80 };
    }

    private byte[] getEncodedDataLeavingFromArea() {
        return new byte[] { 3, 2, 3, (byte) 0x20 };
    }

    private byte[] getEncodedDataBeingInsideArea() {
        return new byte[] { 3, 2, 3, (byte) 0x10 };
    }

    private byte[] getEncodedDataEnteringIntoArea() {
        return new byte[] { 3, 2, 3, 0x40 };
    }

    private byte[] getEncodedDataPeriodicLDR() {
        return new byte[] { 3, 2, 3, 0x08 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        // MS Available
        byte[] rawData = getEncodedDataMsAvailable();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        DeferredLocationEventTypeImpl imp = new DeferredLocationEventTypeImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getMsAvailable());
        assertFalse(imp.getEnteringIntoArea());
        assertFalse(imp.getLeavingFromArea());
        assertFalse(imp.getBeingInsideArea());
        assertFalse(imp.getPeriodicLDR());

        // Entering into Area
        rawData = getEncodedDataEnteringIntoArea();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new DeferredLocationEventTypeImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(imp.getMsAvailable());
        assertTrue(imp.getEnteringIntoArea());
        assertFalse(imp.getLeavingFromArea());
        assertFalse(imp.getBeingInsideArea());
        assertFalse(imp.getPeriodicLDR());

        // Leaving from Area
        rawData = getEncodedDataLeavingFromArea();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new DeferredLocationEventTypeImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(imp.getMsAvailable());
        assertFalse(imp.getEnteringIntoArea());
        assertTrue(imp.getLeavingFromArea());
        assertFalse(imp.getBeingInsideArea());
        assertFalse(imp.getPeriodicLDR());

        // Being Inside Area
        rawData = getEncodedDataBeingInsideArea();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new DeferredLocationEventTypeImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(imp.getMsAvailable());
        assertFalse(imp.getEnteringIntoArea());
        assertFalse(imp.getLeavingFromArea());
        assertTrue(imp.getBeingInsideArea());
        assertFalse(imp.getPeriodicLDR());

        // periodicLDR
        rawData = getEncodedDataPeriodicLDR();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new DeferredLocationEventTypeImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertFalse(imp.getMsAvailable());
        assertFalse(imp.getEnteringIntoArea());
        assertFalse(imp.getLeavingFromArea());
        assertFalse(imp.getBeingInsideArea());
        assertTrue(imp.getPeriodicLDR());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        // Entering into Area
        DeferredLocationEventTypeImpl imp = new DeferredLocationEventTypeImpl(false, true, false, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataEnteringIntoArea();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Entering into Area
        imp = new DeferredLocationEventTypeImpl(false, true, false, false, false);

        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataEnteringIntoArea();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Leaving from Area
        imp = new DeferredLocationEventTypeImpl(false, false, true, false, false);

        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataLeavingFromArea();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Being Inside Area
        imp = new DeferredLocationEventTypeImpl(false, false, false, true, false);

        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataBeingInsideArea();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Periodic LDR
        imp = new DeferredLocationEventTypeImpl(false, false, false, false, true);

        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataPeriodicLDR();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
