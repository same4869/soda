package com.xun.sodalibrary.utils;

public class Ccc {
    //只要使用到这个类，就会进行签名校验，如果不满足则会crash
    static { // 加载libccc.so，只要在方法调用前加载，放哪都行
        System.loadLibrary("ccc");
    }

    //获得c++层加密的字符串
    public static native String cdf();


//    //getSign方法
//    public static String bbc(Context context) {
//        try {
//            // 下面几行代码展示如何任意获取Context对象，在jni中也可以使用这种方式
////            Class<?> activityThreadClz = Class.forName("android.app.ActivityThread");
////            Method currentApplication = activityThreadClz.getMethod("currentApplication");
////            Application application = (Application) currentApplication.invoke(null);
////            PackageManager pm = application.getPackageManager();
////            PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);
//            PackageManager pm = context.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//            Signature[] signatures = pi.signatures;
//            Signature signature0 = signatures[0];
//            return signature0.toCharsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
}
