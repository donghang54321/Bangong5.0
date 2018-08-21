package com.rentian.newoa.modules.apply.view.iview;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.common.view.timePicker.TimeSelector;

/**
 * 申请页面 → 时间选择器
 */
public class ApplyTimePicker extends FrameLayout {

    private Context mContext;

    private TextView title, content;

    private TimeSelector timeSelector;

    public ApplyTimePicker(Context context) {
        this(context, null);
    }

    public ApplyTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApplyTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.apply_time_picker, this);
        title = (TextView) findViewById(R.id.apply_time_picker_title);
        content = (TextView) findViewById(R.id.apply_time_picker_content);
        this.mContext = context;
        listener();
    }

    private void listener() {
        timeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                setContentText(time.substring(0,10));
                Log.e("time",time.substring(0,10));
            }
        }, "2018-01-01 00:00", "2025-12-31 00:00");
        timeSelector.setScrollUnit(TimeSelector.SCROLLTYPE.YEAR, TimeSelector.SCROLLTYPE.MONTH,
                TimeSelector.SCROLLTYPE.DAY, TimeSelector.SCROLLTYPE.HOUR, TimeSelector.SCROLLTYPE.MINUTE);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector.show();
            }
        });
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setContentHint(String s) {
        content.setText(s);
    }

    public String getContentText() {
        return content.getText().toString();
    }

    public void setContentText(String s) {
        content.setText(s);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                //从初始的int类型的颜色值中解析出Alpha、Red、Green、Blue四个分量
                int startInt = (Integer) startValue;
                int startA = (startInt >> 24) & 0xff;
                int startR = (startInt >> 16) & 0xff;
                int startG = (startInt >> 8) & 0xff;
                int startB = startInt & 0xff;

                //从终止的int类型的颜色值中解析出Alpha、Red、Green、Blue四个分量
                int endInt = (Integer) endValue;
                int endA = (endInt >> 24) & 0xff;
                int endR = (endInt >> 16) & 0xff;
                int endG = (endInt >> 8) & 0xff;
                int endB = endInt & 0xff;

                //分别对Alpha、Red、Green、Blue四个分量进行计算，
                //最终合成一个完整的int型的颜色值
                return (startA + (int) (fraction * (endA - startA))) << 24 |
                        (startR + (int) (fraction * (endR - startR))) << 16 |
                        (startG + (int) (fraction * (endG - startG))) << 8 |
                        (startB + (int) (fraction * (endB - startB)));
            }
        }, R.color.colorPrimary, Color.BLACK);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                content.setTextColor(color);
            }
        });
        valueAnimator.start();
    }
}
