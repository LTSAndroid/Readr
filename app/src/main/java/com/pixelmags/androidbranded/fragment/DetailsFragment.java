package com.pixelmags.androidbranded.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelmags.android.api.GetDocumentKey;
import com.pixelmags.android.api.GetIssue;
import com.pixelmags.android.api.GetPreviewImages;
import com.pixelmags.android.comms.Config;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.datamodels.PreviewImage;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.android.storage.SingleIssuePreviewDataSet;
import com.pixelmags.android.storage.UserPrefs;
import com.pixelmags.android.ui.AllIssuesFragment;
import com.pixelmags.android.ui.DownloadFragment;
import com.pixelmags.android.ui.LoginFragment;
import com.pixelmags.android.util.BaseApp;
import com.pixelmags.android.util.GetInternetStatus;
import com.pixelmags.androidbranded.adapter.DetailMagzineAdapter;
import com.pixelmags.androidbranded.api.GetDetailsandMagazine;
import com.pixelmags.androidbranded.bean.DownloadInterFace;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Display details for a given kitten
 *
 * @author bherbst
 */
public class DetailsFragment extends Fragment implements DownloadInterFace{
    private static final String ARG_KITTEN_NUMBER = "Object";

    private static int Kitten = 0;
    private   ArrayList<Magazine> beanArrayList ;
    DetailMagzineAdapter adapterMagazine;
    RecyclerView recyclerViewList;
    ProgressDialog progressBar;
    ArrayList<Magazine> magazinesList;




