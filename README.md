# Diva

A multi-platform application framework consisting of client applications, server components, and shared libraries.

## Overview

Diva is a comprehensive software ecosystem that includes:

- **Client Applications**: Cross-platform mobile and desktop apps with Compose UI
  - **app**: Main Kotlin Multiplatform application with Android, iOS, Desktop, and Web targets
  - **pages**: Project website and documentation pages
- **CLI Tools**: Command-line utilities for development and deployment
- **Server**: Backend services and API
- **Framework**: Shared Kotlin libraries and components
- **Testing**: Comprehensive testing suite

## Project Structure

```
diva/
├── app/              # Kotlin Multiplatform application
│   ├── apps/
│   │   ├── androidApp/    # Android application
│   │   ├── desktopApp/    # Desktop application
│   │   ├── iosApp/        # iOS application
│   │   ├── sharedUI/      # Shared UI components
│   │   └── webApp/        # Web application
│   ├── build-logic/       # Build configuration
│   ├── core/             # Core modules
│   │   ├── database/      # Database module
│   │   └── types/         # Type definitions
│   └── gradle/           # Gradle wrapper and configuration
├── cli/              # Command-line interface
├── pages/            # Project website and documentation
│   ├── docs-page/    # Documentation pages
│   └── landing-page/ # Landing page
├── server/           # Backend server
├── testing/          # Testing suite
├── framework-kt/     # Kotlin framework
│   ├── build-logic/  # Build configuration
│   ├── core/         # Core framework modules
│   ├── database/     # Database components
│   ├── di/           # Dependency injection
│   ├── network-client/ # Network client
│   └── ui/           # UI components
├── LICENSE
├── README.md
└── .gitmodules       # Git submodule configuration
```

## Getting Started

Each subproject has its own README with specific build and installation instructions. Navigate to the respective directories to learn more about each component.

## License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please read the contributing guidelines in each subproject's README.

## Author

juevigrace

