package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.Multimap;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.Polygon;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingOptionMilliseconds;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.testng.Assert;
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
public class SubscriberLocationReportRequestTest {

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

    public byte[] getEncodedDataSergey() {
        return new byte[] { 48, -127, -92, 10, 1, 0, 48, 3, -128, 1, 2, 48, 8, 4, 6, -111, 68, 68, 84, 85, 85, -128, 6, -111,
                102, 102, 118, 119, 119, -127, 5, 33, 67, 21, 50, 84, -126, 8, 33, 67, 101, -121, 9, 33, 67, 101, -125, 6,
                -111, -120, -120, -104, -103, -103, -124, 6, -111, -120, -120, 8, 0, 0, -123, 1, 11, -122, 1, 5, -89, 4, -95,
                2, -128, 0, -120, 1, 12, -87, 14, 3, 2, 4, -128, -95, 8, 4, 6, -111, 68, 68, 84, 85, 85, -118, 1, 6, -117, 2,
                13, 14, -116, 3, 15, 16, 17, -83, 7, -127, 5, 34, -16, 33, 16, -31, -114, 5, 21, 22, 23, 24, 25, -113, 1, 7,
                -111, 0, -110, 0, -109, 1, 0, -108, 4, 0, 27, 28, 29, -107, 1, 9, -74, 6, 2, 1, 10, 2, 1, 11, -105, 0, -104,
                2, 31, 32, -103, 1, 33, -70, 8, -128, 6, -111, -111, -126, 115, 100, -11 };
    }

    private byte[] getEncodedDataIndia1() {
        return new byte[] { 0x30, 0x4b,
                0x0a, 0x01, 0x00, 0x30, 0x03, (byte) 0x80, 0x01, 0x00,
                0x30, 0x0f, 0x04, 0x07, (byte) 0x91, 0x19, 0x49, 0x15,
                (byte) 0x99, (byte) 0x99, 0x26, (byte) 0x80, 0x04, 0x1f, (byte) 0xb2, (byte) 0x82,
                0x00, (byte) 0x80, 0x07, (byte) 0x91, 0x19, 0x49, 0x51, 0x75,
                (byte) 0x98, 0x24, (byte) 0x81, 0x08, 0x04, 0x54, 0x65, 0x41,
                (byte) 0x80, 0x53, 0x39, (byte) 0xf1, (byte) 0x85, 0x0d, (byte) 0xa0, 0x26,
                0x35, (byte) 0x8a, 0x39, (byte) 0x9e, (byte) 0xce, 0x00, 0x6e, 0x11,
                0x46, 0x10, 0x00, (byte) 0x86, 0x01, 0x00, (byte) 0xad, 0x09,
                (byte) 0x80, 0x07, 0x04, (byte) 0xf4, 0x55, 0x71, (byte) 0xb1, 0x30,
                (byte) 0xcc, (byte) 0x91, 0x00 };
    }

    private byte[] getEncodedDataIndiaWAddLocationEstimate_Polygon() {
        return new byte[] { 0x30, 0x52,
                0x0a, 0x01, 0x00, 0x30, 0x03, (byte) 0x80, 0x01, 0x00,
                0x30, 0x0f, 0x04, 0x07, (byte) 0x91, 0x19, 0x49, 0x15,
                (byte) 0x99, (byte) 0x99, (byte) 0x86, (byte) 0x80, 0x04, 0x5b, 0x7c, 0x6c,
                0x00, (byte) 0x80, 0x07, (byte) 0x91, 0x19, 0x49, 0x51, 0x34,
                0x75, (byte) 0x94, (byte) 0x81, 0x08, 0x04, 0x54, 0x65, 0x42,
                0x00, 0x67, (byte) 0x95, (byte) 0xf1, (byte) 0x85, 0x01, 0x53, (byte) 0x86,
                0x01, 0x00, (byte) 0x88, 0x13, 0x53, 0x25, 0x5d, 0x19,
                0x39, 0x33, 0x11, 0x25, 0x5d, 0x19, 0x39, 0x33,
                0x11, 0x25, 0x5e, (byte) 0xe4, 0x39, 0x33, 0x28, (byte) 0xad,
                0x09, (byte) 0x80, 0x07, 0x04, (byte) 0xf4, 0x55, 0x08, 0x2f,
                0x6d, 0x1b
        };
    }

