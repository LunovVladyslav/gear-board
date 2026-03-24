import 'package:flutter/material.dart';
import '../models/message.dart';
import '../models/conversation.dart';
import '../services/database_service.dart';
import '../services/api_service.dart';
import '../services/stt_service.dart';
import '../services/tts_service.dart';

class ChatProvider with ChangeNotifier {
  final DatabaseService _databaseService;
  final ApiService _apiService;
  final SttService _sttService;
  final TtsService _ttsService;

  List<Message> _messages = [];
  Conversation? _currentConversation;
  bool _isLoading = false;
  bool _isRecording = false;
  bool _isSpeaking = false;
  String? _error;
  String _recognizedText = '';

  ChatProvider(
    this._databaseService,
    this._apiService,
    this._sttService,
    this._ttsService,
  );

  List<Message> get messages => _messages;
  Conversation? get currentConversation => _currentConversation;
  bool get isLoading => _isLoading;
  bool get isRecording => _isRecording;
  bool get isSpeaking => _isSpeaking;
  String? get error => _error;
  String get recognizedText => _recognizedText;

  Future<void> loadConversation(String conversationId) async {
    _isLoading = true;
    notifyListeners();

    try {
      _currentConversation = await _databaseService.getConversation(
        conversationId,
      );
      _messages = await _databaseService.getMessagesForConversation(
        conversationId,
      );
      _error = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> createNewConversation(String title) async {
    _isLoading = true;
    notifyListeners();

    try {
      final conversation = Conversation(
        id: DateTime.now().millisecondsSinceEpoch.toString(),
        title: title,
        createdAt: DateTime.now(),
        updatedAt: DateTime.now(),
        messageCount: 0,
      );

      _currentConversation = await _databaseService.createConversation(
        conversation,
      );
      _messages = [];
      _error = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<void> startRecording() async {
    try {
      await _sttService.initialize();
      _isRecording = true;
      _recognizedText = '';
      _error = null;
      notifyListeners();

      await _sttService.startListening(
        onResult: (text) {
          _recognizedText = text;
          notifyListeners();
        },
      );
    } catch (e) {
      _error = e.toString();
      _isRecording = false;
      notifyListeners();
    }
  }

  Future<void> stopRecording() async {
    try {
      await _sttService.stopListening();
      _isRecording = false;
      notifyListeners();

      // Send the recognized text as a message
      if (_recognizedText.isNotEmpty) {
        await sendMessage(_recognizedText);
      }
    } catch (e) {
      _error = e.toString();
      _isRecording = false;
      notifyListeners();
    }
  }

  Future<void> sendMessage(String text) async {
    if (_currentConversation == null || text.trim().isEmpty) return;

    _isLoading = true;
    notifyListeners();

    try {
      // Create user message
      final userMessage = Message(
        id: '${DateTime.now().millisecondsSinceEpoch}_user',
        conversationId: _currentConversation!.id,
        text: text,
        isUser: true,
        timestamp: DateTime.now(),
      );

      // Save to database
      await _databaseService.createMessage(userMessage);
      _messages.add(userMessage);
      notifyListeners();

      // Get AI response
      final aiResponse = await _apiService.sendMessageToAI(
        _currentConversation!.id,
        text,
      );

      // Create AI message
      final aiMessage = Message(
        id: '${DateTime.now().millisecondsSinceEpoch}_ai',
        conversationId: _currentConversation!.id,
        text: aiResponse,
        isUser: false,
        timestamp: DateTime.now(),
      );

      // Save to database
      await _databaseService.createMessage(aiMessage);
      _messages.add(aiMessage);

      // Speak the AI response
      await speakMessage(aiResponse);

      _error = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      _recognizedText = '';
      notifyListeners();
    }
  }

  Future<void> speakMessage(String text) async {
    try {
      await _ttsService.initialize();
      _isSpeaking = true;
      notifyListeners();

      await _ttsService.speak(text);

      _isSpeaking = false;
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      _isSpeaking = false;
      notifyListeners();
    }
  }

  Future<void> stopSpeaking() async {
    await _ttsService.stop();
    _isSpeaking = false;
    notifyListeners();
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }

  @override
  void dispose() {
    _sttService.dispose();
    _ttsService.dispose();
    super.dispose();
  }
}
