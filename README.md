# Diva

A multi-platform application framework consisting of client applications, server components, and shared libraries.

## Overview

Diva is a comprehensive software ecosystem that includes:

- **kmp**: Kotlin Multiplatform ecosystem — main app, shared framework, and examples
- **server**: Backend services and API (Go)
- **web**: Website and web libraries (Astro/TypeScript)

## Project Structure

```
diva/
├── kmp/          # Kotlin Multiplatform
│   ├── app/      # Main KMP application (Android, iOS, Desktop)
│   │   ├── apps/     # Platform entry points
│   │   │   ├── androidApp/  # Android application
│   │   │   └── desktopApp/  # Desktop application
│   │   ├── build-logic/     # Build configuration
│   │   ├── core/            # Core modules
│   │   │   ├── database/    # Database module
│   │   │   ├── models/      # Type definitions
│   │   │   └── ui/          # UI primitives
│   │   ├── features/        # Feature modules
│   │   │   ├── app/         # App shell features
│   │   │   ├── auth/        # Authentication
│   │   │   ├── permission/  # Permission handling
│   │   │   ├── user/        # User features
│   │   │   └── verification/# Verification
│   │   ├── gradle/          # Gradle wrapper and configuration
│   │   └── sharedUI/        # Shared Compose UI components
│   ├── examples/ # Example applications
│   │   └── diva-kmp-app/    # Example KMP application
│   └── framework/# Shared Kotlin framework
│       ├── build-logic/         # Build configuration
│       ├── diva-core/           # Core framework module
│       ├── diva-database/       # Database components
│       ├── diva-network-client/ # Network client
│       ├── diva-ui/             # UI components
│       └── *-test/              # Test support modules
├── server/       # Backend server (Go)
│   ├── cmd/      # Entrypoints
│   ├── internal/ # Internal packages
│   ├── pkg/      # Reusable packages
│   ├── server/   # Server bootstrap
│   └── storage/  # Storage drivers
├── web/          # Website and web libraries
│   ├── app-page/ # App website
│   └── lib/      # Shared website libraries
│       ├── diva-types/ # Type definitions
│       └── diva-ui/    # UI components
├── LICENSE
└── README.md
```

## Getting Started

Each subproject has its own README with specific build and installation instructions. Navigate to the respective directories to learn more about each component.

## License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please read the contributing guidelines in each subproject's README.

## Author

juevigrace
