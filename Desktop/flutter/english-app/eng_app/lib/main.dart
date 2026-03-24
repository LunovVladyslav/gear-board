import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'services/database_service.dart';
import 'services/api_service.dart';
import 'services/stt_service.dart';
import 'services/tts_service.dart';
import 'services/auth_service.dart';
import 'providers/auth_provider.dart';
import 'providers/chat_provider.dart';
import 'providers/conversation_provider.dart';
import 'screens/login_screen.dart';
import 'screens/conversation_history_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize services
  final databaseService = DatabaseService.instance;
  final apiService = ApiService();
  final sttService = SttService();
  final ttsService = TtsService();
  final authService = AuthService(apiService);

  runApp(
    MyApp(
      databaseService: databaseService,
      apiService: apiService,
      sttService: sttService,
      ttsService: ttsService,
      authService: authService,
    ),
  );
}

class MyApp extends StatelessWidget {
  final DatabaseService databaseService;
  final ApiService apiService;
  final SttService sttService;
  final TtsService ttsService;
  final AuthService authService;

  const MyApp({
    super.key,
    required this.databaseService,
    required this.apiService,
    required this.sttService,
    required this.ttsService,
    required this.authService,
  });

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => AuthProvider(authService)..checkAuthStatus(),
        ),
        ChangeNotifierProvider(
          create: (_) =>
              ChatProvider(databaseService, apiService, sttService, ttsService),
        ),
        ChangeNotifierProvider(
          create: (_) => ConversationProvider(databaseService, apiService),
        ),
      ],
      child: MaterialApp(
        title: 'English Learning Chat',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: Colors.blue,
            brightness: Brightness.light,
          ),
          useMaterial3: true,
          appBarTheme: const AppBarTheme(centerTitle: true, elevation: 0),
          cardTheme: CardThemeData(
            elevation: 2,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
          ),
        ),
        darkTheme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: Colors.blue,
            brightness: Brightness.dark,
          ),
          useMaterial3: true,
          appBarTheme: const AppBarTheme(centerTitle: true, elevation: 0),
          cardTheme: CardThemeData(
            elevation: 2,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
          ),
        ),
        themeMode: ThemeMode.system,
        home: Consumer<AuthProvider>(
          builder: (context, authProvider, child) {
            if (authProvider.isLoading) {
              return const Scaffold(
                body: Center(child: CircularProgressIndicator()),
              );
            }

            if (authProvider.isAuthenticated) {
              return const ConversationHistoryScreen();
            }

            return const LoginScreen();
          },
        ),
      ),
    );
  }
}
