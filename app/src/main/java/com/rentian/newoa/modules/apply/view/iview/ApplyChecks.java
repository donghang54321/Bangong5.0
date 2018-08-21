package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentian.newoa.R;

import java.util.ArrayList;

/**
 * 申请页面 → 多项选择
 */
public class ApplyChecks extends FrameLayout {

    private Context mContext;

    private TextView title;

    private LinearLayout content;
    private ArrayList<AppCompatCheckBox> views;

    public ApplyChecks(Context context) {
        this(context, null);
    }

    public ApplyChecks(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyChecks(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.apply_check, this);
        title = (TextView) findViewById(R.id.apply_check_title);
        content = (LinearLayout) findViewById(R.id.apply_check_content);
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setContentData(ArrayList<String> datas) {
        for (int i = 0; i < datas.size(); i++) {
            AppCompatCheckBox checkBox = new AppCompatCheckBox(mContext);
            checkBox.setText(datas.get(i));
            checkBox.setTextSize(16f);
            checkBox.setLayoutParams(
                    new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT
                                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            views.add(checkBox);
            content.addView(checkBox);
        }
    }

    public ArrayList<String> getContentSelecteds() {
        ArrayList<String> selecteds = new ArrayList<>();
        if (null == views || views.size() == 0) {
            return selecteds;
        }
        for (AppCompatCheckBox checkBox : views) {
            if (checkBox.isChecked()) {
                selecteds.add(checkBox.getText().toString());
            }
        }
        return selecteds;
    }
}
