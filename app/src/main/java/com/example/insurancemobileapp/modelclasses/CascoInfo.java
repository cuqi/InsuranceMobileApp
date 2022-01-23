package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class CascoInfo implements KvmSerializable, Serializable {
    private static final String TYPE_CASCO = "typeCasco";
    private static final String WINDOWS = "windows";
    private static final String VEHICLE_PRICE = "vehiclePrice";
    private static final String TYPE_VALUE = "typeValue";
    private static final String FRANCHISE = "franchise";

    private String typeCasco;
    private boolean windows;
    private int vehiclePrice;
    private String typeValue;
    private int franchise;

    public CascoInfo() {}

    public CascoInfo(String typeCasco, boolean windows, int vehiclePrice, String typeValue, int franchise) {
        this.typeCasco = typeCasco;
        this.windows = windows;
        this.vehiclePrice = vehiclePrice;
        this.typeValue = typeValue;
        this.franchise = franchise;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return typeCasco;
            case 1:
                return windows;
            case 2:
                return vehiclePrice;
            case 3:
                return typeValue;
            case 4:
                return franchise;
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
                typeCasco = o.toString();
                break;
            case 1:
                windows = Boolean.valueOf(o.toString());
                break;
            case 2:
                vehiclePrice = Integer.parseInt(o.toString());
                break;
            case 3:
                typeValue = o.toString();
                break;
            case 4:
                franchise = Integer.parseInt(o.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = TYPE_CASCO;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = WINDOWS;
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                break;
            case 2:
                propertyInfo.name = VEHICLE_PRICE;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            case 3:
                propertyInfo.name = TYPE_VALUE;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 4:
                propertyInfo.name = FRANCHISE;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            default:
                break;
        }
    }
}
