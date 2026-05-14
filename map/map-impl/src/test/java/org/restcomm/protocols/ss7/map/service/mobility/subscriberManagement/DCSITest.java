package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com">Fernando Mendioroz</a>
 */
public class DCSITest {

    public byte[] getData() {
        return new byte[] { 48, 35, -96, 26, 48, 24, 4, 7, -111, -108, 113, 65, -121, 64, 35, 2, 1, 7, 4, 7, -111, -108,
                113, 1, 100, 0, -110, 10, 1, 0, -127, 1, 2, -125, 0, -124, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        DCSIImpl prim = new DCSIImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // Wireshark sample
        /*
         *      d-CSI
         *         dp-AnalysedInfoCriteriaList: 1 item
         *             DP-AnalysedInfoCriterium
         *                 dialledNumber: 91947141874023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491714780432
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         */
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = prim.getDPAnalysedInfoCriteriaList();
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertEquals(dialledNumber.getAddress(), "491714780432");
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(gsmSCFAddressDp.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(prim.getExtensionContainer());
        assertEquals(prim.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(prim.getCsiActive());
        assertTrue(prim.getNotificationToCSE());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        long serviceKey = 7L;
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
        DefaultCallHandling defaultCallHandling = DefaultCallHandling.continueCall;
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<>();
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
        Integer camelCapabilityHandling = 2;
        boolean notificationToCSE = true;
        boolean csiActive = true;
        DCSIImpl prim = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
