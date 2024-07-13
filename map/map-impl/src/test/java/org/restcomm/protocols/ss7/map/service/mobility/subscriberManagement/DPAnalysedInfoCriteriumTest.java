
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class DPAnalysedInfoCriteriumTest {

    public byte[] getData() {
        return new byte[] { 0x30, 0x18,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x41,
                (byte) 0x87, 0x40, 0x23, 0x02, 0x01, 0x03, 0x04, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, 0x0a,
                0x01, 0x00 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl();
        dpAnalysedInfoCriterium.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(dpAnalysedInfoCriterium.getDialledNumber().getAddress(), "491714780432");
        assertEquals(dpAnalysedInfoCriterium.getDialledNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDialledNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 3);
        assertEquals(dpAnalysedInfoCriterium.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(dpAnalysedInfoCriterium.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(dpAnalysedInfoCriterium.getExtensionContainer());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = null;

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491714780432");
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710460029");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 3, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        AsnOutputStream asn = new AsnOutputStream();
        dpAnalysedInfoCriterium.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
