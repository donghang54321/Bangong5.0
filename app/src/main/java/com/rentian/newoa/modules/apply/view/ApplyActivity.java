package com.rentian.newoa.modules.apply.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.utils.UploadUtil;
import com.rentian.newoa.modules.apply.bean.ApplyForm;
import com.rentian.newoa.modules.apply.bean.ApplyView;
import com.rentian.newoa.modules.apply.bean.TypeDetails;
import com.rentian.newoa.modules.apply.view.iview.ApplyChecks;
import com.rentian.newoa.modules.apply.view.iview.ApplyEnclosure;
import com.rentian.newoa.modules.apply.view.iview.ApplyMultiText;
import com.rentian.newoa.modules.apply.view.iview.ApplyRadios;
import com.rentian.newoa.modules.apply.view.iview.ApplySingleText;
import com.rentian.newoa.modules.apply.view.iview.ApplySpinner;
import com.rentian.newoa.modules.apply.view.iview.ApplyTimePicker;
import com.rentian.newoa.modules.apply.view.iview.onResultFileData;
import com.rentian.newoa.modules.apply.view.iview.onStartFilePicker;
import com.rentian.newoa.modules.meeting.bean.Msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;


public class ApplyActivity extends AppCompatActivity implements onStartFilePicker {

    public static class ViewType {
        public static String singleText = "text";
        public static String multiText = "textarea";
        public static String timePicker = "date";
        public static String spinners = "dropdown";
        public static String radios = "radio";
        public static String checks = "check";
        public static String enclosure = "file";
        public static String timeLine = "timeLine";
        public static String control = "control";
    }

    private TextView tv_title;
    private LinearLayout filler;
    private ArrayList<ApplyView> views;

    private ApplyForm data = new ApplyForm();

    private onResultFileData result;
    private ApplyEnclosure enclosure;

