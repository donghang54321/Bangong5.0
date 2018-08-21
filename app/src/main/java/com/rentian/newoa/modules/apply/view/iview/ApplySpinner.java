package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.common.view.nicespinner.NiceSpinner;
import com.rentian.newoa.common.view.nicespinner.NiceSpinnerAdapter;

import java.util.ArrayList;

/**
 * 申请页面 → 下拉选择框
 */
public class ApplySpinner extends FrameLayout {
    private NiceSpinnerAdapter adapter;

    private Context mContext;


    private TextView title;

    private NiceSpinner spinner;

    public ApplySpinner(Context context) {
        this(context, null);
    }

    public ApplySpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.apply_spinner, this);
        title = (TextView) findViewById(R.id.apply_spinner_title);
        spinner = (NiceSpinner) findViewById(R.id.apply_spinner_content);
        this.mContext = context;
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setSpinnerDatas(ArrayList<String> datas) {
        adapter=new NiceSpinnerAdapter(mContext,datas);
        spinner.setAdapter(adapter);

    }

    public String getSpinnerSelected() {
        return adapter.getItem(spinner.getSelectedIndex()).toString();
    }
}
