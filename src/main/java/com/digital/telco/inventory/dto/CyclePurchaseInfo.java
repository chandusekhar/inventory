package com.digital.telco.inventory.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CyclePurchaseInfo implements Serializable {

    @JsonProperty(value="$")
	private String $;

    @JsonProperty(value = "IsRecurringFailureOverrideAllowed")
    private String IsRecurringFailureOverrideAllowed;
    
    @JsonProperty(value = "IsRecurringFailureAllowed")
    private String IsRecurringFailureAllowed;
	
}
