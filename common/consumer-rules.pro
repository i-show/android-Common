
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