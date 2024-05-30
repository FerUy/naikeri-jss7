package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;

import java.io.IOException;

public class NetworkNodeDiameterAddressImpl extends SequenceBase implements NetworkNodeDiameterAddress, MAPAsnPrimitive {

    public static final int _ID_diameterName = 0;
    public static final int _ID_diameterRealm = 1;

    public static final String _PrimitiveName = "NetworkNodeDiameterAddress";

    private static final String DIAMETER_NAME = "diameterName";
    private static final String DIAMETER_REALM = "diameterRealm";

    private DiameterIdentity diameterName;
    private DiameterIdentity diameterRealm;

    public NetworkNodeDiameterAddressImpl() {
        super("NetworkNodeDiameterAddress");
    }

    /**
     * @param diameterName (Diameter host name)
     * @param diameterRealm (Diameter host realm)
     */
    public NetworkNodeDiameterAddressImpl(DiameterIdentity diameterName, DiameterIdentity diameterRealm) {
        super("NetworkNodeDiameterAddress");
        this.diameterName = diameterName;
        this.diameterRealm = diameterRealm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement NetworkNodeDiameterAddress#getDiameterName()
     */
    public DiameterIdentity getDiameterName() {
        return diameterName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement NetworkNodeDiameterAddress#getDiameterRealm()
     */
    public DiameterIdentity getDiameterRealm() {
        return diameterRealm;
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

    protected void _decode(AsnInputStream asnInputStream, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.diameterName = null;
        this.diameterRealm = null;

        AsnInputStream ais = asnInputStream.readSequenceStreamData(length);

        while (ais.available() != 0) {

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_diameterName:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " diameterName: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.diameterName = new DiameterIdentityImpl();
                        ((DiameterIdentityImpl) this.diameterName).decodeAll(ais);
                        break;
                    case _ID_diameterRealm:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " diameterRealm: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.diameterRealm = new DiameterIdentityImpl();
                        ((DiameterIdentityImpl) this.diameterRealm).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
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

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOutputStream) throws MAPException {
        try {
            if (this.diameterName != null) {
                ((DiameterIdentityImpl) this.diameterName).encodeAll(asnOutputStream, Tag.CLASS_CONTEXT_SPECIFIC, _ID_diameterName);
            }

            if (this.diameterRealm != null) {
                ((DiameterIdentityImpl) this.diameterRealm).encodeAll(asnOutputStream, Tag.CLASS_CONTEXT_SPECIFIC, _ID_diameterRealm);
            }
        } catch (MAPException e) {
            throw new MAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.diameterName != null) {
            sb.append(", diameterName=");
            sb.append(this.diameterName);
        }
        if (this.diameterRealm != null) {
            sb.append(", diameterRealm=");
            sb.append(this.diameterRealm);
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<NetworkNodeDiameterAddressImpl> NETWORK_NODE_DIAMETER_ADDRESS_XML = new XMLFormat<>(
            NetworkNodeDiameterAddressImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, NetworkNodeDiameterAddressImpl networkNodeDiameterAddress)
                throws XMLStreamException {
            networkNodeDiameterAddress.diameterName = xml.get(DIAMETER_NAME, DiameterIdentityImpl.class);
            networkNodeDiameterAddress.diameterRealm = xml.get(DIAMETER_REALM, DiameterIdentityImpl.class);
        }

        @Override
        public void write(NetworkNodeDiameterAddressImpl networkNodeDiameterAddress, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (networkNodeDiameterAddress.diameterName != null) {
                xml.add((DiameterIdentityImpl) networkNodeDiameterAddress.diameterName, DIAMETER_NAME, DiameterIdentityImpl.class);
            }
            if (networkNodeDiameterAddress.diameterRealm != null) {
                xml.add((DiameterIdentityImpl) networkNodeDiameterAddress.diameterRealm, DIAMETER_REALM, DiameterIdentityImpl.class);
            }
        }
    };
}
