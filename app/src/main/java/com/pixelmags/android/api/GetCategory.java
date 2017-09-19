package com.pixelmags.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pixelmags.android.bean.CategoryBean;
import com.pixelmags.android.comms.Config;
import com.pixelmags.android.comms.WebRequest;
import com.pixelmags.android.download.QueueDownload;
import com.pixelmags.android.json.GetIssueParser;

import com.pixelmags.android.storage.IssueDataSet;
import com.pixelmags.android.storage.UserPrefs;
import com.pixelmags.android.util.BaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;

import static android.content.Context.MODE_PRIVATE;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;

/**
 * Created by austincoutinho on 04/10/15.
 */

public class GetCategory extends WebRequest
{
    private static final String API_NAME="getCategories";
    private static boolean failure = false;
    GetIssueParser getIssueParser;

    private String TAG = "GetCategory";

    public GetCategory(){
        super(API_NAME);
    }

    public static boolean setGetIssueFailure(){
        return failure;
    }


    public String init(String issueID) {

        Log.e("Category ===","<==================>");



        setApiNameValuePairs();
        doPostRequest();

        System.out.println("RESPONSE CODE "+responseCode+" data::" +getAPIResultData());

       /* if(responseCode==200){
            getIssueParser = new GetIssueParser(getAPIResultData());
            if(getIssueParser.initJSONParse()){

                Log.d(TAG," value of is success is : "+getIssueParser.isSuccess());

                if(getIssueParser.isSuccess()){
                    getIssueParser.parse();
                    failure = false;


                    saveIssueToApp();


                } else{

                    Log.d(TAG,"Inside the error condition");

                    // Add error handling code here
                    failure = true;

                }

            }
        }*/

       return doPostRequest();


    }

    private void setApiNameValuePairs(){

        requestBody = new FormBody.Builder()
                //.add("auth_email_address", UserPrefs.getUserEmail())
                //.add("auth_password", UserPrefs.getUserPassword())
                //.add("device_id", UserPrefs.getDeviceID())
                //.add("magazine_id", Config.Magazine_Number)
                //.add("issue_id", mIssueID)
                //.add("app_bundle_id", Config.Bundle_ID)
                .add("api_mode", Config.api_mode)
                .add("api_version", Config.api_version)
                .build();



//        baseApiNameValuePairs = new ArrayList<NameValuePair>(8);
//        baseApiNameValuePairs.add(new BasicNameValuePair("auth_email_address", UserPrefs.getUserEmail()));
//        baseApiNameValuePairs.add(new BasicNameValuePair("auth_password", UserPrefs.getUserPassword()));
//        baseApiNameValuePairs.add(new BasicNameValuePair("device_id", UserPrefs.getDeviceID()));
//        baseApiNameValuePairs.add(new BasicNameValuePair("magazine_id", Config.Magazine_Number));
//        baseApiNameValuePairs.add(new BasicNameValuePair("issue_id", mIssueID));
//        baseApiNameValuePairs.add(new BasicNameValuePair("app_bundle_id", Config.Bundle_ID));
//        baseApiNameValuePairs.add(new BasicNameValuePair("api_mode", Config.api_mode));
//        baseApiNameValuePairs.add(new BasicNameValuePair("api_version", Config.api_version));
    }

    /*private void saveIssueToApp(){


        try {
            // Save the Subscription Objects into the SQlite DB
            IssueDataSet mDbHelper = new IssueDataSet(BaseApp.getContext());

            Log.d(TAG, "Get issue parser of Issue is :" + getIssueParser.mIssue.toString());

            mDbHelper.insertIssueData(mDbHelper.getWritableDatabase(), getIssueParser.mIssue);

            mDbHelper.close();

            // Saving Object into All Download Data Set . change by Likith

//        QueueDownload queueDownload = new QueueDownload();
//        queueDownload.insertIssueInDownloadQueue(String.valueOf(getIssueParser.mIssue.issueID));

            boolean result = startIssueDownload(String.valueOf(getIssueParser.mIssue.issueID));

            Log.d(TAG, "Result of queue download insert is : " + result);

            if (result) {

                Log.d(TAG, "Inside the if condition of notify service of new download");
                PMService pmService = new PMService();
                pmService.newDownloadRequested();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean startIssueDownload(String issueId){

        QueueDownload queueIssue = new QueueDownload();
        return queueIssue.insertIssueInDownloadQueue(issueId);

    }
*/
 public ArrayList<CategoryBean> getFullData(String responce){
     ArrayList <CategoryBean> beanArrayList = new ArrayList<>();
     try {
         JSONObject c = new JSONObject(responce);
         JSONArray A = c.getJSONArray("data");
         for (int i = 0; i < A.length() ; i++) {
             JSONObject m = A.getJSONObject(i);
             CategoryBean bean = new CategoryBean();
             bean.setId(m.getString("id"));
             bean.setName(m.getString("name"));
             bean.setParent_id(m.getString("parent_id"));
             beanArrayList.add(bean);

         }
     } catch (JSONException e) {
         e.printStackTrace();
     }

     return beanArrayList;

 }


}

