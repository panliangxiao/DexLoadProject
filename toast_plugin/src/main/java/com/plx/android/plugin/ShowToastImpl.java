package com.plx.android.plugin;

import android.content.Context;
import android.widget.Toast;

import com.plx.android.open.IShowToast;

public class ShowToastImpl implements IShowToast {
    static {
//		System.load("/data/user/0/com.plx.android.dexloader/app_dex/libtest_plugin.so");
        System.loadLibrary("test_plugin");
    }

    @Override
    public int showToast(Context context) {
        Toast.makeText(context, "我来自另一个dex文件" + JNICLibrary.doNativeCommand(0), Toast.LENGTH_LONG).show();
        return 100;
    }
}
