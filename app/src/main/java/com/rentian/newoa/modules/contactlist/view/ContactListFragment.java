package com.rentian.newoa.modules.contactlist.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.communication.adapter.ContactAdapter;
import com.rentian.newoa.modules.communication.bean.EmployeeInfo;
import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;
import com.rentian.newoa.modules.communication.presenter.ContactsPresenter;
import com.rentian.newoa.modules.communication.view.ContactDetailsActivity;
import com.rentian.newoa.modules.communication.view.HanziToPinyin;
import com.rentian.newoa.modules.communication.view.SideBar;
import com.rentian.newoa.modules.communication.view.iview.IContactsView;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher, IContactsView, AdapterView.OnItemClickListener {

    private ListView mListView;
    private TextView mFooterView;
    private TextView mDialog;
    private View mHeaderView;

    MyApp myApp;

    private ContactsPresenter mPresenter;
    private ArrayList<EmployeeInfo> datas = new ArrayList<>();
    private ContactAdapter mAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myApp = (MyApp) activity.getApplication();
        mPresenter = new ContactsPresenter(this, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contast_list, container, false);
        initWidget(view);
        mPresenter.getAddressBook();
        return view;
    }

    private void initWidget(View view) {
        mListView = (ListView) view.findViewById(R.id.school_friend_member);
        SideBar mSideBar = (SideBar) view.findViewById(R.id.school_friend_sidrbar);
        mDialog = (TextView) view.findViewById(R.id.school_friend_dialog);
        //联系人列表头部分组布局和搜索
        mHeaderView = View.inflate(getContext(), R.layout.item_contact_list_header, null);
        mListView.addHeaderView(mHeaderView);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(getContext(), R.layout.item_list_contact_count, null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<EmployeeInfo> temp = new ArrayList<>(datas);
        for (EmployeeInfo data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 1;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0)) + 1;
        }
        if (position != 0) {
            mListView.setSelection(position);
        } else if (s.contains("#") || s.contains(" ")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void showContacts(List<EmployeeInfoByNet> book) {
        datas.clear();
        SharedPreferences sp = getActivity().getSharedPreferences("user_info",
                Activity.MODE_PRIVATE);
        for (int i = 0; i < book.size(); i++) {
            EmployeeInfo temp = new EmployeeInfo();
            temp.setName(book.get(i).name);
            temp.setUid(book.get(i).uid);
            temp.setTel(book.get(i).tel);
            temp.setPhone(book.get(i).phone);
            temp.setPosition(book.get(i).position);
            temp.setDept(book.get(i).dept);
            temp.setAddr(book.get(i).addr);
            temp.setEmail(book.get(i).email);
            temp.setPinyin(HanziToPinyin.getPinYin(book.get(i).name));
            datas.add(temp);
        }
        mAdapter = new ContactAdapter(getContext(), datas);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
//        MyMSG.Instance().setEmployeeInfo(datas);
        // 给listView设置adapter
        mFooterView.setText(datas.size() + "位联系人");
        mListView.addFooterView(mFooterView);
        mListView.setOnItemClickListener(this);

    }


    @Override
    public void showToast(int flag) {
        if (0 == flag) {
            CommonUtil.showToast(getActivity(), "未连接网络，请检查网络设置，通讯录信息加载失败");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            FrameLayout searchfl = (FrameLayout) view.findViewById(R.id.search_layout);
            searchfl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            Intent intent = new Intent(getActivity(), ContactDetailsActivity.class);
            EmployeeInfo temp = (EmployeeInfo) mAdapter.getItem(position - 1);
            Bundle bundle = new Bundle();
            bundle.putString("name", temp.getName());
            bundle.putString("tel", temp.getTel());
            bundle.putString("phone", temp.getPhone());
            bundle.putString("email", temp.getEmail());
            bundle.putString("addr", temp.getAddr());
            bundle.putString("img", temp.getImg());
            bundle.putString("dept", temp.getDept());
            bundle.putInt("uid", temp.getUid());
            bundle.putString("position", temp.getPosition());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDialog != null)
            if (mDialog.getVisibility() == View.VISIBLE)
                mDialog.setVisibility(View.GONE);
    }

    public void DialogGone() {
        if (mDialog != null)
            if (mDialog.getVisibility() == View.VISIBLE)
                mDialog.setVisibility(View.GONE);
    }


}
