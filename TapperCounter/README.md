# Tapper Counter

Tapper Counter is a simple yet feature-rich counting application for Android. It's designed to be a minimal and clean tool for keeping track of anything you need to count.

## Features

*   **Simple Interface:** Tap the right side of the screen to count up, and the left side to count down.
*   **Haptic Feedback:** Feel a vibration with every tap for tactile confirmation.
*   **Sound Effects:** Optional sound effects for incrementing and decrementing the count.
*   **State Persistence:** Your count is always saved, so you can pick up where you left off.
*   **Target Goal:** Set a target number in the settings. The app will notify you with a special sound, vibration, and visual cue when you reach your goal.
*   **Settings Screen:** A dedicated screen to toggle features like sound effects and the target goal.
*   **Light & Dark Mode:** The app's theme automatically adapts to your phone's system settings for a comfortable viewing experience.

## Project Setup

This project was built to be opened in Android Studio.

### 1. SDK Location

The project requires the Android SDK location to be configured. Before building, create a file named `local.properties` in the project's root directory (`TapperCounter/`) with the following content, replacing the path with the actual path to your Android SDK:

```
sdk.dir=/path/to/your/android/sdk
```

### 2. Required Assets

The project logic is complete, but requires you to add a few media assets for full functionality.

#### Sound Files

Please add the following three `.mp3` files to the `app/src/main/res/raw/` directory:
*   `tick_up.mp3`
*   `tick_down.mp3`
*   `goal_reached.mp3`

#### App Icon

Please generate a full set of adaptive launcher icons for the application. The easiest way to do this is with Android Studio's **Image Asset Studio** (`Right-click res folder > New > Image Asset`). This will create the necessary files in the various `mipmap-` directories.

## Technologies Used

*   **Language:** Kotlin
*   **Build Tool:** Gradle
*   **UI:** Android Views with ViewBinding and Material Components
