package com.pixelmags.android.pixelmagsapp.service;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;

import com.pixelmags.android.comms.Config;
import com.pixelmags.android.datamodels.AllDownloadsIssueTracker;
import com.pixelmags.android.datamodels.Issue;
import com.pixelmags.android.datamodels.PageTypeImage;
import com.pixelmags.android.datamodels.SingleDownloadIssueTracker;
import com.pixelmags.android.storage.AllDownloadsDataSet;
import com.pixelmags.android.storage.IssueDataSet;
import com.pixelmags.android.storage.SingleIssueDownloadDataSet;
import com.pixelmags.android.util.BaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by austincoutinho on 29/01/16.
 *
 * Class is a Singleton
 *
 * This class is responsible for managing all downloads
 *  - Check if any in queue
 *  - Assign the next issue to be in queue
 *  - Start, Stop, Pause download
 *  - Report Download progress
 *
 */
public class DownloadsManager {

    private static int DONE = 0;
    private static int PROCESSING = 1;

    private static int DOWNLOAD_MANAGER_STATUS;

    private boolean mPendingRequest = false;

    private static DownloadsManager instance = null;

    private boolean interrupted = false;

    // Queue will prioritise any page that has it's priority set
    static PriorityQueue<DownloadSinglePageThreadStatic> pageThreadQueue;

    // the tasks and parameters that run the task queues
    QueueProcessorAsyncTask mQueueProcessorTask;
    static boolean queueTaskCompleted = true;


    private DownloadsManager() {
        // Private prevent any other class from instantiating the DownloadManager

        pageThreadQueue = new PriorityQueue<DownloadSinglePageThreadStatic>(10, new Comparator<DownloadSinglePageThreadStatic>() {

            public int compare(DownloadSinglePageThreadStatic page1, DownloadSinglePageThreadStatic page2) {

                return (page1.getPriority() == page2.getPriority()) ? (Integer.valueOf(page1.getPageNo()).compareTo(page2.getPageNo()))
                        : (page1.getPriority() ? -1 : 1);

            }
        });

    }

    public static DownloadsManager getInstance() {
        if(instance == null) {
            instance = new DownloadsManager();
        }
        return instance;
    }

    public int getDownloadManagerStatus(){
        return DOWNLOAD_MANAGER_STATUS;
    }

    public void setRequestPending(){
        mPendingRequest = true;
    }

