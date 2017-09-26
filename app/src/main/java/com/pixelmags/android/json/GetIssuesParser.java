package com.pixelmags.android.json;


import android.util.Log;

import com.pixelmags.android.datamodels.Magazine;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Annie on 09/10/15.
 */
public class GetIssuesParser extends JSONParser {

    public Object mData;
    public ArrayList<Magazine> allIssuesList;
    private String TAG = "GetIssuesParser";



    public GetIssuesParser(String Data){
        super(Data);
        allIssuesList = new ArrayList<Magazine>();
    }

    public boolean parse()
    {

        if(!initJSONParse())
            return false; // return false if the JSON base object cannot be parsed

        try{



            JSONArray arrayData = baseJSON.getJSONArray("data");
            for(int i=0;i< arrayData.length();i++)
            {
                Magazine magazine = new Magazine();
                JSONObject unit = arrayData.getJSONObject(i);

                magazine.id = unit.getInt("ID");

                Log.d(TAG,"Issue Id is : "+magazine.id);

            //    magazine.magazineId = unit.getInt("ID"); // Is this different from ID field ??
                magazine.synopsis = unit.getString("synopsis");
                magazine.type = unit.getString("type");
                magazine.title = unit.getString("title");
                Log.d(TAG,"Issue title is : "+magazine.title);
                magazine.mediaFormat = unit.getString("media_format");
                magazine.manifest = unit.getString("manifest");
                magazine.issueDate =  unit.getString("issueDate");
                Log.d(TAG,"Issue date parsed is : "+magazine.issueDate);
                // magazine.lastModified = unit.getString("lastModified"); // how to get date?
                magazine.android_store_sku = unit.getString("iTunesStoreSKU");
                magazine.price = unit.getString("price");
                magazine.thumbnailURL = unit.getString("thumbnailURL");
                magazine.ageRestriction = unit.getString("ageRestriction");
                magazine.removeFromSale = unit.getBoolean("remove_from_sale");
                magazine.isPublished = unit.getBoolean("isPublished");
                magazine.exclude_from_subscription = unit.getString("exclude_from_subscription");
                magazine.paymentProvider = unit.getString("paymentProvider");
                magazine.magazine_id = unit.getString("magazine_id");
                allIssuesList.add(magazine);

            }








          /*  "ID": "128124",
                    "title": "Jul\/Aug 2017",
                    "synopsis": "Studio Style",
                    "issueDate": "2017-09-12 00:00:00",
                    "iTunesStoreSKU": "x3d_media_aec_magazine.128124.nc",
                    "thumbnailURL": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/119677_thumbnail.jpg",
                    "documentURL": "",
                    "creditsIncluded": 0,
                    "type": "issue",
                    "pageCount": 52,
                    "created": "2017-03-16 14:43:22",
                    "lastModified": "2017-09-13 03:15:53",
                    "price": 0.99,
                    "price_tier": 1,
                    "manifest": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/manifest",
                    "issueDateHuman": "Jul\/Aug 2017",
                    "paymentProvider": "itunes",
                    "isPublished": true,
                    "isNotificationSent": true,
                    "editorialSummary": null,
                    "ageRestriction": 4,
                    "is_print_issue_available": true,
                    "remove_from_sale": false,
                    "pages_last_modified": "2017-09-13 03:02:52",
                    "media_last_modified": "2017-09-13 03:02:52",
                    "exclude_from_subscription": false,
                    "media_format": "pixelmags-jpeg",
                    "in_pixelmags_app": true,
                    "supported_orientations": null,
                    "in_anytime": true,
                    "anytime_available": null,
                    "anytime_available_until": null,
                    "language": "en",
                    "region": "GB",
                    "is_deleted": "0",
                    "auto_renewable": false,
                    "include_back_issues": false,
                    "zip_manifest": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/zip_manifest",
                    "thumbnail_180_url": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/119677_thumbnail_180.jpg",
                    "cost_per_download": null,
                    "allocation_status": "finished",
                    "subscriptionLength": null,
                    "in_web_reader": "0",
                    "in_webstand": "0",
                    "feature_appearance": "0",
                    "right_to_left_language": false,
                    "in_bb_readr": 1,
                    "in_bb_anytime": 1,
                    "bb_anytime_available": null,
                    "bb_anytime_available_until": null,
                    "bb_feature_appearance": "0",
                    "send_apns": 1,
                    "send_email": 1,
                    "apns_template": "There is a new issue available for '%magazine_title%' called '%issue_title%'",
                    "magazine_title": "AEC Magazine",
                    "highlighted_in_readr": 1,
                    "renditions": [],
            "issue_sku_ID": "256568",
                    "issue_ID": "128124",
                    "magazine_apps_id": "229",
                    "sku_info_created": "2017-03-16 14:43:22",
                    "sku_info_lastmodified": "2017-09-13 03:03:34",
                    "bundle_name": "com.pixel-mags.reader.x3dmedia-aec",
                    "bundle_id": "",
                    "thumbs": [{
                "profile": "small",
                        "mime_type": "image\/jpeg",
                        "url": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/119677_thumbnail.jpg",
                        "last_modified": "2017-09-13 03:15:53",
                        "max_dimension": 220
            }, {
                "profile": "large",
                        "mime_type": "image\/jpeg",
                        "url": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/119677_thumbnail_520.jpg",
                        "last_modified": "2017-09-13 03:15:53",
                        "max_dimension": 520
            }, {
                "profile": "fixed_height_180",
                        "mime_type": "image\/jpeg",
                        "url": "http:\/\/cdn.pixel-mags.com\/prod\/x3d\/aec\/issues\/128124\/119677\/119677_thumbnail_180.jpg",
                        "last_modified": "2017-09-13 03:15:53",
                        "max_dimension": 180,
                        "width": 127,
                        "height": 180
            }],
            "magazine_id": "51"*/





















// IAB is fully set up. Now, let's get an inventory of stuff we own.
        //    LaunchActivity.mHelper.queryInventoryAsync(true,skuList,iabInventoryListener());

            Collections.sort(allIssuesList, new Comparator<Magazine>(){
                public int compare(Magazine magazine1, Magazine magazine2) {
                    // ## Ascending order
                    return magazine1.issueDate.compareToIgnoreCase(magazine2.issueDate); // To compare string values
                    // return Integer.valueOf(emp1.getId()).compareTo(emp2.getId()); // To compare integer values

                    // ## Descending order
                    // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
                    // return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
                }
            });

            for(int i=0; i<allIssuesList.size(); i++){
                Log.d(TAG,"Array List after sorting  is : "+ allIssuesList.get(i).issueDate);
            }

            Log.d(TAG,"All Issue Data from API is : "+allIssuesList);



        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }


}
