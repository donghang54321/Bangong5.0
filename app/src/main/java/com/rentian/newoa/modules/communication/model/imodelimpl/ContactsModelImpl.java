package com.rentian.newoa.modules.communication.model.imodelimpl;

import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;
import com.rentian.newoa.modules.communication.model.imodel.IContactsModel;

import java.lang.reflect.Type;
import java.util.List;

public class ContactsModelImpl implements IContactsModel {

    @Override
    public List<EmployeeInfoByNet> getAddressBook() {
        String resJson = HttpURLConnHelper
                .requestByGET(Const.RTOA.URL_TEST_CONTACTS+ MyApp.getInstance().getMyUid());

        Type type = new TypeToken<List<EmployeeInfoByNet>>() {
        }.getType();

        if (null != resJson)
            return CommonUtil.gson.fromJson(resJson, type);

        return null;
    }

}
