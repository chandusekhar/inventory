package com.digital.telco.inventory.dto;

import java.io.Serializable;

import org.springframework.core.serializer.Deserializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataList implements Serializable {
	
    @JsonProperty(value = "Type")
	private String Type;

    @JsonProperty(value="$")
	private String $;

    @JsonProperty(value="Value")
	private String Value;

    @JsonProperty(value="Name")
  //  @JsonDeserialize(using = DeserializerUtf8.class)
	private String Name;
}
