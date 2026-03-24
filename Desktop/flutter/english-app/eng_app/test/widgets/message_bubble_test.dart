import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:eng_app/widgets/message_bubble.dart';
import 'package:eng_app/models/message.dart';

void main() {
  group('MessageBubble Widget Tests', () {
    testWidgets('User message should display correctly', (
      WidgetTester tester,
    ) async {
      final message = Message(
        id: 'msg_1',
        conversationId: 'conv_1',
        text: 'Hello, how are you?',
        isUser: true,
        timestamp: DateTime(2024, 1, 1, 12, 0),
      );

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(body: MessageBubble(message: message)),
        ),
      );

      expect(find.text('Hello, how are you?'), findsOneWidget);
      expect(find.text('12:00'), findsOneWidget);
    });

    testWidgets('AI message should display with speaker icon', (
      WidgetTester tester,
    ) async {
      final message = Message(
        id: 'msg_1',
        conversationId: 'conv_1',
        text: 'I am fine, thank you!',
        isUser: false,
        timestamp: DateTime(2024, 1, 1, 12, 5),
      );

      var speakTapped = false;

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: MessageBubble(
              message: message,
              onSpeakTap: () => speakTapped = true,
            ),
          ),
        ),
      );

      expect(find.text('I am fine, thank you!'), findsOneWidget);
      expect(find.byIcon(Icons.volume_up_outlined), findsOneWidget);

      // Tap the speaker icon
      await tester.tap(find.byIcon(Icons.volume_up_outlined));
      await tester.pump();

      expect(speakTapped, true);
    });

    testWidgets('AI message should show volume_up icon when speaking', (
      WidgetTester tester,
    ) async {
      final message = Message(
        id: 'msg_1',
        conversationId: 'conv_1',
        text: 'I am fine, thank you!',
        isUser: false,
        timestamp: DateTime(2024, 1, 1, 12, 5),
      );

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: MessageBubble(
              message: message,
              onSpeakTap: () {},
              isSpeaking: true,
            ),
          ),
        ),
      );

      expect(find.byIcon(Icons.volume_up), findsOneWidget);
    });

    testWidgets('User message should not have speaker icon', (
      WidgetTester tester,
    ) async {
      final message = Message(
        id: 'msg_1',
        conversationId: 'conv_1',
        text: 'Hello!',
        isUser: true,
        timestamp: DateTime(2024, 1, 1, 12, 0),
      );

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(body: MessageBubble(message: message)),
        ),
      );

      expect(find.byIcon(Icons.volume_up_outlined), findsNothing);
      expect(find.byIcon(Icons.volume_up), findsNothing);
    });
  });
}
