package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCallBarringInfoForCSE;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtForwardingInfoForCSE;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ExtCallBarringInfoForCSEImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ExtForwardingInfoForCSEImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ExtSSInfoForCSEImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ExtSSInfoForCSETest {

  private byte[] getEncodedDataForwardingInfoChoice() {
    return new byte[] { (byte) 0xa0, 0x53,
        (byte) 0x80, 0x01, (byte) 0xc0, (byte) 0xa1, 0x4c, 0x30, 0x24, (byte) 0x82,
        0x01, 0x00, (byte) 0x84, 0x01, 0x0a, (byte) 0x85, 0x09, (byte) 0x91,
        (byte) 0x88, 0x22, 0x58, 0x01, 0x65, 0x28, 0x54, (byte) 0xf1,
        (byte) 0x88, 0x02, 0x02, 0x05, (byte) 0x86, 0x01, (byte) 0x88, (byte) 0x87,
        0x01, 0x06, (byte) 0x8a, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
        0x04, 0x30, 0x23, 0x30, 0x24, (byte) 0x83, 0x01, 0x00,
        (byte) 0x84, 0x01, 0x07, (byte) 0x85, 0x09, (byte) 0x91, (byte) 0x88, 0x22,
        0x58, 0x01, 0x65, 0x28, 0x54, (byte) 0xf7, (byte) 0x88, 0x02,
        0x02, 0x04, (byte) 0x86, 0x01, (byte) 0x84, (byte) 0x87, 0x01, 0x0a,
        (byte) 0x8a, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x04, 0x30,
        0x53, (byte) 0x82, 0x00
    };
  }

  private byte[] getEncodedDataCallBarringChoice() {
    return new byte[] { (byte) 0xa1, 0x20,
        (byte) 0x80, 0x01, 0x10, (byte) 0xa1, 0x10, 0x30, 0x06, (byte) 0x82,
        0x01, 0x2f, (byte) 0x84, 0x01, 0x0a, 0x30, 0x06, (byte) 0x83,
        0x01, 0x00, (byte) 0x84, 0x01, 0x07, (byte) 0x82, 0x04, 0x33,
        0x30, 0x32, 0x39, (byte) 0x83, 0x01, 0x03, (byte) 0x84, 0x00
    };
  }

  @Test(groups = { "functional.decode", "subscriberInformation" })
  public void testDecode() throws Exception {

    /** Test 1 with ss-InfoFor-CSE: callBarringInfoFor-CSE (1) **/
    byte[] data = getEncodedDataForwardingInfoChoice();
    AsnInputStream asn = new AsnInputStream(data);
    int tag = asn.readTag();
    ExtSSInfoForCSEImpl extSSInfoForCSE = new ExtSSInfoForCSEImpl();
    extSSInfoForCSE.decodeAll(asn);

    assertEquals(tag, Tag.CLASS_UNIVERSAL);
    assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

    ExtForwardingInfoForCSE forwardingInfoForCSE = extSSInfoForCSE.getForwardingInfoForCSE();
    ExtCallBarringInfoForCSE callBarringInfoForCSE = extSSInfoForCSE.getCallBarringInfoForCSE();

    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    // ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    //    forwardingInfoFor-CSE
    //        ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
    //        forwardingFeatureList: 2 items
    //            Ext-ForwFeature
    //                basicService: ext-BearerService (2)
    //                    ext-BearerService: allBearerServices (0)
    //                ss-Status: 0a
    //                0000 .... = Unused: 0x0
    //                .... .0.. = P bit: Not provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...0 = A bit: not Active
    //                forwardedToNumber: 9188225801652854f1
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 882285105682451
    //                forwardedToSubaddress: 0205
    //                forwardingOptions: 88
    //                1... .... = Notification to forwarding party: Notification
    //                .0.. .... = Redirecting presentation: No presentation
    //                ..0. .... = Notification to calling party: No notification
    //                .... 10.. = Forwarding reason: no reply (0x2)
    //                noReplyConditionTime: 6
    //                longForwardedToNumber: 91947101043023
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 491710400332
    //            Ext-ForwFeature
    //                basicService: ext-Teleservice (3)
    //                    ext-Teleservice: allTeleservices (0)
    //                ss-Status: 07
    //                0000 .... = Unused: 0x0
    //                .... 0... = Q bit: Operative
    //                .... .1.. = P bit: Provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...1 = A bit: Active
    //                forwardedToNumber: 9188225801652854f7
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 882285105682457
    //                forwardedToSubaddress: 0204
    //                forwardingOptions: 84
    //                1... .... = Notification to forwarding party: Notification
    //                .0.. .... = Redirecting presentation: No presentation
    //                ..0. .... = Notification to calling party: No notification
    //                .... 01.. = Forwarding reason: ms busy (0x1)
    //                noReplyConditionTime: 10
    //                longForwardedToNumber: 91947101043053
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 491710400335
    //        notificationToCSE
    assertNotNull(forwardingInfoForCSE);
    assertEquals(forwardingInfoForCSE.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allMOLR_SS);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.allBearerServices);
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(0).getSsStatus().getBitR());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getSsStatus().getBitA());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardedToNumber().isExtension());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardedToNumber().getAddress(), "882285105682451");
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardedToSubaddress().getData(), new byte[] {0x02, 0x05});
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardingOptions().getNotificationToForwardingParty());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardingOptions().getRedirectingPresentation());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardingOptions().getNotificationToCallingParty());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getForwardingOptions().getExtForwOptionsForwardingReason(),
        ExtForwOptionsForwardingReason.noReply);
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(0).getLongForwardedToNumber().isExtension());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getLongForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getLongForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(0).getLongForwardedToNumber().getAddress(), "491710400332");
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(1).getSsStatus().getBitA());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardedToNumber().isExtension());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardedToNumber().getAddress(), "882285105682457");
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardedToSubaddress().getData(), new byte[] {0x02, 0x04});
    assertTrue(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardingOptions().getNotificationToForwardingParty());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardingOptions().getRedirectingPresentation());
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardingOptions().getNotificationToCallingParty());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getForwardingOptions().getExtForwOptionsForwardingReason(),
        ExtForwOptionsForwardingReason.msBusy);
    assertFalse(forwardingInfoForCSE.getForwardingFeatureList().get(1).getLongForwardedToNumber().isExtension());
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getLongForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getLongForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(forwardingInfoForCSE.getForwardingFeatureList().get(1).getLongForwardedToNumber().getAddress(), "491710400335");
    assertTrue(forwardingInfoForCSE.getNotificationToCSE());
    assertNull(forwardingInfoForCSE.getExtensionContainer());
    assertNull(callBarringInfoForCSE);

    /** Test 2 with ss-InfoFor-CSE: callBarringInfoFor-CSE (1) **/
    data = getEncodedDataCallBarringChoice();
    asn = new AsnInputStream(data);
    tag = asn.readTag();
    extSSInfoForCSE = new ExtSSInfoForCSEImpl();
    extSSInfoForCSE.decodeAll(asn);

    assertEquals(tag, Tag.CLASS_APPLICATION);
    assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

    forwardingInfoForCSE = extSSInfoForCSE.getForwardingInfoForCSE();
    callBarringInfoForCSE = extSSInfoForCSE.getCallBarringInfoForCSE();

    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    // ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    //    callBarringInfoFor-CSE
    //        ss-Code: allLineIdentificationSS - all line identification SS (16)
    //        callBarringFeatureList: 2 items
    //            Ext-CallBarringFeature
    //                basicService: ext-BearerService (2)
    //                    ext-BearerService: general-dataPDS (47)
    //                ss-Status: 0a
    //                0000 .... = Unused: 0x0
    //                .... .0.. = P bit: Not provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...0 = A bit: not Active
    //            Ext-CallBarringFeature
    //                basicService: ext-Teleservice (3)
    //                    ext-Teleservice: allTeleservices (0)
    //                ss-Status: 07
    //                0000 .... = Unused: 0x0
    //                .... 0... = Q bit: Operative
    //                .... .1.. = P bit: Provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...1 = A bit: Active
    //        password: 3029
    //        wrongPasswordAttemptsCounter: 3
    //        notificationToCSE
    assertNull(forwardingInfoForCSE);
    assertNotNull(callBarringInfoForCSE);
    assertEquals(callBarringInfoForCSE.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allLineIdentificationSS);
    assertEquals(callBarringInfoForCSE.getCallBarringFeatureList().get(0).getBasicService().getExtBearerService().getBearerServiceCodeValue(),
        BearerServiceCodeValue.general_dataPDS);
    assertFalse(callBarringInfoForCSE.getCallBarringFeatureList().get(0).getSsStatus().getBitP());
    assertTrue(callBarringInfoForCSE.getCallBarringFeatureList().get(0).getSsStatus().getBitQ());
    assertFalse(callBarringInfoForCSE.getCallBarringFeatureList().get(0).getSsStatus().getBitA());
    assertEquals(callBarringInfoForCSE.getCallBarringFeatureList().get(1).getBasicService().getExtTeleservice().getTeleserviceCodeValue(),
        TeleserviceCodeValue.allTeleservices);
    assertFalse(callBarringInfoForCSE.getCallBarringFeatureList().get(1).getSsStatus().getBitQ());
    assertTrue(callBarringInfoForCSE.getCallBarringFeatureList().get(1).getSsStatus().getBitP());
    assertTrue(callBarringInfoForCSE.getCallBarringFeatureList().get(1).getSsStatus().getBitR());
    assertTrue(callBarringInfoForCSE.getCallBarringFeatureList().get(1).getSsStatus().getBitA());
    assertEquals(callBarringInfoForCSE.getPassword().getData(), "3029");
    assertEquals(callBarringInfoForCSE.getWrongPasswordAttemptsCounter().intValue(), 3);
    assertTrue(callBarringInfoForCSE.getNotificationToCSE());
    assertNull(callBarringInfoForCSE.getExtensionContainer());
  }


  @Test(groups = { "functional.encode", "subscriberInformation" })
  public void testEncode() throws Exception {

    /** Test 1 with ss-InfoFor-CSE: forwardingInfoFor-CSE (0) **/
    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    // ss-InfoFor-CSE: forwardingInfoFor-CSE (0)
    //    forwardingInfoFor-CSE
    //        ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
    //        forwardingFeatureList: 2 items
    //            Ext-ForwFeature
    //                basicService: ext-BearerService (2)
    //                    ext-BearerService: allBearerServices (0)
    //                ss-Status: 0a
    //                0000 .... = Unused: 0x0
    //                .... .0.. = P bit: Not provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...0 = A bit: not Active
    //                forwardedToNumber: 9188225801652854f1
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 882285105682451
    //                forwardedToSubaddress: 0205
    //                forwardingOptions: 88
    //                1... .... = Notification to forwarding party: Notification
    //                .0.. .... = Redirecting presentation: No presentation
    //                ..0. .... = Notification to calling party: No notification
    //                .... 10.. = Forwarding reason: no reply (0x2)
    //                noReplyConditionTime: 6
    //                longForwardedToNumber: 91947101043023
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 491710400332
    //            Ext-ForwFeature
    //                basicService: ext-Teleservice (3)
    //                    ext-Teleservice: allTeleservices (0)
    //                ss-Status: 07
    //                0000 .... = Unused: 0x0
    //                .... 0... = Q bit: Operative
    //                .... .1.. = P bit: Provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...1 = A bit: Active
    //                forwardedToNumber: 9188225801652854f7
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 882285105682457
    //                forwardedToSubaddress: 0204
    //                forwardingOptions: 84
    //                1... .... = Notification to forwarding party: Notification
    //                .0.. .... = Redirecting presentation: No presentation
    //                ..0. .... = Notification to calling party: No notification
    //                .... 01.. = Forwarding reason: ms busy (0x1)
    //                noReplyConditionTime: 10
    //                longForwardedToNumber: 91947101043053
    //                    1... .... = Extension: No Extension
    //                    .001 .... = Nature of number: International Number (0x1)
    //                    .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                    E.164 number (MSISDN): 491710400335
    //        notificationToCSE
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

    AsnOutputStream asnOS = new AsnOutputStream();
    ssInfoForCSE.encodeAll(asnOS);

    byte[] encodedData = asnOS.toByteArray();
    byte[] rawData = getEncodedDataForwardingInfoChoice();
    Assert.assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 2 with ss-InfoFor-CSE: callBarringInfoFor-CSE (1) **/
    // Wireshark trace example from MAP load test with ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    // ss-InfoFor-CSE: callBarringInfoFor-CSE (1)
    //    callBarringInfoFor-CSE
    //        ss-Code: allLineIdentificationSS - all line identification SS (16)
    //        callBarringFeatureList: 2 items
    //            Ext-CallBarringFeature
    //                basicService: ext-BearerService (2)
    //                    ext-BearerService: general-dataPDS (47)
    //                ss-Status: 0a
    //                0000 .... = Unused: 0x0
    //                .... .0.. = P bit: Not provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...0 = A bit: not Active
    //            Ext-CallBarringFeature
    //                basicService: ext-Teleservice (3)
    //                    ext-Teleservice: allTeleservices (0)
    //                ss-Status: 07
    //                0000 .... = Unused: 0x0
    //                .... 0... = Q bit: Operative
    //                .... .1.. = P bit: Provisioned
    //                .... ..1. = R bit: Registered
    //                .... ...1 = A bit: Active
    //        password: 3029
    //        wrongPasswordAttemptsCounter: 3
    //        notificationToCSE
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

    asnOS = new AsnOutputStream();
    ssInfoForCSE.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataCallBarringChoice();
    Assert.assertTrue(Arrays.equals(rawData, encodedData));
  }

}
