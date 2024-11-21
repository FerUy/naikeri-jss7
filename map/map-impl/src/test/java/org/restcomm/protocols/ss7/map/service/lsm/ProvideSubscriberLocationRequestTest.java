package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.Area;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.restcomm.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ProvideSubscriberLocationRequestTest {

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
        return new byte[] { 0x30, 0x41, 0x30, 0x03, (byte) 0x80, 0x01, 0x00, 0x04, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70,
                (byte) 0xa0, 0x1b, (byte) 0x80, 0x01, 0x02, (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x13, (byte) 0x80, 0x01,
                0x0f, (byte) 0x82, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
                (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, (byte) 0x82, 0x08, 0x27, (byte) 0x94, (byte) 0x99, 0x09,
                0x00, 0x00, 0x00, (byte) 0xf7, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00,
                (byte) 0x89, 0x02, 0x01, (byte) 0xfe };
    }

    public byte[] getEncodedData2() {
        return new byte[] { 48, -127, -93, 48, 3, -128, 1, 0, 4, 5, -111, 85, 22, 9, 112, -96, 27, -128, 1, 2, -125, 1, 0, -92,
                19, -128, 1, 15, -126, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101, -127, 0, -126,
                8, 39, -108, -103, 9, 0, 0, 0, -9, -125, 6, -111, 103, 69, 35, 1, -16, -124, 4, 31, 32, 33, 34, -123, 8, 33,
                67, 101, -121, 9, 33, 67, 101, -122, 1, 1, -89, 5, -93, 3, 10, 1, 0, -119, 2, 1, -2, -118, 1, 5, -117, 1, 6,
                -84, 12, -128, 1, 15, -127, 7, 120, 124, 62, -97, -41, -21, 27, -83, 6, -128, 1, 1, -127, 1, 0, -82, 13, -96,
                11, -96, 9, 48, 7, -128, 1, 0, -127, 2, 82, -16, -113, 5, 41, 42, 43, 44, 45, -112, 0, -79, 7, 2, 2, 0, -56, 2,
                1, 100, -78, 9, -95, 7, 48, 5, -128, 3, 51, 52, 53 };
    }

    public byte[] getEncodedDataLoadTestPeriodicLDR() {
        return new byte[] { 0x30, (byte) 0x81,
                (byte) 0xca, 0x30, 0x07, (byte) 0x80, 0x01, 0x03, (byte) 0x81, 0x02,
                0x03, 0x08, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x74, 0x20, 0x10, (byte) 0xa0, 0x3a, (byte) 0x80, 0x01, 0x02,
                (byte) 0xa1, 0x08, (byte) 0x80, 0x06, (byte) 0x91, 0x43, 0x40, 0x44,
                0x65, (byte) 0xf7, (byte) 0x82, 0x04, (byte) 0x91, 0x43, 0x00, 0x21,
                (byte) 0x83, 0x01, 0x01, (byte) 0xa4, 0x0d, (byte) 0x80, 0x01, 0x0f,
                (byte) 0x82, 0x05, (byte) 0xaa, 0x5c, 0x2c, 0x36, 0x02, (byte) 0x83,
                0x01, 0x03, (byte) 0x85, 0x04, 0x03, 0x69, 0x6d, 0x73,
                (byte) 0xa6, 0x0d, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x05, (byte) 0xaa,
                0x5c, 0x2c, 0x36, 0x02, (byte) 0x82, 0x01, 0x03, (byte) 0x81,
                0x00, (byte) 0x82, 0x08, 0x47, 0x08, (byte) 0x92, (byte) 0x84, (byte) 0x89,
                0x06, 0x54, (byte) 0xf6, (byte) 0x84, 0x04, 0x72, 0x02, (byte) 0xe9,
                (byte) 0x8c, (byte) 0x85, 0x08, 0x01, 0x70, 0x01, 0x04, 0x35,
                0x50, 0x31, (byte) 0xf8, (byte) 0x86, 0x01, 0x01, (byte) 0xa7, 0x0f,
                (byte) 0x80, 0x01, 0x0a, (byte) 0x81, 0x00, (byte) 0x82, 0x01, 0x32,
                (byte) 0xa3, 0x03, 0x0a, 0x01, 0x01, (byte) 0x85, 0x00, (byte) 0x89,
                0x02, 0x01, (byte) 0xf6, (byte) 0x8a, 0x01, 0x5c, (byte) 0x8b, 0x01,
                0x43, (byte) 0xac, 0x0a, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x05,
                (byte) 0xaa, 0x5c, 0x2c, 0x36, 0x02, (byte) 0xad, 0x06, (byte) 0x80,
                0x01, 0x01, (byte) 0x81, 0x01, 0x02, (byte) 0x8f, 0x05, 0x04,
                0x0a, 0x00, 0x00, 0x0e, (byte) 0x90, 0x00, (byte) 0xb1, 0x07,
                0x02, 0x01, 0x03, 0x02, 0x02, 0x02, 0x58, (byte) 0xb2,
                0x1a, (byte) 0x80, 0x00, (byte) 0xa1, 0x16, 0x30, 0x0a, (byte) 0x80,
                0x03, 0x47, (byte) 0xf8, 0x10, (byte) 0x81, 0x01, 0x01, (byte) 0x82,
                0x00, 0x30, 0x08, (byte) 0x80, 0x03, 0x47, (byte) 0xf8, 0x70,
                (byte) 0x81, 0x01, 0x00
        };
    }

    public byte[] getEncodedDataLoadTestAreaEventInfo() {
        return new byte[] {0x30, (byte) 0x81,
                (byte) 0xe1, 0x30, 0x07, (byte) 0x80, 0x01, 0x04, (byte) 0x81, 0x02,
                0x03, 0x20, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x74, 0x20, 0x10, (byte) 0xa0, 0x3a, (byte) 0x80, 0x01, 0x01,
                (byte) 0xa1, 0x08, (byte) 0x80, 0x06, (byte) 0x91, 0x43, 0x40, 0x44,
                0x65, (byte) 0xf7, (byte) 0x82, 0x04, (byte) 0x91, 0x43, 0x00, 0x21,
                (byte) 0x83, 0x01, 0x04, (byte) 0xa4, 0x0d, (byte) 0x80, 0x01, 0x0f,
                (byte) 0x82, 0x05, (byte) 0xaa, 0x5c, 0x2c, 0x36, 0x02, (byte) 0x83,
                0x01, 0x03, (byte) 0x85, 0x04, 0x03, 0x69, 0x6d, 0x73,
                (byte) 0xa6, 0x0d, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x05, (byte) 0xaa,
                0x5c, 0x2c, 0x36, 0x02, (byte) 0x82, 0x01, 0x03, (byte) 0x81,
                0x00, (byte) 0x82, 0x08, 0x47, 0x08, 0x72, 0x28, 0x67,
                0x35, 0x27, (byte) 0xf9, (byte) 0x85, 0x08, 0x01, 0x70, 0x31,
                0x75, 0x55, 0x13, 0x78, (byte) 0xf7, (byte) 0x86, 0x01, 0x01,
                (byte) 0xa7, 0x0f, (byte) 0x80, 0x01, 0x0a, (byte) 0x81, 0x00, (byte) 0x82,
                0x01, 0x32, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x01, (byte) 0x85,
                0x00, (byte) 0x89, 0x02, 0x01, (byte) 0xf6, (byte) 0x8a, 0x01, (byte) 0xe9,
                (byte) 0x8b, 0x01, 0x04, (byte) 0xac, 0x0a, (byte) 0x80, 0x01, 0x0f,
                (byte) 0x81, 0x05, (byte) 0xaa, 0x5c, 0x2c, 0x36, 0x02, (byte) 0xad,
                0x06, (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x02, (byte) 0xae,
                0x24, (byte) 0xa0, 0x1c, (byte) 0xa0, 0x1a, 0x30, 0x0a, (byte) 0x80,
                0x01, 0x02, (byte) 0x81, 0x05, 0x47, (byte) 0xf8, 0x10, 0x04,
                (byte) 0xb1, 0x30, 0x0c, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x07,
                0x47, (byte) 0xf8, 0x70, 0x08, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x81,
                0x01, 0x01, (byte) 0x82, 0x01, 0x0a, (byte) 0x8f, 0x05, 0x04,
                0x0a, 0x00, 0x00, 0x0e, (byte) 0x90, 0x00, (byte) 0xb2, 0x1a,
                (byte) 0x80, 0x00, (byte) 0xa1, 0x16, 0x30, 0x0a, (byte) 0x80, 0x03,
                0x47, (byte) 0xf8, 0x10, (byte) 0x81, 0x01, 0x01, (byte) 0x82, 0x00,
                0x30, 0x08, (byte) 0x80, 0x03, 0x47, (byte) 0xf8, 0x70, (byte) 0x81,
                0x01, 0x00
        };
    }


    public byte[] getDataLmsi() {
        return new byte[] { 31, 32, 33, 34 };
    }

    public byte[] getDataHgmlcAddress() {
        return new byte[] { 41, 42, 43, 44, 45 };
    }

    public byte[] getPlmnId() {
        return new byte[] { 51, 52, 53 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {

        // Test from Brazilian operator
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationRequestImpl reqInd = new ProvideSubscriberLocationRequestImpl();
        reqInd.decodeAll(asn);

        LocationType locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.currentLocation);

        ISDNAddressString mlcNumber = reqInd.getMlcNumber();
        assertNotNull(mlcNumber);
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "55619007");

        LCSClientID lcsClientId = reqInd.getLCSClientID();
        assertNotNull(lcsClientId);
        assertEquals(lcsClientId.getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertEquals(lcsClientId.getLCSClientInternalID(), LCSClientInternalID.broadcastService);
        LCSClientName lcsClientName = lcsClientId.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "ndmgapp2ndmgapp2");

        IMSI imsi = reqInd.getIMSI();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "724999900000007");

        assertEquals(reqInd.getLCSPriority(), LCSPriority.normalPriority);

        LCSQoS lcsQoS = reqInd.getLCSQoS();
        assertNotNull(lcsQoS);
        ResponseTime respTime = lcsQoS.getResponseTime();
        assertNotNull(respTime);
        assertEquals(respTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        SupportedGADShapes suppGadShapes = reqInd.getSupportedGADShapes();
        assertNotNull(suppGadShapes);
        assertTrue(suppGadShapes.getEllipsoidArc());
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyEllipsoid());

        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());

        assertFalse(reqInd.getPrivacyOverride());
        assertNull(reqInd.getMSISDN());
        assertNull(reqInd.getLMSI());
        assertNull(reqInd.getIMEI());
        assertNull(reqInd.getExtensionContainer());
        assertNull(reqInd.getLCSReferenceNumber());
        assertNull(reqInd.getLCSServiceTypeID());
        assertNull(reqInd.getLCSCodeword());
        assertNull(reqInd.getLCSPrivacyCheck());
        assertNull(reqInd.getAreaEventInfo());
        assertNull(reqInd.getHGMLCAddress());
        assertFalse(reqInd.getMoLrShortCircuitIndicator());
        assertNull(reqInd.getPeriodicLDRInfo());
        assertNull(reqInd.getReportingPLMNList());

        // Test 2 with fictional data from Sergey
        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        reqInd = new ProvideSubscriberLocationRequestImpl();
        reqInd.decodeAll(asn);

        locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.currentLocation);

        mlcNumber = reqInd.getMlcNumber();
        assertNotNull(mlcNumber);
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "55619007");

        lcsClientId = reqInd.getLCSClientID();
        assertNotNull(lcsClientId);
        assertEquals(lcsClientId.getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertEquals(lcsClientId.getLCSClientInternalID(), LCSClientInternalID.broadcastService);
        lcsClientName = lcsClientId.getLCSClientName();
        assertNotNull(lcsClientName);
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "ndmgapp2ndmgapp2");

        imsi = reqInd.getIMSI();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "724999900000007");

        assertEquals(reqInd.getLCSPriority(), LCSPriority.normalPriority);

        lcsQoS = reqInd.getLCSQoS();
        assertNotNull(lcsQoS);
        respTime = lcsQoS.getResponseTime();
        assertNotNull(respTime);
        assertEquals(respTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        suppGadShapes = reqInd.getSupportedGADShapes();
        assertNotNull(suppGadShapes);
        assertTrue(suppGadShapes.getEllipsoidArc());
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyEllipsoid());

        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());

        assertTrue(reqInd.getPrivacyOverride());
        assertEquals(reqInd.getMSISDN().getAddress(), "765432100");
        assertTrue(Arrays.equals(reqInd.getLMSI().getData(), getDataLmsi()));
        assertEquals(reqInd.getIMEI().getIMEI(), "1234567890123456");
        assertNull(reqInd.getExtensionContainer());
        assertEquals((int) reqInd.getLCSReferenceNumber(), 5);
        assertEquals((int) reqInd.getLCSServiceTypeID(), 6);
        assertEquals(reqInd.getLCSCodeword().getLCSCodewordString().getString(null), "xxyyyzz");
        assertEquals(reqInd.getLCSPrivacyCheck().getCallSessionUnrelated(), PrivacyCheckRelatedAction.allowedWithNotification);
        assertEquals(reqInd.getLCSPrivacyCheck().getCallSessionRelated(), PrivacyCheckRelatedAction.allowedWithoutNotification);

        assertEquals(reqInd.getAreaEventInfo().getAreaDefinition().getAreaList().size(), 1);
        assertEquals(reqInd.getAreaEventInfo().getAreaDefinition().getAreaList().get(0).getAreaIdentification().getMCC(), 250);

        assertTrue(Arrays.equals(reqInd.getHGMLCAddress().getData(), getDataHgmlcAddress()));
        assertTrue(reqInd.getMoLrShortCircuitIndicator());
        assertEquals(reqInd.getPeriodicLDRInfo().getReportingAmount(), 200);
        assertEquals(reqInd.getPeriodicLDRInfo().getReportingInterval(), 100);
        assertTrue(Arrays.equals(reqInd.getReportingPLMNList().getPlmnList().get(0).getPlmnId().getData(), getPlmnId()));

        // Test from MAP load for deferred location with periodic LDR params
        rawData = getEncodedDataLoadTestPeriodicLDR();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        reqInd = new ProvideSubscriberLocationRequestImpl();
        reqInd.decodeAll(asn);
        // Wireshark
        // Component: invoke (1)
        //    invoke
        //        invokeID: 0
        //        opCode: localValue (0)
        //            localValue: provideSubscriberLocation (83)
        //        locationType
        //            locationEstimateType: activateDeferredLocation (3)
        //            Padding: 3
        //            deferredLocationEventType: 08
        //                0... .... = msAvailable: False
        //                .0.. .... = enteringIntoArea: False
        //                ..0. .... = leavingFromArea: False
        //                ...0 .... = beingInsideArea: False
        //                .... 1... = periodicLDR: True
        //        mlc-Number: 91947101742010
        //            1... .... = Extension: No Extension
        //            .001 .... = Nature of number: International Number (0x1)
        //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //            E.164 number (MSISDN): 491710470201
        //        lcs-ClientID
        //            lcsClientType: plmnOperatorServices (2)
        //            lcsClientExternalID
        //                externalAddress: 9143404465f7
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 340444567
        //            lcsClientDialedByMS: 91430021
        //                1... .... = Extension: No Extension
        //                .001 .... = Nature of number: International Number (0x1)
        //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                E.164 number (MSISDN): 340012
        //            lcsClientInternalID: o-andM-HPLMN (1)
        //            lcsClientName
        //                dataCodingScheme: 0f
        //                    0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
        //                    .... 1111 = Language: Language unspecified (15)
        //                nameString: aa5c2c3602
        //                    USSD String: *911#
        //                lcs-FormatIndicator: url (3)
        //            lcsAPN: 03696d73 - ims
        //                APN: ims
        //            lcsRequestorID
        //                dataCodingScheme: 0f
        //                    0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
        //                    .... 1111 = Language: Language unspecified (15)
        //                requestorIDString: aa5c2c3602
        //                    USSD String: *911#
        //                lcs-FormatIndicator: url (3)
        //        privacyOverride
        //        IMSI: 748029489860456
        //        [Association IMSI: 748029489860456]
        //        lmsi: 7202e98c
        //        imei: 01700104355031f8
        //            TBCD digits: 100710405305138
        //        lcs-Priority: 01
        //        lcs-QoS
        //            horizontal-accuracy: 0a
        //            verticalCoordinateRequest
        //            vertical-accuracy: 32
        //            responseTime
        //                responseTimeCategory: delaytolerant (1)
        //            velocityRequest
        //        Padding: 1
        //        supportedGADShapes: f6
        //            1... .... = ellipsoidPoint: True
        //            .1.. .... = ellipsoidPointWithUncertaintyCircle: True
        //            ..1. .... = ellipsoidPointWithUncertaintyEllipse: True
        //            ...1 .... = polygon: True
        //            .... 0... = ellipsoidPointWithAltitude: False
        //            .... .1.. = ellipsoidPointWithAltitudeAndUncertaintyElipsoid: True
        //            .... ..1. = ellipsoidArc: True
        //        lcs-ReferenceNumber: 5c
        //        lcsServiceTypeID: serv67 (67)
        //        lcsCodeword
        //            dataCodingScheme: 0f
        //                0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
        //                .... 1111 = Language: Language unspecified (15)
        //            lcsCodewordString: aa5c2c3602
        //                USSD String: *911#
        //        lcs-PrivacyCheck
        //            callSessionUnrelated: allowedWithNotification (1)
        //            callSessionRelated: allowedIfNoResponse (2)
        //        h-gmlc-Address: 040a00000e
        //            GSN-Address IPv4: 10.0.0.14
        //        mo-lrShortCircuitIndicator
        //        periodicLDRInfo
        //            reportingAmount: 3
        //            reportingInterval: 600
        //        reportingPLMNList
        //            plmn-ListPrioritized
        //            plmn-List: 2 items
        //                ReportingPLMN
        //                    plmn-Id: 47f810
        //                    ran-Technology: umts (1)
        //                    ran-PeriodicLocationSupport
        //                ReportingPLMN
        //                    plmn-Id: 47f870
        //                    ran-Technology: gsm (0)
        // locationType
        locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.activateDeferredLocation);
        DeferredLocationEventType deferredLocationEventType = locationType.getDeferredLocationEventType();
        assertFalse(deferredLocationEventType.getMsAvailable());
        assertFalse(deferredLocationEventType.getEnteringIntoArea());
        assertFalse(deferredLocationEventType.getLeavingFromArea());
        assertFalse(deferredLocationEventType.getBeingInsideArea());
        assertTrue(deferredLocationEventType.getPeriodicLDR());
        // mlc-Number
        mlcNumber = reqInd.getMlcNumber();
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "491710470201");
        // lcs-ClientID
        lcsClientId = reqInd.getLCSClientID();
        LCSClientType lcsClientType = lcsClientId.getLCSClientType();
        assertEquals(lcsClientType, LCSClientType.plmnOperatorServices);
        LCSClientExternalID lcsClientExternalID = lcsClientId.getLCSClientExternalID();
        ISDNAddressString externalAddress = lcsClientExternalID.getExternalAddress();
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(externalAddress.getAddress(), "340444567");
        AddressString lcsClientDialedByMS = lcsClientId.getLCSClientDialedByMS();
        assertEquals(lcsClientDialedByMS.getAddressNature(), AddressNature.international_number);
        assertEquals(lcsClientDialedByMS.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsClientDialedByMS.getAddress(), "340012");
        assertFalse(lcsClientDialedByMS.isExtension());
        LCSClientInternalID lcsClientInternalID = lcsClientId.getLCSClientInternalID();
        assertEquals(lcsClientInternalID, LCSClientInternalID.oandMHPLMN);
        lcsClientName = lcsClientId.getLCSClientName();
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "*911#");
        assertEquals(lcsClientName.getLCSFormatIndicator(), LCSFormatIndicator.url);
        APN apn = lcsClientId.getLCSAPN();
        assertEquals(apn.getApn(), "ims");
        LCSRequestorID lcsRequestorID = lcsClientId.getLCSRequestorID();
        assertEquals(lcsRequestorID.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsRequestorID.getRequestorIDString().getString(null), "*911#");
        assertEquals(lcsRequestorID.getLCSFormatIndicator(), LCSFormatIndicator.url);
        // privacyOverride
        boolean privacyOverride = reqInd.getPrivacyOverride();
        assertTrue(privacyOverride);
        // imsi
        imsi = reqInd.getIMSI();
        assertEquals(imsi.getData(), "748029489860456");
        // lmsi
        LMSI lmsi = reqInd.getLMSI();
        assertEquals(lmsi.getData(), new byte[] {0x72, 0x02, (byte) 0xe9, (byte) 0x8c});
        // imei
        IMEI imei = reqInd.getIMEI();
        assertEquals(imei.getIMEI(), "100710405305138");
        // lcs-Priority
        LCSPriority lcsPriority = reqInd.getLCSPriority();
        assertEquals(lcsPriority.getCode(), 1);
        // lcs-QoS
        lcsQoS = reqInd.getLCSQoS();
        assertNull(lcsQoS.getExtensionContainer());
        assertNull(lcsQoS.getLCSQoSClass());
        assertEquals(lcsQoS.getHorizontalAccuracy().intValue(), 10);
        assertTrue(lcsQoS.getVerticalCoordinateRequest());
        assertEquals(lcsQoS.getResponseTime().getResponseTimeCategory(), ResponseTimeCategory.delaytolerant);
        assertTrue(lcsQoS.getVelocityRequest());
        // supportedGADShapes
        suppGadShapes = reqInd.getSupportedGADShapes();
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());
        assertFalse(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyEllipsoid());
        assertTrue(suppGadShapes.getEllipsoidArc());
        // lcs-ReferenceNumber
        Integer lcsReferenceNumber = reqInd.getLCSReferenceNumber();
        assertEquals(lcsReferenceNumber.intValue(), 92);
        // lcsServiceTypeID
        Integer lcsServiceTypeID = reqInd.getLCSServiceTypeID();
        assertEquals(lcsServiceTypeID.intValue(), 67);
        // lcsCodeword
        LCSCodeword lcsCodeword = reqInd.getLCSCodeword();
        assertEquals(lcsCodeword.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsCodeword.getLCSCodewordString().getString(null), "*911#");
        // lcs-PrivacyCheck
        LCSPrivacyCheck lcsPrivacyCheck = reqInd.getLCSPrivacyCheck();
        assertEquals(lcsPrivacyCheck.getCallSessionUnrelated(), PrivacyCheckRelatedAction.allowedWithNotification);
        assertEquals(lcsPrivacyCheck.getCallSessionRelated(), PrivacyCheckRelatedAction.allowedIfNoResponse);
        // h-gmlc-Address
        GSNAddress hGmlcAddress = reqInd.getHGMLCAddress();
        assertEquals(hGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(hGmlcAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        // mo-lrShortCircuitIndicator
        boolean moLrShortCircuitIndicator = reqInd.getMoLrShortCircuitIndicator();
        assertTrue(moLrShortCircuitIndicator);
        // periodicLDRInfo
        PeriodicLDRInfo periodicLDRInfo = reqInd.getPeriodicLDRInfo();
        assertEquals(periodicLDRInfo.getReportingAmount(), 3);
        assertEquals(periodicLDRInfo.getReportingInterval(), 600);
        // reportingPLMNList
        ReportingPLMNList reportingPLMNList = reqInd.getReportingPLMNList();
        assertEquals(reportingPLMNList.getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(reportingPLMNList.getPlmnList().get(0).getPlmnId().getMnc(), 1);
        assertEquals(reportingPLMNList.getPlmnList().get(0).getRanTechnology(), RANTechnology.umts);
        assertTrue(reportingPLMNList.getPlmnList().get(0).getRanPeriodicLocationSupport());
        assertEquals(reportingPLMNList.getPlmnList().get(1).getPlmnId().getMcc(), 748);
        assertEquals(reportingPLMNList.getPlmnList().get(1).getPlmnId().getMnc(), 7);
        assertEquals(reportingPLMNList.getPlmnList().get(1).getRanTechnology(), RANTechnology.gsm);
        assertFalse(reportingPLMNList.getPlmnList().get(1).getRanPeriodicLocationSupport());

        // Test from MAP load for deferred location with area event params
        rawData = getEncodedDataLoadTestAreaEventInfo();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        reqInd = new ProvideSubscriberLocationRequestImpl();
        reqInd.decodeAll(asn);
        // Wireshark
        // Component: invoke (1)
        //    invoke
        //        invokeID: 0
        //        opCode: localValue (0)
        //            localValue: provideSubscriberLocation (83)
        //        locationType
        //            locationEstimateType: cancelDeferredLocation (4)
        //            Padding: 3
        //            deferredLocationEventType: 20
        //                0... .... = msAvailable: False
        //                .0.. .... = enteringIntoArea: False
        //                ..1. .... = leavingFromArea: True
        //                ...0 .... = beingInsideArea: False
        //                .... 0... = periodicLDR: False
        //        mlc-Number: 91947101742010
        //            1... .... = Extension: No Extension
        //            .001 .... = Nature of number: International Number (0x1)
        //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //            E.164 number (MSISDN): 491710470201
        //        lcs-ClientID
        //            lcsClientType: valueAddedServices (1)
        //            lcsClientExternalID
        //                externalAddress: 9143404465f7
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 340444567
        //            lcsClientDialedByMS: 91430021
        //                1... .... = Extension: No Extension
        //                .001 .... = Nature of number: International Number (0x1)
        //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                E.164 number (MSISDN): 340012
        //            lcsClientInternalID: targetMSsubscribedService (4)
        //            lcsClientName
        //                dataCodingScheme: 0f
        //                nameString: aa5c2c3602
        //                    USSD String: *911#
        //                lcs-FormatIndicator: url (3)
        //            lcsAPN: 03696d73 - ims
        //                APN: ims
        //            lcsRequestorID
        //                dataCodingScheme: 0f
        //                    0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
        //                    .... 1111 = Language: Language unspecified (15)
        //                requestorIDString: aa5c2c3602
        //                    USSD String: *911#
        //                lcs-FormatIndicator: url (3)
        //        privacyOverride
        //        IMSI: 748027827653729
        //        [Association IMSI: 748027827653729]
        //            Mobile Country Code (MCC): Uruguay (748)
        //            Mobile Network Code (MNC): Unknown (027)
        //        imei: 01703175551378f7
        //            TBCD digits: 100713575531877
        //        lcs-Priority: 01
        //        lcs-QoS
        //            horizontal-accuracy: 0a
        //            verticalCoordinateRequest
        //            vertical-accuracy: 32
        //            responseTime
        //                responseTimeCategory: delaytolerant (1)
        //            velocityRequest
        //        Padding: 1
        //        supportedGADShapes: f6
        //            1... .... = ellipsoidPoint: True
        //            .1.. .... = ellipsoidPointWithUncertaintyCircle: True
        //            ..1. .... = ellipsoidPointWithUncertaintyEllipse: True
        //            ...1 .... = polygon: True
        //            .... 0... = ellipsoidPointWithAltitude: False
        //            .... .1.. = ellipsoidPointWithAltitudeAndUncertaintyElipsoid: True
        //            .... ..1. = ellipsoidArc: True
        //        lcs-ReferenceNumber: e9
        //        lcsServiceTypeID: assetManagement (4)
        //        lcsCodeword
        //            dataCodingScheme: 0f
        //                0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
        //                .... 1111 = Language: Language unspecified (15)
        //            lcsCodewordString: aa5c2c3602
        //                USSD String: *911#
        //        lcs-PrivacyCheck
        //            callSessionUnrelated: allowedWithNotification (1)
        //            callSessionRelated: allowedIfNoResponse (2)
        //        areaEventInfo
        //            areaDefinition
        //                areaList: 2 items
        //                    Area
        //                        areaType: locationAreaId (2)
        //                        areaIdentification: 47f81004b1
        //                    Area
        //                        areaType: utranCellId (5)
        //                        areaIdentification: 47f8700800ffff
        //            occurrenceInfo: multipleTimeEvent (1)
        //            intervalTime: 10
        //        h-gmlc-Address: 040a00000e
        //            GSN-Address IPv4: 10.0.0.14
        //        mo-lrShortCircuitIndicator
        //        reportingPLMNList
        //            plmn-ListPrioritized
        //            plmn-List: 2 items
        //                ReportingPLMN
        //                    plmn-Id: 47f810
        //                    ran-Technology: umts (1)
        //                    ran-PeriodicLocationSupport
        //                ReportingPLMN
        //                    plmn-Id: 47f870
        //                    ran-Technology: gsm (0)
        // locationType
        locationType = reqInd.getLocationType();
        assertNotNull(locationType);
        assertEquals(locationType.getLocationEstimateType(), LocationEstimateType.cancelDeferredLocation);
        deferredLocationEventType = locationType.getDeferredLocationEventType();
        assertFalse(deferredLocationEventType.getMsAvailable());
        assertFalse(deferredLocationEventType.getEnteringIntoArea());
        assertTrue(deferredLocationEventType.getLeavingFromArea());
        assertFalse(deferredLocationEventType.getBeingInsideArea());
        assertFalse(deferredLocationEventType.getPeriodicLDR());
        // mlc-Number
        mlcNumber = reqInd.getMlcNumber();
        assertEquals(mlcNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mlcNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mlcNumber.getAddress(), "491710470201");
        // lcs-ClientID
        lcsClientId = reqInd.getLCSClientID();
        lcsClientType = lcsClientId.getLCSClientType();
        assertEquals(lcsClientType, LCSClientType.valueAddedServices);
        lcsClientExternalID = lcsClientId.getLCSClientExternalID();
        externalAddress = lcsClientExternalID.getExternalAddress();
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(externalAddress.getAddress(), "340444567");
        lcsClientDialedByMS = lcsClientId.getLCSClientDialedByMS();
        assertEquals(lcsClientDialedByMS.getAddressNature(), AddressNature.international_number);
        assertEquals(lcsClientDialedByMS.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsClientDialedByMS.getAddress(), "340012");
        assertFalse(lcsClientDialedByMS.isExtension());
        lcsClientInternalID = lcsClientId.getLCSClientInternalID();
        assertEquals(lcsClientInternalID, LCSClientInternalID.targetMSsubscribedService);
        lcsClientName = lcsClientId.getLCSClientName();
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "*911#");
        assertEquals(lcsClientName.getLCSFormatIndicator(), LCSFormatIndicator.url);
        apn = lcsClientId.getLCSAPN();
        assertEquals(apn.getApn(), "ims");
        lcsRequestorID = lcsClientId.getLCSRequestorID();
        assertEquals(lcsRequestorID.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsRequestorID.getRequestorIDString().getString(null), "*911#");
        assertEquals(lcsRequestorID.getLCSFormatIndicator(), LCSFormatIndicator.url);
        // privacyOverride
        privacyOverride = reqInd.getPrivacyOverride();
        assertTrue(privacyOverride);
        // imsi
        imsi = reqInd.getIMSI();
        assertEquals(imsi.getData(), "748027827653729");
        // lmsi
        lmsi = reqInd.getLMSI();
        assertNull(lmsi);
        // imei
        imei = reqInd.getIMEI();
        assertEquals(imei.getIMEI(), "100713575531877");
        // lcs-Priority
        lcsPriority = reqInd.getLCSPriority();
        assertEquals(lcsPriority.getCode(), 1);
        // lcs-QoS
        lcsQoS = reqInd.getLCSQoS();
        assertNull(lcsQoS.getExtensionContainer());
        assertNull(lcsQoS.getLCSQoSClass());
        assertEquals(lcsQoS.getHorizontalAccuracy().intValue(), 10);
        assertTrue(lcsQoS.getVerticalCoordinateRequest());
        assertEquals(lcsQoS.getResponseTime().getResponseTimeCategory(), ResponseTimeCategory.delaytolerant);
        assertTrue(lcsQoS.getVelocityRequest());
        // supportedGADShapes
        suppGadShapes = reqInd.getSupportedGADShapes();
        assertTrue(suppGadShapes.getEllipsoidPoint());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyCircle());
        assertTrue(suppGadShapes.getEllipsoidPointWithUncertaintyEllipse());
        assertTrue(suppGadShapes.getPolygon());
        assertFalse(suppGadShapes.getEllipsoidPointWithAltitude());
        assertTrue(suppGadShapes.getEllipsoidPointWithAltitudeAndUncertaintyEllipsoid());
        assertTrue(suppGadShapes.getEllipsoidArc());
        // lcs-ReferenceNumber
        lcsReferenceNumber = reqInd.getLCSReferenceNumber();
        assertEquals(lcsReferenceNumber.intValue(), -23);
        // lcsServiceTypeID
        lcsServiceTypeID = reqInd.getLCSServiceTypeID();
        assertEquals(lcsServiceTypeID.intValue(), 4);
        // lcsCodeword
        lcsCodeword = reqInd.getLCSCodeword();
        assertEquals(lcsCodeword.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsCodeword.getLCSCodewordString().getString(null), "*911#");
        // lcs-PrivacyCheck
        lcsPrivacyCheck = reqInd.getLCSPrivacyCheck();
        assertEquals(lcsPrivacyCheck.getCallSessionUnrelated(), PrivacyCheckRelatedAction.allowedWithNotification);
        assertEquals(lcsPrivacyCheck.getCallSessionRelated(), PrivacyCheckRelatedAction.allowedIfNoResponse);
        // areaEventInfo
        AreaEventInfo areaEventInfo = reqInd.getAreaEventInfo();
        AreaDefinition areaDefinition = areaEventInfo.getAreaDefinition();
        assertEquals(areaDefinition.getAreaList().get(0).getAreaIdentification().getMCC(), 748);
        assertEquals(areaDefinition.getAreaList().get(0).getAreaIdentification().getMNC(), 1);
        assertEquals(areaDefinition.getAreaList().get(0).getAreaIdentification().getLac(), 1201);
        assertEquals(areaDefinition.getAreaList().get(1).getAreaIdentification().getMCC(), 748);
        assertEquals(areaDefinition.getAreaList().get(1).getAreaIdentification().getMNC(), 7);
        assertEquals(areaDefinition.getAreaList().get(1).getAreaIdentification().getUtranCellId(), 134283263);
        OccurrenceInfo occurrenceInfo = areaEventInfo.getOccurrenceInfo();
        assertEquals(occurrenceInfo, OccurrenceInfo.multipleTimeEvent);
        Integer intervalTime = areaEventInfo.getIntervalTime();
        assertEquals(intervalTime.intValue(), 10);
        // h-gmlc-Address
        hGmlcAddress = reqInd.getHGMLCAddress();
        assertEquals(hGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(hGmlcAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        // mo-lrShortCircuitIndicator
        moLrShortCircuitIndicator = reqInd.getMoLrShortCircuitIndicator();
        assertTrue(moLrShortCircuitIndicator);
        // periodicLDRInfo
        periodicLDRInfo = reqInd.getPeriodicLDRInfo();
        assertNull(periodicLDRInfo);
        // reportingPLMNList
        reportingPLMNList = reqInd.getReportingPLMNList();
        assertEquals(reportingPLMNList.getPlmnList().get(0).getPlmnId().getMcc(), 748);
        assertEquals(reportingPLMNList.getPlmnList().get(0).getPlmnId().getMnc(), 1);
        assertEquals(reportingPLMNList.getPlmnList().get(0).getRanTechnology(), RANTechnology.umts);
        assertTrue(reportingPLMNList.getPlmnList().get(0).getRanPeriodicLocationSupport());
        assertEquals(reportingPLMNList.getPlmnList().get(1).getPlmnId().getMcc(), 748);
        assertEquals(reportingPLMNList.getPlmnList().get(1).getPlmnId().getMnc(), 7);
        assertEquals(reportingPLMNList.getPlmnList().get(1).getRanTechnology(), RANTechnology.gsm);
        assertFalse(reportingPLMNList.getPlmnList().get(1).getRanPeriodicLocationSupport());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        // Test from Brazilian operator
        byte[] rawData = getEncodedData();

        LocationType locationType = new LocationTypeImpl(LocationEstimateType.currentLocation, null);
        ISDNAddressString mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "55619007");

        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSClientName lcsClientName = new LCSClientNameImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);

        LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null,
                LCSClientInternalID.broadcastService, lcsClientName, null, null, null);

        IMSI imsi = MAPParameterFactory.createIMSI("724999900000007");

        LCSQoS lcsQoS = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null, false, null);

        SupportedGADShapes supportedGADShapes = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

        ProvideSubscriberLocationRequestImpl reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber,
                lcsClientID, false, imsi, null, null, null, LCSPriority.normalPriority, lcsQoS, null, supportedGADShapes, null,
                null, null, null, null, null, false, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        reqInd.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Test with fictional data from Sergey
        rawData = getEncodedData2();

        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "765432100");
        LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        USSDString lcsCodewordString = MAPParameterFactory.createUSSDString("xxyyyzz");
        LCSCodewordImpl lcsCodeword = new LCSCodewordImpl(new CBSDataCodingSchemeImpl(0x0f), lcsCodewordString);
        LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl(PrivacyCheckRelatedAction.allowedWithNotification,
                PrivacyCheckRelatedAction.allowedWithoutNotification);
        ArrayList<Area> areaList = new ArrayList<>();
        AreaIdentification areaIdentification = new AreaIdentificationImpl(AreaType.countryCode, 250, 0, 0, 0);
        AreaImpl area = new AreaImpl(AreaType.countryCode, areaIdentification);
        areaList.add(area);
        AreaDefinition areaDefinition = new AreaDefinitionImpl(areaList);
        AreaEventInfoImpl areaEventInfo = new AreaEventInfoImpl(areaDefinition, null, null);
        GSNAddress hgmlcAddress = new GSNAddressImpl(getDataHgmlcAddress());
        PeriodicLDRInfo periodicLDRInfo = new PeriodicLDRInfoImpl(200, 100, null);
        ArrayList<ReportingPLMN> reportingPLMNs = new ArrayList<>();
        PlmnId plmnId = new PlmnIdImpl(getPlmnId());
        ReportingPLMN rplmn = new ReportingPLMNImpl(plmnId, null, false);
        reportingPLMNs.add(rplmn);
        ReportingPLMNList reportingPLMNList = new ReportingPLMNListImpl(false, reportingPLMNs);

        reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber, lcsClientID, true, imsi, msisdn, lmsi, imei,
                LCSPriority.normalPriority, lcsQoS, null, supportedGADShapes, 5, 6, lcsCodeword, lcsPrivacyCheck,
                areaEventInfo, hgmlcAddress, true, periodicLDRInfo, reportingPLMNList);

        asnOS = new AsnOutputStream();
        reqInd.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Test from MAP load for deferred location with periodic LDR params
        rawData = getEncodedDataLoadTestPeriodicLDR();

        DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl(false, false, false, false, true);
        locationType = new LocationTypeImpl(LocationEstimateType.activateDeferredLocation, deferredLocationEventType);
        mlcNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710470201");
        LCSClientType lcsClientType = LCSClientType.plmnOperatorServices;
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "340444567");
        LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, null);
        LCSClientInternalID lcsClientInternalID = LCSClientInternalID.oandMHPLMN;
        CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(15);
        String ussdLcsString = "*911#";
        Charset gsm8Charset = Charset.defaultCharset();
        USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
        lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
        AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "340012");
        APN lcsAPN = new APNImpl("ims");
        LCSRequestorID lcsRequestorID = new LCSRequestorIDImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
        lcsClientID = new LCSClientIDImpl(lcsClientType, lcsClientExternalID, lcsClientInternalID, lcsClientName,
                lcsClientDialedByMS, lcsAPN, lcsRequestorID);
        boolean privacyOverride = true;
        int lcsReferenceNumber = 92;
        imsi = new IMSIImpl("748029489860456");
        lmsi = new LMSIImpl(new byte[] {0x72, 0x02, (byte) 0xe9, (byte) 0x8c});
        imei = new IMEIImpl("100710405305138");
        LCSPriority lcsPriority = LCSPriority.normalPriority;
        Integer horizontalAccuracy = 10;
        Integer verticalAccuracy = 50;
        boolean verticalCoordinateRequest = true;
        ResponseTime responseTime = new ResponseTimeImpl(ResponseTimeCategory.delaytolerant);
        boolean velocityRequest = true;
        lcsQoS = new LCSQoSImpl(horizontalAccuracy, verticalAccuracy, verticalCoordinateRequest, responseTime, null,
                velocityRequest, null);
        supportedGADShapes = new SupportedGADShapesImpl(true, true, true, true, false, true, true);
        int lcsServiceTypeID = 67;
        lcsCodewordString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        lcsCodeword = new LCSCodewordImpl(cbsDataCodingScheme, lcsCodewordString);
        PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithNotification;
        PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;
        lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
        GSNAddress hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        boolean moLrShortCircuitIndicator = true;
        int reportingAmount = 3;
        int reportingInterval = 600;
        periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, null);
        reportingPLMNs = new ArrayList<>();
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
        boolean plmnListPrioritized = true;
        reportingPLMNList = new ReportingPLMNListImpl(plmnListPrioritized, reportingPLMNs);
        reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber, lcsClientID, privacyOverride, imsi, null, lmsi, imei,
                lcsPriority, lcsQoS, null, supportedGADShapes, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, null,
                hGmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo, reportingPLMNList);

        asnOS = new AsnOutputStream();
        reqInd.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Test from MAP load for deferred location with area event params
        rawData = getEncodedDataLoadTestAreaEventInfo();

        deferredLocationEventType = new DeferredLocationEventTypeImpl(false, false, true, false, false);
        locationType = new LocationTypeImpl(LocationEstimateType.cancelDeferredLocation, deferredLocationEventType);
        lcsClientType = LCSClientType.valueAddedServices;
        lcsClientInternalID = LCSClientInternalID.targetMSsubscribedService;
        lcsClientID = new LCSClientIDImpl(lcsClientType, lcsClientExternalID, lcsClientInternalID, lcsClientName,
                lcsClientDialedByMS, lcsAPN, lcsRequestorID);
        imsi = new IMSIImpl("748027827653729");
        lmsi = null;
        imei = new IMEIImpl("100713575531877");
        lcsReferenceNumber = -23;
        lcsServiceTypeID = 4;
        areaList = new ArrayList<>();
        AreaType areaType;
        Area area1, area2;
        areaType = AreaType.locationAreaId;
        areaIdentification = new AreaIdentificationImpl(areaType, 748, 1, 1201, 0);
        area1 = new AreaImpl(areaType, areaIdentification);
        areaType = AreaType.utranCellId;
        areaIdentification = new AreaIdentificationImpl(areaType, 748, 7, 0, 134283263);
        area2 = new AreaImpl(areaType, areaIdentification);
        areaList.add(area1);
        areaList.add(area2);
        areaDefinition = new AreaDefinitionImpl(areaList);
        OccurrenceInfo occurrenceInfo = OccurrenceInfo.multipleTimeEvent;
        Integer intervalTime = 10;
        areaEventInfo = new AreaEventInfoImpl(areaDefinition, occurrenceInfo, intervalTime);
        reqInd = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber, lcsClientID, privacyOverride, imsi, null, lmsi, imei,
                lcsPriority, lcsQoS, null, supportedGADShapes, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo,
                hGmlcAddress, moLrShortCircuitIndicator, null, reportingPLMNList);

        asnOS = new AsnOutputStream();
        reqInd.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
