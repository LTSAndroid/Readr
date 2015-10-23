package com.pixelmags.android.json;

/**
 * Created by Annie on 09/10/15.
 */
public class CanPurchaseParser extends JSONParser {

    public String missueID;
    

    public CanPurchaseParser(String Data){
        super(Data);
    }

    public boolean parse(){

        if(!initJSONParse())
            return false; // return false if the JSON base object cannot be parsed

        try{

            missueID = baseJSON.getString("issue_id");

        }catch(Exception e){}


        return true;
    }

}