    private byte[] getEncodedDataLSMLoadTestPeriodicLDR() {
        return new byte[] { 0x30, (byte) 0x82,
                0x03, (byte) 0xfa, 0x0a, 0x01, 0x03, 0x30, 0x28, (byte) 0x80,
                0x01, 0x01, (byte) 0xa1, 0x06, (byte) 0x80, 0x04, (byte) 0x91, 0x44,
                0x54, 0x76, (byte) 0x82, 0x04, (byte) 0x91, 0x12, 0x09, 0x32,
                (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x0b, (byte) 0x80, 0x01, 0x0f,
                (byte) 0x82, 0x03, (byte) 0xb9, 0x58, 0x0c, (byte) 0x83, 0x01, 0x03,
                (byte) 0x85, 0x05, 0x04, 0x65, 0x39, 0x31, 0x31, 0x30,
                (byte) 0x81, (byte) 0xd2, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x64, 0x00, 0x51, (byte) 0x82, 0x00, (byte) 0xa3, 0x09, (byte) 0x81,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x52,
                (byte) 0x84, 0x02, 0x03, (byte) 0xf0, (byte) 0x85, 0x02, 0x03, (byte) 0xf8,
                (byte) 0x86, 0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33,
                0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33, 0x30,
                0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65,
                0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67,
                (byte) 0x88, 0x29, 0x61, 0x61, 0x61, 0x33, 0x30, 0x30,
                0x30, 0x2e, 0x61, 0x61, 0x61, 0x2e, 0x6d, 0x6e,
                0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63,
                0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70,
                0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e,
                0x6f, 0x72, 0x67, (byte) 0x89, 0x2c, 0x6d, 0x6d, 0x65,
                0x2e, 0x32, 0x30, 0x2e, 0x6d, 0x61, 0x67, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x8a, 0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x08, 0x47, 0x08,
                0x22, 0x29, 0x04, (byte) 0x82, 0x28, (byte) 0xf8, (byte) 0x82, 0x08,
                0x01, 0x70, 0x61, 0x18, (byte) 0x93, (byte) 0x82, (byte) 0x82, (byte) 0xf2,
                (byte) 0x85, 0x07, 0x00, 0x31, (byte) 0xa6, 0x3f, (byte) 0xd8, 0x12,
                (byte) 0xe0, (byte) 0x86, 0x01, 0x00, (byte) 0xa7, 0x04, (byte) 0xa1, 0x02,
                (byte) 0x80, 0x00, (byte) 0xa9, (byte) 0x81, (byte) 0xdc, 0x03, 0x02, 0x03,
                0x08, (byte) 0x80, 0x01, 0x03, (byte) 0xa1, (byte) 0x81, (byte) 0xd2, 0x04,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x51,
                (byte) 0x82, 0x00, (byte) 0xa3, 0x09, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, 0x52, (byte) 0x84, 0x02, 0x03,
                (byte) 0xf0, (byte) 0x85, 0x02, 0x03, (byte) 0xf8, (byte) 0x86, 0x36, 0x6d,
                0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d,
                0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e,
                0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x88, 0x29, 0x61,
                0x61, 0x61, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x61,
                0x61, 0x61, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67,
                (byte) 0x89, 0x2c, 0x6d, 0x6d, 0x65, 0x2e, 0x32, 0x30,
                0x2e, 0x6d, 0x61, 0x67, 0x2e, 0x65, 0x70, 0x63,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x8a, 0x21,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x8a, 0x01, 0x29, (byte) 0x8c, 0x07, 0x00, 0x00,
                0x43, 0x4b, 0x00, 0x62, 0x2b, (byte) 0xad, 0x09, (byte) 0x80,
                0x07, 0x47, (byte) 0xf8, 0x70, 0x20, 0x79, 0x24, 0x41,
                (byte) 0x8e, 0x05, 0x04, 0x0a, 0x00, 0x00, 0x0e, (byte) 0x8f,
                0x01, 0x01, (byte) 0x91, 0x00, (byte) 0x93, 0x01, 0x01, (byte) 0x94,
                0x07, 0x30, 0x03, 0x00, 0x65, 0x02, 0x05, 0x01,
                (byte) 0x95, 0x01, 0x01, (byte) 0xb6, 0x11, 0x02, 0x01, 0x03,
                0x02, 0x02, 0x02, 0x58, (byte) 0xa0, 0x08, 0x02, 0x03,
                0x0d, 0x2e, (byte) 0xff, 0x02, 0x01, 0x64, (byte) 0x97, 0x00,
                (byte) 0x99, 0x05, 0x01, 0x63, (byte) 0x8b, 0x02, 0x03, (byte) 0xba,
                0x09, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64,
                0x00, 0x51, (byte) 0x9b, 0x02, 0x57, (byte) 0x8f, (byte) 0x9c, 0x03,
                0x01, (byte) 0xad, (byte) 0xb0, (byte) 0x9d, (byte) 0x82, 0x01, (byte) 0x8d, 0x3c,
                0x63, 0x6c, 0x3a, 0x63, 0x69, 0x76, 0x69, 0x63,
                0x41, 0x64, 0x64, 0x72, 0x65, 0x73, 0x73, 0x3e,
                0x0a, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x63, 0x6f, 0x75,
                0x6e, 0x74, 0x72, 0x79, 0x3e, 0x55, 0x53, 0x3c,
                0x2f, 0x63, 0x6c, 0x3a, 0x63, 0x6f, 0x75, 0x6e,
                0x74, 0x72, 0x79, 0x3e, 0x0a, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c,
                0x3a, 0x41, 0x31, 0x3e, 0x4e, 0x65, 0x77, 0x20,
                0x59, 0x6f, 0x72, 0x6b, 0x3c, 0x2f, 0x63, 0x6c,
                0x3a, 0x41, 0x31, 0x3e, 0x0a, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c,
                0x3a, 0x41, 0x33, 0x3e, 0x4e, 0x65, 0x77, 0x20,
                0x59, 0x6f, 0x72, 0x6b, 0x3c, 0x2f, 0x63, 0x6c,
                0x3a, 0x41, 0x33, 0x3e, 0x0a, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c,
                0x3a, 0x41, 0x36, 0x3e, 0x42, 0x72, 0x6f, 0x61,
                0x64, 0x77, 0x61, 0x79, 0x3c, 0x2f, 0x63, 0x6c,
                0x3a, 0x41, 0x36, 0x3e, 0x0a, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c,
                0x3a, 0x48, 0x4e, 0x4f, 0x3e, 0x31, 0x32, 0x33,
                0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x48, 0x4e, 0x4f,
                0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a, 0x4c, 0x4f,
                0x43, 0x3e, 0x53, 0x75, 0x69, 0x74, 0x65, 0x20,
                0x37, 0x35, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x4c,
                0x4f, 0x43, 0x3e, 0x0a, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x3c, 0x63, 0x6c, 0x3a,
                0x50, 0x43, 0x3e, 0x31, 0x30, 0x30, 0x32, 0x37,
                0x2d, 0x30, 0x34, 0x30, 0x31, 0x3c, 0x2f, 0x63,
                0x6c, 0x3a, 0x50, 0x43, 0x3e, 0x0a, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x3c, 0x2f, 0x63, 0x6c, 0x3a, 0x63,
                0x69, 0x76, 0x69, 0x63, 0x41, 0x64, 0x64, 0x72,
                0x65, 0x73, 0x73, 0x3e
        };
    }

    private byte[] getEncodedLSMLoadTestBeingInsideArea() {
        return new byte[] { 0x30, (byte) 0x82,
                0x02, 0x70, 0x0a, 0x01, 0x03, 0x30, 0x28, (byte) 0x80,
                0x01, 0x01, (byte) 0xa1, 0x06, (byte) 0x80, 0x04, (byte) 0x91, 0x44,
                0x54, 0x76, (byte) 0x82, 0x04, (byte) 0x91, 0x12, 0x09, 0x32,
                (byte) 0x83, 0x01, 0x00, (byte) 0xa4, 0x0b, (byte) 0x80, 0x01, 0x0f,
                (byte) 0x82, 0x03, (byte) 0xb9, 0x58, 0x0c, (byte) 0x83, 0x01, 0x03,
                (byte) 0x85, 0x05, 0x04, 0x65, 0x39, 0x31, 0x31, 0x30,
                (byte) 0x81, (byte) 0xd8, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x64, 0x00, 0x51, (byte) 0x80, 0x04, 0x72, 0x02, (byte) 0xeb,
                0x37, (byte) 0x82, 0x00, (byte) 0xa3, 0x09, (byte) 0x81, 0x07, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x52, (byte) 0x84, 0x02,
                0x03, (byte) 0xf0, (byte) 0x85, 0x02, 0x03, (byte) 0xf8, (byte) 0x86, 0x36,
                0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d,
                0x6d, 0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30,
                0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x88, 0x29,
                0x61, 0x61, 0x61, 0x33, 0x30, 0x30, 0x30, 0x2e,
                0x61, 0x61, 0x61, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x89, 0x2c, 0x6d, 0x6d, 0x65, 0x2e, 0x32,
                0x30, 0x2e, 0x6d, 0x61, 0x67, 0x2e, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x8a,
                0x21, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x81, 0x08, 0x47, 0x08, 0x72, 0x76,
                0x53, 0x15, 0x27, (byte) 0xf9, (byte) 0x82, 0x08, 0x01, 0x70,
                0x01, 0x74, (byte) 0x86, (byte) 0x88, 0x52, (byte) 0xf1, (byte) 0x85, 0x07,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x86,
                0x01, 0x00, (byte) 0xa7, 0x04, (byte) 0xa1, 0x02, (byte) 0x80, 0x00,
                (byte) 0x88, 0x19, 0x54, 0x25, (byte) 0xe5, (byte) 0xb3, 0x34, 0x42,
                (byte) 0xd3, 0x25, (byte) 0xe6, 0x40, 0x34, 0x43, 0x7c, 0x25,
                (byte) 0xe6, (byte) 0x83, 0x34, 0x43, 0x79, 0x25, (byte) 0xe6, (byte) 0x84,
                0x34, 0x43, 0x7d, (byte) 0xa9, (byte) 0x81, (byte) 0xe2, 0x03, 0x02,
                0x03, 0x10, (byte) 0x80, 0x01, 0x04, (byte) 0xa1, (byte) 0x81, (byte) 0xd8,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                0x51, (byte) 0x80, 0x04, 0x72, 0x02, (byte) 0xeb, 0x37, (byte) 0x82,
                0x00, (byte) 0xa3, 0x09, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
                0x01, 0x64, 0x00, 0x52, (byte) 0x84, 0x02, 0x03, (byte) 0xf0,
                (byte) 0x85, 0x02, 0x03, (byte) 0xf8, (byte) 0x86, 0x36, 0x6d, 0x6d,
                0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d, 0x65,
                0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x6d,
                0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x88, 0x29, 0x61, 0x61,
                0x61, 0x33, 0x30, 0x30, 0x30, 0x2e, 0x61, 0x61,
                0x61, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x89,
                0x2c, 0x6d, 0x6d, 0x65, 0x2e, 0x32, 0x30, 0x2e,
                0x6d, 0x61, 0x67, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x8a, 0x21, 0x65,
                0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30,
                0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38,
                0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74,
                0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67,
                (byte) 0x8a, 0x01, 0x49, (byte) 0x8b, 0x08, 0x00, 0x03, 0x1b,
                0x21, 0x2b, 0x3a, 0x43, 0x60, (byte) 0xad, 0x09, (byte) 0x80,
                0x07, 0x47, (byte) 0xf8, 0x70, 0x22, 0x74, 0x26, 0x14,
                (byte) 0x8e, 0x05, 0x04, 0x0a, 0x00, 0x00, 0x0e, (byte) 0x8f,
                0x01, 0x01, (byte) 0x93, 0x01, 0x01, (byte) 0x94, 0x07, 0x30,
                0x03, 0x00, 0x65, 0x02, 0x05, 0x01, (byte) 0x97, 0x00,
                (byte) 0x98, 0x05, 0x00, 0x63, (byte) 0x8b, 0x02, 0x03, (byte) 0xba,
                0x09, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64,
                0x00, 0x51
        };
    }

    public byte[] getDataExtGeographicalInformation() {
        return new byte[] { 11 };
    }

    public byte[] getDataAddGeographicalInformation() {
        return new byte[] { 12 };
    }

    public byte[] getPositioningDataInformation() {
        return new byte[] { 13, 14 };
    }

    public byte[] getUtranPositioningDataInfo() {
        return new byte[] { 15, 16, 17 };
    }

    public byte[] getGSNAddress() {
        return new byte[] { 21, 22, 23, 24, 25 };
    }

    public byte[] getVelocityEstimate() {
        return new byte[] { 0, 27, 28, 29 };
    }

    public byte[] getGeranGANSSpositioningData() {
        return new byte[] { 31, 32 };
    }

    public byte[] getUtranGANSSpositioningData() {
        return new byte[] { 33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        // test 1
        byte[] data = getEncodedDataSergey();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl slr0 = new SubscriberLocationReportRequestImpl();
        slr0.decodeAll(asn);

        LCSEvent lcsEvent = slr0.getLCSEvent();
        LCSClientID lcsClientID = slr0.getLCSClientID();
        LCSLocationInfo lcsLocationInfo = slr0.getLCSLocationInfo();
        ISDNAddressString msisdn = slr0.getMSISDN();
        IMSI imsi = slr0.getIMSI();
        IMEI imei = slr0.getIMEI();
        ISDNAddressString naEsrd = slr0.getNaESRD();
        ISDNAddressString naEsrk = slr0.getNaESRK();
        ExtGeographicalInformation locationEstimate = slr0.getLocationEstimate();
        Integer ageOfLocationEstimate = slr0.getAgeOfLocationEstimate();
        SLRArgExtensionContainer slrArgExtensionContainer = slr0.getSLRArgExtensionContainer();
        AddGeographicalInformation addLocationEstimate = slr0.getAdditionalLocationEstimate();
        DeferredmtlrData deferredmtlrData = slr0.getDeferredmtlrData();
        Integer lcsReferenceNumber = slr0.getLCSReferenceNumber();
        PositioningDataInformation geranPositioningData = slr0.getGeranPositioningData();
        UtranPositioningDataInfo utranPositioningData = slr0.getUtranPositioningData();
        CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = slr0.getCellGlobalIdOrServiceAreaIdOrLAI();
        GSNAddress hGmlcAddress = slr0.getHGMLCAddress();
        Integer lcsServiceTypeID = slr0.getLCSServiceTypeID();
        boolean saiPresent = slr0.getSaiPresent();
        boolean pseudonymIndicator = slr0.getPseudonymIndicator();
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = slr0.getAccuracyFulfilmentIndicator();
        VelocityEstimate velocityEstimate = slr0.getVelocityEstimate();
        Integer sequenceNumber = slr0.getSequenceNumber();
        PeriodicLDRInfo periodicLDRInfo = slr0.getPeriodicLDRInfo();
        boolean moLrShortCircuitIndicator = slr0.getMoLrShortCircuitIndicator();
        GeranGANSSpositioningData geranGANSSpositioningData = slr0.getGeranGANSSpositioningData();
        UtranGANSSpositioningData utranGANSSpositioningData = slr0.getUtranGANSSpositioningData();
        ServingNodeAddress targetServingNodeForHandover = slr0.getTargetServingNodeForHandover();
        UtranAdditionalPositioningData utranAdditionalPositioningData = slr0.getUtranAdditionalPositioningData();
        Integer utranBaroPressureMeas = slr0.getUtranBaroPressureMeas();
        UtranCivicAddress utranCivicAddress = slr0.getUtranCivicAddress();

        assertEquals(lcsEvent, LCSEvent.emergencyCallOrigination);
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.plmnOperatorServices);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddress(), "4444455555");
        assertEquals(msisdn.getAddress(), "6666677777");
        assertEquals(imsi.getData(), "1234512345");
        assertEquals(imei.getIMEI(), "1234567890123456");
        assertEquals(naEsrd.getAddress(), "8888899999");
        assertEquals(naEsrk.getAddress(), "8888800000");
        assertTrue(Arrays.equals(locationEstimate.getData(), getDataExtGeographicalInformation()));
        assertEquals(ageOfLocationEstimate.intValue(), 5);
        assertTrue(slrArgExtensionContainer.getSlrArgPcsExtensions().getNaEsrkRequest());
        assertTrue(Arrays.equals(addLocationEstimate.getData(), getDataAddGeographicalInformation()));
        assertTrue(deferredmtlrData.getDeferredLocationEventType().getMsAvailable());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea());
        assertEquals(lcsReferenceNumber.intValue(), 6);
        assertTrue(Arrays.equals(geranPositioningData.getData(), getPositioningDataInformation()));
        assertTrue(Arrays.equals(utranPositioningData.getData(), getUtranPositioningDataInfo()));
        assertEquals(cellIdOrSai.getLAIFixedLength().getMCC(), 220);
        assertEquals(cellIdOrSai.getLAIFixedLength().getMNC(), 12);
        assertEquals(cellIdOrSai.getLAIFixedLength().getLac(), 4321);
        assertTrue(Arrays.equals(hGmlcAddress.getData(), getGSNAddress()));
        assertEquals(lcsServiceTypeID.intValue(), 7);
        assertTrue(saiPresent);
        assertTrue(pseudonymIndicator);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyFulfilled);
        assertTrue(Arrays.equals(velocityEstimate.getData(), getVelocityEstimate()));
        assertEquals(sequenceNumber.intValue(), 9);
        assertEquals(periodicLDRInfo.getReportingAmount(), 10);
        assertEquals(periodicLDRInfo.getReportingInterval(), 11);
        assertNull(periodicLDRInfo.getReportingOptionMilliseconds());
        assertTrue(moLrShortCircuitIndicator);
        assertTrue(Arrays.equals(geranGANSSpositioningData.getData(), getGeranGANSSpositioningData()));
        assertTrue(Arrays.equals(utranGANSSpositioningData.getData(), getUtranGANSSpositioningData()));
        assertEquals(targetServingNodeForHandover.getMscNumber().getAddress(), "192837465");
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 2 with real data from Indian operator with Ellipsoid Arc in location estimate
        data = getEncodedDataIndia1();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl slr1 = new SubscriberLocationReportRequestImpl();
        slr1.decodeAll(asn);

        lcsEvent = slr1.getLCSEvent();
        lcsClientID = slr1.getLCSClientID();
        lcsLocationInfo = slr1.getLCSLocationInfo();
        msisdn = slr1.getMSISDN();
        imsi = slr1.getIMSI();
        imei = slr1.getIMEI();
        naEsrd = slr1.getNaESRD();
        naEsrk = slr1.getNaESRK();
        locationEstimate = slr1.getLocationEstimate();
        ageOfLocationEstimate = slr1.getAgeOfLocationEstimate();
        slrArgExtensionContainer = slr1.getSLRArgExtensionContainer();
        addLocationEstimate = slr1.getAdditionalLocationEstimate();
        deferredmtlrData = slr1.getDeferredmtlrData();
        lcsReferenceNumber = slr1.getLCSReferenceNumber();
        geranPositioningData = slr1.getGeranPositioningData();
        utranPositioningData = slr1.getUtranPositioningData();
        cellIdOrSai = slr1.getCellGlobalIdOrServiceAreaIdOrLAI();
        hGmlcAddress = slr1.getHGMLCAddress();
        lcsServiceTypeID = slr1.getLCSServiceTypeID();
        saiPresent = slr1.getSaiPresent();
        pseudonymIndicator = slr1.getPseudonymIndicator();
        accuracyFulfilmentIndicator = slr1.getAccuracyFulfilmentIndicator();
        velocityEstimate = slr1.getVelocityEstimate();
        sequenceNumber = slr1.getSequenceNumber();
        periodicLDRInfo = slr1.getPeriodicLDRInfo();
        moLrShortCircuitIndicator = slr1.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = slr1.getGeranGANSSpositioningData();
        utranGANSSpositioningData = slr1.getUtranGANSSpositioningData();
        targetServingNodeForHandover = slr1.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = slr1.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = slr1.getUtranBaroPressureMeas();
        utranCivicAddress = slr1.getUtranCivicAddress();

        /*
         * invoke
         *     invokeID: 1
         *     opCode: localValue (0)
         *         localValue: subscriberLocationReport (86)
         *     lcs-Event: emergencyCallOrigination (0)
         *     lcs-ClientID
         *         lcsClientType: emergencyServices (0)
         *     lcsLocationInfo
         *         networkNode-Number: 91194915999926
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 919451999962
         *         lmsi: 1fb28200
         *     msisdn: 91194951759824
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 919415578942
         *     IMSI: 404556140835931
         *     [Association IMSI: 404556140835931]
         *         Mobile Country Code (MCC): India (404)
         *         Mobile Network Code (MNC): BSNL, UP (East) (55)
         *     locationEstimate: a026358a399ece006e11461000
         *         1010 .... = Location estimate: Ellipsoid Arc (10)
         *         0... .... = Sign of latitude: North (0)
         *         .010 0110 0011 0101 1000 1010 = Degrees of latitude: 2504074 (26.86580 degrees)
         *         0011 1001 1001 1110 1100 1110 = Degrees of longitude: 3776206 (81.02860 degrees)
         *         Inner radius: 110
         *         .001 0001 = Uncertainty radius: 17
         *         Offset angle: 70
         *         Included angle: 16
         *         .000 0000 = Confidence(%): 0
         *         [Location OSM URI: https://www.openstreetmap.org/?mlat=26.86580&mlon=81.02860&zoom=12]
         *     ageOfLocationEstimate: 0
         *     cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *         cellGlobalIdOrServiceAreaIdFixedLength: 04f45571b130cc
         *     sai-Present
         */
        assertEquals(lcsEvent, LCSEvent.emergencyCallOrigination);
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.emergencyServices);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddress(), "919451999962");
        assertEquals(lcsLocationInfo.getLMSI().getData(), new byte[] { 0x1f, (byte) 0xb2, (byte) 0x82, 0x00});
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "919415578942");
        assertEquals(imsi.getData(), "404556140835931");
        assertNull(imei);
        assertNull(naEsrd);
        assertNull(naEsrk);
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(locationEstimate.getLatitude() - 26.86580) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - 81.02860) < 0.00001);
        assertEquals(locationEstimate.getInnerRadius(), 110);
        assertTrue(Math.abs(locationEstimate.getUncertaintyRadius() - 40.54) < 0.01); // r = 45((1+0.025)^17 -1)
        assertEquals(locationEstimate.getOffsetAngle(), 70.0);
        assertEquals(locationEstimate.getIncludedAngle(), 16.0);
        assertEquals(locationEstimate.getConfidence(), 0);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertNull(slrArgExtensionContainer);
        assertNull(addLocationEstimate);
        assertNull(deferredmtlrData);
        assertNull(lcsReferenceNumber);
        assertNull(geranPositioningData);
        assertNull(utranPositioningData);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 404);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 55);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 29105);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 12492);
        assertNull(hGmlcAddress);
        assertNull(lcsServiceTypeID);
        assertTrue(saiPresent);
        assertFalse(pseudonymIndicator);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertNull(sequenceNumber);
        assertNull(periodicLDRInfo);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 3 with real data from Indian operator with additional location estimate (polygon)
        data = getEncodedDataIndiaWAddLocationEstimate_Polygon();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl slr2 = new SubscriberLocationReportRequestImpl();
        slr2.decodeAll(asn);

        lcsEvent = slr2.getLCSEvent();
        lcsClientID = slr2.getLCSClientID();
        lcsLocationInfo = slr2.getLCSLocationInfo();
        msisdn = slr2.getMSISDN();
        imsi = slr2.getIMSI();
        imei = slr2.getIMEI();
        naEsrd = slr2.getNaESRD();
        naEsrk = slr2.getNaESRK();
        locationEstimate = slr2.getLocationEstimate();
        ageOfLocationEstimate = slr2.getAgeOfLocationEstimate();
        slrArgExtensionContainer = slr2.getSLRArgExtensionContainer();
        addLocationEstimate = slr2.getAdditionalLocationEstimate();
        deferredmtlrData = slr2.getDeferredmtlrData();
        lcsReferenceNumber = slr2.getLCSReferenceNumber();
        geranPositioningData = slr2.getGeranPositioningData();
        utranPositioningData = slr2.getUtranPositioningData();
        cellIdOrSai = slr2.getCellGlobalIdOrServiceAreaIdOrLAI();
        hGmlcAddress = slr2.getHGMLCAddress();
        lcsServiceTypeID = slr2.getLCSServiceTypeID();
        saiPresent = slr2.getSaiPresent();
        pseudonymIndicator = slr2.getPseudonymIndicator();
        accuracyFulfilmentIndicator = slr2.getAccuracyFulfilmentIndicator();
        velocityEstimate = slr2.getVelocityEstimate();
        sequenceNumber = slr2.getSequenceNumber();
        periodicLDRInfo = slr2.getPeriodicLDRInfo();
        moLrShortCircuitIndicator = slr2.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = slr2.getGeranGANSSpositioningData();
        utranGANSSpositioningData = slr2.getUtranGANSSpositioningData();
        targetServingNodeForHandover = slr2.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = slr2.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = slr2.getUtranBaroPressureMeas();
        utranCivicAddress = slr2.getUtranCivicAddress();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 1
         *         opCode: localValue (0)
         *             localValue: subscriberLocationReport (86)
         *         lcs-Event: emergencyCallOrigination (0)
         *         lcs-ClientID
         *             lcsClientType: emergencyServices (0)
         *         lcsLocationInfo
         *             networkNode-Number: 91194915999986
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 919451999968
         *             lmsi: 5b7c6c00
         *         msisdn: 91194951347594
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 919415435749
         *         IMSI: 404556240076591
         *         [Association IMSI: 404556240076591]
         *             Mobile Country Code (MCC): India (404)
         *             Mobile Network Code (MNC): BSNL, UP (East) (55)
         *         locationEstimate: 53
         *             0101 .... = Location estimate: Polygon (5)
         *         ageOfLocationEstimate: 0
         *         add-LocationEstimate: 53255d19393311255d19393311255ee4393328
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 04f455082f6d1b
         */
        assertEquals(lcsEvent, LCSEvent.emergencyCallOrigination);
        assertEquals(lcsClientID.getLCSClientType(), LCSClientType.emergencyServices);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddress(), "919451999968");
        assertEquals(lcsLocationInfo.getLMSI().getData(), new byte[] { 0x5b, (byte) 0x7c, (byte) 0x6c, 0x00 });
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "919415435749");
        assertEquals(imsi.getData(), "404556240076591");
        assertNull(imei);
        assertNull(naEsrd);
        assertNull(naEsrk);
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.Polygon);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertNull(slrArgExtensionContainer);
        assertEquals(addLocationEstimate.getData(), new byte[] { 0x53, 0x25, 0x5d, 0x19, 0x39, 0x33, 0x11, 0x25,
                0x5d, 0x19, 0x39, 0x33, 0x11, 0x25, 0x5e, (byte) 0xe4, 0x39, 0x33, 0x28 });
        PolygonImpl polygon = new PolygonImpl(addLocationEstimate.getData());
        assertEquals(polygon.getNumberOfPoints(), 3);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLatitude() - 26.271325) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLongitude() - 80.436766) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLatitude() - 26.271325) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLongitude() - 80.436766) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLatitude() - 26.276250) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLongitude() - 80.437260) < 0.000001);
        assertNull(deferredmtlrData);
        assertNull(lcsReferenceNumber);
        assertNull(geranPositioningData);
        assertNull(utranPositioningData);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 404);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 55);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 2095);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 27931);
        assertNull(hGmlcAddress);
        assertNull(lcsServiceTypeID);
        assertFalse(saiPresent);
        assertFalse(pseudonymIndicator);
        assertNull(accuracyFulfilmentIndicator);
        assertNull(velocityEstimate);
        assertNull(sequenceNumber);
        assertNull(periodicLDRInfo);
        assertFalse(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        assertNull(utranGANSSpositioningData);
        assertNull(targetServingNodeForHandover);
        assertNull(utranAdditionalPositioningData);
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);

        // test 4 with data from LSM load test (periodic LDR), UTRAN/UTRANGANSS/UTRANAdd position data
        data = getEncodedDataLSMLoadTestPeriodicLDR();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl slr3 = new SubscriberLocationReportRequestImpl();
        slr3.decodeAll(asn);

        lcsEvent = slr3.getLCSEvent();
        lcsClientID = slr3.getLCSClientID();
        lcsLocationInfo = slr3.getLCSLocationInfo();
        msisdn = slr3.getMSISDN();
        imsi = slr3.getIMSI();
        imei = slr3.getIMEI();
        naEsrd = slr3.getNaESRD();
        naEsrk = slr3.getNaESRK();
        locationEstimate = slr3.getLocationEstimate();
        ageOfLocationEstimate = slr3.getAgeOfLocationEstimate();
        slrArgExtensionContainer = slr3.getSLRArgExtensionContainer();
        addLocationEstimate = slr3.getAdditionalLocationEstimate();
        deferredmtlrData = slr3.getDeferredmtlrData();
        lcsReferenceNumber = slr3.getLCSReferenceNumber();
        geranPositioningData = slr3.getGeranPositioningData();
        utranPositioningData = slr3.getUtranPositioningData();
        cellIdOrSai = slr3.getCellGlobalIdOrServiceAreaIdOrLAI();
        hGmlcAddress = slr3.getHGMLCAddress();
        lcsServiceTypeID = slr3.getLCSServiceTypeID();
        saiPresent = slr3.getSaiPresent();
        pseudonymIndicator = slr3.getPseudonymIndicator();
        accuracyFulfilmentIndicator = slr3.getAccuracyFulfilmentIndicator();
        velocityEstimate = slr3.getVelocityEstimate();
        sequenceNumber = slr3.getSequenceNumber();
        periodicLDRInfo = slr3.getPeriodicLDRInfo();
        moLrShortCircuitIndicator = slr3.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = slr3.getGeranGANSSpositioningData();
        utranGANSSpositioningData = slr3.getUtranGANSSpositioningData();
        targetServingNodeForHandover = slr3.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = slr3.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = slr3.getUtranBaroPressureMeas();
        utranCivicAddress = slr3.getUtranCivicAddress();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: subscriberLocationReport (86)
         *         lcs-Event: deferredmt-lrResponse (3)
         *         lcs-ClientID
         *             lcsClientType: valueAddedServices (1)
         *             lcsClientExternalID
         *                 externalAddress: 91445476
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 444567
         *             lcsClientDialedByMS: 91120932
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 219023
         *                     Country Code: Spare code (219)
         *             lcsClientInternalID: broadcastService (0)
         *             lcsClientName
         *                 dataCodingScheme: 0f
         *                     0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
         *                     .... 1111 = Language: Language unspecified (15)
         *                 nameString: b9580c
         *                     USSD String: 911
         *                 lcs-FormatIndicator: url (3)
         *             lcsAPN: 0465393131 - e911
         *                 APN: e911
         *         lcsLocationInfo
         *             networkNode-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *             gprsNodeIndicator
         *             additional-Number: sgsn-Number (1)
         *                 sgsn-Number: 91947101640052
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460025
         *             Padding: 3
         *             supportedLCS-CapabilitySets: f0
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 0... = lcsCapabilitySet5: False
         *             Padding: 3
         *             additional-LCS-CapabilitySets: f8
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 1... = lcsCapabilitySet5: True
         *             mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *             sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *             sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         IMSI: 748022924028828
         *         [Association IMSI: 748022924028828]
         *         imei: 01706118938282f2
         *             TBCD digits: 100716813928282
         *         locationEstimate: 0031a63fd812e0
         *             0000 .... = Location estimate: Ellipsoid Point (0)
         *             0... .... = Sign of latitude: North (0)
         *             .011 0001 1010 0110 0011 1111 = Degrees of latitude: 3253823 (34.90974 degrees)
         *             1101 1000 0001 0010 1110 0000 = Degrees of longitude: -2616608 (-56.14632 degrees)
         *             [Location OSM URI: https://www.openstreetmap.org/?mlat=34.90974&mlon=-56.14632&zoom=12]
         *         ageOfLocationEstimate: 0
         *         slr-ArgExtensionContainer
         *             slr-Arg-PCS-Extensions
         *                 na-ESRK-Request
         *         deferredmt-lrData
         *             Padding: 3
         *             deferredLocationEventType: 08
         *                 0... .... = msAvailable: False
         *                 .0.. .... = enteringIntoArea: False
         *                 ..0. .... = leavingFromArea: False
         *                 ...0 .... = beingInsideArea: False
         *                 .... 1... = periodicLDR: True
         *             terminationCause: congestion (3)
         *             lcsLocationInfo
         *                 networkNode-Number: 91947101640051
         *                 gprsNodeIndicator
         *                 additional-Number: sgsn-Number (1)
         *                     sgsn-Number: 91947101640052
         *                 Padding: 3
         *                 supportedLCS-CapabilitySets: f0
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 0... = lcsCapabilitySet5: False
         *                 Padding: 3
         *                 additional-LCS-CapabilitySets: f8
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 1... = lcsCapabilitySet5: True
         *                 mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                 aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *                 sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *                 sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         lcs-ReferenceNumber: 29
         *         utranPositioningData: 0000434b00622b
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 47f87020792441
         *         h-gmlc-Address: 040a00000e
         *         lcsServiceTypeID: emergencyAlertServices (1)
         *         sai-Present
         *         accuracyFulfilmentIndicator: requestedAccuracyNotFulfilled (1)
         *         velocityEstimate: 30030065020501
         *         sequenceNumber: 1
         *         periodicLDRInfo
         *             reportingAmount: 3
         *             reportingInterval: 600
         *             BER Error: This field lies beyond the end of the known sequence definition.
         *         mo-lrShortCircuitIndicator
         *         utranGANSSpositioningData: 01638b0203
         *         targetServingNodeForHandover: msc-Number (0)
         *             msc-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *         utranAdditionalPositioningData: 578f
         *         utranBaroPressureMeas: 110000
         *         utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202020
         * 2020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a20202020202020202020202020202020202020202020202
         * 03c636c3a41313e4e657720596f
         */
        assertEquals(lcsEvent, LCSEvent.deferredmtlrResponse);
        LCSClientType lcsClientType = lcsClientID.getLCSClientType();
        assertEquals(lcsClientType, LCSClientType.valueAddedServices);
        LCSClientExternalID lcsClientExternalID = lcsClientID.getLCSClientExternalID();
        ISDNAddressString externalAddress = lcsClientExternalID.getExternalAddress();
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(externalAddress.getAddress(), "444567");
        assertFalse(externalAddress.isExtension());
        AddressString lcsClientDialedByMS = lcsClientID.getLCSClientDialedByMS();
        assertEquals(lcsClientDialedByMS.getAddressNature(), AddressNature.international_number);
        assertEquals(lcsClientDialedByMS.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsClientDialedByMS.getAddress(), "219023");
        assertFalse(lcsClientDialedByMS.isExtension());
        LCSClientInternalID lcsClientInternalID = lcsClientID.getLCSClientInternalID();
        assertEquals(lcsClientInternalID, LCSClientInternalID.broadcastService);
        LCSClientName lcsClientName = lcsClientID.getLCSClientName();
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "911");
        assertEquals(lcsClientName.getLCSFormatIndicator(), LCSFormatIndicator.url);
        APN apn = lcsClientID.getLCSAPN();
        assertEquals(apn.getApn(), "e911");
        LCSRequestorID lcsRequestorID = lcsClientID.getLCSRequestorID();
        assertNull(lcsRequestorID);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddress(), "491710460015");
        assertNull(lcsLocationInfo.getLMSI());
        assertTrue(lcsLocationInfo.getGprsNodeIndicator());
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getAddress(), "491710460025");
        assertFalse(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().isExtension());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
        assertFalse(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease6());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease7());
        DiameterIdentity mmeName = lcsLocationInfo.getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity aaaServerName = lcsLocationInfo.getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnName = lcsLocationInfo.getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        DiameterIdentity sgsnRealm = lcsLocationInfo.getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertNull(msisdn);
        assertNull(naEsrd);
        assertNull(naEsrk);
        assertEquals(imsi.getData(), "748022924028828");
        assertEquals(imei.getIMEI(), "100716813928282");
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(locationEstimate.getLatitude() - 34.90974) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - (-56.14632)) < 0.00001);
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertTrue(slrArgExtensionContainer.getSlrArgPcsExtensions().getNaEsrkRequest());
        assertNull(slrArgExtensionContainer.getPrivateExtensionList());
        assertNull(addLocationEstimate);
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getMsAvailable());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getEnteringIntoArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getLeavingFromArea());
        assertFalse(deferredmtlrData.getDeferredLocationEventType().getBeingInsideArea());
        assertTrue(deferredmtlrData.getDeferredLocationEventType().getPeriodicLDR());
        assertEquals(deferredmtlrData.getTerminationCause(), TerminationCause.congestion);
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
        mmeName = deferredmtlrData.getLCSLocationInfo().getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        aaaServerName = deferredmtlrData.getLCSLocationInfo().getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnName = deferredmtlrData.getLCSLocationInfo().getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnRealm = deferredmtlrData.getLCSLocationInfo().getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(lcsReferenceNumber.intValue(), 41); // 0x29 = 41
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
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 7);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 8313);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 9281);
        assertEquals(hGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(hGmlcAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        assertEquals(lcsServiceTypeID.intValue(), 1);
        assertTrue(saiPresent);
        assertFalse(pseudonymIndicator);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled);
        assertEquals(velocityEstimate.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(velocityEstimate.getHorizontalSpeed(), 101);
        assertEquals(velocityEstimate.getBearing(), 3);
        assertEquals(velocityEstimate.getVerticalSpeed(), 2);
        assertEquals(velocityEstimate.getUncertaintyHorizontalSpeed(), 5);
        assertEquals(velocityEstimate.getUncertaintyVerticalSpeed(), 1);
        assertEquals(sequenceNumber.intValue(), 1);
        assertEquals(periodicLDRInfo.getReportingAmount(), 3);
        assertEquals(periodicLDRInfo.getReportingInterval(), 600);
        assertEquals(periodicLDRInfo.getReportingOptionMilliseconds().getReportingAmountMilliseconds(), 863999);
        assertEquals(periodicLDRInfo.getReportingOptionMilliseconds().getReportingIntervalMilliseconds(), 100);
        assertTrue(moLrShortCircuitIndicator);
        assertNull(geranGANSSpositioningData);
        UtranGANSSpositioningDataImpl utranGanssPositioningData = new UtranGANSSpositioningDataImpl(utranGANSSpositioningData.getData());
        Multimap<String, String> methodsAndGanssIds = utranGanssPositioningData.getLocationGeneratedMethodsAndGANSSIds();
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
        Multimap<String, String> methodsAndAddPosIds = utranAdditionalPositioningDataImpl.getUtranAdditionalPositioningMethodsAndIds();
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

        // test 5 with data from LSM load test (being inside area), GERAN/GERANGANSS position data
        data = getEncodedLSMLoadTestBeingInsideArea();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        SubscriberLocationReportRequestImpl slr4 = new SubscriberLocationReportRequestImpl();
        slr4.decodeAll(asn);

        lcsEvent = slr4.getLCSEvent();
        lcsClientID = slr4.getLCSClientID();
        lcsLocationInfo = slr4.getLCSLocationInfo();
        msisdn = slr4.getMSISDN();
        imsi = slr4.getIMSI();
        imei = slr4.getIMEI();
        naEsrd = slr4.getNaESRD();
        naEsrk = slr4.getNaESRK();
        locationEstimate = slr4.getLocationEstimate();
        ageOfLocationEstimate = slr4.getAgeOfLocationEstimate();
        slrArgExtensionContainer = slr4.getSLRArgExtensionContainer();
        addLocationEstimate = slr4.getAdditionalLocationEstimate();
        deferredmtlrData = slr4.getDeferredmtlrData();
        lcsReferenceNumber = slr4.getLCSReferenceNumber();
        geranPositioningData = slr4.getGeranPositioningData();
        utranPositioningData = slr4.getUtranPositioningData();
        cellIdOrSai = slr4.getCellGlobalIdOrServiceAreaIdOrLAI();
        hGmlcAddress = slr4.getHGMLCAddress();
        lcsServiceTypeID = slr4.getLCSServiceTypeID();
        saiPresent = slr4.getSaiPresent();
        pseudonymIndicator = slr4.getPseudonymIndicator();
        accuracyFulfilmentIndicator = slr4.getAccuracyFulfilmentIndicator();
        velocityEstimate = slr4.getVelocityEstimate();
        sequenceNumber = slr4.getSequenceNumber();
        periodicLDRInfo = slr4.getPeriodicLDRInfo();
        moLrShortCircuitIndicator = slr4.getMoLrShortCircuitIndicator();
        geranGANSSpositioningData = slr4.getGeranGANSSpositioningData();
        utranGANSSpositioningData = slr4.getUtranGANSSpositioningData();
        targetServingNodeForHandover = slr4.getTargetServingNodeForHandover();
        utranAdditionalPositioningData = slr4.getUtranAdditionalPositioningData();
        utranBaroPressureMeas = slr4.getUtranBaroPressureMeas();
        utranCivicAddress = slr4.getUtranCivicAddress();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *         lcs-Event: deferredmt-lrResponse (3)
         *         lcs-ClientID
         *             lcsClientType: valueAddedServices (1)
         *             lcsClientExternalID
         *                 externalAddress: 91445476
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 444567
         *                         Country Code: United Kingdom of Great Britain and Northern Ireland (44)
         *             lcsClientDialedByMS: 91120932
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 219023
         *                     Country Code: Spare code (219)
         *             lcsClientInternalID: broadcastService (0)
         *             lcsClientName
         *                 dataCodingScheme: 0f
         *                     0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
         *                     .... 1111 = Language: Language unspecified (15)
         *                 nameString: b9580c
         *                     USSD String: 911
         *                 lcs-FormatIndicator: url (3)
         *             lcsAPN: 0465393131 - e911
         *                 APN: e911
         *         lcsLocationInfo
         *             networkNode-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *                     Country Code: Germany (Federal Republic of) (49)
         *             lmsi: 7202eb37
         *             gprsNodeIndicator
         *             additional-Number: sgsn-Number (1)
         *                 sgsn-Number: 91947101640052
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460025
         *                         Country Code: Germany (Federal Republic of) (49)
         *             Padding: 3
         *             supportedLCS-CapabilitySets: f0
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 0... = lcsCapabilitySet5: False
         *             Padding: 3
         *             additional-LCS-CapabilitySets: f8
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 1... = lcsCapabilitySet5: True
         *             mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *             sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *             sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         IMSI: 748027673551729
         *         [Association IMSI: 748027673551729]
         *         imei: 01700174868852f1
         *             TBCD digits: 100710476888251
         *         locationEstimate: 00000000000000
         *             0000 .... = Location estimate: Ellipsoid Point (0)
         *             0... .... = Sign of latitude: North (0)
         *             .000 0000 0000 0000 0000 0000 = Degrees of latitude: 0 (0.00000 degrees)
         *             0000 0000 0000 0000 0000 0000 = Degrees of longitude: 0 (0.00000 degrees)
         *             [Location OSM URI: https://www.openstreetmap.org/?mlat=0.00000&mlon=0.00000&zoom=12]
         *         ageOfLocationEstimate: 0
         *         slr-ArgExtensionContainer
         *             slr-Arg-PCS-Extensions
         *                 na-ESRK-Request
         *         add-LocationEstimate: 5425e5b33442d325e64034437c25e68334437925e68434437d
         *         deferredmt-lrData
         *             Padding: 3
         *             deferredLocationEventType: 10
         *                 0... .... = msAvailable: False
         *                 .0.. .... = enteringIntoArea: False
         *                 ..0. .... = leavingFromArea: False
         *                 ...1 .... = beingInsideArea: True
         *                 .... 0... = periodicLDR: False
         *             terminationCause: mt-lrRestart (4)
         *             lcsLocationInfo
         *                 networkNode-Number: 91947101640051
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460015
         *                         Country Code: Germany (Federal Republic of) (49)
         *                 lmsi: 7202eb37
         *                 gprsNodeIndicator
         *                 additional-Number: sgsn-Number (1)
         *                     sgsn-Number: 91947101640052
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460025
         *                             Country Code: Germany (Federal Republic of) (49)
         *                 Padding: 3
         *                 supportedLCS-CapabilitySets: f0
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 0... = lcsCapabilitySet5: False
         *                 Padding: 3
         *                 additional-LCS-CapabilitySets: f8
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 1... = lcsCapabilitySet5: True
         *                 mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                 aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *                 sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *                 sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         lcs-ReferenceNumber: 49
         *         geranPositioningData: 00031b212b3a4360
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 47f87022742614
         *         h-gmlc-Address: 040a00000e
         *             GSN-Address IPv4: 10.0.0.14
         *         lcsServiceTypeID: emergencyAlertServices (1)
         *         accuracyFulfilmentIndicator: requestedAccuracyNotFulfilled (1)
         *         velocityEstimate: 30030065020501
         *         mo-lrShortCircuitIndicator
         *         geranGANSSpositioningData: 00638b0203
         *         targetServingNodeForHandover: msc-Number (0)
         *             msc-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *                     Country Code: Germany (Federal Republic of) (49)
         */
        assertEquals(lcsEvent, LCSEvent.deferredmtlrResponse);
        lcsClientType = lcsClientID.getLCSClientType();
        assertEquals(lcsClientType, LCSClientType.valueAddedServices);
        lcsClientExternalID = lcsClientID.getLCSClientExternalID();
        externalAddress = lcsClientExternalID.getExternalAddress();
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(externalAddress.getAddress(), "444567");
        assertFalse(externalAddress.isExtension());
        lcsClientDialedByMS = lcsClientID.getLCSClientDialedByMS();
        assertEquals(lcsClientDialedByMS.getAddressNature(), AddressNature.international_number);
        assertEquals(lcsClientDialedByMS.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsClientDialedByMS.getAddress(), "219023");
        assertFalse(lcsClientDialedByMS.isExtension());
        lcsClientInternalID = lcsClientID.getLCSClientInternalID();
        assertEquals(lcsClientInternalID, LCSClientInternalID.broadcastService);
        lcsClientName = lcsClientID.getLCSClientName();
        assertEquals(lcsClientName.getDataCodingScheme().getCode(), 0x0f);
        assertEquals(lcsClientName.getNameString().getString(null), "911");
        assertEquals(lcsClientName.getLCSFormatIndicator(), LCSFormatIndicator.url);
        apn = lcsClientID.getLCSAPN();
        assertEquals(apn.getApn(), "e911");
        lcsRequestorID = lcsClientID.getLCSRequestorID();
        assertNull(lcsRequestorID);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getNetworkNodeNumber().getAddress(), "491710460015");
        assertEquals(lcsLocationInfo.getLMSI().getData(), new byte[] { 0x72, 0x02, (byte) 0xeb, (byte) 0x37});
        assertTrue(lcsLocationInfo.getGprsNodeIndicator());
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().getAddress(), "491710460025");
        assertFalse(lcsLocationInfo.getAdditionalNumber().getSGSNNumber().isExtension());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease6());
        assertFalse(lcsLocationInfo.getSupportedLCSCapabilitySets().getCapabilitySetRelease7());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease98_99());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease5());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease6());
        assertTrue(lcsLocationInfo.getAdditionalLCSCapabilitySets().getCapabilitySetRelease7());
        mmeName = lcsLocationInfo.getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        aaaServerName = lcsLocationInfo.getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnName = lcsLocationInfo.getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnRealm = lcsLocationInfo.getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertNull(msisdn);
        assertNull(naEsrd);
        assertNull(naEsrk);
        assertEquals(imsi.getData(), "748027673551729");
        assertEquals(imei.getIMEI(), "100710476888251");
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertEquals(locationEstimate.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(locationEstimate.getLatitude() - (0.00000)) < 0.00001);
        assertTrue(Math.abs(locationEstimate.getLongitude() - (0.00000)) < 0.00001);
        assertTrue(slrArgExtensionContainer.getSlrArgPcsExtensions().getNaEsrkRequest());
        assertNull(slrArgExtensionContainer.getPrivateExtensionList());
        assertEquals(ageOfLocationEstimate.intValue(), 0);
        assertEquals(addLocationEstimate.getData(), new byte[] {84, 37, -27, -77, 52, 66, -45, 37, -26, 64, 52, 67, 124, 37, -26, -125, 52, 67, 121, 37, -26,
                -124, 52, 67, 125});
        polygon = new PolygonImpl(addLocationEstimate.getData());
        assertEquals(polygon.getNumberOfPoints(), 4);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLatitude() - 26.646513) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(0).getLongitude() - 73.492076) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLatitude() - 26.648026) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(1).getLongitude() - 73.495703) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLatitude() - 26.648744) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(2).getLongitude() - 73.495638) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(3).getLatitude() - 26.648755) < 0.000001);
        assertTrue(Math.abs(polygon.getEllipsoidPoint(3).getLongitude() - 73.495724) < 0.000001);
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
        mmeName = deferredmtlrData.getLCSLocationInfo().getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        aaaServerName = deferredmtlrData.getLCSLocationInfo().getAaaServerName();
        assertEquals(aaaServerName, new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnName = deferredmtlrData.getLCSLocationInfo().getSgsnName();
        assertEquals(sgsnName, new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        sgsnRealm = deferredmtlrData.getLCSLocationInfo().getSgsnRealm();
        assertEquals(sgsnRealm, new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        assertEquals(lcsReferenceNumber.intValue(), 73); // 0x49 = 73
        geranPositioningData = new PositioningDataInformationImpl(geranPositioningData.getData());
        HashMap<String, Integer> geranMethodsAndUsage = geranPositioningData.getPositioningDataSet();
        assertNotNull(geranMethodsAndUsage.get("Mobile Based E-OTD"));
        assertNotNull(geranMethodsAndUsage.get("Mobile Assisted E-OTD"));
        assertNotNull(geranMethodsAndUsage.get("U-TDOA"));
        assertNotNull(geranMethodsAndUsage.get("Cell ID"));
        assertNotNull(geranMethodsAndUsage.get("Mobile Assisted GPS"));
        assertNotNull(geranMethodsAndUsage.get("Timing Advance"));
        assertNotNull(geranMethodsAndUsage.get("Conventional GPS"));
        Assert.assertNull(geranMethodsAndUsage.get("Reserved (not to be used)"));
        ArrayList<String> geranPosMethods = geranPositioningData.getLocationGeneratedPositioningMethods();
        assertEquals(geranPosMethods.get(0), "Timing Advance");
        assertEquals(geranPosMethods.get(1), "Mobile Assisted E-OTD");
        assertEquals(geranPosMethods.get(2), "Mobile Assisted GPS");
        assertEquals(geranPosMethods.get(3), "U-TDOA");
        assertNull(utranPositioningData);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 7);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 8820);
        assertEquals(cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 9748);
        assertEquals(hGmlcAddress.getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(hGmlcAddress.getGSNAddressData(), new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        assertEquals(lcsServiceTypeID.intValue(), 1);
        assertFalse(saiPresent);
        assertFalse(pseudonymIndicator);
        assertEquals(accuracyFulfilmentIndicator, AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled);
        assertEquals(velocityEstimate.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(velocityEstimate.getHorizontalSpeed(), 101);
        assertEquals(velocityEstimate.getBearing(), 3);
        assertEquals(velocityEstimate.getVerticalSpeed(), 2);
        assertEquals(velocityEstimate.getUncertaintyHorizontalSpeed(), 5);
        assertEquals(velocityEstimate.getUncertaintyVerticalSpeed(), 1);
        assertNull(sequenceNumber);
        assertNull(periodicLDRInfo);
        assertTrue(moLrShortCircuitIndicator);
        geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(geranGANSSpositioningData.getData());
        Multimap<String, String> geranGanssMethodsAndGanssIds = geranGANSSpositioningData.getLocationGeneratedMethodsAndGANSSIds();
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
        assertNull(utranBaroPressureMeas);
        assertNull(utranCivicAddress);
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        // test 2 with real data from Indian operator
        byte[] data = getEncodedDataIndia1();
        /*
         * invoke
         *     invokeID: 1
         *     opCode: localValue (0)
         *         localValue: subscriberLocationReport (86)
         *     lcs-Event: emergencyCallOrigination (0)
         *     lcs-ClientID
         *         lcsClientType: emergencyServices (0)
         *     lcsLocationInfo
         *         networkNode-Number: 91194915999926
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 919451999962
         *         lmsi: 1fb28200
         *     msisdn: 91194951759824
         *         1... .... = Extension: No Extension
         *         .001 .... = Nature of number: International Number (0x1)
         *         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *         E.164 number (MSISDN): 919415578942
         *     IMSI: 404556140835931
         *     [Association IMSI: 404556140835931]
         *         Mobile Country Code (MCC): India (404)
         *         Mobile Network Code (MNC): BSNL, UP (East) (55)
         *     locationEstimate: a026358a399ece006e11461000
         *         1010 .... = Location estimate: Ellipsoid Arc (10)
         *         0... .... = Sign of latitude: North (0)
         *         .010 0110 0011 0101 1000 1010 = Degrees of latitude: 2504074 (26.86580 degrees)
         *         0011 1001 1001 1110 1100 1110 = Degrees of longitude: 3776206 (81.02860 degrees)
         *         Inner radius: 110
         *         .001 0001 = Uncertainty radius: 17
         *         Offset angle: 70
         *         Included angle: 16
         *         .000 0000 = Confidence(%): 0
         *         [Location OSM URI: https://www.openstreetmap.org/?mlat=26.86580&mlon=81.02860&zoom=12]
         *     ageOfLocationEstimate: 0
         *     cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *         cellGlobalIdOrServiceAreaIdFixedLength: 04f45571b130cc
         *     sai-Present
         */
        LCSEvent lcsEvent = LCSEvent.emergencyCallOrigination;
        LCSClientID lcsClientID = new LCSClientIDImpl(LCSClientType.emergencyServices, null, null, null, null, null,
                null);
        ISDNAddressString networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919451999962");
        LMSI lmsi = new LMSIImpl(new byte[] { 31, -78, -126, 0 });
        boolean gprsNodeIndicator = false;
        LCSLocationInfo lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, null,
                null, null, null, null, null, null);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919415578942");
        IMSI imsi = new IMSIImpl("404556140835931");
        IMEI imei = null;
        ISDNAddressString naEsrd = null;
        ISDNAddressString naEsrk = null;
        TypeOfShape typeOfShape = TypeOfShape.EllipsoidArc;
        double latitude = 26.86580;
        double longitude = 81.02860;
        double uncertainty = 0;
        double uncertaintySemiMajorAxis = 0;
        double uncertaintySemiMinorAxis = 0;
        double angleOfMajorAxis = 0;
        int confidence = 0;
        int altitude = 0;
        double uncertaintyAltitude = 0;
        int innerRadius = 110;
        double uncertaintyRadius = 40.54470284992945;
        double offsetAngle = 70;
        double includedAngle = 16.0;
        ExtGeographicalInformation locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
        Integer ageOfLocationEstimate = 0;
        SLRArgExtensionContainer slrArgExtensionContainer = null;
        AddGeographicalInformation addLocationEstimate = null;
        DeferredmtlrData deferredmtlrData = null;
        Integer lcsReferenceNumber = null;
        PositioningDataInformation geranPositioningData = null;
        UtranPositioningDataInfo utranPositioningData = null;
        CellGlobalIdOrServiceAreaIdFixedLength cgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 55, 29105, 12492);
        CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiOrSaiFixedLength);
        boolean saiPresent = true;
        GSNAddress hGmlcAddress = null;
        Integer lcsServiceTypeID = null;
        boolean pseudonymIndicator = false;
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = null;
        VelocityEstimate velocityEstimate = null;
        Integer sequenceNumber = null;
        PeriodicLDRInfo periodicLDRInfo = null;
        boolean moLrShortCircuitIndicator = false;
        GeranGANSSpositioningData geranGANSSpositioningData = null;
        UtranGANSSpositioningData utranGANSSpositioningData = null;
        ServingNodeAddress targetServingNodeForHandover = null;
        UtranAdditionalPositioningData utranAdditionalPositioningData = null;
        Integer utranBaroPressureMeas = null;
        UtranCivicAddress utranCivicAddress = null;

        SubscriberLocationReportRequestImpl slr1 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        AsnOutputStream asnOS = new AsnOutputStream();
        slr1.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 3 with real data from Indian operator with additional location estimate (polygon)
        data = getEncodedDataIndiaWAddLocationEstimate_Polygon();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 1
         *         opCode: localValue (0)
         *             localValue: subscriberLocationReport (86)
         *         lcs-Event: emergencyCallOrigination (0)
         *         lcs-ClientID
         *             lcsClientType: emergencyServices (0)
         *         lcsLocationInfo
         *             networkNode-Number: 91194915999986
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 919451999968
         *             lmsi: 5b7c6c00
         *         msisdn: 91194951347594
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 919415435749
         *         IMSI: 404556240076591
         *         [Association IMSI: 404556240076591]
         *             Mobile Country Code (MCC): India (404)
         *             Mobile Network Code (MNC): BSNL, UP (East) (55)
         *         locationEstimate: 53
         *             0101 .... = Location estimate: Polygon (5)
         *         ageOfLocationEstimate: 0
         *         add-LocationEstimate: 53255d19393311255d19393311255ee4393328
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 04f455082f6d1b
         */
        lcsClientID = new LCSClientIDImpl(LCSClientType.emergencyServices, null, null, null, null, null,
                null);
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919451999968");
        lmsi = new LMSIImpl(new byte[] { 0x5b, (byte) 0x7c, (byte) 0x6c, 0x00});
        lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, null,
                null, null, null, null, null, null);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919415435749");
        imsi = new IMSIImpl("404556240076591");
        locationEstimate = new ExtGeographicalInformationImpl(new byte[] { 0x53 });
        addLocationEstimate = new AddGeographicalInformationImpl(new byte[] { 0x53, 0x25, 0x5d, 0x19, 0x39, 0x33, 0x11, 0x25,
                0x5d, 0x19, 0x39, 0x33, 0x11, 0x25, 0x5e, (byte) 0xe4, 0x39, 0x33, 0x28 });
        Polygon pol = new PolygonImpl(addLocationEstimate.getData());
        assertEquals(pol.getNumberOfPoints(), 3);
        assertTrue(Math.abs(pol.getEllipsoidPoint(0).getLatitude() - 26.271325) < 0.000001);
        assertTrue(Math.abs(pol.getEllipsoidPoint(0).getLongitude() - 80.436766) < 0.000001);
        assertTrue(Math.abs(pol.getEllipsoidPoint(1).getLatitude() - 26.271325) < 0.000001);
        assertTrue(Math.abs(pol.getEllipsoidPoint(1).getLongitude() - 80.436766) < 0.000001);
        assertTrue(Math.abs(pol.getEllipsoidPoint(2).getLatitude() - 26.276250) < 0.000001);
        assertTrue(Math.abs(pol.getEllipsoidPoint(2).getLongitude() - 80.437260) < 0.000001);
        cgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 55, 2095, 27931);
        cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiOrSaiFixedLength);
        saiPresent = false;

        SubscriberLocationReportRequestImpl slr2 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        slr2.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 4 with data from LSM load test (periodic LDR)
        data = getEncodedDataLSMLoadTestPeriodicLDR();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: subscriberLocationReport (86)
         *         lcs-Event: deferredmt-lrResponse (3)
         *         lcs-ClientID
         *             lcsClientType: valueAddedServices (1)
         *             lcsClientExternalID
         *                 externalAddress: 91445476
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 444567
         *             lcsClientDialedByMS: 91120932
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 219023
         *                     Country Code: Spare code (219)
         *             lcsClientInternalID: broadcastService (0)
         *             lcsClientName
         *                 dataCodingScheme: 0f
         *                     0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
         *                     .... 1111 = Language: Language unspecified (15)
         *                 nameString: b9580c
         *                     USSD String: 911
         *                 lcs-FormatIndicator: url (3)
         *             lcsAPN: 0465393131 - e911
         *                 APN: e911
         *         lcsLocationInfo
         *             networkNode-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *             gprsNodeIndicator
         *             additional-Number: sgsn-Number (1)
         *                 sgsn-Number: 91947101640052
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460025
         *             Padding: 3
         *             supportedLCS-CapabilitySets: f0
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 0... = lcsCapabilitySet5: False
         *             Padding: 3
         *             additional-LCS-CapabilitySets: f8
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 1... = lcsCapabilitySet5: True
         *             mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *             sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *             sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         IMSI: 748022924028828
         *         [Association IMSI: 748022924028828]
         *         imei: 01706118938282f2
         *             TBCD digits: 100716813928282
         *         locationEstimate: 0031a63fd812e0
         *             0000 .... = Location estimate: Ellipsoid Point (0)
         *             0... .... = Sign of latitude: North (0)
         *             .011 0001 1010 0110 0011 1111 = Degrees of latitude: 3253823 (34.90974 degrees)
         *             1101 1000 0001 0010 1110 0000 = Degrees of longitude: -2616608 (-56.14632 degrees)
         *             [Location OSM URI: https://www.openstreetmap.org/?mlat=34.90974&mlon=-56.14632&zoom=12]
         *         ageOfLocationEstimate: 0
         *         slr-ArgExtensionContainer
         *             slr-Arg-PCS-Extensions
         *                 na-ESRK-Request
         *         deferredmt-lrData
         *             Padding: 3
         *             deferredLocationEventType: 08
         *                 0... .... = msAvailable: False
         *                 .0.. .... = enteringIntoArea: False
         *                 ..0. .... = leavingFromArea: False
         *                 ...0 .... = beingInsideArea: False
         *                 .... 1... = periodicLDR: True
         *             terminationCause: congestion (3)
         *             lcsLocationInfo
         *                 networkNode-Number: 91947101640051
         *                 gprsNodeIndicator
         *                 additional-Number: sgsn-Number (1)
         *                     sgsn-Number: 91947101640052
         *                 Padding: 3
         *                 supportedLCS-CapabilitySets: f0
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 0... = lcsCapabilitySet5: False
         *                 Padding: 3
         *                 additional-LCS-CapabilitySets: f8
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 1... = lcsCapabilitySet5: True
         *                 mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                 aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *                 sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *                 sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         lcs-ReferenceNumber: 29
         *         utranPositioningData: 0000434b00622b
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 47f87020792441
         *         h-gmlc-Address: 040a00000e
         *         lcsServiceTypeID: emergencyAlertServices (1)
         *         sai-Present
         *         accuracyFulfilmentIndicator: requestedAccuracyNotFulfilled (1)
         *         velocityEstimate: 30030065020501
         *         sequenceNumber: 1
         *         periodicLDRInfo
         *             reportingAmount: 3
         *             reportingInterval: 600
         *             BER Error: This field lies beyond the end of the known sequence definition.
         *         mo-lrShortCircuitIndicator
         *         utranGANSSpositioningData: 01638b0203
         *         targetServingNodeForHandover: msc-Number (0)
         *             msc-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *         utranAdditionalPositioningData: 578f
         *         utranBaroPressureMeas: 110000
         *         utranCivicAddress […]: 3c636c3a6369766963416464726573733e0a202020202020202020202020202020202020202020
         * 2020203c636c3a636f756e7472793e55533c2f636c3a636f756e7472793e0a20202020202020202020202020202020202020202020202
         * 03c636c3a41313e4e657720596f
         */
        lcsEvent = LCSEvent.deferredmtlrResponse;
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "444567");
        LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, null);
        LCSClientInternalID lcsClientInternalID = LCSClientInternalID.broadcastService;
        String clientName = "219023";
        int cbsDataCodingSchemeCode = 15;
        CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
        String ussdLcsString = "911";
        Charset gsm8Charset = Charset.defaultCharset();
        USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
        LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
        AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
        APN lcsAPN = new APNImpl("e911");
        LCSRequestorID lcsRequestorID = null;
        lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS,
                lcsAPN, lcsRequestorID);
        imsi = new IMSIImpl("748022924028828");
        imei = new IMEIImpl("100716813928282");
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460015");
        lmsi = null;
        gprsNodeIndicator = true;
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
        msisdn = null;
        typeOfShape = TypeOfShape.EllipsoidPoint;
        latitude = 34.909744;
        longitude = -56.146317;
        uncertainty = 0;
        uncertaintySemiMajorAxis = 0;
        uncertaintySemiMinorAxis = 0;
        angleOfMajorAxis = 0;
        uncertaintyAltitude = 0;
        innerRadius = 0;
        uncertaintyRadius = 0;
        offsetAngle = 0;
        includedAngle = 0;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
        addLocationEstimate = null;
        boolean naEsrkRequest = true;
        SLRArgPCSExtensions slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(naEsrkRequest);
        slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
        boolean msAvailable = false;
        boolean enteringIntoArea = false;
        boolean leavingFromArea = false;
        boolean beingInsideArea = false;
        boolean periodicLDR = true;
        int reportingAmount = 3;
        int reportingInterval = 600;
        int reportingAmountMilliseconds = 863999;
        int reportingIntervalMilliseconds = 100;
        ReportingOptionMilliseconds reportingOptionMilliseconds = new ReportingOptionMillisecondsImpl(reportingAmountMilliseconds, reportingIntervalMilliseconds);
        periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, reportingOptionMilliseconds);
        DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
        TerminationCause terminationCause = TerminationCause.congestion;
        deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
        lcsReferenceNumber = 41;
        utranPositioningData = new UtranPositioningDataInfoImpl(new byte[] {0x00, 0x00, 0x43, 0x4b, 0x00, 0x62, 0x2b});
        cgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(748, 7, 8313, 9281);
        cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiOrSaiFixedLength);
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        lcsServiceTypeID = 1;
        saiPresent = true;
        accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyNotFulfilled;
        VelocityType velocityType = VelocityType.HorizontalWithVerticalVelocityAndUncertainty;
        int horizontalSpeed = 101;
        int bearing = 3;
        int verticalSpeed = 2;
        int uncertaintyHorizontalSpeed = 5;
        int uncertaintyVerticalSpeed = 1;
        velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
        sequenceNumber = 1;
        moLrShortCircuitIndicator = true;
        utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(new byte[] {0x01, 0x63, (byte) 0x8b, 0x02, 0x03});
        boolean isMsc = true;
        targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, isMsc);
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

        SubscriberLocationReportRequestImpl slr3 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        slr3.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 5 with data from LSM load test (being inside area), GERAN/GERANGANSS position data
        data = getEncodedLSMLoadTestBeingInsideArea();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *         lcs-Event: deferredmt-lrResponse (3)
         *         lcs-ClientID
         *             lcsClientType: valueAddedServices (1)
         *             lcsClientExternalID
         *                 externalAddress: 91445476
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 444567
         *                         Country Code: United Kingdom of Great Britain and Northern Ireland (44)
         *             lcsClientDialedByMS: 91120932
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 219023
         *                     Country Code: Spare code (219)
         *             lcsClientInternalID: broadcastService (0)
         *             lcsClientName
         *                 dataCodingScheme: 0f
         *                     0000 .... = Coding Group: Coding Group 0(Language using the GSM 7 bit default alphabet) (0)
         *                     .... 1111 = Language: Language unspecified (15)
         *                 nameString: b9580c
         *                     USSD String: 911
         *                 lcs-FormatIndicator: url (3)
         *             lcsAPN: 0465393131 - e911
         *                 APN: e911
         *         lcsLocationInfo
         *             networkNode-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *                     Country Code: Germany (Federal Republic of) (49)
         *             lmsi: 7202eb37
         *             gprsNodeIndicator
         *             additional-Number: sgsn-Number (1)
         *                 sgsn-Number: 91947101640052
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460025
         *                         Country Code: Germany (Federal Republic of) (49)
         *             Padding: 3
         *             supportedLCS-CapabilitySets: f0
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 0... = lcsCapabilitySet5: False
         *             Padding: 3
         *             additional-LCS-CapabilitySets: f8
         *                 1... .... = lcsCapabilitySet1: True
         *                 .1.. .... = lcsCapabilitySet2: True
         *                 ..1. .... = lcsCapabilitySet3: True
         *                 ...1 .... = lcsCapabilitySet4: True
         *                 .... 1... = lcsCapabilitySet5: True
         *             mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *             aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *             sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *             sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         IMSI: 748027673551729
         *         [Association IMSI: 748027673551729]
         *         imei: 01700174868852f1
         *             TBCD digits: 100710476888251
         *         locationEstimate: 00000000000000
         *             0000 .... = Location estimate: Ellipsoid Point (0)
         *             0... .... = Sign of latitude: North (0)
         *             .000 0000 0000 0000 0000 0000 = Degrees of latitude: 0 (0.00000 degrees)
         *             0000 0000 0000 0000 0000 0000 = Degrees of longitude: 0 (0.00000 degrees)
         *             [Location OSM URI: https://www.openstreetmap.org/?mlat=0.00000&mlon=0.00000&zoom=12]
         *         ageOfLocationEstimate: 0
         *         slr-ArgExtensionContainer
         *             slr-Arg-PCS-Extensions
         *                 na-ESRK-Request
         *         add-LocationEstimate: 5425e5b33442d325e64034437c25e68334437925e68434437d
         *         deferredmt-lrData
         *             Padding: 3
         *             deferredLocationEventType: 10
         *                 0... .... = msAvailable: False
         *                 .0.. .... = enteringIntoArea: False
         *                 ..0. .... = leavingFromArea: False
         *                 ...1 .... = beingInsideArea: True
         *                 .... 0... = periodicLDR: False
         *             terminationCause: mt-lrRestart (4)
         *             lcsLocationInfo
         *                 networkNode-Number: 91947101640051
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460015
         *                         Country Code: Germany (Federal Republic of) (49)
         *                 lmsi: 7202eb37
         *                 gprsNodeIndicator
         *                 additional-Number: sgsn-Number (1)
         *                     sgsn-Number: 91947101640052
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460025
         *                             Country Code: Germany (Federal Republic of) (49)
         *                 Padding: 3
         *                 supportedLCS-CapabilitySets: f0
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 0... = lcsCapabilitySet5: False
         *                 Padding: 3
         *                 additional-LCS-CapabilitySets: f8
         *                     1... .... = lcsCapabilitySet1: True
         *                     .1.. .... = lcsCapabilitySet2: True
         *                     ..1. .... = lcsCapabilitySet3: True
         *                     ...1 .... = lcsCapabilitySet4: True
         *                     .... 1... = lcsCapabilitySet5: True
         *                 mme-Name: mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org
         *                 aaa-Server-Name: aaa3000.aaa.mnc002.mcc748.3gppnetwork.org
         *                 sgsn-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
         *                 sgsn-Realm: epc.mnc001.mcc748.3gppnetwork.org
         *         lcs-ReferenceNumber: 49
         *         geranPositioningData: 00031b212b3a4360
         *         cellIdOrSai: cellGlobalIdOrServiceAreaIdFixedLength (0)
         *             cellGlobalIdOrServiceAreaIdFixedLength: 47f87022742614
         *         h-gmlc-Address: 040a00000e
         *             GSN-Address IPv4: 10.0.0.14
         *         lcsServiceTypeID: emergencyAlertServices (1)
         *         accuracyFulfilmentIndicator: requestedAccuracyNotFulfilled (1)
         *         velocityEstimate: 30030065020501
         *         mo-lrShortCircuitIndicator
         *         geranGANSSpositioningData: 00638b0203
         *         targetServingNodeForHandover: msc-Number (0)
         *             msc-Number: 91947101640051
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460015
         *                     Country Code: Germany (Federal Republic of) (49)
         */
        externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "444567");
        lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, null);
        clientName = "219023";
        cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
        ussdLcsString = "911";
        gsm8Charset = Charset.defaultCharset();
        ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
        lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
        lcsAPN = new APNImpl("e911");
        lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS,
                lcsAPN, lcsRequestorID);
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460015");
        lmsi = new LMSIImpl(new byte[] { 0x72, 0x02, (byte) 0xeb, (byte) 0x37});
        sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460025");
        additionalNumber = new AdditionalNumberImpl(null, sgsnNumber);
        lcsCapabilitySetRelease7 = false;
        supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
        lcsCapabilitySetRelease7 = true;
        additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
        mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        aaaServerName = new DiameterIdentityImpl("aaa3000.aaa.mnc002.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        sgsnName = new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        sgsnRealm = new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, additionalNumber,
                supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName, sgsnName, sgsnRealm);
        imsi = new IMSIImpl("748027673551729");
        imei = new IMEIImpl("100710476888251");
        latitude = 0.000000;
        longitude = 0.000000;
        uncertainty = 0;
        uncertaintySemiMajorAxis = 0;
        uncertaintySemiMinorAxis = 0;
        angleOfMajorAxis = 0;
        uncertaintyAltitude = 0;
        uncertaintyRadius = 0;
        offsetAngle = 0;
        includedAngle = 0;
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
        slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(naEsrkRequest);
        slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
        addLocationEstimate = new AddGeographicalInformationImpl(new byte[] {0x54, 0x25, (byte) 0xe5, (byte) 0xb3, 0x34, 0x42, (byte) 0xd3,
                0x25, (byte) 0xe6, 0x40, 0x34, 0x43, 0x7c, 0x25, (byte) 0xe6, (byte) 0x83, 0x34, 0x43, 0x79, 0x25, (byte) 0xe6, (byte) 0x84, 0x34, 0x43,
                0x7d});
        beingInsideArea = true;
        periodicLDR = false;
        periodicLDRInfo = null;
        deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
        terminationCause = TerminationCause.mtlrRestart;
        deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);
        lcsReferenceNumber = 73;
        ageOfLocationEstimate = 0;
        geranPositioningData = new PositioningDataInformationImpl(new byte[] {0x00, 0x03, 0x1b, 0x21, 0x2b, 0x3a, 0x43, 0x60});
        utranPositioningData = null;
        cgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(748, 7, 8820, 9748);
        cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiOrSaiFixedLength);
        hGmlcAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, new byte[] { 0x0a, 0x00, 0x00, 0x0e });
        lcsServiceTypeID = 1;
        saiPresent = false;
        velocityEstimate = new VelocityEstimateImpl(velocityType, horizontalSpeed, bearing, verticalSpeed, uncertaintyHorizontalSpeed, uncertaintyVerticalSpeed);
        sequenceNumber = null;
        geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(new byte[] {0x00, 0x63, (byte) 0x8b, 0x02, 0x03});
        utranGANSSpositioningData = null;
        targetServingNodeForHandover = new ServingNodeAddressImpl(networkNodeNumber, isMsc);
        utranAdditionalPositioningData = null;
        utranBaroPressureMeas = null;
        utranCivicAddress = null;

        SubscriberLocationReportRequestImpl slr4 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hGmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        slr4.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }
}
