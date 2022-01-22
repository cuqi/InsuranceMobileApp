package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class HouseholdInfo implements KvmSerializable, Serializable {
    private static final String TYPE_OBJECT = "typeObject";
    private static final String AREA_OF_OBJECT = "areaOfObject";
    private static final String DATE_OF_OBJECT = "dateOfObject";
    private static final String TYPE_HOUSEHOLD_COVER = "typeHouseholdCover";
    private static final String CONTRACT_LENGHT = "contractLenght";


    private String typeObject;
    private String typeHouseholdCover;
    private int areaOfObject;
    private String dateOfObject;
    private int contractLenght;

    public HouseholdInfo() {}

    public HouseholdInfo(String typeObject, String typeHouseholdCover, int areaOfObject, String dateOfObject, int contractLenght) {
        this.typeObject = typeObject;
        this.typeHouseholdCover = typeHouseholdCover;
        this.areaOfObject = areaOfObject;
        this.dateOfObject = dateOfObject;
        this.contractLenght = contractLenght;
    }

    public String getTypeObject() {
        return typeObject;
    }

    public void setTypeObject(String typeObject) {
        this.typeObject = typeObject;
    }

    public String getTypeHouseholdCover() {
        return typeHouseholdCover;
    }

    public void setTypeHouseholdCover(String typeHouseholdCover) {
        this.typeHouseholdCover = typeHouseholdCover;
    }

    public int getAreaOfObject() {
        return areaOfObject;
    }

    public void setAreaOfObject(int areaOfObject) {
        this.areaOfObject = areaOfObject;
    }

    public String getDateOfObject() {
        return dateOfObject;
    }

    public void setDateOfObject(String dateOfObject) {
        this.dateOfObject = dateOfObject;
    }

    public int getContractLenght() {
        return contractLenght;
    }

    public void setContractLenght(int contractLenght) {
        this.contractLenght = contractLenght;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return typeObject;
            case 1:
                return typeHouseholdCover;
            case 2:
                return areaOfObject;
            case 3:
                return dateOfObject;
            case 4:
                return contractLenght;
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
                typeObject = o.toString();
                break;
            case 1:
                typeHouseholdCover = o.toString();
                break;
            case 2:
                areaOfObject = Integer.parseInt(o.toString());
                break;
            case 3:
                dateOfObject = o.toString();
                break;
            case 4:
                contractLenght = Integer.parseInt(o.toString());
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = TYPE_OBJECT;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = TYPE_HOUSEHOLD_COVER;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = AREA_OF_OBJECT;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            case 3:
                propertyInfo.name = DATE_OF_OBJECT;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 4:
                propertyInfo.name = CONTRACT_LENGHT;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            default:
                break;
        }
    }
}