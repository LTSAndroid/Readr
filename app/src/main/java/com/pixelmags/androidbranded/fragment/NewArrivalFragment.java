package com.pixelmags.androidbranded.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.pixelmags.android.bean.MagazineBeanMain;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.androidbranded.adapter.MagazineAdapter;
import com.pixelmags.androidbranded.api.GetNewArival;
import com.pixelmags.androidbranded.bean.PassNewArrival;
import com.pixelmags.androidbranded.download.DetailsTransition;
import com.pixelmags.androidbranded.download.KittenClickListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link NewArrivalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewArrivalFragment extends Fragment implements KittenClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "key";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;
    private String keyId;*/
   // private ArrayList<MagazineBeanMain> allData = new ArrayList<>();
    RecyclerView recyclerViewList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MagazineAdapter adapterMagazine;
    ArrayList<Magazine> beanArrayList;
    String keyId;



    public NewArrivalFragment(String id) {
        // Required empty public constructor
        keyId = id;




    }



    public void init(View v){
        recyclerViewList = (RecyclerView)v.findViewById(R.id.recycler_view_list);
        recyclerViewList.setHasFixedSize(true);
        beanArrayList = new ArrayList<>();
        if(beanArrayList != null) {

            GetNewArrivalsTask getNewArrivalsTask = new GetNewArrivalsTask();
            getNewArrivalsTask.execute();
        }
    }

    @Override
    public void onKittenClicked(MagazineAdapter.SingleItemRowHolder holder, int position,Magazine magazine) {

        DetailsFragment kittenDetails = DetailsFragment.newInstance(magazine);
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




    public NewArrivalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewArrivalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewArrivalFragment newInstance(String param1, String param2) {
        NewArrivalFragment fragment = new NewArrivalFragment();
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
        View v = inflater.inflate(R.layout.fragment_new_arrival, container, false);
        init(v);
        return v;

    }

    /*public void getAPI(){
        GetNewArrivalsTask mPreLaunchTask = new GetNewArrivalsTask();
        mPreLaunchTask.execute((String) null);
        Log.e("New Arrival","<===============>");
    }*/

    public void setAdapter(ArrayList<Magazine> beanArrayList){
        adapterMagazine = new MagazineAdapter(getActivity(),beanArrayList,(KittenClickListener)this);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewList.setAdapter(adapterMagazine);
    }



    private class GetNewArrivalsTask extends AsyncTask<String, String, String> {
        GetNewArival category;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String  resultToDisplay = "";
            category = new GetNewArival();
            category.init(keyId);
            beanArrayList = category.getNewArrival();

            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();

            setAdapter(beanArrayList);

        }

    }







}
