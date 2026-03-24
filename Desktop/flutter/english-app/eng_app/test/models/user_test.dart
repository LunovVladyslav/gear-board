import 'package:flutter_test/flutter_test.dart';
import 'package:eng_app/models/user.dart';

void main() {
  group('User Model Tests', () {
    test('User should be created with all fields', () {
      final user = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'test_token',
      );

      expect(user.id, '123');
      expect(user.email, 'test@example.com');
      expect(user.name, 'Test User');
      expect(user.token, 'test_token');
    });

    test('User should serialize to JSON correctly', () {
      final user = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'test_token',
      );

      final json = user.toJson();

      expect(json['id'], '123');
      expect(json['email'], 'test@example.com');
      expect(json['name'], 'Test User');
      expect(json['token'], 'test_token');
    });

    test('User should deserialize from JSON correctly', () {
      final json = {
        'id': '123',
        'email': 'test@example.com',
        'name': 'Test User',
        'token': 'test_token',
      };

      final user = User.fromJson(json);

      expect(user.id, '123');
      expect(user.email, 'test@example.com');
      expect(user.name, 'Test User');
      expect(user.token, 'test_token');
    });

    test('User copyWith should update only specified fields', () {
      final user = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'old_token',
      );

      final updatedUser = user.copyWith(token: 'new_token');

      expect(updatedUser.id, '123');
      expect(updatedUser.email, 'test@example.com');
      expect(updatedUser.name, 'Test User');
      expect(updatedUser.token, 'new_token');
    });

    test('User can be created without token', () {
      final user = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
      );

      expect(user.token, isNull);
    });
  });
}
