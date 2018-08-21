package com.rentian.newoa.modules.examiation.view.iview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.DimEffect;
import com.mingle.sweetpick.SweetSheet;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.modules.examiation.view.ExaminationAcitity;
import com.rentian.newoa.modules.meeting.bean.Msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ControlView extends FrameLayout {

    private Context mContext;

    private SweetSheet sweetSheet;

    private EditText editText;

    private String taskId;

    private ExaminationAcitity root;

    public ControlView(Context context) {
        this(context, null);
    }

    public ControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.examination_control, this);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickView(v);
            }
        });
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    private void onClickView(View v) {
        sweetSheet.show();
    }

    public void setSweetSheet(RelativeLayout rootView, int ScreenHeight) {
        sweetSheet = new SweetSheet(rootView);
        //定义一个 CustomDelegate 的 Delegate ,并且设置它的出现动画.
        CustomDelegate customDelegate = new CustomDelegate(false,
                CustomDelegate.AnimationType.AlphaAnimation);
        //设置自定义视图.
        customDelegate.setCustomView(initializeSweetSheetView(ScreenHeight));
        sweetSheet.setDelegate(customDelegate);
        //根据设置不同Effect来设置背景效果:BlurEffect 模糊效果.DimEffect 变暗效果,NoneEffect 没有效果.
        sweetSheet.setBackgroundEffect(new DimEffect(0.8f));
    }

    private View initializeSweetSheetView(int ScreenHeight) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.examination_pop, null, false);
        editText = (EditText) view.findViewById(R.id.examination_pop_edit);
        view.findViewById(R.id.examination_pop_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnLeftOnClick(v);
            }
        });

        view.findViewById(R.id.examination_pop_agree).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnRightOnClick(v);
            }
        });
        return view;
    }

    private String sb;

    // 打回
    private void setBtnLeftOnClick(View view) {
        sb = editText.getText().toString();
        if (sb.length() == 0) {
            ToastUtill.showMessage("请输入批语");
        } else {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("notice", sb);
            hashMap.put("taskid", taskId);
            hashMap.put("uid", MyApp.getInstance().getMyUid());
            doHttpAsync("打回", Const.RTOA.URL_EXAMINATION_BACK, hashMap);
        }
    }

    // 同意
    private void setBtnRightOnClick(View view) {
        sb = editText.getText().toString();
        if (sb.length() == 0) {
            ToastUtill.showMessage("请输入批语");
        } else {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("notice", sb);
            hashMap.put("taskid", taskId);
            hashMap.put("uid", MyApp.getInstance().getMyUid());
            doHttpAsync("审批", Const.RTOA.URL_EXAMINATION_AGREE, hashMap);
        }
    }

    public boolean isShow() {
        return sweetSheet.isShow();
    }

    public void dismiss() {
        sweetSheet.dismiss();
    }

    public AsyncTask ControlTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            if ("".equals(sb)) {
                Result resultData = new Result();
                resultData.type = -1;
                resultData.result = "请输入批语";
                return resultData;
            }
            String url, baseJson = null;
            String sendContent = null;
            int type = -1;
            switch ((int) params[0]) {
                case 1:
                    type = 1;
                    url = Const.RTOA.URL_EXAMINATION_AGREE;
                    sendContent = "uid=" + MyApp.getInstance().getMyUid()
                            + "&taskid=" + taskId
                            + "&notice=" + sb;
//                    Log.e("ControlView_url", url + "!");
                    baseJson = HttpURLConnHelper.requestByPOST(
                            url, sendContent);
                    Log.e("ControlView_url", baseJson + "!");
                    Log.e("审批通过", url + sendContent);
                    break;
                case 2:
                    type = 2;
                    url = Const.RTOA.URL_EXAMINATION_BACK;

                    sendContent = "uid=" + MyApp.getInstance().getMyUid()
                            + "&notice=" + sb
                            + "&taskid=" + taskId;
                    baseJson = HttpURLConnHelper.requestByPOST(
                            url, sendContent);
                    break;
            }
            try {
                JSONObject js = new JSONObject(baseJson);
                Log.e("ControlView", js.getString("msg") + "!");
                Result resultData = new Result();
                resultData.type = type;
                resultData.result = js.getString("msg");
//                resultData.jf = js.getString("jf");
                resultData.jf = "0";
                return resultData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Result resultData = new Result();
            resultData.type = type;
            resultData.result = "-1";
            return resultData;
        }

        @Override
        protected void onPostExecute(Object o) {
            Result resultData = (Result) o;
            if (resultData.result.equals("1")) {
                if (resultData.type == 1) {
                    if (resultData.jf.equals("0")) {
                        CommonUtil.showToast(mContext, "完成审批");
                    } else {
                        CommonUtil.showToast(mContext, "完成审批：+" + resultData.jf + "分");
                    }
                    root.finish();
                } else if (resultData.type == 2) {
                    if (resultData.jf.equals("0")) {
                        CommonUtil.showToast(mContext, "打回成功");
                    } else {
                        CommonUtil.showToast(mContext, "打回成功：+" + resultData.jf + "分");
                    }
                    root.finish();
                } else if (resultData.type == -1) {
                    CommonUtil.showToast(mContext, resultData.result);
                }
            } else {
                CommonUtil.showToast(mContext, "失败，请重试");
            }
        }
    };


    class Result {
        int type;
        String result;
        String jf;
    }

    private void doHttpAsync(final String type1, String url, final Map<String, String> hashMap) {

        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(url).addParams(hashMap)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        Log.e("密码", info.getRetDetail());
                        if (info.isSuccessful()) {

                            Type type = new TypeToken<Msg>() {
                            }.getType();

                            Msg msg = CommonUtil.gson.fromJson(info.getRetDetail(), type);

                            if (msg.msg.equals("1")) {
                                ToastUtill.showMessage(type1 + "成功");
                                root.finish();
                            } else {
                                ToastUtill.showMessage(msg.msg);
                            }

                        }
                    }
                }

        );

    }

    public void setActivity(ExaminationAcitity activity) {
        this.root = activity;
    }
}
