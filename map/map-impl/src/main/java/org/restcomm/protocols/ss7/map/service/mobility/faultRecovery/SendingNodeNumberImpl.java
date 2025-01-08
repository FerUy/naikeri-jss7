package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.SendingNodeNumber;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive;

import java.io.IOException;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class SendingNodeNumberImpl implements SendingNodeNumber, MAPAsnPrimitive {

    public static final int _TAG_CSS_NUMBER = 1;

    public static final String _PrimitiveName = "SendingNodeNumber";

    private ISDNAddressString hlrNumber;
    private ISDNAddressString cssNumber;

    public SendingNodeNumberImpl() {
    }

    public SendingNodeNumberImpl(ISDNAddressString hlrNumber, ISDNAddressString cssNumber) {
        this.hlrNumber = hlrNumber;
        this.cssNumber = cssNumber;
    }

    public void setData(ISDNAddressString hlrNumber, ISDNAddressString cssNumber) {
        this.hlrNumber = hlrNumber;
        this.cssNumber = cssNumber;
    }

    @Override
    public ISDNAddressString getHlrNumber() {
        return this.hlrNumber;
    }

    @Override
    public ISDNAddressString getCssNumber() {
        return cssNumber;
    }

    @Override
    public int getTag() throws MAPException {
        if (hlrNumber != null)
            return Tag.STRING_OCTET;
        else if (cssNumber != null)
            return Tag.SEQUENCE;

        throw new MAPException("Error encoding " + _PrimitiveName + ": no choices are selected");
    }

    @Override
    public int getTagClass() {
        if (hlrNumber != null)
            return Tag.CLASS_UNIVERSAL;
        else
            return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
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

    @Override
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
        this.hlrNumber = null;
        this.cssNumber = null;

        AsnInputStream ais = asnInputStream.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // hlr-Number ISDN-AddressString
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding" + _PrimitiveName + ".hlrNumber: " +
                                "bad tag or tag class or is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                    this.hlrNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.hlrNumber).decodeAll(ais);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        switch (tag) {
                            case _TAG_CSS_NUMBER:
                                // css-Number [1] ISDN-AddressString
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".cssNumber: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.cssNumber = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) this.cssNumber).decodeAll(ais);
                                break;

                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else {

                        ais.advanceElement();
                    }
                    break;
            }
            num++;
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOutputStream) throws MAPException {
        this.encodeAll(asnOutputStream, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOutputStream, int tagClass, int tag) throws MAPException {
        try {
            asnOutputStream.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOutputStream.StartContentDefiniteLength();
            this.encodeData(asnOutputStream);
            asnOutputStream.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOutputStream) throws MAPException {
        if (this.hlrNumber == null && this.cssNumber == null)
            throw new MAPException("Error when encoding " + _PrimitiveName + ": at least one of hlrNumber or cssNumber " +
                    "parameters must be present");

        if (this.hlrNumber != null)
            ((ISDNAddressStringImpl) this.hlrNumber).encodeData(asnOutputStream);

        if (this.cssNumber != null)
            ((ISDNAddressStringImpl) this.cssNumber).encodeAll(asnOutputStream, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CSS_NUMBER);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.hlrNumber != null) {
            sb.append(", hlrNumber=");
            sb.append(this.hlrNumber);
        }
        if (this.cssNumber != null) {
            sb.append(", cssNumber=");
            sb.append(this.cssNumber);
        }

        sb.append("]");

        return sb.toString();
    }

}
