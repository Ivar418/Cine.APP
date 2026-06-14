# CineApp

CineApp is a comprehensive cinema booking application built with **Kotlin Multiplatform** and *
*Compose Multiplatform**. It allows users to browse movies, view showing schedules, reserve seats in
real-time, and manage their cinema experience across Android, Web (WASM/JS), and Desktop (JVM)
platforms.

## Features

- **Movie Discovery**: Browse current and upcoming movies with detailed information, genres, and
  ratings.
- **Showings & Schedules**: View available showtimes for movies with real-time seat availability and
  pricing.
- **Reservation System**: Real-time seat selection and reservation flow.
- **Order Management**: Complete booking process with order history and support for ticket
  generation.
- **User Accounts**: Manage favorites, personal details, and booking history.
- **Cross-Platform**: Consistent experience across Android, Web, and Desktop.

## Architecture & Technologies

The project leverages modern Kotlin ecosystem tools for code sharing and UI:

- **Kotlin Multiplatform (KMP)**: Shared business logic, data models, and networking.
- **Compose Multiplatform**: Shared UI implementation across all targets.
- **Decompose**: Used for lifecycle-aware navigation and component-based architecture.
- **Koin**: Dependency injection across all platforms.
- **Ktor**: Asynchronous HTTP client for API communication.
- **BuildKonfig**: Environment-specific configurations (Dev, Staging, Production).

---

## Project Structure

* [/composeApp](./composeApp/src) - Shared Compose Multiplatform code.
  - [commonMain](./composeApp/src/commonMain/kotlin) - Shared logic and UI for all targets.
  - [androidMain](./composeApp/src/androidMain/kotlin), [jvmMain](./composeApp/src/jvmMain/kotlin),
    etc. - Platform-specific implementations.

---

## Getting Started

### Build and Run

#### Android

- Windows: `.\gradlew.bat :composeApp:assembleDebug`
- macOS/Linux: `./gradlew :composeApp:assembleDebug`

#### Desktop (JVM)

- Windows: `.\gradlew.bat :composeApp:run`
- macOS/Linux: `./gradlew :composeApp:run`

#### Web (Wasm/JS)

- **Wasm**: `.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun`
- **JS**: `.\gradlew.bat :composeApp:jsBrowserDevelopmentRun`

---

## Environment Configuration

Environment-specific settings are managed via `BuildKonfig` in `composeApp/build.gradle.kts`.

### Android Build Variants

Switch variants in the "Build Variants" tool window in Android Studio:

- `developmentDebug/Release`: Development (HTTP)
- `stagingDebug/Release`: Staging (HTTPS)
- `productionDebug/Release`: Production (HTTPS)

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
