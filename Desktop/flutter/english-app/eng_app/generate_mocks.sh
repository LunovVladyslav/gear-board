#!/bin/bash

# Script to generate mock files for unit tests
# This must be run before running the tests

echo "🔧 Generating mock files for unit tests..."
echo ""

# Check if Flutter is available
if ! command -v flutter &> /dev/null
then
    echo "❌ Flutter command not found. Please ensure Flutter is installed and in your PATH."
    exit 1
fi

# Install dependencies
echo "📦 Installing dependencies..."
flutter pub get

# Generate mocks
echo "🏗️  Generating mock files..."
flutter pub run build_runner build --delete-conflicting-outputs

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Mock files generated successfully!"
    echo ""
    echo "Generated files:"
    echo "  - test/providers/auth_provider_test.mocks.dart"
    echo "  - test/providers/chat_provider_test.mocks.dart"
    echo "  - test/providers/conversation_provider_test.mocks.dart"
    echo ""
    echo "You can now run tests with: flutter test"
else
    echo ""
    echo "❌ Failed to generate mock files"
    exit 1
fi
