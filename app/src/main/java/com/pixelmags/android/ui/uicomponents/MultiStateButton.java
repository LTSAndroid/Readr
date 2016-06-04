package com.pixelmags.android.ui.uicomponents;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.android.storage.AllDownloadsDataSet;

/**
 * Created by austincoutinho on 26/02/16.
 */
public class MultiStateButton extends Button {


    public MultiStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAsPurchase(String purchaseString){

        setBackgroundResource(R.drawable.multibuttonbaseshape);
        setText(purchaseString);

    }

    public void setAsDownload(String download){

        setBackgroundResource(R.drawable.multibuttonbaseshape);
        setText(download);

    }

    public void updateDownloadButtonState(int state){

        setBackgroundResource(R.drawable.multibuttonbaseshape);


    }


    public void setAsView(String view){

        setBackgroundResource(R.drawable.multibuttonviewshape);
        setText(view);
//        setText(BaseApp.getContext().getString(R.string.view)); // Commented for new changes
//        setText(getContext().getString(R.string.view));
    }


    public void setButtonState(Magazine mMagazine){

        if(mMagazine.isIssueOwnedByUser && mMagazine.currentDownloadStatus == AllDownloadsDataSet.DOWNLOAD_STATUS_COMPLETED
                || mMagazine.currentDownloadStatus == AllDownloadsDataSet.DOWNLOAD_STATUS_IN_PROGRESS){
            mMagazine.status = Magazine.STATUS_VIEW;
            setAsView(Magazine.STATUS_VIEW);
        }else if(mMagazine.isIssueOwnedByUser && mMagazine.currentDownloadStatus == AllDownloadsDataSet.DOWNLOAD_STATUS_NONE){
            mMagazine.status = Magazine.STATUS_DOWNLOAD;
            setAsDownload(mMagazine.STATUS_DOWNLOAD);
        }else if(mMagazine.status == Magazine.STATUS_DOWNLOAD){
            setAsDownload(mMagazine.STATUS_DOWNLOAD);
        }else if(mMagazine.paymentProvider.trim().equalsIgnoreCase("free")){
            mMagazine.status = Magazine.STATUS_VIEW;
            setAsView(Magazine.STATUS_VIEW);
        }else{
            mMagazine.status = Magazine.STATUS_PRICE;
            setAsPurchase(mMagazine.price);
        }

    }

//    public void setButtonState(String status){
//        if(status == Magazine.STATUS_VIEW){
//            setAsView(Magazine.STATUS_VIEW);
//        }
//    }


}
