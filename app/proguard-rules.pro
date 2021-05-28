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

# Requred by Jollyday library to prevent warnings (as mentioned on Jollyday website)
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Requred by Jollyday library for APK minify
-keep class de.galgtonold.jollydayandroid.impl.XMLManager.** { *; }
-keep interface de.galgtonold.jollydayandroid.impl.XMLManager.** { *; }

-keep class de.galgtonold.jollydayandroid.impl.** { *; }
-keep interface de.galgtonold.jollydayandroid.impl.** { *; }

-keep class de.galgtonold.jollydayandroid.** { *; }
-keep interface de.galgtonold.jollydayandroid.** { *; }


-keep class de.galgtonold.jollydayandroid.util.XMLUtil.** { *; }
-keep interface de.galgtonold.jollydayandroid.util.XMLUtil.** { *; }

-keep class de.galgtonold.jollydayandroid.util.** { *; }
-keep interface de.galgtonold.jollydayandroid.util.** { *; }


-keep class javax.xml.stream.** { *; }
-keep interface javax.xml.stream.** { *; }

-keep class javax.xml.stream.events.** { *; }
-keep interface javax.xml.stream.events.** { *; }


-keep class org.simpleframework.xml.** { *; }
-keep interface org.simpleframework.xml.** { *; }

-keep class org.simpleframework.xml.core.** { *; }
-keep interface org.simpleframework.xml.core.** { *; }

-keep class org.simpleframework.xml.stream.** { *; }
-keep interface org.simpleframework.xml.stream.** { *; }


-keep class cz.jaro.alarmmorning.** { *; }

# Remove log messages
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
}
#  public static *** i(...);
#  public static *** w(...);
#  public static *** e(...);