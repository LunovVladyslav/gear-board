import 'package:flutter/foundation.dart';
import 'package:flutter_tts/flutter_tts.dart';

class TtsService {
  final FlutterTts _flutterTts = FlutterTts();
  bool _isInitialized = false;

  bool get isInitialized => _isInitialized;

  Future<void> initialize() async {
    if (_isInitialized) return;

    await _flutterTts.setLanguage('en-US');
    await _flutterTts.setSpeechRate(0.5); // Normal speed
    await _flutterTts.setVolume(1.0);
    await _flutterTts.setPitch(1.0);

    // Set up handlers
    _flutterTts.setStartHandler(() {
      debugPrint('TTS Started');
    });

    _flutterTts.setCompletionHandler(() {
      debugPrint('TTS Completed');
    });

    _flutterTts.setErrorHandler((msg) {
      debugPrint('TTS Error: $msg');
    });

    _isInitialized = true;
  }

  Future<void> speak(String text) async {
    if (!_isInitialized) {
      await initialize();
    }

    await _flutterTts.speak(text);
  }

  Future<void> stop() async {
    await _flutterTts.stop();
  }

  Future<void> pause() async {
    await _flutterTts.pause();
  }

  Future<void> setSpeechRate(double rate) async {
    await _flutterTts.setSpeechRate(rate);
  }

  Future<void> setVolume(double volume) async {
    await _flutterTts.setVolume(volume);
  }

  Future<void> setPitch(double pitch) async {
    await _flutterTts.setPitch(pitch);
  }

  Future<List<String>> getAvailableVoices() async {
    final voices = await _flutterTts.getVoices;
    if (voices is List) {
      return voices
          .where((voice) => voice['locale'].toString().startsWith('en'))
          .map((voice) => voice['name'].toString())
          .toList();
    }
    return [];
  }

  Future<void> setVoice(String voiceName) async {
    await _flutterTts.setVoice({'name': voiceName, 'locale': 'en-US'});
  }

  void dispose() {
    _flutterTts.stop();
  }
}
