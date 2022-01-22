package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class InsuredInfo implements KvmSerializable {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String SSN = "ssn";
    private static final String ADDRESS = "address";
    private static final String POSTAL_CODE = "postalCode";
    private static final String CITY = "city";

    private String first_name;
    private String last_name;
    private String ssn;
    private String address;
    private String postal_code;
    private String city;

    public InsuredInfo() {

    }

    public InsuredInfo(String first_name, String last_name, String ssn, String address, String postal_code, String city) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.ssn = ssn;
        this.address = address;
        this.postal_code = postal_code;
        this.city = city;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return first_name;
            case 1:
                return last_name;
            case 2:
                return ssn;
            case 3:
                return address;
            case 4:
                return postal_code;
            case 5:
                return city;
            default:
                break;
        }
        return null;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                first_name = o.toString();
                break;
            case 1:
                last_name = o.toString();
                break;
            case 2:
                ssn = o.toString();
                break;
            case 3:
                address = o.toString();
                break;
            case 4:
                postal_code = o.toString();
            case 5:
                city = o.toString();
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = FIRST_NAME;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = LAST_NAME;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = SSN;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 3:
                propertyInfo.name = ADDRESS;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 4:
                propertyInfo.name = POSTAL_CODE;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 5:
                propertyInfo.name = CITY;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
            default:
                break;
        }
    }

    @Override
    public int getPropertyCount() {
        return 6;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
