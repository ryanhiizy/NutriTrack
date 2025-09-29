# NutriTrack

NutriTrack is a mobile app that helps users track their nutrition habits, get personalised dietary insights, and view progress over time.
It is built with Android and integrates a local database, public APIs, and generative AI for tailored coaching.

## Features

- **User Accounts** – multi-user login with strong password checks, secure storage, and persistent sessions
- **Profile Management** – edit details, view questionnaire history, and delete past entries while keeping one for continuity
- **NutriCoach** – fruit nutrition facts (FruityVice API), personalised AI tips (Gemini), and a browsable tip history
- **Settings** – user profile, logout, and admin mode unlock (`dollar-entry-apples`)
- **Admin Dashboard** – aggregate HEIFA stats by gender and AI-generated insights on diet patterns
- **Technical** – Room database seeded from CSV, MVVM architecture with LiveData, Retrofit + coroutines for networking, responsive Jetpack Compose UI

## Technologies Used

- **Frontend**: Android, Kotlin, Jetpack Compose
- **Architecture**: MVVM (Model–View–ViewModel) with LiveData and Repository pattern
- **Backend / Data**: Room database (seeded from CSV on first launch)
- **APIs**: FruityVice (nutrition facts), Gemini (AI tips)
- **Tools**: Retrofit (networking), Coroutines (async), Gradle

## Installation

1. Clone the repository

   ```bash
   git clone https://github.com/ryanhiizy/NutriTrack.git
   cd NutriTrack
   ```

2. Open the project in Android Studio.

3. Add your Gemini API key in local.properties:

   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```

4. Build and run on an emulator or device.
