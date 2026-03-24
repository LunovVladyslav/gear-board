import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:eng_app/widgets/voice_button.dart';

void main() {
  group('VoiceButton Widget Tests', () {
    testWidgets('Should display microphone icon when not recording', (
      WidgetTester tester,
    ) async {
      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: VoiceButton(
              onStartRecording: () {},
              onStopRecording: () {},
              isRecording: false,
            ),
          ),
        ),
      );

      expect(find.byIcon(Icons.mic_none), findsOneWidget);
      expect(find.byIcon(Icons.mic), findsNothing);
    });

    testWidgets('Should display filled microphone icon when recording', (
      WidgetTester tester,
    ) async {
      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: VoiceButton(
              onStartRecording: () {},
              onStopRecording: () {},
              isRecording: true,
            ),
          ),
        ),
      );

      expect(find.byIcon(Icons.mic), findsOneWidget);
      expect(find.byIcon(Icons.mic_none), findsNothing);
    });

    testWidgets('Should call onStartRecording on long press start', (
      WidgetTester tester,
    ) async {
      var startCalled = false;

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: VoiceButton(
              onStartRecording: () => startCalled = true,
              onStopRecording: () {},
              isRecording: false,
            ),
          ),
        ),
      );

      final gesture = await tester.startGesture(
        tester.getCenter(find.byType(VoiceButton)),
      );
      await tester.pump(const Duration(milliseconds: 500));

      expect(startCalled, true);

      await gesture.up();
      await tester.pump();
    });

    testWidgets('Should call onStopRecording on long press end', (
      WidgetTester tester,
    ) async {
      var stopCalled = false;

      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: VoiceButton(
              onStartRecording: () {},
              onStopRecording: () => stopCalled = true,
              isRecording: true,
            ),
          ),
        ),
      );

      final gesture = await tester.startGesture(
        tester.getCenter(find.byType(VoiceButton)),
      );
      await tester.pump(const Duration(milliseconds: 500));
      await gesture.up();
      await tester.pump();

      expect(stopCalled, true);
    });

    testWidgets('Button should have correct size', (WidgetTester tester) async {
      await tester.pumpWidget(
        MaterialApp(
          home: Scaffold(
            body: VoiceButton(
              onStartRecording: () {},
              onStopRecording: () {},
              isRecording: false,
            ),
          ),
        ),
      );

      final container = tester.widget<Container>(
        find
            .descendant(
              of: find.byType(VoiceButton),
              matching: find.byType(Container),
            )
            .first,
      );

      expect(container.constraints?.maxWidth, 64);
      expect(container.constraints?.maxHeight, 64);
    });
  });
}
