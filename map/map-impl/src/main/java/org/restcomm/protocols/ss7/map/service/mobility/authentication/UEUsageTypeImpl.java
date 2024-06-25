package org.restcomm.protocols.ss7.map.service.mobility.authentication;

import jakarta.xml.bind.DatatypeConverter;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UEUsageType;
import org.restcomm.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class UEUsageTypeImpl extends OctetStringBase implements UEUsageType {

    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public UEUsageTypeImpl() {
        super(4, 4, "UEUsageType");
    }

    public UEUsageTypeImpl(byte[] data) {
        super(4, 4, "UEUsageType", data);
    }

    public byte[] getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<UEUsageTypeImpl> CARRIER_XML = new XMLFormat<UEUsageTypeImpl>(UEUsageTypeImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UEUsageTypeImpl ueUsageType) throws XMLStreamException {
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s != null) {
                ueUsageType.data = DatatypeConverter.parseHexBinary(s);
            }
        }

        @Override
        public void write(UEUsageTypeImpl ueUsageType, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (ueUsageType.data != null) {
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(ueUsageType.data));
            }
        }
    };
}
