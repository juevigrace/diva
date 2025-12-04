# Diva

A multi-platform application framework consisting of client applications, server components, and shared libraries.

## Overview

Diva is a comprehensive software ecosystem that includes:

- **Client Applications**: Cross-platform mobile and desktop apps with Compose UI
  - **composeApp**: Main Kotlin Multiplatform application
  - **iosApp**: Native iOS application
  - **webApp**: Web application
- **Project Pages**: Main website showcasing projects and containing documentation
- **CLI Tools**: Command-line utilities for development and deployment
- **Server**: Backend services and API
- **Framework**: Shared Kotlin libraries and components

## Project Structure

```
diva/
├── clients/
│   ├── app/          # Kotlin Multiplatform application
│   │   └── apps/
│   │       ├── composeApp/    # Main Compose application
│   │       ├── iosApp/        # iOS application
│   │       └── webApp/        # Web application
│   └── pages/         # Main project website and documentation
│       └── docs/      # Documentation (moved from root)
├── cli/              # Go command-line interface
├── server/           # Go backend server
├── testing/          # Testing suite
├── framework-kt/     # Kotlin framework
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

