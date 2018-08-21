package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rentian.newoa.R;

import java.util.ArrayList;

/**
 * 申请页面 → 单选按钮
 */
public class ApplyRadios extends FrameLayout {

    private Context mContext;

    private TextView title;

    private RadioGroup radioGroup;
    private ArrayList<String> mDatas;

    public ApplyRadios(Context context) {
        this(context, null);
    }

    public ApplyRadios(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyRadios(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.apply_radio, this);
        title = (TextView) findViewById(R.id.apply_radio_title);
        radioGroup = (RadioGroup) findViewById(R.id.apply_radio_Group);
        this.mContext = context;

    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setRadioGroup(ArrayList<String> datas) {
        this.mDatas = datas;
        for (int i = 0; i < datas.size(); i++) {
            AppCompatRadioButton radioButton = new AppCompatRadioButton(mContext);
            radioButton.setText(datas.get(i));
            radioButton.setTextColor(Color.BLACK);
            radioGroup.addView(radioButton);
        }
    }

    public String getCheckedRadioText() {
        int temp = radioGroup.getCheckedRadioButtonId();
        AppCompatRadioButton radioButton= (AppCompatRadioButton) findViewById(temp);

        if (temp == -1) {
            return "null";
        }
        return radioButton.getText().toString();
    }
}
