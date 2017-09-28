package com.pixelmags.androidbranded.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
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
import com.pixelmags.android.ui.AboutFragment;
import com.pixelmags.android.ui.AllIssuesFragment;
import com.pixelmags.android.ui.LoginFragment;
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
import com.pixelmags.androidbranded.fragment.ContactandSupportFragment;
import com.pixelmags.androidbranded.fragment.FeaturedFragment;
import com.pixelmags.androidbranded.fragment.HomeFragment;
import com.pixelmags.androidbranded.fragment.NewArrivalFragment;
import com.pixelmags.androidbranded.fragment.SubFragmentHome;

import java.util.ArrayList;
import java.util.List;


public class HomeActivityReadr extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<Subscription> biilingSubscriptionList;
    public ArrayList<Purchase> userOwnedSKUList;
    public ArrayList<String> skuList;
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
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_readr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        from = new ArrayList<>();
        biilingSubscriptionList = new ArrayList<>();
        userOwnedSKUList = new ArrayList<>();
        CatList = new ArrayList<>();
        skuList = new ArrayList<>();
        addFragment(R.id.container,new HomeFragment(),"Home");
        PMStrictMode.setStrictMode(Config.DEVELOPER_MODE);
        Util.doPreLaunchSteps();

    }
    protected void addFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {
        getSupportFragmentManager().beginTransaction().add(containerViewId, fragment, fragmentTag).disallowAddToBackStack().commit();
    }

    protected void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag, @Nullable String backStackStateName) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, fragmentTag).addToBackStack(backStackStateName).commit();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return false;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        switch (itemId) {
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.account:
                fragment = new LoginFragment();
                break;

            case R.id.About:
                fragment = new AboutFragment();
                break;

            case R.id.support:
                fragment = new ContactandSupportFragment();
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
    }
}







