package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *  @author <a href="mailto:fernando.mendioroz@gmail.com">Fernando Mendioroz</a>
 */
public class VlrCamelSubscriptionInfoTest {

    public byte[] getVlrCamelSubscriptionInfoEncodedData() {
        return new byte[] { 48, -126, 1, 66, -96, 29, 48, 20, 48, 18, 10, 1, 4, 2, 1, 7, -128, 7, -111, -108, 113, 1, 100,
                0, -110, -127, 1, 0, -128, 1, 2, -127, 0, -126, 0, -94, 23, 48, 17, 48, 6, 4, 1, -14, 4, 1, -101, 4, 7, -111,
                -108, 113, 1, 100, 0, -110, -128, 0, -127, 0, -92, 45, 48, 43, 10, 1, 4, -96, 19, -128, 1, 1, -95, 9, 4, 7,
                -111, -108, 113, 65, -121, 64, 35, -94, 3, 2, 1, 1, -95, 6, -126, 1, 24, -125, 1, 96, -126, 1, 1, -93, 6, 4,
                1, 81, 4, 1, 57, -125, 0, -91, 24, 48, 6, 4, 1, 2, 4, 1, 0, 2, 1, 7, -128, 7, -111, -108, 113, 1, 100, 0, -110,
                -126, 0, -125, 0, -90, 29, -96, 20, 48, 18, -128, 1, 2, -127, 1, 7, -126, 7, -111, -108, 113, 1, 100, 0, -110,
                -125, 1, 0, -127, 1, 2, -125, 0, -124, 0, -89, 49, 48, 40, 48, 18, 10, 1, 14, 2, 1, 7, -128, 7, -111, -108, 113,
                1, 100, 0, -110, -127, 1, 0, 48, 18, 10, 1, 13, 2, 1, 7, -128, 7, -111, -108, 113, 1, 100, 0, -110, -127, 1, 0,
                -128, 1, 2, -127, 0, -126, 0, -88, 21, 48, 19, 10, 1, 14, -96, 6, -126, 1, 24, -125, 1, 96, -95, 6, 4, 1, 21, 4,
                1, 57, -87, 35, -96, 26, 48, 24, 4, 7, -111, -108, 113, 65, -121, 64, 35, 2, 1, 7, 4, 7, -111, -108, 113, 1, 100,
                0, -110, 10, 1, 0, -127, 1, 2, -125, 0, -124, 0, -86, 29, -96, 20, 48, 18, -128, 1, 2, -127, 1, 7, -126, 7, -111,
                -108, 113, 1, 100, 0, -110, -125, 1, 0, -127, 1, 2, -125, 0, -124, 0, -85, 16, 48, 14, 10, 1, 2, -96, 9, 10, 1, 0,
                10, 1, 1, 10, 1, 2
        };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getVlrCamelSubscriptionInfoEncodedData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl();
        vlrCamelSubscriptionInfo.decodeAll(asn);

        System.out.println(vlrCamelSubscriptionInfo);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // Wireshark sample
        /*
         * vlrCamelSubscriptionInfo
         *     o-CSI
         *         o-BcsmCamelTDPDataList: 1 item
         *             O-BcsmCamelTDPData
         *                 o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csiActive
         *     ss-CSI
         *         ss-CamelData
         *             ss-EventList: 2 items
         *                 SS-Code: allAdditionalInfoTransferSS - all additional information transfer SS (128)
         *                 SS-Code: uus2 - UUS2 user-to-user signalling (130)
         *             gsmSCF-Address: 91947101640092
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460029
         *         notificationToCSE
         *         csi-Active
         *     o-BcsmCamelTDP-CriteriaList: 1 item
         *         O-BcsmCamelTDP-Criteria
         *             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *             destinationNumberCriteria
         *                 matchType: enabling (1)
         *                 destinationNumberList: 1 item
         *                     ISDN-AddressString: 91947141874023
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491714780432
         *                 destinationNumberLengthList: 1 item
         *                     DestinationNumberLengthList item: 1
         *             basicServiceCriteria: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDS-2400bps (28)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: emergencyCalls (18)
         *             callTypeCriteria: notForwarded (1)
         *             o-CauseValueCriteria: 2 items
         *                 CauseValue: 51
         *                 CauseValue: 39
         *     tif-CSI
         *     m-CSI
         *         mobilityTriggers: 2 items
         *             MM-Code: 02
         *             MM-Code: 00
         *         serviceKey: 7
         *         gsmSCF-Address: 91947101640092
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 491710460029
         *         notificationToCSE
         *         csi-Active
         *     mo-sms-CSI
         *         sms-CAMEL-TDP-DataList: 1 item
         *             SMS-CAMEL-TDP-Data
         *                 sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultSMS-Handling: continueTransaction (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     vt-CSI
         *         t-BcsmCamelTDPDataList: 2 items
         *             T-BcsmCamelTDPData
         *                 t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *             T-BcsmCamelTDPData
         *                 t-BcsmTriggerDetectionPoint: tBusy (13)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *         T-BCSM-CAMEL-TDP-Criteria
         *             t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *             basicServiceCriteria: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDS-2400bps (28)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: emergencyCalls (18)
         *             t-CauseValueCriteria: 2 items
         *                 CauseValue: 15
         *                 CauseValue: 39
         *     d-CSI
         *         dp-AnalysedInfoCriteriaList: 1 item
         *             DP-AnalysedInfoCriterium
         *                 dialledNumber: 91947141874023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491714780432
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     mt-sms-CSI
         *         sms-CAMEL-TDP-DataList: 1 item
         *             SMS-CAMEL-TDP-Data
         *                 sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultSMS-Handling: continueTransaction (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     mt-smsCAMELTDP-CriteriaList: 1 item
         *         MT-smsCAMELTDP-Criteria
         *             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *             tpdu-TypeCriterion: 3 items
         *                 MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                 MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                 MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         */
        // o-CSI
        OCSI oCsi = vlrCamelSubscriptionInfo.getOCsi();
        ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(oBcsmCamelTDPDataList.size(), 1);
        OBcsmCamelTDPData oBcsmCamelTDPData = oBcsmCamelTDPDataList.get(0);
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(oBcsmCamelTDPData.getExtensionContainer());
        assertNull(oCsi.getExtensionContainer());
        assertEquals(oCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(oCsi.getNotificationToCSE());
        assertTrue(oCsi.getCsiActive());
        // extensionContainer
        assertNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        // ssCsi
        SSCSI ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        SSCamelData ssCamelData = ssCsi.getSsCamelData();
        ArrayList<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 2);
        SSCode ssEvent1 = ssEventList.get(0);
        assertNotNull(ssEvent1);
        assertEquals(ssEvent1.getSupplementaryCodeValue(), SupplementaryCodeValue.plmn_specificSS_2);
        SSCode ssEvent2 = ssEventList.get(1);
        assertNotNull(ssEvent2);
        assertEquals(ssEvent2.getSupplementaryCodeValue(), SupplementaryCodeValue.bicRoam);
        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertEquals(gsmSCFAddress.getAddress(), "491710460029");
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(ssCamelData.getExtensionContainer());
        assertNull(ssCsi.getExtensionContainer());
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());
        // o-BcsmCamelTDP-CriteriaList
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);
        DestinationNumberCriteria destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        ArrayList<ISDNAddressString> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 1);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertEquals(destinationNumberOne.getAddress(), "491714780432");
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        ArrayList<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.notForwarded);
        ArrayList<CauseValue> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 2);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 0x51);
        assertNotNull(oCauseValueCriteria.get(1));
        assertEquals(oCauseValueCriteria.get(1).getData(), 0x39);
        // tif-CSI
        assertTrue(vlrCamelSubscriptionInfo.getTifCsi());
        // m-CSI
        MCSI mCsi = vlrCamelSubscriptionInfo.getMCsi();
        ArrayList<MMCode> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.IMSIAttach);
        MMCode mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(mmCode2.getMMCodeValue(), MMCodeValue.LocationUpdateInSameVLR);
        assertNotNull(mCsi);
        assertEquals(mCsi.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertEquals(gsmSCFAddressTwo.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(mCsi.getExtensionContainer());
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());
        // mo-sms-CSI
        SMSCSI smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 7);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        ISDNAddressString gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPData.getExtensionContainer());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        // vt-CSI
        TCSI vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(tBcsmCamelTDPDataList.size(), 2);
        TBcsmCamelTDPData tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(1);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        // t-BCSM-CAMEL-TDP-CriteriaList
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        ArrayList<ExtBasicServiceCode> basicServiceList = tbcsmCamelTdpCriteria.getBasicServiceCriteria();
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        ArrayList<CauseValue> tCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(tCauseValueCriteriaLst);
        assertEquals(tCauseValueCriteriaLst.size(), 2);
        assertEquals(tCauseValueCriteriaLst.get(0).getData(), 0x15);
        assertEquals(tCauseValueCriteriaLst.get(1).getData(), 0x39);
        // d-CSI
        DCSI dCsi = vlrCamelSubscriptionInfo.getDCsi();
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertEquals(dialledNumber.getAddress(), "491714780432");
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(gsmSCFAddressDp.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(dCsi.getExtensionContainer());
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());
        // mt-sms-CSI
        SMSCSI mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPData smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 7);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        ISDNAddressString gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertNull(mtSmsCSI.getExtensionContainer());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        // mt-smsCAMELTDP-CriteriaList
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 3);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);
        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSUBMITREPORT);
        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(2);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSTATUSREPORT);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        // Wireshark sample
        /*
         * vlrCamelSubscriptionInfo
         *     o-CSI
         *         o-BcsmCamelTDPDataList: 1 item
         *             O-BcsmCamelTDPData
         *                 o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csiActive
         *     ss-CSI
         *         ss-CamelData
         *             ss-EventList: 2 items
         *                 SS-Code: allAdditionalInfoTransferSS - all additional information transfer SS (128)
         *                 SS-Code: uus2 - UUS2 user-to-user signalling (130)
         *             gsmSCF-Address: 91947101640092
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710460029
         *         notificationToCSE
         *         csi-Active
         *     o-BcsmCamelTDP-CriteriaList: 1 item
         *         O-BcsmCamelTDP-Criteria
         *             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *             destinationNumberCriteria
         *                 matchType: enabling (1)
         *                 destinationNumberList: 1 item
         *                     ISDN-AddressString: 91947141874023
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491714780432
         *                 destinationNumberLengthList: 1 item
         *                     DestinationNumberLengthList item: 1
         *             basicServiceCriteria: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDS-2400bps (28)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: emergencyCalls (18)
         *             callTypeCriteria: notForwarded (1)
         *             o-CauseValueCriteria: 2 items
         *                 CauseValue: 51
         *                 CauseValue: 39
         *     tif-CSI
         *     m-CSI
         *         mobilityTriggers: 2 items
         *             MM-Code: 02
         *             MM-Code: 00
         *         serviceKey: 7
         *         gsmSCF-Address: 91947101640092
         *             1... .... = Extension: No Extension
         *             .001 .... = Nature of number: International Number (0x1)
         *             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *             E.164 number (MSISDN): 491710460029
         *         notificationToCSE
         *         csi-Active
         *     mo-sms-CSI
         *         sms-CAMEL-TDP-DataList: 1 item
         *             SMS-CAMEL-TDP-Data
         *                 sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultSMS-Handling: continueTransaction (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     vt-CSI
         *         t-BcsmCamelTDPDataList: 2 items
         *             T-BcsmCamelTDPData
         *                 t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *             T-BcsmCamelTDPData
         *                 t-BcsmTriggerDetectionPoint: tBusy (13)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *         T-BCSM-CAMEL-TDP-Criteria
         *             t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *             basicServiceCriteria: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDS-2400bps (28)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: emergencyCalls (18)
         *             t-CauseValueCriteria: 2 items
         *                 CauseValue: 15
         *                 CauseValue: 39
         *     d-CSI
         *         dp-AnalysedInfoCriteriaList: 1 item
         *             DP-AnalysedInfoCriterium
         *                 dialledNumber: 91947141874023
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491714780432
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultCallHandling: continueCall (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     mt-sms-CSI
         *         sms-CAMEL-TDP-DataList: 1 item
         *             SMS-CAMEL-TDP-Data
         *                 sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                 serviceKey: 7
         *                 gsmSCF-Address: 91947101640092
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710460029
         *                 defaultSMS-Handling: continueTransaction (0)
         *         camelCapabilityHandling: 2
         *         notificationToCSE
         *         csi-Active
         *     mt-smsCAMELTDP-CriteriaList: 1 item
         *         MT-smsCAMELTDP-Criteria
         *             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *             tpdu-TypeCriterion: 3 items
         *                 MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                 MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                 MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         */
        OBcsmTriggerDetectionPoint oBcsmTDP = OBcsmTriggerDetectionPoint.routeSelectFailure;
        long serviceKey = 7L;
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
        DefaultCallHandling defaultCallHandling = DefaultCallHandling.continueCall;
        OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList = new ArrayList<>();
        oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
        Integer camelCapabilityHandling = 2;
        boolean notificationToCSE = true;
        boolean csiActive = true;
        OCSI oCSI = new OCSIImpl(oBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        ArrayList<SSCode> ssEventList = new ArrayList<>();
        SSCode ssCode1 = new SSCodeImpl(SupplementaryCodeValue.plmn_specificSS_2);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.bicRoam);
        ssEventList.add(ssCode1);
        ssEventList.add(ssCode2);
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, null);
        SSCSI ssCsi = new SSCSIImpl(ssCamelData, null, notificationToCSE, csiActive);
        MatchType matchType = MatchType.enabling;
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<>();
        ISDNAddressString destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        destinationNumberList.add(destinationNumber);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<>();
        destinationNumberLengthList.add(1);
        DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue3 = BearerServiceCodeValue.allDataCDS_Services;
        ExtBearerServiceCode extBearerServiceCode3 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue3);
        ExtBasicServiceCode extBasicServiceCode3 = new ExtBasicServiceCodeImpl(extBearerServiceCode3);
        TeleserviceCodeValue teleserviceCodeValue2 = TeleserviceCodeValue.allFacsimileTransmissionServices;
        ExtTeleserviceCode extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        ExtBasicServiceCode extBasicServiceCode4 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode3);
        basicServiceList.add(extBasicServiceCode4);
        CallTypeCriteria callTypeCriteria = CallTypeCriteria.notForwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<>();
        CauseValue causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
        CauseValue causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        oCauseValueCriteria.add(causeValue1);
        oCauseValueCriteria.add(causeValue2);
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<>();
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
                basicServiceList, callTypeCriteria, oCauseValueCriteria, null);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);
        boolean tifCsi = true;
        ArrayList<MMCode> mobilityTriggers = new ArrayList<>();
        MMCode mmCode1 = new MMCodeImpl(MMCodeValue.IMSIAttach);
        MMCode mmCode2 = new MMCodeImpl(MMCodeValue.LocationUpdateInSameVLR);
        mobilityTriggers.add(mmCode1);
        mobilityTriggers.add(mmCode2);
        MCSI mcsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<>();
        SMSTriggerDetectionPoint smsTDP = SMSTriggerDetectionPoint.smsDeliveryRequest;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;
        SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, null);
        smsCamelTdpDataList.add(smscameltdpData);
        SMSCSI smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = new ArrayList<>();
        TBcsmTriggerDetectionPoint tBcsmTDP1 = TBcsmTriggerDetectionPoint.tNoAnswer;
        TBcsmTriggerDetectionPoint tBcsmTDP2 = TBcsmTriggerDetectionPoint.tBusy;
        TBcsmCamelTDPData tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        TBcsmCamelTDPData tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
        TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tNoAnswer;
        ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<>();
        CauseValue tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
        CauseValue tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        tCauseValueCriteria.add(tcv1);
        tCauseValueCriteria.add(tcv2);
        TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<>();
        ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
        DCSI dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
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
        VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCSI, null,
                ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mcsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCSI, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);

        AsnOutputStream asnOS = new AsnOutputStream();
        vlrCamelSubscriptionInfo.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getVlrCamelSubscriptionInfoEncodedData();

        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
