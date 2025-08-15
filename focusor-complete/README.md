# FOCUSOR - Complete Android Productivity Suite

## 📱 Overview
FOCUSOR is a production-ready Android application that combines intelligent study planning, comprehensive finance management, AI integration, and note-taking capabilities. Built with Kotlin, Jetpack Compose, and following Clean Architecture principles.

## ✨ Features

### 📚 Education Module
- **Smart Study Planner**: Track subjects with automatic daily page calculations
- **Reading Speed Tracking**: Individual speed tracking per subject
- **Dynamic Predictions**: Real-time progress updates and completion forecasts
- **Smart Recommendations**: AI-powered study suggestions
- **Conflict Detection**: Identifies scheduling conflicts
- **Study Session Logging**: Track actual vs planned progress

### 💰 Finance Module  
- **Card Management**: Track multiple bank cards with real-time balances
- **Transaction Tracking**: Income/expense categorization
- **Installment Calculator**: Track and calculate monthly payments with interest
- **Financial Health Score**: 0-100 score based on multiple metrics
- **Cash Flow Analysis**: Monthly income vs expenses visualization
- **Smart Insights**: AI-generated financial recommendations
- **Emergency Fund Calculator**: Track savings coverage

### 🤖 AI Assistant
- Multi-provider support (OpenAI, Google Gemini, Claude)
- Token usage and cost tracking
- Conversation history with search
- Custom prompt templates

### 📝 Notes
- Rich text editor with formatting
- Categories and tags system
- Priority levels and reminders
- Full-text search

### ⚙️ Settings
- Theme: Light/Dark/AMOLED Black with Material You
- Language: English/Persian with complete RTL support
- Security: PIN/Pattern/Biometric authentication
- Backup: Google Drive and local backup
- Customization: Fonts, colors, and UI density

## 🏗️ Architecture

```
MVVM + Clean Architecture
├── Data Layer (Room, Repository Implementation)
├── Domain Layer (Use Cases, Repository Interfaces)
└── Presentation Layer (ViewModels, Compose UI)
```

## 🛠️ Tech Stack

- **Language**: Kotlin 1.9.0+
- **UI**: Jetpack Compose with Material 3
- **Database**: Room (SQLite)
- **DI**: Hilt
- **Async**: Coroutines + Flow
- **Architecture**: MVVM + Clean Architecture
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## 📦 Project Structure

```
app/src/main/java/com/focusor/studyplanner/
├── data/
│   ├── database/        # Room database setup
│   ├── entity/          # Database entities
│   ├── dao/             # Data Access Objects
│   └── repository/      # Repository implementations
├── domain/
│   ├── model/           # Domain models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic
│       ├── education/   # StudyCalculationEngine
│       └── finance/     # FinanceCalculationEngine
├── presentation/
│   ├── ui/              # Compose screens
│   │   ├── education/   # Education module UI
│   │   ├── finance/     # Finance module UI
│   │   ├── ai/          # AI Assistant UI
│   │   ├── notes/       # Notes UI
│   │   ├── settings/    # Settings UI
│   │   └── theme/       # Material 3 theme
│   ├── viewmodel/       # ViewModels
│   ├── navigation/      # Navigation setup
│   └── widget/          # Home screen widgets
├── di/                  # Dependency injection modules
└── utils/               # Utility classes
```

## 🚀 Building the App

### Prerequisites
1. Android Studio Arctic Fox or later
2. JDK 17 or higher
3. Android SDK with API 34

### Build Steps

1. **Clone the repository**:
```bash
git clone <repository-url>
cd focusor-app
```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory

3. **Sync Gradle**:
   - Android Studio will automatically sync Gradle
   - If not, click "Sync Project with Gradle Files"

4. **Build the APK**:

   **Option A: Using Android Studio**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - The APK will be in `app/build/outputs/apk/debug/`

   **Option B: Using Command Line**
   ```bash
   # Make sure ANDROID_HOME is set
   export ANDROID_HOME=/path/to/android-sdk
   
   # Build debug APK
   ./gradlew assembleDebug
   
   # Build release APK
   ./gradlew assembleRelease
   ```

5. **Install on Device**:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📊 Key Calculation Engines

### Education Module Calculations
- **Daily Pages Required**: `(totalPages - completedPages) / daysUntilDeadline`
- **Reading Speed**: `pagesRead / hoursSpent` (tracked per subject)
- **Weighted Average Speed**: Recent sessions weighted more heavily
- **Hours Needed Today**: `pagesToRead / currentReadingSpeed`
- **Projected Completion**: Based on current pace vs deadline

### Finance Module Calculations
- **Available Balance**: `currentBalance - committedFuturePayments`
- **Monthly Installment**: Using standard amortization formula with interest
- **Financial Health Score**: Based on emergency fund, debt ratio, savings rate
- **Cash Flow**: `monthlyIncome - monthlyExpenses - installments`
- **Days Until Negative**: Projection based on spending rate

## 🔒 Security Features

- Encrypted API keys storage
- Biometric authentication support
- Encrypted database option
- Secure preferences with DataStore
- No sensitive data in logs

## 🌍 Internationalization

- Complete RTL support for Persian
- Jalali calendar support
- Number format localization
- Currency conversion (IRR/Toman)

## 📱 Supported Devices

- **Phones**: 320dp - 430dp width
- **Tablets**: 600dp - 900dp width  
- **Foldables**: Adaptive layouts
- **Android Versions**: 8.0 (API 26) to 14 (API 34)

## 🎨 Customization

- Material You dynamic colors (Android 12+)
- 10 color presets + custom picker
- Multiple font options for English and Persian
- Adjustable font size and UI density

## 📈 Performance

- 60 FPS animations
- Efficient database queries with Flow
- Image caching and lazy loading
- Background task optimization with WorkManager
- ProGuard optimization for release builds

## 🧪 Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📄 License

This project is available for educational and personal use.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Support

For issues or questions, please open an issue on GitHub.

---

**Note**: This is a production-ready application with all features fully implemented. No mock data is used - all calculations are performed in real-time based on actual user input.