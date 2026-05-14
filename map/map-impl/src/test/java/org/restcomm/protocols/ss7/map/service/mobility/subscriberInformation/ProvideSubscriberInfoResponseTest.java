package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.Time;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DaylightSavingTime;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.IMSVoiceOverPsSessionsIndication;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation5GS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSNetworkCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSRadioAccessCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NRCellGlobalId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NRTAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TimeZone;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.primitives.TimeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_MaximumSduSizeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribed_TransferDelayImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.FQDNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class ProvideSubscriberInfoResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 6, 48, 4, (byte) 161, 2, (byte) 129, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 47, 48, 4, (byte) 161, 2, (byte) 129, 0, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataCs3() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x1e, 0x30, (byte) 0x82,
                0x01, 0x1a, (byte) 0xa0, 0x5b, (byte) 0xaa, 0x59, (byte) 0x80, 0x07,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x09, 0x5f, 0x02, (byte) 0x81,
                0x05, 0x47, (byte) 0xf8, 0x10, 0x00, 0x6d, (byte) 0x84, 0x0a,
                0x03, 0x10, (byte) 0xb1, (byte) 0xa6, 0x78, (byte) 0xd8, 0x12, 0x3d,
                0x01, 0x01, (byte) 0x85, 0x00, (byte) 0x86, 0x01, 0x00, (byte) 0x87,
                0x36, 0x6d, 0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e,
                0x6d, 0x6d, 0x65, 0x67, 0x69, 0x33, 0x30, 0x30,
                0x30, 0x2e, 0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70,
                0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0xa1,
                0x02, (byte) 0x80, 0x00, (byte) 0x85, 0x08, 0x10, 0x71, 0x41,
                0x00, 0x64, 0x16, 0x50, (byte) 0xf0, (byte) 0x86, 0x03, 0x39,
                0x3a, 0x52, (byte) 0xa8, 0x1b, (byte) 0x80, 0x03, (byte) 0x94, 0x71,
                0x01, (byte) 0x81, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65,
                0x08, 0x74, (byte) 0xf4, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98,
                0x09, 0x77, 0x39, (byte) 0xf7, (byte) 0x83, 0x01, 0x04, (byte) 0x89,
                0x01, 0x00, (byte) 0x8a, 0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5,
                (byte) 0x8b, 0x01, 0x04, (byte) 0x8e, 0x02, 0x00, 0x03, (byte) 0x8f,
                0x01, 0x00, (byte) 0xb0, 0x78, (byte) 0x80, 0x08, 0x47, (byte) 0xf8,
                0x20, 0x08, 0x00, 0x00, 0x00, 0x08, (byte) 0x81, 0x07,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x07, (byte) 0xf0, 0x01, (byte) 0x83,
                0x0a, 0x03, 0x10, (byte) 0xb1, (byte) 0xa6, 0x78, (byte) 0xd8, 0x12,
                0x3d, 0x01, 0x01, (byte) 0x84, 0x37, 0x61, 0x6d, 0x66,
                0x33, 0x2e, 0x63, 0x6c, 0x75, 0x73, 0x74, 0x65,
                0x72, 0x32, 0x2e, 0x6e, 0x65, 0x74, 0x32, 0x2e,
                0x61, 0x6d, 0x66, 0x2e, 0x35, 0x67, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x32, 0x2e, 0x6d, 0x63,
                0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67, 0x70,
                0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b,
                0x2e, 0x6f, 0x72, 0x67, (byte) 0x85, 0x05, 0x47, (byte) 0xf8,
                0x10, 0x00, 0x6d, (byte) 0x86, 0x00, (byte) 0x87, 0x01, 0x00,
                (byte) 0x88, 0x03, 0x47, (byte) 0xf8, 0x20, (byte) 0x89, 0x02, 0x00,
                (byte) 0xfa, (byte) 0x8a, 0x01, 0x04, (byte) 0x8c, 0x06, 0x47, (byte) 0xf8,
                0x20, 0x07, (byte) 0x8f, (byte) 0xd2
        };
    }

    private byte[] getEncodedDataCs4() {
        return new byte[] { 0x30, 0x4d, 0x30, 0x4b, (byte) 0xa0, 0x31, 0x02, 0x02,
                0x01, 0x2c, (byte) 0x81, 0x07, (byte) 0x91, (byte) 0x94, 0x71, 0x01,
                0x64, 0x00, 0x00, (byte) 0x82, 0x08, 0x04, (byte) 0x97, 0x18,
                0x29, 0x30, 0x69, (byte) 0x91, 0x40, (byte) 0xa3, 0x09, (byte) 0x80,
                0x07, 0x47, (byte) 0xf8, 0x10, 0x00, 0x65, 0x28, 0x17,
                (byte) 0xab, 0x0d, (byte) 0x80, 0x05, 0x05, (byte) 0xc0, 0x00, 0x00,
                0x60, (byte) 0x82, 0x01, 0x01, (byte) 0x83, 0x01, 0x02, (byte) 0xa1,
                0x03, 0x0a, 0x01, 0x02, (byte) 0x89, 0x01, 0x00, (byte) 0x8a,
                0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5, (byte) 0x8b, 0x01, 0x04,
                (byte) 0x8e, 0x02, 0x00, 0x03, (byte) 0x8f, 0x01, 0x00
        };
    }

    private byte[] getEncodedDataCs5() {
        return new byte[] { 0x30, (byte) 0x81, (byte) 0x8d, 0x30, (byte) 0x81, (byte) 0x8a, (byte) 0xa0,
                0x45, 0x02, 0x01, 0x00, (byte) 0x80, 0x08, 0x10, (byte) 0xb1,
                (byte) 0xa6, 0x3f, (byte) 0xd8, 0x12, (byte) 0xe0, 0x00, (byte) 0x81, 0x07,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x00, (byte) 0x82,
                0x08, 0x04, (byte) 0x97, 0x18, 0x29, 0x30, 0x69, (byte) 0x91,
                0x40, (byte) 0xa3, 0x09, (byte) 0x80, 0x07, 0x47, (byte) 0xf8, 0x10,
                0x00, 0x65, 0x28, 0x17, (byte) 0x86, 0x07, (byte) 0x91, (byte) 0x94,
                0x71, 0x01, 0x64, 0x00, 0x00, (byte) 0x88, 0x00, (byte) 0xab,
                0x0d, (byte) 0x80, 0x05, 0x05, (byte) 0xc0, 0x00, 0x00, 0x60,
                (byte) 0x82, 0x01, 0x01, (byte) 0x83, 0x01, 0x02, (byte) 0xa1, 0x02,
                (byte) 0x81, 0x00, (byte) 0x85, 0x08, 0x10, 0x71, 0x41, 0x00,
                0x64, 0x16, 0x50, (byte) 0xf0, (byte) 0x86, 0x03, 0x39, 0x3a,
                0x52, (byte) 0xa8, 0x1b, (byte) 0x80, 0x03, (byte) 0x94, 0x71, 0x01,
                (byte) 0x81, 0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x08,
                0x74, (byte) 0xf4, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09,
                0x77, 0x39, (byte) 0xf7, (byte) 0x83, 0x01, 0x04, (byte) 0x89, 0x01,
                0x00, (byte) 0x8a, 0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5, (byte) 0x8b,
                0x01, 0x04, (byte) 0x8e, 0x02, 0x00, 0x03, (byte) 0x8f, 0x01, 0x00
        };
    }

    private byte[] getEncodedDataPs6() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x00, 0x30, (byte) 0x81,
                (byte) 0xfd, (byte) 0xa3, 0x28, (byte) 0xa0, 0x09, (byte) 0x80, 0x07, 0x47,
                (byte) 0xf8, 0x10, 0x00, 0x6d, 0x27, (byte) 0xbf, (byte) 0x81, 0x06,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x65, 0x17, (byte) 0x83, 0x08,
                (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x10, (byte) 0xf0,
                (byte) 0x84, 0x03, 0x31, 0x33, 0x31, (byte) 0x86, 0x00, (byte) 0x89,
                0x02, 0x03, 0x6f, (byte) 0xa4, 0x03, 0x0a, 0x01, 0x02,
                (byte) 0x89, 0x01, 0x01, (byte) 0x8a, 0x04, (byte) 0xea, 0x5b, 0x27,
                (byte) 0xa5, (byte) 0x8b, 0x01, 0x01, (byte) 0xad, 0x4c, (byte) 0x80, 0x07,
                0x47, (byte) 0xf8, 0x10, 0x00, 0x07, (byte) 0xea, 0x02, (byte) 0x81,
                0x05, 0x47, (byte) 0xf8, 0x10, 0x00, 0x6d, (byte) 0x86, 0x02,
                0x03, 0x6f, (byte) 0x87, 0x36, 0x6d, 0x6d, 0x65, 0x63,
                0x30, 0x33, 0x2e, 0x6d, 0x6d, 0x65, 0x67, 0x69,
                0x33, 0x30, 0x30, 0x30, 0x2e, 0x6d, 0x6d, 0x65,
                0x2e, 0x65, 0x70, 0x63, 0x2e, 0x6d, 0x6e, 0x63,
                0x30, 0x30, 0x32, 0x2e, 0x6d, 0x63, 0x63, 0x37,
                0x34, 0x38, 0x2e, 0x33, 0x67, 0x70, 0x70, 0x6e,
                0x65, 0x74, 0x77, 0x6f, 0x72, 0x6b, 0x2e, 0x6f,
                0x72, 0x67, (byte) 0x8e, 0x02, 0x00, 0x06, (byte) 0x8f, 0x01,
                0x00, (byte) 0xb0, 0x6b, (byte) 0x80, 0x08, 0x47, (byte) 0xf8, 0x20,
                0x08, 0x00, 0x00, 0x00, 0x08, (byte) 0x81, 0x07, 0x47,
                (byte) 0xf8, 0x10, 0x00, 0x07, (byte) 0xea, 0x02, (byte) 0x84, 0x37,
                0x61, 0x6d, 0x66, 0x33, 0x2e, 0x63, 0x6c, 0x75,
                0x73, 0x74, 0x65, 0x72, 0x32, 0x2e, 0x6e, 0x65,
                0x74, 0x32, 0x2e, 0x61, 0x6d, 0x66, 0x2e, 0x35,
                0x67, 0x63, 0x2e, 0x6d, 0x6e, 0x63, 0x30, 0x32,
                0x2e, 0x6d, 0x63, 0x63, 0x37, 0x34, 0x38, 0x2e,
                0x33, 0x67, 0x70, 0x70, 0x6e, 0x65, 0x74, 0x77,
                0x6f, 0x72, 0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x85,
                0x05, 0x47, (byte) 0xf8, 0x10, 0x00, 0x6d, (byte) 0x87, 0x02,
                0x03, 0x6f, (byte) 0x88, 0x03, 0x47, (byte) 0xf8, 0x20, (byte) 0x89,
                0x02, 0x00, (byte) 0xfa, (byte) 0x8a, 0x01, 0x04, (byte) 0x8c, 0x06,
                0x47, (byte) 0xf8, 0x20, 0x07, (byte) 0x8f, (byte) 0xd2
        };
    }

    private byte[] getEncodedDataPs7() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x12, 0x30, (byte) 0x82,
                0x01, 0x0e, (byte) 0xa3, 0x29, (byte) 0xa0, 0x09, (byte) 0x80, 0x07,
                0x47, (byte) 0xf8, 0x70, 0x22, 0x74, 0x26, 0x14, (byte) 0x81,
                0x06, 0x47, (byte) 0xf8, 0x10, 0x00, 0x65, 0x17, (byte) 0x83,
                0x08, (byte) 0x91, (byte) 0x94, 0x71, 0x01, 0x64, 0x00, 0x10,
                (byte) 0xf0, (byte) 0x84, 0x03, 0x31, 0x33, 0x31, (byte) 0x86, 0x00,
                (byte) 0x88, 0x00, (byte) 0x89, 0x01, 0x00, (byte) 0xa4, (byte) 0x81, (byte) 0x8b,
                (byte) 0xa5, (byte) 0x81, (byte) 0x88, 0x30, (byte) 0x81, (byte) 0x85, (byte) 0x80, 0x01,
                0x01, (byte) 0x81, 0x00, (byte) 0x82, 0x02, (byte) 0xf1, 0x21, (byte) 0x83,
                0x01, 0x15, (byte) 0x84, 0x09, 0x08, 0x69, 0x6e, 0x74,
                0x65, 0x72, 0x6e, 0x65, 0x74, (byte) 0x85, 0x09, 0x08,
                0x69, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x65, 0x74,
                (byte) 0x86, 0x01, 0x01, (byte) 0x87, 0x02, 0x01, 0x07, (byte) 0x88,
                0x04, 0x01, 0x03, 0x04, 0x07, (byte) 0x89, 0x04, 0x01,
                0x00, 0x00, 0x02, (byte) 0x8a, 0x06, 0x17, 0x05, 0x26,
                0x30, 0x51, 0x05, (byte) 0x8b, 0x09, 0x09, 0x72, 0x64,
                (byte) 0x80, 0x40, 0x00, (byte) 0x83, 0x40, 0x00, (byte) 0x8c, 0x09,
                0x09, 0x72, 0x64, (byte) 0x80, 0x40, 0x00, 0x43, (byte) 0x80,
                0x00, (byte) 0x8d, 0x09, 0x09, 0x72, 0x64, (byte) 0x80, 0x40,
                0x00, (byte) 0x83, 0x40, 0x00, (byte) 0x8e, 0x04, 0x01, 0x02,
                0x04, 0x08, (byte) 0x8f, 0x02, 0x08, 0x00, (byte) 0x90, 0x05,
                (byte) 0xc0, (byte) 0xa8, 0x05, 0x33, 0x18, (byte) 0x92, 0x03, 0x10,
                0x00, 0x00, (byte) 0x93, 0x03, 0x10, 0x00, 0x00, (byte) 0x94,
                0x03, 0x10, 0x00, 0x00, (byte) 0x9c, 0x02, 0x3a, 0x3b,
                (byte) 0x9d, 0x01, 0x3c, (byte) 0x85, 0x08, 0x10, 0x71, 0x41,
                0x00, 0x64, 0x16, 0x50, (byte) 0xf1, (byte) 0xa7, 0x19, (byte) 0x80,
                0x08, 0x31, 0x30, 0x30, 0x30, 0x32, 0x30, 0x33,
                0x31, (byte) 0x81, 0x0d, 0x31, 0x30, 0x30, 0x30, 0x32,
                0x30, 0x33, 0x31, 0x37, 0x30, 0x38, 0x31, 0x34,
                (byte) 0xa8, 0x1b, (byte) 0x80, 0x03, (byte) 0x94, 0x71, 0x01, (byte) 0x81,
                0x08, 0x09, 0x41, 0x50, 0x01, 0x65, 0x28, 0x32,
                (byte) 0xf8, (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77,
                0x39, (byte) 0xf7, (byte) 0x83, 0x01, 0x04, (byte) 0x89, 0x01, 0x01,
                (byte) 0x8a, 0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5, (byte) 0x8b, 0x01,
                0x01, (byte) 0x8e, 0x02, 0x00, 0x06, (byte) 0x8f, 0x01, 0x00
        };
    }

    private byte[] getEncodedDataPs8() {
        return new byte[] { 0x30, (byte) 0x82, 0x01, 0x36, 0x30, (byte) 0x82,
                0x01, 0x32, (byte) 0x85, 0x08, 0x10, 0x71, 0x41, 0x00,
                0x64, 0x16, 0x50, (byte) 0xf1, (byte) 0xa7, 0x19, (byte) 0x80, 0x08,
                0x31, 0x30, 0x30, 0x30, 0x32, 0x30, 0x33, 0x31,
                (byte) 0x81, 0x0d, 0x31, 0x30, 0x30, 0x30, 0x32, 0x30,
                0x33, 0x31, 0x37, 0x30, 0x38, 0x31, 0x34, (byte) 0xa8,
                0x1b, (byte) 0x80, 0x03, (byte) 0x94, 0x71, 0x01, (byte) 0x81, 0x08,
                0x09, 0x41, 0x50, 0x01, 0x65, 0x08, 0x14, (byte) 0xf5,
                (byte) 0x82, 0x07, (byte) 0x91, (byte) 0x95, (byte) 0x98, 0x09, 0x77, 0x39,
                (byte) 0xf7, (byte) 0x83, 0x01, 0x04, (byte) 0x89, 0x01, 0x01, (byte) 0x8a,
                0x04, (byte) 0xea, 0x5b, 0x27, (byte) 0xa5, (byte) 0x8b, 0x01, 0x04,
                (byte) 0xac, (byte) 0x81, (byte) 0x8b, (byte) 0xa5, (byte) 0x81, (byte) 0x88, 0x30, (byte) 0x81,
                (byte) 0x85, (byte) 0x80, 0x01, 0x01, (byte) 0x81, 0x00, (byte) 0x82, 0x02,
                (byte) 0xf1, 0x21, (byte) 0x83, 0x01, 0x15, (byte) 0x84, 0x09, 0x08,
                0x69, 0x6e, 0x74, 0x65, 0x72, 0x6e, 0x65, 0x74,
                (byte) 0x85, 0x09, 0x08, 0x69, 0x6e, 0x74, 0x65, 0x72,
                0x6e, 0x65, 0x74, (byte) 0x86, 0x01, 0x01, (byte) 0x87, 0x02,
                0x01, 0x07, (byte) 0x88, 0x04, 0x01, 0x03, 0x04, 0x07,
                (byte) 0x89, 0x04, 0x01, 0x00, 0x00, 0x02, (byte) 0x8a, 0x06,
                0x17, 0x05, 0x26, 0x30, 0x51, 0x05, (byte) 0x8b, 0x09,
                0x09, 0x72, 0x64, (byte) 0x80, 0x40, 0x00, (byte) 0x83, 0x40,
                0x00, (byte) 0x8c, 0x09, 0x09, 0x72, 0x64, (byte) 0x80, 0x40,
                0x00, 0x43, (byte) 0x80, 0x00, (byte) 0x8d, 0x09, 0x09, 0x72,
                0x64, (byte) 0x80, 0x40, 0x00, (byte) 0x83, 0x40, 0x00, (byte) 0x8e,
                0x04, 0x01, 0x02, 0x04, 0x08, (byte) 0x8f, 0x02, 0x08,
                0x00, (byte) 0x90, 0x05, (byte) 0xc0, (byte) 0xa8, 0x05, 0x33, 0x18,
                (byte) 0x92, 0x03, 0x10, 0x00, 0x00, (byte) 0x93, 0x03, 0x10,
                0x00, 0x00, (byte) 0x94, 0x03, 0x10, 0x00, 0x00, (byte) 0x9c,
                0x02, 0x3a, 0x3b, (byte) 0x9d, 0x01, 0x3c, (byte) 0xad, 0x4d,
                (byte) 0x80, 0x07, 0x47, (byte) 0xf8, 0x10, 0x00, 0x07, (byte) 0xea,
                0x02, (byte) 0x81, 0x05, 0x47, (byte) 0xf8, 0x10, 0x00, 0x6d,
                (byte) 0x85, 0x00, (byte) 0x86, 0x01, 0x00, (byte) 0x87, 0x36, 0x6d,
                0x6d, 0x65, 0x63, 0x30, 0x33, 0x2e, 0x6d, 0x6d,
                0x65, 0x67, 0x69, 0x33, 0x30, 0x30, 0x30, 0x2e,
                0x6d, 0x6d, 0x65, 0x2e, 0x65, 0x70, 0x63, 0x2e,
                0x6d, 0x6e, 0x63, 0x30, 0x30, 0x32, 0x2e, 0x6d,
                0x63, 0x63, 0x37, 0x34, 0x38, 0x2e, 0x33, 0x67,
                0x70, 0x70, 0x6e, 0x65, 0x74, 0x77, 0x6f, 0x72,
                0x6b, 0x2e, 0x6f, 0x72, 0x67, (byte) 0x8e, 0x02, 0x00,
                0x06, (byte) 0x8f, 0x01, 0x00
        };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberInformation" })
    public void testDecode() throws Exception {

        // test 1
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ProvideSubscriberInfoResponseImpl asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getSubscriberInfo().getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertNull(asc.getExtensionContainer());

        // test 2
        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(asc.getSubscriberInfo().getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        // test 3 (data taken from MAP load test for CS domain, containing location information EPS and 5GS
        rawData = getEncodedDataCs3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // Subscriber Info
        SubscriberInfo si = asc.getSubscriberInfo();
        // LocationInformation
        LocationInformation li = si.getLocationInformation();
        Integer aol = li.getAgeOfLocationInformation();
        GeographicalInformation liGeographicInfo = li.getGeographicalInformation();
        ISDNAddressString vlrNumber = li.getVlrNumber();
        LocationNumberMap locationNumberMap = li.getLocationNumber();
        CellGlobalIdOrServiceAreaIdOrLAI liCGIorSAIorLAI = li.getCellGlobalIdOrServiceAreaIdOrLAI();
        MAPExtensionContainer liExtensionContainer = li.getExtensionContainer();
        LSAIdentity liLsaId = li.getSelectedLSAId();
        ISDNAddressString mscAddress = li.getMscNumber();
        GeodeticInformation liGeodeticInfo = li.getGeodeticInformation();
        boolean liCurrentLocationRetrieved = li.getCurrentLocationRetrieved();
        boolean liSaiPresent = li.getSaiPresent();
        LocationInformationEPS liLocInfoEPS = li.getLocationInformationEPS();
        EUtranCgi liLTECgi = liLocInfoEPS.getEUtranCellGlobalIdentity();
        TAId liTAId = liLocInfoEPS.getTrackingAreaIdentity();
        MAPExtensionContainer liLocInfoEPSExtensionContainer = liLocInfoEPS.getExtensionContainer();
        GeographicalInformation liLocInfoEPSGeographicalInfo = liLocInfoEPS.getGeographicalInformation();
        GeodeticInformation liLocEPSInfoGeodeticInfo = liLocInfoEPS.getGeodeticInformation();
        boolean liLocInfoEPSCurrentLocationRetrieved = liLocInfoEPS.getCurrentLocationRetrieved();
        Integer liLocInfoEPSAgeOfLocationInformation = liLocInfoEPS.getAgeOfLocationInformation();
        DiameterIdentity liLocInfoEPSMmeName = liLocInfoEPS.getMmeName();
        UserCSGInformation liUserCSGInformation = li.getUserCSGInformation();
        assertNotNull(li);
        assertNull(aol);
        assertNull(liGeographicInfo);
        assertNull(vlrNumber);
        assertNull(locationNumberMap);
        assertNull(liCGIorSAIorLAI);
        assertNull(liExtensionContainer);
        assertNull(liLsaId);
        assertNull(mscAddress);
        assertNull(liGeodeticInfo);
        assertFalse(liCurrentLocationRetrieved);
        assertFalse(liSaiPresent);
        assertNotNull(liLocInfoEPS);
        assertEquals(liLTECgi.getMCC(), 748);
        assertEquals(liLTECgi.getMNC(), 1);
        assertEquals(liLTECgi.getEci(), 614146);
        assertEquals(liLTECgi.getENodeBId(), 2399);
        assertEquals(liLTECgi.getCi(), 2);
        assertEquals(liTAId.getMCC(), 748);
        assertEquals(liTAId.getMNC(), 1);
        assertEquals(liTAId.getTAC(), 109);
        assertNull(liLocInfoEPSExtensionContainer);
        assertNull(liLocInfoEPSGeographicalInfo);
        assertEquals(liLocEPSInfoGeodeticInfo.getScreeningAndPresentationIndicators(), 3);
        assertEquals(liLocEPSInfoGeodeticInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(liLocEPSInfoGeodeticInfo.getLatitude(), -34.91034507751465);
        assertEquals(liLocEPSInfoGeodeticInfo.getLongitude(), -56.14981412887573);
        assertEquals(liLocEPSInfoGeodeticInfo.getUncertainty(), 1.0000000000000009);
        assertEquals(liLocEPSInfoGeodeticInfo.getConfidence(), 1);
        assertTrue(liLocInfoEPSCurrentLocationRetrieved);
        assertEquals(liLocInfoEPSMmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        assertEquals(liLocInfoEPSAgeOfLocationInformation.intValue(), 0);
        assertNull(liUserCSGInformation);
        // SubscriberState
        SubscriberState subscriberState = si.getSubscriberState();
        assertEquals(subscriberState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        LocationInformationGPRS liGPRS = si.getLocationInformationGPRS();
        assertNull(liGPRS);
        // PSSubscriberState
        PSSubscriberState psSubscriberState = si.getPSSubscriberState();
        assertNull(psSubscriberState);
        // IMEI
        IMEI imei = si.getIMEI();
        assertEquals(imei.getIMEI(), "011714004661050");
        // MSClassmark2
        MSClassmark2 msClassmark2 = si.getMSClassmark2();
        assertEquals(msClassmark2.getData(), new byte[] {0x39, 0x3a, 0x52});
        // GPRSMSClass
        GPRSMSClass gprsmsClass = si.getGPRSMSClass();
        assertNull(gprsmsClass);
        // MNPInfoRes
        MNPInfoRes mnpInfoRes = si.getMNPInfoRes();
        IMSI mnpImsi = mnpInfoRes.getIMSI();
        ISDNAddressString mnpMsisdn = mnpInfoRes.getMSISDN();
        NumberPortabilityStatus mnpPortabilityStatus = mnpInfoRes.getNumberPortabilityStatus();
        String mnpRouteingNumber = mnpInfoRes.getRouteingNumber().getRouteingNumber();
        assertEquals(mnpRouteingNumber, "491710");
        assertEquals(mnpImsi.getData(), "901405105680474");
        assertEquals(mnpMsisdn.getAddress(), "59899077937");
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        // IMSVoiceOverPsSessionsIndication
        IMSVoiceOverPsSessionsIndication ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported);
        // LastUEActivityTime
        Time lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        UsedRATType lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.eUtran);
        // EPSSubscriberState
        PSSubscriberState epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        LocationInformationEPS locationInfoEPS = si.getLocationInformationEPS();
        assertNull(locationInfoEPS);
        // TimeZone
        TimeZone timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 3});
        // DaylightSavingTime
        DaylightSavingTime daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        LocationInformation5GS li5GS = si.getLocationInformation5GS();
        NRCellGlobalId nrCGI = li5GS.getNRCellGlobalId();
        EUtranCgi li5GSLteCgi = li5GS.getEUtranCgi();
        GeographicalInformation li5GSGeographicalInfo = li5GS.getGeographicalInformation();
        GeodeticInformation li5GSGeodeticInformation = li5GS.getGeodeticInformation();
        FQDN li5GSAMFAddress = li5GS.getAMFAddress();
        TAId li5GSTAId = li5GS.getTAId();
        boolean li5GSCurrentLocationRetrieved = li5GS.isCurrentLocationRetrieved();
        Integer li5GSAgeOfLocationInformation = li5GS.getAgeOfLocationInformation();
        PlmnId li5GSVPlmnId = li5GS.getVPlmnId();
        TimeZone li5GSLocalTimeZone = li5GS.getLocalTimeZone();
        UsedRATType li5GSUsedRATType = li5GS.getUsedRATType();
        MAPExtensionContainer li5GSExtensionContainer = li5GS.getExtensionContainer();
        NRTAId li5GSNRTAId = li5GS.getNRTAId();
        assertEquals(nrCGI.getMCC(), 748);
        assertEquals(nrCGI.getMNC(), 2);
        assertEquals(nrCGI.getNCI(), 34359738376L);
        assertEquals(li5GSLteCgi.getMCC(), 748);
        assertEquals(li5GSLteCgi.getMNC(), 1);
        assertEquals(li5GSLteCgi.getEci(), 520193);
        assertEquals(li5GSLteCgi.getENodeBId(), 2032);
        assertEquals(li5GSLteCgi.getCi(), 1);
        assertNull(li5GSGeographicalInfo);
        assertEquals(li5GSGeodeticInformation.getScreeningAndPresentationIndicators(), 3);
        assertEquals(li5GSGeodeticInformation.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(li5GSGeodeticInformation.getLatitude(), -34.91034507751465);
        assertEquals(li5GSGeodeticInformation.getLongitude(), -56.14981412887573);
        assertEquals(li5GSGeodeticInformation.getUncertainty(), 1.0000000000000009);
        assertEquals(li5GSGeodeticInformation.getConfidence(), 1);
        assertEquals(li5GSAMFAddress.getData(), "amf3.cluster2.net2.amf.5gc.mnc02.mcc748.3gppnetwork.org".getBytes());
        assertEquals(li5GSTAId.getMCC(), 748);
        assertEquals(li5GSTAId.getMNC(), 1);
        assertEquals(li5GSTAId.getTAC(), 109);
        assertTrue(li5GSCurrentLocationRetrieved);
        assertEquals(li5GSAgeOfLocationInformation.intValue(), 0);
        assertEquals(li5GSVPlmnId.getMcc(), 748);
        assertEquals(li5GSVPlmnId.getMnc(), 2);
        assertEquals(li5GSLocalTimeZone.getData(), new byte[] {0, -6});
        assertEquals(li5GSUsedRATType, UsedRATType.eUtran);
        assertNull(li5GSExtensionContainer);
        assertEquals(li5GSNRTAId.getMCC(), 748);
        assertEquals(li5GSNRTAId.getMNC(), 2);
        assertEquals(li5GSNRTAId.getNrTAC(), 495570);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());

        // test 4 (data taken from MAP load test for CS domain, containing location information (no EPS or 5GS)
        rawData = getEncodedDataCs4();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        si = asc.getSubscriberInfo();
        // LocationInformation
        li = si.getLocationInformation();
        aol = li.getAgeOfLocationInformation();
        liGeographicInfo = li.getGeographicalInformation();
        vlrNumber = li.getVlrNumber();
        locationNumberMap = li.getLocationNumber();
        liCGIorSAIorLAI = li.getCellGlobalIdOrServiceAreaIdOrLAI();
        liExtensionContainer = li.getExtensionContainer();
        liLsaId = li.getSelectedLSAId();
        mscAddress = li.getMscNumber();
        liGeodeticInfo = li.getGeodeticInformation();
        liCurrentLocationRetrieved = li.getCurrentLocationRetrieved();
        liSaiPresent = li.getSaiPresent();
        liLocInfoEPS = li.getLocationInformationEPS();
        liUserCSGInformation = li.getUserCSGInformation();
        assertNotNull(li);
        assertEquals(aol.intValue(), 300);
        assertNull(liGeographicInfo);
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddress(), "491710460000");
        assertEquals(locationNumberMap.getLocationNumber().getNatureOfAddressIndicator(), 4);
        assertEquals(locationNumberMap.getLocationNumber().getNumberingPlanIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getInternalNetworkNumberIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getScreeningIndicator(), 3);
        assertEquals(locationNumberMap.getLocationNumber().getAddress(), "819203961904");
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 101);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 10263);
        assertNull(liExtensionContainer);
        assertNull(liLsaId);
        assertNull(mscAddress);
        assertNull(liGeodeticInfo);
        assertFalse(liCurrentLocationRetrieved);
        assertFalse(liSaiPresent);
        assertNull(liLocInfoEPS);
        assertTrue(liUserCSGInformation.getCSGId().getData().get(0));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(1));
        assertFalse(liUserCSGInformation.getCSGId().getData().get(2));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(25));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(26));
        assertNull(liUserCSGInformation.getExtensionContainer());
        assertEquals(liUserCSGInformation.getAccessMode().intValue(), 1);
        assertEquals(liUserCSGInformation.getCmi().intValue(), 2);
        // SubscriberState
        subscriberState = si.getSubscriberState();
        assertEquals(subscriberState.getSubscriberStateChoice(), SubscriberStateChoice.netDetNotReachable);
        assertEquals(subscriberState.getNotReachableReason(), NotReachableReason.restrictedArea);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        liGPRS = si.getLocationInformationGPRS();
        assertNull(liGPRS);
        // PSSubscriberState
        psSubscriberState = si.getPSSubscriberState();
        assertNull(psSubscriberState);
        // IMEI
        imei = si.getIMEI();
        assertNull(imei);
        // MSClassmark2
        msClassmark2 = si.getMSClassmark2();
        assertNull(msClassmark2);
        // GPRSMSClass
        gprsmsClass = si.getGPRSMSClass();
        assertNull(gprsmsClass);
        // MNPInfoRes
        mnpInfoRes = si.getMNPInfoRes();
        assertNull(mnpInfoRes);
        // IMSVoiceOverPsSessionsIndication
        ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported);
        // LastUEActivityTime
        lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.eUtran);
        // EPSSubscriberState
        epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        locationInfoEPS = si.getLocationInformationEPS();
        assertNull(locationInfoEPS);
        // TimeZone
        timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 3});
        // DaylightSavingTime
        daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        li5GS = si.getLocationInformation5GS();
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());

        // test 5 (data taken from MAP load test for CS domain, containing location information, richer than test 4 (no EPS or 5GS)
        rawData = getEncodedDataCs5();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        si = asc.getSubscriberInfo();
        // LocationInformation
        li = si.getLocationInformation();
        aol = li.getAgeOfLocationInformation();
        liGeographicInfo = li.getGeographicalInformation();
        vlrNumber = li.getVlrNumber();
        locationNumberMap = li.getLocationNumber();
        liCGIorSAIorLAI = li.getCellGlobalIdOrServiceAreaIdOrLAI();
        liExtensionContainer = li.getExtensionContainer();
        liLsaId = li.getSelectedLSAId();
        mscAddress = li.getMscNumber();
        liGeodeticInfo = li.getGeodeticInformation();
        liCurrentLocationRetrieved = li.getCurrentLocationRetrieved();
        liSaiPresent = li.getSaiPresent();
        liLocInfoEPS = li.getLocationInformationEPS();
        liUserCSGInformation = li.getUserCSGInformation();
        assertEquals(aol.intValue(), 0);
        assertEquals(liGeographicInfo.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertEquals(liGeographicInfo.getLatitude(), -34.90973353385925);
        assertEquals(liGeographicInfo.getLongitude(), -56.14631652832031);
        assertEquals(liGeographicInfo.getUncertainty(), 0.0);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getAddress(), "491710460000");
        assertEquals(locationNumberMap.getLocationNumber().getNatureOfAddressIndicator(), 4);
        assertEquals(locationNumberMap.getLocationNumber().getNumberingPlanIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getInternalNetworkNumberIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(locationNumberMap.getLocationNumber().getScreeningIndicator(), 3);
        assertEquals(locationNumberMap.getLocationNumber().getAddress(), "819203961904");
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 101);
        assertEquals(liCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 10263);
        assertNull(liExtensionContainer);
        assertNull(liLsaId);
        assertEquals(mscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(mscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(mscAddress.getAddress(), "491710460000");
        assertNull(liGeodeticInfo);
        assertTrue(liCurrentLocationRetrieved);
        assertFalse(liSaiPresent);
        assertNull(liLocInfoEPS);
        assertTrue(liUserCSGInformation.getCSGId().getData().get(0));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(1));
        assertFalse(liUserCSGInformation.getCSGId().getData().get(2));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(25));
        assertTrue(liUserCSGInformation.getCSGId().getData().get(26));
        assertNull(liUserCSGInformation.getExtensionContainer());
        assertEquals(liUserCSGInformation.getAccessMode().intValue(), 1);
        assertEquals(liUserCSGInformation.getCmi().intValue(), 2);
        // SubscriberState
        subscriberState = si.getSubscriberState();
        assertEquals(subscriberState.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertNull(subscriberState.getNotReachableReason());
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        liGPRS = si.getLocationInformationGPRS();
        assertNull(liGPRS);
        // PSSubscriberState
        psSubscriberState = si.getPSSubscriberState();
        assertNull(psSubscriberState);
        // IMEI
        imei = si.getIMEI();
        assertEquals(imei.getIMEI(), "011714004661050");
        // MSClassmark2
        msClassmark2 = si.getMSClassmark2();
        assertEquals(msClassmark2.getData(), new byte[] {0x39, 0x3a, 0x52});
        // GPRSMSClass
        gprsmsClass = si.getGPRSMSClass();
        assertNull(gprsmsClass);
        // MNPInfoRes
        mnpInfoRes = si.getMNPInfoRes();
        mnpImsi = mnpInfoRes.getIMSI();
        mnpMsisdn = mnpInfoRes.getMSISDN();
        mnpPortabilityStatus = mnpInfoRes.getNumberPortabilityStatus();
        mnpRouteingNumber = mnpInfoRes.getRouteingNumber().getRouteingNumber();
        assertEquals(mnpRouteingNumber, "491710");
        assertEquals(mnpImsi.getData(), "901405105680474");
        assertEquals(mnpMsisdn.getAddress(), "59899077937");
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        // IMSVoiceOverPsSessionsIndication
        ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported);
        // LastUEActivityTime
        lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.eUtran);
        // EPSSubscriberState
        epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        locationInfoEPS = si.getLocationInformationEPS();
        assertNull(locationInfoEPS);
        // TimeZone
        timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 3});
        // DaylightSavingTime
        daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        li5GS = si.getLocationInformation5GS();
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());

        // test 6 (data taken from MAP load test for PS domain, containing location information GPRS, EPS and 5GS
        rawData = getEncodedDataPs6();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        si = asc.getSubscriberInfo();
        // LocationInformation
        li = si.getLocationInformation();
        assertNull(li);
        // SubscriberState
        subscriberState = si.getSubscriberState();
        assertNull(subscriberState);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        liGPRS = si.getLocationInformationGPRS();
        CellGlobalIdOrServiceAreaIdOrLAI liGprsCGIorSAIorLAI = liGPRS.getCellGlobalIdOrServiceAreaIdOrLAI();
        boolean liGprsSaiPresent = liGPRS.isSaiPresent();
        Integer liGprsAol = liGPRS.getAgeOfLocationInformation();
        boolean liGprsCurrentLocation = liGPRS.isCurrentLocationRetrieved();
        GeographicalInformation liGPRSGeographicalInfo = liGPRS.getGeographicalInformation();
        GeodeticInformation liGPRSGeodeticInfo = liGPRS.getGeodeticInformation();
        RAIdentity rai = liGPRS.getRouteingAreaIdentity();
        LSAIdentity lsaIdentity = liGPRS.getLSAIdentity();
        ISDNAddressString sgsnNumber = liGPRS.getSGSNNumber();
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 109);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 10175);
        assertTrue(liGprsSaiPresent);
        assertEquals(liGprsAol.intValue(), 879);
        assertFalse(liGprsCurrentLocation);
        assertNull(liGPRSGeographicalInfo);
        assertNull(liGPRSGeodeticInfo);
        assertEquals(rai.getMCC(), 748);
        assertEquals(rai.getMNC(), 1);
        assertEquals(rai.getLAC(), 101);
        assertEquals(rai.getRAC(), 23);
        assertEquals(lsaIdentity.getData(), new byte[] {49, 51, 49});
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getAddress(), "4917104600010");
        assertNull(liGPRS.getExtensionContainer());
        // PSSubscriberState
        psSubscriberState = si.getPSSubscriberState();
        assertEquals(psSubscriberState.getChoice(), PSSubscriberStateChoice.netDetNotReachable);
        assertEquals(psSubscriberState.getNetDetNotReachable(), NotReachableReason.restrictedArea);
        // IMEI
        imei = si.getIMEI();
        assertNull(imei);
        // MSClassmark2
        msClassmark2 = si.getMSClassmark2();
        assertNull(msClassmark2);
        // GPRSMSClass
        gprsmsClass = si.getGPRSMSClass();
        assertNull(gprsmsClass);
        // MNPInfoRes
        mnpInfoRes = si.getMNPInfoRes();
        assertNull(mnpInfoRes);
        // IMSVoiceOverPsSessionsIndication
        ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsSupported);
        // LastUEActivityTime
        lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.geran);
        // EPSSubscriberState
        epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        locationInfoEPS = si.getLocationInformationEPS();
        EUtranCgi epsCgi = locationInfoEPS.getEUtranCellGlobalIdentity();
        TAId epsTAId = locationInfoEPS.getTrackingAreaIdentity();
        MAPExtensionContainer epsExtensionContainer = locationInfoEPS.getExtensionContainer();
        GeographicalInformation epsGeographicalInfo = locationInfoEPS.getGeographicalInformation();
        GeodeticInformation epsGeodeticInfo = locationInfoEPS.getGeodeticInformation();
        boolean epsCurrentLocationRetrieved = locationInfoEPS.getCurrentLocationRetrieved();
        Integer epsAgeOfLocationInformation = locationInfoEPS.getAgeOfLocationInformation();
        DiameterIdentity epsMmeName = locationInfoEPS.getMmeName();
        assertEquals(epsCgi.getMCC(), 748);
        assertEquals(epsCgi.getMNC(), 1);//518658
        assertEquals(epsCgi.getEci(), 518658);
        assertEquals(epsCgi.getENodeBId(), 2026);
        assertEquals(epsCgi.getCi(), 2);
        assertEquals(epsTAId.getMCC(), 748);
        assertEquals(epsTAId.getMNC(), 1);
        assertEquals(epsTAId.getTAC(), 109);
        assertNull(epsExtensionContainer);
        assertNull(epsGeographicalInfo);
        assertNull(epsGeodeticInfo);
        assertFalse(epsCurrentLocationRetrieved);
        assertEquals(epsAgeOfLocationInformation.intValue(), 879);
        assertEquals(epsMmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        // TimeZone
        timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 6});
        // DaylightSavingTime
        daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        li5GS = si.getLocationInformation5GS();
        nrCGI = li5GS.getNRCellGlobalId();
        li5GSLteCgi = li5GS.getEUtranCgi();
        li5GSGeographicalInfo = li5GS.getGeographicalInformation();
        li5GSGeodeticInformation = li5GS.getGeodeticInformation();
        li5GSAMFAddress = li5GS.getAMFAddress();
        li5GSTAId = li5GS.getTAId();
        li5GSCurrentLocationRetrieved = li5GS.isCurrentLocationRetrieved();
        li5GSAgeOfLocationInformation = li5GS.getAgeOfLocationInformation();
        li5GSVPlmnId = li5GS.getVPlmnId();
        li5GSLocalTimeZone = li5GS.getLocalTimeZone();
        li5GSUsedRATType = li5GS.getUsedRATType();
        li5GSExtensionContainer = li5GS.getExtensionContainer();
        li5GSNRTAId = li5GS.getNRTAId();
        assertEquals(nrCGI.getMCC(), 748);
        assertEquals(nrCGI.getMNC(), 2);
        assertEquals(nrCGI.getNCI(), 34359738376L);
        assertEquals(li5GSLteCgi.getMCC(), 748);
        assertEquals(li5GSLteCgi.getMNC(), 1);
        assertEquals(li5GSLteCgi.getEci(), 518658);
        assertEquals(li5GSLteCgi.getENodeBId(), 2026);
        assertEquals(li5GSLteCgi.getCi(), 2);
        assertNull(li5GSGeographicalInfo);
        assertNull(li5GSGeodeticInformation);
        assertEquals(li5GSAMFAddress.getData(), "amf3.cluster2.net2.amf.5gc.mnc02.mcc748.3gppnetwork.org".getBytes());
        assertEquals(li5GSTAId.getMCC(), 748);
        assertEquals(li5GSTAId.getMNC(), 1);
        assertEquals(li5GSTAId.getTAC(), 109);
        assertFalse(li5GSCurrentLocationRetrieved);
        assertEquals(li5GSAgeOfLocationInformation.intValue(), 879);
        assertEquals(li5GSVPlmnId.getMcc(), 748);
        assertEquals(li5GSVPlmnId.getMnc(), 2);
        assertEquals(li5GSLocalTimeZone.getData(), new byte[] {0, -6});
        assertEquals(li5GSUsedRATType, UsedRATType.eUtran);
        assertNull(li5GSExtensionContainer);
        assertEquals(li5GSNRTAId.getMCC(), 748);
        assertEquals(li5GSNRTAId.getMNC(), 2);
        assertEquals(li5GSNRTAId.getNrTAC(), 495570);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());

        // test 7 (data taken from MAP load test for PS domain, containing location information GPRS and some
        // other parameters not found in test 6 such GPRSMCClass and PSSubscriberState with PDPContextInfo
        rawData = getEncodedDataPs7();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        si = asc.getSubscriberInfo();
        // LocationInformation
        li = si.getLocationInformation();
        assertNull(li);
        // SubscriberState
        subscriberState = si.getSubscriberState();
        assertNull(subscriberState);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        liGPRS = si.getLocationInformationGPRS();
        liGprsCGIorSAIorLAI = liGPRS.getCellGlobalIdOrServiceAreaIdOrLAI();
        liGprsSaiPresent = liGPRS.isSaiPresent();
        liGprsAol = liGPRS.getAgeOfLocationInformation();
        liGprsCurrentLocation = liGPRS.isCurrentLocationRetrieved();
        liGPRSGeographicalInfo = liGPRS.getGeographicalInformation();
        liGPRSGeodeticInfo = liGPRS.getGeodeticInformation();
        rai = liGPRS.getRouteingAreaIdentity();
        lsaIdentity = liGPRS.getLSAIdentity();
        sgsnNumber = liGPRS.getSGSNNumber();
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 748);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 7);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 8820);
        assertEquals(liGprsCGIorSAIorLAI.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode(), 9748);
        assertTrue(liGprsSaiPresent);
        assertEquals(liGprsAol.intValue(), 0);
        assertTrue(liGprsCurrentLocation);
        assertNull(liGPRSGeographicalInfo);
        assertNull(liGPRSGeodeticInfo);
        assertEquals(rai.getMCC(), 748);
        assertEquals(rai.getMNC(), 1);
        assertEquals(rai.getLAC(), 101);
        assertEquals(rai.getRAC(), 23);
        assertEquals(lsaIdentity.getData(), new byte[] {49, 51, 49});
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getAddress(), "4917104600010");
        assertNull(liGPRS.getExtensionContainer());
        // PSSubscriberState
        psSubscriberState = si.getPSSubscriberState();
        assertEquals(psSubscriberState.getChoice(), PSSubscriberStateChoice.psPDPActiveReachableForPaging);
        assertNotNull(psSubscriberState.getPDPContextInfoList());
        ArrayList<PDPContextInfo> pdpContextInfoList = psSubscriberState.getPDPContextInfoList();
        int pdpContextIdentifier = pdpContextInfoList.get(0).getPdpContextIdentifier();
        assertEquals(pdpContextIdentifier, 1);
        boolean pdpContextActive = pdpContextInfoList.get(0).getPdpContextActive();
        assertTrue(pdpContextActive);
        PDPTypeValue pdpTypeValue = pdpContextInfoList.get(0).getPdpType().getPDPTypeValue();
        assertEquals(pdpTypeValue, PDPTypeValue.IPv4);
        PDPAddress pdpAddress = pdpContextInfoList.get(0).getPdpAddress();
        assertEquals(pdpAddress.getData(), new byte[] { 21 });
        APN apnSubscribed = pdpContextInfoList.get(0).getApnSubscribed();
        assertEquals(apnSubscribed.getApn(), "internet");
        APN apnInUse = pdpContextInfoList.get(0).getApnInUse();
        assertEquals(apnInUse.getApn(), "internet");
        int asapi = pdpContextInfoList.get(0).getNsapi();
        assertEquals(asapi, 1);
        TransactionId transactionId = pdpContextInfoList.get(0).getTransactionId();
        assertEquals(transactionId.getData(), new byte[] {1, 7});
        TEID teidForGnAndGp = pdpContextInfoList.get(0).getTeidForGnAndGp();
        assertEquals(teidForGnAndGp.getData(), new byte[] {1, 3, 4, 7});
        TEID teidForIu = pdpContextInfoList.get(0).getTeidForIu();
        assertEquals(teidForIu.getData(), new byte[] {1, 0, 0, 2});
        GSNAddress ggsnAddress = pdpContextInfoList.get(0).getGgsnAddress();
        assertEquals(ggsnAddress.getData(), new byte[] {23, 5, 38, 48, 81, 5});
        int allocationRetentionPriority = pdpContextInfoList.get(0).getQosSubscribed().getAllocationRetentionPriority();
        ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosSubscribed().getDeliveryOfErroneousSdus();
        ExtQoSSubscribed_DeliveryOrder deliveryOrder = pdpContextInfoList.get(0).getQosSubscribed().getDeliveryOrder();
        ExtQoSSubscribed_TrafficClass trafficClass = pdpContextInfoList.get(0).getQosSubscribed().getTrafficClass();
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = pdpContextInfoList.get(0).getQosSubscribed().getMaximumSduSize();
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = pdpContextInfoList.get(0).getQosSubscribed().getMaximumBitRateForUplink();
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosSubscribed().getMaximumBitRateForDownlink();
        ExtQoSSubscribed_ResidualBER residualBER = pdpContextInfoList.get(0).getQosSubscribed().getResidualBER();
        ExtQoSSubscribed_SduErrorRatio sduErrorRatio = pdpContextInfoList.get(0).getQosSubscribed().getSduErrorRatio();
        ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = pdpContextInfoList.get(0).getQosSubscribed().getTrafficHandlingPriority();
        ExtQoSSubscribed_TransferDelay transferDelay = pdpContextInfoList.get(0).getQosSubscribed().getTransferDelay();
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosSubscribed().getGuaranteedBitRateForUplink();
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosSubscribed().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 32);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 64);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        allocationRetentionPriority = pdpContextInfoList.get(0).getQosRequested().getAllocationRetentionPriority();
        deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosRequested().getDeliveryOfErroneousSdus();
        deliveryOrder = pdpContextInfoList.get(0).getQosRequested().getDeliveryOrder();
        trafficClass = pdpContextInfoList.get(0).getQosRequested().getTrafficClass();
        maximumSduSize = pdpContextInfoList.get(0).getQosRequested().getMaximumSduSize();
        maximumBitRateForUplink = pdpContextInfoList.get(0).getQosRequested().getMaximumBitRateForUplink();
        maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosRequested().getMaximumBitRateForDownlink();
        residualBER = pdpContextInfoList.get(0).getQosRequested().getResidualBER();
        sduErrorRatio = pdpContextInfoList.get(0).getQosRequested().getSduErrorRatio();
        trafficHandlingPriority = pdpContextInfoList.get(0).getQosRequested().getTrafficHandlingPriority();
        transferDelay = pdpContextInfoList.get(0).getQosRequested().getTransferDelay();
        guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosRequested().getGuaranteedBitRateForUplink();
        guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosRequested().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 16);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 576);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        allocationRetentionPriority = pdpContextInfoList.get(0).getQosNegotiated().getAllocationRetentionPriority();
        deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosNegotiated().getDeliveryOfErroneousSdus();
        deliveryOrder = pdpContextInfoList.get(0).getQosNegotiated().getDeliveryOrder();
        trafficClass = pdpContextInfoList.get(0).getQosNegotiated().getTrafficClass();
        maximumSduSize = pdpContextInfoList.get(0).getQosNegotiated().getMaximumSduSize();
        maximumBitRateForUplink = pdpContextInfoList.get(0).getQosNegotiated().getMaximumBitRateForUplink();
        maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosNegotiated().getMaximumBitRateForDownlink();
        residualBER = pdpContextInfoList.get(0).getQosNegotiated().getResidualBER();
        sduErrorRatio = pdpContextInfoList.get(0).getQosNegotiated().getSduErrorRatio();
        trafficHandlingPriority = pdpContextInfoList.get(0).getQosNegotiated().getTrafficHandlingPriority();
        transferDelay = pdpContextInfoList.get(0).getQosNegotiated().getTransferDelay();
        guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosNegotiated().getGuaranteedBitRateForUplink();
        guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosNegotiated().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 32);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 64);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        // IMEI
        imei = si.getIMEI();
        assertEquals(imei.getIMEI(), "011714004661051");
        // MSClassmark2
        msClassmark2 = si.getMSClassmark2();
        assertNull(msClassmark2);
        // GPRSMSClass
        gprsmsClass = si.getGPRSMSClass();
        MSNetworkCapability msNetworkCapability = gprsmsClass.getMSNetworkCapability();
        MSRadioAccessCapability msRadioAccessCapability = gprsmsClass.getMSRadioAccessCapability();
        assertEquals(msNetworkCapability.getData(), hexStringToByteArray("3130303032303331"));
        assertEquals(msRadioAccessCapability.getData(), hexStringToByteArray("31303030323033313730383134"));
        // MNPInfoRes
        mnpInfoRes = si.getMNPInfoRes();
        mnpImsi = mnpInfoRes.getIMSI();
        mnpMsisdn = mnpInfoRes.getMSISDN();
        mnpPortabilityStatus = mnpInfoRes.getNumberPortabilityStatus();
        mnpRouteingNumber = mnpInfoRes.getRouteingNumber().getRouteingNumber();
        assertEquals(mnpRouteingNumber, "491710");
        assertEquals(mnpImsi.getData(), "901405105682238");
        assertEquals(mnpMsisdn.getAddress(), "59899077937");
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        // IMSVoiceOverPsSessionsIndication
        ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsSupported);
        // LastUEActivityTime
        lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.geran);
        // EPSSubscriberState
        epsSubscriberState = si.getEPSSubscriberState();
        assertNull(epsSubscriberState);
        // LocationInformationEPS
        locationInfoEPS = si.getLocationInformationEPS();
        assertNull(locationInfoEPS);
        // TimeZone
        timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 6});
        // DaylightSavingTime
        daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        li5GS = si.getLocationInformation5GS();
        assertNull(li5GS);

        // test 8 (data taken from MAP load test for PS domain, containing location information GPRS/EPS/5GS and some
        // other parameters not found in test 7 such as EPS Subscriber State
        rawData = getEncodedDataPs8();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new ProvideSubscriberInfoResponseImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        si = asc.getSubscriberInfo();
        // LocationInformation
        li = si.getLocationInformation();
        assertNull(li);
        // SubscriberState
        subscriberState = si.getSubscriberState();
        assertNull(subscriberState);
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
        // LocationInformationGPRS
        liGPRS = si.getLocationInformationGPRS();
        assertNull(liGPRS);
        // PSSubscriberState
        psSubscriberState = si.getPSSubscriberState();
        assertNull(psSubscriberState);
        // IMEI
        imei = si.getIMEI();
        assertEquals(imei.getIMEI(), "011714004661051");
        // MSClassmark2
        msClassmark2 = si.getMSClassmark2();
        assertNull(msClassmark2);
        // GPRSMSClass
        gprsmsClass = si.getGPRSMSClass();
        msNetworkCapability = gprsmsClass.getMSNetworkCapability();
        msRadioAccessCapability = gprsmsClass.getMSRadioAccessCapability();
        assertEquals(msNetworkCapability.getData(), hexStringToByteArray("3130303032303331"));
        assertEquals(msRadioAccessCapability.getData(), hexStringToByteArray("31303030323033313730383134"));
        // MNPInfoRes
        mnpInfoRes = si.getMNPInfoRes();
        mnpImsi = mnpInfoRes.getIMSI();
        mnpMsisdn = mnpInfoRes.getMSISDN();
        mnpPortabilityStatus = mnpInfoRes.getNumberPortabilityStatus();
        mnpRouteingNumber = mnpInfoRes.getRouteingNumber().getRouteingNumber();
        assertEquals(mnpRouteingNumber, "491710");
        assertEquals(mnpImsi.getData(), "901405105680415");
        assertEquals(mnpMsisdn.getAddress(), "59899077937");
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        // IMSVoiceOverPsSessionsIndication
        ims = si.getIMSVoiceOverPsSessionsIndication();
        assertEquals(ims, IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsSupported);
        // LastUEActivityTime
        lastUEActivityTime = si.getLastUEActivityTime();
        assertEquals(lastUEActivityTime.getYear(), 2024);
        assertEquals(lastUEActivityTime.getMonth(), 8);
        assertEquals(lastUEActivityTime.getDay(), 5);
        assertEquals(lastUEActivityTime.getHour(), 10);
        assertEquals(lastUEActivityTime.getMinute(), 27);
        assertEquals(lastUEActivityTime.getSecond(), 49);
        // UsedRATType
        lastRATType = si.getLastRATType();
        assertEquals(lastRATType, UsedRATType.eUtran);
        // EPSSubscriberState
        epsSubscriberState = si.getEPSSubscriberState();
        assertEquals(epsSubscriberState.getChoice(), PSSubscriberStateChoice.psPDPActiveReachableForPaging);
        assertNotNull(epsSubscriberState.getPDPContextInfoList());
        pdpContextInfoList = epsSubscriberState.getPDPContextInfoList();
        pdpContextIdentifier = pdpContextInfoList.get(0).getPdpContextIdentifier();
        assertEquals(pdpContextIdentifier, 1);
        pdpContextActive = pdpContextInfoList.get(0).getPdpContextActive();
        assertTrue(pdpContextActive);
        pdpTypeValue = pdpContextInfoList.get(0).getPdpType().getPDPTypeValue();
        assertEquals(pdpTypeValue, PDPTypeValue.IPv4);
        pdpAddress = pdpContextInfoList.get(0).getPdpAddress();
        assertEquals(pdpAddress.getData(), new byte[] { 21 });
        apnSubscribed = pdpContextInfoList.get(0).getApnSubscribed();
        assertEquals(apnSubscribed.getApn(), "internet");
        apnInUse = pdpContextInfoList.get(0).getApnInUse();
        assertEquals(apnInUse.getApn(), "internet");
        asapi = pdpContextInfoList.get(0).getNsapi();
        assertEquals(asapi, 1);
        transactionId = pdpContextInfoList.get(0).getTransactionId();
        assertEquals(transactionId.getData(), new byte[] {1, 7});
        teidForGnAndGp = pdpContextInfoList.get(0).getTeidForGnAndGp();
        assertEquals(teidForGnAndGp.getData(), new byte[] {1, 3, 4, 7});
        teidForIu = pdpContextInfoList.get(0).getTeidForIu();
        assertEquals(teidForIu.getData(), new byte[] {1, 0, 0, 2});
        ggsnAddress = pdpContextInfoList.get(0).getGgsnAddress();
        assertEquals(ggsnAddress.getData(), new byte[] {23, 5, 38, 48, 81, 5});
        allocationRetentionPriority = pdpContextInfoList.get(0).getQosSubscribed().getAllocationRetentionPriority();
        deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosSubscribed().getDeliveryOfErroneousSdus();
        deliveryOrder = pdpContextInfoList.get(0).getQosSubscribed().getDeliveryOrder();
        trafficClass = pdpContextInfoList.get(0).getQosSubscribed().getTrafficClass();
        maximumSduSize = pdpContextInfoList.get(0).getQosSubscribed().getMaximumSduSize();
        maximumBitRateForUplink = pdpContextInfoList.get(0).getQosSubscribed().getMaximumBitRateForUplink();
        maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosSubscribed().getMaximumBitRateForDownlink();
        residualBER = pdpContextInfoList.get(0).getQosSubscribed().getResidualBER();
        sduErrorRatio = pdpContextInfoList.get(0).getQosSubscribed().getSduErrorRatio();
        trafficHandlingPriority = pdpContextInfoList.get(0).getQosSubscribed().getTrafficHandlingPriority();
        transferDelay = pdpContextInfoList.get(0).getQosSubscribed().getTransferDelay();
        guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosSubscribed().getGuaranteedBitRateForUplink();
        guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosSubscribed().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 32);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 64);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        allocationRetentionPriority = pdpContextInfoList.get(0).getQosRequested().getAllocationRetentionPriority();
        deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosRequested().getDeliveryOfErroneousSdus();
        deliveryOrder = pdpContextInfoList.get(0).getQosRequested().getDeliveryOrder();
        trafficClass = pdpContextInfoList.get(0).getQosRequested().getTrafficClass();
        maximumSduSize = pdpContextInfoList.get(0).getQosRequested().getMaximumSduSize();
        maximumBitRateForUplink = pdpContextInfoList.get(0).getQosRequested().getMaximumBitRateForUplink();
        maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosRequested().getMaximumBitRateForDownlink();
        residualBER = pdpContextInfoList.get(0).getQosRequested().getResidualBER();
        sduErrorRatio = pdpContextInfoList.get(0).getQosRequested().getSduErrorRatio();
        trafficHandlingPriority = pdpContextInfoList.get(0).getQosRequested().getTrafficHandlingPriority();
        transferDelay = pdpContextInfoList.get(0).getQosRequested().getTransferDelay();
        guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosRequested().getGuaranteedBitRateForUplink();
        guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosRequested().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 16);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 576);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        allocationRetentionPriority = pdpContextInfoList.get(0).getQosNegotiated().getAllocationRetentionPriority();
        deliveryOfErroneousSdus = pdpContextInfoList.get(0).getQosNegotiated().getDeliveryOfErroneousSdus();
        deliveryOrder = pdpContextInfoList.get(0).getQosNegotiated().getDeliveryOrder();
        trafficClass = pdpContextInfoList.get(0).getQosNegotiated().getTrafficClass();
        maximumSduSize = pdpContextInfoList.get(0).getQosNegotiated().getMaximumSduSize();
        maximumBitRateForUplink = pdpContextInfoList.get(0).getQosNegotiated().getMaximumBitRateForUplink();
        maximumBitRateForDownlink = pdpContextInfoList.get(0).getQosNegotiated().getMaximumBitRateForDownlink();
        residualBER = pdpContextInfoList.get(0).getQosNegotiated().getResidualBER();
        sduErrorRatio = pdpContextInfoList.get(0).getQosNegotiated().getSduErrorRatio();
        trafficHandlingPriority = pdpContextInfoList.get(0).getQosNegotiated().getTrafficHandlingPriority();
        transferDelay = pdpContextInfoList.get(0).getQosNegotiated().getTransferDelay();
        guaranteedBitRateForUplink = pdpContextInfoList.get(0).getQosNegotiated().getGuaranteedBitRateForUplink();
        guaranteedBitRateForDownlink = pdpContextInfoList.get(0).getQosNegotiated().getGuaranteedBitRateForDownlink();
        assertEquals(allocationRetentionPriority, 9);
        assertEquals(deliveryOfErroneousSdus, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(deliveryOrder, ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(trafficClass, ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(maximumSduSize.getMaximumSduSize(), 1000);
        assertEquals(maximumBitRateForUplink.getBitRate(), 576);
        assertEquals(maximumBitRateForDownlink.getBitRate(), 64);
        assertEquals(residualBER, ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved);
        assertEquals(sduErrorRatio,ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved);
        assertEquals(trafficHandlingPriority, ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(transferDelay.getSourceData(), 32);
        assertEquals(guaranteedBitRateForUplink.getBitRate(), 64);
        assertEquals(guaranteedBitRateForDownlink.getBitRate(), 0);
        // LocationInformationEPS
        locationInfoEPS = si.getLocationInformationEPS();
        epsCgi = locationInfoEPS.getEUtranCellGlobalIdentity();
        epsTAId = locationInfoEPS.getTrackingAreaIdentity();
        epsExtensionContainer = locationInfoEPS.getExtensionContainer();
        epsGeographicalInfo = locationInfoEPS.getGeographicalInformation();
        epsGeodeticInfo = locationInfoEPS.getGeodeticInformation();
        epsCurrentLocationRetrieved = locationInfoEPS.getCurrentLocationRetrieved();
        epsAgeOfLocationInformation = locationInfoEPS.getAgeOfLocationInformation();
        epsMmeName = locationInfoEPS.getMmeName();
        assertEquals(epsCgi.getMCC(), 748);
        assertEquals(epsCgi.getMNC(), 1);//518658
        assertEquals(epsCgi.getEci(), 518658);
        assertEquals(epsCgi.getENodeBId(), 2026);
        assertEquals(epsCgi.getCi(), 2);
        assertEquals(epsTAId.getMCC(), 748);
        assertEquals(epsTAId.getMNC(), 1);
        assertEquals(epsTAId.getTAC(), 109);
        assertNull(epsExtensionContainer);
        assertNull(epsGeographicalInfo);
        assertNull(epsGeodeticInfo);
        assertTrue(epsCurrentLocationRetrieved);
        assertEquals(epsAgeOfLocationInformation.intValue(), 0);
        assertEquals(epsMmeName.getData(), "mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        // TimeZone
        timeZone = si.getTimeZone();
        assertEquals(timeZone.getData(), new byte[] {0, 6});
        // DaylightSavingTime
        daylightSavingTime = si.getDaylightSavingTime();
        assertEquals(daylightSavingTime, DaylightSavingTime.noAdjustment);
        // LocationInformation5GS
        li5GS = si.getLocationInformation5GS();
        // MAPExtensionContainer
        assertNull(asc.getExtensionContainer());
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberInformation" })
    public void testEncode() throws Exception {

        SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        SubscriberInfoImpl subscriberInfo = new SubscriberInfoImpl(null, subscriberState, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        ProvideSubscriberInfoResponseImpl asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        // test 3 (data taken from MAP load test for CS domain, containing location information EPS and 5GS)
        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Subscriber Info
        // LocationInformation
        int aol;
        GeographicalInformationImpl geographicalInformation = null;
        ISDNAddressString vlrNumber = null;
        LocationNumberImpl locationNumber = null;
        LocationNumberMapImpl locationNumberMap = null;
        CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = null;
        CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = null;
        LSAIdentityImpl selectedLSAId = null;
        ISDNAddressStringImpl mscNumber;
        EUtranCgiImpl eUtranCgi = new EUtranCgiImpl();
        eUtranCgi.setData(748, 1, 614146);
        TAIdImpl taId = new TAIdImpl();
        taId.setData(748, 1, 109);
        GeodeticInformationImpl geodeticInformation = new GeodeticInformationImpl(3, TypeOfShape.EllipsoidPointWithUncertaintyCircle, -34.91034507751465,
                -56.14981412887573, 1.0000000000000009, 1);
        boolean currentLocationRetrieved = true;
        boolean saiPresent = false;
        aol = 0;
        DiameterIdentity mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        LocationInformationEPSImpl locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, null, null,
                geodeticInformation, currentLocationRetrieved, aol, mmeName);
        UserCSGInformationImpl userCSGInformation = null;
        LocationInformation locationInformation = new LocationInformationImpl(null, null, null, null,
                null, null, null, null, null, false,
                saiPresent, locationInformationEPS, null);
        // SubscriberState
        subscriberState = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        // MAPExtensionContainer
        MAPExtensionContainer extensionContainer = null;
        // LocationInformationGPRS
        LocationInformationGPRS locationInformationGPRS = null;
        // PSSubscriberState
        PSSubscriberState psSubscriberState = null;
        // IMEI
        IMEI imei = new IMEIImpl("011714004661050");
        // MSClassmark2
        MSClassmark2 msClassmark2 = new MSClassmark2Impl(new byte[] {0x39, 0x3a, 0x52});
        // GPRSMSClass
        GPRSMSClass gprsmsClass = null;
        // MNPInfoRes
        RouteingNumber routeingNumber = new RouteingNumberImpl("491710");
        IMSI mnpImsi = new IMSIImpl("901405105680474");
        ISDNAddressString mnpMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        NumberPortabilityStatus mnpPortabilityStatus = NumberPortabilityStatus.ownNumberNotPortedOut;
        MNPInfoRes mnpInfoRes = new MNPInfoResImpl(routeingNumber, mnpImsi, mnpMsisdn, mnpPortabilityStatus, null);
        // IMSVoiceOverPsSessionsIndication
        IMSVoiceOverPsSessionsIndication ims = IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsNotSupported;
        // LastUEActivityTime
        Time lastUEActivityTime = new TimeImpl(2024, 8, 5, 10, 27, 49);
        // UsedRATType
        UsedRATType lastRATType = UsedRATType.eUtran;
        // EPSSubscriberState
        PSSubscriberState epsSubscriberState = null;
        // LocationInformationEPS
        locationInformationEPS = null;
        // TimeZone
        TimeZone timeZone = new TimeZoneImpl(new byte[] {0, 3});
        // DaylightSavingTime
        DaylightSavingTime daylightSavingTime = DaylightSavingTime.noAdjustment;
        // LocationInformation5GS
        NRCellGlobalIdImpl nrCGI = new NRCellGlobalIdImpl();
        nrCGI.setData(748, 2, 34359738376L);
        EUtranCgiImpl li5GSLteCgi = new EUtranCgiImpl();
        li5GSLteCgi.setData(748, 1, 520193);
        GeographicalInformation li5GSGeographicalInfo = null;
        GeodeticInformation li5GSGeodeticInformation = new GeodeticInformationImpl(3,
                TypeOfShape.EllipsoidPointWithUncertaintyCircle, -34.91034507751465, -56.14981412887573, 1.0000000000000009, 1);
        FQDN li5GSAMFAddress = new FQDNImpl("amf3.cluster2.net2.amf.5gc.mnc02.mcc748.3gppnetwork.org".getBytes());
        TAIdImpl li5GSTAId = new TAIdImpl();
        li5GSTAId.setData(748, 1, 109);
        boolean li5GSCurrentLocationRetrieved = true;
        int li5GSAgeOfLocationInformation = 0;
        PlmnIdImpl li5GSVPlmnId = new PlmnIdImpl(748, 2);
        TimeZone li5GSLocalTimeZone = new TimeZoneImpl(new byte[] {0, -6});
        UsedRATType li5GSUsedRATType = UsedRATType.eUtran;
        MAPExtensionContainer li5GSExtensionContainer = null;
        NRTAIdImpl li5GSNRTAId = new NRTAIdImpl();
        li5GSNRTAId.setData(748, 2, 495570);
        LocationInformation5GS locationInformation5GS = new LocationInformation5GSImpl(nrCGI, li5GSLteCgi, li5GSGeographicalInfo, li5GSGeodeticInformation,
                li5GSAMFAddress, li5GSTAId, li5GSCurrentLocationRetrieved, li5GSAgeOfLocationInformation, li5GSVPlmnId, li5GSLocalTimeZone, li5GSUsedRATType,
                null, li5GSNRTAId);
        // Subscriber Info
        subscriberInfo = new SubscriberInfoImpl(locationInformation, subscriberState, null, locationInformationGPRS,
                psSubscriberState, imei, msClassmark2, gprsmsClass, mnpInfoRes, ims, lastUEActivityTime, lastRATType, epsSubscriberState,
                locationInformationEPS, timeZone, daylightSavingTime, locationInformation5GS);

        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataCs3();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 5 (data taken from MAP load test for CS domain, containing location information, richer than test 4 (no EPS or 5GS)
        geographicalInformation = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, -34.90973353385925,
                -56.14631652832031, 0.0);
        vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710460000");
        int natureOfAddressIndicator = 4;
        String address = "819203961904";
        int numberingPlanIndicator = 1;
        int internalNetworkNumberIndicator = 1;
        int addressRepresentationREstrictedIndicator = 1;
        int screeningIndicator = 3;
        locationNumber = new LocationNumberImpl(natureOfAddressIndicator, address, numberingPlanIndicator, internalNetworkNumberIndicator,
                addressRepresentationREstrictedIndicator, screeningIndicator);
        locationNumberMap = new LocationNumberMapImpl();
        locationNumberMap.setLocationNumber(locationNumber);
        cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
        cellGlobalIdOrServiceAreaIdFixedLength.setData(748, 1, 101, 10263);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
        mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "491710460000");
        geodeticInformation = null;
        BitSetStrictLength csgIdBitSet = new BitSetStrictLength(27);
        csgIdBitSet.set(0);
        csgIdBitSet.set(1);
        csgIdBitSet.set(25);
        csgIdBitSet.set(26);
        CSGId csgId = new CSGIdImpl(csgIdBitSet);
        Integer accessMode = 1;
        Integer cmi = 2;
        userCSGInformation = new UserCSGInformationImpl(csgId, null, accessMode, cmi);
        locationInformation = new LocationInformationImpl(aol, geographicalInformation, vlrNumber, locationNumberMap,
                cellGlobalIdOrServiceAreaIdOrLAI, null, null, mscNumber, geodeticInformation, currentLocationRetrieved,
                saiPresent, locationInformationEPS, userCSGInformation);
        // SubscriberState
        subscriberState = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        // IMEI
        imei = new IMEIImpl("011714004661050");
        // MSClassmark2
        msClassmark2 = new MSClassmark2Impl(new byte[] {0x39, 0x3a, 0x52});
        // MNPInfoRes
        routeingNumber = new RouteingNumberImpl("491710");
        mnpImsi = new IMSIImpl("901405105680474");
        mnpMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        mnpInfoRes = new MNPInfoResImpl(routeingNumber, mnpImsi, mnpMsisdn, mnpPortabilityStatus, null);
        // LastUEActivityTime
        lastUEActivityTime = new TimeImpl(2024, 8, 5, 10, 27, 49);
        // TimeZone
        timeZone = new TimeZoneImpl(new byte[] {0, 3});
        // LocationInformation5GS
        locationInformation5GS = null;
        // Subscriber Info
        subscriberInfo = new SubscriberInfoImpl(locationInformation, subscriberState, null, locationInformationGPRS,
                psSubscriberState, imei, msClassmark2, gprsmsClass, mnpInfoRes, ims, lastUEActivityTime, lastRATType, epsSubscriberState,
                locationInformationEPS, timeZone, daylightSavingTime, locationInformation5GS);

        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataCs5();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 7 (data taken from MAP load test for PS domain, containing location information GPRS and some
        // other parameters not found in test 6 such GPRSMCClass and PSSubscriberState with PDPContextInfo

        // LocationInformation
        locationInformation = null;
        // SubscriberState
        subscriberState = null;
        // LocationInformationGPRS
        cellGlobalIdOrServiceAreaIdFixedLength.setData(748, 7, 8820, 9748);
        cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
        saiPresent = true;
        geographicalInformation = null;
        //geodeticInformation = null;
        RAIdentity raIdentity = new RAIdentityImpl(748, 1, 101, 23);
        LSAIdentity selectedLSAIdentity = new LSAIdentityImpl(new byte[] {49, 51, 49});
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "4917104600010");
        locationInformationGPRS = new LocationInformationGPRSImpl(cellGlobalIdOrServiceAreaIdOrLAI, raIdentity, geographicalInformation,
                sgsnNumber, selectedLSAIdentity, null, saiPresent, geodeticInformation, currentLocationRetrieved, aol);
        PSSubscriberStateChoice psSubscriberStateChoice = PSSubscriberStateChoice.psPDPActiveReachableForPaging;
        NotReachableReason netDetNotReachable = null;
        ArrayList<PDPContextInfo> pdpContextInfoList = new ArrayList<>();
        int pdpContextIdentifier = 1;
        boolean pdpContextActive = true;
        PDPType pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddress pdpAddress = new PDPAddressImpl(new byte[] { 21 });
        APN apnSubscribed = new APNImpl("internet");
        int nsapi = 1;
        TransactionId transactionId = new TransactionIdImpl(new byte[] {1, 7});
        TEID teidForGnAndGp = new TEIDImpl(new byte[] {1, 3, 4, 7});
        TEID teidForIu = new TEIDImpl(new byte[] {1, 0, 0, 2});
        GSNAddress ggsnAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        int allocationRetentionPriority = 9;
        ExtQoSSubscribed_DeliveryOfErroneousSdus deliveryOfErroneousSdus = ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes;
        ExtQoSSubscribed_DeliveryOrder deliveryOrder = ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo;
        ExtQoSSubscribed_TrafficClass trafficClass = ExtQoSSubscribed_TrafficClass.interactiveClass;
        int maximumSduSizeData = 100;
        boolean isSourceData = true;
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSizeImpl(maximumSduSizeData, isSourceData);
        int maximumBitRateForUL = 128;
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForUL, isSourceData);
        int maximumBitRateForDL = 576;
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(maximumBitRateForDL, isSourceData);
        ExtQoSSubscribed_ResidualBER residualBER = ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved;
        ExtQoSSubscribed_SduErrorRatio sduErrorRatio = ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved;
        ExtQoSSubscribed_TrafficHandlingPriority trafficHandlingPriority = ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3;
        int transferDelayValue = 800;
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelayImpl(transferDelayValue, isSourceData);
        int gbrUL = 64;
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(gbrUL, isSourceData);
        int gbrDL = 256;
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(gbrDL, isSourceData);
        ExtQoSSubscribed qosSubscribed = new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus,
                deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink, maximumBitRateForDownlink, residualBER,
                sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        transferDelayValue = 400;
        transferDelay = new ExtQoSSubscribed_TransferDelayImpl(transferDelayValue, isSourceData);
        gbrUL = 128;
        guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(gbrUL, isSourceData);
        gbrDL = 512;
        guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(gbrDL, isSourceData);
        ExtQoSSubscribed qosRequested = new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus,
                deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink, maximumBitRateForDownlink, residualBER,
                sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        transferDelayValue = 800;
        transferDelay = new ExtQoSSubscribed_TransferDelayImpl(transferDelayValue, isSourceData);
        gbrUL = 64;
        guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRateImpl(gbrUL, isSourceData);
        gbrDL = 256;
        guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRateImpl(gbrDL, isSourceData);
        ExtQoSSubscribed qosNegotiated = new ExtQoSSubscribedImpl(allocationRetentionPriority, deliveryOfErroneousSdus,
                deliveryOrder, trafficClass, maximumSduSize, maximumBitRateForUplink, maximumBitRateForDownlink, residualBER,
                sduErrorRatio, trafficHandlingPriority, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        GPRSChargingID chargingId = new GPRSChargingIDImpl(new byte[] {1, 2, 4, 8});
        boolean isNormalCharging = true;
        boolean isPrepaidCharging = false;
        boolean isFlatRateCharging = false;
        boolean isChargingByHotBillingCharging = false;
        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(isNormalCharging, isPrepaidCharging, isFlatRateCharging, isChargingByHotBillingCharging);
        GSNAddress rncAddress = new GSNAddressImpl(new byte[] { (byte) 192, (byte) 168, 5, 51, 24 });
        Ext2QoSSubscribed_SourceStatisticsDescriptor sourceStatisticsDescriptor = Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        boolean optimisedForSignallingTraffic = true;
        int maxBRDLExt = 256000;
        ExtQoSSubscribed_BitRateExtended maxBitRateForDLExt = new ExtQoSSubscribed_BitRateExtendedImpl(maxBRDLExt, isSourceData);
        int gbrExtDL = 128000;
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDLExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrExtDL, isSourceData);
        Ext2QoSSubscribed qos2Subscribed = new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic,
                maxBitRateForDLExt, guaranteedBitRateForDLExtended);
        maxBRDLExt = 512000;
        maxBitRateForDLExt = new ExtQoSSubscribed_BitRateExtendedImpl(maxBRDLExt, isSourceData);
        gbrExtDL = 256000;
        guaranteedBitRateForDLExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrExtDL, isSourceData);
        Ext2QoSSubscribed qos2Requested = new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic,
                maxBitRateForDLExt, guaranteedBitRateForDLExtended);
        maxBRDLExt = 256000;
        maxBitRateForDLExt = new ExtQoSSubscribed_BitRateExtendedImpl(maxBRDLExt, isSourceData);
        gbrExtDL = 128000;
        guaranteedBitRateForDLExtended = new ExtQoSSubscribed_BitRateExtendedImpl(gbrExtDL, isSourceData);
        Ext2QoSSubscribed qos2Negotiated = new Ext2QoSSubscribedImpl(sourceStatisticsDescriptor, optimisedForSignallingTraffic,
                maxBitRateForDLExt, guaranteedBitRateForDLExtended);
        ExtPDPType extPdpType = new ExtPDPTypeImpl(new byte[] { 58, 59 });
        PDPAddress extPdpAddress = new PDPAddressImpl(new byte[] { 60 });
        PDPContextInfo pdpContextInfo = new PDPContextInfoImpl(pdpContextIdentifier, pdpContextActive, pdpType, pdpAddress,
                apnSubscribed, apnSubscribed, nsapi, transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested,
                qosNegotiated, chargingId, chargingCharacteristics, rncAddress, null, qos2Subscribed,
                qos2Requested, qos2Negotiated, null, null, null, null,
                null, null, extPdpType, extPdpAddress);
        pdpContextInfoList.add(pdpContextInfo);
        psSubscriberState = new PSSubscriberStateImpl(psSubscriberStateChoice, netDetNotReachable, pdpContextInfoList);
        // IMEI
        imei = new IMEIImpl("011714004661051");
        // MSClassmark2
        msClassmark2 = null;
        // GPRSMSClass
        byte[] mSNetworkCapabilityB = hexStringToByteArray("3130303032303331");
        byte[] mSRadioAccessCapabilityB = hexStringToByteArray("31303030323033313730383134");
        MSNetworkCapability msNetworkCapability = new MSNetworkCapabilityImpl(mSNetworkCapabilityB);
        MSRadioAccessCapability msRadioAccessCapability = new MSRadioAccessCapabilityImpl(mSRadioAccessCapabilityB);
        gprsmsClass = new GPRSMSClassImpl(msNetworkCapability, msRadioAccessCapability);
        // MNPInfoRes
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        routeingNumber = new RouteingNumberImpl("491710");
        mnpImsi = new IMSIImpl("901405105682238");
        mnpMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        mnpInfoRes = new MNPInfoResImpl(routeingNumber, mnpImsi, mnpMsisdn, mnpPortabilityStatus, null);
        // IMSVoiceOverPsSessionsIndication
        ims = IMSVoiceOverPsSessionsIndication.imsVoiceOverPSSessionsSupported;
        // LastUEActivityTime
        lastUEActivityTime = new TimeImpl(2024, 8, 5, 10, 27, 49);
        // UsedRATType
        lastRATType = UsedRATType.geran;
        timeZone = new TimeZoneImpl(new byte[] {0, 6});

        subscriberInfo = new SubscriberInfoImpl(locationInformation, subscriberState, null, locationInformationGPRS,
                psSubscriberState, imei, msClassmark2, gprsmsClass, mnpInfoRes, ims, lastUEActivityTime, lastRATType, epsSubscriberState,
                locationInformationEPS, timeZone, daylightSavingTime, locationInformation5GS);

        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataPs7();

        assertTrue(Arrays.equals(rawData, encodedData));

        // test 8 (data taken from MAP load test for PS domain, containing location information GPRS/EPS/5GS and some
        // other parameters not found in test 7 such as EPS Subscriber State
        // LocationInformationGPRS
        locationInformationGPRS = null;
        // PSSubscriberState
        psSubscriberState = null;
        // IMEI
        imei = new IMEIImpl("011714004661051");
        // GPRSMSClass
        mSNetworkCapabilityB = hexStringToByteArray("3130303032303331");
        mSRadioAccessCapabilityB = hexStringToByteArray("31303030323033313730383134");
        msNetworkCapability = new MSNetworkCapabilityImpl(mSNetworkCapabilityB);
        msRadioAccessCapability = new MSRadioAccessCapabilityImpl(mSRadioAccessCapabilityB);
        gprsmsClass = new GPRSMSClassImpl(msNetworkCapability, msRadioAccessCapability);
        // MNPInfoRes
        assertEquals(mnpPortabilityStatus, NumberPortabilityStatus.ownNumberNotPortedOut);
        routeingNumber = new RouteingNumberImpl("491710");
        mnpImsi = new IMSIImpl("901405105680415");
        mnpMsisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "59899077937");
        mnpInfoRes = new MNPInfoResImpl(routeingNumber, mnpImsi, mnpMsisdn, mnpPortabilityStatus, null);
        // LastUEActivityTime
        lastUEActivityTime = new TimeImpl(2024, 8, 5, 10, 27, 49);
        // UsedRATType
        lastRATType = UsedRATType.eUtran;
        // EPSSubscriberState
        epsSubscriberState = new PSSubscriberStateImpl();
        PSSubscriberStateChoice epsSubscriberStateChoice = PSSubscriberStateChoice.psPDPActiveReachableForPaging;
        //netDetNotReachable = null;
        pdpContextInfoList = new ArrayList<>();
        pdpContextInfo = new PDPContextInfoImpl(pdpContextIdentifier, pdpContextActive, pdpType, pdpAddress,
                apnSubscribed, apnSubscribed, nsapi, transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested,
                qosNegotiated, chargingId, chargingCharacteristics, rncAddress, null, qos2Subscribed,
                qos2Requested, qos2Negotiated, null, null, null, null,
                null, null, extPdpType, extPdpAddress);
        pdpContextInfoList.add(pdpContextInfo);
        epsSubscriberState = new PSSubscriberStateImpl(epsSubscriberStateChoice, netDetNotReachable, pdpContextInfoList);
        // LocationInformationEPS
        eUtranCgi = new EUtranCgiImpl();
        eUtranCgi.setData(748, 1, 518658);
        taId = new TAIdImpl();
        taId.setData(748, 1, 109);
        saiPresent = false;
        //aol = 0;
        mmeName = new DiameterIdentityImpl("mmec03.mmegi3000.mme.epc.mnc002.mcc748.3gppnetwork.org".getBytes());
        locationInformationEPS = new LocationInformationEPSImpl(eUtranCgi, taId, null, null,
                geodeticInformation, currentLocationRetrieved, aol, mmeName);
        // TimeZone
        timeZone = new TimeZoneImpl(new byte[] {0, 6});

        subscriberInfo = new SubscriberInfoImpl(locationInformation, subscriberState, null, locationInformationGPRS,
                psSubscriberState, imei, msClassmark2, gprsmsClass, mnpInfoRes, ims, lastUEActivityTime, lastRATType, epsSubscriberState,
                locationInformationEPS, timeZone, daylightSavingTime, locationInformation5GS);

        asc = new ProvideSubscriberInfoResponseImpl(subscriberInfo, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataPs8();

        assertTrue(Arrays.equals(rawData, encodedData));

    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
