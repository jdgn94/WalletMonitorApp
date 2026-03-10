# Wallet Monitor - Project Structure & Components

This document provides a summary of the project architecture and the custom UI components developed for the Wallet Monitor application.

## 🏗 Project Architecture

The project follows a **Kotlin Multiplatform (KMP)** structure with **Compose Multiplatform** for the UI.

- **composeApp/src/commonMain**: Contains the shared logic and UI.
    - `app.jdgn.walletmonitor.navigation`: Navigator and screen definitions.
    - `app.jdgn.walletmonitor.ui.components`: Custom reusable UI elements.
    - `app.jdgn.walletmonitor.ui.screens`: Application screens (Home, Settings, etc.).
    - `app.jdgn.walletmonitor.ui.theme`: Color schemes and Material3 theme configuration.
- **composeApp/src/androidMain**: Android-specific implementations (e.g., Database drivers).
- **composeApp/src/iosMain**: iOS-specific implementations.

## 🎨 Custom UI Components

A set of specialized components has been created to provide a consistent and polished user experience.

### 1. `CustomScaffold`
A wrapper around the Material3 Scaffold that simplifies common top bar patterns.
- **Translucent Top Bar**: Automatically applies a 70% opacity to the top bar color for a modern look.
- **Integrated Navigation**: Handles back button logic automatically if a `Navigator` is provided.
- **Large Top Bar Support**: Toggle between standard and large (collapsible) top bars via `isLarge`.

### 2. `FadingScroll`
A scrollable container (Vertical or Horizontal) that adds a smooth "fade-out" effect at the edges.
- **Smart Safe Area**: Automatically adds padding at the start and end of the scroll axis so elements are fully visible when the scroll is at the limit.
- **Customizable Fade**: The `fadeLength` can be adjusted to control the intensity of the blur effect.
- **Unified Padding**: Supports `PaddingValues` to define external margins while maintaining internal scroll logic.

### 3. `CustomBox`
A versatile container with enhanced styling capabilities.
- **Custom Shadows**: Supports colored shadows based on the theme.
- **Interactive**: Built-in ripple effect and `onClick` support.
- **Border & Shape**: Easy configuration of borders and rounded corners.

## 🛠 Tech Stack

- **Compose Multiplatform**: Declarative UI for Android and iOS.
- **Koin**: Dependency injection.
- **SQLDelight**: Multiplatform database.
- **Multiplatform Settings**: Key-value storage for preferences.
- **Voyager/Custom Navigator**: Navigation management.
