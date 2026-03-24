# GearBoard Android — Claude Code Instructions

## Контекст

Реалізуй Android додаток GearBoard згідно технічного завдання у файлі `GEARBOARD_ANDROID_SPEC.md`.
У директорії `frontend/` знаходиться готовий React/TypeScript mock — використовуй його як
референс для UI, кольорів, структури даних та логіки екранів.

---

## Критичні правила виконання

### 1. Фазова розробка з зупинками

Реалізуй строго по одній фазі за раз. Після кожної фази:
- Виведи список створених файлів
- Виведи список залежностей які потрібно перевірити
- Напиши: **"PHASE {N} COMPLETE — ready for local build verification"**
- Чекай моєї команди "continue" перед наступною фазою

НЕ переходь до наступної фази автоматично.

### 2. Локальна збірка обов'язкова

Ти не можеш запустити `./gradlew assembleDebug` в цьому середовищі.
Тому після Phase 1 і після кожної непарної фази я збиратиму проект локально.
Якщо є помилки компіляції — я поверну їх тобі і ти виправиш перед продовженням.

### 3. Git коміти

Після кожної фази виводь готову команду для коміту:
```bash
git add . && git commit -m "Phase N: [опис]"
```

---

## Порядок фаз

**Phase 1: Project Setup**
- `build.gradle.kts` (project + app рівні)
- `settings.gradle.kts`
- `AndroidManifest.xml` з усіма permissions і intent filters
- `GearBoardApplication.kt` (@HiltAndroidApp)
- `MainActivity.kt` (мінімальний — тільки setContent + scaffold)

→ ЗУПИНКА. Я збираю локально.

**Phase 2: Design System**
- `ui/theme/Color.kt` — точно з mock кольорами
- `ui/theme/Type.kt`
- `ui/theme/Theme.kt`
- `ui/theme/Dimensions.kt` — константи розмірів (knob мінімум 64dp і т.д.)

→ ЗУПИНКА. Я збираю локально.

**Phase 3: Core UI Components**
- `GearKnob.kt` — Canvas composable, детально нижче
- `GearToggle.kt`
- `GearSlider.kt`
- `SectionHeader.kt`
- `ConnectionStatusBar.kt`

→ ЗУПИНКА. Я збираю локально.

**Phase 4: Data Layer + MIDI Manager**
- Domain models
- Room entities + DAOs + Database
- Repositories
- Hilt modules
- `GearBoardMidiManager.kt`
- `UsbMidiConnection.kt`
- `BleMidiConnection.kt`

→ ЗУПИНКА. Я збираю локально.

**Phase 5: Connect Screen**
- `ConnectScreen.kt`
- `ConnectViewModel.kt`
- Runtime permission handling (BLUETOOTH_CONNECT, BLUETOOTH_SCAN)
- BLE device scan BottomSheet

→ ЗУПИНКА.

**Phase 6: Board Screen**
- `BoardScreen.kt` — вертикальний скрол, 4 секції
- `PedalCard.kt` — горизонтальний LazyRow
- `AmpSection.kt`
- `CabCard.kt`
- `EffectCard.kt`
- Всі Add модали / BottomSheets
- `BoardViewModel.kt`

→ ЗУПИНКА. Я збираю і тестую на пристрої.

**Phase 7: Presets Screen**
- `PresetScreen.kt`
- `PresetViewModel.kt`
- Export/Import пресетів як JSON файл (через Android SAF — Storage Access Framework)
- Program Change відправлення

→ ЗУПИНКА.

**Phase 8: MIDI Map Screen**
- `MidiMapScreen.kt`
- `MidiMapViewModel.kt`
- Learn mode з 10 секундним таймаутом і countdown UI

→ ЗУПИНКА.

**Phase 9: Monitor + Settings**
- `MonitorScreen.kt` + `MonitorViewModel.kt`
- `SettingsScreen.kt` + `SettingsViewModel.kt` (DataStore)

→ ЗУПИНКА.

