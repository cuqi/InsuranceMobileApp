package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class AOInfo implements KvmSerializable, Serializable {
    private static final String REG_NUM = "regNum";
    private static final String CHASSIS = "chassis";
    private static final String IS_NEW = "isNew";
    private static final String KW = "KW";

    private String regNum;
    private String chassis;
    private int kw;
    private boolean isNew;

    public AOInfo() {}

    public AOInfo(String regNum, String chassis, int kw, boolean isNew) {
        this.regNum = regNum;
        this.chassis = chassis;
        this.kw = kw;
        this.isNew = isNew;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return regNum;
            case 1:
                return chassis;
            case 2:
                return kw;
            case 3:
                return isNew;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 4;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                regNum = o.toString();
                break;
            case 1:
                chassis = o.toString();
                break;
            case 2:
                kw = Integer.parseInt(o.toString());
                break;
            case 3:
                isNew = Boolean.valueOf(o.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = REG_NUM;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = CHASSIS;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = KW;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            case 3:
                propertyInfo.name = IS_NEW;
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                break;
            default:
                break;
        }
    }
}
