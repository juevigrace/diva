# Desktop App Specific ProGuard Rules

# Keep main entry point
-keepclasseswithmembers class com.diva.app.MainKt {
    public static void main(java.lang.String[]);
}

# Keep Compose runtime Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# Keep Skiko native rendering engine classes
-keep class org.jetbrains.skiko.** { *; }
-keepclassmembers class org.jetbrains.skiko.** { *; }

# Keep SLF4J / Logback logging classes for Desktop
-keep class org.slf4j.** { *; }
-keep class ch.qos.logback.** { *; }

# Ignore unresolved references from optional transitive library dependencies
-ignorewarnings
-dontwarn **
