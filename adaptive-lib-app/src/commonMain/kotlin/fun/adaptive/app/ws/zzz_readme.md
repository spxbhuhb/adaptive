# WS Module: Main

Main module for Workspace based UI applications. Features an IntelliJ IDEA inspired workspace with tools 
at side/bottom and tabbed main content.

Focus is on browser for now. Theoretically can run on any platform, but a some platform dependent fragments
are not currently implemented on Android/iOS/Desktop, hence "theoretically".

Initialization involves an `index.html` (examples in site, iot, sandbox) and a `main.kt`. This example
shows content of `main.kt` for the IoT app.

```kotlin
fun main() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { AuthClientModule() }
        module { ValueClientModule() }
        module { ChartWsModule() }
        module { DocWsModule() }
        module { IotWsModule() }
        module { ZigbeeModule() }
        module { AppAdminWsModule() }
        module { AppInspectWsModule() }
        module { AppMainWsModule() }
        module { AppAuthWsModule() }
        module { IotAppWsModule() }
    }
}
```

The modules build up the application registries such as fragments, wire formats, workspace tools
etc.

By convention, module names ending with `WsModule` are workspace aware ones. They typically
register tools, content panes, item types for the workspace.

You can check any of the modules above to see how to initialize workspace elements. The code
is quite straightforward.

The backend and the frontend are initialized by actualizing fragments with
`AppMainWsModule.BACKEND_MAIN_KEY` and `AppMainWsModule.FRONTEND_MAIN_KEY`, respectively.

If you use the `AppMainWsModule`, these keys point to fragments from `adaptive-lib-app`,
which - hopefully - provide reasonable defaults.

- `wsAppBackendMain`
- `wsAppFrontendMain`

You can easily access the application from any fragment:

```kotlin
val app = fragment().wsApplication
```

> [!NOTE]
>
> Note that `wsApplication` returns with a `ClientApplication<Workspace>`. This is because
> `WsBrowserApplication` is platform dependent while `ClientApplication` is not. Hence, from
> common code you can access only `ClientApplication`.
>