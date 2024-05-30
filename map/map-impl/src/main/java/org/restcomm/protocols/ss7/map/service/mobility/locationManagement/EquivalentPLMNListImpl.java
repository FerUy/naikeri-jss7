package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EquivalentPLMNList;
import org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public class EquivalentPLMNListImpl implements EquivalentPLMNList, MAPAsnPrimitive {

    public static final String _PrimitiveName = "EquivalentPLMNList";

    private ArrayList<PlmnId> equivalentPLMNList;

    public EquivalentPLMNListImpl() {
    }

    public EquivalentPLMNListImpl(ArrayList<PlmnId> equivalentPLMNList) {
        this.equivalentPLMNList = equivalentPLMNList;
    }

    public ArrayList<PlmnId> getEquivalentPLMNList() {
        return equivalentPLMNList;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
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

        this.equivalentPLMNList = new ArrayList<>();

        AsnInputStream ais = asnInputStream.readSequenceStreamData(length);

        while (ais.available() != 0) {

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                if (tag == Tag.SEQUENCE) {
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter EquivalentPLMNList is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    PlmnIdImpl plmnId = new PlmnIdImpl();
                    plmnId.decodeAll(ais);
                    this.equivalentPLMNList.add(plmnId);
                } else {
                    ais.advanceElement();
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.equivalentPLMNList.size() < 1 || this.equivalentPLMNList.size() > 50) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": EquivalentPLMNList size must be from 1 to 50, found:" + this.equivalentPLMNList.size(),
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOutputStream) throws MAPException {
        this.encodeAll(asnOutputStream, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOutputStream, int tagClass, int tag) throws MAPException {
        try {
            asnOutputStream.writeTag(tagClass, false, tag);
            int pos = asnOutputStream.StartContentDefiniteLength();
            this.encodeData(asnOutputStream);
            asnOutputStream.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOutputStream) throws MAPException {
        try {
            if (this.equivalentPLMNList == null || this.equivalentPLMNList.size() < 1 || this.equivalentPLMNList.size() > 50) {
                throw new MAPException("EquivalentPLMNList list must contain from 1 to 50 elements");
            }

            for (PlmnId plmnId : this.equivalentPLMNList) {
                ((PlmnIdImpl) plmnId).encodeAll(asnOutputStream);
            }
        } catch (MAPException e) {
            throw new MAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EquivalentPLMNList [");

        if (this.equivalentPLMNList != null) {
            for (PlmnId plmnId : this.equivalentPLMNList) {
                if (plmnId != null) {
                    sb.append(plmnId);
                    sb.append(", ");
                }
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
