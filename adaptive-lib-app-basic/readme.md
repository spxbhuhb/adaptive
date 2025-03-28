# Lib App Basic

Basic application setups.

There are two UI setups for now:

- workspace based (main focus)
- sidebar based (lagging behind a bit, I don't really use it)

## Workspace based UI

This UI features an IntelliJ IDEA inspired workspace with tools at side/bottom and tabbed main content.

Focus is on browser for now. Theoretically can run on any platform, but a some platform dependent fragments
are not currently implemented on Android/iOS/Desktop, hence "theoretically".

Initialization involves an `index.html` (examples in site, iot, sandbox) and a `main.js`:

```kotlin
fun main() {
    WsBrowserApplication(
        UiClientApplicationData(),
        // ----  modules  --------
        LibUiModule(),
        GroveRuntimeModule<Workspace>(),
        ChartWsModule(),
        DocWsModule(),
        IotWsModule(),
        ZigbeeModule<Workspace>(),
        BasicAppWsModule(),
        IotAppWsModule()
    ).main()
}
```

The modules build up the application registries such as fragments, wire formats, workspace tools
etc.

By convention, module names ending with `WsModule` are workspace aware ones. They typically
register tools, content panes, item types for the workspace.

Modules with the `<WorkSpace>` type parameter are actually workspace independent, they can work with
any module context. 

Module functions are called in this order during application startup:

- `WireFormatRegistry.init()`
- `loadResources`
- `BackendAdapter.init()`
- `AdaptiveAdapter.init()`
- `Workspace.init()`

You can check any of the modules above to see how to initialize workspace elements. The code
is quite straightforward.

The backend and the frontend are initialized by actualizing fragments with
`BasicAppWsModule.BACKEND_MAIN_KEY` and `BasicAppWsModule.FRONTEND_MAIN_KEY`, respectively.

If you use the `BasicAppWsModule`, these keys point to fragments from `adaptive-lib-app-basic`,
which - hopefully - provide reasonable defaults.

- `wsAppBackendMain`
- `wsAppFrontendMain`

Both main fragments are initialized in a local context which provides the application instance,
`WsBrowserApplication` in the example.

You can easily access this context from any fragment. 

> [!NOTE]
> 
> Note that here we look for `UiClientApplication`. This is because `WsBrowserApplication` is platform 
> dependent while `UiClientApplication` is not. Hence, from common code you can access only `UiClientApplication`.

```kotlin
val app = fragment().firstContext<UiClientApplication<*,*>>()
```