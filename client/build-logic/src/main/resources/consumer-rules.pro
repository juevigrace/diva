# Kotlin
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }

# Koin
-keep class org.koin.** { *; }

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Serialization
-keepattributes SerialName, Serializable
-keepclassmembers class kotlinx.serialization.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class com.diva.** {
    *** Companion;
}
-keepclasseswithmembers class com.diva.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# SQLDelight
-keep class app.cash.sqldelight.** { *; }
