package com.bit.spring.blink.Tab4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bit.spring.blink.R;

/**
 * Created by bitbit on 2015-12-04.
 */
public class SettingList4 extends Fragment implements View.OnClickListener {

    ImageButton exit;
    TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.setting_4, null);

        text = (TextView) v.findViewById(R.id.textView4);

        return v;
    }

    @Override
    public void onClick(View v) {

    }

}
