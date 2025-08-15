# FOCUSOR Android App - Project Structure

## Overview
FOCUSOR is a comprehensive Android productivity management application built with modern Android development practices. The project follows Clean Architecture principles with MVVM pattern and uses Jetpack Compose for the UI.

## Project Architecture

### Clean Architecture Layers

```
app/
├── data/                    # Data Layer
│   ├── local/              # Local data sources (Room Database)
│   │   ├── dao/            # Data Access Objects
│   │   ├── entity/         # Database entities
│   │   ├── converter/      # Type converters
│   │   └── FocusorDatabase.kt
│   ├── remote/             # Remote data sources (API)
│   └── repository/         # Repository implementations
├── domain/                 # Domain Layer
│   ├── model/              # Domain models
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Use cases (Business logic)
└── presentation/           # Presentation Layer
    ├── screens/            # UI screens
    │   ├── education/      # Education module screens
    │   ├── finance/        # Finance module screens
    │   ├── ai/             # AI Assistant screens
    │   ├── notes/          # Notes module screens
    │   ├── settings/       # Settings screens
    │   └── dashboard/      # Dashboard screen
    ├── navigation/         # Navigation components
    ├── theme/              # UI theme and styling
    └── ui/                 # Reusable UI components
```

## Core Modules

### 1. Education Module 📚
**Purpose**: Intelligent study planning and progress tracking

**Key Features**:
- Subject management with auto-calculated daily pages
- Individual reading speed tracking per subject
- Smart workload distribution across multiple subjects
- Dynamic predictions and completion estimates
- Multi-subject optimization

**Key Components**:
- `Subject` - Domain model with business logic
- `StudySession` - Individual study session tracking
- `StudyPlan` - Multi-subject optimization
- `EducationRepository` - Data access interface
- `EducationViewModel` - UI state management

**Database Entities**:
- `SubjectEntity` - Subject information
- `StudySessionEntity` - Study session records

### 2. Finance Module 💰
**Purpose**: Advanced financial management and analysis

**Key Features**:
- Credit card balance tracking with real-time calculations
- Loan installment management and payment schedules
- Cash flow analysis and predictions
- Financial goal planning and tracking
- Smart alerts and insights

**Key Components**:
- `Card` - Credit card management with calculations
- `Transaction` - Financial transaction tracking
- `Loan` - Loan management with payment calculations
- `FinanceRepository` - Financial data access

**Database Entities**:
- `CardEntity` - Credit card information
- `TransactionEntity` - Financial transactions
- `LoanEntity` - Loan information

### 3. AI Assistant Module 🤖
**Purpose**: Multi-provider AI assistant with conversation management

**Key Features**:
- Support for multiple AI providers (OpenAI, Gemini, Claude, Custom)
- Side-by-side response comparison
- Secure API key management
- Token counting and cost estimation
- Conversation history storage

**Key Components**:
- `AiConversation` - Conversation management
- `AiMessage` - Individual message handling
- `AiConversationRepository` - Conversation data access

**Database Entities**:
- `AiConversationEntity` - Conversation metadata
- `AiMessageEntity` - Individual messages

### 4. Notes Module 📝
**Purpose**: Advanced notes system with organization

**Key Features**:
- Advanced text editor with formatting tools
- Custom categories and tags
- Priority setting and reminders
- Fast search and filtering

**Key Components**:
- `Note` - Note domain model with search capabilities
- `NoteRepository` - Notes data access

**Database Entities**:
- `NoteEntity` - Note information

### 5. Settings Module ⚙️
**Purpose**: App configuration and security

**Key Features**:
- Dynamic themes (Light/Dark/AMOLED)
- Full RTL and Persian numeral support
- Multiple calendar formats (Gregorian/Jalali/Islamic)
- Security: App lock (PIN, pattern, biometric)
- Cloud and local backup

## Technical Stack

### Core Technologies
- **Language**: Kotlin 1.9.0+
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room (offline-first)
- **Preferences**: DataStore
- **Background Tasks**: WorkManager

### Key Dependencies
- **Room** - Local database with type converters
- **DataStore** - Preferences storage
- **WorkManager** - Background jobs & notifications
- **BiometricPrompt** - Security authentication
- **Apache POI** - Excel export functionality
- **PersianDatePicker** - Jalali calendar support
- **MPAndroidChart** - Charts & graphs
- **Retrofit + OkHttp** - API calls for AI integration
- **Google Drive API** - Backup/restore functionality
- **Material 3** - Modern UI components
- **Accompanist** - UI utilities and animations

## Database Schema

### Education Tables
```sql
-- Subjects table
CREATE TABLE subjects (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    source TEXT NOT NULL,
    total_pages INTEGER NOT NULL,
    target_date TEXT NOT NULL,
    current_page INTEGER DEFAULT 0,
    daily_pages INTEGER DEFAULT 0,
    reading_speed REAL DEFAULT 0.0,
    priority INTEGER DEFAULT 1,
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- Study sessions table
CREATE TABLE study_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subject_id INTEGER NOT NULL,
    start_page INTEGER NOT NULL,
    end_page INTEGER NOT NULL,
    duration_minutes INTEGER NOT NULL,
    pages_read INTEGER NOT NULL,
    reading_speed REAL NOT NULL,
    session_date TEXT NOT NULL,
    notes TEXT DEFAULT '',
    FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE
);
```

