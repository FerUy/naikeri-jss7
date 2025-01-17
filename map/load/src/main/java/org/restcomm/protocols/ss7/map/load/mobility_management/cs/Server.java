package org.restcomm.protocols.ss7.map.load.mobility_management.cs;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEACIC;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationPlanValue;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationTypeValue;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.QuintupletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UEUsageType;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.DeleteSubscriberDataArgs;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.InsertSubscriberDataArgs;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetId;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.SendingNodeNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedNodes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfiguration;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNConfigurationProfile;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdjacentAccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EDRXCycleLength;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EDRXCycleLengthValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtAccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.IMSIGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSPrivacyClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LocalGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MOLRClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWAllocationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNTypeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificAPNInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationQuintupletImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.EpcAvImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.QuintupletListImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.UEUsageTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.DeleteSubscriberDataArgsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.InsertSubscriberDataArgsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.ResetIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.SendingNodeNumberImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.UESBIIuAImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.UESBIIuBImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.UESBIIuImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AMBRImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNConfigurationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNConfigurationProfileImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdditionalInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdditionalSubscriptionsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdjacentAccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSAllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CauseValueImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DestinationNumberCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.EDRXCycleLengthImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.EDRXCycleLengthValueImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.EPSQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.EPSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.EPSSubscriptionDataWithdrawImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtAccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExternalClientImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.IMSIGroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LCSPrivacyClassImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAInformationWithdrawImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LocalGroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LongGroupIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MCSSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MOLRClassImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDNTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SSCamelDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ServiceTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ZoneCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.sccpext.impl.SccpExtModuleImpl;
import org.restcomm.protocols.ss7.sccpext.router.RouterExt;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import static org.restcomm.protocols.ss7.sccp.LongMessageRuleType.XUDT_ENABLED;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class Server extends TestHarnessMobilityManagement {

    private static final Logger logger = Logger.getLogger(Server.class);

    private static MAPProvider mapProvider;

    // TCAP
    private TCAPStack tcapStack;

    // SCCP
    SccpExtModuleImpl sccpExtModule;
    private SccpStackImpl sccpStack;

    // M3UA
    private M3UAManagementImpl serverM3UAMgmt;

    // SCTP
    private NettySctpManagementImpl sctpManagement;

    int endCount = 0;
    volatile long start = System.currentTimeMillis();

    static Long imsiForSenders = 748026871012340L;

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {

        this.initSCTP(ipChannelType);

        // Initialize M3UA first
        this.initM3UA();

        // Initialize SCCP
        this.initSCCP();

        // Initialize TCAP
        this.initTCAP();

        // Initialize MAP
        this.initMAP();

        // Finally, start the ASP
        serverM3UAMgmt.startAsp("RASP1");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.sctpManagement = new NettySctpManagementImpl("Server");
//        this.sctpManagement.setSingleThread(false);
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(10000);
        this.sctpManagement.removeAllResources();

        // 1. Create SCTP Server
        sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT, ipChannelType, null);

        // 2. Create SCTP Server Association
        sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);

        // 3. Start Server
        sctpManagement.startServer(SERVER_NAME);
    }

    private void initM3UA() throws Exception {
        this.serverM3UAMgmt = new M3UAManagementImpl("Server", null, new Ss7ExtInterfaceImpl());
        this.serverM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.serverM3UAMgmt.setDeliveryMessageThreadCount(DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.serverM3UAMgmt.start();
        this.serverM3UAMgmt.removeAllResources();

        // Step 1 : Create App Server

        RoutingContext rc = factory.createRoutingContext(new long[] { 101L });
        TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
        NetworkAppearance na = factory.createNetworkAppearance(102L);
        As as = this.serverM3UAMgmt.createAs("RAS1", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        logger.debug(as);

        // Step 2 : Create ASP
        AspFactory aspFactor = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);
        logger.debug(aspFactor);

        // Step3 : Assign ASP to AS
        Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");
        logger.debug(asp);

        // Step 4: Add Route. Remote point code is 2
        this.serverM3UAMgmt.addRoute(CLIENT_SPC, -1, -1, "RAS1");
    }

    private void initSCCP() throws Exception {
        Ss7ExtInterface ss7ExtInterface = new Ss7ExtInterfaceImpl();
        sccpExtModule = new SccpExtModuleImpl();
        ss7ExtInterface.setSs7ExtSccpInterface(sccpExtModule);
        this.sccpStack = new SccpStackImpl("MapLoadServerSccpStack", ss7ExtInterface);
        this.sccpStack.setMtp3UserPart(1, this.serverM3UAMgmt);

        this.sccpStack.start();
        this.sccpStack.removeAllResources();

        Router router = this.sccpStack.getRouter();
        RouterExt routerExt = sccpExtModule.getRouterExt();
        SccpResource sccpResource = this.sccpStack.getSccpResource();

        sccpResource.addRemoteSpc(0, CLIENT_SPC, 0, 0);
        sccpResource.addRemoteSsn(0, CLIENT_SPC, VLR_SSN, 0, false);

        router.addMtp3ServiceAccessPoint(1, 1, SERVER_SPC, NETWORK_INDICATOR, 0, null);
        router.addMtp3Destination(1, 1, CLIENT_SPC, CLIENT_SPC, 0, 255, 255);
        router.addLongMessageRule(0, 1, 16384, XUDT_ENABLED);

        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        EncodingScheme ec = new BCDEvenEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("-", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, SERVER_SPC, 0);
        routerExt.addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, CLIENT_SPC, 0);
        routerExt.addRoutingAddress(2, remoteAddress);

        GlobalTitle gt = fact.createGlobalTitle("*", 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY, ec,
                NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        routerExt.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern,
                "K", 1, -1, null, 0, null);
        routerExt.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern,
                "K", 2, -1, null, 0, null);
    }

    private void initTCAP() throws Exception {
        this.tcapStack = new TCAPStackImpl("TestServer", this.sccpStack.getSccpProvider(), HLR_SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000);
        this.tcapStack.setInvokeTimeout(30000);
        this.tcapStack.setMaxDialogs(MAX_DIALOGS);
    }

    private void initMAP() throws Exception {
        // MAP
        MAPStackImpl mapStack = new MAPStackImpl("TestServer", this.tcapStack.getProvider());
        mapProvider = mapStack.getMAPProvider();

        mapProvider.addMAPDialogListener(this);
        mapProvider.getMAPServiceMobility().addMAPServiceListener(this);

        mapProvider.getMAPServiceMobility().activate();

        mapStack.start();
    }


    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogDelimiter
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogRequest
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.AddressString,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(
                    "onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
                    mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
        }
    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                        AddressString imsi, AddressString vlr) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
                    mapDialog.getLocalDialogId(), destReference, origReference));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogAccept( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(),
                    extensionContainer));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogReject( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason, org.restcomm.protocols.ss7.map.api.dialog.MAPProviderError,
     * org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext,
                               MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogUserAbort
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), userReason, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogProviderAbort
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource,
     * org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
                                      MAPExtensionContainer extensionContainer) {
        logger.error(String.format(
                "onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
                mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogClose(org .mobicents.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogNotice( org.restcomm.protocols.ss7.map.api.MAPDialog,
     * org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic)
     */
    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
                mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogResease
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onDialogRelease for DialogId=%d", mapDialog.getLocalDialogId()));
        }

        this.endCount++;

        if ((this.endCount % 10000) == 0) {
            long currentTime = System.currentTimeMillis();
            long processingTime = currentTime - start;
            start = currentTime;
            logger.warn("Completed 10000 Dialogs in " + processingTime + " milliseconds");
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPDialogListener#onDialogTimeout
     * (org.restcomm.protocols.ss7.map.api.MAPDialog)
     */
    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getLocalDialogId()));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onErrorComponent
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long,
     * org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage)
     */
    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
                mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long, org.restcomm.protocols.ss7.tcap.asn.comp.Problem)
     */
    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s",
                mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
     * (org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long)
     */
    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));
    }

    public static void main(String[] args) {
        IpChannelType ipChannelType = IpChannelType.SCTP;
        if (args.length >= 1 && args[0].equalsIgnoreCase("tcp")) {
            ipChannelType = IpChannelType.TCP;
        }
        System.out.println("IpChannelType="+ipChannelType);

        if (args.length >= 2) {
            TestHarnessMobilityManagement.CLIENT_IP = args[1];
        }
        System.out.println("CLIENT_IP="+ TestHarnessMobilityManagement.CLIENT_IP);

        if (args.length >= 3) {
            TestHarnessMobilityManagement.CLIENT_PORT = Integer.parseInt(args[2]);
        }
        System.out.println("CLIENT_PORT="+ TestHarnessMobilityManagement.CLIENT_PORT);

        if (args.length >= 4) {
            TestHarnessMobilityManagement.SERVER_IP = args[3];
        }
        System.out.println("SERVER_IP="+ TestHarnessMobilityManagement.SERVER_IP);

        if (args.length >= 5) {
            TestHarnessMobilityManagement.SERVER_PORT = Integer.parseInt(args[4]);
        }
        System.out.println("SERVER_PORT="+ TestHarnessMobilityManagement.SERVER_PORT);

        if (args.length >= 6) {
            TestHarnessMobilityManagement.CLIENT_SPC = Integer.parseInt(args[5]);
        }
        System.out.println("CLIENT_SPC="+ TestHarnessMobilityManagement.CLIENT_SPC);

        if (args.length >= 7) {
            TestHarnessMobilityManagement.SERVER_SPC = Integer.parseInt(args[6]);
        }
        System.out.println("SERVER_SPC="+ TestHarnessMobilityManagement.SERVER_SPC);

        if (args.length >= 8) {
            TestHarnessMobilityManagement.NETWORK_INDICATOR = Integer.parseInt(args[7]);
        }
        System.out.println("NETWORK_INDICATOR="+ TestHarnessMobilityManagement.NETWORK_INDICATOR);

        if (args.length >= 9) {
            TestHarnessMobilityManagement.SERVICE_INDICATOR = Integer.parseInt(args[8]);
        }
        System.out.println("SERVICE_INDICATOR="+ TestHarnessMobilityManagement.SERVICE_INDICATOR);

        if (args.length >= 10) {
            TestHarnessMobilityManagement.SSN = Integer.parseInt(args[9]);
        }
        System.out.println("SSN="+ TestHarnessMobilityManagement.SSN);

        if (args.length >= 11) {
            TestHarnessMobilityManagement.ROUTING_CONTEXT = Integer.parseInt(args[10]);
        }
        System.out.println("ROUTING_CONTEXT="+ TestHarnessMobilityManagement.ROUTING_CONTEXT);

        if(args.length >= 12){
            TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Integer.parseInt(args[11]);
        }
        System.out.println("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT=" + TestHarnessMobilityManagement.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);

        final Server server = new Server();
        try {
            server.initializeStack(ipChannelType);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public MAPProvider getMAPProvider() {
        return null;
    }

    @Override
    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        return null;
    }

    @Override
    public boolean isActivated() {
        return false;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public MAPDialogMobility createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference, Long localTrId) throws MAPException {
        return null;
    }

    @Override
    public MAPDialogMobility createNewDialog(MAPApplicationContext mapApplicationContext, SccpAddress sccpCallingPartyAddress, AddressString origReference, SccpAddress sccpCalledPartyAddress, AddressString destReference) throws MAPException {
        return null;
    }

    @Override
    public void addMAPServiceListener(MAPServiceMobilityListener mapServiceMobilityListener) {

    }

    @Override
    public void removeMAPServiceListener(MAPServiceMobilityListener mapServiceMobilityListener) {

    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest sendAuthenticationInfoRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onSendAuthenticationInfoRequest for DialogId=%d", sendAuthenticationInfoRequestIndication
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = sendAuthenticationInfoRequestIndication.getInvokeId();
            MAPDialogMobility mapDialogMobility = sendAuthenticationInfoRequestIndication.getMAPDialog();

            IMSI imsi = sendAuthenticationInfoRequestIndication.getImsi();
            byte[] rand;
            if (imsi.getData().equals("901405105682021"))
                rand = new byte[] {(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                        (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x75};
            else
                rand = new byte[] {(byte) 0xba, 0x73, 0x31, 0x2e, (byte) 0x8b, (byte) 0xa1, 0x19, 0x75, (byte) 0xe0,
                    (byte) 0xe7, (byte) 0xae, 0x2b, (byte) 0xd1, 0x44, (byte) 0xa7, 0x74};
            byte[] xres = new byte[] {(byte) 0xe4, (byte) 0xb9, (byte) 0xca, 0x0c, 0x2b, 0x12, 0x37, (byte) 0xb6};
            byte[] ck = new byte[] {(byte) 0x81, (byte) 0xe8, 0x64, (byte) 0xf0, (byte) 0xc5, 0x0a, 0x53, 0x64, (byte) 0xda,
                    (byte) 0xed, 0x49, 0x76, 0x03, (byte) 0xc9, (byte) 0xbf, 0x5d};
            byte[] ik = new byte[] {(byte) 0x92, 0x59, 0x62, 0x13, (byte) 0xf2, 0x43, 0x75, 0x69, (byte) 0x96, (byte) 0x9d, 0x26,
                    0x0d, (byte) 0xac, 0x60, (byte) 0xbf, 0x6b};
            byte[] autn = new byte[] {0x5a, (byte) 0xcd, 0x63, 0x56, (byte) 0x83, (byte) 0xfe, (byte) 0x80, 0x00, (byte) 0xba,
                    (byte) 0x95, (byte) 0xba, (byte) 0xae, 0x08, 0x0a, 0x30, 0x73};
            AuthenticationQuintuplet authenticationQuintuplet = new AuthenticationQuintupletImpl(rand, xres, ck, ik, autn);
            ArrayList<AuthenticationQuintuplet> authenticationQuintupletList = new ArrayList<>();
            authenticationQuintupletList.add(authenticationQuintuplet);
            QuintupletList quintupletList = new QuintupletListImpl(authenticationQuintupletList);
            AuthenticationSetList authenticationSetList = new AuthenticationSetListImpl(quintupletList);
            byte[] epsRand = new byte[] {(byte) 0xf6, (byte) 0xe2, (byte) 0xc3, (byte) 0xdc, (byte) 0xa4, (byte) 0xca,
                    (byte) 0xae, (byte) 0x9e, 0x4c, (byte) 0xba, 0x0f, (byte) 0xd3, 0x42, 0x72, (byte) 0xee, 0x46};
            byte[] epsXres = new byte[] {0x1e, 0x42, (byte) 0xe6, 0x58, (byte) 0xce, (byte) 0x99, 0x33, (byte) 0xb6};
            byte[] epsAutn = new byte[] {(byte) 0xe9, 0x15, (byte) 0x97, (byte) 0x88, (byte) 0xbc, (byte) 0xeb, (byte) 0x80, 0x00,
                    (byte) 0x81, 0x3f, (byte) 0xc0, 0x40, (byte) 0xff, 0x53, (byte) 0xd5, (byte) 0xfa};
            byte[] epsKasme = new byte[] {0x70, (byte) 0xad, (byte) 0x8c, (byte) 0xd7, (byte) 0x89, 0x28, (byte) 0xc2, (byte) 0xde,
                    (byte) 0x97, (byte) 0xcf, (byte) 0xe7, (byte) 0xb8, (byte) 0xbf, 0x10, 0x40, (byte) 0xe9, (byte) 0xa4, (byte) 0xdd,
                    0x79, (byte) 0x80, 0x5b, 0x54, 0x61, (byte) 0x95, (byte) 0xc2, (byte) 0xb6, 0x0c, (byte) 0xb4, (byte) 0xcc, 0x41, (byte) 0xbe, 0x47};
            EpcAv epcAuthVector = new EpcAvImpl(epsRand, epsXres, epsAutn, epsKasme, null);
            ArrayList<EpcAv> epcAvList = new ArrayList<>();
            epcAvList.add(epcAuthVector);
            EpsAuthenticationSetList epsAuthenticationSetList = new EpsAuthenticationSetListImpl(epcAvList);
            UEUsageType ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x80});

            mapDialogMobility.addSendAuthenticationInfoResponse(invokeId, authenticationSetList, null,
                    epsAuthenticationSetList, ueUsageType);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while answering SendAuthenticationInfoRequest ", e);
        }

    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) {
        logger.error(String.format("ERROR: received SendAuthenticationInfoResponse at the server (acting as HLR) over DialogId=%d", sendAuthenticationInfoResponseIndication
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest updateLocationRequestIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationRequest for DialogId=%d", updateLocationRequestIndication
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            // Create Dialog for MAP CL
            AddressString clDestinationRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
            AddressString clOriginRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

            SccpAddress clClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");

            MAPDialogMobility cancelLocationDialog = mapProvider.getMAPServiceMobility().
                    createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version3),
                            clClientSccpAddress, clOriginRef, clServerSccpAddress, clDestinationRef);

            IMSI imsi = updateLocationRequestIndication.getImsi();
            if (!imsi.getData().equals("901405105682021")) {
                IMSIWithLMSI imsiWithLmsi = null;
                CancellationType cancellationType = CancellationType.updateProcedure;
                MAPExtensionContainer extensionContainer = null;
                TypeOfUpdate typeOfUpdate = null;
                boolean mtrfSupportedAndAuthorized = false;
                boolean mtrfSupportedAndNotAuthorized = false;
                ISDNAddressString newMSCNumber = null; // new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                ISDNAddressString newVLRNumber = null; // new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460000");
                LMSI lmsi = null;
                boolean reattachRequired = false;

                cancelLocationDialog.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                        mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, lmsi, reattachRequired);
                cancelLocationDialog.send();
            }

            long invokeId = updateLocationRequestIndication.getInvokeId();
            MAPDialogMobility insertSubscriberDataDialog = updateLocationRequestIndication.getMAPDialog();

            MAPExtensionContainer extCont = null;
            // imsi
            imsi = null;
            // msisdn
            ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285105682451");
            // category
            Category category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
            // subscriberStatus
            SubscriberStatus subscriberStatus = SubscriberStatus.serviceGranted;
            // bearerServiceList
            ArrayList<ExtBearerServiceCode> bearerServiceList = null;
            // teleserviceList
            ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
            ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
            ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
            teleserviceList.add(shortMessageMT_PP);
            teleserviceList.add(shortMessageMO_PP);
            // provisionedSS
            ArrayList<ExtSSInfo> provisionedSS = new ArrayList<>();
            SSCode clip = new SSCodeImpl(SupplementaryCodeValue.clip);
            ExtSSStatus clipExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
            SSSubscriptionOption clipSubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
            ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
            ExtSSData extSSDataClip = new ExtSSDataImpl(clip, clipExtSSStatus, clipSubscriptionOption, basicServiceGroupList, extCont);
            SSCode clir = new SSCodeImpl(SupplementaryCodeValue.clir);
            ExtSSStatus clirExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
            SSSubscriptionOption clirSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
            ExtSSData extSSDataClir = new ExtSSDataImpl(clir, clirExtSSStatus, clirSubscriptionOption, basicServiceGroupList, extCont);
            ExtSSInfo ssInfoClip = new ExtSSInfoImpl(extSSDataClip);
            ExtSSInfo ssInfoClir = new ExtSSInfoImpl(extSSDataClir);
            provisionedSS.add(ssInfoClip);
            provisionedSS.add(ssInfoClir);
            // odbData
            boolean allOGCallsBarred = false;
            boolean internationalOGCallsBarred = false;
            boolean internationalOGCallsNotToHPLMNCountryBarred = false;
            boolean premiumRateInformationOGCallsBarred = true;
            boolean premiumRateEntertainmentOGCallsBarred = true;
            boolean ssAccessBarred = true;
            boolean interzonalOGCallsBarred = false;
            boolean interzonalOGCallsNotToHPLMNCountryBarred = false;
            boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = false;
            boolean allECTBarred = false;
            boolean chargeableECTBarred = false;
            boolean internationalECTBarred = false;
            boolean interzonalECTBarred = false;
            boolean doublyChargeableECTBarred = true;
            boolean multipleECTBarred = true;
            boolean allPacketOrientedServicesBarred = false;
            boolean roamerAccessToHPLMNAPBarred = false;
            boolean roamerAccessToVPLMNAPBarred = true;
            boolean roamingOutsidePLMNOGCallsBarred = false;
            boolean allICCallsBarred = false;
            boolean roamingOutsidePLMNICCallsBarred = true;
            boolean roamingOutsidePLMNICountryICCallsBarred = false;
            boolean roamingOutsidePLMNBarred = false;
            boolean roamingOutsidePLMNCountryBarred = false;
            boolean registrationAllCFBarred = true;
            boolean registrationCFNotToHPLMNBarred = true;
            boolean registrationInterzonalCFBarred = true;
            boolean registrationInterzonalCFNotToHPLMNBarred = true;
            boolean registrationInternationalCFBarred = true;
            ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred, internationalOGCallsNotToHPLMNCountryBarred,
            premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred, ssAccessBarred,
            interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred, interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
            allECTBarred, chargeableECTBarred, internationalECTBarred, interzonalECTBarred,
            doublyChargeableECTBarred, multipleECTBarred, allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
            roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
            roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred, roamingOutsidePLMNCountryBarred,
            registrationAllCFBarred, registrationCFNotToHPLMNBarred, registrationInterzonalCFBarred,
            registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
            boolean plmnSpecificBarringType1 = true;
            boolean plmnSpecificBarringType2 = false;
            boolean plmnSpecificBarringType3 = false;
            boolean plmnSpecificBarringType4 = false;
            ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
            ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, extCont);
            // roamingRestrictionDueToUnsupportedFeature
            boolean roamingRestrictionDueToUnsupportedFeature = true;
            // regionalSubscriptionData
            ArrayList<ZoneCode> regionalSubscriptionData = null;
            // vbsSubscriptionData
            ArrayList<VoiceBroadcastData> vbsSubscriptionData = new ArrayList<>();
            GroupId gId = new GroupIdImpl("1");
            boolean broadcastInitEntitlement = true;
            LongGroupId lGId = new LongGroupIdImpl("5");
            VoiceBroadcastData voiceBroadcastData = new VoiceBroadcastDataImpl(gId, broadcastInitEntitlement, extCont, lGId);
            vbsSubscriptionData.add(voiceBroadcastData);
            // vgcsSubscriptionData
            ArrayList<VoiceGroupCallData> vgcsSubscriptionData = new ArrayList<>();
            boolean privilegedUplinkRequest = true;
            boolean emergencyUplinkRequest = true;
            boolean emergencyReset = true;
            AdditionalSubscriptions addSubscriptions = new AdditionalSubscriptionsImpl(privilegedUplinkRequest, emergencyUplinkRequest, emergencyReset);
            BitSetStrictLength addInfoBitset = new BitSetStrictLength(136);
            addInfoBitset.set(0);
            addInfoBitset.set(1);
            addInfoBitset.set(24);
            AdditionalInfo addInfo = new AdditionalInfoImpl(addInfoBitset);
            VoiceGroupCallData voiceGroupCallData = new VoiceGroupCallDataImpl(gId, extCont, addSubscriptions, addInfo, lGId);
            vgcsSubscriptionData.add(voiceGroupCallData);
            // vlrCamelSubscriptionInfo
            OBcsmTriggerDetectionPoint oBcsmTDP = OBcsmTriggerDetectionPoint.routeSelectFailure;
            long serviceKey = 7L;
            ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
            DefaultCallHandling defaultCallHandling = DefaultCallHandling.continueCall;
            OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, extCont);
            ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList = new ArrayList<>();
            oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
            Integer camelCapabilityHandling = 2;
            boolean notificationToCSE = true;
            boolean csiActive = true;
            OCSI oCSI = new OCSIImpl(oBcsmCamelTDPDataList, extCont, camelCapabilityHandling, notificationToCSE, csiActive);
            ArrayList<SSCode> ssEventList = new ArrayList<>();
            ssEventList.add(clip);
            ssEventList.add(clir);
            SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, extCont);
            SSCSI ssCsi = new SSCSIImpl(ssCamelData, extCont, notificationToCSE, csiActive);
            ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = getoBcsmCamelTdpCriteria(oBcsmTDP, basicServiceGroupList, extCont);
            boolean tifCsi = true;
            ArrayList<MMCode> mobilityTriggers = new ArrayList<>();
            MMCode mmCode1 = new MMCodeImpl(MMCodeValue.IMSIAttach);
            MMCode mmCode2 = new MMCodeImpl(MMCodeValue.LocationUpdateInSameVLR);
            mobilityTriggers.add(mmCode1);
            mobilityTriggers.add(mmCode2);
            MCSI mcsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extCont, notificationToCSE, csiActive);
            ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<>();
            SMSTriggerDetectionPoint smsTDP = SMSTriggerDetectionPoint.smsDeliveryRequest;
            DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;
            SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, extCont);
            smsCamelTdpDataList.add(smscameltdpData);
            SMSCSI smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = new ArrayList<>();
            TBcsmTriggerDetectionPoint tBcsmTDP1 = TBcsmTriggerDetectionPoint.tNoAnswer;
            TBcsmTriggerDetectionPoint tBcsmTDP2 = TBcsmTriggerDetectionPoint.tBusy;
            TBcsmCamelTDPData tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, extCont);
            TBcsmCamelTDPData tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, extCont);
            tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
            tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
            TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, extCont, camelCapabilityHandling, notificationToCSE, csiActive);
            TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tNoAnswer;
            ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<>();
            CauseValue tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
            CauseValue tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
            tCauseValueCriteria.add(tcv1);
            tCauseValueCriteria.add(tcv2);
            TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceGroupList, tCauseValueCriteria);
            ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<>();
            tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
            ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<>();
            ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
            DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, extCont);
            dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
            DCSI dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extCont, notificationToCSE, csiActive);
            ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<>();
            ArrayList<MTSMSTPDUType> mtsmstpduTypeArrayList = new ArrayList<>();
            MTSMSTPDUType mtsmstpduType1 = MTSMSTPDUType.smsDELIVER;
            MTSMSTPDUType mtsmstpduType2 = MTSMSTPDUType.smsSUBMITREPORT;
            MTSMSTPDUType mtsmstpduType3 = MTSMSTPDUType.smsSTATUSREPORT;
            mtsmstpduTypeArrayList.add(mtsmstpduType1);
            mtsmstpduTypeArrayList.add(mtsmstpduType2);
            mtsmstpduTypeArrayList.add(mtsmstpduType3);
            MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
            mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCSI, extCont,
                    ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mcsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCSI, mtSmsCSI,
                    mtSmsCamelTdpCriteriaList);
            // naeaPreferredCI
            String carrierCode = "458";
            NetworkIdentificationPlanValue networkIdentificationPlanValue = NetworkIdentificationPlanValue.spare_1;
            NetworkIdentificationTypeValue networkIdentificationTypeValue = NetworkIdentificationTypeValue.nationalNetworkIdentification;
            NAEACIC naeaPreferredCIC = new NAEACICImpl(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
            NAEAPreferredCI naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, extCont);
            // gprsSubscriptionData
            GPRSSubscriptionData gprsSubscriptionData = null;
            // roamingRestrictedInSgsnDueToUnsupportedFeature
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature = false;
            // networkAccessMode
            NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;
            // lsaInformation
            LSAInformation lsaInformation = null;
            // lmuIndicator
            boolean lmuIndicator = false;
            // lcsInformation
            LCSInformation lcsInformation = getLcsInformation();
            // istAlertTimer
            Integer istAlertTimer = 200;
            // superChargerSupportedInHLR
            AgeIndicator superChargerSupportedInHLR = null;
            // mcSsInfo
            SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
            ExtSSStatus ssStatus = new ExtSSStatusImpl(true, false, true, false);
            int nbrSB = 2;
            int nbrUser = 4;
            MCSSInfo mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, nbrSB, nbrUser, extCont);
            // csAllocationRetentionPriority
            CSAllocationRetentionPriority csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);
            // sgsnCamelSubscriptionInfo
            SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
            // chargingCharacteristics
            boolean isNormalCharging = false;
            boolean isPrepaidCharging = false;
            boolean isFlatRateCharging = true;
            boolean isChargingByHotBillingCharging = false;
            ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
            // accessRestrictionData
            boolean utranNotAllowed = false;
            boolean geranNotAllowed = false;
            boolean ganNotAllowed = true;
            boolean eUtranNotAllowed = false;
            boolean iHspaEvolutionNotAllowed = false;
            boolean hoToNon3GppAccessNotAllowed = true;
            AccessRestrictionData accessRestrictionData = new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed, eUtranNotAllowed, hoToNon3GppAccessNotAllowed);
            // icsIndicator
            Boolean icsIndicator = Boolean.TRUE;
            // epsSubscriptionData
            int defaultContext = 1;
            boolean completeDataListIncluded = true;
            PDNType pDNType = new PDNTypeImpl(PDNTypeValue.IPv4v6);
            PDPAddress servedPartyIPIPv4Address = null;
            APN apn = new APNImpl("internet");
            QoSClassIdentifier qci = QoSClassIdentifier.QCI_5;
            int priorityLevel = 9;
            Boolean preEmptionCapability = true;
            Boolean preEmptionVulnerability = false;
            AllocationRetentionPriority arp = new AllocationRetentionPriorityImpl(priorityLevel, preEmptionCapability, preEmptionVulnerability, extCont);
            EPSQoSSubscribed ePSQoSSubscribed = new EPSQoSSubscribedImpl(qci, arp, extCont);
            PDNGWIdentity pdnGwIdentity = null;
            PDNGWAllocationType pdnGwAllocationType = null;
            boolean vplmnAddressAllowed = true;
            int maxRequestedBandwidthUL = 2048;
            int maxRequestedBandwidthDL = 4096;
            AMBR ambr = new AMBRImpl(maxRequestedBandwidthUL, maxRequestedBandwidthDL, extCont);
            ArrayList<SpecificAPNInfo> specificAPNInfoList = null;
            APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
            SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
            LIPAPermission lipaPermission = LIPAPermission.lipaConditional;
            int contextId = 1;
            PDPAddress servedPartyIPIPv6Address = new PDPAddressImpl(new byte[] { 21 });
            APNConfiguration apnConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn,
                    ePSQoSSubscribed, pdnGwIdentity, pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr,
                    specificAPNInfoList, extCont, servedPartyIPIPv6Address, apnOiReplacement, sipToPermission, lipaPermission);
            ArrayList<APNConfiguration> ePSDataList = new ArrayList<>();
            ePSDataList.add(apnConfiguration);
            APNConfigurationProfile apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded,
                    ePSDataList, extCont);
            Integer rfspId = 0;
            ISDNAddressString stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    "491710490000");
            boolean mpsCSPriority = true;
            boolean mpsEPSPriority = true;
            EPSSubscriptionData epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile,
                    stnSr, extCont, mpsCSPriority, mpsEPSPriority);
            // csgSubscriptionDataList
            BitSetStrictLength csgIdBitSet = new BitSetStrictLength(27);
            csgIdBitSet.set(0);
            csgIdBitSet.set(1);
            csgIdBitSet.set(25);
            csgIdBitSet.set(26);
            CSGId csgId = new CSGIdImpl(csgIdBitSet);
            int year = 2024;
            int month = 7;
            int day = 4;
            int hour = 19;
            int minute = 20;
            int second = 10;
            Time expirationDate = new TimeImpl(year, month, day, hour, minute, second);
            ArrayList<APN> lipaAllowedAPNList = new ArrayList<>();
            lipaAllowedAPNList.add(apn);
            CSGSubscriptionData csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, extCont, lipaAllowedAPNList);
            ArrayList<CSGSubscriptionData> csgSubscriptionDataList = new ArrayList<>();
            csgSubscriptionDataList.add(csgSubscriptionData);
            // ueReachabilityRequestIndicator
            boolean ueReachabilityRequestIndicator = true;
            // sgsnNumber
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                    "491710490000");
            // mmeName
            DiameterIdentity mmeName = new DiameterIdentityImpl("mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
            // subscribedPeriodicRAUTAUtimer
            Long subscribedPeriodicRAUTAUtimer = 300L;
            // vplmnLIPAAllowed
            boolean vplmnLIPAAllowed = true;
            // mdtUserConsent
            boolean mdtUserConsent = false;
            // subscribedPeriodicLAUtimer
            Long subscribedPeriodicLAUtimer = 360L;
            // vplmnCSGSubscriptionDataList
            ArrayList<CSGSubscriptionData> vplmnCSGSubscriptionDataList = new ArrayList<>();
            vplmnCSGSubscriptionDataList.add(csgSubscriptionData);
            // additionalMSISDN
            ISDNAddressString additionalMSISDN =
                    new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917105682451");
            // psAndSMSOnlyServiceProvision
            boolean psAndSMSOnlyServiceProvision = true;
            // smsInSGSNAllowed
            boolean smsInSGSNAllowed = true;
            // csToPsSRVCCAllowedIndicator
            boolean csToPsSRVCCAllowedIndicator = true;
            // pcscfRestorationRequest
            boolean pcscfRestorationRequest = true;
            // adjacentAccessRestrictionDataList
            int mcc = 262;
            int mnc = 999;
            PlmnId plmnId = new PlmnIdImpl(mcc, mnc);
            boolean nrAsSecondaryRATNotAllowed = true;
            boolean unlicensedSpectrumAsSecondaryRATNotAllowed = false;
            ExtAccessRestrictionData extAccessRestrictionData =
                    new ExtAccessRestrictionDataImpl(nrAsSecondaryRATNotAllowed, unlicensedSpectrumAsSecondaryRATNotAllowed); // extAccessRestrictionData
            AdjacentAccessRestrictionData adjacentAccessRestrictionData =
                    new AdjacentAccessRestrictionDataImpl(plmnId, accessRestrictionData, extAccessRestrictionData);
            ArrayList<AdjacentAccessRestrictionData> adjacentAccessRestrictionDataList = new ArrayList<>();
            adjacentAccessRestrictionDataList.add(adjacentAccessRestrictionData);
            // imsiGroupIdList
            Long groupServiceId = 1L;
            LocalGroupId localGroupId = new LocalGroupIdImpl("120".getBytes(StandardCharsets.UTF_8));
            IMSIGroupId imsiGroupId = new IMSIGroupIdImpl(groupServiceId, plmnId, localGroupId);
            ArrayList<IMSIGroupId> imsiGroupIdList = new ArrayList<>();
            imsiGroupIdList.add(imsiGroupId);
            // ueUsageType
            UEUsageType ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x87});
            // userPlaneIntegrityProtectionIndicator
            boolean userPlaneIntegrityProtectionIndicator = true;
            // dlBufferingSuggestedPacketCount
            Long dlBufferingSuggestedPacketCount = 0L;
            // resetIdList
            ArrayList<ResetId> resetIdList = new ArrayList<>();
            ResetId resetId = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x80});
            resetIdList.add(resetId);
            // eDRXCycleLengthList
            UsedRATType usedRATType = UsedRATType.nbIoT;
            byte[] edrCycleLengthVal = new byte[] { 0x02 };
            EDRXCycleLengthValue eDRXCycleLengthValue = new EDRXCycleLengthValueImpl(edrCycleLengthVal);
            EDRXCycleLength edrxCycleLength = new EDRXCycleLengthImpl(usedRATType, eDRXCycleLengthValue);
            ArrayList<EDRXCycleLength> eDRXCycleLengthList = new ArrayList<>();
            eDRXCycleLengthList.add(edrxCycleLength);
            // iabOperationAllowedIndicator
            boolean iabOperationAllowedIndicator = true;

            insertSubscriberDataDialog.addInsertSubscriberDataRequest(invokeId, imsi, msisdn, category, subscriberStatus,
                    bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                    regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extCont,
                    naeaPreferredCI, gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode,
                    lsaInformation, lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority,
                    sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                    ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                    subscribedPeriodicLAUtimer, vplmnCSGSubscriptionDataList, additionalMSISDN, psAndSMSOnlyServiceProvision,
                    csToPsSRVCCAllowedIndicator, smsInSGSNAllowed, pcscfRestorationRequest, adjacentAccessRestrictionDataList, imsiGroupIdList,
                    ueUsageType, userPlaneIntegrityProtectionIndicator, dlBufferingSuggestedPacketCount, resetIdList,
                    eDRXCycleLengthList, extAccessRestrictionData, iabOperationAllowedIndicator);

            insertSubscriberDataDialog.send();

        } catch (Exception e) {
            logger.error("ERROR while processing onUpdateLocationRequest ", e);
        }
    }

    private static ArrayList<OBcsmCamelTdpCriteria> getoBcsmCamelTdpCriteria(OBcsmTriggerDetectionPoint oBcsmTDP, ArrayList<ExtBasicServiceCode> basicServiceGroupList, MAPExtensionContainer mapExtensionContainer) {
        DestinationNumberCriteria destinationNumberCriteria = getDestinationNumberCriteria();
        CallTypeCriteria callTypeCriteria = CallTypeCriteria.notForwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<>();
        CauseValue causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
        CauseValue causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        oCauseValueCriteria.add(causeValue1);
        oCauseValueCriteria.add(causeValue2);
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<>();
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
                basicServiceGroupList, callTypeCriteria, oCauseValueCriteria, mapExtensionContainer);
        Result result = new Result(oBcsmCamelTDPCriteriaList, oBcsmCamelTdpCriteria);
        result.oBcsmCamelTDPCriteriaList.add(result.oBcsmCamelTdpCriteria);
        return result.oBcsmCamelTDPCriteriaList;
    }

    private static DestinationNumberCriteria getDestinationNumberCriteria() {
        MatchType matchType = MatchType.enabling;
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<>();
        ISDNAddressString destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        destinationNumberList.add(destinationNumber);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<>();
        destinationNumberLengthList.add(1);
        return new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
    }

    private static class Result {
        public final ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
        public final OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria;

        public Result(ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria) {
            this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
            this.oBcsmCamelTdpCriteria = oBcsmCamelTdpCriteria;
        }
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse updateLocationResponseIndication) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onUpdateLocationResponse for DialogId=%d", updateLocationResponseIndication
                    .getMAPDialog().getLocalDialogId()));
        }
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest insertSubscriberDataRequest) {
        logger.error(String.format("ERROR: received InsertSubscriberDataRequest at the server (acting as HLR) over DialogId=%d", insertSubscriberDataRequest
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse insertSubscriberDataResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onInsertSubscriberDataResponse for DialogId=%d", insertSubscriberDataResponse
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = insertSubscriberDataResponse.getInvokeId();
            MAPDialogMobility mapDialogMobility = insertSubscriberDataResponse.getMAPDialog();
            ISDNAddressString hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "882285000008002");
            boolean addCapability = true;
            boolean pagingAreaCapability = true;
            mapDialogMobility.addUpdateLocationResponse(invokeId, hlrNumber, null, addCapability, pagingAreaCapability);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataResponse ", e);
        }
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest cancelLocationRequest) {
        logger.error(String.format("ERROR: received CancelLocationRequest at the server (acting as HLR) over DialogId=%d", cancelLocationRequest
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse cancelLocationResponse) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onCancelLocationResponse for DialogId=%d", cancelLocationResponse
                    .getMAPDialog().getLocalDialogId()));
        }
        // Start a new CL with subscription withdraw and reattach procedure (simulating an OSS request)
        new Thread(new SubscriptionWithdrawReattach(this)).start();
        // Send a random PSI request
        new Thread(new PSISender(this)).start();
        // Start a new DSD request (simulating an OSS request)
        new Thread(new DsdSender(this)).start();
        // Start a new RST request (simulating an HSS restart)
        new Thread(new RSTSender(this)).start();
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest updateGprsLocationRequest) {

    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse updateGprsLocationResponse) {

    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest purgeMSRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("onPurgeMSRequest for DialogId=%d", purgeMSRequest
                    .getMAPDialog().getLocalDialogId()));
        }
        try {
            long invokeId = purgeMSRequest.getInvokeId();
            MAPDialogMobility mapDialogMobility = purgeMSRequest.getMAPDialog();
            int ssn = purgeMSRequest.getMAPDialog().getRemoteAddress().getSubsystemNumber();
            boolean freezeTMSI = false;
            boolean freezePTMSI = false;
            if (ssn == 7)
                freezeTMSI = true;
            else if (ssn == 149)
                freezePTMSI = true;
            boolean freezeMTMSI = false;
            mapDialogMobility.addPurgeMSResponse(invokeId, freezeTMSI, freezePTMSI, null, freezeMTMSI);

            mapDialogMobility.close(false);

        } catch (MAPException e) {
            logger.error("Error while processing InsertSubscriberDataResponse ", e);
        }
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse purgeMSResponse) {
        logger.error(String.format("Received PurgeMSResponse at the server (acting as HLR) over DialogId=%d", purgeMSResponse
                .getMAPDialog().getLocalDialogId()));
    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest sendIdentificationRequest) {

    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse sendIdentificationResponse) {

    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest authenticationFailureReportRequestIndication) {

    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse authenticationFailureReportResponseIndication) {

    }

    @Override
    public void onResetRequest(ResetRequest resetRequestIndication) {

    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest forwardCheckSSIndicationRequestIndication) {

    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest restoreDataRequestIndication) {

    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse restoreDataResponseIndication) {

    }

    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest anyTimeInterrogationRequest) {

    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse anyTimeInterrogationResponse) {

    }

    @Override
    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest anyTimeSubscriptionInterrogationRequest) {

    }

    @Override
    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse anyTimeSubscriptionInterrogationResponse) {

    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest provideSubscriberInfoRequest) {

    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse provideSubscriberInfoResponse) {

    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest deleteSubscriberDataRequest) {

    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse deleteSubscriberDataResponse) {

    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest checkImeiRequest) {
        if (logger.isDebugEnabled()) {
            logger.debug("onCheckImeiRequest for DialogId=%d" +checkImeiRequest
                    .getMAPDialog().getLocalDialogId()+"; MAP CHI=" +checkImeiRequest);
        }
        long invokeId = checkImeiRequest.getInvokeId();
        MAPDialogMobility mapDialogMobility = checkImeiRequest.getMAPDialog();
        BitSetStrictLength bsA = new BitSetStrictLength(128);
        bsA.set(0);
        bsA.set(120);
        UESBIIuA uesbiIuA = new UESBIIuAImpl(bsA);
        BitSetStrictLength bsB = new BitSetStrictLength(128);
        bsA.set(1);
        bsA.set(127);
        UESBIIuB uesbiIuB = new UESBIIuBImpl(bsB);
        UESBIIu uesbiIu = new UESBIIuImpl(uesbiIuA, uesbiIuB);
        try {
            mapDialogMobility.addCheckImeiResponse(invokeId, EquipmentStatus.blackListed, uesbiIu, null);
            mapDialogMobility.close(false);
        } catch (MAPException e) {
            logger.error("MAP Exception while processing onCheckImeiRequest ", e);
        }
    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse checkImeiResponse) {

    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility activateTraceModeRequestMobilityIndication) {

    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility activateTraceModeResponseMobilityIndication) {

    }

    private static class SubscriptionWithdrawReattach implements Runnable {

        private final Server server4SubscriptionWithdrawReattach;

        public SubscriptionWithdrawReattach(Server server) {
            server4SubscriptionWithdrawReattach = server;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                logger.debug("On Subscription Withdraw and Reattach command, about to send CL");
                server4SubscriptionWithdrawReattach.sendCLOnSubWithdrawAndReattach();
            } catch (InterruptedException e) {
                logger.error("Error: " + e.getMessage());
            }
        }
    }

    private void sendCLOnSubWithdrawAndReattach() {
        try {
            AddressString clDestinationRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
            AddressString clOriginRef = mapProvider.getMAPParameterFactory()
                    .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

            SccpAddress clClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
            SccpAddress clServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");

            MAPDialogMobility cancelLocationDialog;
            cancelLocationDialog = mapProvider.getMAPServiceMobility().createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version3),
                    clClientSccpAddress, clOriginRef, clServerSccpAddress, clDestinationRef);

            IMSI imsi = new IMSIImpl("901405105682021");
            IMSIWithLMSI imsiWithLmsi = null;
            CancellationType cancellationType = CancellationType.subscriptionWithdraw;
            MAPExtensionContainer extensionContainer = null;
            TypeOfUpdate typeOfUpdate = null;
            boolean mtrfSupportedAndAuthorized = false;
            boolean mtrfSupportedAndNotAuthorized = false;
            ISDNAddressString newMSCNumber = null;
            ISDNAddressString newVLRNumber = null;
            LMSI lmsi = null;
            boolean reattachRequired = true;

            cancelLocationDialog.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                    mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, lmsi, reattachRequired);
            cancelLocationDialog.send();

        } catch (MAPException e) {
            throw new RuntimeException(e);
        }
    }

    private static class PSISender implements Runnable {

        private final Server server4PsiSender;

        public PSISender(Server server) {
            server4PsiSender = server;
        }

        public Server getServer4PsiSender() {
            return server4PsiSender;
        }

        @Override
        public void run() {
            imsiForSenders++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception on "+getServer4PsiSender()+", " +ie.getMessage());
            }

            try {
                // Create Dialog for MAP PSI
                AddressString psiDestinationRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
                AddressString psiOriginRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

                SccpAddress psiClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
                SccpAddress psiServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().
                        createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberInfoEnquiryContext, MAPApplicationContextVersion.version3),
                                psiClientSccpAddress, psiOriginRef, psiServerSccpAddress, psiDestinationRef);

                Random rand = new Random();
                IMSI imsi = new IMSIImpl(String.valueOf(imsiForSenders));
                byte[] lmsiByte;
                LMSI lmsi;
                switch (rand.nextInt(10) + 1) {
                    case 1:
                        lmsiByte = new byte[] {114, 2, (byte) 233, (byte) 140};
                        lmsi = new LMSIImpl(lmsiByte);
                        break;
                    case 2:
                        lmsiByte = new byte[] {113, (byte) 255, (byte) 172, (byte) 206};
                        lmsi = new LMSIImpl(lmsiByte);
                        break;
                    case 3:
                        lmsiByte = new byte[] {114, 2, (byte) 235, 55};
                        lmsi = new LMSIImpl(lmsiByte);
                        break;
                    case 4:
                        lmsiByte = new byte[] {114, 2, (byte) 231, (byte) 213};
                        lmsi = new LMSIImpl(lmsiByte);
                        break;
                    default:
                        lmsi = null;
                        break;
                }
                boolean locationInformation;
                boolean subscriberState;
                MAPExtensionContainer extensionContainer = null;
                DomainType requestedDomain = DomainType.csDomain;
                boolean imei;
                boolean msClassmark;
                boolean mnpRequestedInfo;
                boolean currentLocation; // currentLocation shall be absent if locationInformation is absent
                boolean tadsData = false; // t-adsData shall be absent in messages sent to the VLR
                RequestedNodes requestedNodes = null; // requestedNodes shall be absent if requestedDomain is "cs-Domain"
                boolean servingNodeIndication;
                /*
                servingNodeIndication shall be absent if locationInformation is absent;
                servingNodeIndication shall be absent if current location is present;
                servingNodeIndication indicates by its presence that only the serving node's address (MME-Name or SGSN-Number or VLR-Number) is requested.
                 */
                boolean locationInformationEPSSupported; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                boolean localTimeZoneRequest;
                RequestedInfo requestedInfo = null;
                EMLPPPriority callPriority = null;
                switch (rand.nextInt(7) + 1) {
                    case 1:
                        locationInformation = true;
                        subscriberState = true;
                        imei = false;
                        msClassmark = false;
                        mnpRequestedInfo = false;
                        currentLocation = true; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = true; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = false;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        callPriority = EMLPPPriority.priorityLevel1;
                        break;
                    case 2:
                        locationInformation = true;
                        subscriberState = true;
                        imei = true;
                        msClassmark = true;
                        mnpRequestedInfo = true;
                        currentLocation = true; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = true; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = true;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        break;
                    case 3:
                        locationInformation = false;
                        subscriberState = true;
                        imei = false;
                        msClassmark = false;
                        mnpRequestedInfo = false;
                        currentLocation = true; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = true; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = false;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        callPriority = EMLPPPriority.priorityLevelA;
                        break;
                    case 4:
                        locationInformation = true;
                        subscriberState = true;
                        imei = true;
                        msClassmark = true;
                        mnpRequestedInfo = false;
                        currentLocation = false; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = true; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = false; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = false;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        callPriority = EMLPPPriority.priorityLevel4;
                        break;
                    case 5:
                        locationInformation = false;
                        subscriberState = true;
                        imei = true;
                        msClassmark = true;
                        mnpRequestedInfo = true;
                        currentLocation = false; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = false; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = false;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        callPriority = EMLPPPriority.priorityLevel2;
                        break;
                    case 6:
                        locationInformation = true;
                        subscriberState = true;
                        imei = true;
                        msClassmark = false;
                        mnpRequestedInfo = false;
                        currentLocation = true; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = true; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = true;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        break;
                    case 7:
                        locationInformation = true;
                        subscriberState = true;
                        imei = true;
                        msClassmark = true;
                        mnpRequestedInfo = true;
                        currentLocation = true; // currentLocation shall be absent if locationInformation is absent
                        servingNodeIndication = false; // servingNodeIndication shall be absent if current location is present;
                        locationInformationEPSSupported = true; // locationInformationEPS-Supported shall be absent if locationInformation is absent
                        localTimeZoneRequest = true;
                        requestedInfo = new RequestedInfoImpl(locationInformation, subscriberState, extensionContainer,
                                currentLocation, requestedDomain, imei, msClassmark, mnpRequestedInfo, tadsData, requestedNodes,
                                servingNodeIndication, locationInformationEPSSupported, localTimeZoneRequest);
                        callPriority = EMLPPPriority.priorityLevelB;
                        break;
                    default:
                        break;
                }

                mapDialogMobility.addProvideSubscriberInfoRequest(imsi, lmsi, requestedInfo, null, callPriority);
                mapDialogMobility.send();

            } catch (MAPException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static class DsdSender implements Runnable {

        private final Server server4DsdSender;

        public DsdSender(Server server) {
            server4DsdSender = server;
        }

        public Server getServer4DsdSender() {
            return server4DsdSender;
        }

        @Override
        public void run() {
            imsiForSenders++;
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception on "+getServer4DsdSender()+", " +ie.getMessage());
            }
            try {
                // Create Dialog for MAP DSD
                AddressString dsdDestinationRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
                AddressString dsdOriginRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

                SccpAddress dsdClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
                SccpAddress dsdServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().
                        createNewDialog(MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version3),
                                dsdClientSccpAddress, dsdOriginRef, dsdServerSccpAddress, dsdDestinationRef);

                Random rand = new Random();
                IMSI imsi = new IMSIImpl(String.valueOf(imsiForSenders));
                ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
                BearerServiceCodeValue bearerServiceCodeValue = getBearerServiceCodeValue();
                ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
                ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode);
                TeleserviceCodeValue teleserviceCodeValue = getTeleserviceCodeValue();
                ExtTeleserviceCode extTeleserviceCode = new ExtTeleserviceCodeImpl(teleserviceCodeValue);
                ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode);
                basicServiceList.add(extBasicServiceCode1);
                basicServiceList.add(extBasicServiceCode2);
                ArrayList<SSCode> ssList = new ArrayList<>();
                SupplementaryCodeValue supplementaryCodeValue = getSupplementaryCodeValue();
                SSCode ssCode = new SSCodeImpl(supplementaryCodeValue);
                ssList.add(ssCode);
                boolean roamingRestrictionDueToUnsupportedFeature = true;
                ZoneCode regionalSubscriptionIdentifier = new ZoneCodeImpl(new byte[] {0x21, 0x0F});
                boolean vbsGroupIndication = true;
                boolean vgcsGroupIndication = true;
                boolean camelSubscriptionInfoWithdraw = true;
                GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = null;
                boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = true;
                LSAInformationWithdraw lsaInformationWithdraw = null;
                switch (rand.nextInt(2 + 1)) {
                    case 1:
                        ArrayList<LSAIdentity> lsaIdentityList = new ArrayList<>();
                        LSAIdentity lsaIdentity1 = new LSAIdentityImpl(new byte[]{12, 10, 1});
                        LSAIdentity lsaIdentity2 = new LSAIdentityImpl(new byte[]{12, 12, 2});
                        lsaIdentityList.add(lsaIdentity1);
                        lsaIdentityList.add(lsaIdentity2);
                        lsaInformationWithdraw = new LSAInformationWithdrawImpl(lsaIdentityList);
                        break;
                    case 2:
                        lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
                        break;
                }
                boolean gmlcListWithdraw = true;
                boolean istInformationWithdraw = true;
                SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false,
                        false, false, false, false);
                boolean chargingCharacteristicsWithdraw = true;
                boolean stnSrWithdraw = true;
                EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = null;
                switch (rand.nextInt(3 + 1)) {
                    case 1:
                        ArrayList<Integer> contextIdList = new ArrayList<>();
                        contextIdList.add(1);
                        contextIdList.add(2);
                        epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
                        break;
                    case 2:
                        epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(true);
                        break;
                }
                boolean apnOiReplacementWithdraw = true;
                boolean csgSubscriptionDeleted = true;
                boolean subscribedPeriodicTAURAUTimerWithdraw = true;
                boolean subscribedPeriodicLAUTimerWithdraw = true;
                boolean subscribedVsrvccWithdraw = true;
                boolean vplmnCsgSubscriptionDeleted = true;
                boolean additionalMSISDNWithdraw = true;
                boolean csToPsSRVCCWithdraw = true;
                boolean imsiGroupIdListWithdraw = true;
                boolean userPlaneIntegrityProtectionWithdraw = true;
                boolean dlBufferingSuggestedPacketCountWithdraw = true;
                boolean ueUsageTypeWithdraw = true;
                boolean resetIdsWithdraw = true;
                boolean iabOperationWithdraw = true;

                mapDialogMobility.addDeleteSubscriberDataRequest(imsi, basicServiceList, ssList,
                        roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication,
                        camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature,
                        lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw,
                        epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted, subscribedPeriodicTAURAUTimerWithdraw,
                        subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted, additionalMSISDNWithdraw, csToPsSRVCCWithdraw,
                        imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw, dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw,
                        iabOperationWithdraw);
                mapDialogMobility.send();

            } catch (MAPException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class RSTSender implements Runnable {

        private final Server client4RstSender;

        public RSTSender(Server server) {
            client4RstSender = server;
        }

        private Server getServer() {
            return client4RstSender;
        }

        @Override
        public void run() {
            imsiForSenders++;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                logger.error("Interrupted Exception on "+getServer()+", " +ie.getMessage());
            }

            try {
                // Create Dialog for MAP RST
                AddressString rstDestinationRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, SCCP_SERVER_ADDRESS);
                AddressString rstOriginRef = mapProvider.getMAPParameterFactory()
                        .createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "491710400000");

                SccpAddress rstClientSccpAddress = createSccpAddress(ROUTING_INDICATOR, SERVER_SPC, HLR_SSN, SCCP_SERVER_ADDRESS);
                SccpAddress rstServerSccpAddress = createSccpAddress(ROUTING_INDICATOR, CLIENT_SPC, VLR_SSN, "491710400000");

                MAPApplicationContext mapApplicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version2);
                MAPDialogMobility mapDialogMobility = mapProvider.getMAPServiceMobility().createNewDialog(mapApplicationContext,
                        rstClientSccpAddress, rstOriginRef, rstServerSccpAddress, rstDestinationRef);

                Random rand = new Random();
                switch (rand.nextInt(6 + 1)) {
                    case 1:
                        ISDNAddressString hlrNumber1 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490001");
                        mapDialogMobility.addResetRequest(null, hlrNumber1, null);
                        break;
                    case 2:
                        ISDNAddressString hlrNumber2 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490002");
                        IMSI imsi21 = new IMSIImpl("748026800000000");
                        IMSI imsi22 = new IMSIImpl("748026900000000");
                        IMSI imsi23 = new IMSIImpl("748027000000000");
                        ArrayList<IMSI> hlrList2 = new ArrayList<>();
                        hlrList2.add(imsi21);
                        hlrList2.add(imsi22);
                        hlrList2.add(imsi23);
                        mapDialogMobility.addResetRequest(null, hlrNumber2, hlrList2);
                        break;
                    case 3:
                        // sendingNodeNumber
                        ISDNAddressString cssNumber3 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490013");
                        SendingNodeNumber sendingNodenumber3 = new SendingNodeNumberImpl(null, cssNumber3);
                        // hlrList
                        IMSI imsi31 = new IMSIImpl("748026800000000");
                        IMSI imsi32 = new IMSIImpl("748026900000000");
                        ArrayList<IMSI> hlrList3 = new ArrayList<>();
                        hlrList3.add(imsi31);
                        hlrList3.add(imsi32);
                        mapDialogMobility.addResetRequest(sendingNodenumber3, hlrList3, null, null, null, null);
                        break;
                    case 4:
                        // sendingNodeNumber
                        ISDNAddressString hlrNumber4 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490004");
                        SendingNodeNumber sendingNodenumber4 = new SendingNodeNumberImpl(hlrNumber4, null);
                        // resetIdList
                        ArrayList<ResetId> resetIdList4 = new ArrayList<>();
                        ResetId resetId41 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x81});
                        ResetId resetId42 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
                        resetIdList4.add(resetId41);
                        resetIdList4.add(resetId42);
                        mapDialogMobility.addResetRequest(sendingNodenumber4, null, null, resetIdList4, null, null);
                        break;
                    case 5:
                        // sendingNodeNumber
                        ISDNAddressString hlrNumber5 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490005");
                        SendingNodeNumber sendingNodenumber5 = new SendingNodeNumberImpl(hlrNumber5, null);
                        // resetIdList
                        ArrayList<ResetId> resetIdList5 = new ArrayList<>();
                        ResetId resetId51 = new ResetIdImpl(new byte[] {(byte) 0x81});
                        ResetId resetId52 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
                        resetIdList5.add(resetId51);
                        resetIdList5.add(resetId52);
                        // subscriptionData
                        InsertSubscriberDataArgs subscriptionData = getSubscritionDataForReset();
                        mapDialogMobility.addResetRequest(sendingNodenumber5, null, null, resetIdList5, subscriptionData, null);
                        break;
                    case 6:
                        // sendingNodeNumber
                        ISDNAddressString cssNumber6 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490016");
                        SendingNodeNumber sendingNodenumber6 = new SendingNodeNumberImpl(null, cssNumber6);
                        // resetIdList
                        ArrayList<ResetId> resetIdList6 = new ArrayList<>();
                        ResetId resetId61 = new ResetIdImpl(new byte[] {(byte) 0x82});
                        ResetId resetId62 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
                        resetIdList6.add(resetId61);
                        resetIdList6.add(resetId62);
                        // subscriptionDataDeletion
                        DeleteSubscriberDataArgs subscriptionDataDeletion = getSubscriptionDataDeletionForReset();
                        mapDialogMobility.addResetRequest(sendingNodenumber6, null, null, resetIdList6, null, subscriptionDataDeletion);
                        break;
                }

                mapDialogMobility.send();

            } catch (MAPException e) {
                logger.error("MAPException while adding MAP RST to MAP dialog", e);
            } catch (Exception e) {
                logger.error("Exception while adding MAP RST to MAP dialog", e);
            }
        }
    }

    private static InsertSubscriberDataArgs getSubscritionDataForReset() throws MAPException {
        // imsi
        IMSI imsi = new IMSIImpl("748026800000000");
        // msisdn
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        // category
        Category category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus
        SubscriberStatus subscriberStatus = SubscriberStatus.serviceGranted;
        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = null;
        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
        ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
        teleserviceList.add(shortMessageMT_PP);
        teleserviceList.add(shortMessageMO_PP);
        // provisionedSS
        ArrayList<ExtSSInfo> provisionedSS = new ArrayList<>();
        SSCode ssCode1 = new SSCodeImpl(getSupplementaryCodeValue());
        ExtSSStatus ssCode1ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        SSSubscriptionOption ssCode1SubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue = getBearerServiceCodeValue();
        ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCodeImpl(bearerServiceCodeValue);
        ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode);
        TeleserviceCodeValue teleserviceCodeValue = getTeleserviceCodeValue();
        ExtTeleserviceCode extTeleserviceCode = new ExtTeleserviceCodeImpl(teleserviceCodeValue);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        ExtSSData extSSData1 = new ExtSSDataImpl(ssCode1, ssCode1ExtSSStatus, ssCode1SubscriptionOption, basicServiceList, null);
        SSCode ssCode2 = new SSCodeImpl(getSupplementaryCodeValue());
        ExtSSStatus ssCode2ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        SSSubscriptionOption ssCode2SubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
        ExtSSData extSSData2 = new ExtSSDataImpl(ssCode2, ssCode2ExtSSStatus, ssCode2SubscriptionOption, basicServiceList, null);
        ExtSSInfo extSSInfo1= new ExtSSInfoImpl(extSSData1);
        ExtSSInfo extSSInfo2 = new ExtSSInfoImpl(extSSData2);
        provisionedSS.add(extSSInfo1);
        provisionedSS.add(extSSInfo2);
        // odbData
        ODBGeneralData oDBGeneralData = getOdbGeneralData();
        boolean plmnSpecificBarringType1 = true;
        boolean plmnSpecificBarringType2 = false;
        boolean plmnSpecificBarringType3 = false;
        boolean plmnSpecificBarringType4 = false;
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        // regionalSubscriptionData
        ArrayList<ZoneCode> regionalSubscriptionData = null;
        // vbsSubscriptionData
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = new ArrayList<>();
        GroupId gId = new GroupIdImpl("1");
        boolean broadcastInitEntitlement = true;
        LongGroupId lGId = new LongGroupIdImpl("5");
        VoiceBroadcastData voiceBroadcastData = new VoiceBroadcastDataImpl(gId, broadcastInitEntitlement, null, lGId);
        vbsSubscriptionData.add(voiceBroadcastData);
        // vgcsSubscriptionData
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = new ArrayList<>();
        boolean privilegedUplinkRequest = true;
        boolean emergencyUplinkRequest = true;
        boolean emergencyReset = true;
        AdditionalSubscriptions addSubscriptions = new AdditionalSubscriptionsImpl(privilegedUplinkRequest, emergencyUplinkRequest, emergencyReset);
        BitSetStrictLength addInfoBitset = new BitSetStrictLength(136);
        addInfoBitset.set(0);
        addInfoBitset.set(1);
        addInfoBitset.set(24);
        AdditionalInfo addInfo = new AdditionalInfoImpl(addInfoBitset);
        VoiceGroupCallData voiceGroupCallData = new VoiceGroupCallDataImpl(gId, null, addSubscriptions, addInfo, lGId);
        vgcsSubscriptionData.add(voiceGroupCallData);
        // vlrCamelSubscriptionInfo
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = getVLRCAMELSubscriptionInfo(basicServiceList, ssCode1, ssCode2);
        // naeaPreferredCI
        String carrierCode = "458";
        NetworkIdentificationPlanValue networkIdentificationPlanValue = NetworkIdentificationPlanValue.spare_1;
        NetworkIdentificationTypeValue networkIdentificationTypeValue = NetworkIdentificationTypeValue.nationalNetworkIdentification;
        NAEACIC naeaPreferredCIC = new NAEACICImpl(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
        NAEAPreferredCI naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, null);
        // gprsSubscriptionData does not apply for CS domain
        // roamingRestrictedInSgsnDueToUnsupportedFeature
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = false;
        // networkAccessMode
        NetworkAccessMode networkAccessMode = NetworkAccessMode.packetAndCircuit;
        // lsaInformation
        LSAInformation lsaInformation = null;
        // lmuIndicator
        boolean lmuIndicator = false;
        // lcsInformation
        LCSInformation lcsInformation = getLcsInformation();
        // istAlertTimer
        Integer istAlertTimer = 200;
        // superChargerSupportedInHLR
        AgeIndicator superChargerSupportedInHLR = null;
        // mcSsInfo
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        ExtSSStatus ssStatus = new ExtSSStatusImpl(true, false, true, false);
        int nbrSB = 2;
        int nbrUser = 4;
        MCSSInfo mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, nbrSB, nbrUser, null);
        // csAllocationRetentionPriority
        CSAllocationRetentionPriority csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);
        // sgsnCamelSubscriptionInfo
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
        // chargingCharacteristics
        boolean isNormalCharging = false;
        boolean isPrepaidCharging = false;
        boolean isFlatRateCharging = true;
        boolean isChargingByHotBillingCharging = false;
        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
        // accessRestrictionData
        boolean utranNotAllowed = false;
        boolean geranNotAllowed = false;
        boolean ganNotAllowed = true;
        boolean eUtranNotAllowed = false;
        boolean iHspaEvolutionNotAllowed = false;
        boolean hoToNon3GppAccessNotAllowed = true;
        AccessRestrictionData accessRestrictionData = new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed, eUtranNotAllowed, hoToNon3GppAccessNotAllowed);
        // icsIndicator
        Boolean icsIndicator = Boolean.TRUE;
        // epsSubscriptionData
        EPSSubscriptionData epsSubscriptionData = getEpsSubscriptionData(chargingCharacteristics);
        // csgSubscriptionDataList
        BitSetStrictLength csgIdBitSet = new BitSetStrictLength(27);
        csgIdBitSet.set(0);
        csgIdBitSet.set(1);
        csgIdBitSet.set(25);
        csgIdBitSet.set(26);
        CSGId csgId = new CSGIdImpl(csgIdBitSet);
        int year = 2024;
        int month = 7;
        int day = 4;
        int hour = 19;
        int minute = 20;
        int second = 10;
        Time expirationDate = new TimeImpl(year, month, day, hour, minute, second);
        ArrayList<APN> lipaAllowedAPNList = new ArrayList<>();
        APN apn = new APNImpl("internet");
        lipaAllowedAPNList.add(apn);
        CSGSubscriptionData csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, null, lipaAllowedAPNList);
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = new ArrayList<>();
        csgSubscriptionDataList.add(csgSubscriptionData);
        // ueReachabilityRequestIndicator
        boolean ueReachabilityRequestIndicator = true;
        // sgsnNumber
        ISDNAddressString sgsnNumber = null;
        // mmeName
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        // subscribedPeriodicRAUTAUtimer
        Long subscribedPeriodicRAUTAUtimer = 300L;
        // vplmnLIPAAllowed
        boolean vplmnLIPAAllowed = true;
        // mdtUserConsent
        boolean mdtUserConsent = false;
        // subscribedPeriodicLAUtimer
        Long subscribedPeriodicLAUtimer = 360L;
        // vplmnCSGSubscriptionDataList
        ArrayList<CSGSubscriptionData> vplmnCSGSubscriptionDataList = new ArrayList<>();
        vplmnCSGSubscriptionDataList.add(csgSubscriptionData);
        // additionalMSISDN
        ISDNAddressString additionalMSISDN =
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917105682451");
        // psAndSMSOnlyServiceProvision
        boolean psAndSMSOnlyServiceProvision = false;
        // smsInSGSNAllowed
        boolean smsInSGSNAllowed = true;
        // csToPsSRVCCAllowedIndicator
        boolean csToPsSRVCCAllowedIndicator = true;
        // pcscfRestorationRequest
        boolean pcscfRestorationRequest = true;
        // adjacentAccessRestrictionDataList
        int mcc = 262;
        int mnc = 999;
        PlmnId plmnId = new PlmnIdImpl(mcc, mnc);
        boolean nrAsSecondaryRATNotAllowed = true;
        boolean unlicensedSpectrumAsSecondaryRATNotAllowed = false;
        ExtAccessRestrictionData extAccessRestrictionData =
                new ExtAccessRestrictionDataImpl(nrAsSecondaryRATNotAllowed, unlicensedSpectrumAsSecondaryRATNotAllowed); // extAccessRestrictionData
        AdjacentAccessRestrictionData adjacentAccessRestrictionData =
                new AdjacentAccessRestrictionDataImpl(plmnId, accessRestrictionData, extAccessRestrictionData);
        ArrayList<AdjacentAccessRestrictionData> adjacentAccessRestrictionDataList = new ArrayList<>();
        adjacentAccessRestrictionDataList.add(adjacentAccessRestrictionData);
        // imsiGroupIdList
        Long groupServiceId = 1L;
        LocalGroupId localGroupId = new LocalGroupIdImpl("120".getBytes(StandardCharsets.UTF_8));
        IMSIGroupId imsiGroupId = new IMSIGroupIdImpl(groupServiceId, plmnId, localGroupId);
        ArrayList<IMSIGroupId> imsiGroupIdList = new ArrayList<>();
        imsiGroupIdList.add(imsiGroupId);
        // ueUsageType
        UEUsageType ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator
        boolean userPlaneIntegrityProtectionIndicator = true;
        // dlBufferingSuggestedPacketCount
        Long dlBufferingSuggestedPacketCount = 0L;
        // resetIdList
        ArrayList<ResetId> resetIdList = new ArrayList<>();
        ResetId resetId1 = new ResetIdImpl(new byte[] {(byte) 0x81});
        ResetId resetId2 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // eDRXCycleLengthList
        UsedRATType usedRATType = UsedRATType.nbIoT;
        byte[] edrCycleLengthVal = new byte[] { 0x02 };
        EDRXCycleLengthValue eDRXCycleLengthValue = new EDRXCycleLengthValueImpl(edrCycleLengthVal);
        EDRXCycleLength edrxCycleLength = new EDRXCycleLengthImpl(usedRATType, eDRXCycleLengthValue);
        ArrayList<EDRXCycleLength> eDRXCycleLengthList = new ArrayList<>();
        eDRXCycleLengthList.add(edrxCycleLength);
        // iabOperationAllowedIndicator
        boolean iabOperationAllowedIndicator = true;

        return new InsertSubscriberDataArgsImpl(imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList,
                provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, naeaPreferredCI, null,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator, lcsInformation,
                istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo,
                chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                subscribedPeriodicLAUtimer, vplmnCSGSubscriptionDataList, additionalMSISDN, psAndSMSOnlyServiceProvision, smsInSGSNAllowed,
                csToPsSRVCCAllowedIndicator, pcscfRestorationRequest, adjacentAccessRestrictionDataList, imsiGroupIdList, ueUsageType,
                userPlaneIntegrityProtectionIndicator, dlBufferingSuggestedPacketCount, resetIdList, eDRXCycleLengthList,
                extAccessRestrictionData, iabOperationAllowedIndicator);
    }
    private static DeleteSubscriberDataArgs getSubscriptionDataDeletionForReset() {
        // imsi
        IMSI imsi = new IMSIImpl("748026800000000");
        // basicServiceList
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue4 = getBearerServiceCodeValue();
        ExtBearerServiceCode extBearerServiceCode4 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue4);
        ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode4);
        TeleserviceCodeValue teleserviceCodeValue4 = getTeleserviceCodeValue();
        ExtTeleserviceCode extTeleserviceCode4 = new ExtTeleserviceCodeImpl(teleserviceCodeValue4);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode4);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        // ssList
        ArrayList<SSCode> ssList = new ArrayList<>();
        SupplementaryCodeValue supplementaryCodeValue = getSupplementaryCodeValue();
        SSCode ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = false;
        // regionalSubscriptionIdentifier
        ZoneCode regionalSubscriptionIdentifier = new ZoneCodeImpl(new byte[] {0x21, 0x0F});
        boolean vbsGroupIndication = true;
        boolean vgcsGroupIndication = true;
        boolean camelSubscriptionInfoWithdraw = true;
        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = null; // doesn't apply for CS domain
        boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = true;
        // lsaInformationWithdraw
        LSAInformationWithdraw lsaInformationWithdraw = null;
        Random rand = new Random();
        switch (rand.nextInt(2 + 1)) {
            case 1:
                ArrayList<LSAIdentity> lsaIdentityList = new ArrayList<>();
                LSAIdentity lsaIdentity1 = new LSAIdentityImpl(new byte[]{12, 10, 1});
                LSAIdentity lsaIdentity2 = new LSAIdentityImpl(new byte[]{12, 12, 2});
                lsaIdentityList.add(lsaIdentity1);
                lsaIdentityList.add(lsaIdentity2);
                lsaInformationWithdraw = new LSAInformationWithdrawImpl(lsaIdentityList);
                break;
            case 2:
                lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
                break;
        }
        boolean gmlcListWithdraw = true;
        boolean istInformationWithdraw = true;
        // specificCSIWithdraw
        SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false,
                false, false, false, false);
        boolean chargingCharacteristicsWithdraw = true;
        boolean stnSrWithdraw = true;
        // epsSubscriptionDataWithdraw
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = null;
        switch (rand.nextInt(2 + 1)) {
            case 1:
                ArrayList<Integer> contextIdList = new ArrayList<>();
                contextIdList.add(1);
                contextIdList.add(2);
                epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
                break;
            case 2:
                epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(true);
                break;
        }
        // boolean values including new MAP v18.0.0 params
        boolean apnOiReplacementWithdraw = true;
        boolean csgSubscriptionDeleted = true;
        boolean subscribedPeriodicTAURAUTimerWithdraw = true;
        boolean subscribedPeriodicLAUTimerWithdraw = true;
        boolean subscribedVsrvccWithdraw = true;
        boolean vplmnCsgSubscriptionDeleted = true;
        boolean additionalMSISDNWithdraw = true;
        boolean csToPsSRVCCWithdraw = true;
        boolean imsiGroupIdListWithdraw = true;
        boolean userPlaneIntegrityProtectionWithdraw = true;
        boolean dlBufferingSuggestedPacketCountWithdraw = true;
        boolean ueUsageTypeWithdraw = true;
        boolean resetIdsWithdraw = true;
        boolean iabOperationWithdraw = true;

        return new DeleteSubscriberDataArgsImpl(imsi, basicServiceList, ssList,
                roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication,
                camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature,
                lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw,
                epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted, subscribedPeriodicTAURAUTimerWithdraw,
                subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted, additionalMSISDNWithdraw, csToPsSRVCCWithdraw,
                imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw, dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw,
                iabOperationWithdraw);
    }

    private static BearerServiceCodeValue getBearerServiceCodeValue() {
        BearerServiceCodeValue bearerServiceCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(51 + 1)) {
            case 2:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCDAServices;
                break;
            case 3:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_300bps;
                break;
            case 4:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_1200bps;
                break;
            case 5:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_1200_75bps;
                break;
            case 6:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_2400bps;
                break;
            case 7:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_4800bps;
                break;
            case 8:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDA_9600bps;
                break;
            case 9:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataCDA;
                break;
            case 10:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCDS_Services;
                break;
            case 11:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_1200bps;
                break;
            case 12:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_2400bps;
                break;
            case 13:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_4800bps;
                break;
            case 14:
                bearerServiceCodeValue = BearerServiceCodeValue.dataCDS_9600bps;
                break;
            case 15:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataCDS;
                break;
            case 16:
                bearerServiceCodeValue = BearerServiceCodeValue.allPadAccessCA_Services;
                break;
            case 17:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_300bps;
                break;
            case 18:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_1200bps;
                break;
            case 19:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_1200_75bps;
                break;
            case 20:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_2400bps;
                break;
            case 21:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_4800bps;
                break;
            case 22:
                bearerServiceCodeValue = BearerServiceCodeValue.padAccessCA_9600bps;
                break;
            case 23:
                bearerServiceCodeValue = BearerServiceCodeValue.general_padAccessCA;
                break;
            case 24:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataPDS_Services;
                break;
            case 25:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_2400bps;
                break;
            case 26:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_4800bps;
                break;
            case 27:
                bearerServiceCodeValue = BearerServiceCodeValue.dataPDS_9600bps;
                break;
            case 28:
                bearerServiceCodeValue = BearerServiceCodeValue.allAlternateSpeech_DataCDA;
                break;
            case 29:
                bearerServiceCodeValue = BearerServiceCodeValue.allAlternateSpeech_DataCDS;
                break;
            case 30:
                bearerServiceCodeValue = BearerServiceCodeValue.allSpeechFollowedByDataCDA;
                break;
            case 31:
                bearerServiceCodeValue = BearerServiceCodeValue.allSpeechFollowedByDataCDS;
                break;
            case 32:
                bearerServiceCodeValue = BearerServiceCodeValue.general_dataPDS;
                break;
            case 33:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCircuitAsynchronous;
                break;
            case 34:
                bearerServiceCodeValue = BearerServiceCodeValue.allAsynchronousServices;
                break;
            case 35:
                bearerServiceCodeValue = BearerServiceCodeValue.allDataCircuitSynchronous;
                break;
            case 36:
                bearerServiceCodeValue = BearerServiceCodeValue.allSynchronousServices;
                break;
            case 37:
                bearerServiceCodeValue = BearerServiceCodeValue.allPLMN_specificBS;
                break;
            case 38:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_1;
                break;
            case 39:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_2;
                break;
            case 40:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_3;
                break;
            case 41:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_4;
                break;
            case 42:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_5;
                break;
            case 43:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_6;
                break;
            case 44:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_7;
                break;
            case 45:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_8;
                break;
            case 46:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_9;
                break;
            case 47:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_A;
                break;
            case 48:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_B;
                break;
            case 49:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_C;
                break;
            case 50:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_E;
                break;
            case 51:
                bearerServiceCodeValue = BearerServiceCodeValue.plmn_specificBS_F;
                break;
            default:
                bearerServiceCodeValue = BearerServiceCodeValue.allBearerServices;
                break;
        }
        return bearerServiceCodeValue;
    }

    private static TeleserviceCodeValue getTeleserviceCodeValue() {
        TeleserviceCodeValue teleserviceCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(33 + 1)) {
            case 2:
                teleserviceCodeValue = TeleserviceCodeValue.allSpeechTransmissionServices;
                break;
            case 3:
                teleserviceCodeValue = TeleserviceCodeValue.telephony;
                break;
            case 4:
                teleserviceCodeValue = TeleserviceCodeValue.emergencyCalls;
                break;
            case 5:
                teleserviceCodeValue = TeleserviceCodeValue.allShortMessageServices;
                break;
            case 6:
                teleserviceCodeValue = TeleserviceCodeValue.shortMessageMT_PP;
                break;
            case 7:
                teleserviceCodeValue = TeleserviceCodeValue.shortMessageMO_PP;
                break;
            case 8:
                teleserviceCodeValue = TeleserviceCodeValue.cellBroadcast;
                break;
            case 9:
                teleserviceCodeValue = TeleserviceCodeValue.allFacsimileTransmissionServices;
                break;
            case 10:
                teleserviceCodeValue = TeleserviceCodeValue.facsimileGroup3AndAlterSpeech;
                break;
            case 11:
                teleserviceCodeValue = TeleserviceCodeValue.automaticFacsimileGroup3;
                break;
            case 12:
                teleserviceCodeValue = TeleserviceCodeValue.facsimileGroup4;
                break;
            case 13:
                teleserviceCodeValue = TeleserviceCodeValue.allDataTeleservices;
                break;
            case 14:
                teleserviceCodeValue = TeleserviceCodeValue.allTeleservices_ExeptSMS;
                break;
            case 15:
                teleserviceCodeValue = TeleserviceCodeValue.allVoiceGroupCallServices;
                break;
            case 16:
                teleserviceCodeValue = TeleserviceCodeValue.voiceGroupCall;
                break;
            case 17:
                teleserviceCodeValue = TeleserviceCodeValue.voiceBroadcastCall;
                break;
            case 18:
                teleserviceCodeValue = TeleserviceCodeValue.allPLMN_specificTS;
                break;
            case 19:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_1;
                break;
            case 20:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_2;
                break;
            case 21:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_3;
                break;
            case 22:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_4;
                break;
            case 23:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_5;
                break;
            case 24:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_6;
                break;
            case 25:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_7;
                break;
            case 26:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_8;
                break;
            case 27:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_9;
                break;
            case 28:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_A;
                break;
            case 29:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_B;
                break;
            case 30:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_C;
                break;
            case 31:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_D;
                break;
            case 32:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_E;
                break;
            case 33:
                teleserviceCodeValue = TeleserviceCodeValue.plmn_specificTS_F;
                break;
            default:
                teleserviceCodeValue = TeleserviceCodeValue.allTeleservices;
                break;
        }
        return teleserviceCodeValue;
    }

    private static SupplementaryCodeValue getSupplementaryCodeValue() {
        SupplementaryCodeValue supplementaryCodeValue;
        Random rand = new Random();
        switch (rand.nextInt(72 + 1)) {
            case 1:
                supplementaryCodeValue = SupplementaryCodeValue.allLineIdentificationSS;
                break;
            case 2:
                supplementaryCodeValue = SupplementaryCodeValue.clip;
                break;
            case 3:
                supplementaryCodeValue = SupplementaryCodeValue.clir;
                break;
            case 4:
                supplementaryCodeValue = SupplementaryCodeValue.colp;
                break;
            case 5:
                supplementaryCodeValue = SupplementaryCodeValue.colr;
                break;
            case 6:
                supplementaryCodeValue = SupplementaryCodeValue.mci;
                break;
            case 7:
                supplementaryCodeValue = SupplementaryCodeValue.allNameIdentificationSS;
                break;
            case 8:
                supplementaryCodeValue = SupplementaryCodeValue.cnap;
                break;
            case 9:
                supplementaryCodeValue = SupplementaryCodeValue.allForwardingSS;
                break;
            case 10:
                supplementaryCodeValue = SupplementaryCodeValue.cfu;
                break;
            case 11:
                supplementaryCodeValue = SupplementaryCodeValue.allCondForwardingSS;
                break;
            case 12:
                supplementaryCodeValue = SupplementaryCodeValue.cfb;
                break;
            case 13:
                supplementaryCodeValue = SupplementaryCodeValue.cfnry;
                break;
            case 14:
                supplementaryCodeValue = SupplementaryCodeValue.cfnrc;
                break;
            case 15:
                supplementaryCodeValue = SupplementaryCodeValue.cd;
                break;
            case 16:
                supplementaryCodeValue = SupplementaryCodeValue.allCallOfferingSS;
                break;
            case 17:
                supplementaryCodeValue = SupplementaryCodeValue.ect;
                break;
            case 18:
                supplementaryCodeValue = SupplementaryCodeValue.mah;
                break;
            case 19:
                supplementaryCodeValue = SupplementaryCodeValue.allCallCompletionSS;
                break;
            case 20:
                supplementaryCodeValue = SupplementaryCodeValue.cw;
                break;
            case 21:
                supplementaryCodeValue = SupplementaryCodeValue.hold;
                break;
            case 22:
                supplementaryCodeValue = SupplementaryCodeValue.ccbs_A;
                break;
            case 23:
                supplementaryCodeValue = SupplementaryCodeValue.ccbs_B;
                break;
            case 24:
                supplementaryCodeValue = SupplementaryCodeValue.mc;
                break;
            case 25:
                supplementaryCodeValue = SupplementaryCodeValue.allMultiPartySS;
                break;
            case 26:
                supplementaryCodeValue = SupplementaryCodeValue.multiPTY;
                break;
            case 27:
                supplementaryCodeValue = SupplementaryCodeValue.allCommunityOfInterestSS;
                break;
            case 28:
                supplementaryCodeValue = SupplementaryCodeValue.cug;
                break;
            case 29:
                supplementaryCodeValue = SupplementaryCodeValue.allChargingSS;
                break;
            case 30:
                supplementaryCodeValue = SupplementaryCodeValue.aoci;
                break;
            case 31:
                supplementaryCodeValue = SupplementaryCodeValue.aocc;
                break;
            case 32:
                supplementaryCodeValue = SupplementaryCodeValue.allAdditionalInfoTransferSS;
                break;
            case 33:
                supplementaryCodeValue = SupplementaryCodeValue.uus1;
                break;
            case 34:
                supplementaryCodeValue = SupplementaryCodeValue.uus2;
                break;
            case 35:
                supplementaryCodeValue = SupplementaryCodeValue.uus3;
                break;
            case 36:
                supplementaryCodeValue = SupplementaryCodeValue.allCallRestrictionSS;
                break;
            case 37:
                supplementaryCodeValue = SupplementaryCodeValue.barringOfOutgoingCalls;
                break;
            case 38:
                supplementaryCodeValue = SupplementaryCodeValue.baoc;
                break;
            case 39:
                supplementaryCodeValue = SupplementaryCodeValue.boic;
                break;
            case 40:
                supplementaryCodeValue = SupplementaryCodeValue.boicExHC;
                break;
            case 41:
                supplementaryCodeValue = SupplementaryCodeValue.barringOfIncomingCalls;
                break;
            case 42:
                supplementaryCodeValue = SupplementaryCodeValue.baic;
                break;
            case 43:
                supplementaryCodeValue = SupplementaryCodeValue.bicRoam;
                break;
            case 44:
                supplementaryCodeValue = SupplementaryCodeValue.allPLMN_specificSS;
                break;
            case 45:
                supplementaryCodeValue = SupplementaryCodeValue.allCallPrioritySS;
                break;
            case 46:
                supplementaryCodeValue = SupplementaryCodeValue.emlpp;
                break;
            case 47:
                supplementaryCodeValue = SupplementaryCodeValue.allLCSPrivacyException;
                break;
            case 48:
                supplementaryCodeValue = SupplementaryCodeValue.universal;
                break;
            case 49:
                supplementaryCodeValue = SupplementaryCodeValue.callrelated;
                break;
            case 50:
                supplementaryCodeValue = SupplementaryCodeValue.callunrelated;
                break;
            case 51:
                supplementaryCodeValue = SupplementaryCodeValue.plmnoperator;
                break;
            case 52:
                supplementaryCodeValue = SupplementaryCodeValue.serviceType;
                break;
            case 53:
                supplementaryCodeValue = SupplementaryCodeValue.allMOLR_SS;
                break;
            case 54:
                supplementaryCodeValue = SupplementaryCodeValue.basicSelfLocation;
                break;
            case 55:
                supplementaryCodeValue = SupplementaryCodeValue.autonomousSelfLocation;
                break;
            case 56:
                supplementaryCodeValue = SupplementaryCodeValue.transferToThirdParty;
                break;
            case 57:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_1;
                break;
            case 58:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_2;
                break;
            case 59:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_3;
                break;
            case 60:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_4;
                break;
            case 61:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_5;
                break;
            case 62:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_6;
                break;
            case 63:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_7;
                break;
            case 64:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_8;
                break;
            case 65:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_9;
                break;
            case 66:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_a;
                break;
            case 67:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_b;
                break;
            case 68:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_c;
                break;
            case 69:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_d;
                break;
            case 70:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_e;
                break;
            case 71:
                supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_f;
                break;
            default:
                supplementaryCodeValue = SupplementaryCodeValue.allSS;
                break;
        }
        return supplementaryCodeValue;
    }

    private static ODBGeneralData getOdbGeneralData() {
        boolean allOGCallsBarred= true;
        boolean internationalOGCallsBarred = true;
        boolean internationalOGCallsNotToHPLMNCountryBarred= true;
        boolean premiumRateInformationOGCallsBarred = false;
        boolean premiumRateEntertainmentOGCallsBarred= true;
        boolean ssAccessBarred= true;
        boolean interzonalOGCallsBarred = true;
        boolean interzonalOGCallsNotToHPLMNCountryBarred= true;
        boolean interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = true;
        boolean allECTBarred= true;
        boolean chargeableECTBarred= true;
        boolean internationalECTBarred = true;
        boolean interzonalECTBarred= true;
        boolean doublyChargeableECTBarred= true;
        boolean multipleECTBarred = true;
        boolean allPacketOrientedServicesBarred= true;
        boolean roamerAccessToHPLMNAPBarred= false;
        boolean roamerAccessToVPLMNAPBarred = false;
        boolean roamingOutsidePLMNOGCallsBarred= false;
        boolean allICCallsBarred= true;
        boolean roamingOutsidePLMNICCallsBarred = true;
        boolean roamingOutsidePLMNICountryICCallsBarred= true;
        boolean roamingOutsidePLMNBarred = false;
        boolean roamingOutsidePLMNCountryBarred= false;
        boolean registrationAllCFBarred= true;
        boolean registrationCFNotToHPLMNBarred = true;
        boolean registrationInterzonalCFBarred= true;
        boolean registrationInterzonalCFNotToHPLMNBarred = false;
        boolean registrationInternationalCFBarred = true;
        return new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                internationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred,
                ssAccessBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, allECTBarred, chargeableECTBarred,
                internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred,
                roamingOutsidePLMNCountryBarred, registrationAllCFBarred, registrationCFNotToHPLMNBarred,
                registrationInterzonalCFBarred, registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
    }

    public static VlrCamelSubscriptionInfo getVLRCAMELSubscriptionInfo(ArrayList<ExtBasicServiceCode> basicServiceList, SSCode ssCode1, SSCode ssCode2) {
        OBcsmTriggerDetectionPoint oBcsmTDP = OBcsmTriggerDetectionPoint.routeSelectFailure;
        long serviceKey = 7L;
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
        DefaultCallHandling defaultCallHandling = DefaultCallHandling.continueCall;
        OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList = new ArrayList<>();
        oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
        Integer camelCapabilityHandling = 2;
        boolean notificationToCSE = true;
        boolean csiActive = true;
        OCSI oCSI = new OCSIImpl(oBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        ArrayList<SSCode> ssEventList = new ArrayList<>();
        ssEventList.add(ssCode1);
        ssEventList.add(ssCode2);
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, null);
        SSCSI ssCsi = new SSCSIImpl(ssCamelData, null, notificationToCSE, csiActive);
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = getoBcsmCamelTdpCriteria(oBcsmTDP, basicServiceList, null);
        boolean tifCsi = true;
        ArrayList<MMCode> mobilityTriggers = new ArrayList<>();
        MMCode mmCode1 = new MMCodeImpl(MMCodeValue.IMSIAttach);
        MMCode mmCode2 = new MMCodeImpl(MMCodeValue.LocationUpdateInSameVLR);
        mobilityTriggers.add(mmCode1);
        mobilityTriggers.add(mmCode2);
        MCSI mcsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<>();
        SMSTriggerDetectionPoint smsTDP = SMSTriggerDetectionPoint.smsDeliveryRequest;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;
        SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, null);
        smsCamelTdpDataList.add(smscameltdpData);
        SMSCSI smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = new ArrayList<>();
        TBcsmTriggerDetectionPoint tBcsmTDP1 = TBcsmTriggerDetectionPoint.tNoAnswer;
        TBcsmTriggerDetectionPoint tBcsmTDP2 = TBcsmTriggerDetectionPoint.tBusy;
        TBcsmCamelTDPData tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        TBcsmCamelTDPData tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
        TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tNoAnswer;
        ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<>();
        CauseValue tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
        CauseValue tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        tCauseValueCriteria.add(tcv1);
        tCauseValueCriteria.add(tcv2);
        TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<>();
        ISDNAddressString dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
        DCSI dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<>();
        ArrayList<MTSMSTPDUType> mtsmstpduTypeArrayList = new ArrayList<>();
        MTSMSTPDUType mtsmstpduType1 = MTSMSTPDUType.smsDELIVER;
        MTSMSTPDUType mtsmstpduType2 = MTSMSTPDUType.smsSUBMITREPORT;
        MTSMSTPDUType mtsmstpduType3 = MTSMSTPDUType.smsSTATUSREPORT;
        mtsmstpduTypeArrayList.add(mtsmstpduType1);
        mtsmstpduTypeArrayList.add(mtsmstpduType2);
        mtsmstpduTypeArrayList.add(mtsmstpduType3);
        MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
        return new VlrCamelSubscriptionInfoImpl(oCSI, null,
                ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mcsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCSI, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);
    }

    private static EPSSubscriptionData getEpsSubscriptionData(ChargingCharacteristics chargingCharacteristics) throws MAPException {
        int defaultContext = 1;
        boolean completeDataListIncluded = true;
        PDNType pDNType = new PDNTypeImpl(PDNTypeValue.IPv4v6);
        PDPAddress servedPartyIPIPv4Address = null;
        APN apn = new APNImpl("internet");
        QoSClassIdentifier qci = QoSClassIdentifier.QCI_5;
        int priorityLevel = 9;
        Boolean preEmptionCapability = true;
        Boolean preEmptionVulnerability = false;
        AllocationRetentionPriority arp = new AllocationRetentionPriorityImpl(priorityLevel, preEmptionCapability, preEmptionVulnerability, null);
        EPSQoSSubscribed ePSQoSSubscribed = new EPSQoSSubscribedImpl(qci, arp, null);
        PDNGWIdentity pdnGwIdentity = null;
        PDNGWAllocationType pdnGwAllocationType = null;
        boolean vplmnAddressAllowed = true;
        int maxRequestedBandwidthUL = 2048;
        int maxRequestedBandwidthDL = 4096;
        AMBR ambr = new AMBRImpl(maxRequestedBandwidthUL, maxRequestedBandwidthDL, null);
        ArrayList<SpecificAPNInfo> specificAPNInfoList = null;
        APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;
        int contextId = 1;
        PDPAddress servedPartyIPIPv6Address = new PDPAddressImpl(new byte[] { 21 });
        APNConfiguration apnConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn,
                ePSQoSSubscribed, pdnGwIdentity, pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr,
                specificAPNInfoList, null, servedPartyIPIPv6Address, apnOiReplacement, sipToPermission, lipaPermission);
        ArrayList<APNConfiguration> ePSDataList = new ArrayList<>();
        ePSDataList.add(apnConfiguration);
        APNConfigurationProfile apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded,
                ePSDataList, null);
        Integer rfspId = 0;
        ISDNAddressString stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710490000");
        boolean mpsCSPriority = true;
        boolean mpsEPSPriority = true;
        return new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile,
                stnSr, null, mpsCSPriority, mpsEPSPriority);
    }

    private static LCSInformation getLcsInformation() {
        ArrayList<ISDNAddressString> gmlcList = new ArrayList<>();
        ISDNAddressString gmlcAddress =
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917104600321");
        gmlcList.add(gmlcAddress);
        // lcsInformation (lcsPrivacyExceptionList)
        SSCode ssCode1 = new SSCodeImpl(SupplementaryCodeValue.allMOLR_SS);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.autonomousSelfLocation);
        SSCode ssCode3 = new SSCodeImpl(SupplementaryCodeValue.allLCSPrivacyException);
        SSCode ssCode4 = new SSCodeImpl(SupplementaryCodeValue.allPLMN_specificSS);
        ExtSSStatus extSSStatus1 = new ExtSSStatusImpl(true, false, false, false);
        ExtSSStatus extSSStatus2 = new ExtSSStatusImpl(false, true, false, false);
        ExtSSStatus extSSStatus3 = new ExtSSStatusImpl(false, false, true, false);
        ExtSSStatus extSSStatus4 = new ExtSSStatusImpl(false, false, false, true);
        LCSClientInternalID lcsClientInternalID1 = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalID2 = LCSClientInternalID.oandMHPLMN;
        LCSClientInternalID lcsClientInternalID3 = LCSClientInternalID.targetMSsubscribedService;
        LCSClientInternalID lcsClientInternalID4 = LCSClientInternalID.anonymousLocation;
        ArrayList<LCSClientInternalID> plmnClientList1 = new ArrayList<>();
        plmnClientList1.add(lcsClientInternalID1);
        plmnClientList1.add(lcsClientInternalID2);
        ArrayList<LCSClientInternalID> plmnClientList2 = new ArrayList<>();
        plmnClientList2.add(lcsClientInternalID2);
        ArrayList<LCSClientInternalID> plmnClientList3 = new ArrayList<>();
        plmnClientList3.add(lcsClientInternalID3);
        ArrayList<LCSClientInternalID> plmnClientList4 = new ArrayList<>();
        plmnClientList4.add(lcsClientInternalID4);
        ArrayList<ExternalClient> externalClientList1 = new ArrayList<>();
        ISDNAddressString externalAddress1 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "874927492");
        LCSClientExternalID clientIdentity1 = new LCSClientExternalIDImpl(externalAddress1, null);
        GMLCRestriction gmlcRestriction1 = GMLCRestriction.homeCountry;
        NotificationToMSUser notificationToMSUser1 = NotificationToMSUser.notifyLocationAllowed;
        ExternalClient externalClient1 = new ExternalClientImpl(clientIdentity1, gmlcRestriction1, notificationToMSUser1, null);
        externalClientList1.add(externalClient1);
        ArrayList<ExternalClient> externalClientList2 = new ArrayList<>();
        ISDNAddressString externalAddress2 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "398279222");
        LCSClientExternalID clientIdentity2 = new LCSClientExternalIDImpl(externalAddress2, null);
        GMLCRestriction gmlcRestriction2 = GMLCRestriction.gmlcList;
        NotificationToMSUser notificationToMSUser2 = NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse;
        ExternalClient externalClient2 = new ExternalClientImpl(clientIdentity2, gmlcRestriction2, notificationToMSUser2, null);
        externalClientList2.add(externalClient2);
        ArrayList<ExternalClient> externalClientList3 = new ArrayList<>();
        ISDNAddressString externalAddress3 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "23543252234");
        LCSClientExternalID clientIdentity3 = new LCSClientExternalIDImpl(externalAddress3, null);
        GMLCRestriction gmlcRestriction3 = GMLCRestriction.homeCountry;
        NotificationToMSUser notificationToMSUser3 = NotificationToMSUser.locationNotAllowed;
        ExternalClient externalClient3 = new ExternalClientImpl(clientIdentity3, gmlcRestriction3, notificationToMSUser3, null);
        externalClientList3.add(externalClient3);
        ArrayList<ExternalClient> externalClientList4 = new ArrayList<>();
        ISDNAddressString externalAddress4 = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "598990298245");
        LCSClientExternalID clientIdentity4 = new LCSClientExternalIDImpl(externalAddress4, null);
        GMLCRestriction gmlcRestriction4 = GMLCRestriction.gmlcList;
        NotificationToMSUser notificationToMSUser4 = NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse;
        ExternalClient externalClient4 = new ExternalClientImpl(clientIdentity4, gmlcRestriction4, notificationToMSUser4, null);
        externalClientList4.add(externalClient4);
        ArrayList<ServiceType> serviceTypeList1 = new ArrayList<>();
        int serviceTypeIdentity1 = 1;
        ServiceType serviceType1 = new ServiceTypeImpl(serviceTypeIdentity1, gmlcRestriction1, notificationToMSUser1, null);
        serviceTypeList1.add(serviceType1);
        ArrayList<ServiceType> serviceTypeList2 = new ArrayList<>();
        int serviceTypeIdentity2 = 2;
        ServiceType serviceType2 = new ServiceTypeImpl(serviceTypeIdentity2, gmlcRestriction2, notificationToMSUser2, null);
        serviceTypeList2.add(serviceType2);
        ArrayList<ServiceType> serviceTypeList3 = new ArrayList<>();
        int serviceTypeIdentity3 = 3;
        ServiceType serviceType3 = new ServiceTypeImpl(serviceTypeIdentity3, gmlcRestriction3, notificationToMSUser3, null);
        serviceTypeList3.add(serviceType3);
        ArrayList<ServiceType> serviceTypeList4 = new ArrayList<>();
        int serviceTypeIdentity4 = 4;
        ServiceType serviceType4 = new ServiceTypeImpl(serviceTypeIdentity4, gmlcRestriction4, notificationToMSUser4, null);
        serviceTypeList4.add(serviceType4);
        LCSPrivacyClass lcsPrivacyClass1 = new LCSPrivacyClassImpl(ssCode1, extSSStatus1, notificationToMSUser1, externalClientList1,
                plmnClientList1, null, externalClientList1, serviceTypeList1);
        LCSPrivacyClass lcsPrivacyClass2 = new LCSPrivacyClassImpl(ssCode2, extSSStatus2, notificationToMSUser2, externalClientList2,
                plmnClientList2, null, externalClientList2, serviceTypeList2);
        LCSPrivacyClass lcsPrivacyClass3 = new LCSPrivacyClassImpl(ssCode3, extSSStatus3, notificationToMSUser3, externalClientList3,
                plmnClientList3, null, externalClientList3, serviceTypeList3);
        LCSPrivacyClass lcsPrivacyClass4 = new LCSPrivacyClassImpl(ssCode4, extSSStatus4, notificationToMSUser4, externalClientList4,
                plmnClientList4, null, externalClientList4, serviceTypeList4);
        ArrayList<LCSPrivacyClass> lcsPrivacyExceptionList = new ArrayList<>();
        lcsPrivacyExceptionList.add(lcsPrivacyClass1);
        lcsPrivacyExceptionList.add(lcsPrivacyClass2);
        lcsPrivacyExceptionList.add(lcsPrivacyClass3);
        lcsPrivacyExceptionList.add(lcsPrivacyClass4);
        // lcsInformation (molrList)
        ArrayList<MOLRClass> molrList = new ArrayList<>();
        MOLRClass molrClass1 = new MOLRClassImpl(ssCode1, extSSStatus1, null);
        MOLRClass molrClass2 = new MOLRClassImpl(ssCode2, extSSStatus2, null);
        MOLRClass molrClass3 = new MOLRClassImpl(ssCode3, extSSStatus3, null);
        molrList.add(molrClass1);
        molrList.add(molrClass2);
        molrList.add(molrClass3);
        // lcsInformation (addLcsPrivacyExceptionList)
        // add-lcs-PrivacyExceptionList may be sent only if lcs-PrivacyExceptionList is
        // present and contains four instances of LCS-PrivacyClass. If the mentioned condition
        // is not satisfied the receiving node shall discard add-lcs-PrivacyExceptionList.
        // If an LCS-PrivacyClass is received both in lcs-PrivacyExceptionList and in
        // add-lcs-PrivacyExceptionList with the same SS-Code, then the error unexpected
        // data value shall be returned.
        SSCode addSsCode = new SSCodeImpl(SupplementaryCodeValue.plmn_specificSS_1);
        ExtSSStatus addExtSSStatus = new ExtSSStatusImpl(true, true, false, true);
        ArrayList<LCSClientInternalID> addPlmnClientList = new ArrayList<>();
        addPlmnClientList.add(lcsClientInternalID1);
        addPlmnClientList.add(lcsClientInternalID2);
        addPlmnClientList.add(lcsClientInternalID3);
        addPlmnClientList.add(lcsClientInternalID4);
        ArrayList<ExternalClient> addExternalClientList = new ArrayList<>();
        ISDNAddressString addExternalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.reserved, "874927492");
        LCSClientExternalID addClientIdentity = new LCSClientExternalIDImpl(addExternalAddress, null);
        GMLCRestriction addGmlcRestriction = GMLCRestriction.homeCountry;
        NotificationToMSUser addMNtificationToMSUser = NotificationToMSUser.notifyLocationAllowed;
        ExternalClient addExternalClient = new ExternalClientImpl(addClientIdentity, addGmlcRestriction, addMNtificationToMSUser, null);
        addExternalClientList.add(addExternalClient);
        ArrayList<ServiceType> addServiceTypeList = new ArrayList<>();
        int addServiceTypeIdentity = 5;
        ServiceType addServiceType4 = new ServiceTypeImpl(addServiceTypeIdentity, addGmlcRestriction, addMNtificationToMSUser, null);
        addServiceTypeList.add(addServiceType4);
        LCSPrivacyClass addLcsPrivacyClass = new LCSPrivacyClassImpl(addSsCode, addExtSSStatus, addMNtificationToMSUser, addExternalClientList,
                addPlmnClientList, null, addExternalClientList, addServiceTypeList);
        ArrayList<LCSPrivacyClass> addLcsPrivacyExceptionList = new ArrayList<>();
        addLcsPrivacyExceptionList.add(addLcsPrivacyClass);
        return new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
    }

    private static SccpAddress createSccpAddress(RoutingIndicator ri, int dpc, int ssn, String address) {
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        GlobalTitle gt = fact.createGlobalTitle(address, 0, org.restcomm.protocols.ss7.indicator.NumberingPlan.ISDN_TELEPHONY,
                BCDEvenEncodingScheme.INSTANCE, NatureOfAddress.INTERNATIONAL);
        if (ssn < 0) {
            ssn = HLR_SSN;
        }
        return fact.createSccpAddress(ri, gt, dpc, ssn);
    }
}
