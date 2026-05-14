package org.restcomm.protocols.ss7.tools.simulator.tests.ati_psi_lsm;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * @author sergey vetyutnev
 */
public class TestATIServerConfigurationData {

    protected static final String ATI_REACTION = "atiReaction";

    protected ATIReaction atiReaction = new ATIReaction(ATIReaction.VAL_RETURN_SUCCESS);

    public ATIReaction getATIReaction() {
        return atiReaction;
    }

    public void setATIReaction(ATIReaction val) {
        atiReaction = val;
    }

    protected static final XMLFormat<TestATIServerConfigurationData> XML = new XMLFormat<>(
            TestATIServerConfigurationData.class) {

        public void write(TestATIServerConfigurationData clt, OutputElement xml) throws XMLStreamException {
            xml.add(clt.atiReaction.toString(), ATI_REACTION, String.class);
        }

        public void read(InputElement xml, TestATIServerConfigurationData clt) throws XMLStreamException {
            String atiR = xml.get(ATI_REACTION, String.class);
            clt.atiReaction = ATIReaction.createInstance(atiR);
        }
    };

}
