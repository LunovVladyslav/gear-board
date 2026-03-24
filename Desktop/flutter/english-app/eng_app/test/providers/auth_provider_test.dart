import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/mockito.dart';
import 'package:mockito/annotations.dart';
import 'package:eng_app/providers/auth_provider.dart';
import 'package:eng_app/services/auth_service.dart';
import 'package:eng_app/models/user.dart';

// IMPORTANT: This file requires generated mocks to compile.
// Run: flutter pub run build_runner build
// This will generate: auth_provider_test.mocks.dart
import 'auth_provider_test.mocks.dart';

@GenerateMocks([AuthService])
void main() {
  group('AuthProvider Tests', () {
    late MockAuthService mockAuthService;
    late AuthProvider authProvider;

    setUp(() {
      mockAuthService = MockAuthService();
      authProvider = AuthProvider(mockAuthService);
    });

    test('Initial state should be unauthenticated', () {
      expect(authProvider.isAuthenticated, false);
      expect(authProvider.user, isNull);
      expect(authProvider.isLoading, false);
      expect(authProvider.error, isNull);
    });

    test('Login success should update user and set authenticated', () async {
      final testUser = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'test_token',
      );

      when(
        mockAuthService.login('test@example.com', 'password'),
      ).thenAnswer((_) async => testUser);

      final result = await authProvider.login('test@example.com', 'password');

      expect(result, true);
      expect(authProvider.isAuthenticated, true);
      expect(authProvider.user, testUser);
      expect(authProvider.isLoading, false);
      expect(authProvider.error, isNull);
      verify(mockAuthService.login('test@example.com', 'password')).called(1);
    });

    test('Login failure should set error and remain unauthenticated', () async {
      when(
        mockAuthService.login('test@example.com', 'wrong_password'),
      ).thenThrow(Exception('Invalid credentials'));

      final result = await authProvider.login(
        'test@example.com',
        'wrong_password',
      );

      expect(result, false);
      expect(authProvider.isAuthenticated, false);
      expect(authProvider.user, isNull);
      expect(authProvider.isLoading, false);
      expect(authProvider.error, isNotNull);
      expect(authProvider.error, contains('Invalid credentials'));
    });

    test('CheckAuthStatus should load existing user', () async {
      final testUser = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'test_token',
      );

      when(mockAuthService.getCurrentUser()).thenAnswer((_) async => testUser);

      await authProvider.checkAuthStatus();

      expect(authProvider.isAuthenticated, true);
      expect(authProvider.user, testUser);
      expect(authProvider.isLoading, false);
      expect(authProvider.error, isNull);
      verify(mockAuthService.getCurrentUser()).called(1);
    });

    test(
      'CheckAuthStatus with no user should remain unauthenticated',
      () async {
        when(mockAuthService.getCurrentUser()).thenAnswer((_) async => null);

        await authProvider.checkAuthStatus();

        expect(authProvider.isAuthenticated, false);
        expect(authProvider.user, isNull);
        expect(authProvider.isLoading, false);
      },
    );

    test('Logout should clear user and set unauthenticated', () async {
      // First login
      final testUser = User(
        id: '123',
        email: 'test@example.com',
        name: 'Test User',
        token: 'test_token',
      );

      when(
        mockAuthService.login('test@example.com', 'password'),
      ).thenAnswer((_) async => testUser);

      await authProvider.login('test@example.com', 'password');
      expect(authProvider.isAuthenticated, true);

      // Then logout
      when(mockAuthService.logout()).thenAnswer((_) async => {});

      await authProvider.logout();

      expect(authProvider.isAuthenticated, false);
      expect(authProvider.user, isNull);
      expect(authProvider.error, isNull);
      verify(mockAuthService.logout()).called(1);
    });

    test('ClearError should remove error message', () async {
      when(
        mockAuthService.login('test@example.com', 'wrong'),
      ).thenThrow(Exception('Error'));

      await authProvider.login('test@example.com', 'wrong');
      expect(authProvider.error, isNotNull);

      authProvider.clearError();
      expect(authProvider.error, isNull);
    });
  });
}
