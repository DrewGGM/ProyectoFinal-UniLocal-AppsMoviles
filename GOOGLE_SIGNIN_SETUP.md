# Google Sign-In Setup Instructions

## Current Status: ❌ NOT CONFIGURED

Your app is trying to use Google Sign-In but the configuration is incomplete.

## Step-by-Step Setup:

### Step 1: Add SHA-1 to Firebase Console ✅ (You have the fingerprint)

1. Go to https://console.firebase.google.com/
2. Select project: **apps-70ede**
3. Click **Project Settings** (gear icon)
4. Scroll to **Your apps** section
5. Find app: `com.example.primeraplicacionprueba`
6. Click **Add fingerprint**
7. Paste this SHA-1:
   ```
   AE:1D:BF:31:A2:19:D2:78:9D:3C:71:78:96:FF:57:2C:86:FF:55:31
   ```
8. Click **Save**

### Step 2: Enable Google Sign-In in Firebase

1. In Firebase Console, go to **Authentication** → **Sign-in method**
2. Click **Google** provider
3. Toggle **Enable**
4. Set support email: `juanca162072@gmail.com`
5. Click **Save**

### Step 3: Get Web Client ID ⚠️ CRITICAL

1. After adding SHA-1 and enabling Google Sign-In
2. Go to **Project Settings** → **General**
3. Scroll down to **Your apps** → Android app
4. You'll see **Web client ID** (looks like: `123456789-xxxxxxxxx.apps.googleusercontent.com`)
5. **Copy this ID**

### Step 4: Update strings.xml ⚠️ REQUIRED

Open: `app/src/main/res/values/strings.xml`

Replace line 5:
```xml
<!-- BEFORE -->
<string name="default_web_client_id">YOUR_WEB_CLIENT_ID_HERE</string>

<!-- AFTER (use your actual Web Client ID) -->
<string name="default_web_client_id">123456789-xxxxxxxxx.apps.googleusercontent.com</string>
```

### Step 5: Download Updated google-services.json

1. In Firebase Console → **Project Settings**
2. Scroll to **Your apps**
3. Click **Download google-services.json**
4. Replace the file at: `app/google-services.json`
5. Verify the new file has `oauth_client` array populated (not empty)

### Step 6: Rebuild and Test

```bash
./gradlew clean assembleDebug
```

## Troubleshooting:

### Error: "Unknown calling package name 'com.google.android.gms'"
- **Cause**: Web Client ID is not configured or wrong
- **Fix**: Complete Step 3 & 4 above

### Error: "Unable to resolve host firestore.googleapis.com"
- **Cause**: Network connectivity issue
- **Fix**:
  - Check internet connection
  - Restart emulator
  - Clear emulator data: `Settings > Apps > Your App > Clear Data`

### Error: "DEVELOPER_ERROR"
- **Cause**: Google Sign-In not properly configured in Firebase Console
- **Fix**: Complete ALL steps above

## Quick Checklist:

- [ ] SHA-1 added to Firebase Console
- [ ] Google Sign-In enabled in Authentication
- [ ] Web Client ID copied from Firebase
- [ ] `strings.xml` updated with actual Web Client ID
- [ ] Downloaded new `google-services.json`
- [ ] Rebuilt app (`./gradlew clean assembleDebug`)
- [ ] Internet connection working

## After Configuration:

When properly configured, clicking the Google button should:
1. Show Google account picker
2. User selects account
3. App authenticates with Firebase
4. User profile created/loaded from Firestore
5. Navigate to home screen

---

**Support Email**: juanca162072@gmail.com
**Project ID**: apps-70ede
**Package Name**: com.example.primeraplicacionprueba
