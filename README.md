# RecipeHub 🍳

RecipeHub is an Android application developed as a **Prova de Aptidão Profissional (PAP)** project. It allows users to manage, share, and discover cooking recipes using a modern and intuitive interface.

## 🚀 Features

- **Secure Authentication**: User sign-up and login with email verification powered by Firebase Auth.
- **Recipe Management**: Create, edit, and delete your own recipes with details like ingredients, preparation mode, and time.
- **Image Integration**: Upload recipe photos directly to Firebase Storage.
- **Community Interaction**: Browse recipes shared by other users in the community.
- **Shopping/Ingredient List**: Manage your ingredients and notes within the app.
- **Profile Management**: Update your user profile and personal information.

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: Modern Android development practices with ViewBinding.
- **Backend**: 
  - **Firebase Authentication**: User management.
  - **Cloud Firestore**: Scalable NoSQL database for recipes and user data.
  - **Firebase Storage**: Media storage for recipe photos.
  - **Realtime Database**: For synchronized lists and notes.
- **UI Libraries**: 
  - Picasso (Asynchronous image loading and caching)
  - Material Components (Modern UI design)
  - AvvyLib (Custom UI enhancements)

## 📋 Setup Instructions

To get this project running locally:

1. Clone the repository.
2. Create a new project in the [Firebase Console](https://console.firebase.google.com/).
3. Register the Android app with the package name `com.example.pap`.
4. Download the `google-services.json` and place it in the `app/` directory.
5. In the Firebase Console, enable:
   - **Authentication** (Email/Password provider).
   - **Cloud Firestore** (Test mode).
   - **Realtime Database** (Test mode).
   - **Firebase Storage** (Test mode).
6. Build and run the project in Android Studio.

## 📸 Presentation

*Feel free to explore the code and the modern implementation of Firebase services in Android!*

---
Developed by Guilherme Ribeiro.
