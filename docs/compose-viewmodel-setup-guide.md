# Compose Multiplatform ViewModel Setup (WASM + Android with Koin)

## Overview

This document demonstrates how to structure a Compose Multiplatform screen using:

* Shared ViewModel (commonMain)
* Koin for dependency injection
* WASM-compatible setup
* Android lifecycle-aware ViewModel integration

---

## Project Structure

```
shared/
 └── src/
     ├── commonMain/
     │   ├── presentation/
     │   │   ├── MyState.kt
     │   │   ├── MyViewModel.kt
     │   │   ├── MyScreen.kt
     │   │   └── MyScreenRoute.kt
     │   └── di/
     │       └── CommonModule.kt
     │
     ├── androidMain/
     │   ├── presentation/
     │   │   └── AndroidMyViewModel.kt
     │   └── di/
     │       └── AndroidModule.kt
     │
     └── wasmJsMain/
         └── main.kt

androidApp/
 └── MainActivity.kt
```

---

## 1. Shared State (commonMain)

**File:** MyState.kt

```kotlin
data class MyState(
    val title: String = "Hello",
    val isLoading: Boolean = false
)
```

---

## 2. Shared ViewModel (commonMain)

**File:** MyViewModel.kt

```kotlin
class MyViewModel {

    private val _state = MutableStateFlow(MyState())
    val state: StateFlow<MyState> = _state

    fun loadData() {
        _state.update {
            it.copy(title = "Loaded from ViewModel")
        }
    }
}
```

---

## 3. Shared UI Screen

**File:** MyScreen.kt

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Text(state.title)
}
```

---

## 4. Shared Route

**File:** MyScreenRoute.kt

```kotlin
@Composable
fun MyScreenRoute() {
    val vm: MyViewModel = getKoin().get()
    MyScreen(vm)
}
```

---

## 5. Koin Common Module

**File:** CommonModule.kt

```kotlin
val commonModule = module {
    factory { MyViewModel() }
}
```

---

## 6. WASM Entry Point

**File:** main.kt

```kotlin
fun main() {
    startKoin {
        modules(commonModule)
    }

    CanvasBasedWindow {
        MyScreenRoute()
    }
}
```

---

## 7. Android ViewModel Wrapper

**File:** AndroidMyViewModel.kt

```kotlin
class AndroidMyViewModel(
    private val delegate: MyViewModel
) : ViewModel() {

    val state = delegate.state

    fun loadData() = delegate.loadData()
}
```

---

## 8. Android Koin Module

**File:** AndroidModule.kt

```kotlin
val androidModule = module {
    viewModel {
        AndroidMyViewModel(get())
    }
}
```

---

## 9. Android Route Override

**File:** MyScreenRoute.android.kt

```kotlin
@Composable
fun MyScreenRoute() {
    val vm: AndroidMyViewModel = koinViewModel()
    MyScreen(viewModel = vm)
}
```

---

## 10. Android Entry Point

**File:** MainActivity.kt

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            modules(commonModule, androidModule)
        }

        setContent {
            MyScreenRoute()
        }
    }
}
```

---

## Execution Flow

### WASM

```
MyScreenRoute → getKoin().get() → MyViewModel → MyScreen
```

### Android

```
MyScreenRoute → koinViewModel() → AndroidMyViewModel → MyViewModel → MyScreen
```

---

## Key Takeaways

* Shared logic lives in `commonMain`
* WASM uses the shared ViewModel directly
* Android wraps it in a lifecycle-aware ViewModel
* UI remains fully shared across platforms
* Koin handles dependency injection per platform

---

## Recommendation

Use:

* Shared ViewModel for all business logic
* Android wrapper only when lifecycle matters
* Platform-specific injection only where necessary

---

End of document