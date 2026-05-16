# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /data/data/com.termux/files/home/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools-proguard.html

# Add any project specific keep options here:

# Keep Room generated code
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Dao
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.TypeConverter

# Keep Navigation Component
-keep class androidx.navigation.fragment.NavHostFragment { *; }

# Keep SQLCipher
-keep class net.zetetic.database.sqlcipher.** { *; }
-keep class net.zetetic.database.** { *; }

# Keep Kotlin
-keep class kotlin.Metadata { *; }
-keepclassmembers class ** {
    @org.jetbrains.annotations.Nullable <fields>;
    @org.jetbrains.annotations.NotNull <fields>;
}

# Keep View Binding
-keep class com.caretracker.databinding.** { *; }
