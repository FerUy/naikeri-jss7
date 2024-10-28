
package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.Polygon;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranAdditionalPositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranCivicAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
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
                -111, 0, -110, 0, -109, 1, 0, -108, 4, 26, 27, 28, 29, -107, 1, 9, -74, 6, 2, 1, 10, 2, 1, 11, -105, 0, -104,
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
        return new byte[] { 26, 27, 28, 29 };
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
        GSNAddress hgmlcAddress = slr0.getHGMLCAddress();
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
        assertTrue(Arrays.equals(hgmlcAddress.getData(), getGSNAddress()));
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
        hgmlcAddress = slr1.getHGMLCAddress();
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
        assertNull(hgmlcAddress);
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
        hgmlcAddress = slr2.getHGMLCAddress();
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
        assertNull(hgmlcAddress);
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
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        // test 1
        byte[] data = getEncodedDataSergey();

        LCSEvent lcsEvent = LCSEvent.emergencyCallOrigination;
        LCSClientIDImpl lcsClientID = new LCSClientIDImpl(LCSClientType.plmnOperatorServices, null, null, null, null, null, null);
        ISDNAddressStringImpl networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4444455555");
        LMSI lmsi;
        boolean gprsNodeIndicator = false;
        LCSLocationInfoImpl lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, gprsNodeIndicator, null,
                null, null, null, null, null, null);
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "6666677777");
        IMSIImpl imsi = new IMSIImpl("1234512345");
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        ISDNAddressString naEsrd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "8888899999");
        ISDNAddressString naEsrk = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "8888800000");
        ExtGeographicalInformationImpl locationEstimate = new ExtGeographicalInformationImpl(getDataExtGeographicalInformation());
        int ageOfLocationEstimate = 5;
        SLRArgPCSExtensionsImpl slrArgPcsExtensions = new SLRArgPCSExtensionsImpl(true);
        SLRArgExtensionContainerImpl slrArgExtensionContainer = new SLRArgExtensionContainerImpl(null, slrArgPcsExtensions);
        AddGeographicalInformationImpl addLocationEstimate = new AddGeographicalInformationImpl(getDataAddGeographicalInformation());
        boolean msAvailable = true;
        boolean enteringIntoArea = false;
        boolean leavingFromArea = false;
        boolean beingInsideArea = false;
        boolean periodicLDR = false;
        DeferredLocationEventTypeImpl deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea, periodicLDR);
        DeferredmtlrDataImpl deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, null, lcsLocationInfo);
        Integer lcsReferenceNumber = 6;
        PositioningDataInformationImpl geranPositioningData = new PositioningDataInformationImpl(getPositioningDataInformation());
        UtranPositioningDataInfoImpl utranPositioningData = new UtranPositioningDataInfoImpl(getUtranPositioningDataInfo());
        LAIFixedLengthImpl laiFixedLength = new LAIFixedLengthImpl(220, 12, 4321);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(laiFixedLength);
        GSNAddressImpl hgmlcAddress = new GSNAddressImpl(getGSNAddress());
        Integer lcsServiceTypeID = 7;
        boolean saiPresent = true;
        boolean pseudonymIndicator = true;
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;
        VelocityEstimateImpl velocityEstimate = new VelocityEstimateImpl(getVelocityEstimate());
        Integer sequenceNumber = 9;
        int reportingAmount = 10;
        int reportingInterval = 11;
        PeriodicLDRInfoImpl periodicLDRInfo = new PeriodicLDRInfoImpl(reportingAmount, reportingInterval, null);
        boolean moLrShortCircuitIndicator = true;
        GeranGANSSpositioningDataImpl geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(getGeranGANSSpositioningData());
        UtranGANSSpositioningDataImpl utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(getUtranGANSSpositioningData());
        ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "192837465");
        ServingNodeAddressImpl targetServingNodeForHandover = new ServingNodeAddressImpl(mscNumber, true);
        UtranAdditionalPositioningData utranAdditionalPositioningData = null;
        Integer utranBaroPressureMeas = null;
        UtranCivicAddress utranCivicAddress = null;

        SubscriberLocationReportRequestImpl slr0 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        AsnOutputStream asnOS = new AsnOutputStream();
        slr0.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        // test 2 with real data from Indian operator
        data = getEncodedDataIndia1();
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
        lcsClientID = new LCSClientIDImpl(LCSClientType.emergencyServices, null, null, null, null, null,
                null);
        networkNodeNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919451999962");
        lmsi = new LMSIImpl(new byte[] { 31, -78, -126, 0 });
        lcsLocationInfo = new LCSLocationInfoImpl(networkNodeNumber, lmsi, null, gprsNodeIndicator, null,
                null, null, null, null, null, null);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "919415578942");
        imsi = new IMSIImpl("404556140835931");
        imei = null;
        naEsrd = null;
        naEsrk = null;
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
        locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty,
                uncertaintySemiMajorAxis, uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude,
                innerRadius, uncertaintyRadius, offsetAngle, includedAngle);
        ageOfLocationEstimate = 0;
        slrArgExtensionContainer = null;
        addLocationEstimate = null;
        deferredmtlrData = null;
        lcsReferenceNumber = null;
        geranPositioningData = null;
        utranPositioningData = null;
        CellGlobalIdOrServiceAreaIdFixedLength cgiOrSaiFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(404, 55, 29105, 12492);
        cellIdOrSai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiOrSaiFixedLength);
        hgmlcAddress = null;
        lcsServiceTypeID = null;
        pseudonymIndicator = false;
        accuracyFulfilmentIndicator = null;
        velocityEstimate = null;
        sequenceNumber = null;
        periodicLDRInfo = null;
        moLrShortCircuitIndicator = false;
        geranGANSSpositioningData = null;
        utranGANSSpositioningData = null;
        targetServingNodeForHandover = null;

        SubscriberLocationReportRequestImpl slr1 = new SubscriberLocationReportRequestImpl(lcsEvent,
                lcsClientID, lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData,
                cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        slr1.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

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
                cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator,
                velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData, utranGANSSpositioningData,
                targetServingNodeForHandover, utranAdditionalPositioningData, utranBaroPressureMeas, utranCivicAddress);

        asnOS = new AsnOutputStream();
        slr2.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }
}
