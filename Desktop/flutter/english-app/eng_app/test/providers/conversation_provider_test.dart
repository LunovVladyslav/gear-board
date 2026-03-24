import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:mockito/annotations.dart';
import 'package:eng_app/providers/conversation_provider.dart';
import 'package:eng_app/services/database_service.dart';
import 'package:eng_app/services/api_service.dart';
import 'package:eng_app/models/conversation.dart';

// IMPORTANT: This file requires generated mocks to compile.
// Run: flutter pub run build_runner build
// This will generate: conversation_provider_test.mocks.dart
import 'conversation_provider_test.mocks.dart';

@GenerateMocks([DatabaseService, ApiService])
void main() {
  group('ConversationProvider Tests', () {
    late MockDatabaseService mockDatabaseService;
    late MockApiService mockApiService;
    late ConversationProvider conversationProvider;

    setUp(() {
      mockDatabaseService = MockDatabaseService();
      mockApiService = MockApiService();
      conversationProvider = ConversationProvider(
        mockDatabaseService,
        mockApiService,
      );
    });

    test('Initial state should be empty', () {
      expect(conversationProvider.conversations, isEmpty);
      expect(conversationProvider.isLoading, false);
      expect(conversationProvider.error, isNull);
    });

    test('LoadConversations should populate conversations list', () async {
      final testConversations = [
        Conversation(
          id: '1',
          title: 'Conversation 1',
          createdAt: DateTime.now(),
          updatedAt: DateTime.now(),
          messageCount: 5,
        ),
        Conversation(
          id: '2',
          title: 'Conversation 2',
          createdAt: DateTime.now(),
          updatedAt: DateTime.now(),
          messageCount: 3,
        ),
      ];

      when(
        mockDatabaseService.getAllConversations(),
      ).thenAnswer((_) async => testConversations);

      await conversationProvider.loadConversations();

      expect(conversationProvider.conversations.length, 2);
      expect(conversationProvider.conversations[0].title, 'Conversation 1');
      expect(conversationProvider.conversations[1].title, 'Conversation 2');
      expect(conversationProvider.isLoading, false);
      expect(conversationProvider.error, isNull);
      verify(mockDatabaseService.getAllConversations()).called(1);
    });

    test('LoadConversations with error should set error state', () async {
      when(
        mockDatabaseService.getAllConversations(),
      ).thenThrow(Exception('Database error'));

      await conversationProvider.loadConversations();

      expect(conversationProvider.conversations, isEmpty);
      expect(conversationProvider.isLoading, false);
      expect(conversationProvider.error, isNotNull);
      expect(conversationProvider.error, contains('Database error'));
    });

    test('CreateConversation should add new conversation', () async {
      final newConversation = Conversation(
        id: '123',
        title: 'New Conversation',
        createdAt: DateTime.now(),
        updatedAt: DateTime.now(),
        messageCount: 0,
      );

      when(
        mockDatabaseService.createConversation(any),
      ).thenAnswer((_) async => newConversation);

      final result = await conversationProvider.createConversation(
        'New Conversation',
      );

      expect(result, isNotNull);
      expect(result?.title, 'New Conversation');
      expect(conversationProvider.conversations.length, 1);
      expect(conversationProvider.conversations[0].title, 'New Conversation');
      expect(conversationProvider.isLoading, false);
      expect(conversationProvider.error, isNull);
    });

    test('CreateConversation with error should return null', () async {
      when(
        mockDatabaseService.createConversation(any),
      ).thenThrow(Exception('Create error'));

      final result = await conversationProvider.createConversation(
        'New Conversation',
      );

      expect(result, isNull);
      expect(conversationProvider.conversations, isEmpty);
      expect(conversationProvider.isLoading, false);
      expect(conversationProvider.error, isNotNull);
    });

    test('DeleteConversation should remove conversation from list', () async {
      // Setup initial conversations
      final testConversations = [
        Conversation(
          id: '1',
          title: 'Conversation 1',
          createdAt: DateTime.now(),
          updatedAt: DateTime.now(),
          messageCount: 5,
        ),
        Conversation(
          id: '2',
          title: 'Conversation 2',
          createdAt: DateTime.now(),
          updatedAt: DateTime.now(),
          messageCount: 3,
        ),
      ];

      when(
        mockDatabaseService.getAllConversations(),
      ).thenAnswer((_) async => testConversations);

      await conversationProvider.loadConversations();
      expect(conversationProvider.conversations.length, 2);

      // Delete conversation
      when(
        mockDatabaseService.deleteConversation('1'),
      ).thenAnswer((_) async => 1);
      when(mockApiService.deleteConversation('1')).thenAnswer((_) async => {});

      await conversationProvider.deleteConversation('1');

      expect(conversationProvider.conversations.length, 1);
      expect(conversationProvider.conversations[0].id, '2');
      expect(conversationProvider.error, isNull);
      verify(mockDatabaseService.deleteConversation('1')).called(1);
    });

    test('ClearError should remove error message', () async {
      when(
        mockDatabaseService.getAllConversations(),
      ).thenThrow(Exception('Error'));

      await conversationProvider.loadConversations();
      expect(conversationProvider.error, isNotNull);

      conversationProvider.clearError();
      expect(conversationProvider.error, isNull);
    });
  });
}
