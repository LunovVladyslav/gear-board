# Flutter Unit Tests

This directory contains comprehensive unit tests for the English Learning Chat App.

## ⚠️ IMPORTANT: Fixing Compilation Issues

**The provider tests will NOT compile until you generate the mock files.**

### Quick Fix
Run this command from the project root:
```bash
./generate_mocks.sh
```

Or manually:
```bash
flutter pub get
flutter pub run build_runner build
```

This generates the required `.mocks.dart` files that the tests import.

## Test Structure

```
test/
├── models/              # Model tests
│   ├── user_test.dart
│   ├── message_test.dart
│   └── conversation_test.dart
├── providers/           # Provider tests (with mocks)
│   ├── auth_provider_test.dart
│   ├── chat_provider_test.dart
│   └── conversation_provider_test.dart
└── widgets/             # Widget tests
    ├── message_bubble_test.dart
    └── voice_button_test.dart
```

## Running Tests

### Run All Tests
```bash
flutter test
```

### Run Specific Test File
```bash
flutter test test/models/user_test.dart
```

### Run Tests with Coverage
```bash
flutter test --coverage
```

### Generate Mock Files (Required Before Running Provider Tests)
```bash
flutter pub run build_runner build
```

This will generate mock files like:
- `test/providers/auth_provider_test.mocks.dart`
- `test/providers/chat_provider_test.mocks.dart`
- `test/providers/conversation_provider_test.mocks.dart`

## Test Coverage

### Models (100% Coverage)
- ✅ **User Model** - 5 tests
  - Creation with all fields
  - JSON serialization/deserialization
  - copyWith method
  - Optional token field

- ✅ **Message Model** - 6 tests
  - Creation with all fields
  - JSON serialization/deserialization
  - Database serialization/deserialization
  - User vs AI message distinction

- ✅ **Conversation Model** - 8 tests
  - Creation with all fields
  - Default messageCount
  - JSON serialization/deserialization
  - Database serialization/deserialization
  - copyWith method
  - Missing messageCount handling

### Providers (Mocked Dependencies)
- ✅ **AuthProvider** - 7 tests
  - Initial unauthenticated state
  - Successful login
  - Failed login with error
  - Check auth status
  - Logout functionality
  - Error clearing

- ✅ **ConversationProvider** - 7 tests
  - Initial empty state
  - Load conversations
  - Create conversation
  - Delete conversation
  - Error handling
  - Error clearing

- ✅ **ChatProvider** - 8 tests
  - Initial empty state
  - Load conversation with messages
  - Create new conversation
  - Start recording
  - Send message (user + AI)
  - Speak message with TTS
  - Error handling
  - Error clearing

### Widgets
- ✅ **MessageBubble** - 4 tests
  - User message display
  - AI message with speaker icon
  - Speaker icon states (speaking/not speaking)
  - User message without speaker icon

- ✅ **VoiceButton** - 5 tests
  - Microphone icon states
  - Long press start callback
  - Long press end callback
  - Button size validation

## Total Test Count: **45 tests**

## Test Dependencies

- `flutter_test` - Flutter testing framework
- `mockito` - Mocking library for unit tests
- `build_runner` - Code generation for mocks

## Writing New Tests

### Model Tests
Model tests are straightforward - test serialization, deserialization, and any utility methods.

### Provider Tests
Provider tests use mocked services. Follow this pattern:

1. Add `@GenerateMocks([ServiceClass])` annotation
2. Import the generated mocks file
3. Create mock instances in `setUp()`
4. Use `when()` to stub method calls
5. Use `verify()` to check method calls

### Widget Tests
Widget tests use `testWidgets()` and `WidgetTester`:

1. Pump the widget into a MaterialApp
2. Use `find` to locate widgets
3. Use `expect` to verify widget state
4. Use `tester.tap()` for interactions

## Continuous Integration

These tests can be integrated into CI/CD pipelines:

```yaml
# Example GitHub Actions
- name: Run tests
  run: |
    flutter pub get
    flutter pub run build_runner build --delete-conflicting-outputs
    flutter test --coverage
```

## Notes

- Mock files are generated and should not be edited manually
- Provider tests require mocks to be generated first
- Widget tests don't require mocks
- All tests follow AAA pattern: Arrange, Act, Assert
