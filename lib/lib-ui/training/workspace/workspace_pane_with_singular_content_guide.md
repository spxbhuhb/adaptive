---
language: kotlin
tags: [ui, workspace, pane, content]
title: Workspace Pane with Singular Content
---

# Summary

This implementation adds a singular "Example Settings" pane to the `Example` workspace module.
It defines a view backend, a UI fragment stub, and configuration logic to register the pane
at application startup.

# Objective

Write code for a new workspace pane with the following parameters:

- name: Example Settings
- singular: yes
- package: fun.adaptive.example.ws
- module: fun.adaptive.example.app.ExampleWsModule
- icon: settings

# Steps

## 1. Create the view backend class

**File**: `src/commonMain/kotlin/fun/adaptive/example/ws/ExampleSettingsViewBackend.kt`

### Purpose

Provide data storage and background logic for workspace panes.

### Code

```kotlin
package `fun`.adaptive.example.ws

import `fun`.adaptive.example.app.ExampleWsModule
import `fun`.adaptive.ui.workspace.logic.WsSingularPaneController

class ExampleSettingsViewBackend(
    module: ExampleWsModule<*>
) : WsSingularPaneController(module.workspace, module.EXAMPLE_SETTINGS_ITEM) {
    // This is where you can put functions like save, fetch etc.
}
```

### Explanation

View backends provide data storage and background logic for UI fragments. In the
particular case of workspace panes, they typically extend one of the base pane classes from
`lib-ui`.

Here we define a singular pane backend which means that the content of the pane is the same
every time the pane is opened. An account or a global settings pane is a good example.

## 2. Create the UI fragment

**File**: `src/commonMain/kotlin/fun/adaptive/app/ws/admin/system/wsExampleSettings.kt`

### Purpose

Create a UI fragment that displays the content of the pane.

### Code

```kotlin
package `fun`.adaptive.example.ws

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.log.devInfo
import `fun`.adaptive.ui.workspace.model.WsPane

@Adaptive
fun wsExampleSettings(pane: WsPane<*, ExampleSettingsViewBackend>): AdaptiveFragment {
    devInfo { "displaying: ${pane.data} with backend ${pane.controller}" }
    return fragment()
}
```

### Explanation

All pane UI fragments should have one and only one parameter: the pane itself. In a
workspace-based application the user can open/hide, create/close panes freely. The
system has to be able to build a pane without knowing what data the pane processes.

The first type parameter of `WsPane` is the type of the workspace item the pane is 
displays, the second type parameter is the type of the view backend the pane has.

`pane.data` contains the data of the pane.
`pane.viewBackend` contains the view backend of the pane.

## 3. Write the pane definition function

**File**: `src/commonMain/kotlin/fun/adaptive/app/ws/admin/system/wsExampleSettingsDef.kt`

### Purpose

Register the pane in the workspace so the user can open it.

### Code

```kotlin
package `fun`.adaptive.example.ws

import `fun`.adaptive.example.app.ExampleWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.WsPane
import `fun`.adaptive.ui.workspace.model.WsPanePosition
import `fun`.adaptive.utility.UUID

fun Workspace.wsExampleSettingsDef(
    module: ExampleWsModule<*>
) {
    addContentPaneBuilder(module.EXAMPLE_SETTINGS_KEY) {
        WsPane(
            UUID(),
            workspace = this,
            Strings.settings,
            Graphics.settings,
            WsPanePosition.Center,
            module.EXAMPLE_SETTINGS_KEY,
            module.EXAMPLE_SETTINGS_ITEM,
            ExampleSettingsViewBackend(module)
        )
    }
}
```

## 4. Extend the module with pane registration

**File**: `src/commonMain/kotlin/fun/adaptive/example/app/ExampleWsModule.kt`

### Purpose

Add the pane to the application module.

### Code

```kotlin
package `fun`.adaptive.example.app

import `fun`.adaptive.example.ws.wsExampleSettings
import `fun`.adaptive.example.ws.wsExampleSettingsDef
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.FragmentKey
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.generated.resources.settings
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem

class ExampleWsModule<WT : Workspace> : ExampleModule<WT>() {

    val EXAMPLE_SETTINGS_KEY : FragmentKey = "app:ws:example:settings"
    val EXAMPLE_SETTINGS_ITEM by lazy { SingularWsItem(Strings.settings, EXAMPLE_SETTINGS_KEY, Graphics.settings) }

    override fun frontendAdapterInit(adapter: AdaptiveAdapter) = with(adapter.fragmentFactory) {
        add(EXAMPLE_SETTINGS_KEY, ::wsExampleSettings)
    }

    override fun workspaceInit(workspace: WT, session: Any?) = with(workspace) {
        wsExampleSettingsDef(this@ExampleWsModule)
    }

}
```

### Explanation

Workspace panes have to be registered, so the system knows that they exist.
This is typically done in a workspace module class by:

- adding the pane fragment to the fragment factory in `fronendAdapterInit`
- adding the pane definition to the workspace in `workspaceInit`

It is best practice to define the pane key in the module, and in the case of 
singular panes, the singular item the pane belongs to.

## See Also

[Application Module Directory Structure Guide](/lib/lib-app/training/application_module_directory_structure_guide.md)
[Application Module Definition Guide](/lib/lib-app/training/application_module_definition_guide.md)
