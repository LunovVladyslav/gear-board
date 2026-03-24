import 'package:flutter/foundation.dart';
import 'package:speech_to_text/speech_to_text.dart';
import 'package:permission_handler/permission_handler.dart';

class SttService {
  final SpeechToText _speechToText = SpeechToText();
  bool _isInitialized = false;

  bool get isInitialized => _isInitialized;
  bool get isListening => _speechToText.isListening;
  bool get isAvailable => _speechToText.isAvailable;

  Future<bool> initialize() async {
    if (_isInitialized) return true;

    // Request microphone permission
    final status = await Permission.microphone.request();
    if (!status.isGranted) {
      throw Exception('Microphone permission not granted');
    }

    _isInitialized = await _speechToText.initialize(
      onError: (error) => debugPrint('STT Error: $error'),
      onStatus: (status) => debugPrint('STT Status: $status'),
    );

    return _isInitialized;
  }

  Future<void> startListening({
    required Function(String) onResult,
    String localeId = 'en_US',
  }) async {
    if (!_isInitialized) {
      await initialize();
    }

    if (!_isInitialized) {
      throw Exception('Speech recognition not initialized');
    }

    await _speechToText.listen(
      onResult: (result) {
        onResult(result.recognizedWords);
      },
      localeId: localeId,
      listenOptions: SpeechListenOptions(
        listenMode: ListenMode.confirmation,
        cancelOnError: true,
        partialResults: true,
      ),
    );
  }

  Future<void> stopListening() async {
    if (_speechToText.isListening) {
      await _speechToText.stop();
    }
  }

  Future<void> cancel() async {
    if (_speechToText.isListening) {
      await _speechToText.cancel();
    }
  }

  Future<List<String>> get availableLocales async {
    final locales = await Future.value(_speechToText.locales());
    return locales
        .where((locale) => locale.localeId.startsWith('en'))
        .map((locale) => locale.localeId)
        .toList();
  }

  void dispose() {
    _speechToText.stop();
  }
}
