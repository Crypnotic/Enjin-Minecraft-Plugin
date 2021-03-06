package com.enjin.rpc.mappings.mappings.shop;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class Variable {
    @Getter
    private String               name;
    @Getter
    private String               type;
    @Getter
    @SerializedName(value = "max_length")
    private Integer              maxLength;
    @SerializedName(value = "min_length")
    private Integer              minLength;
    @SerializedName(value = "max_value")
    private Integer              maxValue;
    @SerializedName(value = "min_value")
    private Integer              minValue;
    @Getter
    private Boolean              required;
    @Getter
    private Map<Integer, Option> options;
    @Getter
    @SerializedName(value = "pricemin")
    private Double               minPrice;
    @Getter
    @SerializedName(value = "pricemax")
    private Double               maxPrice;
}
