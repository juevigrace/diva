# Kotlin
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Serialization
-keepattributes SerialName, Serializable
-dontwarn kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.diva.**$$serializer { *; }
-keepclassmembers class com.diva.** {
    *** Companion;
}
-keepclasseswithmembers class com.diva.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# SQLDelight
-keep class app.cash.sqldelight.** { *; }
-dontwarn app.cash.sqldelight.**

# Compose
-dontwarn androidx.compose.**

# Window Manager
-dontwarn androidx.window.**
-dontwarn androidx.window.extensions.**
-dontwarn androidx.window.sidecar.**
