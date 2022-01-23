package com.example.insurancemobileapp.modelclasses;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.Hashtable;

public class AccidentInfo implements KvmSerializable, Serializable {
    private static final String START_DATE = "startDate";
    private static final String IS_STUDENT = "isStudent";
    private static final String PACK = "pack";

    private String startDate;
    private boolean isStudent;
    private int pack;

    public AccidentInfo() {}

    public AccidentInfo(String startDate, boolean isStudent, int pack) {
        this.startDate = startDate;
        this.isStudent = isStudent;
        this.pack = pack;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return startDate;
            case 1:
                return isStudent;
            case 2:
                return pack;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                startDate = o.toString();
                break;
            case 1:
                isStudent = Boolean.valueOf(o.toString());
                break;
            case 2:
                pack = Integer.parseInt(o.toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = START_DATE;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = IS_STUDENT;
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                break;
            case 2:
                propertyInfo.name = PACK;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            default:
                break;
        }
    }
}
