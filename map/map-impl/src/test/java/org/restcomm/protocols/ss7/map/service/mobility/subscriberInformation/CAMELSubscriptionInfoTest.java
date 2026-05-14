package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CauseValueImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DestinationNumberCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SSCamelDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com">Fernando Mendioroz</a>
 *
 */
public class CAMELSubscriptionInfoTest {

    private byte[] getEncodedData() {
        return new byte[] {
                48, -126, 2, 73, -96, 26, 48, 19, 48, 17, 10, 1, 2, 2, 1, 20, -128, 6, -111, 33, 67, 101, -121, 9, -127,
                1, 0, -128, 1, 5, -126, 0, -95, 45, 48, 43, 10, 1, 4, -96, 19, -128, 1, 1, -95, 9, 4, 7, -111, -108, 113,
                65, -121, 64, 35, -94, 3, 2, 1, 1, -95, 6, -126, 1, 18, -125, 1, 0, -126, 1, 1, -93, 6, 4, 1, 81, 4, 1,
                57, -94, 34, -96, 25, 48, 23, 4, 7, -111, -108, 113, 65, -121, 64, 35, 2, 1, 7, 4, 6, -111, 33, 67, 101,
                -121, 9, 10, 1, 1, -127, 1, 2, -125, 0, -124, 0, -93, 24, 48, 19, 48, 17, 10, 1, 12, 2, 1, 3, -128, 6,
                -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1, 2, -92, 21, 48, 19, 10, 1, 14, -96, 6, -126, 1, 18, -125,
                1, 0, -95, 6, 4, 1, 21, 4, 1, 57, -91, 47, 48, 38, 48, 17, 10, 1, 14, 2, 1, 7, -128, 6, -111, 33, 67, 101,
                -121, 9, -127, 1, 1, 48, 17, 10, 1, 13, 2, 1, 7, -128, 6, -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1,
                2, -127, 0, -126, 0, -90, 21, 48, 19, 10, 1, 14, -96, 6, -126, 1, 18, -125, 1, 0, -95, 6, 4, 1, 21, 4, 1,
                57, -121, 0, -120, 0, -87, 28, -96, 19, 48, 17, -128, 1, 1, -127, 1, 12, -126, 6, -111, 33, 67, 101, -121,
                9, -125, 1, 0, -127, 1, 3, -125, 0, -124, 0, -86, 28, -96, 19, 48, 17, -128, 1, 2, -127, 1, 7, -126, 6,
                -111, 33, 67, 101, -121, 9, -125, 1, 0, -127, 1, 3, -125, 0, -124, 0, -85, 22, 48, 16, 48, 6, 4, 1, 42, 4,
                1, 66, 4, 6, -111, 33, 67, 101, -121, 9, -128, 0, -127, 0, -84, 23, 48, 6, 4, 1, 2, 4, 1, 0, 2, 1, 7, -128,
                6, -111, 33, 67, 101, -121, 9, -126, 0, -125, 0, -114, 3, 2, -112, 0, -81, 28, -96, 19, 48, 17, -128, 1, 2,
                -127, 1, 7, -126, 6, -111, 33, 67, 101, -121, 9, -125, 1, 0, -127, 1, 3, -125, 0, -124, 0, -80, 16, 48, 14,
                10, 1, 2, -96, 9, 10, 1, 0, 10, 1, 1, 10, 1, 2, -79, 23, 48, 6, 4, 1, 2, 4, 1, 0, 2, 1, 7, -128, 6, -111,
                33, 67, 101, -121, 9, -126, 0, -125, 0, -78, 28, 48, 19, 48, 17, 10, 1, 4, 2, 1, 7, -128, 6, -111, 33, 67,
                101, -121, 9, -127, 1, 1, -128, 1, 3, -127, 0, -126, 0, -77, 45, 48, 43, 10, 1, 4, -96, 19, -128, 1, 1, -95,
                9, 4, 7, -111, -108, 113, 65, -121, 64, 35, -94, 3, 2, 1, 1, -95, 6, -126, 1, 18, -125, 1, 0, -126, 1, 1,
                -93, 6, 4, 1, 81, 4, 1, 57, -76, 34, -96, 25, 48, 23, 4, 7, -111, -108, 113, 65, -121, 64, 35, 2, 1, 7, 4,
                6, -111, 33, 67, 101, -121, 9, 10, 1, 1, -127, 1, 3, -125, 0, -124, 0, -75, 24, 48, 19, 48, 17, 10, 1, 12,
                2, 1, 3, -128, 6, -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1, 2, -74, 21, 48, 19, 10, 1, 14, -96, 6,
                -126, 1, 18, -125, 1, 0, -95, 6, 4, 1, 21, 4, 1, 57
        };
    }

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {

        byte[] data = getEncodedData();
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl();
        camelSubscriptionInfo.decodeAll(asn);

        // oCsi
        OCSI ocsi = camelSubscriptionInfo.getOCsi();
        assertNotNull(ocsi);
        assertNull(ocsi.getExtensionContainer());
        assertEquals(ocsi.getCamelCapabilityHandling().intValue(), 5);

        List<OBcsmCamelTDPData> oBcsmCamelTDPDataList = ocsi.getOBcsmCamelTDPDataList();
        assertNotNull(oBcsmCamelTDPDataList);
        assertEquals(oBcsmCamelTDPDataList.size(), 1);
        assertFalse(ocsi.getNotificationToCSE());
        assertTrue(ocsi.getCsiActive());

        OBcsmCamelTDPData oBcsmCamelTDPData = oBcsmCamelTDPDataList.get(0);
        ISDNAddressString gsmSCFAddress = oBcsmCamelTDPData.getGsmSCFAddress();
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 20);
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(oBcsmCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // o-BcsmCamelTDP-CriteriaList
        assertNotNull(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList());
        assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().size(), 1);

        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTdpCriteria.getDestinationNumberCriteria().getMatchType(), MatchType.enabling);
        assertEquals(oBcsmCamelTdpCriteria.getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddressNature(), AddressNature.international_number);
        assertEquals(oBcsmCamelTdpCriteria.getDestinationNumberCriteria().getDestinationNumberList().get(0).getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oBcsmCamelTdpCriteria.getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddress(), "491714780432");
        assertEquals(oBcsmCamelTdpCriteria.getDestinationNumberCriteria().getDestinationNumberLengthList().get(0).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.notForwarded);
        assertEquals(oBcsmCamelTdpCriteria.getOCauseValueCriteria().get(0).getCauseValueCodeValue(), CauseValueCodeValue.InvalidCallReferenceValue);
        assertEquals(oBcsmCamelTdpCriteria.getOCauseValueCriteria().get(1).getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);
        assertNull(oBcsmCamelTdpCriteria.getExtensionContainer());

        // dCsi
        assertNotNull(camelSubscriptionInfo.getDCsi());

        DCSI dcsi = camelSubscriptionInfo.getDCsi();
        assertNotNull(dcsi.getDPAnalysedInfoCriteriaList());
        assertEquals(dcsi.getDPAnalysedInfoCriteriaList().size(), 1);
        assertEquals(dcsi.getCamelCapabilityHandling().intValue(), 2);
        assertNull(dcsi.getExtensionContainer());
        assertTrue(dcsi.getNotificationToCSE());
        assertTrue(dcsi.getCsiActive());

        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dcsi.getDPAnalysedInfoCriteriaList().get(0);
        ISDNAddressString dialedNumber = dpAnalysedInfoCriterium.getDialledNumber();
        gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        assertNull(dpAnalysedInfoCriterium.getExtensionContainer());
        assertEquals(dialedNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialedNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dialedNumber.getAddress(), "491714780432");
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // tCsi
        assertNotNull(camelSubscriptionInfo.getTCsi());

        TCSI tcsi = camelSubscriptionInfo.getTCsi();
        assertNotNull(tcsi.getTBcsmCamelTDPDataList());
        assertEquals(tcsi.getTBcsmCamelTDPDataList().size(), 1);
        assertNull(tcsi.getExtensionContainer());
        assertEquals(tcsi.getCamelCapabilityHandling().intValue(), 2);
        assertFalse(tcsi.getNotificationToCSE());
        assertFalse(tcsi.getCsiActive());

        TBcsmCamelTDPData tBcsmCamelTDPData = tcsi.getTBcsmCamelTDPDataList().get(0);
        gsmSCFAddress = tBcsmCamelTDPData.getGsmSCFAddress();
        assertEquals(tBcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tBcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tBcsmCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // tBcsmCamelTdpCriteriaList
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(0).getCauseValueCodeValue(), CauseValueCodeValue.CallRejected);
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(1).getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);

        // vtCsi
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getExtensionContainer());
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getServiceKey(), 7);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(1).getExtensionContainer());
        assertEquals(camelSubscriptionInfo.getVtCsi().getCamelCapabilityHandling().intValue(), 2);
        assertNull(camelSubscriptionInfo.getVtCsi().getExtensionContainer());
        assertTrue(camelSubscriptionInfo.getVtCsi().getNotificationToCSE());
        assertTrue(camelSubscriptionInfo.getVtCsi().getCsiActive());

        // vtBcsmCamelTdpCriteriaList
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(0).getCauseValueCodeValue(), CauseValueCodeValue.CallRejected);
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(1).getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);

        // tifCsi
        assertTrue(camelSubscriptionInfo.getTifCsi());

        // tifCsiNotificationToCSE
        assertTrue(camelSubscriptionInfo.getTifCsiNotificationToCSE());

        // gprsCsi
        assertNotNull(camelSubscriptionInfo.getGprsCsi());

        GPRSCSI gprscsi = camelSubscriptionInfo.getGprsCsi();
        assertNotNull(gprscsi.getGPRSCamelTDPDataList());
        assertEquals(gprscsi.getGPRSCamelTDPDataList().size(), 1);
        assertEquals(gprscsi.getCamelCapabilityHandling().intValue(), 3);
        assertNull(gprscsi.getExtensionContainer());
        assertTrue(gprscsi.getNotificationToCSE());
        assertTrue(gprscsi.getCsiActive());

        GPRSCamelTDPData gprsCamelTDPData = gprscsi.getGPRSCamelTDPDataList().get(0);
        gsmSCFAddress = gprsCamelTDPData.getGsmSCFAddress();
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attach);
        assertEquals(gprsCamelTDPData.getServiceKey(), 12);
        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.continueTransaction);
        assertNull(gprsCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // moSmsCsi
        assertNotNull(camelSubscriptionInfo.getMoSmsCsi());

        SMSCSI smscsi = camelSubscriptionInfo.getMoSmsCsi();
        assertNotNull(smscsi.getSmsCamelTdpDataList());
        assertEquals(smscsi.getSmsCamelTdpDataList().size(), 1);
        assertEquals(smscsi.getCamelCapabilityHandling().intValue(), 3);
        assertNull(smscsi.getExtensionContainer());
        assertTrue(smscsi.getNotificationToCSE());
        assertTrue(smscsi.getCsiActive());

        SMSCAMELTDPData smsCamelTdpData = smscsi.getSmsCamelTdpDataList().get(0);
        gsmSCFAddress = smsCamelTdpData.getGsmSCFAddress();
        assertEquals(smsCamelTdpData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        assertEquals(smsCamelTdpData.getServiceKey(), 7);
        assertEquals(smsCamelTdpData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCamelTdpData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // ssCsi
        assertNotNull(camelSubscriptionInfo.getSsCsi());

        SSCSI sscsi = camelSubscriptionInfo.getSsCsi();
        assertNotNull(sscsi.getSsCamelData());
        assertNull(sscsi.getExtensionContainer());
        assertTrue(sscsi.getNotificationToCSE());
        assertTrue(sscsi.getCsiActive());

        SSCamelData ssCamelData = sscsi.getSsCamelData();
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertNotNull(ssCamelData.getSsEventList());
        assertEquals(ssCamelData.getSsEventList().size(), 2);
        SSCode ssCode = ssCamelData.getSsEventList().get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.cfnry);
        ssCode = ssCamelData.getSsEventList().get(1);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.hold);
        assertNull(ssCamelData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // mcsi
        assertNotNull(camelSubscriptionInfo.getMCsi());

        MCSI mcsi = camelSubscriptionInfo.getMCsi();
        gsmSCFAddress = mcsi.getGsmSCFAddress();
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");
        assertNotNull(mcsi.getMobilityTriggers());
        assertEquals(mcsi.getMobilityTriggers().size(), 2);
        MMCode mmCode = mcsi.getMobilityTriggers().get(0);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.IMSIAttach);
        mmCode = mcsi.getMobilityTriggers().get(1);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.LocationUpdateInSameVLR);
        assertEquals(mcsi.getServiceKey(), 7);
        assertNull(mcsi.getExtensionContainer());
        assertTrue(mcsi.getNotificationToCSE());
        assertTrue(mcsi.getCsiActive());

        // extensionContainer
        assertNull(camelSubscriptionInfo.getExtensionContainer());

        // specificCSIWithdraw
        assertNotNull(camelSubscriptionInfo.getSpecificCSIDeletedList());
        SpecificCSIWithdraw specificCSIWithdraw = camelSubscriptionInfo.getSpecificCSIDeletedList();
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertFalse(specificCSIWithdraw.getMoSmsCsi());
        assertFalse(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());

        // mtSmsCSI
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getServiceKey(), 7);
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getSmsCamelTdpDataList().get(0).getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertEquals(camelSubscriptionInfo.getMtSmsCsi().getCamelCapabilityHandling().intValue(), 3);
        assertNull(camelSubscriptionInfo.getMtSmsCsi().getExtensionContainer());
        assertTrue(camelSubscriptionInfo.getMtSmsCsi().getNotificationToCSE());
        assertTrue(camelSubscriptionInfo.getMtSmsCsi().getCsiActive());

        // mtSmsCamelTdpCriteriaList
        assertEquals(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList().get(0).getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        assertEquals(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList().get(0).getTPDUTypeCriterion().get(0), MTSMSTPDUType.smsDELIVER);
        assertEquals(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList().get(0).getTPDUTypeCriterion().get(1), MTSMSTPDUType.smsSUBMITREPORT);
        assertEquals(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList().get(0).getTPDUTypeCriterion().get(2), MTSMSTPDUType.smsSTATUSREPORT);

        // mgCsi
        assertEquals(camelSubscriptionInfo.getMgCsi().getMobilityTriggers().get(0).getMMCodeValue(), MMCodeValue.IMSIAttach);
        assertEquals(camelSubscriptionInfo.getMgCsi().getMobilityTriggers().get(1).getMMCodeValue(), MMCodeValue.LocationUpdateInSameVLR);
        assertEquals(camelSubscriptionInfo.getMgCsi().getServiceKey(), 7);
        assertNull(camelSubscriptionInfo.getMgCsi().getExtensionContainer());
        assertEquals(camelSubscriptionInfo.getMgCsi().getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getMgCsi().getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getMgCsi().getGsmSCFAddress().getAddress(), "1234567890");
        assertTrue(camelSubscriptionInfo.getMgCsi().getNotificationToCSE());
        assertTrue(camelSubscriptionInfo.getMgCsi().getCsiActive());

        // oImCsi
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.geToImCsi().getOBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertEquals(camelSubscriptionInfo.geToImCsi().getCamelCapabilityHandling().intValue(), 3);
        assertNull(camelSubscriptionInfo.geToImCsi().getExtensionContainer());
        assertTrue(camelSubscriptionInfo.geToImCsi().getNotificationToCSE());
        assertTrue(camelSubscriptionInfo.geToImCsi().getCsiActive());


        // oImBcsmCamelTdpCriteriaList
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getDestinationNumberCriteria().getMatchType(), MatchType.enabling);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddress(), "491714780432");
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberLengthList().get(0).intValue(), 1);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getCallTypeCriteria(), CallTypeCriteria.notForwarded);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getOCauseValueCriteria().get(0).getCauseValueCodeValue(), CauseValueCodeValue.InvalidCallReferenceValue);
        assertEquals(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0).getOCauseValueCriteria().get(1).getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);

        // dImCsi
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddress(), "491714780432");
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getServiceKey(), 7);
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(camelSubscriptionInfo.getDImCsi().getDPAnalysedInfoCriteriaList().get(0).getExtensionContainer());
        assertEquals(camelSubscriptionInfo.getDImCsi().getCamelCapabilityHandling().intValue(), 3);
        assertNull(camelSubscriptionInfo.getDImCsi().getExtensionContainer());
        assertTrue(camelSubscriptionInfo.getDImCsi().getNotificationToCSE());
        assertTrue(camelSubscriptionInfo.getDImCsi().getCsiActive());

        /*
        TCSI [tBcsmCamelTDPDataList=
        [TBcsmCamelTDPData [tBcsmTriggerDetectionPoint=termAttemptAuthorized,
        serviceKey=3,
        gsmSCFAddress=ISDNAddressString[AddressNature=international_number, NumberingPlan=ISDN, Address=1234567890],
        defaultCallHandling=releaseCall]],
        camelCapabilityHandling=2]>

         */
        // vtImCsi
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getServiceKey(), 3);
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "1234567890");
        assertEquals(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(camelSubscriptionInfo.getVtImCsi().getTBcsmCamelTDPDataList().get(0).getExtensionContainer());
        assertNull(camelSubscriptionInfo.getVtImCsi().getExtensionContainer());
        assertEquals(camelSubscriptionInfo.getVtImCsi().getCamelCapabilityHandling().intValue(), 2);
        assertFalse(camelSubscriptionInfo.getVtImCsi().getNotificationToCSE());
        assertFalse(camelSubscriptionInfo.getVtImCsi().getCsiActive());

        // vtImBcsmCamelTdpCriteriaList
        assertEquals(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0).getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        assertEquals(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(0).getCauseValueCodeValue(), CauseValueCodeValue.CallRejected);
        assertEquals(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(1).getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {

        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1234567890");
        OBcsmCamelTDPData oBcsmCamelTDPData1 = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 20, gsmSCFAddress,
                DefaultCallHandling.continueCall, null);
        OCSIImpl ocsi = new OCSIImpl(new ArrayList<>(){{add(oBcsmCamelTDPData1);}}, null, 5, false, true);
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<>();
        OBcsmTriggerDetectionPoint oBcsmTDP = OBcsmTriggerDetectionPoint.routeSelectFailure;
        MatchType matchType = MatchType.enabling;
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<>();
        ISDNAddressString destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        destinationNumberList.add(destinationNumber);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<>();
        destinationNumberLengthList.add(1);
        DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);

        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue1 = BearerServiceCodeValue.dataCDA_1200bps;
        ExtBearerServiceCode extBearerServiceCode1 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue1);
        ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode1);
        TeleserviceCodeValue teleserviceCodeValue2 = TeleserviceCodeValue.allTeleservices;
        ExtTeleserviceCodeImpl extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        ExtBasicServiceCodeImpl extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        CallTypeCriteria callTypeCriteria = CallTypeCriteria.notForwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<>();
        CauseValue causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
        CauseValue causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        oCauseValueCriteria.add(causeValue1);
        oCauseValueCriteria.add(causeValue2);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
                basicServiceList, callTypeCriteria, oCauseValueCriteria, null);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);
        int camelCapabilityHandling = 2;
        ArrayList<DPAnalysedInfoCriterium>  dpAnalysedInfoCriteriaList = new ArrayList<>();
        ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        long serviceKey = 7L;
        DefaultCallHandling defaultCallHandling = DefaultCallHandling.releaseCall;
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
        boolean notificationToCSE = true;
        boolean csiActive = true;
        DCSI dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);

        TBcsmCamelTDPDataImpl cind = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 3,
                gsmSCFAddress, DefaultCallHandling.releaseCall, null);
        ArrayList<TBcsmCamelTDPData> lst = new ArrayList<>();
        lst.add(cind);
        TCSIImpl tcsi = new TCSIImpl(lst, null, 2, false, false);
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tNoAnswer;
        ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<>();
        CauseValue tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
        CauseValue tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        tCauseValueCriteria.add(tcv1);
        tCauseValueCriteria.add(tcv2);
        TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = new ArrayList<>();
        TBcsmTriggerDetectionPoint tBcsmTDP1 = TBcsmTriggerDetectionPoint.tNoAnswer;
        TBcsmTriggerDetectionPoint tBcsmTDP2 = TBcsmTriggerDetectionPoint.tBusy;
        TBcsmCamelTDPData tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        TBcsmCamelTDPData tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
        TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList = new ArrayList<>();
        vtBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<>();
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attach;
        long sk = 12;
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.continueTransaction;
        GPRSCamelTDPData gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, sk, gsmSCFAddress, defaultSessionHandling, null);
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        camelCapabilityHandling = 3;
        GPRSCSI gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<>();
        SMSTriggerDetectionPoint smsTDP = SMSTriggerDetectionPoint.smsDeliveryRequest;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;
        SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, null);
        smsCamelTdpDataList.add(smscameltdpData);
        SMSCSI moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<SSCode> ssEventList = new ArrayList<>();
        SSCode ssCode1 = new SSCodeImpl(SupplementaryCodeValue.cfnry);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.hold);
        ssEventList.add(ssCode1);
        ssEventList.add(ssCode2);
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, null);
        SSCSI ssCsi = new SSCSIImpl(ssCamelData, null, notificationToCSE, csiActive);
        ArrayList<MMCode> mobilityTriggers = new ArrayList<>();
        MMCode mmCode1 = new MMCodeImpl(MMCodeValue.IMSIAttach);
        MMCode mmCode2 = new MMCodeImpl(MMCodeValue.LocationUpdateInSameVLR);
        mobilityTriggers.add(mmCode1);
        mobilityTriggers.add(mmCode2);
        MCSI mcsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true,
                false, false, false, false, false, false, false, false,
                false, false);
        SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<>();
        ArrayList<MTSMSTPDUType> mtsmstpduTypeArrayList = new ArrayList<>();
        MTSMSTPDUType mtsmstpduType1 = MTSMSTPDUType.smsDELIVER;
        MTSMSTPDUType mtsmstpduType2 = MTSMSTPDUType.smsSUBMITREPORT;
        MTSMSTPDUType mtsmstpduType3 = MTSMSTPDUType.smsSTATUSREPORT;
        mtsmstpduTypeArrayList.add(mtsmstpduType1);
        mtsmstpduTypeArrayList.add(mtsmstpduType2);
        mtsmstpduTypeArrayList.add(mtsmstpduType3);
        MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
        MGCSI mgCsi = new MGCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        ArrayList <OBcsmCamelTDPData> oBcsmCamelTDPDataList = new ArrayList<>();
        OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
        OCSI oImCsi = new OCSIImpl(oBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList = new ArrayList<>();
        oImBcsmCamelTdpCriteriaList.add(oBcsmCamelTdpCriteria);
        DCSI dImCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        tBcsmCamelTDPDataList = new ArrayList<>();
        TBcsmCamelTDPData tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized, 3,
                gsmSCFAddress, DefaultCallHandling.releaseCall, null);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData);
        TCSIImpl vtImCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, 2, false, false);
        tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList = new ArrayList<>();
        vtImBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(ocsi, oBcsmCamelTDPCriteriaList, dCSI, tcsi,
                tBcsmCamelTdpCriteriaList, vtCsi, vtBcsmCamelTdpCriteriaList, true, true,
                gprsCsi, moSmsCsi, ssCsi, mcsi, null, specificCSIWithdraw, mtSmsCSI,
                mtSmsCamelTdpCriteriaList, mgCsi, oImCsi, oImBcsmCamelTdpCriteriaList, dImCsi, vtImCsi, vtImBcsmCamelTdpCriteriaList);

        AsnOutputStream asnOS = new AsnOutputStream();
        camelSubscriptionInfo.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, getEncodedData()));
    }
}
