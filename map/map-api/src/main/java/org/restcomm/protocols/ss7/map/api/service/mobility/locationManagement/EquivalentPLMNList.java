package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * EPLMN-List ::= SEQUENCE SIZE (1..50) OF PLMN-Id
 *
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 *
 */
public interface EquivalentPLMNList extends Serializable {

    ArrayList<PlmnId> getEquivalentPLMNList();
}
