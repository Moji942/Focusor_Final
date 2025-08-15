#!/bin/bash

echo "📦 Packaging FOCUSOR Android App..."

# Create a clean package directory
PACKAGE_DIR="/workspace/focusor-complete"
rm -rf "$PACKAGE_DIR"
mkdir -p "$PACKAGE_DIR"

# Copy the entire project
cp -r /workspace/focusor-app/* "$PACKAGE_DIR/"

# Create a compressed archive
cd /workspace
tar -czf focusor-app-complete.tar.gz focusor-complete/

# Create a zip file as well
zip -r focusor-app-complete.zip focusor-complete/ -q

echo "✅ Package created successfully!"
echo ""
echo "📊 Project Statistics:"
echo "-------------------"
echo "Total Files: $(find "$PACKAGE_DIR" -type f | wc -l)"
echo "Kotlin Files: $(find "$PACKAGE_DIR" -name "*.kt" | wc -l)"
echo "XML Files: $(find "$PACKAGE_DIR" -name "*.xml" | wc -l)"
echo "Project Size: $(du -sh "$PACKAGE_DIR" | cut -f1)"
echo ""
echo "📦 Available packages:"
echo "- /workspace/focusor-app-complete.tar.gz"
echo "- /workspace/focusor-app-complete.zip"
echo ""
echo "🚀 To build the APK:"
echo "1. Extract the package"
echo "2. Open in Android Studio"
echo "3. Sync Gradle"
echo "4. Build → Build APK"
echo ""
echo "✨ Features included:"
echo "- ✅ Education Module with smart calculations"
echo "- ✅ Finance Module with comprehensive tracking"
echo "- ✅ AI Assistant integration"
echo "- ✅ Notes with rich text"
echo "- ✅ Settings with themes and security"
echo "- ✅ Material 3 UI with Compose"
echo "- ✅ Room database with offline support"
echo "- ✅ Clean Architecture + MVVM"
echo "- ✅ Persian RTL support"
echo "- ✅ Home screen widgets"