    private long id;
    int fid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_apply);
        id = getIntent().getLongExtra("id", 0);

        filler = (LinearLayout) findViewById(R.id.apply_filler);
        tv_title = (TextView) findViewById(R.id.title);
        views = new ArrayList<>();

        postAsync();
    }

    private void addSingleText(String title, String hint) {
        ApplySingleText singleText = new ApplySingleText(this);
        singleText.setTitle(title);
        singleText.setContentHint(hint);
        ApplyView temp = new ApplyView(ViewType.singleText, singleText);
        views.add(temp);
    }

    private void addMultiText(String title, String hint) {
        ApplyMultiText multiText = new ApplyMultiText(this);
        multiText.setTitle(title);
        multiText.setContentHint(hint);
        ApplyView temp = new ApplyView(ViewType.multiText, multiText);
        views.add(temp);
    }

    private void addTimePicker(String title, String hint) {
        ApplyTimePicker timePicker = new ApplyTimePicker(this);
        timePicker.setTitle(title);
        timePicker.setContentHint(hint);
        ApplyView temp = new ApplyView(ViewType.timePicker, timePicker);
        views.add(temp);
    }

    private void addSpinner(String title, ArrayList<String> datas) {
        ApplySpinner spinner = new ApplySpinner(this);
        spinner.setTitle(title);
        spinner.setSpinnerDatas(datas);
        ApplyView temp = new ApplyView(ViewType.spinners, spinner);
        views.add(temp);
    }

    private void addRadios(String title, ArrayList<String> datas) {
        ApplyRadios radios = new ApplyRadios(this);
        radios.setTitle(title);
        radios.setRadioGroup(datas);
        ApplyView temp = new ApplyView(ViewType.radios, radios);
        views.add(temp);
    }

    private void addChecks(String title, ArrayList<String> datas) {
        ApplyChecks checks = new ApplyChecks(this);
        checks.setTitle(title);
        checks.setContentData(datas);
        ApplyView temp = new ApplyView(ViewType.checks, checks);
        views.add(temp);
    }

    private void addEnclosure() {
        enclosure = new ApplyEnclosure(this);
        result = enclosure;
        ApplyView temp = new ApplyView(ViewType.enclosure, enclosure);
        views.add(temp);
    }

    @Override
    public void StartFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    String imgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        File file = null;
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
            int actual_image_column_index;
            String img_path = null;
            try {
                actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                img_path = actualimagecursor.getString(actual_image_column_index);
                imgPath = img_path;
            } catch (NullPointerException e) {

            }

            if (img_path == null || "".equals(img_path)) {
                try {
                    file = new File(uri.getPath());
                    returnFileData(file);
                } catch (IOException | JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    file = new File(img_path);
                    returnFileData(file);
                } catch (IOException | JSONException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        result.resultFileData(file);
        enclosure.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void returnFileData(final File file) throws IOException, JSONException, URISyntaxException {
        // Get the Uri of the selected file
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = UploadUtil.uploadFile(file, Const.RTOA.URL_SUBMIT_FILE +
                        "?uid=" + MyApp.getInstance().getMyUid() + "&pid=-1");
                try {
                    JSONObject json = new JSONObject(request);
                    fid = json.getJSONObject("m").getJSONObject("file").getInt("id");
                    Log.e("idid", fid + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        UploadUtil.showDialog(fid + "", ApplyActivity.this);
                        UploadUtil.showDialog(fid + "", ApplyActivity.this);
                    }
                });
            }
        }).start();
    }

    private void postAsync() {
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_APPLY_FORM_TEST)
                        .addParam("uid", MyApp.getInstance().getMyUid())
                        .addParam("id", id + "")
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("ApplyActivity", info.getRetDetail());

                            Type type = new TypeToken<ApplyForm>() {
                            }.getType();

                            data = CommonUtil.gson.fromJson(info.getRetDetail(), type);

                            tv_title.setText(data.name);
                            ArrayList<TypeDetails> list = data.list;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).field_type.equals(ViewType.singleText)) { // 单行文本
                                    addSingleText(list.get(i).label, list.get(i).field_options.description);
                                } else if (list.get(i).field_type.equals(ViewType.multiText)) { // 多行文本
                                    addMultiText(list.get(i).label, list.get(i).field_options.description);
                                } else if (list.get(i).field_type.equals(ViewType.timePicker)) { // 时间选择器
                                    addTimePicker(list.get(i).label, list.get(i).field_options.description);
                                } else if (list.get(i).field_type.equals(ViewType.spinners)) { // 下拉选择框
                                    ArrayList<String> temp = new ArrayList<>();
                                    for (int j = 0; j < list.get(i).field_options.options.size(); j++) {
                                        temp.add(list.get(i).field_options.options.get(j).label);
                                    }
                                    addSpinner(list.get(i).label, temp);
                                    Log.e("temp", temp.size() + "");
                                } else if (list.get(i).field_type.equals(ViewType.radios)) { // 单项选择
                                    ArrayList<String> temp = new ArrayList<>();
                                    for (int j = 0; j < list.get(i).field_options.options.size(); j++) {
                                        temp.add(list.get(i).field_options.options.get(j).label);
                                    }
                                    addRadios(list.get(i).label, temp);
                                } else if (list.get(i).field_type.equals(ViewType.checks)) { // 多项选择
                                    ArrayList<String> temp = new ArrayList<>();
                                    for (int j = 0; j < list.get(i).field_options.options.size(); j++) {
                                        temp.add(list.get(i).field_options.options.get(j).label);
                                    }
                                    addChecks(list.get(i).label, temp);

                                } else if (list.get(i).field_type.equals(ViewType.enclosure)) { // 附件
                                    addEnclosure();
                                }
                            }
                            for (int i = 0; i < views.size(); i++) {
                                filler.addView(views.get(i).getView());
                            }
                        } else {
                            Log.e("ApplyActivity", info.getRetDetail());
                        }
                    }
                }
        );

    }

    public void onBack(View v) {
        finish();
    }

    public void submit(View v) {
        HashMap<String, String> params = new HashMap<>();
        params.put("uid",MyApp.getInstance().getMyUid());
        params.put("businessId",id+"");

        for (int i = 0; i < data.list.size(); i++) {
//                    sendContent+="&com"+data.list.get(i).comid+"="+views.get(i).getView().getst;
            if (views.get(i).getType().equals(ViewType.singleText)) {// 单行文本
                ApplySingleText view = (ApplySingleText) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key, view.getContentText());
            } else if (views.get(i).getType().equals(ViewType.multiText)) { // 多行文本
                ApplyMultiText view = (ApplyMultiText) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key, view.getContentText());
            } else if (views.get(i).getType().equals(ViewType.timePicker)) { // 时间选择器
                ApplyTimePicker view = (ApplyTimePicker) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key,view.getContentText());
            } else if (views.get(i).getType().equals(ViewType.spinners)) { // 下拉选择框
                ApplySpinner view = (ApplySpinner) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key,view.getSpinnerSelected());
            } else if (views.get(i).getType().equals(ViewType.radios)) { // 单项选择
                ApplyRadios view = (ApplyRadios) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key,view.getCheckedRadioText());
            } else if (views.get(i).getType().equals(ViewType.checks)) { // 多项选择
                ApplyMultiText view = (ApplyMultiText) views.get(i).getView();
                String key = "com" + data.list.get(i).comid;
                params.put(key,view.getContentText());
            } else if (views.get(i).getType().equals(ViewType.enclosure)) { // 附件
                String key = "com" + data.list.get(i).comid;
                params.put(key,fid+"");
            }
        }

        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_SUBMIT_APPLY_FORM)
                        .addParams(params)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("Apply", info.getRetDetail());
                            Msg resMsg = CommonUtil.gson.
                                    fromJson(info.getRetDetail(), Msg.class);
                            if ("1".equals(resMsg.msg)) {
                                ToastUtill.showMessage("申请成功", Toast.LENGTH_LONG);
                                finish();
                            } else {
                                ToastUtill.showMessage("申请失败", Toast.LENGTH_LONG);
                            }

                        } else {
                            Log.e("Apply", info.getRetDetail());
                        }
                    }
                }
        );

    }


}
