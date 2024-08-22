package org.restcomm.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsGmscAlertEvent;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class AlertServiceCentreRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 18, 4, 7, -111, -110, 17, 19, 50, 19, -15, 4, 7, -111, -108, -120, 115, 0, -110, -14 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(MAPOperationCode.alertServiceCentre);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = asc.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");

        AddressString sca = asc.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "49883700292");
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "29113123311");
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "49883700292");
        IMSI imsi = null;
        CorrelationID correlationID = null;
        Time maximumUeAvailabilityTime = null;
        SmsGmscAlertEvent smsGmscAlertEvent = null;
        NetworkNodeDiameterAddress smsGmscDiameterAddress = null;
        ISDNAddressString newSGSNNumber = null;
        NetworkNodeDiameterAddress newSGSNDiameterAddress = null;
        ISDNAddressString newMMENumber = null;
        NetworkNodeDiameterAddress newMMEDiameterAddress = null;
        ISDNAddressString newMSCNumber = null;
        AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(msisdn, sca, imsi, correlationID,
                maximumUeAvailabilityTime, smsGmscAlertEvent, smsGmscDiameterAddress, newSGSNNumber, newSGSNDiameterAddress,
                newMMENumber, newMMEDiameterAddress, newMSCNumber);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
