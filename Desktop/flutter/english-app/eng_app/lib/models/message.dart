class Message {
  final String id;
  final String conversationId;
  final String text;
  final bool isUser; // true for user, false for AI
  final DateTime timestamp;

  Message({
    required this.id,
    required this.conversationId,
    required this.text,
    required this.isUser,
    required this.timestamp,
  });

  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'] as String,
      conversationId: json['conversationId'] as String,
      text: json['text'] as String,
      isUser: json['isUser'] as bool,
      timestamp: DateTime.parse(json['timestamp'] as String),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'conversationId': conversationId,
      'text': text,
      'isUser': isUser,
      'timestamp': timestamp.toIso8601String(),
    };
  }

  factory Message.fromDatabase(Map<String, dynamic> map) {
    return Message(
      id: map['id'] as String,
      conversationId: map['conversationId'] as String,
      text: map['text'] as String,
      isUser: (map['isUser'] as int) == 1,
      timestamp: DateTime.parse(map['timestamp'] as String),
    );
  }

  Map<String, dynamic> toDatabase() {
    return {
      'id': id,
      'conversationId': conversationId,
      'text': text,
      'isUser': isUser ? 1 : 0,
      'timestamp': timestamp.toIso8601String(),
    };
  }
}
