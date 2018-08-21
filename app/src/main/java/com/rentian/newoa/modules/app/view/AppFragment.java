package com.rentian.newoa.modules.app.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.app.adapter.ApplicationAdapter;
import com.rentian.newoa.modules.app.callback.AppItemTouchHelper;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppFragment newInstance(String param1, String param2) {
        AppFragment fragment = new AppFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_app, container, false);
        initView(v);
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", "" + mParam1);
        doHttpAsync(Const.RTOA.URL_LIST_APPLICATION, hashMap);
        return v;
    }

    private RecyclerView mRecyclerView;
    private ApplicationAdapter mAdapter;
    private ArrayList<DataList> applist=new ArrayList<>();
    private void initView(View v) {
        mRecyclerView = v.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new ApplicationAdapter(getContext(),applist));
        ItemTouchHelper.Callback callback = new AppItemTouchHelper(mAdapter);

        /**
         * 实例化ItemTouchHelper对象,然后添加到RecyclerView
         */
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

    }
    public void doHttpAsync(String url, Map<String, String> hashMap) {
//        animation_view_click.setVisibility(View.VISIBLE);
//        startAnima();
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        applist.clear();
                        TaskData appData=response.body();
                        applist.addAll(appData.data.app);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }

}
