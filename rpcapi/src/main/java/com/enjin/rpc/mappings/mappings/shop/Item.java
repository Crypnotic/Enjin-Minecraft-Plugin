package com.enjin.rpc.mappings.mappings.shop;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class Item {
    @Getter
    private Integer                id;
    @Getter
    private String                 name;
    @Getter
    private String                 info;
    @Getter
    private Map<Integer, Variable> variables;
    @Getter
    @SerializedName(value = "icon_damage")
    private Byte                   iconDamage;
    @Getter
    @SerializedName(value = "icon_item")
    private String                 iconItem;
    @Getter
    private Double                 price;
    @Getter
    private Integer                points;
}
