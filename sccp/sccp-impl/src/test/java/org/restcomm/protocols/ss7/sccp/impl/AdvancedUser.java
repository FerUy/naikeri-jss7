package org.restcomm.protocols.ss7.sccp.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

public class AdvancedUser extends User {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(AdvancedUser.class.getName());

    public AdvancedUser(SccpProvider provider, SccpAddress address, SccpAddress dest, int ssn) {
        super(provider, address, dest, ssn);
    }

    @Override
    public void onMessage(SccpDataMessage message) {
        this.messages.add(message);
        logger.debug("SccpDataMessage={} seqControl={}", message, message.getSls());
        SccpAddress calledAddress = message.getCalledPartyAddress();
        SccpAddress callingAddress = message.getCallingPartyAddress();
        SccpDataMessage newMessage = provider.getMessageFactory().createDataMessageClass1(callingAddress, calledAddress, message.getData(),
                message.getSls(),message.getOriginLocalSsn(), true, message.getHopCounter(), message.getImportance());
        newMessage.setOutgoingDpc(message.getIncomingOpc());
        try {
            this.provider.send(newMessage);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
