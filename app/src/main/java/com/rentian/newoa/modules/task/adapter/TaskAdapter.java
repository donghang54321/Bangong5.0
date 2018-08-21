package com.rentian.newoa.modules.task.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fang.dashview.DashView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.ExtendedEditText;
import com.rentian.newoa.modules.task.bean.TaskDetail;
import com.rentian.newoa.modules.task.iview.SetZhuyemian;
import com.rentian.newoa.modules.todolist.bean.TaskData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

/**
 * Created by rentianjituan on 2018/5/8.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.NormalItem> {

    //数据
    private ArrayList<TaskDetail> mData;
    private LayoutInflater mLayoutInflater;
    private int dz;
    private int pl;
    private int fj;
    private int dongtai;
    private int isZirenwu;
    private PublishSubject<String> mPublishSubject;
    private DisposableObserver<String> mDisposableObserver;
    private CompositeDisposable mCompositeDisposable;
    private SetZhuyemian setZhuyemian;

    public TaskAdapter(Context context, ArrayList<TaskDetail> data
            , SetZhuyemian setZhuyemian) {
        mLayoutInflater = LayoutInflater.from(context);
        this.setZhuyemian = setZhuyemian;
        mData = data;
        mPublishSubject = PublishSubject.create();
        mDisposableObserver = new DisposableObserver<String>() {

            @Override
            public void onNext(String s) {
//                    mTvSearch.setText(s);
                //// TODO: 2018/5/23
                Log.e("size", s.length() + "1");

                if (delweizhi == 0) {
                    mData.get(delweizhi).title = s;
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", mData.get(0).pid + "");
                    hashMap.put("biaoti", s);
                    doHttpAsync(Const.RTOA.URL_UPDATE_TITLE, hashMap);
                    return;
                }
                if (isZirenwu == 0) {
                    mData.get(delweizhi).neirong = s;
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("id", mData.get(0).pid + "");
                    hashMap.put("content", s);
                    doHttpAsync(Const.RTOA.URL_UPDATE_FEIZIRENWU, hashMap);
                    return;
                }


                if (s.length() == 0) {
                    shanchuMap.put(delweizhi, true);

                }
                if (s.contains("\n")) {
                    mData.get(delweizhi).neirong = s.replace("\n", "");
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("pid", mData.get(0).pid + "");
                    hashMap.put("seq", delweizhi + "");
                    hashMap.put("content", "");
                    doHttpAsync(Const.RTOA.URL_ADD_ZIRENWU, hashMap, delweizhi);
                } else {
                    mData.get(delweizhi).neirong = s;
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("pid", mData.get(0).pid + "");
                    hashMap.put("id", mData.get(delweizhi).id + "");
                    hashMap.put("seq", delweizhi + "");
                    hashMap.put("del", "0");
                    hashMap.put("content", s);
                    doHttpAsync(Const.RTOA.URL_UPDATE_ZIRENWU, hashMap);
                }


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

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).layoutType;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public TaskAdapter.NormalItem onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1 || viewType == 8) {
            return new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_bianji, parent, false));
        } else if (viewType == 2 || viewType == 9) {
            TaskAdapter.NormalItem viewHolder
                    = new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_fujian, parent, false));
            return viewHolder;
        } else if (viewType == 3) {
            TaskAdapter.NormalItem viewHolder
                    = new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_dianzan, parent, false));
            return viewHolder;
        } else if (viewType == 4) {
            TaskAdapter.NormalItem viewHolder
                    = new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_pinglun, parent, false));
            return viewHolder;
        } else if (viewType == 5) {
            TaskAdapter.NormalItem viewHolder
                    = new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_dongtai, parent, false));
            return viewHolder;
        } else if (viewType == 7) {
            TaskAdapter.NormalItem viewHolder
                    = new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_renwu, parent, false));
            return viewHolder;
        } else {
            return new TaskAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task_title, parent, false));

        }

    }

    private Map<Integer, EditText> editTextMap = new HashMap<>();
    private Map<Integer, Boolean> shanchuMap = new HashMap<>();

    @Override
    public void onBindViewHolder(final TaskAdapter.NormalItem holder, final int position) {
        shanchuMap.put(position, false);
        if (getItemViewType(position) == 1) {
            editTextMap.put(position, holder.editText);
            holder.editText.setText(mData.get(position).neirong);
            holder.editText.setTextSize(14.0f);
            holder.editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

//                    Log.e("afterTextChanged","11");
                    sleep = 100;
                    startSearch(s.toString(), position);
                }

            });

        } else if (getItemViewType(position) == 2 || getItemViewType(position) == 9) {
            if (fj == 0) {
                fj = position;
            }
            if (getItemViewType(position) == 9) {
                holder.ivLine.setVisibility(View.VISIBLE);
            }
            holder.tvSize.setText(mData.get(position).daxiao);
            holder.tvName.setText(mData.get(position).mingcheng + "."
                    + mData.get(position).leixing);
        } else if (getItemViewType(position) == 3) {
            holder.tvContent.setText(mData.get(position).c2);
        } else if (getItemViewType(position) == 4) {
            if (mData.get(position).img == null) {
                holder.ivTouxiang.setImageResource(R.drawable.default_avatar);
            } else {
                Glide.with(mLayoutInflater.getContext())
                        .load(Const.RTOA.URL_BASE + mData.get(position).img)
                        .into(holder.ivTouxiang);
            }
            if (mData.get(position).my==1){
                holder.ivDel.setVisibility(View.VISIBLE);
                holder.ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("id", mData.get(position).id + "");
                        doDel(Const.RTOA.URL_DEL_PINGLUN, hashMap,position);
                    }
                });
            }else {
                holder.ivDel.setVisibility(View.GONE);
            }
            holder.tvTime.setText(mData.get(position).time);
            holder.tvContent.setText(mData.get(position).content);
            holder.tvName.setText(mData.get(position).name);
        } else if (getItemViewType(position) == 5) {

            holder.tvTime.setText(mData.get(position).time);
            holder.tvContent.setText(mData.get(position).content);
            if (position == getItemCount() - 1) {
                holder.iv.setImageResource(R.drawable.time2);
                holder.line.setVisibility(View.GONE);
            } else if (mData.get(position).id == 1) {
                holder.iv.setImageResource(R.drawable.time1);
            } else {
                holder.iv.setImageResource(R.drawable.time2);
            }
        } else if (getItemViewType(position) == 6) {

            holder.tvNum.setText(mData.get(position).titleNum);
            holder.tvName.setText(mData.get(position).name);
            if (mData.get(position).name.equals("点赞")) {
                dz = position;
            } else if (mData.get(position).name.equals("评论")) {
                pl = position;
            }else if (mData.get(position).name.equals("动态")) {
                dongtai = position;
            }
        } else if (getItemViewType(position) == 7) {
            getFous(holder.relativeLayout);
            holder.editText.setText(mData.get(position).neirong);
//            holder. editText.setFocusableInTouchMode(true);
            editTextMap.put(position, holder.editText);
            if (mData.get(position).wancheng == 1) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
//            editTexts.add(holder.editText);
            Log.e("position:", mData.get(position).neirong + position);


            if (jiaodian == position) {
//                Log.e("delete", "" + position);
                showSoftInputFromWindow((Activity) mLayoutInflater.getContext(), holder.editText);
            }
            final String[] befores = new String[1];
            holder.editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    befores[0] = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

//                    Log.e("afterTextChanged","11");
                    if (s.length() == 0) {
                        sleep = 0;
                    } else {
                        sleep = 100;
                    }
                    Log.e("wenzi", befores[0] + "   " + s.toString());
                    if (!befores[0].equals(s.toString())) {
                        if (isZirenwu == 1)
                            startSearch(s.toString(), position);
                    }

                }
            });

            holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {//获得焦点
                        Log.e("获得焦点", "1");
                        if (holder.editText.getText().length() == 0) {
                            sleep = 0;
                            shanchuMap.put(position, true);
                        } else {
                            sleep = 100;
                            shanchuMap.put(position, false);
                        }
                    } else {//失去焦点
                        Log.e("失去焦点", "1");
                        sleep = 100;
                        shanchuMap.put(position, false);
                    }
                }
            });

            holder.editText.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (holder.editText.getText().length() == 0
                                && shanchuMap.get(position)) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("pid", mData.get(0).pid + "");
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("seq", position + "");
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("del", "1");
                            hashMap.put("content", "");
                            doHttpAsync(Const.RTOA.URL_UPDATE_ZIRENWU, hashMap);
                            shanchuMap.put(position, false);
                            sleep = 100;
                            jiaodian = position - 1;
                            removejianting();
                            mData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(0, mData.size());


                        }
                    }
                    return false;
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        if (mData.get(position).wancheng == 0) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("complete", "1");
                            submitTask(Const.RTOA.URL_TASK_SUBMIT, hashMap, position);
                        }
//                        editText1.setText(buttonView.getText()+"选中");
                    } else {
                        if (mData.get(position).wancheng == 1) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("complete", "0");
                            submitTask(Const.RTOA.URL_TASK_SUBMIT, hashMap, position);
                        }
//                        editText1.setText(buttonView.getText()+"取消选中");
                    }
                }
            });


        } else if (getItemViewType(position) == 8) {
            editTextMap.put(position, holder.editText);
            holder.editText.getPaint().setFakeBoldText(true);
            holder.editText.setText(mData.get(position).title);
            holder.editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

//                    Log.e("afterTextChanged","11");
                    sleep = 100;
                    startSearch(s.toString(), position);
                }

            });

            holder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                        //TODO回车键按下时要执行的操作
                        Log.e("回车", "enter");
                        return true;
                    }
                    return false;
                }
            });
        }
//        Log.e("ed",editTextMap.size()+"");
    }

    private int jiaodian;

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        editText.setSelection(editText.getText().length());
    }

    public void getFous(RelativeLayout relativeLayout) {
        relativeLayout.setFocusable(true);
        relativeLayout.setFocusableInTouchMode(true);
    }

    private int delweizhi;

    private void startSearch(String query, int pos) {
        mPublishSubject.onNext(query);
        delweizhi = pos;
    }

    public void removejianting() {
        if (editTextMap.size() > 0)
            for (Integer x : editTextMap.keySet()) {
                ((ExtendedEditText) editTextMap.get(x)).clearTextChangedListeners();
            }
    }

    private int sleep = 100;

    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                Log.d("SearchActivity", "开始请求，关键词为：" + query);
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
    public int getItemCount() {
        return mData.size();
    }

    public class NormalItem extends RecyclerView.ViewHolder {
        public TextView tvSize, tvName, tvContent, tvTime, tvNum; // 滑动的view
        public ImageView iv, ivTouxiang, ivLine,ivDel;
        public DashView line;
        public CheckBox checkBox;
        //        public EditText editText;
        public ExtendedEditText editText;
        public RelativeLayout relativeLayout;

        public NormalItem(View view) {
            super(view);
            ivDel=view.findViewById(R.id.iv_del);
            iv = view.findViewById(R.id.iv);
            ivLine = view.findViewById(R.id.line_top);
            tvSize = view.findViewById(R.id.tv_size);
            tvName = view.findViewById(R.id.tv_name);
            tvNum = view.findViewById(R.id.tv_num);
            tvTime = view.findViewById(R.id.tv_time);
            tvContent = view.findViewById(R.id.tv_content);
            ivTouxiang = view.findViewById(R.id.iv_touxiang);
            line = view.findViewById(R.id.line);
            editText = view.findViewById(R.id.et_renwu);
            relativeLayout = view.findViewById(R.id.rl);
            checkBox = view.findViewById(R.id.cb);


        }
    }

    private void doHttpAsync(final String url, final Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {

                            if ("1".equals(hashMap.get("del")))
                                zirenwuNum = zirenwuNum - 1;
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    private void submitTask(String url, final Map<String, String> hashMap, final int fromPosition) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            setZhuyemian.setWancheng(response.body().data.r);
                            if (hashMap.get("complete").equals("1")) {
                                removejianting();
                                mData.get(fromPosition).wancheng = 1;
                                notifyItemMoved(fromPosition, zirenwuNum);
                                for (int i = 0; i < zirenwuNum - fromPosition; i++) {
                                    Collections.swap(mData, fromPosition + i, fromPosition + i + 1);
                                }
//                            Collections.swap(mData, fromPosition, zirenwuNum);
                                notifyItemRangeChanged(1, zirenwuNum);
                            } else if (hashMap.get("complete").equals("0")) {
                                mData.get(fromPosition).wancheng = 0;

                            }
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    private void doHttpAsync(String url, Map<String, String> hashMap, final int position) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            TaskDetail rwcontent = new TaskDetail();
                            rwcontent.layoutType = 7;
                            rwcontent.neirong = " ";
                            rwcontent.id = response.body().data.id;
                            jiaodian = position + 1;
                            mData.add(position + 1, rwcontent);
                            removejianting();
                            notifyItemInserted(position + 1);//通知演示插入动画
                            notifyItemRangeChanged(0, mData.size());
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    private void doDel(String url, Map<String, String> hashMap, final int position) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            mData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(0, mData.size());
                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }

    public int getPl() {
        return pl;
    }

    public void setPl(int pl) {
        this.pl = pl;
    }

    public int getDz() {
        return dz;
    }

    public void setDz(int dz) {
        this.dz = dz;
    }

    public int getFj() {
        return fj;
    }

    public void setFj(int fj) {
        this.fj = fj;
    }

    public void setIsZirenwu(int Zirenwu) {
        this.isZirenwu = Zirenwu;

    }
    public int getDongtai() {
        return dongtai;
    }
    private int zirenwuNum;

    public void setZirenwuNum(int zirenwuNum) {
        this.zirenwuNum = zirenwuNum;
    }

    public int getIsZirenwu() {
        return isZirenwu;
    }
}
