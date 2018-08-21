package com.rentian.newoa.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;


/**
 * Created by rentianjituan on 2018/4/26.
 */

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    @Override
    public void onError(Response<T> response) {
        super.onError(response);
        Log.e("errorcode",response.code()+"");
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {

        this.type = type;
    }
    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
//        request.headers("header1", "HeaderValue1")//
//                .params("params1", "ParamsValue1")//
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");
    }
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {

        ResponseBody body=response.body();
        if (body==null)
            return null;

        T data=null;

        Gson gson=new Gson();
        JsonReader jsonReader=new JsonReader(body.charStream());
        if (type!=null)
            data=gson.fromJson(jsonReader,type);
        if (clazz!=null)
            data=gson.fromJson(jsonReader,clazz);


        return data;
    }
}