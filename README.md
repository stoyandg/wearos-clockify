# Clockify Wear OS Time Tracker

A simple Wear OS app for tracking time and submitting entries to Clockify.

## App Features

- Start and stop a timer on your Galaxy Watch
- Select a Clockify project to associate with the time entry
- Automatically submit time entries to Clockify
- Works offline and syncs time entries when connected

## Requirements

- Wear OS device
- Android Studio for building and sideloading the APK
- Clockify API key (hardcoded in `ClockifyAPI.kt`)

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/stoyandg/wearos-clockify.git
2. **Open in Android Studio**:
- Open the project in Android Studio
- Sync Graadle files to ensure all dependencies are downloaded.
3. **Setup `API_KEY` and `WORKSPACE_ID` in the `ClockifyAPI.kt` file**:
```kotlin
const val API_KEY = "your-clockify-api"
const val WORKSPACE_ID = "your-workspace-id"
```
4. **Build and deploy:**
   - Build the APK in Android Studio
   - Sideload the APK onto your Wear OS device(After enabling developer mode and debugging) using ADB:
     ```
     adb pair IP-address:PORT
     adb connect IP-address:PORT
     adb install IP-address:PORT path_to_your_apk.apk
     ```
## Usage

1. **Start the timer:**
- Open the app on your Wear OS device and press the Start button to begin tracking time

2. **Stop the timer and submit the time entry:**
- Press the "Stop" button to stop the timer.
- Select a Clockify project from the list to submit the time entry.

## License

This project is licensed under the GPL-3.0 License. See the `LICENSE` file for more details.
