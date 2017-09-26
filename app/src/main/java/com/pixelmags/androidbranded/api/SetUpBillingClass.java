package com.pixelmags.androidbranded.api;

/**
 * Created by sejeeth on 18/9/17.
 */

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.pixelmags.android.comms.Config;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.util.IabHelper;
import com.pixelmags.android.util.IabResult;
import com.pixelmags.android.util.Inventory;
import com.pixelmags.android.util.SkuDetails;


import java.util.ArrayList;
import java.util.List;

public class SetUpBillingClass {

    public static IabHelper mHelper;

    public  ArrayList<Magazine> getFilterDataFromBilling(final Context A,final ArrayList<Magazine> from){

        final ArrayList<String> SKU = new ArrayList<>();
        final ArrayList<Magazine> to = new ArrayList<>();
        final ArrayList<Magazine> freeMagazineList = new ArrayList<Magazine>();
        mHelper = new IabHelper(A, Config.base64EncodedPublicKey);
        mHelper.startSetup(
                new IabHelper.OnIabSetupFinishedListener() {
                    public void onIabSetupFinished(IabResult result) {

                        if (!result.isSuccess()) {
                            Log.e("Post ==","Failure Featured");
                        } else {

                            if (from != null) {

                                for (int i = 0; i < from.size(); i++) {
                                    SKU.add(from.get(i).android_store_sku);
                                    Log.e("What is thid ==>", from.get(i).android_store_sku);

                                }

                            }
                            mHelper.queryInventoryAsync(true, SKU, new IabHelper.QueryInventoryFinishedListener() {
                                @Override
                                public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                                    if (!result.isFailure()) {
                                        if(from != null) {
                                            for (int i = 0; i < from.size(); i++) {
                                                String SKU = from.get(i).android_store_sku;
                                                String paymentProvider = from.get(i).paymentProvider;

                                                if(paymentProvider.equalsIgnoreCase("free")){
                                                    Log.e("DOne","Done");
                                                    freeMagazineList.add(from.get(i));
                                                }

                                                if (inventory.hasDetails(SKU)){
                                                    SkuDetails details = inventory.getSkuDetails(SKU);
                                                    from.get(i).price = details.getPrice();

                                                    if(isSimSupport(A)){
                                                        TelephonyManager tm = (TelephonyManager)A. getSystemService(Context.TELEPHONY_SERVICE);
                                                        String countryCodeValue = tm.getNetworkCountryIso();
                                                        countryCodeValue = countryCodeValue.toUpperCase();
                                                        String language = A.getResources().getConfiguration().locale.getLanguage();
                                                        Config.localeValue = language+"_"+countryCodeValue+"@currency="+details.getCurrencyType();
                                                    }else{
                                                        Config.localeValue = A.getResources().getConfiguration().locale+"@currency="+details.getCurrencyType();
                                                    }

                                                    Magazine finalMagazine = new Magazine();
                                                    finalMagazine.id = from.get(i).id;
                                                    finalMagazine.isIssueOwnedByUser = false;

                                                    // check if the issue is already owned by user

/*
                                                    if (myIssueArray != null) {

                                                        for (int issueCount = 0; issueCount < myIssueArray.size(); issueCount++) {
                                                            MyIssue issue = myIssueArray.get(issueCount);

                                                            if (issue.issueID == finalMagazine.id) {
                                                                finalMagazine.isIssueOwnedByUser = true;
                                                                purchaseIssuePrice = details.getPrice();
                                                                Log.d(TAG,"Purchase Issue Price is : "+purchaseIssuePrice);
                                                                purchaseIssueCurrencyType = details.getCurrencyType();
                                                                Log.d(TAG,"Purchase Issue Currency Type is : "+purchaseIssueCurrencyType);
                                                            }
                                                        }
                                                    }*/

                                                    finalMagazine.synopsis = from.get(i).synopsis;
                                                    finalMagazine.type = from.get(i).type;
                                                    finalMagazine.title = from.get(i).title;
                                                    finalMagazine.mediaFormat = from.get(i).mediaFormat;
                                                    finalMagazine.manifest = from.get(i).manifest;
                                                    finalMagazine.android_store_sku = from.get(i).android_store_sku;
                                                    finalMagazine.price = from.get(i).price;
                                                    finalMagazine.thumbnailURL = from.get(i).thumbnailURL;
                                                    finalMagazine.isThumbnailDownloaded = from.get(i).isThumbnailDownloaded;
                                                    finalMagazine.ageRestriction = from.get(i).ageRestriction;
                                                    finalMagazine.removeFromSale = from.get(i).removeFromSale;
                                                    finalMagazine.isPublished = from.get(i).isPublished;
                                                    finalMagazine.paymentProvider = from.get(i).paymentProvider;
                                                    finalMagazine.exclude_from_subscription = from.get(i).exclude_from_subscription;

                                                    to.add(finalMagazine);
                                                }else{
                                                    if(i == from.size()-1 ){
                                                        if(to.size() == 0){

                                                        }

                                                    }
                                                }
                                            }

                                            if(to.size() != 0) {
                                                to.addAll(freeMagazineList);
                                            }


                                        }

                                    }
                                }
                            });
                        }
                    }
                });


        return to;

    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }




}
