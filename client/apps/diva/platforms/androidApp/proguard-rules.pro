# Android App Specific ProGuard Rules

# Keep Android Application & Activity entry points
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity

# Keep ViewModels used by Koin / Jetpack Navigation
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Compose Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
