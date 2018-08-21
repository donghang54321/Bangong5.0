package com.rentian.newoa.modules.examiation.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.callback.CallbackOk;
import com.okhttplib.callback.ProgressCallback;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.FillListView;
import com.rentian.newoa.common.view.timeline.adapter.TimeLineAdapter;
import com.rentian.newoa.common.view.timeline.bean.TimeLineItem;
import com.rentian.newoa.modules.apply.bean.ApplyView;
import com.rentian.newoa.modules.apply.view.ApplyActivity;
import com.rentian.newoa.modules.apply.view.iview.ApplyMultiText;
import com.rentian.newoa.modules.apply.view.iview.ApplySingleText;
import com.rentian.newoa.modules.examiation.bean.ExamiationDetail;
import com.rentian.newoa.modules.examiation.bean.Node;
import com.rentian.newoa.modules.examiation.bean.ProcessInfo;
import com.rentian.newoa.modules.examiation.bean.ProcessList;
import com.rentian.newoa.modules.examiation.view.iview.ControlView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class ExaminationAcitity extends AppCompatActivity {
    private String taskId;
    private ExamiationDetail data;
    private ArrayList<ApplyView> views = new ArrayList<>();
    private ControlView controlView;
    private LinearLayout filler;
    private TextView tv_title;
    private WebView wv;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_examination_acitity);
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        type = getIntent().getIntExtra("type", 0);
        filler = (LinearLayout) findViewById(R.id.apply_filler);
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("申请单详情");
        taskId = getIntent().getStringExtra("taskId");
//        conn = new MyServiceConnection();
//        bindService(new Intent(ExaminationAcitity.this, MyService.class), conn,
//                Context.BIND_AUTO_CREATE);
        wv = (WebView) findViewById(R.id.wv_examination_details);
