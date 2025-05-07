---
language: kotlin
tags: [lib-app, application, module, ui, server, workspace]
title: Application Module Definition
---

# Summary

This guide defines the classes needed to implement an application module named
`example` under the package `fun.adaptive`. It supports:

- Adat class registration (e.g., `ExampleData`)
- Application-wide resources (e.g., string stores)
- Server-side services and workers
- Workspace-based UI for the client side

These modules establish a reusable base for apps built using the Adaptive platform.

---

# Objective

Define and implement the core module classes for:

- Shared functionality across client and server
- Server-only features (services, workers)
- Workspace-based client UI setup

---

# Steps

## 1. Shared Module Definition

**File**: `src/commonMain/kotlin/fun/adaptive/example/app/ExampleModule.kt`

### Purpose

Defines shared behavior for client and server, including wire-format and resource registration.

### Code

```kotlin
package `fun`.adaptive.example.app

import `fun`.adaptive.example.model.ExampleData
import `fun`.adaptive.example.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.runtime.AbstractWorkspace
import `fun`.adaptive.runtime.AppModule
import `fun`.adaptive.wireformat.WireFormatRegistry

open class ExampleModule<WT : AbstractWorkspace> : AppModule<WT>() {

    override fun wireFormatInit(registry: WireFormatRegistry) = with(registry) {
        // Register Adat classes used across the app
        + ExampleData
    }

    override fun resourceInit() {
        // Register shared resource bundles (e.g., strings)
        application.stringStores += commonMainStringsStringStore0
    }
}
```

### Explanation

This class sets up wire formats for Adat-based serialization and application resources. 
Both server and client modules extend it to ensure consistent behavior across environments.

`fun.adaptive.example.generated.resoures` is the package for generated resources. It can be set
in `build.gradle.kts`. If a `Gradle` module contains more than one application module, only one 
should add the resources as they are typically shared between all application modules in the
same `Gradle` module.

```kotlin
adaptive {
    resources {
        packageOfResources = "fun.adaptive.example.generated.resources"
    }
}
```

---

## 2. Server-Side Module Definition

**File**: `src/commonMain/kotlin/fun/adaptive/example/app/ExampleServerModule.kt`

### Purpose

Initializes server-only components like services and workers.

### Code

```kotlin
package `fun`.adaptive.example.app

import `fun`.adaptive.example.server.ExampleService
import `fun`.adaptive.example.server.ExampleWorker
import `fun`.adaptive.runtime.ServerWorkspace

class ExampleServerModule<WT : ServerWorkspace> : ExampleModule<WT>() {

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        // Register server-side services and background workers
        + ExampleService()
        + ExampleWorker()
    }

}
```

### Explanation

This class handles back-end-specific registration. It ensures that services (like REST endpoints) and workers (e.g., cron jobs) are initialized when the server starts.

---

## 3. Workspace-Based UI Module

**File**: `src/commonMain/kotlin/fun/adaptive/example/app/ExampleWsModule.kt`

### Purpose

Initializes the UI fragments and workspace panes available in the client application.

### Code

```kotlin
package `fun`.adaptive.example.app

import `fun`.adaptive.example.ws.wsExampleSettings
import `fun`.adaptive.example.ws.wsExampleSettingsDef
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
jvmServer {
    module { ExampleServerModule() }
}
```

### What Happens:

- The module registers serialization formats (`wireFormatInit`)
- Shared string resources are loaded (`resourceInit`)
- Workers and services are created and started (`workspaceInit`)

## Client side (browser)

```kotlin
wsBrowserClient {
    module { ExampleWsModule() }
}
```

### What Happens:

- The module registers serialization formats (`wireFormatInit`)
- Shared string resources are loaded (`resourceInit`)
- UI fragments are prepared for runtime instantiation (`frontendAdapterInit`)
- The list of workspace tools/panes is initialized (`workspaceInit`)

---

# Notes

- Adat classes are data models used for serialization, UI generation and persistence in Adaptive apps.
- The `WT` generic parameter allows the module to be reused in different workspace contexts.
- You can override the base module methods in tests or special-case modules without needing to duplicate all logic.

---

# See also

[Application Module Directory Structure Guide](application_module_directory_structure_guide.md)

---

# Conclusion

These module definitions encapsulate reusable logic and maintain clean separation between shared,
server-only, and client-only concerns. They serve as a foundation for building scalable application 
features in a modular Kotlin/Multiplatform Adaptive app.

