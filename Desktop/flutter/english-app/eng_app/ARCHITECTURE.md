# Architecture Documentation

This document describes the technical architecture of the English Learning App.

## Overview

The app follows a **layered architecture** with clear separation of concerns, using the **Provider pattern** for state management.

```
┌─────────────────────────────────────────┐
│            UI Layer (Screens)           │
│  - Login Screen                         │
│  - Chat Screen                          │
│  - Conversation History Screen          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│       State Management (Providers)      │
│  - AuthProvider                         │
│  - ChatProvider                         │
│  - ConversationProvider                 │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Service Layer                   │
│  - ApiService (Backend)                 │
│  - DatabaseService (SQLite)             │
│  - SttService (Speech-to-Text)          │
│  - TtsService (Text-to-Speech)          │
│  - AuthService (Authentication)         │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Data Layer (Models)            │
│  - User                                 │
│  - Conversation                         │
│  - Message                              │
└─────────────────────────────────────────┘
```

## Architecture Layers

### 1. UI Layer (Screens & Widgets)

**Responsibility**: Present data and handle user interactions

#### Screens
- **LoginScreen**: User authentication interface
- **ConversationHistoryScreen**: List of past conversations
- **ChatScreen**: Active conversation with voice input

#### Reusable Widgets
- **MessageBubble**: Display individual messages
- **VoiceButton**: Animated microphone button for recording
- **ConversationTile**: Conversation list item with swipe-to-delete

**Key Principles**:
- Screens are stateful widgets that consume providers
- UI logic is minimal; business logic is in providers
- Widgets are reusable and composable

### 2. State Management Layer (Providers)

**Responsibility**: Manage application state and coordinate between UI and services

#### AuthProvider
```dart
class AuthProvider with ChangeNotifier {
  User? _currentUser;
  bool _isLoading = false;
  String? _error;
  
  // Methods: login(), logout(), checkAuthStatus()
}
```

**Manages**:
- Current user state
- Authentication status
- Login/logout operations

#### ChatProvider
```dart
class ChatProvider with ChangeNotifier {
  List<Message> _messages = [];
  bool _isRecording = false;
  String _recognizedText = '';
  
  // Methods: startRecording(), stopRecording(), sendMessage()
}
```

**Manages**:
- Active conversation messages
- Voice recording state
- Speech recognition results
- Message sending and AI responses

#### ConversationProvider
```dart
class ConversationProvider with ChangeNotifier {
  List<Conversation> _conversations = [];
  
  // Methods: loadConversations(), createConversation(), deleteConversation()
}
```

**Manages**:
- Conversation list
- Conversation CRUD operations
- Synchronization with backend

### 3. Service Layer

**Responsibility**: Implement business logic and external integrations

#### ApiService
**Purpose**: Backend API communication

```dart
class ApiService {
  static const String baseUrl = 'https://your-backend-api.com/api';
  
  Future<User> login(String email, String password);
  Future<String> sendMessageToAI(String conversationId, String message);
  Future<void> deleteConversation(String conversationId);
}
```

**Endpoints**:
- `POST /auth/login` - Authentication
- `POST /chat/message` - Send message, receive AI response
- `POST /conversations` - Create conversation
- `GET /conversations/:id/messages` - Sync history
- `DELETE /conversations/:id` - Delete conversation

#### DatabaseService
**Purpose**: Local data persistence using SQLite

```dart
class DatabaseService {
  static final DatabaseService instance = DatabaseService._init();
  
  Future<Conversation> createConversation(Conversation conversation);
  Future<List<Conversation>> getAllConversations();
  Future<Message> createMessage(Message message);
  Future<List<Message>> getMessagesForConversation(String conversationId);
}
```

**Database Schema**:

**conversations** table:
```sql
CREATE TABLE conversations (
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  createdAt TEXT NOT NULL,
  updatedAt TEXT NOT NULL,
  messageCount INTEGER NOT NULL
)
```

**messages** table:
```sql
CREATE TABLE messages (
  id TEXT PRIMARY KEY,
  conversationId TEXT NOT NULL,
  text TEXT NOT NULL,
  isUser INTEGER NOT NULL,
  timestamp TEXT NOT NULL,
  FOREIGN KEY (conversationId) REFERENCES conversations (id) ON DELETE CASCADE
)
```

#### SttService (Speech-to-Text)
**Purpose**: Convert speech to text

```dart
class SttService {
  final SpeechToText _speechToText = SpeechToText();
  
  Future<bool> initialize();
  Future<void> startListening({required Function(String) onResult});
  Future<void> stopListening();
}
```

**Features**:
- Real-time speech recognition
- Partial results support
- Multiple locale support (English variants)
- Error handling

#### TtsService (Text-to-Speech)
**Purpose**: Convert text to speech

```dart
class TtsService {
  final FlutterTts _flutterTts = FlutterTts();
  
  Future<void> initialize();
  Future<void> speak(String text);
  Future<void> stop();
  Future<void> setSpeechRate(double rate);
}
```

**Features**:
- Natural voice synthesis
- Configurable speech rate, pitch, and volume
- Multiple voice options
- Playback controls

#### AuthService
**Purpose**: Authentication and token management

```dart
class AuthService {
  final FlutterSecureStorage _secureStorage = FlutterSecureStorage();
  
  Future<void> saveToken(String token);
  Future<String?> getToken();
  Future<void> deleteToken();
}
```

