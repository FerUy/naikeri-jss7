package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import junit.framework.Assert;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.sms.InformServiceCentreRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.MWStatusImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class InformServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 4, 3, 2, 2, 64 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 61, 4, 6, -111, 17, 33, 34, 51, -13, 3, 2, 2, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 2,
                2, 2, 43, -128, 2, 1, -68 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MWStatus mwStatus = isc.getMwStatus();
        assertNotNull(mwStatus);
        assertFalse(mwStatus.getScAddressNotIncluded());
        assertTrue(mwStatus.getMnrfSet());
        assertFalse(mwStatus.getMcefSet());
        assertFalse(mwStatus.getMnrgSet());
        assertFalse(mwStatus.getMnr5gSet());
        assertFalse(mwStatus.getMnr5gn3gSet());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        isc = new InformServiceCentreRequestImpl();
        isc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        MAPExtensionContainer extensionContainer = isc.getExtensionContainer();
        ISDNAddressString storedMSISDN = isc.getStoredMSISDN();
        mwStatus = isc.getMwStatus();
        int absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
        int additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();
        Integer smsf3gppAbsentSubscriberDiagnosticSM = isc.getSmsf3gppAbsentSubscriberDiagnosticSM();
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = isc.getSmsfNon3gppAbsentSubscriberDiagnosticSM();

        Assert.assertNotNull(storedMSISDN);
        Assert.assertEquals(AddressNature.international_number, storedMSISDN.getAddressNature());
        Assert.assertEquals(NumberingPlan.ISDN, storedMSISDN.getNumberingPlan());
        Assert.assertEquals("111222333", storedMSISDN.getAddress());
        Assert.assertNotNull(mwStatus);
        Assert.assertFalse(mwStatus.getScAddressNotIncluded());
        Assert.assertTrue(mwStatus.getMnrfSet());
        Assert.assertFalse(mwStatus.getMcefSet());
        Assert.assertTrue(mwStatus.getMnrgSet());
        Assert.assertFalse(mwStatus.getMnr5gSet());
        Assert.assertFalse(mwStatus.getMnr5gn3gSet());
        Assert.assertEquals(555, absentSubscriberDiagnosticSM);
        Assert.assertEquals(444, additionalAbsentSubscriberDiagnosticSM);
        Assert.assertNull(smsf3gppAbsentSubscriberDiagnosticSM);
        Assert.assertNull(smsfNon3gppAbsentSubscriberDiagnosticSM);
        Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        MWStatus mwStatus = new MWStatusImpl(false, true, false, false, false, false);
        InformServiceCentreRequestImpl isc = new InformServiceCentreRequestImpl(null, mwStatus, null, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        isc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "111222333");
        mwStatus = new MWStatusImpl(false, true, false, true, false, false);
        Integer absentSubscriberDiagnosticSM = 555;
        Integer additionalAbsentSubscriberDiagnosticSM = 444;
        Integer smsf3gppAbsentSubscriberDiagnosticSM = null;
        Integer smsfNon3gppAbsentSubscriberDiagnosticSM = null;
        isc = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM, smsf3gppAbsentSubscriberDiagnosticSM,
                smsfNon3gppAbsentSubscriberDiagnosticSM);

        asnOS.reset();
        isc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
