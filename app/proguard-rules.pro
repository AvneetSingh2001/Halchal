# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepnames class com.avicodes.halchalin.data.models.Admin
-keepnames class com.avicodes.halchalin.data.models.ads
-keepnames class com.avicodes.halchalin.data.models.Article
-keepnames class com.avicodes.halchalin.data.models.ArticleProcessed
-keepnames class com.avicodes.halchalin.data.models.Categories
-keepnames class com.avicodes.halchalin.data.models.City
-keepnames class com.avicodes.halchalin.data.models.Comment
-keepnames class com.avicodes.halchalin.data.models.CommentProcessed
-keepnames class com.avicodes.halchalin.data.models.ExoPlayerItem
-keepnames class com.avicodes.halchalin.data.models.Featured
-keepnames class com.avicodes.halchalin.data.models.News
-keepnames class com.avicodes.halchalin.data.models.NewsLocal
-keepnames class com.avicodes.halchalin.data.models.NewsRemote
-keepnames class com.avicodes.halchalin.data.models.NewsResponse
-keepnames class com.avicodes.halchalin.data.models.ResUrls
-keepnames class com.avicodes.halchalin.data.models.TopAds
-keepnames class com.avicodes.halchalin.data.models.User