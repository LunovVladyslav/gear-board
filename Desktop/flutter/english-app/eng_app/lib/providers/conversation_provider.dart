import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import '../models/conversation.dart';
import '../services/database_service.dart';
import '../services/api_service.dart';

class ConversationProvider with ChangeNotifier {
  final DatabaseService _databaseService;
  final ApiService _apiService;

  List<Conversation> _conversations = [];
  bool _isLoading = false;
  String? _error;

  ConversationProvider(this._databaseService, this._apiService);

  List<Conversation> get conversations => _conversations;
  bool get isLoading => _isLoading;
  String? get error => _error;

  Future<void> loadConversations() async {
    _isLoading = true;
    notifyListeners();

    try {
      _conversations = await _databaseService.getAllConversations();
      _error = null;
    } catch (e) {
      _error = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<Conversation?> createConversation(String title) async {
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

      final created = await _databaseService.createConversation(conversation);
      _conversations.insert(0, created);
      _error = null;
      _isLoading = false;
      notifyListeners();
      return created;
    } catch (e) {
      _error = e.toString();
      _isLoading = false;
      notifyListeners();
      return null;
    }
  }

  Future<void> deleteConversation(String id) async {
    try {
      await _databaseService.deleteConversation(id);
      _conversations.removeWhere((conv) => conv.id == id);

      // Also delete from backend
      try {
        await _apiService.deleteConversation(id);
      } catch (e) {
        debugPrint('Failed to delete from backend: $e');
      }

      _error = null;
      notifyListeners();
    } catch (e) {
      _error = e.toString();
      notifyListeners();
    }
  }

  void clearError() {
    _error = null;
    notifyListeners();
  }
}
