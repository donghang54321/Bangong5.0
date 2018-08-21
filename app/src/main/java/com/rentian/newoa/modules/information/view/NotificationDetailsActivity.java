package com.rentian.newoa.modules.information.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;


public class NotificationDetailsActivity extends AppCompatActivity {

    private String url;

    private WebView wv;
    private ImageView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_notification_detials);
        url = Const.RTOA.URL_INFORMATION_DETAIL +
                "?id=" + getIntent().getLongExtra("id", 0) +
                "&uid=" + MyApp.getInstance().getMyUid();

        wv = (WebView) findViewById(R.id.wv_notification_details);
        initComponent();
    }


    private void initComponent() {


        //支持JS
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

        wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setBackgroundColor(0xdadee2); // 设置背景色
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                AlphaAnimation a = new AlphaAnimation(0f, 1f);
                a.setDuration(200);
                wv.startAnimation(a);
            }

            /**
             * 加载失败运行方法
             * @param view
             * @param errorCode
             * @param description
             * @param failingUrl
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Animation animation = AnimationUtils.loadAnimation(NotificationDetailsActivity.this, R.anim.login_btn_repeat);
                error.startAnimation(animation);
            }

        });

        wv.loadUrl(url);
    }

    public void onBack(View v) {
        finish();
    }
}
