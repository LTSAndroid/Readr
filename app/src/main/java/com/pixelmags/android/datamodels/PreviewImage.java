package com.pixelmags.android.datamodels;


/*
* Created (rewritten) Austin Coutinho 4 Jan 2016
* Edited by Likith Ts Jun 2017
*/

import android.graphics.Bitmap;

public class PreviewImage
{
    public String previewImageURL;
    public int imageWidth;
    public int imageHeight;
    public Bitmap previewImageBitmap; // to store temporarily once downloaded
    public String issueId;

    public PreviewImage()
    {
        super();
    }

    public void setImageWidth(int value)
    {
        imageWidth = value;
    }

    public void setImageHeight(int value)
    {
        imageHeight = value;
    }


    public void setPreviewImageURL(String value)
    {
        previewImageURL = value;
    }

}

