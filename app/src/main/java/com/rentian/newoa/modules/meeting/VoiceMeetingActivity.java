package com.rentian.newoa.modules.meeting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.OkHttp3Utils;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.login.view.LoginActivity;
import com.rentian.newoa.modules.meeting.adapter.RoomChatAdapter;
import com.rentian.newoa.modules.meeting.adapter.SpeakerAdapter;
import com.rentian.newoa.modules.meeting.bean.Member;
import com.rentian.newoa.modules.meeting.bean.Message;
import com.rentian.newoa.modules.meeting.bean.Msg;
import com.rentian.newoa.modules.meeting.bean.RoomData;
import com.rentian.newoa.modules.meeting.bean.SendMsg;
import com.rentian.newoa.modules.meeting.view.InterPhoneBannerView;
import com.tencent.gcloud.voice.GCloudVoiceEngine;
import com.tencent.gcloud.voice.IGCloudVoiceNotify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 语音会议界面
 * com.yuntongxun.ecdemo.ui.meeting in ECDemo_Android
 * Created by Jorstin on 2015/7/17.
 */
public class VoiceMeetingActivity extends AppCompatActivity
        implements View.OnLayoutChangeListener, CIMEventListener {

    private static final String TAG = "ECSDK_Demo.VoiceMeetingActivity";

    public static final String EXTRA_MEETING = "com.yuntongxun.ecdemo.Meeting";
    public static final String EXTRA_MEETING_PASS = "com.yuntongxun.ecdemo.Meeting_Pass";
    public static final String EXTRA_CALL_IN = "com.yuntongxun.ecdemo.Meeting_Join";
    /**
     * 管理会议成员
     */
    public static final int REQUEST_CODE_KICK_MEMBER = 0x001;
    /**
     * 外呼电话邀请加入会议
     */
    public static final int REQUEST_CODE_INVITE_BY_PHONECALL = 0x002;
    /**
     * 会议成员显示控件
     */
    private GridView mGridView;
    /**
     * 会议成员显示控件适配器
     */
    private SpeakerAdapter speakerAdapter;
    /**
     * 语音会议加入状态通知
     */
    private InterPhoneBannerView mInterPhoneBannerView;
    private TextView mMikeToast, mMikeToast1;
    private ArrayList<Member> data;
    private ArrayList<Message> messgae = new ArrayList<>();
    private ArrayList<Member> onlineSpeaker = new ArrayList<>();
    /**
     * 是否是自己创建的会议房间
     */
    private boolean isSelfMeeting = false;
    /**
     * 会议房间是否已经被解散
     */
    private boolean isMeetingOver = false;
    private boolean isMeeting = false;
    /**
     * 是否是扬声器模式
     */
    private boolean mSpeakerOn = false;
    /***
     * 会议成员是否有自己
     */
    private boolean hasSelf = false;
    private boolean isMikeEnable;
    /**
     * 右上角下拉菜单
     */
    private int uid;
    private RoomData roomData;
    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0://发言
                    addMember();
                    break;

                case 1://查询发言列表
                    speakerAdapter.notifyDataSetChanged();
                    if (messgae.size() > 0) {
                        if (chatAdapter == null) {
                            chatAdapter = new RoomChatAdapter(VoiceMeetingActivity.this, messgae);
                            mListView.setAdapter(chatAdapter);
                            chatAdapter.notifyDataSetChanged();
                            mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
                        } else {
//                            chatAdapter = new RoomChatAdapter(VoiceMeetingActivity.this, messgae);
//                            mListView.setAdapter(chatAdapter);
                            chatAdapter.notifyDataSetChanged();
                            mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
                            Log.e("messge", messgae.size() + "");
                        }
                    }
                    break;

                case 2://禁言
                    delMember();
                    break;
                case 3:
                    engine.Poll();
                    break;
            }


        }
    };

    private void joinRoom() {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("roomid", MyApp.getMyRoomId());
        hashMap.put("uid", MyApp.getInstance().getMyUid());
        hashMap.put("name", MyApp.getInstance().myName);
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_ROOM_ADD).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("加入房间", info.getRetDetail());

                        } else {
                            Log.e("加入房间", info.getRetDetail());
                        }
                    }
                }
        );
    }


    private void addMember() {

        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("account", MyApp.getInstance().getMyUid());
        hashMap.put("roomid", MyApp.getInstance().getMyRoomId());



        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_SPEAKER_ADD).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("json", info.getRetDetail());
                            Type type = new TypeToken<Msg>() {
                            }.getType();
                            Msg msg = CommonUtil.gson.fromJson(
                                    info.getRetDetail(), type);
                            if (msg.msg.equals("1")) {
                                int rec = engine.OpenMic();
                                if (rec == 0) {
                                    isOnSpeak = true;
                                    iv_mic.setImageResource(R.drawable.aan2);
                                }
                                Log.e("rec", rec + "");

//                            ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
//                            try {
//
//                                if (setupManager != null) {
//                                    setupManager.setMute(isMikeEnable);
//                                    isMikeEnable = !setupManager.getMuteStatus();
//                                }
//                                if (isMikeEnable == true) {
                                iv_mic.setImageResource(R.drawable.aan2);
//                                } else {
//                                    iv_mic.setImageResource(R.drawable.aan1);
//                                }
//                                mMikeToast.setText(!isMikeEnable ? R.string.str_chatroom_mike_disenable : R.string.str_chatroom_mike_enable);
//                                mMikeToast1.setText(!isMikeEnable ? R.string.str_chatroom_mike_disenable1 : R.string.str_chatroom_mike_enable1);
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            }
                            }
                    }
                }
        );






    }

    @Override
    public void onConnectionSuccessed(boolean autoBind) {

        //连接服务端
        if (!autoBind) {
            CIMPushManager.bindAccount(this, MyApp.getInstance().getMyUid());
        }
    }

    @Override
    public void onConnectionClosed() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public int getEventDispatchOrder() {
        return 0;
    }

    private RoomChatAdapter chatAdapter;

    private void requestHistoryMessage() {
        Map<String, String> hashMap = new HashMap<String, String>();

        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_ON).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("历史消息", info.getRetDetail());
                            Type type = new TypeToken<RoomData>() {
                            }.getType();
                            roomData = CommonUtil.gson.fromJson(info.getRetDetail(), type);
