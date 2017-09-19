package com.pixelmags.androidbranded.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import com.pixelmags.android.api.GetCategory;
import com.pixelmags.android.bean.CategoryBean;
import com.pixelmags.android.comms.Config;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.datamodels.MyIssue;
import com.pixelmags.android.datamodels.MySubscription;
import com.pixelmags.android.datamodels.Subscription;
import com.pixelmags.android.download.DownloadThumbnails;
import com.pixelmags.android.pixelmagsapp.MainActivity;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.android.pixelmagsapp.billing.CreatePurchaseTask;
import com.pixelmags.android.storage.AllIssuesDataSet;
import com.pixelmags.android.storage.MyIssuesDataSet;
import com.pixelmags.android.storage.MySubscriptionsDataSet;
import com.pixelmags.android.storage.SubscriptionsDataSet;
import com.pixelmags.android.storage.UserPrefs;
import com.pixelmags.android.ui.AllIssuesFragment;
import com.pixelmags.android.util.BaseApp;
import com.pixelmags.android.util.IabHelper;
import com.pixelmags.android.util.IabResult;
import com.pixelmags.android.util.Inventory;
import com.pixelmags.android.util.PMStrictMode;
import com.pixelmags.android.util.Purchase;
import com.pixelmags.android.util.SkuDetails;
import com.pixelmags.android.util.Util;
import com.pixelmags.androidbranded.api.GetFeaturedIssuese;
import com.pixelmags.androidbranded.api.GetNewArival;
import com.pixelmags.androidbranded.bean.PassFeaturedData;
import com.pixelmags.androidbranded.bean.PassNewArrival;
import com.pixelmags.androidbranded.bean.passData;
import com.pixelmags.androidbranded.fragment.FeaturedFragment;
import com.pixelmags.androidbranded.fragment.HomeFragment;
import com.pixelmags.androidbranded.fragment.NewArrivalFragment;
import com.pixelmags.androidbranded.fragment.SubFragmentHome;

import java.util.ArrayList;
import java.util.List;


public class HomeActivityReadr extends AppCompatActivity {


    public IabHelper mHelper;
    public ArrayList<Magazine> billingMagazinesListFeatured;
    public ArrayList<Magazine> billingMagazinesListNewArrival;
    public ArrayList<Subscription> biilingSubscriptionList;
    public ArrayList<Purchase> userOwnedSKUList;
    public ArrayList<String> skuList;
    public MainActivity.CanPurchaseTask mCanPurchaseTask = null;
    public CreatePurchaseTask mCreatePurchaseTask = null;
    boolean mIsBound = false;
    private ArrayList<Subscription> pixelMagsSubscriptionList = null;
    private String TAG = "MainActivity";
    private String purchaseIssuePrice;
    private String purchaseIssueCurrencyType;
    private ArrayList<Magazine> from;
    private ArrayList<Magazine> beanListNewArrival;
    GetCategory category;
    private ArrayList<CategoryBean> CatList;
    public PassFeaturedData fragmentCommunicator;
    String firstCallId;
    PassFeaturedData passFeaturedInterface;
    //PassNewArrival passNewarrivalInterface;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_readr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        from = new ArrayList<>();
        biilingSubscriptionList = new ArrayList<>();
        userOwnedSKUList = new ArrayList<>();
        CatList = new ArrayList<>();
        skuList = new ArrayList<>();
        addFragment(R.id.container,new HomeFragment(),"Home");

        PMStrictMode.setStrictMode(Config.DEVELOPER_MODE);
        Util.doPreLaunchSteps();


       /* GetCat cat = new GetCat();
        cat.execute();*/



    }




  /*  public ArrayList<CategoryBean> getTabDetails(){
        ArrayList<CategoryBean> beanList = new ArrayList<>();
        category = new GetCategory();
        return category.getFullData(category.init(""));
    }

    public void postExecuteCode(ArrayList<Magazine> pass,String data){
        Log.e("Post ==","Q");
        setUpBillingNOrmal(pass,data);

    }
*/

