package com.rentian.newoa.modules.information.asynctask;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.bean.ResInformation;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInformationTask extends AsyncTask<String, Void, String> {

    private Message msg;

    private List<Map<String, Object>> list;

    public GetInformationTask(Message msg) {
        this.msg = msg;
    }

    @Override
    protected String doInBackground(String... url) {
        String json = HttpURLConnHelper.requestByGET(url[0]);
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("通知",result);
        if (null != result) {
            Type type = new TypeToken<List<ResInformation>>() {
            }.getType();
            List<ResInformation> informs = CommonUtil.gson.fromJson(
                    result, type);
            list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = null;
            for (int i = 0; i < informs.size(); i++) {
                map = new HashMap<String, Object>();
                map.put("id", informs.get(i).id);
                map.put("notificatin_date", "发布时间：" + informs.get(i).time);
                map.put("notification_publisher", "");
                map.put("notification_subject", informs.get(i).subject);
                list.add(map);
            }
            msg.obj = list;
        }
        msg.sendToTarget();
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

}