    /**
     * Create a new DetailsFragment
     * @param magazine The number (between 1 and 6) of the kitten to display
     */
    public static DetailsFragment newInstance(Magazine magazine) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_KITTEN_NUMBER, magazine);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_fragment, container, false);

        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        beanArrayList = new ArrayList<>();
        magazinesList = new ArrayList<>();
        ImageView image = (ImageView) view.findViewById(R.id.detailsimage);
        TextView text = (TextView)view.findViewById(R.id.detailstitle);
        recyclerViewList = (RecyclerView)view.findViewById(R.id.recycler_view_details);
        Bundle args = getArguments();
        Magazine kittenNumber = args.getParcelable(ARG_KITTEN_NUMBER);
        Log.e("Kitten",kittenNumber.toString());

        if(kittenNumber != null) {
            Picasso.with(getActivity())
                    .load(kittenNumber.thumbnailURL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(image);
            GetFeaturedArrivalsTask getFeaturedArrivalsTask = new GetFeaturedArrivalsTask(kittenNumber.id+"");
            getFeaturedArrivalsTask.execute();

        }else{
            Log.e("Bean Error","***********");

        }




    }

    public void setAdaptesr(ArrayList<Magazine> beanArrayList){
        // Log.e("ArraySize",beanArrayList.size()+"");

        if(beanArrayList != null) {
            adapterMagazine = new DetailMagzineAdapter(getActivity(), beanArrayList,(DownloadInterFace) this);
            recyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewList.setAdapter(adapterMagazine);
        }
    }

    @Override
    public void onKittenClicked(DetailMagzineAdapter.SingleItemRowHolder holder, int position, Magazine magazine, ArrayList<Magazine> magazineArrayList) {
       magazinesList = magazineArrayList;
        GetInternetStatus getInternetStatus = new GetInternetStatus(getActivity());
        if(getInternetStatus.isNetworkAvailable()){
            Log.e("Magazine Id <===> Issue ID",magazinesList.get(position).magazine_id+" === "+magazinesList.get(position).id);
            DownloadPreviewImagesAsyncTask mPreviewImagesTask;
            mPreviewImagesTask = new DownloadPreviewImagesAsyncTask(magazinesList.get(position).magazine_id, String.valueOf(magazinesList.get(position).id),position);
            mPreviewImagesTask.execute((String) null);

        }else{
            getInternetStatus.showAlertDialog();
        }

    }

    public class DownloadPreviewImagesAsyncTask extends AsyncTask<String, String, String> {

        ArrayList<PreviewImage> previewImageArrayList;
        private String mIssueID;
        private String magID;
        private int position;

        DownloadPreviewImagesAsyncTask(String magID, String issueID, int position) {
            mIssueID = issueID;
            this.magID = magID;
            previewImageArrayList = null;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = new ProgressDialog(getActivity());
            if (progressBar != null) {
                progressBar.show();
                progressBar.setCancelable(false);
                progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressBar.setContentView(R.layout.progress_dialog);
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            String resultToDisplay = "";

            try {

                GetPreviewImages getPreviewImages = new GetPreviewImages();
                previewImageArrayList = getPreviewImages.init(mIssueID, Config.Bundle_ID);


            }catch (Exception e){
                e.printStackTrace();
            }
            return resultToDisplay;

        }

        protected void onPostExecute(String result) {

           Log.e("Pro  =>",previewImageArrayList.size()+"");

            try{
                if(previewImageArrayList != null){

                   /* Log.e("Image Preview Table ","Table Name of the preview Image is : "+"Preview_Issue_Table_"+magID+mIssueID);

                    SingleIssuePreviewDataSet mDbDownloadTableWriter = new SingleIssuePreviewDataSet(BaseApp.getContext());

                    boolean resultInsertion = mDbDownloadTableWriter.initFormationOfSingleIssueDownloadTable(mDbDownloadTableWriter.getWritableDatabase(),
                            "Preview_Issue_Table_"+magID+mIssueID, previewImageArrayList);

                    mDbDownloadTableWriter.close();*/


                    DownloadIssue downloadIssue;
                    downloadIssue = new DownloadIssue(position);
                    downloadIssue.execute((String) null);

                }else{

                    if(progressBar != null)
                        progressBar.dismiss();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }



    public class DownloadIssue extends AsyncTask<String, String, String> {

        private int position;
        private String issueId;

        public DownloadIssue(int position){
            this.position = position;
        }


        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            Log.e("Issue Coordinates ==> ","1,2,3,4");
            String resultToDisplay = "";

            try {
                issueId = String.valueOf(magazinesList.get(position).id);
                GetIssue getIssue = new GetIssue();
                getIssue.init(issueId);

            }catch (Exception e){
                e.printStackTrace();

            }
            return resultToDisplay;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(progressBar != null)
                progressBar.dismiss();

            boolean status = GetIssue.setGetIssueFailure();



            if(status){

             /*  *//* new AlertDialog.Builder(getActivity())
                        .setTitle("Issue Download Failed!")
                        .setMessage("Please contact customer care for support.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.draw*//*able.ic_dialog_alert)
                        .show();*/
                Toast.makeText(getActivity(),"Download Failed",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getActivity(),"Download started",Toast.LENGTH_LONG).show();




               /* new AlertDialog.Builder(getActivity())
                        .setTitle("Issue Download!")
                        .setMessage("You can view your Issue in download section.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                *//*currentPage =getString(R.string.menu_title_downloads);*//*


                                Fragment fragmentDownload = new DownloadFragment();
                                // Insert the fragment by replacing any existing fragment
                                FragmentManager allIssuesFragmentManager = getFragmentManager();
                                allIssuesFragmentManager.beginTransaction()
                                        .replace(R.id.main_fragment_container, fragmentDownload,"DownloadFragment")
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        //       .addToBackStack(null)
                                        .commit();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {

                                dialog.dismiss();


                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/
            }





        }


    }


    private class GetFeaturedArrivalsTask extends AsyncTask<String, String, String> {
        GetDetailsandMagazine category;
        String ids;


        public  GetFeaturedArrivalsTask(String id){
            this.ids = id;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String  resultToDisplay = "";
            category = new GetDetailsandMagazine();
            category.init(ids);
            beanArrayList = category.getDetailsandMagazineData();
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPreExecute();
            setAdaptesr(beanArrayList);

        }

    }





}