### Finance Tables
```sql
-- Cards table
CREATE TABLE cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    card_number TEXT NOT NULL,
    current_balance TEXT NOT NULL,
    credit_limit TEXT NOT NULL,
    due_date INTEGER NOT NULL,
    minimum_payment TEXT DEFAULT '0',
    interest_rate TEXT DEFAULT '0',
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- Transactions table
CREATE TABLE transactions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    card_id INTEGER,
    amount TEXT NOT NULL,
    description TEXT NOT NULL,
    category TEXT NOT NULL,
    transaction_type TEXT NOT NULL,
    transaction_date TEXT NOT NULL,
    is_recurring INTEGER DEFAULT 0,
    recurring_interval TEXT,
    notes TEXT DEFAULT '',
    FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE CASCADE
);

-- Loans table
CREATE TABLE loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    original_amount TEXT NOT NULL,
    remaining_amount TEXT NOT NULL,
    interest_rate TEXT NOT NULL,
    term_months INTEGER NOT NULL,
    monthly_payment TEXT NOT NULL,
    start_date TEXT NOT NULL,
    next_payment_date TEXT NOT NULL,
    payment_day INTEGER NOT NULL,
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);
```

### AI Assistant Tables
```sql
-- AI conversations table
CREATE TABLE ai_conversations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    provider TEXT NOT NULL,
    model TEXT NOT NULL,
    total_tokens INTEGER DEFAULT 0,
    estimated_cost REAL DEFAULT 0.0,
    is_active INTEGER DEFAULT 1,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- AI messages table
CREATE TABLE ai_messages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    conversation_id INTEGER NOT NULL,
    role TEXT NOT NULL,
    content TEXT NOT NULL,
    tokens INTEGER DEFAULT 0,
    timestamp TEXT NOT NULL,
    FOREIGN KEY (conversation_id) REFERENCES ai_conversations (id) ON DELETE CASCADE
);
```

### Notes Table
```sql
-- Notes table
CREATE TABLE notes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    category TEXT DEFAULT 'General',
    tags TEXT DEFAULT '',
    priority INTEGER DEFAULT 1,
    is_pinned INTEGER DEFAULT 0,
    reminder_date TEXT,
    is_completed INTEGER DEFAULT 0,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);
```

## Business Logic Highlights

### Education Calculations
- **Daily Pages Calculation**: Based on remaining pages and days until target
- **Reading Speed**: Weighted average of last 7 study sessions
- **Completion Prediction**: Using current reading speed and available time
- **Multi-Subject Optimization**: Priority-based hour allocation
- **Backlog Recovery**: Extra time needed to catch up on overdue subjects

### Finance Calculations
- **Credit Utilization**: Current balance / Credit limit * 100
- **Interest Charges**: Monthly rate calculation for credit cards
- **Loan Payments**: Amortization schedule calculations
- **Cash Flow Analysis**: Income vs expenses with recurring transaction detection
- **Financial Health Score**: Multi-factor scoring system

### AI Assistant Features
- **Token Counting**: Accurate token estimation for cost calculation
- **Provider Management**: Multiple API key management
- **Conversation History**: Persistent chat storage
- **Cost Tracking**: Real-time cost estimation per conversation

## Security Features
- **Database Encryption**: Room database with SQLCipher
- **Biometric Authentication**: Fingerprint/face unlock support
- **Secure Storage**: Encrypted API key storage
- **App Lock**: PIN, pattern, or biometric protection

## Performance Optimizations
- **Offline-First**: All data stored locally with optional cloud sync
- **Lazy Loading**: Efficient data loading with pagination
- **Background Processing**: WorkManager for non-blocking operations
- **Memory Management**: Proper lifecycle management in Compose
- **Database Indexing**: Optimized queries with proper indexes

## Testing Strategy
- **Unit Tests**: Business logic and repository testing
- **Integration Tests**: Database and API integration
- **UI Tests**: Compose UI testing with Compose Testing
- **End-to-End Tests**: Complete user flow testing

## Build Configuration
- **Debug Build**: Development with debugging enabled
- **Release Build**: Production-ready with ProGuard optimization
- **Signing**: APK signing for Google Play Store submission
- **ProGuard Rules**: Comprehensive obfuscation rules for all dependencies

## Deployment
- **Google Play Store**: Production release with staged rollout
- **Internal Testing**: Alpha and beta testing channels
- **Crash Reporting**: Firebase Crashlytics integration
- **Analytics**: User behavior tracking and app performance monitoring

## Future Enhancements
- **Cloud Sync**: Google Drive and Dropbox integration
- **Advanced Analytics**: Machine learning insights
- **Collaboration**: Shared study plans and financial goals
- **Voice Commands**: AI assistant with voice input
- **Wearable Support**: Android Wear integration
- **Desktop Companion**: Cross-platform desktop application

## Development Guidelines
1. **No Mock Data**: All calculations based on actual user inputs
2. **Mathematical Accuracy**: Use BigDecimal for monetary calculations
3. **Real-Time Updates**: Immediate UI updates on data changes
4. **Accessibility**: Full TalkBack and high contrast support
5. **RTL Support**: Complete right-to-left language support
6. **Offline Functionality**: All core features work without internet
7. **Data Security**: Encrypted storage and secure transmission

This project structure ensures a scalable, maintainable, and production-ready Android application that meets all the specified requirements for the FOCUSOR productivity management app.