//                            加载聊天数据
                            if (null != roomData.message && roomData.message.size() > 0) {
                                messgae.clear();
                                messgae.addAll(roomData.message);
                                Collections.reverse(messgae);
                                if (chatAdapter == null) {
                                    chatAdapter = new RoomChatAdapter(VoiceMeetingActivity.this, messgae);
                                    mListView.setAdapter(chatAdapter);
//                                    mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
                                    mListView.setSelection(mListView.getBottom());
                                } else {
                                    chatAdapter.notifyDataSetChanged();
                                    mListView.setSelection(mListView.getBottom());
//                                    mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
                                }
                            }

                        } else {
                            Log.e("json", info.getRetDetail());
                        }
                    }
                }
        );
    }

    private void upDateSpeakerList() {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("roomid", MyApp.getInstance().getMyRoomId());
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_LIST_SPEAKER).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("Fayan", info.getRetDetail());
                            Type type = new TypeToken<ArrayList<Member>>() {
                            }.getType();
                            ArrayList<Member> list =
                                    CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            onlineSpeaker.clear();
                            onlineSpeaker.addAll(list);
                            speakerAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("json", info.getRetDetail());
                        }
                    }
                }
        );
    }

    private void delMember() {

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("account", MyApp.getInstance().getMyUid());
        hashMap.put("roomid", MyApp.getInstance().getMyRoomId());
        new OkHttp3Utils().post(Const.RTOA.URL_MEETING_SPEAKER_DEL, hashMap,
                new OkHttp3Utils.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        Type type = new TypeToken<Msg>() {
                        }.getType();
                        Msg msg = CommonUtil.gson.fromJson(
                                data, type);
                        if (msg.msg.equals("1")) {

                            int rec = engine.CloseMic();
                            if (rec == 0) {
                                isOnSpeak = false;
                                iv_mic.setImageResource(R.drawable.aan1);
                            }
                            Log.e("rec2", rec + "");


                        }
                    }
                });

    }


    /**
     * 底部退出按钮
     */
    private ImageView bt_exit;
    private ImageView iv_mic, iv_speaker;

    //    @Override
