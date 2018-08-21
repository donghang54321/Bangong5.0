package com.rentian.newoa.modules.apply.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.apply.adapter.ApplyListAdapter;
import com.rentian.newoa.modules.apply.bean.ApplyList;
import com.rentian.newoa.modules.apply.view.ApplyActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rentianjituan on 2016/5/3.
 */
public class ApplyListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView lv;
    private ArrayList<ApplyList> data = new ArrayList<>();
    private ApplyListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_apply_list,container,false);
        lv =  view.findViewById(R.id.lv_apply_list);
        lv.setOnItemClickListener(this);
        update();
        return view;



    }

    private void update() {
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_APPLY_LIST_TEST)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("Apply", info.getRetDetail());

                            Type type = new TypeToken<List<ApplyList>>() {
                            }.getType();


                            data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            adapter = new ApplyListAdapter(getActivity(), data);
                            lv.setAdapter(adapter);
                        } else {
                            Log.e("Apply", info.getRetDetail());
                        }
                    }
                }
        );

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.getItemId(position) != 0) {
                Intent intent = new Intent(getActivity(), ApplyActivity.class);
                intent.putExtra("id", adapter.getItemId(position));
                startActivity(intent);
            }
    }

}
