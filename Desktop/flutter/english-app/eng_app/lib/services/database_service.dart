import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';
import '../models/conversation.dart';
import '../models/message.dart';

class DatabaseService {
  static final DatabaseService instance = DatabaseService._init();
  static Database? _database;

  DatabaseService._init();

  Future<Database> get database async {
    if (_database != null) return _database!;
    _database = await _initDB('english_chat.db');
    return _database!;
  }

  Future<Database> _initDB(String filePath) async {
    final dbPath = await getDatabasesPath();
    final path = join(dbPath, filePath);

    return await openDatabase(path, version: 1, onCreate: _createDB);
  }

  Future<void> _createDB(Database db, int version) async {
    const idType = 'TEXT PRIMARY KEY';
    const textType = 'TEXT NOT NULL';
    const intType = 'INTEGER NOT NULL';

    // Create conversations table
    await db.execute('''
      CREATE TABLE conversations (
        id $idType,
        title $textType,
        createdAt $textType,
        updatedAt $textType,
        messageCount $intType
      )
    ''');

    // Create messages table
    await db.execute('''
      CREATE TABLE messages (
        id $idType,
        conversationId $textType,
        text $textType,
        isUser $intType,
        timestamp $textType,
        FOREIGN KEY (conversationId) REFERENCES conversations (id) ON DELETE CASCADE
      )
    ''');

    // Create index for faster queries
    await db.execute('''
      CREATE INDEX idx_messages_conversation 
      ON messages(conversationId)
    ''');
  }

  // Conversation CRUD operations
  Future<Conversation> createConversation(Conversation conversation) async {
    final db = await database;
    await db.insert('conversations', conversation.toDatabase());
    return conversation;
  }

  Future<List<Conversation>> getAllConversations() async {
    final db = await database;
    final result = await db.query('conversations', orderBy: 'updatedAt DESC');
    return result.map((json) => Conversation.fromDatabase(json)).toList();
  }

  Future<Conversation?> getConversation(String id) async {
    final db = await database;
    final maps = await db.query(
      'conversations',
      where: 'id = ?',
      whereArgs: [id],
    );

    if (maps.isNotEmpty) {
      return Conversation.fromDatabase(maps.first);
    }
    return null;
  }

  Future<int> updateConversation(Conversation conversation) async {
    final db = await database;
    return db.update(
      'conversations',
      conversation.toDatabase(),
      where: 'id = ?',
      whereArgs: [conversation.id],
    );
  }

  Future<int> deleteConversation(String id) async {
    final db = await database;
    // Messages will be deleted automatically due to CASCADE
    return await db.delete('conversations', where: 'id = ?', whereArgs: [id]);
  }

  // Message CRUD operations
  Future<Message> createMessage(Message message) async {
    final db = await database;
    await db.insert('messages', message.toDatabase());

    // Update conversation's message count and updatedAt
    final conversation = await getConversation(message.conversationId);
    if (conversation != null) {
      await updateConversation(
        conversation.copyWith(
          messageCount: conversation.messageCount + 1,
          updatedAt: DateTime.now(),
        ),
      );
    }

    return message;
  }

  Future<List<Message>> getMessagesForConversation(
    String conversationId,
  ) async {
    final db = await database;
    final result = await db.query(
      'messages',
      where: 'conversationId = ?',
      whereArgs: [conversationId],
      orderBy: 'timestamp ASC',
    );
    return result.map((json) => Message.fromDatabase(json)).toList();
  }

  Future<int> deleteMessage(String id) async {
    final db = await database;
    return await db.delete('messages', where: 'id = ?', whereArgs: [id]);
  }

  Future<void> close() async {
    final db = await database;
    await db.close();
  }
}
