
package org.restcomm.protocols.ss7.tools.simulator.tests.sms;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsServerConfigurationData_OldFormat extends TestSmsServerConfigurationData {

    protected static final XMLFormat<TestSmsServerConfigurationData_OldFormat> XML = new XMLFormat<>(
            TestSmsServerConfigurationData_OldFormat.class) {

        public void write(TestSmsServerConfigurationData_OldFormat srv, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, TestSmsServerConfigurationData_OldFormat srv) throws XMLStreamException {
            srv.hlrSsn = xml.getAttribute(HLR_SSN).toInt();
            srv.vlrSsn = xml.getAttribute(VLR_SSN).toInt();

            srv.serviceCenterAddress = xml.get(SERVICE_CENTER_ADDRESS, String.class);

            String an = xml.get(ADDRESS_NATURE, String.class);
            srv.addressNature = AddressNature.valueOf(an);
            String np = xml.get(NUMBERING_PLAN, String.class);
            srv.numberingPlan = NumberingPlan.valueOf(np);
            String mpv = xml.get(MAP_PROTOCOL_VERSION, String.class);
            srv.mapProtocolVersion = MapProtocolVersion.createInstance(mpv);
            String ton = xml.get(TYPE_OF_NUMBER, String.class);
            srv.typeOfNumber = TypeOfNumber.valueOf(ton);
            String npi = xml.get(NUMBERING_PLAN_IDENTIFICATION, String.class);
            srv.numberingPlanIdentification = NumberingPlanIdentification.valueOf(npi);
            String sct = xml.get(SMS_CODING_TYPE, String.class);
            srv.smsCodingType = SmsCodingType.createInstance(sct);
        }
    };

}
