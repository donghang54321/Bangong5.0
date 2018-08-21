//package com.rentian.rtsxy.common.view;
//
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.rentian.rentianoa.R;
//
//public class BubbleDragView extends RelativeLayout {
//
//    public static final int BACK_GROUND_COLOR = 0xfff15b6c;
//    public static final int TEXT_COLOR = 0xffffffff;
//
//    /**
//     * 气泡显示数量
//     */
//    private String lastCount = "";
//    private String newCount;
//
//    /**
//     * 气泡背景颜色
//     */
//    private int backGroundColor;
//
//    /**
//     * 气泡文字颜色
//     */
//    private int textColor;
//
//    /**
//     * 绘制是的范围
//     */
//    private Paint mPaint = new Paint();
//
//    /**
//     * 计数显示控件
//     */
//    private TextView mTextView;
//
//    /**
//     * 是否进行着动画
//     */
//    private boolean isAnim;
//
//    /**
//     * 是否第一次运行
//     */
//    private boolean isFirst = true;
//
//    public BubbleDragView(Context context) {
//        this(context, null);
//    }
//
//    public BubbleDragView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    /**
//     * 获取自定义样式属性
//     *
//     * @param context
//     * @param attrs
//     * @param defStyleAttr
//     */
//    public BubbleDragView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        /**
//         * 获得我们所定义的自定义样式属性
//         */
//        TypedArray array = context.getTheme().obtainStyledAttributes
//                (attrs, R.styleable.BubbleDragView, defStyleAttr, 0);
//        backGroundColor = array.getColor(R.styleable.BubbleDragView_bd_backGround, BACK_GROUND_COLOR);
//        textColor = array.getColor(R.styleable.BubbleDragView_bd_textColor, TEXT_COLOR);
//        this.setVisibility(INVISIBLE);
//        init();
//    }
//
//    private void init() {
//        mPaint.setAntiAlias(true);
//        if (Build.VERSION.SDK_INT > 11) {
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//        mTextView = new TextView(getContext());
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mTextView.setPadding(12, 0, 12, 0);
//        mTextView.setTextSize(12);
//        mTextView.setTextColor(textColor);
//        mTextView.setLayoutParams(params);
//        addView(mTextView);
//    }
//
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        mPaint.setColor(backGroundColor);
//        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), getHeight() / 2, getHeight() / 2, mPaint);
//        super.dispatchDraw(canvas);
//    }
//
//    /**
//     * 更改数值
//     */
//    public void setCount(int count) {
//        if (count > 99) {
//            newCount = "99+";
//        } else {
//            newCount = count + "";
//        }
//        if (lastCount.equals(newCount)) {
//            lastCount = newCount;
//        } else {
//            setAnim(count);
//        }
//    }
//
//    private void setAnim(int count) {
//        if (count <= 0) {
//            this.setVisibility(INVISIBLE);
//            isFirst = true;
//        } else {
//            this.setVisibility(VISIBLE);
//            if (isFirst) {
//                mTextView.setText(newCount);
//                lastCount = newCount;
//                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
//                alphaAnimation.setDuration(500);
//                alphaAnimation.setStartOffset(500);
//                this.startAnimation(alphaAnimation);
//                isAnim = true;
//                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        isAnim = false;
//                        isFirst = false;
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            } else {
//                if (!isAnim) {
//                    isAnim = true;
//                    ObjectAnimator rotationY1 = ObjectAnimator.ofFloat(this, "rotationY", 0f, 90f);
//                    rotationY1.setDuration(500);
//                    rotationY1.start();
//                    handler.sendEmptyMessageDelayed(0, 500);
//                    ObjectAnimator rotationY2 = ObjectAnimator.ofFloat(this, "rotationY", -90f, 0f);
//                    rotationY2.setDuration(500);
//                    rotationY2.setStartDelay(500);
//                    rotationY2.start();
//                    handler.sendEmptyMessageDelayed(1, 1000);
//                } else {
//                    mTextView.setText(newCount);
//                    lastCount = newCount;
//                }
//            }
//        }
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    mTextView.setText(newCount);
//                    lastCount = newCount;
//                    break;
//                case 1:
//                    isAnim = false;
//            }
//        }
//    };
//}
