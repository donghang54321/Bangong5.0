package com.rentian.newoa.modules.todolist.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.view.RecycleViewDivider;
import com.rentian.newoa.modules.task.iview.SetZhuyemian;
import com.rentian.newoa.modules.todolist.SimpleItemTouchHelperCallback;
import com.rentian.newoa.modules.todolist.adapter.MyAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.bean.TestData;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements
        SetZhuyemian {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    public String cid;
    private SetActionBarListener dataCallback;
    private OnFragmentInteractionListener mListener;
    private LottieAnimationView animation_view_click;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
            cid = getArguments().getString(ARG_PARAM2);
        }
        if ("1".equals(mParam1)) {
            dataCallback = (SetActionBarListener) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        initAnimation();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", cid);
        doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);
        return view;
    }

    /*
            * 开始动画
            */
    public void startAnima() {

        boolean inPlaying = animation_view_click.isAnimating();
        if (!inPlaying) {
            animation_view_click.setProgress(0f);
            animation_view_click.playAnimation();
        }
    }

    /*
    * 停止动画
    */
    private void stopAnima() {
        boolean inPlaying = animation_view_click.isAnimating();
        if (inPlaying) {
            animation_view_click.cancelAnimation();
        }
    }

    private ArrayList<DataList> todolist = new ArrayList<>();

    public void doHttpAsync(String url, Map<String, String> hashMap) {
        animation_view_click.setVisibility(View.VISIBLE);
        startAnima();
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        todolist.clear();
                        myAdapter.notifyDataSetChanged();
                        for (int i = 0; i < response.body().data.todo.size(); i++) {
                            DataList title = response.body().data.todo.get(i);
                            title.titleType = 0;
                            title.index = 0;
                            todolist.add(title);
                            for (int j = 0; j < response.body().data.todo.get(i).list.size(); j++) {
                                DataList shuju = response.body().data.todo.get(i).list.get(j);
                                if (shuju.wc == 0) {
                                    shuju.titleType = 1;
                                } else if (shuju.wc == 1) {
                                    shuju.titleType = 2;
                                }
                                todolist.add(shuju);
                            }

                        }
                        rvList.setItemViewCacheSize(todolist.size());
                        if (!animation_view_click.isAnimating()) {
                            myAdapter.notifyItemRangeChanged(0, todolist.size());
                        }
                        animation_view_click.setTag("1");
                    }
                });

    }

    private RecyclerView rvList;
    private MyAdapter myAdapter;
    private ArrayList<TestData> mData = new ArrayList<>();
    private SHSwipeRefreshLayout swipeRefreshLayout;

    private void initView(View view) {
        animation_view_click = view.findViewById(R.id.animation_view);
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        rvList = view.findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, 2, R.color.colorAccent));
        //先实例化Callback
        myAdapter = new MyAdapter(getActivity(), todolist, this);
        rvList.setAdapter(myAdapter);
        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(myAdapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(rvList);
        callback.setTouchHelper(touchHelper);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!cid.equals("-1")) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("cid", cid);
                    doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);
                }
                swipeRefreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                    }
                }, 0);
            }

            @Override
            public void onLoading() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                    }
                }, 0);
            }

            /**
             * 监听下拉刷新过程中的状态改变
             * @param percent 当前下拉距离的百分比（0-1）
             * @param state 分三种状态{NOT_OVER_TRIGGER_POINT：还未到触发下拉刷新的距离；OVER_TRIGGER_POINT：已经到触发下拉刷新的距离；START：正在下拉刷新}
             */
            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("下拉刷新");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("松开刷新");
                        break;
                    case SHSwipeRefreshLayout.START:
                        swipeRefreshLayout.setLoaderViewText("正在刷新");
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("上拉加载");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("松开加载");
                        break;
                    case SHSwipeRefreshLayout.START:
                        swipeRefreshLayout.setLoaderViewText("正在加载...");
                        break;
                }
            }
        });
    }

    private void initAnimation() {
        LottieComposition.Factory.fromAssetFileName(getContext(), "done1.json", new OnCompositionLoadedListener() {

            @Override
            public void onCompositionLoaded(LottieComposition composition) {
                animation_view_click.setComposition(composition);
                animation_view_click.setProgress(0.0f);
//                animation_view_click.setMaxProgress(0.6f);
//                animation_view_click.setSpeed(0.6f);
                animation_view_click.setRepeatMode(LottieDrawable.RESTART);
//                animation_view_click.loop(true);
            }
        });
        initAniListner();
        if ("1".equals(mParam1))
        startAnima();
    }

    private void initAniListner() {
        animation_view_click.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation_view_click.getProgress() > 0.59
                        && !"1".equals(animation_view_click.getTag())) {
//                    animation_view_click.setRepeatCount(0);
                    animation_view_click.setProgress(0.0f);
                }
//                Log.e("getProgress", animation_view_click.getProgress() + "");
//                tv_seek.setText(" 动画进度" +(int) (animation.getAnimatedFraction()*100) +"%");
            }
        });
        animation_view_click.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stopAnima();
                myAdapter.notifyDataSetChanged();
                animation_view_click.setTag("0");
                animation_view_click.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setWancheng(int i) {
        if (!cid.equals("-1")) {
            Map<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("cid", cid);
            doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
