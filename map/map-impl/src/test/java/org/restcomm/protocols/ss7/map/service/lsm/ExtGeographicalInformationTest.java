
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ExtGeographicalInformationTest {

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle() {
        return new byte[] { 4, 8, 16, 92, 113, -57, -106, 11, 97, 7 };
    }

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle2() {
        return new byte[] { 4, 8, 0x10, 0x0f, 0x03, 0x7f, 0x36, 0x32, 0x7f, 0x23 };
    }

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyEllipse() {
        return new byte[] { 4, 11, 48, -36, 113, -57, 22, 11, 96, 25, 48, 11, 23 };
    }

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyEllipse2() {
        return new byte[] { 4, 11, 0x30, 0x25, 0x49, 0x64, 0x38, 0x68, (byte) 0x82, 0x23, 0x21, 0x50, 0x50 };
    }

    private byte[] getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid() {
        return new byte[] { 4, 14, (byte) 144, -35, 39, -46, 22, 65, -3, -128, 17, 18, 41, 14, 14, 29 };
    }
    private byte[] getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid2 () {
        return new byte[] { 4, 14,
                (byte) 0x90, (byte) 0xb1, (byte) 0xb7, 0x3f, (byte) 0xd8, (byte) 0xee, (byte) 0xe1, 0x02,
                0x3a, 0x0d, 0x08, 0x0f, 0x17, 0x05
        };
    }

    private byte[] getEncodedData_EllipsoidArc() {
        return new byte[] { 4, 13, (byte) 160, 1, 108, 22, 121, -48, 54, 23, 112, 9, 11, 12, 39 };
    }

    private byte[] getEncodedData_EllipsoidArc2() {
        return new byte[] { 4, 13,
                (byte) 0xa0, 0x23, (byte) 0xc3, (byte) 0xec, 0x36, (byte) 0xab, (byte) 0x8f, 0x03,
                0x07, 0x2a, 0x1b, 0x41, 0x3c
        };
    }

    private byte[] getEncodedData_EllipsoidPoint() {
        return new byte[] { 4, 7, 0, 0, 0, 0, -3, -35, -34 };
    }

    private byte[] getEncodedData_EllipsoidPoint2() {
        return new byte[] { 4, 7, 0x00, 0x2b, 0x6c, (byte) 0xf1, 0x35, (byte) 0xf4, (byte) 0xbe };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {

        /*
         * EllipsoidPointWithUncertaintyCircle
         */
        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        ExtGeographicalInformationImpl impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(impl.getUncertainty() - 9.48) < 0.01);

        /*
         * EllipsoidPointWithUncertaintyCircle 2
         */
        rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        // locationEstimate: 100f037f36327f23
        //    0001 .... = Location estimate: Ellipsoid point with uncertainty Circle (1)
        //    0... .... = Sign of latitude: North (0)
        //    .000 1111 0000 0011 0111 1111 = Degrees of latitude: 983935 (10.55648 degrees)
        //    0011 0110 0011 0010 0111 1111 = Degrees of longitude: 3551871 (76.21489 degrees)
        //    .010 0011 = Uncertainty code: 35 (271.0 m)
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 10.55648) < 0.00001);
        assertTrue(Math.abs(impl.getLongitude() - 76.21489) < 0.00001);
        assertTrue(Math.abs(impl.getUncertainty() - 271.02) < 0.01); // 271.02436848064326

        /*
         * EllipsoidPointWithUncertaintyEllipse
         */
        rawData = getEncodedData_EllipsoidPointWithUncertaintyEllipse();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyEllipse);
        assertTrue(Math.abs(impl.getLatitude() - (-65)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 98.35) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 960.17) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 11) < 0.01);
        assertEquals(impl.getConfidence(), 23);

        /*
         * EllipsoidPointWithUncertaintyEllipse 2
         */
        rawData = getEncodedData_EllipsoidPointWithUncertaintyEllipse2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        // locationEstimate: 3025496438688223215050
        //    0011 .... = Location estimate: Ellipsoid point with uncertainty Ellipse (3)
        //    0... .... = Sign of latitude: North (0)
        //    .010 0101 0100 1001 0110 0100 = Degrees of latitude: 2443620 (26.21720 degrees)
        //    0011 1000 0110 1000 1000 0010 = Degrees of longitude: 3696770 (79.32408 degrees)
        //    .010 0011 = Uncertainty semi-major: 35 (271.0 m)
        //    .010 0001 = Uncertainty semi-minor: 33 (222.3 m)
        //    Orientation of major axis: 80
        //    .101 0000 = Confidence(%): 80
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyEllipse);
        assertTrue(Math.abs(impl.getLatitude() - 26.21720) < 0.00001);
        assertTrue(Math.abs(impl.getLongitude() - 79.32408) < 0.00001);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 271.0) < 0.1);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 222.3) < 0.1);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 80) < 0.01);
        assertEquals(impl.getConfidence(), 80);

        /*
         * EllipsoidPointWithAltitudeAndUncertaintyEllipsoid
         */
        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid);
        assertTrue(Math.abs(impl.getLatitude() - (-65.5)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31.3) < 0.01);
        assertEquals(impl.getAltitude(), -17);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 45.60) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 487.85) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 28.0) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintyAltitude() - 18.58) < 0.01);
        assertEquals(impl.getConfidence(), 29);

        /*
         * EllipsoidPointWithAltitudeAndUncertaintyEllipsoid 2
         */
        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        // locationEstimate: 90b1b73fd8eee1023a0d080f1705
        //    1001 .... = Location estimate: Ellipsoid point with altitude and uncertainty Ellipsoid (9)
        //    1... .... = Sign of latitude: South (1)
        //    .011 0001 1011 0111 0011 1111 = Degrees of latitude: 3258175 (-34.95643 degrees)
        //    1101 1000 1110 1110 1110 0001 = Degrees of longitude: -2560287 (-54.93780 degrees)
        //    0... .... .... .... = D: Direction of Altitude: Altitude expresses height (0)
        //    .000 0010 0011 1010 = Altitude in meters: 570
        //    .000 1101 = Uncertainty semi-major: 13 (24.5 m)
        //    .000 1000 = Uncertainty semi-minor: 8 (11.4 m)
        //    Orientation of major axis: 30
        //    .001 0111 = Uncertainty Altitude: 23 (34.4 m)
        //    .000 0101 = Confidence(%): 5
        //    [Location OSM URI: https://www.openstreetmap.org/?mlat=-34.95643&mlon=-54.93780&zoom=12]
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid);
        assertTrue(Math.abs(impl.getLatitude() - (-34.95643)) < 0.00001);
        assertTrue(Math.abs(impl.getLongitude() - (-54.93780)) < 0.00001);
        assertEquals(impl.getAltitude(), 570);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 24.5) < 0.1);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 11.4) < 0.1);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 30.0) < 0.1);
        assertTrue(Math.abs(impl.getUncertaintyAltitude() - 34.4) < 0.01);
        assertEquals(impl.getConfidence(), 5);

        /*
         * EllipsoidArc
         */
        rawData = getEncodedData_EllipsoidArc();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(impl.getLatitude() - 1) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 171.3) < 0.01);
        assertEquals(impl.getInnerRadius(), 6000);
        assertTrue(Math.abs(impl.getUncertaintyRadius() - 13.58) < 0.01);
        assertTrue(Math.abs(impl.getOffsetAngle() - 11) < 0.01);
        assertTrue(Math.abs(impl.getIncludedAngle() - 12) < 0.01);
        assertEquals(impl.getConfidence(), 39);

        rawData = getEncodedData_EllipsoidPoint();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        /*
         * EllipsoidArc 2
         */
        rawData = getEncodedData_EllipsoidArc2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        // locationEstimate: a023c3ec36ab8f03072a1b413c
        //    1010 .... = Location estimate: Ellipsoid Arc (10)
        //    0... .... = Sign of latitude: North (0)
        //    .010 0011 1100 0011 1110 1100 = Degrees of latitude: 2343916 (25.14749 degrees)
        //    0011 0110 1010 1011 1000 1111 = Degrees of longitude: 3582863 (76.87990 degrees)
        //    Inner radius: 775
        //    .010 1010 = Uncertainty radius: 42
        //    Offset angle: 27
        //    Included angle: 65
        //    .011 1100 = Confidence(%): 60
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(impl.getLatitude() - 25.14749) < 0.00001);
        assertTrue(Math.abs(impl.getLongitude() - 76.87990) < 0.00001);
        assertEquals(impl.getInnerRadius(), 775); //
        assertTrue(Math.abs(impl.getUncertaintyRadius() - 537.64) < 0.01); // r = 45((1+0.025)^42 -1) = 537.6369923749309
        assertTrue(Math.abs(impl.getOffsetAngle() - 27.0) < 0.1);
        assertTrue(Math.abs(impl.getIncludedAngle() - 65.0) < 0.1);
        assertEquals(impl.getConfidence(), 60);

        /*
         * EllipsoidPoint
         */
        rawData = getEncodedData_EllipsoidPoint();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(impl.getLatitude() - 0) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-3)) < 0.01); // -177

        /*
         * EllipsoidPoint 2
         */
        // locationEstimate: 002b6cf135f4be
        //    0000 .... = Location estimate: Ellipsoid Point (0)
        //    0... .... = Sign of latitude: North (0)
        //    .010 1011 0110 1100 1111 0001 = Degrees of latitude: 2845937 (30.53360 degrees)
        //    0011 0101 1111 0100 1011 1110 = Degrees of longitude: 3536062 (75.87566 degrees)
        //    [Location OSM URI: https://www.openstreetmap.org/?mlat=30.53360&mlon=75.87566&zoom=12]
        rawData = getEncodedData_EllipsoidPoint2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(impl.getLatitude() - 30.53360) < 0.00001);
        assertTrue(Math.abs(impl.getLongitude() - 75.87566) < 0.00001);

    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {

        /*
         * EllipsoidPointWithUncertaintyCircle
         */
        ExtGeographicalInformationImpl impl = new ExtGeographicalInformationImpl(
                TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidPointWithUncertaintyCircle 2
         */
        // locationEstimate: 100f037f36327f23
        //    0001 .... = Location estimate: Ellipsoid point with uncertainty Circle (1)
        //    0... .... = Sign of latitude: North (0)
        //    .000 1111 0000 0011 0111 1111 = Degrees of latitude: 983935 (10.55648 degrees)
        //    0011 0110 0011 0010 0111 1111 = Degrees of longitude: 3551871 (76.21489 degrees)
        //    .010 0011 = Uncertainty code: 35 (271.0 m)
        impl = new ExtGeographicalInformationImpl(
                TypeOfShape.EllipsoidPointWithUncertaintyCircle, 10.55648, 76.21489, 271.03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle2();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidPointWithUncertaintyEllipse
         */
        // locationEstimate: 3025496438688223215050
        //    0011 .... = Location estimate: Ellipsoid point with uncertainty Ellipse (3)
        //    0... .... = Sign of latitude: North (0)
        //    .010 0101 0100 1001 0110 0100 = Degrees of latitude: 2443620 (26.21720 degrees)
        //    0011 1000 0110 1000 1000 0010 = Degrees of longitude: 3696770 (79.32408 degrees)
        //    .010 0011 = Uncertainty semi-major: 35 (271.0 m)
        //    .010 0001 = Uncertainty semi-minor: 33 (222.3 m)
        //    Orientation of major axis: 80
        //    .101 0000 = Confidence(%): 80
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyEllipse, 26.21720, 79.32408, 0, 271.025, 222.3, 80,
                80, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPointWithUncertaintyEllipse2();
        assertTrue(Arrays.equals(rawData, encodedData));


        /*
         * EllipsoidPointWithAltitudeAndUncertaintyEllipsoid
         */
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, -65.5, 31.3,
                0, 50, 500, 28, 29, -17, 19, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();

        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidPointWithAltitudeAndUncertaintyEllipsoid 2
         */
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, -34.9564254283905, -54.93779897689819,
                0, 25, 12, 30, 5, 570, 35, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid2();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidArc
         */
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidArc, 1, 171.3, 0, 0, 0, 0, 39, 0, 0, 6000, 15, 11, 12);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidArc();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidArc 2
         */
        // locationEstimate: a023c3ec36ab8f03072a1b413c
        //    1010 .... = Location estimate: Ellipsoid Arc (10)
        //    0... .... = Sign of latitude: North (0)
        //    .010 0011 1100 0011 1110 1100 = Degrees of latitude: 2343916 (25.14749 degrees)
        //    0011 0110 1010 1011 1000 1111 = Degrees of longitude: 3582863 (76.87990 degrees)
        //    Inner radius: 775
        //    .010 1010 = Uncertainty radius: 42
        //    Offset angle: 27
        //    Included angle: 65
        //    .011 1100 = Confidence(%): 60
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidArc, 25.14749050140381, 76.87989950180054, 0, 0, 0, 0, 60, 0, 0, 775, 537.6369923749309, 27, 65);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidArc2();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidPoint
         */
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPoint();
        assertTrue(Arrays.equals(rawData, encodedData));

        /*
         * EllipsoidPoint 2
         */
        // locationEstimate: 002b6cf135f4be
        //    0000 .... = Location estimate: Ellipsoid Point (0)
        //    0... .... = Sign of latitude: North (0)
        //    .010 1011 0110 1100 1111 0001 = Degrees of latitude: 2845937 (30.53360 degrees)
        //    0011 0101 1111 0100 1011 1110 = Degrees of longitude: 3536062 (75.87566 degrees)
        //    [Location OSM URI: https://www.openstreetmap.org/?mlat=30.53360&mlon=75.87566&zoom=12]
        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, 30.53360, 75.87566, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPoint2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
