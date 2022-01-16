package com.example.insurancemobileapp;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class TestClass implements KvmSerializable {
    private static final String X = "x";
    private static final String Y = "y";


    private int x;
    private String y;

    public TestClass() {

    }
    public TestClass(int x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch(i){
            case 0:
                x = Integer.parseInt(o.toString());
                break;
            case 1:
                y = o.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch(i){
            case 0:
                propertyInfo.name = X;
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            case 1:
                propertyInfo.name = Y;
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            default:
                break;
        }
    }
}
