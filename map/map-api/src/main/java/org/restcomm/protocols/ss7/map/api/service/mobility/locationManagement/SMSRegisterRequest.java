package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import java.io.Serializable;

/**
 *
 * SMSRegisterRequest::= ENUMERATED {
 *  sms-registration-required (0),
 *  sms-registration-not-preferred (1),
 *  no-preference (2),
 *  ...}
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public interface SMSRegisterRequest extends Serializable {

    boolean isSmsRegistrationRequired();

    boolean isSmsRegistrationNotPreferred();

    boolean isNoPreference();
}
