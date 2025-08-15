#!/bin/bash

echo "================================================"
echo "    FOCUSOR APK Auto-Builder Script"
echo "================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check Java
echo "Checking Java installation..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓${NC} Java found: $JAVA_VERSION"
else
    echo -e "${RED}✗${NC} Java not found. Please install Java 17 or higher"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  Mac: brew install openjdk@17"
    exit 1
fi

# Check for Android SDK
echo ""
echo "Checking Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo -e "${YELLOW}⚠${NC} ANDROID_HOME not set. Trying common locations..."
    
    # Try common SDK locations
    POSSIBLE_SDKS=(
        "$HOME/Android/Sdk"
        "$HOME/android-sdk"
        "/usr/local/android-sdk"
        "$HOME/Library/Android/sdk"
        "C:/Users/$USER/AppData/Local/Android/Sdk"
    )
    
    for SDK in "${POSSIBLE_SDKS[@]}"; do
        if [ -d "$SDK" ]; then
            export ANDROID_HOME="$SDK"
            echo -e "${GREEN}✓${NC} Found Android SDK at: $ANDROID_HOME"
            break
        fi
    done
    
    if [ -z "$ANDROID_HOME" ]; then
        echo -e "${RED}✗${NC} Android SDK not found!"
        echo ""
        echo "Please install Android SDK:"
        echo "1. Install Android Studio from https://developer.android.com/studio"
        echo "   OR"
        echo "2. Download command line tools:"
        echo "   wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip"
        echo ""
        echo "Then set ANDROID_HOME:"
        echo "   export ANDROID_HOME=/path/to/android-sdk"
        exit 1
    fi
else
    echo -e "${GREEN}✓${NC} ANDROID_HOME is set: $ANDROID_HOME"
fi

# Create local.properties
echo ""
echo "Setting up local.properties..."
cd /workspace/focusor-app 2>/dev/null || cd focusor-app
echo "sdk.dir=$ANDROID_HOME" > local.properties
echo -e "${GREEN}✓${NC} local.properties created"

# Check Gradle wrapper
echo ""
echo "Checking Gradle wrapper..."
if [ ! -f "gradlew" ]; then
    echo -e "${RED}✗${NC} gradlew not found!"
    exit 1
fi

chmod +x gradlew
echo -e "${GREEN}✓${NC} Gradle wrapper is ready"

# Clean previous builds
echo ""
echo "Cleaning previous builds..."
./gradlew clean 2>/dev/null || true

# Build the APK
echo ""
echo "================================================"
echo "Building APK... This may take 3-5 minutes"
echo "================================================"
echo ""

if ./gradlew assembleDebug; then
    echo ""
    echo -e "${GREEN}================================================${NC}"
    echo -e "${GREEN}    ✓ BUILD SUCCESSFUL!${NC}"
    echo -e "${GREEN}================================================${NC}"
    echo ""
    
    # Find the APK
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "📱 APK Details:"
        echo "   Location: $(pwd)/$APK_PATH"
        echo "   Size: $APK_SIZE"
        echo "   Package: com.focusor.studyplanner"
        echo ""
        echo "📲 To install on your phone:"
        echo "   1. Transfer the APK to your phone"
        echo "   2. Enable 'Install from Unknown Sources'"
        echo "   3. Open the APK file to install"
        echo ""
        echo "   Or use ADB:"
        echo "   adb install $APK_PATH"
    else
        echo -e "${YELLOW}⚠${NC} APK built but not found at expected location"
    fi
else
    echo ""
    echo -e "${RED}================================================${NC}"
    echo -e "${RED}    ✗ BUILD FAILED${NC}"
    echo -e "${RED}================================================${NC}"
    echo ""
    echo "Common fixes:"
    echo "1. Make sure Android SDK is properly installed"
    echo "2. Check that you have SDK platform 34 installed:"
    echo "   $ANDROID_HOME/cmdline-tools/bin/sdkmanager 'platforms;android-34'"
    echo "3. Check build tools are installed:"
    echo "   $ANDROID_HOME/cmdline-tools/bin/sdkmanager 'build-tools;34.0.0'"
    echo "4. Try running with more details:"
    echo "   ./gradlew assembleDebug --stacktrace"
    exit 1
fi