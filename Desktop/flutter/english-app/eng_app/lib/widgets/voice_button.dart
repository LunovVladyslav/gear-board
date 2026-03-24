import 'package:flutter/material.dart';

class VoiceButton extends StatefulWidget {
  final VoidCallback onStartRecording;
  final VoidCallback onStopRecording;
  final bool isRecording;

  const VoiceButton({
    super.key,
    required this.onStartRecording,
    required this.onStopRecording,
    required this.isRecording,
  });

  @override
  State<VoiceButton> createState() => _VoiceButtonState();
}

class _VoiceButtonState extends State<VoiceButton>
    with SingleTickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1000),
    )..repeat(reverse: true);

    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.2).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onLongPressStart: (_) => widget.onStartRecording(),
      onLongPressEnd: (_) => widget.onStopRecording(),
      child: AnimatedBuilder(
        animation: _scaleAnimation,
        builder: (context, child) {
          return Transform.scale(
            scale: widget.isRecording ? _scaleAnimation.value : 1.0,
            child: Container(
              width: 64,
              height: 64,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: widget.isRecording
                    ? Colors.red
                    : Theme.of(context).colorScheme.primary,
                boxShadow: [
                  BoxShadow(
                    color: widget.isRecording
                        ? Colors.red.withValues(alpha: 0.4)
                        : Theme.of(
                            context,
                          ).colorScheme.primary.withValues(alpha: 0.4),
                    blurRadius: widget.isRecording ? 20 : 8,
                    spreadRadius: widget.isRecording ? 5 : 0,
                  ),
                ],
              ),
              child: Icon(
                widget.isRecording ? Icons.mic : Icons.mic_none,
                color: Colors.white,
                size: 32,
              ),
            ),
          );
        },
      ),
    );
  }
}
