package com.pixelmags.androidbranded.api;

import com.pixelmags.android.comms.Config;
import com.pixelmags.android.comms.WebRequest;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.json.GetIssuesParser;

import java.util.ArrayList;

import okhttp3.FormBody;

/**
 * Created by sejeeth on 10/9/17.
 */

public class GetNewArival extends WebRequest {
    private static final String API_NAME="getNewArrivals";
    private static boolean failure = false;
    GetIssuesParser getIssuesParserParser;
    ArrayList<Magazine> DataNewArrivalMagazine;


    public GetNewArival(){
        super(API_NAME);
    }

    public static boolean setGetIssueFailure(){
        return failure;
    }


    public void init(String keyId) {
        DataNewArrivalMagazine = new ArrayList<>();
        setApiNameValuePairs(keyId);
        doPostRequest();
        if (responseCode == 200) {
            getIssuesParserParser = new GetIssuesParser(getAPIResultData());
            if (getIssuesParserParser.initJSONParse()) {
                if (getIssuesParserParser.isSuccess()) {
                    getIssuesParserParser.parse();
                    DataNewArrivalMagazine = getIssuesParserParser.allIssuesList;
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

    public ArrayList<Magazine> getNewArrival() {
        return DataNewArrivalMagazine;

    }




}



