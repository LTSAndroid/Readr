package com.pixelmags.androidbranded.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.androidbranded.bean.PassFeaturedData;
import com.pixelmags.androidbranded.comms.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link SubFragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubFragmentHome extends Fragment implements PassFeaturedData{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Key";
    private static final String ARG_PARAM2 = "param2";
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ViewPagerAdapter adapter;
    AutoScrollViewPager pagerTop;
    ArrayList<Magazine> featuredSub;
    ArrayList<Magazine> newarrivalsub;
    ViewPager viewPager;
    SmartTabLayout viewPagerTab;
    String id;
   // public  List<String> mFragmentTitleList;

    public SubFragmentHome(){

    }

    public  SubFragmentHome(String key){
        this.id = key;
    }

    private void setupViewPager(ViewPager viewPager, SmartTabLayout tab) {
        adapter = null;
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new FeaturedFragment(mParam1), "Featured");
        adapter.addFrag(new NewArrivalFragment(mParam1), "New Arrival");
        viewPager.setAdapter(adapter);
        tab.setViewPager(viewPager);

    }

    private void setupViewPagerExist(ViewPager viewPager,SmartTabLayout tab,String id) {
        adapter.addFrag(new FeaturedFragment(id), "Featured");
        adapter.addFrag(new NewArrivalFragment(id), "New Arrival");
        viewPager.setAdapter(adapter);
        tab.setViewPager(viewPager);



    }


    /*public SubFragmentHome(ArrayList<Magazine> f,ArrayList<Magazine> n) {
        // Required empty public constructor
        this.featuredSub = f;
        this.newarrivalsub = n;

    }
*/
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubFragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static SubFragmentHome newInstance(String param1, String param2) {
        SubFragmentHome fragment = new SubFragmentHome();
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
            Log.e("Keys From My Fragment",mParam1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sub_fragment_home, container, false);
        init(v);

        return v;
    }
    public void init(View v){
        viewPager = (ViewPager) v.findViewById(R.id.subpager);
        viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);
        pagerTop = (AutoScrollViewPager)v.findViewById(R.id.auto_scroll_slider);
        pagerTop.setAdapter(new CustomPagerAdapter(getActivity()));
        pagerTop.startAutoScroll(1000);
        setupViewPager(viewPager,viewPagerTab);
    }





    @Override
    public void passFeatuedArrival(String k) {
        Log.e("KEy Here",k);
        mFragmentTitleList.clear();
        mFragmentList.clear();
        setupViewPagerExist(viewPager,viewPagerTab,k);





    }


    public  class ViewPagerAdapter extends FragmentStatePagerAdapter {

//        mFragmentTitleList = new ArrayList<String>();

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

    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.slider_item, container, false);

            /*ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);*/

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }



}
