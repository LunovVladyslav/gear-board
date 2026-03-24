import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../models/user.dart';
import 'api_service.dart';

class AuthService {
  final ApiService _apiService;
  final FlutterSecureStorage _secureStorage = const FlutterSecureStorage();

  static const String _tokenKey = 'auth_token';
  static const String _userIdKey = 'user_id';
  static const String _userEmailKey = 'user_email';
  static const String _userNameKey = 'user_name';

  AuthService(this._apiService);

  Future<User> login(String email, String password) async {
    final user = await _apiService.login(email, password);

    // Save user data securely
    await _secureStorage.write(key: _tokenKey, value: user.token);
    await _secureStorage.write(key: _userIdKey, value: user.id);
    await _secureStorage.write(key: _userEmailKey, value: user.email);
    await _secureStorage.write(key: _userNameKey, value: user.name);

    // Set token in API service
    if (user.token != null) {
      _apiService.setAuthToken(user.token!);
    }

    return user;
  }

  Future<User?> getCurrentUser() async {
    final token = await _secureStorage.read(key: _tokenKey);
    final userId = await _secureStorage.read(key: _userIdKey);
    final email = await _secureStorage.read(key: _userEmailKey);
    final name = await _secureStorage.read(key: _userNameKey);

    if (token != null && userId != null && email != null && name != null) {
      _apiService.setAuthToken(token);
      return User(id: userId, email: email, name: name, token: token);
    }

    return null;
  }

  Future<void> logout() async {
    await _secureStorage.delete(key: _tokenKey);
    await _secureStorage.delete(key: _userIdKey);
    await _secureStorage.delete(key: _userEmailKey);
    await _secureStorage.delete(key: _userNameKey);
  }

  Future<bool> isLoggedIn() async {
    final token = await _secureStorage.read(key: _tokenKey);
    return token != null;
  }
}
