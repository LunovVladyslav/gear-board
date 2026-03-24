import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:mockito/annotations.dart';
import 'package:eng_app/providers/chat_provider.dart';
import 'package:eng_app/services/database_service.dart';
import 'package:eng_app/services/api_service.dart';
import 'package:eng_app/services/stt_service.dart';
import 'package:eng_app/services/tts_service.dart';
import 'package:eng_app/models/message.dart';
import 'package:eng_app/models/conversation.dart';

// IMPORTANT: This file requires generated mocks to compile.
// Run: flutter pub run build_runner build
// This will generate: chat_provider_test.mocks.dart
import 'chat_provider_test.mocks.dart';

@GenerateMocks([DatabaseService, ApiService, SttService, TtsService])
void main() {
  group('ChatProvider Tests', () {
    late MockDatabaseService mockDatabaseService;
    late MockApiService mockApiService;
    late MockSttService mockSttService;
    late MockTtsService mockTtsService;
    late ChatProvider chatProvider;

    setUp(() {
      mockDatabaseService = MockDatabaseService();
      mockApiService = MockApiService();
      mockSttService = MockSttService();
      mockTtsService = MockTtsService();
      chatProvider = ChatProvider(
        mockDatabaseService,
        mockApiService,
        mockSttService,
        mockTtsService,
      );
    });

    test('Initial state should be empty', () {
      expect(chatProvider.messages, isEmpty);
      expect(chatProvider.currentConversation, isNull);
      expect(chatProvider.isLoading, false);
      expect(chatProvider.isRecording, false);
      expect(chatProvider.isSpeaking, false);
      expect(chatProvider.error, isNull);
      expect(chatProvider.recognizedText, isEmpty);
    });

    test('LoadConversation should load messages', () async {
      final testConversation = Conversation(
        id: 'conv_123',
        title: 'Test Conversation',
        createdAt: DateTime.now(),
        updatedAt: DateTime.now(),
        messageCount: 2,
      );

      final testMessages = [
        Message(
          id: 'msg_1',
          conversationId: 'conv_123',
          text: 'Hello',
          isUser: true,
          timestamp: DateTime.now(),
        ),
        Message(
          id: 'msg_2',
          conversationId: 'conv_123',
          text: 'Hi there!',
          isUser: false,
          timestamp: DateTime.now(),
        ),
      ];

      when(
        mockDatabaseService.getConversation('conv_123'),
      ).thenAnswer((_) async => testConversation);
      when(
        mockDatabaseService.getMessagesForConversation('conv_123'),
      ).thenAnswer((_) async => testMessages);

      await chatProvider.loadConversation('conv_123');

      expect(chatProvider.currentConversation, testConversation);
      expect(chatProvider.messages.length, 2);
      expect(chatProvider.messages[0].text, 'Hello');
      expect(chatProvider.messages[1].text, 'Hi there!');
      expect(chatProvider.isLoading, false);
      expect(chatProvider.error, isNull);
    });

    test('CreateNewConversation should create conversation', () async {
      final newConversation = Conversation(
        id: '123',
        title: 'New Chat',
        createdAt: DateTime.now(),
        updatedAt: DateTime.now(),
        messageCount: 0,
      );

      when(
        mockDatabaseService.createConversation(any),
      ).thenAnswer((_) async => newConversation);

      await chatProvider.createNewConversation('New Chat');

      expect(chatProvider.currentConversation, isNotNull);
      expect(chatProvider.currentConversation?.title, 'New Chat');
      expect(chatProvider.messages, isEmpty);
      expect(chatProvider.isLoading, false);
      expect(chatProvider.error, isNull);
    });

    test('StartRecording should set recording state', () async {
      when(mockSttService.initialize()).thenAnswer((_) async => true);
      when(
        mockSttService.startListening(onResult: anyNamed('onResult')),
      ).thenAnswer((_) async => {});

      await chatProvider.startRecording();

      expect(chatProvider.isRecording, true);
      expect(chatProvider.error, isNull);
      verify(mockSttService.initialize()).called(1);
    });

    test('SendMessage should create user and AI messages', () async {
      // Setup conversation
      final testConversation = Conversation(
        id: 'conv_123',
        title: 'Test',
        createdAt: DateTime.now(),
        updatedAt: DateTime.now(),
        messageCount: 0,
      );

      when(
        mockDatabaseService.createConversation(any),
      ).thenAnswer((_) async => testConversation);

      await chatProvider.createNewConversation('Test');

      // Setup mocks for message sending
      when(
        mockDatabaseService.createMessage(any),
      ).thenAnswer((invocation) async => invocation.positionalArguments[0]);
      when(
        mockDatabaseService.getConversation('conv_123'),
      ).thenAnswer((_) async => testConversation);
      when(
        mockApiService.sendMessageToAI('conv_123', 'Hello'),
      ).thenAnswer((_) async => 'Hi there!');
      when(mockTtsService.initialize()).thenAnswer((_) async => {});
      when(mockTtsService.speak('Hi there!')).thenAnswer((_) async => {});

      await chatProvider.sendMessage('Hello');

      expect(chatProvider.messages.length, 2);
      expect(chatProvider.messages[0].text, 'Hello');
      expect(chatProvider.messages[0].isUser, true);
      expect(chatProvider.messages[1].text, 'Hi there!');
      expect(chatProvider.messages[1].isUser, false);
      expect(chatProvider.isLoading, false);
      verify(mockApiService.sendMessageToAI('conv_123', 'Hello')).called(1);
    });

    test('SpeakMessage should use TTS service', () async {
      when(mockTtsService.initialize()).thenAnswer((_) async => {});
      when(mockTtsService.speak('Test message')).thenAnswer((_) async => {});

      await chatProvider.speakMessage('Test message');

      verify(mockTtsService.initialize()).called(1);
      verify(mockTtsService.speak('Test message')).called(1);
    });

    test('ClearError should remove error message', () async {
      when(
        mockDatabaseService.getConversation('invalid'),
      ).thenThrow(Exception('Error'));

      await chatProvider.loadConversation('invalid');
      expect(chatProvider.error, isNotNull);

      chatProvider.clearError();
      expect(chatProvider.error, isNull);
    });
  });
}