//    protected int getLayoutId() {
//        return R.layout.meeting_voice;
//    }
    private boolean isOnSpeak;
    private GCloudVoiceEngine engine;

    private void initGvoice() {

        GCloudVoiceEngine.getInstance().init(MyApp.getInstance(), this);

        engine = GCloudVoiceEngine.getInstance();

        engine.SetAppInfo("1093905910", "38cd997eefc6d92e8cd35542ce2236d7", MyApp.getInstance().getMyUid());
        engine.Init();
        engine.SetMode(GCloudVoiceEngine.Mode.RealTime);
        Notify notify = new Notify();
        engine.SetNotify(notify);
        //timer to poll
        TimerTask task = new TimerTask() {
            public void run() {
                android.os.Message message = new android.os.Message();
                message.what = 3;
                mhandler.sendMessage(message);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 0, 500);

        int a = engine.JoinNationalRoom("rtsxy", 1, 10000);

    }

    class Notify implements IGCloudVoiceNotify {

        public final String tag = "GCloudVoiceNotify";

        @Override
        public void OnJoinRoom(int code, String roomName, int memberID) {
            Log.e(tag, "OnJoinRoom CallBack code=" + code + " roomname:" + roomName + " memberID:" + memberID);
            if (code == 1) {
//                bt.setText("点击开麦");
                engine.OpenSpeaker();
            } else {
                finish();
            }
        }

        @Override
        public void OnStatusUpdate(int status, String roomName, int memberID) {
            Log.e(tag, "OnStatusUpdate CallBack code=" + status + " roomname:" + roomName + " memberID:" + memberID);
        }

        @Override
        public void OnQuitRoom(int code, String roomName) {
            Log.e(tag, "OnQuitRoom CallBack code=" + code + " roomname:" + roomName);
        }

        @Override
        public void OnMemberVoice(int[] members, int count) {
            Log.e(tag, "OnMemberVoice CallBack " + "count:" + count);
            String str = "OnMemberVoice Callback ";
            for (int i = 0; i < count; ++i) {
                str += " memberid:" + members[i];
                str += " state:" + members[2 * i + 1];
            }
            Log.e(tag, str);
        }

        @Override
        public void OnUploadFile(int code, String filePath, String fileID) {
            Log.e(tag, "OnUploadFile CallBack code=" + code + " filePath:" + filePath + " fileID:" + fileID);
        }

        @Override
        public void OnDownloadFile(int code, String filePath, String fileID) {
            Log.e(tag, "OnDownloadFile CallBack code=" + code + " filePath:" + filePath + " fileID:" + fileID);
        }

        @Override
        public void OnPlayRecordedFile(int code, String filePath) {
            Log.e(tag, "OnPlayRecordedFile CallBack code=" + code + " filePath:" + filePath);
        }

        @Override
        public void OnApplyMessageKey(int code) {
            Log.e(tag, "OnApplyMessageKey CallBack code=" + code);
        }

        @Override
        public void OnSpeechToText(int code, String fileID, String result) {
            Log.e(tag, "OnSpeechToText CallBack code=" + code + " fileID:" + fileID + " result:" + result);
        }

        @Override
        public void OnRecording(char[] pAudioData, int nDataLength) {
            Log.e(tag, "OnRecording CallBack  nDataLength:" + nDataLength);
        }

        @Override
        public void OnStreamSpeechToText(int i, int i1, String s) {
            Log.e(tag, "OnStreamSpeechToText CallBack  result:" + s);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 19) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccentDown));
        }


        setContentView(R.layout.meeting_voice);
        CIMListenerManager.registerMessageListener(this);
        CIMPushManager.connect(this, Const.RTOA.CIM_SERVER_HOST, Const.RTOA.CIM_SERVER_PORT);
        CIMPushManager.bindAccount(this, MyApp.getInstance().getMyUid());

