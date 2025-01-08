package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Trace is from Brazil Operator
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForLCSResponseTest {
    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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
        // The trace is from Brazilian operator
        return new byte[] { 0x30, 0x14, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x16, 0x28, (byte) 0x81, 0x00,
                0x70, (byte) 0xa1, 0x07, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x00 };
    }

    public byte[] getEncodedDataFull() {
        return new byte[] { 48, 89, -96, 9, -127, 7, -111, 85, 22, 40, -127, 0, 112, -95, 7, 4, 5, -111, 85, 22, 9, 0, -94, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -125, 5, 11, 12, 13, 14, 15, -124, 5, 21, 22, 23, 24, 25, -123, 5, 31, 32, 33,
                34, 35, -122, 5, 41, 42, 43, 44, 45 };
    }

    public byte[] getEncodedDataLoadTest1() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x07, (byte) 0xa0, 0x0a,
                (byte) 0x80, 0x08, 0x47, 0x08, 0x72, (byte) 0x89, (byte) 0x83, (byte) 0x83,
                0x09, (byte) 0xf7, (byte) 0xa1, (byte) 0x81, (byte) 0xd0, 0x04, 0x07, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x51, (byte) 0xa3, 0x09,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                0x52, (byte) 0x84, 0x02, 0x03, (byte) 0xf0, (byte) 0x85, 0x02, 0x03,
                (byte) 0xf8, (byte) 0x86, 0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30,
                0x33, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33,
                0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x88, 0x29, 0x61, 0x61, 0x61, 0x33, 0x30,
                0x30, 0x30, 0x2e, 0x61, 0x61, 0x61, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x89, 0x2c, 0x6d, 0x6d,
                0x65, 0x2e, 0x32, 0x30, 0x2e, 0x6d, 0x61, 0x67,
                0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x8a, 0x21, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x83, 0x05, 0x04,
                0x5a, 0x03, 0x78, 0x05, (byte) 0x84, 0x05, 0x04, 0x0a,
                0x00, 0x00, 0x0e, (byte) 0x85, 0x05, 0x04, 0x0a, 0x00,
                0x00, 0x12, (byte) 0x86, 0x11, 0x50, 0x5a, 0x00, 0x00,
                0x00, 0x00, 0x02, 0x41, 0x04, 0x00, 0x00, 0x00,
                0x03, 0x2a, 0x05, 0x78, 0x5b
        };
    }

    public byte[] getEncodedGSNAddress1() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    public byte[] getEncodedGSNAddress2() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    public byte[] getEncodedGSNAddress3() {
        return new byte[] { 31, 32, 33, 34, 35 };
    }

    public byte[] getEncodedGSNAddress4() {
        return new byte[] { 41, 42, 43, 44, 45 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SendRoutingInfoForLCSResponseImpl sriLcsResp = new SendRoutingInfoForLCSResponseImpl();
        sriLcsResp.decodeAll(asn);

        SubscriberIdentity subsIdent = sriLcsResp.getTargetMS();
        assertNotNull(subsIdent);

        IMSI imsi = subsIdent.getIMSI();
        ISDNAddressString msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "556182180007");

        LCSLocationInfo lcsLocInfo = sriLcsResp.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        ISDNAddressString networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "55619000");

        assertNull(sriLcsResp.getExtensionContainer());
        assertNull(sriLcsResp.getVgmlcAddress());
        assertNull(sriLcsResp.getHGmlcAddress());
        assertNull(sriLcsResp.getPprAddress());
        assertNull(sriLcsResp.getAdditionalVGmlcAddress());

        data = getEncodedDataFull();

        asn = new AsnInputStream(data);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        sriLcsResp = new SendRoutingInfoForLCSResponseImpl();
        sriLcsResp.decodeAll(asn);

        subsIdent = sriLcsResp.getTargetMS();
        assertNotNull(subsIdent);

        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();

        assertNotNull(msisdn);
        assertNull(imsi);

        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "556182180007");

        lcsLocInfo = sriLcsResp.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);

        networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "55619000");

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(sriLcsResp.getExtensionContainer()));
        assertTrue(Arrays.equals(sriLcsResp.getVgmlcAddress().getData(), getEncodedGSNAddress1()));
        assertTrue(Arrays.equals(sriLcsResp.getHGmlcAddress().getData(), getEncodedGSNAddress2()));
        assertTrue(Arrays.equals(sriLcsResp.getPprAddress().getData(), getEncodedGSNAddress3()));
        assertTrue(Arrays.equals(sriLcsResp.getAdditionalVGmlcAddress().getData(), getEncodedGSNAddress4()));

        // data from MAP load LSM test
        data = getEncodedDataLoadTest1();

        asn = new AsnInputStream(data);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        sriLcsResp = new SendRoutingInfoForLCSResponseImpl();
        sriLcsResp.decodeAll(asn);

        // Wireshark sample
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //                localValue: sendRoutingInfoForLCS (85)
        //            targetMS: imsi (0)
        //                IMSI: 748027983838907
        //                [Association IMSI: 748027983838907]
        //            lcsLocationInfo
        //                networkNode-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //                        Country Code: Germany (Federal Republic of) (49)
        //                additional-Number: sgsn-Number (1)
        //                    sgsn-Number: 91947101640052
        //                        1... .... = Extension: No Extension
        //                        .001 .... = Nature of number: International Number (0x1)
        //                        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                        E.164 number (MSISDN): 491710460025
        //                            Country Code: Germany (Federal Republic of) (49)
        //                Padding: 3
        //                supportedLCS-CapabilitySets: f0
        //                    1... .... = lcsCapabilitySet1: True
        //                    .1.. .... = lcsCapabilitySet2: True
        //                    ..1. .... = lcsCapabilitySet3: True
        //                    ...1 .... = lcsCapabilitySet4: True
        //                    .... 0... = lcsCapabilitySet5: False
        //                Padding: 3
        //                additional-LCS-CapabilitySets: f8
        //                    1... .... = lcsCapabilitySet1: True
        //                    .1.. .... = lcsCapabilitySet2: True
        //                    ..1. .... = lcsCapabilitySet3: True
        //                    ...1 .... = lcsCapabilitySet4: True
        //                    .... 1... = lcsCapabilitySet5: True
        //                mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
        //                aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
        //                sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
        //                sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
        //            v-gmlc-Address: 045a037805
        //                GSN-Address IPv4: 90.3.120.5
        //            h-gmlc-Address: 040a00000e
        //                GSN-Address IPv4: 10.0.0.14
        //            ppr-Address: 040a000012
        //                GSN-Address IPv4: 10.0.0.18
        //            additional-v-gmlc-Address: 505a00000000024104000000032a05785b
        //                GSN Address IPv6: 5a00:0:2:4104:0:3:2a05:785b
        subsIdent = sriLcsResp.getTargetMS();
        assertNotNull(subsIdent);
        imsi = subsIdent.getIMSI();
        msisdn = subsIdent.getMSISDN();
        assertNull(msisdn);
        assertNotNull(imsi);
        lcsLocInfo = sriLcsResp.getLCSLocationInfo();
        assertNotNull(lcsLocInfo);
        networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
        assertNotNull(networkNodeNumber);
        assertEquals(networkNodeNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(networkNodeNumber.getAddress(), "491710460015");
        AdditionalNumber additionalNumber = sriLcsResp.getLCSLocationInfo().getAdditionalNumber();
        assertNotNull(additionalNumber);
        assertFalse(additionalNumber.getSGSNNumber().isExtension());
        assertEquals(additionalNumber.getSGSNNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(additionalNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(additionalNumber.getSGSNNumber().getAddress(), "491710460025");
        SupportedLCSCapabilitySets supportedLCSCapabilitySets = sriLcsResp.getLCSLocationInfo().getSupportedLCSCapabilitySets();
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease4());
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease5());
        assertTrue(supportedLCSCapabilitySets.getCapabilitySetRelease6());
        assertFalse(supportedLCSCapabilitySets.getCapabilitySetRelease7());
        SupportedLCSCapabilitySets additionalLcsCapabilitySets = sriLcsResp.getLCSLocationInfo().getAdditionalLCSCapabilitySets();
        assertTrue(additionalLcsCapabilitySets.getCapabilitySetRelease98_99());
        assertTrue(additionalLcsCapabilitySets.getCapabilitySetRelease4());
        assertTrue(additionalLcsCapabilitySets.getCapabilitySetRelease5());
        assertTrue(additionalLcsCapabilitySets.getCapabilitySetRelease6());
        assertTrue(additionalLcsCapabilitySets.getCapabilitySetRelease7());
        assertNull(sriLcsResp.getExtensionContainer());
        DiameterIdentity mmeName = sriLcsResp.getLCSLocationInfo().getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity aaaServerName = sriLcsResp.getLCSLocationInfo().getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnName = sriLcsResp.getLCSLocationInfo().getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnRealm = sriLcsResp.getLCSLocationInfo().getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        GSNAddress vGmlcAddress = sriLcsResp.getVgmlcAddress();
        assertEquals(vGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(vGmlcAddress.getGSNAddressData(), new byte[] { 0x5a, 0x03, 0x78, 5 });
        GSNAddress hGmlcAddress = sriLcsResp.getHGmlcAddress();
        assertEquals(hGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(hGmlcAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        GSNAddress pprAddress = sriLcsResp.getPprAddress();
        assertEquals(pprAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(pprAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x12 });
        GSNAddress addVGmlcAddress = sriLcsResp.getAdditionalVGmlcAddress();
        assertEquals(addVGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv6);
        assertEquals(addVGmlcAddress.getGSNAddressData(), new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getEncodedData();

        ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "556182180007");
        SubscriberIdentity subsIdent = new SubscriberIdentityImpl(msisdn);

        ISDNAddressString networkNodeNumber = this.MAPParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "55619000");

        LCSLocationInfo lcsLocInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, false, null, null, null, null, null, null, null);

        SendRoutingInfoForLCSResponseImpl sriLcsResp = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo, null, null, null,
                null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        sriLcsResp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        data = getEncodedDataFull();

        GSNAddressImpl vGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress1());
        GSNAddressImpl hGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress2());
        GSNAddressImpl pprAddress = new GSNAddressImpl(getEncodedGSNAddress3());
        GSNAddressImpl additionalVGmlcAddress = new GSNAddressImpl(getEncodedGSNAddress4());

        sriLcsResp = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo,
                MAPExtensionContainerTest.GetTestExtensionContainer(), vGmlcAddress, hGmlcAddress, pprAddress,
                additionalVGmlcAddress);

        asnOS = new AsnOutputStream();
        sriLcsResp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        // data from MAP load LSM test
        data = getEncodedDataLoadTest1();

        // Wireshark sample
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //                localValue: sendRoutingInfoForLCS (85)
        //            targetMS: imsi (0)
        //                IMSI: 748027983838907
        //                [Association IMSI: 748027983838907]
        //            lcsLocationInfo
        //                networkNode-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //                        Country Code: Germany (Federal Republic of) (49)
        //                additional-Number: sgsn-Number (1)
        //                    sgsn-Number: 91947101640052
        //                        1... .... = Extension: No Extension
        //                        .001 .... = Nature of number: International Number (0x1)
        //                        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                        E.164 number (MSISDN): 491710460025
        //                            Country Code: Germany (Federal Republic of) (49)
        //                Padding: 3
        //                supportedLCS-CapabilitySets: f0
        //                    1... .... = lcsCapabilitySet1: True
        //                    .1.. .... = lcsCapabilitySet2: True
        //                    ..1. .... = lcsCapabilitySet3: True
        //                    ...1 .... = lcsCapabilitySet4: True
        //                    .... 0... = lcsCapabilitySet5: False
        //                Padding: 3
        //                additional-LCS-CapabilitySets: f8
        //                    1... .... = lcsCapabilitySet1: True
        //                    .1.. .... = lcsCapabilitySet2: True
        //                    ..1. .... = lcsCapabilitySet3: True
        //                    ...1 .... = lcsCapabilitySet4: True
        //                    .... 1... = lcsCapabilitySet5: True
        //                mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
        //                aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
        //                sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
        //                sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
        //            v-gmlc-Address: 045a037805
        //                GSN-Address IPv4: 90.3.120.5
        //            h-gmlc-Address: 040a00000e
        //                GSN-Address IPv4: 10.0.0.14
        //            ppr-Address: 040a000012
        //                GSN-Address IPv4: 10.0.0.18
        //            additional-v-gmlc-Address: 505a00000000024104000000032a05785b
        //                GSN Address IPv6: 5a00:0:2:4104:0:3:2a05:785b
        IMSI imsi = new IMSIImpl("748027983838907");
        subsIdent = new SubscriberIdentityImpl(imsi);

        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "491710460015");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "491710460025");
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
        boolean gprsNodeIndicator = false;
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
        lcsLocInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, gprsNodeIndicator, additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);

        vGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x5a, 0x03, 0x78, 5 });
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        pprAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x12 });
        additionalVGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv6, new byte[] { 0x5a, 0, 0, 0, 0, 2, 65, 4, 0, 0, 0, 3, 42, 5, 120, 91 });

        sriLcsResp = new SendRoutingInfoForLCSResponseImpl(subsIdent, lcsLocInfo, null,
                vGmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);

        asnOS = new AsnOutputStream();
        sriLcsResp.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }
}
