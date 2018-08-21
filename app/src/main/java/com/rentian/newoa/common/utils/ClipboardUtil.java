package com.rentian.newoa.common.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by HBRT on 2016/2/24.
 */
public class ClipboardUtil {

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}
