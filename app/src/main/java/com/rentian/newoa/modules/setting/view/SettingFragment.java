package com.rentian.newoa.modules.setting.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.treasure.Treasure;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.bean.SimplePreferences;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.setting.adapter.GridAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment with a Google +1 button.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://www.baidu.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private GridAdapter mGridAdapter;
    private TextView tvName,tvTuichu;
    private CircleImageView touxiang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        initView(view);
        initData();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", "" + mParam1);
        doHttpAsync(Const.RTOA.URL_PERSON_SETTING, hashMap);
        return view;
    }

    private RecyclerView mRecyclerView;
    private TextView tvPhone;
    private void initView(View view) {

        mRecyclerView = view.findViewById(R.id.rv_gird);
        tvName = view.findViewById(R.id.tv_name);
        tvTuichu=view.findViewById(R.id.tv_tuichu);
        tvTuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimplePreferences preferences = Treasure.get(getContext()
                        , SimplePreferences.class);
                preferences.setIsLogin(false);
                getActivity().finish();
            }
        });
        touxiang = view.findViewById(R.id.iv_touxiang);
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(
//                getContext(), LinearLayoutManager.VERTICAL, 1, R.color.task_text2));


        RelativeLayout relativeLayout1 = view.findViewById(R.id.item1);
        TextView textView1 = (TextView) relativeLayout1.getChildAt(0);
        textView1.setText("账号信息");

        RelativeLayout relativeLayout2 = view.findViewById(R.id.item2);
        TextView textView2 = (TextView) relativeLayout2.getChildAt(0);
        textView2.setText("设置");

        RelativeLayout relativeLayout3 = view.findViewById(R.id.item3);
        TextView textView3 = (TextView) relativeLayout3.getChildAt(0);
        textView3.setText("联系客服");
        tvPhone= (TextView) relativeLayout3.getChildAt(2);

        RelativeLayout relativeLayout4 = view.findViewById(R.id.item4);
        TextView textView4 = (TextView) relativeLayout4.getChildAt(0);
        textView4.setText("在线反馈");

        RelativeLayout relativeLayout5 = view.findViewById(R.id.item5);
        TextView textView5 = (TextView) relativeLayout5.getChildAt(0);
        textView5.setText("推荐给朋友");

        RelativeLayout relativeLayout6 = view.findViewById(R.id.item6);
        TextView textView6 = (TextView) relativeLayout6.getChildAt(0);
        textView6.setText("检查更新");
    }

    public void initData() {
        tvName.setText(MyApp.getInstance().myName);
        Glide.with(this)
                .load(Const.RTOA.URL_BASE + mParam2)
                .into(touxiang);
    }


    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    private ArrayList<DataList> list = new ArrayList<>();

    public void doHttpAsync(String url, Map<String, String> hashMap) {
//        animation_view_click.setVisibility(View.VISIBLE);
//        startAnima();
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            list.clear();
                            TaskData taskData = response.body();
                            tvPhone.setText(taskData.data.phone);
                            list.addAll(taskData.data.icon);
                            if (list.size()>0) {
                                if (mGridAdapter == null) {
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), list.size()));
                                    mGridAdapter = new GridAdapter(getContext(), list, 1);
                                    mRecyclerView.setAdapter(mGridAdapter);
                                } else {
                                    mGridAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

    }

}