//        uid = Integer.parseInt(MyApp.getInstance().getMyUid());
        initView();
        getTip();
//        setTopBar(false);
        initGvoice();
        isSelfMeeting = false;

//        mhandler.sendEmptyMessage(1);
        requestHistoryMessage();
//        joinRoom();
        upDateSpeakerList();
//        if (mMeetingCallin) {
//            // 判断是否需要调用加入接口加入会议
//            mInterPhoneBannerView.setTips(R.string.top_tips_connecting_wait);
//            MeetingHelper.joinMeeting(MyApp.getInstance().getMyRoomId(), mMeetingPassword);
//
//            return;
//        }
        isMeeting = true;

    }

    public void onBackground(View view) {
        rl_et.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            rl_et.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
//            if (messgae.size() != 0) {
//                mListView.smoothScrollToPosition(chatAdapter.getCount()-1);
//            }
        }
    }

    public void onBack(View view) {
        //// TODO: 2018/1/5f
        finish();
//
    }

    public void onCheck(View view) {
        startActivity(new Intent(this, MeetingMemberActivity.class));
    }

    /**
     * 初始化界面资源
     */
    private RelativeLayout rl_et;
    private LinearLayout ll;
    private EditText ed_voice;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private ListView mListView;
    private TextView tv_tip;

    private void initView() {
        mListView = (ListView) findViewById(R.id.chatting_history_lv);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        activityRootView = findViewById(R.id.root);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        ed_voice = (EditText) findViewById(R.id.et_voice);
        ed_voice.setTextColor(Color.BLACK);
        rl_et = (RelativeLayout) findViewById(R.id.et_send);
        ll = (LinearLayout) findViewById(R.id.bottom);
        mInterPhoneBannerView = (InterPhoneBannerView) findViewById(R.id.notice_tips_ly);
//        mVoiceCenter = (VoiceMeetingCenter) findViewById(R.id.meeting_speak_ly);
//        mMeetingMic = (VoiceMeetingMicAnim) findViewById(R.id.bottom);
//        mMeetingMic.setOnMeetingMicEnableListener(this);
        bt_exit = (ImageView) findViewById(R.id.voice_btn_exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_et.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                ed_voice.requestFocus();
                InputMethodManager imm = (InputMethodManager) ed_voice.getContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

//                doTitleLeftAction();
            }
        });
        iv_mic = (ImageView) findViewById(R.id.iv_mic);
        iv_mic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!isOnSpeak) {
                    //TODO
                    mhandler.sendEmptyMessage(0);

                } else {
                    //TODO
                    mhandler.sendEmptyMessage(2);
                }
//                }
            }

        });
        iv_speaker = (ImageView) findViewById(R.id.speaker);
        iv_speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeSpeakerOnMode();
            }
        });
        mGridView = (GridView) findViewById(R.id.chatroom_member_list);
