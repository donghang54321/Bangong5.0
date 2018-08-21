package com.rentian.newoa.modules.team.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.RecycleViewDivider;
import com.rentian.newoa.modules.login.view.RegisterActivity;
import com.rentian.newoa.modules.todolist.adapter.PoplistAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class AddteamActivity extends Base2Activity implements SetActionBarListener {
    private PublishSubject<String> mPublishSubject;
    private DisposableObserver<String> mDisposableObserver;
    private CompositeDisposable mCompositeDisposable;
    private EditText editText;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteam);
        setStatusBarDarkMode();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        initRxAndroid();
        initView();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private ArrayList<DataList> mGongsi = new ArrayList<>();
    private PoplistAdapter mPoplistAdapter;

    private void initView() {
        editText = findViewById(R.id.et);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                mPublishSubject.onNext(s.toString());
            }

        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //// TODO: 2018/8/8
        mPoplistAdapter = new PoplistAdapter(this, mGongsi, loginData.data.cid, this);
        mRecyclerView.setAdapter(mPoplistAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initRxAndroid() {
        mPublishSubject = PublishSubject.create();
        mDisposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String s) {
//                    mTvSearch.setText(s);
                //// TODO: 2018/5/23
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("s", s);
                doHttpAsync(Const.RTOA.URL_LIST_TEAM, hashMap, 1);

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        mPublishSubject.debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {

            @Override
            public boolean test(String s) throws Exception {
                return s.length() >= 0;
            }

        }).switchMap(new Function<String, ObservableSource<String>>() {

            @Override
            public ObservableSource<String> apply(String query) throws Exception {
                return getSearchObservable(query);
            }

        }).observeOn(AndroidSchedulers.mainThread()).subscribe(mDisposableObserver);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mDisposableObserver);
    }

    private int sleep = 100;

    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                try {
                    Thread.sleep(sleep + (long) (Math.random() * sleep * 5));
                } catch (InterruptedException e) {
                    if (!observableEmitter.isDisposed()) {
                        observableEmitter.onError(e);
                    }
                }
//                Log.e("SearchActivity", "结束请求，关键词为：" + query);
                observableEmitter.onNext(query);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void setActionBar(int i, String imgurl) {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", i + "");
        doHttpAsync(Const.RTOA.URL_ADD_TEAM, hashMap, 2);
    }

    public void doHttpAsync(String url, Map<String, String> hashMap, final int tag) {
        OkGo.<TaskData>post(url)
                .tag(tag)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {

                        if (response.body().code == 0) {
                            if (tag == 1) {
                                mGongsi.clear();

                                mGongsi.addAll(response.body().data.company);
                                for (DataList x : mGongsi) {
                                    x.titleType = 3;
                                }
                                mPoplistAdapter.notifyDataSetChanged();
                            } else if (tag == 2) {
                                ToastUtill.showMessage(response.body().msg);
                                finish();
                            }
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }


                    }
                });

    }
}
