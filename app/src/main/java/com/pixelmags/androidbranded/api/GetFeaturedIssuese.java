package com.pixelmags.androidbranded.api;

import android.util.Log;

import com.pixelmags.android.bean.MagazineBeanMain;
import com.pixelmags.android.comms.Config;
import com.pixelmags.android.comms.WebRequest;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.download.DownloadThumbnails;
import com.pixelmags.android.download.QueueDownload;
import com.pixelmags.android.json.GetIssueParser;
import com.pixelmags.android.json.GetIssuesParser;
import com.pixelmags.android.storage.AllIssuesDataSet;
import com.pixelmags.android.storage.IssueDataSet;
import com.pixelmags.android.storage.UserPrefs;
import com.pixelmags.android.util.BaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;

/**
 * Created by sejeeth on 10/9/17.
 */

public class GetFeaturedIssuese extends WebRequest
{
    private static final String API_NAME="getRecentIssues";
    private static boolean failure = false;
    GetIssuesParser getIssuesParserParser;
    private String mIssueID;
    private String TAG = "GetIssue";
    ArrayList<Magazine> DataFeaturedMagazine;


    public GetFeaturedIssuese(){
        super(API_NAME);
    }

    public static boolean setGetIssueFailure(){
        return failure;
    }


    public void init(String keyId) {
        DataFeaturedMagazine = new ArrayList<>();
        setApiNameValuePairs(keyId);
        doPostRequest();
        if (responseCode == 200) {
            getIssuesParserParser = new GetIssuesParser(getAPIResultData());
            if (getIssuesParserParser.initJSONParse()) {
                if (getIssuesParserParser.isSuccess()) {
                    getIssuesParserParser.parse();
                    DataFeaturedMagazine = getIssuesParserParser.allIssuesList;
                }else {

                }
            }
        }
    }

    private void setApiNameValuePairs(String key){

        requestBody = new FormBody.Builder()
                .add("api_mode", Config.api_mode)
                .add("api_version", Config.api_version)
                .add("category_id", key)
                .build();

    }

    public ArrayList<Magazine> getFeaturedData() {
        return DataFeaturedMagazine;

    }




}


