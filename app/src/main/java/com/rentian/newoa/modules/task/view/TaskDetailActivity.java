package com.rentian.newoa.modules.task.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.RecycleViewDivider;
import com.rentian.newoa.modules.contactlist.view.SearchActivity;
import com.rentian.newoa.modules.login.view.LoginActivity;
import com.rentian.newoa.modules.meeting.bean.Message;
import com.rentian.newoa.modules.task.adapter.TaskAdapter;
import com.rentian.newoa.modules.task.bean.TaskDetail;
import com.rentian.newoa.modules.task.iview.SetZhuyemian;
import com.rentian.newoa.modules.timesecletor.MeiZuActivity;
import com.rentian.newoa.modules.todolist.adapter.MyAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.showzeng.demo.Fragment.CommentDialogFragment;
import cn.showzeng.demo.Interface.DialogFragmentDataCallback;
import de.hdodenhof.circleimageview.CircleImageView;

public class TaskDetailActivity extends Base2Activity
        implements DialogFragmentDataCallback, SetZhuyemian
        , View.OnClickListener {
    private RecyclerView rvList;
    private TaskAdapter myAdapter;
    private ArrayList<TaskDetail> detaillist = new ArrayList<>();
    private CircleImageView ivSender;
    private ImageView ivRank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_task);

        setSupportActionBar(toolbar);
        toolbar.setTitle("事项分类");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        setStatusBarDarkMode();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
