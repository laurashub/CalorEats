package com.example.rose.caloreats;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Restaurant implements Parcelable {

    public int rid;
    public String name;
    public String url;
    public ArrayList<String> locations;
    public ArrayList<String> phoneNumbers;

    public Restaurant(int id, String name_){
        this.rid=id;
        this.name=name_;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(rid);
        out.writeString(name);
        out.writeString(url);
        out.writeStringList(locations);
        out.writeStringList(phoneNumbers);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    private Restaurant(Parcel in) {
        rid = in.readInt();
        name = in.readString();
        url = in.readString();
        locations = in.createStringArrayList();
        phoneNumbers = in.createStringArrayList();
    }
}
