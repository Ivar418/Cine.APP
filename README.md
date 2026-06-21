# CineApp

CineApp is a comprehensive cinema booking application built with **Kotlin Multiplatform** and *
*Compose Multiplatform**. It allows users to browse movies, view showing schedules, reserve seats in
real-time, and manage their cinema experience across Android, Web (WASM), and Desktop (JVM)
platforms.

## 📱 Features

- **Movie Discovery**: Browse current and upcoming movies with detailed information, genres, and
  ratings.
- **Showings & Schedules**: View available showtimes for movies with real-time seat availability and
  pricing.
- **Reservation System**: Real-time seat selection and reservation flow.
- **Order History**: Keep track of your past and upcoming movie tickets.
- **User Accounts**: Manage favorites, personal profile, and booking history.
- **Local Notifications**: Stay informed about your reservations (using `kmpnotifier`).
- **Cross-Platform**: Consistent experience across Android, Web, and Desktop.

## 🏗️ Architecture & Technologies

The project follows a component-based architecture leveraging the modern Kotlin ecosystem:

- **Kotlin Multiplatform (KMP)**: Shared business logic, data models, and networking.
- **Compose Multiplatform**: Shared UI implementation across all targets (Android, Desktop, Web).
- **Decompose**: Lifecycle-aware navigation and component-based state management.
- **Koin**: Dependency injection framework used for cross-platform service management.
- **Ktor 3**: Asynchronous HTTP client for API communication with JSON serialization.
- **Coil 3**: Multiplatform image loading library for movie posters and backdrops.
- **BuildKonfig**: Environment-specific configurations (Development, Staging, Production).
- **KMP Notifier**: Local notification management across platforms.
- **Essenty**: Lifecycle and back-button handling, integrated with Decompose.

---

## 📁 Project Structure

The project is organized to maximize code sharing:

- [`composeApp/src/commonMain`](./composeApp/src/commonMain/kotlin/com/ivarvisser/cineapp) - The
  heart of the app.
  - `ui/feature`: Feature-based UI components and Decompose components (Home, Movie, Ordering,
    etc.).
  - `data`: Repositories and data sources for movie and user data.
  - `domain`: Domain models and business logic.
  - `di`: Koin modules for dependency injection.
- [`composeApp/src/androidMain`](./composeApp/src/androidMain) - Android-specific implementation and
  resources.
- [`composeApp/src/desktopMain`](./composeApp/src/desktopMain) - Desktop (JVM) specific
  implementation.
- [`composeApp/src/wasmJsMain`](./composeApp/src/wasmJsMain) - Web (Wasm) specific implementation.

---

## 🚀 Getting Started

### Prerequisites

- **JDK 11** or higher.
- **Android Studio** (latest version recommended) with the Kotlin Multiplatform plugin.

### Build and Run

#### Android
- Windows: `.\gradlew.bat :composeApp:assembleDebug`
- macOS/Linux: `./gradlew :composeApp:assembleDebug`

#### Desktop (JVM)
- Windows: `.\gradlew.bat :composeApp:run`
- macOS/Linux: `./gradlew :composeApp:run`

#### Web (Wasm)

- Windows: `.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun`
- macOS/Linux: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`

---

## ⚙️ Environment Configuration

Environment-specific settings are managed via `BuildKonfig` in `composeApp/build.gradle.kts`.

### Android Build Variants

Switch variants in the **Build Variants** tool window in Android Studio:

- `developmentDebug/Release`: Development (Local API)
- `stagingDebug/Release`: Staging (Test API)
- `productionDebug/Release`: Production (Live API)

### Other Platforms
Pass the `buildkonfig.flavor` property to Gradle:
- **Development**: `-Pbuildkonfig.flavor=development`
- **Staging**: `-Pbuildkonfig.flavor=staging`
- **Production**: `-Pbuildkonfig.flavor=release` (Default)

### Configuration Details

| Environment     | Base URL                        | Protocol |
|:----------------|:--------------------------------|:---------|
| **Development** | `10.164.147.229:7172`           | `HTTP`   |
| **Staging**     | `acc-cinenetapi.ivarvisser.nl`  | `HTTPS`  |
| **Production**  | `prod-cinenetapi.ivarvisser.nl` | `HTTPS`  |

---

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
and [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform).
