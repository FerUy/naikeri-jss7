
package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.EUtranCgiImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationEPSImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.TAIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

/**
*
* @author Lasith Waruna Perera
* @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
*
*/
public class PurgeMSRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, -111, 34, 50, -12 };
    };

    public byte[] getData2() {
        return new byte[] { -93, 60, 4, 5, 17, 17, 33, 34, 34, -128, 4, -111, 34, 50, -12, -127, 4, -111, 34, 50, -11, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33 };
    }

    public byte[] getDataLocInfoWLocEPS() {
        return new byte[] {
                (byte) 0xa3, (byte) 0x81,
                (byte) 0x80, 0x04, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65,
                0x08, (byte) 0x90, (byte) 0xf6, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
                0x01, (byte) 0x94, 0x00, 0x00, (byte) 0xa2, 0x6b, (byte) 0x81, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00, 0x00, (byte) 0x86,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00, 0x00,
                (byte) 0xaa, 0x57, (byte) 0x80, 0x07, 0x47, (byte) 0xf8, 0x70, 0x00,
                0x47, 0x73, 0x04, (byte) 0x81, 0x05, 0x47, (byte) 0xf8, 0x70,
                0x1b, 0x6c, (byte) 0x83, 0x08, 0x10, (byte) 0xb1, (byte) 0xa6, 0x3f,
                (byte) 0xd8, 0x12, (byte) 0xe0, 0x00, (byte) 0x85, 0x00, (byte) 0x86, 0x01,
                0x00, (byte) 0x87, 0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30,
                0x33, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33,
                0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67
        };
    }

    public byte[] getDataLocInfo() {
        return new byte[] { (byte) 0xa3, 0x3f,
                0x04, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                (byte) 0x90, (byte) 0xf6, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x00, (byte) 0xa2, 0x2a, 0x02, 0x01, 0x01,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00,
                0x00, (byte) 0x82, 0x08, 0x04, (byte) 0x97, 0x18, 0x29, 0x30,
                0x69, (byte) 0x91, 0x40, (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x47,
                (byte) 0xf8, 0x10, 0x00, 0x6d, 0x27, (byte) 0xbf, (byte) 0x86, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00, 0x00
        };
    }

    public byte[] getDataLocInfoEPS() {
        return new byte[] { (byte) 0xa3, 0x6e,
                0x04, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                (byte) 0x90, (byte) 0xf6, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x00, (byte) 0xa4, 0x59, (byte) 0x80, 0x07, 0x47,
                (byte) 0xf8, 0x10, 0x00, 0x09, 0x5f, 0x02, (byte) 0x81, 0x05,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x6d, (byte) 0x84, 0x0a, 0x01,
                0x10, (byte) 0xb1, (byte) 0xa6, 0x78, (byte) 0xd8, 0x12, 0x3d, 0x01,
                0x02, (byte) 0x85, 0x00, (byte) 0x86, 0x01, 0x00,(byte)  0x87, 0x36,
                0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d,
                0x6d, 0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30,
                0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63,
                0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e,
                0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33,
                0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f,
                0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67
        };
    }

    public byte[] getDataLocInfoGPRS() {
        return new byte[] { (byte) 0xa3, 0x45,
                0x04, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                0x73, (byte) 0xf0, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x00, (byte) 0xa3, 0x30, (byte) 0xa0, 0x09, (byte) 0x80,
                0x07, 0x47, (byte) 0xf8, 0x01, 0x25, 0x1d, (byte) 0x89, 0x1c,
                (byte) 0x81, 0x06, 0x47, (byte) 0xf8, 0x10, 0x00, 0x65, 0x16,
                (byte) 0x82, 0x08, 0x10, (byte) 0xb1, (byte) 0xa4, (byte) 0xbf, (byte) 0xd8, (byte) 0xdb,
                (byte) 0xe0, 0x03, (byte) 0x83, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x00, (byte) 0x84, 0x03, 0x31, 0x33, 0x32,
                (byte) 0x88, 0x00, (byte) 0x89, 0x01, 0x00
        };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        /*
         * MAP version 2
         */
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        PurgeMSRequestImpl pms = new PurgeMSRequestImpl(2);
        pms.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        // imsi
        assertEquals(pms.getImsi().getData(), "1111122222");
        // vlrNumber
        ISDNAddressString vlrNumber = pms.getVlrNumber();
        assertEquals(vlrNumber.getAddress(), "22234");
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        /*
         * MAP version 3 / data 2
         */
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        pms = new PurgeMSRequestImpl(3);
        pms.decodeAll(asn);
        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        // imsi
        assertEquals(pms.getImsi().getData(), "1111122222");
        // vlrNumber
        vlrNumber = pms.getVlrNumber();
        assertEquals(vlrNumber.getAddress(), "22234");
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // sgsnNumber
        ISDNAddressString sgsnNumber = pms.getSgsnNumber();
        assertEquals(sgsnNumber.getAddress(), "22235");
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        // MAPExtensionContainerTest
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pms.getExtensionContainer()));
        // locationInformation
        assertNull(pms.getLocationInformation());
        // locationInformationGPRS
        assertNull(pms.getLocationInformationGPRS());
        // locationInformationEPS
        assertNull(pms.getLocationInformationEPS());

        /*
         * data containing LocationInformation with LocationInformationEPS
         */
        data = this.getDataLocInfoWLocEPS();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        pms = new PurgeMSRequestImpl(3);
        pms.decodeAll(asn);
        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        // imsi
        assertEquals(pms.getImsi().getData(), "901405105680096");
        //  vlrNumber
        vlrNumber = pms.getVlrNumber();
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710490000");
        //  sgsnNumber
        sgsnNumber = pms.getSgsnNumber();
        assertNull(sgsnNumber);
        // LocationInformation
        // ageOfLocationInformation
        LocationInformation locationInformation = pms.getLocationInformation();
        Integer aol = locationInformation.getAgeOfLocationInformation();
        assertNull(aol);
        //	geographicalInformation
        GeographicalInformation geogInfo = pms.getLocationInformation().getGeographicalInformation();
        assertNull(geogInfo);
        //	vlr-number
        vlrNumber = locationInformation.getVlrNumber();
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710490000");
        //	locationNumber
        LocationNumberMap locationNumberMap = locationInformation.getLocationNumber();
        assertNull(locationNumberMap);
        //	cellGlobalIdOrServiceAreaIdOrLAI
        CellGlobalIdOrServiceAreaIdOrLAI cgiOrSaiOrLai = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI();
        assertNull(cgiOrSaiOrLai);
        //	extensionContainer
        MAPExtensionContainer extensionContainer = locationInformation.getExtensionContainer();
        assertNull(extensionContainer);
        //	selectedLSA-Id
        LSAIdentity selectedLSAId = locationInformation.getSelectedLSAId();
        assertNull(selectedLSAId);
        //	msc-Number
        ISDNAddressString mscNumber = locationInformation.getMscNumber();
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscNumber.getAddress(), "491710490000");
        //	geodeticInformation
        GeodeticInformation geodInfo = locationInformation.getGeodeticInformation();
        assertNull(geodInfo);
        //	currentLocationRetrieved
        boolean currentLocationRetrieved = locationInformation.getCurrentLocationRetrieved();
        assertFalse(currentLocationRetrieved);
        //	sai-Present
        boolean saiPresent = locationInformation.getSaiPresent();
        assertFalse(saiPresent);
        //	locationInformationEPS
        LocationInformationEPS locationInfoEPS = pms.getLocationInformation().getLocationInformationEPS();
        //	e-utranCellGlobalIdentity
        EUtranCgi eUtranCgi = locationInfoEPS.getEUtranCellGlobalIdentity();
        assertEquals(eUtranCgi.getMCC(), 748);
        assertEquals(eUtranCgi.getMNC(), 7);
        assertEquals(eUtranCgi.getEci(), 4682500);
        assertEquals(eUtranCgi.getENodeBId(), 18291);
        assertEquals(eUtranCgi.getCi(), 4);
        //	trackingAreaIdentity
        TAId taId = locationInfoEPS.getTrackingAreaIdentity();
        assertEquals(taId.getMCC(), 748);
        assertEquals(taId.getMNC(), 7);
        assertEquals(taId.getTAC(), 7020);
        //	extensionContainer
        extensionContainer = locationInfoEPS.getExtensionContainer();
        assertNull(extensionContainer);
        //	geographicalInformation
        geogInfo = locationInfoEPS.getGeographicalInformation();
        assertEquals(geogInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(geogInfo.getLatitude(), -34.90973353385925);
        assertEquals(geogInfo.getLongitude(), -56.14631652832031);
        assertEquals(geogInfo.getUncertainty(), 0.0);
        //	geodeticInformation
        geodInfo = locationInfoEPS.getGeodeticInformation();
        assertNull(geodInfo);
        //	currentLocationRetrieved
        currentLocationRetrieved = locationInfoEPS.getCurrentLocationRetrieved();
        assertTrue(currentLocationRetrieved);
        //	ageOfLocationInformation
        aol = locationInfoEPS.getAgeOfLocationInformation();
        assertEquals(aol.intValue(), 0);
        //	mme-Name
        DiameterIdentity mmeName = locationInfoEPS.getMmeName();
        assertEquals(mmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        //	userCSGInformation
        UserCSGInformation userCSGInformation = locationInformation.getUserCSGInformation();
        assertNull(userCSGInformation);
        // LocationInformationGPRS
        LocationInformationGPRS locationInformationGPRS = pms.getLocationInformationGPRS();
        assertNull(locationInformationGPRS);
        // locationInformationEPS
        locationInfoEPS = pms.getLocationInformationEPS();
        assertNull(locationInfoEPS);

        /*
         * version 3 / data containing LocationInformation without LocationInformationEPS
         */
        data = this.getDataLocInfo();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        pms = new PurgeMSRequestImpl(3);
        pms.decodeAll(asn);
        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        // imsi
        assertEquals(pms.getImsi().getData(), "901405105680096");
        // vlrNumber
        vlrNumber = pms.getVlrNumber();
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710490000");
        // sgsnNumber
        sgsnNumber = pms.getSgsnNumber();
        assertNull(sgsnNumber);
        // LocationInformation
        // ageOfLocationInformation
        locationInformation = pms.getLocationInformation();
        aol = locationInformation.getAgeOfLocationInformation();
        assertEquals(aol.intValue(), 1);
        //	geographicalInformation
        geogInfo = pms.getLocationInformation().getGeographicalInformation();
        assertNull(geogInfo);
        //	vlr-number
        vlrNumber = locationInformation.getVlrNumber();
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710490000");
        //	locationNumber
        locationNumberMap = locationInformation.getLocationNumber();
        LocationNumber locationNumber = locationNumberMap.getLocationNumber();
        assertEquals(locationNumber.getNatureOfAddressIndicator(), 4);
        assertEquals(locationNumber.getNumberingPlanIndicator(), 1);
        assertEquals(locationNumber.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(locationNumber.getInternalNetworkNumberIndicator(), 1);
        assertEquals(locationNumber.getScreeningIndicator(), 3);
        assertEquals(locationNumber.getAddress(), "819203961904");
        //	cellGlobalIdOrServiceAreaIdOrLAI
        cgiOrSaiOrLai = locationInformation.getCellGlobalIdOrServiceAreaIdOrLAI();
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 109);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 10175);
        //	extensionContainer
        extensionContainer = locationInformation.getExtensionContainer();
        assertNull(extensionContainer);
        //	selectedLSA-Id
        selectedLSAId = locationInformation.getSelectedLSAId();
        assertNull(selectedLSAId);
        //	msc-Number
        mscNumber = locationInformation.getMscNumber();
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscNumber.getAddress(), "491710490000");
        //	geodeticInformation
        geodInfo = locationInformation.getGeodeticInformation();
        assertNull(geodInfo);
        //	currentLocationRetrieved
        currentLocationRetrieved = locationInformation.getCurrentLocationRetrieved();
        assertFalse(currentLocationRetrieved);
        //	sai-Present
        saiPresent = locationInformation.getSaiPresent();
        assertFalse(saiPresent);
        //	locationInformationEPS
        locationInfoEPS = pms.getLocationInformation().getLocationInformationEPS();
        assertNull(locationInfoEPS);
        //	userCSGInformation
        userCSGInformation = locationInformation.getUserCSGInformation();
        assertNull(userCSGInformation);
        // LocationInformationGPRS
        locationInformationGPRS = pms.getLocationInformationGPRS();
        assertNull(locationInformationGPRS);
        // locationInformationEPS
        locationInfoEPS = pms.getLocationInformationEPS();
        assertNull(locationInfoEPS);

        /*
         * MAP version 3 / another data containing only LocationInformationEPS as location information
         */
        data = this.getDataLocInfoEPS();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        pms = new PurgeMSRequestImpl(3);
        pms.decodeAll(asn);
        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        // imsi
        assertEquals(pms.getImsi().getData(), "901405105680096");
        // vlrNumber
        vlrNumber = pms.getVlrNumber();
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710490000");
        // sgsnNumber
        sgsnNumber = pms.getSgsnNumber();
        assertNull(sgsnNumber);
        // LocationInformation
        locationInformation = pms.getLocationInformation();
        assertNull(locationInformation);
        // LocationInformationGPRS
        locationInformationGPRS = pms.getLocationInformationGPRS();
        assertNull(locationInformationGPRS);
        //	locationInformationEPS
        locationInfoEPS = pms.getLocationInformationEPS();
        //	e-utranCellGlobalIdentity
        eUtranCgi = locationInfoEPS.getEUtranCellGlobalIdentity();
        assertEquals(eUtranCgi.getMCC(), 748);
        assertEquals(eUtranCgi.getMNC(), 1);
        assertEquals(eUtranCgi.getEci(), 614146);
        assertEquals(eUtranCgi.getENodeBId(), 2399);
        assertEquals(eUtranCgi.getCi(), 2);
        //	trackingAreaIdentity
        taId = locationInfoEPS.getTrackingAreaIdentity();
        assertEquals(taId.getMCC(), 748);
        assertEquals(taId.getMNC(), 1);
        assertEquals(taId.getTAC(), 109);
        //	extensionContainer
        extensionContainer = locationInfoEPS.getExtensionContainer();
        assertNull(extensionContainer);
        //	geographicalInformation
        geogInfo = locationInfoEPS.getGeographicalInformation();
        assertNull(geogInfo);
        //	geodeticInformation
        geodInfo = locationInfoEPS.getGeodeticInformation();
        assertEquals(geodInfo.getScreeningAndPresentationIndicators(), 1);
        assertEquals(geodInfo.getUncertainty(), 1.0000000000000009);
        assertEquals(geodInfo.getConfidence(), 2);
        assertEquals(geodInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(geodInfo.getLatitude(), -34.91034507751465);
        assertEquals(geodInfo.getLongitude(), -56.14981412887573);
        //	currentLocationRetrieved
        currentLocationRetrieved = locationInfoEPS.getCurrentLocationRetrieved();
        assertTrue(currentLocationRetrieved);
        //	ageOfLocationInformation
        aol = locationInfoEPS.getAgeOfLocationInformation();
        assertEquals(aol.intValue(), 0);
        //	mme-Name
        mmeName = locationInfoEPS.getMmeName();
        assertEquals(mmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());

        /*
         * MAP version 3 / another data containing only LocationInformationGPRS as location information
         */
        data = this.getDataLocInfoGPRS();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        pms = new PurgeMSRequestImpl(3);
        pms.decodeAll(asn);
        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        // imsi
        assertEquals(pms.getImsi().getData(), "901405105680370");
        // vlrNumber
        vlrNumber = pms.getVlrNumber();
        assertNull(vlrNumber);
        // sgsnNumber
        sgsnNumber = pms.getSgsnNumber();
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sgsnNumber.getAddress(), "491710490000");
        // LocationInformation
        locationInformation = pms.getLocationInformation();
        assertNull(locationInformation);
        // LocationInformationGPRS
        locationInformationGPRS = pms.getLocationInformationGPRS();
        //	cellGlobalIdOrServiceAreaIdOrLAI
        cgiOrSaiOrLai = locationInformationGPRS.getCellGlobalIdOrServiceAreaIdOrLAI();
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 10);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 9501);
        assertEquals(cgiOrSaiOrLai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 35100);
        // routeingAreaIdentity
        RAIdentity rai= locationInformationGPRS.getRouteingAreaIdentity();
        assertEquals(rai.getMCC(), 748);
        assertEquals(rai.getMNC(), 1);
        assertEquals(rai.getLAC(), 101);
        assertEquals(rai.getRAC(), 22);
        //	geographicalInformation
        geogInfo = locationInformationGPRS.getGeographicalInformation();
        assertEquals(geogInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(geogInfo.getLatitude(), -34.90561366081238);
        assertEquals(geogInfo.getLongitude(), -55.04219055175781);
        assertEquals(geogInfo.getUncertainty(), 3.310000000000004);
        //	geodeticInformation
        geodInfo = locationInformationGPRS.getGeodeticInformation();
        assertNull(geodInfo);
        // sgsnNumber
        sgsnNumber = locationInformationGPRS.getSGSNNumber();
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sgsnNumber.getAddress(), "491710490000");
        // selectedLSAId
        selectedLSAId = locationInformationGPRS.getLSAIdentity();
        assertEquals(selectedLSAId.getData(), new byte[] {49, 51, 50});
        //	currentLocationRetrieved
        currentLocationRetrieved = locationInformationGPRS.isCurrentLocationRetrieved();
        assertTrue(currentLocationRetrieved);
        // ageOfLocationInformation
        aol = locationInformationGPRS.getAgeOfLocationInformation();
        assertEquals(aol.intValue(), 0);
        //	locationInformationEPS
        locationInfoEPS = pms.getLocationInformationEPS();
        assertNull(locationInfoEPS);

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
        // version 2
        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        IMSIImpl imsi = new IMSIImpl("1111122222");

        PurgeMSRequestImpl pms = new PurgeMSRequestImpl(imsi, vlrNumber, null, extensionContainer, null, null, null, 2);

        AsnOutputStream asn = new AsnOutputStream();
        pms.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // version 3
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22235");
        pms = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, null, null, null, 3);

        asn = new AsnOutputStream();
        pms.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        /*
         * another test data (this.getDataLocInfoWLocEPS()) containing LocationInformation with LocationInformationEPS
         */
        // imsi
        imsi = new IMSIImpl("901405105680096");
        // vlrNumber
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        // sgsnNumber
        sgsnNumber = null;
        // LocationInformation
        // ageOfLocationInformation
        Integer aol = null;
        // geographicalInformation
        GeographicalInformation geographicalInformation = null;
        //locationNumber
        LocationNumber locationNumber;
        LocationNumberMap locationNumberMap;
        // cellGlobalIdOrServiceAreaIdOrLAI
        CellGlobalIdOrServiceAreaIdOrLAI cgiOrSaiOrLai = null;
        // extensionContainer
        extensionContainer = null;
        // selectedLSA-Id
        LSAIdentity selectedLSAId = null;
        // msc-Number
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        // geodeticInformation
        GeodeticInformation geodeticInformation = null;
        // currentLocationRetrieved
        boolean currentLocationRetrieved = false;
        // saiPresent
        boolean saiPresent = false;
        // LocationInformationEPS within LocationInformation
        // eUtranCgi
        EUtranCgi eUtranCgi = new EUtranCgiImpl(hexStringToByteArray("47f87000477304"));
        assertEquals(eUtranCgi.getMCC(), 748);
        assertEquals(eUtranCgi.getMNC(), 7);
        assertEquals(eUtranCgi.getEci(), 4682500);
        assertEquals(eUtranCgi.getENodeBId(), 18291);
        assertEquals(eUtranCgi.getCi(), 4);
        // taId;
        TAId taId = new TAIdImpl(hexStringToByteArray("47f8701b6c"));
        assertEquals(taId.getMCC(), 748);
        assertEquals(taId.getMNC(), 7);
        assertEquals(taId.getTAC(), 7020);
        // geographicalInformation
        TypeOfShape typeOfShape = TypeOfShape.EllipsoidPointWithUncertaintyCircle;
        double latitude = -34.909744;
        double longitude = -56.146317;
        double uncertainty = 1.0;
        geographicalInformation = new GeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty);
        // boolean currentLocationRetrieved
        currentLocationRetrieved = true;
        // ageOfLocationInformation
        aol = 0;
        // mmeName
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        LocationInformationEPS locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                geodeticInformation, currentLocationRetrieved, aol, mmeName);
        // userCSGInformation
        UserCSGInformation userCSGInformation = null;
        aol = null;
        geographicalInformation = null;
        locationNumberMap = null;
        currentLocationRetrieved = false;
        LocationInformation locationInformation = new LocationInformationImpl(aol, geographicalInformation,
                vlrNumber, locationNumberMap, cgiOrSaiOrLai, extensionContainer, selectedLSAId, mscNumber,
                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
        // LocationInformationGPRS
        LocationInformationGPRS locationInformationGPRS = null;
        // LocationInformationEPS
        locationInformationEPS = null;
        pms = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, locationInformation, locationInformationGPRS, locationInformationEPS, 3);

        asn = new AsnOutputStream();
        pms.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLocInfoWLocEPS()));

        /*
         * another test data (this.getDataLocInfo()) containing LocationInformation without LocationInformationEPS
         */
        // ageOfLocationInformation
        aol = 1;
        //	locationNumber
        int natureOfAddressIndicator = 4;
        String locationNumberAddressDigits= "819203961904";
        int numberingPlanIndicator = 1;
        int internalNetworkNumberIndicator = 1;
        int addressRepresentationRestrictedIndicator = 1;
        int screeningIndicator = 3;
        locationNumber = new LocationNumberImpl(natureOfAddressIndicator, locationNumberAddressDigits, numberingPlanIndicator,
                internalNetworkNumberIndicator, addressRepresentationRestrictedIndicator, screeningIndicator);
        locationNumberMap = new LocationNumberMapImpl(locationNumber);
        //	cellGlobalIdOrServiceAreaIdOrLAI
        int mcc = 748;
        int mnc = 1;
        int lac = 109;
        int cellId = 10175;
        CellGlobalIdOrServiceAreaIdFixedLength cgiFixed = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
        cgiOrSaiOrLai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiFixed);
        // LocationInformation
        locationInformation = new LocationInformationImpl(aol, geographicalInformation,
                vlrNumber, locationNumberMap, cgiOrSaiOrLai, extensionContainer, selectedLSAId, mscNumber,
                geodeticInformation, currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);

        pms = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, locationInformation, locationInformationGPRS, locationInformationEPS, 3);

        asn = new AsnOutputStream();
        pms.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLocInfo()));

        /*
         * another test data (this.getDataLocInfoEPS()) containing only LocationInformationEPS as location information
         */
        // LocationInformation
        locationInformation = null;
        //	locationInformationEPS
        //	e-utranCellGlobalIdentity
        eUtranCgi = new EUtranCgiImpl(hexStringToByteArray("47f81000095f02"));
        assertEquals(eUtranCgi.getMCC(), 748);
        assertEquals(eUtranCgi.getMNC(), 1);
        assertEquals(eUtranCgi.getEci(), 614146);
        assertEquals(eUtranCgi.getENodeBId(), 2399);
        assertEquals(eUtranCgi.getCi(), 2);
        //	trackingAreaIdentity
        taId = new TAIdImpl(hexStringToByteArray("47f810006d"));
        assertEquals(taId.getMCC(), 748);
        assertEquals(taId.getMNC(), 1);
        assertEquals(taId.getTAC(), 109);
        //	geodeticInformation
        int screeningAndPresentationIndicators = 1;
        uncertainty = 1.0000000000000009;
        int confidence = 2;
        latitude = -34.91034507751465;
        longitude = -56.14981412887573;
        geodeticInformation = new GeodeticInformationImpl(screeningAndPresentationIndicators, typeOfShape, latitude, longitude, uncertainty, confidence);
        //	currentLocationRetrieved
        currentLocationRetrieved = true;
        //	ageOfLocationInformation
        aol = 0;
        //	mme-Name
        mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        // LocationInformationEPS
        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, extensionContainer, geographicalInformation,
                geodeticInformation, currentLocationRetrieved, aol, mmeName);

        pms = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, locationInformation, locationInformationGPRS, locationInformationEPS, 3);

        asn = new AsnOutputStream();
        pms.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLocInfoEPS()));

        /*
         * another test data (this.getDataLocInfoGPRS()) containing only LocationInformationEPS as location information
         */
        // imsi
        imsi = new IMSIImpl("901405105680370");
        // vlrNumber
        vlrNumber = null;
        // sgsnNumber
        sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        // LocationInformationGPRS
        //	cellGlobalIdOrServiceAreaIdOrLAI
        mnc = 10;
        lac = 9501;
        cellId = 35100;
        cgiFixed = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(mcc, mnc, lac, cellId);
        cgiOrSaiOrLai = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cgiFixed);
        // routeingAreaIdentity
        RAIdentity routeingAreaIdentity = new RAIdentityImpl(hexStringToByteArray("47f810006516"));
        assertEquals(routeingAreaIdentity.getMCC(), 748);
        assertEquals(routeingAreaIdentity.getMNC(), 1);
        assertEquals(routeingAreaIdentity.getLAC(), 101);
        assertEquals(routeingAreaIdentity.getRAC(), 22);
        //	geographicalInformation
        latitude = -34.90561366081238;
        longitude = -55.04219055175781;
        uncertainty = 3.310000000000004;
        geographicalInformation = new GeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty);
        //	geodeticInformation
        geodeticInformation = null;
        // selectedLSAId
        selectedLSAId = new LSAIdentityImpl(new byte[] {49, 51, 50});
        // ageOfLocationInformation
        aol = 0;
        //	locationInformationGPRS
        locationInformationGPRS =new LocationInformationGPRSImpl(cgiOrSaiOrLai,
                routeingAreaIdentity, geographicalInformation, sgsnNumber, selectedLSAId, extensionContainer, saiPresent, geodeticInformation,
                currentLocationRetrieved, aol);
        //	locationInformationEPS
        locationInformationEPS = null;

        pms = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, locationInformation, locationInformationGPRS, locationInformationEPS, 3);

        asn = new AsnOutputStream();
        pms.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLocInfoGPRS()));



    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
