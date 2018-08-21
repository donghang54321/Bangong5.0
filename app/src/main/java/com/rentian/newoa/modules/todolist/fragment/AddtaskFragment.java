package com.rentian.newoa.modules.todolist.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.noober.menu.FloatMenu;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.modules.contactlist.view.SearchActivity;
import com.rentian.newoa.modules.timesecletor.MeiZuActivity;

import cn.showzeng.demo.Interface.DialogFragmentDataCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rentianjituan on 2018/6/15.
 */

public class AddtaskFragment extends DialogFragment implements View.OnClickListener {

    private EditText commentEditText;
    private InputMethodManager inputMethodManager;
    private DialogFragmentDataCallback dataCallback;
    private RelativeLayout rl1, rl2, rl3, rl4, rl5;
    private ImageView sendButton,rankButton;
    private CircleImageView ivAdduser;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        if (!(getActivity() instanceof DialogFragmentDataCallback)) {
            throw new IllegalStateException("DialogFragment 所在的 activity 必须实现 DialogFragmentDataCallback 接口");
        }
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog mDialog = new Dialog(getActivity(), cn.showzeng.demo.R.style.BottomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.diaolog_fragment_task);
        mDialog.setCanceledOnTouchOutside(true);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams;
        if (window != null) {
            layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }

        commentEditText = mDialog.findViewById(R.id.edit_comment);
        sendButton = mDialog.findViewById(R.id.main_tab_iv5);
        rankButton=mDialog.findViewById(R.id.main_tab_iv3);
        ivAdduser=mDialog.findViewById(R.id.main_tab_iv2);
        rl1 = mDialog.findViewById(R.id.tab_rl_1);
        rl2 = mDialog.findViewById(R.id.tab_rl_2);
        rl3 = mDialog.findViewById(R.id.tab_rl_3);
        rl4 = mDialog.findViewById(R.id.tab_rl_4);
        rl5 = mDialog.findViewById(R.id.tab_rl_5);
        fillEditText();
        setSoftKeyboard();
//        initRankmenu(rl3);
        commentEditText.addTextChangedListener(mTextWatcher);
        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        return mDialog;
    }

    private void fillEditText() {
        dataCallback = (DialogFragmentDataCallback) getActivity();
        commentEditText.setText(dataCallback.getCommentText());
        commentEditText.setSelection(dataCallback.getCommentText().length());
        if (dataCallback.getCommentText().length() == 0) {
            sendButton.setEnabled(false);
            sendButton.setColorFilter(ContextCompat.getColor(getActivity(), cn.showzeng.demo.R.color.iconCover));
        }
    }

    private FloatMenu floatMenu;

    private void initRankmenu(View view) {
        floatMenu = new FloatMenu(mContext, view);
        floatMenu.inflate(R.menu.menu_normal);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
            }
        });

    }

    private void setSoftKeyboard() {
        commentEditText.setFocusable(true);
        commentEditText.setFocusableInTouchMode(true);
        commentEditText.requestFocus();

        //为 commentEditText 设置监听器，在 DialogFragment 绘制完后立即呼出软键盘，呼出成功后即注销
        commentEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.showSoftInput(commentEditText, 0)) {
                        commentEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                sendButton.setEnabled(true);
                sendButton.setClickable(true);
                sendButton.setColorFilter(ContextCompat.getColor(getActivity(), cn.showzeng.demo.R.color.colorAccent));
            } else {
                sendButton.setEnabled(false);
                sendButton.setColorFilter(ContextCompat.getColor(getActivity(), cn.showzeng.demo.R.color.iconCover));
            }
        }
    };

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tab_rl_1) {
            MeiZuActivity.show(getActivity());

        } else if (i == R.id.tab_rl_2) {
            startActivityForResult(new Intent(getActivity(),
                    SearchActivity.class),REQUESTCODE);

        } else if (i == R.id.tab_rl_3) {
//            floatMenu.show();
//            dataCallback.setRank();
            dialogChoice();

        } else if (i == R.id.tab_rl_4) {
            ToastUtill.showMessage("" + i);

        } else if (i == R.id.main_tab_iv5) {
            if (commentEditText.getText().toString().trim().length() != 0)
                dataCallback.sendPinglun(commentEditText.getText().toString());
            commentEditText.setText("");
            dismiss();

        } else {
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dataCallback.setCommentText(commentEditText.getText().toString());
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dataCallback.setCommentText(commentEditText.getText().toString());
        super.onCancel(dialog);
    }

    /**
     * 单选
     */
    private RadioButton btRank1, btRank2, btRank3, btRank4;

    @SuppressLint("InflateParams")
    private void dialogChoice() {
        //// TODO: 2018/7/5 选择优先级
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate
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
        setRbtOnCheckedChangeListener(btRank1, 1, dialog,R.color.rank1,R.drawable.jibie1);
        setRbtOnCheckedChangeListener(btRank2, 2, dialog,R.color.rank2,R.drawable.jibie2);
        setRbtOnCheckedChangeListener(btRank3, 3, dialog,R.color.rank3,R.drawable.jibie3);
        setRbtOnCheckedChangeListener(btRank4, 4, dialog,R.color.rank4,R.drawable.jibie);



    }

    private void setRbtOnCheckedChangeListener(RadioButton bt, final int rank
            , final AlertDialog dialog, final int color,final int drawable) {
        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCallback.setRank(rank);
                    dialog.dismiss();
                    rankButton.setImageResource(drawable);
                    rankButton.setColorFilter(ContextCompat.getColor(getActivity(), color));
                }

            }
        });
    }
    private final static int REQUESTCODE = 1; // 返回的结果码
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                Glide.with(getActivity())
                        .load(Const.RTOA.URL_BASE + data.getStringExtra("url"))
                        .into(ivAdduser);
            }
        }
    }

}
