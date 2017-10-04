package com.pixelmags.androidbranded.adapter;

/**
 * Created by sejeeth on 20/9/17.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.pixelmags.android.bean.MagazineBeanMain;
import com.pixelmags.android.comms.Config;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.android.storage.UserPrefs;
import com.pixelmags.android.ui.AllIssuesFragment;
import com.pixelmags.android.ui.LoginFragment;
import com.pixelmags.android.util.GetInternetStatus;
import com.pixelmags.androidbranded.bean.DownloadInterFace;
import com.pixelmags.androidbranded.bean.RecyclerViewClick;
import com.pixelmags.androidbranded.download.KittenClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailMagzineAdapter extends RecyclerView.Adapter<DetailMagzineAdapter.SingleItemRowHolder> {

    private ArrayList<Magazine> itemsList;
    private Context mContext;
    AllIssuesFragment.DownloadPreviewImagesAsyncTask mPreviewImagesTask;
    private static DownloadInterFace itemListener;



    public DetailMagzineAdapter(Context context, ArrayList<Magazine> itemsList,DownloadInterFace interFace) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.itemListener = interFace;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.magazine_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {
        ViewCompat.setTransitionName(holder.itemImage, String.valueOf(i) + "_image");
        holder.tvTitle.setText(itemsList.get(i).title);
        final Magazine mag = itemsList.get(i);

        Picasso.with(mContext)
                .load(itemsList.get(i).thumbnailURL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onKittenClicked(holder,i,mag,itemsList);


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemsList.size();

    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        public TextView download;
        public ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

           /* this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.download = (TextView)view.findViewById(R.id.download_issue);*/


           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });*/


        }

    }


    public void downloadButtonClicked(int position) {


    }

}
