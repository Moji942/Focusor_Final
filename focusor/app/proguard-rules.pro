# Keep models and annotations for Room
-keep class androidx.room.** { *; }
-keep class **Database_Impl { *; }
-keep class **Dao_Impl { *; }
-dontwarn androidx.room.**

# Retrofit / OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

# Apache POI
-dontwarn org.apache.xmlbeans.**
-dontwarn org.openxmlformats.**
-dontwarn com.zaxxer.**
-keep class org.apache.poi.** { *; }

# Google API Client
-dontwarn com.google.api.client.**
-dontwarn com.google.api.services.**
-keep class com.google.api.client.** { *; }
-keep class com.google.api.services.** { *; }

# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# Keep generated Hilt classes
-keep class dagger.hilt.internal.** { *; }
-keep class hilt_aggregated_deps.** { *; }

# Kotlin coroutines
-dontwarn kotlinx.coroutines.**