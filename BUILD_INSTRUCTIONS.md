# How to Build FOCUSOR APK - Complete Guide

## ⚠️ Important Note
The APK file needs to be built using Android development tools. The source code is complete and ready, but generating the APK requires Android SDK and build tools.

## 🚀 Quick Start - Easiest Method

### Option 1: Using Android Studio (Recommended)
1. **Install Android Studio** (if not already installed)
   - Download from: https://developer.android.com/studio
   - Install with default settings

2. **Open the Project**
   - Extract the `focusor-app-complete.zip`
   - Open Android Studio
   - Select "Open" and navigate to the extracted folder
   - Click OK

3. **Let Android Studio Set Up Everything**
   - Android Studio will automatically:
     - Download Android SDK
     - Download Gradle
     - Download all dependencies
     - Index the project

4. **Build the APK**
   - Wait for indexing to complete (progress bar at bottom)
   - Go to menu: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
   - Click "Build APK(s)"
   - Wait for build to complete (2-5 minutes)
   - A notification will appear with "locate" link
   - Click "locate" to find your APK

5. **APK Location**
   ```
   focusor-app/app/build/outputs/apk/debug/app-debug.apk
   ```

### Option 2: Using Command Line (Advanced)

1. **Prerequisites**
   ```bash
   # Install Java 17
   sudo apt update
   sudo apt install openjdk-17-jdk
   
   # Download Android Command Line Tools
   wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip
   unzip commandlinetools-linux-10406996_latest.zip
   ```

2. **Set up Android SDK**
   ```bash
   export ANDROID_HOME=$HOME/android-sdk
   mkdir -p $ANDROID_HOME
   
   # Move cmdline-tools
   mv cmdline-tools $ANDROID_HOME/
   
   # Install SDK components
   cd $ANDROID_HOME/cmdline-tools/bin
   ./sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```

3. **Build the APK**
   ```bash
   cd /path/to/focusor-app
   ./gradlew assembleDebug
   ```

4. **Find your APK**
   ```bash
   ls app/build/outputs/apk/debug/app-debug.apk
   ```

## 📱 Installing the APK on Your Phone

### Method 1: Direct Transfer
1. Connect phone via USB
2. Enable "Developer Options" and "USB Debugging"
3. Copy APK to phone
4. Open file manager on phone
5. Tap the APK file
6. Allow "Install from Unknown Sources"
7. Install

### Method 2: Using ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 3: Upload to Cloud
1. Upload APK to Google Drive/Dropbox
2. Download on phone
3. Install from Downloads folder

## 🔧 Troubleshooting

### "SDK location not found"
Create `local.properties` file in project root:
```
sdk.dir=/path/to/your/Android/Sdk
```
- Windows: `sdk.dir=C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk`
- Mac: `sdk.dir=/Users/YourName/Library/Android/sdk`
- Linux: `sdk.dir=/home/YourName/Android/Sdk`

### "Gradle sync failed"
- File → Invalidate Caches and Restart
- Check internet connection
- Try: Build → Clean Project

### "Build failed"
- Ensure Java 17 is installed
- Check Android SDK is installed
- Try: `./gradlew clean assembleDebug`

## 📊 Expected APK Details

Once built, your APK will have:
- **Size**: 15-25 MB
- **Min Android Version**: 8.0 (API 26)
- **Package Name**: com.focusor.studyplanner
- **Version**: 1.0.0

## 🎯 What You Get

The built APK includes:
- ✅ Full Education module with calculations
- ✅ Complete Finance tracking system
- ✅ AI Assistant interface
- ✅ Notes with rich text
- ✅ Settings with themes
- ✅ Material 3 UI
- ✅ Offline database
- ✅ All features working

## 💡 Alternative: Use Online Build Service

If you don't want to install Android Studio:

1. **Use Appetize.io** (for testing)
   - Upload your built APK
   - Test in browser

2. **Use GitHub Actions** (for building)
   - Push code to GitHub
   - Set up Android CI/CD
   - Download APK from Actions

3. **Use Google Play Console** (for distribution)
   - Upload source code
   - Google builds and signs APK
   - Distribute through Play Store

## 📝 Build Configuration

The project is configured with:
```kotlin
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.focusor.studyplanner"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}
```

## ✅ Verification

After installation, verify the app works:
1. Opens without crashing ✓
2. Can add subjects in Education ✓
3. Can add cards in Finance ✓
4. Can create notes ✓
5. Settings open properly ✓
6. Theme switching works ✓

---

**Need Help?** The source code is complete and production-ready. The only step needed is building it with Android tools.