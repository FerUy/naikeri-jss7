package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCFInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCHInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIPInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIRInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSG;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCWInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForECTInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForIPSMGWData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedServingNode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.NetworkNodeDiameterAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCBInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCFInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCHInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCLIPInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCLIRInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCSGImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForCWInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForECTInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForIPSMGWDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ModificationRequestForODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;


/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class AnyTimeModificationRequestTest {

  private byte[] getEncodedDataWithModReqForCFInfo() {
    return new byte[] { 0x30, 0x3d,
      (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
      0x65, 0x08, 0x60, (byte) 0xf6, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
      0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0xa2, 0x20, (byte) 0x80,
      0x01, (byte) 0x81, (byte) 0xa1, 0x03, (byte) 0x82, 0x01, 0x40, (byte) 0x82,
      0x01, 0x05, (byte) 0x83, 0x09, (byte) 0x91, (byte) 0x88, 0x22, 0x58,
      0x01, 0x65, 0x28, 0x54, (byte) 0xf1, (byte) 0x84, 0x02, 0x02,
      0x05, (byte) 0x85, 0x01, 0x11, (byte) 0x86, 0x01, 0x01, (byte) 0x86,
      0x00, (byte) 0x89, 0x02, 0x00, (byte) 0x80};
  }

  private byte[] getEncodedDataWithModReqForCBInfo() {
    return new byte[] { 0x30, 0x34,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x70, (byte) 0xf0, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0xa3, 0x17, (byte) 0x80,
        0x01, 0x15, (byte) 0xa1, 0x03, (byte) 0x83, 0x01, 0x12, (byte) 0x82,
        0x01, 0x05, (byte) 0x83, 0x04, 0x31, 0x32, 0x33, 0x30,
        (byte) 0x84, 0x01, 0x04, (byte) 0x85, 0x01, 0x01, (byte) 0x86, 0x00,
        (byte) 0x89, 0x02, 0x00, (byte) 0x80
    };
  }

  private byte[] getEncodedDataWithModReqForCSI() {
    return new byte[] { 0x30, 0x29,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x60, (byte) 0xf6, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0xa4, 0x0c, (byte) 0x80,
        0x01, 0x04, (byte) 0x81, 0x01, 0x01, (byte) 0x82, 0x01, 0x01,
        (byte) 0x84, 0x01, 0x02, (byte) 0x86, 0x00, (byte) 0x89, 0x02, 0x00,
        (byte) 0x80
    };
  }

  private byte[] getEncodedDataWithModReqForODB() {
    return new byte[] { 0x30, 0x2d,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x60, (byte) 0xf6, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0xa7,
        0x10, (byte) 0xa0, 0x0b, 0x03, 0x05, 0x03, 0x4a, (byte) 0xd5,
        0x55, 0x50, 0x03, 0x02, 0x04, (byte) 0x80, (byte) 0x81, 0x01,
        0x01, (byte) 0x89, 0x02, 0x00, (byte) 0x80
    };
  }

  private byte[] getEncodedDataWithModReqForIPSMGWData() {
    return new byte[] { 0x30, 0x73,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0xa8,
        0x56, (byte) 0x80, 0x01, 0x01, (byte) 0xa2, 0x51, (byte) 0x80, 0x2c,
        0x6d, 0x6d, 0x65, 0x2e, 0x32, 0x30, 0x2e, 0x6d,
        0x61, 0x67, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d,
        0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63,
        0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
        0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
        0x2e, 0x6f, 0x72, 0x67, (byte) 0x81, 0x21, 0x65, 0x70,
        0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x31,
        0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
        0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
        0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x89,
        0x02, 0x00, (byte) 0x80
    };
  }

  private byte[] getEncodedDataWithModReqForCSG() {
    return new byte[] { 0x30, 0x20,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xaa, 0x03, (byte) 0x80, 0x01, 0x01
    };
  }

  private byte[] getEncodedDataWithModReqForCWData() {
    return new byte[] { 0x30, 0x28,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xab, 0x0b, (byte) 0xa0, 0x03, (byte) 0x82,
        0x01, 0x28, (byte) 0x81, 0x01, 0x05, (byte) 0x82, 0x01, 0x01
    };
  }

  private byte[] getEncodedDataWithModReqForCLIPData() {
    return new byte[] { 0x30, 0x26,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xac, 0x09, (byte) 0x80, 0x01, 0x05,
        (byte) 0x81, 0x01, 0x00, (byte) 0x82, 0x01, 0x01
    };
  }

  private byte[] getEncodedDataWithModReqForCLIRData() {
    return new byte[] { 0x30, 0x26,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xad, 0x09, (byte) 0x80, 0x01, 0x05,
        (byte) 0x81, 0x01, 0x01, (byte) 0x82, 0x01, 0x01
    };
  }

  private byte[] getEncodedDataWithModReqForHOLDData() {
    return new byte[] { 0x30, 0x23,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x51, (byte) 0xf8, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xae, 0x06, (byte) 0x80, 0x01, 0x05,
        (byte) 0x81, 0x01, 0x01
    };
  }

  private byte[] getEncodedDataWithModReqForECTData() {
    return new byte[] { 0x30, 0x23,
        (byte) 0xa0, 0x0a, (byte) 0x80, 0x08, 0x09, 0x41, 0x50, 0x01,
        0x65, 0x08, 0x61, (byte) 0xf1, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94,
        0x71, 0x01, (byte) 0x94, 0x00, 0x32, (byte) 0x86, 0x00, (byte) 0x89,
        0x02, 0x00, (byte) 0x80, (byte) 0xaf, 0x06, (byte) 0x80, 0x01, 0x05,
        (byte) 0x81, 0x01, 0x01
    };
  }

  @Test(groups = { "functional.decode", "subscriberInformation" })
  public void testDecode() throws Exception {

    /** Test 1 with modificationRequestFor-CF-Info **/
    byte[] rawData1 = getEncodedDataWithModReqForCFInfo();
    AsnInputStream asn = new AsnInputStream(rawData1);

    int tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq1 = new AnyTimeModificationRequestImpl();
    atmReq1.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    SubscriberIdentity subscriberIdentity = atmReq1.getSubscriberIdentity();
    ISDNAddressString gsmSCFAddress = atmReq1.getGsmSCFAddress();
    ModificationRequestForCFInfo modificationRequestForCFInfo = atmReq1.getModificationRequestForCFInfo();
    ModificationRequestForCBInfo modificationRequestForCBInfo = atmReq1.getModificationRequestForCBInfo();
    ModificationRequestForCSI modificationRequestForCSI = atmReq1.getModificationRequestForCSI();
    MAPExtensionContainer extensionContainer = atmReq1.getExtensionContainer();
    boolean longFTNSupported = atmReq1.isLongFTNSupported();
    ModificationRequestForODBData modificationRequestForODBData = atmReq1.getModificationRequestForODBData();
    RequestedServingNode activationRequestForUEReachability = atmReq1.getActivationRequestForUEReachability();
    ModificationRequestForIPSMGWData modificationRequestForIPSMGWData = atmReq1.getModificationRequestForIPSMGWData();
    ModificationRequestForCSG modificationRequestForCSG = atmReq1.getModificationRequestForCSG();
    ModificationRequestForCWInfo modificationRequestForCWData = atmReq1.getModificationRequestForCWData();
    ModificationRequestForCLIPInfo modificationRequestForCLIPData = atmReq1.getModificationRequestForCLIPData();
    ModificationRequestForCLIRInfo modificationRequestForCLIRData= atmReq1.getModificationRequestForCLIRData();
    ModificationRequestForCHInfo modificationRequestForHoldData = atmReq1.getModificationRequestForHOLDData();
    ModificationRequestForECTInfo modificationRequestForECTData = atmReq1.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CF-Info
    // Component: invoke (1)
    //    invoke
    //        invokeID: 0
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        subscriberIdentity: imsi (0)
    //            IMSI: 901405105680066
    //            [Association IMSI: 901405105680066]
    //        gsmSCF-Address: 91947101940032
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710490023
    //        modificationRequestFor-CF-Info
    //            ss-Code: uus1 - UUS1 user-to-user signalling (129)
    //            basicService: ext-BearerService (2)
    //                ext-BearerService: allSpeechFollowedByDataCDA (64)
    //            ss-Status: 05
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..0. = R bit: Not registered
    //            .... ...1 = A bit: Active
    //            forwardedToNumber: 9188225801652854f1
    //                1... .... = Extension: No Extension
    //                .001 .... = Nature of number: International Number (0x1)
    //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                E.164 number (MSISDN): 882285105682451
    //            forwardedToSubaddress: 0205
    //            noReplyConditionTime: 17
    //            modifyNotificationToCSE: activate (1)
    //        longFTN-Supported
    //        Padding: 0
    //        activationRequestForUE-reachability: 80
    //            1... .... = mmeAndSgsn: True
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680066");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertEquals(modificationRequestForCFInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.uus1);
    assertEquals(modificationRequestForCFInfo.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allSpeechFollowedByDataCDA);
    assertNull(modificationRequestForCFInfo.getBasicService().getExtTeleservice());
    assertFalse(modificationRequestForCFInfo.getSsStatus().getBitQ());
    assertTrue(modificationRequestForCFInfo.getSsStatus().getBitP());
    assertFalse(modificationRequestForCFInfo.getSsStatus().getBitR());
    assertTrue(modificationRequestForCFInfo.getSsStatus().getBitA());
    assertFalse(modificationRequestForCFInfo.getForwardedToNumber().isExtension());
    assertEquals(modificationRequestForCFInfo.getForwardedToNumber().getAddressNature(), AddressNature.international_number);
    assertEquals(modificationRequestForCFInfo.getForwardedToNumber().getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(modificationRequestForCFInfo.getForwardedToNumber().getAddress(), "882285105682451");
    assertEquals(modificationRequestForCFInfo.getForwardedToSubaddress().getData(), new byte[] {2, 5});
    assertEquals(modificationRequestForCFInfo.getNoReplyConditionTime().intValue(), 17);
    assertEquals(modificationRequestForCFInfo.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 2 with modificationRequestFor-CB-Info **/
    byte[] rawData2 = getEncodedDataWithModReqForCBInfo();
    asn = new AsnInputStream(rawData2);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq2 = new AnyTimeModificationRequestImpl();
    atmReq2.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq2.getSubscriberIdentity();
    gsmSCFAddress = atmReq2.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq2.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq2.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq2.getModificationRequestForCSI();
    extensionContainer = atmReq2.getExtensionContainer();
    longFTNSupported = atmReq2.isLongFTNSupported();
    modificationRequestForODBData = atmReq2.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq2.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq2.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq2.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq2.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq2.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq2.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq2.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq2.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CB-Info
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680070
    //        [Association IMSI: 901405105680070]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    modificationRequestFor-CB-Info
    //        ss-Code: mci - malicious call identification (21)
    //        basicService: ext-Teleservice (3)
    //            ext-Teleservice: emergencyCalls (18)
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        password: 1230
    //        wrongPasswordAttemptsCounter: 4
    //        modifyNotificationToCSE: activate (1)
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680070");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertEquals(modificationRequestForCBInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.mci);
    assertNull(modificationRequestForCBInfo.getBasicService().getExtBearerService());
    assertEquals(modificationRequestForCBInfo.getBasicService().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.emergencyCalls);
    assertFalse(modificationRequestForCBInfo.getSsStatus().getBitQ());
    assertTrue(modificationRequestForCBInfo.getSsStatus().getBitP());
    assertFalse(modificationRequestForCBInfo.getSsStatus().getBitR());
    assertTrue(modificationRequestForCBInfo.getSsStatus().getBitA());
    assertEquals(modificationRequestForCBInfo.getPassword().getData(), "1230");
    assertEquals(modificationRequestForCBInfo.getWrongPasswordAttemptsCounter().intValue(), 4);
    assertEquals(modificationRequestForCBInfo.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 3 with modificationRequestFor-CSI **/
    byte[] rawData3 = getEncodedDataWithModReqForCSI();
    asn = new AsnInputStream(rawData3);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq3 = new AnyTimeModificationRequestImpl();
    atmReq3.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq3.getSubscriberIdentity();
    gsmSCFAddress = atmReq3.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq3.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq3.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq3.getModificationRequestForCSI();
    extensionContainer = atmReq3.getExtensionContainer();
    longFTNSupported = atmReq3.isLongFTNSupported();
    modificationRequestForODBData = atmReq3.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq3.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq3.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq3.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq3.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq3.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq3.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq3.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq3.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CSI
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680066
    //        [Association IMSI: 901405105680066]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    modificationRequestFor-CSI
    //        requestedCamel-SubscriptionInfo: gprs-CSI (4)
    //        modifyNotificationToCSE: activate (1)
    //        modifyCSI-State: activate (1)
    //        additionalRequestedCAMEL-SubscriptionInfo: o-IM-CSI (2)
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680066");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertEquals(modificationRequestForCSI.getRequestedCamelSubscriptionInfo(), RequestedCAMELSubscriptionInfo.gprsCSI);
    assertEquals(modificationRequestForCSI.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertEquals(modificationRequestForCSI.getModifyCSIState(), ModificationInstruction.activate);
    assertEquals(modificationRequestForCSI.getAdditionalRequestedCamelSubscriptionInfo(), AdditionalRequestedCAMELSubscriptionInfo.oImCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 4 with modificationRequestFor-CSI **/
    byte[] rawData4 = getEncodedDataWithModReqForODB();
    asn = new AsnInputStream(rawData4);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq4 = new AnyTimeModificationRequestImpl();
    atmReq4.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq4.getSubscriberIdentity();
    gsmSCFAddress = atmReq4.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq4.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq4.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq4.getModificationRequestForCSI();
    extensionContainer = atmReq4.getExtensionContainer();
    longFTNSupported = atmReq4.isLongFTNSupported();
    modificationRequestForODBData = atmReq4.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq4.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq4.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq4.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq4.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq4.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq4.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq4.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq4.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-ODB-data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680066
    //        [Association IMSI: 901405105680066]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    modificationRequestFor-ODB-data
    //        odb-data
    //            Padding: 3
    //            odb-GeneralData: 4ad55550
    //                0... .... = allOG-CallsBarred: False
    //                .1.. .... = internationalOGCallsBarred: True
    //                ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                ...0 .... = premiumRateInformationOGCallsBarred: False
    //                .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                .... .0.. = ss-AccessBarred: False
    //                .... ..1. = interzonalOGCallsBarred: True
    //                .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
    //                .1.. .... = allECT-Barred: True
    //                ..0. .... = chargeableECT-Barred: False
    //                ...1 .... = internationalECT-Barred: True
    //                .... 0... = interzonalECT-Barred: False
    //                .... .1.. = doublyChargeableECT-Barred: True
    //                .... ..0. = multipleECT-Barred: False
    //                .... ...1 = allPacketOrientedServicesBarred: True
    //                0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                ...1 .... = allIC-CallsBarred: True
    //                .... 0... = roamingOutsidePLMNIC-CallsBarred: False
    //                .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
    //                .... ..0. = roamingOutsidePLMN-Barred: False
    //                .... ...1 = roamingOutsidePLMN-CountryBarred: True
    //                0... .... = registrationAllCF-Barred: False
    //                .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                ..0. .... = registrationInterzonalCF-Barred: False
    //                ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                .... 0... = registrationInternationalCF-Barred: False
    //            Padding: 4
    //            odb-HPLMN-Data: 80
    //                1... .... = plmn-SpecificBarringType1: True
    //                .0.. .... = plmn-SpecificBarringType2: False
    //                ..0. .... = plmn-SpecificBarringType3: False
    //                ...0 .... = plmn-SpecificBarringType4: False
    //        modifyNotificationToCSE: activate (1)
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680066");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNotNull(modificationRequestForODBData.getOdbData());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getAllOGCallsBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getInternationalOGCallsBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getInternationalOGCallsNotToHPLMNCountryBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getPremiumRateInformationOGCallsBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getPremiumRateEntertainmentOGCallsBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getSsAccessBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getInterzonalOGCallsBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getInterzonalOGCallsNotToHPLMNCountryBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getAllECTBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getChargeableECTBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getInternationalECTBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getInterzonalECTBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getDoublyChargeableECTBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getMultipleECTBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getAllPacketOrientedServicesBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamerAccessToHPLMNAPBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamerAccessToVPLMNAPBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getAllICCallsBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICCallsBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamingOutsidePLMNICountryICCallsBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamingOutsidePLMNBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getRoamingOutsidePLMNCountryBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRegistrationAllCFBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getRegistrationCFNotToHPLMNBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRegistrationInterzonalCFBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getODBGeneralData().getRegistrationInterzonalCFNotToHPLMNBarred());
    assertFalse(modificationRequestForODBData.getOdbData().getODBGeneralData().getRegistrationInternationalCFBarred());
    assertTrue(modificationRequestForODBData.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType1());
    assertFalse(modificationRequestForODBData.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType2());
    assertFalse(modificationRequestForODBData.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType3());
    assertFalse(modificationRequestForODBData.getOdbData().getOdbHplmnData().getPlmnSpecificBarringType4());
    assertNull(modificationRequestForODBData.getExtensionContainer());
    assertEquals(modificationRequestForODBData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 5 with modificationRequestFor-IP-SM-GW-Data **/
    byte[] rawData5 = getEncodedDataWithModReqForIPSMGWData();
    asn = new AsnInputStream(rawData5);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq5 = new AnyTimeModificationRequestImpl();
    atmReq5.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq5.getSubscriberIdentity();
    gsmSCFAddress = atmReq5.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq5.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq5.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq5.getModificationRequestForCSI();
    extensionContainer = atmReq5.getExtensionContainer();
    longFTNSupported = atmReq5.isLongFTNSupported();
    modificationRequestForODBData = atmReq5.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq5.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq5.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq5.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq5.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq5.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq5.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq5.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq5.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-IP-SM-GW-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    modificationRequestFor-IP-SM-GW-Data
    //        modifyRegistrationStatus: activate (1)
    //        ip-sm-gw-DiameterAddress
    //            diameter-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
    //            diameter-Realm: epc.mnc001.mcc748.3gppnetwork.org
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertEquals(modificationRequestForIPSMGWData.getModifyRegistrationStatus(), ModificationInstruction.activate);
    assertEquals(modificationRequestForIPSMGWData.getIpSmGwDiameterAddress().getDiameterName(), new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
    assertEquals(modificationRequestForIPSMGWData.getIpSmGwDiameterAddress().getDiameterRealm(), new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
    assertNull(modificationRequestForIPSMGWData.getExtensionContainer());
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 6 with modificationRequestFor-CSG **/
    byte[] rawData6 = getEncodedDataWithModReqForCSG();
    asn = new AsnInputStream(rawData6);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq6 = new AnyTimeModificationRequestImpl();
    atmReq6.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq6.getSubscriberIdentity();
    gsmSCFAddress = atmReq6.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq6.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq6.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq6.getModificationRequestForCSI();
    extensionContainer = atmReq6.getExtensionContainer();
    longFTNSupported = atmReq6.isLongFTNSupported();
    modificationRequestForODBData = atmReq6.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq6.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq6.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq6.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq6.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq6.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq6.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq6.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq6.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CSG
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CSG
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertEquals(modificationRequestForCSG.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCSG.getExtensionContainer());
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 7 with modificationRequestFor-CW-Data **/
    byte[] rawData7 = getEncodedDataWithModReqForCWData();
    asn = new AsnInputStream(rawData7);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq7 = new AnyTimeModificationRequestImpl();
    atmReq7.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq7.getSubscriberIdentity();
    gsmSCFAddress = atmReq7.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq7.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq7.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq7.getModificationRequestForCSI();
    extensionContainer = atmReq7.getExtensionContainer();
    longFTNSupported = atmReq7.isLongFTNSupported();
    modificationRequestForODBData = atmReq7.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq7.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq7.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq7.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq7.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq7.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq7.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq7.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq7.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CW-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CW-Data
    //        basicService: ext-BearerService (2)
    //            ext-BearerService: allDataPDS-Services (40)
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertEquals(modificationRequestForCWData.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allDataPDS_Services);
    assertNull(modificationRequestForCWData.getBasicService().getExtTeleservice());
    assertFalse(modificationRequestForCWData.getSsStatus().getBitQ());
    assertTrue(modificationRequestForCWData.getSsStatus().getBitP());
    assertFalse(modificationRequestForCWData.getSsStatus().getBitR());
    assertTrue(modificationRequestForCWData.getSsStatus().getBitA());
    assertEquals(modificationRequestForCWData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCWData.getExtensionContainer());
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 8 with modificationRequestFor-CLIP-Data **/
    byte[] rawData8 = getEncodedDataWithModReqForCLIPData();
    asn = new AsnInputStream(rawData8);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq8 = new AnyTimeModificationRequestImpl();
    atmReq8.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq8.getSubscriberIdentity();
    gsmSCFAddress = atmReq8.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq8.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq8.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq8.getModificationRequestForCSI();
    extensionContainer = atmReq8.getExtensionContainer();
    longFTNSupported = atmReq8.isLongFTNSupported();
    modificationRequestForODBData = atmReq8.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq8.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq8.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq8.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq8.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq8.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq8.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq8.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq8.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CLIP-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CLIP-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        overrideCategory: overrideEnabled (0)
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertFalse(modificationRequestForCLIPData.getSsStatus().getBitQ());
    assertTrue(modificationRequestForCLIPData.getSsStatus().getBitP());
    assertFalse(modificationRequestForCLIPData.getSsStatus().getBitR());
    assertTrue(modificationRequestForCLIPData.getSsStatus().getBitA());
    assertEquals(modificationRequestForCLIPData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCLIPData.getExtensionContainer());
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 9 with modificationRequestFor-CLIR-Data **/
    byte[] rawData9 = getEncodedDataWithModReqForCLIRData();
    asn = new AsnInputStream(rawData9);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq9 = new AnyTimeModificationRequestImpl();
    atmReq9.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq9.getSubscriberIdentity();
    gsmSCFAddress = atmReq9.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq9.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq9.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq9.getModificationRequestForCSI();
    extensionContainer = atmReq9.getExtensionContainer();
    longFTNSupported = atmReq9.isLongFTNSupported();
    modificationRequestForODBData = atmReq9.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq9.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq9.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq9.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq9.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq9.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq9.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq9.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq9.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-CLIR-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CLIR-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        cliRestrictionOption: temporaryDefaultRestricted (1)
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertFalse(modificationRequestForCLIRData.getSsStatus().getBitQ());
    assertTrue(modificationRequestForCLIRData.getSsStatus().getBitP());
    assertFalse(modificationRequestForCLIRData.getSsStatus().getBitR());
    assertTrue(modificationRequestForCLIRData.getSsStatus().getBitA());
    assertEquals(modificationRequestForCLIRData.getCliRestrictionOption(), CliRestrictionOption.temporaryDefaultRestricted);
    assertEquals(modificationRequestForCLIRData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForCLIRData.getExtensionContainer());
    assertNull(modificationRequestForHoldData);
    assertNull(modificationRequestForECTData);

    /** Test 10 with modificationRequestFor-HOLD-Data **/
    byte[] rawData10 = getEncodedDataWithModReqForHOLDData();
    asn = new AsnInputStream(rawData10);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq10 = new AnyTimeModificationRequestImpl();
    atmReq10.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq10.getSubscriberIdentity();
    gsmSCFAddress = atmReq10.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq10.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq10.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq10.getModificationRequestForCSI();
    extensionContainer = atmReq10.getExtensionContainer();
    longFTNSupported = atmReq10.isLongFTNSupported();
    modificationRequestForODBData = atmReq10.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq10.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq10.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq10.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq10.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq10.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq10.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq10.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq10.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-HOLD-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-HOLD-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680158");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertFalse(modificationRequestForHoldData.getSsStatus().getBitQ());
    assertTrue(modificationRequestForHoldData.getSsStatus().getBitP());
    assertFalse(modificationRequestForHoldData.getSsStatus().getBitR());
    assertTrue(modificationRequestForHoldData.getSsStatus().getBitA());
    assertEquals(modificationRequestForHoldData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForHoldData.getExtensionContainer());
    assertNull(modificationRequestForECTData);

    /** Test 11 with modificationRequestFor-ECT-Data **/
    byte[] rawData11 = getEncodedDataWithModReqForECTData();
    asn = new AsnInputStream(rawData11);

    tag = asn.readTag();
    AnyTimeModificationRequestImpl atmReq11 = new AnyTimeModificationRequestImpl();
    atmReq11.decodeAll(asn);

    assertEquals(tag, Tag.SEQUENCE);
    assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

    subscriberIdentity = atmReq11.getSubscriberIdentity();
    gsmSCFAddress = atmReq11.getGsmSCFAddress();
    modificationRequestForCFInfo = atmReq11.getModificationRequestForCFInfo();
    modificationRequestForCBInfo = atmReq11.getModificationRequestForCBInfo();
    modificationRequestForCSI = atmReq11.getModificationRequestForCSI();
    extensionContainer = atmReq11.getExtensionContainer();
    longFTNSupported = atmReq11.isLongFTNSupported();
    modificationRequestForODBData = atmReq11.getModificationRequestForODBData();
    activationRequestForUEReachability = atmReq11.getActivationRequestForUEReachability();
    modificationRequestForIPSMGWData = atmReq11.getModificationRequestForIPSMGWData();
    modificationRequestForCSG = atmReq11.getModificationRequestForCSG();
    modificationRequestForCWData = atmReq11.getModificationRequestForCWData();
    modificationRequestForCLIPData = atmReq11.getModificationRequestForCLIPData();
    modificationRequestForCLIRData= atmReq11.getModificationRequestForCLIRData();
    modificationRequestForHoldData = atmReq11.getModificationRequestForHOLDData();
    modificationRequestForECTData = atmReq11.getModificationRequestForECTData();

    // Wireshark trace example from MAP load test with modificationRequestFor-ECT-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680161
    //        [Association IMSI: 901405105680161]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-ECT-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    assertEquals(subscriberIdentity.getIMSI().getData(), "901405105680161");
    assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
    assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
    assertEquals(gsmSCFAddress.getAddress(), "491710490023");
    assertNull(modificationRequestForCFInfo);
    assertNull(modificationRequestForCBInfo);
    assertNull(modificationRequestForCSI);
    assertNull(extensionContainer);
    assertTrue(longFTNSupported);
    assertNull(modificationRequestForODBData);
    assertTrue(activationRequestForUEReachability.getMmeAndSgsn());
    assertNull(modificationRequestForIPSMGWData);
    assertNull(modificationRequestForCSG);
    assertNull(modificationRequestForCWData);
    assertNull(modificationRequestForCLIPData);
    assertNull(modificationRequestForCLIRData);
    assertNull(modificationRequestForHoldData);
    assertFalse(modificationRequestForECTData.getSsStatus().getBitQ());
    assertTrue(modificationRequestForECTData.getSsStatus().getBitP());
    assertFalse(modificationRequestForECTData.getSsStatus().getBitR());
    assertTrue(modificationRequestForECTData.getSsStatus().getBitA());
    assertEquals(modificationRequestForECTData.getModifyNotificationToCSE(), ModificationInstruction.activate);
    assertNull(modificationRequestForECTData.getExtensionContainer());
  }


  @Test(groups = { "functional.encode", "subscriberInformation" })
  public void testEncode() throws Exception {

    /** Test 1 with modificationRequestFor-CF-Info **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CF-Info
    // Component: invoke (1)
    //    invoke
    //        invokeID: 0
    //        opCode: localValue (0)
    //            localValue: anyTimeModification (65)
    //        subscriberIdentity: imsi (0)
    //            IMSI: 901405105680066
    //            [Association IMSI: 901405105680066]
    //        gsmSCF-Address: 91947101940032
    //            1... .... = Extension: No Extension
    //            .001 .... = Nature of number: International Number (0x1)
    //            .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //            E.164 number (MSISDN): 491710490023
    //        modificationRequestFor-CF-Info
    //            ss-Code: uus1 - UUS1 user-to-user signalling (129)
    //            basicService: ext-BearerService (2)
    //                ext-BearerService: allSpeechFollowedByDataCDA (64)
    //            ss-Status: 05
    //            0000 .... = Unused: 0x0
    //            .... 0... = Q bit: Operative
    //            .... .1.. = P bit: Provisioned
    //            .... ..0. = R bit: Not registered
    //            .... ...1 = A bit: Active
    //            forwardedToNumber: 9188225801652854f1
    //                1... .... = Extension: No Extension
    //                .001 .... = Nature of number: International Number (0x1)
    //                .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //                E.164 number (MSISDN): 882285105682451
    //            forwardedToSubaddress: 0205
    //            noReplyConditionTime: 17
    //            modifyNotificationToCSE: activate (1)
    //        longFTN-Supported
    //        Padding: 0
    //        activationRequestForUE-reachability: 80
    //            1... .... = mmeAndSgsn: True
    SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl("901405105680066"));
    ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
        "491710490023");
    SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.uus1);
    ExtBasicServiceCode basicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allSpeechFollowedByDataCDA));
    ExtSSStatus ssStatus = new ExtSSStatusImpl(false, true, false, true);
    AddressString forwardedToNumber = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
    ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(new byte[] { 2, 5 });
    Integer noReplyConditionTime = 17;
    ModificationInstruction modifyNotificationToCSE = ModificationInstruction.activate;
    ModificationRequestForCFInfo modificationRequestForCFInfo = new ModificationRequestForCFInfoImpl(ssCode, basicServiceCode, ssStatus, forwardedToNumber,
        forwardedToSubaddress, noReplyConditionTime, modifyNotificationToCSE, null);
    ModificationRequestForCBInfo modificationRequestForCBInfo;
    ModificationRequestForCSI modificationRequestForCSI;
    boolean longFTNSupported = true;
    ModificationRequestForODBData modificationRequestForODBData;
    RequestedServingNode activationRequestForUEReachability = new RequestedServingNodeImpl(true);
    ModificationRequestForIPSMGWData modificationRequestForIPSMGWData;
    ModificationRequestForCSG modificationRequestForCSG;
    ModificationRequestForCWInfo modificationRequestForCWData;
    ModificationRequestForCLIPInfo modificationRequestForCLIPData;
    ModificationRequestForCLIRInfo modificationRequestForCLIRData;
    ModificationRequestForCHInfo modificationRequestForHOLDData;
    ModificationRequestForECTInfo modificationRequestForECTData;

    AnyTimeModificationRequestImpl atmReq1 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        null, null, null, longFTNSupported, null,
        null, activationRequestForUEReachability, null, null,
        null, null, null, null);

    AsnOutputStream asnOS = new AsnOutputStream();
    atmReq1.encodeAll(asnOS);

    byte[] encodedData = asnOS.toByteArray();
    byte[] rawData = getEncodedDataWithModReqForCFInfo();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 2 with modificationRequestFor-CB-Info **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CB-Info
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680070
    //        [Association IMSI: 901405105680070]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    modificationRequestFor-CB-Info
    //        ss-Code: mci - malicious call identification (21)
    //        basicService: ext-Teleservice (3)
    //            ext-Teleservice: emergencyCalls (18)
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        password: 1230
    //        wrongPasswordAttemptsCounter: 4
    //        modifyNotificationToCSE: activate (1)
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl("901405105680070"));
    modificationRequestForCFInfo = null;
    ssCode = new SSCodeImpl(SupplementaryCodeValue.mci);
    basicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.emergencyCalls));
    Password password = new PasswordImpl("1230");
    Integer wrongPasswordAttemptsCounter = 4;
    modificationRequestForCBInfo = new ModificationRequestForCBInfoImpl(ssCode, basicServiceCode, ssStatus, password,
        wrongPasswordAttemptsCounter, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq2 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, null, null, longFTNSupported, null,
        null, activationRequestForUEReachability, null, null,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq2.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCBInfo();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 3 with modificationRequestFor-CB-Info **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CSI
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680066
    //        [Association IMSI: 901405105680066]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    modificationRequestFor-CSI
    //        requestedCamel-SubscriptionInfo: gprs-CSI (4)
    //        modifyNotificationToCSE: activate (1)
    //        modifyCSI-State: activate (1)
    //        additionalRequestedCAMEL-SubscriptionInfo: o-IM-CSI (2)
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl("901405105680066"));
    modificationRequestForCBInfo = null;
    RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo = RequestedCAMELSubscriptionInfo.gprsCSI;
    ModificationInstruction modifyCSIState = ModificationInstruction.getInstance(1);
    AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo = AdditionalRequestedCAMELSubscriptionInfo.oImCSI;
    modificationRequestForCSI = new ModificationRequestForCSIImpl(requestedCAMELSubscriptionInfo, modifyNotificationToCSE,
        modifyCSIState, null, additionalRequestedCAMELSubscriptionInfo);
    AnyTimeModificationRequestImpl atmReq3 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, null,
        null, activationRequestForUEReachability, null, null,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq3.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCSI();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 4 with modificationRequestFor-CSI **/
    // Wireshark trace example from MAP load test with modificationRequestFor-ODB-data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680066
    //        [Association IMSI: 901405105680066]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    modificationRequestFor-ODB-data
    //        odb-data
    //            Padding: 3
    //            odb-GeneralData: 4ad55550
    //                0... .... = allOG-CallsBarred: False
    //                .1.. .... = internationalOGCallsBarred: True
    //                ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
    //                ...0 .... = premiumRateInformationOGCallsBarred: False
    //                .... 1... = premiumRateEntertainementOGCallsBarred: True
    //                .... .0.. = ss-AccessBarred: False
    //                .... ..1. = interzonalOGCallsBarred: True
    //                .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
    //                1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
    //                .1.. .... = allECT-Barred: True
    //                ..0. .... = chargeableECT-Barred: False
    //                ...1 .... = internationalECT-Barred: True
    //                .... 0... = interzonalECT-Barred: False
    //                .... .1.. = doublyChargeableECT-Barred: True
    //                .... ..0. = multipleECT-Barred: False
    //                .... ...1 = allPacketOrientedServicesBarred: True
    //                0... .... = roamerAccessToHPLMN-AP-Barred: False
    //                .1.. .... = roamerAccessToVPLMN-AP-Barred: True
    //                ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
    //                ...1 .... = allIC-CallsBarred: True
    //                .... 0... = roamingOutsidePLMNIC-CallsBarred: False
    //                .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
    //                .... ..0. = roamingOutsidePLMN-Barred: False
    //                .... ...1 = roamingOutsidePLMN-CountryBarred: True
    //                0... .... = registrationAllCF-Barred: False
    //                .1.. .... = registrationCFNotToHPLMN-Barred: True
    //                ..0. .... = registrationInterzonalCF-Barred: False
    //                ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
    //                .... 0... = registrationInternationalCF-Barred: False
    //            Padding: 4
    //            odb-HPLMN-Data: 80
    //                1... .... = plmn-SpecificBarringType1: True
    //                .0.. .... = plmn-SpecificBarringType2: False
    //                ..0. .... = plmn-SpecificBarringType3: False
    //                ...0 .... = plmn-SpecificBarringType4: False
    //        modifyNotificationToCSE: activate (1)
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    modificationRequestForCSI =  null;
    ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, false, true, false, true, false, true, true, false,
        true, false, true, false, true, false, true, false, true, false, true, false, true, false,
        true, false, true, false);
    ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(true, false, false, false);
    ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
    modificationRequestForODBData = new ModificationRequestForODBDataImpl(odbData, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq4 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        null, activationRequestForUEReachability, null, null,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq4.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForODB();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 5 with modificationRequestFor-IP-SM-GW-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-IP-SM-GW-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    modificationRequestFor-IP-SM-GW-Data
    //        modifyRegistrationStatus: activate (1)
    //        ip-sm-gw-DiameterAddress
    //            diameter-Name: mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org
    //            diameter-Realm: epc.mnc001.mcc748.3gppnetwork.org
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl("901405105680158"));
    modificationRequestForODBData = null;
    ModificationInstruction modifyRegistrationStatus = ModificationInstruction.activate;
    DiameterIdentity diameterName = new DiameterIdentityImpl("mme.20.mag.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
    DiameterIdentity diameterRealm = new DiameterIdentityImpl("epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
    NetworkNodeDiameterAddress networkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl(diameterName, diameterRealm);
    modificationRequestForIPSMGWData = new ModificationRequestForIPSMGWDataImpl(modifyRegistrationStatus, null, networkNodeDiameterAddress);

    AnyTimeModificationRequestImpl atmReq5 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, null, null,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq5.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForIPSMGWData();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 6 with modificationRequestFor-CSG **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CSG
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CSG
    //        modifyNotificationToCSE: activate (1)
    modificationRequestForIPSMGWData = null;
    modificationRequestForCSG = new ModificationRequestForCSGImpl(modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq6 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, null,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq6.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCSG();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 7 with modificationRequestFor-CW-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CW-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CW-Data
    //        basicService: ext-BearerService (2)
    //            ext-BearerService: allDataPDS-Services (40)
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    modificationRequestForCSG = null;
    basicServiceCode = new ExtBasicServiceCodeImpl(new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allDataPDS_Services));
    ssStatus = new ExtSSStatusImpl(false, true, false, true);
    modificationRequestForCWData = new ModificationRequestForCWInfoImpl(basicServiceCode, ssStatus, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq7 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData,
        null, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq7.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCWData();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 8 with modificationRequestFor-CLIP-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CLIP-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CLIP-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        overrideCategory: overrideEnabled (0)
    //        modifyNotificationToCSE: activate (1)
    modificationRequestForCWData = null;
    OverrideCategory overrideCategory = OverrideCategory.overrideEnabled;
    modificationRequestForCLIPData = new ModificationRequestForCLIPInfoImpl(ssStatus, overrideCategory, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq8 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData,
        modificationRequestForCLIPData, null, null, null);

    asnOS = new AsnOutputStream();
    atmReq8.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCLIPData();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 9 with modificationRequestFor-CLIR-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-CLIR-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-CLIR-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        cliRestrictionOption: temporaryDefaultRestricted (1)
    //        modifyNotificationToCSE: activate (1)
    modificationRequestForCLIPData = null;
    CliRestrictionOption cliRestrictionOption = CliRestrictionOption.temporaryDefaultRestricted;
    modificationRequestForCLIRData = new ModificationRequestForCLIRInfoImpl(ssStatus, cliRestrictionOption, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq9 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData,
        modificationRequestForCLIPData, modificationRequestForCLIRData, null, null);

    asnOS = new AsnOutputStream();
    atmReq9.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForCLIRData();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 10 with modificationRequestFor-HOLD-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-HOLD-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680158
    //        [Association IMSI: 901405105680158]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-HOLD-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    modificationRequestForCLIRData = null;
    modificationRequestForHOLDData = new ModificationRequestForCHInfoImpl(ssStatus, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq10 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData,
        modificationRequestForCLIPData, modificationRequestForCLIRData, modificationRequestForHOLDData, null);

    asnOS = new AsnOutputStream();
    atmReq10.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForHOLDData();
    assertTrue(Arrays.equals(rawData, encodedData));

    /** Test 11 with modificationRequestFor-ECT-Data **/
    // Wireshark trace example from MAP load test with modificationRequestFor-ECT-Data
    // invoke
    //    invokeID: 0
    //    opCode: localValue (0)
    //        localValue: anyTimeModification (65)
    //    subscriberIdentity: imsi (0)
    //        IMSI: 901405105680161
    //        [Association IMSI: 901405105680161]
    //    gsmSCF-Address: 91947101940032
    //        1... .... = Extension: No Extension
    //        .001 .... = Nature of number: International Number (0x1)
    //        .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
    //        E.164 number (MSISDN): 491710490023
    //    longFTN-Supported
    //    Padding: 0
    //    activationRequestForUE-reachability: 80
    //        1... .... = mmeAndSgsn: True
    //    modificationRequestFor-ECT-Data
    //        ss-Status: 05
    //        0000 .... = Unused: 0x0
    //        .... 0... = Q bit: Operative
    //        .... .1.. = P bit: Provisioned
    //        .... ..0. = R bit: Not registered
    //        .... ...1 = A bit: Active
    //        modifyNotificationToCSE: activate (1)
    subscriberIdentity = new SubscriberIdentityImpl(new IMSIImpl("901405105680161"));
    modificationRequestForHOLDData = null;
    modificationRequestForECTData = new ModificationRequestForECTInfoImpl(ssStatus, modifyNotificationToCSE, null);
    AnyTimeModificationRequestImpl atmReq11 = new AnyTimeModificationRequestImpl(subscriberIdentity, gsmSCFAddress, modificationRequestForCFInfo,
        modificationRequestForCBInfo, modificationRequestForCSI, null, longFTNSupported, modificationRequestForODBData,
        modificationRequestForIPSMGWData, activationRequestForUEReachability, modificationRequestForCSG, modificationRequestForCWData,
        modificationRequestForCLIPData, modificationRequestForCLIRData, modificationRequestForHOLDData, modificationRequestForECTData);

    asnOS = new AsnOutputStream();
    atmReq11.encodeAll(asnOS);

    encodedData = asnOS.toByteArray();
    rawData = getEncodedDataWithModReqForECTData();
    assertTrue(Arrays.equals(rawData, encodedData));
  }
}
