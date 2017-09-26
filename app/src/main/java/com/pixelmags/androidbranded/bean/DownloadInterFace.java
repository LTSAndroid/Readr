package com.pixelmags.androidbranded.bean;

import com.pixelmags.android.datamodels.Magazine;
import com.pixelmags.androidbranded.adapter.DetailMagzineAdapter;

import java.util.ArrayList;

/**
 * Created by sejeeth on 26/9/17.
 */

public interface DownloadInterFace {

    void onKittenClicked(DetailMagzineAdapter.SingleItemRowHolder holder, int position, Magazine magazine, ArrayList<Magazine> magazineArrayList);
}
