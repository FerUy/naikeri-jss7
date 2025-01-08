package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.restcomm.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.sms.MoForwardShortMessageResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ValidityPeriodImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class MoForwardShortMessageResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 48, 4, 5, 11, 22, 33, 44, 55, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataMOSmsLoadTest() {
        return new byte[] { 0x30, 0x1c,
                0x04, 0x1a, 0x35, (byte) 0xbb, 0x0b, (byte) 0x91,
                (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39, (byte) 0xf7, 0x00, 0x00,
                0x03, 0x0d, (byte) 0xd3, (byte) 0xe6, 0x14, (byte) 0xc4, 0x7e, (byte) 0x87,
                (byte) 0xc9, 0x20, 0x7a, 0x79, 0x4e, 0x07
        };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MoForwardShortMessageResponseImpl ind = new MoForwardShortMessageResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SmsSignalInfo sm_rp_ui = ind.getSM_RP_UI();
        MAPExtensionContainer extensionContainer = ind.getExtensionContainer();
        assertEquals(sm_rp_ui.getData(), new byte[] { 11, 22, 33, 44, 55 });
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        // test from MAP load test
        rawData = getEncodedDataMOSmsLoadTest();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        // Wireshark sample
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: mo-forwardSM (46)
         *             sm-RP-UI: 35bb0b919598097739f70000030dd3e614c47e87c9207a794e07
         */
        sm_rp_ui = ind.getSM_RP_UI();
        extensionContainer = ind.getExtensionContainer();
        assertEquals(sm_rp_ui.getData(), new byte[] { 0x35, (byte) 0xbb, 0x0b, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39, (byte) 0xf7,
                0x00, 0x00, 0x03, 0x0d, (byte) 0xd3, (byte) 0xe6, 0x14, (byte) 0xc4, 0x7e, (byte) 0x87, (byte) 0xc9, 0x20, 0x7a, 0x79, 0x4e, 0x07 });
        assertNotNull(sm_rp_ui.decodeTpdu(true));
        assertEquals(sm_rp_ui.decodeTpdu(true).getSmsTpduType(), SmsTpduType.SMS_SUBMIT);
        SmsSubmitTpduImpl smsTpdu = new SmsSubmitTpduImpl(sm_rp_ui.decodeTpdu(true).encodeData(), Charset.defaultCharset());
        assertEquals(smsTpdu.getSmsTpduType(), SmsTpduType.SMS_SUBMIT);
        assertTrue(smsTpdu.getRejectDuplicates());
        assertFalse(smsTpdu.getReplyPathExists());
        assertTrue(smsTpdu.getStatusReportRequest());
        assertEquals(smsTpdu.getMessageReference(), 187);
        assertEquals(smsTpdu.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
        assertEquals(smsTpdu.getDestinationAddress().getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
        assertEquals(smsTpdu.getDestinationAddress().getAddressValue(), "59899077937");
        assertEquals(smsTpdu.getProtocolIdentifier().getCode(), 0);
        assertEquals(smsTpdu.getDataCodingScheme().getCode(), 0);
        assertEquals(smsTpdu.getValidityPeriod().getRelativeFormatValue().intValue(), 3);
        assertEquals(smsTpdu.getUserData().getDataCodingScheme().getCode(), 0);
        assertEquals(smsTpdu.getUserData().getEncodedUserDataLength(), 13);
        assertNull(extensionContainer);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        MoForwardShortMessageResponseImpl ind = new MoForwardShortMessageResponseImpl(sm_RP_UI, extensionContainer);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test from MAP load test
        // Wireshark sample
        /*
         * Component: returnResultLast (2)
         *     returnResultLast
         *         invokeID: 0
         *         resultretres
         *             opCode: localValue (0)
         *                 localValue: mo-forwardSM (46)
         *             sm-RP-UI: 35bb0b919598097739f70000030dd3e614c47e87c9207a794e07
         */
        boolean rejectDuplicates = true;
        boolean replyPathExists = false;
        boolean statusReportRequest = true;
        int messageReference = 187;
        AddressField destinationAddress = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "59899077937");
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifierImpl(0);
        ValidityPeriod validityPeriod = new ValidityPeriodImpl(3);
        DataCodingScheme dataCodingScheme = new DataCodingSchemeImpl(0);
        UserDataHeader userDataHeader = new UserDataHeaderImpl();
        Charset gsm8Charset = Charset.defaultCharset();
        UserData userData = new UserDataImpl("SMS load test", dataCodingScheme, userDataHeader, gsm8Charset);
        SmsTpduImpl smsTpdu = new SmsSubmitTpduImpl(rejectDuplicates, replyPathExists, statusReportRequest, messageReference, destinationAddress,
                protocolIdentifier, validityPeriod, userData);
        sm_RP_UI = new SmsSignalInfoImpl(smsTpdu, gsm8Charset);
        extensionContainer = null;

        ind = new MoForwardShortMessageResponseImpl(sm_RP_UI, extensionContainer);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataMOSmsLoadTest();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
