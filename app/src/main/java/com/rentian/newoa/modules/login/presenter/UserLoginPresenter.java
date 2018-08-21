package com.rentian.newoa.modules.login.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.login.bean.ResUser;
import com.rentian.newoa.modules.login.bean.User;
import com.rentian.newoa.modules.login.module.imodule.IUserLoginModule;
import com.rentian.newoa.modules.login.module.imoduleimpl.UserLoginModuleImpl;
import com.rentian.newoa.modules.login.view.iview.IUserLoginView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserLoginPresenter {

    private IUserLoginModule mModule;
    private IUserLoginView mView;

    private Activity activity;

    private boolean isOnLogin = false;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                if (null != msg.obj)// obj是个Json格式数据
                {

                    String resJson = msg.obj.toString();
                    Log.e("json", resJson);
                    ResUser resUser = CommonUtil.gson.fromJson(resJson,
                            ResUser.class);

                    int status = Integer.parseInt(resUser.status_login);

                    switch (status) {
                        case -1:
                            mView.showLoginResult(-1);// 用户名不存在
                            break;
                        case 0:
                            mView.showLoginResult(0);// 参数错误
                            break;
                        case 1:
                            break;
                        case 2:
                            mView.showLoginResult(2);// 系统错误
                            break;
                        case 3:
                            mView.showLoginResult(3);// 密码错误
                            break;
                        case 5:
                            MyApp myApp = (MyApp) activity.getApplication();
                            MyApp.getInstance().setMyUid(resUser.userid);
                            MyApp.getInstance().setMyRoomId(resUser.room);
                            MyApp.getInstance().setZone(resUser.zone);
                            MyApp.getInstance().setResp(resUser.resp);
                            if (resUser.deptname != null) {
                                myApp.myDept = resUser.deptname;
                            }

                            if (resUser.name != null) {
                                myApp.myName = resUser.name;
                            }

//                            if (resUser.mq_ip != null) {
//                                Const.RTOA.CIM_SERVER_HOST = resUser.mq_ip;
//                            }
//
//                            Const.RTOA.CIM_SERVER_PORT = resUser.mq_port;

                            if (resUser.mail != null) {
                                myApp.myMail = resUser.mail;
                            }

                            if (resUser.post != null) {
                                myApp.myPost = resUser.post;
                            }

                            if (resUser.tel != null) {
                                myApp.myTel = resUser.tel;
                            }

                            if (resUser.cell != null) {
                                myApp.myPhone = resUser.cell;
                            }

                            if (resUser.img != null) {
                                myApp.myImg = resUser.img;
                            }

                            Log.e("roomid", resUser.room + "**");
                            saveUser();

                                mView.showLoginResult(5);// 登陆成功
                            break;
                        default:
                            break;

                    }

                    if (5 != status) {
                        isOnLogin = false;
                        mView.showLoginPost();
                    }

                } else {
                    mView.showLoginResult(6);// 网络错误
                    isOnLogin = false;
                    mView.showLoginPost();
                }

                // MyApp myApp = (MyApp) activity.getApplication();
                // myApp.setUid("137");
                // saveUser();
                // mView.showLoginResult(5);// 登陆成功

            }else if (msg.what == 1){
                mView.showLoginResult(6);// 网络错误
                isOnLogin = false;
                mView.showLoginPost();
            }
        }

        ;
    };

    public UserLoginPresenter(IUserLoginView mView, Activity activity) {
        this.mView = mView;
        this.mModule = new UserLoginModuleImpl();
        this.activity = activity;
        loadUser();
    }

    private void okhttPLogin(final User user) {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("username", user.getUsername().trim());
        hashMap.put("password", user.getPassword().trim());

        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_LOGIN).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("json", info.getRetDetail());
                            Message msg = mHandler.obtainMessage(0);


                            msg.obj = info.getRetDetail();

                            msg.sendToTarget();

                        } else {
                            Log.e("login", info.getRetDetail());
                            Message msg = mHandler.obtainMessage(1);


                            msg.obj = info.getRetDetail();

                            msg.sendToTarget();
                        }
                    }
                }
        );

    }

    public void login() {
        final User user = getUserInfo();

        if (!isOnLogin) {
            int isInputLegal = mModule.isUserInputLegal(user);

            switch (isInputLegal) {
                case 0:
                    isOnLogin = true;
                    mView.showLoginExecuting();
                    okhttPLogin(user);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Message msg = mHandler.obtainMessage(0);
//
//
//                            msg.obj = mModule.login(user);
//
//                            msg.sendToTarget();
//
//                        }
//                    }).start();

                    break;
                case 1:
                    mView.showLoginResult(7);// blank username or password
                    break;

                default:
                    break;
            }

        } else {
            mView.showLoginResult(8);// 正在登陆
        }

    }

    private String isLogin;

    public void loadUser() {
        User user = mModule.getUserInfo(activity);
//
//        if (0 != user.getUsername().length()
//                && 0 != user.getPassword().length()) {
        mView.setUserInfo(user);
        isLogin = user.getIsLogin();
//        if (isLogin.equals("1")) {
//            login();
//        }
//        }

    }

    public User getUserInfo() {
        return mView.getUserInfo();
    }

    private void saveUser() {
        mModule.saveUser(activity, getUserInfo());
    }


}
