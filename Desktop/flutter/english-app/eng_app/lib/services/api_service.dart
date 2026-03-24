import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/user.dart';
import '../models/message.dart';

class ApiService {
  // TODO: Replace with your actual backend URL
  static const String baseUrl = 'https://your-backend-api.com/api';

  String? _authToken;

  void setAuthToken(String token) {
    _authToken = token;
  }

  Map<String, String> _getHeaders() {
    final headers = {'Content-Type': 'application/json'};

    if (_authToken != null) {
      headers['Authorization'] = 'Bearer $_authToken';
    }

    return headers;
  }

  // Authentication
  Future<User> login(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'email': email, 'password': password}),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final user = User.fromJson(data['user']);
        _authToken = data['token'];
        return user.copyWith(token: data['token']);
      } else {
        throw Exception('Login failed: ${response.body}');
      }
    } catch (e) {
      throw Exception('Login error: $e');
    }
  }

  // Send message to AI and get response
  Future<String> sendMessageToAI(String conversationId, String message) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/chat/message'),
        headers: _getHeaders(),
        body: jsonEncode({
          'conversationId': conversationId,
          'message': message,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['response'] as String;
      } else {
        throw Exception('Failed to get AI response: ${response.body}');
      }
    } catch (e) {
      throw Exception('AI message error: $e');
    }
  }

  // Create new conversation on backend
  Future<String> createConversation(String title) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/conversations'),
        headers: _getHeaders(),
        body: jsonEncode({'title': title}),
      );

      if (response.statusCode == 201) {
        final data = jsonDecode(response.body);
        return data['id'] as String;
      } else {
        throw Exception('Failed to create conversation: ${response.body}');
      }
    } catch (e) {
      throw Exception('Create conversation error: $e');
    }
  }

  // Sync conversation history with backend
  Future<List<Message>> syncConversationHistory(String conversationId) async {
    try {
      final response = await http.get(
        Uri.parse('$baseUrl/conversations/$conversationId/messages'),
        headers: _getHeaders(),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body) as List;
        return data.map((json) => Message.fromJson(json)).toList();
      } else {
        throw Exception('Failed to sync history: ${response.body}');
      }
    } catch (e) {
      throw Exception('Sync error: $e');
    }
  }

  // Delete conversation
  Future<void> deleteConversation(String conversationId) async {
    try {
      final response = await http.delete(
        Uri.parse('$baseUrl/conversations/$conversationId'),
        headers: _getHeaders(),
      );

      if (response.statusCode != 200 && response.statusCode != 204) {
        throw Exception('Failed to delete conversation: ${response.body}');
      }
    } catch (e) {
      throw Exception('Delete error: $e');
    }
  }
}
