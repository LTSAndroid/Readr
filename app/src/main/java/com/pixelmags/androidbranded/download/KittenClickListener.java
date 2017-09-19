package com.pixelmags.androidbranded.download;

import android.support.v7.widget.RecyclerView;

import com.pixelmags.android.IssueView.NewIssueView;
import com.pixelmags.androidbranded.adapter.MagazineAdapter;

/**
 * Listener for kitten click events in the grid of kittens
 *
 * @author bherbst
 */
public interface KittenClickListener {
    /**
     * Called when a kitten is clicked
     * @param holder The ViewHolder for the clicked kitten
     * @param position The position in the grid of the kitten that was clicked
     */
    void onKittenClicked(MagazineAdapter.SingleItemRowHolder holder, int position);
}