    public boolean processDownloadsTable(){

        AllDownloadsIssueTracker issueToDownload = null;

        try{
            issueToDownload = nextIssueInQueue();
            if(issueToDownload != null)
            {
                return startDownload(issueToDownload);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return false; // return false if nothing to process
    }

    public AllDownloadsIssueTracker nextIssueInQueue(){

        AllDownloadsIssueTracker issueToDownload = null;

        try{

            issueToDownload = fetchAnyDownloadRunning();
            if(issueToDownload != null){

                System.out.println("<<< ISSUE DOWNLOAD IN PROGRESS : "+ issueToDownload.issueTitle +" >>>");

                return issueToDownload;
            }

            AllDownloadsDataSet mDbReader = new AllDownloadsDataSet(BaseApp.getContext());
            issueToDownload = mDbReader.getNextIssueInQueue(mDbReader.getReadableDatabase(), Config.Magazine_Number);
            mDbReader.close();

            if(issueToDownload != null){
                System.out.println("<<< NEXT ISSUE TO DOWNLOAD "+ issueToDownload.issueTitle +" >>>");
            }



        }catch(Exception e){
            e.printStackTrace();
        }

        return issueToDownload;
    }


    public AllDownloadsIssueTracker fetchAnyDownloadRunning(){

        AllDownloadsIssueTracker issueDownloadInProgress = null;

        try{
            AllDownloadsDataSet mDbReader = new AllDownloadsDataSet(BaseApp.getContext());
            issueDownloadInProgress = mDbReader.getIssueDownloadInProgress(mDbReader.getReadableDatabase(), Config.Magazine_Number);
            mDbReader.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return issueDownloadInProgress;
    }


    public boolean startDownload(AllDownloadsIssueTracker issueToDownload){

        // start the threaded download here
        System.out.println("<<< START DOWNLOAD NOW FOR ISSUE "+ issueToDownload.issueTitle +" >>>");

        try{
            AllDownloadsDataSet mDbWriter = new AllDownloadsDataSet(BaseApp.getContext());

            // set the Issue as downloading within the AllDownloadTable
            boolean issueUpdated = mDbWriter.setIssueToInProgress(mDbWriter.getWritableDatabase(), issueToDownload);
            mDbWriter.close();

            if(issueUpdated){
                // get the Issue pages
                IssueDataSet mDbReader = new IssueDataSet(BaseApp.getContext());
                Issue issueWithPageData = mDbReader.getIssue(mDbReader.getReadableDatabase(), String.valueOf(issueToDownload.issueID));
                mDbReader.close();

                // and recreate / reload the table.
                if(issueWithPageData!=null){

                    ArrayList<SingleDownloadIssueTracker> pagesForSingleDownloadTable = new ArrayList<SingleDownloadIssueTracker>();

                    for(int i=0; i< issueWithPageData.pages.size();i++) {

                        PageTypeImage page = (PageTypeImage) issueWithPageData.pages.get(i);
                        PageTypeImage.PageDetails pageDetails = page.getPageDetails(PageTypeImage.MediaType.LARGE);

                        SingleDownloadIssueTracker pageTracker = new SingleDownloadIssueTracker(pageDetails, i);
                        pagesForSingleDownloadTable.add(pageTracker);
                    }

                    SingleIssueDownloadDataSet mDbDownloadTableWriter = new SingleIssueDownloadDataSet(BaseApp.getContext());
                    boolean result = mDbDownloadTableWriter.initFormationOfSingleIssueDownloadTable(mDbDownloadTableWriter.getWritableDatabase(), issueToDownload, pagesForSingleDownloadTable);
                    mDbDownloadTableWriter.close();

                    if(result){
                        createIssueThreads(issueToDownload);
                    }
                }
            }

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private void createIssueThreads(AllDownloadsIssueTracker issueToDownload){

        // get all the pages that have to be downloaded
        SingleIssueDownloadDataSet mDbDownloadReader = new SingleIssueDownloadDataSet(BaseApp.getContext());
        ArrayList<SingleDownloadIssueTracker> pagesForSingleDownloadTable = mDbDownloadReader.getSingleIssuePagesPendingDownload(mDbDownloadReader.getReadableDatabase(), issueToDownload.uniqueIssueDownloadTable);
        mDbDownloadReader.close();

        // create threads for each of them and insert them into the priority queue
        if(pagesForSingleDownloadTable != null){

            for(int i=0; i< pagesForSingleDownloadTable.size();i++) {

                DownloadSinglePageThreadStatic pageThread = new DownloadSinglePageThreadStatic();
                pageThread.setProcessingValues(issueToDownload, pagesForSingleDownloadTable.get(i), false);
                pageThreadQueue.add(pageThread);

            }

            launchQueueTask();

        }





        // check if any process tried to interrupt Threads
        if(!interrupted){

            // block any other issues to be downloaded at same time
            // prevent multiple launches of the Issue download
            // send a notification to the UI after each Thread, Page download
            // Mark Issue as download complete
            // On Issue download complete, recheck if further issues need to be downloaded

        }

    }


    // process the threads in batches.
    public void launchQueueTask(){

        if(queueTaskCompleted){
            // launch the queue task again

            //mQueueProcessorTask = new QueueProcessorAsyncTask();
            //mQueueProcessorTask.execute((String) null);

            QueueProcessorThread qThread = new QueueProcessorThread();
            Thread t1 = new Thread(qThread);
            t1.start();


        } // else do nothing as the queue will continue to process until it is empty


    }

    public boolean pauseDownload(){

        return false;
    }

    public boolean stopDownload(){

        return false;
    }


    /**
     *
     * Represents an asynchronous task used to process the downloads table.
     *
     */
    public class QueueProcessorAsyncTask extends AsyncTask<String, String, Boolean> {

        int MAX_THREADS = 3;
        boolean runQueue = true;

        QueueProcessorAsyncTask() {
            queueTaskCompleted = false;
        }

        public void interruptQueueProcessorAsyncTask(){
            // use later to pause, resume threads

        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {

                queueTaskCompleted = false;

                while(runQueue){

                    DownloadSinglePageThreadStatic testEmpty = pageThreadQueue.peek(); // returns null if the queue is empty

                    if(testEmpty == null) {
                        runQueue = false;
                        break;
                    }

                    ArrayList<Thread> allDownloadThreads = new ArrayList<Thread>();

                    for (int i = 0; i < MAX_THREADS; i++) {

                        // peek to check if the queue is empty
                        DownloadSinglePageThreadStatic executeThread = pageThreadQueue.poll();

                        if(executeThread == null) {
                            break;
                        }

                        Thread t1 = new Thread(executeThread);
                        allDownloadThreads.add(t1);
                    }

                    // start the threads
                    for (Thread thread : allDownloadThreads){
                        thread.start();
                    }

                    // wait for them to be completed
                    try{

                        for (Thread joinThread : allDownloadThreads){
                            joinThread.join();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    // while loop ends
                }
                return true;

            }catch (Exception e){
                e.printStackTrace();
            }

            return false;

        }

        protected void onPostExecute(Boolean result) {

            System.out.println("<< Queue Tasks Completed >>");
            queueTaskCompleted = true;
        }

        @Override
        protected void onCancelled() {
            mQueueProcessorTask = null;
        }
    }

    /**
     *
     * Represents an asynchronous task used to process the downloads table.
     *
     */
    public static class QueueProcessorThread implements Runnable {

        int MAX_THREADS = 1;
        boolean runQueue = true;

        @Override
        public void run() {

            try {

                queueTaskCompleted = false;

                while(runQueue){

                    DownloadSinglePageThreadStatic testEmpty = pageThreadQueue.peek(); // returns null if the queue is empty

                    if(testEmpty == null) {
                        runQueue = false;
                        break;
                    }

                    ArrayList<Thread> allDownloadThreads = new ArrayList<Thread>();

                    for (int i = 0; i < MAX_THREADS; i++) {

                        // peek to check if the queue is empty
                        DownloadSinglePageThreadStatic executeThread = pageThreadQueue.poll();

                        if(executeThread == null) {
                            break;
                        }

                        Thread t1 = new Thread(executeThread);
                        allDownloadThreads.add(t1);
                    }

                    // start the threads
                    for (Thread thread : allDownloadThreads){
                        thread.start();
                    }

                    // wait for them to be completed
                    try{

                        for (Thread joinThread : allDownloadThreads){
                            joinThread.join();
                        }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    // while loop ends
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            queueTaskCompleted = true;

        }


    }


    public static class DownloadSinglePageThreadStatic implements Runnable {

        // form Issues/(Magazine_number)/(issue number)/PDF/

        private static final String ISSUE_DIR_PREFIX_1 = "/Issues/"+ Config.Magazine_Number+"/";
        private static final String ISSUE_DIR_PREFIX_PDF = "/PDF";


        private boolean isPriority;
        private boolean isDownloaded;
        private AllDownloadsIssueTracker issueAllDownloadsTracker;
        private SingleDownloadIssueTracker pageSingleDownloadTracker;

        public void setProcessingValues(AllDownloadsIssueTracker allDownloadsTracker, SingleDownloadIssueTracker pageTracker, boolean setAsPriority){

            this.issueAllDownloadsTracker = allDownloadsTracker;
            this.pageSingleDownloadTracker = pageTracker;
            this.isPriority = setAsPriority; // this is the value that will be used in the Comparator to download the image as priority
            this.isDownloaded = false;

        }

        public boolean getPriority(){
            return isPriority;
        }

        public int getPageNo(){
            return pageSingleDownloadTracker.pageNo;
        }

        private String getPageFileName(){

            String fileName = String.valueOf(pageSingleDownloadTracker.pageNo)+".jpg";
            return fileName;
        }

        @Override
        public void run() {

            try {


                System.out.println("Download :: downloading page ---- "+ pageSingleDownloadTracker.pageNo );

                InputStream in = new URL(pageSingleDownloadTracker.urlPdfLarge).openStream();

                if(in != null){

                    ContextWrapper cw = new ContextWrapper(BaseApp.getContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    String pageImageDir = ISSUE_DIR_PREFIX_1 + issueAllDownloadsTracker.issueID + ISSUE_DIR_PREFIX_PDF;

                    File folder = new File(directory.getAbsolutePath() + pageImageDir);
                    folder.mkdirs();

                    // Create page image file, specifying the path, and the filename which we want to save the file as.
                    File pageImage = new File(folder, getPageFileName());

                    //this will be used to write the downloaded data into the file we created
                    FileOutputStream fileOutput = new FileOutputStream(pageImage);

                    //create a buffer
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0; //used to store a temporary size of the buffer

                    //read through the input buffer and write the contents to the file
                    while ( (bufferLength = in.read(buffer)) > 0 ) {
                        //add the data in the buffer to the file in the file output stream (the file on the sd card)
                        fileOutput.write(buffer, 0, bufferLength);
                    }
                    //close the output stream when done
                    fileOutput.close();

                    registerDownloadAsComplete();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // Update the UniqueDownloadTable of the Issue after each page download
        private void registerDownloadAsComplete(){

            this.isDownloaded = true;
            pageSingleDownloadTracker.downloadStatusPdfLarge = SingleIssueDownloadDataSet.DOWNLOAD_STATUS_COMPLETED;

            try{

            /* // DO not do update after every page as that locks the db out for a long time.
            SingleIssueDownloadDataSet mDbDownloadTableWriter = new SingleIssueDownloadDataSet(BaseApp.getContext());
            boolean result = mDbDownloadTableWriter.updateIssuePageEntry(mDbDownloadTableWriter.getWritableDatabase(), pageSingleDownloadTracker, issueAllDownloadsTracker.uniqueIssueDownloadTable);
            mDbDownloadTableWriter.close();
            */

                System.out.println("Download Complete :: " + pageSingleDownloadTracker.pageNo);

            }catch(Exception e){
                e.printStackTrace();
            }

        }


    }


}
