package org.restcomm.protocols.ss7.tools.simulator.tests.ati_psi_lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class TestServerStandardManMBean extends StandardMBean {

    public TestServerStandardManMBean(TestServerMan impl, Class<TestServerManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {

                new MBeanAttributeInfo("SRILCSReaction", SRILCSReaction.class.getName(), "SRILCS response type", true, true, false),
                new MBeanAttributeInfo("SRILCSReaction_value", String.class.getName(), "SRILCS response type", true, false, false),

                new MBeanAttributeInfo("PSLReaction", PSLReaction.class.getName(), "PSL response type", true, true, false),
                new MBeanAttributeInfo("PSLReaction_value", String.class.getName(), "PSL response type", true, false, false),

                new MBeanAttributeInfo("SLRReaction", PSLReaction.class.getName(), "SLR response type", true, true, false),
                new MBeanAttributeInfo("SLRReaction_value", String.class.getName(), "SLR response type", true, false, false),

                new MBeanAttributeInfo("SRISMReaction", SRISMReaction.class.getName(), "SRISM response type", true, true, false),
                new MBeanAttributeInfo("SRISMReaction_value", String.class.getName(), "SRISM response type", true, false, false),

                new MBeanAttributeInfo("PSIReaction", PSIReaction.class.getName(), "PSI response type", true, true, false),
                new MBeanAttributeInfo("PSIReaction_value", String.class.getName(), "PSI response type", true, false, false),

                new MBeanAttributeInfo("ATIReaction", ATIReaction.class.getName(), "ATI response type", true, true, false),
                new MBeanAttributeInfo("ATIReaction_Value", String.class.getName(), "ATI response type", true, false, false),

                new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(),
                        "AddressNature parameter for address creation (mlcNumber, networkNodeNumber, NaESRD)",
                        true, true, false),
                new MBeanAttributeInfo("NumberingPlanType", NumberingPlanMapType.class.getName(),
                        "NumberingPlanType parameter for address creation (numberingPlan, networkNodeNumber, NaESRD)",
                        true, true, false),
                new MBeanAttributeInfo("NumberingPlan", String.class.getName(),
                        "NumberingPlan parameter for mlcNumber address creation",
                        true, true, false),
                new MBeanAttributeInfo("NetworkNodeNumberAddress", String.class.getName(),
                        "NetworkNodeNumber address parameter",
                        true, true, false),
                new MBeanAttributeInfo("LCSEventType", LCSEventType.class.getName(),
                        "LCS Event parameter",
                        true, true, false),
                new MBeanAttributeInfo("IMEI", String.class.getName(),
                        "IMEI address parameter",
                        true, true, false),
                new MBeanAttributeInfo("IMSI", String.class.getName(),
                        "IMSI address parameter",
                        true, true, false),
                new MBeanAttributeInfo("MSISDN", String.class.getName(),
                        "MSISDN address parameter",
                        true, true, false),
                new MBeanAttributeInfo("HGMLCAddress", String.class.getName(),
                        "H-GMLC address parameter",
                        true, true, false),
                new MBeanAttributeInfo("LCSReferenceNumber", Integer.class.getName(),
                        "LCS Reference number parameter",
                        true, true, false),
                new MBeanAttributeInfo("MCC", Integer.class.getName(),
                        "MCC parameter for CellId Or SAI",
                        true, true, false),
                new MBeanAttributeInfo("MNC", Integer.class.getName(),
                        "MNC parameter for CellId Or SAI",
                        true, true, false),
                new MBeanAttributeInfo("LAC", Integer.class.getName(),
                        "LAC parameter for CellId Or SAI",
                        true, true, false),
                new MBeanAttributeInfo("CellId", Integer.class.getName(),
                        "CellId parameter for CellId Or SAI",
                        true, true, false),
                new MBeanAttributeInfo("LocationEstimate", ExtGeographicalInformation.class.getName(),
                        "Location Estimate parameter",
                        true, true, false),
                new MBeanAttributeInfo("AgeOfLocationEstimate", Integer.class.getName(),
                        "Age of Location Estimate parameter",
                        true, true, false),
                new MBeanAttributeInfo("NaESRDAddress", String.class.getName(),
                        "NaESRD address parameter for response",
                        true, true, false),

                new MBeanAttributeInfo("AddressNature", AddressNatureType.class.getName(),
                        "AddressNature parameter for AddressString creating", true, true, false),
                new MBeanAttributeInfo("AddressNature_Value", String.class.getName(),
                        "AddressNature parameter for AddressString creating", true, false, false),
                new MBeanAttributeInfo("NumberingPlan", NumberingPlanMapType.class.getName(),
                        "NumberingPlan parameter for AddressString creating", true, true, false),
                new MBeanAttributeInfo("NumberingPlan_Value", String.class.getName(),
                        "NumberingPlan parameter for AddressString creating", true, false, false),
                new MBeanAttributeInfo("ServiceCenterAddress", String.class.getName(),
                        "Origination Service center address string", true, true, false),
                new MBeanAttributeInfo("MapProtocolVersion", MapProtocolVersion.class.getName(), "MAP protocol version", true,
                        true, false),
                new MBeanAttributeInfo("MapProtocolVersion_Value", String.class.getName(), "MAP protocol version", true, false,
                        false),
                new MBeanAttributeInfo("NetworkNodeNumberAddress", String.class.getName(),
                        "NetworkNodeNumber address parameter",
                        true, true, false),
                new MBeanAttributeInfo("VMSCAddress", String.class.getName(),
                        "VMSC address parameter",
                        true, true, false),
                new MBeanAttributeInfo("IMSI", String.class.getName(),
                        "IMSI address parameter",
                        true, true, false),
                new MBeanAttributeInfo("AgeOfLocation", Integer.class.getName(),
                        "Age of Location Estimate parameter",
                        true, true, false),
                new MBeanAttributeInfo("GeographicalLatitude", String.class.getName(),
                        "GeographicalLatitude for response",
                        true, true, false),
                new MBeanAttributeInfo("GeographicalLongitude", String.class.getName(),
                        "GeographicalLatitude for response",
                        true, true, false),
                new MBeanAttributeInfo("GeographicalUncertainty", String.class.getName(),
                        "GeographicalLatitude for response",
                        true, true, false),
                new MBeanAttributeInfo("GeographicalConfidence", String.class.getName(),
                        "GeodeticLatitude for response",
                        true, true, false),
                new MBeanAttributeInfo("GeodeticLatitude", String.class.getName(),
                        "GeodeticLatitude parameter for response",
                        true, true, false),
                new MBeanAttributeInfo("GeodeticLongitude", String.class.getName(),
                        "GeodeticLatitude parameter for response",
                        true, true, false),
                new MBeanAttributeInfo("GeodeticUncertainty", String.class.getName(),
                        "GeodeticLatitude parameter for response",
                        true, true, false),
                new MBeanAttributeInfo("GeodeticConfidence", String.class.getName(),
                        "GeographicalLatitude parameter for response",
                        true, true, false),
                new MBeanAttributeInfo("GeodeticConfidence", String.class.getName(),
                        "GeographicalLatitude parameter for response",
                        true, true, false),
                new MBeanAttributeInfo("ScreeningAndPresentationIndicators", String.class.getName(),
                        "ScreeningAndPresentationIndicators parameter for response",
                        true, true, false),
        };

        MBeanParameterInfo[] performSRIRequestParam = new MBeanParameterInfo[]{
                new MBeanParameterInfo("addressIMSI", String.class.getName(), "Address for IMSI")};

        MBeanParameterInfo[] signString = new MBeanParameterInfo[]{
                new MBeanParameterInfo("val", String.class.getName(), "Index number or value")};

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {

                new MBeanOperationInfo("putLCSEventType", "Type parameter for LCS Event creating: "
                        + "0:emergencyCallOrigination,1:emergencyCallRelease,2:molr,3:deferredmtlrResponse", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo(
                        "putAddressNature",
                        "AddressNature parameter for AddressString creating: "
                                + "0:unknown,1:international_number,2:national_significant_number,3:network_specific_number,4:subscriber_number,5:reserved,6:abbreviated_number,7:reserved_for_extension",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putNumberingPlanType", "NumberingPlan parameter for AddressString creating: "
                        + "0:unknown,1:ISDN,2:spare_2,3:data,4:telex,5:spare_5,6:land_mobile,7:spare_7,8:national,9:private_plan,15:reserved", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performSendRoutingInfoForLCSRequest", "Send Routing Information for LCS request"
                        + "1:ReturnSuccess, 2:SystemFailure, 3:DataMissing, 4:UnexpectedDataValue, 5:FacilityNotSupported, 6:UnknownSubscriber , 7:AbsentSubscriber , "
                        + "8:UnauthorizedRequestingNetwork", performSRIRequestParam, String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performProvideSubscriberLocationRequest", "Provide Subscriber Location request"
                        + "1:ReturnSuccess, 2:SystemFailure, 3:DataMissing, 4:UnexpectedDataValue, 5:FacilityNotSupported, 6:UnidentifiedSubscriber , 7:IllegalSubscriber , "
                        + "8:IllegalEquipment, 9:AbsentSubscriber, 10:UnauthorizedRequestingNetwork, 11:UnauthorizedLCSClient, 12:PositionMethodFailure",
                        null, String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("performSubscriberLocationReportResponse", "Subscriber Location Report response"
                        + "1:ReturnSuccess, 2:SystemFailure, 3:DataMissing, 4:ResourceLimitation, 5:UnexpectedDataValue, 6:UnknownSubscriber, 7:UnauthorizedRequestingNetwork,"
                        + "8:UnknownOrUnreachableLCSClient", null, String.class.getName(), MBeanOperationInfo.ACTION),


                new MBeanOperationInfo("putATIReaction", "ATI response type: "
                        + "1:ReturnSuccess,3:ReturnSystemFailureError,4:ReturnDataMissingError,5:ReturnUnknownSubscriberError",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION)
        };

        return new MBeanInfo(TestServerMan.class.getName(), "MapLcsServer test parameters management", attributes, null, operations, null);
    }
}