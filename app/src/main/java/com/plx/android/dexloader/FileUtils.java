package com.plx.android.dexloader;

import android.content.Context;

import com.plx.android.open.IShowToast;
import com.plx.android.open.PluginManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dalvik.system.DexClassLoader;

/**
 * 将assets文件copy到app/data/cache目录
 */
public class FileUtils {
    public static void copyFiles(Context context, String fileName, File desFile) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getApplicationContext().getAssets().open(fileName);
            out = new FileOutputStream(desFile.getAbsolutePath());
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) != -1)
                out.write(bytes, 0, len);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 加载dex文件中的class，并调用其中的showToast方法
     */
    public static void loadDexClass(Context context) {
        File cacheFile = context.getDir("dex", 0);
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "classes.dex";
        String libSearchPath = cacheFile.getAbsolutePath() + File.separator + "libtest_plugin.so";
        File desFile = new File(internalPath);
        File desSoFile = new File(libSearchPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                FileUtils.copyFiles(context, "classes.dex", desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!desSoFile.exists()) {
                desSoFile.createNewFile();
                FileUtils.copyFiles(context, "libtest_plugin.so", desSoFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        IShowToast impl = PluginManager.get().getInterface(IShowToast.class);
        if (impl == null) {

            //下面开始加载dex class
            //1.待加载的dex文件路径，如果是外存路径，一定要加上读外存文件的权限,
            //2.解压后的dex存放位置，此位置一定要是可读写且仅该应用可读写
            //3.指向包含本地库(so)的文件夹路径，可以设为null
            //4.父级类加载器，一般可以通过Context.getClassLoader获取到，也可以通过ClassLoader.getSystemClassLoader()取到。
            DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), cacheFile.getAbsolutePath(), context.getClassLoader());
            try {
                //该name就是internalPath路径下的dex文件里面的ShowToastImpl这个类的包名+类名
                Class<?> clz = dexClassLoader.loadClass("com.plx.android.plugin.ShowToastImpl");
                impl = (IShowToast) clz.newInstance();//通过该方法得到IShowToast类
                if (impl != null) {
                    PluginManager.get().put(IShowToast.class, impl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (impl != null) {
            impl.showToast(context);//调用打开弹窗
        }

    }
}
