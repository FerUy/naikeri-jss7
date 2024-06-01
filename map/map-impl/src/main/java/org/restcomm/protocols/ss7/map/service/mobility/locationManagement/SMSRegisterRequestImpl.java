package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SMSRegisterRequest;
import org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive;

import java.io.IOException;

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class SMSRegisterRequestImpl implements SMSRegisterRequest, MAPAsnPrimitive {

    private static final int _INDEX_SmsRegistrationRequired = 0;
    private static final int _INDEX_SmsRegistrationNotPreferred = 1;
    private static final int _INDEX_NoPreference = 2;

    public static final String _PrimitiveName = "SMSRegisterRequest";

    private BitSetStrictLength bitString = new BitSetStrictLength(3);

    public SMSRegisterRequestImpl() {
    }

    public SMSRegisterRequestImpl(boolean smsRegistrationRequired, boolean smsRegistrationNotPreferred,
                                  boolean NoPreference) {
        if (smsRegistrationRequired)
            this.bitString.set(_INDEX_SmsRegistrationRequired);
        if (smsRegistrationNotPreferred)
            this.bitString.set(_INDEX_SmsRegistrationNotPreferred);
        if (NoPreference)
            this.bitString.set(_INDEX_NoPreference);
    }

    public int getTag() throws MAPException {
        return Tag.STRING_BIT;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    public boolean isSmsRegistrationRequired() {
        return this.bitString.get(_INDEX_SmsRegistrationRequired);
    }

    public boolean isSmsRegistrationNotPreferred() {
        return this.bitString.get(_INDEX_SmsRegistrationNotPreferred);
    }

    public boolean isNoPreference() {
        return this.bitString.get(_INDEX_NoPreference);
    }

    public void decodeAll(AsnInputStream asnInputStream) throws MAPParsingComponentException {
        try {
            int length = asnInputStream.readLength();
            this._decode(asnInputStream, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream asnInputStream, int length) throws MAPParsingComponentException {
        try {
            this._decode(asnInputStream, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream asnInputStream, int length) throws MAPParsingComponentException, IOException, AsnException {
        if (length < 1 || length > 2)
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": the field must contain 1 or 2 octets. Contains: " + length,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        this.bitString = asnInputStream.readBitStringData(length);
    }

    public void encodeAll(AsnOutputStream asnOutputStream) throws MAPException {
        this.encodeAll(asnOutputStream, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOutputStream, int tagClass, int tag) throws MAPException {
        try {
            asnOutputStream.writeTag(tagClass, true, tag);
            int pos = asnOutputStream.StartContentDefiniteLength();
            this.encodeData(asnOutputStream);
            asnOutputStream.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOutputStream) throws MAPException {
        try {
            asnOutputStream.writeBitStringData(this.bitString);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bitString == null) ? 0 : bitString.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SMSRegisterRequestImpl other = (SMSRegisterRequestImpl) obj;
        if (bitString == null) {
            if (other.bitString != null)
                return false;
        } else if (!bitString.equals(other.bitString))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedRATTypes [");

        if (isSmsRegistrationRequired())
            sb.append("sms-registration-required , ");
        if (isSmsRegistrationNotPreferred())
            sb.append("sms-registration-not-preferred, ");
        if (isNoPreference())
            sb.append("no-preference, ");

        sb.append("]");

        return sb.toString();
    }
}
