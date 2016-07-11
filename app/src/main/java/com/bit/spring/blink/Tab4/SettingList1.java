package com.bit.spring.blink.Tab4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bit.spring.blink.R;

/**
 * Created by bitbit on 2015-12-04.
 */
public class SettingList1 extends Fragment {

    TextView text;
    ImageButton exit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.setting_1, null);

        text = (TextView) v.findViewById(R.id.timetext1);

        exit = (ImageButton) v.findViewById(R.id.exit1);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });
        return v;
    }

}
