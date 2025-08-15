# FOCUSOR Android App - Quick Start Guide

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 34
- Minimum Android API Level: 26 (Android 8.0)
- Target Android API Level: 34 (Android 14)

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd FOCUSOR
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the FOCUSOR directory and select it

3. **Sync Project**
   - Wait for Gradle sync to complete
   - If prompted, update any dependencies

4. **Build the Project**
   ```bash
   # Using the build script
   ./build.sh debug
   
   # Or using Gradle directly
   ./gradlew assembleDebug
   ```

5. **Run on Device/Emulator**
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio
   - Or use: `./gradlew installDebug`

## 📱 App Features

### Education Module 📚
- Add subjects with target completion dates
- Track reading progress and speed
- Get intelligent study recommendations
- Multi-subject workload optimization

### Finance Module 💰
- Credit card balance tracking
- Loan payment management
- Cash flow analysis
- Financial health scoring

### AI Assistant 🤖
- Multi-provider AI support (OpenAI, Gemini, Claude)
- Conversation history
- Cost tracking and token counting
- Side-by-side response comparison

### Notes Module 📝
- Advanced text editor
- Categories and tags
- Priority setting and reminders
- Fast search and filtering

### Settings ⚙️
- Dynamic themes (Light/Dark/AMOLED)
- RTL and Persian numeral support
- Multiple calendar formats
- Security and backup options

## 🏗️ Project Structure

```
FOCUSOR/
├── app/
│   ├── src/main/
│   │   ├── java/com/focusor/app/
│   │   │   ├── data/           # Data layer
│   │   │   ├── domain/         # Business logic
│   │   │   └── presentation/   # UI layer
│   │   └── res/               # Resources
│   └── build.gradle.kts       # App-level build config
├── build.gradle.kts           # Project-level build config
├── settings.gradle.kts        # Project settings
├── build.sh                   # Build script
└── README.md                  # Project documentation
```

## 🔧 Development

### Key Technologies
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Room** - Local database
- **Hilt** - Dependency injection
- **Material 3** - Design system
- **WorkManager** - Background tasks

### Architecture
- **Clean Architecture** - Separation of concerns
- **MVVM** - UI pattern
- **Repository Pattern** - Data access
- **Use Case Pattern** - Business logic

### Building Different Variants

```bash
# Debug build (development)
./build.sh debug

# Release build (production)
./build.sh release

# Both debug and release
./build.sh all

# Clean project
./build.sh clean

# Run tests
./build.sh test
```

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### UI Tests
```bash
./gradlew app:connectedDebugAndroidTest
```

## 📦 Building for Production

### Release APK
```bash
./build.sh release
```

### Signed APK
1. Configure signing in `app/build.gradle.kts`
2. Add keystore file
3. Run: `./gradlew assembleRelease`

### Google Play Store
1. Build release APK
2. Test thoroughly
3. Upload to Google Play Console
4. Configure staged rollout

## 🔒 Security Features

### Database Encryption
- Room database with SQLCipher
- Encrypted storage for sensitive data

### Authentication
- Biometric authentication
- PIN/Pattern lock
- Secure API key storage

### Data Protection
- Encrypted backups
- Secure file sharing
- Privacy-compliant data handling

## 🌐 Internationalization

### Supported Languages
- English (default)
- Persian (planned)
- Arabic (planned)

### RTL Support
- Complete right-to-left layout support
- Persian numeral support
- Multiple calendar formats

## 📊 Analytics & Monitoring

### Crash Reporting
- Firebase Crashlytics integration
- Automatic crash reporting
- Performance monitoring

### User Analytics
- User behavior tracking
- Feature usage analytics
- Performance metrics

## 🚀 Deployment

### Internal Testing
1. Build debug APK
2. Upload to internal testing track
3. Share with testers

### Beta Testing
1. Build release APK
2. Upload to beta testing track
3. Invite beta testers

### Production Release
1. Build signed release APK
2. Upload to production track
3. Configure staged rollout
4. Monitor for issues

## 🐛 Troubleshooting

### Common Issues

**Build Failures**
```bash
# Clean and rebuild
./build.sh clean
./build.sh debug
```

**Dependency Issues**
```bash
# Refresh dependencies
./gradlew --refresh-dependencies
```

**Database Issues**
```bash
# Clear app data or uninstall/reinstall
adb uninstall com.focusor.app
```

### Logs
```bash
# View app logs
adb logcat | grep com.focusor.app
```

## 📚 Additional Resources

### Documentation
- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Detailed project structure
- [README.md](README.md) - Project overview
- [Android Developer Documentation](https://developer.android.com/)

### Key Files
- `app/build.gradle.kts` - App configuration
- `app/src/main/AndroidManifest.xml` - App manifest
- `app/src/main/java/com/focusor/app/FocusorApplication.kt` - Application class

### Development Guidelines
1. Follow Clean Architecture principles
2. Use meaningful commit messages
3. Write unit tests for business logic
4. Follow Material Design guidelines
5. Ensure accessibility compliance
6. Test on multiple devices
7. Maintain code documentation

## 🤝 Contributing

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep functions small and focused

### Git Workflow
1. Create feature branch
2. Make changes
3. Write tests
4. Commit with descriptive message
5. Create pull request
6. Code review
7. Merge to main

## 📞 Support

For questions or issues:
1. Check the documentation
2. Search existing issues
3. Create a new issue with details
4. Contact the development team

---

**Happy Coding! 🎉**

The FOCUSOR Android app is designed to be a comprehensive productivity management solution. Follow this guide to get started with development and contribute to the project.