//        rvList.addItemDecoration(new RecycleViewDivider(
//                getActivity(), LinearLayoutManager.VERTICAL, 5, R.color.colorAccent));
        //先实例化Callback
        myAdapter = new TaskAdapter(this, detaillist, this);
        rvList.setAdapter(myAdapter);
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", "" + getIntent().getIntExtra("id", 0));
        doHttpAsync(Const.RTOA.URL_TODO_TASKDETAIL, hashMap);
    }


    private TextView tvTime, tvNum1, tvNum2, tvNum3, tvNum4;
    private ImageView ivDZ, ivDT;
    private CheckBox checkBox;

    private void initView() {
        ivSender = findViewById(R.id.iv_sender);

        if (getIntent().getStringExtra("img") == null) {
            ivSender.setImageResource(R.drawable.default_avatar);
        } else {
            Glide.with(this)
                    .load(Const.RTOA.URL_BASE + getIntent().getStringExtra("img"))
                    .into(ivSender);
        }

        ivRank = findViewById(R.id.iv_jibie);
        ivRank.setOnClickListener(this);
        checkBox = findViewById(R.id.checkbox);
        rvList = findViewById(R.id.rv_list);
        tvTime = findViewById(R.id.tv_time);
        tvTime.setOnClickListener(this);
        tvNum1 = findViewById(R.id.main_tab_tv1);
        tvNum2 = findViewById(R.id.main_tab_tv2);
        tvNum3 = findViewById(R.id.main_tab_tv3);
        tvNum4 = findViewById(R.id.main_tab_tv4);
        ivDZ = findViewById(R.id.main_tab_iv1);
        ivDT = findViewById(R.id.main_tab_iv4);
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (mShouldScroll) {
//                    mShouldScroll = false;
//                    smoothMoveToPosition(rvList, mToPosition);
//                }
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
            }
        });

    }

    /**
     * 单选
     */
    private RadioButton btRank1, btRank2, btRank3, btRank4;

    @SuppressLint("InflateParams")
    private void dialogChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate
                (R.layout.dialog_set_rank, null, false);
        btRank1 = view.findViewById(R.id.male_rb1);
        btRank2 = view.findViewById(R.id.male_rb2);
        btRank3 = view.findViewById(R.id.male_rb3);
        btRank4 = view.findViewById(R.id.male_rb4);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("优先级");
        dialog.setView(view);
        dialog.show();
        setRbtOnCheckedChangeListener(btRank1, 1, dialog, R.color.rank1, R.drawable.jibie1);
        setRbtOnCheckedChangeListener(btRank2, 2, dialog, R.color.rank2, R.drawable.jibie2);
        setRbtOnCheckedChangeListener(btRank3, 3, dialog, R.color.rank3, R.drawable.jibie3);
        setRbtOnCheckedChangeListener(btRank4, 4, dialog, R.color.rank4, R.drawable.jibie);


    }

    private void setRbtOnCheckedChangeListener(RadioButton bt, final int rank
            , final AlertDialog dialog, final int color, final int drawable) {
        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("rank", "" + rank);
                    hashMap.put("id", getIntent().getIntExtra("id", 0) + "");
                    postSetrank(Const.RTOA.URL_SET_RANK, hashMap);
                    dialog.dismiss();
                    ivRank.setImageResource(drawable);
                    ivRank.setColorFilter(ContextCompat.getColor(getApplicationContext(), color));
                }

            }
        });
    }

    private void postSetrank(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        //// TODO: 2018/4/26 拖拽完成后

                    }
                });
    }


    private TaskData taskData;

    private void doHttpAsync(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        Log.e("taskdetail", response.body().code + "");
                        if (response.body().code == 0) {
                            taskData = response.body();
                            detaillist.clear();

                            ivRank.setColorFilter(Color.parseColor(
                                    taskData.data.detail.color));

                            if (taskData.data.detail.complete == 1) {
                                checkBox.setChecked(true);
                            } else {
                                checkBox.setChecked(false);
                            }
                            TaskDetail rwcontent = new TaskDetail();
                            TaskDetail zhurenwu = new TaskDetail();
                            zhurenwu.layoutType = 8;
                            zhurenwu.pid = response.body().data.detail.pid;
                            zhurenwu.title = response.body().data.detail.title;
                            detaillist.add(zhurenwu);
                            myAdapter.setZirenwuNum(response.body().data.detail.sub.size());
                            if (response.body().data.detail.parent == 1) {
                                myAdapter.setIsZirenwu(1);
                                menu.getItem(1).setTitle("文本");
                                for (TaskDetail x : response.body().data.detail.sub) {
                                    x.layoutType = 7;
                                }
                            } else {
                                menu.getItem(1).setTitle("列表");
                                String content = "";
                                for (TaskDetail x : response.body().data.detail.sub) {
                                    content += x.neirong + "\n";
                                }
                                rwcontent.layoutType = 1;
                                rwcontent.neirong = content;
//                                detaillist.add(rwcontent);

                            }
                            int i = 0;
                            for (TaskDetail x : response.body().data.detail.file) {
                                if (i == 0 && myAdapter.getIsZirenwu() == 1) {
                                    x.layoutType = 9;
                                } else {
                                    x.layoutType = 2;
                                }
                                i++;
                            }
                            response.body().data.detail.layoutType = 3;
                            if (response.body().data.detail.comment != null) {
                                for (TaskDetail x : response.body().data.detail.comment) {
                                    x.layoutType = 4;
                                }
                            } else {
                                response.body().data.detail.comment = new ArrayList<>();
                            }
                            if (response.body().data.detail.trend != null) {
                                for (TaskDetail x : response.body().data.detail.trend) {

                                    x.layoutType = 5;
                                }
                            } else {
                                response.body().data.detail.trend = new ArrayList<>();

                            }
                            if (response.body().data.detail.parent == 1) {
                                detaillist.addAll(response.body().data.detail.sub);
                            } else {
                                detaillist.add(rwcontent);
                            }
                            detaillist.addAll(response.body().data.detail.file);
                            detaillist.add(getBiaoti(response
                                    , response.body().data.detail.amount_like, "点赞"));
                            detaillist.add(response.body().data.detail);
                            detaillist.add(getBiaoti(response
                                    , response.body().data.detail.comment.size(), "评论"));
                            detaillist.addAll(response.body().data.detail.comment);

                            detaillist.add(getBiaoti(response
                                    , response.body().data.detail.trend.size(), "动态"));
                            detaillist.addAll(response.body().data.detail.trend);
                            tvTime.setText(response.body().data.detail.week);
                            if (response.body().data.detail.beyond == 0) {
                                tvTime.setTextColor(getResources().
                                        getColor(R.color.task_text1));
                            } else {
                                tvTime.setTextColor(getResources().
                                        getColor(R.color.head_image_2));
                            }
                            tvNum1.setText(response.body().data.detail.amount_like + "");
                            tvNum2.setText(response.body().data.detail.comment.size() + "");
                            tvNum3.setText(response.body().data.detail.file.size() + "");
                            tvNum4.setText(response.body().data.detail.amount_focus + "");
                            if (response.body().data.detail.isdz == 1) {
                                ivDZ.setImageResource(R.drawable.dianzan1);
                            } else {
                                ivDZ.setImageResource(R.drawable.dianzan);
                            }
                            if (response.body().data.detail.isgz == 1) {
                                ivDT.setImageResource(R.drawable.dongtai1);
                            } else {
                                ivDT.setImageResource(R.drawable.dongtai);
                            }
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }
                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    Map<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("id", detaillist.get(0).pid + "");
                                    hashMap.put("complete", "1");
                                    doWancheng(Const.RTOA.URL_ZHUTASK_SUBMIT, hashMap);
                                } else {
                                    Map<String, String> hashMap = new HashMap<String, String>();
                                    hashMap.put("id", detaillist.get(0).pid + "");
                                    hashMap.put("complete", "0");
                                    doWancheng(Const.RTOA.URL_ZHUTASK_SUBMIT, hashMap);
                                }
                            }
                        });
                        myAdapter.notifyDataSetChanged();
                        rvList.setItemViewCacheSize(detaillist.size());
                    }
                });

    }

    private void setZiRenwu(String url, final Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            myAdapter.removejianting();
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", "" + getIntent().getIntExtra("id", 0));
                            doHttpAsync(Const.RTOA.URL_TODO_TASKDETAIL, hashMap);
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                        myAdapter.notifyItemRangeChanged(0, detaillist.size());
                        rvList.setItemViewCacheSize(detaillist.size());
                    }
                });

    }

    private TaskDetail getBiaoti(Response<TaskData> response
            , int num, String name) {
        TaskDetail title = new TaskDetail();
        title.layoutType = 6;
        title.name = name;
        title.titleNum = "" + num;
        return title;
    }

    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar跟menu
        getMenuInflater().inflate(R.menu.menu_task, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_list:
                if (item.getTitle().equals("文本")) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", getIntent().getIntExtra("id", 0) + "");
                    hashMap.put("sub", "0");
                    setZiRenwu(Const.RTOA.URL_SET_ZIRENWU, hashMap);
                    item.setTitle("列表");
                    myAdapter.setIsZirenwu(0);
                } else {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", getIntent().getIntExtra("id", 0) + "");
                    hashMap.put("sub", "1");
                    setZiRenwu(Const.RTOA.URL_SET_ZIRENWU, hashMap);
                    item.setTitle("文本");
//                    myAdapter=new TaskAdapter(this, detaillist);
                    myAdapter.setIsZirenwu(1);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTabClick1(View v) {
        int position = myAdapter.getDz();
        if (position != -1) {
            smoothMoveToPosition(rvList, position);
        } else {
            smoothMoveToPosition(rvList, position + 1);
        }
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", detaillist.get(0).pid + "");
        sendPinglun(Const.RTOA.URL_TASK_DIANZAN, hashMap);
//        rvList.smoothScrollToPosition(myAdapter.getDz());
    }

    public void onTabClick2(View v) {
        int position = myAdapter.getPl();
        if (position != -1) {
            smoothMoveToPosition(rvList, position);
        } else {
            smoothMoveToPosition(rvList, position + 1);
        }
        CommentDialogFragment commentDialogFragment = new CommentDialogFragment();
        commentDialogFragment.show(getFragmentManager(), "CommentDialogFragment");
    }

    public void onTabClick3(View v) {
        int position = myAdapter.getFj();
        if (position != -1) {
            smoothMoveToPosition(rvList, position);
        } else {
            smoothMoveToPosition(rvList, position + 1);
        }
    }

    public void onTabClick4(View v) {
        int position = myAdapter.getDongtai();
        if (position != -1) {
            smoothMoveToPosition(rvList, position);
        } else {
            smoothMoveToPosition(rvList, position + 1);
        }

        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", detaillist.get(0).pid + "");
        sendPinglun(Const.RTOA.URL_TASK_SHOUCANG, hashMap);
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Override
    public String getCommentText() {
        return "";
    }

    @Override
    public void setCommentText(String commentTextTemp) {

    }

    @Override
    public void sendPinglun(String commentTextTemp) {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("content", commentTextTemp);
        hashMap.put("id", detaillist.get(0).pid + "");
        hashMap.put("replyid", "0");
        sendPinglun(Const.RTOA.URL_TASK_PINGLUN, hashMap);
    }

    @Override
    public void setRank(int rank) {

    }

    private void sendPinglun(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", "" + getIntent().getIntExtra("id", 0));
                            doHttpAsync(Const.RTOA.URL_TODO_TASKDETAIL, hashMap);
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    private void doWancheng(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            taskData.data.detail.complete = 1;

                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    private void doTimeSelector(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            tvTime.setText(response.body().data.week);
                            if (response.body().data.beyond == 0) {
                                tvTime.setTextColor(getResources().
                                        getColor(R.color.task_text1));
                            } else {
                                tvTime.setTextColor(getResources().
                                        getColor(R.color.head_image_2));
                            }

                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    @Override
    public void setWancheng(int i) {
        Log.e("setWancheng", "1");
        if (taskData.data.detail.complete == 1) {
            return;
        }
        if (i == 1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivRank) {
            dialogChoice();
        } else if (v == tvTime) {
            startActivityForResult(new Intent(this,
                    MeiZuActivity.class), REQUESTCODE);
        }
    }

    private final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 3) {
            if (requestCode == REQUESTCODE) {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", detaillist.get(0).pid + "");
                hashMap.put("time", data.getStringExtra("date"));
                doTimeSelector(Const.RTOA.URL_TIME_SELECTOR, hashMap);
            }
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        if (message.getAction().equals(Const.MessageType.TYPE_999)) {
            Log.e("长连接：", "下线" + MyApp.getInstance().getMyUid());
            //返回登录页面，停止接受消息
            CIMPushManager.stop(this);
            this.finish();
        } else if (message.getAction().equals(Const.MessageType.TYPE_6)) {

            if (("" + getIntent().getIntExtra("id", 0)).equals(message.getContent())) {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", "" + getIntent().getIntExtra("id", 0));
                doHttpAsync(Const.RTOA.URL_TODO_TASKDETAIL, hashMap);
            }


        } else if (message.getAction().equals(Const.MessageType.TYPE_3)) {
        }

    }
}