**Phase 10: Navigation + Freemium + Polish**
- `GearBoardNavigation.kt` — Bottom Navigation Bar, 5 пунктів
- Freemium обмеження (детально нижче)
- Keep-screen-on при підключенні
- Landscape layout для Board екрану
- Edge case handling

---

## Специфічні вимоги до компонентів

### GearKnob (найважливіший компонент)

```kotlin
@Composable
fun GearKnob(
    value: Float,           // 0f..1f нормалізоване значення
    onValueChange: (Float) -> Unit,
    label: String,
    displayValue: String,   // форматоване для показу ("6.0", "127" і т.д.)
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLongPress: (() -> Unit)? = null  // відкрити MIDI Learn
)
```

Вимоги до реалізації:
- Мінімальний touch target: 64dp × 64dp (обов'язково)
- Діапазон повороту: від -135° до +135° (270° загалом)
- Amber indicator line на knob body
- Vertical drag gesture: 1dp руху = 1/200 зміни значення
- Double-tap → reset до 0.5f (середина)
- Long press → викликати onLongPress callback
- Haptic feedback: `performHapticFeedback(HapticFeedbackType.TextHandleMove)` на кожні 5% зміни
- Canvas малює: темне коло body + amber arc індикатор позиції + amber лінія напрямку

### Freemium обмеження (Phase 10)

```kotlin
object FreemiumLimits {
    const val FREE_MAX_PRESETS = 3
    const val FREE_MAX_PEDALS = 1
    const val FREE_MAX_EFFECTS = 2
    // Amp і Cab повністю доступні у безкоштовній версії
}

// Перевірка через SettingsRepository.isPremium: StateFlow<Boolean>
// Спосіб розблокування: Google Play Billing one-time purchase
// productId = "gearboard_premium"
```

При спробі додати понад ліміт — показати BottomSheet з описом преміум версії
і кнопкою "Unlock Full Version — $4.99".

Google Play Billing інтеграція: підключи білінг бібліотеку але залиш
`billingClient.launchBillingFlow()` як TODO з коментарем — реальний productId
налаштовується після реєстрації в Google Play Console.

### Export/Import пресетів (Phase 7)

```kotlin
// Export: зберегти JSON файл через SAF
val exportLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.CreateDocument("application/json")
) { uri -> viewModel.exportPresets(uri) }

// Import: вибрати JSON файл
val importLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.OpenDocument()
) { uri -> viewModel.importPresets(uri) }

// JSON формат пресету:
data class PresetExport(
    val version: Int = 1,
    val appVersion: String,
    val presets: List<PresetData>
)
```

### Bottom Navigation (Phase 10)

```kotlin
// 5 пунктів — НЕ більше
val navItems = listOf(
    NavItem("connect", Icons.Default.Bluetooth, "Connect"),
    NavItem("board", Icons.Default.Tune, "Board"),
    NavItem("presets", Icons.Default.LibraryMusic, "Presets"),
    NavItem("midimap", Icons.Default.Cable, "MIDI Map"),
    NavItem("monitor", Icons.Default.Timeline, "Monitor")
)
// Settings — іконка в TopAppBar, НЕ в bottom nav
// Активний пункт — Accent color (0xFFE8A020)
```

---

## Що НЕ реалізовувати

- WiFi MIDI
- Cloud sync
- In-app реклама
- iOS/KMP код
- Магазин пресетів (тільки local export/import JSON)
- Аудіо обробка будь-якого виду
- Neural DSP специфічні пресети (залишити як TODO)
- Фотографії реальних педалей в ADD PEDAL діалозі — тільки іконки

---

## Перевірка перед стартом

Перед тим як писати перший рядок коду:
1. Прочитай `GEARBOARD_ANDROID_SPEC.md` повністю
2. Переглянь `frontend/src/` для розуміння структури даних
3. Підтвердь що зрозумів freemium модель і фазову структуру
4. Напиши короткий summary того що будеш робити в Phase 1

Починай тільки після мого підтвердження.