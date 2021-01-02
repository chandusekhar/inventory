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
public class ProvideList implements Serializable{

    @JsonProperty(value="$")
	private String $;
    
    @JsonProperty(value="Value")
    private String Value;

    @JsonProperty(value="Name")
    private String Name;
}
