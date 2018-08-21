package com.rentian.newoa.modules.communication.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.NetworkUtil;
import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;
import com.rentian.newoa.modules.communication.model.imodel.IContactsModel;
import com.rentian.newoa.modules.communication.model.imodelimpl.ContactsModelImpl;
import com.rentian.newoa.modules.communication.view.iview.IContactsView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ContactsPresenter {
    private IContactsView mView;

    private IContactsModel mModule;

    private Context mContext;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (null != msg.obj) {
                    List<EmployeeInfoByNet> book = (List<EmployeeInfoByNet>) msg.obj;

                    mView.showContacts(book);
                } else {

                }
            }
        }
    };

    public ContactsPresenter(IContactsView view, Context mContext) {
        this.mView = view;

        this.mModule = new ContactsModelImpl();

        this.mContext = mContext;
    }


    public void getAddressBook() {
        if (NetworkUtil.isNetworkConnected(mContext)) {

            OkHttpUtil.getDefault().doPostAsync(
                    HttpInfo.Builder().setUrl(Const.RTOA.URL_TEST_CONTACTS)
                            .addParam("uid", MyApp.getInstance().getMyUid())
                            .build(),
                    new CallbackOk() {
                        @Override
                        public void onResponse(HttpInfo info) throws IOException {
                            if (info.isSuccessful()) {
                                Log.e("通讯录", info.getRetDetail());
                                Type type = new TypeToken<List<EmployeeInfoByNet>>() {
                                }.getType();

                                Message msg = mHandler.obtainMessage(0);

                                msg.obj = CommonUtil.gson.
                                        fromJson(info.getRetDetail(), type);

                                msg.sendToTarget();

                            } else {
                                Log.e("通讯录", info.getRetDetail());
                            }
                        }
                    }
            );
        } else {
            mView.showToast(0);

        }
    }


}
