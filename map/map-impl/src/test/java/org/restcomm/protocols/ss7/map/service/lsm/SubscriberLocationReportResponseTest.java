
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
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
public class SubscriberLocationReportResponseTest {

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
        return new byte[] { 48, 46, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 3, -111, 17, 17 };
    }

    public byte[] getEncodedDataLSMLoadTestNaESRK() {
        return new byte[] { 0x30, 0x2d,
                (byte) 0x80, 0x06, (byte) 0x91, 0x29, (byte) 0x98, 0x72, 0x07, (byte) 0x90,
                (byte) 0x82, 0x05, 0x04, 0x0a, 0x00, 0x00, 0x0e, (byte) 0x83,
                0x00, (byte) 0xa4, 0x1a, (byte) 0x80, 0x00, (byte) 0xa1, 0x16, 0x30,
                0x0a, (byte) 0x80, 0x03, 0x47, (byte) 0xf8, 0x10, (byte) 0x81, 0x01,
                0x01, (byte) 0x82, 0x00, 0x30, 0x08, (byte) 0x80, 0x03, 0x47,
                (byte) 0xf8, 0x70, (byte) 0x81, 0x01, 0x00
        };
    }

    public byte[] getEncodedDataLSMLoadTestNaESRD() {
        return new byte[] { 0x30, 0x2d,
                (byte) 0x81, 0x06, (byte) 0x91, 0x21, 0x01, 0x01, 0x01, 0x57,
                (byte) 0x82, 0x05, 0x04, 0x0a, 0x00, 0x00, 0x0e, (byte) 0x83,
                0x00, (byte) 0xa4, 0x1a, (byte) 0x80, 0x00, (byte) 0xa1, 0x16, 0x30,
                0x0a, (byte) 0x80, 0x03, 0x47, (byte) 0xf8, 0x10, (byte) 0x81, 0x01,
                0x01, (byte) 0x82, 0x00, 0x30, 0x08, (byte) 0x80, 0x03, 0x47,
                (byte) 0xf8, 0x70, (byte) 0x81, 0x01, 0x00
        };
    }

    public byte[] getEncodedDataLSMLoadTestLcsRefNum() {
        return new byte[] { 0x30, 0x30,
                (byte) 0x80, 0x06, (byte) 0x91, 0x29, (byte) 0x98, 0x72, 0x07, (byte) 0x90,
                (byte) 0x82, 0x05, 0x04, 0x0a, 0x00, 0x00, 0x0e, (byte) 0x83,
                0x00, (byte) 0xa4, 0x1a, (byte) 0x80, 0x00, (byte) 0xa1, 0x16, 0x30,
                0x0a,(byte)  0x80, 0x03, 0x47, (byte) 0xf8, 0x10, (byte) 0x81, 0x01,
                0x01, (byte) 0x82, 0x00, 0x30, 0x08, (byte) 0x80, 0x03, 0x47,
                (byte) 0xf8, 0x70, (byte) 0x81, 0x01, 0x00, (byte) 0x85, 0x01, 0x49
        };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportResponseImpl slr0 = new SubscriberLocationReportResponseImpl();
        slr0.decodeAll(asn);

        assertEquals(slr0.getNaESRD().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(slr0.getNaESRD().getAddressNature(), AddressNature.international_number);
        assertEquals(slr0.getNaESRD().getAddress(), "1111");
        assertNull(slr0.getNaESRK());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(slr0.getExtensionContainer()));
        assertNull(slr0.getHGMLCAddress());
        assertFalse(slr0.getMolrShortCircuitIndicator());
        assertNull(slr0.getReportingPLMNList());
        assertNull(slr0.getLcsReferenceNumber());

        // test 2 from LSM load test (with NaESRK)
        data = getEncodedDataLSMLoadTestNaESRK();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportResponseImpl slr1 = new SubscriberLocationReportResponseImpl();
        slr1.decodeAll(asn);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: subscriberLocationReport (86)
         *             na-ESRK: 912998720790
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 9289277009
         *             h-gmlc-Address: 040a00000e
         *                 GSN-Address IPv4: 10.0.0.14
         *             mo-lrShortCircuitIndicator
         *             reportingPLMNList
         *                 plmn-ListPrioritized
         *                 plmn-List: 2 items
         *                     ReportingPLMN
         *                         plmn-Id: 47f810
         *                         ran-Technology: umts (1)
         *                         ran-PeriodicLocationSupport
         *                     ReportingPLMN
         *                         plmn-Id: 47f870
         *                         ran-Technology: gsm (0)
         */
        assertNull(slr1.getNaESRD());
        assertEquals(slr1.getNaESRK().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(slr1.getNaESRK().getAddressNature(), AddressNature.international_number);
        assertEquals(slr1.getNaESRK().getAddress(), "9289277009");
        assertNull(slr1.getExtensionContainer());
        assertEquals(slr1.getHGMLCAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(slr1.getHGMLCAddress().getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        assertTrue(slr1.getMolrShortCircuitIndicator());
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMnc(), 1);
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(0).getRanTechnology(), RANTechnology.umts);
        assertTrue(slr1.getReportingPLMNList().getPlmnList().get(0).getRanPeriodicLocationSupport());
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(1).getPlmnId().getMnc(), 7);
        assertEquals(slr1.getReportingPLMNList().getPlmnList().get(1).getRanTechnology(), RANTechnology.gsm);
        assertFalse(slr1.getReportingPLMNList().getPlmnList().get(1).getRanPeriodicLocationSupport());
        assertNull(slr1.getLcsReferenceNumber());

        // test 3 from LSM load test (with NaESRD)
        data = getEncodedDataLSMLoadTestNaESRD();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportResponseImpl slr2 = new SubscriberLocationReportResponseImpl();
        slr2.decodeAll(asn);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: subscriberLocationReport (86)
         *             na-ESRD: 912101010157
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 1210101075
         *             h-gmlc-Address: 040a00000e
         *                 GSN-Address IPv4: 10.0.0.14
         *             mo-lrShortCircuitIndicator
         *             reportingPLMNList
         *                 plmn-ListPrioritized
         *                 plmn-List: 2 items
         *                     ReportingPLMN
         *                         plmn-Id: 47f810
         *                         ran-Technology: umts (1)
         *                         ran-PeriodicLocationSupport
         *                     ReportingPLMN
         *                         plmn-Id: 47f870
         *                         ran-Technology: gsm (0)
         */
        assertEquals(slr2.getNaESRD().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(slr2.getNaESRD().getAddressNature(), AddressNature.international_number);
        assertEquals(slr2.getNaESRD().getAddress(), "1210101075");
        assertNull(slr2.getNaESRK());
        assertNull(slr2.getExtensionContainer());
        assertEquals(slr2.getHGMLCAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(slr2.getHGMLCAddress().getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        assertTrue(slr2.getMolrShortCircuitIndicator());
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMnc(), 1);
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(0).getRanTechnology(), RANTechnology.umts);
        assertTrue(slr2.getReportingPLMNList().getPlmnList().get(0).getRanPeriodicLocationSupport());
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(1).getPlmnId().getMnc(), 7);
        assertEquals(slr2.getReportingPLMNList().getPlmnList().get(1).getRanTechnology(), RANTechnology.gsm);
        assertFalse(slr2.getReportingPLMNList().getPlmnList().get(1).getRanPeriodicLocationSupport());
        assertNull(slr2.getLcsReferenceNumber());

        // test 4 from LSM load test (with LCSReferenceNumber)
        data = getEncodedDataLSMLoadTestLcsRefNum();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportResponseImpl slr3 = new SubscriberLocationReportResponseImpl();
        slr3.decodeAll(asn);
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: subscriberLocationReport (86)
         *             na-ESRK: 912998720790
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 9289277009
         *             h-gmlc-Address: 040a00000e
         *                 GSN-Address IPv4: 10.0.0.14
         *             mo-lrShortCircuitIndicator
         *             reportingPLMNList
         *                 plmn-ListPrioritized
         *                 plmn-List: 2 items
         *                     ReportingPLMN
         *                         plmn-Id: 47f810
         *                         ran-Technology: umts (1)
         *                         ran-PeriodicLocationSupport
         *                     ReportingPLMN
         *                         plmn-Id: 47f870
         *                         ran-Technology: gsm (0)
         *             lcs-ReferenceNumber: 49
         */
        assertNull(slr3.getNaESRD());
        assertEquals(slr3.getNaESRK().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(slr3.getNaESRK().getAddressNature(), AddressNature.international_number);
        assertEquals(slr3.getNaESRK().getAddress(), "9289277009");
        assertNull(slr3.getExtensionContainer());
        assertEquals(slr3.getHGMLCAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(slr3.getHGMLCAddress().getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        assertTrue(slr3.getMolrShortCircuitIndicator());
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMnc(), 1);
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(0).getRanTechnology(), RANTechnology.umts);
        assertTrue(slr3.getReportingPLMNList().getPlmnList().get(0).getRanPeriodicLocationSupport());
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(1).getPlmnId().getMnc(), 7);
        assertEquals(slr3.getReportingPLMNList().getPlmnList().get(1).getRanTechnology(), RANTechnology.gsm);
        assertFalse(slr3.getReportingPLMNList().getPlmnList().get(1).getRanPeriodicLocationSupport());
        assertEquals(slr3.getLcsReferenceNumber().intValue(), 73);

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        ISDNAddressStringImpl naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111");
        GSNAddress hGmlcAddress;
        ReportingPLMNList reportingPLMNList;

        SubscriberLocationReportResponseImpl slr0 = new SubscriberLocationReportResponseImpl(naEsrd, null,
                MAPExtensionContainerTest.GetTestExtensionContainer(), null, false, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        slr0.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 2 from LSM load test (with NaESRK)
        data = getEncodedDataLSMLoadTestNaESRK();

        // Wireshark sample
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //                localValue: subscriberLocationReport (86)
        //            na-ESRK: 912998720790
        //                1... .... = Extension: No Extension
        //                .001 .... = Nature of number: International Number (0x1)
        //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                E.164 number (MSISDN): 9289277009
        //                    Country Code: Pakistan (Islamic Republic of) (92)
        //            h-gmlc-Address: 040a00000e
        //                GSN-Address IPv4: 10.0.0.14
        //            mo-lrShortCircuitIndicator
        //            reportingPLMNList
        //                plmn-ListPrioritized
        //                plmn-List: 2 items
        //                    ReportingPLMN
        //                        plmn-Id: 47f810
        //                        ran-Technology: umts (1)
        //                        ran-PeriodicLocationSupport
        //                    ReportingPLMN
        //                        plmn-Id: 47f870
        //                        ran-Technology: gsm (0)
        ISDNAddressString naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9289277009");
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        // molrShortCircuitIndicator = true;
        ArrayList<ReportingPLMN> reportingPLMNs = getReportingPLMNS();
        boolean plmnListPrioritized = true;
        reportingPLMNList = new ReportingPLMNListImpl(plmnListPrioritized, reportingPLMNs);

        SubscriberLocationReportResponseImpl slr1 = new SubscriberLocationReportResponseImpl(null, naEsrk,
                null, hGmlcAddress, true, reportingPLMNList, null);

        asnOS = new AsnOutputStream();
        slr1.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 3 from LSM load test (with NaESRD)
        data = getEncodedDataLSMLoadTestNaESRD();

        // Wireshark sample
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //                localValue: subscriberLocationReport (86)
        //            na-ESRD: 912101010157
        //                1... .... = Extension: No Extension
        //                .001 .... = Nature of number: International Number (0x1)
        //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                E.164 number (MSISDN): 1210101075
        //            h-gmlc-Address: 040a00000e
        //                GSN-Address IPv4: 10.0.0.14
        //            mo-lrShortCircuitIndicator
        //            reportingPLMNList
        //                plmn-ListPrioritized
        //                plmn-List: 2 items
        //                    ReportingPLMN
        //                        plmn-Id: 47f810
        //                        ran-Technology: umts (1)
        //                        ran-PeriodicLocationSupport
        //                    ReportingPLMN
        //                        plmn-Id: 47f870
        //                        ran-Technology: gsm (0)
        naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1210101075");
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        reportingPLMNs = getReportingPLMNS();
        reportingPLMNList = new ReportingPLMNListImpl(plmnListPrioritized, reportingPLMNs);

        SubscriberLocationReportResponseImpl slr2 = new SubscriberLocationReportResponseImpl(naEsrd, null,
                null, hGmlcAddress, true, reportingPLMNList, null);

        asnOS = new AsnOutputStream();
        slr2.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 4 from LSM load test (with LCSReferenceNumber)
        data = getEncodedDataLSMLoadTestLcsRefNum();

        // Wireshark sample
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //                localValue: subscriberLocationReport (86)
        //            na-ESRK: 912998720790
        //                1... .... = Extension: No Extension
        //                .001 .... = Nature of number: International Number (0x1)
        //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                E.164 number (MSISDN): 9289277009
        //            h-gmlc-Address: 040a00000e
        //                GSN-Address IPv4: 10.0.0.14
        //            mo-lrShortCircuitIndicator
        //            reportingPLMNList
        //                plmn-ListPrioritized
        //                plmn-List: 2 items
        //                    ReportingPLMN
        //                        plmn-Id: 47f810
        //                        ran-Technology: umts (1)
        //                        ran-PeriodicLocationSupport
        //                    ReportingPLMN
        //                        plmn-Id: 47f870
        //                        ran-Technology: gsm (0)
        //            lcs-ReferenceNumber: 49
        naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9289277009");
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        reportingPLMNs = getReportingPLMNS();
        reportingPLMNList = new ReportingPLMNListImpl(plmnListPrioritized, reportingPLMNs);
        int lcsReferenceNumber = 73;

        SubscriberLocationReportResponseImpl slr3 = new SubscriberLocationReportResponseImpl(null, naEsrk,
                null, hGmlcAddress, true, reportingPLMNList, lcsReferenceNumber);

        asnOS = new AsnOutputStream();
        slr3.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }

    private static ArrayList<ReportingPLMN> getReportingPLMNS() {
        ArrayList<ReportingPLMN> reportingPLMNs = new ArrayList<>();
        PlmnId plmnId1 = new PlmnIdImpl(748, 1);
        RANTechnology rat1 = RANTechnology.umts;
        boolean ranPeriodicLocationSupport1 = true;
        PlmnId plmnId2 = new PlmnIdImpl(748, 7);
        RANTechnology rat2 = RANTechnology.gsm;
        boolean ranPeriodicLocationSupport2 = false;
        ReportingPLMN rPlmn1 = new ReportingPLMNImpl(plmnId1, rat1, ranPeriodicLocationSupport1);
        ReportingPLMN rPlmn2 = new ReportingPLMNImpl(plmnId2, rat2, ranPeriodicLocationSupport2);
        reportingPLMNs.add(rPlmn1);
        reportingPLMNs.add(rPlmn2);
        return reportingPLMNs;
    }

}
