
package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DaylightSavingTime;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.IMSVoiceOverPsSessionsIndication;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation5GS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NRCellGlobalId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NRTAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TimeZone;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
*/
public class ProvideSubscriberInfoResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 6, 48, 4, (byte) 161, 2, (byte) 129, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 47, 48, 4, (byte) 161, 2, (byte) 129, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataCs3() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x1e, 0x30, (byte) 0x82,
                0x01, 0x1a, (byte) 0xa0, 0x5b, (byte) 0xaa, 0x59, (byte) 0x80, 0x07,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x09, 0x5f, 0x02, (byte) 0x81,
                0x05, 0x47, (byte) 0xf8, 0x10, 0x00, 0x6d, (byte) 0x84, 0x0a,
                0x03, 0x10, (byte) 0xb1, (byte) 0xa6, 0x78, (byte) 0xd8, 0x12, 0x3d,
                0x01, 0x01, (byte) 0x85, 0x00, (byte) 0x86, 0x01, 0x00, (byte) 0x87,
                0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e,
                0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33, 0x30, 0x30,
                0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0xa1,
                0x02, (byte) 0x80, 0x00, (byte) 0x85, 0x08, 0x10, 0x71, 0x41,
                0x00, 0x64, 0x16, 0x50, (byte) 0xf0, (byte) 0x86, 0x03, 0x39,
                0x3a, 0x52, (byte) 0xa8, 0x1b, (byte) 0x80, 0x03, (byte) 0x94, 0x71,
                0x01, (byte) 0x81, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65,
                0x08, 0x74, (byte) 0xf4, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x77, 0x39, (byte) 0xf7, (byte) 0x83, 0x01, 0x04, (byte) 0x89,
                0x01, 0x00, (byte) 0x8a, 0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5,
                (byte) 0x8b, 0x01, 0x04, (byte) 0x8e, 0x02, 0x00, 0x03, (byte) 0x8f,
                0x01, 0x00, (byte) 0xb0, 0x78, (byte) 0x80, 0x08, 0x47, (byte) 0xf8,
                0x20, 0x08, 0x00, 0x00, 0x00, 0x08, (byte) 0x81, 0x07,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x07, (byte) 0xf0, 0x01, (byte) 0x83,
                0x0a, 0x03, 0x10, (byte) 0xb1, (byte) 0xa6, 0x78, (byte) 0xd8, 0x12,
                0x3d, 0x01, 0x01, (byte) 0x84, 0x37, 0x61, 0x6d, 0x66,
                0x33, 0x2e, 0x63, 0x6c, 0x75, 0x73, 0x74, 0x65,
                0x72, 0x32, 0x2e, 0x6e, 0x65, 0x74, 0x32, 0x2e,
                0x61, 0x6d, 0x66, 0x2e, 0x35, 0x67, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x85, 0x05, 0x47, (byte) 0xf8,
                0x10, 0x00, 0x6d, (byte) 0x86, 0x00, (byte) 0x87, 0x01, 0x00,
                (byte) 0x88, 0x03, 0x47, (byte) 0xf8, 0x20, (byte) 0x89, 0x02, 0x00,
                (byte) 0xfa, (byte) 0x8a, 0x01, 0x04, (byte) 0x8c, 0x06, 0x47, (byte) 0xf8,
                0x20, 0x07, (byte) 0x8f, (byte) 0xd2
        };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberInformation" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ProvideSubscriberInfoResponseImpl asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getSubscriberInfo().getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertNull(asc.getExtensionContainer());

        // test 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getSubscriberInfo().getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        // test 3 (data taken from MAP load test for CS domain, containing location information EPS and 5GS
        rawData = getEncodedDataCs3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // Subscriber Info
        SubscriberInfo si = asc.getSubscriberInfo();
        // LocationInformation
        LocationInformation li = si.getLocationInformation();
        Integer aol = li.getAgeOfLocationInformation();
        GeographicalInformation liGeographicInfo = li.getGeographicalInformation();
        ISDNAddressString vlrNumber = li.getVlrNumber();
        LocationNumberMap locationNumberMap = li.getLocationNumber();
        CellGlobalIdOrServiceAreaIdOrLAI loCGIorSAIorLAI = li.getCellGlobalIdOrServiceAreaIdOrLAI();
        MAPExtensionContainer liExtensionContainer = li.getExtensionContainer();
        LSAIdentity liLsaId = li.getSelectedLSAId();
        ISDNAddressString mscAddress = li.getMscNumber();
        GeodeticInformation liGeodeticInfo = li.getGeodeticInformation();
        boolean liCurrentLocationRetrieved = li.getCurrentLocationRetrieved();
        boolean liSaiPresent = li.getSaiPresent();
        LocationInformationEPS liLocInfoEPS = li.getLocationInformationEPS();
        EUtranCgi liLTECgi = liLocInfoEPS.getEUtranCellGlobalIdentity();
        TAId liTAId = liLocInfoEPS.getTrackingAreaIdentity();
        MAPExtensionContainer liLocInfoEPSExtensionContainer = liLocInfoEPS.getExtensionContainer();
        GeographicalInformation liLocInfoEPSGeographicalInfo = liLocInfoEPS.getGeographicalInformation();
        GeodeticInformation liLocEPSInfoGeodeticInfo = liLocInfoEPS.getGeodeticInformation();
        boolean liLocInfoEPSCurrentLocationRetrieved = liLocInfoEPS.getCurrentLocationRetrieved();
        Integer liLocInfoEPSAgeOfLocationInformation = liLocInfoEPS.getAgeOfLocationInformation();
        DiameterIdentity liLocInfoEPSMmeName = liLocInfoEPS.getMmeName();
        UserCSGInformation liUserCSGInformation = li.getUserCSGInformation();
        assertNotNull(li);
        assertNull(aol);
        assertNull(liGeographicInfo);
        assertNull(vlrNumber);
        assertNull(locationNumberMap);
        assertNull(loCGIorSAIorLAI);
        assertNull(liExtensionContainer);
        assertNull(liLsaId);
        assertNull(mscAddress);
        assertNull(liGeodeticInfo);
        assertFalse(liCurrentLocationRetrieved);
        assertFalse(liSaiPresent);
        assertNotNull(liLocInfoEPS);
        assertEquals(liLTECgi.getMCC(), 748);
        assertEquals(liLTECgi.getMNC(), 1);
        assertEquals(liLTECgi.getEci(), 614146);
        assertEquals(liLTECgi.getENodeBId(), 2399);
        assertEquals(liLTECgi.getCi(), 2);
        assertEquals(liTAId.getMCC(), 748);
        assertEquals(liTAId.getMNC(), 1);
        assertEquals(liTAId.getTAC(), 109);
        assertNull(liLocInfoEPSExtensionContainer);
        assertNull(liLocInfoEPSGeographicalInfo);
        assertEquals(liLocEPSInfoGeodeticInfo.getScreeningAndPresentationIndicators(), 3);
        assertEquals(liLocEPSInfoGeodeticInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(liLocEPSInfoGeodeticInfo.getLatitude(), -34.91034507751465);
        assertEquals(liLocEPSInfoGeodeticInfo.getLongitude(), -56.14981412887573);
        assertEquals(liLocEPSInfoGeodeticInfo.getUncertainty(), 1.0000000000000009);
        assertEquals(liLocEPSInfoGeodeticInfo.getConfidence(), 1);
        assertTrue(liLocInfoEPSCurrentLocationRetrieved);
        assertEquals(liLocInfoEPSMmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(liLocInfoEPSAgeOfLocationInformation.intValue(), 0);
        assertNull(liUserCSGInformation);
        // SubscriberState
        SubscriberState subscriberState = si.getSubscriberState();
        assertEquals(subscriberState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        LocationInformationGPRS liGPRS = si.getLocationInformationGPRS();
        assertNull(liGPRS);
        // PSSubscriberState
        PSSubscriberState psSubscriberState = si.getPSSubscriberState();
        assertNull(psSubscriberState);
        // IMEI
        IMEI imei = si.getIMEI();
        assertEquals(imei.getIMEI(), "011714004661050");
        // MSClassmark2
        MSClassmark2 msClassmark2 = si.getMSClassmark2();
        assertEquals(msClassmark2.getData(), new byte[] {0x39, 0x3a, 0x52});
        // GPRSMSClass
        GPRSMSClass gprsmsClass = si.getGPRSMSClass();
        assertNull(gprsmsClass);
        // MNPInfoRes
        MNPInfoRes mnpInfoRes = si.getMNPInfoRes();
        RouteingNumber rn = mnpInfoRes.getRouteingNumber();
        IMSI imsi = mnpInfoRes.getIMSI();
        ISDNAddressString msisdn = mnpInfoRes.getMSISDN();
        NumberPortabilityStatus portabilityStatus = mnpInfoRes.getNumberPortabilityStatus();
        assertEquals(rn.getRouteingNumber(), "491710");
        assertEquals(imsi.getData(), "901405105680474");
        assertEquals(msisdn.getAddress(), "59899077937");
        assertEquals(portabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        // IMSVoiceOverPsSessionsIndication
        IMSVoiceOverPsSessionsIndication ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported);
        // LastUEActivityTime
        Time lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        UsedRATType lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.eUtran);
        // EPSSubscriberState
        PSSubscriberState epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        LocationInformationEPS locationInfoEPS = si.getLocationInformationEPS();
        assertNull(locationInfoEPS);
        // TimeZone
        TimeZone timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[]{0, 3});
        // DaylightSavingTime
        DaylightSavingTime daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        LocationInformation5GS li5GS = si.getLocationInformation5GS();
        NRCellGlobalId nrCGI = li5GS.getNRCellGlobalId();
        EUtranCgi li5GSLteCgi = li5GS.getEUtranCgi();
        GeographicalInformation li5GSGeographicalInfo = li5GS.getGeographicalInformation();
        GeodeticInformation li5GSGeodeticInformation = li5GS.getGeodeticInformation();
        FQDN li5GSAMFAddress = li5GS.getAMFAddress();
        TAId li5GSTAId = li5GS.getTAId();
        boolean li5GSCurrentLocationRetrieved = li5GS.isCurrentLocationRetrieved();
        Integer li5GSAgeOfLocationInformation = li5GS.getAgeOfLocationInformation();
        PlmnId li5GSVPlmnId = li5GS.getVPlmnId();
        TimeZone li5GSLocalTimeZone = li5GS.getLocalTimeZone();
        UsedRATType li5GSUsedRATType = li5GS.getUsedRATType();
        MAPExtensionContainer li5GSExtensionContainer = li5GS.getExtensionContainer();
        NRTAId li5GSNRTAId = li5GS.getNRTAId();
        assertEquals(nrCGI.getMCC(), 748);
        assertEquals(nrCGI.getMNC(), 2);
        assertEquals(nrCGI.getNCI(), 34359738376L);
        assertEquals(li5GSLteCgi.getMCC(), 748);
        assertEquals(li5GSLteCgi.getMNC(), 1);
        assertEquals(li5GSLteCgi.getEci(), 520193);
        assertEquals(li5GSLteCgi.getENodeBId(), 2032);
        assertEquals(li5GSLteCgi.getCi(), 1);
        assertNull(li5GSGeographicalInfo);
        assertEquals(li5GSGeodeticInformation.getScreeningAndPresentationIndicators(), 3);
        assertEquals(li5GSGeodeticInformation.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(li5GSGeodeticInformation.getLatitude(), -34.91034507751465);
        assertEquals(li5GSGeodeticInformation.getLongitude(), -56.14981412887573);
        assertEquals(li5GSGeodeticInformation.getUncertainty(), 1.0000000000000009);
        assertEquals(liLocEPSInfoGeodeticInfo.getConfidence(), 1);
        assertEquals(li5GSAMFAddress.getData(), "amf3.cluster2.net2.amf.5gc.mnc02.mcc748.3gppnetwork.org".getBytes());
        assertEquals(li5GSTAId.getMCC(), 748);
        assertEquals(li5GSTAId.getMNC(), 1);
        assertEquals(li5GSTAId.getTAC(), 109);
        assertTrue(li5GSCurrentLocationRetrieved);
        assertEquals(li5GSAgeOfLocationInformation.intValue(), 0);
        assertEquals(li5GSVPlmnId.getMcc(), 748);
        assertEquals(li5GSVPlmnId.getMnc(), 2);
        assertEquals(li5GSLocalTimeZone.getData(), new byte[] {0, -6});
        assertEquals(li5GSUsedRATType, UsedRATType.eUtran);
        assertNull(li5GSExtensionContainer);
        assertEquals(li5GSNRTAId.getMCC(), 748);
        assertEquals(li5GSNRTAId.getMNC(), 2);
        assertEquals(li5GSNRTAId.getNrTAC(), 495570);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberInformation" })
    public void testEncode() throws Exception {

        SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        SubscriberInfoImpl subscriberInfo = new SubscriberInfoImpl(null, subscriberState, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        ProvideSubscriberInfoResponseImpl asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
