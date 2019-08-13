package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    //creating new objects, individually or as arrays
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {

        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }

    };

    private int id;
    private String shortDescription;
    private String videoURL;
    private String description;
    private String thumbnailURL;

    public Step(int id, String shortDescription, String videoURL, String description, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.videoURL = videoURL;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
    }

    //reconstructing user object from parcel
    public Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        videoURL = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //object serialization
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(videoURL);
        dest.writeString(description);
        dest.writeString(thumbnailURL);
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
