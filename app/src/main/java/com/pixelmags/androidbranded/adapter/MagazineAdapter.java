package com.pixelmags.androidbranded.adapter;

/**
 * Created by sejeeth on 8/9/17.
 */

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.pixelmags.android.bean.MagazineBeanMain;
import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.android.pixelmagsapp.R;
import com.pixelmags.android.ui.uicomponents.MultiStateButton;
import com.pixelmags.androidbranded.bean.RecyclerViewClick;
import com.pixelmags.androidbranded.download.KittenClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MagazineAdapter extends RecyclerView.Adapter<MagazineAdapter.SingleItemRowHolder> {

    private ArrayList<Magazine> itemsList;
    private Context mContext;
    private static KittenClickListener itemListener;


    public MagazineAdapter(Context context, ArrayList<Magazine> itemsList,KittenClickListener kittenClickListener) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.itemListener = kittenClickListener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.magazine_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {
     ViewCompat.setTransitionName(holder.imageView, String.valueOf(i) + "_image");
        holder.issueTitleText.setText(itemsList.get(i).title);
        //holder.download.setText("\\u20B9 "+itemsList.get(i).price);
        holder.issuePriceButton.setText("\u20B9 "+itemsList.get(i).price);
        final Magazine mag = itemsList.get(i);

        Picasso.with(mContext)
                .load(itemsList.get(i).thumbnailURL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onKittenClicked((SingleItemRowHolder)holder, i,mag);
            }
        });
       // MagazineBeanMain singleItem = itemsList.get(i);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();

    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView issueTitleText;
        public MultiStateButton issuePriceButton;
        public ImageView imageView;


        public SingleItemRowHolder(View view) {
            super(view);

            this.issueTitleText = (TextView) view.findViewById(R.id.gridTitleText);
            this.imageView = (ImageView) view.findViewById(R.id.gridImage);
            this.issuePriceButton = (MultiStateButton)view.findViewById(R.id.gridMultiStateButton);
        }

    }

}