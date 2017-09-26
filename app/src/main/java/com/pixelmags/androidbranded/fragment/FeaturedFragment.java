package com.pixelmags.androidbranded.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.androidbranded.adapter.MagazineAdapter;
import com.pixelmags.androidbranded.api.GetFeaturedIssuese;
import com.pixelmags.androidbranded.download.DetailsTransition;
import com.pixelmags.androidbranded.download.KittenClickListener;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link FeaturedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedFragment extends Fragment implements KittenClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "key";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerViewList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MagazineAdapter adapterMagazine;
    ArrayList<Magazine> beanArrayList;
    public Context context;
    public String Key;


    public FeaturedFragment(){

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = getActivity();

    }






    public FeaturedFragment(String key) {
        // Required empty public constructor
        this.Key = key;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeaturedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeaturedFragment newInstance(String param1, String param2) {
        FeaturedFragment fragment = new FeaturedFragment();
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
        View v = inflater.inflate(R.layout.fragment_featured, container, false);
        init(v);
        return v;
    }

    public void init(View v){
        recyclerViewList = (RecyclerView)v.findViewById(R.id.recycler_view_list);
        recyclerViewList.setHasFixedSize(true);
        beanArrayList = new ArrayList<>();

        GetFeaturedArrivalsTask getFeaturedArrivalsTask = new GetFeaturedArrivalsTask();
        getFeaturedArrivalsTask.execute();
    }
    public void setAdaptesr(ArrayList<Magazine> beanArrayList){
       // Log.e("ArraySize",beanArrayList.size()+"");

        if(beanArrayList != null) {
            adapterMagazine = new MagazineAdapter(getActivity(), beanArrayList, (KittenClickListener) this);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewList.setAdapter(adapterMagazine);
        }
    }



    @Override
    public void onKittenClicked(MagazineAdapter.SingleItemRowHolder holder, int position,Magazine magazine) {

        Log.e("Data Fetured",magazine.toString());

        DetailsFragment kittenDetails = DetailsFragment.newInstance(magazine);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            kittenDetails.setSharedElementEnterTransition(new DetailsTransition());
            kittenDetails.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            kittenDetails.setSharedElementReturnTransition(new DetailsTransition());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.itemImage, "kittenImage")
                .replace(R.id.container, kittenDetails)
                .addToBackStack(null)
                .commit();


    }


    private class GetFeaturedArrivalsTask extends AsyncTask<String, String, String> {
        GetFeaturedIssuese category;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String  resultToDisplay = "";
            category = new GetFeaturedIssuese();
            category.init(Key);
            beanArrayList = category.getFeaturedData();
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            setAdaptesr(beanArrayList);

        }

    }


}

