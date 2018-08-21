package com.rentian.newoa.modules.apply.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.modules.examiation.adapter.ExamiationAdapter;
import com.rentian.newoa.modules.examiation.bean.Examiation;
import com.rentian.newoa.modules.examiation.view.ExaminationAcitity;


import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/5/3.
 */
public class ExaminationListFragment extends Fragment {
    private ListView lv;
    private ArrayList<Examiation> data;
    private ExamiationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_examination_list,container,false);
        lv = (ListView)view.findViewById(R.id.lv_examination);
        initListener();
        return view;
    }
    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ExaminationAcitity.class);
                intent.putExtra("taskId", data.get(position).taskid);
                startActivity(intent);
            }
        });
    }

    private void update() {
        UpdateTextTask updateTextTask = new UpdateTextTask(getActivity());
        updateTextTask.execute();
    }

    class UpdateTextTask extends AsyncTask<Void, Integer, Integer> {

        private Context context;

        UpdateTextTask(Context context) {
            this.context = context;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            String url = Const.RTOA.URL_EXAMINATION_LIST +
                    "?uid=" + MyApp.getInstance().getMyUid();
            String baseJson = HttpURLConnHelper.requestByGET(
                    url);
            Type type = new TypeToken<ArrayList<Examiation>>() {
            }.getType();


            data = CommonUtil.gson.fromJson(baseJson, type);

            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
            adapter = new ExamiationAdapter(getActivity(), data);
            lv.setAdapter(adapter);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkAvailable(getActivity()))
            update();
        else ToastUtill.showMessage("没有网络");
    }
}
