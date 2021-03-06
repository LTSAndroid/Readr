package com.pixelmags.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixelmags.android.pixelmagsapp.R;

/**
 * Created by Annie on 29/09/15.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {
    private TextView Version;
    private TextView Copyright;
    private TextView Url;
    private TextView Url1;
    private String TAG = "AboutFragment";
    private Spanned Text;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // set the AboutFragment Listener
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        Version = (TextView) rootView.findViewById(R.id.Version);
        Copyright = (TextView) rootView.findViewById(R.id.copyright);
        Url = (TextView) rootView.findViewById(R.id.ourwebsite);
        Url1 = (TextView) rootView.findViewById(R.id.ourconditions);
        Url1.setOnClickListener(this);
//
//        Text = Html.fromHtml("View Terms &amp; Conditions<br />" +
//                "<a href='http://www.pixelmags.com/t+c/current/'>http://www.pixelmags.com/t+c/current/</a>");
//        Url1.setMovementMethod(LinkMovementMethod.getInstance());
//        Url1.setText(Text);

        return rootView;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ourconditions){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pixelmags.com/t+c/current/"));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button
                    Fragment fragment = new AllIssuesFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("Update", "Success");
                    fragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, fragment, "All Issues")
                            .commit();

                    return true;

                }

                return false;
            }
        });
    }
}

