# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Environment\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include uri and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keepattributes Signature, InnerClasses, EnclosingMethod

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep class com.google.android.material.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-keep class androidx.** {*;}
-keep interface androidx.** {*;}
-keep public class * extends androidx.**
-dontwarn androidx.**

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

#忽略警告
#未混淆的类和成员
-printseeds ../out/confuse/seeds.txt
#列出从 apk 中删除的代码
-printusage ../out/confuse/unused.txt
#混淆前后的映射
-printmapping ../out/confuse/mapping.txt

# ishow common
-keep class com.ishow.noah.entries.**{*;}
-keep class com.ishow.noah.modules.sample.entries.**{*;}

# 生成的Binding文件不需要混淆
-keep class **.*Binding {*;}
-keep class **.*BindingImpl {*;}