//        mMeetingMemberAdapter = new MeetingMemberAdapter(this);
//        mGridView.setAdapter(mMeetingMemberAdapter);
        speakerAdapter = new SpeakerAdapter(onlineSpeaker, this);
        mGridView.setAdapter(speakerAdapter);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private String sendContent;

    public void sendMessage(View v) {
        sendContent = ed_voice.getText().toString();
        if (sendContent.trim().length() != 0) {
            sendMsg(sendContent);
        }
    }

    private void sendMsg(String content) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", MyApp.getInstance().getMyUid());
        hashMap.put("extra", MyApp.getInstance().myName);
        hashMap.put("receiver", "0");
        hashMap.put("content", content);
        hashMap.put("action", "2");

        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_SEND_MESSAGE).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("发送消息", info.getRetDetail());
                            Type type = new TypeToken<SendMsg>() {
                            }.getType();

                            SendMsg msg = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            if (msg.msg.equals("1")) {
                                sendContent = "";
                                ed_voice.setText("");
                            }

                        } else {
                            Log.e("json", info.getRetDetail());
                        }
                    }
                }
        );

    }

    private void getTip() {

        OkHttpUtil.getDefault().doGetAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETTING_TIP).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("tip", info.getRetDetail());
                            Type type = new TypeToken<SendMsg>() {
                            }.getType();

                            SendMsg msg = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            tv_tip.setText(msg.msg);
                        } else {
                            Log.e("tip", info.getRetDetail());
                        }
                    }
                }
        );

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            //// TODO: 2018/1/5
////            doTitleLeftAction();
//            finish();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    private boolean once = true;
    private int count = 0;

    @Override
    protected void onResume() {
        super.onResume();
        joinRoom();
        mInterPhoneBannerView.setTips("发言人：");
        activityRootView.addOnLayoutChangeListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        changeAmplitude(false);
    }

    @Override
    protected void onStop() {
        super.onPause();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        if(screen){//如果亮屏
            //相关操作
//            exitRoom();
        }
//        exitRoom();
    }

    private boolean isNeedGetData = true;

//    @Override
//    public void onMeetingStart(String meetingNo) {
//        super.onMeetingStart(meetingNo);
//        isMeeting = true;
//        // 加入会议成功
//        hasSelf = false;
////        MeetingHelper.queryMeetingMembers(meetingNo);
//
//        changeAmplitude(true);
//
//    }
//
//    @Override
//    protected boolean isEnableSwipe() {
//        // TODO Auto-generated method stub
//        return false;
//    }

    private void changeAmplitude(boolean enbale) {
        if (!isMeeting && enbale) {
            return;
        }
//        if(mVoiceCenter != null) {
//            if(enbale) {
//                mVoiceCenter.startAmplitude();
//            } else {
//                mVoiceCenter.stopAmplitude();
//            }
//        }
//        if (mMeetingMic != null) {
//            if (enbale) {
//                mMeetingMic.startMicAmpl();
//            } else {
//                mMeetingMic.stopMicAmpl();
//            }
//        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSelfMeeting = false;
    }

    private boolean isSelfMobile = false;








    //收到消息
    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        if (message.getAction().equals(Const.MessageType.TYPE_999)) {
            //返回登录页面，停止接受消息
            CIMPushManager.stop(this);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else if (message.getAction().equals(Const.MessageType.TYPE_2)) {
            Message message1 = new Message();
            message1.name = message.getExtra();
            message1.content = message.getContent();
            messgae.add(message1);
            if (chatAdapter == null) {
                chatAdapter = new RoomChatAdapter(VoiceMeetingActivity.this, messgae);
                mListView.setAdapter(chatAdapter);
//                    chatAdapter.notifyDataSetChanged();
                mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
            } else {
                chatAdapter.notifyDataSetChanged();
                mListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
            }
//            MediaPlayer.create(this, R.raw.classic).start();
//            list.add(message);
//            adapter.notifyDataSetChanged();
//            chatListView.setSelection(chatListView.getTop());

        } else if (message.getAction().equals(Const.MessageType.TYPE_3)) {
            upDateSpeakerList();
        }

    }


    @Override
    public void onReplyReceived(ReplyBody replybody) {

    }

    @Override
    public void onSentSuccessed(SentBody body) {

    }

    @Override
    public void onNetworkChanged(NetworkInfo networkinfo) {

    }

    /**
     * 退出会议
     */
    private void exitRoom() {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("roomid", MyApp.getMyRoomId());
        hashMap.put("uid", MyApp.getInstance().getMyUid());
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_MEETING_ROOM_DEL).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("exitRoom", info.getRetDetail());
                        } else {
                        }
                    }
                }
        );
    }

    @Override
    public void finish() {
        super.finish();
        delMember();
        engine.CloseMic();
        engine.QuitRoom("rtsxy", 10000);
        exitRoom();
        CIMListenerManager.removeMessageListener(this);

    }


}