/*    public void setUpBillingNOrmal(final ArrayList<Magazine> filterdata,String Featured) {
        Log.e("Post ==","SeTUp Normal");
        mHelper = new IabHelper(this, Config.base64EncodedPublicKey);

        if (Featured.equalsIgnoreCase("FEATURED")) {
            mHelper.startSetup(
                    new IabHelper.OnIabSetupFinishedListener() {
                        public void onIabSetupFinished(IabResult result) {
                            if (!result.isSuccess()) {
                                Log.e("Post ==","Failure Featured");
                            } else {

                                Log.d(TAG, "Pixel Image Magazine List is : " + filterdata.size());

                                skuList = new ArrayList<String>();

                                if (filterdata != null) {

                                    for (int i = 0; i < filterdata.size(); i++) {
                                        skuList.add(filterdata.get(i).android_store_sku);
                                        Log.e("What is thid ==>", filterdata.get(i).android_store_sku);

                                    }

                                }
                                mHelper.queryInventoryAsync(true, skuList, mQueryFinishedListenerFeatured);
                            }
                        }
                    });




        }else {

            mHelper.startSetup(
                    new IabHelper.OnIabSetupFinishedListener() {
                        public void onIabSetupFinished(IabResult result) {
                            if (!result.isSuccess()) {
                                Log.e("Post ==","Failure New Arrival");
                            } else {

                                Log.d(TAG, "Pixel Image Magazine List is : " + filterdata.size());
                                skuList = new ArrayList<String>();

                                if (filterdata != null) {

                                    for (int i = 0; i < filterdata.size(); i++) {
                                        skuList.add(filterdata.get(i).android_store_sku);
                                        Log.e("What is thid ==>", filterdata.get(i).android_store_sku);

                                    }

                                }
                                mHelper.queryInventoryAsync(true, skuList, mQueryFinishedListenerNewArrival);
                            }
                        }
                    });

        }

    }*/

    /*public void setUpthePaymentApi3(){
        // Checking In-app Billing Version 3 API Support
        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener()
                                   {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess())
                                           {
                                               //failed
                                               Log.d(TAG,"In App Billing setup failed");
                                           }
                                           else
                                           {
                                               //success
                                               Log.d(TAG,"In App Billing setup success");
                                           }
                                       }
                                   });

    }*/




   /* public void APICallFeatured(String id){
        GetFeaturedIssuese featuredIssuese = new GetFeaturedIssuese();
        featuredIssuese.init(id);
        from = new ArrayList<>();
        from = featuredIssuese.getFeaturedData();
        Log.e("Activity Featured DataList ==?",""+from.size());

    }

    public void APICallNewArrival(String id){
        GetNewArival arival = new GetNewArival();
        arival.init(id);
        beanListNewArrival = new ArrayList<>();
        beanListNewArrival = arival.getNewArrival();


    }*/

   /* @Override
    public void pass(int i) {
        Log.e("Here i m getting ====","Hon=me Fra");
        PreLaunchAppTask mPreLaunchTask = new PreLaunchAppTask(CatList.get(i).getId(),"Key");
        mPreLaunchTask.execute((String) null);


    }*/


   /* private class GetCat extends  AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String disp = "";
            CatList = getTabDetails();
            return disp;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            if(CatList.size()>0){
                PreLaunchAppTask mPreLaunchTask = new PreLaunchAppTask(CatList.get(0).getId(),"");
                mPreLaunchTask.execute((String) null);

            }

        }
    }
*/

   /* private class PreLaunchAppTask extends AsyncTask<String, String, String> {

        String ids;
        String Key = "";

        public  PreLaunchAppTask(String id,String key) {
         this.ids = id;
         this.Key = key;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String resultToDisplay = "";

            APICallFeatured(ids);
            APICallNewArrival(ids);
            return resultToDisplay;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            Log.e("Featured Data final ==>",from.size()+"");
            Log.e("New Arrival Data final ==>",beanListNewArrival.size()+"");
            //postExecuteCode(from,"FEATURED");
            //postExecuteCode(beanListNewArrival,"NEWARRIVAL");

            if(Key.equalsIgnoreCase("")) {
                addFragment(R.id.container, new HomeFragment(from, beanListNewArrival, CatList), "Home");

            }else{

                HomeFragment customFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("Home");

                if(customFragment != null){
                    passFeaturedInterface = (PassFeaturedData) customFragment;
                    passFeaturedInterface.passFeatuedArrival(from,beanListNewArrival);

                }



            }



        }

    }*/

   /* IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
            }else {
                List<String> allOwnedSKUS = inventory.getAllOwnedSkus();
                userOwnedSKUList = new ArrayList<Purchase>();
                for ( int i = 0; i < allOwnedSKUS.size(); i++) {
                    Purchase purchaseData = inventory.getPurchase(allOwnedSKUS.get(i));
                    Log.d(TAG,"User previous Purchase Data list is : "+purchaseData);

                    //Assign button Status here and also restore purchase if the issue is not purchased
                    userOwnedSKUList.add(purchaseData);
                }

            }

        }
    };


    IabHelper.QueryInventoryFinishedListener mQueryFinishedListenerFeatured = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                return;
            }

            ArrayList<MyIssue> myIssueArray = null;
            ArrayList<MySubscription> mySubsArray = null;

            *//*if(UserPrefs.getUserLoggedIn()) {
                MyIssuesDataSet mDbReader = new MyIssuesDataSet(BaseApp.getContext());
                myIssueArray = mDbReader.getMyIssues(mDbReader.getReadableDatabase());
                mDbReader.close();

                MySubscriptionsDataSet mDbSubReader = new MySubscriptionsDataSet(BaseApp.getContext());
                mySubsArray = mDbSubReader.getMySubscriptions(mDbSubReader.getReadableDatabase());
                mDbSubReader.close();
            }
*//*
            billingMagazinesListFeatured = new ArrayList<Magazine>();
            ArrayList<Magazine> freeMagazineList = new ArrayList<Magazine>();
            // biilingSubscriptionList = new ArrayList<Subscription>();

            if(from != null) {

                for (int i = 0; i < from.size(); i++) {
                    String SKU = from.get(i).android_store_sku;
                    String paymentProvider = from.get(i).paymentProvider;

                    if(paymentProvider.equalsIgnoreCase("free")){
                        Log.e("DOne","Done");

                        freeMagazineList.add(from.get(i));
                    }


                    if (inventory.hasDetails(SKU)) //yet to be changed,this is for billing test
                    {

                        SkuDetails details = inventory.getSkuDetails(SKU);

                        Log.d(TAG,"Details inside SKU is : "+details);

                        from.get(i).price = details.getPrice();

                        if(isSimSupport(HomeActivityReadr.this)){
                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String countryCodeValue = tm.getNetworkCountryIso();
                            countryCodeValue = countryCodeValue.toUpperCase();
                            String language = getResources().getConfiguration().locale.getLanguage();
                            Config.localeValue = language+"_"+countryCodeValue+"@currency="+details.getCurrencyType();
                        }else{
                            Config.localeValue = getResources().getConfiguration().locale+"@currency="+details.getCurrencyType();
                        }



                        Magazine finalMagazine = new Magazine();

                        finalMagazine.id = from.get(i).id;
                        finalMagazine.isIssueOwnedByUser = false;

                        // check if the issue is already owned by user


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
                        }

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

                        billingMagazinesListFeatured.add(finalMagazine);
                    }else{
                        if(i == from.size()-1 ){
                            if(billingMagazinesListFeatured.size() == 0){

                            }

                        }
                    }
                }

                if(billingMagazinesListFeatured.size() != 0) {
                    billingMagazinesListFeatured.addAll(freeMagazineList);
                }

                Log.e("FINAL == FEATURED ARRIVAL",billingMagazinesListFeatured.size()+"");
            }

            mHelper.queryInventoryAsync(mGotInventoryListener);
        }

    };

    IabHelper.QueryInventoryFinishedListener mQueryFinishedListenerNewArrival = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                return;
            }

            ArrayList<MyIssue> myIssueArray = null;
            ArrayList<MySubscription> mySubsArray = null;

            if(UserPrefs.getUserLoggedIn()) {
                MyIssuesDataSet mDbReader = new MyIssuesDataSet(BaseApp.getContext());
                myIssueArray = mDbReader.getMyIssues(mDbReader.getReadableDatabase());
                mDbReader.close();

                MySubscriptionsDataSet mDbSubReader = new MySubscriptionsDataSet(BaseApp.getContext());
                mySubsArray = mDbSubReader.getMySubscriptions(mDbSubReader.getReadableDatabase());
                mDbSubReader.close();

//                for(int i=0; i< mySubsArray.size();i++)
//                {
//                    MySubscription sub = mySubsArray.get(i);
//                }

            }

            billingMagazinesListNewArrival = new ArrayList<Magazine>();
            ArrayList<Magazine> freeMagazineList = new ArrayList<Magazine>();


            if(beanListNewArrival != null) {

                for (int i = 0; i < beanListNewArrival.size(); i++) {
                    String SKU = beanListNewArrival.get(i).android_store_sku;
                    String paymentProvider = beanListNewArrival.get(i).paymentProvider;

                    Log.d(TAG,"Type of magazine list is : "+beanListNewArrival.get(i).type);
                    Log.d(TAG,"SKU is : "+SKU);
                    Log.d(TAG,"Inventory is : "+inventory);


                    if(paymentProvider.equalsIgnoreCase("free")){
                        Log.e("DOne","Done");

                        freeMagazineList.add(beanListNewArrival.get(i));
                    }


                    if (inventory.hasDetails(SKU)) //yet to be changed,this is for billing test
                    {

                        SkuDetails details = inventory.getSkuDetails(SKU);

                        Log.d(TAG,"Details inside SKU is : "+details);

                        beanListNewArrival.get(i).price = details.getPrice();

                        if(isSimSupport(HomeActivityReadr.this)){
                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String countryCodeValue = tm.getNetworkCountryIso();
                            countryCodeValue = countryCodeValue.toUpperCase();
                            String language = getResources().getConfiguration().locale.getLanguage();
                            Config.localeValue = language+"_"+countryCodeValue+"@currency="+details.getCurrencyType();
                        }else{
                            Config.localeValue = getResources().getConfiguration().locale+"@currency="+details.getCurrencyType();
                        }

                        Magazine finalMagazine = new Magazine();
                        finalMagazine.id = beanListNewArrival.get(i).id;
                        finalMagazine.isIssueOwnedByUser = false;

                        // check if the issue is already owned by user
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
                        }

                        //    magazine.magazineId = unit.getInt("ID"); // Is this different from ID field ??
                        finalMagazine.synopsis = beanListNewArrival.get(i).synopsis;
                        finalMagazine.type = beanListNewArrival.get(i).type;
                        finalMagazine.title = beanListNewArrival.get(i).title;
                        finalMagazine.mediaFormat = beanListNewArrival.get(i).mediaFormat;
                        finalMagazine.manifest = beanListNewArrival.get(i).manifest;
                        // magazine.lastModified = unit.getString("lastModified"); // how to get date?
                        finalMagazine.android_store_sku = beanListNewArrival.get(i).android_store_sku;
                        finalMagazine.price = beanListNewArrival.get(i).price;
                        finalMagazine.thumbnailURL = beanListNewArrival.get(i).thumbnailURL;
                        finalMagazine.isThumbnailDownloaded = beanListNewArrival.get(i).isThumbnailDownloaded;
                        finalMagazine.ageRestriction = beanListNewArrival.get(i).ageRestriction;
                        finalMagazine.removeFromSale = beanListNewArrival.get(i).removeFromSale;
                        finalMagazine.isPublished = beanListNewArrival.get(i).isPublished;
                        finalMagazine.paymentProvider = beanListNewArrival.get(i).paymentProvider;
                        finalMagazine.exclude_from_subscription = beanListNewArrival.get(i).exclude_from_subscription;

                        billingMagazinesListNewArrival.add(finalMagazine);
                    }else{
                        if(i == beanListNewArrival.size()-1 ){
                            if(billingMagazinesListNewArrival.size() == 0){
                               *//* AllIssuesDataSet mDbHelper = new AllIssuesDataSet(BaseApp.getContext());
                                mDbHelper.dropAllIssuesTable(mDbHelper.getWritableDatabase());*//*
                            }

                        }
                    }
                }

                if(billingMagazinesListNewArrival.size() != 0) {
                    billingMagazinesListNewArrival.addAll(freeMagazineList);


                }

                Log.e("FINAL == NEW ARRIVAL",billingMagazinesListNewArrival.size()+"");


            }



            mHelper.queryInventoryAsync(mGotInventoryListener);
        }

    };



    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }*/

    protected void addFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment, fragmentTag).disallowAddToBackStack().commit();
    }

    protected void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, fragmentTag).addToBackStack(backStackStateName).commit();
    }


    }







