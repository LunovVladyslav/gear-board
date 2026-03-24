import 'package:flutter_test/flutter_test.dart';
import 'package:eng_app/models/conversation.dart';

void main() {
  group('Conversation Model Tests', () {
    final createdAt = DateTime(2024, 1, 1, 10, 0);
    final updatedAt = DateTime(2024, 1, 1, 12, 0);

    test('Conversation should be created with all fields', () {
      final conversation = Conversation(
        id: 'conv_123',
        title: 'English Practice',
        createdAt: createdAt,
        updatedAt: updatedAt,
        messageCount: 5,
      );

      expect(conversation.id, 'conv_123');
      expect(conversation.title, 'English Practice');
      expect(conversation.createdAt, createdAt);
      expect(conversation.updatedAt, updatedAt);
      expect(conversation.messageCount, 5);
    });

    test('Conversation should have default messageCount of 0', () {
      final conversation = Conversation(
        id: 'conv_123',
        title: 'English Practice',
        createdAt: createdAt,
        updatedAt: updatedAt,
      );

      expect(conversation.messageCount, 0);
    });

    test('Conversation should serialize to JSON correctly', () {
      final conversation = Conversation(
        id: 'conv_123',
        title: 'English Practice',
        createdAt: createdAt,
        updatedAt: updatedAt,
        messageCount: 5,
      );

      final json = conversation.toJson();

      expect(json['id'], 'conv_123');
      expect(json['title'], 'English Practice');
      expect(json['createdAt'], createdAt.toIso8601String());
      expect(json['updatedAt'], updatedAt.toIso8601String());
      expect(json['messageCount'], 5);
    });

    test('Conversation should deserialize from JSON correctly', () {
      final json = {
        'id': 'conv_123',
        'title': 'English Practice',
        'createdAt': createdAt.toIso8601String(),
        'updatedAt': updatedAt.toIso8601String(),
        'messageCount': 5,
      };

      final conversation = Conversation.fromJson(json);

      expect(conversation.id, 'conv_123');
      expect(conversation.title, 'English Practice');
      expect(conversation.createdAt, createdAt);
      expect(conversation.updatedAt, updatedAt);
      expect(conversation.messageCount, 5);
    });

    test('Conversation should serialize to database format correctly', () {
      final conversation = Conversation(
        id: 'conv_123',
        title: 'English Practice',
        createdAt: createdAt,
        updatedAt: updatedAt,
        messageCount: 5,
      );

      final dbMap = conversation.toDatabase();

      expect(dbMap['id'], 'conv_123');
      expect(dbMap['title'], 'English Practice');
      expect(dbMap['createdAt'], createdAt.toIso8601String());
      expect(dbMap['updatedAt'], updatedAt.toIso8601String());
      expect(dbMap['messageCount'], 5);
    });

    test('Conversation should deserialize from database format correctly', () {
      final dbMap = {
        'id': 'conv_123',
        'title': 'English Practice',
        'createdAt': createdAt.toIso8601String(),
        'updatedAt': updatedAt.toIso8601String(),
        'messageCount': 5,
      };

      final conversation = Conversation.fromDatabase(dbMap);

      expect(conversation.id, 'conv_123');
      expect(conversation.title, 'English Practice');
      expect(conversation.createdAt, createdAt);
      expect(conversation.updatedAt, updatedAt);
      expect(conversation.messageCount, 5);
    });

    test('Conversation copyWith should update only specified fields', () {
      final conversation = Conversation(
        id: 'conv_123',
        title: 'English Practice',
        createdAt: createdAt,
        updatedAt: updatedAt,
        messageCount: 5,
      );

      final newUpdatedAt = DateTime(2024, 1, 1, 14, 0);
      final updatedConversation = conversation.copyWith(
        messageCount: 10,
        updatedAt: newUpdatedAt,
      );

      expect(updatedConversation.id, 'conv_123');
      expect(updatedConversation.title, 'English Practice');
      expect(updatedConversation.createdAt, createdAt);
      expect(updatedConversation.updatedAt, newUpdatedAt);
      expect(updatedConversation.messageCount, 10);
    });

    test('Conversation should handle missing messageCount in JSON', () {
      final json = {
        'id': 'conv_123',
        'title': 'English Practice',
        'createdAt': createdAt.toIso8601String(),
        'updatedAt': updatedAt.toIso8601String(),
      };

      final conversation = Conversation.fromJson(json);

      expect(conversation.messageCount, 0);
    });
  });
}
