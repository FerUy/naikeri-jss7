package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtForwardingInfoForCSE;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtSSInfoForCSE;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CauseValueImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DestinationNumberCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwOptionsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class AnyTimeModificationResponseTest {

  private byte[] getEncodedDataWithForwardingInfoChoice() {
    return new byte[] { 0x30, (byte) 0x82,
        0x01, 0x71, (byte) 0xa0, 0x55,
        (byte) 0xa0, 0x53, (byte) 0x80, 0x01, (byte) 0xc0, (byte) 0xa1, 0x4c, 0x30,
        0x24, (byte) 0x82, 0x01, 0x00, (byte) 0x84, 0x01, 0x0a, (byte) 0x85,
        0x09, (byte) 0x91, (byte) 0x88, 0x22, 0x58, 0x01, 0x65, 0x28,
        0x54, (byte)0xf1, (byte) 0x88, 0x02, 0x02, 0x05, (byte) 0x86, 0x01,
        (byte) 0x88, (byte) 0x87, 0x01, 0x06, (byte) 0x8a, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, 0x04, 0x30, 0x23, 0x30, 0x24, (byte) 0x83,
        0x01, 0x00, (byte) 0x84, 0x01, 0x07, (byte) 0x85, 0x09, (byte) 0x91,
        (byte) 0x88, 0x22, 0x58, 0x01, 0x65, 0x28, 0x54, (byte) 0xf7,
        (byte) 0x88, 0x02, 0x02, 0x04, (byte) 0x86, 0x01, (byte) 0x84, (byte) 0x87,
        0x01, 0x0a, (byte) 0x8a, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
        0x04, 0x30, 0x53, (byte) 0x82, 0x00, (byte) 0xa1, (byte) 0x81, (byte) 0xc1,
        (byte) 0xa0, 0x1d, 0x30, 0x14, 0x30, 0x12, 0x0a, 0x01,
        0x04, 0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00,
        (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x00, (byte) 0x82, 0x00, (byte) 0xa1,
        0x2d, 0x30, 0x2b, 0x0a, 0x01, 0x04, (byte) 0xa0, 0x13,
        (byte) 0x80, 0x01, 0x01, (byte) 0xa1, 0x09, 0x04, 0x07, (byte) 0x91,
        (byte) 0x94, 0x71, 0x41, (byte) 0x87, 0x40, 0x23, (byte) 0xa2, 0x03,
        0x02, 0x01, 0x01, (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x00,
        (byte) 0x83, 0x01, 0x00, (byte) 0x82, 0x01, 0x01, (byte) 0xa3, 0x06,
        0x04, 0x01, 0x51, 0x04, 0x01, 0x39, (byte) 0xa2, 0x23,
        (byte) 0xa0, 0x1a, 0x30, 0x18, 0x04, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x41, (byte) 0x87, 0x40, 0x23, 0x02, 0x01, 0x07,
        0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
        (byte) 0x92, 0x0a, 0x01, 0x00, (byte) 0x81, 0x01, 0x02, (byte) 0x83,
        0x00, (byte) 0x84, 0x00, (byte) 0xa4, 0x15, 0x30, 0x13, 0x0a,
        0x01, 0x0e, (byte) 0xa0, 0x06, (byte) 0x82, 0x01, 0x00, (byte) 0x83,
        0x01, 0x00, (byte) 0xa1, 0x06, 0x04, 0x01, 0x15, 0x04,
        0x01, 0x39, (byte) 0xa5, 0x31, 0x30, 0x28, 0x30, 0x12,
        0x0a, 0x01, 0x0e, 0x02, 0x01, 0x07, (byte) 0x80, 0x07,
        (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81,
        0x01, 0x00, 0x30, 0x12, 0x0a, 0x01, 0x0d, 0x02,
        0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
        0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00, (byte) 0x80, 0x01,
        0x02, (byte) 0x81, 0x00, (byte) 0x82, 0x00, (byte) 0x87, 0x00, (byte) 0x88,
        0x00, (byte) 0xa3, 0x0f, 0x30, 0x0b, 0x03, 0x05, 0x03,
        0x1c, 0x06, 0x48, (byte) 0xf8, 0x03, 0x02, 0x04, (byte) 0x80,
        0x05, 0x00, (byte) 0xa4, 0x18, (byte) 0xa1, 0x14, 0x30, 0x08,
        (byte) 0xa1, 0x03, (byte) 0x82, 0x01, 0x00, (byte) 0x82, 0x01, 0x0a,
        0x30, 0x08, (byte) 0xa1, 0x03, (byte) 0x83, 0x01, 0x00, (byte) 0x82,
        0x01, 0x07, (byte) 0x82, 0x00, (byte) 0xa5, 0x05, (byte) 0x81, 0x01,
        0x0a, (byte) 0x82, 0x00, (byte) 0xa6, 0x08, (byte) 0x81, 0x01, 0x07,
        (byte) 0x82, 0x01, 0x00, (byte) 0x83, 0x00, (byte) 0xa7, 0x08, (byte) 0x81,
        0x01, 0x0a, (byte) 0x82, 0x01, 0x02, (byte) 0x83, 0x00, (byte) 0xa8,
        0x05, (byte) 0x81, 0x01, 0x0a, (byte) 0x82, 0x00, (byte) 0x89, 0x07,
        (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x04, 0x00, 0x12
    };
  }

  private byte[] getEncodedDataWithCallBarringInfoForCSE() {
    return new byte[] { 0x30, (byte) 0x82,
        0x01, 0x3e, (byte) 0xa0, 0x22,
        (byte) 0xa1, 0x20, (byte) 0x80, 0x01, 0x10, (byte) 0xa1, 0x10, 0x30,
        0x06, (byte) 0x82, 0x01, 0x2f, (byte) 0x84, 0x01, 0x0a, 0x30,
        0x06, (byte) 0x83, 0x01, 0x00, (byte) 0x84, 0x01, 0x07, (byte) 0x82,
        0x04, 0x33, 0x30, 0x32, 0x39, (byte) 0x83, 0x01, 0x03,
        (byte) 0x84, 0x00, (byte) 0xa1, (byte) 0x81, (byte) 0xc1, (byte) 0xa0, 0x1d, 0x30,
        0x14, 0x30, 0x12, 0x0a, 0x01, 0x04, 0x02, 0x01,
        0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64,
        0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00, (byte) 0x80, 0x01, 0x02,
        (byte) 0x81, 0x00, (byte) 0x82, 0x00, (byte) 0xa1, 0x2d, 0x30, 0x2b,
        0x0a, 0x01, 0x04, (byte) 0xa0, 0x13, (byte) 0x80, 0x01, 0x01,
        (byte) 0xa1, 0x09, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x41,
        (byte) 0x87, 0x40, 0x23, (byte) 0xa2, 0x03, 0x02, 0x01, 0x01,
        (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x2f, (byte) 0x83, 0x01, 0x00,
        (byte) 0x82, 0x01, 0x01, (byte) 0xa3, 0x06, 0x04, 0x01, 0x51,
        0x04, 0x01, 0x39, (byte) 0xa2, 0x23, (byte) 0xa0, 0x1a, 0x30,
        0x18, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x41, (byte) 0x87,
        0x40, 0x23, 0x02, 0x01, 0x07, 0x04, 0x07, (byte) 0x91,
        (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, 0x0a, 0x01,
        0x00, (byte) 0x81, 0x01, 0x02, (byte) 0x83, 0x00, (byte) 0x84, 0x00,
        (byte) 0xa4, 0x15, 0x30, 0x13, 0x0a, 0x01, 0x0e, (byte) 0xa0,
        0x06, (byte) 0x82, 0x01, 0x2f, (byte) 0x83, 0x01, 0x00, (byte) 0xa1,
        0x06, 0x04, 0x01, 0x15, 0x04, 0x01, 0x39, (byte) 0xa5,
        0x31, 0x30, 0x28, 0x30, 0x12, 0x0a, 0x01, 0x0e,
        0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
        0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00, 0x30,
        0x12, 0x0a, 0x01, 0x0d, 0x02, 0x01, 0x07, (byte) 0x80,
        0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92,
        (byte) 0x81, 0x01, 0x00, (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x00,
        (byte) 0x82, 0x00, (byte) 0x87, 0x00, (byte) 0x88, 0x00, (byte) 0xa3, 0x0f,
        0x30, 0x0b, 0x03, 0x05, 0x03, 0x1c, 0x06, 0x48,
        (byte) 0xf8, 0x03, 0x02, 0x04, (byte) 0x80, 0x05, 0x00, (byte) 0xa4,
        0x18, (byte) 0xa1, 0x14, 0x30, 0x08, (byte) 0xa1, 0x03, (byte) 0x82,
        0x01, 0x2f, (byte) 0x82, 0x01, 0x0a, 0x30, 0x08, (byte) 0xa1,
        0x03, (byte) 0x83, 0x01, 0x00, (byte) 0x82, 0x01, 0x07, (byte) 0x82,
        0x00, (byte) 0xa5, 0x05, (byte) 0x81, 0x01, 0x0a, (byte) 0x82, 0x00,
        (byte) 0xa6, 0x08, (byte) 0x81, 0x01, 0x07, (byte) 0x82, 0x01, 0x00,
        (byte) 0x83, 0x00, (byte) 0xa7, 0x08, (byte) 0x81, 0x01, 0x0a, (byte) 0x82,
        0x01, 0x02, (byte) 0x83, 0x00, (byte) 0xa8, 0x05, (byte) 0x81, 0x01,
        0x0a, (byte) 0x82, 0x00, (byte) 0x89, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
        0x01, 0x04, 0x00, 0x12
    };
  }

  @Test(groups = { "functional.decode", "subscriberInformation" })
  public void testDecode() throws Exception {

    /** Test 1 with ss-InfoFor-CSE: forwardingInfoFor-CSE (0) **/
    byte[] rawData1 = getEncodedDataWithForwardingInfoChoice();
    AsnInputStream asn = new AsnInputStream(rawData1);

    int tag = asn.readTag();
    AnyTimeModificationResponseImpl atmResp1 = new AnyTimeModificationResponseImpl();
    atmResp1.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    ExtSSInfoForCSE ssInfoForCSE = atmResp1.getSsInfoForCSE();
    CAMELSubscriptionInfo camelSubscriptionInfo = atmResp1.getCamelSubscriptionInfo();
    MAPExtensionContainer extensionContainer = atmResp1.getExtensionContainer();
    ODBInfo odbInfo = atmResp1.getOdbInfo();
    CallWaitingData callWaitingData = atmResp1.getCwData();
    CallHoldData callHoldData = atmResp1.getChData();
    ClipData clipData = atmResp1.getClipData();
    ClirData clirData = atmResp1.getClirData();
    EctData ectData = atmResp1.getEctData();
    AddressString serviceCentreAddress = atmResp1.getServiceCentreAddress();

    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    // returnResultLast
    //    invokeID: 0
    //    resultretres
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    //            forwardingInfoFor-CSE
    //                ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
    //                forwardingFeatureList: 2 items
    //                    Ext-ForwFeature
    //                        basicService: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        ss-Status: 0a
    //                        0000 .... = Unused: 0x0
    //                        .... .0.. = P bit: Not provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...0 = A bit: not Active
    //                        forwardedToNumber: 9188225801652854f1
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 882285105682451
    //                        forwardedToSubaddress: 0205
    //                        forwardingOptions: 88
    //                        1... .... = Notification to forwarding party: Notification
    //                        .0.. .... = Redirecting presentation: No presentation
    //                        ..0. .... = Notification to calling party: No notification
    //                        .... 10.. = Forwarding reason: no reply (0x2)
    //                        noReplyConditionTime: 6
    //                        longForwardedToNumber: 91947101043023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710400332
    //                    Ext-ForwFeature
    //                        basicService: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                        ss-Status: 07
    //                        0000 .... = Unused: 0x0
    //                        .... 0... = Q bit: Operative
    //                        .... .1.. = P bit: Provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...1 = A bit: Active
    //                        forwardedToNumber: 9188225801652854f7
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 882285105682457
    //                        forwardedToSubaddress: 0204
    //                        forwardingOptions: 84
    //                        1... .... = Notification to forwarding party: Notification
    //                        .0.. .... = Redirecting presentation: No presentation
    //                        ..0. .... = Notification to calling party: No notification
    //                        .... 01.. = Forwarding reason: ms busy (0x1)
    //                        noReplyConditionTime: 10
    //                        longForwardedToNumber: 91947101043053
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710400335
    //                notificationToCSE
    //        camel-SubscriptionInfo
    //            o-CSI
    //                o-BcsmCamelTDPDataList: 1 item
    //                    O-BcsmCamelTDPData
    //                        o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csiActive
    //            o-BcsmCamelTDP-CriteriaList: 1 item
    //                O-BcsmCamelTDP-Criteria
    //                    o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                    destinationNumberCriteria
    //                        matchType: enabling (1)
    //                        destinationNumberList: 1 item
    //                            ISDN-AddressString: 91947141874023
    //                                1... .... = Extension: No Extension
    //                                .001 .... = Nature of number: International Number (0x1)
    //                                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                                E.164 number (MSISDN): 491714780432
    //                        destinationNumberLengthList: 1 item
    //                            DestinationNumberLengthList item: 1
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    callTypeCriteria: notForwarded (1)
    //                    o-CauseValueCriteria: 2 items
    //                        CauseValue: 51
    //                        CauseValue: 39
    //            d-CSI
    //                dp-AnalysedInfoCriteriaList: 1 item
    //                    DP-AnalysedInfoCriterium
    //                        dialledNumber: 91947141874023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491714780432
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            t-BCSM-CAMEL-TDP-CriteriaList: 1 item
    //                T-BCSM-CAMEL-TDP-Criteria
    //                    t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    t-CauseValueCriteria: 2 items
    //                        CauseValue: 15
    //                        CauseValue: 39
    //            vt-CSI
    //                t-BcsmCamelTDPDataList: 2 items
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tNoAnswer (14)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tBusy (13)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            tif-CSI
    //            tif-CSI-NotificationToCSE
    //        odb-Info
    //            odb-Data
    //                Padding: 3
    //                odb-GeneralData: 1c0648f8
    //                    0... .... = allOG-CallsBarred: False
    //                    .0.. .... = internationalOGCallsBarred: False
    //                    ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                    ...1 .... = premiumRateInformationOGCallsBarred: True
    //                    .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                    .... .1.. = ss-AccessBarred: True
    //                    .... ..0. = interzonalOGCallsBarred: False
    //                    .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                    0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
    //                    .0.. .... = allECT-Barred: False
    //                    ..0. .... = chargeableECT-Barred: False
    //                    ...0 .... = internationalECT-Barred: False
    //                    .... 0... = interzonalECT-Barred: False
    //                    .... .1.. = doublyChargeableECT-Barred: True
    //                    .... ..1. = multipleECT-Barred: True
    //                    .... ...0 = allPacketOrientedServicesBarred: False
    //                    0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                    .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                    ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                    ...0 .... = allIC-CallsBarred: False
    //                    .... 1... = roamingOutsidePLMNIC-CallsBarred: True
    //                    .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
    //                    .... ..0. = roamingOutsidePLMN-Barred: False
    //                    .... ...0 = roamingOutsidePLMN-CountryBarred: False
    //                    1... .... = registrationAllCF-Barred: True
    //                    .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                    ..1. .... = registrationInterzonalCF-Barred: True
    //                    ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                    .... 1... = registrationInternationalCF-Barred: True
    //                Padding: 4
    //                odb-HPLMN-Data: 80
    //                    1... .... = plmn-SpecificBarringType1: True
    //                    .0.. .... = plmn-SpecificBarringType2: False
    //                    ..0. .... = plmn-SpecificBarringType3: False
    //                    ...0 .... = plmn-SpecificBarringType4: False
    //            notificationToCSE
    //        cw-Data
    //            cwFeatureList: 2 items
    //                Ext-CwFeature
    //                    basicService: ext-BearerService (2)
    //                        ext-BearerService: allBearerServices (0)
    //                    ss-Status: 0a
    //                    0000 .... = Unused: 0x0
    //                    .... .0.. = P bit: Not provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...0 = A bit: not Active
    //                Ext-CwFeature
    //                    basicService: ext-Teleservice (3)
    //                        ext-Teleservice: allTeleservices (0)
    //                    ss-Status: 07
    //                    0000 .... = Unused: 0x0
    //                    .... 0... = Q bit: Operative
    //                    .... .1.. = P bit: Provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...1 = A bit: Active
    //            notificationToCSE
    //        ch-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        clip-Data
    //            ss-Status: 07
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...1 = A bit: Active
    //            overrideCategory: overrideEnabled (0)
    //            notificationToCSE
    //        clir-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            cliRestrictionOption: temporaryDefaultAllowed (2)
    //            notificationToCSE
    //        ect-data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        serviceCentreAddress: 91947101040012
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710400021
    assertNotNull(ssInfoForCSE.getForwardingInfoForCSE());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allMOLR_SS);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.allBearerServices);
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getSsStatus().getBitR());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getSsStatus().getBitA());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardedToNumber().isExtension());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardedToNumber().getAddress(), "882285105682451");
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardedToSubaddress().getData(), new byte[] {0x02, 0x05});
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardingOptions().getNotificationToForwardingParty());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardingOptions().getRedirectingPresentation());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardingOptions().getNotificationToCallingParty());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getForwardingOptions().getExtForwOptionsForwardingReason(),
        ExtForwOptionsForwardingReason.noReply);
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getLongForwardedToNumber().isExtension());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getLongForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getLongForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(0).getLongForwardedToNumber().getAddress(), "491710400332");
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getSsStatus().getBitA());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardedToNumber().isExtension());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardedToNumber().getAddress(), "882285105682457");
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardedToSubaddress().getData(), new byte[] {0x02, 0x04});
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardingOptions().getNotificationToForwardingParty());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardingOptions().getRedirectingPresentation());
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardingOptions().getNotificationToCallingParty());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getForwardingOptions().getExtForwOptionsForwardingReason(),
        ExtForwOptionsForwardingReason.msBusy);
    assertFalse(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getLongForwardedToNumber().isExtension());
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getLongForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getLongForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(ssInfoForCSE.getForwardingInfoForCSE().getForwardingFeatureList().get(1).getLongForwardedToNumber().getAddress(), "491710400335");
    assertTrue(ssInfoForCSE.getForwardingInfoForCSE().getNotificationToCSE());
    Assert.assertNull(ssInfoForCSE.getForwardingInfoForCSE().getExtensionContainer());
    assertNull(ssInfoForCSE.getCallBarringInfoForCSE());
    assertNotNull(camelSubscriptionInfo.getOCsi());
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.continueCall);
    assertNull(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getExtensionContainer());
    assertEquals(camelSubscriptionInfo.getOCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getOCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getOCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getOCsi().getExtensionContainer());
    assertNotNull(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList());
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getMatchType(), MatchType.enabling);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddressNature(),
        AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getNumberingPlan(),
        NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddress(),
        "491714780432");
    assertFalse((camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).isExtension()));
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().size(), 1);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.allBearerServices);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getCallTypeCriteria(), CallTypeCriteria.notForwarded);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOCauseValueCriteria().get(0).getCauseValueCodeValue(),
        CauseValueCodeValue.InvalidCallReferenceValue);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOCauseValueCriteria().get(1).getCauseValueCodeValue(),
        CauseValueCodeValue.BearerCapabilityNotAuthorized);
    assertNotNull(camelSubscriptionInfo.getDCsi());
    assertFalse(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().isExtension());
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddressNature(),
        AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getNumberingPlan(),
        NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddress(),
        "491714780432");
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDefaultCallHandling().getCode(), 0);
    assertEquals(camelSubscriptionInfo.getDCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getDCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getDCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getDCsi().getExtensionContainer());
    assertNotNull(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList());
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTBcsmTriggerDetectionPoint(),
        TBcsmTriggerDetectionPoint.tNoAnswer);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().
            getBearerServiceCodeValue(), BearerServiceCodeValue.allBearerServices);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().
        getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(0).
            getCauseValueCodeValue(), CauseValueCodeValue.CallRejected);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(1).
        getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);
    assertNotNull(camelSubscriptionInfo.getVtCsi());
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(),
        TBcsmTriggerDetectionPoint.tNoAnswer);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.continueCall);
    assertEquals(camelSubscriptionInfo.getVtCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getVtCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getVtCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getVtCsi().getExtensionContainer());
    assertTrue(camelSubscriptionInfo.getTifCsi());
    assertTrue(camelSubscriptionInfo.getTifCsiNotificationToCSE());
    assertNull(extensionContainer);
    assertNotNull(odbInfo.getOdbData());
    assertNotNull(odbInfo.getOdbData().getODBGeneralData());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalOGCallsNotToHPLMNCountryBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsNotToHPLMNCountryBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getPremiumRateInformationOGCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getPremiumRateEntertainmentOGCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getSsAccessBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getChargeableECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalECTBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getDoublyChargeableECTBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getMultipleECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllPacketOrientedServicesBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamerAccessToHPLMNAPBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRoamerAccessToVPLMNAPBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllICCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICountryICCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNCountryBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationAllCFBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationCFNotToHPLMNBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInterzonalCFBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInterzonalCFNotToHPLMNBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInternationalCFBarred());
    assertTrue(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType1());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType2());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType3());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType4());
    assertNull(odbInfo.getOdbData().getExtensionContainer());
    assertTrue(odbInfo.getNotificationToCSE());
    assertNotNull(callWaitingData);
    assertEquals(callWaitingData.getCwFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.allBearerServices);
    assertFalse(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitQ());
    assertTrue(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitR());
    assertEquals(callWaitingData.getCwFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitA());
    assertTrue(callWaitingData.getNotificationToCSE());
    assertNotNull(callHoldData);
    assertFalse(callHoldData.getSsStatus().getBitP());
    assertTrue(callHoldData.getSsStatus().getBitR());
    assertFalse(callHoldData.getSsStatus().getBitA());
    assertTrue(callHoldData.getNotificationToCSE());
    assertNotNull(clipData);
    assertFalse(clipData.getSsStatus().getBitQ());
    assertTrue(clipData.getSsStatus().getBitP());
    assertTrue(clipData.getSsStatus().getBitR());
    assertTrue(clipData.getSsStatus().getBitA());
    assertEquals(clipData.getOverrideCategory(), OverrideCategory.overrideEnabled);
    assertTrue(clipData.getNotificationToCSE());
    assertNotNull(clirData);
    assertFalse(clirData.getSsStatus().getBitP());
    assertTrue(clirData.getSsStatus().getBitR());
    assertFalse(clirData.getSsStatus().getBitA());
    assertEquals(clirData.getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultAllowed);
    assertTrue(clirData.getNotificationToCSE());
    assertNotNull(ectData);
    assertFalse(ectData.getSsStatus().getBitP());
    assertTrue(ectData.getSsStatus().getBitR());
    assertFalse(ectData.getSsStatus().getBitA());
    assertTrue(ectData.getNotificationToCSE());
    assertNotNull(serviceCentreAddress);
    assertFalse(serviceCentreAddress.isExtension());
    assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(serviceCentreAddress.getAddress(), "491710400021");

    /** Test 2 with ss-InfoFor-CSE: callBarringInfoFor-CSE (1) **/
    byte[] rawData2 = getEncodedDataWithCallBarringInfoForCSE();
    AsnInputStream asn2 = new AsnInputStream(rawData2);

    tag = asn2.readTag();
    AnyTimeModificationResponseImpl atmResp2 = new AnyTimeModificationResponseImpl();
    atmResp2.decodeAll(asn2);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    ssInfoForCSE = atmResp2.getSsInfoForCSE();
    camelSubscriptionInfo = atmResp2.getCamelSubscriptionInfo();
    extensionContainer = atmResp2.getExtensionContainer();
    odbInfo = atmResp2.getOdbInfo();
    callWaitingData = atmResp2.getCwData();
    callHoldData = atmResp2.getChData();
    clipData = atmResp2.getClipData();
    clirData = atmResp2.getClirData();
    ectData = atmResp2.getEctData();
    serviceCentreAddress = atmResp2.getServiceCentreAddress();

    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    // returnResultLast
    //    invokeID: 0
    //    resultretres
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    //            callBarringInfoFor-CSE
    //                ss-Code: allLineIdentificationSS - all line identification SS (16)
    //                callBarringFeatureList: 2 items
    //                    Ext-CallBarringFeature
    //                        basicService: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        ss-Status: 0a
    //                        0000 .... = Unused: 0x0
    //                        .... .0.. = P bit: Not provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...0 = A bit: not Active
    //                    Ext-CallBarringFeature
    //                        basicService: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                        ss-Status: 07
    //                        0000 .... = Unused: 0x0
    //                        .... 0... = Q bit: Operative
    //                        .... .1.. = P bit: Provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...1 = A bit: Active
    //                password: 3029
    //                wrongPasswordAttemptsCounter: 3
    //                notificationToCSE
    //        camel-SubscriptionInfo
    //            o-CSI
    //                o-BcsmCamelTDPDataList: 1 item
    //                    O-BcsmCamelTDPData
    //                        o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csiActive
    //            o-BcsmCamelTDP-CriteriaList: 1 item
    //                O-BcsmCamelTDP-Criteria
    //                    o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                    destinationNumberCriteria
    //                        matchType: enabling (1)
    //                        destinationNumberList: 1 item
    //                            ISDN-AddressString: 91947141874023
    //                                1... .... = Extension: No Extension
    //                                .001 .... = Nature of number: International Number (0x1)
    //                                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                                E.164 number (MSISDN): 491714780432
    //                        destinationNumberLengthList: 1 item
    //                            DestinationNumberLengthList item: 1
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    callTypeCriteria: notForwarded (1)
    //                    o-CauseValueCriteria: 2 items
    //                        CauseValue: 51
    //                        CauseValue: 39
    //            d-CSI
    //                dp-AnalysedInfoCriteriaList: 1 item
    //                    DP-AnalysedInfoCriterium
    //                        dialledNumber: 91947141874023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491714780432
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            t-BCSM-CAMEL-TDP-CriteriaList: 1 item
    //                T-BCSM-CAMEL-TDP-Criteria
    //                    t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    t-CauseValueCriteria: 2 items
    //                        CauseValue: 15
    //                        CauseValue: 39
    //            vt-CSI
    //                t-BcsmCamelTDPDataList: 2 items
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tNoAnswer (14)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tBusy (13)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            tif-CSI
    //            tif-CSI-NotificationToCSE
    //        odb-Info
    //            odb-Data
    //                Padding: 3
    //                odb-GeneralData: 1c0648f8
    //                    0... .... = allOG-CallsBarred: False
    //                    .0.. .... = internationalOGCallsBarred: False
    //                    ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                    ...1 .... = premiumRateInformationOGCallsBarred: True
    //                    .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                    .... .1.. = ss-AccessBarred: True
    //                    .... ..0. = interzonalOGCallsBarred: False
    //                    .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                    0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
    //                    .0.. .... = allECT-Barred: False
    //                    ..0. .... = chargeableECT-Barred: False
    //                    ...0 .... = internationalECT-Barred: False
    //                    .... 0... = interzonalECT-Barred: False
    //                    .... .1.. = doublyChargeableECT-Barred: True
    //                    .... ..1. = multipleECT-Barred: True
    //                    .... ...0 = allPacketOrientedServicesBarred: False
    //                    0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                    .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                    ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                    ...0 .... = allIC-CallsBarred: False
    //                    .... 1... = roamingOutsidePLMNIC-CallsBarred: True
    //                    .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
    //                    .... ..0. = roamingOutsidePLMN-Barred: False
    //                    .... ...0 = roamingOutsidePLMN-CountryBarred: False
    //                    1... .... = registrationAllCF-Barred: True
    //                    .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                    ..1. .... = registrationInterzonalCF-Barred: True
    //                    ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                    .... 1... = registrationInternationalCF-Barred: True
    //                Padding: 4
    //                odb-HPLMN-Data: 80
    //                    1... .... = plmn-SpecificBarringType1: True
    //                    .0.. .... = plmn-SpecificBarringType2: False
    //                    ..0. .... = plmn-SpecificBarringType3: False
    //                    ...0 .... = plmn-SpecificBarringType4: False
    //            notificationToCSE
    //        cw-Data
    //            cwFeatureList: 2 items
    //                Ext-CwFeature
    //                    basicService: ext-BearerService (2)
    //                        ext-BearerService: general-dataPDS (47)
    //                    ss-Status: 0a
    //                    0000 .... = Unused: 0x0
    //                    .... .0.. = P bit: Not provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...0 = A bit: not Active
    //                Ext-CwFeature
    //                    basicService: ext-Teleservice (3)
    //                        ext-Teleservice: allTeleservices (0)
    //                    ss-Status: 07
    //                    0000 .... = Unused: 0x0
    //                    .... 0... = Q bit: Operative
    //                    .... .1.. = P bit: Provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...1 = A bit: Active
    //            notificationToCSE
    //        ch-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        clip-Data
    //            ss-Status: 07
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...1 = A bit: Active
    //            overrideCategory: overrideEnabled (0)
    //            notificationToCSE
    //        clir-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            cliRestrictionOption: temporaryDefaultAllowed (2)
    //            notificationToCSE
    //        ect-data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        serviceCentreAddress: 91947101040012
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710400021
    assertNull(ssInfoForCSE.getForwardingInfoForCSE());
    assertNotNull(ssInfoForCSE.getCallBarringInfoForCSE());
    assertNotNull(ssInfoForCSE.getCallBarringInfoForCSE());
    assertEquals(ssInfoForCSE.getCallBarringInfoForCSE().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allLineIdentificationSS);
    assertEquals(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.general_dataPDS);
    assertFalse(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(0).getSsStatus().getBitQ());
    assertFalse(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(0).getSsStatus().getBitA());
    assertEquals(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(ssInfoForCSE.getCallBarringInfoForCSE().getCallBarringFeatureList().get(1).getSsStatus().getBitA());
    assertEquals(ssInfoForCSE.getCallBarringInfoForCSE().getPassword().getData(), "3029");
    assertEquals(ssInfoForCSE.getCallBarringInfoForCSE().getWrongPasswordAttemptsCounter().intValue(), 3);
    assertTrue(ssInfoForCSE.getCallBarringInfoForCSE().getNotificationToCSE());
    assertNull(ssInfoForCSE.getCallBarringInfoForCSE().getExtensionContainer());
    assertNotNull(camelSubscriptionInfo.getOCsi());
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.continueCall);
    assertNull(camelSubscriptionInfo.getOCsi().getOBcsmCamelTDPDataList().get(0).getExtensionContainer());
    assertEquals(camelSubscriptionInfo.getOCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getOCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getOCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getOCsi().getExtensionContainer());
    assertNotNull(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList());
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getMatchType(), MatchType.enabling);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddressNature(),
        AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getNumberingPlan(),
        NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).getAddress(),
        "491714780432");
    assertFalse((camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().get(0).isExtension()));
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getDestinationNumberCriteria().getDestinationNumberList().size(), 1);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.general_dataPDS);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getCallTypeCriteria(), CallTypeCriteria.notForwarded);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOCauseValueCriteria().get(0).getCauseValueCodeValue(),
        CauseValueCodeValue.InvalidCallReferenceValue);
    assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0).getOCauseValueCriteria().get(1).getCauseValueCodeValue(),
        CauseValueCodeValue.BearerCapabilityNotAuthorized);
    assertNotNull(camelSubscriptionInfo.getDCsi());
    assertFalse(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().isExtension());
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddressNature(),
        AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getNumberingPlan(),
        NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDialledNumber().getAddress(),
        "491714780432");
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getDCsi().getDPAnalysedInfoCriteriaList().get(0).getDefaultCallHandling().getCode(), 0);
    assertEquals(camelSubscriptionInfo.getDCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getDCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getDCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getDCsi().getExtensionContainer());
    assertNotNull(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList());
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTBcsmTriggerDetectionPoint(),
        TBcsmTriggerDetectionPoint.tNoAnswer);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(0).getExtBearerService().
        getBearerServiceCodeValue(), BearerServiceCodeValue.general_dataPDS);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getBasicServiceCriteria().get(1).getExtTeleservice().
        getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(0).
        getCauseValueCodeValue(), CauseValueCodeValue.CallRejected);
    assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0).getTCauseValueCriteria().get(1).
        getCauseValueCodeValue(), CauseValueCodeValue.BearerCapabilityNotAuthorized);
    assertNotNull(camelSubscriptionInfo.getVtCsi());
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getTBcsmTriggerDetectionPoint(),
        TBcsmTriggerDetectionPoint.tNoAnswer);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getServiceKey(), 7);
    assertFalse(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().isExtension());
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getGsmSCFAddress().getAddress(), "491710460029");
    assertEquals(camelSubscriptionInfo.getVtCsi().getTBcsmCamelTDPDataList().get(0).getDefaultCallHandling(), DefaultCallHandling.continueCall);
    assertEquals(camelSubscriptionInfo.getVtCsi().getCamelCapabilityHandling().intValue(), 2);
    assertTrue(camelSubscriptionInfo.getVtCsi().getNotificationToCSE());
    assertTrue(camelSubscriptionInfo.getVtCsi().getCsiActive());
    assertNull(camelSubscriptionInfo.getVtCsi().getExtensionContainer());
    assertTrue(camelSubscriptionInfo.getTifCsi());
    assertTrue(camelSubscriptionInfo.getTifCsiNotificationToCSE());
    assertNull(extensionContainer);
    assertNotNull(odbInfo.getOdbData());
    assertNotNull(odbInfo.getOdbData().getODBGeneralData());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalOGCallsNotToHPLMNCountryBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsNotToHPLMNCountryBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getPremiumRateInformationOGCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getPremiumRateEntertainmentOGCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getSsAccessBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getChargeableECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInternationalECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getInterzonalECTBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getDoublyChargeableECTBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getMultipleECTBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllPacketOrientedServicesBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamerAccessToHPLMNAPBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRoamerAccessToVPLMNAPBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNOGCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getAllICCallsBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICountryICCallsBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNBarred());
    assertFalse(odbInfo.getOdbData().getODBGeneralData().getRoamingOutsidePLMNCountryBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationAllCFBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationCFNotToHPLMNBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInterzonalCFBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInterzonalCFNotToHPLMNBarred());
    assertTrue(odbInfo.getOdbData().getODBGeneralData().getRegistrationInternationalCFBarred());
    assertTrue(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType1());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType2());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType3());
    assertFalse(odbInfo.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType4());
    assertNull(odbInfo.getOdbData().getExtensionContainer());
    assertTrue(odbInfo.getNotificationToCSE());
    assertNotNull(callWaitingData);
    assertEquals(callWaitingData.getCwFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.general_dataPDS);
    assertFalse(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitQ());
    assertTrue(callWaitingData.getCwFeatureList().get(0).getSsStatus().getBitR());
    assertEquals(callWaitingData.getCwFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(callWaitingData.getCwFeatureList().get(1).getSsStatus().getBitA());
    assertTrue(callWaitingData.getNotificationToCSE());
    assertNotNull(callHoldData);
    assertFalse(callHoldData.getSsStatus().getBitP());
    assertTrue(callHoldData.getSsStatus().getBitR());
    assertFalse(callHoldData.getSsStatus().getBitA());
    assertTrue(callHoldData.getNotificationToCSE());
    assertNotNull(clipData);
    assertFalse(clipData.getSsStatus().getBitQ());
    assertTrue(clipData.getSsStatus().getBitP());
    assertTrue(clipData.getSsStatus().getBitR());
    assertTrue(clipData.getSsStatus().getBitA());
    assertEquals(clipData.getOverrideCategory(), OverrideCategory.overrideEnabled);
    assertTrue(clipData.getNotificationToCSE());
    assertNotNull(clirData);
    assertFalse(clirData.getSsStatus().getBitP());
    assertTrue(clirData.getSsStatus().getBitR());
    assertFalse(clirData.getSsStatus().getBitA());
    assertEquals(clirData.getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultAllowed);
    assertTrue(clirData.getNotificationToCSE());
    assertNotNull(ectData);
    assertFalse(ectData.getSsStatus().getBitP());
    assertTrue(ectData.getSsStatus().getBitR());
    assertFalse(ectData.getSsStatus().getBitA());
    assertTrue(ectData.getNotificationToCSE());
    assertNotNull(serviceCentreAddress);
    assertFalse(serviceCentreAddress.isExtension());
    assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(serviceCentreAddress.getAddress(), "491710400021");
  }


  @Test(groups = { "functional.encode", "subscriberInformation" })
  public void testEncode() throws Exception {

    /** Test 1 with ss-InfoFor-CSE: forwardingInfoFor-CSE (0) **/
    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    // returnResultLast
    //    invokeID: 0
    //    resultretres
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    //            forwardingInfoFor-CSE
    //                ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
    //                forwardingFeatureList: 2 items
    //                    Ext-ForwFeature
    //                        basicService: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        ss-Status: 0a
    //                        0000 .... = Unused: 0x0
    //                        .... .0.. = P bit: Not provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...0 = A bit: not Active
    //                        forwardedToNumber: 9188225801652854f1
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 882285105682451
    //                        forwardedToSubaddress: 0205
    //                        forwardingOptions: 88
    //                        1... .... = Notification to forwarding party: Notification
    //                        .0.. .... = Redirecting presentation: No presentation
    //                        ..0. .... = Notification to calling party: No notification
    //                        .... 10.. = Forwarding reason: no reply (0x2)
    //                        noReplyConditionTime: 6
    //                        longForwardedToNumber: 91947101043023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710400332
    //                    Ext-ForwFeature
    //                        basicService: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                        ss-Status: 07
    //                        0000 .... = Unused: 0x0
    //                        .... 0... = Q bit: Operative
    //                        .... .1.. = P bit: Provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...1 = A bit: Active
    //                        forwardedToNumber: 9188225801652854f7
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 882285105682457
    //                        forwardedToSubaddress: 0204
    //                        forwardingOptions: 84
    //                        1... .... = Notification to forwarding party: Notification
    //                        .0.. .... = Redirecting presentation: No presentation
    //                        ..0. .... = Notification to calling party: No notification
    //                        .... 01.. = Forwarding reason: ms busy (0x1)
    //                        noReplyConditionTime: 10
    //                        longForwardedToNumber: 91947101043053
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710400335
    //                notificationToCSE
    //        camel-SubscriptionInfo
    //            o-CSI
    //                o-BcsmCamelTDPDataList: 1 item
    //                    O-BcsmCamelTDPData
    //                        o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csiActive
    //            o-BcsmCamelTDP-CriteriaList: 1 item
    //                O-BcsmCamelTDP-Criteria
    //                    o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                    destinationNumberCriteria
    //                        matchType: enabling (1)
    //                        destinationNumberList: 1 item
    //                            ISDN-AddressString: 91947141874023
    //                                1... .... = Extension: No Extension
    //                                .001 .... = Nature of number: International Number (0x1)
    //                                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                                E.164 number (MSISDN): 491714780432
    //                        destinationNumberLengthList: 1 item
    //                            DestinationNumberLengthList item: 1
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    callTypeCriteria: notForwarded (1)
    //                    o-CauseValueCriteria: 2 items
    //                        CauseValue: 51
    //                        CauseValue: 39
    //            d-CSI
    //                dp-AnalysedInfoCriteriaList: 1 item
    //                    DP-AnalysedInfoCriterium
    //                        dialledNumber: 91947141874023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491714780432
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            t-BCSM-CAMEL-TDP-CriteriaList: 1 item
    //                T-BCSM-CAMEL-TDP-Criteria
    //                    t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: allBearerServices (0)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    t-CauseValueCriteria: 2 items
    //                        CauseValue: 15
    //                        CauseValue: 39
    //            vt-CSI
    //                t-BcsmCamelTDPDataList: 2 items
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tNoAnswer (14)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tBusy (13)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            tif-CSI
    //            tif-CSI-NotificationToCSE
    //        odb-Info
    //            odb-Data
    //                Padding: 3
    //                odb-GeneralData: 1c0648f8
    //                    0... .... = allOG-CallsBarred: False
    //                    .0.. .... = internationalOGCallsBarred: False
    //                    ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                    ...1 .... = premiumRateInformationOGCallsBarred: True
    //                    .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                    .... .1.. = ss-AccessBarred: True
    //                    .... ..0. = interzonalOGCallsBarred: False
    //                    .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                    0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
    //                    .0.. .... = allECT-Barred: False
    //                    ..0. .... = chargeableECT-Barred: False
    //                    ...0 .... = internationalECT-Barred: False
    //                    .... 0... = interzonalECT-Barred: False
    //                    .... .1.. = doublyChargeableECT-Barred: True
    //                    .... ..1. = multipleECT-Barred: True
    //                    .... ...0 = allPacketOrientedServicesBarred: False
    //                    0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                    .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                    ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                    ...0 .... = allIC-CallsBarred: False
    //                    .... 1... = roamingOutsidePLMNIC-CallsBarred: True
    //                    .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
    //                    .... ..0. = roamingOutsidePLMN-Barred: False
    //                    .... ...0 = roamingOutsidePLMN-CountryBarred: False
    //                    1... .... = registrationAllCF-Barred: True
    //                    .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                    ..1. .... = registrationInterzonalCF-Barred: True
    //                    ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                    .... 1... = registrationInternationalCF-Barred: True
    //                Padding: 4
    //                odb-HPLMN-Data: 80
    //                    1... .... = plmn-SpecificBarringType1: True
    //                    .0.. .... = plmn-SpecificBarringType2: False
    //                    ..0. .... = plmn-SpecificBarringType3: False
    //                    ...0 .... = plmn-SpecificBarringType4: False
    //            notificationToCSE
    //        cw-Data
    //            cwFeatureList: 2 items
    //                Ext-CwFeature
    //                    basicService: ext-BearerService (2)
    //                        ext-BearerService: allBearerServices (0)
    //                    ss-Status: 0a
    //                    0000 .... = Unused: 0x0
    //                    .... .0.. = P bit: Not provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...0 = A bit: not Active
    //                Ext-CwFeature
    //                    basicService: ext-Teleservice (3)
    //                        ext-Teleservice: allTeleservices (0)
    //                    ss-Status: 07
    //                    0000 .... = Unused: 0x0
    //                    .... 0... = Q bit: Operative
    //                    .... .1.. = P bit: Provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...1 = A bit: Active
    //            notificationToCSE
    //        ch-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        clip-Data
    //            ss-Status: 07
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...1 = A bit: Active
    //            overrideCategory: overrideEnabled (0)
    //            notificationToCSE
    //        clir-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            cliRestrictionOption: temporaryDefaultAllowed (2)
    //            notificationToCSE
    //        ect-data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        serviceCentreAddress: 91947101040012
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710400021
    SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allMOLR_SS);
    ExtBasicServiceCode bearerServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allBearerServices));
    ExtBasicServiceCode teleServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
    ExtSSStatus ssStatus1 = new ExtSSStatusImpl(true, false, true, false);
    ExtSSStatus ssStatus2= new ExtSSStatusImpl(false, true, true, true);
    ISDNAddressString forwardedToNumber1 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
    ISDNAddressString forwardedToNumber2 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682457");
    ISDNSubaddressString forwardedToSubaddress1 = new ISDNSubaddressStringImpl(new byte[] { 2, 5 });
    ISDNSubaddressString forwardedToSubaddress2 = new ISDNSubaddressStringImpl(new byte[] { 2, 4 });
    ExtForwOptions forwardingOptions1 = new ExtForwOptionsImpl(true, false, false,
        ExtForwOptionsForwardingReason.noReply);
    ExtForwOptions forwardingOptions2 = new ExtForwOptionsImpl(true, false, false,
        ExtForwOptionsForwardingReason.msBusy);
    Integer noReplyConditionTime1 = 6;
    Integer noReplyConditionTime2 = 10;
    FTNAddressString longForwardedToNumber1 = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710400332");
    FTNAddressString longForwardedToNumber2 = new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710400335");
    ExtForwFeature extForwFeature1 = new ExtForwFeatureImpl(bearerServiceCode, ssStatus1, forwardedToNumber1,
        forwardedToSubaddress1, forwardingOptions1, noReplyConditionTime1, null, longForwardedToNumber1);
    ExtForwFeature extForwFeature2 = new ExtForwFeatureImpl(teleServiceCode, ssStatus2, forwardedToNumber2,
        forwardedToSubaddress2, forwardingOptions2, noReplyConditionTime2, null, longForwardedToNumber2);
    ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<>();
    forwardingFeatureList.add(extForwFeature1);
    forwardingFeatureList.add(extForwFeature2);
    ExtForwardingInfoForCSE forwardingInfoForCSE = new ExtForwardingInfoForCSEImpl(ssCode, forwardingFeatureList, true, null);
    ExtSSInfoForCSEImpl ssInfoForCSE = new ExtSSInfoForCSEImpl(forwardingInfoForCSE);

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
    ArrayList<ExtBasicServiceCode> basicServiceGroupList = new ArrayList<>();
    basicServiceGroupList.add(bearerServiceCode);
    basicServiceGroupList.add(teleServiceCode);
    MatchType matchType = MatchType.enabling;
    ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<>();
    ISDNAddressString destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
    destinationNumberList.add(destinationNumber);
    ArrayList<Integer> destinationNumberLengthList = new ArrayList<>();
    destinationNumberLengthList.add(1);
    DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
    CallTypeCriteria callTypeCriteria = CallTypeCriteria.notForwarded;
    ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<>();
    CauseValue causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
    CauseValue causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
    oCauseValueCriteria.add(causeValue1);
    oCauseValueCriteria.add(causeValue2);
    ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<>();
    OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
        basicServiceGroupList, callTypeCriteria, oCauseValueCriteria, null);
    Result result = new Result(oBcsmCamelTDPCriteriaList, oBcsmCamelTdpCriteria);
    result.oBcsmCamelTDPCriteriaList.add(result.oBcsmCamelTdpCriteria);
    oBcsmCamelTDPCriteriaList = result.oBcsmCamelTDPCriteriaList;
    ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<>();
    ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
    DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
    dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
    DCSI dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
    TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tNoAnswer;
    ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<>();
    CauseValue tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
    CauseValue tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
    tCauseValueCriteria.add(tcv1);
    tCauseValueCriteria.add(tcv2);
    TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceGroupList, tCauseValueCriteria);
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
    CAMELSubscriptionInfo camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(oCSI, oBcsmCamelTDPCriteriaList, dCSI,
        null, tBcsmCamelTdpCriteriaList, vtCsi, null, true, true, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null);
    // odbData
    boolean allOGCallsBarred = false;
    boolean internationalOGCallsBarred = false;
    boolean internationalOGCallsNotToHPLMNCountryBarred = false;
    boolean premiumRateInformationOGCallsBarred = true;
    boolean premiumRateEntertainmentOGCallsBarred = true;
    boolean ssAccessBarred = true;
    boolean interzonalOGCallsBarred = false;
    boolean interzonalOGCallsNotToHPLMNCountryBarred = false;
    boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = false;
    boolean allECTBarred = false;
    boolean chargeableECTBarred = false;
    boolean internationalECTBarred = false;
    boolean interzonalECTBarred = false;
    boolean doublyChargeableECTBarred = true;
    boolean multipleECTBarred = true;
    boolean allPacketOrientedServicesBarred = false;
    boolean roamerAccessToHPLMNAPBarred = false;
    boolean roamerAccessToVPLMNAPBarred = true;
    boolean roamingOutsidePLMNOGCallsBarred = false;
    boolean allICCallsBarred = false;
    boolean roamingOutsidePLMNICCallsBarred = true;
    boolean roamingOutsidePLMNICountryICCallsBarred = false;
    boolean roamingOutsidePLMNBarred = false;
    boolean roamingOutsidePLMNCountryBarred = false;
    boolean registrationAllCFBarred = true;
    boolean registrationCFNotToHPLMNBarred = true;
    boolean registrationInterzonalCFBarred = true;
    boolean registrationInterzonalCFNotToHPLMNBarred = true;
    boolean registrationInternationalCFBarred = true;
    ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred, internationalOGCallsNotToHPLMNCountryBarred,
        premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred, ssAccessBarred,
        interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred, interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
        allECTBarred, chargeableECTBarred, internationalECTBarred, interzonalECTBarred,
        doublyChargeableECTBarred, multipleECTBarred, allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
        roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
        roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred, roamingOutsidePLMNCountryBarred,
        registrationAllCFBarred, registrationCFNotToHPLMNBarred, registrationInterzonalCFBarred,
        registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
    boolean plmnSpecificBarringType1 = true;
    boolean plmnSpecificBarringType2 = false;
    boolean plmnSpecificBarringType3 = false;
    boolean plmnSpecificBarringType4 = false;
    ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
    ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
    ODBInfo odbInfo = new ODBInfoImpl(odbData, true, null);
    ExtCwFeature cwFeature1 = new ExtCwFeatureImpl(bearerServiceCode, ssStatus1);
    ExtCwFeature cwFeature2 = new ExtCwFeatureImpl(teleServiceCode, ssStatus2);
    ArrayList<ExtCwFeature> cwFeatureList = new ArrayList<>();
    cwFeatureList.add(cwFeature1);
    cwFeatureList.add(cwFeature2);
    CallWaitingData cwData = new CallWaitingDataImpl(cwFeatureList, true);
    CallHoldData chData = new CallHoldDataImpl(ssStatus1, true);
    ClipData clipData = new ClipDataImpl(ssStatus2, OverrideCategory.overrideEnabled, true);
    ClirData clirData = new ClirDataImpl(ssStatus1, CliRestrictionOption.temporaryDefaultAllowed, true);
    EctData ectData = new EctDataImpl(ssStatus1, true);
    AddressString serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710400021");

    AnyTimeModificationResponseImpl atmResp1 = new AnyTimeModificationResponseImpl(ssInfoForCSE, camelSubscriptionInfo, null,
        odbInfo, cwData, chData, clipData, clirData, ectData, serviceCentreAddress);

    AsnOutputStream asnOS = new AsnOutputStream();
    atmResp1.encodeAll(asnOS);
    byte[] encodedData = asnOS.toByteArray();
    byte[] rawData = getEncodedDataWithForwardingInfoChoice();
    assertTrue(Arrays.equals(rawData, encodedData));


    /** Test 2 with ss-InfoFor-CSE: callBarringInfoFor-CSE (1) **/
    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    // returnResultLast
    //    invokeID: 0
    //    resultretres
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    //            callBarringInfoFor-CSE
    //                ss-Code: allLineIdentificationSS - all line identification SS (16)
    //                callBarringFeatureList: 2 items
    //                    Ext-CallBarringFeature
    //                        basicService: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        ss-Status: 0a
    //                        0000 .... = Unused: 0x0
    //                        .... .0.. = P bit: Not provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...0 = A bit: not Active
    //                    Ext-CallBarringFeature
    //                        basicService: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                        ss-Status: 07
    //                        0000 .... = Unused: 0x0
    //                        .... 0... = Q bit: Operative
    //                        .... .1.. = P bit: Provisioned
    //                        .... ..1. = R bit: Registered
    //                        .... ...1 = A bit: Active
    //                password: 3029
    //                wrongPasswordAttemptsCounter: 3
    //                notificationToCSE
    //        camel-SubscriptionInfo
    //            o-CSI
    //                o-BcsmCamelTDPDataList: 1 item
    //                    O-BcsmCamelTDPData
    //                        o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csiActive
    //            o-BcsmCamelTDP-CriteriaList: 1 item
    //                O-BcsmCamelTDP-Criteria
    //                    o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
    //                    destinationNumberCriteria
    //                        matchType: enabling (1)
    //                        destinationNumberList: 1 item
    //                            ISDN-AddressString: 91947141874023
    //                                1... .... = Extension: No Extension
    //                                .001 .... = Nature of number: International Number (0x1)
    //                                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                                E.164 number (MSISDN): 491714780432
    //                        destinationNumberLengthList: 1 item
    //                            DestinationNumberLengthList item: 1
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    callTypeCriteria: notForwarded (1)
    //                    o-CauseValueCriteria: 2 items
    //                        CauseValue: 51
    //                        CauseValue: 39
    //            d-CSI
    //                dp-AnalysedInfoCriteriaList: 1 item
    //                    DP-AnalysedInfoCriterium
    //                        dialledNumber: 91947141874023
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491714780432
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            t-BCSM-CAMEL-TDP-CriteriaList: 1 item
    //                T-BCSM-CAMEL-TDP-Criteria
    //                    t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
    //                    basicServiceCriteria: 2 items
    //                        Ext-BasicServiceCode: ext-BearerService (2)
    //                            ext-BearerService: general-dataPDS (47)
    //                        Ext-BasicServiceCode: ext-Teleservice (3)
    //                            ext-Teleservice: allTeleservices (0)
    //                    t-CauseValueCriteria: 2 items
    //                        CauseValue: 15
    //                        CauseValue: 39
    //            vt-CSI
    //                t-BcsmCamelTDPDataList: 2 items
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tNoAnswer (14)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                    T-BcsmCamelTDPData
    //                        t-BcsmTriggerDetectionPoint: tBusy (13)
    //                        serviceKey: 7
    //                        gsmSCF-Address: 91947101640092
    //                            1... .... = Extension: No Extension
    //                            .001 .... = Nature of number: International Number (0x1)
    //                            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                            E.164 number (MSISDN): 491710460029
    //                        defaultCallHandling: continueCall (0)
    //                camelCapabilityHandling: 2
    //                notificationToCSE
    //                csi-Active
    //            tif-CSI
    //            tif-CSI-NotificationToCSE
    //        odb-Info
    //            odb-Data
    //                Padding: 3
    //                odb-GeneralData: 1c0648f8
    //                    0... .... = allOG-CallsBarred: False
    //                    .0.. .... = internationalOGCallsBarred: False
    //                    ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                    ...1 .... = premiumRateInformationOGCallsBarred: True
    //                    .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                    .... .1.. = ss-AccessBarred: True
    //                    .... ..0. = interzonalOGCallsBarred: False
    //                    .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                    0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
    //                    .0.. .... = allECT-Barred: False
    //                    ..0. .... = chargeableECT-Barred: False
    //                    ...0 .... = internationalECT-Barred: False
    //                    .... 0... = interzonalECT-Barred: False
    //                    .... .1.. = doublyChargeableECT-Barred: True
    //                    .... ..1. = multipleECT-Barred: True
    //                    .... ...0 = allPacketOrientedServicesBarred: False
    //                    0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                    .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                    ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                    ...0 .... = allIC-CallsBarred: False
    //                    .... 1... = roamingOutsidePLMNIC-CallsBarred: True
    //                    .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
    //                    .... ..0. = roamingOutsidePLMN-Barred: False
    //                    .... ...0 = roamingOutsidePLMN-CountryBarred: False
    //                    1... .... = registrationAllCF-Barred: True
    //                    .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                    ..1. .... = registrationInterzonalCF-Barred: True
    //                    ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                    .... 1... = registrationInternationalCF-Barred: True
    //                Padding: 4
    //                odb-HPLMN-Data: 80
    //                    1... .... = plmn-SpecificBarringType1: True
    //                    .0.. .... = plmn-SpecificBarringType2: False
    //                    ..0. .... = plmn-SpecificBarringType3: False
    //                    ...0 .... = plmn-SpecificBarringType4: False
    //            notificationToCSE
    //        cw-Data
    //            cwFeatureList: 2 items
    //                Ext-CwFeature
    //                    basicService: ext-BearerService (2)
    //                        ext-BearerService: general-dataPDS (47)
    //                    ss-Status: 0a
    //                    0000 .... = Unused: 0x0
    //                    .... .0.. = P bit: Not provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...0 = A bit: not Active
    //                Ext-CwFeature
    //                    basicService: ext-Teleservice (3)
    //                        ext-Teleservice: allTeleservices (0)
    //                    ss-Status: 07
    //                    0000 .... = Unused: 0x0
    //                    .... 0... = Q bit: Operative
    //                    .... .1.. = P bit: Provisioned
    //                    .... ..1. = R bit: Registered
    //                    .... ...1 = A bit: Active
    //            notificationToCSE
    //        ch-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        clip-Data
    //            ss-Status: 07
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...1 = A bit: Active
    //            overrideCategory: overrideEnabled (0)
    //            notificationToCSE
    //        clir-Data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            cliRestrictionOption: temporaryDefaultAllowed (2)
    //            notificationToCSE
    //        ect-data
    //            ss-Status: 0a
    //            0000 .... = Unused: 0x0
    //            .... .0.. = P bit: Not provisioned
    //            .... ..1. = R bit: Registered
    //            .... ...0 = A bit: not Active
    //            notificationToCSE
    //        serviceCentreAddress: 91947101040012
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710400021
    ssCode = new SSCodeImpl(SupplementaryCodeValue.allLineIdentificationSS);
    ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<>();
    bearerServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.general_dataPDS));
    teleServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allTeleservices));
    ssStatus1 = new ExtSSStatusImpl(true, false, true, false);
    ssStatus2= new ExtSSStatusImpl(false, true, true, true);
    ExtCallBarringFeature callBarringFeature1 = new ExtCallBarringFeatureImpl(bearerServiceCode, ssStatus1, null);
    ExtCallBarringFeature callBarringFeature2 = new ExtCallBarringFeatureImpl(teleServiceCode, ssStatus2, null);
    callBarringFeatureList.add(callBarringFeature1);
    callBarringFeatureList.add(callBarringFeature2);
    Password password = new PasswordImpl("3029");
    Integer wrongPasswordAttemptsCounter = 3;
    ExtCallBarringInfoForCSEImpl callBarringInfoForCSE = new ExtCallBarringInfoForCSEImpl(ssCode, callBarringFeatureList, password, wrongPasswordAttemptsCounter,
        true, null);
    ssInfoForCSE = new ExtSSInfoForCSEImpl(callBarringInfoForCSE);

    gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
    oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, null);
    oBcsmCamelTDPDataList = new ArrayList<>();
    oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
    camelCapabilityHandling = 2;
    oCSI = new OCSIImpl(oBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
    basicServiceGroupList = new ArrayList<>();
    basicServiceGroupList.add(bearerServiceCode);
    basicServiceGroupList.add(teleServiceCode);
    destinationNumberList = new ArrayList<>();
    destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
    destinationNumberList.add(destinationNumber);
    destinationNumberLengthList = new ArrayList<>();
    destinationNumberLengthList.add(1);
    destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
    oCauseValueCriteria = new ArrayList<>();
    causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
    causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
    oCauseValueCriteria.add(causeValue1);
    oCauseValueCriteria.add(causeValue2);
    oBcsmCamelTDPCriteriaList = new ArrayList<>();
    oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
        basicServiceGroupList, callTypeCriteria, oCauseValueCriteria, null);
    result = new Result(oBcsmCamelTDPCriteriaList, oBcsmCamelTdpCriteria);
    result.oBcsmCamelTDPCriteriaList.add(result.oBcsmCamelTdpCriteria);
    oBcsmCamelTDPCriteriaList = result.oBcsmCamelTDPCriteriaList;
    dpAnalysedInfoCriteriaList = new ArrayList<>();
    dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
    dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
    dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
    dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
    tCauseValueCriteria = new ArrayList<>();
    tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
    tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
    tCauseValueCriteria.add(tcv1);
    tCauseValueCriteria.add(tcv2);
    tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceGroupList, tCauseValueCriteria);
    tBcsmCamelTdpCriteriaList = new ArrayList<>();
    tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
    tBcsmCamelTDPDataList = new ArrayList<>();
    tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, null);
    tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, null);
    tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
    tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
    vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
    camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(oCSI, oBcsmCamelTDPCriteriaList, dCSI,
        null, tBcsmCamelTdpCriteriaList, vtCsi, null, true, true, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null);

    cwFeature1 = new ExtCwFeatureImpl(bearerServiceCode, ssStatus1);
    cwFeature2 = new ExtCwFeatureImpl(teleServiceCode, ssStatus2);
    cwFeatureList = new ArrayList<>();
    cwFeatureList.add(cwFeature1);
    cwFeatureList.add(cwFeature2);
    cwData = new CallWaitingDataImpl(cwFeatureList, true);
    chData = new CallHoldDataImpl(ssStatus1, true);
    clipData = new ClipDataImpl(ssStatus2, OverrideCategory.overrideEnabled, true);
    clirData = new ClirDataImpl(ssStatus1, CliRestrictionOption.temporaryDefaultAllowed, true);
    ectData = new EctDataImpl(ssStatus1, true);
    serviceCentreAddress = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710400021");

    AnyTimeModificationResponseImpl atmResp2 = new AnyTimeModificationResponseImpl(ssInfoForCSE, camelSubscriptionInfo, null,
        odbInfo, cwData, chData, clipData, clirData, ectData, serviceCentreAddress);

    asnOS = new AsnOutputStream();
    atmResp2.encodeAll(asnOS);
    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithCallBarringInfoForCSE();
    assertTrue(Arrays.equals(rawData, encodedData));
  }

  private static class Result {
    protected ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
    protected OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria;

    public Result(ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria) {
      this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
      this.oBcsmCamelTdpCriteria = oBcsmCamelTdpCriteria;
    }
  }
}