**Features**:
- Secure token storage
- Persistent authentication
- Token lifecycle management

### 4. Data Layer (Models)

**Responsibility**: Define data structures

#### User Model
```dart
class User {
  final String id;
  final String email;
  final String name;
  final String? token;
  
  User({required this.id, required this.email, required this.name, this.token});
  
  factory User.fromJson(Map<String, dynamic> json);
  Map<String, dynamic> toJson();
}
```

#### Conversation Model
```dart
class Conversation {
  final String id;
  final String title;
  final DateTime createdAt;
  final DateTime updatedAt;
  final int messageCount;
  
  factory Conversation.fromDatabase(Map<String, dynamic> map);
  Map<String, dynamic> toDatabase();
}
```

#### Message Model
```dart
class Message {
  final String id;
  final String conversationId;
  final String text;
  final bool isUser;
  final DateTime timestamp;
  
  factory Message.fromDatabase(Map<String, dynamic> map);
  Map<String, dynamic> toDatabase();
}
```

## Data Flow

### User Login Flow
```
LoginScreen → AuthProvider → ApiService → Backend
                ↓
         AuthService (save token)
                ↓
         Navigate to ConversationHistoryScreen
```

### Voice Message Flow
```
User presses VoiceButton
        ↓
ChatProvider.startRecording()
        ↓
SttService.startListening()
        ↓
User speaks → Speech recognized → ChatProvider updates recognizedText
        ↓
User releases button → ChatProvider.stopRecording()
        ↓
ChatProvider.sendMessage()
        ↓
DatabaseService.createMessage() (save user message)
        ↓
ApiService.sendMessageToAI() (get AI response)
        ↓
DatabaseService.createMessage() (save AI message)
        ↓
TtsService.speak() (read AI response aloud)
        ↓
UI updates with new messages
```

### Conversation History Flow
```
ConversationHistoryScreen loads
        ↓
ConversationProvider.loadConversations()
        ↓
DatabaseService.getAllConversations()
        ↓
Display list of conversations
        ↓
User taps conversation
        ↓
Navigate to ChatScreen
        ↓
ChatProvider.loadConversation()
        ↓
DatabaseService.getMessagesForConversation()
        ↓
Display messages
```

## Design Patterns

### 1. Provider Pattern (State Management)
- Centralized state management
- Reactive UI updates via `notifyListeners()`
- Dependency injection through `Provider.of<T>()` and `Consumer<T>`

### 2. Singleton Pattern
- `DatabaseService.instance` - Single database connection
- Service instances are created once and reused

### 3. Repository Pattern
- `DatabaseService` acts as a repository for local data
- `ApiService` acts as a repository for remote data
- Providers coordinate between repositories

### 4. Factory Pattern
- Model classes use factory constructors for JSON/Database deserialization
- `User.fromJson()`, `Message.fromDatabase()`, etc.

## Error Handling

### Strategy
1. **Service Layer**: Catch exceptions, throw custom exceptions with context
2. **Provider Layer**: Catch exceptions, set error state, notify listeners
3. **UI Layer**: Display error messages to users

### Example
```dart
// Service
Future<User> login(String email, String password) async {
  try {
    final response = await http.post(...);
    if (response.statusCode == 200) {
      return User.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Login failed: ${response.body}');
    }
  } catch (e) {
    throw Exception('Login error: $e');
  }
}

// Provider
Future<bool> login(String email, String password) async {
  _isLoading = true;
  _error = null;
  notifyListeners();
  
  try {
    _currentUser = await _apiService.login(email, password);
    await _authService.saveToken(_currentUser!.token!);
    _isLoading = false;
    notifyListeners();
    return true;
  } catch (e) {
    _error = e.toString();
    _isLoading = false;
    notifyListeners();
    return false;
  }
}
```

## Testing Strategy

### Unit Tests
- Test models (serialization/deserialization)
- Test service methods with mocked dependencies
- Test provider logic

### Widget Tests
- Test individual widgets in isolation
- Test user interactions
- Test state changes

### Integration Tests
- Test complete user flows
- Test navigation
- Test data persistence

## Performance Considerations

### Database Optimization
- Indexed `conversationId` in messages table for faster queries
- Cascade delete for automatic cleanup
- Batch operations where possible

### Memory Management
- Dispose controllers and services in widget `dispose()` methods
- Limit message list size (pagination recommended for production)
- Use `const` constructors where possible

### Network Optimization
- Cache API responses where appropriate
- Implement retry logic for failed requests
- Use connection pooling (http package handles this)

## Security Considerations

### Authentication
- Tokens stored in `FlutterSecureStorage` (encrypted)
- HTTPS for all API communications
- Token expiration handling (to be implemented)

### Data Privacy
- Local database is device-specific
- No sensitive data in logs (using `debugPrint` instead of `print`)
- User data is not shared between devices without backend sync

## Future Enhancements

### Recommended Improvements
1. **Offline Mode**: Queue messages when offline, sync when online
2. **Pagination**: Load messages in chunks for better performance
3. **Push Notifications**: Notify users of new messages
4. **Analytics**: Track user engagement and app usage
5. **Internationalization**: Support multiple languages
6. **Theme Customization**: Dark mode and custom themes
7. **Voice Selection**: Allow users to choose TTS voice
8. **Conversation Export**: Export conversations as PDF/text

---

For implementation details, see the source code in the `lib/` directory.
