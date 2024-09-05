package com.bisindoku.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItems implements Parcelable {
    private String id;
    private String namaItem;
    private String linkVideo;

    // Constructor
    public MenuItems(String id, String namaItem, String linkVideo) {
        this.id = id;
        this.namaItem = namaItem;
        this.linkVideo = linkVideo;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    // Parcelable implementation
    protected MenuItems(Parcel in) {
        id = in.readString();  // Read the 'id' field
        namaItem = in.readString();
        linkVideo = in.readString();
    }

    public static final Parcelable.Creator<MenuItems> CREATOR = new Parcelable.Creator<MenuItems>() {
        @Override
        public MenuItems createFromParcel(Parcel in) {
            return new MenuItems(in);
        }

        @Override
        public MenuItems[] newArray(int size) {
            return new MenuItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);  // Write the 'id' field
        parcel.writeString(namaItem);
        parcel.writeString(linkVideo);
    }
}
