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

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Apache POI
-keep class org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-dontwarn org.apache.poi.**
-dontwarn org.apache.xmlbeans.**

# Google Drive API
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.api.client.** { *; }
-dontwarn com.google.api.**

# Persian Date Picker
-keep class com.mohamadamin.persianmaterialdatetimepicker.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }

# WorkManager
-keep class androidx.work.** { *; }

# Biometric
-keep class androidx.biometric.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** {
    *;
}

# Material Design
-keep class com.google.android.material.** { *; }

# Navigation
-keep class androidx.navigation.** { *; }

# ViewModel & LiveData
-keep class androidx.lifecycle.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep custom models
-keep class com.focusor.app.data.model.** { *; }
-keep class com.focusor.app.domain.model.** { *; }

# Keep database entities
-keep class com.focusor.app.data.local.entity.** { *; }

# Keep API responses
-keep class com.focusor.app.data.remote.dto.** { *; }

# Keep utility classes
-keep class com.focusor.app.util.** { *; }

# Keep extensions
-keep class com.focusor.app.extensions.** { *; }