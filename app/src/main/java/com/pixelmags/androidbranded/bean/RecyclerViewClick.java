package com.pixelmags.androidbranded.bean;

import android.view.View;

import com.pixelmags.androidbranded.adapter.MagazineAdapter;

/**
 * Created by sejeeth on 13/9/17.
 */

public interface RecyclerViewClick {

    public void recyclerViewListClicked(MagazineAdapter.SingleItemRowHolder v, int position);
}
