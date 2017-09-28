package com.pixelmags.android.download;

import android.util.Log;

import com.pixelmags.android.datamodels.Issue;
import com.pixelmags.android.storage.AllDownloadsDataSet;
import com.pixelmags.android.storage.IssueDataSet;
import com.pixelmags.android.util.BaseApp;

/**
 * Created by austincoutinho on 04/02/16.
 *
 * This class is used to insert an Issue, etc in the Download Queue
 *
 */
public class QueueDownload {


    public QueueDownload(){

    }

    public boolean insertIssueInDownloadQueue(String issueId) {

        try{
            Log.e("Issue Id Queue ==>",issueId);

            IssueDataSet mDbReader = new IssueDataSet(BaseApp.getContext());

            Issue mIssue = mDbReader.getIssue(mDbReader.getReadableDatabase(), issueId);

            mDbReader.close();

            if(mIssue != null){
                Log.e("Issue Id Object ==>",mIssue.toString());


                AllDownloadsDataSet mDownloadsDbReader = new AllDownloadsDataSet(BaseApp.getContext());

                boolean result = mDownloadsDbReader.issueDownloadPreChecksAndDownload(mDownloadsDbReader.getWritableDatabase(), mIssue);
                mDownloadsDbReader.close();


                if(result){

                    DownloadThumbnails.copyThumbnailOfIssueDownloaded(String.valueOf(mIssue.issueID));
                }
                return result;

            }else{
                Log.e("Download Failure ==>","Here");

            }




        }catch(Exception e){
            e.printStackTrace();
        }


        return false;

    }


}
