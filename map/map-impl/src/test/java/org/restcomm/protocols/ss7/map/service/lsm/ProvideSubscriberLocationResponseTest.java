
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AddGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.PositioningDataInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ProvideSubscriberLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ServingNodeAddressImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
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
        /*
        geranPositioningData = new PositioningDataInformationImpl(new byte[] {0x00, 0x03});
        cellCgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 72, 5012, 11713);
         */

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
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas,
                utranCivicAddress);

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
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas,
                utranCivicAddress);

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
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas,
                utranCivicAddress);

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
                utranGANSSpositioningData, targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas,
                utranCivicAddress);

        asnOS = new AsnOutputStream();
        psl5.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
