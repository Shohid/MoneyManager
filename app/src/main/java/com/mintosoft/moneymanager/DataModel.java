package com.mintosoft.moneymanager;

/**
 * Created by AKASH on 2/15/2017.
 */
public class DataModel {

    String name;
    String type;
    String feature;

    public DataModel(String name, String type,  String feature ) {
        this.name=name;
        this.type=type;
        this.feature=feature;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFeature() {
        return feature;
    }

}
