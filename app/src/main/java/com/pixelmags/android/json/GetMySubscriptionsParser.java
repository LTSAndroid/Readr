package com.pixelmags.android.json;

import android.util.Log;

import com.pixelmags.android.datamodels.MySubscription;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Annie on 11/10/15.
 */
public class GetMySubscriptionsParser extends JSONParser {

    public Object mData;
    public ArrayList<MySubscription> mySubscriptionsList;
    private String TAG = "GetMySubscriptionsParser";

    public GetMySubscriptionsParser(String Data){
        super(Data);
        mySubscriptionsList = new ArrayList<MySubscription>();
    }

    public boolean parse(){

        if(!initJSONParse())
            return false; // return false if the JSON base object cannot be parsed

        try{

            JSONArray arrayData = baseJSON.getJSONArray("data");
            for(int i=0;i<arrayData.length();i++)
            {
                MySubscription mySubscription = new MySubscription();
                JSONObject unit = arrayData.getJSONObject(i);

                Log.d(TAG,"Credit Available is : "+unit.isNull("credits_available"));
                if(!unit.isNull("credits_available"))
                mySubscription.creditsAvailable = unit.getInt("credits_available");
                //   mySubscription.issueID = unit.getInt("issue_id");  // Does not exist
                mySubscription.purchaseDate = unit.getString("purchase_date");
                mySubscription.expiresDate = unit.getString("expires_date");
                mySubscription.magazineID = unit.getString("magazine_id");
                mySubscription.subscriptionProductId = unit.getString("subscription_product_id");

                mySubscriptionsList.add(mySubscription);
            }

        }catch(Exception e){
            e.printStackTrace();
        }


        return true;
    }

}



