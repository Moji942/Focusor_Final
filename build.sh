#!/bin/bash

# FOCUSOR Android App Build Script
# This script builds the FOCUSOR app for different build types

echo "🚀 Building FOCUSOR Android App..."

# Check if we're in the right directory
if [ ! -f "settings.gradle.kts" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

# Function to build debug APK
build_debug() {
    echo "📱 Building Debug APK..."
    ./gradlew assembleDebug
    if [ $? -eq 0 ]; then
        echo "✅ Debug APK built successfully!"
        echo "📁 Location: app/build/outputs/apk/debug/app-debug.apk"
    else
        echo "❌ Debug build failed!"
        exit 1
    fi
}

# Function to build release APK
build_release() {
    echo "📱 Building Release APK..."
    ./gradlew assembleRelease
    if [ $? -eq 0 ]; then
        echo "✅ Release APK built successfully!"
        echo "📁 Location: app/build/outputs/apk/release/app-release.apk"
    else
        echo "❌ Release build failed!"
        exit 1
    fi
}

# Function to clean project
clean_project() {
    echo "🧹 Cleaning project..."
    ./gradlew clean
    echo "✅ Project cleaned!"
}

# Function to run tests
run_tests() {
    echo "🧪 Running tests..."
    ./gradlew test
    if [ $? -eq 0 ]; then
        echo "✅ Tests passed!"
    else
        echo "❌ Tests failed!"
        exit 1
    fi
}

# Function to show help
show_help() {
    echo "FOCUSOR Android App Build Script"
    echo ""
    echo "Usage: $0 [OPTION]"
    echo ""
    echo "Options:"
    echo "  debug     Build debug APK"
    echo "  release   Build release APK"
    echo "  clean     Clean project"
    echo "  test      Run tests"
    echo "  all       Build both debug and release APKs"
    echo "  help      Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 debug"
    echo "  $0 release"
    echo "  $0 all"
}

# Main script logic
case "$1" in
    "debug")
        build_debug
        ;;
    "release")
        build_release
        ;;
    "clean")
        clean_project
        ;;
    "test")
        run_tests
        ;;
    "all")
        build_debug
        build_release
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    *)
        echo "❌ Unknown option: $1"
        echo ""
        show_help
        exit 1
        ;;
esac

echo "🎉 Build script completed!"