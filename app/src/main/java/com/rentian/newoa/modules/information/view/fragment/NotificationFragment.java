package com.rentian.newoa.modules.information.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.information.asynctask.GetInformationTask;
import com.rentian.newoa.modules.information.view.NotificationDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "0";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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


    private ListView lv;
    private List<Map<String, Object>> list = new ArrayList<>();
    private SimpleAdapter adapter;
    private RefreshLayout myRefreshListView;
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private int pn = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (null != msg.obj) {
                    List<Map<String, Object>> list2=(List<Map<String, Object>>) msg.obj;
                    if (list2.size()<10){
                        myRefreshListView.setOnLoadListener(null);
                    }
                    list.addAll((List<Map<String, Object>>) msg.obj);
                    if (list.size()==0){
                        myRefreshListView.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    if (pn > 1)
                        myRefreshListView.setLoading(false);
                    else setViewGone(loadLayout);
                } else {
                    Toast.makeText(getActivity(), "服务器未响应", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        findViews(view);
        loadView.start();
        registerListeners();
        new GetInformationTask(handler.obtainMessage(0))
                .execute(Const.RTOA.URL_INFORMATION + MyApp.getInstance().getMyUid()
                        + "&pn=" + pn+ "&type=" + mParam1);

        return view;
    }


    private void findViews(View view) {
        myRefreshListView = view.findViewById(R.id.swipe_layout);
        loadView = view.findViewById(R.id.loading_view);
        loadLayout = view.findViewById(R.id.loading_layout);
        lv = view.findViewById(R.id.lv_ofe_notificatin);
        adapter = new SimpleAdapter(
                getActivity(), list,
                R.layout.item_office_notification_lv, new String[]{
                "notificatin_date",
                "notification_publisher",
                "notification_subject"}, new int[]{
                R.id.notification_date,
                R.id.notification_publisher,
                R.id.notification_subject}) {
            @Override
            public long getItemId(int position) {
                long id = (Integer) list.get(position).get("id");
                return id;
            }
        };

        lv.setAdapter(adapter);
    }

    private void registerListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(),
                        NotificationDetailsActivity.class);
                intent.putExtra("id", arg3);
                startActivity(intent);

            }
        });
        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                pn++;
                // 更新数据
                new GetInformationTask(handler.obtainMessage(0))
                        .execute(Const.RTOA.URL_INFORMATION + MyApp.getInstance().getMyUid() + "&pn=" + pn);

            }
        });
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                myRefreshListView.setRefreshing(false);
            }
        });
    }

    private void setViewGone(final View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 0f, 1f, 0f,//
                loadView.getWidth() / 2,//
                loadView.getHeight() / 2);
        scaleAnimation.setDuration(1000);
        view.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                loadView.stop();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
