# Diva App

A Kotlin Multiplatform application that provides cross-platform functionality for Android, iOS, and Desktop platforms.

## Description

Diva App is the main multiplatform client for the Diva ecosystem, built using Kotlin Multiplatform to share business logic between Android, iOS, and Desktop platforms while maintaining native UI performance.

## Requirements

- **Android**: Android SDK 21+ (Android 5.0)
- **iOS**: iOS 12.0+
- **Desktop**: Windows 10+, macOS 10.15+, or Linux (Ubuntu 18.04+)
- **Kotlin**: 1.9.0+
- **Android Studio**: Latest stable version
- **Xcode**: Latest stable version (for iOS development)

## Building

### Using Gradle

```bash
# Build all platforms
./gradlew build

# Build Android debug APK
./gradlew assembleDebug

# Build Android release APK
./gradlew assembleRelease

# Build iOS framework (requires macOS)
./gradlew embedAndSignAppleFrameworkForXcode

# Build Desktop application
./gradlew createDistributable
```

### Running the Application

#### Android
1. Open the project in Android Studio
2. Select an Android device or emulator
3. Click Run or use `./gradlew installDebug`

#### iOS
1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select a target device or simulator
3. Click Run or use Cmd+R

#### Desktop
1. Build the application: `./gradlew createDistributable`
2. Navigate to `build/compose/binaries/main/app/`
3. Run the executable for your platform:
   - Windows: `app.exe`
   - macOS: `app`
   - Linux: `app`

## Development

### Project Structure

```
app/
```

### Common Commands

```bash
# Clean build
./gradlew clean

# Run tests
./gradlew test

# Generate documentation
./gradlew dokkaHtml
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.