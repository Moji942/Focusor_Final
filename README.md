# FOCUSOR - Production-Ready Android Productivity Management App

## Overview
FOCUSOR is a comprehensive Android productivity management application featuring intelligent study planning, advanced financial management, multi-provider AI assistant, comprehensive notes system, and full offline support with optional cloud backup.

## Features

### 📚 Education Module
- Intelligent study planning with auto-calculated daily pages
- Individual reading speed tracking per subject
- Smart workload distribution across multiple subjects
- Dynamic predictions and completion estimates
- Multi-subject optimization

### 💰 Finance Module
- Real-time card balance tracking
- Loan installment management
- Cash flow analysis and predictions
- Financial goal planning and tracking
- Smart alerts and insights

### 🤖 AI Assistant
- Multi-provider support (OpenAI, Gemini, Claude, Custom)
- Side-by-side response comparison
- Secure API key management
- Token counting and cost estimation
- Conversation history storage

### 📝 Notes Module
- Advanced text editor with formatting tools
- Custom categories and tags
- Priority setting and reminders
- Fast search and filtering

### ⚙️ Settings & Security
- Dynamic themes (Light/Dark/AMOLED)
- Full RTL and Persian numeral support
- Multiple calendar formats (Gregorian/Jalali/Islamic)
- App lock (PIN, pattern, biometric)
- Encrypted database
- Cloud and local backup

## Technical Specifications
- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)
- **Kotlin Version**: 1.9.0+
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room (offline-first)
- **Preferences**: DataStore
- **Background Tasks**: WorkManager

## Dependencies
- Room - Local Database
- DataStore - Preferences Storage
- WorkManager - Background Jobs & Notifications
- BiometricPrompt - Security
- Apache POI - Excel Export
- PersianDatePicker - Jalali Calendar Support
- MPAndroidChart - Charts & Graphs
- Retrofit + OkHttp - API Calls
- Google Drive API - Backup/Restore
- Material 3 - UI Components
- Accompanist - UI Utilities

## Installation
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Build and run on device/emulator

## Production Release
The app includes a signed release APK with ProGuard enabled, ready for Google Play submission.

## License
This project is proprietary and confidential.