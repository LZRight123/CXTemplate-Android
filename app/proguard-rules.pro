
# ------------------------ 三方sdk混淆规则 begin----------------------
# - 友盟统计
-keep class com.umeng.** {*;}
-keep class org.repackage.** {*;}
-keep class com.uyumao.** { *; }
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.fantasy.midbook.R$*{
public static final int *;
}

# - bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# - 协程 Coroutines for coil
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal
-dontwarn java.lang.ClassValue
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# ------------------------ 三方sdk混淆规则 end----------------------

# 7. google auth
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
