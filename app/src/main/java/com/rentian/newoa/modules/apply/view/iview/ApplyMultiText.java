package com.rentian.newoa.modules.apply.view.iview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rentian.newoa.R;

/**
 * 申请页面 → 多行文本
 */
public class ApplyMultiText extends FrameLayout {

    private TextView title;
    private EditText editText;

    public ApplyMultiText(Context context) {
        this(context, null);
    }

    public ApplyMultiText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyMultiText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.apply_multi_text, this);
        title = (TextView) findViewById(R.id.apply_multi_text_title);
        editText = (EditText) findViewById(R.id.apply_multi_text_et);
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
}
