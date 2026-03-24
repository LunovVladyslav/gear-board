# Proguard rules for GearBoard

# Hilt
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.gearboard.data.model.** { *; }
-keep class com.gearboard.domain.model.** { *; }

# Billing
-keep class com.android.vending.billing.** { *; }

# Compose
-dontwarn androidx.compose.**
