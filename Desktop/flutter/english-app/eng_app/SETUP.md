# Detailed Setup Guide

This guide provides step-by-step instructions for setting up the English Learning App development environment on different platforms.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Flutter Installation](#flutter-installation)
- [Platform Setup](#platform-setup)
  - [Android](#android-setup)
  - [iOS](#ios-setup)
  - [Web](#web-setup)
  - [Desktop](#desktop-setup)
- [Project Setup](#project-setup)
- [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Software

- **Git**: Version control system
- **Flutter SDK**: 3.10.3 or higher
- **Dart SDK**: Included with Flutter
- **Code Editor**: VS Code, Android Studio, or IntelliJ IDEA

### Verify Flutter Installation

```bash
flutter doctor -v
```

This command checks your environment and displays a report of the status of your Flutter installation.

## Flutter Installation

If Flutter is not installed:

1. **Download Flutter SDK**
   - Visit https://docs.flutter.dev/get-started/install
   - Choose your operating system
   - Download the Flutter SDK

2. **Extract and Add to PATH**
   ```bash
   # macOS/Linux
   export PATH="$PATH:`pwd`/flutter/bin"
   
   # Add to ~/.zshrc or ~/.bashrc for persistence
   echo 'export PATH="$PATH:/path/to/flutter/bin"' >> ~/.zshrc
   ```

3. **Run Flutter Doctor**
   ```bash
   flutter doctor
   ```

## Platform Setup

### Android Setup

#### 1. Install Android Studio

Download from https://developer.android.com/studio

#### 2. Install Android SDK

During Android Studio installation:
- Select "Standard" installation type
- Accept all licenses
- Wait for SDK components to download

#### 3. Configure Android SDK

**Option A: Automatic (Recommended)**
```bash
# Android Studio will set this automatically
```

**Option B: Manual**
```bash
# Set environment variable
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
export ANDROID_HOME=$HOME/Android/Sdk          # Linux

# Add to PATH
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

#### 4. Create Android Emulator

1. Open Android Studio
2. Go to Tools → Device Manager
3. Click "Create Device"
4. Select a device (e.g., Pixel 5)
5. Download a system image (API 33 recommended)
6. Finish setup

#### 5. Verify Android Setup

```bash
flutter doctor --android-licenses  # Accept all licenses
flutter doctor                      # Should show ✓ for Android
```

### iOS Setup

> **Note**: iOS development requires macOS

#### 1. Install Xcode

Download from Mac App Store (requires macOS 12.5+)

#### 2. Install Xcode Command Line Tools

```bash
sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
sudo xcodebuild -runFirstLaunch
```

#### 3. Install CocoaPods

```bash
sudo gem install cocoapods
```

Or using Homebrew:
```bash
brew install cocoapods
```

#### 4. Setup iOS Simulator

```bash
open -a Simulator
```

#### 5. Verify iOS Setup

```bash
flutter doctor  # Should show ✓ for Xcode
```

### Web Setup

#### 1. Install Chrome

Download from https://www.google.com/chrome/

#### 2. Enable Web Support

```bash
flutter config --enable-web
```

#### 3. Verify Web Setup

```bash
flutter devices  # Should show Chrome
```

### Desktop Setup

#### macOS

```bash
flutter config --enable-macos-desktop
```

#### Windows

```bash
flutter config --enable-windows-desktop
```

Requirements:
- Visual Studio 2022 or later with "Desktop development with C++" workload

#### Linux

```bash
flutter config --enable-linux-desktop
```

Requirements:
```bash
sudo apt-get install clang cmake ninja-build pkg-config libgtk-3-dev
```

## Project Setup

### 1. Clone Repository

```bash
git clone <your-repository-url>
cd eng_app
```

### 2. Install Dependencies

```bash
flutter pub get
```

### 3. Configure Backend (Optional)

Create `.env` file:
```bash
cp .env.example .env
```

Edit `.env`:
```
API_BASE_URL=https://your-backend-api.com/api
```

### 4. Run the App

```bash
# List available devices
flutter devices

# Run on specific device
flutter run -d <device-id>

# Examples:
flutter run -d chrome          # Web
flutter run -d macos           # macOS
flutter run -d emulator-5554   # Android emulator
flutter run -d "iPhone 14"     # iOS simulator
```

## Troubleshooting

### Common Issues

#### "SDK location not found" (Android)

**Solution**:
```bash
# Create local.properties file
echo "sdk.dir=$HOME/Library/Android/sdk" > android/local.properties  # macOS
echo "sdk.dir=$HOME/Android/Sdk" > android/local.properties          # Linux
```

Or set environment variable:
```bash
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
export ANDROID_HOME=$HOME/Android/Sdk          # Linux
```

#### "CocoaPods not installed" (iOS)

**Solution**:
```bash
sudo gem install cocoapods
cd ios
pod install
cd ..
```

#### "Unable to locate Android SDK"

**Solution**:
```bash
flutter config --android-sdk /path/to/android/sdk
```

#### Microphone Permission Issues

**Android**: Add to `android/app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

**iOS**: Already configured in `ios/Runner/Info.plist`:
```xml
<key>NSMicrophoneUsageDescription</key>
<string>This app needs microphone access for speech recognition</string>
```

#### Build Failures

**Clean and rebuild**:
```bash
flutter clean
flutter pub get
flutter run
```

#### Gradle Issues (Android)

```bash
cd android
./gradlew clean
cd ..
flutter clean
flutter pub get
```

### Getting Help

1. **Check Flutter Doctor**
   ```bash
   flutter doctor -v
   ```

2. **Check Logs**
   ```bash
   flutter run --verbose
   ```

3. **Flutter Documentation**
   - https://docs.flutter.dev/
   - https://flutter.dev/community

4. **Common Issues**
   - https://docs.flutter.dev/testing/common-errors

## Performance Tips

### Development Mode

```bash
# Hot reload: Press 'r' in terminal
# Hot restart: Press 'R' in terminal
# Quit: Press 'q' in terminal
```

### Release Build

```bash
# Android
flutter build apk --release

# iOS
flutter build ios --release

# Web
flutter build web --release

# Desktop
flutter build macos --release
flutter build windows --release
flutter build linux --release
```

## Next Steps

After successful setup:

1. Read [ARCHITECTURE.md](ARCHITECTURE.md) to understand the codebase
2. Review [README.md](README.md) for project overview
3. Start developing!

---

**Need help?** Open an issue on GitHub or check the Flutter documentation.
