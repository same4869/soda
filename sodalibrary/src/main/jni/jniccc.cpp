#include "com_xun_sodalibrary_utils_Ccc.h"
#include <android/log.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

jstring Java_com_xun_sodalibrary_utils_Ccc_cdf(JNIEnv *env, jclass type) {
    char *packageName = "this is a string from c++";
    return env->NewStringUTF(packageName);
}

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "security", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "security", __VA_ARGS__))

static int verifySign(JNIEnv *env);

static jobject getApplication(JNIEnv *env) {
    jobject application = NULL;
    jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz != NULL) {
        jmethodID currentApplication = env->GetStaticMethodID(
                activity_thread_clz, "currentApplication", "()Landroid/app/Application;");
        if (currentApplication != NULL) {
            application = env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        env->DeleteLocalRef(activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }

    return application;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }
    if (verifySign(env) == JNI_OK) {
        return JNI_VERSION_1_4;
    }
    //LOGE("签名不一致!");


    jclass tclss = env->FindClass("com/xun/sodalibrary/utils/Ccc");
    jobject application = getApplication(env);
    char *msg = "app可能被非法恶意篡改，请前往官网下载最新包~";
    jstring jmsg = env->NewStringUTF(msg);
    jmethodID showId = env->GetStaticMethodID(tclss, "cdc",
                                              "(Landroid/app/Application;Ljava/lang/String;)V");
    env->CallStaticVoidMethod(tclss, showId, application, jmsg);
    //LOGE("签名不一致2!");

    return JNI_ERR;
}

static const char *SIGN = "30820305308201eda0030201020204246c57e0300d06092a864886f70d01010b05003033310f300d060355040a13066d69686f796f310f300d060355040b13066d69686f796f310f300d060355040313066d69686f796f301e170d3139303430323036353730315a170d3434303332363036353730315a3033310f300d060355040a13066d69686f796f310f300d060355040b13066d69686f796f310f300d060355040313066d69686f796f30820122300d06092a864886f70d01010105000382010f003082010a02820101008c835aa23d4aadbc4442e621bc0a55fe8489f952df3fba392fd70807ed4276e7a4a28baa932cd4a1bb60ab07cc992ba31a1bc33b6753b0ba5aceb58a26655d9848f51af2267f281f7f0b56c957d63e5dd7e01d19b30de03873b5da330d87b1264d6f6968476b1f2efe887f4506e125e5839332b6c2e1713f621fa2cb7300dd2544f5679a6656c5e9367f8bc408a192887d3a3bc60af6b1f0fdc73a6ca9d31b1eee39d9e6866ab86042f996707be426256bf1b7afd98b946ec1f314df6044df54f888d39eaf67fd28148edba1bc9d0d2e6e1bdd794e676a135eb6354d646824c586b8c3c7d8a91142341edbf3e9fc92e487059168502b1f372bea6f8755b778d10203010001a321301f301d0603551d0e04160414cca92e1593e25974b4ec2f4865e9b7acb967be79300d06092a864886f70d01010b050003820101004e03acaf22066b885f2bf8695470e84e8c0af075ecaf35e67c9e6a13cfc018235e82ce7a7de7c0bcdadbd5e51233f8f5e65e1c1eeaefda6fe1dbdbd498f3da33986fc27ffda300d39a58b2f69cea4b33473d464cc9e9b58f35d91f31635643923869b1aeda1d15552dae2adefc4bd9ff0f47d1d84b6f9344f984e306142e73bea409e5bd09ebab806a3ac2e1f29e24756d8cece865d95933bcdc0f4abe274d82277518554debe943040f8bb623a2a83bf2b3362029109573c10b876f124aa9d8f02302def4d64957401ffc7c7184a5a500b1e8be2a073f9dbe6abc24cafce8aca5e7e2c1b21ad933de13780f7dd59ecabf220cc541b9f455276af8f2e5afdaa6";
static const char *SIGN_RELEASE = "3082033b30820223a003020102020418b180e4300d06092a864886f70d01010b0500304e310b3009060355040613023131310b3009060355040813023131310b3009060355040713023131310b3009060355040a13023131310b3009060355040b13023131310b3009060355040313023131301e170d3230303832313039343032345a170d3435303831353039343032345a304e310b3009060355040613023131310b3009060355040813023131310b3009060355040713023131310b3009060355040a13023131310b3009060355040b13023131310b300906035504031302313130820122300d06092a864886f70d01010105000382010f003082010a02820101008779fa9cb5c66f7cd38ae5d8d624bf8eea4412b40f8f756a6e75388331f2dcb68816c955036752a6885d5c62bab48793b2e9d01414314073326f6cb8bbaa3369113ba1f956855906ceb140b44b3d2ecc2f05994f269aa9ff35ed998c808399f72fefc68e57d90f2b186f761b40e3969a74671f0e60c419dfa6285c5d9ab0ede93ac9d96f988a69b843df0bc32c8756c995d3116cb324b043e619bb2dd72d4626448d5940db263e8e2abb4ff2c53f486a1fe9552ace86e83cbb4d15fc414d7547e4a792432c6f45f5eab8a23aba09a6fc7572dd974428a01815d933b1a3bfbac19885ee3752875055d61fd91f2a248975bc31725c303b6b2a8268e1247a177c1d0203010001a321301f301d0603551d0e041604141f187feb6f230bb68ef8ed51ea480f283f37830d300d06092a864886f70d01010b050003820101006e05b4b0a517c7c9fb71cd685270a9f972b5dcf82be58e838f2f041a908e642f6990d786b4e59967989182b7b2fbdfb1c17bb44f0582361bdbd6499eaec06fd8d46f0b3b4017e17553548fe3b55700f5d3e99fb78062e862508b8ea7e9eaab1b130eec707a9c13ec7abab525cb82c75b40e1a868ea46bb1eca45c37c74b678c19ea0ebbc7d2cb8a161f1b552d4a18fb83c1bd1312384a0767a907d6e418d8846454177649b50a80566d6822240254928af511d646f7446c1eb31442551ee35e4b7322a4a490a71116682fc5d875eab2afd823921bd0b4207ae1d6ace447434171d7aeef8a5cd005bc242aa1a3bead96c8eb8fafd974830c4d24198399e8b44d2";

