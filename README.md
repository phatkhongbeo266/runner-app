# Level Up Application

Achieve your fitness goals with **Level Up**, an intuitive app designed to track and analyze your running activities with precision. Whether you're a seasoned marathon runner or just starting out, Run Tracker offers real-time GPS tracking, insightful statistics, and interactive graphs to help you stay motivated and improve your performance.

## Key Features

- **Real-Time GPS Tracking**: Record your runs with accurate GPS tracking and save your routes. Share your mapped-out paths with friends and family to celebrate your milestones.

- **Comprehensive Running History**: Keep a detailed log of all your runs, including distance, time, and calories burned, so you can monitor your improvement over time.

- **Detailed Graphs & Analysis**: Dive into your stats with easy-to-read graphs showing your performance metrics such as speed, distance, and time. Use these insights to identify trends and areas for improvement.

- **Personalized Settings**: Customize the app according to your needs. Set daily goals, choose between metric or imperial units, and select your preferred language for a tailored experience.

- **Cloud Sync**: Seamlessly sync your running data with your Google account, ensuring all your progress is backed up and accessible across multiple devices.

## Technology Stack

- **Android Kotlin**: Developed using Android Kotlin for optimal performance on Android devices, offering a smooth and native user experience.

- **Google AdMob**: Integrates in-app advertisements to generate revenue without interrupting the user experience.

- **Firebase**: Powers data storage, user authentication, and real-time syncing, ensuring your running stats and progress are securely managed and accessible in real-time.


## 🚀 Firebase Setup

### 1️⃣ Get SHA-1 Debug Key
Use the provided SHA-1:
```
04:B7:F8:00:88:93:91:A9:46:92:DE:ED:5D:59:1A:0A:A7:35:28:19
```
Or generate manually:

```bash
./gradlew signingReport
```

Or
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

---

### 2️⃣ Register Android App in Firebase

* Go to [Firebase Console](https://console.firebase.google.com)
* Create or select an existing project
* Add Android app:
    * **Package name**: (from `AndroidManifest.xml`)
    * **App nickname**: optional
    * **SHA-1**: paste `04:B7:F8:00:88:93:91:A9:46:92:DE:ED:5D:59:1A:0A:A7:35:28:19`
---

### 3️⃣ Add `google-services.json`

* Download the file from Firebase Console
* Place it in:
  ```
  <project-root>/app/google-services.json
  ```
---

### 4️⃣ Update `default_web_client_id`

* Open `google-services.json` → find:

  ```json
  "client": [
    {
      "oauth_client": [
        {
          "client_id": "xxxxxxxxxxxx-xxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com"
        }
      ]
    }
  ]
  ```
* Copy the `client_id`
* In `res/values/strings.xml`, update:

  ```xml
  <string name="default_web_client_id">YOUR_CLIENT_ID</string>
  ```

---

### 5️⃣ Enable Google Sign-in Authentication

* Firebase Console → Authentication → Sign-in method
* Enable **Google**
* Set support email if required

---

### 6️⃣ Setup Firestore Database

* Firebase Console → Firestore Database
* Create database (use test mode for development)
* If no data appears after testing, manually create:
    * Collection name: `RunTrackerUsers`
---

## 🤖 Chatbot Setup

### 1️⃣ Configure API URL and API Key
In `app/build.gradle`, inside `defaultConfig`:
```gradle
buildConfigField "String", "CHATBOT_API_URL", '"https://your-api-url.com"'
buildConfigField "String", "CHATBOT_API_KEY", '"your-api-key"'
```
> ⚠️ Be careful with quotes (`"` and `'`).
---

### 2️⃣ Sync Project with Gradle
* In Android Studio → **File → Sync Project with Gradle Files**
---

### 3️⃣ Update `ChatData.kt`
If the API request/response format changes:
* Modify `ChatData.kt`:
    * Add new fields as needed
    * Update existing fields (names or types)

Example:
```kotlin
data class ChatResponse(
    val message: String,
    val sender: String,
    val timestamp: Long
)
```
---



