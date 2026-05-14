package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSLocationInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class DeferredmtlrDataTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 18, 3, 2, 3, 80, -128, 1, 4, -95, 9, 4, 7, -111, 51, 0, 68, 0, 85, 0
        };
    }

    public byte[] getEncodedDataFromLSMLoadTest() {
        return new byte[] {
                48, -127, -30, 3, 2, 3, 16, -128, 1, 4, -95, -127, -40, 4, 7, -111, -108, 113, 1, 100,
                0, 81, -128, 4, 114, 2, -21, 55, -126, 0, -93, 9, -127, 7, -111, -108, 113, 1, 100, 0,
                82, -124, 2, 3, -16, -123, 2, 3, -8, -122, 54, 109, 109, 101, 99, 48, 51, 46, 109, 109,
                101, 103, 105, 51, 48, 48, 48, 46, 109, 109, 101, 46, 101, 112, 99, 46, 109, 110, 99, 48,
                48, 50, 46, 109, 99, 99, 55, 52, 56, 46, 51, 103, 112, 112, 110, 101, 116, 119, 111, 114,
                107, 46, 111, 114, 103, -120, 41, 97, 97, 97, 51, 48, 48, 48, 46, 97, 97, 97, 46, 109, 110,
                99, 48, 48, 50, 46, 109, 99, 99, 55, 52, 56, 46, 51, 103, 112, 112, 110, 101, 116, 119, 111,
                114, 107, 46, 111, 114, 103, -119, 44, 109, 109, 101, 46, 50, 48, 46, 109, 97, 103, 46, 101,
                112, 99, 46, 109, 110, 99, 48, 48, 49, 46, 109, 99, 99, 55, 52, 56, 46, 51, 103, 112, 112,
                110, 101, 116, 119, 111, 114, 107, 46, 111, 114, 103, -118, 33, 101, 112, 99, 46, 109, 110,
                99, 48, 48, 49, 46, 109, 99, 99, 55, 52, 56, 46, 51, 103, 112, 112, 110, 101, 116, 119, 111,
                114, 107, 46, 111, 114, 103
        };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        // test 1
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        DeferredmtlrDataImpl deferredmtlrData = new DeferredmtlrDataImpl();
        deferredmtlrData.decodeAll(asn);

        assertFalse(deferredmtlrData.getDeferredLocationEventType().getMsAvailable());
        assertTrue(deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea());
        assertTrue(deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getPeriodicLDR());
        assertEquals(deferredmtlrData.getTerminationCause(), TerminationCause.mtlrRestart);
        assertEquals(deferredmtlrData.getLCSLocationInfo().getNetworkNodeNumber().getAddress(), "330044005500");

        // Test 2 from LSM load test
        data = getEncodedDataFromLSMLoadTest();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        deferredmtlrData = new DeferredmtlrDataImpl();
        deferredmtlrData.decodeAll(asn);
        // Wireshark example
        /*
         * deferredmt-lrData
         *     Padding: 3
         *     deferredLocationEventType: 10
         *         0... .... = msAvailable: False
         *         .0.. .... = enteringIntoArea: False
         *         ..0. .... = leavingFromArea: False
         *         ...1 .... = beingInsideArea: True
         *         .... 0... = periodicLDR: False
         *     terminationCause: mt-lrRestart (4)
         *     lcsLocationInfo
         *         networkNode-Number: 91947101640051
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 491710460015
         *         lmsi: 7202eb37
         *         gprsNodeIndicator
         *         additional-Number: sgsn-Number (1)
         *             sgsn-Number: 91947101640052
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460025
         *         Padding: 3
         *         supportedLCS-CapabilitySets: f0
         *             1... .... = lcsCapabilitySet1: True
         *             .1.. .... = lcsCapabilitySet2: True
         *             ..1. .... = lcsCapabilitySet3: True
         *             ...1 .... = lcsCapabilitySet4: True
         *             .... 0... = lcsCapabilitySet5: False
         *         Padding: 3
         *         additional-LCS-CapabilitySets: f8
         *             1... .... = lcsCapabilitySet1: True
         *             .1.. .... = lcsCapabilitySet2: True
         *             ..1. .... = lcsCapabilitySet3: True
         *             ...1 .... = lcsCapabilitySet4: True
         *             .... 1... = lcsCapabilitySet5: True
         *         mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *         sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *         sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         */
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getMsAvailable());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea());
        assertTrue(deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getPeriodicLDR());
        assertEquals(deferredmtlrData.getTerminationCause(), TerminationCause.mtlrRestart);
        assertTrue(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
        assertFalse(deferredmtlrData.getLCSLocationInfo().getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease6());
        assertTrue(deferredmtlrData.getLCSLocationInfo().getAdditionalLCSCapabilitySets().getCapabilitySetRelease7());
        DiameterIdentity mmeName = deferredmtlrData.getLCSLocationInfo().getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity aaaServerName = deferredmtlrData.getLCSLocationInfo().getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnName = deferredmtlrData.getLCSLocationInfo().getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnRealm = deferredmtlrData.getLCSLocationInfo().getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        // test 1
        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(false, true, false, true, false);
        ISDNAddressStringImpl networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "330044005500");
        LCSLocationInfoImpl lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null,
                null, null, null, null);

        DeferredmtlrDataImpl deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, TerminationCause.mtlrRestart,
                lcsLocationInfo);

        AsnOutputStream asnOS = new AsnOutputStream();
        deferredmtlrData.encodeAll(asnOS);

        byte[] data = getEncodedData();
        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // Test 2 from LSM load test
        // Wireshark example
        /*
         * deferredmt-lrData
         *     Padding: 3
         *     deferredLocationEventType: 10
         *         0... .... = msAvailable: False
         *         .0.. .... = enteringIntoArea: False
         *         ..0. .... = leavingFromArea: False
         *         ...1 .... = beingInsideArea: True
         *         .... 0... = periodicLDR: False
         *     terminationCause: mt-lrRestart (4)
         *     lcsLocationInfo
         *         networkNode-Number: 91947101640051
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 491710460015
         *         lmsi: 7202eb37
         *         gprsNodeIndicator
         *         additional-Number: sgsn-Number (1)
         *             sgsn-Number: 91947101640052
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460025
         *         Padding: 3
         *         supportedLCS-CapabilitySets: f0
         *             1... .... = lcsCapabilitySet1: True
         *             .1.. .... = lcsCapabilitySet2: True
         *             ..1. .... = lcsCapabilitySet3: True
         *             ...1 .... = lcsCapabilitySet4: True
         *             .... 0... = lcsCapabilitySet5: False
         *         Padding: 3
         *         additional-LCS-CapabilitySets: f8
         *             1... .... = lcsCapabilitySet1: True
         *             .1.. .... = lcsCapabilitySet2: True
         *             ..1. .... = lcsCapabilitySet3: True
         *             ...1 .... = lcsCapabilitySet4: True
         *             .... 1... = lcsCapabilitySet5: True
         *         mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *         aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *         sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *         sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         */
        deferredLocationEventType = new DeferredLocationEventTypeImpl(false, false, false, true, false);
        TerminationCause terminationCause = TerminationCause.mtlrRestart;
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "491710460015");
        LMSI lmsi = new LMSIImpl(new byte[] {0x72, 0x02, (byte) 0xeb, 0x37});
        boolean gprsNodeIndicator = true;
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460025");
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
        boolean lcsCapabilitySetRelease98_99 = true;
        boolean lcsCapabilitySetRelease4 = true;
        boolean lcsCapabilitySetRelease5 = true;
        boolean lcsCapabilitySetRelease6 = true;
        boolean lcsCapabilitySetRelease7 = false;
        SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
        lcsCapabilitySetRelease7 = true;
        SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity sgsnName = new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        DiameterIdentity sgsnRealm = new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

        deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);

        asnOS = new AsnOutputStream();
        deferredmtlrData.encodeAll(asnOS);

        data = getEncodedDataFromLSMLoadTest();
        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));


    }
}
