
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class ODBDataTest {

    public byte[] getData() {
        return new byte[] { 48, 52, 3, 5, 3, 74, -43, 85, 80, 3, 2, 4, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ODBDataImpl prim = new ODBDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ODBGeneralData oDBGeneralData = prim.getODBGeneralData();
        assertFalse(oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertFalse(oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertFalse(oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainmentOGCallsBarred());
        assertFalse(oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertFalse(oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertFalse(oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertFalse(oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertFalse(oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertFalse(oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertFalse(oDBGeneralData.getRegistrationInternationalCFBarred());

        ODBHPLMNData odbHplmnData = prim.getOdbHplmnData();

        assertFalse(odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        ODBDataImpl prim = new ODBDataImpl(oDBGeneralData, odbHplmnData, extensionContainer);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
