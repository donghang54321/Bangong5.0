package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rentian.newoa.R;

/**
 * 申请页面 → 单行文本
 */
public class ApplySingleText extends FrameLayout {

    private TextView title;

    private EditText editText;

    private View v;

    public ApplySingleText(Context context) {
        this(context, null);
    }

    public ApplySingleText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplySingleText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        v=LayoutInflater.from(context).inflate(R.layout.apply_single_text, this);
        title = (TextView) findViewById(R.id.apply_single_text_title);
        editText = (EditText) findViewById(R.id.apply_single_text_et);
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setContentHint(String s) {
        editText.setHint(s);
    }

    public String getContentText() {
        return editText.getText().toString();
    }

    public void setContentTextEnabled(boolean isEnabled) {
        editText.setEnabled(isEnabled);
    }
    public void setBackground() {
        title.setBackgroundResource(R.drawable.stock_button);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
    }
}
