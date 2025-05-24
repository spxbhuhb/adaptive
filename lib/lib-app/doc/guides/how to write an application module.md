# How to write an application module

This guide defines the classes needed to implement an [application module](def://) named
`example` under the package `my.project`. It supports:

- [Adat class](def://) and enum class registration (e.g., `ExampleData`)
- Application-wide [resources](def://) (e.g., string stores)
- Server-side [services](def://) and [workers](def://)
- [Multi-pane workspace](def://) UI for the client side

These modules establish a reusable base for apps built using [Adaptive](def://).

# Steps

## 1. Shared Module Definition

**File**: `src/commonMain/kotlin/my/project/example/app/ExampleModule.kt`

### Purpose

Defines shared behavior for client and server, including wire-format and resource registration.

### Code



### Explanation



---

## 2. Server-Side Module Definition

**File**: `src/commonMain/kotlin/my/project/example/app/ExampleServerModule.kt`

### Purpose

Initializes server-only components like services and workers.

### Code


### Explanation


## 3. Workspace-Based UI Module

**File**: `src/commonMain/kotlin/my/project/example/app/ExampleWsModule.kt`

### Purpose

Initializes the UI fragments and workspace panes available in the client application.

### Code

```kotlin
package my.project.example.app

import my.project.example.ws.wsExampleSettings
import my.project.example.ws.wsExampleSettingsDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.workspace.Workspace

class ExampleWsModule<WT : Workspace> : ExampleModule<WT>() {

    // Define frontend fragment keys and singular workspace items here
    val EXAMPLE_SETTINGS_KEY : FragmentKey = "app:ws:admin:system:settings"
    val EXAMPLE_SETTINGS_ITEM by lazy { SingularWsItem(Strings.exampleSettings, EXAMPLE_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        // Register UI fragments
        add(EXAMPLE_SETTINGS_KEY, ::wsExampleSettings)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        // Register workspace pane/item definitions
        wsExampleSettingsDef(this@ExampleWsModule)
    }

}
```

### Explanation

This client-side module defines and registers UI fragments like workspace panes.
Without this, workspace tool icons won't show up and the user won't be able to load content
with types handled by these panes.

By convention workspace pane fragment names (`wsExampleSettings` in the code) start with
`ws` and workspace pane definition function names (`wsExampleSettingsDef` in the code) are the 
fragment name with `Def` added.

---

# Usage Example

## Server side (JVM)

```kotlin
import my.project.example.app.ExampleServerModule

fun main() {
    jvmServer {
        module { ExampleServerModule() }
    }
}
```

### What Happens:

- The module registers serialization formats (`wireFormatInit`)
- Shared string resources are loaded (`resourceInit`)
- Workers and services are created and started (`workspaceInit`)

## Client side (browser)

```kotlin
import my.project.example.app.ExampleClientModule

fun main() {
    wsBrowserClient {
        module { ExampleWsModule() }
    }
}
```

### What Happens:

- The module registers serialization formats (`wireFormatInit`)
- Shared string resources are loaded (`resourceInit`)
- UI fragments are prepared for runtime instantiation (`frontendAdapterInit`)
- The list of workspace tools/panes is initialized (`workspaceInit`)

# Notes

- Adat classes are data models used for serialization, UI generation and persistence in Adaptive apps.
- The `WT` generic parameter allows the module to be reused in different workspace contexts.
- You can override the base module methods in tests or special-case modules without needing to duplicate all logic.

# See also

- [What is an application](guide://)