static int verifySign(JNIEnv *env) {
    // Application object
    jobject application = getApplication(env);
    if (application == NULL) {
        return JNI_ERR;
    }
    // Context(ContextWrapper) class
    jclass context_clz = env->GetObjectClass(application);
    // getPackageManager()
    jmethodID getPackageManager = env->GetMethodID(context_clz, "getPackageManager",
                                                   "()Landroid/content/pm/PackageManager;");
    // android.content.pm.PackageManager object
    jobject package_manager = env->CallObjectMethod(application, getPackageManager);
    // PackageManager class
    jclass package_manager_clz = env->GetObjectClass(package_manager);
    // getPackageInfo()
    jmethodID getPackageInfo = env->GetMethodID(package_manager_clz, "getPackageInfo",
                                                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // context.getPackageName()
    jmethodID getPackageName = env->GetMethodID(context_clz, "getPackageName",
                                                "()Ljava/lang/String;");
    // call getPackageName() and cast from jobject to jstring
    jstring package_name = (jstring)(env->CallObjectMethod(application, getPackageName));
    // PackageInfo object
    jobject package_info = env->CallObjectMethod(package_manager, getPackageInfo, package_name, 64);
    // class PackageInfo
    jclass package_info_clz = env->GetObjectClass(package_info);
    // field signatures
    jfieldID signatures_field = env->GetFieldID(package_info_clz, "signatures",
                                                "[Landroid/content/pm/Signature;");
    jobject signatures = env->GetObjectField(package_info, signatures_field);
    jobjectArray signatures_array = (jobjectArray) signatures;
    jobject signature0 = env->GetObjectArrayElement(signatures_array, 0);
    jclass signature_clz = env->GetObjectClass(signature0);

    jmethodID toCharsString = env->GetMethodID(signature_clz, "toCharsString",
                                               "()Ljava/lang/String;");
    // call toCharsString()
    jstring signature_str = (jstring)(env->CallObjectMethod(signature0, toCharsString));

    // release
    env->DeleteLocalRef(application);
    env->DeleteLocalRef(context_clz);
    env->DeleteLocalRef(package_manager);
    env->DeleteLocalRef(package_manager_clz);
    env->DeleteLocalRef(package_name);
    env->DeleteLocalRef(package_info);
    env->DeleteLocalRef(package_info_clz);
    env->DeleteLocalRef(signatures);
    env->DeleteLocalRef(signature0);
    env->DeleteLocalRef(signature_clz);

    const char *sign = env->GetStringUTFChars(signature_str, NULL);
    if (sign == NULL) {
        LOGE("分配内存失败");
        return JNI_ERR;
    }

    //LOGI("应用中读取到的签名为：%s", sign);
    //LOGI("签名长度：%zu",strlen(sign));
    //LOGI("native中预置的debug签名为：%s", SIGN);
    //LOGI("签名长度：%zu",strlen(SIGN));
    //LOGI("native中预置的release签名为：%s", SIGN_RELEASE);
    //LOGI("签名长度：%zu",strlen(SIGN_RELEASE));

    int result = strcmp(sign, SIGN);
    int result2 = strcmp(sign, SIGN_RELEASE);
    env->ReleaseStringUTFChars(signature_str, sign);
    env->DeleteLocalRef(signature_str);
    // 使用之后要释放这段内存
    if (result == 0) { // 签名一致
        LOGI("signpassdeb");
        return JNI_OK;
    }
    if (result2 == 0) { // 签名一致
        LOGI("signpassrel");
        return JNI_OK;
    }
    LOGI("signpassn");
    return JNI_ERR;
}