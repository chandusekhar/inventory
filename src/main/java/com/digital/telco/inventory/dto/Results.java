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
public class Results implements Serializable{

	@JsonProperty(value = "HasPurchasedItemCycle")
	private String HasPurchasedItemCycle;

	@JsonProperty(value = "Rev")
	private String Rev;

	@JsonProperty(value = "Description")
	private String Description;

	@JsonProperty(value = "EffectiveTime")
	private String EffectiveTime;

	@JsonProperty(value = "TemplateVersion")
	private String TemplateVersion;

	@JsonProperty(value = "IsOneTime")
	private String IsOneTime;

	@JsonProperty(value = "TemplateDescription")
	private String TemplateDescription;

	@JsonProperty(value = "Correction")
	private String Correction;

	@JsonProperty(value = "TemplateRev")
	private String TemplateRev;

	@JsonProperty(value = "Name")
	private String Name;

	@JsonProperty(value = "TemplateVersionDescription")
	private String TemplateVersionDescription;

	@JsonProperty(value = "ProvideList")
	private List<ProvideList> ProvideList;

	@JsonProperty(value = "TemplateName")
	private String TemplateName;

	@JsonProperty(value = "TemplateId")
	private String TemplateId;

	@JsonProperty(value = "PurchasePreActiveAllowed")
	private String PurchasePreActiveAllowed;

	@JsonProperty(value = "$")
	private String $;

	@JsonProperty(value = "DeferredSettlementPurchaseAllowed")
	private String DeferredSettlementPurchaseAllowed;
	
	@JsonProperty(value = "ExternalId")
	private String ExternalId;
	
	@JsonProperty(value = "TemplateVersionName")
	private String TemplateVersionName;
	
	@JsonProperty(value = "TemplateType")
	private String TemplateType;
	
	@JsonProperty(value = "TemplateEffectiveTime")
	private String TemplateEffectiveTime;
	
	@JsonProperty(value = "TemplateExternalId")
	private String TemplateExternalId;
	
	@JsonProperty(value = "MetadataList")
	private List<MetadataList> MetadataList;

	@JsonProperty(value = "CatalogItemId")
	private String CatalogItemId;
	
	@JsonProperty(value = "TemplateCorrection")
	private String TemplateCorrection;
	
	@JsonProperty(value = "CycleOffsetType")
	private String CycleOffsetType;
	
	@JsonProperty(value = "CyclePeriod")
	private String CyclePeriod;
	
	@JsonProperty(value = "TemplateAttr")
	private TemplateAttr TemplateAttr;
	
	@JsonProperty(value = "CyclePeriodInterval")
	private String CyclePeriodInterval;
	
	@JsonProperty(value = "CyclePurchaseInfo")
	private CyclePurchaseInfo CyclePurchaseInfo;
	
	@JsonProperty(value = "IsPurchaseOffsetAllowed")
	private String IsPurchaseOffsetAllowed;
}
