package com.pixelmags.android.datamodels;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.pixelmags.android.storage.AllDownloadsDataSet;

import java.lang.reflect.Array;
import java.util.Date;

/**
 * Created by Annie on 10/10/15.
 */

/*
    This datamodel represents an issue in the Store View
 */
public class Magazine implements Parcelable
{
    public static String STATUS_BUY = "Buy";
    public static String STATUS_DOWNLOAD = "Download";
    public static String STATUS_VIEW = "View";
    public static String STATUS_PRICE = "price";
    public static String STATUS_FREE="free";
    public static String STATUS_QUEUE = "In Queue";
    public static String STATUS_PAUSED = "Paused";
    //
    public int id;
    public String title;
    public String synopsis;
//    public int magazineId;
    public String price;
    public String android_store_sku;
    public String type;
    public String manifest;
    public Date lastModified;
    public String issueDate;
    public boolean isPublished;
    public boolean removeFromSale;
    public String ageRestriction;
    public String exclude_from_subscription;
    public String paymentProvider;

    public int state;
    public String region;
    public String mediaFormat;
    public String thumbnailURL;
    public Boolean inAnytime;
    public boolean isIssueOwnedByUser = false;
//    public String issueId;
    public String status = Magazine.STATUS_PRICE;
//    public Object data;
    public Number sortOrder;
    public Array thumbs;
    public Object sortedThumbs;
    public Array renditions;
    public String magazine_id;


    // runtime variable to store download status
    public int currentDownloadStatus = AllDownloadsDataSet.DOWNLOAD_STATUS_NONE;

    public Bitmap thumbnailBitmap; // to store temporarily once downloaded
    public String thumbnailDownloadedInternalPath;
    public boolean isThumbnailDownloaded;

    public AllDownloadsIssueTracker issueDownloadData; // for the download fragment

    public Magazine(Parcel in) {
        id = in.readInt();
        title = in.readString();
        synopsis = in.readString();
        price = in.readString();
        android_store_sku = in.readString();
        type = in.readString();
        manifest = in.readString();
        issueDate = in.readString();
        isPublished = in.readByte() != 0;
        removeFromSale = in.readByte() != 0;
        ageRestriction = in.readString();
        exclude_from_subscription = in.readString();
        paymentProvider = in.readString();
        state = in.readInt();
        region = in.readString();
        mediaFormat = in.readString();
        thumbnailURL = in.readString();
        isIssueOwnedByUser = in.readByte() != 0;
        status = in.readString();
        currentDownloadStatus = in.readInt();
        thumbnailBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        thumbnailDownloadedInternalPath = in.readString();
        isThumbnailDownloaded = in.readByte() != 0;
        magazine_id = in.readString();

    }

    public static final Creator<Magazine> CREATOR = new Creator<Magazine>() {
        @Override
        public Magazine createFromParcel(Parcel in) {
            return new Magazine(in);
        }

        @Override
        public Magazine[] newArray(int size) {
            return new Magazine[size];
        }
    };

    public Magazine() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(price);
        dest.writeString(magazine_id);
        dest.writeString(android_store_sku);
        dest.writeString(type);
        dest.writeString(manifest);
        dest.writeString(issueDate);
        dest.writeByte((byte) (isPublished ? 1 : 0));
        dest.writeByte((byte) (removeFromSale ? 1 : 0));
        dest.writeString(ageRestriction);
        dest.writeString(exclude_from_subscription);
        dest.writeString(paymentProvider);
        dest.writeInt(state);
        dest.writeString(region);
        dest.writeString(mediaFormat);
        dest.writeString(thumbnailURL);
        dest.writeByte((byte) (isIssueOwnedByUser ? 1 : 0));
        dest.writeString(status);
        dest.writeInt(currentDownloadStatus);
        dest.writeParcelable(thumbnailBitmap, flags);
        dest.writeString(thumbnailDownloadedInternalPath);
        dest.writeByte((byte) (isThumbnailDownloaded ? 1 : 0));
    }
}
