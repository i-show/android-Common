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
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

#支付宝支付
-keep class com.alipay.**{*;}

# 极光推送
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#FastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# Gson
-dontwarn sun.misc.**
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# 百度统计
-keep class com.baidu.kirin.** { *; }
-keep class com.baidu.mobstat.** { *; }
-keep class com.baidu.bottom.** { *; }

# OKHttp
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}
-dontwarn okhttp3.**

-keep class okio.**{*;}
-keep interface okio.**{*;}
-dontwarn okio.**

-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.**

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# Retrofit2
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# ShareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}

-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

# ishow common
-keep class com.ishow.noahark.entries.**{*;}
-keep class com.ishow.common.entries.**{*;}

-keepclassmembers class ** {
    @com.ishow.common.utils.permission.PermissionGranted <methods>;
}
-keepclassmembers class ** {
    @com.ishow.common.utils.permission.PermissionDenied <methods>;
}
# 生成的Binding文件不需要混淆
-keep class **.*Binding {*;}
-keep class **.*BindingImpl {*;}
