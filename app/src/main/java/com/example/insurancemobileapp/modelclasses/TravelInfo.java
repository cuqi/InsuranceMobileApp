package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class TravelInfo implements KvmSerializable, Serializable {
    private static final String TYPE_TRAVEL_POLICY = "type";
    private static final String TYPE_COVER = "cover";
    private static final String INSURED_DAYS = "days";
    private static final String COUNTRY = "country";
    private static final String NUM_PEOPLE = "numPeople";
    
    private String typeTravelPolicy;
    private String typeCover;
    private int insuredDays;
    private String country;
    private int numPeople;

    public TravelInfo() {}

    public TravelInfo(String typeTravelPolicy, String typeCover, int insuredDays, String country, int numPeople) {
        this.typeTravelPolicy = typeTravelPolicy;
        this.typeCover = typeCover;
        this.country = country;
        this.insuredDays = insuredDays;
        this.numPeople = numPeople;
    }

    public String getTypeTravelPolicy() {
        return typeTravelPolicy;
    }

    public void setTypeTravelPolicy(String typeTravelPolicy) {
        this.typeTravelPolicy = typeTravelPolicy;
    }

    public String getTypeCover() {
        return typeCover;
    }

    public void setTypeCover(String typeCover) {
        this.typeCover = typeCover;
    }

    public int getInsuredDays() {
        return insuredDays;
    }

    public void setInsuredDays(int insuredDays) {
        this.insuredDays = insuredDays;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return typeTravelPolicy;
            case 1:
                return typeCover;
            case 2:
                return insuredDays;
            case 3:
                return country;
            case 4:
                return numPeople;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 5;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                typeTravelPolicy = o.toString();
                break;
            case 1:
                typeCover = o.toString();
                break;
            case 2:
                insuredDays = Integer.parseInt(o.toString());
                break;
            case 3:
                country = o.toString();
                break;
            case 4:
                numPeople = Integer.parseInt(o.toString());
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = TYPE_TRAVEL_POLICY;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = TYPE_COVER;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = INSURED_DAYS;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            case 3:
                propertyInfo.name = COUNTRY;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 4:
                propertyInfo.name = NUM_PEOPLE;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            default:
                break;
        }
    }
}