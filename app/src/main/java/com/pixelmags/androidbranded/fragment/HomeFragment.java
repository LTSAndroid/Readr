package com.pixelmags.androidbranded.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.pixelmags.android.api.GetCategory;
import com.pixelmags.android.bean.CategoryBean;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.MainActivity;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.androidbranded.activity.HomeActivityReadr;
import com.pixelmags.androidbranded.bean.PassFeaturedData;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<CategoryBean> beanArrayList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String KEY_DEMO = "demo";
    private final List<Fragment> mFragmentList = new ArrayList<>();

    GetCategory category;
    ViewPager viewPager;
    TabLayout viewPagerTab;
    ArrayList<Magazine> featuredList;
    ArrayList<Magazine> newArrivals;
    String categoryIds;
    Context context;
    PassFeaturedData passFeaturedInterface;
    ArrayList<CategoryBean> CatList;



    public HomeFragment() {
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        init(v);
        return v;

    }

    public void init(View root){
         viewPagerTab = (TabLayout) root .findViewById(R.id.viewpagertab);
         CatList = new ArrayList<CategoryBean>();
         new GetCat().execute();


    }

    public ArrayList<CategoryBean> getTabDetails(){
        ArrayList<CategoryBean> beanList = new ArrayList<>();
        category = new GetCategory();
        return category.getFullData(category.init(""));
    }

    private class GetCat extends  AsyncTask<String, String, String>{

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
                setupViewPager(viewPagerTab,CatList);
                SubFragmentHome ldf = new SubFragmentHome(CatList.get(0).getId());
                Bundle args = new Bundle();
                args.putString("Key", CatList.get(0).getId());
                ldf.setArguments(args);
                addFragment(R.id.sub_home_container,ldf,"SUbHome");
            }

        }
    }

    private void setupViewPager(final TabLayout tabLayout, final ArrayList<CategoryBean> beanArrayList) {

        for (int i = 0; i < beanArrayList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(beanArrayList.get(i).getName()));
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.e("POSITION ===>",tabLayout.getSelectedTabPosition()+"");
                SubFragmentHome subFragmentHome = (SubFragmentHome)getActivity().getSupportFragmentManager().findFragmentByTag("SUbHome");
                subFragmentHome.passFeatuedArrival(CatList.get(tabLayout.getSelectedTabPosition()).getId());




            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    protected void addFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId, @NonNull Fragment fragment, @NonNull String fragmentTag) {


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }


    /*class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override

        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }*/

   /* private class PreLaunchAppTask extends AsyncTask<String, String, String> {
        GetCategory category;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String  resultToDisplay = "";

            beanArrayList =  getTabDetails();
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
                 Log.e("Arra",beanArrayList.size()+"");

            setupViewPager(viewPager,viewPagerTab,beanArrayList);

            if(beanArrayList.size()!=0 && beanArrayList != null){
                SubFragmentHome ldf = new SubFragmentHome(featuredList,newArrivals);

                Bundle args = new Bundle();
                args.putString("Key", beanArrayList.get(0).getId());
                ldf.setArguments(args);
                addFragment(R.id.sub_home_container,ldf,"SUbHome");
            }




        }

    }
*/


    @Override
    public void onResume() {



        super.onResume();
    }


   /* @Override
    public void passFeatuedArrival(ArrayList<Magazine> f, ArrayList<Magazine> n) {
        SubFragmentHome ldf = new SubFragmentHome(f,n);
        Bundle args = new Bundle();
        args.putString("Key", "0");
        ldf.setArguments(args);
        replaceFragment(R.id.sub_home_container,ldf,"SUbHome");


    }*/
}