//        update();
        postAsync();
    }

    private void addSingleText(String title, String hint) {
        ApplySingleText singleText = new ApplySingleText(this);
        singleText.setTitle(title);
        singleText.setContentHint(hint);
        singleText.setContentTextEnabled(false);
        ApplyView temp = new ApplyView(ApplyActivity.ViewType.singleText, singleText);
        views.add(temp);
    }

    private void addFile(String title, final String hint, final String url) {
        ApplySingleText singleText = new ApplySingleText(this);
        singleText.setTitle(title);
        singleText.setContentHint(hint);
        singleText.setContentTextEnabled(false);
        singleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtill.showMessage("正在下载，已添加至下载列表");
//                myBinder.downloadNewApk(url, hint);
                download(url);

            }
        });
        singleText.setBackground();
        ApplyView temp = new ApplyView(ApplyActivity.ViewType.singleText, singleText);
        views.add(temp);
    }


    private void addMultiText(String title, String hint) {
        ApplyMultiText multiText = new ApplyMultiText(this);
        multiText.setTitle(title);
        multiText.setContentHint(hint);
        multiText.setContentTextEnabled(false);
        ApplyView temp = new ApplyView(ApplyActivity.ViewType.multiText, multiText);
        views.add(temp);
    }

    private void addTimeLine(ArrayList<TimeLineItem> data) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,//
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 30, 0, 0);
        FillListView listView = new FillListView(this);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(Color.WHITE);
        listView.setLayoutParams(layoutParams);
        TimeLineAdapter adapter = new TimeLineAdapter(this, data);
        listView.setAdapter(adapter);
        ApplyView temp = new ApplyView(ApplyActivity.ViewType.timeLine, listView);
        views.add(temp);
    }

    private void addControlView() {
        controlView = new ControlView(ExaminationAcitity.this);
        controlView.setTaskId(taskId);
        controlView.setSweetSheet((RelativeLayout) findViewById(R.id.examination_root), getScreenWidth());
        controlView.setActivity(this);
        ApplyView temp = new ApplyView(ApplyActivity.ViewType.control, controlView);
        views.add(temp);
    }

    private void postAsync() {
        views.clear();
        String url = null;
        String param = null;
        if (type == 0) {
            url = Const.RTOA.URL_EXAMINATION_DETAIL;
            param = "taskId";
            Log.e("json1", url);

        } else if (type == 1) {
            url = Const.RTOA.URL_CHECK_APPLY_DETAIL;
            param = "eventid";
            Log.e("json2", url);
        }
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(url)
                        .addParam("uid", MyApp.getInstance().getMyUid())
                        .addParam(param, taskId)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("examination", info.getRetDetail());
                            Type type1 = new TypeToken<ExamiationDetail>() {
                            }.getType();

                            data = CommonUtil.gson.fromJson(info.getRetDetail(), type1);
                            ArrayList<Node> list = data.node;
                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).field_type.equals(ApplyActivity.ViewType.singleText) || list.get(i).field_type.equals(ApplyActivity.ViewType.timePicker)) { // 单行文本
                                        addSingleText(list.get(i).label, list.get(i).field_options.data.get(0).value);
                                    } else if (list.get(i).field_type.equals(ApplyActivity.ViewType.multiText)) { // 多行文本
                                        if (list.get(i).field_options.data == null) {
                                            addMultiText(list.get(i).label, "");
                                        } else {
                                            addMultiText(list.get(i).label, list.get(i).field_options.data.get(0).value);
                                        }
                                    } else if (list.get(i).field_type.equals(ApplyActivity.ViewType.enclosure)) {

                                        String url = Const.RTOA.URL_DOWNLOAD_ATTACHMENTS + "?dfid=" + list.get(i).field_options.data.get(0).value;
                                        addFile("点击下载附件", list.get(i).file, url);

                                    }
                                }

                                addSingleText("申请人：", data.name);

                                addTimeLine(processTimeLineData(data.list));
                                if (type == 0)
                                    addControlView();

                                for (int i = 0; i < views.size(); i++) {
                                    filler.addView(views.get(i).getView());
                                }

                            } else {
                                wv.setVisibility(View.VISIBLE);
                                initComponent();
                            }
                        } else {
                            Log.e("examination", info.getRetDetail());

                        }
                    }
                }
        );

    }

    class UpdateTextTask extends AsyncTask<Void, Integer, Integer> {
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            String baseJson = "";
            if (type == 0) {
                String url = Const.RTOA.URL_EXAMINATION_DETAIL +
                        "?uid=" + MyApp.getInstance().getMyUid() +
                        "&taskId=" + taskId;
                baseJson = HttpURLConnHelper.requestByGET(
                        url);
                Log.e("json1", url);

            } else if (type == 1) {
                String url = Const.RTOA.URL_CHECK_APPLY_DETAIL +
                        "?uid=" + MyApp.getInstance().getMyUid() +
                        "&eventid=" + taskId;
                baseJson = HttpURLConnHelper.requestByGET(
                        url);
                Log.e("json2", url);
            }
//            Log.e("json", url);
            Type type = new TypeToken<ExamiationDetail>() {
            }.getType();

            data = CommonUtil.gson.fromJson(baseJson, type);
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
//            tv_title.setText(data.form);

            ArrayList<Node> list = data.node;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).field_type.equals(ApplyActivity.ViewType.singleText) || list.get(i).field_type.equals(ApplyActivity.ViewType.timePicker)) { // 单行文本
                        addSingleText(list.get(i).label, list.get(i).field_options.data.get(0).value);
                    } else if (list.get(i).field_type.equals(ApplyActivity.ViewType.multiText)) { // 多行文本
                        if (list.get(i).field_options.data == null) {
                            addMultiText(list.get(i).label, "");
                        } else {
                            addMultiText(list.get(i).label, list.get(i).field_options.data.get(0).value);
                        }
                    } else if (list.get(i).field_type.equals(ApplyActivity.ViewType.enclosure)) {

                        String url = Const.RTOA.URL_DOWNLOAD_ATTACHMENTS + "?dfid=" + list.get(i).field_options.data.get(0).value;
                        addFile("点击下载附件", list.get(i).file, url);

                    }
                }

                addSingleText("申请人：", data.name);

                addTimeLine(processTimeLineData(data.list));
                if (type == 0)
                    addControlView();

                for (int i = 0; i < views.size(); i++) {
                    filler.addView(views.get(i).getView());
                }

            } else {
                wv.setVisibility(View.VISIBLE);
                initComponent();
//                Toast.makeText(ExaminationAcitity.this, "手机端不支持此功能，请在网页端查看", Toast.LENGTH_SHORT).show();
//                finish();
//                overridePendingTransition(0, 0);
            }
        }
    }

    private ArrayList<TimeLineItem> processTimeLineData(ArrayList<ProcessList> data) {
        ArrayList<TimeLineItem> timeLineData = new ArrayList<>();
        int tempItem = -1;

        for (int i = 0; i < data.size(); i++) {
            TimeLineItem temp = new TimeLineItem();
            String tempTitle = data.get(i).doc;

            ArrayList<ProcessInfo> info = data.get(i).info;
            if (null != info.get(0).name) {
                tempTitle += "，" + info.get(0).name;
            }
            if (null != info.get(0).post) {
                tempTitle += "(" + info.get(0).post + ")";
            }
            temp.setTitle(tempTitle);
            temp.setDetail(info.get(0).notice);
            if (info.get(0).time2 != null) {
                temp.setTime(info.get(0).time2);
                temp.setIcon(getResources().getDrawable(R.drawable.circle_checked));
            } else {
                if (tempItem == -1)
                    tempItem = i;
            }
            timeLineData.add(temp);
        }

        if (tempItem != -1) {
            timeLineData.get(tempItem).setIcon(getResources().getDrawable(R.drawable.circle_right));
        }
        return timeLineData;
    }

    public void onBack(View view) {
        finish();
    }

    public int getScreenWidth() {
        WindowManager windowManager = this.getWindowManager();
        return windowManager.getDefaultDisplay().getWidth();
    }

    @Override
    public void onBackPressed() {
        if (null != controlView) {
            if (controlView.isShow()) {
                controlView.dismiss();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void update() {
        views.clear();
        UpdateTextTask updateTextTask = new UpdateTextTask();
        updateTextTask.execute();
    }

    private void initComponent() {


        //支持JS
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(true);

        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setBackgroundColor(0xdadee2); // 设置背景色
        wv.setWebChromeClient(new WebChromeClient());
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
            }

        });

        Log.e("msg", Const.RTOA.URL_APPLY_DETAIL2 +
                "?uid=" + MyApp.getInstance().getMyUid() +
                "&eventid=" + taskId);
        if (type == 0) {
            wv.loadUrl(Const.RTOA.URL_EXAMINATION_DETAIL2 +
                    "?uid=" + MyApp.getInstance().getMyUid() +
                    "&taskId=" + taskId);
        } else {
            wv.loadUrl(Const.RTOA.URL_APPLY_DETAIL2 +
                    "?uid=" + MyApp.getInstance().getMyUid() +
                    "&eventid=" + taskId);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        // 不需要更新
//        unbindService(conn);
//        stopService(new Intent(this, MyService.class));
    }

    private DownloadFileInfo fileInfo;

    private void download(String url) {
        if (null == fileInfo)
            fileInfo = new DownloadFileInfo(url, "fileName", new ProgressCallback() {
                @Override
                public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {
//                    downloadProgress.setProgress(percent);
//                    tvResult.setText(percent+"%");
//                    LogUtil.d("附件", "下载进度：" + percent);
                    setNotification(percent);
                }

                @Override
                public void onResponseMain(String filePath, HttpInfo info) {
                    if (info.isSuccessful()) {
                        builder.setContentTitle("下载完成，文件路径：" + filePath);
                    } else {
                        builder.setContentTitle(info.getRetDetail() + "\n下载状态：" + fileInfo.getDownloadStatus());

                    }
                }
            });
        HttpInfo info = HttpInfo.Builder().addDownloadFile(fileInfo).build();
        OkHttpUtil.Builder().setReadTimeout(120).build(this).doDownloadFileAsync(info);
    }

    private NotificationManager manger;
    private NotificationCompat.Builder builder;

    private void setNotification(int progress) {
        if (builder == null) {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //取消右上角的时间显示
        builder.setShowWhen(false);
        if (progress == 100) {
            builder.setContentTitle("完成，路径：" + fileInfo.getSaveFileDir());
        } else {
            builder.setContentTitle("下载中..." + progress + "%");
        }
        builder.setProgress(100, progress, false);
        //builder.setContentInfo(progress+"%");
        builder.setOngoing(true);
        builder.setShowWhen(true);
//        Intent intent = new Intent(this, MyService.class);
//        intent.putExtra("command", 1);
        Notification notification = builder.build();
        manger.notify(0, notification);
    }
}
