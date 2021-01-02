package com.digital.telco.inventory.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateAttr implements Serializable{

    @JsonProperty(value="AddlInfo1")
	private String AddlInfo1;

    @JsonProperty(value="AddlInfo2")
    private String AddlInfo2;

    @JsonProperty(value="SerialNo")
    private String SerialNo;

    @JsonProperty(value="CardFaceValue")
    private String CardFaceValue;

    @JsonProperty(value="$")
    private String $;

    @JsonProperty(value="Remarks")
    private String Remarks;

    @JsonProperty(value="VendorId")
    private String VendorId;

    @JsonProperty(value="ChannelId")
    private String ChannelId;

    @JsonProperty(value="OfferType")
    private String OfferType;

    @JsonProperty(value="TransactionId")
    private String TransactionId;

    @JsonProperty(value="VoucherType")
    private String VoucherType;
    
    @JsonProperty(value="Amount")
    private Double Amount;
    

    @JsonProperty(value="ServiceFee")
    private Double ServiceFee;
    
    @JsonProperty(value="CurrencyGrant")
    private Double CurrencyGrant;
    
    @JsonProperty(value="ListName")
    private String ListName;
    
    @JsonProperty(value="ServiceAction")
    private String ServiceAction;
    
    @JsonProperty(value="SourceMSISDN")
    private String SourceMSISDN;
    
    @JsonProperty(value="TargetMSISDN")
    private String TargetMSISDN;
    
    @JsonProperty(value="TransferType")
    private String TransferType;
    
    
    @JsonProperty(value="ExternalProductId")
    private String ExternalProductId;
    
    @JsonProperty(value="DataCharge")
    private Double DataCharge;
    
    @JsonProperty(value="DataGrant")
    private Double DataGrant;
    
    @JsonProperty(value="CurrencyCharge")
    private Double CurrencyCharge;
    
    @JsonProperty(value="ApplicationCharge")
    private String ApplicationCharge;
    
    @JsonProperty(value="ApplicationGrant")
    private String ApplicationGrant;
    
    @JsonProperty(value="ApplicationList")
    private List<String> ApplicationList;
    
    @JsonProperty(value="IntlTextCharge")
    private String IntlTextCharge;
    
    @JsonProperty(value="IntlTextGrant")
    private String IntlTextGrant;
    
    @JsonProperty(value="IntlTextRegionList")
    private List<String> IntlTextRegionList;
    
    @JsonProperty(value="IntlVoiceCharge")
    private String IntlVoiceCharge;
    
    @JsonProperty(value="IntlVoiceGrant")
    private String IntlVoiceGrant;
    
    @JsonProperty(value="IntlVoiceRegionList")
    private List<String> IntlVoiceRegionList;
    
    @JsonProperty(value="RoamingDataCharge")
    private String RoamingDataCharge;
    
    @JsonProperty(value="RoamingDataGrant")
    private String RoamingDataGrant;
    
    @JsonProperty(value="RoamingDataRegionList")
    private List<String> RoamingDataRegionList; 
    
    @JsonProperty(value="RoamingVoiceCharge")
    private String RoamingVoiceCharge;
    
    @JsonProperty(value="RoamingVoiceGrant")
    private String RoamingVoiceGrant;
    
    @JsonProperty(value="RoamingVoiceRegionList")
    private List<String> RoamingVoiceRegionList;
    
    
    
}
