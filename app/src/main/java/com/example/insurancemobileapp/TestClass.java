package com.example.insurancemobileapp;

import android.os.Parcel;

import java.io.Serializable;

public class TestClass implements Serializable {
    private String x;
    private int y;

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(x);
//        out.writeInt(y);
//    }

//    public static final Parcelable.Creator<TestClass> CREATOR = new Parcelable.Creator<TestClass>() {
//        public TestClass createFromParcel(Parcel in) {
//            return new TestClass(in);
//        }
//
//        public TestClass[] newArray(int size) {
//            return new TestClass[size];
//        }
//    };

    private TestClass(Parcel in) {
        x = in.readString();
        y = in.readInt();
    }
    public TestClass() {

    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //    public TestClass(int x, String y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public Object getProperty(int i) {
//        switch (i) {
//            case 0:
//                return x;
//            case 1:
//                return y;
//            default:
//                break;
//        }
//        return null;
//    }
//
//    @Override
//    public int getPropertyCount() {
//        return 2;
//    }
//
//    @Override
//    public void setProperty(int i, Object o) {
//        switch(i){
//            case 0:
//                x = Integer.parseInt(o.toString());
//                break;
//            case 1:
//                y = o.toString();
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
//        switch(i){
//            case 0:
//                propertyInfo.name = X;
//                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
//                break;
//            case 1:
//                propertyInfo.name = Y;
//                propertyInfo.type = PropertyInfo.STRING_CLASS;
//                break;
//            default:
//                break;
//        }
//    }
}
