# English Learning App 🗣️

A Flutter-based mobile and desktop application for practicing English conversation skills using AI-powered voice chat with Text-to-Speech (TTS) and Speech-to-Text (STT) capabilities.

## ✨ Features

- **Voice-Based Conversations**: Practice English by speaking with an AI assistant
- **Real-time Speech Recognition**: Convert your speech to text instantly
- **Text-to-Speech Playback**: Listen to AI responses with natural voice synthesis
- **Conversation History**: Save and review past conversations
- **Multi-Platform Support**: Works on Android, iOS, Web, macOS, Windows, and Linux
- **Offline Storage**: Local database for conversation persistence
- **User Authentication**: Secure login system with backend integration

## 🏗️ Architecture

The app follows a clean architecture pattern with clear separation of concerns:

- **Models**: Data structures for User, Conversation, and Message
- **Services**: Business logic for API, Database, TTS, STT, and Authentication
- **Providers**: State management using the Provider pattern
- **Screens**: UI components for Login, Chat, and Conversation History
- **Widgets**: Reusable UI components (MessageBubble, VoiceButton, ConversationTile)

## 🚀 Getting Started

### Prerequisites

- **Flutter SDK**: Version 3.10.3 or higher
- **Dart SDK**: Version 3.10.3 or higher
- **Platform-specific requirements**:
  - **Android**: Android Studio with Android SDK (API level 21+)
  - **iOS**: Xcode 14.2+ and CocoaPods
  - **Web**: Chrome browser
  - **Desktop**: Platform-specific build tools

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repository-url>
   cd eng_app
   ```

2. **Install dependencies**
   ```bash
   flutter pub get
   ```

3. **Configure backend URL** (Optional)
   
   Create a `.env` file based on `.env.example` and set your backend API URL:
   ```
   API_BASE_URL=https://your-backend-api.com/api
   ```
   
   Or update directly in `lib/services/api_service.dart` (line 8)

4. **Run the app**
   ```bash
   # For development
   flutter run
   
   # For specific platform
   flutter run -d chrome        # Web
   flutter run -d macos         # macOS
   flutter run -d android       # Android
   flutter run -d ios           # iOS
   ```

## 📱 Platform-Specific Setup

### Android

1. Install Android Studio from https://developer.android.com/studio
2. Set up Android SDK through Android Studio
3. Configure `ANDROID_HOME` environment variable or use:
   ```bash
   flutter config --android-sdk <path-to-android-sdk>
   ```

### iOS

1. Install Xcode from the Mac App Store
2. Install CocoaPods:
   ```bash
   sudo gem install cocoapods
   ```
3. Run pod install:
   ```bash
   cd ios && pod install
   ```

### Web

No additional setup required. Chrome is recommended for development.

### Desktop (macOS/Windows/Linux)

Ensure you have the platform-specific build tools installed. See [Flutter desktop documentation](https://docs.flutter.dev/desktop) for details.

## 📂 Project Structure

```
lib/
├── models/              # Data models
│   ├── conversation.dart
│   ├── message.dart
│   └── user.dart
├── providers/           # State management
│   ├── auth_provider.dart
│   ├── chat_provider.dart
│   └── conversation_provider.dart
├── screens/             # UI screens
│   ├── chat_screen.dart
│   ├── conversation_history_screen.dart
│   └── login_screen.dart
├── services/            # Business logic
│   ├── api_service.dart
│   ├── auth_service.dart
│   ├── database_service.dart
│   ├── stt_service.dart
│   └── tts_service.dart
├── widgets/             # Reusable components
│   ├── conversation_tile.dart
│   ├── message_bubble.dart
│   └── voice_button.dart
└── main.dart           # App entry point
```

## 🛠️ Technologies Used

- **Flutter**: Cross-platform UI framework
- **Provider**: State management
- **SQLite** (sqflite): Local database
- **speech_to_text**: Speech recognition
- **flutter_tts**: Text-to-speech synthesis
- **http**: API communication
- **flutter_secure_storage**: Secure credential storage
- **shared_preferences**: Local preferences storage

## ⚙️ Configuration

### Backend Integration

The app is designed to work with a backend API. Update the base URL in `lib/services/api_service.dart`:

```dart
static const String baseUrl = 'https://your-backend-api.com/api';
```

### API Endpoints Expected

- `POST /auth/login` - User authentication
- `POST /chat/message` - Send message and get AI response
- `POST /conversations` - Create new conversation
- `GET /conversations/:id/messages` - Sync conversation history
- `DELETE /conversations/:id` - Delete conversation

## 🐛 Known Issues

- **Android SDK**: Requires Android Studio installation for Android development
- **iOS**: Requires Xcode 15+ (currently using 14.2)
- **CocoaPods**: Not installed on the development machine (required for iOS)

## 🧪 Testing

Run tests with:
```bash
flutter test
```

Generate mocks for testing:
```bash
./generate_mocks.sh
```

## 📝 License

This project is available for educational and personal use.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📧 Support

For issues and questions, please open an issue in the GitHub repository.

---

**Note**: This app requires microphone permissions for speech recognition. Make sure to grant the necessary permissions when prompted.
