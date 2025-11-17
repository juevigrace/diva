# Diva CLI

A powerful command-line interface tool for managing Diva projects, deployments, and development workflows.

## Description

Diva CLI provides developers with a comprehensive set of commands to interact with the Diva ecosystem, including project initialization, build automation, deployment management, and development utilities.

## Requirements

- **Go**: 1.21 or later
- **Make**: GNU Make 3.8 or later
- **Git**: 2.0 or later

## Installation

### From Source

```bash
git clone <repository-url>
cd diva-cli
make install
```

### Binary Download

Download the appropriate binary for your platform from the releases page.

## Building

### Using Make

```bash
# Build the CLI binary
make build

# Build for all platforms
make build-all

# Clean build artifacts
make clean

# Run tests
make test

# Install to system PATH
make install

# Uninstall from system
make uninstall
```

### Available Make Targets

- `build`: Build for the current platform
- `build-all`: Build for all supported platforms (Linux, macOS, Windows)
- `test`: Run unit and integration tests
- `clean`: Remove build artifacts
- `install`: Install binary to /usr/local/bin
- `uninstall`: Remove binary from system
- `lint`: Run Go linter
- `fmt`: Format Go source code

## Usage

```bash
# Show help
diva --help

# Initialize a new Diva project
diva init my-project
```

## Development

### Project Structure

```
cli/
```

### Adding New Commands

1. Create a new command file in `cmd/`
2. Register the command in the main CLI setup
3. Add tests in the appropriate test files
4. Update documentation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass: `make test`
6. Submit a pull request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
