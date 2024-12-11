
package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.NetworkNodeDiameterAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public interface MAPDialogSms extends MAPDialog {

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional, default: false
     * @return invokeId
     */
    Long addForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend) throws MAPException;

    Long addForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend) throws MAPException;

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE response
     */
    void addForwardShortMessageResponse(long invokeId) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA (M) Contains the Service Centre address received from the mobile station
     * @param sm_RP_OA (M) The MSISDN received from the VLR or from the SGSN is inserted in this parameter in the MO SM transfer
     * @param sm_RP_UI (M) The short message transfer protocol data unit received from the Service Centre is inserted in this parameter
     * @param extensionContainer (O)
     * @param imsi (C) The IMSI of the originating subscriber shall be inserted in this parameter in the MO SM transfer
     * @param correlationID (C) Composed of an HLR-Id identifying the destination user's HLR,
     *                      a SIP-URI-B identifying the MSISDN-less destination user, and a SIP-URI-A identifying the originating user
     * @param smDeliveryOutcome (C) Indicates the status of the mobile terminated SM delivery.
     *                          Shall be present if Correlation ID is present and shall take one of the unsuccessful outcome values
     *
     * @return invokeId
     */
    Long addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer,
            IMSI imsi, CorrelationID correlationID, SMDeliveryOutcome smDeliveryOutcome) throws MAPException;

    Long addMoForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            MAPExtensionContainer extensionContainer, IMSI imsi, CorrelationID correlationID, SMDeliveryOutcome smDeliveryOutcome) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE response
     *
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     */
    void addMoForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional
     * @param extensionContainer optional
     */
    Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainer extensionContainer, Integer smDeliveryTimer,
            Time smDeliveryStartTime, boolean smsOverIPOnlyIndicator, CorrelationID correlationID,
            Time maximumRetransmissionTime, ISDNAddressString smsGmscAddress, NetworkNodeDiameterAddress smsGmscDiameterAddress) throws MAPException;

    Long addMtForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend, MAPExtensionContainer extensionContainer,
            Integer smDeliveryTimer, Time smDeliveryStartTime, boolean smsOverIPOnlyIndicator,
            CorrelationID correlationID, Time maximumRetransmissionTime, ISDNAddressString smsGmscAddress,
            NetworkNodeDiameterAddress smsGmscDiameterAddress) throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE response
     *
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     */
    void addMtForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param sm_RP_PRI mandatory
     * @param serviceCentreAddress mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param sM_RP_MTI optional
     * @param sM_RP_SMEA optional
     */
    Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, boolean sm_RP_PRI, AddressString serviceCentreAddress,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA,
            SMDeliveryNotIntended smDeliveryNotIntended, boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator,
            boolean singleAttemptDelivery, TeleserviceCode teleserviceCode, CorrelationID correlationID, boolean smsfSupportIndicator) throws MAPException;

    Long addSendRoutingInfoForSMRequest(int customInvokeTimeout, ISDNAddressString msisdn, boolean sm_RP_PRI,
            AddressString serviceCentreAddress, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator,
            SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA, SMDeliveryNotIntended smDeliveryNotIntended,
            boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator, boolean singleAttemptDelivery,
            TeleserviceCode teleserviceCode, CorrelationID correlationID, boolean smsfSupportIndicator) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param imsi mandatory
     * @param locationInfoWithLMSI mandatory
     * @param extensionContainer optional
     */
    void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI,
            MAPExtensionContainer extensionContainer, Boolean mwdSet, IpSmGwGuidance ipSmGwGuidance) throws MAPException;

    /**
     * Sending MAP-REPORT-SM-DELIVERY-STATUS request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @param sMDeliveryOutcome mandatory
     * @param absentSubscriberDiagnosticSM mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param deliveryOutcomeIndicator optional
     * @param additionalSMDeliveryOutcome optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @param ipSmGwIndicator optional
     * @param ipSmGwSMDeliveryOutcome optional
     * @param ipSmGwAbsentSubscriberDiagnosticSM optional
     * @param imsi optional
     * @param singleAttemptDelivery optional
     * @param correlationID optional
     * @param smsf3gppDeliveryOutcomeIndicator optional
     * @param smsf3gppDeliveryOutcome optional
     * @param smsf3gppAbsentSubscriberDiagnosticSM optional
     * @param smsfNon3gppDeliveryOutcomeIndicator optional
     * @param smsfNon3gppDeliveryOutcome optional
     * @param smsfNon3gppAbsentSubscriberDiagnosticSM optional
     *
     */
    Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
            Integer absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM,
            boolean ipSmGwIndicator, SMDeliveryOutcome ipSmGwSMDeliveryOutcome, Integer ipSmGwAbsentSubscriberDiagnosticSM,
            IMSI imsi, boolean singleAttemptDelivery, CorrelationID correlationID, boolean smsf3gppDeliveryOutcomeIndicator,
            SMDeliveryOutcome smsf3gppDeliveryOutcome, Integer smsf3gppAbsentSubscriberDiagnosticSM, boolean smsfNon3gppDeliveryOutcomeIndicator,
            SMDeliveryOutcome smsfNon3gppDeliveryOutcome, Integer smsfNon3gppAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addReportSMDeliveryStatusRequest(int customInvokeTimeout, ISDNAddressString msisdn, AddressString serviceCentreAddress,
            SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator,
            boolean deliveryOutcomeIndicator, SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM,
            boolean ipSmGwIndicator, SMDeliveryOutcome ipSmGwSMDeliveryOutcome, Integer ipSmGwAbsentSubscriberDiagnosticSM,
            IMSI imsi, boolean singleAttemptDelivery, CorrelationID correlationID, boolean smsf3gppDeliveryOutcomeIndicator,
            SMDeliveryOutcome smsf3gppDeliveryOutcome, Integer smsf3gppAbsentSubscriberDiagnosticSM, boolean smsfNon3gppDeliveryOutcomeIndicator,
            SMDeliveryOutcome smsfNon3gppDeliveryOutcome, Integer smsfNon3gppAbsentSubscriberDiagnosticSM)
            throws MAPException;

    /**
     * Sending MAP-REPORT-SM-DELIVERY-STATUS response
     *
     * @param storedMSISDN optional
     * @param extensionContainer optional
     */
    void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN,
            MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-INFORM-SERVICE-CENTRE request
     *
     * @param storedMSISDN optional
     * @param mwStatus optional
     * @param extensionContainer optional
     * @param absentSubscriberDiagnosticSM optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @param smsf3gppAbsentSubscriberDiagnosticSM optional
     * @param smsfNon3gppAbsentSubscriberDiagnosticSM optional
     */
    Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM, Integer smsf3gppAbsentSubscriberDiagnosticSM,
            Integer smsfNon3gppAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addInformServiceCentreRequest(int customInvokeTimeout, ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM, Integer smsf3gppAbsentSubscriberDiagnosticSM,
            Integer smsfNon3gppAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @param imsi optional
     * @param correlationID optional
     * @param maximumUeAvailabilityTime optional
     * @param smsGmscAlertEvent optional
     * @param smsGmscDiameterAddress optional
     * @param newSGSNNumber optional
     * @param newSGSNDiameterAddress optional
     * @param newMMENumber optional
     * @param newMMEDiameterAddress optional
     * @param newMSCNumber optional
     */
    Long addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress, IMSI imsi,
            CorrelationID correlationID, Time maximumUeAvailabilityTime, SmsGmscAlertEvent smsGmscAlertEvent,
            NetworkNodeDiameterAddress smsGmscDiameterAddress, ISDNAddressString newSGSNNumber,
            NetworkNodeDiameterAddress newSGSNDiameterAddress, ISDNAddressString newMMENumber,
            NetworkNodeDiameterAddress newMMEDiameterAddress, ISDNAddressString newMSCNumber) throws MAPException;

    Long addAlertServiceCentreRequest(int customInvokeTimeout, ISDNAddressString msisdn, AddressString serviceCentreAddress, IMSI imsi,
            CorrelationID correlationID, Time maximumUeAvailabilityTime, SmsGmscAlertEvent smsGmscAlertEvent,
            NetworkNodeDiameterAddress smsGmscDiameterAddress, ISDNAddressString newSGSNNumber,
            NetworkNodeDiameterAddress newSGSNDiameterAddress, ISDNAddressString newMMENumber,
            NetworkNodeDiameterAddress newMMEDiameterAddress, ISDNAddressString newMSCNumber) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     */
    void addAlertServiceCentreResponse(long invokeId) throws MAPException;


    Long addReadyForSMRequest(IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainer extensionContainer,
            boolean additionalAlertReasonIndicator, Time maximumUeAvailabilityTime) throws MAPException;

    Long addReadyForSMRequest(int customInvokeTimeout, IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator,
            MAPExtensionContainer extensionContainer, boolean additionalAlertReasonIndicator, Time maximumUeAvailabilityTime) throws MAPException;

    void addReadyForSMResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException;

    Long addNoteSubscriberPresentRequest(IMSI imsi) throws MAPException;

    Long addNoteSubscriberPresentRequest(int customInvokeTimeout, IMSI imsi) throws MAPException;

}
