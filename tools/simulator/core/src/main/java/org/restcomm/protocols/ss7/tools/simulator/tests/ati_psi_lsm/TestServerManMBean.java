package org.restcomm.protocols.ss7.tools.simulator.tests.ati_psi_lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public interface TestServerManMBean {

    /** Operations **/

    String performSendRoutingInfoForLCSResponse();

    void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequest);

    String performSubscriberLocationReportRequest(Boolean refNum);

    String performSendRoutingInfoForSMResponse();

    String performSendRoutingInformationResponse();

    void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest provideSubscriberInfoRequest);

    String performProvideSubscriberInfoResponse();

    /** Attributes **/

    AddressNatureType getAddressNature();

    String getNumberingPlan();

    NumberingPlanMapType getNumberingPlanType();

    void setAddressNature(AddressNatureType addressNatureType);

    void setNumberingPlan(String numberingPlan);

    void setNumberingPlanType(NumberingPlanMapType numberingPlanMapType);

    String getMlcNumber();

    void setMlcNumber(String mlcNumber);

    void putAddressNature(String addressNature);

    void putNumberingPlanType(String NumberingPlanType);

    String getNetworkNodeNumber();

    void setNetworkNodeNumber(String networkNodeNumber);

    String getVmscAddress();

    void setVmscAddress(String vmscAddress);

    String getImsi();

    void setImsi(String imsi);

    String getLmsi();

    void setLmsi(String lmsi);

    int getMcc();

    void setMcc(int mcc);

    int getMnc();

    void setMnc(int mnc);

    int getLac();

    void setLac(int lac);

    int getCi();

    void setCi(int ci);

    int getAol();

    void setAol(int aol);

    boolean isSaiPresent();

    void setSaiPresent(boolean saiPresent);

    double getGeographicalLatitude();

    void setGeographicalLatitude(double geographicalLatitude);

    double getGeographicalLongitude();

    void setGeographicalLongitude(double geographicalLongitude);

    double getGeographicalUncertainty();

    void setGeographicalUncertainty(double geographicalUncertainty) ;

    int getScreeningAndPresentationIndicators();

    void setScreeningAndPresentationIndicators(int screeningAndPresentationIndicators);

    double getGeodeticLatitude();

    void setGeodeticLatitude(double geodeticLatitude);

    double getGeodeticLongitude();

    void setGeodeticLongitude(double geodeticLongitude);

    double getGeodeticUncertainty();

    void setGeodeticUncertainty(double geodeticUncertainty);

    int getGeodeticConfidence();

    void setGeodeticConfidence(int geodeticConfidence);

    boolean isCurrentLocationRetrieved();

    void setCurrentLocationRetrieved(boolean currentLocationRetrieved);

    String getImei();

    void setImei(String imei);


    SRISMReaction getSRISMReaction();

    String getSRIReaction_Value();

    void setSRISMReaction(SRISMReaction val);

    void putSRIReaction(String val);

    PSIReaction getPSIReaction();

    String getPSIReaction_Value();

    void setPSIReaction(PSIReaction val);

    void putPSIReaction(String val);

    String getCurrentRequestDef();

    /** PSL Request **/

    Double getLocationEstimateLatitude();

    void setLocationEstimateLatitude(Double locationEstimateLatitude);

    Double getLocationEstimateLongitude();

    void setLocationEstimateLongitude(Double locationEstimateLongitude);

    TypeOfShapeEnumerated getTypeOfShape();

    void setTypeOfShapeEnumerated(TypeOfShapeEnumerated typeOfShapeEnumerated);

    LocationEstimateTypeEnumerated getLocEstimateType();

    void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate);


    /** SLR Request (some apply to PSL too) **/

    String getHGMLCAddress();

    void setHGMLCAddress(String hgmlcAddress);

    String getIMEI();

    void setIMEI(String imei);

    String getIMSI();

    void setIMSI(String imsi);

    String getLMSI();

    void setLMSI(String lmsi);

    Integer getCellId();

    void setCellId(Integer lac);

    Integer getLAC();

    void setLAC(Integer lac);

    void setAgeOfLocationEstimate(Integer ageOfLocationEstimate);

    Integer getAgeOfLocationEstimate();

    LCSEvent getLCSEvent();

    void setLCSEvent(LCSEvent lcsEvent);

    LCSEventType getLCSEventType();

    void setLCSEventType(LCSEventType val);

    Integer getLCSReferenceNumber();

    void setLCSReferenceNumber(Integer lcsReferenceNumber);

    Integer getMCC();

    void setMCC(Integer mcc);

    Integer getMNC();

    void setMNC(Integer mnc);

    String getMSISDN();

    void setMSISDN(String msisdn);

    Integer getLcsServiceTypeID();

    void setLcsServiceTypeID(Integer lcsServiceTypeID);

    boolean getMoLrShortCircuitIndicator();

    void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator);

    LCSClientTypeEnumerated getLcsClientTypeEnumerated();

    void setLcsClientTypeEnumerated(LCSClientTypeEnumerated lcsClientType);

    void setCodeWordUSSDString(String codeWordUSSDString);

    String getCodeWordUSSDString();

    void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated privacyCheckRelatedActionEnumerated);

    PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated();

    void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated privacyCheckRelatedActionEnumerated);

    PrivacyCheckRelatedActionEnumerated getCallSessionRelated();

    void setAreaType(AreaTypeEnumerated areaTypeEnumerated);

    AreaTypeEnumerated getAreaType();

    void setOccurrenceInfo(OccurrenceInfoEnumerated occurrenceInfoEnumerated);

    OccurrenceInfoEnumerated getOccurrenceInfo();

    void setIntervalTime(Integer intervalTime);

    Integer getIntervalTime();

    void setReportingAmount(Integer reportingAmount);

    Integer getReportingAmount();

    void setReportingInterval(Integer reportingInterval);

    Integer getReportingInterval();

    void setDataCodingScheme(Integer dataCodingScheme);

    Integer getDataCodingScheme();


    /** SLR Response **/

    String getNaESRDAddress();

    void setNaESRDAddress(String address);


    /** Others **/

    SRILCSReaction getSRILCSReaction();

    String getSRILCSReaction_Value();

    void setSRILCSReaction(SRILCSReaction val);

    void putSRILCSReaction(String val);

    PSLReaction getPSLReaction();

    String getPSLReaction_Value();

    void setPSLReaction(PSLReaction val);

    void putPSLReaction(String val);

    SLRReaction getSLRReaction();

    String getSLRReaction_Value();

    void setSLRReaction(SLRReaction val);

    void putSLRReaction(String val);


    /** Methods for configurable properties via HTTP interface for values that are based on EnumeratedBase abstract class **/

    void putLCSEventType(String lcsEventType);

    ATIReaction getATIReaction();

    void setATIReaction(ATIReaction val);
}
