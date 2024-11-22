
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.EllipsoidPoint;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ProvideSubscriberLocationResponseTest {

    MAPParameterFactory mapParameterFactory = new MAPParameterFactoryImpl();
    private static Logger logger = Logger.getLogger(ProvideSubscriberLocationResponseTest.class);

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
        return new byte[] { 48, 6, 4, 1, 99, -128, 1, 15 };
    }

    private byte[] getEncodedDataSergey() {
        return new byte[] { 48, 59, 4, 1, 99, -128, 1, 15, -126, 1, 19, -125, 0, -124, 2, 11, 12, -123, 3, 15, 16, 17, -90, 7,
                -127, 5, 33, -15, 16, 8, -82, -121, 0, -120, 1, 0, -119, 4, 21, 22, 23, 24, -118, 0, -117, 2, 25, 26, -116, 1,
                29, -83, 8, -128, 6, -111, 68, 100, 102, -120, -8 };
    }

    private byte[] getEncodedDataIndia1() {
        return new byte[] { 0x30, 0x1d,
                0x04, 0x0d, (byte) 0xa0, 0x0e, 0x2a, (byte) 0xb5,
                0x36, 0x49, 0x53, 0x00, 0x37, 0x2a, 0x00, (byte) 0xb3,
                0x43, (byte) 0x80, 0x01, 0x00, (byte) 0xa6, 0x09, (byte) 0x80, 0x07,
                0x04, (byte) 0xf4, 0x27, 0x09, 0x62, 0x08, (byte) 0xd0
        };
    }

    private byte[] getEncodedDataIndia2() {
        return new byte[] { 0x30, 0x1b,
                0x04, 0x0b, 0x30, 0x22, (byte) 0xee, 0x69,
                0x34, 0x6c, (byte) 0xbc, 0x23, 0x21, 0x46, 0x50, (byte) 0x80,
                0x01, 0x00, (byte) 0xa6, 0x09, (byte) 0x80, 0x07, 0x04, (byte) 0xf4,
                (byte) 0x95, 0x1b, (byte) 0xb2, 0x51, (byte) 0xbb
        };
    }

    private byte[] getEncodedDataIndiaWithGERANPositioningData() {
        return new byte[] {0x30, 0x1c,
                0x04, 0x08, 0x10, 0x0f, 0x03, 0x7f, 0x36, 0x32,
                0x7f, 0x23, (byte) 0x80, 0x01, 0x00, (byte) 0x84, 0x02, 0x00,
                0x03, (byte) 0xa6, 0x09, (byte) 0x80, 0x07, 0x04, (byte) 0xf4, 0x27,
                0x13, (byte) 0x94, 0x2d, (byte) 0xc1,
        };
    }

    private byte[] getEncodedDataLSMLoadTest1() {
        return new byte[] {0x30, (byte) 0x82, 0x01, (byte) 0xe4, 0x04, 0x0d,
                (byte) 0xa0, (byte) 0xb1, (byte) 0xb1, 0x3f, (byte) 0xd8, (byte) 0xf3, 0x21, 0x00,
                0x05, 0x01, 0x14, 0x14, 0x02, (byte) 0x80, 0x01, 0x0a,
                (byte) 0x83, 0x00, (byte) 0x85, 0x07, 0x00, 0x00, 0x43, 0x4b,
                0x00, 0x62, 0x2b, (byte) 0xa6, 0x09, (byte) 0x80, 0x07, 0x47,
                (byte) 0xf8, 0x10, 0x00, 0x0b, 0x12, (byte) 0xcc, (byte) 0x87, 0x00,
                (byte) 0x88, 0x01, 0x00, (byte) 0x89, 0x07, 0x30, 0x03, 0x00,
                0x65, 0x02, 0x05, 0x01, (byte) 0x8a, 0x00, (byte) 0x8c, 0x05,
                0x01, 0x63, (byte) 0x8b, 0x02, 0x03, (byte) 0xad, 0x09, (byte) 0x80,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x51,
                (byte) 0x8e, 0x02, 0x57, (byte) 0x8f, (byte) 0x8f, 0x03, 0x01, (byte) 0xad,
                (byte) 0xb0, (byte) 0x90, (byte) 0x82, 0x01, (byte) 0x8d, 0x3c, 0x63, 0x6c,
                0x3a, 0x63, 0x69, 0x76, 0x69, 0x63, 0x41, 0x64,
                0x64, 0x72, 0x65, 0x73, 0x73, 0x3e, 0x0a, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c,
                0x63, 0x6c, 0x3a, 0x63, 0x6f, 0x75, 0x6e, 0x74,
                0x72, 0x79, 0x3e, 0x55, 0x53, 0x3c, 0x2f, 0x63,
                0x6c, 0x3a, 0x63, 0x6f, 0x75, 0x6e, 0x74, 0x72,
                0x79, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x41,
                0x31, 0x3e, 0x4e, 0x65, 0x77, 0x20, 0x59, 0x6f,
                0x72, 0x6b, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x41,
                0x31, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x41,
                0x33, 0x3e, 0x4e, 0x65, 0x77, 0x20, 0x59, 0x6f,
                0x72, 0x6b, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x41,
                0x33, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x41,
                0x36, 0x3e, 0x42, 0x72, 0x6f, 0x61, 0x64, 0x77,
                0x61, 0x79, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x41,
                0x36, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x48,
                0x4e, 0x4f, 0x3e, 0x31, 0x32, 0x33, 0x3c, 0x2f,
                0x63, 0x6c, 0x3a, 0x48, 0x4e, 0x4f, 0x3e, 0x0a,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x3c, 0x63, 0x6c, 0x3a, 0x4c, 0x4f, 0x43, 0x3e,
                0x53, 0x75, 0x69, 0x74, 0x65, 0x20, 0x37, 0x35,
                0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x4c, 0x4f, 0x43,
                0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x50, 0x43,
                0x3e, 0x31, 0x30, 0x30, 0x32, 0x37, 0x2d, 0x30,
                0x34, 0x30, 0x31, 0x3c, 0x2f, 0x63, 0x6c, 0x3a,
                0x50, 0x43, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x63, 0x69, 0x76,
                0x69, 0x63, 0x41, 0x64, 0x64, 0x72, 0x65, 0x73,
                0x73, 0x3e
        };
    }

    private byte[] getEncodedDataLSMLoadTest2() {
        return new byte[] { 0x30, (byte) 0x82, 0x02, 0x00, 0x04, 0x07,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x80,
                0x01, 0x00, (byte) 0x82, 0x25, 0x56, (byte) 0x84, 0x22, 0x68,
                0x32, 0x54, (byte) 0xbe, (byte) 0x84, 0x4a, (byte) 0x8a, 0x32, 0x48,
                0x2a, (byte) 0x84, 0x2e, (byte) 0xed, 0x32, 0x15, (byte) 0xc5, (byte) 0x84,
                0x52, (byte) 0xd6, 0x32, 0x43, 0x3f, (byte) 0x84, 0x54, (byte) 0xa6,
                0x32, 0x46, (byte) 0x8f, (byte) 0x84, 0x40, 0x43, 0x32, 0x7d,
                0x28, (byte) 0x83, 0x00, (byte) 0x84, 0x08, 0x00, 0x03, 0x1b,
                0x21, 0x2b, 0x3a, 0x43, 0x60, (byte) 0xa6, 0x09, (byte) 0x80,
                0x07, 0x47, (byte) 0xf8, 0x10, 0x00, 0x77, 0x3b, (byte) 0xe8,
                (byte) 0x88, 0x01, 0x00, (byte) 0x89, 0x07, 0x30, 0x03, 0x00,
                0x65, 0x02, 0x05, 0x01, (byte) 0x8a, 0x00, (byte) 0x8b, 0x05,
                0x00, 0x63, (byte) 0x8b, 0x02, 0x03, (byte) 0xad, 0x09, (byte) 0x80,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x51,
                (byte) 0x8f, 0x03, 0x01, (byte) 0xad, (byte) 0xb0, (byte) 0x90, (byte) 0x82, 0x01,
                (byte) 0x8d, 0x3c, 0x63, 0x6c, 0x3a, 0x63, 0x69, 0x76,
                0x69, 0x63, 0x41, 0x64, 0x64, 0x72, 0x65, 0x73,
                0x73, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x63,
                0x6f, 0x75, 0x6e, 0x74, 0x72, 0x79, 0x3e, 0x55,
                0x53, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x63, 0x6f,
                0x75, 0x6e, 0x74, 0x72, 0x79, 0x3e, 0x0a, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c,
                0x63, 0x6c, 0x3a, 0x41, 0x31, 0x3e, 0x4e, 0x65,
                0x77, 0x20, 0x59, 0x6f, 0x72, 0x6b, 0x3c, 0x2f,
                0x63, 0x6c, 0x3a, 0x41, 0x31, 0x3e, 0x0a, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c,
                0x63, 0x6c, 0x3a, 0x41, 0x33, 0x3e, 0x4e, 0x65,
                0x77, 0x20, 0x59, 0x6f, 0x72, 0x6b, 0x3c, 0x2f,
                0x63, 0x6c, 0x3a, 0x41, 0x33, 0x3e, 0x0a, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c,
                0x63, 0x6c, 0x3a, 0x41, 0x36, 0x3e, 0x42, 0x72,
                0x6f, 0x61, 0x64, 0x77, 0x61, 0x79, 0x3c, 0x2f,
                0x63, 0x6c, 0x3a, 0x41, 0x36, 0x3e, 0x0a, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c,
                0x63, 0x6c, 0x3a, 0x48, 0x4e, 0x4f, 0x3e, 0x31,
                0x32, 0x33, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x48,
                0x4e, 0x4f, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a,
                0x4c, 0x4f, 0x43, 0x3e, 0x53, 0x75, 0x69, 0x74,
                0x65, 0x20, 0x37, 0x35, 0x3c, 0x2f, 0x63, 0x6c,
                0x3a, 0x4c, 0x4f, 0x43, 0x3e, 0x0a, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3c, 0x63,
                0x6c, 0x3a, 0x50, 0x43, 0x3e, 0x31, 0x30, 0x30,
                0x32, 0x37, 0x2d, 0x30, 0x34, 0x30, 0x31, 0x3c,
                0x2f, 0x63, 0x6c, 0x3a, 0x50, 0x43, 0x3e, 0x0a,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x3c, 0x2f, 0x63, 0x6c,
                0x3a, 0x63, 0x69, 0x76, 0x69, 0x63, 0x41, 0x64,
                0x64, 0x72, 0x65, 0x73, 0x73, 0x3e
        };
    }

    public byte[] getExtGeographicalInformation() {
        return new byte[] { 99 };
    }

    public byte[] getPositioningDataInformation() {
        return new byte[] { 11, 12 };
    }

    public byte[] getUtranPositioningDataInfo() {
        return new byte[] { 15, 16, 17 };
    }

    public byte[] getAddGeographicalInformation() {
        return new byte[] { 19 };
    }

    public byte[] getVelocityEstimate() {
        return new byte[] { 21, 22, 23, 24 };
    }

    public byte[] getGeranGANSSpositioningData() {
        return new byte[] { 25, 26 };
    }

    public byte[] getUtranGANSSpositioningData() {
        return new byte[] { 29 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl1 = new ProvideSubscriberLocationResponseImpl();
        psl1.decodeAll(asn);

        ExtGeographicalInformation locationEstimate = psl1.getLocationEstimate();
        PositioningDataInformation geranPositioningData = psl1.getGeranPositioningData();
        UtranPositioningDataInfo utranPositioningData = psl1.getUtranPositioningData();
        Integer ageOfLocationEstimate = psl1.getAgeOfLocationEstimate();
        AddGeographicalInformation additionalLocationEstimate = psl1.getAdditionalLocationEstimate();
        MAPExtensionContainer extensionContainer = psl1.getExtensionContainer();
        boolean deferredMTLRResponseIndicator = psl1.getDeferredMTLRResponseIndicator();
        CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = psl1.getCellIdOrSai();
        boolean saiPresent = psl1.getSaiPresent();
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = psl1.getAccuracyFulfilmentIndicator();
        VelocityEstimate velocityEstimate = psl1.getVelocityEstimate();
        boolean moLrShortCircuitIndicator = psl1.getMoLrShortCircuitIndicator();
        GeranGANSSpositioningData geranGANSSpositioningData = psl1.getGeranGANSSpositioningData();
        UtranGANSSpositioningData utranGANSSpositioningData = psl1.getUtranGANSSpositioningData();
        ServingNodeAddress targetServingNodeForHandover = psl1.getTargetServingNodeForHandover();
        UtranAdditionalPositioningData utranAdditionalPositioningData = psl1.getUtranAdditionalPositioningData();
        Integer utranBaroPressureMeas = psl1.getUtranBaroPressureMeas();
        UtranCivicAddress utranCivicAddress = psl1.getUtranCivicAddress();

        assertTrue(Arrays.equals(locationEstimate.getData(), getExtGeographicalInformation()));
        assertNull(geranPositioningData);
        assertNull(utranPositioningData);
        assertEquals(ageOfLocationEstimate.intValue(), 15);
        assertNull(additionalLocationEstimate);
        assertNull(extensionContainer);
        assertFalse(deferredMTLRResponseIndicator);
        assertNull(cellGlobalIdOrServiceAreaIdOrLAI);
        assertFalse(saiPresent);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 2
        rawData = getEncodedDataSergey();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl2 = new ProvideSubscriberLocationResponseImpl();
        psl2.decodeAll(asn);

        locationEstimate = psl2.getLocationEstimate();
        geranPositioningData = psl2.getGeranPositioningData();
        utranPositioningData = psl2.getUtranPositioningData();
        ageOfLocationEstimate = psl2.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl2.getAdditionalLocationEstimate();
        extensionContainer = psl2.getExtensionContainer();
        deferredMTLRResponseIndicator = psl2.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl2.getCellIdOrSai();
        saiPresent = psl2.getSaiPresent();
        accuracyFulfilmentIndicator = psl2.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl2.getVelocityEstimate();
        moLrShortCircuitIndicator = psl2.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl2.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl2.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl2.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl2.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl2.getUtranBaroPressureMeas();
        utranCivicAddress = psl2.getUtranCivicAddress();

        assertTrue(Arrays.equals(locationEstimate.getData(), getExtGeographicalInformation()));
        assertTrue(Arrays.equals(geranPositioningData.getData(), getPositioningDataInformation()));
        assertTrue(Arrays.equals(utranPositioningData.getData(), getUtranPositioningDataInfo()));
        assertEquals(ageOfLocationEstimate.intValue(), 15);
        assertTrue(Arrays.equals(additionalLocationEstimate.getData(), getAddGeographicalInformation()));
        assertNull(extensionContainer);
        assertTrue(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMCC(), 121);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getMNC(), 1);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getLAIFixedLength().getLac(), 2222);
        assertTrue(saiPresent);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        assertTrue(Arrays.equals(velocityEstimate.getData(), getVelocityEstimate()));
        assertTrue(moLrShortCircuitIndicator);
        assertTrue(Arrays.equals(geranGANSSpositioningData.getData(), getGeranGANSSpositioningData()));
        assertTrue(Arrays.equals(utranGANSSpositioningData.getData(), getUtranGANSSpositioningData()));
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddress(), "444666888");
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 3 with real data from Indian operator
        rawData = getEncodedDataIndia1();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl3 = new ProvideSubscriberLocationResponseImpl();
        psl3.decodeAll(asn);

        locationEstimate = psl3.getLocationEstimate();
        geranPositioningData = psl3.getGeranPositioningData();
        utranPositioningData = psl3.getUtranPositioningData();
        ageOfLocationEstimate = psl3.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl3.getAdditionalLocationEstimate();
        extensionContainer = psl3.getExtensionContainer();
        deferredMTLRResponseIndicator = psl3.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl3.getCellIdOrSai();
        saiPresent = psl3.getSaiPresent();
        accuracyFulfilmentIndicator = psl3.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl3.getVelocityEstimate();
        moLrShortCircuitIndicator = psl3.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl3.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl3.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl3.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl3.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl3.getUtranBaroPressureMeas();
        utranCivicAddress = psl3.getUtranCivicAddress();

        // Wireshark sample description corresponding to bytes from getEncodedDataIndia1():
        //    opCode: localValue (0)
        //        localValue: provideSubscriberLocation (83)
        //    locationEstimate: a00e2ab536495300372a00b343
        //        1010 .... = Location estimate: Ellipsoid Arc (10)
        //        0... .... = Sign of latitude: North (0)
        //        .000 1110 0010 1010 1011 0101 = Degrees of latitude: 928437 (9.96105 degrees)
        //        0011 0110 0100 1001 0101 0011 = Degrees of longitude: 3557715 (76.34029 degrees)
        //        Inner radius: 55
        //        .010 1010 = Uncertainty radius: 42
        //        Offset angle: 0
        //        Included angle: 179
        //        .100 0011 = Confidence(%): 67
        //        [Location OSM URI: https://www.openstreetmap.org/?mlat=9.96105&mlon=76.34029&zoom=12]
        //    ageOfLocationEstimate: 0
        //    cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //        cellGlobalIdOrServiceAreaIdFixedLength: 04f427096208d0
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(locationEstimate.getLatitude() - 9.96105) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - 76.34029) < 0.00001);
        assertEquals(locationEstimate.getInnerRadius(), 55);
        assertTrue(Math.abs(locationEstimate.getUncertaintyRadius() - 537.6) < 0.1); // r = 45((1+0.025)^42 -1)
        assertEquals(locationEstimate.getOffsetAngle(), 0.0);
        assertEquals(locationEstimate.getIncludedAngle(), 179.0);
        assertEquals(locationEstimate.getConfidence(), 67);
        assertNull(geranPositioningData);
        assertNull(utranPositioningData);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertNull(additionalLocationEstimate);
        assertNull(extensionContainer);
        assertFalse(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 404);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 72);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 2402);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 2256);
        assertFalse(saiPresent);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 4 with real data from Indian operator
        rawData = getEncodedDataIndia2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl4 = new ProvideSubscriberLocationResponseImpl();
        psl4.decodeAll(asn);

        locationEstimate = psl4.getLocationEstimate();
        geranPositioningData = psl4.getGeranPositioningData();
        utranPositioningData = psl4.getUtranPositioningData();
        ageOfLocationEstimate = psl4.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl4.getAdditionalLocationEstimate();
        extensionContainer = psl4.getExtensionContainer();
        deferredMTLRResponseIndicator = psl4.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl4.getCellIdOrSai();
        saiPresent = psl4.getSaiPresent();
        accuracyFulfilmentIndicator = psl4.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl4.getVelocityEstimate();
        moLrShortCircuitIndicator = psl4.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl4.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl4.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl4.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl4.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl4.getUtranBaroPressureMeas();
        utranCivicAddress = psl4.getUtranCivicAddress();
        // Wireshark sample description corresponding to bytes from getEncodedDataIndia2():
        //            opCode: localValue (0)
        //                localValue: provideSubscriberLocation (83)
        //            locationEstimate: 3022ee69346cbc23214650
        //                0011 .... = Location estimate: Ellipsoid point with uncertainty Ellipse (3)
        //                0... .... = Sign of latitude: North (0)
        //                .010 0010 1110 1110 0110 1001 = Degrees of latitude: 2289257 (24.56107 degrees)
        //                0011 0100 0110 1100 1011 1100 = Degrees of longitude: 3435708 (73.72230 degrees)
        //                .010 0011 = Uncertainty semi-major: 35 (271.0 m)
        //                .010 0001 = Uncertainty semi-minor: 33 (222.3 m)
        //                Orientation of major axis: 70
        //                .101 0000 = Confidence(%): 80
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=24.56107&mlon=73.72230&zoom=12]
        //            ageOfLocationEstimate: 0
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 04f4951bb251bb}
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyEllipse);
        assertTrue(Math.abs(locationEstimate.getLatitude() - 24.56107) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - 73.72230) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getUncertaintySemiMajorAxis() - 271.0) < 0.1);
        assertTrue(Math.abs(locationEstimate.getUncertaintySemiMinorAxis() - 222.3) < 0.1);
        assertEquals(locationEstimate.getAngleOfMajorAxis(), 70.0);
        assertEquals(locationEstimate.getConfidence(), 80);
        assertNull(geranPositioningData);
        assertNull(utranPositioningData);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertNull(additionalLocationEstimate);
        assertNull(extensionContainer);
        assertFalse(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 404);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 59);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 7090);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 20923);
        assertFalse(saiPresent);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 5 with real data from Indian operator
        rawData = getEncodedDataIndiaWithGERANPositioningData();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl5 = new ProvideSubscriberLocationResponseImpl();
        psl5.decodeAll(asn);

        locationEstimate = psl5.getLocationEstimate();
        geranPositioningData = psl5.getGeranPositioningData();
        utranPositioningData = psl5.getUtranPositioningData();
        ageOfLocationEstimate = psl5.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl5.getAdditionalLocationEstimate();
        extensionContainer = psl5.getExtensionContainer();
        deferredMTLRResponseIndicator = psl5.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl5.getCellIdOrSai();
        saiPresent = psl5.getSaiPresent();
        accuracyFulfilmentIndicator = psl5.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl5.getVelocityEstimate();
        moLrShortCircuitIndicator = psl5.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl5.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl5.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl5.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl5.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl5.getUtranBaroPressureMeas();
        utranCivicAddress = psl5.getUtranCivicAddress();
        // Wireshark sample description corresponding to bytes from getEncodedDataIndiaWithGERANPositioningData():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: 100f037f36327f23
        //                0001 .... = Location estimate: Ellipsoid point with uncertainty Circle (1)
        //                0... .... = Sign of latitude: North (0)
        //                .000 1111 0000 0011 0111 1111 = Degrees of latitude: 983935 (10.55648 degrees)
        //                0011 0110 0011 0010 0111 1111 = Degrees of longitude: 3551871 (76.21489 degrees)
        //                .010 0011 = Uncertainty code: 35 (271.0 m)
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=10.55648&mlon=76.21489&zoom=12]
        //            ageOfLocationEstimate: 0
        //            geranPositioningData: 0003
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 04f42713942dc1
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(locationEstimate.getLatitude() - 10.55648) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - 76.21489) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getUncertainty() - 271.03) < 0.01);
        assertEquals(geranPositioningData.getLocationGeneratedPositioningMethods().size(), 1);
        assertEquals(geranPositioningData.getLocationGeneratedPositioningMethods().get(0), "Timing Advance");
        assertEquals(geranPositioningData.getPositioningMethodsAndUsage().get("Timing Advance").intValue(), 3);
        assertNull(utranPositioningData);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertNull(additionalLocationEstimate);
        assertNull(extensionContainer);
        assertFalse(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 404);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 72);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 5012);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 11713);
        assertFalse(saiPresent);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 6 from LSM load
        rawData = getEncodedDataLSMLoadTest1();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl6 = new ProvideSubscriberLocationResponseImpl();
        psl6.decodeAll(asn);

        locationEstimate = psl6.getLocationEstimate();
        geranPositioningData = psl6.getGeranPositioningData();
        utranPositioningData = psl6.getUtranPositioningData();
        ageOfLocationEstimate = psl6.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl6.getAdditionalLocationEstimate();
        extensionContainer = psl6.getExtensionContainer();
        deferredMTLRResponseIndicator = psl6.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl6.getCellIdOrSai();
        saiPresent = psl6.getSaiPresent();
        accuracyFulfilmentIndicator = psl6.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl6.getVelocityEstimate();
        moLrShortCircuitIndicator = psl6.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl6.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl6.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl6.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl6.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl6.getUtranBaroPressureMeas();
        utranCivicAddress = psl6.getUtranCivicAddress();
        // Wireshark sample description corresponding to bytes from getEncodedDataLSMLoadTest1():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: a0b1b13fd8f321000501141402
        //                1010 .... = Location estimate: Ellipsoid Arc (10)
        //                1... .... = Sign of latitude: South (1)
        //                .011 0001 1011 0001 0011 1111 = Degrees of latitude: 3256639 (-34.93995 degrees)
        //                1101 1000 1111 0011 0010 0001 = Degrees of longitude: -2559199 (-54.91446 degrees)
        //                Inner radius: 5
        //                .000 0001 = Uncertainty radius: 1
        //                Offset angle: 20
        //                Included angle: 20
        //                .000 0010 = Confidence(%): 2
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=-34.93995&mlon=-54.91446&zoom=12]
        //            ageOfLocationEstimate: 10
        //            deferredmt-lrResponseIndicator
        //            utranPositioningData: 0000434b00622b
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 47f810000b12cc
        //            sai-Present
        //            accuracyFulfilmentIndicator: requestedAccuracyFulfilled (0)
        //            velocityEstimate: 30030065020501
        //            mo-lrShortCircuitIndicator
        //            utranGANSSpositioningData: 01638b0203
        //            targetServingNodeForHandover: msc-Number (0)
        //                msc-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //            utranAdditionalPositioningData: 578f
        //            utranBaroPressureMeas: 110000
        //            utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202
        //            0202020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a202020202020202020202020202020202
        //            0202020202020203c636c3a41313e4e657720596f
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(locationEstimate.getLatitude() - (-34.93995)) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - (-54.91446)) < 0.00001);
        assertEquals(locationEstimate.getInnerRadius(), 5);
        assertTrue(Math.abs(locationEstimate.getUncertaintyRadius() - 1) < 0.1);
        assertEquals(locationEstimate.getOffsetAngle(), 20.0);
        assertEquals(locationEstimate.getIncludedAngle(), 20.0);
        assertEquals(locationEstimate.getConfidence(), 2);
        assertNull(geranPositioningData);
        assertEquals(utranPositioningData.getUtranPositioningDataDiscriminator(), 0);
        UtranPositioningDataInfoImpl utranPositioningDataInfo = new UtranPositioningDataInfoImpl(utranPositioningData.getData());
        HashMap<String, Integer> utranPositioningDataMethodsAndUsage = utranPositioningDataInfo.getUtranPositioningDataSet();
        assertNotNull(utranPositioningDataMethodsAndUsage.get("OTDOA"));
        assertNotNull(utranPositioningDataMethodsAndUsage.get("Reserved (GERAN use only)"));
        assertNotNull(utranPositioningDataMethodsAndUsage.get("U-TDOA"));
        assertNotNull(utranPositioningDataMethodsAndUsage.get("Cell ID"));
        assertNotNull(utranPositioningDataMethodsAndUsage.get("Mobile Assisted GPS"));
        assertNull(utranPositioningDataMethodsAndUsage.get("Mobile Based GPS"));
        assertNull(utranPositioningDataMethodsAndUsage.get("Conventional GPS"));
        assertNull(utranPositioningDataMethodsAndUsage.get("IPDL"));
        assertNull(utranPositioningDataMethodsAndUsage.get("RTT"));
        ArrayList<String> utranPosMethods = utranPositioningDataInfo.getUtranLocationGeneratedPositioningMethods();
        assertEquals(utranPosMethods.get(0), "U-TDOA");
        assertEquals(utranPosMethods.get(1), "OTDOA");
        assertEquals(utranPosMethods.get(2), "Mobile Assisted GPS");
        assertEquals(ageOfLocationEstimate.intValue(), 10);
        assertNull(additionalLocationEstimate);
        assertNull(extensionContainer);
        assertTrue(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 11);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 4812);
        assertTrue(saiPresent);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        assertEquals(velocityEstimate.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(velocityEstimate.getHorizontalSpeed(), 101);
        assertEquals(velocityEstimate.getBearing(), 3);
        assertEquals(velocityEstimate.getVerticalSpeed(), 2);
        assertEquals(velocityEstimate.getUncertaintyHorizontalSpeed(), 5);
        assertEquals(velocityEstimate.getUncertaintyVerticalSpeed(), 1);
        assertTrue(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        UtranGANSSpositioningDataImpl utranGanssPositioningData = new UtranGANSSpositioningDataImpl(utranGANSSpositioningData.getData());
        HashMap<String, String> methodsAndGanssIds = utranGanssPositioningData.getLocationGeneratedMethodsAndGANSSId();
        Set<String> utranGanssMethods = methodsAndGanssIds.keySet();
        Collection<String> utranGanssIds = methodsAndGanssIds.values();
        assertTrue(utranGanssMethods.contains("MS-Based"));
        assertTrue(utranGanssMethods.contains("MS-Assisted"));
        assertTrue(utranGanssMethods.contains("Conventional"));
        assertFalse(utranGanssMethods.contains("Reserved"));
        assertTrue(utranGanssIds.contains("Galileo"));
        assertTrue(utranGanssIds.contains("GLONASS"));
        assertTrue(utranGanssIds.contains("SBAS"));
        assertFalse(utranGanssIds.contains("Modernized GPS"));
        assertFalse(utranGanssIds.contains("QZSS"));
        assertFalse(utranGanssIds.contains("BDS"));
        assertNull(targetServingNodeForHandover.getMmeNumber());
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(targetServingNodeForHandover.getMscNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddress(), "491710460015");
        assertFalse(targetServingNodeForHandover.getMscNumber().isExtension());
        UtranAdditionalPositioningDataImpl utranAdditionalPositioningDataImpl = new UtranAdditionalPositioningDataImpl(utranAdditionalPositioningData.getData());
        HashMap<String, String> methodsAndAddPosIds = utranAdditionalPositioningDataImpl.getUtranAdditionalPositioningDataSet();
        Set<String> utranAddMethods = methodsAndAddPosIds.keySet();
        Collection<String> utranAddPosIds = methodsAndAddPosIds.values();
        assertTrue(utranAddMethods.contains("Standalone"));
        assertTrue(utranAddMethods.contains("MS-Assisted"));
        assertFalse(utranAddMethods.contains("Reserved"));
        assertTrue(utranAddPosIds.contains("WLAN"));
        assertTrue(utranAddPosIds.contains("Bluetooth"));
        assertFalse(utranAddPosIds.contains("Barometric Pressure"));
        assertFalse(utranAddPosIds.contains("MBS"));
        assertEquals(utranBaroPressureMeas.intValue(), 110000);
        String civicAddressString = "<cl:civicAddress>\n" +
                "                        <cl:country>US</cl:country>\n" +
                "                        <cl:A1>New York</cl:A1>\n" +
                "                        <cl:A3>New York</cl:A3>\n" +
                "                        <cl:A6>Broadway</cl:A6>\n" +
                "                        <cl:HNO>123</cl:HNO>\n" +
                "                        <cl:LOC>Suite 75</cl:LOC>\n" +
                "                        <cl:PC>10027-0401</cl:PC>\n" +
                "                    </cl:civicAddress>";
        assertEquals(utranCivicAddress.getData(), civicAddressString.getBytes(StandardCharsets.UTF_8));

        // test 7 from LSM load
        rawData = getEncodedDataLSMLoadTest2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        ProvideSubscriberLocationResponseImpl psl7 = new ProvideSubscriberLocationResponseImpl();
        psl7.decodeAll(asn);

        locationEstimate = psl7.getLocationEstimate();
        geranPositioningData = psl7.getGeranPositioningData();
        utranPositioningData = psl7.getUtranPositioningData();
        ageOfLocationEstimate = psl7.getAgeOfLocationEstimate();
        additionalLocationEstimate = psl7.getAdditionalLocationEstimate();
        extensionContainer = psl7.getExtensionContainer();
        deferredMTLRResponseIndicator = psl7.getDeferredMTLRResponseIndicator();
        cellGlobalIdOrServiceAreaIdOrLAI = psl7.getCellIdOrSai();
        saiPresent = psl7.getSaiPresent();
        accuracyFulfilmentIndicator = psl7.getAccuracyFulfilmentIndicator();
        velocityEstimate = psl7.getVelocityEstimate();
        moLrShortCircuitIndicator = psl7.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = psl7.getGeranGANSSpositioningData();
        utranGANSSpositioningData = psl7.getUtranGANSSpositioningData();
        targetServingNodeForHandover = psl7.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = psl7.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = psl7.getUtranBaroPressureMeas();
        utranCivicAddress = psl7.getUtranCivicAddress();
        // Wireshark sample description corresponding to bytes from getEncodedDataLSMLoadTest2():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: 00000000000000
        //                0000 .... = Location estimate: Ellipsoid Point (0)
        //                0... .... = Sign of latitude: North (0)
        //                .000 0000 0000 0000 0000 0000 = Degrees of latitude: 0 (0.00000 degrees)
        //                0000 0000 0000 0000 0000 0000 = Degrees of longitude: 0 (0.00000 degrees)
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=0.00000&mlon=0.00000&zoom=12]
        //            ageOfLocationEstimate: 0
        //            add-LocationEstimate: 568422683254be844a8a32482a842eed3215c58452d632433f8454a632468f844043327d28
        //            deferredmt-lrResponseIndicator
        //            geranPositioningData: 00031b212b3a4360
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 47f81000773be8
        //            accuracyFulfilmentIndicator: requestedAccuracyFulfilled (0)
        //            velocityEstimate: 30030065020501
        //            mo-lrShortCircuitIndicator
        //            geranGANSSpositioningData: 00638b0203
        //            targetServingNodeForHandover: msc-Number (0)
        //                msc-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //            utranBaroPressureMeas: 110000
        //            utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202
        //            0202020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a202020202020202020202020202020202
        //            0202020202020203c636c3a41313e4e657720596f
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(locationEstimate.getLatitude() - (0.00000)) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - (0.00000)) < 0.00001);
        PositioningDataInformationImpl positioningData = new PositioningDataInformationImpl(geranPositioningData.getData());
        HashMap<String, Integer> geranMethodsAndUsage = positioningData.getPositioningMethodsAndUsage();
        assertNotNull(geranMethodsAndUsage.get("Mobile Based E-OTD"));
        assertNotNull(geranMethodsAndUsage.get("Mobile Assisted E-OTD"));
        assertNotNull(geranMethodsAndUsage.get("U-TDOA"));
        assertNotNull(geranMethodsAndUsage.get("Cell ID"));
        assertNotNull(geranMethodsAndUsage.get("Mobile Assisted GPS"));
        assertNotNull(geranMethodsAndUsage.get("Timing Advance"));
        assertNotNull(geranMethodsAndUsage.get("Conventional GPS"));
        assertNull(geranMethodsAndUsage.get("Reserved (not to be used)"));
        ArrayList<String> geranPosMethods = positioningData.getLocationGeneratedPositioningMethods();
        assertEquals(geranPosMethods.get(0), "Timing Advance");
        assertEquals(geranPosMethods.get(1), "Mobile Assisted E-OTD");
        assertEquals(geranPosMethods.get(2), "Mobile Assisted GPS");
        assertEquals(geranPosMethods.get(3), "U-TDOA");
        assertNull(utranPositioningData);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertEquals(additionalLocationEstimate.getData(), new byte[] {86, -124, 34, 104, 50, 84, -66, -124, 74, -118, 50,
                72, 42, -124, 46, -19, 50, 21, -59, -124, 82, -42, 50, 67, 63, -124, 84, -90, 50, 70, -113, -124, 64, 67,
                50, 125, 40});
        PolygonImpl polygon = new PolygonImpl(additionalLocationEstimate.getData());
        assertEquals(polygon.getNumberOfPoints(), 6);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLatitude() - (-2.907000)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLongitude() - 70.778003) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLatitude() - (-3.017228)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLongitude() - 70.708909) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLatitude() - (-2.941386)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLongitude() - 70.432084) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(3).getLatitude() - (-3.040016)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(3).getLongitude() - 70.681894) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(4).getLatitude() - (-3.044994)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(4).getLongitude() - 70.700090) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(5).getLatitude() - (-2.989000)) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(5).getLongitude() - 71.000004) < 0.000001);
        assertNull(extensionContainer);
        assertTrue(deferredMTLRResponseIndicator);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 119);
        assertEquals(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 15336);
        assertFalse(saiPresent);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        assertEquals(velocityEstimate.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(velocityEstimate.getHorizontalSpeed(), 101);
        assertEquals(velocityEstimate.getBearing(), 3);
        assertEquals(velocityEstimate.getVerticalSpeed(), 2);
        assertEquals(velocityEstimate.getUncertaintyHorizontalSpeed(), 5);
        assertEquals(velocityEstimate.getUncertaintyVerticalSpeed(), 1);
        assertTrue(moLrShortCircuitIndicator);
        GeranGANSSpositioningDataImpl geranGansspositioningData = new GeranGANSSpositioningDataImpl(geranGANSSpositioningData.getData());
        HashMap<String, String> geranGanssMethodsAndGanssIds = geranGansspositioningData.getLocationGeneratedMethodsAndGANSSId();
        Set<String> geranGanssMethods = geranGanssMethodsAndGanssIds.keySet();
        Collection<String> geranGanssIds = geranGanssMethodsAndGanssIds.values();
        assertTrue(geranGanssMethods.contains("MS-Based"));
        assertTrue(geranGanssMethods.contains("MS-Assisted"));
        assertTrue(geranGanssMethods.contains("Conventional"));
        assertFalse(geranGanssMethods.contains("Reserved"));
        assertTrue(geranGanssIds.contains("Galileo"));
        assertTrue(geranGanssIds.contains("GLONASS"));
        assertTrue(geranGanssIds.contains("SBAS"));
        assertFalse(geranGanssIds.contains("Modernized GPS"));
        assertFalse(geranGanssIds.contains("QZSS"));
        assertFalse(geranGanssIds.contains("BDS"));
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover.getMmeNumber());
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(targetServingNodeForHandover.getMscNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddress(), "491710460015");
        assertFalse(targetServingNodeForHandover.getMscNumber().isExtension());
        assertNull(utranAdditionalPositioningData);
        assertEquals(utranBaroPressureMeas.intValue(), 110000);
        civicAddressString = "<cl:civicAddress>\n" +
                "                        <cl:country>US</cl:country>\n" +
                "                        <cl:A1>New York</cl:A1>\n" +
                "                        <cl:A3>New York</cl:A3>\n" +
                "                        <cl:A6>Broadway</cl:A6>\n" +
                "                        <cl:HNO>123</cl:HNO>\n" +
                "                        <cl:LOC>Suite 75</cl:LOC>\n" +
                "                        <cl:PC>10027-0401</cl:PC>\n" +
                "                    </cl:civicAddress>";
        assertEquals(utranCivicAddress.getData(), civicAddressString.getBytes(StandardCharsets.UTF_8));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();

        ExtGeographicalInformationImpl locationEstimate = new ExtGeographicalInformationImpl(getExtGeographicalInformation());

        ProvideSubscriberLocationResponseImpl psl1 = new ProvideSubscriberLocationResponseImpl(locationEstimate, null, null, 15, null,
                null, false, null, false, null, null, false, null, null, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        psl1.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 2
        rawData = getEncodedDataSergey();

        PositioningDataInformationImpl geranPositioningData = new PositioningDataInformationImpl(
                getPositioningDataInformation());
        UtranPositioningDataInfoImpl utranPositioningData = new UtranPositioningDataInfoImpl(getUtranPositioningDataInfo());
        AddGeographicalInformationImpl additionalLocationEstimate = new AddGeographicalInformationImpl(
                getAddGeographicalInformation());
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(121, 1, 2222);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                laiFixedLength);
        VelocityEstimateImpl velocityEstimate = new VelocityEstimateImpl(getVelocityEstimate());
        GeranGANSSpositioningDataImpl geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(
                getGeranGANSSpositioningData());
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(
                getUtranGANSSpositioningData());
        ISDNAddressStringImpl isdnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "444666888");
        ServingNodeAddressImpl targetServingNodeForHandover = new ServingNodeAddressImpl(isdnNumber, true);
        UtranAdditionalPositioningData utranAdditionalPositioningData = null;
        Integer utranBaroPressureMeas = null;
        UtranCivicAddress utranCivicAddress = null;

        ProvideSubscriberLocationResponseImpl psl2 = new ProvideSubscriberLocationResponseImpl(locationEstimate, geranPositioningData, utranPositioningData, 15,
                additionalLocationEstimate, null, true, cellGlobalIdOrServiceAreaIdOrLAI, true,
                AccuracyFulfilmentIndicator.requestedAccuracyFulfilled, velocityEstimate, true, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl2.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3 with real data from Indian operator
        rawData = getEncodedDataIndia1();

        // Wireshark sample description corresponding to bytes from getEncodedDataIndia1():
        //    opCode: localValue (0)
        //        localValue: provideSubscriberLocation (83)
        //    locationEstimate: a00e2ab536495300372a00b343
        //        1010 .... = Location estimate: Ellipsoid Arc (10)
        //        0... .... = Sign of latitude: North (0)
        //        .000 1110 0010 1010 1011 0101 = Degrees of latitude: 928437 (9.96105 degrees)
        //        0011 0110 0100 1001 0101 0011 = Degrees of longitude: 3557715 (76.34029 degrees)
        //        Inner radius: 55
        //        .010 1010 = Uncertainty radius: 42
        //        Offset angle: 0
        //        Included angle: 179
        //        .100 0011 = Confidence(%): 67
        //        [Location OSM URI: https://www.openstreetmap.org/?mlat=9.96105&mlon=76.34029&zoom=12]
        //    ageOfLocationEstimate: 0
        //    cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //        cellGlobalIdOrServiceAreaIdFixedLength: 04f427096208d0
        TypeOfShape typeOfShape = TypeOfShape.EllipsoidArc;
        double latitude = 9.961048364639282;
        double longitude = 76.34028196334839;
        double uncertainty = 0;
        double uncertaintySemiMajorAxis = 0;
        double uncertaintySemiMinorAxis = 0;
        double angleOfMajorAxis = 0;
        int confidence = 67;
        int altitude = 0;
        double uncertaintyAltitude = 0;
        int innerRadius = 55;
        double uncertaintyRadius = 537.64; // r = 45((1+0.025)^42 -1) = 537.6369923749309
        double offsetAngle = 0.0;
        double includedAngle = 179;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
        Integer ageOfLocationEstimate = 0;
        geranPositioningData = null;
        utranPositioningData = null;
        additionalLocationEstimate = null;
        MAPExtensionContainer extensionContainer = null;
        boolean deferredMTLRResponseIndicator = false;
        CellGlobalIdOrServiceAreaIdFixedLength cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 72, 2402, 2256);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellCgiOrSaiFixedLength);
        boolean saiPresent = false;
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;
        velocityEstimate = null;
        boolean moLrShortCircuitIndicator = false;
        geranGANSSpositioningData = null;
        utranGANSSpositioningData = null;
        targetServingNodeForHandover = null;

        ProvideSubscriberLocationResponseImpl psl3 = new ProvideSubscriberLocationResponseImpl(locationEstimate, geranPositioningData, utranPositioningData,
                ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent, accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl3.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4 with real data from Indian operator
        rawData = getEncodedDataIndia2();

        // Wireshark sample description corresponding to bytes from getEncodedDataIndia2():
        //            opCode: localValue (0)
        //                localValue: provideSubscriberLocation (83)
        //            locationEstimate: 3022ee69346cbc23214650
        //                0011 .... = Location estimate: Ellipsoid point with uncertainty Ellipse (3)
        //                0... .... = Sign of latitude: North (0)
        //                .010 0010 1110 1110 0110 1001 = Degrees of latitude: 2289257 (24.56107 degrees)
        //                0011 0100 0110 1100 1011 1100 = Degrees of longitude: 3435708 (73.72230 degrees)
        //                .010 0011 = Uncertainty semi-major: 35 (271.0 m)
        //                .010 0001 = Uncertainty semi-minor: 33 (222.3 m)
        //                Orientation of major axis: 70
        //                .101 0000 = Confidence(%): 80
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=24.56107&mlon=73.72230&zoom=12]
        //            ageOfLocationEstimate: 0
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 04f4951bb251bb}
        typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyEllipse;
        latitude = 24.56107;
        longitude = 73.72230;
        uncertainty = 0;
        uncertaintySemiMajorAxis = 271.03;
        uncertaintySemiMinorAxis = 222.3;
        angleOfMajorAxis = 70;
        confidence = 80;
        uncertaintyAltitude = 0;
        innerRadius = 0;
        uncertaintyRadius = 0;
        offsetAngle = 0;
        includedAngle = 0;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
        cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 59, 7090, 20923);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellCgiOrSaiFixedLength);

        ProvideSubscriberLocationResponseImpl psl4 = new ProvideSubscriberLocationResponseImpl(locationEstimate, geranPositioningData, utranPositioningData,
                ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent, accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl4.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 with real data from Indian operator
        rawData = getEncodedDataIndiaWithGERANPositioningData();

        // Wireshark sample description corresponding to bytes from getEncodedDataIndiaWithGERANPositioningData():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: 100f037f36327f23
        //                0001 .... = Location estimate: Ellipsoid point with uncertainty Circle (1)
        //                0... .... = Sign of latitude: North (0)
        //                .000 1111 0000 0011 0111 1111 = Degrees of latitude: 983935 (10.55648 degrees)
        //                0011 0110 0011 0010 0111 1111 = Degrees of longitude: 3551871 (76.21489 degrees)
        //                .010 0011 = Uncertainty code: 35 (271.0 m)
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=10.55648&mlon=76.21489&zoom=12]
        //            ageOfLocationEstimate: 0
        //            geranPositioningData: 0003
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 04f42713942dc1
        typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
        latitude = 10.55648;
        longitude = 76.21489;
        uncertainty = 271.03;
        uncertaintySemiMajorAxis = 0;
        uncertaintySemiMinorAxis = 0;
        angleOfMajorAxis = 0;
        confidence = 0;
        uncertaintyAltitude = 0;
        uncertaintyRadius = 0;
        offsetAngle = 0;
        includedAngle = 0;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
        geranPositioningData = new PositioningDataInformationImpl(new byte[] {0x00, 0x03});
        cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 72, 5012, 11713);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellCgiOrSaiFixedLength);

        ProvideSubscriberLocationResponseImpl psl5 = new ProvideSubscriberLocationResponseImpl(locationEstimate, geranPositioningData, utranPositioningData,
                ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent, accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl5.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 6 from LSM load
        rawData = getEncodedDataLSMLoadTest1();

        // Wireshark sample description corresponding to bytes from getEncodedDataLSMLoadTest1():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: a0b1b13fd8f321000501141402
        //                1010 .... = Location estimate: Ellipsoid Arc (10)
        //                1... .... = Sign of latitude: South (1)
        //                .011 0001 1011 0001 0011 1111 = Degrees of latitude: 3256639 (-34.93995 degrees)
        //                1101 1000 1111 0011 0010 0001 = Degrees of longitude: -2559199 (-54.91446 degrees)
        //                Inner radius: 5
        //                .000 0001 = Uncertainty radius: 1
        //                Offset angle: 20
        //                Included angle: 20
        //                .000 0010 = Confidence(%): 2
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=-34.93995&mlon=-54.91446&zoom=12]
        //            ageOfLocationEstimate: 10
        //            deferredmt-lrResponseIndicator
        //            utranPositioningData: 0000434b00622b
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 47f810000b12cc
        //            sai-Present
        //            accuracyFulfilmentIndicator: requestedAccuracyFulfilled (0)
        //            velocityEstimate: 30030065020501
        //            mo-lrShortCircuitIndicator
        //            utranGANSSpositioningData: 01638b0203
        //            targetServingNodeForHandover: msc-Number (0)
        //                msc-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //            utranAdditionalPositioningData: 578f
        //            utranBaroPressureMeas: 110000
        //            utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202
        //            0202020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a202020202020202020202020202020202
        //            0202020202020203c636c3a41313e4e657720596f
        typeOfShape = TypeOfShape.EllipsoidArc;
        latitude = -34.939956;
        longitude = -54.914474;
        innerRadius = 5;
        uncertaintyRadius = 1.50;
        offsetAngle = 20.0;
        includedAngle = 20.0;
        confidence = 2;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis, uncertaintySemiMinorAxis,
                angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle,
                includedAngle);
        geranPositioningData = null;
        utranPositioningData = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
        ageOfLocationEstimate = 10;
        deferredMTLRResponseIndicator = true;
        cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(748, 1, 11, 4812);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellCgiOrSaiFixedLength);
        saiPresent = true;
        accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;
        VelocityType velocityType = VelocityType.HorizontalWithVerticalVelocityAndUncertainty;
        int horizontalSpeed = 101;
        int bearing = 3;
        int verticalSpeed = 2;
        int uncertaintyHorizontalSpeed = 5;
        int uncertaintyVerticalSpeed = 1;
        velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
        moLrShortCircuitIndicator = true;
        utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03});
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "491710460015");
        targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, true);
        utranAdditionalPositioningData = new UtranAdditionalPositioningDataImpl(new byte[] {0x57, (byte) 0x8F});
        utranBaroPressureMeas = 110000;
        String civicAddressString = "<cl:civicAddress>\n" +
                "                        <cl:country>US</cl:country>\n" +
                "                        <cl:A1>New York</cl:A1>\n" +
                "                        <cl:A3>New York</cl:A3>\n" +
                "                        <cl:A6>Broadway</cl:A6>\n" +
                "                        <cl:HNO>123</cl:HNO>\n" +
                "                        <cl:LOC>Suite 75</cl:LOC>\n" +
                "                        <cl:PC>10027-0401</cl:PC>\n" +
                "                    </cl:civicAddress>";
        byte[] civicAddressByteArray = civicAddressString.getBytes(StandardCharsets.UTF_8);
        utranCivicAddress = new UtranCivicAddressImpl(civicAddressByteArray);

        ProvideSubscriberLocationResponseImpl psl6 = new ProvideSubscriberLocationResponseImpl(locationEstimate, geranPositioningData, utranPositioningData,
                ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent, accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl6.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 7 from LSM load
        rawData = getEncodedDataLSMLoadTest2();

        // Wireshark sample description corresponding to bytes from getEncodedDataLSMLoadTest2():
        // Component: returnResultLast (2)
        //    returnResultLast
        //        invokeID: 0
        //        resultretres
        //            opCode: localValue (0)
        //            locationEstimate: 00000000000000
        //                0000 .... = Location estimate: Ellipsoid Point (0)
        //                0... .... = Sign of latitude: North (0)
        //                .000 0000 0000 0000 0000 0000 = Degrees of latitude: 0 (0.00000 degrees)
        //                0000 0000 0000 0000 0000 0000 = Degrees of longitude: 0 (0.00000 degrees)
        //                [Location OSM URI: https://www.openstreetmap.org/?mlat=0.00000&mlon=0.00000&zoom=12]
        //            ageOfLocationEstimate: 0
        //            add-LocationEstimate: 568422683254be844a8a32482a842eed3215c58452d632433f8454a632468f844043327d28
        //            deferredmt-lrResponseIndicator
        //            geranPositioningData: 00031b212b3a4360
        //            cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
        //                cellGlobalIdOrServiceAreaIdFixedLength: 47f81000773be8
        //            accuracyFulfilmentIndicator: requestedAccuracyFulfilled (0)
        //            velocityEstimate: 30030065020501
        //            mo-lrShortCircuitIndicator
        //            geranGANSSpositioningData: 00638b0203
        //            targetServingNodeForHandover: msc-Number (0)
        //                msc-Number: 91947101640051
        //                    1... .... = Extension: No Extension
        //                    .001 .... = Nature of number: International Number (0x1)
        //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
        //                    E.164 number (MSISDN): 491710460015
        //            utranBaroPressureMeas: 110000
        //            utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202
        //            0202020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a202020202020202020202020202020202
        //            0202020202020203c636c3a41313e4e657720596f
        ExtGeographicalInformation extGeographicalInformation = null;
        try {
            latitude = 0.0;
            longitude = 0.0;
            extGeographicalInformation = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(latitude, longitude);
        } catch (MAPException e) {
            logger.error(e.getMessage());
        }
        geranPositioningData = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
        utranPositioningData = null;
        ageOfLocationEstimate = 0;
        EllipsoidPoint ellipsoidPoint1 = new EllipsoidPoint(-2.907010, 70.778014);
        EllipsoidPoint ellipsoidPoint2 = new EllipsoidPoint(-3.017238, 70.708922);
        EllipsoidPoint ellipsoidPoint3 = new EllipsoidPoint(-2.941387, 70.432091);
        EllipsoidPoint ellipsoidPoint4 = new EllipsoidPoint(-3.040019, 70.681903);
        EllipsoidPoint ellipsoidPoint5 = new EllipsoidPoint(-3.045001, 70.700109);
        EllipsoidPoint ellipsoidPoint6 = new EllipsoidPoint(-2.989001, 71.000004);
        EllipsoidPoint[] ellipsoidPoints = {ellipsoidPoint1, ellipsoidPoint2, ellipsoidPoint3, ellipsoidPoint4, ellipsoidPoint5, ellipsoidPoint6};
        PolygonImpl polygon6 = new PolygonImpl();
        polygon6.setData(ellipsoidPoints);
        additionalLocationEstimate = new AddGeographicalInformationImpl(polygon6.getData());
        cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(748, 1, 119, 15336);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellCgiOrSaiFixedLength);
        saiPresent = false;
        geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03});
        utranGANSSpositioningData = null;
        utranAdditionalPositioningData = null;

        ProvideSubscriberLocationResponseImpl psl7 = new ProvideSubscriberLocationResponseImpl(extGeographicalInformation, geranPositioningData, utranPositioningData,
                ageOfLocationEstimate, additionalLocationEstimate, extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI,
                saiPresent, accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl7.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
