package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import static org.testng.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NAEACIC;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationPlanValue;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkIdentificationTypeValue;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.UEUsageType;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.DeleteSubscriberDataArgs;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.InsertSubscriberDataArgs;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetId;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.SendingNodeNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.*;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.NAEACICImpl;
import org.restcomm.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.UEUsageTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AMBRImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNConfigurationImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNConfigurationProfileImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdditionalInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdditionalSubscriptionsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AdjacentAccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AgeIndicatorImpl;
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
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtAccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSizeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelayImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExternalClientImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdrawImpl;
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
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
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
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
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
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com">Fernando Mendioroz</a>
 */
public class ResetRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 9, 10, 1, 1, 4, 4, (byte) 145, 33, 67, (byte) 245 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 14, 4, 4, (byte) 145, 33, 67, (byte) 245, 48, 6, 4, 4, 33, 67, 0, (byte) 241 };
    }

    private byte[] getEncodedDataRel18_0() {
        return new byte[] { 0x30, 0x29,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00,
                0x00, 0x30, 0x1e, 0x04, 0x08, 0x47, 0x08, 0x62,
                0x08, 0x00, 0x00, 0x00, (byte) 0xf0, 0x04, 0x08, 0x47,
                0x08, 0x62, 0x09, 0x00, 0x00, 0x00, (byte) 0xf0, 0x04,
                0x08, 0x47, 0x08, 0x72, 0x00, 0x00, 0x00, 0x00,
                (byte) 0xf0
        };
    }

    private byte[] getEncodedDataRel18wSubsDataToVLR() {
        return new byte[] { 0x30, (byte) 0x82, 0x03, 0x7d, 0x04, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00, 0x00, (byte) 0xa1,
                0x0c, 0x04, 0x04, 0x01, 0x02, 0x04, (byte) 0x81, 0x04,
                0x04, 0x01, 0x02, 0x04, (byte) 0x82, (byte) 0xa2, (byte) 0x82, 0x03,
                0x62, (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x08, 0x00,
                0x00, 0x00, (byte) 0xf0, (byte) 0x82, 0x01, 0x0a, (byte) 0x83, 0x01,
                0x00, (byte) 0xa4, 0x06, 0x04, 0x01, 0x18, 0x04, 0x01,
                0x13, (byte) 0xa6, 0x06, 0x04, 0x01, 0x21, 0x04, 0x01,
                0x22, (byte) 0xa7, 0x26, (byte) 0xa3, 0x11, 0x04, 0x01, (byte) 0xf2,
                (byte) 0x84, 0x01, 0x05, (byte) 0x81, 0x01, 0x01, 0x30, 0x06,
                (byte) 0x82, 0x01, 0x18, (byte) 0x83, 0x01, 0x60, (byte) 0xa3, 0x11,
                0x04, 0x01, (byte) 0x9b, (byte) 0x84, 0x01, 0x05, (byte) 0x82, 0x01,
                0x02, 0x30, 0x06, (byte) 0x82, 0x01, 0x18, (byte) 0x83, 0x01,
                0x60, (byte) 0xa8, 0x0b, 0x03, 0x05, 0x03, (byte) 0xef, (byte) 0xff,
                0x1c, (byte) 0xe8, 0x03, 0x02, 0x04, (byte) 0x80, (byte) 0x89, 0x00,
                (byte) 0xaa, 0x04, 0x04, 0x02, 0x05, 0x07, (byte) 0xab, 0x0f,
                0x30, 0x0d, 0x04, 0x03, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x05,
                0x00, (byte) 0x80, 0x04, (byte) 0xf5, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xac,
                0x25, 0x30, 0x23, 0x04, 0x03, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x03, 0x02, 0x05, (byte) 0xe0, (byte) 0x80, 0x12, 0x00, (byte) 0xc0,
                0x00, 0x00, (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0x81, 0x04, (byte) 0xf5, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xad, (byte) 0x82,
                0x01, 0x42, (byte) 0xa0, 0x1d, 0x30, 0x14, 0x30, 0x12,
                0x0a, 0x01, 0x04, 0x02, 0x01, 0x07, (byte) 0x80, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81,
                0x01, 0x00, (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x00, (byte) 0x82,
                0x00, (byte) 0xa2, 0x17, 0x30, 0x11, 0x30, 0x06, 0x04,
                0x01, (byte) 0xf2, 0x04, 0x01, (byte) 0x9b, 0x04, 0x07, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x80, 0x00,
                (byte) 0x81, 0x00, (byte) 0xa4, 0x2d, 0x30, 0x2b, 0x0a, 0x01,
                0x04, (byte) 0xa0, 0x13, (byte) 0x80, 0x01, 0x01, (byte) 0xa1, 0x09,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x41, (byte) 0x87, 0x40,
                0x23, (byte) 0xa2, 0x03, 0x02, 0x01, 0x01, (byte) 0xa1, 0x06,
                (byte) 0x82, 0x01, 0x18, (byte) 0x83, 0x01, 0x60, (byte) 0x82, 0x01,
                0x01, (byte) 0xa3, 0x06, 0x04, 0x01, 0x51, 0x04, 0x01,
                0x39, (byte) 0x83, 0x00, (byte) 0xa5, 0x18, 0x30, 0x06, 0x04,
                0x01, 0x02, 0x04, 0x01, 0x00, 0x02, 0x01, 0x07,
                (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                (byte) 0x92, (byte) 0x82, 0x00, (byte) 0x83, 0x00, (byte) 0xa6, 0x1d, (byte) 0xa0,
                0x14, 0x30, 0x12, (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x01,
                0x07, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64,
                0x00, (byte) 0x92, (byte) 0x83, 0x01, 0x00, (byte) 0x81, 0x01, 0x02,
                (byte) 0x83, 0x00, (byte) 0x84, 0x00, (byte) 0xa7, 0x31, 0x30, 0x28,
                0x30, 0x12, 0x0a, 0x01, 0x0e, 0x02, 0x01, 0x07,
                (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                (byte) 0x92, (byte) 0x81, 0x01, 0x00, 0x30, 0x12, 0x0a, 0x01,
                0x0d, 0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00,
                (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x00, (byte) 0x82, 0x00, (byte) 0xa8,
                0x15, 0x30, 0x13, 0x0a, 0x01, 0x0e, (byte) 0xa0, 0x06,
                (byte) 0x82, 0x01, 0x18, (byte) 0x83, 0x01, 0x60, (byte) 0xa1, 0x06,
                0x04, 0x01, 0x15, 0x04, 0x01, 0x39, (byte) 0xa9, 0x23,
                (byte) 0xa0, 0x1a, 0x30, 0x18, 0x04, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x41, (byte) 0x87, 0x40, 0x23, 0x02, 0x01, 0x07,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                (byte) 0x92, 0x0a, 0x01, 0x00, (byte) 0x81, 0x01, 0x02, (byte) 0x83,
                0x00, (byte) 0x84, 0x00, (byte) 0xaa, 0x1d, (byte) 0xa0, 0x14, 0x30,
                0x12, (byte) 0x80, 0x01, 0x02, (byte) 0x81, 0x01, 0x07, (byte) 0x82,
                0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92,
                (byte) 0x83, 0x01, 0x00, (byte) 0x81, 0x01, 0x02, (byte) 0x83, 0x00,
                (byte) 0x84, 0x00, (byte) 0xab, 0x10, 0x30, 0x0e, 0x0a, 0x01,
                0x02, (byte) 0xa0, 0x09, 0x0a, 0x01, 0x00, 0x0a, 0x01,
                0x01, 0x0a, 0x01, 0x02, (byte) 0xaf, 0x05, (byte) 0x80, 0x03,
                0x23, 0x54, 0x08, (byte) 0x98, 0x01, 0x00, (byte) 0x9a, 0x02,
                0x00, (byte) 0xc8, (byte) 0xbc, 0x0c, (byte) 0x80, 0x01, 0x21, (byte) 0x81,
                0x01, 0x0a, (byte) 0x82, 0x01, 0x02, (byte) 0x83, 0x01, 0x04,
                (byte) 0x9d, 0x01, 0x04, (byte) 0x92, 0x02, 0x02, 0x00, (byte) 0x93,
                0x02, 0x02, 0x24, (byte) 0x94, 0x01, (byte) 0xff, (byte) 0xbf, 0x1f,
                0x75, (byte) 0x80, 0x09, 0x51, 0x5c, 0x53, 0x54, 0x55,
                0x56, 0x57, 0x58, 0x59, (byte) 0x82, 0x01, 0x00, (byte) 0xa3,
                0x08, (byte) 0x80, 0x02, 0x08, 0x00, (byte) 0x81, 0x02, 0x10,
                0x00, (byte) 0xa4, 0x4e, 0x02, 0x01, 0x01, 0x05, 0x00,
                (byte) 0xa1, 0x47, 0x30, 0x45, (byte) 0x80, 0x01, 0x01, (byte) 0x81,
                0x01, 0x03, (byte) 0x83, 0x09, 0x08, 0x69, 0x6e, 0x74,
                0x65, 0x72, 0x6e, 0x65, 0x74, (byte) 0xa4, 0x0e, (byte) 0x80,
                0x01, 0x05, (byte) 0xa1, 0x09, (byte) 0x80, 0x01, 0x09, (byte) 0x81,
                0x01, (byte) 0xff, (byte) 0x82, 0x01, 0x00, (byte) 0x87, 0x00, (byte) 0x88,
                0x02, 0x02, 0x00, (byte) 0xa9, 0x08, (byte) 0x80, 0x02, 0x08,
                0x00, (byte) 0x81, 0x02, 0x10, 0x00, (byte) 0x8c, 0x01, 0x15,
                (byte) 0x8d, 0x09, 0x51, 0x5c, 0x53, 0x54, 0x55, 0x56,
                0x57, 0x58, 0x59, (byte) 0x8e, 0x01, 0x00, (byte) 0x8f, 0x01,
                0x02, (byte) 0x86, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94,
                0x00, 0x00, (byte) 0x87, 0x00, (byte) 0x88, 0x00, (byte) 0xbf, 0x20,
                0x1c, 0x30, 0x1a, 0x03, 0x05, 0x05, (byte) 0xc0, 0x00,
                0x00, 0x60, 0x04, 0x04, (byte) 0xea, 0x31, 0x74, 0x6a,
                (byte) 0xa0, 0x0b, 0x04, 0x09, 0x08, 0x69, 0x6e, 0x74,
                0x65, 0x72, 0x6e, 0x65, 0x74, (byte) 0x9f, 0x21, 0x00,
                (byte) 0x9f, 0x23, 0x31, 0x6d, 0x6d, 0x65, 0x63, 0x32,
                0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69, 0x38,
                0x30, 0x30, 0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d,
                0x6e, 0x63, 0x30, 0x30, 0x31, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x9f, 0x24, 0x02, 0x01,
                0x2c, (byte) 0x9f, 0x25, 0x00, (byte) 0x9f, 0x26, 0x01, 0x00,
                (byte) 0x9f, 0x27, 0x02, 0x01, 0x68, (byte) 0xbf, 0x28, 0x1c,
                0x30, 0x1a, 0x03, 0x05, 0x05, (byte) 0xc0, 0x00, 0x00,
                0x60, 0x04, 0x04, (byte) 0xea, 0x31, 0x74, 0x6a, (byte) 0xa0,
                0x0b, 0x04, 0x09, 0x08, 0x69, 0x6e, 0x74, 0x65,
                0x72, 0x6e, 0x65, 0x74, (byte) 0x9f, 0x29, 0x08, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x65, 0x28, 0x54, (byte) 0xf1, (byte) 0x9f,
                0x2b, 0x00, (byte) 0x9f, 0x2c, 0x00, (byte) 0x9f, 0x2d, 0x00,
                (byte) 0xbf, 0x2e, 0x12, 0x30, 0x10, (byte) 0x80, 0x03, 0x62,
                (byte) 0x92, (byte) 0x99, (byte) 0x81, 0x02, 0x02, 0x24, (byte) 0x82, 0x05,
                0x00, (byte) 0x80, 0x00, 0x00, 0x00, (byte) 0xbf, 0x2f, 0x0f,
                0x30, 0x0d, (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x03, 0x62,
                (byte) 0x92, (byte) 0x99, (byte) 0x82, 0x03, 0x31, 0x32, 0x30, (byte) 0x9f,
                0x30, 0x04, 0x00, 0x00, 0x00, (byte) 0x87, (byte) 0x9f, 0x31,
                0x00, (byte) 0x9f, 0x32, 0x01, 0x00, (byte) 0xbf, 0x34, 0x08,
                0x30, 0x06, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x01, 0x02,
                (byte) 0x9f, 0x35, 0x05, 0x00, (byte) 0x80, 0x00, 0x00, 0x00,
                (byte) 0x9f, 0x36, 0x00
        };
    }

    private byte[] getEncodedDataRel18wSubsDataToSgsn() {
        return new byte[] { 0x30, (byte) 0x82,
                0x01, (byte) 0x8c, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x00, (byte) 0xa1, 0x03, 0x04, 0x01, (byte) 0x81,
                (byte) 0xa2, (byte) 0x82, 0x01, 0x7a, (byte) 0x80, 0x08, 0x47, 0x08,
                0x62, 0x08, 0x00, 0x00, 0x00, (byte) 0xf0, (byte) 0x82, 0x01,
                0x0a, (byte) 0x83, 0x01, 0x00, (byte) 0xa6, 0x06, 0x04, 0x01,
                0x21, 0x04, 0x01, 0x70, (byte) 0xa7, 0x16, (byte) 0xa3, 0x09,
                0x04, 0x01, (byte) 0xf6, (byte) 0x84, 0x01, 0x05, (byte) 0x81, 0x01,
                0x01, (byte) 0xa3, 0x09, 0x04, 0x01, 0x44, (byte) 0x84, 0x01,
                0x05, (byte) 0x82, 0x01, 0x02, (byte) 0xa8, 0x0b, 0x03, 0x05,
                0x03, 0x1c, 0x06, 0x48, (byte) 0xf8, 0x03, 0x02, 0x04,
                0x50, (byte) 0x89, 0x00, (byte) 0xb0, 0x60, 0x05, 0x00, (byte) 0xa1,
                0x51, 0x30, 0x4f, 0x02, 0x01, 0x01, (byte) 0x90, 0x02,
                (byte) 0xf1, 0x21, (byte) 0x91, 0x01, 0x15, (byte) 0x92, 0x03, 0x27,
                0x32, 0x05, (byte) 0x93, 0x00, (byte) 0x94, 0x09, 0x08, 0x69,
                0x6e, 0x74, 0x65, 0x72, 0x6e, 0x65, 0x74, (byte) 0x80,
                0x09, 0x09, 0x72, (byte) 0x97, (byte) 0x80, 0x40, 0x00, (byte) 0xa3,
                0x40, 0x00, (byte) 0x81, 0x02, 0x08, 0x00, (byte) 0x82, 0x03,
                0x10, 0x00, 0x00, (byte) 0x83, 0x02, 0x00, 0x00, (byte) 0x84,
                0x01, 0x5b, (byte) 0x85, 0x09, 0x51, 0x5c, 0x53, 0x54,
                0x55, 0x56, 0x57, 0x58, 0x59, (byte) 0x86, 0x02, 0x3a,
                0x3b, (byte) 0x87, 0x01, 0x3c, (byte) 0x88, 0x01, 0x00, (byte) 0x89,
                0x01, 0x02, (byte) 0x83, 0x09, 0x51, 0x5c, 0x53, 0x54,
                0x55, 0x56, 0x57, 0x58, 0x59, (byte) 0x98, 0x01, 0x00,
                (byte) 0x9a, 0x02, 0x00, (byte) 0xc8, (byte) 0x9b, 0x01, 0x07, (byte) 0xb1,
                (byte) 0x81, (byte) 0x86, (byte) 0xa0, 0x1d, (byte) 0xa0, 0x14, 0x30, 0x12,
                (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x0c, (byte) 0x82, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x83,
                0x01, 0x00, (byte) 0x81, 0x01, 0x03, (byte) 0x83, 0x00, (byte) 0x84,
                0x00, (byte) 0xa1, 0x1d, (byte) 0xa0, 0x14, 0x30, 0x12, (byte) 0x80,
                0x01, 0x02, (byte) 0x81, 0x01, 0x07, (byte) 0x82, 0x07, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x83, 0x01,
                0x00, (byte) 0x81, 0x01, 0x03, (byte) 0x83, 0x00, (byte) 0x84, 0x00,
                (byte) 0xa3, 0x1d, (byte) 0xa0, 0x14, 0x30, 0x12, (byte) 0x80, 0x01,
                0x02, (byte) 0x81, 0x01, 0x07, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x83, 0x01, 0x00,
                (byte) 0x81, 0x01, 0x03, (byte) 0x83, 0x00, (byte) 0x84, 0x00, (byte) 0xa4,
                0x10, 0x30, 0x0e, 0x0a, 0x01, 0x02, (byte) 0xa0, 0x09,
                0x0a, 0x01, 0x00, 0x0a, 0x01, 0x01, 0x0a, 0x01,
                0x02, (byte) 0xa5, 0x15, 0x30, 0x03, 0x04, 0x01, (byte) 0x83,
                0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
                0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x82, 0x00, (byte) 0x83, 0x00,
                (byte) 0x92, 0x02, 0x02, 0x00, (byte) 0x93, 0x02, 0x02, 0x24,
                (byte) 0x9f, 0x21, 0x00, (byte) 0x9f, 0x22, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, (byte) 0x94, 0x00, 0x00, (byte) 0x9f, 0x24, 0x02,
                0x01, 0x2c, (byte) 0x9f, 0x25, 0x00, (byte) 0x9f, 0x26, 0x01,
                0x00, (byte) 0x9f, 0x2a, 0x00, (byte) 0x9f, 0x2b, 0x00, (byte) 0x9f,
                0x2c, 0x00, (byte) 0x9f, 0x2d, 0x00, (byte) 0x9f, 0x30, 0x04,
                0x00, 0x00, 0x00, (byte) 0x87, (byte) 0x9f, 0x31, 0x00, (byte) 0x9f,
                0x32, 0x01, 0x00, (byte) 0xbf, 0x34, 0x08, 0x30, 0x06,
                (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x02
        };
    }

    private byte[] getEncodedDataRel18wSubsDataDelToVLR() {
        return new byte[] { 0x30, 0x72,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00,
                0x00, (byte) 0xa1, 0x09, 0x04, 0x01, (byte) 0x82, 0x04, 0x04,
                0x01, 0x02, 0x04, (byte) 0x82, (byte) 0xa3, 0x5c, (byte) 0x80, 0x08,
                0x47, 0x08, 0x62, 0x08, 0x00, 0x00, 0x00, (byte) 0xf0,
                (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x23, (byte) 0x83, 0x01, (byte) 0x00,
                (byte) 0xa2, 0x03, 0x04, 0x01, (byte) 0xf2, (byte) 0x85, 0x02, 0x21,
                0x0f, (byte) 0x87, 0x00, (byte) 0x88, 0x00, (byte) 0x89, 0x00, (byte) 0x8b,
                0x00, (byte) 0xac, 0x0c, 0x30, 0x0a, 0x04, 0x03, 0x0c,
                0x0a, 0x01, 0x04, 0x03, 0x0c, 0x0c, 0x02, (byte) 0x8d,
                0x00, (byte) 0x8e, 0x00, (byte) 0x8f, 0x03, 0x02, (byte) 0x90, 0x00,
                (byte) 0x90, 0x00, (byte) 0x91, 0x00, (byte) 0x93, 0x00, (byte) 0x94, 0x00,
                (byte) 0x96, 0x00, (byte) 0x97, 0x00, (byte) 0x95, 0x00, (byte) 0x98, 0x00,
                (byte) 0x99, 0x00, (byte) 0x9a, 0x00, (byte) 0x9b, 0x00, (byte) 0x9c, 0x00,
                (byte) 0x9d, 0x00, (byte) 0x9e, 0x00, (byte) 0x9f, 0x1f, 0x00, (byte) 0x9f,
                0x20, 0x00
        };
    }

    private byte[] getEncodedDataRel18wSubsDataToVLR2() {
        return new byte[] { 0x30, (byte) 0x82,
                0x05, 0x1a, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                (byte) 0x94, 0x00, 0x50, (byte) 0xa1, 0x09, 0x04, 0x01, (byte) 0x81,
                0x04, 0x04, 0x01, 0x02, 0x04, (byte) 0x82, (byte) 0xa2, (byte) 0x82,
                0x05, 0x02, (byte) 0x80, 0x08, 0x47, 0x08, 0x62, 0x08,
                0x00, 0x00, 0x00, (byte) 0xf0, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x95,
                (byte) 0x98, 0x09, 0x77, 0x39, (byte) 0xf7, (byte) 0x82, 0x01, 0x0a,
                (byte) 0x83, 0x01, 0x00, (byte) 0xa6, 0x06, 0x04, 0x01, 0x21,
                0x04, 0x01, 0x22, (byte) 0xa7, 0x26, (byte) 0xa3, 0x11, 0x04,
                0x01, 0x2a, (byte) 0x84, 0x01, 0x05, (byte) 0x81, 0x01, 0x01,
                0x30, 0x06, (byte) 0x82, 0x01, 0x00, (byte) 0x83, 0x01, 0x63,
                (byte) 0xa3, 0x11, 0x04, 0x01, 0x42, (byte) 0x84, 0x01, 0x05,
                (byte) 0x82, 0x01, 0x02, 0x30, 0x06, (byte) 0x82, 0x01, 0x00,
                (byte) 0x83, 0x01, 0x63, (byte) 0xa8, 0x0b, 0x03, 0x05, 0x03,
                (byte) 0xef, (byte) 0xff, 0x1c, (byte) 0xe8, 0x03, 0x02, 0x04, (byte) 0x80,
                (byte) 0x89, 0x00, (byte) 0xab, 0x0f, 0x30, 0x0d, 0x04, 0x03,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x05, 0x00, (byte) 0x80, 0x04, (byte) 0xf5,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xac, 0x25, 0x30, 0x23, 0x04,
                0x03, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x03, 0x02, 0x05, (byte) 0xe0,
                (byte) 0x80, 0x12, 0x00, (byte) 0xc0, 0x00, 0x00, (byte) 0x80, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte) 0x81, 0x04, (byte) 0xf5, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xad, (byte) 0x82, 0x01, 0x42, (byte) 0xa0, 0x1d,
                0x30, 0x14, 0x30, 0x12, 0x0a, 0x01, 0x04, 0x02,
                0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00, (byte) 0x80, 0x01,
                0x02, (byte) 0x81, 0x00, (byte) 0x82, 0x00, (byte) 0xa2, 0x17, 0x30,
                0x11, 0x30, 0x06, 0x04, 0x01, 0x2a, 0x04, 0x01,
                0x42, 0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64,
                0x00, (byte) 0x92, (byte) 0x80, 0x00, (byte) 0x81, 0x00, (byte) 0xa4, 0x2d,
                0x30, 0x2b, 0x0a, 0x01, 0x04, (byte) 0xa0, 0x13, (byte) 0x80,
                0x01, 0x01, (byte) 0xa1, 0x09, 0x04, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x41, (byte) 0x87, 0x40, 0x23, (byte) 0xa2, 0x03, 0x02,
                0x01, 0x01, (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x00, (byte) 0x83,
                0x01, 0x63, (byte) 0x82, 0x01, 0x01, (byte) 0xa3, 0x06, 0x04,
                0x01, 0x51, 0x04, 0x01, 0x39, (byte) 0x83, 0x00, (byte) 0xa5,
                0x18, 0x30, 0x06, 0x04, 0x01, 0x02, 0x04, 0x01,
                0x00, 0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x82, 0x00, (byte) 0x83,
                0x00, (byte) 0xa6, 0x1d, (byte) 0xa0, 0x14, 0x30, 0x12, (byte) 0x80,
                0x01, 0x02, (byte) 0x81, 0x01, 0x07, (byte) 0x82, 0x07, (byte) 0x91,
                (byte) 0x94, 0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x83, 0x01,
                0x00, (byte) 0x81, 0x01, 0x02, (byte) 0x83, 0x00, (byte) 0x84, 0x00,
                (byte) 0xa7, 0x31, 0x30, 0x28, 0x30, 0x12, 0x0a, 0x01,
                0x0e, 0x02, 0x01, 0x07, (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x81, 0x01, 0x00,
                0x30, 0x12, 0x0a, 0x01, 0x0d, 0x02, 0x01, 0x07,
                (byte) 0x80, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                (byte) 0x92, (byte) 0x81, 0x01, 0x00, (byte) 0x80, 0x01, 0x02, (byte) 0x81,
                0x00, (byte) 0x82, 0x00, (byte) 0xa8, 0x15, 0x30, 0x13, 0x0a,
                0x01, 0x0e, (byte) 0xa0, 0x06, (byte) 0x82, 0x01, 0x00, (byte) 0x83,
                0x01, 0x63, (byte) 0xa1, 0x06, 0x04, 0x01, 0x15, 0x04,
                0x01, 0x39, (byte) 0xa9, 0x23, (byte) 0xa0, 0x1a, 0x30, 0x18,
                0x04, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x41, (byte) 0x87, 0x40,
                0x23, 0x02, 0x01, 0x07, 0x04, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, (byte) 0x92, 0x0a, 0x01, 0x00,
                (byte) 0x81, 0x01, 0x02, (byte) 0x83, 0x00, (byte) 0x84, 0x00, (byte) 0xaa,
                0x1d, (byte) 0xa0, 0x14, 0x30, 0x12, (byte) 0x80, 0x01, 0x02,
                (byte) 0x81, 0x01, 0x07, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x94, 0x71,
                0x01, 0x64, 0x00, (byte) 0x92, (byte) 0x83, 0x01, 0x00, (byte) 0x81,
                0x01, 0x02, (byte) 0x83, 0x00, (byte) 0x84, 0x00, (byte) 0xab, 0x10,
                0x30, 0x0e, 0x0a, 0x01, 0x02, (byte) 0xa0, 0x09, 0x0a,
                0x01, 0x00, 0x0a, 0x01, 0x01, 0x0a, 0x01, 0x02,
                (byte) 0xaf, 0x05, (byte) 0x80, 0x03, 0x23, 0x54, 0x08, (byte) 0x98,
                0x01, 0x00, (byte) 0xb6, (byte) 0x82, 0x01, (byte) 0x95, (byte) 0xa0, 0x0a,
                0x04, 0x08, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00,
                0x23, (byte) 0xf1, (byte) 0xa1, (byte) 0x82, 0x01, 0x1b, 0x30, 0x46,
                0x04, 0x01, (byte) 0xc0, 0x04, 0x01, 0x08, (byte) 0x80, 0x01,
                0x00, (byte) 0xa1, 0x12, 0x30, 0x10, 0x30, 0x08, (byte) 0x80,
                0x06, (byte) 0x9f, 0x78, (byte) 0x94, 0x72, (byte) 0x94, (byte) 0xf2, (byte) 0x80,
                0x01, 0x01, (byte) 0x81, 0x01, 0x00, (byte) 0xa2, 0x06, 0x0a,
                0x01, 0x00, 0x0a, 0x01, 0x01, (byte) 0xa4, 0x12, 0x30,
                0x10, 0x30, 0x08, (byte) 0x80, 0x06, (byte) 0x9f, 0x78, (byte) 0x94,
                0x72, (byte) 0x94, (byte) 0xf2, (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01,
                0x00, (byte) 0xa5, 0x0b, 0x30, 0x09, 0x02, 0x01, 0x01,
                (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x00, 0x30, 0x43,
                0x04, 0x01, (byte) 0xc2, 0x04, 0x01, 0x04, (byte) 0x80, 0x01,
                0x01, (byte) 0xa1, 0x12, 0x30, 0x10, 0x30, 0x08, (byte) 0x80,
                0x06, (byte) 0x9f, (byte) 0x93, 0x28, (byte) 0x97, 0x22, (byte) 0xf2, (byte) 0x80,
                0x01, 0x00, (byte) 0x81, 0x01, 0x01, (byte) 0xa2, 0x03, 0x0a,
                0x01, 0x01, (byte) 0xa4, 0x12, 0x30, 0x10, 0x30, 0x08,
                (byte) 0x80, 0x06, (byte) 0x9f, (byte) 0x93, 0x28, (byte) 0x97, 0x22, (byte) 0xf2,
                (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x01, (byte) 0xa5, 0x0b,
                0x30, 0x09, 0x02, 0x01, 0x02, (byte) 0x80, 0x01, 0x00,
                (byte) 0x81, 0x01, 0x01, 0x30, 0x45, 0x04, 0x01, (byte) 0xb0,
                0x04, 0x01, 0x02, (byte) 0x80, 0x01, 0x03, (byte) 0xa1, 0x13,
                0x30, 0x11, 0x30, 0x09, (byte) 0x80, 0x07, (byte) 0x9f, 0x32,
                0x45, 0x23, 0x25, 0x32, (byte) 0xf4, (byte) 0x80, 0x01, 0x01,
                (byte) 0x81, 0x01, 0x03, (byte) 0xa2, 0x03, 0x0a, 0x01, 0x04,
                (byte) 0xa4, 0x13, 0x30, 0x11, 0x30, 0x09, (byte) 0x80, 0x07,
                (byte) 0x9f, 0x32, 0x45, 0x23, 0x25, 0x32, (byte) 0xf4, (byte) 0x80,
                0x01, 0x01, (byte) 0x81, 0x01, 0x03, (byte) 0xa5, 0x0b, 0x30,
                0x09, 0x02, 0x01, 0x03, (byte) 0x80, 0x01, 0x01, (byte) 0x81,
                0x01, 0x03, 0x30, 0x45, 0x04, 0x01, (byte) 0xf0, 0x04,
                0x01, 0x01, (byte) 0x80, 0x01, 0x02, (byte) 0xa1, 0x13, 0x30,
                0x11, 0x30, 0x09, (byte) 0x80, 0x07, (byte) 0x9f, (byte) 0x95, (byte) 0x98,
                0x09, (byte) 0x92, 0x28, 0x54, (byte) 0x80, 0x01, 0x00, (byte) 0x81,
                0x01, 0x02, (byte) 0xa2, 0x03, 0x0a, 0x01, 0x03, (byte) 0xa4,
                0x13, 0x30, 0x11, 0x30, 0x09, (byte) 0x80, 0x07, (byte) 0x9f,
                (byte) 0x95, (byte) 0x98, 0x09, (byte) 0x92, 0x28, 0x54, (byte) 0x80, 0x01,
                0x00, (byte) 0x81, 0x01, 0x02, (byte) 0xa5, 0x0b, 0x30, 0x09,
                0x02, 0x01, 0x04, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01,
                0x02, (byte) 0xa2, 0x18, 0x30, 0x06, 0x04, 0x01, (byte) 0xc0,
                0x04, 0x01, 0x08, 0x30, 0x06, 0x04, 0x01, (byte) 0xc2,
                0x04, 0x01, 0x04, 0x30, 0x06, 0x04, 0x01, (byte) 0xb0,
                0x04, 0x01, 0x02, (byte) 0xa3, 0x4e, 0x30, 0x4c, 0x04,
                0x01, (byte) 0xf1, 0x04, 0x01, 0x0d, (byte) 0x80, 0x01, 0x00,
                (byte) 0xa1, 0x12, 0x30, 0x10, 0x30, 0x08, (byte) 0x80, 0x06,
                (byte) 0x9f, 0x78, (byte) 0x94, 0x72, (byte) 0x94, (byte) 0xf2, (byte) 0x80, 0x01,
                0x01, (byte) 0x81, 0x01, 0x00, (byte) 0xa2, 0x0c, 0x0a, 0x01,
                0x00, 0x0a, 0x01, 0x01, 0x0a, 0x01, 0x04, 0x0a,
                0x01, 0x03, (byte) 0xa4, 0x12, 0x30, 0x10, 0x30, 0x08,
                (byte) 0x80, 0x06, (byte) 0x9f, 0x78, (byte) 0x94, 0x72, (byte) 0x94, (byte) 0xf2,
                (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x00, (byte) 0xa5, 0x0b,
                0x30, 0x09, 0x02, 0x01, 0x05, (byte) 0x80, 0x01, 0x01,
                (byte) 0x81, 0x01, 0x00, (byte) 0x9a, 0x02, 0x00, (byte) 0xc8, (byte) 0xbc,
                0x0c, (byte) 0x80, 0x01, 0x21, (byte) 0x81, 0x01, 0x0a, (byte) 0x82,
                0x01, 0x02, (byte) 0x83, 0x01, 0x04, (byte) 0x9d, 0x01, 0x04,
                (byte) 0x92, 0x02, 0x02, 0x00, (byte) 0x93, 0x02, 0x02, 0x24,
                (byte) 0x94, 0x01, (byte) 0xff, (byte) 0xbf, 0x1f, 0x75, (byte) 0x80, 0x09,
                0x51, 0x5c, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58,
                0x59, (byte) 0x82, 0x01, 0x00, (byte) 0xa3, 0x08, (byte) 0x80, 0x02,
                0x08, 0x00, (byte) 0x81, 0x02, 0x10, 0x00, (byte) 0xa4, 0x4e,
                0x02, 0x01, 0x01, 0x05, 0x00, (byte) 0xa1, 0x47, 0x30,
                0x45, (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x01, 0x03, (byte) 0x83,
                0x09, 0x08, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x6e,
                0x65, 0x74, (byte) 0xa4, 0x0e, (byte) 0x80, 0x01, 0x05, (byte) 0xa1,
                0x09, (byte) 0x80, 0x01, 0x09, (byte) 0x81, 0x01, (byte) 0xff, (byte) 0x82,
                0x01, 0x00, (byte) 0x87, 0x00, (byte) 0x88, 0x02, 0x02, 0x00,
                (byte) 0xa9, 0x08, (byte) 0x80, 0x02, 0x08, 0x00, (byte) 0x81, 0x02,
                0x10, 0x00, (byte) 0x8c, 0x01, 0x15, (byte) 0x8d, 0x09, 0x51,
                0x5c, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59,
                (byte) 0x8e, 0x01, 0x00, (byte) 0x8f, 0x01, 0x02, (byte) 0x86, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00, 0x00, (byte) 0x87,
                0x00, (byte) 0x88, 0x00, (byte) 0xbf, 0x20, 0x1c, 0x30, 0x1a,
                0x03, 0x05, 0x05, (byte) 0xc0, 0x00, 0x00, 0x60, 0x04,
                0x04, (byte) 0xea, 0x31, 0x74, 0x6a, (byte) 0xa0, 0x0b, 0x04,
                0x09, 0x08, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x6e,
                0x65, 0x74, (byte) 0x9f, 0x21, 0x00, (byte) 0x9f, 0x23, 0x31,
                0x6d, 0x6d, 0x65, 0x63, 0x32, 0x30, 0x2e, 0x6d,
                0x6d, 0x65, 0x67, 0x69, 0x38, 0x30, 0x30, 0x2e,
                0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30,
                0x30, 0x31, 0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34,
                0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e, 0x65,
                0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72,
                0x67, (byte) 0x9f, 0x24, 0x02, 0x01, 0x2c, (byte) 0x9f, 0x25,
                0x00, (byte) 0x9f, 0x26, 0x01, 0x00, (byte) 0x9f, 0x27, 0x02,
                0x01, 0x68, (byte) 0xbf, 0x28, 0x1c, 0x30, 0x1a, 0x03,
                0x05, 0x05, (byte) 0xc0, 0x00, 0x00, 0x60, 0x04, 0x04,
                (byte) 0xea, 0x31, 0x74, 0x6a, (byte) 0xa0, 0x0b, 0x04, 0x09,
                0x08, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x65,
                0x74, (byte) 0x9f, 0x29, 0x08, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x65, 0x28, 0x54, (byte) 0xf1, (byte) 0x9f, 0x2b, 0x00, (byte) 0x9f,
                0x2c, 0x00, (byte) 0x9f, 0x2d, 0x00, (byte) 0xbf, 0x2e, 0x12,
                0x30, 0x10, (byte) 0x80, 0x03, 0x62, (byte) 0x92, (byte) 0x99, (byte) 0x81,
                0x02, 0x02, 0x24, (byte) 0x82, 0x05, 0x00, (byte) 0x80, 0x00,
                0x00, 0x00, (byte) 0xbf, 0x2f, 0x0f, 0x30, 0x0d, (byte) 0x80,
                0x01, 0x01, (byte) 0x81, 0x03, 0x62, (byte) 0x92, (byte) 0x99, (byte) 0x82,
                0x03, 0x31, 0x32, 0x30, (byte) 0x9f, 0x30, 0x04, 0x00,
                0x00, 0x00, (byte) 0x87, (byte) 0x9f, 0x31, 0x00, (byte) 0x9f, 0x32,
                0x01, 0x00, (byte) 0xbf, 0x33, 0x09, 0x04, 0x01, (byte) 0x81,
                0x04, 0x04, 0x01, 0x02, 0x04, (byte) 0x82, (byte) 0xbf, 0x34,
                0x08, 0x30, 0x06, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x01,
                0x02, (byte) 0x9f, 0x35, 0x05, 0x00, (byte) 0x80, 0x00, 0x00,
                0x00, (byte) 0x9f, 0x36, 0x00
        };
    }

    private byte[] getEncodedDataRel18wSubsDataDelToSGSN() {
        return new byte[] { 0x30, 0x69,
                (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01, (byte) 0x94, 0x00,
                0x00, (byte) 0xa1, 0x09, 0x04, 0x01, (byte) 0x82, 0x04, 0x04,
                0x01, 0x02, 0x04, (byte) 0x82, (byte) 0xa3, 0x53, (byte) 0x80, 0x08,
                0x47, 0x08, 0x62, 0x08, 0x00, 0x00, 0x00, (byte) 0xf0,
                (byte) 0xa1, 0x06, (byte) 0x82, 0x01, 0x12, (byte) 0x83, 0x01, 0x00,
                (byte) 0xa2, 0x03, 0x04, 0x01, (byte) 0x92, (byte) 0x85, 0x02, 0x21,
                0x0f, (byte) 0x89, 0x00, (byte) 0xaa, 0x08, 0x30, 0x06, 0x02,
                0x01, 0x01, 0x02, 0x01, 0x02, (byte) 0x8b, 0x00, (byte) 0xac,
                0x02, 0x05, 0x00, (byte) 0x8d, 0x00, (byte) 0x8e, 0x00, (byte) 0x90,
                0x00, (byte) 0x91, 0x00, (byte) 0x93, 0x00, (byte) 0x94, 0x00, (byte) 0x96,
                0x00, (byte) 0x97, 0x00, (byte) 0x95, 0x00, (byte) 0x98, 0x00, (byte) 0x99,
                0x00, (byte) 0x9a, 0x00, (byte) 0x9b, 0x00, (byte) 0x9c, 0x00, (byte) 0x9d,
                0x00, (byte) 0x9e, 0x00, (byte) 0x9f, 0x1f, 0x00, (byte) 0x9f, 0x20,
                0x00
        };
    }

    @Test(groups = { "functional.decode", "service.mobility.faultRecovery" })
    public void testDecode() throws Exception {

        // test 1 (MAP v1)
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ResetRequestImpl prim = new ResetRequestImpl(1);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        NetworkResource networkResource = prim.getNetworkResource();
        ISDNAddressString hlrNumber = prim.getHlrNumber();
        ArrayList<IMSI> hlrList = prim.getHlrList();
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        ArrayList<ResetId> resetIdList = prim.getResetIdList();
        InsertSubscriberDataArgs subscriptionData = prim.getSubscriptionData();
        DeleteSubscriberDataArgs subscriptionDataDeletion = prim.getSubscriptionDataDeletion();

        assertEquals(prim.getMapProtocolVersion(), 1);
        assertEquals(networkResource, NetworkResource.hlr);
        assertEquals(hlrNumber.getAddress(), "12345");
        assertNull(hlrList);
        assertNull(extensionContainer);
        assertNull(resetIdList);
        assertNull(subscriptionData);
        assertNull(subscriptionDataDeletion);

        // test 2 (MAP v2, old version)
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();

        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        assertEquals(hlrNumber.getAddress(), "12345");
        assertEquals(hlrList.size(), 1);
        assertEquals(hlrList.get(0).getData(), "1234001");
        assertNull(extensionContainer);
        assertNull(resetIdList);
        assertNull(subscriptionData);
        assertNull(subscriptionDataDeletion);

        // test 3, 3GPP Release v18.0.0 with hlrList
        rawData = getEncodedDataRel18_0();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         hlr-List: 3 items
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             IMSI: 748026900000000
         *             [Association IMSI: 748026900000000]
         *             IMSI: 748027000000000
         *             [Association IMSI: 748027000000000]
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(hlrNumber.getAddress(), "491710490000");
        assertEquals(hlrList.size(), 3);
        assertEquals(hlrList.get(0).getData(), "748026800000000");
        assertEquals(hlrList.get(1).getData(), "748026900000000");
        assertEquals(hlrList.get(2).getData(), "748027000000000");
        assertNull(extensionContainer);
        assertNull(resetIdList);
        assertNull(subscriptionData);
        assertNull(subscriptionDataDeletion);

        // test 4, 3GPP Release v18.0.0 with resetIdList and subscriptionData
        rawData = getEncodedDataRel18wSubsDataToVLR();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 01020481
         *             Reset-Id: 01020482
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             bearerServiceList: 2 items
         *                 Ext-BearerServiceCode: allDataCDS-Services (24)
         *                 Ext-BearerServiceCode: dataCDA-1200-75bps (19)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: shortMessageMO-PP (34)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: allCallCompletionSS - all Call completion SS (64)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: cfnrc - call forwarding on mobile subscriber not reachable (43)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: efff1ce8
         *                     1... .... = allOG-CallsBarred: True
         *                     .1.. .... = internationalOGCallsBarred: True
         *                     ..1. .... = internationalOGCallsNotToHPLMN-CountryBarred: True
         *                     ...0 .... = premiumRateInformationOGCallsBarred: False
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..1. = interzonalOGCallsBarred: True
         *                     .... ...1 = interzonalOGCallsNotToHPLMN-CountryBarred: True
         *                     1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
         *                     .1.. .... = allECT-Barred: True
         *                     ..1. .... = chargeableECT-Barred: True
         *                     ...1 .... = internationalECT-Barred: True
         *                     .... 1... = interzonalECT-Barred: True
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...1 = allPacketOrientedServicesBarred: True
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .0.. .... = roamerAccessToVPLMN-AP-Barred: False
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...1 .... = allIC-CallsBarred: True
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...0 .... = registrationInterzonalCFNotToHPLMN-Barred: False
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 80
         *                     1... .... = plmn-SpecificBarringType1: True
         *                     .0.. .... = plmn-SpecificBarringType2: False
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...0 .... = plmn-SpecificBarringType4: False
         *             roamingRestrictionDueToUnsupportedFeature
         *             regionalSubscriptionData: 1 item
         *                 ZoneCode: 0507
         *             vbsSubscriptionData: 1 item
         *                 VoiceBroadcastData
         *                     groupid: ffffff
         *                         TBCD digits:
         *                     broadcastInitEntitlement
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vgcsSubscriptionData: 1 item
         *                 VoiceGroupCallData
         *                     groupId: ffffff
         *                         TBCD digits:
         *                     Padding: 5
         *                     additionalSubscriptions: e0
         *                         1... .... = privilegedUplinkRequest: True
         *                         .1.. .... = emergencyUplinkRequest: True
         *                         ..1. .... = emergencyReset: True
         *                     Padding: 0
         *                     additionalInfo: c000008000000000000000000000000000
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vlrCamelSubscriptionInfo
         *                 o-CSI
         *                     o-BcsmCamelTDPDataList: 1 item
         *                         O-BcsmCamelTDPData
         *                             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csiActive
         *                 ss-CSI
         *                     ss-CamelData
         *                         ss-EventList: 2 items
         *                             SS-Code: allCallCompletionSS - all Call completion SS (64)
         *                             SS-Code: cfnrc - call forwarding on mobile subscriber not reachable (43)
         *                         gsmSCF-Address: 91947101640092
         *                             1... .... = Extension: No Extension
         *                             .001 .... = Nature of number: International Number (0x1)
         *                             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                             E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 o-BcsmCamelTDP-CriteriaList: 1 item
         *                     O-BcsmCamelTDP-Criteria
         *                         o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                         destinationNumberCriteria
         *                             matchType: enabling (1)
         *                             destinationNumberList: 1 item
         *                                 ISDN-AddressString: 91947141874023
         *                                     1... .... = Extension: No Extension
         *                                     .001 .... = Nature of number: International Number (0x1)
         *                                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                     E.164 number (MSISDN): 491714780432
         *                             destinationNumberLengthList: 1 item
         *                                 DestinationNumberLengthList item: 1
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allDataCDS-Services (24)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: allFacsimileTransmissionServices (96)
         *                         callTypeCriteria: notForwarded (1)
         *                         o-CauseValueCriteria: 2 items
         *                             CauseValue: 51
         *                             CauseValue: 39
         *                 tif-CSI
         *                 m-CSI
         *                     mobilityTriggers: 2 items
         *                         MM-Code: 02
         *                         MM-Code: 00
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 vt-CSI
         *                     t-BcsmCamelTDPDataList: 2 items
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tBusy (13)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *                     T-BCSM-CAMEL-TDP-Criteria
         *                         t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *                         t-CauseValueCriteria: 2 items
         *                             CauseValue: 15
         *                             CauseValue: 39
         *                 d-CSI
         *                     dp-AnalysedInfoCriteriaList: 1 item
         *                         DP-AnalysedInfoCriterium
         *                             dialledNumber: 91947141874023
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491714780432
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *             naea-PreferredCI
         *                 naea-PreferredCIC: 235408
         *             networkAccessMode: packetAndCircuit (0)
         *             istAlertTimer: 200
         *             mc-SS-Info
         *                 ss-Code: cfu - call forwarding unconditional (33)
         *                 ss-Status: 0a
         *                 0000 .... = Unused: 0x0
         *                 .... .0.. = P bit: Not provisioned
         *                 .... ..1. = R bit: Registered
         *                 .... ...0 = A bit: not Active
         *                 nbrSB: 2
         *                 nbrUser: 4
         *             cs-AllocationRetentionPriority: 04
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ics-Indicator: True
         *             eps-SubscriptionData
         *                 apn-oi-Replacement: 515c53545556575859
         *                 rfsp-id: 0
         *                 ambr
         *                     max-RequestedBandwidth-UL: 2048
         *                     max-RequestedBandwidth-DL: 4096
         *                 apn-ConfigurationProfile
         *                     defaultContext: 1
         *                     completeDataListIncluded
         *                     epsDataList: 1 item
         *                         APN-Configuration
         *                             contextId: 1
         *                             pdn-Type: 03
         *                             apn: 08696e7465726e6574 - internet
         *                                 APN: internet
         *                             eps-qos-Subscribed
         *                                 qos-Class-Identifier: 5
         *                                 allocation-Retention-Priority
         *                                     priority-level: 9
         *                                     pre-emption-capability: True
         *                                     pre-emption-vulnerability: False
         *                             vplmnAddressAllowed
         *                             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *                             ambr
         *                                 max-RequestedBandwidth-UL: 2048
         *                                 max-RequestedBandwidth-DL: 4096
         *                             servedPartyIP-IPv6-Address: 15
         *                             apn-oi-Replacement: 515c53545556575859
         *                             sipto-Permission: siptoAboveRanAllowed (0)
         *                             lipa-Permission: lipaConditional (2)
         *                 stn-sr: 91947101940000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710490000
         *                 mps-CSPriority
         *                 mps-EPSPriority
         *             csg-SubscriptionDataList: 1 item
         *             ue-ReachabilityRequestIndicator
         *             mme-Name: mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             subscribedPeriodicLAUtimer: 360
         *             vplmn-Csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             additionalMSISDN: 91947101652854f1
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 4917105682451
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             adjacentAccessRestrictionDataList: 1 item
         *                 AdjacentAccessRestrictionData
         *                     plmnId: 629299
         *                     Padding: 2
         *                     accessRestrictionData: 24
         *                         0... .... = utranNotAllowed: False
         *                         .0.. .... = geranNotAllowed: False
         *                         ..1. .... = ganNotAllowed: True
         *                         ...0 .... = i-hspa-evolutionNotAllowed: False
         *                         .... 0... = wb-e-utranNotAllowed: False
         *                         .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                         .... ..0. = nb-iotNotAllowed: False
         *                         .... ...0 = enhancedCoverageNotAllowed: False
         *                     Padding: 0
         *                     ext-AccessRestrictionData: 80000000
         *                         1... .... = nrAsSecondaryRATNotAllowed: True
         *                         .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             imsi-Group-Id-List: 1 item
         *                 IMSI-GroupId
         *                     group-Service-Id: 1
         *                     plmnId: 629299
         *                     local-Group-ID: 313230
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: nb-iot (5)
         *                     eDRX-Cycle-Length-Value: 02
         *             Padding: 0
         *             ext-AccessRestrictionData: 80000000
         *                 1... .... = nrAsSecondaryRATNotAllowed: True
         *                 .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             iab-Operation-Allowed-Indicator
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        /*
         * sendingNodenumber
         */
        assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(hlrNumber.getAddress(), "491710490000");
        /*
         * hlrList
         */
        assertNull(hlrList);
        /*
         * extensionContainer
         */
        assertNull(extensionContainer);
        /*
         * resetIdList
         */
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 2);
        assertEquals(resetIdList.get(0).getData(), new byte[] { 0x01, 0x02, 0x04, (byte) 0x81});
        assertEquals(resetIdList.get(1).getData(), new byte[] { 0x01, 0x02, 0x04, (byte) 0x82});
        /*
         * subscriptionData
         */
        assertNotNull(subscriptionData);
        // imsi
        IMSI imsi = subscriptionData.getImsi();
        assertEquals(imsi.getData(), "748026800000000");
        // msisdn
        ISDNAddressString msisdn = subscriptionData.getMsisdn();
        assertNull(msisdn);
        // category
        Category category = subscriptionData.getCategory();
        assertEquals(category.getCategoryValue(), CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus
        SubscriberStatus subscriberStatus = subscriptionData.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.serviceGranted);
        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = subscriptionData.getBearerServiceList();
        assertEquals(bearerServiceList.size(), 2);
        assertEquals(bearerServiceList.get(0).getBearerServiceCodeValue(), BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(bearerServiceList.get(1).getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200_75bps);
        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = subscriptionData.getTeleserviceList();
        assertEquals(teleserviceList.size(), 2);
        assertEquals(teleserviceList.get(0).getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
        assertEquals(teleserviceList.get(1).getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMO_PP);
        // provisionedSS
        ArrayList<ExtSSInfo> provisionedSS = subscriptionData.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 2);
        ExtSSInfo extSSInfo = provisionedSS.get(0);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.plmn_specificSS_2);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getOverrideCategory(),
                OverrideCategory.overrideDisabled);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        extSSInfo = provisionedSS.get(1);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.bicRoam);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getCliRestrictionOption(),
                CliRestrictionOption.temporaryDefaultAllowed);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        // odbData
        ODBData odbData = subscriptionData.getODBData();
        ODBGeneralData oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainmentOGCallsBarred());
        assertTrue(oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertFalse(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInternationalCFBarred());
        ODBHPLMNData odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(odbHplmnData.getPlmnSpecificBarringType1());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType2());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType3());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType4());
        assertNull(odbData.getExtensionContainer());
        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = subscriptionData.getRoamingRestrictionDueToUnsupportedFeature();
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        // regionalSubscriptionData
        ArrayList<ZoneCode> regionalSubscriptionData = subscriptionData.getRegionalSubscriptionData();
        assertEquals(regionalSubscriptionData.size(), 1);
        assertEquals(regionalSubscriptionData.get(0).getData(), new byte[] {0x05, 0x07});
        // vbsSubscriptionData
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = subscriptionData.getVbsSubscriptionData();
        assertEquals(vbsSubscriptionData.size(), 1);
        System.out.println(vbsSubscriptionData.get(0).getGroupId().getGroupId());
        assertEquals(vbsSubscriptionData.get(0).getGroupId().getGroupId(), "");
        assertTrue(vbsSubscriptionData.get(0).getBroadcastInitEntitlement());
        assertEquals(vbsSubscriptionData.get(0).getLongGroupId().getLongGroupId(), "5");
        assertNull(vbsSubscriptionData.get(0).getExtensionContainer());
        // vgcsSubscriptionData
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = subscriptionData.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        VoiceGroupCallData voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertEquals(voiceGroupCallData.getGroupId().getGroupId(), "");
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertEquals(voiceGroupCallData.getLongGroupId().getLongGroupId(), "5");
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(0));
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(1));
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(24));
        assertNull(voiceGroupCallData.getExtensionContainer());
        // vlrCamelSubscriptionInfo
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = subscriptionData.getVlrCamelSubscriptionInfo();
        // vlrCamelSubscriptionInfo o-CSI
        OCSI oCsi = vlrCamelSubscriptionInfo.getOCsi();
        ArrayList<OBcsmCamelTDPData> oBcsmCamelTDPDataList = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(oBcsmCamelTDPDataList.size(), 1);
        OBcsmCamelTDPData oBcsmCamelTDPData = oBcsmCamelTDPDataList.get(0);
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(oBcsmCamelTDPData.getExtensionContainer());
        assertNull(oCsi.getExtensionContainer());
        assertEquals(oCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(oCsi.getNotificationToCSE());
        assertTrue(oCsi.getCsiActive());
        // vlrCamelSubscriptionInfo extensionContainer
        assertNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        // vlrCamelSubscriptionInfo ssCsi
        SSCSI ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        SSCamelData ssCamelData = ssCsi.getSsCamelData();
        ArrayList<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 2);
        SSCode ssEvent1 = ssEventList.get(0);
        assertNotNull(ssEvent1);
        assertEquals(ssEvent1.getSupplementaryCodeValue(), SupplementaryCodeValue.plmn_specificSS_2);
        SSCode ssEvent2 = ssEventList.get(1);
        assertNotNull(ssEvent2);
        assertEquals(ssEvent2.getSupplementaryCodeValue(), SupplementaryCodeValue.bicRoam);
        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertEquals(gsmSCFAddress.getAddress(), "491710460029");
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(ssCamelData.getExtensionContainer());
        assertNull(ssCsi.getExtensionContainer());
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo o-BcsmCamelTDP-CriteriaList
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);
        DestinationNumberCriteria destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        ArrayList<ISDNAddressString> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 1);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertEquals(destinationNumberOne.getAddress(), "491714780432");
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        ArrayList<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.notForwarded);
        ArrayList<CauseValue> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 2);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 0x51);
        assertNotNull(oCauseValueCriteria.get(1));
        assertEquals(oCauseValueCriteria.get(1).getData(), 0x39);
        // vlrCamelSubscriptionInfo tif-CSI
        assertTrue(vlrCamelSubscriptionInfo.getTifCsi());
        // vlrCamelSubscriptionInfo m-CSI
        MCSI mCsi = vlrCamelSubscriptionInfo.getMCsi();
        ArrayList<MMCode> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.IMSIAttach);
        MMCode mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(mmCode2.getMMCodeValue(), MMCodeValue.LocationUpdateInSameVLR);
        assertNotNull(mCsi);
        assertEquals(mCsi.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertEquals(gsmSCFAddressTwo.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(mCsi.getExtensionContainer());
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mo-sms-CSI
        SMSCSI smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 7);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        ISDNAddressString gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPData.getExtensionContainer());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo vt-CSI
        TCSI vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(tBcsmCamelTDPDataList.size(), 2);
        TBcsmCamelTDPData tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(1);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        // vlrCamelSubscriptionInfo t-BCSM-CAMEL-TDP-CriteriaList
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        ArrayList<ExtBasicServiceCode> basicServiceList = tbcsmCamelTdpCriteria.getBasicServiceCriteria();
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allDataCDS_Services);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.allFacsimileTransmissionServices);
        ArrayList<CauseValue> tCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(tCauseValueCriteriaLst);
        assertEquals(tCauseValueCriteriaLst.size(), 2);
        assertEquals(tCauseValueCriteriaLst.get(0).getData(), 0x15);
        assertEquals(tCauseValueCriteriaLst.get(1).getData(), 0x39);
        // vlrCamelSubscriptionInfo d-CSI
        DCSI dCsi = vlrCamelSubscriptionInfo.getDCsi();
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertEquals(dialledNumber.getAddress(), "491714780432");
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(gsmSCFAddressDp.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(dCsi.getExtensionContainer());
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mt-sms-CSI
        SMSCSI mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPData smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 7);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        ISDNAddressString gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertNull(mtSmsCSI.getExtensionContainer());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mt-smsCAMELTDP-CriteriaList
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 3);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);
        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSUBMITREPORT);
        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(2);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSTATUSREPORT);
        // naeaPreferredCI
        NAEAPreferredCI naeaPreferredCI = subscriptionData.getNAEAPreferredCI();
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getData(), new byte[] {0x23, 0x54, 0x08});
        // gprsSubscriptionData
        GPRSSubscriptionData gprsSubscriptionData = subscriptionData.getGPRSSubscriptionData();
        assertNull(gprsSubscriptionData);
        // roamingRestrictedInSgsnDueToUnsupportedFeature
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = subscriptionData.getRoamingRestrictedInSgsnDueToUnsupportedFeature();
        assertFalse(roamingRestrictedInSgsnDueToUnsupportedFeature);
        // networkAccessMode
        NetworkAccessMode networkAccessMode = subscriptionData.getNetworkAccessMode();
        assertEquals(networkAccessMode, NetworkAccessMode.packetAndCircuit);
        // lsaInformation
        LSAInformation lsaInformation = subscriptionData.getLSAInformation();
        assertNull(lsaInformation);
        // lmuIndicator
        boolean lmuIndicator = subscriptionData.getLmuIndicator();
        assertFalse(lmuIndicator);
        // lcsInformation
        LCSInformation lcsInformation = subscriptionData.getLCSInformation();
        assertNull(lcsInformation);
        // istAlertTimer
        Integer istAlertTimer = subscriptionData.getIstAlertTimer();
        assertEquals(istAlertTimer.intValue(), 200);
        // superChargerSupportedInHLR
        AgeIndicator superChargerSupportedInHLR = subscriptionData.getSuperChargerSupportedInHLR();
        assertNull(superChargerSupportedInHLR);
        // mcSsInfo
        MCSSInfo mcSsInfo = subscriptionData.getMcSsInfo();
        assertEquals(mcSsInfo.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(mcSsInfo.getSSStatus().getData(), new byte[] {0x0a});
        assertFalse(mcSsInfo.getSSStatus().getBitP());
        assertTrue(mcSsInfo.getSSStatus().getBitR());
        assertFalse(mcSsInfo.getSSStatus().getBitA());
        assertEquals(mcSsInfo.getNbrSB(), 2);
        assertEquals(mcSsInfo.getNbrUser(), 4);
        assertNull(mcSsInfo.getExtensionContainer());
        // csAllocationRetentionPriority
        CSAllocationRetentionPriority csAllocationRetentionPriority = subscriptionData.getCSAllocationRetentionPriority();
        assertEquals(csAllocationRetentionPriority.getData(), 4);
        // sgsnCamelSubscriptionInfo
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = subscriptionData.getSgsnCamelSubscriptionInfo();
        assertNull(sgsnCamelSubscriptionInfo);
        // chargingCharacteristics
        ChargingCharacteristics chargingCharacteristics = subscriptionData.getChargingCharacteristics();
        assertFalse(chargingCharacteristics.isNormalCharging());
        assertFalse(chargingCharacteristics.isPrepaidCharging());
        assertTrue(chargingCharacteristics.isFlatRateChargingCharging());
        assertFalse(chargingCharacteristics.isChargingByHotBillingCharging());
        // accessRestrictionData
        AccessRestrictionData accessRestrictionData = subscriptionData.getAccessRestrictionData();
        assertFalse(accessRestrictionData.getUtranNotAllowed());
        assertFalse(accessRestrictionData.getGeranNotAllowed());
        assertTrue(accessRestrictionData.getGanNotAllowed());
        assertFalse(accessRestrictionData.getEUtranNotAllowed());
        assertFalse(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // icsIndicator
        Boolean icsIndicator = subscriptionData.getIcsIndicator();
        assertTrue(icsIndicator);
        // epsSubscriptionData
        EPSSubscriptionData epsSubscriptionData = subscriptionData.getEpsSubscriptionData();
        assertNotNull(epsSubscriptionData);
        APNConfigurationProfile apnConfigurationProfile = epsSubscriptionData.getAPNConfigurationProfile();
        ArrayList<APNConfiguration> ePSDataList = apnConfigurationProfile.getEPSDataList();
        APNConfiguration apnConfiguration = ePSDataList.get(0);
        assertEquals(apnConfiguration.getContextId(), 1);
        assertEquals(apnConfiguration.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4v6);
        assertNull(apnConfiguration.getServedPartyIPIPv4Address());
        assertEquals(apnConfiguration.getApn().getApn(), "internet");
        assertEquals(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPriorityLevel(), 9);
        assertTrue(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPreEmptionCapability());
        assertFalse(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPreEmptionVulnerability());
        assertNull(apnConfiguration.getPdnGwIdentity());
        assertNull(apnConfiguration.getPdnGwAllocationType());
        assertTrue(apnConfiguration.getVplmnAddressAllowed());
        assertFalse(apnConfiguration.getChargingCharacteristics().isNormalCharging());
        assertFalse(apnConfiguration.getChargingCharacteristics().isPrepaidCharging());
        assertTrue(apnConfiguration.getChargingCharacteristics().isFlatRateChargingCharging());
        assertFalse(apnConfiguration.getChargingCharacteristics().isChargingByHotBillingCharging());
        assertEquals(apnConfiguration.getAmbr().getMaxRequestedBandwidthUL(), 2048);
        assertEquals(apnConfiguration.getAmbr().getMaxRequestedBandwidthDL(), 4096);
        assertNull(apnConfiguration.getAmbr().getExtensionContainer());
        assertNull(apnConfiguration.getSpecificAPNInfoList());
        assertNull(apnConfiguration.getExtensionContainer());
        assertEquals(apnConfiguration.getServedPartyIPIPv6Address().getData(), new byte[] {21});
        assertEquals(apnConfiguration.getApnOiReplacement().getData(), new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        assertEquals(apnConfiguration.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(apnConfiguration.getLipaPermission(), LIPAPermission.lipaConditional);
        assertNull(epsSubscriptionData.getAPNConfigurationProfile().getExtensionContainer());
        assertEquals(epsSubscriptionData.getApnOiReplacement().getData(), new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        assertEquals(epsSubscriptionData.getRfspId().intValue(), 0);
        assertEquals(epsSubscriptionData.getAmbr().getMaxRequestedBandwidthUL(), 2048);
        assertEquals(epsSubscriptionData.getAmbr().getMaxRequestedBandwidthDL(), 4096);
        assertNull(epsSubscriptionData.getAmbr().getExtensionContainer());
        assertEquals(epsSubscriptionData.getAPNConfigurationProfile().getDefaultContext(), 1);
        assertTrue(epsSubscriptionData.getAPNConfigurationProfile().getCompleteDataListIncluded());
        assertEquals(epsSubscriptionData.getAPNConfigurationProfile().getEPSDataList().get(0), apnConfiguration);
        assertEquals(epsSubscriptionData.getStnSr().getAddressNature(), AddressNature.international_number);
        assertEquals(epsSubscriptionData.getStnSr().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(epsSubscriptionData.getStnSr().getAddress(), "491710490000");
        assertNull(epsSubscriptionData.getExtensionContainer());
        assertTrue(epsSubscriptionData.getMpsCSPriority());
        assertTrue(epsSubscriptionData.getMpsEPSPriority());
        // csgSubscriptionDataList
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = subscriptionData.getCsgSubscriptionDataList();
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(0));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(1));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(25));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(26));
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getYear(), 2024);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getMonth(), 7);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getDay(), 4);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getHour(), 19);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getMinute(), 20);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getSecond(), 10);
        assertNull(csgSubscriptionDataList.get(0).getExtensionContainer());
        assertEquals(csgSubscriptionDataList.get(0).getLipaAllowedAPNList().get(0).getApn(), "internet");
        // ueReachabilityRequestIndicator
        boolean ueReachabilityRequestIndicator = subscriptionData.getUeReachabilityRequestIndicator();
        assertTrue(ueReachabilityRequestIndicator);
        // sgsnNumber
        ISDNAddressString sgsnNumber = subscriptionData.getSgsnNumber();
        assertNull(sgsnNumber);
        // mmeName
        DiameterIdentity mmeName = subscriptionData.getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        // subscribedPeriodicRAUTAUtimer
        Long subscribedPeriodicRAUTAUtimer = subscriptionData.getSubscribedPeriodicRAUTAUtimer();
        assertEquals(subscribedPeriodicRAUTAUtimer.longValue(), 300);
        // vplmnLIPAAllowed
        boolean vplmnLIPAAllowed = subscriptionData.getVplmnLIPAAllowed();
        assertTrue(vplmnLIPAAllowed);
        // mdtUserConsent
        boolean mdtUserConsent = subscriptionData.getMdtUserConsent();
        assertFalse(mdtUserConsent);
        // subscribedPeriodicLAUtimer
        Long subscribedPeriodicLAUtimer = subscriptionData.getSubscribedPeriodicLAUtimer();
        assertEquals(subscribedPeriodicLAUtimer.longValue(), 360);
        // vplmnCSGSubscriptionDataList
        ArrayList<CSGSubscriptionData> vplmnCSGSubscriptionDataList = subscriptionData.getVPLMNCSGSubscriptionDataList();
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(0));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(1));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(25));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(26));
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getYear(), 2024);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getMonth(), 7);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getDay(), 4);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getHour(), 19);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getMinute(), 20);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getSecond(), 10);
        assertNull(vplmnCSGSubscriptionDataList.get(0).getExtensionContainer());
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getLipaAllowedAPNList().get(0).getApn(), "internet");
        // additionalMSISDN
        ISDNAddressString additionalMSISDN = subscriptionData.getAdditionalMSISDN();
        assertEquals(additionalMSISDN.getAddressNature(), AddressNature.international_number);
        assertEquals(additionalMSISDN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(additionalMSISDN.getAddress(), "4917105682451");
        // psAndSMSOnlyServiceProvision
        boolean psAndSMSOnlyServiceProvision = subscriptionData.getPSandSMSOnlyServiceProvision();
        assertFalse(psAndSMSOnlyServiceProvision);
        // smsInSGSNAllowed
        boolean smsInSGSNAllowed = subscriptionData.getSMSInSGSNAllowed();
        assertTrue(smsInSGSNAllowed);
        // csToPsSRVCCAllowedIndicator
        boolean csToPsSRVCCAllowedIndicator = subscriptionData.getCsToPsSRVCCAllowedIndicator();
        assertTrue(csToPsSRVCCAllowedIndicator);
        // pcscfRestorationRequest
        boolean pcscfRestorationRequest = subscriptionData.getPCSCFRestorationRequest();
        assertTrue(pcscfRestorationRequest);
        // adjacentAccessRestrictionDataList
        ArrayList<AdjacentAccessRestrictionData> adjacentAccessRestrictionDataList = subscriptionData.getAdjacentAccessRestrictionDataList();
        assertTrue(adjacentAccessRestrictionDataList.get(0).getExtAccessRestrictionData().getNrAsSecondaryRATNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getExtAccessRestrictionData().getUnlicensedSpectrumAsSecondaryRATNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getGeranNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getUtranNotAllowed());
        assertTrue(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getGanNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getEUtranNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getIHspaEvolutionNotAllowed());
        assertTrue(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getHoToNon3GPPAccessNotAllowed());
        assertEquals(adjacentAccessRestrictionDataList.get(0).getPLMNId().getMcc(), 262);
        assertEquals(adjacentAccessRestrictionDataList.get(0).getPLMNId().getMnc(), 999);
        // imsiGroupIdList
        ArrayList<IMSIGroupId> imsiGroupIdList = subscriptionData.getIMSIGroupIdList();
        assertEquals(imsiGroupIdList.get(0).getGroupServiceId().longValue(), 1L);
        assertEquals(imsiGroupIdList.get(0).getLocalGroupId().getData(), "120".getBytes(StandardCharsets.UTF_8));
        // ueUsageType
        UEUsageType ueUsageType = subscriptionData.getUEUsageType();
        assertEquals(ueUsageType.getData(), new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator
        boolean userPlaneIntegrityProtectionIndicator = subscriptionData.getUserPlaneIntegrityProtectionIndicator();
        assertTrue(userPlaneIntegrityProtectionIndicator);
        // dlBufferingSuggestedPacketCount
        Long dlBufferingSuggestedPacketCount = subscriptionData.getDLBufferingSuggestedPacketCount();
        assertEquals(dlBufferingSuggestedPacketCount.longValue(), 0);
        // resetIdList
        resetIdList = subscriptionData.getResetIdList();
        assertNull(resetIdList);
        // eDRXCycleLengthList
        ArrayList<EDRXCycleLength> eDRXCycleLengthList = subscriptionData.getEDRXCycleLengthList();
        assertEquals(eDRXCycleLengthList.get(0).getUsedRATType(), UsedRATType.nbIoT);
        assertEquals(eDRXCycleLengthList.get(0).getEDRXCycleLengthValue().getData(), new byte[] { 0x02 });
        // iabOperationAllowedIndicator
        boolean iabOperationAllowedIndicator = subscriptionData.getIabOperationAllowedIndicator();
        assertTrue(iabOperationAllowedIndicator);
        /*
         * subscriptionDataDeletion
         */
        assertNull(subscriptionDataDeletion);

        // test 5, 3GPP Release v18.0.0 with resetIdList and subscriptionData
        rawData = getEncodedDataRel18wSubsDataToSgsn();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 1 item
         *             Reset-Id: 81
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: allDataTeleservices (112)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: plmn-specificSS-6 (246)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: ccbs-B - completion of call to busy subscribers, destination side (68)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: 1c0648f8
         *                     0... .... = allOG-CallsBarred: False
         *                     .0.. .... = internationalOGCallsBarred: False
         *                     ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
         *                     ...1 .... = premiumRateInformationOGCallsBarred: True
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..0. = interzonalOGCallsBarred: False
         *                     .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
         *                     0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
         *                     .0.. .... = allECT-Barred: False
         *                     ..0. .... = chargeableECT-Barred: False
         *                     ...0 .... = internationalECT-Barred: False
         *                     .... 0... = interzonalECT-Barred: False
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...0 = allPacketOrientedServicesBarred: False
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .1.. .... = roamerAccessToVPLMN-AP-Barred: True
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...0 .... = allIC-CallsBarred: False
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 50
         *                     0... .... = plmn-SpecificBarringType1: False
         *                     .1.. .... = plmn-SpecificBarringType2: True
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...1 .... = plmn-SpecificBarringType4: True
         *             roamingRestrictionDueToUnsupportedFeature
         *             gprsSubscriptionData
         *                 completeDataListIncluded
         *                 gprsDataList: 1 item
         *                     PDP-Context
         *                         pdp-ContextId: 1
         *                         pdp-Type: f121
         *                         .... 0001 = PDP Type Organization: IETF (0x1)
         *                         pdp-Address: 15
         *                         qos-Subscribed: 273205
         *                         00.. .... = Spare bit(s): 0
         *                         ..10 0... = Quality of Service Delay class: Delay class 4 (best effort) (4)
         *                         .... .111 = Reliability class: Reserved (7)
         *                         0011 .... = Peak throughput: Up to 4 000 octet/s (3)
         *                         .... 0... = Spare bit(s): 0
         *                         .... .010 = Precedence class: Normal priority (2)
         *                         000. .... = Spare bit(s): 0
         *                         ...0 0101 = Mean throughput: 2 000 octet/h (5)
         *                         vplmnAddressAllowed
         *                         apn: 08696e7465726e6574 - internet
         *                             APN: internet
         *                         ext-QoS-Subscribed: 097297804000a34000
         *                             0000 1001 = Allocation/Retention priority: 9
         *                             011. .... = Traffic class: Interactive class (3)
         *                             ...1 0... = Delivery order: Streaming class (2)
         *                             .... ..10 = Delivery of erroneous SDUs: Erroneous SDUs are delivered('yes') (2)
         *                             Maximum SDU size: 0x97 not defined in TS 24.008
         *                             Maximum bit rate for uplink in kbit/s: 576
         *                             Maximum bit rate for downlink in kbit/s: 64
         *                             0000 .... = Residual Bit Error Rate (BER): Subscribed residual BER/Reserved (0)
         *                             .... 0000 = SDU error ratio: Subscribed SDU error ratio/Reserved (0)
         *                             1010 00.. = Transfer delay (Raw data see TS 24.008 for interpretation): 40
         *                             .... ..11 = Traffic handling priority: Priority level 3 (3)
         *                             Guaranteed bit rate for uplink in kbit/s: 64
         *                             Guaranteed bit rate for downlink in kbit/s: Subscribed guaranteed bit rate for downlink/reserved
         *                         .... 1000 .... .... = pdp-ChargingCharacteristics: N (Normal billing) (8)
         *                         ext2-QoS-Subscribed: 100000
         *                             000. .... = Spare bit(s): 0
         *                             ...1 .... = Signalling indication: Optimised for signalling traffic
         *                             .... 0000 = Source statistics description: unknown (0)
         *                             Maximum bitrate for downlink (extended): Use the value indicated by the Maximum bit rate for downlink (0)
         *                             Guaranteed bitrate for downlink (extended): Use the value indicated by the Guaranteed bit rate for downlink (0)
         *                         ext3-QoS-Subscribed: 0000
         *                             Maximum bitrate for uplink (extended): Use the value indicated by the Maximum bit rate for uplink (0)
         *                             Guaranteed bitrate for uplink (extended): Use the value indicated by the Guaranteed bit rate for uplink (0)
         *                         ext4-QoS-Subscribed: 5b
         *                             .... ...1 = PVI Pre-emption Vulnerability: Disabled
         *                             ..01 10.. = PL Priority Level: 6
         *                             .1.. .... = PCI Pre-emption Capability: Disabled
         *                         apn-oi-Replacement: 515c53545556575859
         *                         ext-pdp-Type: 3a3b
         *                         ext-pdp-Address: 3c
         *                         sipto-Permission: siptoAboveRanAllowed (0)
         *                         lipa-Permission: lipaConditional (2)
         *                 apn-oi-Replacement: 515c53545556575859
         *             networkAccessMode: packetAndCircuit (0)
         *             istAlertTimer: 200
         *             superChargerSupportedInHLR: 07
         *             sgsn-CAMEL-SubscriptionInfo
         *                 gprs-CSI
         *                     gprs-CamelTDPDataList: 1 item
         *                         GPRS-CamelTDPData
         *                             gprs-TriggerDetectionPoint: attach (1)
         *                             serviceKey: 12
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSessionHandling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *                 mg-csi
         *                     mobilityTriggers: 1 item
         *                         MM-Code: 83
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ue-ReachabilityRequestIndicator
         *             sgsn-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             psAndSMS-OnlyServiceProvision
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: geran (1)
         *                     eDRX-Cycle-Length-Value: 02
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        /*
         * sendingNodenumber
         */
        assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(hlrNumber.getAddress(), "491710490000");
        /*
         * hlrList
         */
        assertNull(hlrList);
        /*
         * extensionContainer
         */
        assertNull(extensionContainer);
        /*
         * resetIdList
         */
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 1);
        assertEquals(resetIdList.get(0).getData(), new byte[] { (byte) 0x81});
        /*
         * subscriptionData
         */
        assertNotNull(subscriptionData);
        // imsi
        imsi = subscriptionData.getImsi();
        assertEquals(imsi.getData(), "748026800000000");
        // msisdn
        msisdn = subscriptionData.getMsisdn();
        assertNull(msisdn);
        // category
        category = subscriptionData.getCategory();
        assertEquals(category.getCategoryValue(), CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus
        subscriberStatus = subscriptionData.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.serviceGranted);
        // bearerServiceList
        bearerServiceList = subscriptionData.getBearerServiceList();
        assertNull(bearerServiceList);
        // teleserviceList
        teleserviceList = subscriptionData.getTeleserviceList();
        assertEquals(teleserviceList.size(), 2);
        assertEquals(teleserviceList.get(0).getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
        assertEquals(teleserviceList.get(1).getTeleserviceCodeValue(), TeleserviceCodeValue.allDataTeleservices);
        // provisionedSS
        provisionedSS = subscriptionData.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 2);
        extSSInfo = provisionedSS.get(0);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.plmn_specificSS_6);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getOverrideCategory(),
                OverrideCategory.overrideDisabled);
        extSSInfo = provisionedSS.get(1);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.ccbs_B);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getCliRestrictionOption(),
                CliRestrictionOption.temporaryDefaultAllowed);
        // odbData
        odbData = subscriptionData.getODBData();
        oDBGeneralData = odbData.getODBGeneralData();
        assertFalse(oDBGeneralData.getAllOGCallsBarred());
        assertFalse(oDBGeneralData.getInternationalOGCallsBarred());
        assertFalse(oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainmentOGCallsBarred());
        assertTrue(oDBGeneralData.getSsAccessBarred());
        assertFalse(oDBGeneralData.getInterzonalOGCallsBarred());
        assertFalse(oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertFalse(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(oDBGeneralData.getAllECTBarred());
        assertFalse(oDBGeneralData.getChargeableECTBarred());
        assertFalse(oDBGeneralData.getInternationalECTBarred());
        assertFalse(oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(oDBGeneralData.getMultipleECTBarred());
        assertFalse(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertFalse(oDBGeneralData.getAllICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInternationalCFBarred());
        odbHplmnData = odbData.getOdbHplmnData();
        assertFalse(odbHplmnData.getPlmnSpecificBarringType1());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType2());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType3());
        assertTrue(odbHplmnData.getPlmnSpecificBarringType4());
        assertNull(odbData.getExtensionContainer());
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = subscriptionData.getRoamingRestrictionDueToUnsupportedFeature();
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        // regionalSubscriptionData
        regionalSubscriptionData = subscriptionData.getRegionalSubscriptionData();
        assertNull(regionalSubscriptionData);
        // vbsSubscriptionData
        vbsSubscriptionData = subscriptionData.getVbsSubscriptionData();
        assertNull(vbsSubscriptionData);
        // vgcsSubscriptionData
        vgcsSubscriptionData = subscriptionData.getVgcsSubscriptionData();
        assertNull(vgcsSubscriptionData);
        // vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = subscriptionData.getVlrCamelSubscriptionInfo();
        assertNull(vlrCamelSubscriptionInfo);
        // naeaPreferredCI
        naeaPreferredCI = subscriptionData.getNAEAPreferredCI();
        assertNull(naeaPreferredCI);
        // gprsSubscriptionData
        gprsSubscriptionData = subscriptionData.getGPRSSubscriptionData();
        assertNotNull(gprsSubscriptionData);
        // roamingRestrictedInSgsnDueToUnsupportedFeature
        roamingRestrictedInSgsnDueToUnsupportedFeature = subscriptionData.getRoamingRestrictedInSgsnDueToUnsupportedFeature();
        assertFalse(roamingRestrictedInSgsnDueToUnsupportedFeature);
        // networkAccessMode
        networkAccessMode = subscriptionData.getNetworkAccessMode();
        assertEquals(networkAccessMode, NetworkAccessMode.packetAndCircuit);
        // lsaInformation
        lsaInformation = subscriptionData.getLSAInformation();
        assertNull(lsaInformation);
        // lmuIndicator
        lmuIndicator = subscriptionData.getLmuIndicator();
        assertFalse(lmuIndicator);
        // lcsInformation
        lcsInformation = subscriptionData.getLCSInformation();
        assertNull(lcsInformation);
        // istAlertTimer
        istAlertTimer = subscriptionData.getIstAlertTimer();
        assertEquals(istAlertTimer.intValue(), 200);
        // superChargerSupportedInHLR
        superChargerSupportedInHLR = subscriptionData.getSuperChargerSupportedInHLR();
        assertEquals(superChargerSupportedInHLR, new AgeIndicatorImpl(new byte[] {7}));
        // mcSsInfo
        mcSsInfo = subscriptionData.getMcSsInfo();
        assertNull(mcSsInfo);
        // csAllocationRetentionPriority
        csAllocationRetentionPriority = subscriptionData.getCSAllocationRetentionPriority();
        assertNull(csAllocationRetentionPriority);
        // sgsnCamelSubscriptionInfo
        sgsnCamelSubscriptionInfo = subscriptionData.getSgsnCamelSubscriptionInfo();
        assertNotNull(sgsnCamelSubscriptionInfo);
        // chargingCharacteristics
        chargingCharacteristics = subscriptionData.getChargingCharacteristics();
        assertFalse(chargingCharacteristics.isNormalCharging());
        assertFalse(chargingCharacteristics.isPrepaidCharging());
        assertTrue(chargingCharacteristics.isFlatRateChargingCharging());
        assertFalse(chargingCharacteristics.isChargingByHotBillingCharging());
        // accessRestrictionData
        accessRestrictionData = subscriptionData.getAccessRestrictionData();
        assertFalse(accessRestrictionData.getUtranNotAllowed());
        assertFalse(accessRestrictionData.getGeranNotAllowed());
        assertTrue(accessRestrictionData.getGanNotAllowed());
        assertFalse(accessRestrictionData.getEUtranNotAllowed());
        assertFalse(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // icsIndicator
        icsIndicator = subscriptionData.getIcsIndicator();
        assertNull(icsIndicator);
        // epsSubscriptionData
        epsSubscriptionData = subscriptionData.getEpsSubscriptionData();
        assertNull(epsSubscriptionData);
        // csgSubscriptionDataList
        csgSubscriptionDataList = subscriptionData.getCsgSubscriptionDataList();
        assertNull(csgSubscriptionDataList);
        // ueReachabilityRequestIndicator
        ueReachabilityRequestIndicator = subscriptionData.getUeReachabilityRequestIndicator();
        assertTrue(ueReachabilityRequestIndicator);
        // sgsnNumber
        sgsnNumber = subscriptionData.getSgsnNumber();
        assertNotNull(sgsnNumber);
        // mmeName
        mmeName = subscriptionData.getMmeName();
        assertNull(mmeName);
        // subscribedPeriodicRAUTAUtimer
        subscribedPeriodicRAUTAUtimer = subscriptionData.getSubscribedPeriodicRAUTAUtimer();
        assertEquals(subscribedPeriodicRAUTAUtimer.longValue(), 300);
        // vplmnLIPAAllowed
        vplmnLIPAAllowed = subscriptionData.getVplmnLIPAAllowed();
        assertTrue(vplmnLIPAAllowed);
        // mdtUserConsent
        mdtUserConsent = subscriptionData.getMdtUserConsent();
        assertFalse(mdtUserConsent);
        // subscribedPeriodicLAUtimer
        subscribedPeriodicLAUtimer = subscriptionData.getSubscribedPeriodicLAUtimer();
        assertNull(subscribedPeriodicLAUtimer);
        // vplmnCSGSubscriptionDataList
        vplmnCSGSubscriptionDataList = subscriptionData.getVPLMNCSGSubscriptionDataList();
        assertNull(vplmnCSGSubscriptionDataList);
        // additionalMSISDN
        additionalMSISDN = subscriptionData.getAdditionalMSISDN();
        assertNull(additionalMSISDN);
        // psAndSMSOnlyServiceProvision
        psAndSMSOnlyServiceProvision = subscriptionData.getPSandSMSOnlyServiceProvision();
        assertTrue(psAndSMSOnlyServiceProvision);
        // smsInSGSNAllowed
        smsInSGSNAllowed = subscriptionData.getSMSInSGSNAllowed();
        assertTrue(smsInSGSNAllowed);
        // csToPsSRVCCAllowedIndicator
        csToPsSRVCCAllowedIndicator = subscriptionData.getCsToPsSRVCCAllowedIndicator();
        assertTrue(csToPsSRVCCAllowedIndicator);
        // pcscfRestorationRequest
        pcscfRestorationRequest = subscriptionData.getPCSCFRestorationRequest();
        assertTrue(pcscfRestorationRequest);
        // adjacentAccessRestrictionDataList
        adjacentAccessRestrictionDataList = subscriptionData.getAdjacentAccessRestrictionDataList();
        assertNull(adjacentAccessRestrictionDataList);
        // imsiGroupIdList
        imsiGroupIdList = subscriptionData.getIMSIGroupIdList();
        assertNull(imsiGroupIdList);
        // ueUsageType
        ueUsageType = subscriptionData.getUEUsageType();
        assertEquals(ueUsageType.getData(), new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator
        userPlaneIntegrityProtectionIndicator = subscriptionData.getUserPlaneIntegrityProtectionIndicator();
        assertTrue(userPlaneIntegrityProtectionIndicator);
        // dlBufferingSuggestedPacketCount
        dlBufferingSuggestedPacketCount = subscriptionData.getDLBufferingSuggestedPacketCount();
        assertEquals(dlBufferingSuggestedPacketCount.longValue(), 0);
        // resetIdList
        resetIdList = subscriptionData.getResetIdList();
        assertNull(resetIdList);
        // eDRXCycleLengthList
        eDRXCycleLengthList = subscriptionData.getEDRXCycleLengthList();
        assertEquals(eDRXCycleLengthList.get(0).getUsedRATType(), UsedRATType.geran);
        assertEquals(eDRXCycleLengthList.get(0).getEDRXCycleLengthValue().getData(), new byte[] { 0x02 });
        // iabOperationAllowedIndicator
        iabOperationAllowedIndicator = subscriptionData.getIabOperationAllowedIndicator();
        assertFalse(iabOperationAllowedIndicator);
        /*
         * subscriptionDataDeletion
         */
        assertNull(subscriptionDataDeletion);

        // test 6, 3GPP Release v18.0.0 with resetIdList and subscriptionDataDeletion (sent to VLR)
        rawData = getEncodedDataRel18wSubsDataDelToVLR();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: css-Number (1)
         *             css-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 82
         *             Reset-Id: 01020482
         *         subscriptionDataDeletion
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             basicServiceList: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: padAccessCA-1200-75bps (35)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: allTeleservices (0)
         *             ss-List: 1 item
         *                 SS-Code: plmn-specificSS-2 (242)
         *             regionalSubscriptionIdentifier: 210f
         *             vbsGroupIndication
         *             vgcsGroupIndication
         *             camelSubscriptionInfoWithdraw
         *             roamingRestrictedInSgsnDueToUnsuppportedFeature
         *             lsaInformationWithdraw: lsaIdentityList (1)
         *                 lsaIdentityList: 2 items
         *                     LSAIdentity: 0c0a01
         *                     LSAIdentity: 0c0c02
         *             gmlc-ListWithdraw
         *             istInformationWithdraw
         *             Padding: 2
         *             specificCSI-Withdraw: 9000
         *                 1... .... = o-csi: True
         *                 .0.. .... = ss-csi: False
         *                 ..0. .... = tif-csi: False
         *                 ...1 .... = d-csi: True
         *                 .... 0... = vt-csi: False
         *                 .... .0.. = mo-sms-csi: False
         *                 .... ..0. = m-csi: False
         *                 .... ...0 = gprs-csi: False
         *                 0... .... = t-csi: False
         *                 .0.. .... = mt-sms-csi: False
         *                 ..0. .... = mg-csi: False
         *                 ...0 .... = o-IM-CSI: False
         *                 .... 0... = d-IM-CSI: False
         *                 .... .0.. = vt-IM-CSI: False
         *             chargingCharacteristicsWithdraw
         *             stn-srWithdraw
         *             apn-oi-replacementWithdraw
         *             csg-SubscriptionDeleted
         *             subscribedPeriodicTAU-RAU-TimerWithdraw
         *             subscribedPeriodicLAU-TimerWithdraw
         *             subscribed-vsrvccWithdraw
         *             vplmn-Csg-SubscriptionDeleted
         *             additionalMSISDN-Withdraw
         *             cs-to-ps-SRVCC-Withdraw
         *             imsiGroupIdList-Withdraw
         *             userPlaneIntegrityProtectionWithdraw
         *             dl-Buffering-Suggested-Packet-Count-Withdraw
         *             ue-UsageTypeWithdraw
         *             reset-idsWithdraw
         *             iab-OperationWithdraw
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        assertNull(hlrNumber);
        /*
         * sendingNodenumber
         */
        SendingNodeNumber sendingNodeNumber = prim.getSendingNodenumber();
        ISDNAddressString cssNumber = sendingNodeNumber.getCssNumber();
        assertEquals(cssNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(cssNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(cssNumber.getAddress(), "491710490000");
        /*
         * hlrList
         */
        assertNull(hlrList);
        /*
         * extensionContainer
         */
        assertNull(extensionContainer);
        /*
         * resetIdList
         */
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 2);
        assertEquals(resetIdList.get(0).getData(), new byte[] {(byte) 0x82});
        assertEquals(resetIdList.get(1).getData(), new byte[] {0x01, 0x02, 0x04, (byte) 0x82});
        /*
         * subscriptionData
         */
        assertNull(subscriptionData);
        /*
         * subscriptionDataDeletion
         */
        assertNotNull(subscriptionDataDeletion);
        // imsi
        imsi = subscriptionDataDeletion.getImsi();
        assertEquals(imsi.getData(), "748026800000000");
        // basicServiceList
        basicServiceList = subscriptionDataDeletion.getBasicServiceList();
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_1200_75bps);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        // ssList
        ArrayList<SSCode> ssList = subscriptionDataDeletion.getSsList();
        assertEquals(ssList.size(), 1);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.plmn_specificSS_2);
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = subscriptionDataDeletion.getRoamingRestrictionDueToUnsupportedFeature();
        assertFalse(roamingRestrictionDueToUnsupportedFeature);
        // regionalSubscriptionIdentifier
        ZoneCode regionalSubscriptionIdentifier = subscriptionDataDeletion.getRegionalSubscriptionIdentifier();
        assertEquals(regionalSubscriptionIdentifier.getData(), new byte[] {0x21, 0x0F});
        // vbsGroupIndication
        boolean vbsGroupIndication = subscriptionDataDeletion.getVbsGroupIndication();
        assertTrue(vbsGroupIndication);
        // vgcsGroupIndication
        boolean vgcsGroupIndication = subscriptionDataDeletion.getVgcsGroupIndication();
        assertTrue(vgcsGroupIndication);
        // camelSubscriptionInfoWithdraw
        boolean camelSubscriptionInfoWithdraw = subscriptionDataDeletion.getCamelSubscriptionInfoWithdraw();
        assertTrue(camelSubscriptionInfoWithdraw);
        // extensionContainer
        extensionContainer = subscriptionDataDeletion.getExtensionContainer();
        assertNull(extensionContainer);
        // gprsSubscriptionDataWithdraw
        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = subscriptionDataDeletion.getGPRSSubscriptionDataWithdraw();
        assertNull(gprsSubscriptionDataWithdraw);
        // roamingRestrictedInSgsnDueToUnsuppportedFeature
        boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = subscriptionDataDeletion.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        // lsaInformationWithdraw
        LSAInformationWithdraw lsaInformationWithdraw = subscriptionDataDeletion.getLSAInformationWithdraw();
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().size(), 2);
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().get(0).getData(), new byte[]{12, 10, 1});
        assertEquals(lsaInformationWithdraw.getLSAIdentityList().get(1).getData(), new byte[]{12, 12, 2});
        // gmlcListWithdraw
        boolean gmlcListWithdraw = subscriptionDataDeletion.getGmlcListWithdraw();
        assertTrue(gmlcListWithdraw);
        // istInformationWithdraw
        boolean istInformationWithdraw = subscriptionDataDeletion.getIstInformationWithdraw();
        assertTrue(istInformationWithdraw);
        // specificCSIWithdraw
        SpecificCSIWithdraw specificCSIWithdraw = subscriptionDataDeletion.getSpecificCSIWithdraw();
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());
        assertFalse(specificCSIWithdraw.getMoSmsCsi());
        assertFalse(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());
        // chargingCharacteristicsWithdraw
        boolean chargingCharacteristicsWithdraw = subscriptionDataDeletion.getChargingCharacteristicsWithdraw();
        assertTrue(chargingCharacteristicsWithdraw);
        // stnSrWithdraw
        boolean stnSrWithdraw = subscriptionDataDeletion.getStnSrWithdraw();
        assertTrue(stnSrWithdraw);
        // epsSubscriptionDataWithdraw
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = subscriptionDataDeletion.getEPSSubscriptionDataWithdraw();
        assertNull(epsSubscriptionDataWithdraw);
        // apnOiReplacementWithdraw
        boolean apnOiReplacementWithdraw = subscriptionDataDeletion.getApnOiReplacementWithdraw();
        assertTrue(apnOiReplacementWithdraw);
        // csgSubscriptionDeleted
        boolean csgSubscriptionDeleted = subscriptionDataDeletion.getCsgSubscriptionDeleted();
        assertTrue(csgSubscriptionDeleted);
        // subscribedPeriodicTAURAUTimerWithdraw
        boolean subscribedPeriodicTAURAUTimerWithdraw = subscriptionDataDeletion.getSubscribedPeriodicTAURAUTimerWithdraw();
        assertTrue(subscribedPeriodicTAURAUTimerWithdraw);
        // subscribedPeriodicLAUTimerWithdraw
        boolean subscribedPeriodicLAUTimerWithdraw = subscriptionDataDeletion.getSubscribedPeriodicLAUTimerWithdraw();
        assertTrue(subscribedPeriodicLAUTimerWithdraw);
        // subscribedVsrvccWithdraw
        boolean subscribedVsrvccWithdraw = subscriptionDataDeletion.getSubscribedVsrvccWithdraw();
        assertTrue(subscribedVsrvccWithdraw);
        // vplmnCsgSubscriptionDeleted
        boolean vplmnCsgSubscriptionDeleted = subscriptionDataDeletion.getVplmnCsgSubscriptionDeleted();
        assertTrue(vplmnCsgSubscriptionDeleted);
        // additionalMSISDNWithdraw
        boolean additionalMSISDNWithdraw = subscriptionDataDeletion.getGmlcListWithdraw();
        assertTrue(additionalMSISDNWithdraw);
        // csToPsSRVCCWithdraw
        boolean csToPsSRVCCWithdraw = subscriptionDataDeletion.getCsToPsSRVCCWithdraw();
        assertTrue(csToPsSRVCCWithdraw);
        // imsiGroupIdListWithdraw
        boolean imsiGroupIdListWithdraw = subscriptionDataDeletion.getImsiGroupIdListWithdraw();
        assertTrue(imsiGroupIdListWithdraw);
        // userPlaneIntegrityProtectionWithdraw
        boolean userPlaneIntegrityProtectionWithdraw = subscriptionDataDeletion.getUserPlaneIntegrityProtectionWithdraw();
        assertTrue(userPlaneIntegrityProtectionWithdraw);
        // dlBufferingSuggestedPacketCountWithdraw
        boolean dlBufferingSuggestedPacketCountWithdraw = subscriptionDataDeletion.getDlBufferingSuggestedPacketCountWithdraw();
        assertTrue(dlBufferingSuggestedPacketCountWithdraw);
        // ueUsageTypeWithdraw
        boolean ueUsageTypeWithdraw = subscriptionDataDeletion.getUeUsageTypeWithdraw();
        assertTrue(ueUsageTypeWithdraw);
        // resetIdsWithdraw
        boolean resetIdsWithdraw = subscriptionDataDeletion.getResetIdsWithdraw();
        assertTrue(resetIdsWithdraw);
        // iabOperationWithdraw
        boolean iabOperationWithdraw = subscriptionDataDeletion.getIabOperationWithdraw();
        assertTrue(iabOperationWithdraw);

        // test 7, 3GPP Release v18.0.0 with resetIdList and subscriptionData (containing msisdn, LCSInformation and resetIdList)
        rawData = getEncodedDataRel18wSubsDataToVLR2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        hlrNumber = prim.getHlrNumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940050
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490005
         *         reset-Id-List: 2 items
         *             Reset-Id: 81
         *             Reset-Id: 01020482
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             msisdn: 919598097739f7
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 59899077937
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: shortMessageMO-PP (34)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: cfnry - call forwarding on no reply (42)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: hold - call hold (66)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: efff1ce8
         *                     1... .... = allOG-CallsBarred: True
         *                     .1.. .... = internationalOGCallsBarred: True
         *                     ..1. .... = internationalOGCallsNotToHPLMN-CountryBarred: True
         *                     ...0 .... = premiumRateInformationOGCallsBarred: False
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..1. = interzonalOGCallsBarred: True
         *                     .... ...1 = interzonalOGCallsNotToHPLMN-CountryBarred: True
         *                     1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
         *                     .1.. .... = allECT-Barred: True
         *                     ..1. .... = chargeableECT-Barred: True
         *                     ...1 .... = internationalECT-Barred: True
         *                     .... 1... = interzonalECT-Barred: True
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...1 = allPacketOrientedServicesBarred: True
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .0.. .... = roamerAccessToVPLMN-AP-Barred: False
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...1 .... = allIC-CallsBarred: True
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...0 .... = registrationInterzonalCFNotToHPLMN-Barred: False
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 80
         *                     1... .... = plmn-SpecificBarringType1: True
         *                     .0.. .... = plmn-SpecificBarringType2: False
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...0 .... = plmn-SpecificBarringType4: False
         *             roamingRestrictionDueToUnsupportedFeature
         *             vbsSubscriptionData: 1 item
         *                 VoiceBroadcastData
         *                     groupid: ffffff
         *                         TBCD digits:
         *                     broadcastInitEntitlement
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vgcsSubscriptionData: 1 item
         *                 VoiceGroupCallData
         *                     groupId: ffffff
         *                         TBCD digits:
         *                     Padding: 5
         *                     additionalSubscriptions: e0
         *                         1... .... = privilegedUplinkRequest: True
         *                         .1.. .... = emergencyUplinkRequest: True
         *                         ..1. .... = emergencyReset: True
         *                     Padding: 0
         *                     additionalInfo: c000008000000000000000000000000000
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vlrCamelSubscriptionInfo
         *                 o-CSI
         *                     o-BcsmCamelTDPDataList: 1 item
         *                         O-BcsmCamelTDPData
         *                             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csiActive
         *                 ss-CSI
         *                     ss-CamelData
         *                         ss-EventList: 2 items
         *                             SS-Code: cfnry - call forwarding on no reply (42)
         *                             SS-Code: hold - call hold (66)
         *                         gsmSCF-Address: 91947101640092
         *                             1... .... = Extension: No Extension
         *                             .001 .... = Nature of number: International Number (0x1)
         *                             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                             E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 o-BcsmCamelTDP-CriteriaList: 1 item
         *                     O-BcsmCamelTDP-Criteria
         *                         o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                         destinationNumberCriteria
         *                             matchType: enabling (1)
         *                             destinationNumberList: 1 item
         *                                 ISDN-AddressString: 91947141874023
         *                                     1... .... = Extension: No Extension
         *                                     .001 .... = Nature of number: International Number (0x1)
         *                                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                     E.164 number (MSISDN): 491714780432
         *                             destinationNumberLengthList: 1 item
         *                                 DestinationNumberLengthList item: 1
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                         callTypeCriteria: notForwarded (1)
         *                         o-CauseValueCriteria: 2 items
         *                             CauseValue: 51
         *                             CauseValue: 39
         *                 tif-CSI
         *                 m-CSI
         *                     mobilityTriggers: 2 items
         *                         MM-Code: 02
         *                         MM-Code: 00
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 vt-CSI
         *                     t-BcsmCamelTDPDataList: 2 items
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tBusy (13)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *                     T-BCSM-CAMEL-TDP-Criteria
         *                         t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                         t-CauseValueCriteria: 2 items
         *                             CauseValue: 15
         *                             CauseValue: 39
         *                 d-CSI
         *                     dp-AnalysedInfoCriteriaList: 1 item
         *                         DP-AnalysedInfoCriterium
         *                             dialledNumber: 91947141874023
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491714780432
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *             naea-PreferredCI
         *                 naea-PreferredCIC: 235408
         *             networkAccessMode: packetAndCircuit (0)
         *             lcsInformation
         *                 gmlc-List: 1 item
         *                     ISDN-AddressString: 91947101640023f1
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 4917104600321
         *                 lcs-PrivacyExceptionList: 4 items
         *                     LCS-PrivacyClass
         *                         ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
         *                         ss-Status: 08
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: notifyLocationAllowed (0)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         plmnClientList: 2 items
         *                             LCSClientInternalID: broadcastService (0)
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: emergencyAlertServices (1)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                     LCS-PrivacyClass
         *                         ss-Code: autonomousSelfLocation - allow an MS to perform self location without interaction with the PLMN for a predetermined period of time (194)
         *                         ss-Status: 04
         *                         0000 .... = Unused: 0x0
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f93289722f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 398279222
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f93289722f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 398279222
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: personTracking (2)
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                     LCS-PrivacyClass
         *                         ss-Code: allLCSPrivacyException - all LCS Privacy Exception Classes (176)
         *                         ss-Status: 02
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..1. = R bit: Registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: locationNotAllowed (3)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f3245232532f4
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 23543252234
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: targetMSsubscribedService (4)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f3245232532f4
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 23543252234
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: fleetManagement (3)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                     LCS-PrivacyClass
         *                         ss-Code: allPLMN-specificSS (240)
         *                         ss-Status: 01
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f959809922854
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 598990298245
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: anonymousLocation (3)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f959809922854
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 598990298245
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: assetManagement (4)
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                 molr-List: 3 items
         *                     MOLR-Class
         *                         ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
         *                         ss-Status: 08
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                     MOLR-Class
         *                         ss-Code: autonomousSelfLocation - allow an MS to perform self location without interaction with the PLMN for a predetermined period of time (194)
         *                         ss-Status: 04
         *                         0000 .... = Unused: 0x0
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                     MOLR-Class
         *                         ss-Code: allLCSPrivacyException - all LCS Privacy Exception Classes (176)
         *                         ss-Status: 02
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..1. = R bit: Registered
         *                         .... ...0 = A bit: not Active
         *                 add-lcs-PrivacyExceptionList: 1 item
         *                     LCS-PrivacyClass
         *                         ss-Code: plmn-specificSS-1 (241)
         *                         ss-Status: 0d
         *                         0000 .... = Unused: 0x0
         *                         .... 1... = Q bit: Quiescent
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         notificationToMSUser: notifyLocationAllowed (0)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         plmnClientList: 4 items
         *                             LCSClientInternalID: broadcastService (0)
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                             LCSClientInternalID: targetMSsubscribedService (4)
         *                             LCSClientInternalID: anonymousLocation (3)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: trafficCongestionReporting (5)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *             istAlertTimer: 200
         *             mc-SS-Info
         *                 ss-Code: cfu - call forwarding unconditional (33)
         *                 ss-Status: 0a
         *                 0000 .... = Unused: 0x0
         *                 .... .0.. = P bit: Not provisioned
         *                 .... ..1. = R bit: Registered
         *                 .... ...0 = A bit: not Active
         *                 nbrSB: 2
         *                 nbrUser: 4
         *             cs-AllocationRetentionPriority: 04
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ics-Indicator: True
         *             eps-SubscriptionData
         *                 apn-oi-Replacement: 515c53545556575859
         *                 rfsp-id: 0
         *                 ambr
         *                     max-RequestedBandwidth-UL: 2048
         *                     max-RequestedBandwidth-DL: 4096
         *                 apn-ConfigurationProfile
         *                     defaultContext: 1
         *                     completeDataListIncluded
         *                     epsDataList: 1 item
         *                         APN-Configuration
         *                             contextId: 1
         *                             pdn-Type: 03
         *                             apn: 08696e7465726e6574 - internet
         *                                 APN: internet
         *                             eps-qos-Subscribed
         *                                 qos-Class-Identifier: 5
         *                                 allocation-Retention-Priority
         *                                     priority-level: 9
         *                                     pre-emption-capability: True
         *                                     pre-emption-vulnerability: False
         *                             vplmnAddressAllowed
         *                             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *                             ambr
         *                                 max-RequestedBandwidth-UL: 2048
         *                                 max-RequestedBandwidth-DL: 4096
         *                             servedPartyIP-IPv6-Address: 15
         *                             apn-oi-Replacement: 515c53545556575859
         *                             sipto-Permission: siptoAboveRanAllowed (0)
         *                             lipa-Permission: lipaConditional (2)
         *                 stn-sr: 91947101940000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710490000
         *                 mps-CSPriority
         *                 mps-EPSPriority
         *             csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             ue-ReachabilityRequestIndicator
         *             mme-Name: mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             subscribedPeriodicLAUtimer: 360
         *             vplmn-Csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             additionalMSISDN: 91947101652854f1
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 4917105682451
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             adjacentAccessRestrictionDataList: 1 item
         *                 AdjacentAccessRestrictionData
         *                     plmnId: 629299
         *                     Padding: 2
         *                     accessRestrictionData: 24
         *                         0... .... = utranNotAllowed: False
         *                         .0.. .... = geranNotAllowed: False
         *                         ..1. .... = ganNotAllowed: True
         *                         ...0 .... = i-hspa-evolutionNotAllowed: False
         *                         .... 0... = wb-e-utranNotAllowed: False
         *                         .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                         .... ..0. = nb-iotNotAllowed: False
         *                         .... ...0 = enhancedCoverageNotAllowed: False
         *                     Padding: 0
         *                     ext-AccessRestrictionData: 80000000
         *                         1... .... = nrAsSecondaryRATNotAllowed: True
         *                         .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             imsi-Group-Id-List: 1 item
         *                 IMSI-GroupId
         *                     group-Service-Id: 1
         *                     plmnId: 629299
         *                     local-Group-ID: 313230
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             reset-Id-List: 2 items
         *                 Reset-Id: 81
         *                 Reset-Id: 01020482
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: nb-iot (5)
         *                     eDRX-Cycle-Length-Value: 02
         *             Padding: 0
         *             ext-AccessRestrictionData: 80000000
         *                 1... .... = nrAsSecondaryRATNotAllowed: True
         *                 .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             iab-Operation-Allowed-Indicator
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        /*
         * sendingNodenumber
         */
        assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(hlrNumber.getAddress(), "491710490005");
        /*
         * hlrList
         */
        assertNull(hlrList);
        /*
         * extensionContainer
         */
        assertNull(extensionContainer);
        /*
         * resetIdList
         */
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 2);
        assertEquals(resetIdList.get(0).getData(), new byte[] { (byte) 0x81});
        assertEquals(resetIdList.get(1).getData(), new byte[] { 0x01, 0x02, 0x04, (byte) 0x82});
        /*
         * subscriptionData
         */
        assertNotNull(subscriptionData);
        // imsi
        imsi = subscriptionData.getImsi();
        assertEquals(imsi.getData(), "748026800000000");
        // msisdn
        msisdn = subscriptionData.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "59899077937");
        // category
        category = subscriptionData.getCategory();
        assertEquals(category.getCategoryValue(), CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus
        subscriberStatus = subscriptionData.getSubscriberStatus();
        assertEquals(subscriberStatus, SubscriberStatus.serviceGranted);
        // bearerServiceList
        bearerServiceList = subscriptionData.getBearerServiceList();
        assertNull(bearerServiceList);
        // teleserviceList
        teleserviceList = subscriptionData.getTeleserviceList();
        assertEquals(teleserviceList.size(), 2);
        assertEquals(teleserviceList.get(0).getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
        assertEquals(teleserviceList.get(1).getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMO_PP);
        // provisionedSS
        provisionedSS = subscriptionData.getProvisionedSS();
        assertNotNull(provisionedSS);
        assertEquals(provisionedSS.size(), 2);
        extSSInfo = provisionedSS.get(0);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfnry);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getOverrideCategory(),
                OverrideCategory.overrideDisabled);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allBearerServices);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.facsimileGroup4);
        extSSInfo = provisionedSS.get(1);
        assertEquals(extSSInfo.getSsData().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.hold);
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitQ());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitP());
        assertFalse(extSSInfo.getSsData().getSsStatus().getBitR());
        assertTrue(extSSInfo.getSsData().getSsStatus().getBitA());
        assertEquals(extSSInfo.getSsData().getSSSubscriptionOption().getCliRestrictionOption(),
                CliRestrictionOption.temporaryDefaultAllowed);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allBearerServices);
        assertEquals(extSSInfo.getSsData().getBasicServiceGroupList().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.facsimileGroup4);
        // odbData
        odbData = subscriptionData.getODBData();
        oDBGeneralData = odbData.getODBGeneralData();
        assertTrue(oDBGeneralData.getAllOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsBarred());
        assertTrue(oDBGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertFalse(oDBGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(oDBGeneralData.getPremiumRateEntertainmentOGCallsBarred());
        assertTrue(oDBGeneralData.getSsAccessBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(oDBGeneralData.getAllECTBarred());
        assertTrue(oDBGeneralData.getChargeableECTBarred());
        assertTrue(oDBGeneralData.getInternationalECTBarred());
        assertTrue(oDBGeneralData.getInterzonalECTBarred());
        assertTrue(oDBGeneralData.getDoublyChargeableECTBarred());
        assertTrue(oDBGeneralData.getMultipleECTBarred());
        assertTrue(oDBGeneralData.getAllPacketOrientedServicesBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(oDBGeneralData.getAllICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(oDBGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNBarred());
        assertFalse(oDBGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(oDBGeneralData.getRegistrationAllCFBarred());
        assertTrue(oDBGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInterzonalCFBarred());
        assertFalse(oDBGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(oDBGeneralData.getRegistrationInternationalCFBarred());
        odbHplmnData = odbData.getOdbHplmnData();
        assertTrue(odbHplmnData.getPlmnSpecificBarringType1());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType2());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType3());
        assertFalse(odbHplmnData.getPlmnSpecificBarringType4());
        assertNull(odbData.getExtensionContainer());
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = subscriptionData.getRoamingRestrictionDueToUnsupportedFeature();
        assertTrue(roamingRestrictionDueToUnsupportedFeature);
        // regionalSubscriptionData
        regionalSubscriptionData = subscriptionData.getRegionalSubscriptionData();
        assertNull(regionalSubscriptionData);
        // vbsSubscriptionData
        vbsSubscriptionData = subscriptionData.getVbsSubscriptionData();
        assertEquals(vbsSubscriptionData.size(), 1);
        System.out.println(vbsSubscriptionData.get(0).getGroupId().getGroupId());
        assertEquals(vbsSubscriptionData.get(0).getGroupId().getGroupId(), "");
        assertTrue(vbsSubscriptionData.get(0).getBroadcastInitEntitlement());
        assertEquals(vbsSubscriptionData.get(0).getLongGroupId().getLongGroupId(), "5");
        assertNull(vbsSubscriptionData.get(0).getExtensionContainer());
        // vgcsSubscriptionData
        vgcsSubscriptionData = subscriptionData.getVgcsSubscriptionData();
        assertNotNull(vgcsSubscriptionData);
        assertEquals(vgcsSubscriptionData.size(), 1);
        voiceGroupCallData = vgcsSubscriptionData.get(0);
        assertEquals(voiceGroupCallData.getGroupId().getGroupId(), "");
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyReset());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getEmergencyUplinkRequest());
        assertTrue(voiceGroupCallData.getAdditionalSubscriptions().getPrivilegedUplinkRequest());
        assertEquals(voiceGroupCallData.getLongGroupId().getLongGroupId(), "5");
        assertNotNull(voiceGroupCallData.getAdditionalInfo());
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(0));
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(1));
        assertTrue(voiceGroupCallData.getAdditionalInfo().getData().get(24));
        assertNull(voiceGroupCallData.getExtensionContainer());
        // vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = subscriptionData.getVlrCamelSubscriptionInfo();
        // vlrCamelSubscriptionInfo o-CSI
        oCsi = vlrCamelSubscriptionInfo.getOCsi();
        oBcsmCamelTDPDataList = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(oBcsmCamelTDPDataList.size(), 1);
        oBcsmCamelTDPData = oBcsmCamelTDPDataList.get(0);
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(oBcsmCamelTDPData.getExtensionContainer());
        assertNull(oCsi.getExtensionContainer());
        assertEquals(oCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(oCsi.getNotificationToCSE());
        assertTrue(oCsi.getCsiActive());
        // vlrCamelSubscriptionInfo extensionContainer
        assertNull(vlrCamelSubscriptionInfo.getExtensionContainer());
        // vlrCamelSubscriptionInfo ssCsi
        ssCsi = vlrCamelSubscriptionInfo.getSsCsi();
        ssCamelData = ssCsi.getSsCamelData();
        ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 2);
        ssEvent1 = ssEventList.get(0);
        assertNotNull(ssEvent1);
        assertEquals(ssEvent1.getSupplementaryCodeValue(), SupplementaryCodeValue.cfnry);
        ssEvent2 = ssEventList.get(1);
        assertNotNull(ssEvent2);
        assertEquals(ssEvent2.getSupplementaryCodeValue(), SupplementaryCodeValue.hold);
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertEquals(gsmSCFAddress.getAddress(), "491710460029");
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(ssCamelData.getExtensionContainer());
        assertNull(ssCsi.getExtensionContainer());
        assertTrue(ssCsi.getCsiActive());
        assertTrue(ssCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo o-BcsmCamelTDP-CriteriaList
        oBcsmCamelTDPCriteriaList = vlrCamelSubscriptionInfo.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);
        destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 1);
        destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertEquals(destinationNumberOne.getAddress(), "491714780432");
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allBearerServices);
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.facsimileGroup4);
        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.notForwarded);
        oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 2);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 0x51);
        assertNotNull(oCauseValueCriteria.get(1));
        assertEquals(oCauseValueCriteria.get(1).getData(), 0x39);
        // vlrCamelSubscriptionInfo tif-CSI
        assertTrue(vlrCamelSubscriptionInfo.getTifCsi());
        // vlrCamelSubscriptionInfo m-CSI
        mCsi = vlrCamelSubscriptionInfo.getMCsi();
        mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.IMSIAttach);
        mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(mmCode2.getMMCodeValue(), MMCodeValue.LocationUpdateInSameVLR);
        assertNotNull(mCsi);
        assertEquals(mCsi.getServiceKey(), 7);
        gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertEquals(gsmSCFAddressTwo.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNull(mCsi.getExtensionContainer());
        assertTrue(mCsi.getCsiActive());
        assertTrue(mCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mo-sms-CSI
        smsCsi = vlrCamelSubscriptionInfo.getSmsCsi();
        smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 7);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPData.getExtensionContainer());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(smsCsi.getCsiActive());
        assertTrue(smsCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo vt-CSI
        vtCsi = vlrCamelSubscriptionInfo.getVtCsi();
        tBcsmCamelTDPDataList = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(tBcsmCamelTDPDataList.size(), 2);
        tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        tbcsmCamelTDPData = tBcsmCamelTDPDataList.get(1);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 7);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddress(), "491710460029");
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertEquals(vtCsi.getCamelCapabilityHandling().intValue(), 2);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertTrue(vtCsi.getNotificationToCSE());
        assertTrue(vtCsi.getCsiActive());
        // vlrCamelSubscriptionInfo t-BCSM-CAMEL-TDP-CriteriaList
        tBcsmCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tNoAnswer);
        basicServiceList = tbcsmCamelTdpCriteria.getBasicServiceCriteria();
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.allBearerServices);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(),
                TeleserviceCodeValue.facsimileGroup4);
        tCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(tCauseValueCriteriaLst);
        assertEquals(tCauseValueCriteriaLst.size(), 2);
        assertEquals(tCauseValueCriteriaLst.get(0).getData(), 0x15);
        assertEquals(tCauseValueCriteriaLst.get(1).getData(), 0x39);
        // vlrCamelSubscriptionInfo d-CSI
        dCsi = vlrCamelSubscriptionInfo.getDCsi();
        dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertEquals(dialledNumber.getAddress(), "491714780432");
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(gsmSCFAddressDp.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNull(dCsi.getExtensionContainer());
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mt-sms-CSI
        mtSmsCSI = vlrCamelSubscriptionInfo.getMtSmsCSI();
        smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 7);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddress(), "491710460029");
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertNull(mtSmsCSI.getExtensionContainer());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(mtSmsCSI.getNotificationToCSE());
        // vlrCamelSubscriptionInfo mt-smsCAMELTDP-CriteriaList
        mtSmsCamelTdpCriteriaList = vlrCamelSubscriptionInfo.getMtSmsCamelTdpCriteriaList();
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);
        tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 3);
        mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);
        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSUBMITREPORT);
        mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(2);
        assertSame(mtSMSTPDUTypeTwo, MTSMSTPDUType.smsSTATUSREPORT);
        // naeaPreferredCI
        naeaPreferredCI = subscriptionData.getNAEAPreferredCI();
        assertEquals(naeaPreferredCI.getNaeaPreferredCIC().getData(), new byte[] {0x23, 0x54, 0x08});
        // gprsSubscriptionData
        gprsSubscriptionData = subscriptionData.getGPRSSubscriptionData();
        assertNull(gprsSubscriptionData);
        // roamingRestrictedInSgsnDueToUnsupportedFeature
        roamingRestrictedInSgsnDueToUnsupportedFeature = subscriptionData.getRoamingRestrictedInSgsnDueToUnsupportedFeature();
        assertFalse(roamingRestrictedInSgsnDueToUnsupportedFeature);
        // networkAccessMode
        networkAccessMode = subscriptionData.getNetworkAccessMode();
        assertEquals(networkAccessMode, NetworkAccessMode.packetAndCircuit);
        // lsaInformation
        lsaInformation = subscriptionData.getLSAInformation();
        assertNull(lsaInformation);
        // lmuIndicator
        lmuIndicator = subscriptionData.getLmuIndicator();
        assertFalse(lmuIndicator);
        // lcsInformation
        lcsInformation = subscriptionData.getLCSInformation();
        assertEquals(lcsInformation.getGmlcList().get(0).getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(lcsInformation.getGmlcList().get(0).getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getGmlcList().get(0).getAddress(), "4917104600321");
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getSsCode().getSupplementaryCodeValue(),
                SupplementaryCodeValue.allMOLR_SS);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getSsCode().getSupplementaryCodeValue(),
                SupplementaryCodeValue.autonomousSelfLocation);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getSsCode().getSupplementaryCodeValue(),
                SupplementaryCodeValue.allLCSPrivacyException);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getSsCode().getSupplementaryCodeValue(),
                SupplementaryCodeValue.allPLMN_specificSS);
        assertTrue(lcsInformation.getLcsPrivacyExceptionList().get(0).getSsStatus().getBitQ());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(0).getSsStatus().getBitP());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(0).getSsStatus().getBitR());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(0).getSsStatus().getBitA());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(1).getSsStatus().getBitQ());
        assertTrue(lcsInformation.getLcsPrivacyExceptionList().get(1).getSsStatus().getBitP());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(1).getSsStatus().getBitR());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(1).getSsStatus().getBitA());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(2).getSsStatus().getBitQ());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(2).getSsStatus().getBitP());
        assertTrue(lcsInformation.getLcsPrivacyExceptionList().get(2).getSsStatus().getBitR());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(2).getSsStatus().getBitA());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(3).getSsStatus().getBitQ());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(3).getSsStatus().getBitP());
        assertFalse(lcsInformation.getLcsPrivacyExceptionList().get(3).getSsStatus().getBitR());
        assertTrue(lcsInformation.getLcsPrivacyExceptionList().get(3).getSsStatus().getBitA());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyLocationAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getNotificationToMSUser(),
                NotificationToMSUser.locationNotAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "874927492");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "398279222");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "23543252234");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "598990298245");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyLocationAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.locationNotAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse);
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getExternalClientList().get(0).getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getPLMNClientList().get(0), LCSClientInternalID.broadcastService);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getPLMNClientList().get(0), LCSClientInternalID.oandMHPLMN);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getPLMNClientList().get(0), LCSClientInternalID.targetMSsubscribedService);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getPLMNClientList().get(0), LCSClientInternalID.anonymousLocation);
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "398279222");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "23543252234");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "598990298245");
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyLocationAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.locationNotAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse);
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getExtExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getExtExternalClientList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getExtExternalClientList().get(0).getExtensionContainer());
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getServiceTypeIdentity(), 1);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getServiceTypeList().get(0).getServiceTypeIdentity(), 2);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getServiceTypeList().get(0).getServiceTypeIdentity(), 3);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getServiceTypeList().get(0).getServiceTypeIdentity(), 4);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getServiceTypeList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getServiceTypeList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getServiceTypeList().get(0).getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyLocationAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(1).getServiceTypeList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationAllowedIfNoResponse);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(2).getServiceTypeList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.locationNotAllowed);
        assertEquals(lcsInformation.getLcsPrivacyExceptionList().get(3).getServiceTypeList().get(0).getNotificationToMSUser(),
                NotificationToMSUser.notifyAndVerifyLocationNotAllowedIfNoResponse);
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(1).getServiceTypeList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(2).getServiceTypeList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getLcsPrivacyExceptionList().get(3).getServiceTypeList().get(0).getExtensionContainer());
        assertEquals(lcsInformation.getMOLRList().get(0).getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allMOLR_SS);
        assertEquals(lcsInformation.getMOLRList().get(1).getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.autonomousSelfLocation);
        assertEquals(lcsInformation.getMOLRList().get(2).getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allLCSPrivacyException);
        assertTrue(lcsInformation.getMOLRList().get(0).getSsStatus().getBitQ());
        assertFalse(lcsInformation.getMOLRList().get(0).getSsStatus().getBitP());
        assertFalse(lcsInformation.getMOLRList().get(0).getSsStatus().getBitR());
        assertFalse(lcsInformation.getMOLRList().get(0).getSsStatus().getBitA());
        assertFalse(lcsInformation.getMOLRList().get(1).getSsStatus().getBitQ());
        assertTrue(lcsInformation.getMOLRList().get(1).getSsStatus().getBitP());
        assertFalse(lcsInformation.getMOLRList().get(1).getSsStatus().getBitR());
        assertFalse(lcsInformation.getMOLRList().get(1).getSsStatus().getBitA());
        assertFalse(lcsInformation.getMOLRList().get(2).getSsStatus().getBitQ());
        assertFalse(lcsInformation.getMOLRList().get(2).getSsStatus().getBitP());
        assertTrue(lcsInformation.getMOLRList().get(2).getSsStatus().getBitR());
        assertFalse(lcsInformation.getMOLRList().get(2).getSsStatus().getBitA());
        assertNull(lcsInformation.getMOLRList().get(0).getExtensionContainer());
        assertNull(lcsInformation.getMOLRList().get(1).getExtensionContainer());
        assertNull(lcsInformation.getMOLRList().get(2).getExtensionContainer());
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getSsCode().getSupplementaryCodeValue(),
                SupplementaryCodeValue.plmn_specificSS_1);
        assertTrue(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getSsStatus().getBitQ());
        assertTrue(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getSsStatus().getBitP());
        assertFalse(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getSsStatus().getBitR());
        assertTrue(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getSsStatus().getBitA());
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "874927492");
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getNotificationToMSUser(), NotificationToMSUser.notifyLocationAllowed);
        assertNull(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExternalClientList().get(0).getClientIdentity().getExtensionContainer());
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getPLMNClientList().get(0), LCSClientInternalID.broadcastService);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getPLMNClientList().get(1), LCSClientInternalID.oandMHPLMN);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getPLMNClientList().get(2), LCSClientInternalID.targetMSsubscribedService);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getPLMNClientList().get(3), LCSClientInternalID.anonymousLocation);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getNumberingPlan(), NumberingPlan.reserved);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddressNature(), AddressNature.international_number);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getExtExternalClientList().get(0).getClientIdentity().getExternalAddress().
                getAddress(), "874927492");
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getServiceTypeIdentity(), 5);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getGMLCRestriction(), GMLCRestriction.homeCountry);
        assertEquals(lcsInformation.getAddLcsPrivacyExceptionList().get(0).getServiceTypeList().get(0).getNotificationToMSUser(), NotificationToMSUser.notifyLocationAllowed);
        // istAlertTimer
        istAlertTimer = subscriptionData.getIstAlertTimer();
        assertEquals(istAlertTimer.intValue(), 200);
        // superChargerSupportedInHLR
        superChargerSupportedInHLR = subscriptionData.getSuperChargerSupportedInHLR();
        assertNull(superChargerSupportedInHLR);
        // mcSsInfo
        mcSsInfo = subscriptionData.getMcSsInfo();
        assertEquals(mcSsInfo.getSSCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(mcSsInfo.getSSStatus().getData(), new byte[] {0x0a});
        assertFalse(mcSsInfo.getSSStatus().getBitP());
        assertTrue(mcSsInfo.getSSStatus().getBitR());
        assertFalse(mcSsInfo.getSSStatus().getBitA());
        assertEquals(mcSsInfo.getNbrSB(), 2);
        assertEquals(mcSsInfo.getNbrUser(), 4);
        assertNull(mcSsInfo.getExtensionContainer());
        // csAllocationRetentionPriority
        csAllocationRetentionPriority = subscriptionData.getCSAllocationRetentionPriority();
        assertEquals(csAllocationRetentionPriority.getData(), 4);
        // sgsnCamelSubscriptionInfo
        sgsnCamelSubscriptionInfo = subscriptionData.getSgsnCamelSubscriptionInfo();
        assertNull(sgsnCamelSubscriptionInfo);
        // chargingCharacteristics
        chargingCharacteristics = subscriptionData.getChargingCharacteristics();
        assertFalse(chargingCharacteristics.isNormalCharging());
        assertFalse(chargingCharacteristics.isPrepaidCharging());
        assertTrue(chargingCharacteristics.isFlatRateChargingCharging());
        assertFalse(chargingCharacteristics.isChargingByHotBillingCharging());
        // accessRestrictionData
        accessRestrictionData = subscriptionData.getAccessRestrictionData();
        assertFalse(accessRestrictionData.getUtranNotAllowed());
        assertFalse(accessRestrictionData.getGeranNotAllowed());
        assertTrue(accessRestrictionData.getGanNotAllowed());
        assertFalse(accessRestrictionData.getEUtranNotAllowed());
        assertFalse(accessRestrictionData.getIHspaEvolutionNotAllowed());
        assertTrue(accessRestrictionData.getHoToNon3GPPAccessNotAllowed());
        // icsIndicator
        icsIndicator = subscriptionData.getIcsIndicator();
        assertTrue(icsIndicator);
        // epsSubscriptionData
        epsSubscriptionData = subscriptionData.getEpsSubscriptionData();
        assertNotNull(epsSubscriptionData);
        apnConfigurationProfile = epsSubscriptionData.getAPNConfigurationProfile();
        ePSDataList = apnConfigurationProfile.getEPSDataList();
        apnConfiguration = ePSDataList.get(0);
        assertEquals(apnConfiguration.getContextId(), 1);
        assertEquals(apnConfiguration.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4v6);
        assertNull(apnConfiguration.getServedPartyIPIPv4Address());
        assertEquals(apnConfiguration.getApn().getApn(), "internet");
        assertEquals(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPriorityLevel(), 9);
        assertTrue(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPreEmptionCapability());
        assertFalse(apnConfiguration.getEPSQoSSubscribed().getAllocationRetentionPriority().getPreEmptionVulnerability());
        assertNull(apnConfiguration.getPdnGwIdentity());
        assertNull(apnConfiguration.getPdnGwAllocationType());
        assertTrue(apnConfiguration.getVplmnAddressAllowed());
        assertFalse(apnConfiguration.getChargingCharacteristics().isNormalCharging());
        assertFalse(apnConfiguration.getChargingCharacteristics().isPrepaidCharging());
        assertTrue(apnConfiguration.getChargingCharacteristics().isFlatRateChargingCharging());
        assertFalse(apnConfiguration.getChargingCharacteristics().isChargingByHotBillingCharging());
        assertEquals(apnConfiguration.getAmbr().getMaxRequestedBandwidthUL(), 2048);
        assertEquals(apnConfiguration.getAmbr().getMaxRequestedBandwidthDL(), 4096);
        assertNull(apnConfiguration.getAmbr().getExtensionContainer());
        assertNull(apnConfiguration.getSpecificAPNInfoList());
        assertNull(apnConfiguration.getExtensionContainer());
        assertEquals(apnConfiguration.getServedPartyIPIPv6Address().getData(), new byte[] {21});
        assertEquals(apnConfiguration.getApnOiReplacement().getData(), new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        assertEquals(apnConfiguration.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(apnConfiguration.getLipaPermission(), LIPAPermission.lipaConditional);
        assertNull(epsSubscriptionData.getAPNConfigurationProfile().getExtensionContainer());
        assertEquals(epsSubscriptionData.getApnOiReplacement().getData(), new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        assertEquals(epsSubscriptionData.getRfspId().intValue(), 0);
        assertEquals(epsSubscriptionData.getAmbr().getMaxRequestedBandwidthUL(), 2048);
        assertEquals(epsSubscriptionData.getAmbr().getMaxRequestedBandwidthDL(), 4096);
        assertNull(epsSubscriptionData.getAmbr().getExtensionContainer());
        assertEquals(epsSubscriptionData.getAPNConfigurationProfile().getDefaultContext(), 1);
        assertTrue(epsSubscriptionData.getAPNConfigurationProfile().getCompleteDataListIncluded());
        assertEquals(epsSubscriptionData.getAPNConfigurationProfile().getEPSDataList().get(0), apnConfiguration);
        assertEquals(epsSubscriptionData.getStnSr().getAddressNature(), AddressNature.international_number);
        assertEquals(epsSubscriptionData.getStnSr().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(epsSubscriptionData.getStnSr().getAddress(), "491710490000");
        assertNull(epsSubscriptionData.getExtensionContainer());
        assertTrue(epsSubscriptionData.getMpsCSPriority());
        assertTrue(epsSubscriptionData.getMpsEPSPriority());
        // csgSubscriptionDataList
        csgSubscriptionDataList = subscriptionData.getCsgSubscriptionDataList();
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(0));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(1));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(25));
        assertTrue(csgSubscriptionDataList.get(0).getCsgId().getData().get(26));
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getYear(), 2024);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getMonth(), 7);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getDay(), 4);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getHour(), 19);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getMinute(), 20);
        assertEquals(csgSubscriptionDataList.get(0).getExpirationDate().getSecond(), 10);
        assertNull(csgSubscriptionDataList.get(0).getExtensionContainer());
        assertEquals(csgSubscriptionDataList.get(0).getLipaAllowedAPNList().get(0).getApn(), "internet");
        // ueReachabilityRequestIndicator
        ueReachabilityRequestIndicator = subscriptionData.getUeReachabilityRequestIndicator();
        assertTrue(ueReachabilityRequestIndicator);
        // sgsnNumber
        sgsnNumber = subscriptionData.getSgsnNumber();
        assertNull(sgsnNumber);
        // mmeName
        mmeName = subscriptionData.getMmeName();
        assertEquals(mmeName, new DiameterIdentityImpl("mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8)));
        // subscribedPeriodicRAUTAUtimer
        subscribedPeriodicRAUTAUtimer = subscriptionData.getSubscribedPeriodicRAUTAUtimer();
        assertEquals(subscribedPeriodicRAUTAUtimer.longValue(), 300);
        // vplmnLIPAAllowed
        vplmnLIPAAllowed = subscriptionData.getVplmnLIPAAllowed();
        assertTrue(vplmnLIPAAllowed);
        // mdtUserConsent
        mdtUserConsent = subscriptionData.getMdtUserConsent();
        assertFalse(mdtUserConsent);
        // subscribedPeriodicLAUtimer
        subscribedPeriodicLAUtimer = subscriptionData.getSubscribedPeriodicLAUtimer();
        assertEquals(subscribedPeriodicLAUtimer.longValue(), 360);
        // vplmnCSGSubscriptionDataList
        vplmnCSGSubscriptionDataList = subscriptionData.getVPLMNCSGSubscriptionDataList();
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(0));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(1));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(25));
        assertTrue(vplmnCSGSubscriptionDataList.get(0).getCsgId().getData().get(26));
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getYear(), 2024);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getMonth(), 7);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getDay(), 4);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getHour(), 19);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getMinute(), 20);
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getExpirationDate().getSecond(), 10);
        assertNull(vplmnCSGSubscriptionDataList.get(0).getExtensionContainer());
        assertEquals(vplmnCSGSubscriptionDataList.get(0).getLipaAllowedAPNList().get(0).getApn(), "internet");
        // additionalMSISDN
        additionalMSISDN = subscriptionData.getAdditionalMSISDN();
        assertEquals(additionalMSISDN.getAddressNature(), AddressNature.international_number);
        assertEquals(additionalMSISDN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(additionalMSISDN.getAddress(), "4917105682451");
        // psAndSMSOnlyServiceProvision
        psAndSMSOnlyServiceProvision = subscriptionData.getPSandSMSOnlyServiceProvision();
        assertFalse(psAndSMSOnlyServiceProvision);
        // smsInSGSNAllowed
        smsInSGSNAllowed = subscriptionData.getSMSInSGSNAllowed();
        assertTrue(smsInSGSNAllowed);
        // csToPsSRVCCAllowedIndicator
        csToPsSRVCCAllowedIndicator = subscriptionData.getCsToPsSRVCCAllowedIndicator();
        assertTrue(csToPsSRVCCAllowedIndicator);
        // pcscfRestorationRequest
        pcscfRestorationRequest = subscriptionData.getPCSCFRestorationRequest();
        assertTrue(pcscfRestorationRequest);
        // adjacentAccessRestrictionDataList
        adjacentAccessRestrictionDataList = subscriptionData.getAdjacentAccessRestrictionDataList();
        assertTrue(adjacentAccessRestrictionDataList.get(0).getExtAccessRestrictionData().getNrAsSecondaryRATNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getExtAccessRestrictionData().getUnlicensedSpectrumAsSecondaryRATNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getGeranNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getUtranNotAllowed());
        assertTrue(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getGanNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getEUtranNotAllowed());
        assertFalse(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getIHspaEvolutionNotAllowed());
        assertTrue(adjacentAccessRestrictionDataList.get(0).getAccessRestrictionData().getHoToNon3GPPAccessNotAllowed());
        assertEquals(adjacentAccessRestrictionDataList.get(0).getPLMNId().getMcc(), 262);
        assertEquals(adjacentAccessRestrictionDataList.get(0).getPLMNId().getMnc(), 999);
        // imsiGroupIdList
        imsiGroupIdList = subscriptionData.getIMSIGroupIdList();
        assertEquals(imsiGroupIdList.get(0).getGroupServiceId().longValue(), 1L);
        assertEquals(imsiGroupIdList.get(0).getLocalGroupId().getData(), "120".getBytes(StandardCharsets.UTF_8));
        // ueUsageType
        ueUsageType = subscriptionData.getUEUsageType();
        assertEquals(ueUsageType.getData(), new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator
        userPlaneIntegrityProtectionIndicator = subscriptionData.getUserPlaneIntegrityProtectionIndicator();
        assertTrue(userPlaneIntegrityProtectionIndicator);
        // dlBufferingSuggestedPacketCount
        dlBufferingSuggestedPacketCount = subscriptionData.getDLBufferingSuggestedPacketCount();
        assertEquals(dlBufferingSuggestedPacketCount.longValue(), 0);
        // resetIdList
        resetIdList = subscriptionData.getResetIdList();
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 2);
        assertEquals(resetIdList.get(0).getData(), new byte[] { (byte) 0x81});
        assertEquals(resetIdList.get(1).getData(), new byte[] { 0x01, 0x02, 0x04, (byte) 0x82});
        // eDRXCycleLengthList
        eDRXCycleLengthList = subscriptionData.getEDRXCycleLengthList();
        assertEquals(eDRXCycleLengthList.get(0).getUsedRATType(), UsedRATType.nbIoT);
        assertEquals(eDRXCycleLengthList.get(0).getEDRXCycleLengthValue().getData(), new byte[] { 0x02 });
        // iabOperationAllowedIndicator
        iabOperationAllowedIndicator = subscriptionData.getIabOperationAllowedIndicator();
        assertTrue(iabOperationAllowedIndicator);
        /*
         * subscriptionDataDeletion
         */
        assertNull(subscriptionDataDeletion);

        // test 8, 3GPP Release v18.0.0 with resetIdList and subscriptionDataDeletion (sent to SGSN)
        rawData = getEncodedDataRel18wSubsDataDelToSGSN();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new ResetRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        networkResource = prim.getNetworkResource();
        sendingNodeNumber = prim.getSendingNodenumber();
        hlrList = prim.getHlrList();
        extensionContainer = prim.getExtensionContainer();
        resetIdList = prim.getResetIdList();
        subscriptionData = prim.getSubscriptionData();
        subscriptionDataDeletion = prim.getSubscriptionDataDeletion();
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: css-Number (1)
         *             css-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 82
         *             Reset-Id: 01020482
         *         subscriptionDataDeletion
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             basicServiceList: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDA-1200bps (18)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: allTeleservices (0)
         *             ss-List: 1 item
         *                 SS-Code: baoc - barring of all outgoing calls (146)
         *             regionalSubscriptionIdentifier: 210f
         *             camelSubscriptionInfoWithdraw
         *             gprsSubscriptionDataWithdraw: contextIdList (1)
         *                 contextIdList: 2 items
         *                     ContextId: 1
         *                     ContextId: 2
         *             roamingRestrictedInSgsnDueToUnsuppportedFeature
         *             lsaInformationWithdraw: allLSAData (0)
         *                 allLSAData
         *             gmlc-ListWithdraw
         *             istInformationWithdraw
         *             chargingCharacteristicsWithdraw
         *             stn-srWithdraw
         *             apn-oi-replacementWithdraw
         *             csg-SubscriptionDeleted
         *             subscribedPeriodicTAU-RAU-TimerWithdraw
         *             subscribedPeriodicLAU-TimerWithdraw
         *             subscribed-vsrvccWithdraw
         *             vplmn-Csg-SubscriptionDeleted
         *             additionalMSISDN-Withdraw
         *             cs-to-ps-SRVCC-Withdraw
         *             imsiGroupIdList-Withdraw
         *             userPlaneIntegrityProtectionWithdraw
         *             dl-Buffering-Suggested-Packet-Count-Withdraw
         *             ue-UsageTypeWithdraw
         *             reset-idsWithdraw
         *             iab-OperationWithdraw
         */
        assertEquals(prim.getMapProtocolVersion(), 2);
        assertNull(networkResource);
        /*
         * sendingNodenumber
         */
        hlrNumber = sendingNodeNumber.getHlrNumber();
        cssNumber = sendingNodeNumber.getCssNumber();
        assertEquals(cssNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(cssNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(cssNumber.getAddress(), "491710490000");
        assertNull(hlrNumber);
        /*
         * hlrList
         */
        assertNull(hlrList);
        /*
         * extensionContainer
         */
        assertNull(extensionContainer);
        /*
         * resetIdList
         */
        assertNotNull(resetIdList);
        assertEquals(resetIdList.size(), 2);
        assertEquals(resetIdList.get(0).getData(), new byte[] {(byte) 0x82});
        assertEquals(resetIdList.get(1).getData(), new byte[] {0x01, 0x02, 0x04, (byte) 0x82});
        /*
         * subscriptionData
         */
        assertNull(subscriptionData);
        /*
         * subscriptionDataDeletion
         */
        assertNotNull(subscriptionDataDeletion);
        // imsi
        imsi = subscriptionDataDeletion.getImsi();
        assertEquals(imsi.getData(), "748026800000000");
        // basicServiceList
        basicServiceList = subscriptionDataDeletion.getBasicServiceList();
        assertEquals(basicServiceList.size(), 2);
        assertEquals(basicServiceList.get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.dataCDA_1200bps);
        assertEquals(basicServiceList.get(1).getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allTeleservices);
        // ssList
        ssList = subscriptionDataDeletion.getSsList();
        assertEquals(ssList.size(), 1);
        assertEquals(ssList.get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.baoc);
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = subscriptionDataDeletion.getRoamingRestrictionDueToUnsupportedFeature();
        assertFalse(roamingRestrictionDueToUnsupportedFeature);
        // regionalSubscriptionIdentifier
        regionalSubscriptionIdentifier = subscriptionDataDeletion.getRegionalSubscriptionIdentifier();
        assertEquals(regionalSubscriptionIdentifier.getData(), new byte[] {0x21, 0x0F});
        // vbsGroupIndication
        vbsGroupIndication = subscriptionDataDeletion.getVbsGroupIndication();
        assertFalse(vbsGroupIndication);
        // vgcsGroupIndication
        vgcsGroupIndication = subscriptionDataDeletion.getVgcsGroupIndication();
        assertFalse(vgcsGroupIndication);
        // camelSubscriptionInfoWithdraw
        camelSubscriptionInfoWithdraw = subscriptionDataDeletion.getCamelSubscriptionInfoWithdraw();
        assertTrue(camelSubscriptionInfoWithdraw);
        // extensionContainer
        extensionContainer = subscriptionDataDeletion.getExtensionContainer();
        assertNull(extensionContainer);
        // gprsSubscriptionDataWithdraw
        gprsSubscriptionDataWithdraw = subscriptionDataDeletion.getGPRSSubscriptionDataWithdraw();
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().size(), 2);
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().get(0).intValue(), 1);
        assertEquals(gprsSubscriptionDataWithdraw.getContextIdList().get(1).intValue(), 2);
        // roamingRestrictedInSgsnDueToUnsuppportedFeature
        roamingRestrictedInSgsnDueToUnsuppportedFeature = subscriptionDataDeletion.getRoamingRestrictedInSgsnDueToUnsuppportedFeature();
        assertTrue(roamingRestrictedInSgsnDueToUnsuppportedFeature);
        // lsaInformationWithdraw
        lsaInformationWithdraw = subscriptionDataDeletion.getLSAInformationWithdraw();
        assertTrue(lsaInformationWithdraw.getAllLSAData());
        // gmlcListWithdraw
        gmlcListWithdraw = subscriptionDataDeletion.getGmlcListWithdraw();
        assertTrue(gmlcListWithdraw);
        // istInformationWithdraw
        istInformationWithdraw = subscriptionDataDeletion.getIstInformationWithdraw();
        assertTrue(istInformationWithdraw);
        // specificCSIWithdraw
        specificCSIWithdraw = subscriptionDataDeletion.getSpecificCSIWithdraw();
        assertNull(specificCSIWithdraw);
        // chargingCharacteristicsWithdraw
        chargingCharacteristicsWithdraw = subscriptionDataDeletion.getChargingCharacteristicsWithdraw();
        assertTrue(chargingCharacteristicsWithdraw);
        // stnSrWithdraw
        stnSrWithdraw = subscriptionDataDeletion.getStnSrWithdraw();
        assertTrue(stnSrWithdraw);
        // epsSubscriptionDataWithdraw
        epsSubscriptionDataWithdraw = subscriptionDataDeletion.getEPSSubscriptionDataWithdraw();
        assertNull(epsSubscriptionDataWithdraw);
        // apnOiReplacementWithdraw
        apnOiReplacementWithdraw = subscriptionDataDeletion.getApnOiReplacementWithdraw();
        assertTrue(apnOiReplacementWithdraw);
        // csgSubscriptionDeleted
        csgSubscriptionDeleted = subscriptionDataDeletion.getCsgSubscriptionDeleted();
        assertTrue(csgSubscriptionDeleted);
        // subscribedPeriodicTAURAUTimerWithdraw
        subscribedPeriodicTAURAUTimerWithdraw = subscriptionDataDeletion.getSubscribedPeriodicTAURAUTimerWithdraw();
        assertTrue(subscribedPeriodicTAURAUTimerWithdraw);
        // subscribedPeriodicLAUTimerWithdraw
        subscribedPeriodicLAUTimerWithdraw = subscriptionDataDeletion.getSubscribedPeriodicLAUTimerWithdraw();
        assertTrue(subscribedPeriodicLAUTimerWithdraw);
        // subscribedVsrvccWithdraw
        subscribedVsrvccWithdraw = subscriptionDataDeletion.getSubscribedVsrvccWithdraw();
        assertTrue(subscribedVsrvccWithdraw);
        // vplmnCsgSubscriptionDeleted
        vplmnCsgSubscriptionDeleted = subscriptionDataDeletion.getVplmnCsgSubscriptionDeleted();
        assertTrue(vplmnCsgSubscriptionDeleted);
        // additionalMSISDNWithdraw
        additionalMSISDNWithdraw = subscriptionDataDeletion.getGmlcListWithdraw();
        assertTrue(additionalMSISDNWithdraw);
        // csToPsSRVCCWithdraw
        csToPsSRVCCWithdraw = subscriptionDataDeletion.getCsToPsSRVCCWithdraw();
        assertTrue(csToPsSRVCCWithdraw);
        // imsiGroupIdListWithdraw
        imsiGroupIdListWithdraw = subscriptionDataDeletion.getImsiGroupIdListWithdraw();
        assertTrue(imsiGroupIdListWithdraw);
        // userPlaneIntegrityProtectionWithdraw
        userPlaneIntegrityProtectionWithdraw = subscriptionDataDeletion.getUserPlaneIntegrityProtectionWithdraw();
        assertTrue(userPlaneIntegrityProtectionWithdraw);
        // dlBufferingSuggestedPacketCountWithdraw
        dlBufferingSuggestedPacketCountWithdraw = subscriptionDataDeletion.getDlBufferingSuggestedPacketCountWithdraw();
        assertTrue(dlBufferingSuggestedPacketCountWithdraw);
        // ueUsageTypeWithdraw
        ueUsageTypeWithdraw = subscriptionDataDeletion.getUeUsageTypeWithdraw();
        assertTrue(ueUsageTypeWithdraw);
        // resetIdsWithdraw
        resetIdsWithdraw = subscriptionDataDeletion.getResetIdsWithdraw();
        assertTrue(resetIdsWithdraw);
        // iabOperationWithdraw
        iabOperationWithdraw = subscriptionDataDeletion.getIabOperationWithdraw();
        assertTrue(iabOperationWithdraw);
    }

    @Test(groups = { "functional.encode", "service.mobility.faultRecovery" })
    public void testEncode() throws Exception {

        // test 1 (MAP v1)
        ISDNAddressStringImpl hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        ResetRequestImpl prim = new ResetRequestImpl(NetworkResource.hlr, hlrNumber, null, 1);

        AsnOutputStream asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        // test 2 (MAP v2, old version)
        ArrayList<IMSI> hlrList = new ArrayList<>();
        IMSIImpl imsi = new IMSIImpl("1234001");
        hlrList.add(imsi);
        prim = new ResetRequestImpl(null, hlrNumber, hlrList, 2);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 3, 3GPP Release v18.0.0 with hlrList
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         hlr-List: 3 items
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             IMSI: 748026900000000
         *             [Association IMSI: 748026900000000]
         *             IMSI: 748027000000000
         *             [Association IMSI: 748027000000000]
         */
        hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        SendingNodeNumber sendingNodeNumber = new SendingNodeNumberImpl(hlrNumber, null);
        IMSI imsi21 = new IMSIImpl("748026800000000");
        IMSI imsi22 = new IMSIImpl("748026900000000");
        IMSI imsi23 = new IMSIImpl("748027000000000");
        hlrList = new ArrayList<>();
        hlrList.add(imsi21);
        hlrList.add(imsi22);
        hlrList.add(imsi23);
        prim = new ResetRequestImpl(sendingNodeNumber, hlrList, null, null, null, null);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18_0();
        assertTrue(Arrays.equals(rawData, encodedData));

        // test 4, 3GPP Release v18.0.0 with resetIdList and subscriptionData
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 01020481
         *             Reset-Id: 01020482
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             bearerServiceList: 2 items
         *                 Ext-BearerServiceCode: allDataCDS-Services (24)
         *                 Ext-BearerServiceCode: dataCDA-1200-75bps (19)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: shortMessageMO-PP (34)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: allCallCompletionSS - all Call completion SS (64)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: cfnrc - call forwarding on mobile subscriber not reachable (43)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: efff1ce8
         *                     1... .... = allOG-CallsBarred: True
         *                     .1.. .... = internationalOGCallsBarred: True
         *                     ..1. .... = internationalOGCallsNotToHPLMN-CountryBarred: True
         *                     ...0 .... = premiumRateInformationOGCallsBarred: False
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..1. = interzonalOGCallsBarred: True
         *                     .... ...1 = interzonalOGCallsNotToHPLMN-CountryBarred: True
         *                     1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
         *                     .1.. .... = allECT-Barred: True
         *                     ..1. .... = chargeableECT-Barred: True
         *                     ...1 .... = internationalECT-Barred: True
         *                     .... 1... = interzonalECT-Barred: True
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...1 = allPacketOrientedServicesBarred: True
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .0.. .... = roamerAccessToVPLMN-AP-Barred: False
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...1 .... = allIC-CallsBarred: True
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...0 .... = registrationInterzonalCFNotToHPLMN-Barred: False
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 80
         *                     1... .... = plmn-SpecificBarringType1: True
         *                     .0.. .... = plmn-SpecificBarringType2: False
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...0 .... = plmn-SpecificBarringType4: False
         *             roamingRestrictionDueToUnsupportedFeature
         *             regionalSubscriptionData: 1 item
         *                 ZoneCode: 0507
         *             vbsSubscriptionData: 1 item
         *                 VoiceBroadcastData
         *                     groupid: ffffff
         *                         TBCD digits:
         *                     broadcastInitEntitlement
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vgcsSubscriptionData: 1 item
         *                 VoiceGroupCallData
         *                     groupId: ffffff
         *                         TBCD digits:
         *                     Padding: 5
         *                     additionalSubscriptions: e0
         *                         1... .... = privilegedUplinkRequest: True
         *                         .1.. .... = emergencyUplinkRequest: True
         *                         ..1. .... = emergencyReset: True
         *                     Padding: 0
         *                     additionalInfo: c000008000000000000000000000000000
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vlrCamelSubscriptionInfo
         *                 o-CSI
         *                     o-BcsmCamelTDPDataList: 1 item
         *                         O-BcsmCamelTDPData
         *                             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csiActive
         *                 ss-CSI
         *                     ss-CamelData
         *                         ss-EventList: 2 items
         *                             SS-Code: allCallCompletionSS - all Call completion SS (64)
         *                             SS-Code: cfnrc - call forwarding on mobile subscriber not reachable (43)
         *                         gsmSCF-Address: 91947101640092
         *                             1... .... = Extension: No Extension
         *                             .001 .... = Nature of number: International Number (0x1)
         *                             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                             E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 o-BcsmCamelTDP-CriteriaList: 1 item
         *                     O-BcsmCamelTDP-Criteria
         *                         o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                         destinationNumberCriteria
         *                             matchType: enabling (1)
         *                             destinationNumberList: 1 item
         *                                 ISDN-AddressString: 91947141874023
         *                                     1... .... = Extension: No Extension
         *                                     .001 .... = Nature of number: International Number (0x1)
         *                                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                     E.164 number (MSISDN): 491714780432
         *                             destinationNumberLengthList: 1 item
         *                                 DestinationNumberLengthList item: 1
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allDataCDS-Services (24)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: allFacsimileTransmissionServices (96)
         *                         callTypeCriteria: notForwarded (1)
         *                         o-CauseValueCriteria: 2 items
         *                             CauseValue: 51
         *                             CauseValue: 39
         *                 tif-CSI
         *                 m-CSI
         *                     mobilityTriggers: 2 items
         *                         MM-Code: 02
         *                         MM-Code: 00
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 vt-CSI
         *                     t-BcsmCamelTDPDataList: 2 items
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tBusy (13)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *                     T-BCSM-CAMEL-TDP-Criteria
         *                         t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: dataPDS-9600bps (46)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: plmn-specificTS-A (218)
         *                         t-CauseValueCriteria: 2 items
         *                             CauseValue: 15
         *                             CauseValue: 39
         *                 d-CSI
         *                     dp-AnalysedInfoCriteriaList: 1 item
         *                         DP-AnalysedInfoCriterium
         *                             dialledNumber: 91947141874023
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491714780432
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *             naea-PreferredCI
         *                 naea-PreferredCIC: 235408
         *             networkAccessMode: packetAndCircuit (0)
         *             istAlertTimer: 200
         *             mc-SS-Info
         *                 ss-Code: cfu - call forwarding unconditional (33)
         *                 ss-Status: 0a
         *                 0000 .... = Unused: 0x0
         *                 .... .0.. = P bit: Not provisioned
         *                 .... ..1. = R bit: Registered
         *                 .... ...0 = A bit: not Active
         *                 nbrSB: 2
         *                 nbrUser: 4
         *             cs-AllocationRetentionPriority: 04
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ics-Indicator: True
         *             eps-SubscriptionData
         *                 apn-oi-Replacement: 515c53545556575859
         *                 rfsp-id: 0
         *                 ambr
         *                     max-RequestedBandwidth-UL: 2048
         *                     max-RequestedBandwidth-DL: 4096
         *                 apn-ConfigurationProfile
         *                     defaultContext: 1
         *                     completeDataListIncluded
         *                     epsDataList: 1 item
         *                         APN-Configuration
         *                             contextId: 1
         *                             pdn-Type: 03
         *                             apn: 08696e7465726e6574 - internet
         *                                 APN: internet
         *                             eps-qos-Subscribed
         *                                 qos-Class-Identifier: 5
         *                                 allocation-Retention-Priority
         *                                     priority-level: 9
         *                                     pre-emption-capability: True
         *                                     pre-emption-vulnerability: False
         *                             vplmnAddressAllowed
         *                             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *                             ambr
         *                                 max-RequestedBandwidth-UL: 2048
         *                                 max-RequestedBandwidth-DL: 4096
         *                             servedPartyIP-IPv6-Address: 15
         *                             apn-oi-Replacement: 515c53545556575859
         *                             sipto-Permission: siptoAboveRanAllowed (0)
         *                             lipa-Permission: lipaConditional (2)
         *                 stn-sr: 91947101940000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710490000
         *                 mps-CSPriority
         *                 mps-EPSPriority
         *             csg-SubscriptionDataList: 1 item
         *             ue-ReachabilityRequestIndicator
         *             mme-Name: mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             subscribedPeriodicLAUtimer: 360
         *             vplmn-Csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             additionalMSISDN: 91947101652854f1
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 4917105682451
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             adjacentAccessRestrictionDataList: 1 item
         *                 AdjacentAccessRestrictionData
         *                     plmnId: 629299
         *                     Padding: 2
         *                     accessRestrictionData: 24
         *                         0... .... = utranNotAllowed: False
         *                         .0.. .... = geranNotAllowed: False
         *                         ..1. .... = ganNotAllowed: True
         *                         ...0 .... = i-hspa-evolutionNotAllowed: False
         *                         .... 0... = wb-e-utranNotAllowed: False
         *                         .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                         .... ..0. = nb-iotNotAllowed: False
         *                         .... ...0 = enhancedCoverageNotAllowed: False
         *                     Padding: 0
         *                     ext-AccessRestrictionData: 80000000
         *                         1... .... = nrAsSecondaryRATNotAllowed: True
         *                         .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             imsi-Group-Id-List: 1 item
         *                 IMSI-GroupId
         *                     group-Service-Id: 1
         *                     plmnId: 629299
         *                     local-Group-ID: 313230
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: nb-iot (5)
         *                     eDRX-Cycle-Length-Value: 02
         *             Padding: 0
         *             ext-AccessRestrictionData: 80000000
         *                 1... .... = nrAsSecondaryRATNotAllowed: True
         *                 .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             iab-Operation-Allowed-Indicator
         */
        hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        SendingNodeNumber sendingNodenumber = new SendingNodeNumberImpl(hlrNumber, null);
        sendingNodenumber.getHlrNumber();
        ArrayList<ResetId> resetIdList = new ArrayList<>();
        ResetId resetId1 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x81});
        ResetId resetId2 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // imsi
        imsi = new IMSIImpl("748026800000000");
        // msisdn
        // category
        Category category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus
        SubscriberStatus subscriberStatus = SubscriberStatus.serviceGranted;
        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue1 = BearerServiceCodeValue.allDataCDS_Services;
        ExtBearerServiceCode extBearerServiceCode1 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue1);
        BearerServiceCodeValue bearerServiceCodeValue2 = BearerServiceCodeValue.dataCDA_1200_75bps;
        ExtBearerServiceCode extBearerServiceCode2 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue2);
        bearerServiceList.add(extBearerServiceCode1);
        bearerServiceList.add(extBearerServiceCode2);
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<>();
        ExtTeleserviceCode shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ExtTeleserviceCode shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
        teleserviceList.add(shortMessageMT_PP);
        teleserviceList.add(shortMessageMO_PP);
        // provisionedSS
        ArrayList<ExtSSInfo> provisionedSS = new ArrayList<>();
        SSCode ssCode1 = new SSCodeImpl(SupplementaryCodeValue.plmn_specificSS_2);
        ExtSSStatus ssCode1ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        SSSubscriptionOption ssCode1SubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<>();
        BearerServiceCodeValue bearerServiceCodeValue3 = BearerServiceCodeValue.allDataCDS_Services;
        ExtBearerServiceCode extBearerServiceCode3 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue3);
        ExtBasicServiceCode extBasicServiceCode3 = new ExtBasicServiceCodeImpl(extBearerServiceCode3);
        TeleserviceCodeValue teleserviceCodeValue2 = TeleserviceCodeValue.allFacsimileTransmissionServices;
        ExtTeleserviceCode extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        ExtBasicServiceCode extBasicServiceCode4 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode3);
        basicServiceList.add(extBasicServiceCode4);
        ExtSSData extSSData1 = new ExtSSDataImpl(ssCode1, ssCode1ExtSSStatus, ssCode1SubscriptionOption, basicServiceList, null);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.bicRoam);
        ExtSSStatus ssCode2ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        SSSubscriptionOption ssCode2SubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
        ExtSSData extSSData2 = new ExtSSDataImpl(ssCode2, ssCode2ExtSSStatus, ssCode2SubscriptionOption, basicServiceList, null);
        ExtSSInfo extSSInfo1= new ExtSSInfoImpl(extSSData1);
        ExtSSInfo extSSInfo2 = new ExtSSInfoImpl(extSSData2);
        provisionedSS.add(extSSInfo1);
        provisionedSS.add(extSSInfo2);
        // odbData
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
        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                internationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred,
                ssAccessBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, allECTBarred, chargeableECTBarred,
                internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred,
                roamingOutsidePLMNCountryBarred, registrationAllCFBarred, registrationCFNotToHPLMNBarred,
                registrationInterzonalCFBarred, registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
        boolean plmnSpecificBarringType1 = true;
        boolean plmnSpecificBarringType2 = false;
        boolean plmnSpecificBarringType3 = false;
        boolean plmnSpecificBarringType4 = false;
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        // roamingRestrictionDueToUnsupportedFeature
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        // regionalSubscriptionData
        ArrayList<ZoneCode> regionalSubscriptionData = new ArrayList<>();
        ZoneCode zoneCode = new ZoneCodeImpl(new byte[] {5, 7});
        regionalSubscriptionData.add(zoneCode);
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
        MatchType matchType = MatchType.enabling;
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<>();
        ISDNAddressString destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        destinationNumberList.add(destinationNumber);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<>();
        destinationNumberLengthList.add(1);
        DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
        CallTypeCriteria callTypeCriteria = CallTypeCriteria.notForwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<>();
        CauseValue causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
        CauseValue causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        oCauseValueCriteria.add(causeValue1);
        oCauseValueCriteria.add(causeValue2);
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<>();
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
                basicServiceList, callTypeCriteria, oCauseValueCriteria, null);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);
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
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCSI, null,
                ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mcsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCSI, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);
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
        LCSInformation lcsInformation = null;
        // istAlertTimer
        int istAlertTimer = 200;
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
        int defaultContext = 1;
        boolean completeDataListIncluded = true;
        PDNType pDNType = new PDNTypeImpl(PDNTypeValue.IPv4v6);
        PDPAddress servedPartyIPIPv4Address = null;
        APN apn = new APNImpl("internet");
        QoSClassIdentifier qci = QoSClassIdentifier.QCI_5;
        int priorityLevel = 9;
        boolean preEmptionCapability = true;
        boolean preEmptionVulnerability = false;
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
        int rfspId = 0;
        ISDNAddressString stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710490000");
        boolean mpsCSPriority = true;
        boolean mpsEPSPriority = true;
        EPSSubscriptionData epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile,
                stnSr, null, mpsCSPriority, mpsEPSPriority);
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
        apn = new APNImpl("internet");
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
        long subscribedPeriodicRAUTAUtimer = 300L;
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
        long groupServiceId = 1L;
        LocalGroupId localGroupId = new LocalGroupIdImpl("120".getBytes(StandardCharsets.UTF_8));
        IMSIGroupId imsiGroupId = new IMSIGroupIdImpl(groupServiceId, plmnId, localGroupId);
        ArrayList<IMSIGroupId> imsiGroupIdList = new ArrayList<>();
        imsiGroupIdList.add(imsiGroupId);
        // ueUsageType
        UEUsageType ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator
        boolean userPlaneIntegrityProtectionIndicator = true;
        // dlBufferingSuggestedPacketCount
        long dlBufferingSuggestedPacketCount = 0L;
        // resetIdList does not apply for subscriptionData inside MAP RST
        // eDRXCycleLengthList
        UsedRATType usedRATType = UsedRATType.nbIoT;
        byte[] edrCycleLengthVal = new byte[] { 0x02 };
        EDRXCycleLengthValue eDRXCycleLengthValue = new EDRXCycleLengthValueImpl(edrCycleLengthVal);
        EDRXCycleLength edrxCycleLength = new EDRXCycleLengthImpl(usedRATType, eDRXCycleLengthValue);
        ArrayList<EDRXCycleLength> eDRXCycleLengthList = new ArrayList<>();
        eDRXCycleLengthList.add(edrxCycleLength);
        // iabOperationAllowedIndicator
        boolean iabOperationAllowedIndicator = true;
        InsertSubscriberDataArgs subscriptionData = new InsertSubscriberDataArgsImpl(imsi, null, category, subscriberStatus, bearerServiceList, teleserviceList,
                provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, naeaPreferredCI, null,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator, lcsInformation,
                istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo,
                chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                subscribedPeriodicLAUtimer, vplmnCSGSubscriptionDataList, additionalMSISDN, psAndSMSOnlyServiceProvision, smsInSGSNAllowed,
                csToPsSRVCCAllowedIndicator, pcscfRestorationRequest, adjacentAccessRestrictionDataList, imsiGroupIdList, ueUsageType,
                userPlaneIntegrityProtectionIndicator, dlBufferingSuggestedPacketCount, null, eDRXCycleLengthList,
                extAccessRestrictionData, iabOperationAllowedIndicator);

        prim = new ResetRequestImpl(sendingNodeNumber, null, null, resetIdList, subscriptionData, null);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18wSubsDataToVLR();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5, 3GPP Release v18.0.0 with resetIdList and subscriptionData
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 1 item
         *             Reset-Id: 81
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: allDataTeleservices (112)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: plmn-specificSS-6 (246)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: ccbs-B - completion of call to busy subscribers, destination side (68)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: 1c0648f8
         *                     0... .... = allOG-CallsBarred: False
         *                     .0.. .... = internationalOGCallsBarred: False
         *                     ..0. .... = internationalOGCallsNotToHPLMN-CountryBarred: False
         *                     ...1 .... = premiumRateInformationOGCallsBarred: True
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..0. = interzonalOGCallsBarred: False
         *                     .... ...0 = interzonalOGCallsNotToHPLMN-CountryBarred: False
         *                     0... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: False
         *                     .0.. .... = allECT-Barred: False
         *                     ..0. .... = chargeableECT-Barred: False
         *                     ...0 .... = internationalECT-Barred: False
         *                     .... 0... = interzonalECT-Barred: False
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...0 = allPacketOrientedServicesBarred: False
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .1.. .... = roamerAccessToVPLMN-AP-Barred: True
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...0 .... = allIC-CallsBarred: False
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .0.. = roamingOutsidePLMNICountryIC-CallsBarred: False
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...1 .... = registrationInterzonalCFNotToHPLMN-Barred: True
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 50
         *                     0... .... = plmn-SpecificBarringType1: False
         *                     .1.. .... = plmn-SpecificBarringType2: True
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...1 .... = plmn-SpecificBarringType4: True
         *             roamingRestrictionDueToUnsupportedFeature
         *             gprsSubscriptionData
         *                 completeDataListIncluded
         *                 gprsDataList: 1 item
         *                     PDP-Context
         *                         pdp-ContextId: 1
         *                         pdp-Type: f121
         *                         .... 0001 = PDP Type Organization: IETF (0x1)
         *                         pdp-Address: 15
         *                         qos-Subscribed: 273205
         *                         00.. .... = Spare bit(s): 0
         *                         ..10 0... = Quality of Service Delay class: Delay class 4 (best effort) (4)
         *                         .... .111 = Reliability class: Reserved (7)
         *                         0011 .... = Peak throughput: Up to 4 000 octet/s (3)
         *                         .... 0... = Spare bit(s): 0
         *                         .... .010 = Precedence class: Normal priority (2)
         *                         000. .... = Spare bit(s): 0
         *                         ...0 0101 = Mean throughput: 2 000 octet/h (5)
         *                         vplmnAddressAllowed
         *                         apn: 08696e7465726e6574 - internet
         *                             APN: internet
         *                         ext-QoS-Subscribed: 097297804000a34000
         *                             0000 1001 = Allocation/Retention priority: 9
         *                             011. .... = Traffic class: Interactive class (3)
         *                             ...1 0... = Delivery order: Streaming class (2)
         *                             .... ..10 = Delivery of erroneous SDUs: Erroneous SDUs are delivered('yes') (2)
         *                             Maximum SDU size: 0x97 not defined in TS 24.008
         *                             Maximum bit rate for uplink in kbit/s: 576
         *                             Maximum bit rate for downlink in kbit/s: 64
         *                             0000 .... = Residual Bit Error Rate (BER): Subscribed residual BER/Reserved (0)
         *                             .... 0000 = SDU error ratio: Subscribed SDU error ratio/Reserved (0)
         *                             1010 00.. = Transfer delay (Raw data see TS 24.008 for interpretation): 40
         *                             .... ..11 = Traffic handling priority: Priority level 3 (3)
         *                             Guaranteed bit rate for uplink in kbit/s: 64
         *                             Guaranteed bit rate for downlink in kbit/s: Subscribed guaranteed bit rate for downlink/reserved
         *                         .... 1000 .... .... = pdp-ChargingCharacteristics: N (Normal billing) (8)
         *                         ext2-QoS-Subscribed: 100000
         *                             000. .... = Spare bit(s): 0
         *                             ...1 .... = Signalling indication: Optimised for signalling traffic
         *                             .... 0000 = Source statistics description: unknown (0)
         *                             Maximum bitrate for downlink (extended): Use the value indicated by the Maximum bit rate for downlink (0)
         *                             Guaranteed bitrate for downlink (extended): Use the value indicated by the Guaranteed bit rate for downlink (0)
         *                         ext3-QoS-Subscribed: 0000
         *                             Maximum bitrate for uplink (extended): Use the value indicated by the Maximum bit rate for uplink (0)
         *                             Guaranteed bitrate for uplink (extended): Use the value indicated by the Guaranteed bit rate for uplink (0)
         *                         ext4-QoS-Subscribed: 5b
         *                             .... ...1 = PVI Pre-emption Vulnerability: Disabled
         *                             ..01 10.. = PL Priority Level: 6
         *                             .1.. .... = PCI Pre-emption Capability: Disabled
         *                         apn-oi-Replacement: 515c53545556575859
         *                         ext-pdp-Type: 3a3b
         *                         ext-pdp-Address: 3c
         *                         sipto-Permission: siptoAboveRanAllowed (0)
         *                         lipa-Permission: lipaConditional (2)
         *                 apn-oi-Replacement: 515c53545556575859
         *             networkAccessMode: packetAndCircuit (0)
         *             istAlertTimer: 200
         *             superChargerSupportedInHLR: 07
         *             sgsn-CAMEL-SubscriptionInfo
         *                 gprs-CSI
         *                     gprs-CamelTDPDataList: 1 item
         *                         GPRS-CamelTDPData
         *                             gprs-TriggerDetectionPoint: attach (1)
         *                             serviceKey: 12
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSessionHandling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 3
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *                 mg-csi
         *                     mobilityTriggers: 1 item
         *                         MM-Code: 83
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ue-ReachabilityRequestIndicator
         *             sgsn-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             psAndSMS-OnlyServiceProvision
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: geran (1)
         *                     eDRX-Cycle-Length-Value: 02
         */
        hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        sendingNodenumber = new SendingNodeNumberImpl(hlrNumber, null);
        sendingNodenumber.getHlrNumber();
        resetIdList = new ArrayList<>();
        resetId1 = new ResetIdImpl(new byte[] {(byte) 0x81});
        resetIdList.add(resetId1);
        // imsi
        imsi = new IMSIImpl("748026800000000");
        // msisdn doesn't make sense in MAP RST
        // category
        category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
        // bearerServiceList
        bearerServiceList = null;
        // teleserviceList
        teleserviceList = new ArrayList<>();
        shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ExtTeleserviceCode allDataTeleservices = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allDataTeleservices);
        teleserviceList.add(shortMessageMT_PP);
        teleserviceList.add(allDataTeleservices);
        // provisionedSS
        provisionedSS = new ArrayList<>();
        ssCode1 = new SSCodeImpl(SupplementaryCodeValue.plmn_specificSS_6);
        ssCode1ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        ssCode1SubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
        extSSData1 = new ExtSSDataImpl(ssCode1, ssCode1ExtSSStatus, ssCode1SubscriptionOption, basicServiceGroupList, null);
        ssCode2 = new SSCodeImpl(SupplementaryCodeValue.ccbs_B);
        ssCode2ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        ssCode2SubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
        extSSData2 = new ExtSSDataImpl(ssCode2, ssCode2ExtSSStatus, ssCode2SubscriptionOption, basicServiceGroupList, null);
        extSSInfo1= new ExtSSInfoImpl(extSSData1);
        extSSInfo2 = new ExtSSInfoImpl(extSSData2);
        provisionedSS.add(extSSInfo1);
        provisionedSS.add(extSSInfo2);
        // odbData
        allOGCallsBarred = false;
        internationalOGCallsBarred = false;
        internationalOGCallsNotToHPLMNCountryBarred = false;
        premiumRateInformationOGCallsBarred = true;
        interzonalOGCallsBarred = false;
        interzonalOGCallsNotToHPLMNCountryBarred = false;
        interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = false;
        allECTBarred = false;
        chargeableECTBarred = false;
        internationalECTBarred = false;
        interzonalECTBarred = false;
        allPacketOrientedServicesBarred = false;
        roamerAccessToVPLMNAPBarred = true;
        allICCallsBarred = false;
        roamingOutsidePLMNICountryICCallsBarred = false;
        registrationInterzonalCFNotToHPLMNBarred = true;
        oDBGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred, internationalOGCallsNotToHPLMNCountryBarred,
                premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred, ssAccessBarred,
                interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred, interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred,
                allECTBarred, chargeableECTBarred, internationalECTBarred, interzonalECTBarred,
                doublyChargeableECTBarred, multipleECTBarred, allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred, roamingOutsidePLMNCountryBarred,
                registrationAllCFBarred, registrationCFNotToHPLMNBarred, registrationInterzonalCFBarred,
                registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
        plmnSpecificBarringType1 = false;
        plmnSpecificBarringType2 = true;
        plmnSpecificBarringType4 = true;
        odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
        odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        // regionalSubscriptionData
        regionalSubscriptionData = null;
        // vbsSubscriptionData
        vbsSubscriptionData = null;
        // vgcsSubscriptionData
        vgcsSubscriptionData = null;
        // vlrCamelSubscriptionInfo
        vlrCamelSubscriptionInfo = null;
        // naeaPreferredCI
        naeaPreferredCI = null;
        // gprsSubscriptionData
        ArrayList<PDPContext> gprsDataList = new ArrayList<>();
        int pdpContextId = 1;
        PDPType pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddress pdpAddress = new PDPAddressImpl(new byte[] { 21 });
        QoSSubscribed_ReliabilityClass reliabilityClass = QoSSubscribed_ReliabilityClass.reserved_7;
        QoSSubscribed_DelayClass delayClass = QoSSubscribed_DelayClass.delay_Class_4_bestEffort;
        QoSSubscribed_PrecedenceClass precedenceClass = QoSSubscribed_PrecedenceClass.normalPriority;
        QoSSubscribed_PeakThroughput peakThroughput = QoSSubscribed_PeakThroughput.upTo_4000_octetS;
        QoSSubscribed_MeanThroughput meanThroughput = QoSSubscribed_MeanThroughput._2000_octetH;
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(reliabilityClass, delayClass, precedenceClass, peakThroughput, meanThroughput);
        apn = new APNImpl("internet");
        int allocationRetentionPriority = 9;
        ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes;
        ExtQoSSubscribed_DeliveryOrder deliveryOrder = ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo;
        ExtQoSSubscribed_TrafficClass trafficClass = ExtQoSSubscribed_TrafficClass.interactiveClass;
        int maximumSduSizeData = 151;
        boolean isSourceData = true;
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(maximumSduSizeData, isSourceData);
        int maximumBitRateForUL = 128;
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForUL, isSourceData);
        int maximumBitRateForDL = 576;
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForDL, isSourceData);
        ExtQoSSubscribed_ResidualBER residualBER = ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved;
        ExtQoSSubscribed_SduErrorRatio sduErrorRatio = ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved;
        ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3;
        int transferDelayValue = 1000;
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelayImpl(transferDelayValue, isSourceData);
        int gbrUL = 64;
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(gbrUL, isSourceData);
        int gbrDL = 256;
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(gbrDL, isSourceData);
        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus,
                deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink, maximumBitRateForDownlink, residualBER,
                sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        isNormalCharging = true;
        isFlatRateCharging = false;
        chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
        Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor = Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        boolean optimisedForSignallingTraffic = true;
        int maxBRDLExt = 256000;
        ExtQoSSubscribed_BitRateExtended maxBitRateForDLExt = new ExtQoSSubscribed_BitRateExtendedImpl(maxBRDLExt, isSourceData);
        int gbrExtDL = 128000;
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDLExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrExtDL, isSourceData);
        Ext2QoSSubscribed ext2QoSSubscribed = new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic,
                maxBitRateForDLExt, guaranteedBitRateForDLExtended);
        int mbrULExt = 256000;
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(mbrULExt, isSourceData);
        int gbrULExt = 128000;
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrULExt, isSourceData);
        Ext3QoSSubscribed ext3QoSSubscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
        Ext4QoSSubscribed ext4QoSSubscribed = new Ext4QoSSubscribedImpl(91);
        apnOiReplacement = new APNOIReplacementImpl(new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        ExtPDPType extpdpType = new ExtPDPTypeImpl(new byte[] { 58, 59 });
        PDPAddress extpdpAddress = new PDPAddressImpl(new byte[] { 60 });
        PDPContext pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                null, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed, ext4QoSSubscribed,
                apnOiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
        gprsDataList.add(pdpContext);
        GPRSSubscriptionData gprsSubscriptionData = new GPRSSubscriptionDataImpl(completeDataListIncluded, gprsDataList, null, apnOiReplacement);
        // superChargerSupportedInHLR
        superChargerSupportedInHLR = new AgeIndicatorImpl(new byte[] {7});
        // mcSsInfo
        mcSsInfo = null;
        // csAllocationRetentionPriority
        csAllocationRetentionPriority = null;
        // sgsnCamelSubscriptionInfo
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attach;
        long sk = 12;
        gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.continueTransaction;
        GPRSCamelTDPData gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, sk, gsmSCFAddress, defaultSessionHandling, null);
        ArrayList<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        camelCapabilityHandling = 3;
        GPRSCSI gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        smsCamelTdpDataList = new ArrayList<>();
        smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, null);
        smsCamelTdpDataList.add(smscameltdpData);
        SMSCSI moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        mtSmsCamelTdpCriteriaList = new ArrayList<>();
        mtsmstpduTypeArrayList = new ArrayList<>();
        mtsmstpduTypeArrayList.add(mtsmstpduType1);
        mtsmstpduTypeArrayList.add(mtsmstpduType2);
        mtsmstpduTypeArrayList.add(mtsmstpduType3);
        mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
        mobilityTriggers = new ArrayList<>();
        MMCode mmCode = new MMCodeImpl(MMCodeValue.GPRSAttach);
        mobilityTriggers.add(mmCode);
        MGCSI mgCsi = new MGCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        sgsnCamelSubscriptionInfo = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi,
                null, mtSmsCSI, mtSmsCamelTdpCriteriaList, mgCsi);
        // chargingCharacteristics
        isNormalCharging = false;
        isFlatRateCharging = true;
        chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
        // accessRestrictionData
        accessRestrictionData = new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed, eUtranNotAllowed, hoToNon3GppAccessNotAllowed);
        // icsIndicator
        icsIndicator = null;
        // epsSubscriptionData
        epsSubscriptionData = null;
        // csgSubscriptionDataList
        csgSubscriptionDataList = null;
        // sgsnNumber
        sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        // mmeName
        mmeName = null;
        // subscribedPeriodicLAUtimer
        subscribedPeriodicLAUtimer = null;
        // vplmnCSGSubscriptionDataList
        vplmnCSGSubscriptionDataList = null;
        // additionalMSISDN
        additionalMSISDN = null;
        // psAndSMSOnlyServiceProvision
        psAndSMSOnlyServiceProvision = true;
        // adjacentAccessRestrictionDataList
        adjacentAccessRestrictionDataList = null;
        // imsiGroupIdList
        imsiGroupIdList = null;
        // ueUsageType
        ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x87});
        // resetIdList does not apply for subscriptionData inside MAP RST
        // eDRXCycleLengthList
        usedRATType = UsedRATType.geran;
        edrCycleLengthVal = new byte[] { 0x02 };
        eDRXCycleLengthValue = new EDRXCycleLengthValueImpl(edrCycleLengthVal);
        edrxCycleLength = new EDRXCycleLengthImpl(usedRATType, eDRXCycleLengthValue);
        eDRXCycleLengthList = new ArrayList<>();
        eDRXCycleLengthList.add(edrxCycleLength);
        // extAccessRestrictionData
        extAccessRestrictionData = null;
        // iabOperationAllowedIndicator
        iabOperationAllowedIndicator = false;
        subscriptionData = new InsertSubscriberDataArgsImpl(imsi, null, category, subscriberStatus, bearerServiceList, teleserviceList,
                provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, naeaPreferredCI, gprsSubscriptionData,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator, lcsInformation,
                istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority, sgsnCamelSubscriptionInfo,
                chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData, csgSubscriptionDataList,
                ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent,
                subscribedPeriodicLAUtimer, vplmnCSGSubscriptionDataList, additionalMSISDN, psAndSMSOnlyServiceProvision, smsInSGSNAllowed,
                csToPsSRVCCAllowedIndicator, pcscfRestorationRequest, adjacentAccessRestrictionDataList, imsiGroupIdList, ueUsageType,
                userPlaneIntegrityProtectionIndicator, dlBufferingSuggestedPacketCount, null, eDRXCycleLengthList,
                extAccessRestrictionData, iabOperationAllowedIndicator);

        prim = new ResetRequestImpl(sendingNodeNumber, null, null, resetIdList, subscriptionData, null);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18wSubsDataToSgsn();

        assertTrue(Arrays.equals(rawData, encodedData));


        // test 6, 3GPP Release v18.0.0 with resetIdList and subscriptionDataDeletion
        // Wireshark sample
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: css-Number (1)
         *             css-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 82
         *             Reset-Id: 01020482
         *         subscriptionDataDeletion
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             basicServiceList: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: padAccessCA-1200-75bps (35)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: allTeleservices (0)
         *             ss-List: 1 item
         *                 SS-Code: plmn-specificSS-2 (242)
         *             regionalSubscriptionIdentifier: 210f
         *             vbsGroupIndication
         *             vgcsGroupIndication
         *             camelSubscriptionInfoWithdraw
         *             roamingRestrictedInSgsnDueToUnsuppportedFeature
         *             lsaInformationWithdraw: lsaIdentityList (1)
         *                 lsaIdentityList: 2 items
         *                     LSAIdentity: 0c0a01
         *                     LSAIdentity: 0c0c02
         *             gmlc-ListWithdraw
         *             istInformationWithdraw
         *             Padding: 2
         *             specificCSI-Withdraw: 9000
         *                 1... .... = o-csi: True
         *                 .0.. .... = ss-csi: False
         *                 ..0. .... = tif-csi: False
         *                 ...1 .... = d-csi: True
         *                 .... 0... = vt-csi: False
         *                 .... .0.. = mo-sms-csi: False
         *                 .... ..0. = m-csi: False
         *                 .... ...0 = gprs-csi: False
         *                 0... .... = t-csi: False
         *                 .0.. .... = mt-sms-csi: False
         *                 ..0. .... = mg-csi: False
         *                 ...0 .... = o-IM-CSI: False
         *                 .... 0... = d-IM-CSI: False
         *                 .... .0.. = vt-IM-CSI: False
         *             chargingCharacteristicsWithdraw
         *             stn-srWithdraw
         *             apn-oi-replacementWithdraw
         *             csg-SubscriptionDeleted
         *             subscribedPeriodicTAU-RAU-TimerWithdraw
         *             subscribedPeriodicLAU-TimerWithdraw
         *             subscribed-vsrvccWithdraw
         *             vplmn-Csg-SubscriptionDeleted
         *             additionalMSISDN-Withdraw
         *             cs-to-ps-SRVCC-Withdraw
         *             imsiGroupIdList-Withdraw
         *             userPlaneIntegrityProtectionWithdraw
         *             dl-Buffering-Suggested-Packet-Count-Withdraw
         *             ue-UsageTypeWithdraw
         *             reset-idsWithdraw
         *             iab-OperationWithdraw
         */
        ISDNAddressString cssNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        sendingNodenumber = new SendingNodeNumberImpl(null, cssNumber);
        sendingNodenumber.getCssNumber();
        resetIdList = new ArrayList<>();
        resetId1 = new ResetIdImpl(new byte[] {(byte) 0x82});
        resetId2 = new ResetIdImpl(new byte[] {0x01, 0x02, 0x04, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // imsi
        imsi = new IMSIImpl("748026800000000");
        // basicServiceList
        basicServiceList = new ArrayList<>();
        bearerServiceCodeValue1 = BearerServiceCodeValue.padAccessCA_1200_75bps;
        extBearerServiceCode1 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue1);
        ExtBasicServiceCode extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode1);
        teleserviceCodeValue2 = TeleserviceCodeValue.allTeleservices;
        extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        ExtBasicServiceCode extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        // ssList
        ArrayList<SSCode> ssList = new ArrayList<>();
        SupplementaryCodeValue supplementaryCodeValue = SupplementaryCodeValue.plmn_specificSS_2;
        ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = false;
        // vbsGroupIndication
        boolean vbsGroupIndication = true;
        // vgcsGroupIndication
        boolean vgcsGroupIndication = true;
        // camelSubscriptionInfoWithdraw
        boolean camelSubscriptionInfoWithdraw = true;
        // gprsSubscriptionDataWithdraw
        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = null; // doesn't apply for CS domain
        boolean roamingRestrictedInSgsnDueToUnsuppportedFeature = true;
        // lsaInformationWithdraw
        ArrayList<LSAIdentity> lsaIdentityList = new ArrayList<>();
        LSAIdentity lsaIdentity1 = new LSAIdentityImpl(new byte[]{12, 10, 1});
        LSAIdentity lsaIdentity2 = new LSAIdentityImpl(new byte[]{12, 12, 2});
        lsaIdentityList.add(lsaIdentity1);
        lsaIdentityList.add(lsaIdentity2);
        LSAInformationWithdraw lsaInformationWithdraw = new LSAInformationWithdrawImpl(lsaIdentityList);
        // regionalSubscriptionIdentifier
        ZoneCode regionalSubscriptionIdentifier = new ZoneCodeImpl(new byte[] {0x21, 0x0F});
        // gmlcListWithdraw
        boolean gmlcListWithdraw = true;
        // istInformationWithdraw
        boolean istInformationWithdraw = true;
        // specificCSIWithdraw
        SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false,
                false, false, false, false);
        // chargingCharacteristicsWithdraw
        boolean chargingCharacteristicsWithdraw = true;
        // stnSrWithdraw
        boolean stnSrWithdraw = true;
        // epsSubscriptionDataWithdraw
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = null;
        // apnOiReplacementWithdraw
        boolean apnOiReplacementWithdraw = true;
        // csgSubscriptionDeleted
        boolean csgSubscriptionDeleted = true;
        // subscribedPeriodicTAURAUTimerWithdraw
        boolean subscribedPeriodicTAURAUTimerWithdraw = true;
        // subscribedPeriodicLAUTimerWithdraw
        boolean subscribedPeriodicLAUTimerWithdraw = true;
        // subscribedVsrvccWithdraw
        boolean subscribedVsrvccWithdraw = true;
        // vplmnCsgSubscriptionDeleted
        boolean vplmnCsgSubscriptionDeleted = true;
        // additionalMSISDNWithdraw
        boolean additionalMSISDNWithdraw = true;
        // csToPsSRVCCWithdraw
        boolean csToPsSRVCCWithdraw = true;
        // imsiGroupIdListWithdraw
        boolean imsiGroupIdListWithdraw = true;
        // userPlaneIntegrityProtectionWithdraw
        boolean userPlaneIntegrityProtectionWithdraw = true;
        // dlBufferingSuggestedPacketCountWithdraw
        boolean dlBufferingSuggestedPacketCountWithdraw = true;
        // ueUsageTypeWithdraw
        boolean ueUsageTypeWithdraw = true;
        // resetIdsWithdraw
        boolean resetIdsWithdraw = true;
        // iabOperationWithdraw
        boolean iabOperationWithdraw = true;
        DeleteSubscriberDataArgs subscriptionDataDeletion = new DeleteSubscriberDataArgsImpl(imsi, basicServiceList, ssList,
                roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication,
                camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature,
                lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw,
                epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted, subscribedPeriodicTAURAUTimerWithdraw,
                subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted, additionalMSISDNWithdraw, csToPsSRVCCWithdraw,
                imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw, dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw,
                iabOperationWithdraw);
        prim = new ResetRequestImpl(new SendingNodeNumberImpl(null, cssNumber), null, null, resetIdList, null, subscriptionDataDeletion);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18wSubsDataDelToVLR();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 7, 3GPP Release v18.0.0 with resetIdList and subscriptionData (containing msisdn, LCSInformation and resetIdList)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: hlr-Number (0)
         *             hlr-Number: 91947101940050
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490005
         *         reset-Id-List: 2 items
         *             Reset-Id: 81
         *             Reset-Id: 01020482
         *         subscriptionData
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             msisdn: 919598097739f7
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 59899077937
         *             category: 0a
         *             subscriberStatus: serviceGranted (0)
         *             teleserviceList: 2 items
         *                 Ext-TeleserviceCode: shortMessageMT-PP (33)
         *                 Ext-TeleserviceCode: shortMessageMO-PP (34)
         *             provisionedSS: 2 items
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: cfnry - call forwarding on no reply (42)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: overrideCategory (1)
         *                             overrideCategory: overrideDisabled (1)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                 Ext-SS-Info: ss-Data (3)
         *                     ss-Data
         *                         ss-Code: hold - call hold (66)
         *                         ss-Status: 05
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         ss-SubscriptionOption: cliRestrictionOption (2)
         *                             cliRestrictionOption: temporaryDefaultAllowed (2)
         *                         basicServiceGroupList: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *             odb-Data
         *                 Padding: 3
         *                 odb-GeneralData: efff1ce8
         *                     1... .... = allOG-CallsBarred: True
         *                     .1.. .... = internationalOGCallsBarred: True
         *                     ..1. .... = internationalOGCallsNotToHPLMN-CountryBarred: True
         *                     ...0 .... = premiumRateInformationOGCallsBarred: False
         *                     .... 1... = premiumRateEntertainementOGCallsBarred: True
         *                     .... .1.. = ss-AccessBarred: True
         *                     .... ..1. = interzonalOGCallsBarred: True
         *                     .... ...1 = interzonalOGCallsNotToHPLMN-CountryBarred: True
         *                     1... .... = interzonalOGCallsAndInternationalOGCallsNotToHPLMN-CountryBarred: True
         *                     .1.. .... = allECT-Barred: True
         *                     ..1. .... = chargeableECT-Barred: True
         *                     ...1 .... = internationalECT-Barred: True
         *                     .... 1... = interzonalECT-Barred: True
         *                     .... .1.. = doublyChargeableECT-Barred: True
         *                     .... ..1. = multipleECT-Barred: True
         *                     .... ...1 = allPacketOrientedServicesBarred: True
         *                     0... .... = roamerAccessToHPLMN-AP-Barred: False
         *                     .0.. .... = roamerAccessToVPLMN-AP-Barred: False
         *                     ..0. .... = roamingOutsidePLMNOG-CallsBarred: False
         *                     ...1 .... = allIC-CallsBarred: True
         *                     .... 1... = roamingOutsidePLMNIC-CallsBarred: True
         *                     .... .1.. = roamingOutsidePLMNICountryIC-CallsBarred: True
         *                     .... ..0. = roamingOutsidePLMN-Barred: False
         *                     .... ...0 = roamingOutsidePLMN-CountryBarred: False
         *                     1... .... = registrationAllCF-Barred: True
         *                     .1.. .... = registrationCFNotToHPLMN-Barred: True
         *                     ..1. .... = registrationInterzonalCF-Barred: True
         *                     ...0 .... = registrationInterzonalCFNotToHPLMN-Barred: False
         *                     .... 1... = registrationInternationalCF-Barred: True
         *                 Padding: 4
         *                 odb-HPLMN-Data: 80
         *                     1... .... = plmn-SpecificBarringType1: True
         *                     .0.. .... = plmn-SpecificBarringType2: False
         *                     ..0. .... = plmn-SpecificBarringType3: False
         *                     ...0 .... = plmn-SpecificBarringType4: False
         *             roamingRestrictionDueToUnsupportedFeature
         *             vbsSubscriptionData: 1 item
         *                 VoiceBroadcastData
         *                     groupid: ffffff
         *                         TBCD digits:
         *                     broadcastInitEntitlement
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vgcsSubscriptionData: 1 item
         *                 VoiceGroupCallData
         *                     groupId: ffffff
         *                         TBCD digits:
         *                     Padding: 5
         *                     additionalSubscriptions: e0
         *                         1... .... = privilegedUplinkRequest: True
         *                         .1.. .... = emergencyUplinkRequest: True
         *                         ..1. .... = emergencyReset: True
         *                     Padding: 0
         *                     additionalInfo: c000008000000000000000000000000000
         *                     longGroupId: f5ffffff
         *                         TBCD digits: 5
         *             vlrCamelSubscriptionInfo
         *                 o-CSI
         *                     o-BcsmCamelTDPDataList: 1 item
         *                         O-BcsmCamelTDPData
         *                             o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csiActive
         *                 ss-CSI
         *                     ss-CamelData
         *                         ss-EventList: 2 items
         *                             SS-Code: cfnry - call forwarding on no reply (42)
         *                             SS-Code: hold - call hold (66)
         *                         gsmSCF-Address: 91947101640092
         *                             1... .... = Extension: No Extension
         *                             .001 .... = Nature of number: International Number (0x1)
         *                             .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                             E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 o-BcsmCamelTDP-CriteriaList: 1 item
         *                     O-BcsmCamelTDP-Criteria
         *                         o-BcsmTriggerDetectionPoint: routeSelectFailure (4)
         *                         destinationNumberCriteria
         *                             matchType: enabling (1)
         *                             destinationNumberList: 1 item
         *                                 ISDN-AddressString: 91947141874023
         *                                     1... .... = Extension: No Extension
         *                                     .001 .... = Nature of number: International Number (0x1)
         *                                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                     E.164 number (MSISDN): 491714780432
         *                             destinationNumberLengthList: 1 item
         *                                 DestinationNumberLengthList item: 1
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                         callTypeCriteria: notForwarded (1)
         *                         o-CauseValueCriteria: 2 items
         *                             CauseValue: 51
         *                             CauseValue: 39
         *                 tif-CSI
         *                 m-CSI
         *                     mobilityTriggers: 2 items
         *                         MM-Code: 02
         *                         MM-Code: 00
         *                     serviceKey: 7
         *                     gsmSCF-Address: 91947101640092
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 491710460029
         *                     notificationToCSE
         *                     csi-Active
         *                 mo-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 vt-CSI
         *                     t-BcsmCamelTDPDataList: 2 items
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tNoAnswer (14)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                         T-BcsmCamelTDPData
         *                             t-BcsmTriggerDetectionPoint: tBusy (13)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 t-BCSM-CAMEL-TDP-CriteriaList: 1 item
         *                     T-BCSM-CAMEL-TDP-Criteria
         *                         t-BCSM-TriggerDetectionPoint: tNoAnswer (14)
         *                         basicServiceCriteria: 2 items
         *                             Ext-BasicServiceCode: ext-BearerService (2)
         *                                 ext-BearerService: allBearerServices (0)
         *                             Ext-BasicServiceCode: ext-Teleservice (3)
         *                                 ext-Teleservice: facsimileGroup4 (99)
         *                         t-CauseValueCriteria: 2 items
         *                             CauseValue: 15
         *                             CauseValue: 39
         *                 d-CSI
         *                     dp-AnalysedInfoCriteriaList: 1 item
         *                         DP-AnalysedInfoCriterium
         *                             dialledNumber: 91947141874023
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491714780432
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultCallHandling: continueCall (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-sms-CSI
         *                     sms-CAMEL-TDP-DataList: 1 item
         *                         SMS-CAMEL-TDP-Data
         *                             sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                             serviceKey: 7
         *                             gsmSCF-Address: 91947101640092
         *                                 1... .... = Extension: No Extension
         *                                 .001 .... = Nature of number: International Number (0x1)
         *                                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                                 E.164 number (MSISDN): 491710460029
         *                             defaultSMS-Handling: continueTransaction (0)
         *                     camelCapabilityHandling: 2
         *                     notificationToCSE
         *                     csi-Active
         *                 mt-smsCAMELTDP-CriteriaList: 1 item
         *                     MT-smsCAMELTDP-Criteria
         *                         sms-TriggerDetectionPoint: sms-DeliveryRequest (2)
         *                         tpdu-TypeCriterion: 3 items
         *                             MT-SMS-TPDU-Type: sms-DELIVER (0)
         *                             MT-SMS-TPDU-Type: sms-SUBMIT-REPORT (1)
         *                             MT-SMS-TPDU-Type: sms-STATUS-REPORT (2)
         *             naea-PreferredCI
         *                 naea-PreferredCIC: 235408
         *             networkAccessMode: packetAndCircuit (0)
         *             lcsInformation
         *                 gmlc-List: 1 item
         *                     ISDN-AddressString: 91947101640023f1
         *                         1... .... = Extension: No Extension
         *                         .001 .... = Nature of number: International Number (0x1)
         *                         .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                         E.164 number (MSISDN): 4917104600321
         *                 lcs-PrivacyExceptionList: 4 items
         *                     LCS-PrivacyClass
         *                         ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
         *                         ss-Status: 08
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: notifyLocationAllowed (0)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         plmnClientList: 2 items
         *                             LCSClientInternalID: broadcastService (0)
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: emergencyAlertServices (1)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                     LCS-PrivacyClass
         *                         ss-Code: autonomousSelfLocation - allow an MS to perform self location without interaction with the PLMN for a predetermined period of time (194)
         *                         ss-Status: 04
         *                         0000 .... = Unused: 0x0
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f93289722f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 398279222
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f93289722f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 398279222
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: personTracking (2)
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationAllowedIfNoResponse (1)
         *                     LCS-PrivacyClass
         *                         ss-Code: allLCSPrivacyException - all LCS Privacy Exception Classes (176)
         *                         ss-Status: 02
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..1. = R bit: Registered
         *                         .... ...0 = A bit: not Active
         *                         notificationToMSUser: locationNotAllowed (3)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f3245232532f4
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 23543252234
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: targetMSsubscribedService (4)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f3245232532f4
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 23543252234
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: fleetManagement (3)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: locationNotAllowed (3)
         *                     LCS-PrivacyClass
         *                         ss-Code: allPLMN-specificSS (240)
         *                         ss-Status: 01
         *                         0000 .... = Unused: 0x0
         *                         .... 0... = Q bit: Operative
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f959809922854
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 598990298245
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         plmnClientList: 1 item
         *                             LCSClientInternalID: anonymousLocation (3)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f959809922854
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 598990298245
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: assetManagement (4)
         *                                 gmlc-Restriction: gmlc-List (0)
         *                                 notificationToMSUser: notifyAndVerify-LocationNotAllowedIfNoResponse (2)
         *                 molr-List: 3 items
         *                     MOLR-Class
         *                         ss-Code: allMOLR-SS - all Mobile Originating Location Request Classes (192)
         *                         ss-Status: 08
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                     MOLR-Class
         *                         ss-Code: autonomousSelfLocation - allow an MS to perform self location without interaction with the PLMN for a predetermined period of time (194)
         *                         ss-Status: 04
         *                         0000 .... = Unused: 0x0
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...0 = A bit: not Active
         *                     MOLR-Class
         *                         ss-Code: allLCSPrivacyException - all LCS Privacy Exception Classes (176)
         *                         ss-Status: 02
         *                         0000 .... = Unused: 0x0
         *                         .... .0.. = P bit: Not provisioned
         *                         .... ..1. = R bit: Registered
         *                         .... ...0 = A bit: not Active
         *                 add-lcs-PrivacyExceptionList: 1 item
         *                     LCS-PrivacyClass
         *                         ss-Code: plmn-specificSS-1 (241)
         *                         ss-Status: 0d
         *                         0000 .... = Unused: 0x0
         *                         .... 1... = Q bit: Quiescent
         *                         .... .1.. = P bit: Provisioned
         *                         .... ..0. = R bit: Not registered
         *                         .... ...1 = A bit: Active
         *                         notificationToMSUser: notifyLocationAllowed (0)
         *                         externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         plmnClientList: 4 items
         *                             LCSClientInternalID: broadcastService (0)
         *                             LCSClientInternalID: o-andM-HPLMN (1)
         *                             LCSClientInternalID: targetMSsubscribedService (4)
         *                             LCSClientInternalID: anonymousLocation (3)
         *                         ext-externalClientList: 1 item
         *                             ExternalClient
         *                                 clientIdentity
         *                                     externalAddress: 9f78947294f2
         *                                         1... .... = Extension: No Extension
         *                                         .001 .... = Nature of number: International Number (0x1)
         *                                         .... 1111 = Number plan: Reserved for extension (0xf)
         *                                         Address digits: 874927492
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *                         serviceTypeList: 1 item
         *                             ServiceType
         *                                 serviceTypeIdentity: trafficCongestionReporting (5)
         *                                 gmlc-Restriction: home-Country (1)
         *                                 notificationToMSUser: notifyLocationAllowed (0)
         *             istAlertTimer: 200
         *             mc-SS-Info
         *                 ss-Code: cfu - call forwarding unconditional (33)
         *                 ss-Status: 0a
         *                 0000 .... = Unused: 0x0
         *                 .... .0.. = P bit: Not provisioned
         *                 .... ..1. = R bit: Registered
         *                 .... ...0 = A bit: not Active
         *                 nbrSB: 2
         *                 nbrUser: 4
         *             cs-AllocationRetentionPriority: 04
         *             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *             Padding: 2
         *             accessRestrictionData: 24
         *                 0... .... = utranNotAllowed: False
         *                 .0.. .... = geranNotAllowed: False
         *                 ..1. .... = ganNotAllowed: True
         *                 ...0 .... = i-hspa-evolutionNotAllowed: False
         *                 .... 0... = wb-e-utranNotAllowed: False
         *                 .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                 .... ..0. = nb-iotNotAllowed: False
         *                 .... ...0 = enhancedCoverageNotAllowed: False
         *             ics-Indicator: True
         *             eps-SubscriptionData
         *                 apn-oi-Replacement: 515c53545556575859
         *                 rfsp-id: 0
         *                 ambr
         *                     max-RequestedBandwidth-UL: 2048
         *                     max-RequestedBandwidth-DL: 4096
         *                 apn-ConfigurationProfile
         *                     defaultContext: 1
         *                     completeDataListIncluded
         *                     epsDataList: 1 item
         *                         APN-Configuration
         *                             contextId: 1
         *                             pdn-Type: 03
         *                             apn: 08696e7465726e6574 - internet
         *                                 APN: internet
         *                             eps-qos-Subscribed
         *                                 qos-Class-Identifier: 5
         *                                 allocation-Retention-Priority
         *                                     priority-level: 9
         *                                     pre-emption-capability: True
         *                                     pre-emption-vulnerability: False
         *                             vplmnAddressAllowed
         *                             .... 0010 .... .... = chargingCharacteristics: F (Flat rate) (2)
         *                             ambr
         *                                 max-RequestedBandwidth-UL: 2048
         *                                 max-RequestedBandwidth-DL: 4096
         *                             servedPartyIP-IPv6-Address: 15
         *                             apn-oi-Replacement: 515c53545556575859
         *                             sipto-Permission: siptoAboveRanAllowed (0)
         *                             lipa-Permission: lipaConditional (2)
         *                 stn-sr: 91947101940000
         *                     1... .... = Extension: No Extension
         *                     .001 .... = Nature of number: International Number (0x1)
         *                     .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                     E.164 number (MSISDN): 491710490000
         *                 mps-CSPriority
         *                 mps-EPSPriority
         *             csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             ue-ReachabilityRequestIndicator
         *             mme-Name: mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org
         *             subscribedPeriodicRAUTAUtimer: 300
         *             vplmnLIPAAllowed
         *             mdtUserConsent: False
         *             subscribedPeriodicLAUtimer: 360
         *             vplmn-Csg-SubscriptionDataList: 1 item
         *                 CSG-SubscriptionData
         *                     Padding: 5
         *                     csg-Id: c0000060
         *                     expirationDate: ea31746a
         *                     lipa-AllowedAPNList: 1 item
         *                         APN: 08696e7465726e6574 - internet
         *                             APN: internet
         *             additionalMSISDN: 91947101652854f1
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 4917105682451
         *             smsInSGSNAllowed
         *             cs-to-ps-SRVCC-Allowed-Indicator
         *             pcscf-Restoration-Request
         *             adjacentAccessRestrictionDataList: 1 item
         *                 AdjacentAccessRestrictionData
         *                     plmnId: 629299
         *                     Padding: 2
         *                     accessRestrictionData: 24
         *                         0... .... = utranNotAllowed: False
         *                         .0.. .... = geranNotAllowed: False
         *                         ..1. .... = ganNotAllowed: True
         *                         ...0 .... = i-hspa-evolutionNotAllowed: False
         *                         .... 0... = wb-e-utranNotAllowed: False
         *                         .... .1.. = ho-toNon3GPP-AccessNotAllowed: True
         *                         .... ..0. = nb-iotNotAllowed: False
         *                         .... ...0 = enhancedCoverageNotAllowed: False
         *                     Padding: 0
         *                     ext-AccessRestrictionData: 80000000
         *                         1... .... = nrAsSecondaryRATNotAllowed: True
         *                         .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             imsi-Group-Id-List: 1 item
         *                 IMSI-GroupId
         *                     group-Service-Id: 1
         *                     plmnId: 629299
         *                     local-Group-ID: 313230
         *             ueUsageType: 00000087
         *             userPlaneIntegrityProtectionIndicator
         *             dl-Buffering-Suggested-Packet-Count: 0
         *             reset-Id-List: 2 items
         *                 Reset-Id: 81
         *                 Reset-Id: 01020482
         *             eDRX-Cycle-Length-List: 1 item
         *                 EDRX-Cycle-Length
         *                     rat-Type: nb-iot (5)
         *                     eDRX-Cycle-Length-Value: 02
         *             Padding: 0
         *             ext-AccessRestrictionData: 80000000
         *                 1... .... = nrAsSecondaryRATNotAllowed: True
         *                 .0.. .... = unlicensedSpectrumAsSecondaryRATNotAllowed: False
         *             iab-Operation-Allowed-Indicator
         */
        hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490005");
        sendingNodenumber = new SendingNodeNumberImpl(hlrNumber, null);
        sendingNodenumber.getHlrNumber();
        resetIdList = new ArrayList<>();
        resetId1 = new ResetIdImpl(new byte[] {(byte) 0x81});
        resetId2 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // imsi
        imsi = new IMSIImpl("748026800000000");
        // msisdn
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        // category
        category = new CategoryImpl(CategoryValue.ordinaryCallingSubscriber);
        // subscriberStatus (subscriberStatus = SubscriberStatus.serviceGranted;)
        // bearerServiceList (bearerServiceList = null;)
        // teleserviceList
        teleserviceList = new ArrayList<>();
        shortMessageMT_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        shortMessageMO_PP = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMO_PP);
        teleserviceList.add(shortMessageMT_PP);
        teleserviceList.add(shortMessageMO_PP);
        // provisionedSS
        provisionedSS = new ArrayList<>();
        ssCode1 = new SSCodeImpl(SupplementaryCodeValue.cfnry);
        ssCode1ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        ssCode1SubscriptionOption = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        basicServiceList = new ArrayList<>();
        bearerServiceCodeValue3 = BearerServiceCodeValue.allBearerServices;
        extBearerServiceCode3 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue3);
        extBasicServiceCode3 = new ExtBasicServiceCodeImpl(extBearerServiceCode3);
        teleserviceCodeValue2 = TeleserviceCodeValue.facsimileGroup4;
        extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        extBasicServiceCode4 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode3);
        basicServiceList.add(extBasicServiceCode4);
        extSSData1 = new ExtSSDataImpl(ssCode1, ssCode1ExtSSStatus, ssCode1SubscriptionOption, basicServiceList, null);
        ssCode2 = new SSCodeImpl(SupplementaryCodeValue.hold);
        ssCode2ExtSSStatus = new ExtSSStatusImpl(false, true, false, true);
        ssCode2SubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.temporaryDefaultAllowed);
        extSSData2 = new ExtSSDataImpl(ssCode2, ssCode2ExtSSStatus, ssCode2SubscriptionOption, basicServiceList, null);
        extSSInfo1= new ExtSSInfoImpl(extSSData1);
        extSSInfo2 = new ExtSSInfoImpl(extSSData2);
        provisionedSS.add(extSSInfo1);
        provisionedSS.add(extSSInfo2);
        // odbData
        allOGCallsBarred= true;
        internationalOGCallsBarred = true;
        internationalOGCallsNotToHPLMNCountryBarred= true;
        premiumRateInformationOGCallsBarred = false;
        interzonalOGCallsBarred = true;
        interzonalOGCallsNotToHPLMNCountryBarred= true;
        interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred = true;
        allECTBarred= true;
        chargeableECTBarred= true;
        internationalECTBarred = true;
        interzonalECTBarred= true;
        allPacketOrientedServicesBarred= true;
        roamerAccessToVPLMNAPBarred = false;
        allICCallsBarred= true;
        roamingOutsidePLMNICountryICCallsBarred= true;
        registrationInterzonalCFNotToHPLMNBarred = false;
        oDBGeneralData = new ODBGeneralDataImpl(allOGCallsBarred, internationalOGCallsBarred,
                internationalOGCallsNotToHPLMNCountryBarred, premiumRateInformationOGCallsBarred, premiumRateEntertainmentOGCallsBarred,
                ssAccessBarred, interzonalOGCallsBarred, interzonalOGCallsNotToHPLMNCountryBarred,
                interzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred, allECTBarred, chargeableECTBarred,
                internationalECTBarred, interzonalECTBarred, doublyChargeableECTBarred, multipleECTBarred,
                allPacketOrientedServicesBarred, roamerAccessToHPLMNAPBarred, roamerAccessToVPLMNAPBarred,
                roamingOutsidePLMNOGCallsBarred, allICCallsBarred, roamingOutsidePLMNICCallsBarred,
                roamingOutsidePLMNICountryICCallsBarred, roamingOutsidePLMNBarred,
                roamingOutsidePLMNCountryBarred, registrationAllCFBarred, registrationCFNotToHPLMNBarred,
                registrationInterzonalCFBarred, registrationInterzonalCFNotToHPLMNBarred, registrationInternationalCFBarred);
        plmnSpecificBarringType1 = true;
        plmnSpecificBarringType2 = false;
        plmnSpecificBarringType4 = false;
        odbHplmnData = new ODBHPLMNDataImpl(plmnSpecificBarringType1, plmnSpecificBarringType2, plmnSpecificBarringType3, plmnSpecificBarringType4);
        odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = true;
        // regionalSubscriptionData (regionalSubscriptionData = null;)
        // vbsSubscriptionData
        vbsSubscriptionData = new ArrayList<>();
        gId = new GroupIdImpl("");
        lGId = new LongGroupIdImpl("5");
        voiceBroadcastData = new VoiceBroadcastDataImpl(gId, broadcastInitEntitlement, null, lGId);
        vbsSubscriptionData.add(voiceBroadcastData);
        // vgcsSubscriptionData
        vgcsSubscriptionData = new ArrayList<>();
        addSubscriptions = new AdditionalSubscriptionsImpl(privilegedUplinkRequest, emergencyUplinkRequest, emergencyReset);
        addInfoBitset = new BitSetStrictLength(136);
        addInfoBitset.set(0);
        addInfoBitset.set(1);
        addInfoBitset.set(24);
        addInfo = new AdditionalInfoImpl(addInfoBitset);
        voiceGroupCallData = new VoiceGroupCallDataImpl(gId, null, addSubscriptions, addInfo, lGId);
        vgcsSubscriptionData.add(voiceGroupCallData);
        // vlrCamelSubscriptionInfo
        gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710460029");
        oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(oBcsmTDP, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        oBcsmCamelTDPDataList = new ArrayList<>();
        oBcsmCamelTDPDataList.add(oBcsmCamelTDPData);
        camelCapabilityHandling = 2;
        oCSI = new OCSIImpl(oBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        ssEventList = new ArrayList<>();
        ssEventList.add(ssCode1);
        ssEventList.add(ssCode2);
        ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddress, null);
        ssCsi = new SSCSIImpl(ssCamelData, null, notificationToCSE, csiActive);
        destinationNumberList = new ArrayList<>();
        destinationNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        destinationNumberList.add(destinationNumber);
        destinationNumberLengthList = new ArrayList<>();
        destinationNumberLengthList.add(1);
        destinationNumberCriteria = new DestinationNumberCriteriaImpl(matchType, destinationNumberList, destinationNumberLengthList);
        oCauseValueCriteria = new ArrayList<>();
        causeValue1 = new CauseValueImpl(CauseValueCodeValue.InvalidCallReferenceValue);
        causeValue2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        oCauseValueCriteria.add(causeValue1);
        oCauseValueCriteria.add(causeValue2);
        oBcsmCamelTDPCriteriaList = new ArrayList<>();
        oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTDP, destinationNumberCriteria,
                basicServiceList, callTypeCriteria, oCauseValueCriteria, null);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);
        mobilityTriggers = new ArrayList<>();
        mmCode1 = new MMCodeImpl(MMCodeValue.IMSIAttach);
        mmCode2 = new MMCodeImpl(MMCodeValue.LocationUpdateInSameVLR);
        mobilityTriggers.add(mmCode1);
        mobilityTriggers.add(mmCode2);
        mcsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, null, notificationToCSE, csiActive);
        smsCamelTdpDataList = new ArrayList<>();
        smscameltdpData = new SMSCAMELTDPDataImpl(smsTDP, serviceKey, gsmSCFAddress, defaultSMSHandling, null);
        smsCamelTdpDataList.add(smscameltdpData);
        smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        tBcsmCamelTDPDataList = new ArrayList<>();
        tBcsmCamelTDPData1 = new TBcsmCamelTDPDataImpl(tBcsmTDP1, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        tBcsmCamelTDPData2 = new TBcsmCamelTDPDataImpl(tBcsmTDP2, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData1);
        tBcsmCamelTDPDataList.add(tBcsmCamelTDPData2);
        vtCsi = new TCSIImpl(tBcsmCamelTDPDataList, null, camelCapabilityHandling, notificationToCSE, csiActive);
        tCauseValueCriteria = new ArrayList<>();
        tcv1 = new CauseValueImpl(CauseValueCodeValue.CallRejected);
        tcv2 = new CauseValueImpl(CauseValueCodeValue.BearerCapabilityNotAuthorized);
        tCauseValueCriteria.add(tcv1);
        tCauseValueCriteria.add(tcv2);
        tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint, basicServiceList, tCauseValueCriteria);
        tBcsmCamelTdpCriteriaList = new ArrayList<>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);
        dpAnalysedInfoCriteriaList = new ArrayList<>();
        dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491714780432");
        dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, serviceKey, gsmSCFAddress, defaultCallHandling, null);
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);
        dCSI = new DCSIImpl(dpAnalysedInfoCriteriaList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, null, notificationToCSE, csiActive);
        mtSmsCamelTdpCriteriaList = new ArrayList<>();
        mtsmstpduTypeArrayList = new ArrayList<>();
        mtsmstpduTypeArrayList.add(mtsmstpduType1);
        mtsmstpduTypeArrayList.add(mtsmstpduType2);
        mtsmstpduTypeArrayList.add(mtsmstpduType3);
        mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTDP, mtsmstpduTypeArrayList);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);
        vlrCamelSubscriptionInfo = new VlrCamelSubscriptionInfoImpl(oCSI, null,
                ssCsi, oBcsmCamelTDPCriteriaList, tifCsi, mcsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCSI, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);
        // naeaPreferredCI
        carrierCode = "458";
        naeaPreferredCIC = new NAEACICImpl(carrierCode, networkIdentificationPlanValue, networkIdentificationTypeValue);
        naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC, null);
        // gprsSubscriptionData does not apply for CS domain
        // roamingRestrictedInSgsnDueToUnsupportedFeature (roamingRestrictedInSgsnDueToUnsupportedFeature = false;)
        // networkAccessMode ( networkAccessMode = NetworkAccessMode.packetAndCircuit;)
        // lsaInformation (lsaInformation = null;)
        // lmuIndicator (lmuIndicator = false;)
        // lcsInformation
        ArrayList<ISDNAddressString> gmlcList = new ArrayList<>();
        ISDNAddressString gmlcAddress =
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917104600321");
        gmlcList.add(gmlcAddress);
        // lcsInformation (lcsPrivacyExceptionList)
        ssCode1 = new SSCodeImpl(SupplementaryCodeValue.allMOLR_SS);
        ssCode2 = new SSCodeImpl(SupplementaryCodeValue.autonomousSelfLocation);
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
        lcsInformation = new LCSInformationImpl(gmlcList, lcsPrivacyExceptionList, molrList, addLcsPrivacyExceptionList);
        // istAlertTimer (istAlertTimer = 200;)
        // superChargerSupportedInHLR
        superChargerSupportedInHLR = null;
        // mcSsInfo
        ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        ssStatus = new ExtSSStatusImpl(true, false, true, false);
        mcSsInfo = new MCSSInfoImpl(ssCode, ssStatus, nbrSB, nbrUser, null);
        // csAllocationRetentionPriority
        csAllocationRetentionPriority = new CSAllocationRetentionPriorityImpl(4);
        // sgsnCamelSubscriptionInfo
        sgsnCamelSubscriptionInfo = null;
        // chargingCharacteristics
        chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
        // accessRestrictionData
        accessRestrictionData = new AccessRestrictionDataImpl(utranNotAllowed, geranNotAllowed, ganNotAllowed, iHspaEvolutionNotAllowed, eUtranNotAllowed, hoToNon3GppAccessNotAllowed);
        // icsIndicator
        icsIndicator = Boolean.TRUE;
        // epsSubscriptionData
        pDNType = new PDNTypeImpl(PDNTypeValue.IPv4v6);
        apn = new APNImpl("internet");
        arp = new AllocationRetentionPriorityImpl(priorityLevel, preEmptionCapability, preEmptionVulnerability, null);
        ePSQoSSubscribed = new EPSQoSSubscribedImpl(qci, arp, null);
        ambr = new AMBRImpl(maxRequestedBandwidthUL, maxRequestedBandwidthDL, null);
        apnOiReplacement = new APNOIReplacementImpl(new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 });
        servedPartyIPIPv6Address = new PDPAddressImpl(new byte[] { 21 });
        apnConfiguration = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn,
                ePSQoSSubscribed, pdnGwIdentity, pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr,
                specificAPNInfoList, null, servedPartyIPIPv6Address, apnOiReplacement, sipToPermission, lipaPermission);
        ePSDataList = new ArrayList<>();
        ePSDataList.add(apnConfiguration);
        apnConfigurationProfile = new APNConfigurationProfileImpl(defaultContext, completeDataListIncluded,
                ePSDataList, null);
        stnSr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        epsSubscriptionData = new EPSSubscriptionDataImpl(apnOiReplacement, rfspId, ambr, apnConfigurationProfile,
                stnSr, null, mpsCSPriority, mpsEPSPriority);
        // csgSubscriptionDataList
        csgIdBitSet = new BitSetStrictLength(27);
        csgIdBitSet.set(0);
        csgIdBitSet.set(1);
        csgIdBitSet.set(25);
        csgIdBitSet.set(26);
        csgId = new CSGIdImpl(csgIdBitSet);
        expirationDate = new TimeImpl(year, month, day, hour, minute, second);
        lipaAllowedAPNList = new ArrayList<>();
        apn = new APNImpl("internet");
        lipaAllowedAPNList.add(apn);
        csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, null, lipaAllowedAPNList);
        csgSubscriptionDataList = new ArrayList<>();
        csgSubscriptionDataList.add(csgSubscriptionData);
        // ueReachabilityRequestIndicator (ueReachabilityRequestIndicator = true;)
        // sgsnNumber
        sgsnNumber = null;
        // mmeName
        mmeName = new DiameterIdentityImpl("mmec20.mmegi800.epc.mnc001.mcc748.3gppnetwork.org".getBytes(StandardCharsets.UTF_8));
        // subscribedPeriodicRAUTAUtimer (subscribedPeriodicRAUTAUtimer = 300L;)
        // vplmnLIPAAllowed (vplmnLIPAAllowed = true;)
        // mdtUserConsent (mdtUserConsent = false;)
        // subscribedPeriodicLAUtimer
        subscribedPeriodicLAUtimer = 360L;
        // vplmnCSGSubscriptionDataList
        vplmnCSGSubscriptionDataList = new ArrayList<>();
        vplmnCSGSubscriptionDataList.add(csgSubscriptionData);
        // additionalMSISDN
        additionalMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "4917105682451");
        // psAndSMSOnlyServiceProvision
        psAndSMSOnlyServiceProvision = false;
        // smsInSGSNAllowed (smsInSGSNAllowed = true;)
        // csToPsSRVCCAllowedIndicator (csToPsSRVCCAllowedIndicator = true;)
        // pcscfRestorationRequest (pcscfRestorationRequest = true;)
        // adjacentAccessRestrictionDataList
        plmnId = new PlmnIdImpl(mcc, mnc);
        extAccessRestrictionData =
                new ExtAccessRestrictionDataImpl(nrAsSecondaryRATNotAllowed, unlicensedSpectrumAsSecondaryRATNotAllowed); // extAccessRestrictionData
        adjacentAccessRestrictionData =
                new AdjacentAccessRestrictionDataImpl(plmnId, accessRestrictionData, extAccessRestrictionData);
        adjacentAccessRestrictionDataList = new ArrayList<>();
        adjacentAccessRestrictionDataList.add(adjacentAccessRestrictionData);
        // imsiGroupIdList
        localGroupId = new LocalGroupIdImpl("120".getBytes(StandardCharsets.UTF_8));
        imsiGroupId = new IMSIGroupIdImpl(groupServiceId, plmnId, localGroupId);
        imsiGroupIdList = new ArrayList<>();
        imsiGroupIdList.add(imsiGroupId);
        // ueUsageType
        ueUsageType = new UEUsageTypeImpl(new byte[] {0, 0, 0, (byte) 0x87});
        // userPlaneIntegrityProtectionIndicator (userPlaneIntegrityProtectionIndicator = true;)
        // dlBufferingSuggestedPacketCount (dlBufferingSuggestedPacketCount = 0L;)
        // resetIdList
        resetIdList = new ArrayList<>();
        resetId1 = new ResetIdImpl(new byte[] {(byte) 0x81});
        resetId2 = new ResetIdImpl(new byte[] {1, 2, 4, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // eDRXCycleLengthList
        usedRATType = UsedRATType.nbIoT;
        edrCycleLengthVal = new byte[] { 0x02 };
        eDRXCycleLengthValue = new EDRXCycleLengthValueImpl(edrCycleLengthVal);
        edrxCycleLength = new EDRXCycleLengthImpl(usedRATType, eDRXCycleLengthValue);
        eDRXCycleLengthList = new ArrayList<>();
        eDRXCycleLengthList.add(edrxCycleLength);
        // iabOperationAllowedIndicator
        iabOperationAllowedIndicator = true;
        subscriptionData = new InsertSubscriberDataArgsImpl(imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList,
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

        prim = new ResetRequestImpl(new SendingNodeNumberImpl(hlrNumber, null), null, null, resetIdList, subscriptionData, null);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18wSubsDataToVLR2();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 8, 3GPP Release v18.0.0 with resetIdList and subscriptionDataDeletion (sent to SGSN)
        /*
         * Component: invoke (1)
         *     invoke
         *         invokeID: 0
         *         opCode: localValue (0)
         *             localValue: reset (37)
         *         sendingNodenumber: css-Number (1)
         *             css-Number: 91947101940000
         *                 1... .... = Extension: No Extension
         *                 .001 .... = Nature of number: International Number (0x1)
         *                 .... 0001 = Number plan: ISDN/Telephony Numbering (Rec ITU-T E.164) (0x1)
         *                 E.164 number (MSISDN): 491710490000
         *         reset-Id-List: 2 items
         *             Reset-Id: 82
         *             Reset-Id: 01020482
         *         subscriptionDataDeletion
         *             IMSI: 748026800000000
         *             [Association IMSI: 748026800000000]
         *             basicServiceList: 2 items
         *                 Ext-BasicServiceCode: ext-BearerService (2)
         *                     ext-BearerService: dataCDA-1200bps (18)
         *                 Ext-BasicServiceCode: ext-Teleservice (3)
         *                     ext-Teleservice: allTeleservices (0)
         *             ss-List: 1 item
         *                 SS-Code: baoc - barring of all outgoing calls (146)
         *             regionalSubscriptionIdentifier: 210f
         *             camelSubscriptionInfoWithdraw
         *             gprsSubscriptionDataWithdraw: contextIdList (1)
         *                 contextIdList: 2 items
         *                     ContextId: 1
         *                     ContextId: 2
         *             roamingRestrictedInSgsnDueToUnsuppportedFeature
         *             lsaInformationWithdraw: allLSAData (0)
         *                 allLSAData
         *             gmlc-ListWithdraw
         *             istInformationWithdraw
         *             chargingCharacteristicsWithdraw
         *             stn-srWithdraw
         *             apn-oi-replacementWithdraw
         *             csg-SubscriptionDeleted
         *             subscribedPeriodicTAU-RAU-TimerWithdraw
         *             subscribedPeriodicLAU-TimerWithdraw
         *             subscribed-vsrvccWithdraw
         *             vplmn-Csg-SubscriptionDeleted
         *             additionalMSISDN-Withdraw
         *             cs-to-ps-SRVCC-Withdraw
         *             imsiGroupIdList-Withdraw
         *             userPlaneIntegrityProtectionWithdraw
         *             dl-Buffering-Suggested-Packet-Count-Withdraw
         *             ue-UsageTypeWithdraw
         *             reset-idsWithdraw
         *             iab-OperationWithdraw
         */
        cssNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "491710490000");
        sendingNodenumber = new SendingNodeNumberImpl(null, cssNumber);
        sendingNodenumber.getCssNumber();
        resetIdList = new ArrayList<>();
        resetId1 = new ResetIdImpl(new byte[] {(byte) 0x82});
        resetId2 = new ResetIdImpl(new byte[] {0x01, 0x02, 0x04, (byte) 0x82});
        resetIdList.add(resetId1);
        resetIdList.add(resetId2);
        // imsi
        imsi = new IMSIImpl("748026800000000");
        // basicServiceList
        basicServiceList = new ArrayList<>();
        bearerServiceCodeValue1 = BearerServiceCodeValue.dataCDA_1200bps;
        extBearerServiceCode1 = new ExtBearerServiceCodeImpl(bearerServiceCodeValue1);
        extBasicServiceCode1 = new ExtBasicServiceCodeImpl(extBearerServiceCode1);
        teleserviceCodeValue2 = TeleserviceCodeValue.allTeleservices;
        extTeleserviceCode2 = new ExtTeleserviceCodeImpl(teleserviceCodeValue2);
        extBasicServiceCode2 = new ExtBasicServiceCodeImpl(extTeleserviceCode2);
        basicServiceList.add(extBasicServiceCode1);
        basicServiceList.add(extBasicServiceCode2);
        // ssList
        ssList = new ArrayList<>();
        supplementaryCodeValue = SupplementaryCodeValue.baoc;
        ssCode = new SSCodeImpl(supplementaryCodeValue);
        ssList.add(ssCode);
        // roamingRestrictionDueToUnsupportedFeature
        roamingRestrictionDueToUnsupportedFeature = false;
        // vbsGroupIndication
        vbsGroupIndication = false;
        // vgcsGroupIndication
        vgcsGroupIndication = false;
        // camelSubscriptionInfoWithdraw (camelSubscriptionInfoWithdraw = true;)
        // gprsSubscriptionDataWithdraw
        ArrayList<Integer> contextIdList = new ArrayList<>();
        contextIdList.add(1);
        contextIdList.add(2);
        gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl(contextIdList);
        // roamingRestrictedInSgsnDueToUnsuppportedFeature (roamingRestrictedInSgsnDueToUnsuppportedFeature = true;)
        // lsaInformationWithdraw
        lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
        // regionalSubscriptionIdentifier
        regionalSubscriptionIdentifier = new ZoneCodeImpl(new byte[] {0x21, 0x0F});
        // gmlcListWithdraw (gmlcListWithdraw = true;)
        // istInformationWithdraw (istInformationWithdraw = true;)
        // specificCSIWithdraw
        specificCSIWithdraw =  null;
        // chargingCharacteristicsWithdraw (chargingCharacteristicsWithdraw = true;)
        // stnSrWithdraw (stnSrWithdraw = true;)
        // epsSubscriptionDataWithdraw (epsSubscriptionDataWithdraw = null;)
        // apnOiReplacementWithdraw (apnOiReplacementWithdraw = true;)
        // csgSubscriptionDeleted (csgSubscriptionDeleted = true;)
        // subscribedPeriodicTAURAUTimerWithdraw (subscribedPeriodicTAURAUTimerWithdraw = true;)
        // subscribedPeriodicLAUTimerWithdraw (subscribedPeriodicLAUTimerWithdraw = true;)
        // subscribedVsrvccWithdraw (subscribedVsrvccWithdraw = true;)
        // vplmnCsgSubscriptionDeleted (vplmnCsgSubscriptionDeleted = true;)
        // additionalMSISDNWithdraw (additionalMSISDNWithdraw = true;)
        // csToPsSRVCCWithdraw (csToPsSRVCCWithdraw = true;)
        // imsiGroupIdListWithdraw (imsiGroupIdListWithdraw = true;)
        // userPlaneIntegrityProtectionWithdraw (userPlaneIntegrityProtectionWithdraw = true;)
        // dlBufferingSuggestedPacketCountWithdraw (dlBufferingSuggestedPacketCountWithdraw = true;)
        // ueUsageTypeWithdraw (ueUsageTypeWithdraw = true;)
        // resetIdsWithdraw (resetIdsWithdraw = true;)
        // iabOperationWithdraw (iabOperationWithdraw = true;)
        subscriptionDataDeletion = new DeleteSubscriberDataArgsImpl(imsi, basicServiceList, ssList,
                roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication,
                camelSubscriptionInfoWithdraw, null, gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature,
                lsaInformationWithdraw, gmlcListWithdraw, istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw,
                epsSubscriptionDataWithdraw, apnOiReplacementWithdraw, csgSubscriptionDeleted, subscribedPeriodicTAURAUTimerWithdraw,
                subscribedPeriodicLAUTimerWithdraw, subscribedVsrvccWithdraw, vplmnCsgSubscriptionDeleted, additionalMSISDNWithdraw, csToPsSRVCCWithdraw,
                imsiGroupIdListWithdraw, userPlaneIntegrityProtectionWithdraw, dlBufferingSuggestedPacketCountWithdraw, ueUsageTypeWithdraw, resetIdsWithdraw,
                iabOperationWithdraw);
        prim = new ResetRequestImpl(new SendingNodeNumberImpl(null, cssNumber), null, null, resetIdList, null, subscriptionDataDeletion);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataRel18wSubsDataDelToSGSN();

        assertTrue(Arrays.equals(rawData, encodedData));

    }
}
