import 'package:flutter_test/flutter_test.dart';
import 'package:eng_app/models/message.dart';

void main() {
  group('Message Model Tests', () {
    final testTimestamp = DateTime(2024, 1, 1, 12, 0);

    test('Message should be created with all fields', () {
      final message = Message(
        id: 'msg_123',
        conversationId: 'conv_456',
        text: 'Hello, how are you?',
        isUser: true,
        timestamp: testTimestamp,
      );

      expect(message.id, 'msg_123');
      expect(message.conversationId, 'conv_456');
      expect(message.text, 'Hello, how are you?');
      expect(message.isUser, true);
      expect(message.timestamp, testTimestamp);
    });

    test('Message should serialize to JSON correctly', () {
      final message = Message(
        id: 'msg_123',
        conversationId: 'conv_456',
        text: 'Hello, how are you?',
        isUser: true,
        timestamp: testTimestamp,
      );

      final json = message.toJson();

      expect(json['id'], 'msg_123');
      expect(json['conversationId'], 'conv_456');
      expect(json['text'], 'Hello, how are you?');
      expect(json['isUser'], true);
      expect(json['timestamp'], testTimestamp.toIso8601String());
    });

    test('Message should deserialize from JSON correctly', () {
      final json = {
        'id': 'msg_123',
        'conversationId': 'conv_456',
        'text': 'Hello, how are you?',
        'isUser': true,
        'timestamp': testTimestamp.toIso8601String(),
      };

      final message = Message.fromJson(json);

      expect(message.id, 'msg_123');
      expect(message.conversationId, 'conv_456');
      expect(message.text, 'Hello, how are you?');
      expect(message.isUser, true);
      expect(message.timestamp, testTimestamp);
    });

    test('Message should serialize to database format correctly', () {
      final message = Message(
        id: 'msg_123',
        conversationId: 'conv_456',
        text: 'Hello, how are you?',
        isUser: true,
        timestamp: testTimestamp,
      );

      final dbMap = message.toDatabase();

      expect(dbMap['id'], 'msg_123');
      expect(dbMap['conversationId'], 'conv_456');
      expect(dbMap['text'], 'Hello, how are you?');
      expect(dbMap['isUser'], 1); // Boolean stored as int
      expect(dbMap['timestamp'], testTimestamp.toIso8601String());
    });

    test('Message should deserialize from database format correctly', () {
      final dbMap = {
        'id': 'msg_123',
        'conversationId': 'conv_456',
        'text': 'Hello, how are you?',
        'isUser': 1, // Boolean stored as int
        'timestamp': testTimestamp.toIso8601String(),
      };

      final message = Message.fromDatabase(dbMap);

      expect(message.id, 'msg_123');
      expect(message.conversationId, 'conv_456');
      expect(message.text, 'Hello, how are you?');
      expect(message.isUser, true);
      expect(message.timestamp, testTimestamp);
    });

    test('AI message should have isUser as false', () {
      final message = Message(
        id: 'msg_123',
        conversationId: 'conv_456',
        text: 'I am fine, thank you!',
        isUser: false,
        timestamp: testTimestamp,
      );

      expect(message.isUser, false);

      final dbMap = message.toDatabase();
      expect(dbMap['isUser'], 0); // false stored as 0
    });
  });
}
