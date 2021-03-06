package com.pixelmags.android.pixelmagsapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pixelmags.android.storage.AllDownloadsDataSet;


/*

Created : Austin Coutinho

Perform all large downloads in the background

Usage :
- download thumnails in batches

- download the issue in batches
    - pause issue download

- service to run at start
- end when the all the downloads are complete and the app is no longer being used

- run a batch download of a table
- Download via wifi only (make this setting available to the user)

 */

public class PMService extends Service {

    // This is the object that receives interactions from clients.
    private final IBinder mBinder = new LocalBinder();
    AllDownloadsDataSet allDownloads;
    DownloadsManager downloadsManager;
    DownloadManagerAsyncTask mDMTask;
    boolean DMTaskRunning = false;
    private String TAG = "PMService";


    public PMService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("PMService Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("PMService Stopped");
    }

    public void requestServiceShutdown(){
        stopSelf();
    }


    public void newDownloadRequested(){

        Log.d(TAG, " NEW Download NOTIFICATION Received");

        initiateDownloadsProcessing();

    }

    public void resumeDownloadsProcessing(){

        Log.d(TAG, "Resuming Download processing");
        initiateDownloadsProcessing();

    }

    private void initiateDownloadsProcessing(){

        // DMTaskCompleted is used to check if the Download task is still running;

        if(!DMTaskRunning)
        {
            Log.d(TAG,"Inside the if condition of DM Task running");
            DMTaskRunning = true;
            // proceed to process the downloads in the background
            mDMTask = new DownloadManagerAsyncTask();
            mDMTask.execute((String)null);

        }else{
            Log.d(TAG,"Inside the else condition of DM TASK");
            // just let the DownloadsManger know that a request is pending
            downloadsManager = DownloadsManager.getInstance();
            downloadsManager.setRequestPending();
        }
    }

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {

        public PMService getService() {
            return PMService.this;
        }

    }

    /**
     *
     * Represents an asynchronous task used to process the downloads table.
     *
     */
    public class DownloadManagerAsyncTask extends AsyncTask<String, String, Boolean> {

        DownloadManagerAsyncTask() {
            DMTaskRunning = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {

                DMTaskRunning = true;

                downloadsManager = DownloadsManager.getInstance();
                downloadsManager.processDownloadsTable();

                return true;

            }catch (Exception e){
                e.printStackTrace();
            }

            return false;

        }

        protected void onPostExecute(Boolean result) {
            DMTaskRunning = false;
        }

        @Override
        protected void onCancelled() {
            mDMTask = null;
        }
    }

}
